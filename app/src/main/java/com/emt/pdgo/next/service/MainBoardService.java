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
//import androidx.annotation.Nullable;
//
//import com.bhm.sdk.rxlibrary.rxbus.RxBus;
//import com.bhm.sdk.rxlibrary.rxbus.Subscribe;
//import com.emt.pdgo.next.MyApplication;
//import com.emt.pdgo.next.common.config.CommandReceiveConfig;
//import com.emt.pdgo.next.common.config.CommandSendConfig;
//import com.emt.pdgo.next.common.config.RxBusCodeConfig;
//import com.emt.pdgo.next.data.CommandLogItem;
//import com.emt.pdgo.next.data.PdGoDbManager;
//import com.emt.pdgo.next.data.bean.DeviceStatusInfo;
//import com.emt.pdgo.next.util.MyUtil;
//import com.emt.pdgo.next.util.StringUtils;
//import com.emt.pdgo.next.util.logger.Logger;
//
//import org.reactivestreams.Subscriber;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidParameterException;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import android_serialport_api.SerialPort;
//import io.reactivex.Flowable;
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;
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
//public class MainBoardService extends Service {
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
//        public MainBoardService getService() {
//            return MainBoardService.this;
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
//        compositeDisposable = new CompositeDisposable();
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
////             Log.e(TAG,"sendData（） 发送命令 1=======>" + msg);
//            if (msg == null) {
//                Log.e(TAG, "sendData() 发送命令  msg  is null");
//                return;
//            }
//            if (mOutputStream == null) {
//                Log.e(TAG, "sendData---> mOutputStream is null");
//                mHandler.sendEmptyMessage(sendError);
//                return;
//            }
////             Log.e(TAG,"sendData()  发送命令 2=====>" + msg);
//            msg = msg + "\r\n";
////             Log.e(TAG,"sendData() 发送命令 3======>" + msg);
//            mOutputStream.write(msg.getBytes());
//            mOutputStream.flush();
//            m_lastTime = System.currentTimeMillis();
//            m_sendCmd = true;
////             Log.e(TAG,"sendData() 发送命令完成 4 ======>" + msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//            mHandler.sendEmptyMessage(sendError);
//            this.m_isStop = true;
//            Log.e(TAG, "sendData---> mOutputStream error:" + e.getMessage());
//        }
//
//    }
//
//
//    private class ReadThread extends Thread {
//
//        // private int maxLength = 1024;
//        private String data = "";
//        private BufferedReader mBufferedReader;
//        private InputStreamReader mInputStreamReader;
//
//        @Override
//        public void run() {
//            super.run();
////             Log.e(TAG,"ReadThread is receive data...... ");
//            try {
//
//
//                if (mInputStream != null) {
//                    mInputStreamReader = new InputStreamReader(mInputStream, "UTF-8");
//                    mBufferedReader = new BufferedReader(mInputStreamReader);
//
//                    while (!m_isStop && !isInterrupted()) {
//                        try {
//                            if (mBufferedReader == null) {
//                                Log.e(TAG, "mBufferedReader is null ");
//                                isReadData = false;
//                                break;
//                            }
//                            isReadData = true;
//                            byte[] buffer = new byte[1024];
//                            int size = mInputStream.read(buffer);
//
//                                if (size > 0) {
//                                    Log.e(TAG, new String(buffer, 0, size));
//                                    m_sendCmd = false;
//                                    m_lastTime = System.currentTimeMillis();
//                                    Thread.sleep(20);
//                                }
//
//
//                            if (mInputStream.available() > 0) {
//
////                                data = mBufferedReader.readLine();
////                                if (!TextUtils.isEmpty(data)) {
////                                    Log.e(TAG, "接收到串口数据 ---->" + data);
////                                    if (String.valueOf(data.charAt(0)).equals("{") && data.substring(data.length() - 1).equals("}")) {
////
////                                        Log.e(TAG, "{}");
////
////                                    }
////
////                                    if (String.valueOf(data.charAt(0)).equals("{")) {
////                                        Log.e(TAG, "{");
////                                    }
////
////                                    m_sendCmd = false;
////                                    m_lastTime = System.currentTimeMillis();
////                                    receiveData(data.getBytes("UTF-8"), data.getBytes("UTF-8").length);
////
////                                }
//                            } else {
//                                if (m_sendCmd) {
//                                    long time = System.currentTimeMillis() - m_lastTime;
//                                    if (time > 5000) {
//                                        m_sendCmd = false;
//                                        m_lastTime = System.currentTimeMillis();
//                                        mHandler.sendEmptyMessage(readTimeOut);
//                                    }
//                                }
//                                Thread.sleep(20);
//
//                            }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            mHandler.sendEmptyMessage(readError);
//                            Logger.d("ReadThread receive  data IOException:" + e.getMessage());
//                            break;
//                        } catch (Exception e) {
//                            mHandler.sendEmptyMessage(readError);
//                            Logger.d("ReadThread receive  data Exception:" + e.getMessage());
//                            break;
//                        }
//                    }
//                }
//                isReadData = false;
//                mReadThread = null;
//                close();
//                closeSerial();
//            } catch (Exception e) {
//
//            }
//        }
//
//        public void close() {
//
//            try {
//                Log.e(TAG, "======closeSerial---------1");
//                if (mBufferedReader != null) {
//                    Log.e(TAG, "======closeSerial---------2");
//                    mBufferedReader.close();
//                    mBufferedReader = null;
//                }
//                Log.e(TAG, "======closeSerial---------3");
//                if (mInputStreamReader != null) {
//                    Log.e(TAG, "======closeSerial---------4");
//                    mInputStreamReader.close();
//                    mInputStreamReader = null;
//                }
//                Log.e(TAG, "======closeSerial---------5");
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e(TAG, "======closeSerial-- IOException-1" + e.getMessage());
//            } finally {
//
//                try {
//                    Log.e(TAG, "======closeSerial---------6");
//                    if (mBufferedReader != null) {
//                        Log.e(TAG, "======closeSerial---------7");
//                        mBufferedReader.close();
//                        mBufferedReader = null;
//                    }
//                    Log.e(TAG, "======closeSerial---------8");
//                    if (mInputStreamReader != null) {
//                        Log.e(TAG, "======closeSerial---------9");
//                        mInputStreamReader.close();
//                        mInputStreamReader = null;
//                    }
//                    Log.e(TAG, "======closeSerial---------10");
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
//
//    private String mErrorInfo = "ef bf bd ef bf bd ef bf bd ef bf bd ef bf bd d1 b9 ef bf bd ef bf bd ef bf bd";
//
//    /*******
//     * 发送数据到Ui
//     *
//     * @param buffer
//     * @param size
//     */
//    private void receiveData(byte[] buffer, int size) {
//
//        byte[] temp = new byte[size];
//        for (int i = 0; i < size; i++) {
//            temp[i] = buffer[i];
//        }
//
//        /**
//         * 53 65 72 69 61 6c 52 65 63 65 45 72 72 6f 72 0d 0a ca e4 c8 eb b5 e7
//         * d1 b9 d2 e7 b3 f6 0d 0a
//         */
//        String tempString = StringUtils.bytesToHexString(temp);
//        if (mErrorInfo.equals(tempString)) {
//            Log.e(TAG, "接收到主板数据 异常数据---->" + tempString);
//            return;
//        }
////         Log.e(TAG," 接收到主板数据====>" + tempString);
//
//        String receiveData = null;
//        try {
//            receiveData = new String(temp, 0, size, "UTF-8").toString();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
////        Log.e("receiveData", " 接收到主板数据:" + receiveData);
//
//
//        if (receiveData.contains(CommandReceiveConfig.COMMAND_CHECK_SYSSTARUS)) {//获取设备状态
////             Log.e(TAG,TAG + "  获取设备状态: " + receiveData);
//            String[] datas = receiveData.split(" ");
//            if (datas.length >= 11) {
//
//                if (mDeviceStatusInfo == null) {
//                    mDeviceStatusInfo = new DeviceStatusInfo();
//                }
//                //负压阀
//                if (String.valueOf(datas[1]).equals(CommandReceiveConfig.VALVE_NEG_ON)) {
////                     Log.e(TAG,TAG + "  负压阀: 打开" );
//                    mDeviceStatusInfo.isNegON = true;
//                } else if (String.valueOf(datas[1]).equals(CommandReceiveConfig.VALVE_NEG_OFF)) {
//                    mDeviceStatusInfo.isNegON = false;
////                     Log.e(TAG,TAG + "  负压阀:关闭 " );
//                }
//                //补液阀
//                if (String.valueOf(datas[2]).equals(CommandReceiveConfig.VALVE_SUPPLY_ON)) {
//                    mDeviceStatusInfo.isSupplyON = true;
//                } else if (String.valueOf(datas[2]).equals(CommandReceiveConfig.VALVE_SUPPLY_OFF)) {
//                    mDeviceStatusInfo.isSupplyON = false;
//                }
//                //灌注阀
//                if (String.valueOf(datas[3]).equals(CommandReceiveConfig.VALVE_PERFUSION_ON)) {
//                    mDeviceStatusInfo.isPerfusionON = true;
//                } else if (String.valueOf(datas[3]).equals(CommandReceiveConfig.VALVE_PERFUSION_OFF)) {
//                    mDeviceStatusInfo.isPerfusionON = false;
//                }
//                //引流阀
//                if (String.valueOf(datas[4]).equals(CommandReceiveConfig.VALVE_DRAIN_ON)) {
//                    mDeviceStatusInfo.isDrainON = true;
//                } else if (String.valueOf(datas[4]).equals(CommandReceiveConfig.VALVE_DRAIN_OFF)) {
//                    mDeviceStatusInfo.isDrainON = false;
//                }
//                //安全阀
//                if (String.valueOf(datas[5]).equals(CommandReceiveConfig.VALVE_SAFE_ON)) {
//                    mDeviceStatusInfo.isSafeON = true;
//                } else if (String.valueOf(datas[5]).equals(CommandReceiveConfig.VALVE_SAFE_OFF)) {
//                    mDeviceStatusInfo.isSafeON = false;
//                }
//
//                mDeviceStatusInfo.upWeightInitialValue = Integer.valueOf(datas[6]);
//                mDeviceStatusInfo.lowWeightInitialValue = Integer.valueOf(datas[7]);
//
//                float mTemperature = Float.valueOf(datas[8]);
//                mDeviceStatusInfo.dialysateTemperature = mTemperature > 10000 ? 0f : (Float.valueOf(datas[8]) / 10f);
//                DecimalFormat df = new DecimalFormat("#.0");
//                String strTemperature = df.format(mDeviceStatusInfo.dialysateTemperature);
//                mDeviceStatusInfo.dialysateTemperature = Float.valueOf(strTemperature);
//
//                mDeviceStatusInfo.upWeight = Integer.valueOf(datas[9]);
//                mDeviceStatusInfo.lowWeight = Integer.valueOf(datas[10]);
//
//                RxBus.get().send(RxBusCodeConfig.DEBUG_EVENT_RECEIVE_DEVICE_STATUS, receiveData);//发送debug界面展示
//
//                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_DEVICE_STATUS, mDeviceStatusInfo);//
//            }
//
//        } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_CURRENT_VOLUME)) {
//            String[] curr_data = MyUtil.getMyData(receiveData);
//            if (curr_data.length == 2) {
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_CURRENT_VOLUME, curr_data[1]);//引流返回: 实时引流量
//            }
//        } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_PERFUSION_CURRENT_VOLUME)) {
//            String[] curr_data = MyUtil.getMyData(receiveData);
//            if (curr_data.length == 2) {
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_CURRENT_VOLUME, curr_data[1]);//灌注返回 : CurrentPerfusion [实时灌注量]
//            }
//        } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_WAITING_CURRENT_VOLUME)) {//返回:  RealTimeData [实时留腹量]
//            if (MyApplication.isStartTreatment) {//开始治疗时候才处理实时留腹量
//                String[] curr_data = MyUtil.getMyData(receiveData);
//                if (curr_data.length == 2) {
//                    RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_WAITING_CURRENT_VOLUME, curr_data[1]);//返回:  RealTimeData: [实时留腹量]
//                }
//            }
//        } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_FLUSH_REFRESH)) {//引流返回: 当前冲洗量、冲洗次数
//            RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_FLUSH_REFRESH, receiveData);
//        } else {
//
//            if (receiveData.contains(CommandReceiveConfig.COMMAND_CHECK_HELLO)) {
//                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_COMMAND_HELLO, receiveData);// 1、握手 Hello
//            } else if (receiveData.contains(CommandReceiveConfig.COMMAND_CHECK_WEIGHCOEFF)) {
//                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_COMMAND_WEIGH_COEFF, "");//设置秤的系数
//            } else if (receiveData.contains(CommandReceiveConfig.SYSSELF_VALUE) || receiveData.contains(CommandReceiveConfig.SYSSELF_WEIGHT_H)
//                    || receiveData.contains(CommandReceiveConfig.SYSSELF_WEIGHT_L) || receiveData.contains(CommandReceiveConfig.SYSSELF_SENSOR)) {
//                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_SYSSELF_DATA, receiveData);//自检返回
//            } else if (receiveData.contains(CommandReceiveConfig.PREHEAT_NO_HEATINGTARGET) || receiveData.contains(CommandReceiveConfig.PREHEAT_HEATINGTARGET)
//                    || receiveData.contains(CommandReceiveConfig.PREHEAT_COMPLETE)) {
//                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_PREHEAT_DATA, receiveData);//预热返回
//            } else if (receiveData.contains(CommandReceiveConfig.FIRSTRINSE_COMPLETE) || receiveData.contains(CommandReceiveConfig.FIRSTRINSE_FAULT2)
//                    || receiveData.contains(CommandReceiveConfig.FIRSTRINSE_FAULT3) || receiveData.contains(CommandReceiveConfig.FIRSTRINSE_FAULT4)) {//管路预冲
//                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_FIRSTRINSE_DATA, receiveData);
////            } else if (receiveData.contains(CommandSendConfig.WEIGH_PEELED_UPPER)) {
////
////            } else if (receiveData.contains(CommandSendConfig.WEIGH_PEELED_LOWER)) {
////
////            } else if (receiveData.contains(CommandSendConfig.WEIGH_PEELED_UPPER_AND_LOWER)) {
//
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_START_NTPD)
//                    || receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_START_TPD)) {//引流返回: 开始引流(非TPD模式或者TPD模式)
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_COMPLETE_NTPD)
//                    || receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_COMPLETE_TPD)) {//引流返回: 完成治疗引流(非TPD模式或者TPD模式)
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_FAULT_NTPD)
//                    || receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_FAULT_TPD)) {//引流返回: 引流故障(非TPD模式或者TPD模式)
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_PERFUSION_FAULT_NTPD)
//                    || receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_PERFUSION_FAULT_TPD)) {//引流返回: 引流冲洗故障(非TPD模式或者TPD模式)
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_EMPTY_WAITING_START_NTPD)
//                    || receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_EMPTY_WAITING_START_TPD)) {//引流返回: 开始排空等待(非TPD模式或者TPD模式)
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_NEGDRAIN_START)) {//引流返回: 负压引流任务创建时提示
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_NEGDRAIN_END)) {//引流返回: 负压引流结束
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_FAULT_WAIT_USER_ENSURE)) {//引流返回: 停止治疗的二次确认提示
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
////            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_FLUSH_REFRESH)) {//引流返回: 当前冲洗量、冲洗次数
////                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
////                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_FLUSH_REFRESH, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DRAIN_REFRESH_TARGET)) {//引流返回: 更新引流目标
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_WAITING_PERIOD)) {//留腹返回: 留腹等待 WaitingPeriod [留腹时间]
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_WAITING_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_WAITING_TIME)) {//留腹返回: 实际留腹的时间 WaitingTime [实际留腹的时间秒]
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_WAITING_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_SUPPLY_START)) {//补液返回: 补液开始
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_SUPPLY_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_SUPPLY_COMPLETE)) {//补液返回: 补液完成
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_SUPPLY_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_SUPPLY_STOP)) {//补液返回: 停止补液 TreatmentSupplyStop  [当前上位称重量]
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_SUPPLY_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_SUPPLY_FAULT)) {//补液返回: 补液故障 TreatmentSupplyfault [当前上位称重量]
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_SUPPLY_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_SUPPLY_FAULT_WAIT_USER_ENSURE)) {//补液返回: 停止补液治疗的二次确认提示
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_SUPPLY_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_SUPPLY_FAULT_COMPLETE_SUCCESS)) {//补液返回: 跳过补液成功
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_SUPPLY_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_PERFUSION_CURRENT_CYCLE_START)) {//灌注返回: 开始灌注 CurrentCycleStart [周期] [灌注量] [单位]
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_PERFUSION_CURRENT_CYCLE_END)) {//灌注返回: 周期灌注完成 CurrentCycleEnd [实际灌注量] g [灌注时间] [灌注后的留腹量] [周期]
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_PERFUSION_FAULT)) {//灌注返回: 周期灌注故障 TreatmentPerfusionfault [实际灌注量] g
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_PERFUSION_FAULT_WAIT_USER_ENSURE)) {//灌注返回: 停止周期灌注的二次确认提示
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_DATALIST_CURRENT_PERIOD)) {//治疗返回:当前周期完成（灌注->留腹->引流 完成） DataList:Period [周期] [灌注量] [辅助冲洗量] [灌注后留腹量] [引流目标量] [引流量] [预估腹腔剩余液体量]
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_OTHER_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_CYCLE_STOP)) {//治疗返回:治疗程序已终止
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_OTHER_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_COMPLETE)) {//治疗返回:治疗完成 TreatmentComplete [总共使用液体重量] g [超滤] g
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_OTHER_DATA, receiveData);
//            } else if (receiveData.contains(CommandReceiveConfig.TREATMENT_FORCED_END)) {//治疗返回:强制结束治疗: 停止治疗的二次确认提示
//                RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_OTHER_DATA, receiveData);
//            } else {
//                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_COMMAND, receiveData);
//            }
//
//            if (MyApplication.DEBUG) {
////                CommandLogItem mCommandLogItem = getCommandLogItem("  接收的下位机数据: " + receiveData);
////                MyApplication.mCommandLogDatas.add(mCommandLogItem);
//                mTime = getCommandLogItem();
//                CommandLogItem mCommandLogItem = new CommandLogItem(2, mTime, receiveData);
//                RxBus.get().send(RxBusCodeConfig.DEBUG_EVENT_COMMAND, mCommandLogItem);
//                insertRecord(2, mTime, receiveData);
//            }
//            Log.e("receiveData", " 接收到主板数据:" + receiveData);
//
//
//        }
//    }
//
//
//    private void insertRecord(int type, String time, String command) {
//        Flowable flowable = PdGoDbManager.getInstance().insertNewCommandRecord(type, time, command);
//        Disposable subscription = flowable.subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread())
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        Log.e("insertRecord", " 保存指令成功");
//                    }
//                });
//
//        compositeDisposable.add(subscription);
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