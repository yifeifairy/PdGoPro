package com.emt.pdgo.next.data.local;


import com.emt.pdgo.next.data.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Administrator on 2016/11/18.
 * 主板命令设置参数表
 */

@Table(database =AppDatabase.class)
public class MainBoardOrder extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    @Column(name = "upMax")
    public int upMax=5000;

    @Column(name = "upMin")
    public int upMin=0;

    @Column(name = "lowMax")
    public int lowMax=5000;

    @Column(name = "lowMin")
    public int lowMin=0;


    // A 无延长管 B有延长管 默认是无延长管
    @Column(name = "pipType")
    public String pipType="A";

    // 自动 auto 手动manual
    @Column(name = "rinseType")
    public String rinseType="auto";

    //上位秤减少 xg后灌注阀关闭 默认100
    @Column(name = "floodValveWeight")
    public int floodValveWeight=100;

    //下位秤增加 xg后引流阀关闭 默认100
    @Column(name = "drainValveWeight")
    public int drainValveWeight=100;

    //腹透液总量
    @Column(name = "dialysisValue")
    public int dialysisValue=1800;

    //引流停止判断 引流停止判断值(输入参数1、2、3(1:30/1min 2:75/2min 3:100/5min))
    @Column(name = "drainStopValue")
    public int drainStopValue=1;

    //上次留腹量
    @Column(name = "lastLeftValue")
    public int lastLeftValue=2000;

    //超滤目标
    @Column(name = "beyondValue")
    public int beyondValue=200;

    //引流最低比例 30%-120% 默认60%
    @Column(name = "drainMinRatio")
    public int drainMinRatio=60;

    //灌注最低比例 30%-120% 默认60%
    @Column(name = "floodMinRatio")
    public int floodMinRatio=60;

    //达不到最低引流比例 1 报警 0 完成引流
    @Column(name = "notToDrainMinRatioType")
    public int notToDrainMinRatioType=1;

    /**
     * 灌注流量时间默认是10秒
     */
    @Column(name = "floodFlowTime")
    public int floodFlowTime=10;
    /**
     * 灌注流量重量默认是100g
     */
    @Column(name = "floodFlowWeight")
    public int floodFlowWeight=100;

    //灌注流量 默认是100g/min 单位统一为分钟
    @Column(name = "floodFlow")
    public int floodFlow=100;

    //治疗定时设置
    @Column(name = "treatmentTime")
    public int treatmentTime=0;

    //闹钟定时设置
    @Column(name = "alarmTime")
    public int alarmTime=0;

    //上位秤系数值 默认是101.147*1000
    @Column(name = "upWeigherRatio")
    public int upWeigherRatio=60560;

    //下位秤系数值 默认是101.147*1000
    @Column(name = "lowWeigherRatio")
    public int lowWeigherRatio=20600;

    @Override
    public String toString() {
        return "MainBoardOrder{" +
                "id=" + id +
                ", upMax=" + upMax +
                ", upMin=" + upMin +
                ", lowMax=" + lowMax +
                ", lowMin=" + lowMin +
                ", pipType='" + pipType + '\'' +
                ", rinseType='" + rinseType + '\'' +
                ", floodValveWeight=" + floodValveWeight +
                ", drainValveWeight=" + drainValveWeight +
                ", dialysisValue=" + dialysisValue +
                ", drainStopValue=" + drainStopValue +
                ", lastLeftValue=" + lastLeftValue +
                ", beyondValue=" + beyondValue +
                ", drainMinRatio=" + drainMinRatio +
                ", floodMinRatio=" + floodMinRatio +
                ", notToDrainMinRatioType=" + notToDrainMinRatioType +
                ", floodFlowTime=" + floodFlowTime +
                ", floodFlowWeight=" + floodFlowWeight +
                ", floodFlow=" + floodFlow +
                ", treatmentTime=" + treatmentTime +
                ", alarmTime=" + alarmTime +
                ", upWeigherRatio=" + upWeigherRatio +
                ", lowWeigherRatio=" + lowWeigherRatio +
                '}';
    }

}
