package com.zhuoxin.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.MyBaseAdapter;
import com.zhuoxin.phone.entity.TelNumberInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class TelNumberAdapter extends MyBaseAdapter<TelNumberInfo> {
    public TelNumberAdapter(List<TelNumberInfo> dataList, Context context) {
        super(dataList, context);
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_numberlist, null);
            holder = new ViewHolder();
            holder.tv_telnumbername = (TextView) view.findViewById(R.id.tv_telnumbername);
            holder.tv_telnumber = (TextView) view.findViewById(R.id.tv_telnumber);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        TelNumberInfo info = getItem(i);
        holder.tv_telnumbername.setText(info.name);
        holder.tv_telnumber.setText(info.number);
        return view;
    }

    private static class ViewHolder {
        TextView tv_telnumbername;
        TextView tv_telnumber;
    }
}

