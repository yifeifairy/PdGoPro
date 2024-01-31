//package com.emt.pdgo.next.util;
//
//import android.os.CountDownTimer;
//import android.util.Log;
//
//import java.io.OutputStream;
//
//public class MonitorTouchCountDownTimer extends CountDownTimer {
//
//    public MonitorTouchCountDownTimer(long millisInFuture, long countDownInterval) {
//        super(millisInFuture, countDownInterval);
//    }
//
//    @Override
//    public void onTick(long l) {
//        Log.e("TouchCountDownTimer", ""+ l/1000+"s");
//    }
//
//    @Override
//    public void onFinish() {
//        try {
//            String cmd = "input keyevent " + 26  + "\n";
//            OutputStream os = Runtime.getRuntime().exec("su").getOutputStream();
//            os.write(cmd.getBytes());
//            os.flush();
////            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
