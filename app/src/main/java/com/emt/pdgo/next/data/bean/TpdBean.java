package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

public class TpdBean implements Serializable {
    private static final long serialVersionUID = 8662627273537646720L;

    public int peritonealDialysisFluidTotal = 2800;//腹透液总量
    public int perCyclePerfusionVolume = 500;//每周期灌注量
    public int cycle = 4;//循环治疗周期数
    public int firstPerfusionVolume = 0;//首次灌注量
    public int abdomenRetainingTime = 15;//留腹时间
    public int abdomenRetainingVolumeFinally = 0;//最末留腹量
    public int abdomenRetainingVolumeLastTime = 0;//上次最末留腹量
    public int ultrafiltrationVolume = 0;//预估超滤量

    public String treatTime = "1小时";

    public boolean isFinalSupply;
}
