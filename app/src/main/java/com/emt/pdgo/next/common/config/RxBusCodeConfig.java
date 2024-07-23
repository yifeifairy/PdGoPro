package com.emt.pdgo.next.common.config;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.common.config
 * @ClassName: RxBusCodeConfig
 * @Description: RxBusCode
 * @Author: chenjh
 * @CreateDate: 2020/1/6 4:19 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/6 4:19 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */

public class RxBusCodeConfig {

//    private final static RxBusCodeConfig instance = null;
//
//    private RxBusCodeConfig() {
//
//    }
//
//    public static RxBusCodeConfig getInstance() {
//
//        return instance;
//    }

    /*** 电池管理 */
    public static final int EVENT_POWER_MONITOR = 110;



    /*** 发现 usb设备 */
    public static final int EVENT_USB_DEVICE_ATTACHED = 9801;
    /*** USB设备拔出 */
    public static final int EVENT_ACTION_USB_DEVICE_DETACHED = 9802;

    // 调试信息
    public static final int DEBUG_EVENT_COMMAND = 9901;
    /*** 设备信息：阀相关信息、上位秤和下位秤数值、温控传感器数值 */
    public static final int DEBUG_EVENT_RECEIVE_DEVICE_STATUS = 9902;

//    // 打开主板串口
//    public static final int EVENT_MAIN_BOARD_OPEN_SERIAL_OK = 80;
//    // 控制板连接失败
//    public static final int EVENT_MAIN_BOARD_OPEN_SERIAL_ERROR = 81;
//    // 主板串口读取数据失败
//    public static final int EVENT_MAIN_BOARD_READ_DATA_ERROR = 82;
//    // 主板串口发送数据失败
//    public static final int EVENT_MAIN_BOARD_SEND_DATA_ERROR = 83;
//    // 控制板故障，连接失败
//    public static final int EVENT_MAIN_BOARD_READ_DATA_TIME_OUT = 84;
//    // 主板串口已关闭
//    public static final int EVENT_MAIN_BOARD_SERIAL_CLOSED = 85;

    // 设置上下位秤去皮成功
    public final static int EVENT_CMD_RESULT_OK = 1001;

    // 设置上下位秤去皮成功
    public final static int EVENT_CMD_RESULT_ERR = 1002;

    // 设置上下位秤去皮成功
    public final static int EVENT_FIRST_WEIGH_TARE_ALL_OK = 1101;
    // 设置上下位秤去皮失败
    public final static int EVENT_FIRST_WEIGH_TARE_ALL_ERR = 1102;

    public final static int EVENT_CMD_thermostat_OK = 11111;

    /*** 阀自检 */
    public static final int EVENT_RECEIVE_SELF_CHECK_VALVE = 2001;
    /*** 称自检 */
    public static final int EVENT_RECEIVE_SELF_CHECK_WEIGHT = 2002;
    /*** 温控自检 */
    public static final int EVENT_RECEIVE_SELF_CHECK_THERMOSTAT = 2003;

    /*** 电源管理 1、AC220V断开 */
    public static final int EVENT_RECEIVE_AC_POWER_OFF = 3001;
    /*** 电源管理 2、AC220V连接 */
    public static final int EVENT_RECEIVE_AC_POWER_ON = 3002;
    /*** 电源管理 3、AC掉电超过30分钟 */
    public static final int EVENT_RECEIVE_AC_POWER_FAULT = 3003;

    /*** 获取设备信息 */
    public static final int EVENT_RECEIVE_DEVICE_INFO = 1000;

    // 打开主板串口
    public final static int EVENT_MAIN_BOARD_OK = 90;
    // 主板错误
    public final static int EVENT_MAIN_BOARD_ERROR = 91;
    // 打开LED主板串口
    public static final int EVENT_LED_BOARD_OK = 92;
    // LED主板错误
    public static final int EVENT_LED_BOARD_ERROR = 93;

    public static final int Firmware_Upgrade = 95;


    // 事件：上位机发送指令给下位机
    public static final int EVENT_SEND_COMMAND = 101;

    public static final int EVENT_SEND_COMMAND_10001 = 10001;

    // 事件：下位机返回上位机数据
    public static final int EVENT_RECEIVE_COMMAND = 102;

    // 事件：上位机发送指令给LED下位机
    public static final int EVENT_SEND_LED_COMMAND = 103;

    // 事件：LED下位机返回数据
    public static final int EVENT_RECEIVE_LED_COMMAND = 104;

    /*** 事件：握手 */
    public static final int EVENT_RECEIVE_COMMAND_HELLO = 105;

    /*** 事件：秤系数值设定 */
    public static final int EVENT_RECEIVE_COMMAND_WEIGH_COEFF = 106;

    /*** 设备信息：阀相关信息、上位秤和下位秤数值、温控传感器数值 */
    public static final int EVENT_RECEIVE_DEVICE_STATUS = 107;

    // 事件：下位机返回上位机数据
    public static final int EVENT_RECEIVE_OTHER = 108;

    /*** 自检结果 */
    public static final int EVENT_RECEIVE_SYSSELF_DATA = 110;

    /*** 预热 */
    public static final int EVENT_RECEIVE_PREHEAT_DATA = 200;

    /*** 安装管路 */
    public static final int EVENT_RECEIVE_PIPECART= 300;

//    /*** 蓝牙设备 */
//    public static final int EVENT_INPUT_BODY_DATA_BLUETOOTH_DEVICE = 510;
//    /*** 蓝牙设备 */
//    public static final int EVENT_INPUT_BODY_DATA_BLUETOOTH_DEVICE_CONNECTLS = 511;
    /*** 治疗前体重 */
    public static final int EVENT_INPUT_BODY_DATA_WEIGHT = 501;
    /*** 治疗前心率 */
    public static final int EVENT_INPUT_BODY_DATA_HEART_RATE = 502;
    /*** 治疗前收缩压 */
    public static final int EVENT_INPUT_BODY_DATA_SBP = 503;
    /*** 治疗前舒张压 */
    public static final int EVENT_INPUT_BODY_DATA_DBP = 504;
    /*** 血压计返回的数据 */
    public static final int EVENT_INPUT_BODY_DATA_BLOOD = 505;



    /*** 管路预冲 */
    public static final int EVENT_RECEIVE_FIRSTRINSE_DATA = 700;
    /*** 管路预冲 */
    public static final int EVENT_RECEIVE_AUTORINSE_FLUSH_DATA = 700;

    /*** 自动预冲 */
    public static final int EVENT_RECEIVE_AUTO_RINSE_DATA = 702;

    /*** 治疗数据 */
    public static final int EVENT_RECEIVE_TREATMENT_DATA = 888;
    /*** 灌注数据 */
    public static final int EVENT_TREATMENT_PERFUSION_DATA = 801;
    /*** 留腹数据 */
    public static final int EVENT_TREATMENT_RETAIN_DATA = 802;
    /*** 引流数据 */
    public static final int EVENT_TREATMENT_DRAIN_DATA = 803;
    /*** 补液数据 */
    public static final int EVENT_TREATMENT_SUPPLY_DATA = 804;
    /*** 其他治疗数据 */
    public static final int EVENT_TREATMENT_OTHER_DATA = 800;

    /*** 设置温度 */
    public static final int EVENT_TREATMENT_TEMPERATURE_SET = 820;
    /*** 温控数据 */
    public static final int EVENT_RECEIVE_THERMOSTAT = 821;
    /*** 温控参数数据 */
    public static final int EVENT_RECEIVE_THERMOSTAT_SET = 822;

    /*** 手动预冲数据 */
    public static final int EVENT_RECEIVE_Manual_PRE = 823;

    /*** 停止加热 */
    public static final int EVENT_RECEIVE_STOP_HEART = 824;

    /*** 实时灌注量 */
    public static final int EVENT_TREATMENT_PERFUSION_CURRENT_VOLUME = 805;
    /*** 实时引流量 */
    public static final int EVENT_TREATMENT_DRAIN_CURRENT_VOLUME = 806;
    /*** 实时留腹量 */
    public static final int EVENT_TREATMENT_WAITING_CURRENT_VOLUME = 807;
    /*** 当前冲洗量、冲洗次数 */
    public static final int EVENT_TREATMENT_FLUSH_REFRESH = 808;
//    /*** 当前冲洗量、冲洗次数 */
//    public static final int EVENT_TREATMENT_FLUSH_REFRESH = 807;
//    /*** 更新引流目标 */
//    public static final int EVENT_TREATMENT_DRAIN_REFRESH_TARGET = 809;
    public static final int BOARD_VERSION = 810;

    public static final int EVENT_DPR = 1110;

    public static final int CALIBRATION = 1111;

    public static final int CHECK_PER = 1112;



    /*** 关闭TreatmentAlarmDialog */
    public static final int EVENT_TREATMENT_CLOSED_ALARM_DIALOG = 811;

    public static final int UPDATE_RECEIVER = 900;

    public static final int RESULT_REPORT = 901;

    public static final int NET_STATUS = 902;

    public static final int VALVE_STATUS = 903;

}
