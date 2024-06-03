//package com.emt.pdgo.next.util;
//
//import android.os.SystemClock;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import tp.xmaihh.serialport.stick.AbsStickPackageHelper;
//
//public class MyBaseStickPackageHelper implements AbsStickPackageHelper {
//    public MyBaseStickPackageHelper() {
//    }
//
//    @Override
//    public byte[] execute(InputStream is) {
//        try {
//            int available = is.available();
//            if (available > 0) {
//                byte[] buffer = new byte[available];
//                int size = is.read(buffer);
//                if (size > 0) {
//                    return buffer;
//                }
//            } else {
//                SystemClock.sleep(500);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}