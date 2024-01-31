package com.emt.pdgo.next.net.bean;

import java.util.List;

public class HisPdListBean {


    private int total;
    private List<RowsDTO> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsDTO> getRows() {
        return rows;
    }

    public void setRows(List<RowsDTO> rows) {
        this.rows = rows;
    }

    public static class RowsDTO {
        private String icodextrinTotal;
        private String firstInFlow;
        private String predictUlt;
        private String lastRetention;
        private int patientId;
        private String createTime;
        private String treatTime;
        private String agoRetention;
        private String retentionTime;
        private String inFlowCycle;
        private String cycle;

        public String getIcodextrinTotal() {
            return icodextrinTotal;
        }

        public void setIcodextrinTotal(String icodextrinTotal) {
            this.icodextrinTotal = icodextrinTotal;
        }

        public String getFirstInFlow() {
            return firstInFlow;
        }

        public void setFirstInFlow(String firstInFlow) {
            this.firstInFlow = firstInFlow;
        }

        public String getPredictUlt() {
            return predictUlt;
        }

        public void setPredictUlt(String predictUlt) {
            this.predictUlt = predictUlt;
        }

        public String getLastRetention() {
            return lastRetention;
        }

        public void setLastRetention(String lastRetention) {
            this.lastRetention = lastRetention;
        }

        public int getPatientId() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId = patientId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTreatTime() {
            return treatTime;
        }

        public void setTreatTime(String treatTime) {
            this.treatTime = treatTime;
        }

        public String getAgoRetention() {
            return agoRetention;
        }

        public void setAgoRetention(String agoRetention) {
            this.agoRetention = agoRetention;
        }

        public String getRetentionTime() {
            return retentionTime;
        }

        public void setRetentionTime(String retentionTime) {
            this.retentionTime = retentionTime;
        }

        public String getInFlowCycle() {
            return inFlowCycle;
        }

        public void setInFlowCycle(String inFlowCycle) {
            this.inFlowCycle = inFlowCycle;
        }

        public String getCycle() {
            return cycle;
        }

        public void setCycle(String cycle) {
            this.cycle = cycle;
        }
    }
}
