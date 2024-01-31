//package com.emt.pdgo.next.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.bhm.sdk.rxlibrary.rxbus.RxBus;
//import com.bhm.sdk.rxlibrary.rxbus.Subscribe;
//import com.emt.pdgo.next.MyApplication;
//import com.emt.pdgo.next.common.config.CommandSendConfig;
//import com.emt.pdgo.next.common.config.RxBusCodeConfig;
//import com.emt.pdgo.next.data.CommandLogItem;
//import com.emt.pdgo.next.data.bean.DeviceStatusInfo;
//import com.emt.pdgo.next.util.StringUtils;
//import com.emt.pdgo.next.util.logger.Logger;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.security.InvalidParameterException;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Date;
//
//import android_serialport_api.SerialPort;
//import io.reactivex.annotations.Nullable;
//import io.reactivex.disposables.CompositeDisposable;
//
///**
// * @ProjectName:
// * @Package: com.emt.pdgo.next.service
// * @ClassName: MainBoardService
// * @Description: 主板通信服务
// * @Author: chenjh
// * @CreateDate: 2020/1/7 3:14 PM
// * @UpdateUser: 更新者
// * @UpdateDate: 2020/1/7 3:14 PM
// * @UpdateRemark: 更新内容
// * @Version: 1.0
// */
//public class MainBoardService_back extends Service {
//
//    private String TAG = "MainBoardService";
//
//    public static final String OpenSerialError = "com.emt.pdgo.next.service.MainBoardService.OpenSerialError";
//    public static final String ReadDataError = "com.emt.pdgo.next.service.MainBoardService.ReadDataError";
//    public static final String SendDataError = "com.emt.pdgo.next.service.MainBoardService.SendDataError";
//    public static final String ReadDataTimeOut = "com.emt.pdgo.next.service.MainBoardService.ReadDataTimeOut";
//    public static final String SerialClosed = "com.emt.pdgo.next.service.MainBoardService.SerialClosed";
//
//    public class MainBoardBind extends Binder {
//
//        public MainBoardService_back getService() {
//            return MainBoardService_back.this;
//        }
//
//    }
//
//    private OutputStream mOutputStream;
//    private InputStream mInputStream;
//    private SerialPort mSerialPort;
//
//    private String mSerialName = "/dev/ttyS2";//"/dev/ttyAMA2";
//    private int mSerialBaudrate = 115200;
//
//    private ReadThread mReadThread = null;
//
//    public volatile boolean isOpen = false;
//    public volatile boolean isReadData = false;
//
//    private long m_lastTime = System.currentTimeMillis();
//    private boolean m_sendCmd = false;
//
//
//    private CompositeDisposable compositeDisposable;
//
//    private DeviceStatusInfo mDeviceStatusInfo;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        RxBus.get().register(this);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    public static final int openError = 1;
//    public static final int sendError = 2;
//    public static final int readError = 3;
//    public static final int readDateEvent = 4;
//    public static final int openOk = 5;
//    public static final int readTimeOut = 6;
//    public static final int closed = 7;
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case openOk:
//                    MyApplication.isOpenMainSerial = true;
//                    RxBus.get().send(RxBusCodeConfig.EVENT_MAIN_BOARD_OK, "");
//                    break;
//                case openError: //控制板连接失败
//                    MyApplication.isOpenMainSerial = false;
//                    RxBus.get().send(RxBusCodeConfig.EVENT_MAIN_BOARD_ERROR, OpenSerialError);
//                    break;
//                case sendError: //主板串口发送数据失败
//                    RxBus.get().send(RxBusCodeConfig.EVENT_MAIN_BOARD_ERROR, SendDataError);
//                    break;
//                case readError: //主板串口读取数据失败
//                    RxBus.get().send(RxBusCodeConfig.EVENT_MAIN_BOARD_ERROR, ReadDataError);
//                    break;
//                case readTimeOut: //控制板故障，连接失败
//                    RxBus.get().send(RxBusCodeConfig.EVENT_MAIN_BOARD_ERROR, ReadDataTimeOut);
//                    break;
//                case closed: //主板串口已关闭
//                    RxBus.get().send(RxBusCodeConfig.EVENT_MAIN_BOARD_ERROR, SerialClosed);
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e(TAG, "MainBoardService  onStartCommand ");
//        if (!isOpen) {
//            m_isStop = false;
//            if (intent == null) {
//                Log.e(TAG, "MainBoardService  onStartCommand intent is null");
//                return super.onStartCommand(intent, flags, startId);
//            }
//            Log.e(TAG, "MainBoardService  onStartCommand mSerialName:" + mSerialName + "  mSerialBaudrate:"
//                    + mSerialBaudrate);
//            openSerial();
//        } else {
//            Log.e(TAG, "MainBoardService  onStartCommand has start");
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private boolean m_isStop = false;
//
//    @Override
//    public void onDestroy() {
//        m_isStop = true;
//        RxBus.get().unRegister(this);
//        compositeDisposable.clear();
//        super.onDestroy();
//        Log.e(TAG, "MainBoardService ----------->onDestroy");
//        try {
//            closeReadThread();
//            Log.e(TAG, "MainBoardService  onDestroy ");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "MainBoardService  onDestroy close serial  error: " + e.getMessage());
//        } finally {
//
//        }
//
//    }
//
//    private void closeReadThread() {
//
//        this.m_isStop = true;
//        int index = 0;
//        while (mReadThread != null && index < 3) {
//            index += 1;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        closeSerial();
//        if (mReadThread != null) {
//
//            try {
//                mReadThread.join();
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        mReadThread = null;
//
//        Log.e(TAG, "======closeSerial======6");
//        isOpen = false;
//        Log.e(TAG, "======closeSerial======7");
//    }
//
//    private void closeSerial() {
//        Log.e(TAG, "======closeSerial======2");
//        if (mOutputStream != null) {
//            try {
//                mOutputStream.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            mOutputStream = null;
//        }
//
//        Log.e(TAG, "======closeSerial======2");
//        if (mInputStream != null) {
//            try {
//                mInputStream.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            mInputStream = null;
//        }
//
//        Log.e(TAG, "======closeSerial====== 1");
//        if (mSerialPort != null) {
//            try {
//                mSerialPort.close();
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        mSerialPort = null;
//        mHandler.sendEmptyMessage(closed);
//    }
//
//    /**
//     * 打开串口
//     */
//    public void openSerial() {
//        Log.e(TAG, "openSerial");
//        try {
//            if (mSerialPort != null) {
//                closeReadThread();
//            }
//            mSerialPort = new SerialPort(new File(mSerialName), mSerialBaudrate, 0);
//            mOutputStream = mSerialPort.getOutputStream();
//            mInputStream = mSerialPort.getInputStream();
//            if (mInputStream != null && mOutputStream != null) {
//                Thread.sleep(1000);
//                this.m_isStop = false;
//                isReadData = false;
//                mReadThread = new ReadThread();
//                mReadThread.start();
//                isOpen = true;
//                mHandler.sendEmptyMessage(openOk);
//                Log.e(TAG, "打开串口成功");
//            } else {
//                isOpen = false;
//                Message mgs = new Message();
//                mgs.what = openError;
//                mgs.obj = "打开串口失败，打开输入输出失败";
//                Log.e(TAG, "打开串口失败，打开输入输出失败");
//                mHandler.sendMessage(mgs);
//            }
//        } catch (SecurityException e) {
//            e.printStackTrace();
//            Log.e(TAG, "SecurityException error；" + e.getMessage());
//            isOpen = false;
//            Message mgs = new Message();
//            mgs.what = openError;
//            mgs.obj = "打开串口失败，权限不够";
//            mHandler.sendMessage(mgs);
////            RxBus.getDefaultInstance().post(e);
//        } catch (IOException e) {
//            e.printStackTrace();
//            isOpen = false;
//            Log.e(TAG, "IOException error；" + e.getMessage());
//            Message mgs = new Message();
//            mgs.what = openError;
//            mgs.obj = "打开串口失败，IO发生异常";
//            mHandler.sendMessage(mgs);
////            RxBus.getDefaultInstance().post(e);
//
//        } catch (InvalidParameterException e) {
//            isOpen = false;
//            e.printStackTrace();
//            Log.e(TAG, "InvalidParameterException error；" + e.getMessage());
//            Message mgs = new Message();
//            mgs.what = openError;
//            mgs.obj = "打开串口失败，串口不可用";
//            mHandler.sendMessage(mgs);
////            RxBus.getDefaultInstance().post(e);
//        } catch (InterruptedException e) {
//            isOpen = false;
//            Log.e(TAG, "InterruptedException error；" + e.getMessage());
//            Message mgs = new Message();
//            mgs.what = openError;
//            mgs.obj = "打开串口失败，串口设置错误，不可用";
//            mHandler.sendMessage(mgs);
//
////            RxBus.getDefaultInstance().post(e);
//        }
//    }
//
//    /**
//     * 发送数据给下位机
//     *
//     * @param msg
//     * @return
//     * @throws IOException
//     */
//    public void sendData(String msg) {
//
//        try {
//            Log.e(TAG, "sendData（） 发送命令 1=======>" + msg);
//            if (msg == null) {
//                Logger.e("sendData() 发送命令  msg  is null");
//                return;
//            }
//            if (mOutputStream == null) {
//                Logger.e("sendData---> mOutputStream is null");
//                mHandler.sendEmptyMessage(sendError);
//                return;
//            }
////             Log.e(TAG,"sendData()  发送命令 2=====>" + msg);
////            msg = msg + "\r\n";
//            Logger.e("sendData() 发送命令 3======>" + msg);
////            msg = StringUtils.convertDecToHexString(msg);
//            mOutputStream.write(msg.getBytes());
//            mOutputStream.flush();
//            m_lastTime = System.currentTimeMillis();
//            m_sendCmd = true;
//            Logger.e("sendData() 发送命令完成 4 ======>" + msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//            mHandler.sendEmptyMessage(sendError);
//            this.m_isStop = true;
//            Logger.e("sendData---> mOutputStream error:" + e.getMessage());
//        }
//
//    }
//
//
//    private class ReadThread extends Thread {
//
//        private int maxLength = 4096;
//        private String data = "";
//
//        //        private BufferedReader mBufferedReader;
////        private InputStreamReader mInputStreamReader;
////        int MAX_BUFFER_BYTES = 2048;
////        String msg = "";
////        byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
//        @Override
//        public void run() {
//            super.run();
////             Log.e(TAG,"ReadThread is receive data...... ");
//
//            if (mInputStream != null) {
//
//                while (!m_isStop && !isInterrupted()) {
//                    try {
//
//                        isReadData = true;
//                        if (mInputStream.available() > 0) {
//
//                            byte[] buffer = new byte[1024];
//                            int size = mInputStream.read(buffer);
//                            byte[] readBytes = new byte[size];
//                            System.arraycopy(buffer, 0, readBytes, 0, size);
////                            Log.e(TAG, "字节数组为：" + Arrays.toString(readBytes));
////                            Log.e(TAG, "方法一：" + StringUtils.bytesToHexFun1(readBytes));
////                            Log.e(TAG, "方法二：" + StringUtils.bytesToHexFun2(readBytes));
////                            Log.e(TAG, "方法三：" + StringUtils.bytesToHexFun3(readBytes));
//
////                            Log.e(TAG, "解析返回的数据1：" + new String(StringUtils.toBytes(StringUtils.bytesToHexFun3(readBytes)), "utf-8"));
////                            Log.e(TAG, "解析返回的数据2：" + new String(readBytes, "utf-8"));
//
////                            byte[]  readBytes = StringUtils.readBytes3(mInputStream);
//                            receiveSerialData(readBytes);//处理数据
//                            if (readBytes.length > 0) {
//                                m_sendCmd = false;
//                                m_lastTime = System.currentTimeMillis();
//                            }
//
////                            if (data != null && !"".equals(data)) {
//////                                Log.e(TAG,"接收到主板数据 ASCII  转 String 1: "+ StringUtils.asciiToString(data));
////                                m_sendCmd = false;
////                                m_lastTime = System.currentTimeMillis();
////                                receiveData(data.getBytes("UTF-8"), data.getBytes("UTF-8").length);
////
////                            }
//                        } else {
//                            if (m_sendCmd) {
//                                long time = System.currentTimeMillis() - m_lastTime;
//                                if (time > 5000) {
//                                    m_sendCmd = false;
//                                    m_lastTime = System.currentTimeMillis();
//                                    mHandler.sendEmptyMessage(readTimeOut);
//                                }
//                            }
//                            Thread.sleep(20);
//
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        mHandler.sendEmptyMessage(readError);
//                        Logger.d("ReadThread receive  data IOException:" + e.getMessage());
//                        break;
//                    } catch (Exception e) {
//                        mHandler.sendEmptyMessage(readError);
//                        Logger.d("ReadThread receive  data Exception:" + e.getMessage());
//                        break;
//                    }
//                }
//            }
//            isReadData = false;
//            mReadThread = null;
////            close();
//            closeSerial();
//        }
//
//        public void close() {
//
//            try {
//                Log.e(TAG, "======closeSerial---------1");
//                if (mInputStream != null) {
//                    Log.e(TAG, "======closeSerial---------2");
//                    mInputStream.close();
//                    mInputStream = null;
//                }
//                Log.e(TAG, "======closeSerial---------3");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e(TAG, "======closeSerial-- IOException-1" + e.getMessage());
//            } finally {
//
//                try {
//                    Log.e(TAG, "======closeSerial---------6");
//                    if (mInputStream != null) {
//                        Log.e(TAG, "======closeSerial---------7");
//                        mInputStream.close();
//                        mInputStream = null;
//                    }
//                    Log.e(TAG, "======closeSerial---------8");
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "======closeSerial-- IOException-2" + e.getMessage());
//                }
//            }
//
//        }
//
//    }
//
//
//    @Subscribe(code = RxBusCodeConfig.EVENT_SEND_COMMAND)
//    public void sendCommand(String receiveData) {
//        if (!TextUtils.isEmpty(receiveData)) {
//            sendData(receiveData);
////            if (receiveData.contains(CommandSendConfig.GETSTATUS_ON)) {
////                MyApplication.sendSelfCheck = true;
////            }
//            if (MyApplication.DEBUG) {
////                CommandLogItem mCommandLogItem = getCommandLogItem("  上位机发出的指令: " + data);
////                MyApplication.mCommandLogDatas.add(mCommandLogItem);
//                mTime = getCommandLogItem();
//                CommandLogItem mCommandLogItem = new CommandLogItem(1, mTime, receiveData);
//                insertRecord(1, mTime, receiveData);
//                RxBus.get().send(RxBusCodeConfig.DEBUG_EVENT_COMMAND, mCommandLogItem);
//            }
//
//            Log.e(TAG, TAG + "  发送给下位机指令: " + receiveData);
//        }
//    }
//
//    private String mErrorInfo = "ef bf bd ef bf bd ef bf bd ef bf bd ef bf bd d1 b9 ef bf bd ef bf bd ef bf bd";
//
//    /*******
//     *
//     * 处理串口的数据
//     *
//     * @param readBytes
//     */
//    private void receiveSerialData(byte[] readBytes) {
//
//        try {
//            Log.e(TAG, "字节数组为：" + Arrays.toString(readBytes));
//            Log.e(TAG, "方法一：" + StringUtils.bytesToHexFun1(readBytes));
//            Log.e(TAG, "方法二：" + StringUtils.bytesToHexFun2(readBytes));
//            Log.e(TAG, "方法三：" + StringUtils.bytesToHexFun3(readBytes));
//
////            Byte[] rrr = ;
//        Log.e(TAG, "数组转为Hex：" + StringUtils.bytesToHexString2(readBytes));
////            Log.e(TAG, "解析返回的数据1：" + new String(StringUtils.toBytes(StringUtils.bytesToHexFun3(readBytes)), "utf-8"));
//            String mData = new String(readBytes, "utf-8");
//
//            Log.e(TAG, "解析返回的数据2：" + mData);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "  发送给下位机指令: " + e.getMessage());
//        }
//
//
//    }
//
//
//    private void insertRecord(int type, String time, String command) {
//
//
//    }
//
//    private SimpleDateFormat mTimeFormat;
//    private String mTime;
//
//    private String getCommandLogItem() {
//        if (mTimeFormat == null) {
//            mTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        }
//
//        long mtime = System.currentTimeMillis();
//
//        return mTimeFormat.format(new Date(mtime));
//    }
//
//}
//
