package com.emt.pdgo.next.data.entity;

/**
 * Created by chenjh on 2020/1/13.
 */


/**
 * 治疗周期的数据
 *
 * @author chenjh
 * @date 2019/3/5 09:34
 */

public class TreatmentHistoryBean {

    public String uuid;//

    public String sn;//

    public int cycle;//当前周期数

    public int perfusionVolume;//灌注量

    public int perfusionTime;//灌注时间

    public int drainTargetVolume;//引流目标量

    public int drainVolume;//引流量

    public int rinsePerfusionVolume;//辅助冲洗量

    public int drainTime;//引流时间

    public int waitingVolume;//灌注后留腹量

    public int waitingTime;//实际留腹的时间秒

    public int predictedResidualLiquidVolume;//预估腹腔剩余液体量

    public long createTime;
    public long endTime;



}