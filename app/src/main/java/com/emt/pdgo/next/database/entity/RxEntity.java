package com.emt.pdgo.next.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class RxEntity {

    @PrimaryKey(autoGenerate  = true)
    public int id;

    public String time;

    public int perVol; // 腹透液总量

    public int perCycleVol; // 每周期灌注量

    public int treatCycle; // 循环治疗周期数

    public int firstPerVol; // TPD首次灌注量

    public int abdTime; // 留腹时间

    public int endAbdVol; // 最末留腹量

    public int lastTimeAbdVol; // 上次留腹量

    public int ult; // 本次预估超滤

    public String ulTreatTime; // 预估治疗时间

}
