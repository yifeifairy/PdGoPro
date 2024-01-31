package com.emt.pdgo.next.model.dpr;

public class PidParam {

    public int const_p1; // 上位阀门PID比例常数
    public int const_i1; // 上位阀门PID微分常数
//    public int const_d1;
    public int const_p2; // 下位阀门PID比例常数
    public int const_i2; // 下位阀门PID微分常数
//    public int const_d2;
    public int saturate_bw1; // 上位阀速度误差阈值
    public int saturate_bw2; // 下位阀速度误差阈值
    public int modify;

}
