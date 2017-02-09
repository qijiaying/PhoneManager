package com.zhuoxin.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.MyBaseAdapter;
import com.zhuoxin.phone.entity.AppInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class SoftwareAdapter extends MyBaseAdapter<AppInfo> {
    public SoftwareAdapter(List<AppInfo> dataList, Context context) {
        super(dataList, context);
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_softwarelist, null);
            holder = new ViewHolder();
            holder.iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
            holder.tv_appname = (TextView) view.findViewById(R.id.tv_appname);
            holder.tv_packageName = (TextView) view.findViewById(R.id.tv_packageName);
            holder.tv_appversion = (TextView) view.findViewById(R.id.tv_appversion);
            holder.cb_appdelete = (CheckBox) view.findViewById(R.id.cb_delete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.cb_appdelete.setTag(i);
        holder.iv_appicon.setImageDrawable(getItem(i).appicon);
        holder.tv_appname.setText(getItem(i).appname);
        holder.tv_packageName.setText(getItem(i).packageName);
        holder.tv_appversion.setText(getItem(i).appversion);
        //如果是系统的app，禁用checkBox
       if(getItem(i).isSystem){
           holder.cb_appdelete.setClickable(false);
       }else {
           holder.cb_appdelete.setClickable(true);
       }
        holder.cb_appdelete.setChecked(getItem(i).isDelete);
        holder.cb_appdelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //ListView中图片错位、checkBox错位
                int index = (int) holder.cb_appdelete.getTag();
                getItem(index).isDelete = isChecked;
            }
        });
        return view;
    }

     static class ViewHolder {
        ImageView iv_appicon;
        TextView tv_appname;
        TextView tv_packageName;
        TextView tv_appversion;
        CheckBox cb_appdelete;
    }
}

