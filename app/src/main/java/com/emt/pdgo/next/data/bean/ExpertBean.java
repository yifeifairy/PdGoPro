package com.emt.pdgo.next.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpertBean implements Serializable {
    private static final long serialVersionUID = -2174524275147639161L;

    public int total = 10000;
    public int cycleVol = 1000;
    public int cycle = 10;
    public int firstVol = 0;
    public int retainTime = 60;
    public int finalRetainVol = 0;
    public boolean isFinalSupply;
    public boolean cycleMyself;
    public int lastRetainVol = 0;
    public int finalSupply = 0;
    public int ultVol = 0;
    public int baseSupplyVol = 50;
    public List<Integer> baseSupplyCycle = new ArrayList<>();
    public int osmSupplyVol = 50;

    public List<Integer> osmSupplyCycle = new ArrayList<>();

}
