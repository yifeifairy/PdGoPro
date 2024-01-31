package com.emt.pdgo.next.data.serial.treatment;

public class SerialTreatmentPrescriptBean {

    /** 处方总灌注量 */
    public int totalVolume;
    /** 循环周期数 */
    public int cycle;
    /** 首次灌注量 */
    public int firstPerfuseVolume;
    /** 循环周期灌注量 */
    public int cyclePerfuseVolume;
    /** 上次最末留腹量 */
    public int lastRetainVolume;
    /** 末次留腹量 */
    public int finalRetainVolume;
    /** 留腹时间 */
    public int retainTime;
    /** 预估超滤量 */
    public int ultraFiltrationVolume;

    /** 标志 0治疗前设置;1 治疗中修改 */
    public int apdmodify;

}
