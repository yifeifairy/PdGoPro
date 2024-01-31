package com.emt.pdgo.next.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;


import java.io.File;
import java.text.DecimalFormat;


/**
 * Created by Administrator on 2016/10/12.
 */
public class MyUtil {

    private static final String TAG = "MyUtil";

    private static final int MIN_DELAY_TIME = 500;  // 两次点击间隔不能少于500ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * @param str      String类型的数字 如"150"
     * @param formatAs 格式化格式       ，如“00000”
     * @return 格式化后的字符串
     */
    public static String formatNumber(String str, String formatAs) {
        DecimalFormat df = new DecimalFormat(formatAs);
        String str2 = df.format(Double.valueOf(str));
        return str2;
    }

    /**
     * @param num      int数字 如150
     * @param formatAs 格式化格式 ，如“00000”
     * @return 格式化后的字符串
     */
    public static String formatNumber(int num, String formatAs) {
        DecimalFormat df = new DecimalFormat(formatAs);
        String str2 = df.format(num);
        return str2;
    }

    /**
     * dp 2 px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connect == null)
            return false;
        NetworkInfo netinfo = connect.getActiveNetworkInfo();
        if (netinfo == null)
            return false;
        if (netinfo.isConnected())
            return true;
        return false;
    }


    /**
     * 判断SD卡的状态
     *
     * @return 可读可写 为 true，可读 为 true，其他情况 为 false
     */
    public static boolean isSdReadable() {
        boolean mExternalStorageAvailable = false;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
            Log.i(TAG, "External storage card is readable.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Log.i(TAG, "External storage card is readable.");
            mExternalStorageAvailable = true;
        } else {
            // Something else is wrong. It may be one of many other
            // states, but all we need to know is we can neither read nor
            // write
            mExternalStorageAvailable = false;
        }
        return mExternalStorageAvailable;
    }

    /**
     * 判断是否存在SD卡
     *
     * @return
     */
    public static boolean existSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }


    /**
     * 判断SD卡剩余空间
     *
     * @return
     */
    public static long getSDFreeSize() {

        if (!existSDCard()) {
            Log.i(TAG, "External storage card is readable.");
            return 0;
        }

        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }


    /**
     * 获取SD卡总大小
     *
     * @return
     */
    public static long getSDAllSize() {

        if (!existSDCard()) {
            Log.i(TAG, "External storage card is readable.");
            return 0;
        }

        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; //单位MB
    }


    /**
     * 是否连接上Wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnetced(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static String[] getMyData(String tempData) {
        String[] mData;

        if (tempData.indexOf(":") != -1) {
            mData = tempData.replace(":", " ").split(" ");
        } else {
            mData = tempData.split(" ");
        }
        return mData;
    }

   


}
