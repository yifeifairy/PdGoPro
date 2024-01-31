package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

/**
 * 预冲参数bean
 *
 * @author chenjh
 * @date 2019/1/24 13:45
 */
public class FirstRinseParameterBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = -599263213583189457L;

    public int firstvolume = 50;
    public int secondvolume = 50;
    public int supplyperiod = 60;
    public int supplyspeed = 30;
    public int supplyselect = 1;
//    /*** 排空时间 5 s  **/
//    public int emptyingTime = 8;
//    /*** 预设增量1[上位秤增加重量X1克] g 范围 50-100 **/
//    public int presetWeight = 20;//300
//    /*** 预设增量2[下位秤增加重量X1克] g 范围 （预设增量1 * 80%） **/
//    public int presetWeight2 = 30;//240
//    /*** 预设减量 [上位秤减少重量X1克] g 范围 50-100 **/
//    public int presetWeightLoss = 30;//50
//
//    public int supply_rate = 6;

}
