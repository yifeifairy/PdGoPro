package com.emt.pdgo.next.data.local;

import com.emt.pdgo.next.data.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 温控板设置参数表
 *
 * @author chenjh
 * @date 2019/3/5 09:15
 */
@Table(database =AppDatabase.class)
public class TemperatureControlParameter extends BaseModel {


    @PrimaryKey(autoincrement = true)
    @Column
    public int id;
    
    //最大温差 0.5<=T1<=2
    @Column(name = "maxDifference")
    public float maxDifference = 1.0f;

    //目标温度 34<=T2<=40
    @Column(name = "targetTemper")
    public float targetTemper = 37.5f;

    //上下回差 0<=T3<=1
    @Column(name = "upLowDifference")
    public float upLowDifference = 0f;

    //报警温度 T4=40.5 不可调
    @Column(name = "alarmTemper")
    public float alarmTemper = 40.5f;

    //加热板温度上限 40<=T5<=60
    @Column(name = "maxHeatPlate")
    public float maxHeatPlate = 55.5f;

    //目标温度差 0<=T6<=2
    @Column(name = "targetDifference")
    public float targetDifference = 0.5f;

    //加热板温度调低值 1<=T7<=10
    @Column(name = "lowHeatPlate")
    public float lowHeatPlate = 3.5f;

    //温度校正值 TaE TbE TcE -2<=TxE<=2
    @Column(name = "adjustTemperTA")
    public float adjustTemperTA = 0f;

    //温度校正值 TaE TbE TcE -2<=TxE<=2
    @Column(name = "adjustTemperTB")
    public float adjustTemperTB = 0f;

    //温度校正值 TaE TbE TcE -2<=TxE<=2
    @Column(name = "adjustTemperTC")
    public float adjustTemperTC = 0f;

    //加热温控值 35<= T<=40 每0.5一格
    @Column(name = "heatTemperControl")
    public float heatTemperControl = 37.5f;

    @Override
    public String toString() {
        return "TemperatureControlParameter{" +
                "id=" + id +
                ", maxDifference=" + maxDifference +
                ", upLowDifference=" + upLowDifference +
                ", maxHeatPlate=" + maxHeatPlate +
                ", lowHeatPlate=" + lowHeatPlate +
                ", targetTemper=" + targetTemper +
                ", alarmTemper=" + alarmTemper +
                ", targetDifference=" + targetDifference +
                ", adjustTemperTA=" + adjustTemperTA +
                ", adjustTemperTB=" + adjustTemperTB +
                ", adjustTemperTC=" + adjustTemperTC +
                ", heatTemperControl=" + heatTemperControl +
                '}';
    }

}
