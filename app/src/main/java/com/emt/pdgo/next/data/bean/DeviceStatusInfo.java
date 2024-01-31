package com.emt.pdgo.next.data.bean;

/**
 * 设备状态：
 * 最后灌注阀(LastON/LastOFF)
 * 补液阀(SupplyON/SupplyOFF)
 * 灌注阀(PerfusionON/PerfusionOFF)
 * 引流阀(DrainON/DrainOFF)
 * 强制阀(ForceON/ForceOFF)
 * 上位称初始重量g
 * 下位称初始重量g
 * 液袋温度摄氏度(除以10使用)
 * 上位称重量g
 * 下位称重量g
 *
 * @author chenjh
 * @date 2019/5/8 16:06
 */
public class DeviceStatusInfo extends BaseBean {

    /*** 负压阀 */
    public boolean isNegON;
    /*** 补液阀 */
    public boolean isSupplyON;
    /*** 灌注阀 */
    public boolean isPerfusionON;
    /*** 引流阀 */
    public boolean isDrainON;
    /*** 安全阀 */
    public boolean isSafeON;

    /*** 上位称初始重量g */
    public int upWeightInitialValue;
    /*** 下位称初始重量g */
    public int lowWeightInitialValue;
    /*** 液袋温度摄氏度(除以10使用) */
    public float dialysateTemperature;
    /*** 上位称重量g */
    public int upWeight;
    /*** 下位称重量g */
    public int lowWeight;
    /*** 是否断电 */
    public int isAcPowerIn;
    /*** 关机 */
    public int batteryVoltage;

//    @Override
//    public String toString() {
//        return "DeviceStatusInfo{" +
//                "upWeightInitialValue=" + upWeightInitialValue +
//                ", lowWeightInitialValue='" + lowWeightInitialValue + '\'' +
//                ", dialysateTemperature=" + dialysateTemperature +
//                ", upWeight=" + upWeight +
//                ", lowWeight=" + lowWeight +
//                ", isNegON=" + isNegON +
//                ", isSupplyON='" + isSupplyON + '\'' +
//                ", isPerfusionON='" + isPerfusionON + '\'' +
//                ", isDrainON=" + isDrainON +
//                ", isSafeON=" + isSafeON +
//                '}';
//    }

}
