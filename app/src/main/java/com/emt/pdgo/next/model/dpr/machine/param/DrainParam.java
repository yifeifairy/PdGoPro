package com.emt.pdgo.next.model.dpr.machine.param;

import java.io.Serializable;

public class DrainParam implements Serializable {

    private static final long serialVersionUID = -8322881121530858101L;
    public int drainFlowRate = 10;
    public int drainFlowPeriod = 10;
    public int drainPassRate = 10;
    public boolean isFinalDrainEmpty = false;
    public boolean isFinalDrainEmptyWait = false;
    public int finalDrainEmptyWaitTime = 10;
    public boolean isVaccumDrain = false;
    public int VaccumDraintimes = 10;

}
