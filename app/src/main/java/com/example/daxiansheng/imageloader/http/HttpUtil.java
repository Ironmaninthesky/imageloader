package com.example.daxiansheng.imageloader.http;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DaXianSheng on 2016/10/8.
 */
public class HttpUtil {
    private static final int FAIL = 100;
    private static final int SUCCESS = 200;
    private static OkHttpClient mClient=new OkHttpClient();

    public static byte[] getBytes(String url){
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().bytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(String url){
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void getStringAsync(final String url,final RequestCallBack callBack){
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        callBack.onFailure();
                        break;
                    case SUCCESS:
                        callBack.onSuccess((String) msg.obj);
                        break;
                }
                callBack.onFinish();
            }
        };
        Thread thread = new Thread(){
            @Override
            public void run() {
                String result = getString(url);
                if (result==null) {
                    handler.sendEmptyMessage(FAIL);
                    return;
                }
                Message msg = Message.obtain();
                msg.what=SUCCESS;
                msg.obj=result;
                handler.sendMessage(msg);
            }
        };

        thread.start();
    }

    public interface RequestCallBack{
        void onFailure();
        void onSuccess(String result);
        void onFinish();
    }
}
