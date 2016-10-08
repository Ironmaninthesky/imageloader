package com.example.daxiansheng.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.daxiansheng.imageloader.cache.DiskCache;
import com.example.daxiansheng.imageloader.cache.RamCache;
import com.example.daxiansheng.imageloader.http.HttpUtil;
import com.example.daxiansheng.imageloader.utils.ImageUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

/**
 * Created by DaXianSheng on 2016/10/8.
 */
public class ImageLoader {
    private static final int LOAD_IMAGE = 100;
    private static final String TAG = ImageLoader.class.getSimpleName();
    //制定缓存的最大数量
    private static BlockingQueue<Runnable> mBlockingQueue=new LinkedBlockingQueue<>(128);
    private static Context context;
    public static void init(Context app){
        context=app;
    }
    public static Context getContext(){
        return context;
    }

    private static ThreadPoolExecutor threadPoolExecutor=
            new ThreadPoolExecutor(5,9,10, TimeUnit.SECONDS,mBlockingQueue);
    //实例化磁盘缓存
    private static DiskCache mDiskCache=new DiskCache();

    public static void display(final ImageView container,final String url,
                              int defaultResId){
        final Bitmap bitmap= RamCache.getInstance().get(url);
        if (bitmap!=null) {
            //缓存中的图片直接显示
            container.setImageBitmap(bitmap);
            return;
        }
        if (defaultResId!=0) {
            container.setImageResource(defaultResId);
        }
        //设置标记
        container.setTag(url);
        //接收数据
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOAD_IMAGE:
                        if (container.getTag().equals(url)) {
                            container.setImageBitmap((Bitmap) msg.obj);
                        }
                        break;
                }
            }
        };
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //磁盘中查找
                Bitmap bitmapFromFile=mDiskCache.get(url);
                if (bitmapFromFile!=null) {
                    RamCache.getInstance().put(url,bitmapFromFile);
                    //显示图片
                    Message obtain = Message.obtain();
                    obtain.what=LOAD_IMAGE;
                    obtain.obj=bitmapFromFile;
                    handler.sendMessage(obtain);
                    return;
                }
                //从网络获取
                byte[] bytes= HttpUtil.getBytes(url);
                if (bytes==null) {
                    Log.e(TAG, "run: "+"网络获取异常"+url);
                    return;
                }

                Bitmap bitmap= ImageUtil.sampleBitmap(bytes);
                //添加到Ram缓存
                RamCache.getInstance().put(url,bitmap);
                //添加到磁盘缓存
                mDiskCache.put(url,bitmap);
                Message msg = Message.obtain();
                msg.what=LOAD_IMAGE;
                msg.obj=bitmap;
                handler.sendMessage(msg);
            }
        });
    }
    public static void display(final ImageView container,final String url){
        display(container,url,0);
    }
}
