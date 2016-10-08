package com.example.daxiansheng.imageloader.utils;

import android.content.Context;

import com.example.daxiansheng.imageloader.ImageLoader;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by DaXianSheng on 2016/10/8.
 */
public class SDCardUtil {
    private static final String TAG=SDCardUtil.class.getSimpleName();
    private static Context context= ImageLoader.getContext();
    //获取缓存的路径
    public static File getCacheDir(){
        if (context==null) {
            throw new NullPointerException("Imageloader还没有初始化");
        }
        return context.getCacheDir();
    }
    public static File urlToPath(String url){
        return new File(getCacheDir().getAbsolutePath()+File.separator+md5(url));
    }
    private static String subString(String url){
        int lastIndexOf=url.lastIndexOf("/");
        int lastIndexOfDot=url.lastIndexOf(".");
        return url.substring(lastIndexOf,lastIndexOfDot);
    }

    /**
     * 使用MD5来实现文件名的唯一性
     * @param url
     * @return
     */
    private static String md5(String url){
        try {
            MessageDigest md5=MessageDigest.getInstance("MD5");
            //将传进来的String变成byte数组进行摘要获取
            md5.update(url.getBytes());
            //将获取来的摘要信息获取出来，转换成Android中可以直接使用的类型
            byte[] digest = md5.digest();
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String toHex(byte[] data){
        String ret=null;
        if (data!=null) {
            StringBuilder sb = new StringBuilder();
            for (byte b :
                    data) {
                sb.append(String.format("%2x", b));
            }
            ret=sb.toString();
        }
        return ret;
    }
}
