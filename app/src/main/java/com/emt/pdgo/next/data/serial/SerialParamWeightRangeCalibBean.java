package com.emt.pdgo.next.data.serial;

/**
 * 上位机下发命令: 秤参数 下位秤数值
 */
public class SerialParamWeightRangeCalibBean {

    /**
     * 上位称和下位称 ：upper 或 lower
     * */
    public String id;
    /**
     * 称的标定值，例如 2000
     */
    public int range = 0;

}
