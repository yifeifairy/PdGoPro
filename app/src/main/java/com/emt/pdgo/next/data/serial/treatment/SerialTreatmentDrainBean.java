package com.emt.pdgo.next.data.serial.treatment;

/**
 * 引流参数
 */
public class SerialTreatmentDrainBean {

    /** 引流流速阈值 */
    public int drainFlowRate;
    /** 引流流速周期 秒 */
    public int drainFlowPeriod;
    /** 引流冲洗量 */
    public int drainRinseVolume;
    /** 引流次数 */
    public int drainRinseTimes;
    /** 0周期引流合格率 */
    public int firstDrainPassRate;
    /** 周期引流合格率 */
    public int drainPassRate;
    /** 最末引流是否排空 */
    public int isFinalDrainEmpty;
    /** 最末引流排空是否等待 */
    public int isFinalDrainEmptyWait;
    /** 最末引流排空等待时间 */
    public int finalDrainEmptyWaitTime;
    /** 是否开启负压引流 */
    public int isVaccumDrain;

}
