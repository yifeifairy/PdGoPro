package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

/**
 * 灌注参数bean
 *
 * @author chenjh
 * @date 2019/1/24 13:46
 */
public class PerfusionParameterBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 5571236012473176025L;

//    /*** 是否允许最末灌注减去留腹量 g **/
////    public int allowAbdominalVolume = 0; // 是0否1
//    public boolean allowAbdominalVolume = false; // 是0否1
//    /*** 灌注最大警戒值 g 1000-5000 **/
//    public int maxWarningValue = 5000;
//    /*** 流量测定时间间隔 s **/
//    public int timeInterval = 60;
//    /*** 流量测定 阈值 g **/
//    public int thresholdValue = 30;
//    /*** 加热袋重量最低值 g 100-1000 **/
//    public int minWeight = 100;

    public boolean perfAllowAbdominalVolume = false; // 是0否1
    /*** 灌注最大警戒值 g 1000-5000 **/
    public int perfMaxWarningValue = 3000;
    /*** 流量测定时间间隔 s **/
    public int perfTimeInterval = 60;
    /*** 流量测定 阈值 g **/
    public int perfThresholdValue = 30;
    /*** 加热袋重量最低值 g 100-1000 **/
    public int perfMinWeight = 100;

}
