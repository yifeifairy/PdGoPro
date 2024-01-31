package com.emt.pdgo.next.data.bean;

/**
 * 治疗参数设置
 *
 * @author chenjh
 * @date 2019/3/5 17:01
 */
public class TreatmentParameterBean extends BaseBean {


    /*** 零周期引流阈值 %   50% 75% 100% 120% **/
    public int mDrainThresholdZeroCycle = 50;//零周期引流阈值  50% 75% 100% 120%
    public int mDrainThresholdOtherCycle = 50;//其他周期引流阈值   50% 75% 100% 120%

    public int mDrainLastDelay = 0;//末次排空延迟
    public int mPerfusionWarningValue = 3000;//灌注警戒值

    public boolean isDrainManualEmptying;//最末引流排空等待
    public boolean isAbdomenRetainingDeduct;//留腹扣除
    public boolean isAlarmWakeUp;//治疗结束报警唤醒
    public boolean isAbdomenRetainingTime;//只计算循环透析治疗期间的超滤


}
