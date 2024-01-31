package com.emt.pdgo.next.data.serial.treatment;

/**
 * 1、留腹过程开始
 */
public class ReceiveRetainProcessStartBean {
//{"data":{"cycle":1.0,"ultraFiltration":0.0,"currentDrainEstimate":0.0,"nextPefuseEstimate":500.0,"expectRetainTime":900.0,"retainTime":0.0},"msg":"process start","topic":"treatment/retain"}
    public int cycle;
    public int ultraFiltration;//超滤量
    public int currentDrainEstimate;
    public int nextPefuseEstimate;
    public int expectRetainTime;//预计留腹时间
    public int retainTime;
}
