package com.zhuoxin.phone.activity;

/**
 * Created by Administrator on 2016/11/14.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.BaseActivity;
import com.zhuoxin.phone.db.DBManager;

import java.io.File;

public class SplashActivity extends BaseActivity {
    ImageView iv_logo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        startAnimation();
        copyAssets();
    }
    private void startAnimation(){
        //动画效果+对动画执行过程的侦听
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            //动画开启时
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //动画结束时
            public void onAnimationEnd(Animation animation) {
                //Toast.makeText(SplashActivity.this, "动画结束，开始跳转主页面", Toast.LENGTH_SHORT).show();
                startActivity(PhoneActivity.class);
                finish();
            }

            //动画重复时
            public void onAnimationRepeat(Animation animation) {
            }
        };
        animation.setAnimationListener(animationListener);
        iv_logo.startAnimation(animation);
    }
    private void copyAssets() {
        //获取手机中的应用的存储位置
        File file = this.getFilesDir();
        File targetFile = new File(file, "commonnum.db");
        if (!DBManager.isExistsDB(targetFile)) {
            DBManager.copyAssetsFileToFile(this, "commonnum.db", targetFile);
        }
    }
}

