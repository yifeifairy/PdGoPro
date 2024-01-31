package com.emt.pdgo.next.data.bean;

import java.io.Serializable;

/**
 * 设备自检返回的状态：
 *
 * @author chenjh
 * @date 2021/01/14 11:30
 */
public class SelfCheckDeviceStatus extends BaseBean implements Serializable {

    private static final long serialVersionUID = -5884392926961695080L;

    /*** 报警模块 */
    public boolean alarmSystemOK;

    /*** 引流模块：负压阀是否正常 */
    public boolean moduleNegOK;
    /*** 补液模块 */
    public boolean moduleSupplyOK;

    public boolean moduleSupply2OK;

    /*** 灌注模块 */
    public boolean modulePerfusionOK;
    /*** 引流模块：引流阀是否正常*/
    public boolean moduleDrainOK;
    /*** 安全阀 */
    public boolean moduleSafeOK;
    /*** 透析液模块 */
    public boolean moduleUpperWeightOK;
    /*** 废液模块 */
    public boolean moduleLowerWeightOK;

    /*** 加热模块 */
    public boolean moduleHeatingOK;
    /*** 控制模块 */
    public boolean moduleControlOK;

    /*** 新加模块 */
    public boolean t0;
    public boolean t1;
    public boolean t2;



    /*** 温度过高 */
    public boolean overtemp;

    public void initModule(){
        alarmSystemOK = false;
        moduleHeatingOK = false;
        moduleControlOK = false;
        moduleNegOK = false;
        moduleSupplyOK = false;
        modulePerfusionOK = false;
        moduleDrainOK = false;
        moduleUpperWeightOK = false;
        moduleLowerWeightOK = false;
        moduleSafeOK = false;
        t0 = false;
        t1 = false;
        t2 = false;
        moduleSupply2OK = false;
    }

    public boolean selfCheckResult(){

//        return moduleHeatingOK && moduleControlOK && moduleNegOK && moduleSupplyOK
//                && modulePerfusionOK && moduleDrainOK && moduleUpperWeightOK && moduleLowerWeightOK;
        return moduleHeatingOK  && moduleNegOK && moduleSupplyOK  && t0 &&t1&&t2&&moduleSupply2OK
                && modulePerfusionOK && moduleDrainOK && moduleUpperWeightOK && moduleLowerWeightOK;
    }

}
