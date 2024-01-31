package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

public class KidBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = -556632454157588444L;
    public int peritonealDialysisFluidTotal = 1000;//腹透液总量
    public int perCyclePerfusionVolume = 100;//每周期灌注量
    public int cycle = 10;//循环治疗周期数
    public boolean isFinalSupply;
    public int firstPerfusionVolume = 0;//首次灌注量
    public int abdomenRetainingTime = 60;//留腹时间
    public int abdomenRetainingVolumeFinally = 0;//最末留腹量
    public int abdomenRetainingVolumeLastTime = 0;//上次最末留腹量
    public int ultrafiltrationVolume = 0;//预估超滤量
    public String treatTime = "1小时";

    public int sex = 2; // 0男；1女；2保密

    public int height = 90;
    public int weight = 15;
    public double bsa = 0.7251;

    public int finalSupply = 0; // 末袋补液
    
}
