package com.emt.pdgo.next.net.bean;

public class TreatAssessRequestBean {

    private String machineSn;
    private String startTime;
    private String endTime;
    private String dailyLifeType;
    private String drainageClarity;
    private String unusualType;

    public String getMachineSn() {
        return machineSn;
    }

    public void setMachineSn(String machineSn) {
        this.machineSn = machineSn;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDailyLifeType() {
        return dailyLifeType;
    }

    public void setDailyLifeType(String dailyLifeType) {
        this.dailyLifeType = dailyLifeType;
    }

    public String getDrainageClarity() {
        return drainageClarity;
    }

    public void setDrainageClarity(String drainageClarity) {
        this.drainageClarity = drainageClarity;
    }

    public String getUnusualType() {
        return unusualType;
    }

    public void setUnusualType(String unusualType) {
        this.unusualType = unusualType;
    }
}
