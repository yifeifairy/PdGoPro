package com.emt.pdgo.next.data.serial.treatment;

/**
 * 治疗过程返回的灌注故障
 */
public class ReceivePerfuseFailureBean {

    /**
     * 周期
     */
    public int cycle;
    /**
     * 当前周期实际灌注量
     */
    public int perfuse;
    /**
     * 超滤量
     */
    public int retain;
    /**
     * 流速
     */
    public int flowRate;

    public int perfuseTarget;
    
}
