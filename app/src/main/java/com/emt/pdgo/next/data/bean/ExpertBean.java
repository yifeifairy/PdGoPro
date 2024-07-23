package com.emt.pdgo.next.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpertBean implements Serializable {
    private static final long serialVersionUID = -2174524275147639161L;

    public int total = 1000;
    public int cycleVol = 300;
    public int cycle = 2;
    public int firstVol = 0;
    public int retainTime = 60;
    public int finalRetainVol = 0;
    public boolean isFinalSupply;
    public boolean cycleMyself = true;
    public int lastRetainVol = 0;
    public int ultVol = 0;

    public int baseSupplyVol = 500;
    public List<Integer> baseSupplyCycle = new ArrayList<>();
    public int osmSupplyVol = 500;
    public List<Integer> osmSupplyCycle = new ArrayList<>();

    public int finalSupply = 0;

    public double con_1 = 1.5;
    public double con_2 = 2.5;

}
