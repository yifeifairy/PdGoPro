package com.emt.pdgo.next.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class TestUtil {
//    public class MediaReceiver extends BroadcastReceiver {
//        private final static String U_DISK_FILE_NAME = "test.apk";
//        @Overridepublic void onReceive(Context context, Intent intent)
//        {
//            switch (intent.getAction())
//            {case Intent.ACTION_MEDIA_CHECKING:break;
//                case Intent.ACTION_MEDIA_MOUNTED:// 获取挂载路径,
//                    // 读取U盘文件
//                     Uri uri = intent.getData();if (uri != null) {
//                         String filePath = uri.getPath();
//                }
//                    File rootFile = new File(filePath);
//                     for (File file : rootFile.listFiles())
//                     {// 文件列表，进行相应操作...
//                          if(file.getName().equals(U_DISK_FILE_NAME)){
//                              Intent install = new Intent(Intent.ACTION_VIEW);
//                              //设置intent的数据类型是应用程序
//                               applicationinstall.setDataAndType(Uri.parse("file://" + file.toString()),"application/vnd.android.package-archive");
//                               //为这个新apk开启一个新的activity栈
//                               install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                               context.startActivity(install);break;}
//                     }
//                    break;
//                case Intent.ACTION_MEDIA_EJECT:break;
//                case Intent.ACTION_MEDIA_UNMOUNTED:break;}}
//            }
//        }
}
