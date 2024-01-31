package com.emt.pdgo.next.net.bean.upload;


import com.emt.pdgo.next.data.bean.BaseBean;

/**
 * 上传的治疗处方数据
 *
 * @author chenjh
 * @date 2019/3/18 13:31
 */
public class TreatmentPrescriptionUploadBean extends BaseBean {


    /***  99为区分，后面11位为手机号 **/
    public String phone = "9910000000000";

    /***  建议体重 **/
    public float recommendedWeight;//1-500
    /***  治疗前体重  **/
    public float weightBeforeTreatment;//1-500
    /***  治疗前收缩压  **/
    public int SBPBeforeTreatment;//血压 收缩压 50-220 (应大于舒张压）
    /***  治疗前舒张压  **/
    public int DBPBeforeTreatment;//血压 舒张压 30-150

    /***  治疗后体重  **/
    public float weightAfterTreatment;//1-500
    /***  治疗后收缩压  **/
    public int SBPAfterTreatment;//血压 收缩压 50-220 (应大于舒张压）
    /***  治疗后舒张压  **/
    public int DBPAfterTreatment;//血压 舒张压 30-150
    /***  空腹血糖  **/
    public float fastingBloodGlucose;
    /***  尿量 ml  **/
    public int urineOutput;
    /***  饮水量 ml  **/
    public int waterIntake;


    /***  总灌注量 0 - 20000  **/
    public int totalPerfusionVolume;
    /***  总超滤量 -10000～10000  **/
    public int totalUltrafiltrationVolume;
    /***  处方：总治疗量 0～20000  **/
    public int peritonealDialysisFluidTotal;
    /***  处方：周期数 1～13  **/
    public int periodicities;
    /***  处方：首次灌注量 0～5000  **/
    public int firstPerfusionVolume = 0;
    /***  处方：周期灌注量 0～5000  **/
    public int perCyclePerfusionVolume;
    /***  处方：最末留腹量 0～5000  **/
    public int abdomenRetainingVolumeFinally = 0;
    /***  处方：留腹时间 0～600  **/
    public int abdomenRetainingTime = 1;
    /***  处方：上次最末留腹量 0～5000  **/
    public int abdomenRetainingVolumeLastTime = 0;
    /***  处方：预估超滤量 0～5000  **/
    public int ultrafiltrationVolume = 0;


    /***  腹透液浓度： 1/2/3=1.5%/2.5%/4.25% **/
    public int dialyzateConcentration;
    /***  腹透液钙类别： 1/2=普通钙/低钙 **/
    public int dialyzateCalciumCategory;
    /***  是否腹痛  **/
    public int isBellyache;
    /***  是否引流液浑浊  **/
    public int isDrainageOpacitas;
    /***  是否隧道口疼痛或红肿  **/
    public int isTunnelPortalPainOrSwelling;


    /***  设备使用时间   **/
    public int equipmentUseTime = 0;
    /***  自检的上位秤数据   **/
    public int upWeightInitialValue = 0;
    /***  自检的下位秤数据   **/
    public int lowWeightInitialValue = 0;

    /***  PLC程序版本号ymmdd,20331表示16年03月31日版本  **/
    public String plcId = "";
    /***  程序版本号 11 ,0或11～99  **/
    public int buildId = 0;
    /***  程序build版本号 ：build-yyyymmdd+X  201801008（尾数会变异） **/
    public String buildValue = "";


}
