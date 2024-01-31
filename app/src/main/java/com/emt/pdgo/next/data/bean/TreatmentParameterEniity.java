package com.emt.pdgo.next.data.bean;


import java.io.Serializable;

/**
 * 治疗之前的体重、血压、处方、治疗参数
 *
 * @author chenjh
 * @date 2019/1/21 15:29
 */
public class TreatmentParameterEniity extends BaseBean implements Serializable {

    private static final long serialVersionUID = -4807330875719996837L;

    public float weight = 50.0f;// 体重
    public int heartRate = 0;// 心率
    public int sbp = 120;//血压 收缩压
    public int dbp = 75;//血压 舒张压

    // 处方
    public int peritonealDialysisFluidTotal = 2800;//腹透液总量
    public int perCyclePerfusionVolume = 500;//每周期灌注量
    public int cycle = 4;//循环治疗周期数
    public int firstPerfusionVolume = 0;//首次灌注量
    public int abdomenRetainingTime = 15;//留腹时间
    public int abdomenRetainingVolumeFinally = 0;//最末留腹量
    public int abdomenRetainingVolumeLastTime = 0;//上次最末留腹量
    public int ultrafiltrationVolume = 0;//预估超滤量

    public boolean isFinalSupply = false;

    public int increaseVol = 100;
    public int increaseTime = 10;

    public int finalSupply = 0; // 末袋补液

    public String  treatTime = "1小时"; // 预估治疗时间

    // 引流参数
//    /*** 流量测定时间间隔 s **/
//    public int drainTimeInterval = 60;//60
//    /*** 流量测定 阈值 g **/
//    public int drainThresholdValue = 30;//30
//
//    /*** 0周期引流比例 % **/
//    public int drainZeroCyclePercentage = 100;
//    /*** 其他周期引流比例 % **/
//    public int drainOtherCyclePercentage = 75;
//    /*** 引流/灌注超时报警 s **/
//    public int drainTimeoutAlarm = 45;
//
//    /*** 引流辅助冲洗 量 g **/
//    public int drainRinseVolume = 50;//50
//    /*** 引流辅助冲洗 次数 次 **/
//    public int drainRinseNumber = 1;
//    /*** 最末引流排空等待  **/
//    public boolean isDrainManualEmptying;//
//    /*** 最末引流提醒间隔 min **/
//    public int drainWarnTimeInterval = 30;//30

    // 灌注参数
    /*** 是否允许最末灌注减去留腹量 g **/
//    public int allowAbdominalVolume = 0; // 是0否1
//    public boolean perfAllowAbdominalVolume = false; // 是0否1
//    /*** 灌注最大警戒值 g 1000-5000 **/
//    public int perfMaxWarningValue = 3000;
//    /*** 流量测定时间间隔 s **/
//    public int perfTimeInterval = 60;
//    /*** 流量测定 阈值 g **/
//    public int perfThresholdValue = 30;
//    /*** 加热袋重量最低值 g 100-1000 **/
//    public int perfMinWeight = 100;

    // 补液参数
//    /*** 流量测定时间间隔 s 60- 600 **/
//    public int supplyTimeInterval = 60;
//    /*** 流量测定 阈值 g 30-200 **/
//    public int supplyThresholdValue = 30;
//    /*** 补液目标保护值 g 0-500 **/
//    public int supplyTargetProtectionValue = 500;
//    /*** 启动补液的加热袋重量最低值 g 500-10000 **/
//    public int supplyMinWeight = 500;

//    public int drainThresholdZeroCycle = 50;//零周期引流阈值  50% 75% 100% 120%
//    public int drainThresholdOtherCycle = 50;//其他周期引流阈值   50% 75% 100% 120%
//
//    public int perfusionWarningValue = 3000;//灌注警戒值
//
//    public int alarmTimeInterval = 30;//等待提醒间隔时间 末次排空延迟
//
//    public boolean isDrainManualEmptying;//最末引流排空等待
//    public boolean isNegpreDrain = false;//负压引流
//    public boolean isAbdomenRetainingDeduct= false;//留腹扣除
//    public boolean isAlarmWakeUp= false;//治疗结束报警唤醒
//    public boolean isZeroCycleUltrafiltration= false;//只计算循环透析治疗期间的超滤

    public boolean isDormancy= false;//屏幕休眠
    public boolean isTouchTone= false;//屏幕触控声

}
