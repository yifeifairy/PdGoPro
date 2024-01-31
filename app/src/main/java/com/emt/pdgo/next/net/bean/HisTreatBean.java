package com.emt.pdgo.next.net.bean;

import java.util.List;

public class HisTreatBean extends ResponseBaseBean{

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
            private int curTimes;
            private String machineSn;
            private String startTime;
            private String drainage;
            private String endTime;
            private String ultrafiltration;
            private String inFlow;
            private List<InfoDTO> info;

            public int getCurTimes() {
                return curTimes;
            }

            public void setCurTimes(int curTimes) {
                this.curTimes = curTimes;
            }

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

            public String getDrainage() {
                return drainage;
            }

            public void setDrainage(String drainage) {
                this.drainage = drainage;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getUltrafiltration() {
                return ultrafiltration;
            }

            public void setUltrafiltration(String ultrafiltration) {
                this.ultrafiltration = ultrafiltration;
            }

            public String getInFlow() {
                return inFlow;
            }

            public void setInFlow(String inFlow) {
                this.inFlow = inFlow;
            }

            public List<InfoDTO> getInfo() {
                return info;
            }

            public void setInfo(List<InfoDTO> info) {
                this.info = info;
            }

            public static class InfoDTO {
                private String drainageTargetItems;
                private String leaveWombTimeItems;
                private String inFlowItem;
                private int curTime;
                private String abdominalItems;
                private String auxItems;
                private String drainageItems;
                private String exhaustTimeItems;
                private String inFlowTimeItem;
                private String estimatedItems;

                public String getDrainageTargetItems() {
                    return drainageTargetItems;
                }

                public void setDrainageTargetItems(String drainageTargetItems) {
                    this.drainageTargetItems = drainageTargetItems;
                }

                public String getLeaveWombTimeItems() {
                    return leaveWombTimeItems;
                }

                public void setLeaveWombTimeItems(String leaveWombTimeItems) {
                    this.leaveWombTimeItems = leaveWombTimeItems;
                }

                public String getInFlowItem() {
                    return inFlowItem;
                }

                public void setInFlowItem(String inFlowItem) {
                    this.inFlowItem = inFlowItem;
                }

                public int getCurTime() {
                    return curTime;
                }

                public void setCurTime(int curTime) {
                    this.curTime = curTime;
                }

                public String getAbdominalItems() {
                    return abdominalItems;
                }

                public void setAbdominalItems(String abdominalItems) {
                    this.abdominalItems = abdominalItems;
                }

                public String getAuxItems() {
                    return auxItems;
                }

                public void setAuxItems(String auxItems) {
                    this.auxItems = auxItems;
                }

                public String getDrainageItems() {
                    return drainageItems;
                }

                public void setDrainageItems(String drainageItems) {
                    this.drainageItems = drainageItems;
                }

                public String getExhaustTimeItems() {
                    return exhaustTimeItems;
                }

                public void setExhaustTimeItems(String exhaustTimeItems) {
                    this.exhaustTimeItems = exhaustTimeItems;
                }

                public String getInFlowTimeItem() {
                    return inFlowTimeItem;
                }

                public void setInFlowTimeItem(String inFlowTimeItem) {
                    this.inFlowTimeItem = inFlowTimeItem;
                }

                public String getEstimatedItems() {
                    return estimatedItems;
                }

                public void setEstimatedItems(String estimatedItems) {
                    this.estimatedItems = estimatedItems;
                }
            }
        }
}
