package com.emt.pdgo.next.common.config;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.common
 * @ClassName: PdGoConstConfig
 * @Description: 常量配置
 * @Author: chenjh
 * @CreateDate: 2019/12/3 11:16 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/3 11:16 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class PdGoConstConfig {

    public static String TOKEN = "token";

    /*** 语音功能 打开 */
    public static String ttsSoundOpen = "tts_sound_open";

    /*** 补液参数 */
    public static String DEVICE_STATUS_INFO = "device_status_info";

    /*** 检测参数 */
    public static String PDP_CHECK_CONTROL_PARAM = "pdp_check_control_param";
    /*** app的服务端接口地址 */
    public static String PDP_APP_SERVER_URL = "pdp_app_server_url";
    /*** 设备码 */
    public static String PDP_MACHINE_SN = "pdp_machine_sn";

    public static String PRESCRIPT_TIME = "prescript_time"; // 远程处方创建时间;

    /** 用户id */
    public static String UID = "uuid";

    public static String useDeviceTime = "useDeviceTime";

    public static String targetTemper = "targetTemper";

    public static String BRIGHTNESS = "brightness";

    public static String OTHER_PARAMETER = "otherBeanS";

//    /*** 预冲参数 */
    public static String FIRST_RINSE_PARAMETER = "first_rinseParameter";
    /*** 引流参数 */
    public static String DRAIN_PARAMETER = "drain_parameter_entity";
    /*** 留腹参数 */
    public static String RETAIN_PARAM = "retain_param_entity";
    /*** 灌注参数 */
    public static String PERFUSION_PARAMETER = "perfusion_parameter_entity";
    /*** 补液参数 */
    public static String SUPPLY_PARAMETER = "supply_parameter_entity";
    /*** 用户参数 */
    public static String USER_PARAMETER = "user_entity";
    /*** 用户信息*/
    public static String USER_INFO = "user_info";

    /*** 引流参数 */
    public static String DPR_DRAIN_PARAM = "dpr_drain_param";
    /*** 留腹参数 */
    public static String DPR_RETAIN_PARAM = "dpr_retain_param";
    /*** 灌注参数 */
    public static String DPR_PERFUSION_PARA = "dpr_perfusion_para";
    /*** 补液参数 */
    public static String DPR_SUPPLY_PARAM = "dpr_supply_param";
    public static String DPR_OTHER_PARAM = "dpr_other_param";

    public static String PHONE = "phone";

    /*** 治疗之前身体数据 */
    public static String TREATMENT_BEFORE_WEIGHT_PARAMETER = "treatment_before_weight_parameter";

    /*** 治疗参数 */
    public static String TREATMENT_PARAMETER = "treatment_entity";

    public static String KID_PARAMS = "kid_params";
    public static String TPD_PARAMS = "tpd_params";
    public static String aApd_PARAMS = "aApd_params";
    public static String EXPERT_PARAMS = "expert_params";

    public static String PID_BEAN = "pid_bean";
    public static String IPD_BEAN = "ipdBean";
    public static String CCPD_BEAN = "ccpd_bean";

    /*** 治疗参数 */
    public static String REPLAY_ENTITY = "replay_entity";

    public static String CYCLE_PARAMS = "cycle_params";


    /*** 开始透析时间 也就是开始治疗时间 ****/
    public String startTime = "";
    /******** 结束治疗时间 也就是治疗完成时间 ***********/
    public String endTime = "";

    /*** 工程师模式，输入数值类型： ****/
    public static final String CHECK_TYPE_ENGINEER_PWD = "engineer_pwd";

    public static final String RESET = "reset";

    public static final String zeroClear = "zeroClear";

    /*** 温控板参数设置界面，输入数值类型：T1,最大温差值 ****/
    public static final String CHECK_TYPE_TEMPERATURE_MAX_DIFFERENCE = "max_temperature_difference";
    /*** 温控板参数设置界面，输入数值类型：T2,目标温度值 ****/
    public static final String CHECK_TYPE_TEMPERATURE_TARGBTN = "targbtn_temperature";
    /*** 温控板参数设置界面，输入数值类型：T3,上下回差 ****/
    public static final String CHECK_TYPE_TEMPERATURE_UP_LOW_DIFFERENCE = "up_low_difference";
    /*** 温控板参数设置界面，输入数值类型：T4,报警温度值 ****/
    public static final String CHECK_TYPE_TEMPERATURE_ALARM = "alarm_temperature";
    /*** 温控板参数设置界面，输入数值类型：T5,加热板温度上限 ****/
    public static final String CHECK_TYPE_TEMPERATURE_MAX_HEAT_PLATE = "max_heat_plate";
    /*** 温控板参数设置界面，输入数值类型：T6,目标温度差 ****/
    public static final String CHECK_TYPE_TEMPERATURE_TARGBTN_DIFFERENCE = "targbtn_difference";
    /*** 温控板参数设置界面，输入数值类型：T7,加热板温度调低值 ****/
    public static final String CHECK_TYPE_TEMPERATURE_LOW_HEAT_PLATE = "low_heat_plate";
    /*** 温控板参数设置界面，输入数值类型：TaE,Ta温度校正值 ****/
    public static final String CHECK_TYPE_TEMPERATURE_TA_ADJUST = "ta_adjust_temperature";
    /*** 温控板参数设置界面，输入数值类型：TbE,Tb温度校正值 ****/
    public static final String CHECK_TYPE_TEMPERATURE_TB_ADJUST = "tb_adjust_temperature";
    /*** 温控板参数设置界面，输入数值类型：TcE,Tc温度校正值 ****/
    public static final String CHECK_TYPE_TEMPERATURE_TC_ADJUST = "tc_adjust_temperature";

    /*** 预热设置界面，输入数值类型：加热目标温度值 ****/
    public static final String CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE = "target_temperature";


    /*** 传感器参数设置界面，输入数值类型：上位秤 去皮值 ****/
    public static final String CHECK_TYPE_WEIGH_PEELED_UPPER = "weigh_peeled_upper";
    /*** 传感器参数设置界面，输入数值类型：下位秤 去皮值 ****/
    public static final String CHECK_TYPE_WEIGH_PEELED_LOWER = "weigh_peeled_lower";
    /*** 传感器参数设置界面，输入数值类型：上位秤 称量系数 ****/
    public static final String CHECK_TYPE_WEIGH_COEFF_UPPER = "weigh_coeff_upper";
    /*** 传感器参数设置界面，输入数值类型：下位秤 称量系数 ****/
    public static final String CHECK_TYPE_WEIGH_COEFF_LOWER = "weigh_coeff_lower";

    public static final String CHECK_TYPE_PREHEAT = "check_type_preheat";

    /*** 传感器参数设置界面，输入数值类型：秤 去皮 ****/
    public static final String CHECK_TYPE_WEIGH_ZERO_CLEAR = "weight/zero_clear";
    /*** 传感器参数设置界面，输入数值类型：秤 零点校准 ****/
    public static final String CHECK_TYPE_WEIGH_ZERO_CALIB = "weight/zero_calib";
    /*** 传感器参数设置界面，输入数值类型：秤 量程校准 ****/
    public static final String CHECK_TYPE_WEIGH_RANGE_CALIB = "weight/range_calib";


    /*** 预冲参数设置界面，输入数值类型：排空时间 5-15 s ****/
    public static final String CHECK_TYPE_FIRST_RINSE_EMPTYING_TIME = "first_rinse_emptying_time";
    /*** 预冲参数设置界面，输入数值类型：预设增量1[上位秤增加重量X1克] g 范围 50-100 ****/
    public static final String CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT = "first_rinse_preset_weight";
    /*** 预冲参数设置界面，输入数值类型：预设减量 [上位秤减少重量X1克] g 范围 50-100 ****/
    public static final String CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT_LOSS = "first_rinse_preset_weight_loss";
    /*** 预冲参数设置界面，输入数值类型：预设增量1[上位秤增加重量X1克] g 范围 50-100 ****/
    public static final String CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT3 = "first_rinse_preset_weight3";


    /*** 灌注参数设置界面，输入数值类型：流量测速 时间间隔 ****/
    public static final String CHECK_TYPE_PERFUSION_TIME_INTERVAL = "perfusion_time_interval";
    /*** 灌注参数设置界面，输入数值类型：流量测速 阈值 ****/
    public static final String CHECK_TYPE_PERFUSION_THRESHOLD_VALUE = "perfusion_threshold_value";
    /*** 灌注参数设置界面，输入数值类型：加热袋最低重量允许 ****/
    public static final String CHECK_TYPE_PERFUSION_MIN_WEIGHT = "perfusion_min_weight";
    /*** 灌注参数设置界面，输入数值类型：是否允许最末灌注减去留腹量 ****/
    public static final String CHECK_TYPE_PERFUSION_ALLOW_ABDOMINAL_VOLUME = "perfusion_allow_abdominal_volume";
    /*** 灌注参数设置界面，输入数值类型：灌注最大警戒值 ****/
    public static final String CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE = "perfusion_max_warning_value";

    /*** 补液参数设置界面，输入数值类型：流量测速 时间间隔 ****/
    public static final String CHECK_TYPE_SUPPLY_TIME_INTERVAL = "supply_time_interval";
    /*** 补液参数设置界面，输入数值类型：流量测速 阈值 ****/
    public static final String CHECK_TYPE_SUPPLY_THRESHOLD_VALUE = "supply_threshold_value";
    /*** 补液参数设置界面，输入数值类型：补液目标保护值 ****/
    public static final String CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE = "supply_target_protection_value";
    /*** 补液参数设置界面，输入数值类型：启动补液的加热袋最低值 ****/
    public static final String CHECK_TYPE_SUPPLY_MIN_WEIGHT = "supply_min_weight";

    /*** 引流参数设置界面，输入数值类型：流量测速 时间间隔 ****/
    public static final String CHECK_TYPE_DRAIN_TIME_INTERVAL = "drain_time_interval";
    /*** 引流参数设置界面，输入数值类型：流量测速 阈值 ****/
    public static final String CHECK_TYPE_DRAIN_THRESHOLD_VALUE = "drain_threshold_value";
    /*** 引流参数设置界面，输入数值类型：0周期引流比例 ****/
    public static final String CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE = "drain_zero_cycle_percentage";
    /*** 引流参数设置界面，输入数值类型：其他周期引流比例 ****/
    public static final String CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE = "drain_other_cycle_percentage";
    /*** 引流参数设置界面，输入数值类型：引流/灌注超时报警 ****/
    public static final String CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM = "drain_unit_timeout_alarm";
    /*** 引流参数设置界面，输入数值类型：引流辅助冲洗 量 ****/
    public static final String CHECK_TYPE_DRAIN_RINSE_VOLUME = "drain_rinse_volume";
    /*** 引流参数设置界面，输入数值类型：引流辅助冲洗 次数 ****/
    public static final String CHECK_TYPE_DRAIN_RINSE_NUMBER = "drain_rinse_number";
    /*** 引流参数设置界面，输入数值类型：最末引流等待 ****/
    public static final String CHECK_TYPE_DRAIN_UNIT_LATENCY_TIME = "drain_unit_latency_time";
    /*** 引流参数设置界面，输入数值类型：最末引流提醒间隔 ****/
    public static final String CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL = "drain_unit_warn_time_interval";

    // DPR引流
    public static final String drainFlowRate = "drainFlowRate";
    public static final String drainFlowPeriod = "drainFlowPeriod";
    public static final String drainPassRate = "drainPassRate";
    public static final String finalDrainEmptyWaitTime = "finalDrainEmptyWaitTime";
    public static final String VaccumDraintimes = "VaccumDraintimes";
    // 灌注
    public static final String perfuseFlowRate = "perfuseFlowRate";
    public static final String perfuseFlowPeriod = "perfuseFlowPeriod";
    public static final String perfuseMaxVolume = "perfuseMaxVolume";
    // 补液
    public static final String supplyFlowRate = "supplyFlowRate";
    public static final String supplyFlowPeriod = "supplyFlowPeriod";
    public static final String supplyProtectVolume = "supplyProtectVolume";
    public static final String supplyMinVolume = "supplyMinVolume";
    // DPR
    public static final String TotalRemainder = "TotalRemainder";
    public static final String PerfuseDecDrain = "PerfuseDecDrain";
    public static final String DprSuppThreshold = "DprSuppThreshold";
    public static final String DprFlowPeriod = "DprFlowPeriod";
    public static final String FaultTime = "FaultTime";

    /*** 用户参数设置界面，输入数值类型：体重差值1 ****/
    public static final String CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_1 = "under_weight_value_1";
    /*** 用户参数设置界面，输入数值类型：体重差值2 ****/
    public static final String CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_2 = "under_weight_value_2";
    /*** 用户参数设置界面，输入数值类型：体重差值3 ****/
    public static final String CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_3 = "under_weight_value_3";

    public static final String CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_4 = "under_weight_value_4";
    public static final String CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_5 = "under_weight_value_5";

    // 自动息屏
    public static final String AUTO_SLEEP = "autoSleepV";

    // 电机测试
    public static final String VALUE_TEST = "value_test";

    /*** 治疗开始前，输入患者身体数据界面，输入数值类型：医生建议体重 ****/
    public static final String CHECK_TYPE_TREATMENT_BEFORE_RECOMMENDED_WEIGHT = "treatment_before_recommended_weight";
    /*** 治疗开始前，输入患者身体数据界面，输入数值类型：治疗前体重 ****/
    public static final String CHECK_TYPE_TREATMENT_BEFORE_WEIGHT = "treatment_before_weight";
    /*** 治疗开始前，输入患者身体数据界面，输入数值类型：治疗前收缩压 ****/
    public static final String CHECK_TYPE_TREATMENT_BEFORE_SYSTOLIC_BLOOD_PRESSURE = "treatment_before_systolic_blood_pressure";
    /*** 治疗开始前，输入患者身体数据界面，输入数值类型：治疗前舒张压 ****/
    public static final String CHECK_TYPE_TREATMENT_BEFORE_DIASTOLIC_BLOOD_PRESSURE = "treatment_before_diastolic_blood_pressure";

    /*** 治疗开始前，输入患者身体数据界面，输入数值类型：治疗前心率 ****/
    public static final String CHECK_TYPE_TREATMENT_BEFORE_HEART_RATE = "treatment_before_heart_rate";

    public static final String TPD_CYCLE_VOL = "tpd_cycle_vol";
    public static final String TPD_FIRST_VOL = "tpd_first_vol";

    /*** 治疗开始前，输入处方数据界面，输入数值类型：腹透液总量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL = "prescription_peritoneal_dialysis_fluid_total";
    /*** 治疗开始前，输入处方数据界面，输入数值类型：每周期灌注量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME = "prescription_per_cycle_perfusion_volume";
    /*** 治疗开始前，输入处方数据界面，输入数值类型：循环治疗周期数 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_PERIODICITIES = "prescription_cycleicities";
    /*** 治疗开始前，输入处方数据界面，输入数值类型：留腹时间 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME = "prescription_abdomen_retaining_time";
    /*** 治疗开始前，输入处方数据界面，输入数值类型：最末留腹量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY = "prescription_abdomen_retaining_volume_finally";
    /*** 治疗开始前，输入处方数据界面，输入数值类型：上次留腹量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME = "prescription_abdomen_retaining_volume_last_time";
    /*** 治疗开始前，输入处方数据界面，输入数值类型：TPD首次灌注量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME = "prescription_perfusion_volume";
    /*** 治疗开始前，输入处方数据界面，输入数值类型：预估超滤量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME = "prescription_estimated_ultrafiltration_volume";

    public static final String FINAL_SUPPLY = "FINAL_SUPPLY";

    /*** 治疗开始后，输入处方数据界面，输入数值类型：腹透液总量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL_TIMING = "prescription_peritoneal_dialysis_fluid_total_timing";
    /*** 治疗开始后，输入处方数据界面，输入数值类型：每周期灌注量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME_TIMING = "prescription_per_cycle_perfusion_volume_timing";
    /*** 治疗开始后，输入处方数据界面，输入数值类型：循环治疗周期数 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_PERIODICITIES_TIMING = "prescription_cycleicities_timing";
    /*** 治疗开始后，输入处方数据界面，输入数值类型：留腹时间 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME_TIMING = "prescription_abdomen_retaining_time_timing";
    /*** 治疗开始后，输入处方数据界面，输入数值类型：最末留腹量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY_TIMING = "prescription_abdomen_retaining_volume_finally_timing";
    /*** 治疗开始后，输入处方数据界面，输入数值类型：上次留腹量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME_TIMING = "prescription_abdomen_retaining_volume_last_time_timing";
    /*** 治疗开始后，输入处方数据界面，输入数值类型：TPD首次灌注量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME_TIMING = "prescription_perfusion_volume_timing";
    /*** 治疗开始后，输入处方数据界面，输入数值类型：预估超滤量 ****/
    public static final String CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME_TIMING = "prescription_estimated_ultrafiltration_volume_timing";

    public static final String INCREASING_VOL = "increasing_vol"; // 递增灌注量
    public static final String INCREASING_TIME = "increasing_time"; // 递增留腹时间

    public static final String BASE_SUPPLY_VOL = "base_supply_vol"; // 补液1周期灌注量
    public static final String BASE_SUPPLY_CYCLE = "base_supply_cycle"; // 补液1循环周期数
    public static final String SPECIAL_SUPPLY_VOL = "special_supply_vol"; // 补液2周期灌注量
    public static final String SPECIAL_SUPPLY_CYCLE = "special_supply_cycle"; // 补液2循环周期数

    // aApd
    public static final String aApd_bag = "aApd_bag";
    public static final String aApd_p1 = "aApd_p1";
    public static final String aApd_p2 = "aApd_p2";
    public static final String aApd_p3 = "aApd_p3";
    public static final String aApd_p4 = "aApd_p4";
    public static final String aApd_p5 = "aApd_p5";
    public static final String aApd_p6 = "aApd_p6";

    public static final String aApd_r1 = "aApd_r1";
    public static final String aApd_r2 = "aApd_r2";
    public static final String aApd_r3 = "aApd_r3";
    public static final String aApd_r4 = "aApd_r4";
    public static final String aApd_r5 = "aApd_r5";
    public static final String aApd_r6 = "aApd_r6";

    public static final String aApd_c1 = "aApd_c1";
    public static final String aApd_c2 = "aApd_c2";
    public static final String aApd_c3 = "aApd_c3";
    public static final String aApd_c4 = "aApd_c4";
    public static final String aApd_c5 = "aApd_c5";
    public static final String aApd_c6 = "aApd_c6";

    // 儿童模式
    public static final String KID_CYCLE_VOL = "kid_cycle_vol";
    public static final String KID_TOTAL = "kid_total";
    public static final String KID_CYCLE = "kid_cycle";
    public static final String KID_RETAIN_TIME = "kid_retain_time";
    public static final String KID_FINAL_RETAIN_VOL = "kid_final_retain_vol";
    public static final String KID_LAST_RETAIN_VOL = "kid_last_retain_vol";
    public static final String KID_FINAL_SUPPLY_VOL = "kid_final_supply_vol";
    public static final String KID_EST_ULT_VOL = "KID_EST_ULT_VOL"; // 预估超滤量
    public static final String KID_BSA = "kid_bsa";
    public static final String KID_FIRST_VOL = "kid_first_vol";
    public static final String HEIGHT = "height"; // 身高
    public static final String WEIGHT = "weight"; // 体重

    // 专家模式
    public static final String EXPERT_TOTAL = "expert_total";
    public static final String EXPERT_CYCLE_VOL = "expert_cycle_vol";
    public static final String EXPERT_CYCLE = "expert_cycle";
    public static final String EXPERT_RETAIN_TIME = "expert_retain_time";
    public static final String EXPERT_FINAL_RETAIN_VOL = "expert_final_retain_vol";
    public static final String EXPERT_LAST_RETAIN_VOL = "expert_last_retain_vol";
    public static final String EXPERT_FINAL_SUPPLY = "expert_final_supply";
    public static final String EXPERT_ULT_VOL = "expert_ult_vol";
    public static final String EXPERT_BASE_SUPPLY_VOL = "expert_base_supply_vol";
    public static final String EXPERT_BASE_SUPPLY_CYCLE = "expert_base_supply_cycle";
    public static final String EXPERT_OSM_SUPPLY_VOL = "expert_osm_supply_vol";
    public static final String EXPERT_OSM_SUPPLY_CYCLE = "expert_osm_supply_cycle";

    /*** 治疗开始前，输入治疗参数，输入数值类型：零周期引流阈值 ****/
    public static final String CHECK_TYPE_PARAMETER_SETTING_DRAIN_THRESHOLD_ZERO_CYCLE = "parameter_setting_drain_threshold_zero_cycle";
    /*** 治疗开始前，输入治疗参数，输入数值类型：其他周期引流阈值 ****/
    public static final String CHECK_TYPE_PARAMETER_SETTING_DRAIN_THRESHOLD_OTHER_CYCLE = "parameter_setting_drain_threshold_other_cycle";
    /*** 治疗开始前，输入治疗参数，输入数值类型：手动排空功能 ****/
    public static final String CHECK_TYPE_PARAMETER_SETTING_DRAIN_MANUAL_EMPTYING = "parameter_setting_drain_manual_emptying";
    /*** 治疗开始前，输入治疗参数，输入数值类型：末次排空延迟 ****/
    public static final String CHECK_TYPE_PARAMETER_SETTING_DRAIN_LAST_DELAY = "parameter_setting_drain_last_delay";
    /*** 治疗开始前，输入治疗参数，输入数值类型：灌注警戒值 ****/
    public static final String CHECK_TYPE_PARAMETER_SETTING_PERFUSION_WARNING_VALUE = "parameter_setting_perfusion_warning_value";
    /*** 治疗开始前，输入治疗参数，输入数值类型：超滤计算方式 ****/
    public static final String CHECK_TYPE_PARAMETER_SETTING_OTHER_ULTRAFILTRATION_CALCULATION_METHOD = "parameter_setting_other_ultrafiltration_calculation_method";
    /*** 治疗开始前，输入治疗参数，输入数值类型：等待提醒间隔时间 ****/
    public static final String CHECK_TYPE_PARAMETER_SETTING_WAITING_INTERVA = "parameter_setting_waiting_interva";


    /*** 治疗中断反馈界面，输入数值类型：引流量 ****/
    public static final String CHECK_TYPE_TREATMENT_FEEDBACK_DRAIN_VOLUME = "treatment_feedback_drain_volume";
    /*** 治疗中断反馈界面，输入数值类型：灌注量 ****/
    public static final String CHECK_TYPE_TREATMENT_FEEDBACK_PERFUSION_VOLUME = "treatment_feedback_perfusion_volume";
    /*** 治疗中断反馈界面，输入数值类型：超滤量 ****/
    public static final String CHECK_TYPE_TREATMENT_FEEDBACK_UFV = "treatment_feedback_ufv";


    /*** 治疗完成界面，输入数值类型：尿量 ml ****/
    public static final String CHECK_TYPE_TREATMENT_COMPLETION_URINE_OUTPUT = "treatment_completion_urine_output";
    /*** 治疗完成界面，输入数值类型：饮水量 ml ****/
    public static final String CHECK_TYPE_TREATMENT_COMPLETION_WATER_INTAKE = "treatment_completion_water_intake";
    /*** 治疗完成界面，输入数值类型：空腹血糖 ****/
    public static final String CHECK_TYPE_TREATMENT_COMPLETION_FASTING_BLOOD_GLUCOSE = "treatment_completion_fasting_blood_glucose";
    /*** 治疗完成界面，输入数值类型：治疗后体重 ****/
    public static final String CHECK_TYPE_TREATMENT_COMPLETION_WEIGHT = "treatment_completion_weight";
    /*** 治疗完成界面，输入数值类型：治疗后收缩压 ****/
    public static final String CHECK_TYPE_TREATMENT_COMPLETION_SYSTOLIC_BLOOD_PRESSURE = "treatment_completion_systolic_blood_pressure";
    /*** 治疗完成界面，输入数值类型：治疗后舒张压 ****/
    public static final String CHECK_TYPE_TREATMENT_COMPLETION_DIASTOLIC_BLOOD_PRESSURE = "treatment_completion_diastolic_blood_pressure";

    public static final String START_TREAT_TIME = "start_treat_time";
    public static final String END_TREAT_TIME = "end_treat_time";
    public static final String TREAT_firstDrainTime = "TREAT_firstDrainTime";
    public static final String TREAT_maxCycle = "TREAT_maxCycle";
    public static final String TREAT_cycles = "TREAT_cycles";
    public static final String TREAT_inFlows = "TREAT_inFlows";
    public static final String TREAT_inFlowTime = "TREAT_inFlowTime";
    public static final String TREAT_leaveWombTime = "TREAT_leaveWombTime";
    public static final String TREAT_exhaustTime = "TREAT_exhaustTime";
    public static final String TREAT_drainages = "TREAT_drainages";
    public static final String TREAT_auxiliaryFlushingVolume = "TREAT_auxiliaryFlushingVolume";
    public static final String TREAT_abdominalVolumeAfterInflow = "TREAT_abdominalVolumeAfterInflow";
    public static final String TREAT_drainageTargetValue = "TREAT_drainageTargetValue";
    public static final String TREAT_estimatedResidualAbdominalFluid = "TREAT_estimatedResidualAbdominalFluid";

    // DPR
    // 处方
    public static final String DRP_PRESCRIPTION = "drp_prescription";
    public static final String cFpd = "cFpd";

    public static final String CFPD_LEAVE_BELLY = "cfpd_leave_belly"; // 持续留腹量（ml)
    public static final String CFPD_FIRST_PER = "cfpd_first_per"; // 首次灌注量（ml)
    // 处方输入
    public static final String DPR_TOTAL_AMOUNT = "dpr_total_amount"; // 治疗总量（ml)
    public static final String DPR_TOTAL_TIME = "dpr_total_time"; // 治疗时间（h)
    public static final String DPR_PER_RATE = "dpr_per_rate"; // 持续灌注速率（ml/h)
    public static final String DPR_LEAVE_BELLY = "dpr_leave_belly"; // 持续留腹量（ml)
    public static final String DPR_DRAIN_RATE = "dpr_drain_rate"; // 持续引流速率（ml/h)
    public static final String DPR_BELLY_EMPTY = "dpr_belly_empty"; // 腹部排空时间（min)
    public static final String DPR_FIRST_PER = "dpr_first_per"; // 首次灌注量（ml)
    public static final String DPR_TARGET_TEMP = "dpr_target_temp"; // 目标温度（℃)
    public static final String DPR_LIMIT_TEMP = "dpr_limit_temp"; // 下限温度（℃)

    public static final String DPR_FIX_TIME = "dpr_fix_time";

    public static final String DPR_NOR_VOL = "dpr_nor_vol"; // 普通液治疗量
    public static final String DPR_PER_VOL = "dpr_per_vol"; // 渗透液治疗量
    public static final String DPR_OSM = "dpr_osm"; // 渗透压
    public static final String DPR_INTER_TIME = "dpr_inter_time"; // 间隔时间
    public static final String DPR_NUM_TIME = "dpr_num_time"; // 次数
    public static final String DPR_TREAT_DURATION = "dpr_treat_duration"; // 计划治疗时长

}
