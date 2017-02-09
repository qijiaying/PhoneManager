package com.zhuoxin.phone.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.ActionBarActivity;
import com.zhuoxin.phone.view.ActionBarView;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {
    RelativeLayout rl_start;
    RelativeLayout rl_notifiaction;
    RelativeLayout rl_push;
    RelativeLayout rl_help;
    RelativeLayout rl_aboutus;
    ToggleButton tb_start;
    ToggleButton tb_notifiaction;
    ToggleButton tb_push;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActionBar(true, "系统设置", false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
        initData();
        mContext = this;
    }

    private void initView() {
        rl_start = (RelativeLayout) findViewById(R.id.rl_start);
        rl_notifiaction = (RelativeLayout) findViewById(R.id.rl_notifiaction);
        rl_push = (RelativeLayout) findViewById(R.id.rl_push);
        rl_help = (RelativeLayout) findViewById(R.id.rl_help);
        rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
        tb_start = (ToggleButton) findViewById(R.id.tb_start);
        tb_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //把状态保存到sp中
                getSharedPreferences("config", MODE_PRIVATE).edit().putBoolean("startWhenBootComplete", tb_start.isChecked()).commit();
            }
        });
        tb_notifiaction = (ToggleButton) findViewById(R.id.tb_notifiaction);
        tb_notifiaction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //状态选中时，显示通知栏消息；未选中时，清空通知栏消息
                if (isChecked) {
                    Intent intent = new Intent(SettingsActivity.this,HomeActivity.class);
                    PendingIntent pengingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new Notification.Builder(SettingsActivity.this)
                            .setContentTitle("虚假通知")
                            .setContentText("这是一条捏造的虚假消息")
                            .setSmallIcon(R.drawable.item_arrow_right)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.item_arrow_right))
                            .setAutoCancel(true)   //自动取消
                            .setContentIntent(pengingIntent)
                            .build();
                    NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(0, notification);
                }else{
                    NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(0);
                }
            }
        });

        tb_push = (ToggleButton) findViewById(R.id.tb_push);

        rl_start.setOnClickListener(this);
        rl_notifiaction.setOnClickListener(this);
        rl_push.setOnClickListener(this);
        rl_help.setOnClickListener(this);
        rl_aboutus.setOnClickListener(this);
    }

    private void initData() {
        boolean starWthenBootComplete = getSharedPreferences("config", MODE_PRIVATE).getBoolean("startWhenBootComplete", false);
        tb_start.setChecked(starWthenBootComplete);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rl_start:
                //开机启动按钮，去设置对应的togglebutton，并且把开机启动的状态进行保存
                //切换状态
                tb_start.setChecked(!tb_start.isChecked());
                break;
            case R.id.rl_notifiaction:
                //切换状态
                tb_notifiaction.setChecked(!tb_notifiaction.isChecked());
                break;
            case R.id.rl_push:
                Toast.makeText(this,"后续版本见",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_help:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromSettings",true);
                startActivity(GuideActivity.class,bundle);
                break;
            case R.id.rl_aboutus:
                startActivity(AboutActivity.class);
                break;
        }
    }
}
