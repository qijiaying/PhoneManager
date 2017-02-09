package com.zhuoxin.phone.activity;

/**
 * Created by Administrator on 2016/11/14.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.ActionBarActivity;
import com.zhuoxin.phone.biz.MemoryManager;
import com.zhuoxin.phone.db.DBManager;
import com.zhuoxin.phone.view.CleanCircleView;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {
    TextView tv_telmgr;
    TextView tv_softmgr;
    TextView tv_rocket;
    TextView tv_filemgr ;

    CleanCircleView ccv_home;
    ImageView iv_home;
    TextView tv_home;
    TextView tv_phonemgr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initView();
        initActionBar(false, "手机管家", true, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_home.setText((int) (MemoryManager.usedPercent(this)) + "%");
        int targetAngle = (int) (3.6 * MemoryManager.usedPercent(this));
        ccv_home.setTargetAngle(targetAngle);
    }

    public void initView() {
        tv_telmgr = (TextView) findViewById(R.id.tv_telmgr);
        tv_telmgr.setOnClickListener(this);
        tv_softmgr = (TextView) findViewById(R.id.tv_softmgr);
        tv_softmgr.setOnClickListener(this);
        tv_rocket = (TextView) findViewById(R.id.tv_rocket);
        tv_rocket.setOnClickListener(this);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);
        tv_home.setText((int) (MemoryManager.usedPercent(this)) + "%");
        tv_phonemgr = (TextView) findViewById(R.id.tv_phonemgr);
        tv_phonemgr.setOnClickListener(this);
        tv_filemgr = (TextView) findViewById(R.id.tv_filemgr);
        tv_filemgr.setOnClickListener(this);

        ccv_home = (CleanCircleView) findViewById(R.id.ccv_home);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_home.setOnClickListener(this);

    }
    @OnClick(R.id.tv_sdclean)
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_menu:
                startActivity(SettingsActivity.class);
                break;
            case R.id.tv_telmgr:
                startActivity(PhoneActivity.class);
                break;
            case R.id.tv_softmgr:
                startActivity(SoftManagerActivity.class);
                break;
            case R.id.tv_rocket:
                startActivity(RocketActivity.class);
                break;
            case R.id.tv_phonemgr:
                startActivity(PhoneStateActivity.class);
                break;
            case R.id.tv_filemgr:
                startActivity(FileManagerActivity.class);
                break;
            case R.id.tv_sdclean:
                File file = new File(this.getFilesDir(),"clearpath.db");//data/data/com.zhuoxin.phone/files/clearpath.db
                if(!DBManager.isExistsDB(file)){
                    DBManager.copyAssetsFileToFile(this,"clearpath.db",file);
                }
                startActivity(CleanActivity.class);
                break;
            case R.id.iv_home:
            case R.id.tv_home:
                if (!ccv_home.isRunning) {
                    MemoryManager.killRunning(this);
                    ccv_home.isRunning = true;
                    tv_home.setText((int) (MemoryManager.usedPercent(this)) + "%");
                    int targetAngle = (int) (3.6 * MemoryManager.usedPercent(this));
                    ccv_home.setTargetAngle(targetAngle);
                }
                break;
        }
    }
}

