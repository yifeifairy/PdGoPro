package com.emt.pdgo.next.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.util.NetworkUtils;

public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RxBus.get().register(this);

//        NetworkUtils.getNetWorkState(context);
        Log.e("netReceiver","net:"+NetworkUtils.getNetWorkState(context));
//        Log.e("NetBroadcastReceiver","net--  "+NetworkUtils.getNetWorkState(context));
        RxBus.get().send(RxBusCodeConfig.NET_STATUS, NetworkUtils.getNetWorkState(context) + "");
//————————————————
//        版权声明：本文为CSDN博主「智玲君」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/gpf1320253667/article/details/84553900
//        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            try {
//                //获取所有网络连接的信息
//                Network[] networks = connMgr.getAllNetworks();
//                //用于存放网络连接信息
//                StringBuilder sb = new StringBuilder();
//                //通过循环将网络信息逐个取出来
//                for (Network network : networks) {
//                    //获取ConnectivityManager对象对应的NetworkInfo对象
//                    NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
//                    sb.append(networkInfo.getTypeName()).append(" connect is ").append(networkInfo.isConnected());
//                }
//                Log.e("wifiIv", "" + sb.toString());
//                Log.e("wifiIv", "wifiIv");
//                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
//                if (activeNetworkInfo != null) {
//                    if (activeNetworkInfo.isConnected()) {
//                        if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
////                        wifiIv.setImageResource(R.drawable.wifi);
//                        } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
////                        netIv.setImageResource(R.drawable.net);
//                        }
//                    }
//                    Log.e("wifiIv", "wifiIv" + activeNetworkInfo.getType());
//                } else {
////                wifiIv.setImageResource(R.drawable.no_wifi);
////                netIv.setImageResource(R.drawable.no_net);
//                }
////            assert activeNetworkInfo != null;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            //获取ConnectivityManager对象对应的NetworkInfo对象
//            //获取WIFI连接的信息
//            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            //获取移动数据连接的信息
//            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
////            if (wifiNetworkInfo.isConnected()) {
////                wifiIv.setImageResource(R.drawable.wifi);
////                Log.e("wifiIv", "" + wifiNetworkInfo.isConnected());
////            } else {
////                wifiIv.setImageResource(R.drawable.no_wifi);
////                Log.e("wifiIv", "" + wifiNetworkInfo.isConnected());
////            }
////            if (dataNetworkInfo.isConnected()) {
////                netIv.setImageResource(R.drawable.net);
////                Log.e("netIv", "" + dataNetworkInfo.isConnected());
////            } else {
////                netIv.setImageResource(R.drawable.no_net);
////                Log.e("netIv", "" + dataNetworkInfo.isConnected());
////            }
//            Log.e("netIv", "netIv" + dataNetworkInfo.isConnected());
//            //wifi开关变化
////                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
////                switch (state) {
////                    case WifiManager.WIFI_STATE_DISABLED: {
////                        //wifi关闭
////                        Log.e("=====", "已经关闭");
//////                        wifiIv.setImageResource(R.drawable.no_wifi);
////                        break;
////                    }
////                    case WifiManager.WIFI_STATE_DISABLING: {
////                        //wifi正在关闭
////                        Log.e("=====", "正在关闭");
////                        break;
////                    }
////                    case WifiManager.WIFI_STATE_ENABLED: {
////                        //wifi已经打开
////                        Log.e("=====", "已经打开");
//////                        wifiIv.setImageResource(R.drawable.no_conn_wifi);
////                        break;
////                    }
////                    case WifiManager.WIFI_STATE_ENABLING: {
////                        //wifi正在打开
////                        Log.e("=====", "正在打开");
////                        break;
////                    }
////                    case WifiManager.WIFI_STATE_UNKNOWN: {
////                        //未知
////                        Log.e("=====", "未知状态");
////                        break;
////                    }
////                }
////            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
////                //监听wifi连接状态
////                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
////                Log.e("=====", "--NetworkInfo--" + info.toString());
////                if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
////                    Log.e("=====", "wifi没连接上");
////                } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
////                    Log.e("=====", "wifi已连接");
////                    wifiIv.setImageResource(R.drawable.wifi);
////                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
////                    Log.e("=====", "wifi正在连接");
////                }
////            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
////                //监听wifi列表变化
////                Log.e("=====", "wifi列表发生变化");
////            }
////        }
//        }
    }

}