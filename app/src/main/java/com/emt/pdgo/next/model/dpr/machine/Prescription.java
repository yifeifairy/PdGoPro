package com.emt.pdgo.next.model.dpr.machine;

import java.io.Serializable;

public class Prescription implements Serializable {


    private static final long serialVersionUID = 564685909634286234L;
    // 治疗总量
    public int totalVolume = 10000;

    // 治疗总时间
    public int totalTime = 5;

    public int empty_enable = 0;

    public int supplying_interval = 1;

    // 灌注速率
    public int perfuse_rate = 30;

    // 留腹量
    public int continue_retain = 1000;

    // 引流速率
    public int drain_rate = 30;

    // 腹腔排空时间
    public int emptying_time = 30;

    // 首次灌注量
    public int firstpersuse = 2000;

    public int nor_liquid = 0;

    public int sel_liquid = 0;
    public double osm = 0L;
    public int inter_time = 0;
    public int num = 0;

    public int norSel = 0;
    public int penSel = 0;

    public String startTime = "11时30分";
    public String endTime = "12时30分";

    // 目标温度
    public int target_temperature = 375;

    // 下限温度
    public int lowtemperature = 350;

    public String modify;

}
