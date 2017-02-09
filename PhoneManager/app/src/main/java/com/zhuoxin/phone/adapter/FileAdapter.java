package com.zhuoxin.phone.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.zhuoxin.phone.R;
import com.zhuoxin.phone.base.MyBaseAdapter;
import com.zhuoxin.phone.entity.FileInfo;
import com.zhuoxin.phone.utils.FileTypeUtil;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class FileAdapter extends MyBaseAdapter<FileInfo> {
    public FileAdapter(List<FileInfo> dataList, Context context) {
        super(dataList, context);
    }
    public boolean isScroll = false;
    //创建软引用的键值对
    //HashMap<String,SoftReference<Bitmap>> bitmapSoftMap = new HashMap<String,SoftReference<Bitmap>>();
    //创建LruCache
    int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
    private final int LRUSIZE = maxMemory /16 ;
    LruCache<String, Bitmap> bitmapLruCache = new LruCache<String, Bitmap>(LRUSIZE) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getHeight() * value.getRowBytes();
        }
    };

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_file, null);
            holder = new ViewHolder();
            holder.cb_file = (CheckBox) view.findViewById(R.id.cb_file);
            holder.iv_fileicon = (ImageView) view.findViewById(R.id.iv_fileicon);
            holder.tv_filename = (TextView) view.findViewById(R.id.tv_filename);
            holder.tv_filetype = (TextView) view.findViewById(R.id.tv_filetype);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //设置布局中的数据
        holder.cb_file.setTag(i);
        holder.cb_file.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //取出当前cb的位置
                int index = (int) holder.cb_file.getTag();
                //修改对应位置的数据
                getItem(index).setSelect(isChecked);
            }
        });
        holder.cb_file.setChecked(getItem(i).isSelect());
        //图片,传统方式R.drawable.icon_image不可拆分和判断,建议用位图来操作
        Bitmap bitmap = null;
       if(! isScroll){
           bitmap = bitmapLruCache.get(getItem(i).getFile().getName());
           if(bitmap == null){
               //重系统中重新加载图片
               bitmap = getBitmap(getItem(i));
               //放入Lru
               bitmapLruCache.put(getItem(i).getFile().getName(),bitmap);
           }
           holder.iv_fileicon.setImageBitmap(bitmap);//添加获取位图的方法，用来根据不同的文件获取不同的位图
       }else {
           holder.iv_fileicon.setImageResource(R.drawable.item_arrow_right);
       }

        holder.tv_filename.setText(getItem(i).getFile().getName());
        holder.tv_filetype.setText(getItem(i).getFileType());
        return view;
    }

    static class ViewHolder {
        CheckBox cb_file;
        ImageView iv_fileicon;
        TextView tv_filename;
        TextView tv_filetype;
    }

    private Bitmap getBitmap(FileInfo fileInfo) {
        //定义位图
        Bitmap bitmap = null;
        //判断类型,image和非image
        //在decode图片资源之前，先获取和设置图片的缩放率
        //1.new一个options
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_IMAGE)) {
            //2.把图片参数取出，放到options中
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileInfo.getFile().getAbsolutePath(), options);
            //3.计算缩放率并设置
            int scaleUnit = 60;
            int scale = (options.outHeight > options.outWidth ? options.outHeight : options.outWidth) / scaleUnit;
            options.inSampleSize = scale;
            //4.根据设置好的options进行图片加载
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(fileInfo.getFile().getAbsolutePath(), options);
            return bitmap;
        } else {
            //其他情况,取drawable目录就可以了
            //通过GetIdentifier来把fileType转换成资源文件下对应的R.id的形式
            int icon = context.getResources().getIdentifier(fileInfo.getIconName(), "drawable", context.getPackageName());
            //如果icon <=0  证明 数据没取出来，给一个默认值
            if (icon <= 0) {
                icon = R.drawable.item_arrow_right;
            }
            //把R.icon的资源转换成bitmap
            //2.把图片参数取出，放到options中
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), icon, options);
            //3.计算缩放率并设置
            int scaleUnit = 60;
            int scale = (options.outHeight > options.outWidth ? options.outHeight : options.outWidth) / scaleUnit;
            options.inSampleSize = scale;
            //4.根据设置好的options进行图片加载
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeResource(context.getResources(), icon, options);
            return bitmap;
        }
    }

    private Cursor getCursorResult(String path) {
        //通过内容提供者查询结果
        Cursor cursorResult = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //取出结果，把结果对应的cursor返回
        while (cursorResult.moveToNext()) {
            String cursorPath = cursorResult.getString(cursorResult.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            //如果路径和传递进来的路径相等，直接打断
            if (cursorPath.equals(path)) {
                break;
            }
        }
        return cursorResult;
    }

    private String getAlbumArt(Cursor cursorResult) {
        int album_id = cursorResult.getInt(cursorResult.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cursor = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            album_art = cursor.getString(0);
        }
        cursor.close();
        cursor = null;
        return album_art;
    }

    private Bitmap getMp3Bitmap(String album_art) {
        Bitmap bm = null;
        if (album_art == null) {
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_audio);
        } else {
            bm = BitmapFactory.decodeFile(album_art);
        }
        return bm;
    }
}
