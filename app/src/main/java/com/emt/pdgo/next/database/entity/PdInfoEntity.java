package com.emt.pdgo.next.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PdInfoEntity {

    @PrimaryKey(autoGenerate = true)

    public int id;

    public int cycle;//当前周期数


    public int preVol; // 灌注量

    public String preTime; // 灌注时间


    public int auFvVol; // 辅助冲洗量

    public int abdAfterVol; // 灌注后\n留腹量

    public String abdTime; // 留腹\n时间

    public int drainTvVol; // 引流\n目标值

    public int drainage; // 引流量

    public String drainTime; // 引流\n时间

    public int remain; // 预估腹腔\n剩余液体

}
