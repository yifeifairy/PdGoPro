package com.emt.pdgo.next.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.utils.ByteUtil;

public class MyService extends Service {

    private static SerialHelper serialHelper;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serialHelper = new SerialHelper("/dev/ttyS0", 115200) {
            @Override
            protected void onDataReceived(ComBean comBean) {
                String hex = ByteUtil.ByteArrToHex(comBean.bRec).toUpperCase(Locale.ROOT);
                Log.e("数据接收","hex--"+hex);
            }
        };
        try {
            serialHelper.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendHex(String hex) {
        Log.e("MyService","hex"+hex);
        if (serialHelper.isOpen()) {
            serialHelper.sendHex(hex);
        } else {
            Log.e("MyService","串口未打开");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}