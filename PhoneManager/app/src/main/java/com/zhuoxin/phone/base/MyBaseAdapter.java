package com.zhuoxin.phone.base;

/**
 * Created by Administrator on 2016/11/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    List<T> dataList = new ArrayList<T>();
    public LayoutInflater inflater;
    public Context context;

    public MyBaseAdapter(List<T> dataList,Context context) {
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
        this.context =context ;
    }
    //添加数据
    public void setDataList(List<T> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
    }
    public List<T> getDataList(){
        return dataList;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);
}
