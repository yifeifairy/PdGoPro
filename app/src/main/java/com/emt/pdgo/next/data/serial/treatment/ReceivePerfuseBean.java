package com.emt.pdgo.next.data.serial.treatment;

/**
 * 治疗过程返回的灌注数据
 */
public class ReceivePerfuseBean {

    /**
     * 周期
     */
    public int cycle;
    /**
     * 当前周期目标灌注量（首次灌注量、周期灌注量、最末灌注量）
     */
    public int perfuseTarget;

    /**
     * 超滤量
     */
    public int retain;

}
