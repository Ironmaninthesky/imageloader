package com.example.daxiansheng.imageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by DaXianSheng on 2016/10/8.
 */
public class ImageUtil {
    public static Bitmap sampleBitmap(byte[] bytes,int expectWidth,int expectHeight){
        //图片加载的配置选项
        BitmapFactory.Options options=new BitmapFactory.Options();
        //只读取图片的边缘，二次采样
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        //获取图片的宽高
        int outWidth=options.outWidth;
        int outheight=options.outHeight;
        //压缩
        int sampleMax=Math.max(outWidth/expectWidth,outheight/expectHeight);
        options.inSampleSize=sampleMax;
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
    }
    public static Bitmap sampleBitmap(byte[] bytes){
        return sampleBitmap(bytes,200,200);
    }
}
