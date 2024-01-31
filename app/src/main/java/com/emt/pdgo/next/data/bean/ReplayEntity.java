package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

public class ReplayEntity extends BaseBean implements Serializable {

        private static final long serialVersionUID = 1947121831372198899L;

        public String startTime = "1999-12-31 23:59:59";
        public String endTime = "1999-12-31 23:59:59";
        public int drainageTime = 0;
        public int totalPerfusionVolume = 0;
        public int totalDrainVolume;
        public int totalUltrafiltrationVolume;

        public int maxCycle;
        public String cycle;
        public String inFlow;
        public String inFlowTime;
        public String leaveWombTime;
        public String exhaustTime;
        public String drainage;
        public String auxiliaryFlushingVolume;
        public String abdominalVolumeAfterInflow;
        public String drainageTargetValue;
        public String estimatedResidualAbdominalFluid;


}
