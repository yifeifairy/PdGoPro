package com.emt.pdgo.next.common.config;

/**
 *  
 * @ProjectName:
 * @Package:        com.emt.pdgo.next.common.config
 * @ClassName:      CommandSendConfig
 * @Description:    上位机和下位机通信指令配置
 * @Author:         chenjh
 * @CreateDate:     2019/12/3 11:17 AM
 * @UpdateUser:     更新者
 * @UpdateDate:     2020/07/18 10:00 AM
 * @UpdateRemark:   更新内容
 * @Version:        1.0
 */
public class CommandSendConfig {

    /*** 系统指令: 关机指令 */
    public static final String METHOD_BATTERY_OFF = "battery/off";

    /*** 自检指令: 自检 */
    public static final String METHOD_SELFCHECK_START = "selfcheck/start";//sys/check

    /*** 获取设备信息: 开启 */
//    public static final String METHOD_STATUS_ON = "status/on";
    public static final String METHOD_STATUS_ON = "report";
    /*** 获取设备信息: 关闭 */
    public static final String METHOD_STATUS_OFF = "status/off";



//    /*** 秤指令: 秤数值设定 */
//    public static final String WEIGH_CALIB = "weight/calib";//weight/set
//    /*** 秤指令: 秤去皮(归零) */
//    public static final String WEIGH_TARE = "weight/tare";


    /*** 秤指令: 去皮 */
    public static final String METHOD_WEIGH_ZERO_CLEAR = "weight/zero_clear";//weight/set
    /*** 秤指令: 标零 */
    public static final String METHOD_WEIGH_ZERO_CALIB = "weight/zero_calib";//weight/set
    /*** 秤指令: 秤标量程 */
    public static final String METHOD_WEIGH_RANGE_CALIB = "weight/range_calib";

    /*** 温控指令: 设置温控参数 */
    public static final String METHOD_TEMPERATURE_PARAMS_SET = "thermostat/set";// tparam/set
    /*** 温控指令: 获取温控参数 */
    public static final String METHOD_TEMPERATURE_PARAMS_GET = "thermostat/get";// tparam/get

    /*** 管路指令: 装管路开始 */
    public static final String METHOD_PIPECART_INSTALL = "pipecart/install";// feeder/on
    /*** 管路指令: 装管路完成 */
    public static final String METHOD_PIPECART_FINISH = "pipecart/finish";// feeder/off


    /*** 预热指令: 开始预热 */
    public static final String METHOD_PREHEAT_START = "preheat/start";
    /*** 预热指令: 结束预热 */
    public static final String METHOD_PREHEAT_STOP = "preheat/stop";

    /*** 预热指令: 温度设置 */
    public static final String METHOD_TEMPERATURE_SET = "temperature/set";


    /*** 预冲洗指令: 启动手动预冲 */
    public static final String METHOD_RINSE_MANUAL_START = "manualrinse/start";
    /*** 预冲洗指令: 结束手动预冲 */
    public static final String METHOD_RINSE_MANUAL_STOP = "manualrinse/stop";
    /*** 预冲洗指令: 手动打开单个阀 */
    public static final String METHOD_RINSE_MANUAL_OPEN = "manualrinse/open";
    /*** 预冲洗指令: 手动关闭单个阀 */
    public static final String METHOD_RINSE_MANUAL_CLOSE = "manualrinse/close";

    /*** 预冲洗指令: 启动自动预冲 */
    public static final String METHOD_RINSE_AUTO_START = "autorinse/start";
    /*** 预冲洗指令: 跳过自动预冲（故障时候使用） */
    public static final String METHOD_RINSE_AUTO_FAILURE_CONTINUE = "autorinse/failure_continue";
    /*** 预冲洗指令: 结束自动预冲 */
    public static final String METHOD_RINSE_AUTO_ABORT = "autorinse/abort";



    /*** 治疗指令: 开始治疗 */
    public static final String METHOD_TREATMENT_START = "treatment/start";
    /*** 治疗指令: 暂停治疗 */
    public static final String METHOD_TREATMENT_PAUSE = "treatment/pause";
    /*** 治疗指令: 继续治疗 */
    public static final String METHOD_TREATMENT_RESUME = "treatment/resume";

    /*** 治疗指令: 继续当前治疗故障的阶段 */
    public static final String METHOD_TREATMENT_FAILURE_CONTINUET = "treatment/failure_continue";

    /*** 治疗指令: 跳过当前环节 */
    public static final String METHOD_TREATMENT_SKIP = "treatment/skip";
    /*** 治疗指令: 终止治疗 */
    public static final String METHOD_TREATMENT_ABORT = "treatment/abort";

    /*** 治疗指令: 引流 */
    public static final String TREATMENT_DRAIN = "treatment/drain";
    /*** 治疗指令: 灌注 */
    public static final String TREATMENT_PERFUSE = "treatment/perfuse";
//  /*** 治疗指令: 留腹 */
//    public static final String TREATMENT_DRAIN = "treatment/drain";
    /*** 治疗指令: 补液 */
    public static final String TREATMENT_SUPPLY = "treatment/supply";

    /*** 蜂鸣器指令: 蜂鸣器开 */
    public static final String METHOD_BEEP_ON = "beep/on";
    /*** 蜂鸣器指令: 蜂鸣器关 */
    public static final String METHOD_BEEP_OFF = "beep/off";

    /*** Led指令: led开 */
    // LED_SUPPLY_OFF
            //led/supply/off
    public static final String LED_ON = "led/on";
    /*** Led指令: led关 */
    public static final String LED_OFF = "led/off";

    /*** 冲洗故障 */
    public static final String FAULT_RINSE = "rinse fault";

    /*** 电机操作: 开启 */
    public static final String ALL_VALVE_OPEN = "valve/open";
    // valve_all perfuse
    /*** 电机操作: 关闭 */
    public static final String ALL_VALVE_CLOSE = "valve/close";

    /*** 电机操作: 开启 */
    public static final String PERFUSE_VALVE_OPEN = "perfuse_valve_open";
    // valve_all perfuse
    /*** 电机操作: 关闭 */
    public static final String PERFUSE_VALVE_CLOSE = "perfuse_valve_close";

    /*** 电机操作: 开启 */
    public static final String SAFE_VALVE_OPEN = "safe_valve_open";
    /*** 电机操作: 关闭 */
    public static final String SAFE_VALVE_CLOSE = "safe_valve_close";

    /*** 电机操作: 开启 */
    public static final String SUPPLY_VALVE_OPEN = "supply_valve_open";
    /*** 电机操作: 关闭 */
    public static final String SUPPLY_VALVE_CLOSE = "supply_valve_close";

    /*** 电机操作: 开启 */
    public static final String SUPPLY2_VALVE_OPEN = "supply2_valve_open";
    /*** 电机操作: 关闭 */
    public static final String SUPPLY2_VALVE_CLOSE = "supply2_valve_close";

    /*** 电机操作: 开启 */
    public static final String GROUP1_VALVE_OPEN = "group1_valve_open";
    /*** 电机操作: 关闭 */
    public static final String GROUP1_VALVE_CLOSE = "group1_valve_close";

    /*** 电机操作: 开启 */
    public static final String GROUP2_VALVE_OPEN = "group2_valve_open";
    /*** 电机操作: 关闭 */
    public static final String GROUP2_VALVE_CLOSE = "group2_valve_close";

    /*** drain电机操作: 开启 */
    public static final String DRAIN_VALVE_OPEN = "drain_valve_open";
    /*** drain电机操作: 关闭 */
    public static final String DRAIN_VALVE_CLOSE = "drain_valve_close";

    /*** vaccum电机操作: 开启 */
    public static final String VACCUM_VALVE_OPEN = "vaccum_valve_open";
    /*** vaccum电机操作: 关闭 */
    public static final String VACCUM_VALVE_CLOSE = "vaccum_valve_close";

}
