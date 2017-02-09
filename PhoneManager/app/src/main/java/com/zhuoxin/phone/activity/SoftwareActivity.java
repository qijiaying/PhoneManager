package com.zhuoxin.phone.activity;

/**
 * Created by Administrator on 2016/11/14.
 */

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.MediaBrowserCompatUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.adapter.SoftwareAdapter;
import com.zhuoxin.phone.base.ActionBarActivity;
import com.zhuoxin.phone.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

import static com.zhuoxin.phone.activity.SoftManagerActivity.title;

public class SoftwareActivity extends ActionBarActivity {
    //ListView、List<AppInfo>、Adapter
    ListView ll_software;
    ProgressBar pb_softmgr_loading ;
    List<AppInfo> appInfoList = new ArrayList<AppInfo>();
    SoftwareAdapter adapter;
    String appType;
    //删除cb和btn
    CheckBox cb_deleteall;
    Button btn_delete;
    //广播接收者
    BroadcastReceiver receiver ;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //处理逻辑，根据不同的msg进行处理
            int what = msg.what ;
            switch (what){
                case 1 :
                    pb_softmgr_loading.setVisibility(View.GONE);
                    ll_software.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SoftwareActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
                    break;
                case 2 :
                    Toast.makeText(SoftwareActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;//如果不想让其他的handle处理，就穿true
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        initActionBar(true, title, false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        //动态创建receiver ,必须反注册
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //重新获取app信息并保存
                saveAppInfos();
                adapter.notifyDataSetChanged();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void initView() {
        appType = getIntent().getBundleExtra("bundle").getString("appType", "all");
        ll_software = (ListView) findViewById(R.id.ll_software);
        pb_softmgr_loading = (ProgressBar) findViewById(R.id.pb_softmgr_loading);
        cb_deleteall = (CheckBox) findViewById(R.id.cb_deleteall);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        //初始化Adapter并和ll(ListView)关联
        adapter = new SoftwareAdapter(appInfoList, this);
        ll_software.setAdapter(adapter);
        //获取手机中的数据，并存放至appInfiList中
        saveAppInfos();

        cb_deleteall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //通过for循环把数据的状态修改掉
                for (int i = 0; i < appInfoList.size(); i++) {
                    if (appType.equals("all")) {
                        if (!appInfoList.get(i).isSystem) {
                            appInfoList.get(i).isDelete = isChecked;
                        }
                    } else if (appType.equals("system")) {
                        appInfoList.get(i).isDelete = false;
                    } else {
                        appInfoList.get(i).isDelete = isChecked;
                    }
                }
                //把最新的数据给了adapter，并刷新界面
                adapter.notifyDataSetChanged();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //循环取出列表中的app，如果是isDelete,调用删除的方法，删除
                for(AppInfo info : appInfoList){
                    if(info.isDelete){
                        if(!info.packageName.equals(getPackageName())){
                            //调用删除的方法
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:"+info.packageName));
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    public void saveAppInfos() {
        pb_softmgr_loading.setVisibility(View.VISIBLE);
        ll_software.setVisibility(View.INVISIBLE);
        //因为访问数据（文件、网络）是耗时操作，开辟子线程操作，避免出现ANR现象
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfoList.clear();
                //获取几乎所有的安装包
                List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
                //x循环获取所有软件信息
                for (PackageInfo packageInfo : packageInfoList) {
                    //建立ApplicationInfo
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    //创建apptype
                    boolean apptype;
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        apptype = true;
                    } else {
                        apptype = false;
                    }
                    //创建appicon
                    Drawable appicon = getPackageManager().getApplicationIcon(applicationInfo);
                    //创建appname
                    String appname = (String) getPackageManager().getApplicationLabel(applicationInfo);
                    String packageName = packageInfo.packageName;
                    String appversion = packageInfo.versionName;
                    //判断当前页面要展示的数据，把数据库存到appInfoList
                    if (appType.equals("all")) {
                        AppInfo info = new AppInfo(appicon, appname, apptype, packageName, appversion, false);
                        appInfoList.add(info);
                    } else if (appType.equals("system")) {
                        if (apptype) {
                            AppInfo info = new AppInfo(appicon, appname, apptype, packageName, appversion, false);
                            appInfoList.add(info);
                        }
                    } else {
                        if (!apptype) {
                            AppInfo info = new AppInfo(appicon, appname, apptype, packageName, appversion, false);
                            appInfoList.add(info);
                        }
                    }
                }
                //1.runOnUIThread
               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });*/
                //2.Handler机制
                Message msg = handler.obtainMessage();
                msg.what = 1;//对msg设置标记
                handler.sendMessage(msg);
                //3.AsyncTask
            }
        }).start();

    }
}

