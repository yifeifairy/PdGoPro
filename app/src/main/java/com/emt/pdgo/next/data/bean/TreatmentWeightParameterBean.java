package com.emt.pdgo.next.data.bean;

/**
 * 治疗之前体重参数
 *
 * @author chenjh
 * @date 2019/2/2 11:18
 */
public class TreatmentWeightParameterBean extends BaseBean {

    /**
     * 建议体重
     */
    public float recommendedWeight;//1-500
    /**
     * 体重
     */
    public float weight;//1-500
    /**
     * 舒张压
     */
    public int DBP;//血压 舒张压 30-150
    /**
     * 收缩压
     */
    public int SBP;//血压 收缩压 50-220 (应大于舒张压）

}
