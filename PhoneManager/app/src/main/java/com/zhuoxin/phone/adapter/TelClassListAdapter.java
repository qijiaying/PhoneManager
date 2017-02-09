package com.zhuoxin.phone.adapter;

/**
 * Created by Administrator on 2016/11/14.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.MyBaseAdapter;
import com.zhuoxin.phone.entity.TelClassInfo;

import java.util.List;
public class TelClassListAdapter extends MyBaseAdapter<TelClassInfo> {
    public TelClassListAdapter(List<TelClassInfo> dataList, Context context) {
        super(dataList, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            //1.填充布局到convertView中
            convertView = inflater.inflate(R.layout.item_classlist, null);
            //2.找到holder对应的控件
            holder = new ViewHolder();
            holder.tv_telclassname = (TextView) convertView.findViewById(R.id.tv_telclassname);
            //3.把holder保存到convertView中
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取电话信息，并且设置到textview中
        TelClassInfo info = getItem(position);
        holder.tv_telclassname.setText(info.name);
        //把convertview返回
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_telclassname;
    }
}

