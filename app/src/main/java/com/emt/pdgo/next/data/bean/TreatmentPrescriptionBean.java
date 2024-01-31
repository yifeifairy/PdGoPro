package com.emt.pdgo.next.data.bean;

/**
 * 治疗处方设置
 *
 * @author chenjh
 * @date 2019/3/5 17:01
 */
public class TreatmentPrescriptionBean extends BaseBean {


    public int mPeritonealDialysisFluidTotal = 2000;//腹透液总量
    public int mPerCyclePerfusionVolume = 500;//每周期灌注量
    public int mPeriodicities = 4;//循环治疗周期数
    public int mFirstPerfusionVolume = 0;//首次灌注量
    public int mAbdomenRetainingTime = 1;//留腹时间
    public int mAbdomenRetainingVolumeFinally = 0;//最末留腹量
    public int mAbdomenRetainingVolumeLastTime = 0;//上次留腹量
    public int mUltrafiltrationVolume = 0;//预估超滤量


}
