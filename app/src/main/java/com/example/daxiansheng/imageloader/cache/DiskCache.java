package com.example.daxiansheng.imageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.daxiansheng.imageloader.utils.SDCardUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by DaXianSheng on 2016/10/8.
 */
public class DiskCache {
    private static final String TAG=DiskCache.class.getSimpleName();

    /**
     * 向sdcard缓存图片
     * png缓存的图片基本属于无损
     * jpeg缓存的图片可以进行质量的压缩
     * @param url
     * @param bitmap
     */
    public void put(String url,Bitmap bitmap){
        FileOutputStream stream=null;
        try {
            stream=new FileOutputStream(SDCardUtil.urlToPath(url));
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "put: "+ "文件缓存异常");

        }
    }

    /**
     * 从sdcard中获取图片
     * @param url
     * @return
     */
    public Bitmap get(String url){
        //先判断是否存在
        File file=new File(SDCardUtil.urlToPath(url).getAbsolutePath());
        if (!file.exists()) {
            return null;
        }
        Bitmap bitmap= BitmapFactory.decodeFile(SDCardUtil.urlToPath(url).getAbsolutePath());
        return bitmap;
    }
}
