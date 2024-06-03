package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

public class DetailedBean implements Serializable {

    private static final long serialVersionUID = 7697291299275630885L;
    private int maxCycle;
    private String perfuse;

    /**
     * 安全阀状态
     */
    private String safe;

    /**
     * 补液阀状态
     */
    private String supply1;

    /**
     *  末袋补液阀状态
     */
    private String supply2;

    /**
     * 引流阀状态
     */
    private String drain;

    /**
     * 负压引流阀状态
     */
    private String vaccum;

//    /**
//     * 负压引流阀状态
//     */
//    public String neg;

    /**
     * 温度
     */
    private int temp;

    /**
     * 温度
     */
    private int isOverTemp;
    /**
     * t0
     */
    private int isT0Err;
    /**
     * t1
     */
    private int isT1Err;
    /**
     * t2
     */
    private int isT2Err;

    /**
     * 上位称数值
     */
    private int upper;

    /**
     * 下位称数值
     */
    private int lower;

    /**
     * 是否断电
     */
    private int isAcPowerIn;

    /**
     * 是否关机
     */
    private int batteryVoltage;

    /**
     * 电池电量
     */
    private int batteryLevel;

    /**
     * 1充电,0断电
     */
    private int chargeflag;

    private int dialysateInitialValue;
    private int lowFirstValue;

    public int getDialysateInitialValue() {
        return dialysateInitialValue;
    }

    public void setDialysateInitialValue(int dialysateInitialValue) {
        this.dialysateInitialValue = dialysateInitialValue;
    }

    public int getLowFirstValue() {
        return lowFirstValue;
    }

    public void setLowFirstValue(int lowFirstValue) {
        this.lowFirstValue = lowFirstValue;
    }

//    public int getAbdomenRetainingVolume() {
//        return abdomenRetainingVolume;
//    }
//
//    public void setAbdomenRetainingVolume(int abdomenRetainingVolume) {
//        this.abdomenRetainingVolume = abdomenRetainingVolume;
//    }

    private int currCycle;
    private int perTargetVolume;
    private int currCyclePerfusionVolume;
    private int retain;
    private int mSupplyTargetValue;
//    private int abdomenRetainingVolume;
    private int mSupplyTargetProtectionValue;
    private int supplyVol;
    private int drainTarget;
    private int currDrainVol;
    private int currRinseNum;
    private int currRinsePerfusionVolume;
    private String retainTime;

    private int stage;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getMaxCycle() {
        return maxCycle;
    }

    public void setMaxCycle(int maxCycle) {
        this.maxCycle = maxCycle;
    }

    public String getPerfuse() {
        return perfuse;
    }

    public void setPerfuse(String perfuse) {
        this.perfuse = perfuse;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public String getSupply1() {
        return supply1;
    }

    public void setSupply1(String supply1) {
        this.supply1 = supply1;
    }

    public String getSupply2() {
        return supply2;
    }

    public void setSupply2(String supply2) {
        this.supply2 = supply2;
    }

    public String getDrain() {
        return drain;
    }

    public void setDrain(String drain) {
        this.drain = drain;
    }

    public String getVaccum() {
        return vaccum;
    }

    public void setVaccum(String vaccum) {
        this.vaccum = vaccum;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getIsOverTemp() {
        return isOverTemp;
    }

    public void setIsOverTemp(int isOverTemp) {
        this.isOverTemp = isOverTemp;
    }

    public int getIsT0Err() {
        return isT0Err;
    }

    public void setIsT0Err(int isT0Err) {
        this.isT0Err = isT0Err;
    }

    public int getIsT1Err() {
        return isT1Err;
    }

    public void setIsT1Err(int isT1Err) {
        this.isT1Err = isT1Err;
    }

    public int getIsT2Err() {
        return isT2Err;
    }

    public void setIsT2Err(int isT2Err) {
        this.isT2Err = isT2Err;
    }

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getIsAcPowerIn() {
        return isAcPowerIn;
    }

    public void setIsAcPowerIn(int isAcPowerIn) {
        this.isAcPowerIn = isAcPowerIn;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getChargeflag() {
        return chargeflag;
    }

    public void setChargeflag(int chargeflag) {
        this.chargeflag = chargeflag;
    }

    public int getCurrCycle() {
        return currCycle;
    }

    public void setCurrCycle(int currCycle) {
        this.currCycle = currCycle;
    }

    public int getPerTargetVolume() {
        return perTargetVolume;
    }

    public void setPerTargetVolume(int perTargetVolume) {
        this.perTargetVolume = perTargetVolume;
    }

    public int getCurrCyclePerfusionVolume() {
        return currCyclePerfusionVolume;
    }

    public void setCurrCyclePerfusionVolume(int currCyclePerfusionVolume) {
        this.currCyclePerfusionVolume = currCyclePerfusionVolume;
    }

    public int getRetain() {
        return retain;
    }

    public void setRetain(int retain) {
        this.retain = retain;
    }

    public int getmSupplyTargetValue() {
        return mSupplyTargetValue;
    }

    public void setmSupplyTargetValue(int mSupplyTargetValue) {
        this.mSupplyTargetValue = mSupplyTargetValue;
    }

//    public int getAbdomenRetainingVolume() {
//        return abdomenRetainingVolume;
//    }
//
//    public void setAbdomenRetainingVolume(int abdomenRetainingVolume) {
//        this.abdomenRetainingVolume = abdomenRetainingVolume;
//    }

    public int getmSupplyTargetProtectionValue() {
        return mSupplyTargetProtectionValue;
    }

    public void setmSupplyTargetProtectionValue(int mSupplyTargetProtectionValue) {
        this.mSupplyTargetProtectionValue = mSupplyTargetProtectionValue;
    }

    public int getSupplyVol() {
        return supplyVol;
    }

    public void setSupplyVol(int supplyVol) {
        this.supplyVol = supplyVol;
    }

    public int getDrainTarget() {
        return drainTarget;
    }

    public void setDrainTarget(int drainTarget) {
        this.drainTarget = drainTarget;
    }

    public int getCurrDrainVol() {
        return currDrainVol;
    }

    public void setCurrDrainVol(int currDrainVol) {
        this.currDrainVol = currDrainVol;
    }

    public int getCurrRinseNum() {
        return currRinseNum;
    }

    public void setCurrRinseNum(int currRinseNum) {
        this.currRinseNum = currRinseNum;
    }

    public int getCurrRinsePerfusionVolume() {
        return currRinsePerfusionVolume;
    }

    public void setCurrRinsePerfusionVolume(int currRinsePerfusionVolume) {
        this.currRinsePerfusionVolume = currRinsePerfusionVolume;
    }

    public String getRetainTime() {
        return retainTime;
    }

    public void setRetainTime(String retainTime) {
        this.retainTime = retainTime;
    }
}
