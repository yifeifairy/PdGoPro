package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

/**
 * 补液参数bean
 *
 * @author chenjh
 * @date 2019/1/24 13:47
 */
public class SupplyParameterBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = -2362493370325306662L;

//    /*** 流量测定时间间隔 s 60- 600 **/
//    public int timeInterval = 60;
//    /*** 流量测定 阈值 g 30-200 **/
//    public int thresholdValue = 30;
//    /*** 补液目标保护值 g 0-500 **/
//    public int targetProtectionValue = 500;
//    /*** 启动补液的加热袋重量最低值 g 500-10000 **/
//    public int minWeight = 500;

    /*** 流量测定时间间隔 s 60- 600 **/
    public int supplyTimeInterval = 30;
    /*** 流量测定 阈值 g 30-200 **/
    public int supplyThresholdValue = 30;
    /*** 补液目标保护值 g 0-500 **/
    public int supplyTargetProtectionValue = 500;
    /*** 启动补液的加热袋重量最低值 g 500-10000 **/
    public int supplyMinWeight = 500;


}
