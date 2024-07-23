package com.emt.pdgo.next.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandReceiveConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.ReceivePublicDataBean;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.FaultCodeEntity;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.PowerMonitorDialogActivity;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.ToastUtils;
import com.emt.pdgo.next.util.helper.HexHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.emt.pdgo.next.util.logger.Logger;
import com.google.gson.Gson;
import com.yujing.serialport.SerialPortFinder;
import com.yujing.yserialport.DataListener;
import com.yujing.yserialport.YSerialPort;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class SerialPortService extends Service {

    private static final String TAG = "SerialPortService";

//    private SerialHelper serialPortPlus;

    private YSerialPort serialPortPlus;

    private final String mSerialName = "/dev/ttyS1";//"/dev/ttyAMA2";
    private final int mSerialBaudrate = 115200;

    public volatile boolean isOpen = false;
    public volatile boolean isReadData = false;

    public Gson myGson = new Gson();
    public String splitData = "";

    @Subscribe(code = RxBusCodeConfig.EVENT_SEND_COMMAND)
    public void sendCommand(String sendData) {
        if (!TextUtils.isEmpty(sendData) && serialPortPlus != null) {
            try {
//                if (!sendData.contains("report")) {
                    if (EmtDataBase.getInstance(MyApplication.getInstance()).getFaultCodeDao().getCodeList().size() >= 1000) {
                        new Thread(() -> EmtDataBase
                        .getInstance(MyApplication.getInstance())
                        .getFaultCodeDao()
                        .delete()).start();
                    }

                    if (MyApplication.treatmentRunning) {
                        if (!sendData.contains("report")) {
                            FaultCodeEntity entity = new FaultCodeEntity();
                            entity.time = EmtTimeUil.getTime();
                            entity.code = "发送:"+sendData;
                            new Thread(() ->
                                    EmtDataBase.getInstance(MyApplication.getInstance()).getFaultCodeDao()
                                            .insertFaultCode(entity)).start();
                        }
                    } else {
                        FaultCodeEntity entity = new FaultCodeEntity();
                        entity.time = EmtTimeUil.getTime();
                        entity.code = "发送:" + sendData;
                        new Thread(() ->
                                EmtDataBase.getInstance(MyApplication.getInstance()).getFaultCodeDao()
                                        .insertFaultCode(entity)).start();
                    }
//                }
                Log.e("portService","发送:"+sendData);
                serialPortPlus.send(sendData.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("sendData---> mOutputStream error:" + e.getMessage());
            }
        }

    }

    @Override
    public void onCreate() {
        RxBus.get().register(this);
        if (!MyApplication.DEBUG) {
            openSerialPort();
        }
//        reentrantLock = new ReentrantLock(true);
        Log.e("串口","串口信息--"+ Arrays.toString(new SerialPortFinder().getAllDevices())
        +"--串口信息--"+ Arrays.toString(new SerialPortFinder().getAllDevicesPath()));
    }

    /**
     * 打开串口
     */
    private void openSerialPort() {
        try {
//            serialPortPlus = new SerialPortPlus(mSerialName, mSerialBaudrate);

//            if (Build.VERSION.SDK_INT < 21) {
//                serialPortPlus = new SerialHelper(mSerialName, mSerialBaudrate) {
//                    @Override
//                    protected void onDataReceived(ComBean comBean) {
//                        String hex = ByteUtil.ByteArrToHex(comBean.bRec).toUpperCase(Locale.ROOT);
////        Log.e("hex","hex数据--" + hex);
//                        try {
//                            String mSerialJson = new String(HexHelper.hexStringToBytes(hex), StandardCharsets.UTF_8);
////                            Log.e(TAG, "下位机原始数据:" + mSerialJson);
//                            if (mSerialJson.startsWith("{\"") && mSerialJson.endsWith("\"}")) {//有用{}说明是完整的json字符串
//                                sendSerialData(mSerialJson);
//                            } else {
//                                if (TextUtils.isEmpty(splitData)) {
//                                    splitData = mSerialJson;
////                    Log.e(TAG, "收到不完整数据,需要下次接收的数据拼接 ：" + splitData);
//                                } else {
//                                    String tempData = splitData + mSerialJson;
//                                    if (tempData.startsWith("{\"") && tempData.endsWith("\"}")) {//有用{"   "}说明是完整的json字符串
////                        Log.e(TAG, "不完整数据拼接成正常数据 ：" + tempData);
//                                        splitData = "";
//                                        sendSerialData(tempData);
//                                    } else {
////                        Log.e(TAG, "收到不完整数据2 1：" + splitData);
////                                        splitData = tempData;
////                        Log.e(TAG, "收到数据以后拼接还是不完整数据 ：" + splitData);
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            String mSerialJson = new String(HexHelper.hexStringToBytes(hex), StandardCharsets.UTF_8);
//                            Log.e(TAG, "收到解析异常数据 ：" + mSerialJson);
//                        }
//                    }
//                };
//            } else {
                // /dev/ttyS1
//                serialPortPlus = new SerialHelper("/dev/ttyS1", mSerialBaudrate) {
//                    @Override
//                    protected void onDataReceived(ComBean comBean) {
//
//                        String hex = ByteUtil.ByteArrToHex(comBean.bRec).toUpperCase(Locale.ROOT);
////        Log.e("hex","hex数据--" + hex);
//                        try {
//                            String mSerialJson = new String(HexHelper.hexStringToBytes(hex), StandardCharsets.UTF_8);
////                            Log.e(TAG, "下位机原始数据:" + mSerialJson);
//                            if (mSerialJson.startsWith("{\"") && mSerialJson.endsWith("\"}")) {//有用{}说明是完整的json字符串
//                                sendSerialData(mSerialJson);
//                            } else {
//                                if (TextUtils.isEmpty(splitData)) {
//                                    splitData = mSerialJson;
////                    Log.e(TAG, "收到不完整数据,需要下次接收的数据拼接 ：" + splitData);
//                                } else {
//                                    String tempData = splitData + mSerialJson;
//                                    if (tempData.startsWith("{\"") && tempData.endsWith("\"}")) {//有用{"   "}说明是完整的json字符串
////                        Log.e(TAG, "不完整数据拼接成正常数据 ：" + tempData);
//                                        splitData = "";
//                                        sendSerialData(tempData);
//                                    } else {
////                        Log.e(TAG, "收到不完整数据2 1：" + splitData);
////                                        splitData = tempData;
////                        Log.e(TAG, "收到数据以后拼接还是不完整数据 ：" + splitData);
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            String mSerialJson = new String(HexHelper.hexStringToBytes(hex), StandardCharsets.UTF_8);
//                            Log.e(TAG, "收到解析异常数据 ：" + mSerialJson);
//                        }
//                    }
//                };
//            }
            serialPortPlus = new YSerialPort(this.getApplication(),mSerialName,"115200");
            serialPortPlus.addDataListener(new DataListener() {
                @Override
                public void value(String hexString, byte[] bytes) {
                        try {
                            String mSerialJson = new String(HexHelper.hexStringToBytes(hexString), StandardCharsets.UTF_8);
//                            Log.e(TAG, "下位机原始数据:" + mSerialJson);
                            if (mSerialJson.startsWith("{\"") && mSerialJson.endsWith("\"}")) {//有用{}说明是完整的json字符串
                                sendSerialData(mSerialJson);
                            } else {
                                if (TextUtils.isEmpty(splitData)) {
                                    splitData = mSerialJson;
//                    Log.e(TAG, "收到不完整数据,需要下次接收的数据拼接 ：" + splitData);
                                } else {
                                    String tempData = splitData + mSerialJson;
                                    if (tempData.startsWith("{\"") && tempData.endsWith("\"}")) {//有用{"   "}说明是完整的json字符串
//                        Log.e(TAG, "不完整数据拼接成正常数据 ：" + tempData);
                                        splitData = "";
                                        sendSerialData(tempData);
                                    } else {
//                        Log.e(TAG, "收到不完整数据2 1：" + splitData);
//                                        splitData = tempData;
//                        Log.e(TAG, "收到数据以后拼接还是不完整数据 ：" + splitData);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String mSerialJson = new String(HexHelper.hexStringToBytes(hexString), StandardCharsets.UTF_8);
                            Log.e(TAG, "收到解析异常数据 ：" + mSerialJson);
                        }
                    }
                });

            serialPortPlus.setToAuto(5);
            serialPortPlus.start();
//            serialPortPlus.setReceiveDataListener(this);
//            Thread.sleep(1000);
//            if (serialPortPlus.getInputStream() != null && serialPortPlus.getOutputStream() != null) {
//            serialPortPlus.setStickPackageHelper(new MyBaseStickPackageHelper());
//            serialPortPlus.open();
//            if (serialPortPlus.) {
//                Log.e(TAG, "打开串口成功");
//                isOpen = true;
//                MyApplication.isOpenMainSerial = true;
//                RxBus.get().send(RxBusCodeConfig.EVENT_MAIN_BOARD_OK, "");
//            } else {
//                Log.e(TAG, "打开串口失败");
//                isOpen = false;
//                MyApplication.isOpenMainSerial = false;
//            }
            RxBus.get().send(RxBusCodeConfig.EVENT_MAIN_BOARD_OK, "");
        } catch (Exception e) {
            isOpen = false;
            MyApplication.isOpenMainSerial = false;
            e.printStackTrace();
            ToastUtils.showToast(this, "打开串口失败");
        }
    }

//    @Override
//    public void receiveData(ByteBuf byteBuf) {
//        String hex = ByteBufUtil.hexDump(byteBuf).toUpperCase();
////        Log.e("hex","hex数据--" + hex);
//        try {
//            String mSerialJson = new String(HexHelper.hexStringToBytes(hex), StandardCharsets.UTF_8);
//            if (mSerialJson.startsWith("{\"") && mSerialJson.endsWith("\"}")) {//有用{}说明是完整的json字符串
//                sendSerialData(mSerialJson);
//            } else {
//                if (TextUtils.isEmpty(splitData)) {
//                    splitData = mSerialJson;
////                    Log.e(TAG, "收到不完整数据,需要下次接收的数据拼接 ：" + splitData);
//                } else {
//                    String tempData = splitData + mSerialJson;
//                    if (tempData.startsWith("{\"") && tempData.endsWith("\"}")) {//有用{"   "}说明是完整的json字符串
////                        Log.e(TAG, "不完整数据拼接成正常数据 ：" + tempData);
//                        splitData = "";
//                        sendSerialData(tempData);
//                    } else {
////                        Log.e(TAG, "收到不完整数据2 1：" + splitData);
//                        splitData = tempData;
////                        Log.e(TAG, "收到数据以后拼接还是不完整数据 ：" + splitData);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            String mSerialJson = new String(HexHelper.hexStringToBytes(hex), StandardCharsets.UTF_8);
//            Log.e(TAG, "收到解析异常数据 ：" + mSerialJson);
//        }
//
//    }

    /**
     * 处理完整的下位机数据
     *
     * @param mSerialJson s
     */
    private void sendSerialData(String mSerialJson) {
        String key = "\"}{\"";
        if (mSerialJson.contains(key)) {//处理多条指令
            String[] mSerialJsons = mSerialJson.split("\"\\}\\{\"");//分割}{
            for (String tempSerialJson : mSerialJsons) {
                if (tempSerialJson.startsWith("{\"")) {
                    handleSerialData(tempSerialJson + "\"}");//最后一位补}
                } else if (tempSerialJson.endsWith("\"}")) {
                    handleSerialData("{\"" + tempSerialJson);//第一位补{
                }
                else if (tempSerialJson.startsWith("publish") && tempSerialJson.contains("dpr")){ // 专门为DPR治疗修改
//                    if (tempSerialJson.endsWith("}")) {
//                        Log.e(TAG, "专门为DPR治疗修改前: " + tempSerialJson );
//                        String newJson = tempSerialJson.substring(0,tempSerialJson.length()-1);
//                        tempSerialJson = "{\"" + newJson + "\"}";
//                        handleSerialData(tempSerialJson);
//                        Log.e(TAG, "专门为DPR治疗修改后: " + tempSerialJson );
//                    } else {
                        Log.e(TAG, "专门为DPR治疗修改前: " + tempSerialJson);
                        tempSerialJson = "{\"" + tempSerialJson + "\"}";
                        handleSerialData(tempSerialJson);
                        Log.e(TAG, "专门为DPR治疗修改后: " + tempSerialJson);
//                    }
                }
                else {
                    Log.e(TAG, "tempSerialJson: " + tempSerialJson + "}");
//                    handleSerialData(mSerialJson);
                }
            }
        } else {//处理单条指令 "}{"
            handleSerialData(mSerialJson);
        }
    }


    private void handleSerialData(String mSerialJson) {
//        Log.e(TAG, "接收->下位机数据:" + mSerialJson);

//        }
        try {
            if (EmtDataBase.getInstance(MyApplication.getInstance()).getFaultCodeDao().getCodeList().size() >= 1000) {
                new Thread(() -> EmtDataBase
                        .getInstance(MyApplication.getInstance())
                        .getFaultCodeDao()
                        .delete()).start();
            }
            if (MyApplication.treatmentRunning) {
                if (!mSerialJson.contains("report")) {
                    FaultCodeEntity entity = new FaultCodeEntity();
                    entity.time = EmtTimeUil.getTime();
                    entity.code = "接受:" + mSerialJson;
                    new Thread(() ->
                            EmtDataBase.getInstance(MyApplication.getInstance()).getFaultCodeDao()
                                    .insertFaultCode(entity)).start();
                }
            } else {
                FaultCodeEntity entity = new FaultCodeEntity();
                entity.time = EmtTimeUil.getTime();
                entity.code = "接受:" + mSerialJson;
                new Thread(() ->
                        EmtDataBase.getInstance(MyApplication.getInstance()).getFaultCodeDao()
                                .insertFaultCode(entity)).start();
            }
            if (myGson == null) {
                myGson = new Gson();
            }
//            JsonParser parse = new JsonParser();
//            JsonObject json = (JsonObject) parse.parse(mSerialJson);

//            if (json.has("publish")) {
            if (mSerialJson.contains("publish") && mSerialJson.startsWith("{\"publish\"")) {
//                Log.e(TAG, "接收->下位机数据:" + mSerialJson);
                ReceivePublicDataBean mBean = JsonHelper.jsonToClass(mSerialJson, ReceivePublicDataBean.class);

                int startIndex = JsonHelper.getCharacterPosition("\\{", mSerialJson, 2);
                int endIndex = JsonHelper.getCharacterPosition("\\}", mSerialJson, 2);

                String md5Json = mSerialJson.substring(startIndex, endIndex + 1);
//
                String md5Sign = MD5Helper.Md5(md5Json);

                if (md5Sign.equals(mBean.sign)) {//md5校验码一致，数据没问题
//                    Log.d(TAG, "接收下位机发布消息/数据：md5校验码一致: " + mSerialJson);
                    if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_POWER_MONITOR)) {//市电断电
                        Log.d(TAG, "接收->电池管理: " + mSerialJson);
//                        RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_DATA, mBean);
                        switch (mBean.publish.msg) {
                            case CommandReceiveConfig.MSG_AC_POWER_OFF: //电源管理: 1、AC220V断开
                                Log.e(TAG, "电源管理: 1、AC220V断开: " + mSerialJson);
                                MyApplication.acPowerOff = true;
                                if (!MyApplication.treatmentRunning) {
                                    //非治疗过程断电，弹出界面可以操作切断电池供电
                                    showPowerMonitorDialog();
                                }
                                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_AC_POWER_OFF, myGson.toJson(mBean.publish.data));
                                break;
                            case CommandReceiveConfig.MSG_AC_POWER_ON: //电源管理: 2、AC220V连接
                                Log.e(TAG, "电源管理: 2、AC220V连接: " + mSerialJson);
                                MyApplication.acPowerOff = false;
                                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_AC_POWER_ON, myGson.toJson(mBean.publish.data));
                                break;
                            case CommandReceiveConfig.MSG_AC_POWER_FAULT: //电源管理: 3、AC掉电超过30分钟
                                Log.e(TAG, "电源管理: 3、AC掉电超过30分钟: " + mSerialJson);
                                RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_AC_POWER_FAULT, myGson.toJson(mBean.publish.data));
                                break;
                        }
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_THERMOSTAT_GET)) {//获取温控参数
                        Log.d(TAG, "接收->温控数据: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_THERMOSTAT_SET, myGson.toJson(mBean.publish.data));
                        // {"publish":{"topic":"thermostat","msg":"no alive","data":{}},"sign":"7d3dc9df5512aa49af30641c8838f5de"}
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_THERMOSTAT)) {//获取温控
                        Log.d(TAG, "接收->温控数据: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_THERMOSTAT, myGson.toJson(mBean.publish.data));
                        // {"publish":{"topic":"thermostat","msg":"no alive","data":{}},"sign":"7d3dc9df5512aa49af30641c8838f5de"}
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_DEVICE_STATUS)) {//获取设备状态
//                        Log.e(TAG, "接收->设备数据: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_DEVICE_INFO, myGson.toJson(mBean.publish.data));
//                        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(myGson.toJson(mBean.publish.data), ReceiveDeviceBean.class);
//                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_DEVICE_INFO, mReceiveDeviceBean);
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_SELF_CHECK_VALVE)) {//阀自检结果
                        Log.e(TAG, "接收->阀自检: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_SELF_CHECK_VALVE, myGson.toJson(mBean.publish.data));
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_SELF_CHECK_WEIGHT)) {//上下位称自检结果
                        Log.e(TAG, "接收->称自检: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_SELF_CHECK_WEIGHT, myGson.toJson(mBean.publish.data));
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_SELF_CHECK_THERMOSTAT)) {//温控自检结果
                        Log.e(TAG, "接收->温控自检: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_SELF_CHECK_THERMOSTAT, myGson.toJson(mBean.publish.data));
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_PIPECART_INSTALL)
                            || mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_PIPECART_FINISH)) {//安装管路
                        Log.d(TAG, "接收->安装管路数据: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_PIPECART, myGson.toJson(mBean.publish));
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_PREHEAT)) {//预热返回结果
                        Log.e(TAG, "接收->预热: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_PREHEAT_DATA, myGson.toJson(mBean.publish));
                    } else if (mBean.publish.topic.contains(CommandReceiveConfig.TOPIC_AUTO_RINSE)) {//自动预冲返回结果
                        Log.e(TAG, "接收->自动预冲: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_AUTO_RINSE_DATA, myGson.toJson(mBean.publish));
                    } else if (mBean.publish.topic.contains(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS)) {//手动预冲返回结果
                        Log.e(TAG, "接收->手动预冲: "  + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_Manual_PRE, myGson.toJson(mBean.publish));
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_TREATMENT)) {//治疗过程的返回指令
//                        Log.d(TAG, "治疗过程的返回指令: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_TREATMENT_DATA, myGson.toJson(mBean.publish));
                        //{"publish":{"topic":"treatment","msg":"finish","data":{}},"sign":"3ff485ddce23a973de089080cd8591d7"}
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_TREATMENT_DRAIN)) {//治疗阶段返回引流结果
//                        Log.e(TAG, "接收->引流: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA, mBean);
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_TREATMENT_PERFUSE)) {//治疗阶段返回灌注结果
                        RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_DATA, mBean);
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_TREATMENT_RETAIN)) {//治疗阶段返回留腹结果
                        RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_RETAIN_DATA, mBean);
                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_TEMPERATURE_SET)) {//设置温度
                        RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_TEMPERATURE_SET, mBean);
//                    } else if (mBean.publish.topic.equals(CommandReceiveConfig.TOPIC_POWER_MONITOR)) {//电源监控
////                        RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_DATA, mBean);
//                        if (mBean.publish.msg.equals(CommandReceiveConfig.MSG_AC_POWER_OFF)) {//电源管理: 1、AC220V断开
//                            Log.d(TAG, "电源管理: 1、AC220V断开: " + mSerialJson);
//                            showPowerMonitorDialog();
//                        } else if (mBean.publish.msg.equals(CommandReceiveConfig.MSG_AC_POWER_ON)) {//电源管理: 2、AC220V连接
//                            Log.d(TAG, "电源管理: 2、AC220V连接: " + mSerialJson);
//                        }
                    }
//                    else if (mBean.publish.topic.equals("dpr/perfuse")) {
//                        RxBus.get().send(RxBusCodeConfig.EVENT_DPR_PER, mBean);
//                    } else if (mBean.publish.topic.equals("dpr/drain")) {
//                        RxBus.get().send(RxBusCodeConfig.EVENT_DPR_DRAIN, mBean);
//                    }else if (mBean.publish.topic.equals("dpr/supply")) {
//                        RxBus.get().send(RxBusCodeConfig.EVENT_DPR_SUPPLY, mBean);
//                    }else if (mBean.publish.topic.equals("dpr/supply1")) {
//                        RxBus.get().send(RxBusCodeConfig.EVENT_DPR_SUPPLY1, mBean);
//                    }else if (mBean.publish.topic.equals("dpr/supply2")) {
//                        RxBus.get().send(RxBusCodeConfig.EVENT_DPR_SUPPLY2, mBean);
//                    }else if (mBean.publish.topic.equals("dpr/treatment")) {
//                        RxBus.get().send(RxBusCodeConfig.EVENT_DPR_TREAT, mBean);
//                    }
                    else if (mBean.publish.topic.contains("dpr/")) {
                        RxBus.get().send(RxBusCodeConfig.EVENT_DPR, mBean);
                        Log.e(TAG, "接收->DPR数据: " + mSerialJson);
                    } else if (mBean.publish.topic.contains("check/perfuse")) {
                        RxBus.get().send(RxBusCodeConfig.CHECK_PER, mBean);
                    } else if (mBean.publish.topic.equals("valve/open") && mBean.publish.msg.equals("finish")) {
                        RxBus.get().send(RxBusCodeConfig.VALVE_STATUS, myGson.toJson(mBean.publish));
                    }else {//{"publish":{"topic":"power_monitor","msg":"ac power off","data":{}},"sign":"9ba3a2204dfb0ebe8873b324a07674b7"}
                        //{"publish":{"topic":"treatment","msg":"start","data":{}},"sign":"2a7ba846bf87413456bc45a4f1ea85fb"}
                        Log.d(TAG, "接收->其他数据: " + mSerialJson);
                        RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_OTHER, mSerialJson);
                    }
                } else {
                    Log.e(TAG, "接收下位机发布消息/数据：md5校验码不一致: " + mSerialJson);
                }

//            } else if (json.has("result")) {//应答（下位机->上位机）
            } else if (mSerialJson.contains("result") && mSerialJson.startsWith("{\"result\"")) {//应答（下位机->上位机）
//                Log.e(TAG, "应答（下位机->上位机）");
                ReceiveResultDataBean mBean = JsonHelper.jsonToClass(mSerialJson, ReceiveResultDataBean.class);

                int startIndex = JsonHelper.getCharacterPosition("\\{", mSerialJson, 2);
                int endIndex = JsonHelper.getCharacterPosition("\\}", mSerialJson, 2);

                String md5Json = mSerialJson.substring(startIndex, endIndex + 1);

                String md5Sign = MD5Helper.Md5(md5Json);

                if (md5Sign.equals(mBean.sign)) {//md5校验码一致，数据没问题

//                    Log.d(TAG, "应答（下位机->上位机）：接收数据没问题，md5校验码一致: " + mSerialJson);
                    if (0 == mBean.result.code) {
                        Log.e(TAG, "应答（下位机->上位机）：指令执行" + mSerialJson);
                        if (mBean.result.topic.equals("thermostat/get")) {
                            RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_THERMOSTAT_SET, mBean);
//                            Log.e("应答（下位机->上位机）：指令执行","thermostat/get");
                        }
//                        if (mBean.result.topic.equals("preheat/stop")) {
//                            RxBus.get().send(RxBusCodeConfig.EVENT_RECEIVE_STOP_HEART, mBean);
//                        }
                        if (mBean.result.topic.equals("report")) {
                            RxBus.get().send(RxBusCodeConfig.RESULT_REPORT, myGson.toJson(mBean.result.data));
//                            Log.e("应答（下位机->上位机）：指令执行","thermostat/get");
                        }
                        if (mBean.result.topic.contains("motolocal/calibration")) {
                            RxBus.get().send(RxBusCodeConfig.CALIBRATION, myGson.toJson(mBean.result.data));
                        }

                        if (mBean.result.topic.contains("board_version")) {
                            Log.e("sssssss",myGson.toJson(mBean.result.msg));

                            RxBus.get().send(RxBusCodeConfig.BOARD_VERSION, mBean);
                        }

                        //{"result":{"topic":"status/on","code":0,"msg":"ok","data":{}},"sign":"0e57f2988589e77e14ad6fb2bb605cdf"}
                        //{"result":{"topic":"selfcheck/start","code":0,"msg":"ok","data":{}},"sign":"c37efdf6408439658ba2cad171c6787b"}
//                        else {
                            RxBus.get().send(RxBusCodeConfig.EVENT_CMD_RESULT_OK, mBean.result.topic);
//                        if("weight/tare_all".equals(MyApplication.currCmd)){//开机去皮
//                            RxBus.get().send(RxBusCodeConfig.EVENT_FIRST_WEIGH_TARE_ALL_OK, "");
//                        }
//                            Log.d(TAG, "接收->其他数据: " + mSerialJson);
//                        }
                    } else {
                        RxBus.get().send(RxBusCodeConfig.EVENT_CMD_RESULT_ERR, mBean);
                        Log.e(TAG, "应答（下位机->上位机）：指令不执行" + mSerialJson);
//                        FaultCodeEntity entity = new FaultCodeEntity();
//                        entity.time = EmtTimeUil.getTime();
//                        entity.code = mBean.result.msg;
//                        EmtDataBase.getInstance(MyApplication.getInstance()).getFaultCodeDao()
//                                .insertFaultCode(entity);
                    }
                } else {
                    Log.e(TAG, "应答（下位机->上位机）：接收数据有问题，md5校验码不一致: " + mSerialJson);
                }
//                    System.out.println(new Gson().toJson(mBean));
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "数据解析异常: " + mSerialJson);
        }
    }

    /**
     * 市电断开时，打开PowerMonitorDialogActivity
     */
    private void showPowerMonitorDialog() {
        Intent intent = new Intent(this, PowerMonitorDialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (serialPortPlus != null) {
            serialPortPlus.onDestroy();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}