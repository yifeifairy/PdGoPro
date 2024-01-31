package com.emt.pdgo.next.data.local;

import com.emt.pdgo.next.data.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class HisPdItem extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    @Column(name = "cycle")
    public int cycle;//当前周期数

    @Column(name = "preVol")
    public int preVol; // 灌注量

    @Column(name = "preTime")
    public int preTime; // 灌注时间

    @Column(name = "auFvVol")
    public int auFvVol; // 辅助冲洗量
    @Column(name = "abdAfterVol")
    public int abdAfterVol; // 灌注后\n留腹量
    @Column(name = "abdTime")
    public int abdTime; // 留腹\n时间
    @Column(name = "drainTvVol")
    public int drainTvVol; // 引流\n目标值
    @Column(name = "drainage")
    public int drainage; // 引流量
    @Column(name = "drainTime")
    public int drainTime; // 引流\n时间
    @Column(name = "remain")
    public int remain; // 预估腹腔\n剩余液体


}
