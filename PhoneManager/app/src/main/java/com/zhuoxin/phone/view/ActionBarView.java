package com.zhuoxin.phone.view;

/**
 * Created by Administrator on 2016/11/14.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuoxin.phone.R;

/**
 * Created by Administrator on 2016/11/10.
 */

public class ActionBarView extends LinearLayout {
    ImageView iv_back ;
    ImageView iv_menu;
    TextView tv_title ;
    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //需要先把actionbar的布局引入过来inflate
        inflate(context,R.layout.layout_actionbar,this);
        this.setBackgroundResource(R.drawable.shape_gray);
        this.setElevation(29);
        //找到对应的view
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }
    //添加一个初始化ActionBar的方法
    public void initAactionBar(boolean hasBack ,String title ,boolean hasMenu,OnClickListener listener){
        if(hasBack){
            iv_back.setOnClickListener(listener);
        }else{
            iv_back.setVisibility(View.INVISIBLE);
        }
        tv_title.setText(title);
        if(hasMenu){
            iv_menu.setOnClickListener(listener);
        }else{
            iv_menu.setVisibility(View.INVISIBLE);
        }
    }
}
