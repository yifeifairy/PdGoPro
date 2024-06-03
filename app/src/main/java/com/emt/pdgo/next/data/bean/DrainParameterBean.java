package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

/**
 * 引流参数bean
 *
 * @author chenjh
 * @date 2019/1/24 13:45
 */
public class DrainParameterBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = -5484231191502215087L;
//    /*** 流量测定时间间隔 s **/
//    public int timeInterval = 60;//60
//    /*** 流量测定 阈值 g **/
//    public int thresholdValue = 30;//30
//
//    /*** 0周期引流比例 % **/
//    public int zeroCyclePercentage = 100;
//    /*** 其他周期引流比例 % **/
//    public int otherCyclePercentage = 75;
//    /*** 引流/灌注超时报警 s **/
//    public int timeoutAlarm = 45;
//
//    /*** 引流辅助冲洗 量 g **/
//    public int rinseVolume = 50;//50
//    /*** 引流辅助冲洗 次数 次 **/
//    public int rinseNumber = 1;
//    /*** 最末引流排空等待  **/
//    public boolean isDrainManualEmptying;//
//    /*** 最末引流提醒间隔 min **/
//    public int warnTimeInterval = 30;//30


    /*** 流量测定时间间隔 s **/
    public int drainTimeInterval = 30;//60
    /*** 流量测定 阈值 g **/
    public int drainThresholdValue = 30;//30

    /*** 0周期引流比例 % **/
    public int drainZeroCyclePercentage = 100;
    /*** 其他周期引流比例 % **/
    public int drainOtherCyclePercentage = 75;
    /*** 引流/灌注超时报警 s **/
    public int drainTimeoutAlarm = 45;

    /*** 引流辅助冲洗 量 g **/
    public int drainRinseVolume = 50;//50
    /*** 引流辅助冲洗 次数 次 **/
    public int drainRinseNumber = 1;
    /*** 最末引流排空等待  **/
    public boolean isDrainManualEmptying;//
    /*** 最末引流提醒间隔 min **/
    public int drainWarnTimeInterval = 30;//30

    public boolean isNegpreDrain = false;//负压引流
    public int alarmTimeInterval = 30;//等待提醒间隔时间 末次排空延迟
}
