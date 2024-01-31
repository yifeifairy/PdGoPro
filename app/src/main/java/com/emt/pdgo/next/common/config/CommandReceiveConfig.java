package com.emt.pdgo.next.common.config;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.common
 * @ClassName: CommandReceiveConfig
 * @Description: 上位机和下位机通信指令配置
 * @Author: chenjh
 * @CreateDate: 2019/12/3 11:17 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/10/26 14:14 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class CommandReceiveConfig {

    /*** 电源监控管理 */
    public static final String TOPIC_POWER_MONITOR = "power_monitor";
    /*** 电源管理: 1、AC220V断开 */
    public static final String MSG_AC_POWER_OFF = "ac power off";
    /*** 电源管理: 2、AC220V连接 */
    public static final String MSG_AC_POWER_ON = "ac power in";
    /*** 电源管理: 3、AC掉电超过30分钟 */
    public static final String MSG_AC_POWER_FAULT = "ac power fault";

    /*** 自检指令返回: 阀自检结果 */
    public static final String TOPIC_SELF_CHECK_VALVE = "selfcheck/valve";
    /*** 自检指令返回: 上下位称自检结果 */
    public static final String TOPIC_SELF_CHECK_WEIGHT = "selfcheck/weight";
    /*** 自检指令返回: 温控自检结果 */
    public static final String TOPIC_SELF_CHECK_THERMOSTAT = "selfcheck/thermostat";

    /*** 温控指令返回数据 */
    public static final String TOPIC_THERMOSTAT = "thermostat";

    /*** 温控参数设置返回数据 */
    public static final String TOPIC_THERMOSTAT_SET = "thermostat/set";
    /* 温控参数设置返回数据 */
    public static final String TOPIC_THERMOSTAT_GET = "thermostat/get";

    /*** 设备状态 */
    public static final String TOPIC_DEVICE_STATUS = "status";


    /*** 预冲洗指令 */
    public static final String TOPIC_AUTO_RINSE = "autorinse";
    /*** 预冲洗指令: 自动预冲 开始 */
    public static final String TOPIC_AUTO_RINSE_START = "autorinse/start";
    /*** 预冲洗指令: 自动预冲 冲洗阶段 */
    public static final String TOPIC_AUTO_RINSE_FLUSH = "autorinse/flush";
    /*** 预冲洗指令: 自动预冲 引流阶段 */
    public static final String TOPIC_AUTO_RINSE_DRAIN = "autorinse/drain";
    /*** 预冲洗指令: 自动预冲 补液阶段 */
    public static final String TOPIC_AUTO_RINSE_SUPPLY = "autorinse/supply";
    /*** 预冲洗指令: 自动预冲 灌注阶段 */
    public static final String TOPIC_AUTO_RINSE_PERFUSE = "autorinse/perfuse";
    /*** 预冲洗指令: 自动预冲 完成 */
    public static final String TOPIC_AUTO_RINSE_FINISH = "autorinse/finish";
    /*** 预冲洗指令: 自动预冲 过程 */
    public static final String TOPIC_AUTO_RINSE_PROCESS = "autorinse/process";

    /*** 预冲洗指令: 手动预冲 过程 */
    public static final String TOPIC_AUTO_MANUALRINSE_PROCESS = "manurinse";

    public static final String TOPIC_FINISH = "finish";
    public static final String TOPIC_SUCCESS = "success";


    /***  */
    public static final String MSG_OK = "ok";
    /***  */
    public static final String MSG_ERR = "err";
    /*** msg中返回success代表成功 */
    public static final String MSG_SUCCESS = "success";
    /*** msg中返回start */
    public static final String MSG_START = "start";
    /*** msg中返回finish */
    public static final String MSG_FINISH = "finish";
    /***  */
    public static final String MSG_READY = "ready";
    /*** msg中返回time out */
    public static final String MSG_TIME_OUT = "time out";
    public static final String MSG_FAILURE = "failure";


    /*** 治疗指令 */
    public static final String TOPIC_TREATMENT = "treatment";

    /*** 治疗指令 完成治疗 */
    public static final String TOPIC_TREATMENT_FINISH = "treatment finish";

    /*** 治疗指令: 开始治疗 */
    public static final String TOPIC_TREATMENT_START = "treatment/start";
    /*** 治疗指令: 暂停治疗 */
    public static final String TOPIC_TREATMENT_PAUSE = "treatment/pause";
    /*** 治疗指令: 继续治疗 */
    public static final String TOPIC_TREATMENT_RESUME = "treatment/resume";
    /*** 治疗指令: 跳过当前环节 */
    public static final String TOPIC_TREATMENT_SKIP = "treatment/skip";
    /*** 治疗指令: 终止治疗 */
    public static final String TOPIC_TREATMENT_ABORT = "treatment/abort";
    /*** 治疗指令: 终止治疗二次确认 */
    public static final String TOPIC_TREATMENT_ABORT2 = "treatment/abort2";


    /*** 治疗指令: 引流 */
    public static final String TOPIC_TREATMENT_DRAIN = "treatment/drain";
    /*** 治疗指令: 灌注 */
    public static final String TOPIC_TREATMENT_PERFUSE = "treatment/perfuse";
    /*** 治疗指令: 补液 */
    public static final String TOPIC_TREATMENT_SUPPLY = "treatment/supply";
    /*** 治疗指令: 留腹 */
    public static final String TOPIC_TREATMENT_RETAIN = "treatment/retain";

    /*** 温度指令: 温度设置 */
    public static final String TOPIC_TEMPERATURE_SET = "temperature/set";


    /*** 蜂鸣器指令: 蜂鸣器开 */
    public static final String BEEP_ON = "beep/on";
    /*** 蜂鸣器指令: 蜂鸣器关 */
    public static final String BEEP_OFF = "beep/off";

    /*** Led指令: led开 */
    public static final String LED_ON = "led/on";
    /*** Led指令: led关 */
    public static final String LED_OFF = "led/off";

    /** 过程开始 */
    public static final String MSG_PROCESS_START = "process start";

    /*** 治疗过程返回msg: 过程完成 */
    public static final String MSG_PROCESS_FINISH = "process finish";

    /*** 自检开始 */
    public static final String MSG_SELFCHECK_START = "selfcheck start";

    /** 自检完成 */
    public static final String MSG_SELFCHECK_FINISH = "selfcheck finish";
    /** 自检失败 */
    public static final String MSG_SELFCHECK_FAILURE = "selfcheck failure";

    /** 治疗过程返回灌注信息msg: 灌注开始 */
    public static final String MSG_PERFUSE_START = "perfuse start";

    /**
     * 治疗过程返回灌注信息msg: 灌注进行中
     */
    public static final String MSG_PERFUSE_RUNNING = "perfuse running";

    /**
     * 治疗过程返回灌注信息msg: 灌注完成
     */
    public static final String MSG_PERFUSE_FINISH = "perfuse finish";

    /**
     * 治疗过程返回灌注信息msg: 灌注失败
     */
    public static final String MSG_PERFUSE_FAILURE = "perfuse failure";

    /**
     * 治疗过程返回灌注信息msg: 灌注留腹超过限制
     */
    public static final String MSG_RETAIN_OVER_LIMIT = "retain over limit";

    /** 治疗过程修改温度返回信息msg: 加热温度到达 */
    public static final String MSG_TEMPERATURE_ARRIVE = "temperature arrive";
    /** 治疗过程修改温度返回信息msg: 温度设置失败 */
    public static final String MSG_TEMPERATURE_SET_FAILED = "temperature set failed";
    /** 治疗过程修改温度返回信息msg: 温度设置成功 */
    public static final String MSG_TEMPERATURE_SET_SUCCESS = "temperature set success";
    /** 治疗过程修改温度返回信息msg: 灌注前检查温度 */
    public static final String MSG_CHECK_TEMPERATURE = "check temperature";

    /** 预热返回信息msg: 预加热进程中，设置温度失败 */
    public static final String MSG_TARGET_TEMPERATURE_SET_FAILED = "target temperature set failed";

//    /**
//     * 温度异常警告
//     */
//    public static final String MSG_RETAIN_OVER_TEMPERATURE = "perfuse over temp";

    /**
     * 灌注高温警告
     */
    public static final String MSG_RETAIN_HIGH_TEMPERATURE_WARNING = "perfuse temp continuous over 41";


    /**
     * 灌注低温警告
     */
    public static final String MSG_RETAIN_LOW_TEMPERATURE_WARNING = "perfuse temp continuous lower 34";

    /**
     * 补液开始
     */
    public static final String MSG_SUPPLY_START = "supply start";

    /**
     * 补液重新开始
     */
    public static final String MSG_SUPPLY_RESTART = "supply restart";
    /**
     * 补液完成
     */
    public static final String MSG_SUPPLY_FINISH = "supply finish";
    /**
     * 补液失败：
     */
    public static final String MSG_SUPPLY_FAILURE = "supply failure";

    /**
     * 10、留腹时间结束：
     */
    public static final String MSG_RETAIN_TIME_OUT = "retain time out";

    /**
     * 11、留腹时间结束但故障未处理：自检失败未处理
     */
    public static final String MSG_RETAIN_TIME_OUT_SELFCHECK_FAILURE_UNHANDLE = "retain time out selfcheck failure unhandle";
    /**
     * 11、留腹时间结束但故障未处理：补液失败未处理
     */
    public static final String MSG_RETAIN_TIME_OUT_SUPPLY_FAILURE_UNHANDLE = "retain time out supply failure unhandle";
    /**
     * 11、留腹时间结束但故障未处理：其他类型失败未处理
     */
    public static final String MSG_RETAIN_TIME_OUT_OTHER_FAILURE_UNHANDLE = "retain time out other failure unhandle";

    /**
     * 治疗过程返回msg: 引流开始
     */
    public static final String MSG_DRAIN_START = "drain start";
    /**
     * 治疗过程返回msg: 引流重新开始
     */
    public static final String MSG_DRAIN_RESTART = "drain restart";
    /**
     * 治疗过程返回msg: NTPD引流进行中
     */
    public static final String MSG_DRAIN_NTPD_RUNNING = "drain ntpd running";
    /**
     * 治疗过程返回msg: TPD引流进行中
     */
    public static final String MSG_DRAIN_TPD_RUNNING = "drain tpd running";
    /**
     * 治疗过程返回msg: 引流不合格(故障)
     */
    public static final String MSG_DRAIN_FAILURE = "drain failure";
    /**
     * 治疗过程返回msg: 引流完成
     */
    public static final String MSG_DRAIN_FINISH = "drain finish";

    /**
     * 负压引流
     */
    public static final String VACCUM_DRAIN_FINISH = "vaccum drain finish";
    /**
     * 治疗过程返回msg: 引流小冲开始
     */
    public static final String MSG_DRAIN_RINSE_START = "drain rinse start";
    /**
     * 治疗过程返回msg: 引流小冲进行中
     */
    public static final String MSG_DRAIN_RINSE_RUNNING = "drain rinse running";

    /**
     * 治疗过程返回msg: 引流不足
     */
    public static final String MSG_DRAIN_POOR = "drain poor";
    /**
     * 治疗过程返回msg: 引流小冲完成
     */
    public static final String MSG_DRAIN_RINSE_FINISH = "drain rinse finish";
    /**
     * 治疗过程返回msg: 引流小冲失败
     */
    public static final String MSG_DRAIN_RINSE_FAILURE = "drain rinse failure";

    /**
     * 治疗过程返回msg: 负压引流开始
     */
    public static final String MSG_VACCUM_DRAIN_START = "vaccum drain start";
    /**
     * 治疗过程返回msg: 负压引流完成
     */
    public static final String MSG_VACCUM_DRAIN_FINISH = "vaccum drain finish";

    /**
     * 治疗过程返回msg: 开始末次引流排空
     */
    public static final String MSG_FINAL_EMPTY_START = "final empty start";
    /**
     * 治疗过程返回msg: 末次引流排空完成
     */
    public static final String MSG_FINAL_EMPTY_FINISH = "final empty finish";
    /**
     * 治疗过程返回msg: 末次引流排空完成
     */
    public static final String MSG_FINAL_EMPTY_RUNNING = "final empty running";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*** 电机状态： 关闭运行中（收回来的是打开阀门，推出去是关闭阀门）  */
    public static final String valve_close_run = "80";
    /*** 电机状态： 关闭完成 （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_close_suc = "00";
    /*** 电机状态： 关闭超时 （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_close_timeout = "d0";
    /*** 电机状态： 关闭错误 （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_close_fail = "e0";
    /*** 电机状态： 关闭卡滞 （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_close_stuck = "f0";
    /*** 电机状态： 打开运行中 （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_open_run = "81";
    /*** 电机状态： 打开完成 （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_open_suc = "01";
    /*** 电机状态： 打开超时 （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_open_timeout = "d1";
    /*** 电机状态： 打开错误  （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_open_fail = "e1";
    /*** 电机状态： 打开卡滞 （收回来的是打开阀门，推出去是关闭阀门） */
    public static final String valve_open_stuck = "f1";




    /*** 冲洗故障 */
    public static final String TREATMENT_DRAIN_RINSE_FAULT = "rinse fault";

    /*** 预热指令: 预热 */
    public static final String TOPIC_PREHEAT = "preheat";
    /*** 装管路 */
    public static final String TOPIC_PIPECART = "pipecart";
    /*** 准备装管路 */
    public static final String TOPIC_PIPECART_INSTALL = "pipecart/install";
    /*** 装管路完成 */
    public static final String TOPIC_PIPECART_FINISH = "pipecart/finish";

    /*** 预热返回: Preheat complete [加热到的设置目标值] */
//    public static final String PREHEAT_FINISH = "Preheat finish";
    public static final String PREHEAT_FINISH = "finish";
    /*** 预热返回: 无加热对象，提示需要放置液袋到加热托盘上 */
    public static final String PREHEAT_NO_HEATING_TARGET = "no heating target";
    /*** 预热返回: 找到加热对象,开始预热 */
    public static final String PREHEAT_TARGET_READY = "heating target ready";//heating target ready
    /*** 预热指令: 加热模块故障 */
    public static final String PREHEAT_THERMOSTAT_FAULT = "thermostat fault";

    /*** 命令 */
    public static final String COMMAND_CHECK = "CommandCheck";
    /*** 命令无效 */
    public static final String COMMAND_CHECK_INVALID_COMMAND = "InvalidCommand";
    /*** 缺少参数 */
    public static final String COMMAND_CHECK_MISSING_PARAMETER = "Missing parameter";

    /*** 握手返回Hello */
    public static final String COMMAND_CHECK_HELLO = "Hello";
    /*** 设置秤的系数 */
    public static final String COMMAND_CHECK_WEIGHCOEFF = "weighcoeff";

    /*** 返回状态 */
    public static final String COMMAND_CHECK_SYSSTARUS = "sysstarus";////确定不是sysstatus

    /*** 自检返回: 阀 */
    public static final String SYSSELF_VALUE = "Valve:";
    /*** 自检返回: 负压阀正常 */
    public static final String SYSSELF_VALVE_NEG_OK = "Neg-Valve-OK";
    /*** 自检返回: 负压阀故障 */
    public static final String SYSSELF_VALVE_NEG_ERR = "Neg-Valve-ERR";
    /*** 自检返回: 补液阀正常 */
    public static final String SYSSELF_VALVE_SUPPLY_OK = "Supply-Valve-OK";
    /*** 自检返回: 补液阀故障 */
    public static final String SYSSELF_VALVE_SUPPLY_ERR = "Supply-Valve-ERR";
    /*** 自检返回: 灌注阀正常 */
    public static final String SYSSELF_VALVE_PERFUSION_OK = "Perfusion-Valve-OK";
    /*** 自检返回: 灌注阀故障 */
    public static final String SYSSELF_VALVE_PERFUSION_ERR = "Perfusion-Valve-ERR";
    /*** 自检返回: 引流阀正常 */
    public static final String SYSSELF_VALVE_DRAIN_OK = "Drain-Valve-OK";
    /*** 自检返回: 引流阀故障 */
    public static final String SYSSELF_VALVE_DRAIN_ERR = "Drain-Valve-ERR";
    /*** 自检返回: 安全阀正常 */
    public static final String SYSSELF_VALVE_SAFE_OK = "Safe-Valve-OK";
    /*** 自检返回: 安全阀故障 */
    public static final String SYSSELF_VALVE_SAFE_ERR = "Safe-Valve-ERR";

    /*** 自检返回: 温度传感器 */
    public static final String SYSSELF_SENSOR = "Sensor:";
    /*** 自检返回: 温度传感器 T0通过 */
    public static final String SYSSELF_SENSOR_T0_OK = "T0-OK";
    /*** 自检返回: 温度传感器 T0未通过 */
    public static final String SYSSELF_SENSOR_T0_ERR = "T0-ERR";
    /*** 自检返回: 温度传感器 T1通过 */
    public static final String SYSSELF_SENSOR_T1_OK = "T1-OK";
    /*** 自检返回: 温度传感器 T1未通过 */
    public static final String SYSSELF_SENSOR_T1_ERR = "T1-ERR";
    /*** 自检返回: 温度传感器 T2通过 */
    public static final String SYSSELF_SENSOR_T2_OK = "T2-OK";
    /*** 自检返回: 温度传感器 T2未通过 */
    public static final String SYSSELF_SENSOR_T2_ERR = "T2-ERR";

    /*** 自检返回: 上位秤 */
    public static final String SYSSELF_WEIGHT_H = "WeightH:";
    /*** 自检返回: 下位秤 */
    public static final String SYSSELF_WEIGHT_L = "WeightL:";
    /*** 自检返回: 上位秤通过 */
    public static final String SYSSELF_WEIGHT_H_OK = "Upper-Weight-OK";
    /*** 自检返回: 上位秤未通过 */
    public static final String SYSSELF_WEIGHT_H_ERR = "Upper-Weight-ERR";
    /*** 自检返回: 下位秤通过 */
    public static final String SYSSELF_WEIGHT_L_OK = "Lower-Weight-OK";
    /*** 自检返回: 下位秤自检未通过 */
    public static final String SYSSELF_WEIGHT_L_ERR = "Lower-Weight-ERR";


    /*** 阀操作命令: 开启灌注阀 */
    public static final String VALVE_PERFUSION_ON = "PerfusionON";
    /*** 阀操作命令: 关闭灌注阀 */
    public static final String VALVE_PERFUSION_OFF = "PerfusionOFF";
    /*** 阀操作命令: 开启引流阀 */
    public static final String VALVE_DRAIN_ON = "DrainON";
    /*** 阀操作命令: 关闭引流阀 */
    public static final String VALVE_DRAIN_OFF = "DrainOFF";
    /*** 阀操作命令: 开启补液阀 */
    public static final String VALVE_SUPPLY_ON = "SupplyON";
    /*** 阀操作命令: 关闭补液阀 */
    public static final String VALVE_SUPPLY_OFF = "SupplyOFF";
    /*** 阀操作命令: 开启末次阀(最后灌注阀) */
    public static final String VALVE_LAST_ON = "LastON";
    /*** 阀操作命令: 关闭末次阀(最后灌注阀) */
    public static final String VALVE_LAST_OFF = "LastOFF";
    /*** 阀操作命令: 开启安全阀 */
    public static final String VALVE_SAFE_ON = "SafeON";
    /*** 阀操作命令: 关闭安全阀 */
    public static final String VALVE_SAFE_OFF = "SafeOFF";
    /*** 阀操作命令: 开启安全阀 */
    public static final String VALVE_FORCE_ON = "ForceON";
    /*** 阀操作命令: 关闭安全阀 */
    public static final String VALVE_FORCE_OFF = "ForceOFF";
    /*** 阀操作命令: 开启负压阀 */
    public static final String VALVE_NEG_ON = "NegON";
    /*** 阀操作命令: 关闭负压阀 */
    public static final String VALVE_NEG_OFF = "NegOFF";


    /*** 预热返回: Preheat complete [加热到的设置目标值] */
    public static final String PREHEAT_COMPLETE = "Preheat complete";
    /*** 预热返回: 无加热对象，提示需要放置液袋到加热托盘上 */
    public static final String PREHEAT_NO_HEATINGTARGET = "Preheat NoHeatingtarget";
    /*** 预热返回: 发现无加热对象后，在找到加热对象后向上位机发送 */
    public static final String PREHEAT_HEATINGTARGET = "Preheat HeatingTarget";

    /*** 预冲返回: 自动预冲flush */
    public static final String AUTORINSE_FLUSH = "autorinse/flush";
    /*** 预冲返回: 自动预冲supply */
    public static final String AUTORINSE_SUPPLY = "autorinse/supply";
    /*** 预冲返回: 预冲成功完成 */
    public static final String FIRSTRINSE_COMPLETE = "FirstRinseComplete";
    /*** 预冲返回: 返回步骤二故障（关闭灌注阀，补液阀仍然打开） */
    public static final String FIRSTRINSE_FAULT2 = "FirstRinsefault2";
    /*** 预冲返回: 返回步骤三故障（关掉补液阀，开始灌注） */
    public static final String FIRSTRINSE_FAULT3 = "FirstRinsefault3";
    /*** 预冲返回: 返回步骤四故障（打开引流阀）*/
    public static final String FIRSTRINSE_FAULT4 = "FirstRinsefault4";


    /*** 引流返回: 开始引流(非TPD模式、常规模式) */
    public static final String TREATMENT_DRAIN_START_NTPD = "TREATMENTDrainNTPDStart";
    /*** 引流返回: 开始引流(TPD模式) */
    public static final String TREATMENT_DRAIN_START_TPD = "TREATMENTDrainTPDStart";

    /*** 引流返回: 完成治疗引流(NTPD模式)  [实际引流量] [引流时间] [剩余留腹] [周期] */
    public static final String TREATMENT_DRAIN_COMPLETE_NTPD = "TREATMENTDrainNTPDComplete";
    /*** 引流返回: 完成治疗引流(TPD模式)  [实际引流量] [引流时间] [剩余留腹] [周期] */
    public static final String TREATMENT_DRAIN_COMPLETE_TPD = "TREATMENTDrainTPDComplete";

    /*** 引流返回: 引流故障(NTPD模式) */
    public static final String TREATMENT_DRAIN_FAULT_NTPD = "TREATMENTDrainNTPDFault";
    /*** 引流返回: 引流故障(TPD模式) */
    public static final String TREATMENT_DRAIN_FAULT_TPD = "TREATMENTDrainTPDFault";

    /*** 引流返回: 引流冲洗故障(NTPD模式)  */
    public static final String TREATMENT_DRAIN_PERFUSION_FAULT_NTPD = "DrainPerfusionNTPDFault";
    /*** 引流返回: 引流冲洗故障(TPD模式)  */
    public static final String TREATMENT_DRAIN_PERFUSION_FAULT_TPD = "DrainPerfusionTPDFault";

    /*** 引流返回: 开始排空等待(NTPD模式) */
    public static final String TREATMENT_DRAIN_EMPTY_WAITING_START_NTPD = "NTPDEmptyWaitingStart";
    /*** 引流返回: 开始排空等待(TPD模式) */
    public static final String TREATMENT_DRAIN_EMPTY_WAITING_START_TPD = "TPDEmptyWaitingStart";

    /*** 引流返回: 负压引流任务创建时提示 */
    public static final String TREATMENT_DRAIN_NEGDRAIN_START = "NegDrainStart";
    /*** 引流返回: 负压引流结束 */
    public static final String TREATMENT_DRAIN_NEGDRAIN_END = "NegPreDrainEnd";

    /*** 引流返回: 实时引流量 */
    public static final String TREATMENT_DRAIN_CURRENT_VOLUME = "CurrentDrain";
    /*** 引流返回: 当前冲洗量、冲洗次数 */
    public static final String TREATMENT_FLUSH_REFRESH = "FlushRefresh";
    /*** 引流返回: 更新引流目标 */
    public static final String TREATMENT_DRAIN_REFRESH_TARGET = "DrainRefresh";

    /*** 引流返回: 停止周期引流的二次确认提示 */
    public static final String TREATMENT_DRAIN_FAULT_WAIT_USER_ENSURE = "TREATMENTstart DrainFault WaitUserEnsure";


    /*** 灌注返回: 周期灌注开始 CurrentCycleStart [周期] [灌注量] [单位] */
    public static final String TREATMENT_PERFUSION_CURRENT_CYCLE_START = "CurrentCycleStart";
    /*** 灌注返回: 周期灌注完成 CurrentCycleEnd [实际灌注量] g [灌注时间] [灌注后的留腹量] [周期] */
    public static final String TREATMENT_PERFUSION_CURRENT_CYCLE_END = "CurrentCycleEnd";
    /*** 灌注返回: 周期灌注故障 TREATMENTPerfusionfault [实际灌注量] g */
    public static final String TREATMENT_PERFUSION_FAULT = "TREATMENTPerfusionfault";
    /*** 灌注返回: 周期实时灌注量 CurrentPerfusion [实时灌注量] */
    public static final String TREATMENT_PERFUSION_CURRENT_VOLUME = "CurrentPerfusion";
    /*** 灌注返回: 停止周期灌注的二次确认提示 */
    public static final String TREATMENT_PERFUSION_FAULT_WAIT_USER_ENSURE = "TREATMENTstart Perfusionfault WaitUserEnsure";


    /*** 留腹返回: 留腹等待 WaitingPeriod [留腹时间] */
    public static final String TREATMENT_WAITING_PERIOD = "WaitingPeriod";
    /*** 留腹返回: 实际留腹的时间 WaitingTime [实际留腹的时间秒] */
    public static final String TREATMENT_WAITING_TIME = "WaitingTime";
    /*** 返回:  RealTimeData [实时留腹量] */
    public static final String TREATMENT_WAITING_CURRENT_VOLUME = "RealTimeData";


    /*** 补液返回: 补液开始 */
    public static final String TREATMENT_SUPPLY_START = "SupplyStart";
    /*** 补液返回: 补液完成 */
    public static final String TREATMENT_SUPPLY_COMPLETE = "SupplyComplete";
    /*** 补液返回: 停止补液 TREATMENTSupplyStop  [当前上位称重量]  */
    public static final String TREATMENT_SUPPLY_STOP = "TREATMENTSupplyStop";
    /*** 补液返回: 补液故障 TREATMENTSupplyfault [当前上位称重量]  */
    public static final String TREATMENT_SUPPLY_FAULT = "TREATMENTSupplyfault";
    /*** 补液返回: 停止补液治疗的二次确认提示 */
    public static final String TREATMENT_SUPPLY_FAULT_WAIT_USER_ENSURE = "TREATMENTstart Supplyfault WaitUserEnsure";
    /*** 补液返回: 跳过补液成功  */
    public static final String TREATMENT_SUPPLY_FAULT_COMPLETE_SUCCESS = "SupplyfaultCompleteSuccess";


    /*** 治疗返回: 当前周期完成（灌注->留腹->引流 完成） DataList:Period [周期] [灌注量] [辅助冲洗量] [灌注后留腹量] [引流目标量] [引流量] [预估腹腔剩余液体量] */
    public static final String TREATMENT_DATALIST_CURRENT_PERIOD = "DataList:cycle";

    /*** 治疗返回: 治疗程序已终止 */
    public static final String TREATMENT_CYCLE_STOP = "TREATMENTCycleStop";
    /*** 治疗返回: 治疗完成 TREATMENTComplete [总共使用液体重量] g [超滤] g */
    public static final String TREATMENT_COMPLETE = "TREATMENTComplete";

    /*** 强制结束治疗: 停止治疗的二次确认提示 */
    public static final String TREATMENT_FORCED_END = "TREATMENTForcedEnd";


/********************************************************* 温控参数设置 start ***********************************************************************/

    /*** 返回成功 温度参数设置: T1,参数param1(液袋温度，最大温差) */
    public static final String TEMPERATURE_PARAM_1_SUCCESS = "Param1SetSuccess";
    /*** 返回成功 温度参数设置: T2,参数param2(加热温度，目标值) */
    public static final String TEMPERATURE_PARAM_2_SUCCESS = "Param2SetSuccess";
    /*** 返回成功 温度参数设置: T3,参数param3(上下回差) */
    public static final String TEMPERATURE_PARAM_3_SUCCESS = "Param3SetSuccess";
    /*** 返回成功 温度参数设置: T5,参数param5(板温温度上限) */
    public static final String TEMPERATURE_PARAM_5_SUCCESS = "Param5SetSuccess";
    /*** 返回成功 温度参数设置: T6,参数param6（目标温度差） */
    public static final String TEMPERATURE_PARAM_6_SUCCESS = "Param6SetSuccess";
    /*** 返回成功 温度参数设置: T7,参数param7（加热板温度调低值） */
    public static final String TEMPERATURE_PARAM_7_SUCCESS = "Param7SetSuccess";
    /*** 返回成功 温度参数设置: 参数TaE  */
    public static final String TEMPERATURE_PARAM_TAE_SUCCESS = "TaESetSuccess";
    /*** 返回成功 温度参数设置: 参数TbE  */
    public static final String TEMPERATURE_PARAM_TBE_SUCCESS = "TbESetSuccess";
    /*** 返回成功 温度参数设置: 参数TcE  */
    public static final String TEMPERATURE_PARAM_TCE_SUCCESS = "TcESetSuccess";


    /*** 返回失败 温度参数设置: T1,参数param1(液袋温度，最大温差) */
    public static final String TEMPERATURE_PARAM_1_FAIL = "Param1SetFail";
    /*** 返回失败 温度参数设置: T2,参数param2(加热温度，目标值) */
    public static final String TEMPERATURE_PARAM_2_FAIL = "Param2SetFail";
    /*** 返回失败 温度参数设置: T3,参数param3(上下回差) */
    public static final String TEMPERATURE_PARAM_3_FAIL = "Param3SetFail";
    /*** 返回失败 温度参数设置: T5,参数param5(板温温度上限) */
    public static final String TEMPERATURE_PARAM_5_FAIL = "Param5SetFail";
    /*** 返回失败 温度参数设置: T6,参数param6（目标温度差） */
    public static final String TEMPERATURE_PARAM_6_FAIL = "Param6SetFail";
    /*** 返回失败 温度参数设置: T7,参数param7（加热板温度调低值） */
    public static final String TEMPERATURE_PARAM_7_FAIL = "Param7SetFail";
    /*** 返回失败 温度参数设置: 参数TaE  */
    public static final String TEMPERATURE_PARAM_TAE_FAIL = "TaESetFail";
    /*** 返回失败 温度参数设置: 参数TbE  */
    public static final String TEMPERATURE_PARAM_TBE_FAIL = "TbESetFail";
    /*** 返回失败 温度参数设置: 参数TcE  */
    public static final String TEMPERATURE_PARAM_TCE_FAIL = "TcESetFail";

/********************************************************* 温控参数设置 start ***********************************************************************/


/********************************************************* 显示屏LED start ***********************************************************************/

    /*** 返回 显示屏LED通讯指令: 点亮AI显示屏 */
    public static final String LED_AI_DISPLAY_START = "StartDisplay";
    /*** 返回 显示屏LED通讯指令: 关闭AI显示屏 */
    public static final String LED_AI_DISPLAY_END = "EndDisplay";
    /*** 返回 显示屏LED通讯指令: 点亮补液LED */
    public static final String LED_SUPPLY_START = "StartSupplyLED";
    /*** 返回 显示屏LED通讯指令: 关闭补液LED */
    public static final String LED_SUPPLY_END = "EndSupplyLED";
    /*** 返回 显示屏LED通讯指令: 点亮灌注LED */
    public static final String LED_PERFUSION_START = "StartPerfusionLED";
    /*** 返回 显示屏LED通讯指令: 关闭灌注LED */
    public static final String LED_PERFUSION_END = "EndPerfusionLED";
    /*** 返回 显示屏LED通讯指令: 点亮引流LED */
    public static final String LED_DRAIN_START = "StartDrainLED";
    /*** 返回 显示屏LED通讯指令: 关闭引流LED */
    public static final String LED_DRAIN_END = "EndDrainLED";

/********************************************************* 显示屏LED end ***********************************************************************/

}
