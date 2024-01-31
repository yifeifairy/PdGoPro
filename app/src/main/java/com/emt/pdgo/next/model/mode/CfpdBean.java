package com.emt.pdgo.next.model.mode;

import java.io.Serializable;

public class CfpdBean implements Serializable {

    private static final long serialVersionUID = 1488523663147835479L;

    // 钙含量
    public int calcium = 0; // 0 false;1true

    // 浓度
    public int con;

    // 治疗总量
    public int totalVolume = 10000;

    // 治疗总时间
    public int totalTime = 5;

    // 灌注速率
    public int perfuse_rate = 30;

    // 留腹量
    public int continue_retain = 1000;

    // 引流速率
    public int drain_rate = 30;

    // 腹腔排空时间
    public int emptying_time = 30;

    // 首次灌注量
    public int firstpersuse = 1000;

    // 目标温度
    public int target_temperature = 375;

    // 下限温度
    public int lowtemperature = 350;

}
