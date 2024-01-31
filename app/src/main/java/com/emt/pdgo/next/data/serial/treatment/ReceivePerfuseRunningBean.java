package com.emt.pdgo.next.data.serial.treatment;

/**
 * 治疗过程返回的实时灌注数据
 */
public class ReceivePerfuseRunningBean {

    /*** 当前周期数 */
    public int cycle;
    /*** 实时灌注量 */
    public int perfuse;
    /*** 实时留腹量 */
    public int retain;
    /*** 灌注目标值 */
    public int perfuseTarget;
}
