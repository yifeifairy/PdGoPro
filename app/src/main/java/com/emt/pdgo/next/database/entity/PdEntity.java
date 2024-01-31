package com.emt.pdgo.next.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.emt.pdgo.next.database.converter.PdInfoConverter;

import java.util.List;

@TypeConverters(PdInfoConverter.class)
@Entity
public class PdEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String startTime;
    public String endTime;

    public int totalDrainVol;
    public int totalPerVol;
    public int totalUltVol;
    public int cycle;
    public String phone;
    /**
     * 0周期首次引流时间
     */
    public int firstTime;
    public int totalVol;
    public int finalAbdTime;

    /***  处方：首次灌注量 0～5000  **/
    public int firstPerfusionVolume;
    /***  处方：周期灌注量 0～5000  **/
    public int perCyclePerfusionVolume;
    /***  处方：最末留腹量 0～5000  **/
    public int abdomenRetainingVolumeFinally;
    /***  处方：留腹时间 0～600  **/
    public int abdomenRetainingTime ;
    /***  处方：上次最末留腹量 0～5000  **/
    public int abdomenRetainingVolumeLastTime;
    /***  处方：预估超滤量 0～5000  **/
    public int ultrafiltrationVolume;

    public List<PdInfoEntity> pdInfoEntities;

    @Entity
    public static class PdInfoEntity {

        @PrimaryKey(autoGenerate = true)
        public int id;

        public int cycle;//当前周期数


        public int preVol; // 灌注量

        public int preTime; // 灌注时间


        public int auFvVol; // 辅助冲洗量

        public int abdAfterVol; // 灌注后\n留腹量

        public int abdTime; // 留腹\n时间

        public int drainTvVol; // 引流\n目标值

        public int drainage; // 引流量

        public int drainTime; // 引流\n时间

        public int remain; // 预估腹腔\n剩余液体

    }

}
