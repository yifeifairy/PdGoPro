package com.emt.pdgo.next.common.config;


import android.util.Log;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.data.bean.LedBean;
import com.emt.pdgo.next.data.bean.PidBean;
import com.emt.pdgo.next.data.bean.ValueBean;
import com.emt.pdgo.next.data.serial.SerialParamAutoRinseBean;
import com.emt.pdgo.next.data.serial.SerialParamEmptytBean;
import com.emt.pdgo.next.data.serial.SerialParamIdBean;
import com.emt.pdgo.next.data.serial.SerialParamManualRinseBean;
import com.emt.pdgo.next.data.serial.SerialParamPreheatBean;
import com.emt.pdgo.next.data.serial.SerialParamTemperatureBean;
import com.emt.pdgo.next.data.serial.SerialParamThermoStatBean;
import com.emt.pdgo.next.data.serial.SerialParamWeightBean;
import com.emt.pdgo.next.data.serial.SerialParamWeightRangeCalibBean;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.dpr.param.ParamBean;
import com.emt.pdgo.next.model.StartTreatmentModel;
import com.emt.pdgo.next.model.dpr.PidParam;
import com.emt.pdgo.next.model.dpr.machine.Prescription;
import com.emt.pdgo.next.model.dpr.machine.SpecialPrescription;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.google.gson.Gson;

/**
 * @ProjectName: 串口数据处理工具类
 * @Package: com.emt.pdgo.next.common.config
 * @ClassName: CommandDataHelper
 * @Description: RxBusCode
 * @Author: chenjh
 * @CreateDate: 2020/1/11 10:46 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/11 10:46 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */

public class CommandDataHelper {

    private static CommandDataHelper instance;

    private CommandDataHelper() {

    }

    public static CommandDataHelper getInstance() {
        if (instance == null) {
            instance = new CommandDataHelper();
        }
        return instance;
    }

    public String dprSpecial(int modify, int base, int last, int special) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SpecialPrescription params = new SpecialPrescription();
        params.specmodify = modify;
        params.basebag = base;
        params.lastbag = last;
        params.special = special;
        mSerialRequestBean.method = "specialpresci/config";
        mSerialRequestBean.params = params;

        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);
    }

    /**
     * 发送: 获取设备信息: 开启
     */
    public String setStatusOn() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.params = new SerialParamEmptytBean();
        mSerialRequestBean.method = CommandSendConfig.METHOD_STATUS_ON;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    /**
     * 发送: 获取设备信息: 关闭
     */
    public String setStatusOff() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.params = new SerialParamEmptytBean();
        mSerialRequestBean.method = CommandSendConfig.METHOD_STATUS_OFF;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);


    }

    /**
     perfuse   retain     drain    all
     灌注灯     留腹灯      引流灯    所有灯
     colour 0  红色（故障）   1绿色(正常治疗）  2黄色（自检）
     */
    public String LedOpen(String  type, boolean open, int dayFlag, int colour) {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        LedBean ledBean = new LedBean();
        ledBean.id = type;
        ledBean.dayflag = dayFlag;
        ledBean.colour = colour;
//        SerialParamIdBean mSerialWeightBean4 = new SerialParamIdBean();
//        mSerialWeightBean4.id = "xxx";

//        mSerialRequestBean.params = mSerialWeightBean1;
//        mSerialRequestBean.params = mSerialWeightBean2;
        mSerialRequestBean.params = ledBean;
//        mSerialRequestBean.params = mSerialWeightBean4;

        if (open) {
            mSerialRequestBean.method = CommandSendConfig.LED_ON;
        } else  {
            mSerialRequestBean.method = CommandSendConfig.LED_OFF;
        }

//        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_ZERO_CLEAR;//"weight/tare"

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    public String LedClose(String  type, int dayFlag) {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        LedBean ledBean = new LedBean();
        ledBean.id = type;
        ledBean.dayflag = dayFlag;

        mSerialRequestBean.params = ledBean;
//        mSerialRequestBean.params = mSerialWeightBean4;
        mSerialRequestBean.method = CommandSendConfig.LED_OFF;
//        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_ZERO_CLEAR;//"weight/tare"
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);
    }

    /**
     * 秤指令的json字符串:  秤去皮(归零)
     */
    public String weightTareAllCmdJson() {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

//        SerialParamIdBean mSerialWeightBean1 = new SerialParamIdBean();
//        mSerialWeightBean1.id = "upper";
//
//        SerialParamIdBean mSerialWeightBean2 = new SerialParamIdBean();
//        mSerialWeightBean2.id = "lower";

        SerialParamIdBean mSerialWeightBean3 = new SerialParamIdBean();
        mSerialWeightBean3.id = "all";

//        SerialParamIdBean mSerialWeightBean4 = new SerialParamIdBean();
//        mSerialWeightBean4.id = "xxx";

//        mSerialRequestBean.params = mSerialWeightBean1;
//        mSerialRequestBean.params = mSerialWeightBean2;
        mSerialRequestBean.params = mSerialWeightBean3;
//        mSerialRequestBean.params = mSerialWeightBean4;

        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_ZERO_CLEAR;//"weight/tare"

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    /**
     * 秤指令的json字符串:  上位秤去皮(归零)
     */
    public String weightTareUpperCmdJson() {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialParamIdBean mSerialWeightBean = new SerialParamIdBean();
        mSerialWeightBean.id = "upper";

        mSerialRequestBean.params = mSerialWeightBean;

        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_ZERO_CLEAR;//"weight/tare"

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    /**
     * 秤指令的json字符串:  下位秤去皮(归零)
     */
    public String weightTareLowerCmdJson() {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialParamIdBean mSerialWeightBean = new SerialParamIdBean();
        mSerialWeightBean.id = "lower";

        mSerialRequestBean.params = mSerialWeightBean;

        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_ZERO_CLEAR;//"weight/tare"

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    /**
     * 秤指令的json字符串:  上位秤 零点标定
     */
    public String weightZeroCalibUpperCmdJson() {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialParamIdBean mSerialWeightBean = new SerialParamIdBean();
        mSerialWeightBean.id = "upper";

        mSerialRequestBean.params = mSerialWeightBean;

        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_ZERO_CALIB;//"weight/tare"

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    /**
     * 秤指令的json字符串:  下位秤 零点标定
     */
    public String weightZeroCalibLowerCmdJson() {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialParamIdBean mSerialWeightBean = new SerialParamIdBean();
        mSerialWeightBean.id = "lower";

        mSerialRequestBean.params = mSerialWeightBean;

        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_ZERO_CALIB;//"weight/tare"

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }


    /**
     * 发送: 自检指令
     */
    public String selfCheckCmdJson(int upper, int lower) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialParamWeightBean mSerialWeightBean = new SerialParamWeightBean();
        mSerialWeightBean.upper = upper;//600
        mSerialWeightBean.lower = lower;//600

        mSerialRequestBean.params = mSerialWeightBean;
        mSerialRequestBean.method = CommandSendConfig.METHOD_SELFCHECK_START;

        String md5Json = new Gson().toJson(mSerialRequestBean);
//        Log.e("自检指令","md5Json1:" + md5Json);
//        md5Json = "{\"method\":\"selfcheck/start\",\"params\":{\"upper\":0,\"lower\":0}}";
//        Log.e("自检指令","md5Json2:" + md5Json);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        Log.e("自检指令", "sign:" + mRequestBean.sign);

        return new Gson().toJson(mRequestBean);
    }


    /**
     * 上位秤标定量程量程指令
     *
     * @param value 上位秤的实际数值
     * @return
     */
    public String setUpperWeightCmdJson(int value) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialParamWeightRangeCalibBean mSerialWeightBean1 = new SerialParamWeightRangeCalibBean();
        mSerialWeightBean1.id = "upper";
        mSerialWeightBean1.range = value;

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.params = mSerialWeightBean1;

        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_RANGE_CALIB;//"weight/calib";

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

//        return "{\"request\":{\"method\":\"weight/range_calib\",\"params\":{\"id\":\"upper\", \"range\": 2000}},\"sign\":\"163086aca045893cf10c54d3fb27f643\"}";
    }

    /**
     * 发送: 下位秤 标定量程 2000
     */
    public String setLowerWeightCmdJson(int value) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();


        SerialParamWeightRangeCalibBean mSerialWeightBean1 = new SerialParamWeightRangeCalibBean();
        mSerialWeightBean1.id = "lower";
        mSerialWeightBean1.range = value;

        mSerialRequestBean.params = mSerialWeightBean1;

        mSerialRequestBean.method = CommandSendConfig.METHOD_WEIGH_RANGE_CALIB;//"weight/calib";

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

//        String cmdStr="{\"request\":{\"method\":\"weight/range_calib\",\"params\":{\"id\":\"lower\", \"range\": 2000}},\"sign\":\"e57b3b17748dfaaceff39936a968f7c9\"}";
//        return  cmdStr;
    }

//    /**
//     * 发送: 秤指令 1
//     */
//    public String setWeightCmd(int upper, int lower){
//
//        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
//
//        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
//
//
//        SerialParamWeightUpperBean mSerialWeightBean1 = new SerialParamWeightUpperBean();
//        mSerialWeightBean1.upper = upper;
//
//        SerialParamWeightLowerBean mSerialWeightBean2 = new SerialParamWeightLowerBean();
//        mSerialWeightBean2.lower = lower;
//
//        SerialParamWeightBean mSerialWeightBean3 = new SerialParamWeightBean();
//        mSerialWeightBean3.upper = upper;
//        mSerialWeightBean3.lower = lower;
//
//        SerialParamWeightBean mSerialWeightBean4 = new SerialParamWeightBean();
//        mSerialWeightBean4.upper = upper;
//        mSerialWeightBean4.lower = lower;
//
//
//
////        mSerialRequestBean.params = mSerialWeightBean1;
////        mSerialRequestBean.params = mSerialWeightBean2;
//        mSerialRequestBean.params = mSerialWeightBean3;
////        mSerialRequestBean.params = mSerialWeightBean4;
//
//        mSerialRequestBean.method = CommandSendConfig.WEIGH_ZERO_CALIB;//"weight/calib";
//
//        String md5Json = new Gson().toJson(mSerialRequestBean);
//
//        mRequestBean.request = mSerialRequestBean;
//        mRequestBean.sign = MD5Helper.Md5(md5Json);
//
//        return new Gson().toJson(mRequestBean);
//
//    }


    /**
     * 发送: 蜂鸣器的开关
     */
    public String setBeeptCmdJson(String beeptCmd) {
//        {"request":{"method":"beep/on","params":{}},"sign":"e6d7eb32a4624cf71eee2c637f29ea22"}
//
//        {"request":{"method":"beep/off","params":{}},"sign":"d7e9042a6008dfd8cc5d850e1b495ba7"}
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();


        SerialParamEmptytBean mEmptytBean = new SerialParamEmptytBean();

        mSerialRequestBean.params = mEmptytBean;

        mSerialRequestBean.method = beeptCmd;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    public String valueOpen(String value, boolean open) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        ValueBean valueBean = new ValueBean();
        valueBean.id = value;
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        mSerialRequestBean.params = valueBean;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        String method = "";
        if (open) {
            method = "valve/open";
        } else {
            method = "valve/close";
        }
        mSerialRequestBean.method = method;
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);
    }

    /**
     * 发送: 预热指令
     */
    public String setPreheatCmdJson(int temp, int weight) {
//        {"request":{"method":"preheat/start","params":{"temp":370,"weight":300}},"sign":"29c4b4012060e0f4a5c0d5ed53c925bb"}
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialParamPreheatBean mParamPreheatBean = new SerialParamPreheatBean();
        mParamPreheatBean.temp = temp;
        mParamPreheatBean.weight = weight;

        mSerialRequestBean.params = mParamPreheatBean;
        mSerialRequestBean.method = CommandSendConfig.METHOD_PREHEAT_START;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    /**
     * 发送: 预热结束指令
     */
    public String stopPreheatCmdJson() {
//        {"request":{"method":"preheat/start","params":{"temp":370,"weight":300}},"sign":"29c4b4012060e0f4a5c0d5ed53c925bb"}
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialParamTemperatureBean mTemperatureBean = new SerialParamTemperatureBean();
        mTemperatureBean.temp = 0;

        mSerialRequestBean.params = mTemperatureBean;
        mSerialRequestBean.method = CommandSendConfig.METHOD_PREHEAT_STOP;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    /**
     * 发送: 设置温度（发送预热指令之后修改温度）
     *
     * @param temp
     * @return
     */
    public String setTemperatureCmdJson(int temp) {
//        {"method":"temperature/set","params":{"temp":370}}
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialParamTemperatureBean mTemperatureBean = new SerialParamTemperatureBean();
        mTemperatureBean.temp = temp;

        mSerialRequestBean.params = mTemperatureBean;
        mSerialRequestBean.method = CommandSendConfig.METHOD_TEMPERATURE_SET;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }


    /**
     * 发送: 装管路指令
     */
    public String setPipecartCmdJson(String cmd) {
//        {"request":{"method":"pipecart/install","params":{}},"sign":"a0cbeeda928db8302217ea94944b9bde"}
//        {"request":{"method":"pipecart/finish","params":{}},"sign":"f451a5f1b971a816b04ba199f2b20d8b"}

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = cmd;
        mSerialRequestBean.params = new SerialParamEmptytBean();

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    /**
     * 获取温控参数
     *
     * @return
     */
    public String getThermostatCmdJson() {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = CommandSendConfig.METHOD_TEMPERATURE_PARAMS_GET;

        mSerialRequestBean.params = new SerialParamEmptytBean();

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    /**
     * 设置温控参数
     *
     * @param mParamBean
     * @return
     */
    public String setThermostatCmdJson(SerialParamThermoStatBean mParamBean) {
//        mParamBean.p1 = 20;
//        mParamBean.p2 = 376;
//        mParamBean.p3 = 10;
//        mParamBean.p4 = 420;
//        mParamBean.p5 = 510;
//        mParamBean.p6 = 6;
//        mParamBean.p7 = 51;
//
//        mParamBean.TaE = 1;
//        mParamBean.TbE = 2;
//        mParamBean.TcE = 3;

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = CommandSendConfig.METHOD_TEMPERATURE_PARAMS_SET;

        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    /**
     * 发送: 启动自动预冲指令
     */
    public String startAutoRinseCmd(SerialParamAutoRinseBean mParamBean) {


        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = CommandSendConfig.METHOD_RINSE_AUTO_START;
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        // {“request:{"method":"autorinse/start" "params":{"time": "30", "supply":"30","perfuse":"30","drain","30"}}, "sign":"********"}
        return new Gson().toJson(mRequestBean);

    }

    /**
     * 发送: 停止自动预冲指令
     */
    public String abortAutoRinseCmd() {


        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = CommandSendConfig.METHOD_RINSE_AUTO_ABORT;
        mSerialRequestBean.params = new SerialParamEmptytBean();

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    /**
     * 发送: 继续自动预冲指令
     */
    public String continueAutoRinseCmd() {
//        {"request":{"method":"autorinse/start","params":{"time":15,"supply":300,"perfuse":200,"drain":100}},"sign":"5a191b384fde52daa556dfec43f82957"}
//
//        {"request":{"method":"autorinse/continue","params":{}},"sign":"42c71f69ff8ed2637d07b67fef0583bf"}
//
//        {"request":{"method":"autorinse/stop","params":{}},"sign":"6083ebd998d65e972824255b24934186"}

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();


        mSerialRequestBean.method = CommandSendConfig.METHOD_RINSE_AUTO_FAILURE_CONTINUE;
        mSerialRequestBean.params = new SerialParamEmptytBean();

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    /**
     * 手动预冲
     * @param mParamBean
     * @return
     */
    public String startManualRinseCmd(SerialParamManualRinseBean mParamBean) {


        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = "manualrinse/start";
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);

    }

    public String startManualRinseCmd2(SerialParamAutoRinseBean mParamBean) {


        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = "manualrinse/start";
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);

    }


    /**
     * 发送: 手动预冲指令
     */
    public String setManualRinseCmd(String cmd, String id) {

//        {"request":{"method":"manualrinse/start","params":{}},"sign":"2b71dffab34921e43136971bc8a802d9"}
//        {"request":{"method":"manualrinse/open","params":{"id":"perfuse"}},"sign":"f39d3fdea0479d046c5f15f6250255b1"}
//        {"request":{"method":"manualrinse/open","params":{"id":"supply"}},"sign":"cb740a727bf74acee57f99db0808a9e2"}
//        {"request":{"method":"manualrinse/open","params":{"id":"drain"}},"sign":"d0e1c495cb42ac8ab1ccd8b835c779bb"}
//        {"request":{"method":"manualrinse/close","params":{"id":"perfuse"}},"sign":"4d5f5e3953f993ce0b497b63b1590243"}
//        {"request":{"method":"manualrinse/close","params":{"id":"supply"}},"sign":"321dcba0cdb8114f614b0639a66c7b7d"}
//        {"request":{"method":"manualrinse/close","params":{"id":"drain"}},"sign":"a2476be4cb56281b148343cb7a6288cf"}
//        {"request":{"method":"manualrinse/stop","params":{}},"sign":"69a163e1b943bf723194eb1c8b40c407"}


        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = cmd;

        if (cmd.equals(CommandSendConfig.METHOD_RINSE_MANUAL_START) || cmd.equals(CommandSendConfig.METHOD_RINSE_MANUAL_STOP)) {

            mSerialRequestBean.params = new SerialParamEmptytBean();
        } else {
            SerialParamIdBean mParamBean = new SerialParamIdBean();
            mParamBean.id = id;

            mSerialRequestBean.params = mParamBean;

        }

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    /**
     * 电机关闭打开
     */
    public String isValveOpen(boolean isOpen, String id) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        String method;
        if (isOpen) {
            method = "valve/open";
        } else {
            method = "valve/close";
        }
        mSerialRequestBean.method = method;
        SerialParamIdBean mParamBean = new SerialParamIdBean();
        mParamBean.id = id;
        mSerialRequestBean.params = mParamBean;
        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    /**
     * 发送: 继续当前治疗的故障阶段指令
     */
    public String continueTreatmentCmd() {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = CommandSendConfig.METHOD_TREATMENT_FAILURE_CONTINUET;
        mSerialRequestBean.params = new SerialParamEmptytBean();

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);

    }

    /**
     * 发送: 自定义指令
     */
    public String customCmd(String cmd) {

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        mSerialRequestBean.method = cmd;
        mSerialRequestBean.params = new SerialParamEmptytBean();

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        return new Gson().toJson(mRequestBean);
    }

    public String startTreatment(int mode, int debug) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        StartTreatmentModel params = new StartTreatmentModel();
        params.treatmentmode = mode;
        params.debug = debug;
        mSerialRequestBean.method = "treatment/start";
        mSerialRequestBean.params = params;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);
    }

    public String pidParam(PidBean pidBean, boolean modify) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        PidParam param = new PidParam();
//        param.const_d1 = (int) pidBean.const_d1;
//        param.const_d2 = (int)pidBean.const_d2;
        param.const_i1 = (int) (pidBean.const_i1 * 10);
        param.const_i2 = (int) (pidBean.const_i2 * 10);
        param.const_p1 = (int) (pidBean.const_p1 * 10);
        param.const_p2 = (int) (pidBean.const_p2 * 10);
        param.saturate_bw1 =(int) (pidBean.saturate_bw1 * 10);
        param.saturate_bw2 =(int) (pidBean.saturate_bw2 * 10);
        param.modify = modify ? 1 : 0;
        mSerialRequestBean.method = "pidparam";
        mSerialRequestBean.params = param;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);
    }

    public String special(int modify, int base, int last, int supply_channel) {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SpecialPrescription params = new SpecialPrescription();
        params.specmodify = modify;
        params.basebag = base;
        params.lastbag = last;
        params.supply_channel = supply_channel;
        mSerialRequestBean.method = "specialpresci/config";
        mSerialRequestBean.params = params;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);
    }

    /**
     * DPR处方配置
     */
    public String config(int modify) {
        Prescription prescription = PdproHelper.getInstance().getPrescription();
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        ParamBean paramBean = new ParamBean();
        paramBean.dprmodify = modify;
        paramBean.supplying_interval = prescription.supplying_interval;
        paramBean.drain_rate = prescription.drain_rate;
        paramBean.emptying_time = prescription.emptying_time;
        paramBean.perfuse_rate = prescription.perfuse_rate;
        paramBean.firstperfuse = prescription.firstpersuse;
        paramBean.continue_retain = prescription.continue_retain;
        paramBean.empty_enable = prescription.empty_enable;
        paramBean.totalVolume = prescription.totalVolume;
        paramBean.lowtemperature = prescription.lowtemperature;
        mSerialRequestBean.method = "dprpresci/config";
        mSerialRequestBean.params = paramBean;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        return new Gson().toJson(mRequestBean);
    }

}
