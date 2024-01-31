package com.emt.pdgo.next.data.serial.treatment;

/**
 * 补液参数
 */
public class SerialTreatmentSupplyBean {

    /** 补液流速阈值 */
    public int supplyFlowRate;
    /** 补液流速周期 秒 */
    public int supplyFlowPeriod;
    /** 补液目标保护值 */
    public int supplyProtectVolume;
    /** 启动补液的加热袋重量最低值 */
    public int supplyMinVolume;

}
