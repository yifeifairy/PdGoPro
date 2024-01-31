package com.emt.pdgo.next.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CycleBean implements Serializable {
    private static final long serialVersionUID = -5756933774853816227L;
    public List<Integer> baseSupplyCycle = new ArrayList<>();
    public List<Integer> osmSupplyCycle = new ArrayList<>();
}
