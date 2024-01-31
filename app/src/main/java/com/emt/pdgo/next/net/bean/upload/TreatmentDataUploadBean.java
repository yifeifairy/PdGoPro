package com.emt.pdgo.next.net.bean.upload;



/**
 * 上传的治疗数据
 *
 * @author chenjh
 * @date 2019/3/18 10:41
 */
public class TreatmentDataUploadBean  {


    /*** 机器序列号 ："PD00019C"，“P”字母开头+一个表示型号的字母 **/
    public String machineSN;

    /*** 透析总使用液体量 **/
    public int totalInjectAmount;

    /*** 开始治疗时间 yyyy-mm-dd hh:mm:ss **/
    public String startTime;
    /*** 治疗结束时间 yyyy-mm-dd hh:mm:ss **/
    public String endTime;

    /*** 超滤量 **/
    public int ultrafiltration;
    /*** 最末留腹量 **/
    public int lastLeave;
    /*** 最末留腹灌注所花费时间 **/
    public int lastLeaveTime;
    /*** 0周期引流所花费时间 **/
    public int drainageTime;

    /*** 治疗周期数 **/
    public int times;
    /*** 治疗周期顺序编号 ："1,2,3,4,5" **/
    public String cycle;
    /*** 每个周期灌注的量（包括首次灌注，不包括末次灌注量）："2000,1000,1000,1000,1000" **/
    public String inFlow;
    /*** 每个周期灌注所花费的时间（包括首次灌注，不包括末次灌注）："21,13,11,14,15" **/
    public String inFlowTime;
    /*** 每个周期留腹的时间 ："30,30,30,30,30" **/
    public String leaveWombTime;
    /*** 每个周期引流所花费时间（不包含0周期）： "11,16,21,18,22" **/
    public String exhaustTime;
    /*** 每个周期引流量（包含0周期）："1600,1400,950,860,1130,1760" **/
    public String drainage;

    /**  每个周期辅助冲洗量:1600,1400,950,860,1130 **/
    public String auxiliaryFlushingVolume;
    /**  每个周期灌注后留腹量:1600,1400,950,860,1130 **/
    public String abdominalVolumeAfterInflow;
    /**  每个周期引流目标值:1600,1400,950,860,1130 **/
    public String drainageTargetValue;
    /**  每个周期预估腹腔剩余液体:1600,1400,950,860,1130 **/
    public String estimatedResidualAbdominalFluid;

    public TreatmentPrescriptionUploadBean  treatmentPrescriptionUploadVo;

//    /*** "9913981709377,60,65,80,120,64,75,110,4.3,1200,1200, 8000,2000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"
//     * 总灌注量,-154总超滤量,12000处方：总治疗量,5处方：周期数,0处方：首次灌注量,2000处方：周期灌注量,2000处方：最末留腹量,90处方：留腹时间,0处方：上次最末留腹量,
//     * 0处方：预估超滤量,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,30设备使用时间,2267自检的上位秤数据,3330自检的下位秤数据,40101PLC程序版本号,
//     * 11程序版本号,201801008程序build版本号 **/
//    public String barcode;

//    public List<TreatmentCycleDataBean> cycleDataList;


}
