package com.emt.pdgo.next.data.bean;

import java.util.List;

public class PdData {

    private String startTime;
    private String endTime;
    private int totalDarinVol;
    private int totalPerVol;
    private int ultVol;
    private String durationTime;
    private int totalAbdTime;

    private int finalTime;


    private int rxCycle;

    public int getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(int finalTime) {
        this.finalTime = finalTime;
    }

    public int getRxCycle() {
        return rxCycle;
    }

    public void setRxCycle(int rxCycle) {
        this.rxCycle = rxCycle;
    }

    public int getTotalAbdTime() {
        return totalAbdTime;
    }

    public void setTotalAbdTime(int totalAbdTime) {
        this.totalAbdTime = totalAbdTime;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public int getTotalDarinVol() {
        return totalDarinVol;
    }

    public void setTotalDarinVol(int totalDarinVol) {
        this.totalDarinVol = totalDarinVol;
    }

    public int getTotalPerVol() {
        return totalPerVol;
    }

    public void setTotalPerVol(int totalPerVol) {
        this.totalPerVol = totalPerVol;
    }

    public int getUltVol() {
        return ultVol;
    }

    public void setUltVol(int ultVol) {
        this.ultVol = ultVol;
    }

    private List<PdEntityData> pdEntityDataList;

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

    public List<PdEntityData> getPdEntityDataList() {
        return pdEntityDataList;
    }

    public void setPdEntityDataList(List<PdEntityData> pdEntityDataList) {
        this.pdEntityDataList = pdEntityDataList;
    }

    public static class PdEntityData {
        private int cycle;  //当前周期数
        private int preVol; // 灌注量
        private int preTime; // 灌注时间
        private int ufVol; // 辅助冲洗量

        private int abdVol; // 灌注后\n留腹量

        private int abdTime; // 留腹\n时间

        private int drainTargetVol; // 引流\n目标值

        private int drainage; // 引流量

        private int drainTime; // 引流\n时间

        private int estimate; // 预估剩余液体

        public int getEstimate() {
            return estimate;
        }

        public void setEstimate(int estimate) {
            this.estimate = estimate;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public int getPreVol() {
            return preVol;
        }

        public void setPreVol(int preVol) {
            this.preVol = preVol;
        }

        public int getPreTime() {
            return preTime;
        }

        public void setPreTime(int preTime) {
            this.preTime = preTime;
        }

        public int getUfVol() {
            return ufVol;
        }

        public void setUfVol(int ufVol) {
            this.ufVol = ufVol;
        }

        public int getAbdVol() {
            return abdVol;
        }

        public void setAbdVol(int abdVol) {
            this.abdVol = abdVol;
        }

        public int getAbdTime() {
            return abdTime;
        }

        public void setAbdTime(int abdTime) {
            this.abdTime = abdTime;
        }

        public int getDrainTargetVol() {
            return drainTargetVol;
        }

        public void setDrainTargetVol(int drainTargetVol) {
            this.drainTargetVol = drainTargetVol;
        }

        public int getDrainage() {
            return drainage;
        }

        public void setDrainage(int drainage) {
            this.drainage = drainage;
        }

        public int getDrainTime() {
            return drainTime;
        }

        public void setDrainTime(int drainTime) {
            this.drainTime = drainTime;
        }
    }
}
