package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

public class PidBean implements Serializable {

    private static final long serialVersionUID = 3563869406878233926L;

    public float const_p1 = 0.3f;
    public float const_i1 = 0.2f;
    public float const_d1 = 0;
    public float const_p2 = 0.3f;
    public float const_i2 = 0.2f;
    public float const_d2 = 0f;
//    public double saturate_bw = 20;

    public float saturate_bw1 = 15f; // 上位阀速度误差阈值
    public float saturate_bw2 = 15f; // 下位阀速度误差阈值


//    "const_p1":0.3,   //灌注控制比例常数
//            "const_i1":0.2,    //灌注控制微分常数
//            "const_d1":0,    //灌注控制积分常数
//            "const_p2":0.3,   //引流控制比例常数
//            "const_i2":0.2,   //引流控制微分常数
//            "const_d2":0,    //引流控制积分常数
//            "saturate_bw":20     //控制死区速度}}

}
