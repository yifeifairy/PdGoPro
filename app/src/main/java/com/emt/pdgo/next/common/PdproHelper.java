package com.emt.pdgo.next.common;


import android.text.TextUtils;
import android.util.Log;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.bean.AapdBean;
import com.emt.pdgo.next.data.bean.CycleBean;
import com.emt.pdgo.next.data.bean.DeviceStatusInfo;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.ExpertBean;
import com.emt.pdgo.next.data.bean.FirstRinseParameterBean;
import com.emt.pdgo.next.data.bean.KidBean;
import com.emt.pdgo.next.data.bean.OtherParamBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.PidBean;
import com.emt.pdgo.next.data.bean.ReplayEntity;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.TpdBean;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
import com.emt.pdgo.next.data.bean.TreatmentWeightParameterBean;
import com.emt.pdgo.next.data.bean.UserParameterBean;
import com.emt.pdgo.next.model.dpr.machine.Prescription;
import com.emt.pdgo.next.model.dpr.machine.param.DprOtherParam;
import com.emt.pdgo.next.model.dpr.machine.param.DrainParam;
import com.emt.pdgo.next.model.dpr.machine.param.PerfuseParam;
import com.emt.pdgo.next.model.dpr.machine.param.RetainParam;
import com.emt.pdgo.next.model.dpr.machine.param.SupplyParam;
import com.emt.pdgo.next.model.mode.CcpdBean;
import com.emt.pdgo.next.model.mode.CfpdBean;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.net.bean.UserInfoBean;
import com.emt.pdgo.next.util.CacheUtils;

/**
 * @author chenjh
 * @date 2018/11/16 09:43
 */
public class PdproHelper {


    private static PdproHelper instance;
//
//    private PdproHelper(Context context) {
//        this.context = context.getApplicationContext();
//    }

    private PdproHelper() {

    }

    public static PdproHelper getInstance() {
        if (instance == null) {
            instance = new PdproHelper();
        }
        return instance;
    }

    /**
     * app的服务端接口地址
     *
     * @return
     */
    public String getAppServerUrl() {
        try {
            return CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.PDP_APP_SERVER_URL);
        } catch (Exception e) {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.PDP_APP_SERVER_URL.toLowerCase(), "https://ejc.ckdcloud.com/api/");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 远程处方时间
     *
     * @return
     */
    public String getPrescriptTime() {
        try {
            return CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.PRESCRIPT_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 设备编号
     *
     * @return
     */
    public String getMachineSN() {
        try {
            return CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.PDP_MACHINE_SN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 用户ID
     *
     * @return
     */
    public String getUid() {
        try {
            return CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.UID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 出厂日期
     * @return
     */
    public int useDeviceTime() {
        try {
            return Integer.parseInt(CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.useDeviceTime));
        } catch (Exception e) {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.useDeviceTime, 1+"");
            e.printStackTrace();
        }
        return 0;
    }

    public int autoSleep() {
        try {
            return Integer.parseInt(CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.AUTO_SLEEP));
        } catch (Exception e) {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.AUTO_SLEEP, 2+"");
            e.printStackTrace();
        }
        return 2;
    }

    /**
     * 电机测试
     * @return 秒
     */
    public int valueTest() {
        try {
            return Integer.parseInt(CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.VALUE_TEST));
        } catch (Exception e) {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.VALUE_TEST, 10+"");
            e.printStackTrace();
        }
        return 10;
    }

    public int brightness() {
        try {
            return Integer.parseInt(CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.BRIGHTNESS));
        } catch (Exception e) {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.BRIGHTNESS,100+"");
            e.printStackTrace();
        }
        return 100;
    }

    /**
     * 获取TTS语音功能是否打开
     *
     * @return
     */
    public boolean getTtsSoundOpen() {

        try {
            String ttsSoundOpen = CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.ttsSoundOpen);
            if (TextUtils.isEmpty(ttsSoundOpen)) {
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.ttsSoundOpen, "true");
                return true;
            } else {
                return ttsSoundOpen.equals("true");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 设置TTS语音功能是否打开
     *
     * @param isOpen
     */
    public void updateTtsSoundOpen(boolean isOpen) {
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.ttsSoundOpen, isOpen ? "true" : "false");
    }

    public ReplayEntity replayEntity() {
        ReplayEntity entity = null;
        try {
            entity = (ReplayEntity) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.REPLAY_ENTITY);
            if (entity == null) {
                entity = new ReplayEntity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;

    }

    public Prescription getPrescription() {
        Prescription prescription = null;
        try {
            prescription = (Prescription) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.DRP_PRESCRIPTION);
            if (prescription == null) {
                prescription = new Prescription();
            }
        } catch (Exception e) {
            Log.e("pdHelper",e.getLocalizedMessage());
        }
        return prescription;
    }

    public CfpdBean getCfpdBean() {
        CfpdBean prescription = null;
        try {
            prescription = (CfpdBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.cFpd);
            if (prescription == null) {
                prescription = new CfpdBean();
            }
        } catch (Exception e) {
            Log.e("pdHelper",e.getLocalizedMessage());
        }
        return prescription;
    }

    public KidBean kidBean() {
        KidBean kidBean = null;
        try {
            kidBean = (KidBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.KID_PARAMS);
            if (kidBean == null) {
                kidBean = new KidBean();
//                mData.isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
//                mData.drainThresholdZeroCycle = getDrainParameterBean().zeroCyclePercentage;
//                mData.drainThresholdOtherCycle = getDrainParameterBean().otherCyclePercentage;
//                mData.perfusionWarningValue = getPerfusionParameterBean().maxWarningValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kidBean;
    }

    public AapdBean aapdBean() {
        AapdBean aapdBean = null;
        try {
            aapdBean = (com.emt.pdgo.next.data.bean.AapdBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.aApd_PARAMS);
            if (aapdBean == null) {
                aapdBean = new AapdBean();
//                mData.isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
//                mData.drainThresholdZeroCycle = getDrainParameterBean().zeroCyclePercentage;
//                mData.drainThresholdOtherCycle = getDrainParameterBean().otherCyclePercentage;
//                mData.perfusionWarningValue = getPerfusionParameterBean().maxWarningValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aapdBean;
    }

    public TpdBean tpdBean() {
        TpdBean tpdBean = null;
        try {
            tpdBean = (TpdBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.TPD_PARAMS);
            if (tpdBean == null) {
                tpdBean = new TpdBean();
//                mData.isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
//                mData.drainThresholdZeroCycle = getDrainParameterBean().zeroCyclePercentage;
//                mData.drainThresholdOtherCycle = getDrainParameterBean().otherCyclePercentage;
//                mData.perfusionWarningValue = getPerfusionParameterBean().maxWarningValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tpdBean;
    }

    public IpdBean ipdBean() {
        IpdBean tpdBean = null;
        try {
            tpdBean = (IpdBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.IPD_BEAN);
            if (tpdBean == null) {
                tpdBean = new IpdBean();
//                mData.isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
//                mData.drainThresholdZeroCycle = getDrainParameterBean().zeroCyclePercentage;
//                mData.drainThresholdOtherCycle = getDrainParameterBean().otherCyclePercentage;
//                mData.perfusionWarningValue = getPerfusionParameterBean().maxWarningValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tpdBean;
    }

    public CcpdBean ccpdBean() {
        CcpdBean tpdBean = null;
        try {
            tpdBean = (CcpdBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.CCPD_BEAN);
            if (tpdBean == null) {
                tpdBean = new CcpdBean();
//                mData.isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
//                mData.drainThresholdZeroCycle = getDrainParameterBean().zeroCyclePercentage;
//                mData.drainThresholdOtherCycle = getDrainParameterBean().otherCyclePercentage;
//                mData.perfusionWarningValue = getPerfusionParameterBean().maxWarningValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tpdBean;
    }

    public PidBean pidBean() {
        PidBean bean = null;
        try {
            bean = (PidBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.PID_BEAN);
            if (bean == null) {
                bean = new PidBean();
//                mData.isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
//                mData.drainThresholdZeroCycle = getDrainParameterBean().zeroCyclePercentage;
//                mData.drainThresholdOtherCycle = getDrainParameterBean().otherCyclePercentage;
//                mData.perfusionWarningValue = getPerfusionParameterBean().maxWarningValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public ExpertBean expertBean() {
        ExpertBean bean = null;
        try {
            bean = (ExpertBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.EXPERT_PARAMS);
            if (bean == null) {
                bean = new ExpertBean();
//                mData.isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
//                mData.drainThresholdZeroCycle = getDrainParameterBean().zeroCyclePercentage;
//                mData.drainThresholdOtherCycle = getDrainParameterBean().otherCyclePercentage;
//                mData.perfusionWarningValue = getPerfusionParameterBean().maxWarningValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public TreatmentParameterEniity getTreatmentParameter() {

        TreatmentParameterEniity mData = null;

        try {
            mData = (TreatmentParameterEniity) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.TREATMENT_PARAMETER);
            if (mData == null) {
                mData = new TreatmentParameterEniity();
//                mData.isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
//                mData.drainThresholdZeroCycle = getDrainParameterBean().zeroCyclePercentage;
//                mData.drainThresholdOtherCycle = getDrainParameterBean().otherCyclePercentage;
//                mData.perfusionWarningValue = getPerfusionParameterBean().maxWarningValue;
            }
        } catch (Exception e) {

        }
        return mData;
    }

    public OtherParamBean getOtherParamBean() {
        OtherParamBean bean = null;
        try {
            bean = (OtherParamBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.OTHER_PARAMETER);
            if (bean == null) {
                bean = new OtherParamBean();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }


    /**
     * 获取预冲参数
     *
     * @return
     */
    public FirstRinseParameterBean getFirstRinseParameterBean() {

        FirstRinseParameterBean mData = null;

        try {
            mData = (FirstRinseParameterBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.FIRST_RINSE_PARAMETER);
            if (mData == null) {
                mData = new FirstRinseParameterBean();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    /**
     * 获取引流参数
     *
     * @return
     */
    public DrainParameterBean getDrainParameterBean() {

        DrainParameterBean mData = null;

        try {
            mData = (DrainParameterBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.DRAIN_PARAMETER);
            if (mData == null) {
                mData = new DrainParameterBean();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    public RetainParamBean getRetainParamBean() {

        RetainParamBean mData = null;

        try {
            mData = (RetainParamBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.RETAIN_PARAM);
            if (mData == null) {
                mData = new RetainParamBean();
            }
        } catch (Exception e) {

        }
        return mData;
    }

//    /**
//     * 获取灌注参数
//     *
//     * @return
//     */
    public PerfusionParameterBean getPerfusionParameterBean() {

        PerfusionParameterBean mData = null;

        try {
            mData = (PerfusionParameterBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.PERFUSION_PARAMETER);
            if (mData == null) {
                mData = new PerfusionParameterBean();
            }
        } catch (Exception e) {

        }
        return mData;
    }


    /**
     * 获取补液参数
     *
     * @return
     */
    public SupplyParameterBean getSupplyParameterBean() {

        SupplyParameterBean mData = null;

        try {
            mData = (SupplyParameterBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.SUPPLY_PARAMETER);
            if (mData == null) {
                mData = new SupplyParameterBean();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    public String getPhone() {
        try {
            return CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.PHONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public SupplyParam getSupplyParam() {

        SupplyParam mData = null;

        try {
            mData = (SupplyParam) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.DPR_SUPPLY_PARAM);
            if (mData == null) {
                mData = new SupplyParam();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    public DprOtherParam getDprOtherParam() {

        DprOtherParam mData = null;

        try {
            mData = (DprOtherParam) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.DPR_OTHER_PARAM);
            if (mData == null) {
                mData = new DprOtherParam();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    public RetainParam getRetainParam() {

        RetainParam mData = null;

        try {
            mData = (RetainParam) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.DPR_RETAIN_PARAM);
            if (mData == null) {
                mData = new RetainParam();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    public PerfuseParam getPerfuseParam() {

        PerfuseParam mData = null;

        try {
            mData = (PerfuseParam) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.DPR_PERFUSION_PARA);
            if (mData == null) {
                mData = new PerfuseParam();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    public DrainParam getDrainParam() {

        DrainParam mData = null;

        try {
            mData = (DrainParam) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.DPR_DRAIN_PARAM);
            if (mData == null) {
                mData = new DrainParam();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    /**
     * 获取治疗之前体重参数
     *
     * @return
     */
    public TreatmentWeightParameterBean getTreatmentWeightParameterBean() {

        TreatmentWeightParameterBean mData = null;

        try {
            mData = (TreatmentWeightParameterBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.TREATMENT_BEFORE_WEIGHT_PARAMETER);
            if (mData == null) {
                mData = new TreatmentWeightParameterBean();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    public CycleBean cycleBean() {

        CycleBean mData = null;

        try {
            mData = (CycleBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.CYCLE_PARAMS);
            if (mData == null) {
                mData = new CycleBean();
            }
        } catch (Exception e) {

        }
        return mData;
    }

    /**
     * 获取设备参数
     *
     * @return
     */
    public DeviceStatusInfo getDeviceStatusInfo() {

        DeviceStatusInfo mData = null;

        try {
            if (mData == null) {
                mData = new DeviceStatusInfo();
            }
        } catch (Exception e) {

        }
        return mData;
    }


    /**
     * 获取用户参数配置
     *
     * @return
     */
    public UserParameterBean getUserParameterBean() {

        UserParameterBean mData = null;

        try {
            mData = (UserParameterBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.USER_PARAMETER);
            if (mData == null) {
                Log.e("获取用户参数配置", "未设置" );
                mData = new UserParameterBean();
                mData.isHospital = false;
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, mData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mData;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfoBean getUserInfoBean() {

        UserInfoBean mData = null;

        try {
            mData = (UserInfoBean) CacheUtils.getInstance().getACache().getAsObject(PdGoConstConfig.USER_INFO);
            if (mData == null) {
                mData = new UserInfoBean();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mData;
    }

}
