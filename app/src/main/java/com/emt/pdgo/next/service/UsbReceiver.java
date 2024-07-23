package com.emt.pdgo.next.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.ui.activity.USBDiskActivity;

import java.io.File;
import java.util.Arrays;

public class UsbReceiver extends BroadcastReceiver {

    public static String usbPath="";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //媒体安装 - 获取USB路径需在媒体安装完成后获取
        if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {//usb插入
            Log.e("UsbReceiver", "usb插入");
            if (intent.getData() != null && intent.getData().getPath() != null) {
                usbPath = intent.getData().getPath() ;
                Log.e("UsbReceiver", "usb插入=usbPath=="+usbPath);
                File file = new File(usbPath);
                String[] files = file.list();
                assert files != null;
                Log.e("UsbReceiver", "usb插入=files=="+ Arrays.toString(files));
                for (String s : files) {
                    if (s.startsWith(EmtConstant.sn_name) && s.endsWith(".apk")) {
//                        MyApplication.getInstance().installApk(usbPath+"/"+s);
                        Intent i = new Intent(MyApplication.mContext, USBDiskActivity.class);
                        i.putExtra("filePath",
                                usbPath+"/"+s
                        );
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        MyApplication.getInstance().usbActivityIsRunning = true;
                        MyApplication.mContext.startActivity(i);
                    }
                }
            }
        } else if (action.equals("android.intent.action.norco_disk_disable")) {
            Log.e("UsbReceiver", "usb插入disable");
        } else if (action.equals("android.intent.action.norco_disk_enable")) {
            Log.e("UsbReceiver", "usb插入:enable");
        }


    }

    private void checkUsbFileList(String usbPath) {
//        L.Companion.d("usb路径：" + usbPath);
        StringBuilder te= new StringBuilder();
        File file = new File(usbPath);
//        if (file.exists() && file.isDirectory()) {
            String[] files = file.list();
            assert files != null;
            for (String s : files) {
//                L.Companion.d("usb文件：" + files[i]);
                Log.e("===", "==USB文件=" + s);
                te.append(s);
            }
//        }
    }
}
