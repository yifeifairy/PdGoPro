package com.emt.pdgo.next.data.local;

import com.emt.pdgo.next.data.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 历史处方
 */
@Table(database = AppDatabase.class)
public class HisRx extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String time;

    @Column
    public int perVol; // 腹透液总量

    @Column
    public int perCycleVol; // 每周期灌注量

    @Column
    public int treatCycle; // 循环治疗周期数

    @Column
    public int firstPerVol; // TPD首次灌注量

    @Column
    public int abdTime; // 留腹时间

    @Column
    public int endAbdVol; // 最末留腹量

    @Column
    public int lastTimeAbdVol; // 上次留腹量

    @Column
    public int ult; // 本次预估超滤

    @Column
    public String ulTreatTime; // 预估治疗时间
}
