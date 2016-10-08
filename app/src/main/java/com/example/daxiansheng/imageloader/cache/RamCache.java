package com.example.daxiansheng.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by DaXianSheng on 2016/10/8.
 */
public class RamCache {
    private static RamCache mRamCache;
    private LruCache<String,Bitmap> mLruCache;

    /**
     * 双重判断实现单例 解决线程不安全问题，又节约内存
     * @return
     */
    public static RamCache getInstance(){
        if (mRamCache==null) {
            synchronized (RamCache.class){
                if (mRamCache==null) {
                    mRamCache=new RamCache();
                }
            }
        }
        return mRamCache;
    }
    private RamCache(){
        long maxMemory = Runtime.getRuntime().maxMemory();
        //获取最大内存的1/8
        int expand= (int) (maxMemory/8);
        //LruCache的最大占用空间
        mLruCache=new LruCache<String, Bitmap>(expand){
            //计算bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getHeight()*value.getRowBytes();
            }
        };
    }

    /**
     * 向lruCache中存入bitmap
     * @param url
     * @param bitmap
     */
    public void put(String url,Bitmap bitmap){
        mLruCache.put(url,bitmap);
    }

    /**
     * 从LruCache中取出bitmap
     * @param url
     * @return
     */
    public Bitmap get(String url){
        return mLruCache.get(url);
    }
}
