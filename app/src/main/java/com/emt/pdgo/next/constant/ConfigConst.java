package com.emt.pdgo.next.constant;

public class ConfigConst {

    public static int peritonealDialysisFluidTotal = 2800;
    public static int perCyclePerfusionVolume = 500;
    public static int cycle = 4;
    public static int firstPerfusionVolume = 0;
    public static int abdomenRetainingTime = 15;
    public static int abdomenRetainingVolumeFinally = 0;
    public static int abdomenRetainingVolumeLastTime = 0;
    public static int ultrafiltrationVolume = 0;
    public static boolean isFinalSupply = false;

    public static float conMin = 0.5f;
    public static float conMax = 10f;

    // 预充参数
    public static int firstVolume = 50;
    public static int secondVolume = 50;
    public static int supplyPeriod = 60;
    public static int supplySpeed = 30;
    public static int supplyVol = 50;
    public static int supplySelect = 1;

    // 灌注参数
    public static int perfAllowAbdominalVolume = 1;
    public static int perfMaxWarningValue = 3000;
    public static int perfTimeInterval = 30;
    public static int perfThresholdValue = 30;
    public static int perfMinWeight = 100;

    // 引流参数
    public static int drainTimeInterval = 30;
    public static int drainThresholdValue = 30;
    public static int drainZeroCyclePercentage = 75;
    public static int drainOtherCyclePercentage = 75;
    public static int drainTimeoutAlarm = 45;
    public static int drainRinseVolume = 50;
    public static int drainRinseNumber = 2;
    public static boolean isDrainManualEmptying = true;
    public static int drainWarnTimeInterval = 60;

    // 刘福参数
    public static boolean isAbdomenRetainingDeduct = false;
    public static boolean isZeroCycleUltrafiltration = false;

    // 补液参数
    public static int supplyTimeInterval = 30;
    public static int supplyThresholdValue = 30;
    public static int supplyTargetProtectionValue = 500;
    public static int supplyMinWeight = 500;

}
