package com.emt.pdgo.next.util;

public class ConverseUtil {

    /**
     * 渗透压换算 π=cRT
     * c为摩尔浓度，单位：mol/L，也可以算作C=n/V（物质的量(mol)/体积(L)）
     * R为理想气体常数，当π的单位为kPa，V的单位为升（L）时，R值为8.314J·K-1·mol-1。
     * T为热量，单位：K（开尔文），与摄氏度的换算关系是 T(K) = 273+T(C)，例：25摄氏度=298开尔文。
//     * π为稀溶液的渗透压，c为溶液的浓度，R为气体常数d等于8.31，n为溶质的物质的量，T为绝对温度
     */
    public static double osmConverse(double concentration, double temp) {
        return 0.1 * concentration * (273 + temp) * 8.31;
    }

    /**
     * c为摩尔浓度，单位：mol/L，也可以算作C=n/V（物质的量(mol)/体积(L)）
     */
    public static void concentrationCal() {

    }

}
