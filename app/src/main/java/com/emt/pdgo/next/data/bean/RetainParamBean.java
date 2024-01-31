package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

public class RetainParamBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = -1065575917703821577L;
    public boolean isAbdomenRetainingDeduct= false;//留腹扣除
    public boolean isAlarmWakeUp= false;//治疗结束报警唤醒
    public boolean isZeroCycleUltrafiltration= false;//只计算循环透析治疗期间的超滤

    public int time = 240;

}
