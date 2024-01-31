package com.emt.pdgo.next.data.serial.receive;


public class ReceiveDeviceBean1 {

    /**
     * 灌注阀状态
     */
    public String perfuse;

    /**
     * 安全阀状态
     */
    public String safe;

    /**
     * 补液阀状态
     */
    public String supply;

    /**
     *  末袋补液阀状态
     */
    public String supply2;

    /**
     * 引流阀状态
     */
    public String drain;

    /**
     * 负压引流阀状态
     */
    public String vaccum;

//    /**
//     * 负压引流阀状态
//     */
//    public String neg;

    /**
     * 温度
     */
    public int temp;

    /**
     * 温度
     */
    public int isOverTemp;
    /**
     * t0
     */
    public int isT0Err;
    /**
     * t1
     */
    public int isT1Err;
    /**
     * t2
     */
    public int isT2Err;

    /**
     * 上位称数值
     */
    public int upper;

    /**
     * 下位称数值
     */
    public int lower;

    /**
     * 是否断电
     */
    public int isAcPowerIn;

    /**
     * 是否关机
     */
    public int batteryVoltage;

    public int batteryLevel;

}
