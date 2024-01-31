package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

/**
 * 用户参数bean
 * @author chenjh
 * @date 2019-08-27 10:00
 *
 */
public class UserParameterBean extends BaseBean implements Serializable {


    private static final long serialVersionUID = 7403053551029396063L;
    /*** 用户类型：true医院；false个人用户 **/
    public boolean isHospital = false;
    /*** 是否配方建议：true启用，false关闭 **/
    public boolean formulaRrecommended = false;
    /*** 体重差选择1 默认1   1-5 **/
    public float underweight1 = 1f;
    /*** 体重差选择2 默认2   1-5 **/
    public float underweight2 = 2f;
    /*** 体重差选择3 默认3.6 1-5 **/
    public float underweight3 = 3.6f;
    /*** 是否暗黑模式：true启用，false关闭 **/
    public boolean isNight = false;

    public boolean isAutoNight = false;

    public int countdownTimer = 240;

    public int awaken = 0;

}
