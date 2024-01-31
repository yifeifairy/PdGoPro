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
public class TreatmentHistorySummary extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    /**
     * 是否上传成功
     */
    @Column(name = "isUpload")
    public boolean isUpload;

    /**
     * 设备SN码
     */
    @Column(name = "sn")
    public String sn;//sn

    /**
     * 用户名称
     */
    @Column(name = "username")
    public String username;//username

    /**
     * 用户手机号可以作为外键
     */
    @Column(name = "phone")
    public String phone;//用户手机号可以作为外键

    @Column(name = "uuid")
    public String uuid;//

    /*** 开始治疗时间 yyyy-mm-dd hh:mm:ss **/
    @Column(name = "startTime")
    public String startTime;//

    /*** 治疗结束时间 yyyy-mm-dd hh:mm:ss **/
    @Column(name = "endTime")
    public String endTime;

    @Column(name = "num")
    public int num;//次数

    @Column(name = "totalDrainVolume")
    public int totalDrainVolume;//总引流量

    @Column(name = "totalPerfusionVolume")
    public int totalPerfusionVolume;//总灌注量

    @Column(name = "totalUltrafiltrationVolume")
    public int totalUltrafiltrationVolume;//总超滤量

    @Column(name = "uploadContent")
    public String uploadContent;//上传的内容

}
