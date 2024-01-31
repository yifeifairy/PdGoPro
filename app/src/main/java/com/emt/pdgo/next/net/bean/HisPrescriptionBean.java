package com.emt.pdgo.next.net.bean;

public class HisPrescriptionBean {


    private String msg;
    private String code;
    private RcpInfoDTO rcpInfo;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RcpInfoDTO getRcpInfo() {
        return rcpInfo;
    }

    public void setRcpInfo(RcpInfoDTO rcpInfo) {
        this.rcpInfo = rcpInfo;
    }

    public static class RcpInfoDTO {
        private String id;
        private String machineSn;
        private int patientId;
        private int icodextrinTotal;
        private int inFlowCycle;
        private int cycle;
        private int firstInFlow;
        private int retentionTime;
        private String retentionTimeName;
        private int lastRetention;
        private Object agoRetention;
        private Object predictUlt;
        private String treatTime;
        private String createTime;
        private String userName;
        private int hospitalId;
        private String hospitalName;
        private String userPhone;
        private String treatmentType;
        private String treatmentTypeName;
        private String avgInFlow;
        private String allInFlow;
        private String targetUlt;
        private String peritoneal;
        private String peritonealName;
        private String recipelId;
        private String recipels;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMachineSn() {
            return machineSn;
        }

        public void setMachineSn(String machineSn) {
            this.machineSn = machineSn;
        }

        public int getPatientId() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId = patientId;
        }

        public int getIcodextrinTotal() {
            return icodextrinTotal;
        }

        public void setIcodextrinTotal(int icodextrinTotal) {
            this.icodextrinTotal = icodextrinTotal;
        }

        public int getInFlowCycle() {
            return inFlowCycle;
        }

        public void setInFlowCycle(int inFlowCycle) {
            this.inFlowCycle = inFlowCycle;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public int getFirstInFlow() {
            return firstInFlow;
        }

        public void setFirstInFlow(int firstInFlow) {
            this.firstInFlow = firstInFlow;
        }

        public int getRetentionTime() {
            return retentionTime;
        }

        public void setRetentionTime(int retentionTime) {
            this.retentionTime = retentionTime;
        }

        public String getRetentionTimeName() {
            return retentionTimeName;
        }

        public void setRetentionTimeName(String retentionTimeName) {
            this.retentionTimeName = retentionTimeName;
        }

        public int getLastRetention() {
            return lastRetention;
        }

        public void setLastRetention(int lastRetention) {
            this.lastRetention = lastRetention;
        }

        public Object getAgoRetention() {
            return agoRetention;
        }

        public void setAgoRetention(Object agoRetention) {
            this.agoRetention = agoRetention;
        }

        public Object getPredictUlt() {
            return predictUlt;
        }

        public void setPredictUlt(int predictUlt) {
            this.predictUlt = predictUlt;
        }

        public String getTreatTime() {
            return treatTime;
        }

        public void setTreatTime(String treatTime) {
            this.treatTime = treatTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getTreatmentType() {
            return treatmentType;
        }

        public void setTreatmentType(String treatmentType) {
            this.treatmentType = treatmentType;
        }

        public String getTreatmentTypeName() {
            return treatmentTypeName;
        }

        public void setTreatmentTypeName(String treatmentTypeName) {
            this.treatmentTypeName = treatmentTypeName;
        }

        public String getAvgInFlow() {
            return avgInFlow;
        }

        public void setAvgInFlow(String avgInFlow) {
            this.avgInFlow = avgInFlow;
        }

        public String getAllInFlow() {
            return allInFlow;
        }

        public void setAllInFlow(String allInFlow) {
            this.allInFlow = allInFlow;
        }

        public String getTargetUlt() {
            return targetUlt;
        }

        public void setTargetUlt(String targetUlt) {
            this.targetUlt = targetUlt;
        }

        public String getPeritoneal() {
            return peritoneal;
        }

        public void setPeritoneal(String peritoneal) {
            this.peritoneal = peritoneal;
        }

        public String getPeritonealName() {
            return peritonealName;
        }

        public void setPeritonealName(String peritonealName) {
            this.peritonealName = peritonealName;
        }

        public String getRecipelId() {
            return recipelId;
        }

        public void setRecipelId(String recipelId) {
            this.recipelId = recipelId;
        }

        public String getRecipels() {
            return recipels;
        }

        public void setRecipels(String recipels) {
            this.recipels = recipels;
        }
    }
}
