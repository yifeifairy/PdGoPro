package com.emt.pdgo.next.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yujing.yserialport.DataListener;
import com.yujing.yserialport.YSerialPort;


public class MyService extends Service {

    private static YSerialPort serialHelper;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serialHelper = new YSerialPort(this.getApplication(),"/dev/ttyS0", "115200");
        serialHelper.addDataListener(new DataListener() {
            @Override
            public void value(String hexString, byte[] bytes) {
//                String hex = ByteUtil.ByteArrToHex(comBean.bRec).toUpperCase(Locale.ROOT);
//                Log.e("数据接收","hex--"+hex);
            }
        });
//        {
//            @Override
//            protected void onDataReceived(ComBean comBean) {
//                String hex = ByteUtil.ByteArrToHex(comBean.bRec).toUpperCase(Locale.ROOT);
//                Log.e("数据接收","hex--"+hex);
//            }
//        };
//        try {
//            serialHelper.open();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    public static void sendHex(String hex) {
//        Log.e("MyService","hex"+hex);
//        if (serialHelper.isOpen()) {
//            serialHelper.sendHex(hex);
//        } else {
//            Log.e("MyService","串口未打开");
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}