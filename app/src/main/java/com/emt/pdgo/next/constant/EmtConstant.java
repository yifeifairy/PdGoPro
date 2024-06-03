package com.emt.pdgo.next.constant;

public class EmtConstant {

    public static final int dep = 250;

    public static final int upper = 0; // 1942
    public static final int lower = 0; // 2966

    public static final String version = "40603";

    public static final String JUMP_WITH_PARAM = "JUMP_WITH_PARAM";

    public static final String ACTIVITY_TREAT_MODE ="ACTIVITY_TREAT_MODE";
    public static final String ACTIVITY_ENGINE_SETTING = "ACTIVITY_ENGINE_SETTING";
    public static final String ACTIVITY_PRE_HEAT = "ACTIVITY_PRE_HEAT";
    public static final String ACTIVITY_PRESCRIPTION = "ACTIVITY_PRESCRIPTION";
    public static final String ACTIVITY_REMOTE_PRESCRIBING = "ACTIVITY_REMOTE_PRESCRIBING";
    public static final String ACTIVITY_PIPELINE_CONNECTION = "ACTIVITY_PIPELINE_CONNECTION";
    public static final String ACTIVITY_PIPELINE_CONNECTION_SKIP_BOOT = "ACTIVITY_PIPELINE_CONNECTION_SKIP_BOOT";
    public static final String ACTIVITY_PRE_RINSE = "ACTIVITY_PRE_RINSE";

    public static final String JUMP_WITH_PARAM_NOR = "JUMP_WITH_PARAM_NOR";
    public static final String JUMP_WITH_PARAM_SPE = "JUMP_WITH_PARAM_SPE";

    public static final String isLight= "isLight";

    public static int expert_supply = 10; // 专家选补液通道的要求

    // 延时
    public static final int DELAY_TIME = 3000;

    // REPORT延时
    public static final int REPORT_DELAY_TIME = 3000;

    public static final int DPR_TOTAL_AMOUNT_MIN = 50; // 治疗总量最小值（ml)
    public static final int DPR_TOTAL_AMOUNT_MAX = 200000; // 治疗总量最大值（ml)
    public static final int DPR_TOTAL_TIME_MIN = 1; // 治疗时间最小值（h)
    public static final int DPR_TOTAL_TIME_MAX = 120; // 治疗时间最大值（h)
    public static final int DPR_PER_RATE_MIN = 1; // 持续灌注速率（ml/min)
    public static final int DPR_PER_RATE_MAX = 260; // 持续灌注速率（ml/min)
    public static final int DPR_LEAVE_BELLY_MIN = 50; // 持续留腹量（ml)
    public static final int DPR_LEAVE_BELLY_MAX = 5000; // 持续留腹量（ml)
    public static final int DPR_DRAIN_RATE_MIN = 1; // 持续引流速率（ml/min)
    public static final int DPR_DRAIN_RATE_MAX = 260; // 持续引流速率（ml/min)
    public static final int DPR_BELLY_EMPTY_MIN = 1; // 腹部排空时间（min)
    public static final int DPR_BELLY_EMPTY_MAX = 600; // 腹部排空时间（min)
    public static final int DPR_FIRST_PER_MIN = 50; // 首次灌注量（ml)
    public static final int DPR_FIRST_PER_MAX = 5000; // 首次灌注量（ml)
    public static final int DPR_TARGET_TEMP_MIN = 34; // 目标温度（℃)
    public static final int DPR_TARGET_TEMP_MAX = 40; // 目标温度（℃)
    public static final int DPR_LIMIT_TEMP_MIN = 0; // 下限温度（℃)
    public static final int DPR_LIMIT_TEMP_MAX = 37; // 下限温度（℃)

    public static final int DPR_NOR_VOL_MAX = 500000; // 普通液治疗量
    public static final int DPR_NOR_VOL_MIN = 0; // 普通液治疗量
    public static final int DPR_PER_VOL_MAX = 100; // 渗透液治疗量
    public static final int DPR_PER_VOL_MIN = 0; // 渗透液治疗量
    public static final int DPR_OSM_MAX = 600; // 渗透压
    public static final int DPR_OSM_MIN = 280; // 渗透压
    public static final int DPR_INTER_TIME_MAX = 1000; // 间隔时间
    public static final int DPR_INTER_TIME_MIN = 0; // 间隔时间
    public static final int DPR_NUM_TIME_MAX = 1000; // 次数
    public static final int DPR_NUM_TIME_MIN = 0;

    public static final int DPR_FIX_TIME_MAX = 60;
    public static final int DPR_FIX_TIME_MIN = 1;

    public static final int DPR_TREAT_DURATION_MAX = 1000; // 计划治疗时长
    public static final int DPR_TREAT_DURATION_MIN = 0; // 计划治疗时长

    public static final int BASE_VALUE = 3; // 补液电机
    public static final int LAST_VALUE = 4; // 末袋电机

    public static final int BASE_SUPPLY = 1; // 基础补液
    public static final int LAST_SUPPLY = 2; // 末袋补液

    // 正常模式
    public static final int totalVolMin = 1000;
    public static final int totalVolMax = 500000;

    public static final int cycleVolMin = 10;
    public static final int cycleVolMax = 5000;

    public static final int cycleNumMin = 1;
    public static final int cycleNumMax = 100;

    public static final int firstVolMin = 0;
    public static final int firstVolMax = 5000;

    public static final int abdTimeMin = 1;
    public static final int abdTimeMax = 600;

    public static final int lastAbdMin = 0;
    public static final int lastAbdMax = 5000;

    public static final int finalVolMin = 0;
    public static final int finalVolMax = 5000;

    public static final int ultVolMin = 0;
    public static final int ultVolMax = 3000;

    public static final int aapdCycleVolMin = 0;
//    public static final int cycleVolMax = 5000;

    public static final int aapdCycleNumMin = 0;

    public static final int aapdAbdTimeMin = 0;
//    public static final int abdTimeMax = 600;

    // 预充参数
    public static int firstVolumeMin = 20;
    public static int firstVolumeMax = 150;
    public static int secondVolumeMin = 20;
    public static int secondVolumeMax = 150;
    public static int supplyPeriodMin = 60;
    public static int supplyPeriodMax = 10;
    public static int supplySpeedMin = 120;
    public static int supplySpeedMax = 30;
//    public static int supplySelectMin = 1;

    // 灌注参数
    public static int perfMaxWarningValueMin = 50;
    public static int perfTimeIntervalMin = 20;
    public static int perfThresholdValueMin = 0;

    public static int perfMaxWarningValueMax = 5000;
    public static int perfTimeIntervalMax = 600;
    public static int perfThresholdValueMax = 200;

    // 引流参数
    public static boolean isDrainManualEmptying = true;
    public static int drainTimeIntervalMin = 20;
    public static int drainThresholdValueMin = 0;
    public static int drainZeroCyclePercentageMin = 50;
    public static int drainOtherCyclePercentageMin = 50;
    public static int drainTimeoutAlarmMin = 1;
    public static int drainRinseVolumeMin = 30;
    public static int drainRinseNumberMin = 1;
    public static int drainWarnTimeIntervalMin = 1;

    public static int drainTimeIntervalMax= 600;
    public static int drainThresholdValueMax = 200;
    public static int drainZeroCyclePercentageMax = 120;
    public static int drainOtherCyclePercentageMax= 120;
    public static int drainTimeoutAlarmMax = 60;
    public static int drainRinseVolumeMax = 200;
    public static int drainRinseNumberMax = 3;
    public static int drainWarnTimeIntervalMax = 120;

    // 刘福参数
    public static boolean isAbdomenRetainingDeduct = false;
    public static boolean isZeroCycleUltrafiltration = false;

    // 补液参数
    public static int supplyTimeIntervalMin = 20;
    public static int supplyThresholdValueMin = 5;
    public static int supplyTargetProtectionValueMin = 100;

    public static int supplyTimeIntervalMax = 600;
    public static int supplyThresholdValueMax = 120;
    public static int supplyTargetProtectionValueMax = 500;
//    public static int supplyMinWeight = 500;
}
