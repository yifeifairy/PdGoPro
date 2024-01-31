package com.emt.pdgo.next.data.local;

import com.emt.pdgo.next.data.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.data.local
 * @ClassName: 治疗历史数据
 * @Description: java类作用描述
 * @Author: chenjh
 * @CreateDate: 2020/1/14 2:56 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/14 2:56 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
@Table(database = AppDatabase.class)
public class TreatmentHistory extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    /**
     * 设备SN码
     */
    @Column(name = "sn")
    public String sn;//sn

    /**
     * 用户手机号可以作为外键
     */
    @Column(name = "phone")
    public String phone;//用户手机号可以作为外键

    @Column(name = "uuid")
    public String uuid;//

    @Column(name = "cycle")
    public int cycle;//当前周期数

    @Column(name = "startTime")
    public String startTime;//当前周期治疗的开始时间

    @Column(name = "endTime")
    public String endTime;//当前周期治疗的结束时间

    @Column(name = "perfusionVolume")
    public int perfusionVolume;//灌注量

    @Column(name = "perfusionTime")
    public int perfusionTime;//灌注时间

    @Column(name = "waitingVolume")
    public int waitingVolume;//留腹量

    @Column(name = "waitingTime")
    public int waitingTime;//实际留腹的时间秒

    @Column(name = "drainVolume")
    public int drainVolume;//引流量

    @Column(name = "drainTime")
    public int drainTime;//引流时间

    @Column(name = "auFvVol")
    public int auFvVol;// 辅助冲洗量

    @Column(name = "abdAfterVol")
    public int abdAfterVol;//灌注后\n留腹量

    @Column(name = "drainTvVol")
    public int drainTvVol;//引流\n目标值

    @Column(name = "remain")
    public int remain;// 预估腹腔\n剩余液体

    @Column(name = "totalPerfusion")
    public int totalPerfusion; // 总灌注量

    @Column(name = "totalDrain")
    public int totalDrain; // 总引流量

    @Column(name = "totalUv")
    public int totalUv; // 累计超滤量

}
