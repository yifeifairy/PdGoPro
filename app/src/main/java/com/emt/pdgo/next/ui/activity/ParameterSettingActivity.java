package com.emt.pdgo.next.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentDrainBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentParamBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentPerfuseBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentRetainBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentSupplyBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParameterSettingActivity extends BaseActivity {

    // 引流参数
    @BindView(R.id.drainTimeIntervalEt)
    TextView drainTimeIntervalEt;
    @BindView(R.id.drainThresholdEt)
    TextView drainThresholdEt;
    @BindView(R.id.drainZeroCycleRatioEt)
    TextView drainZeroCycleRatioEt;
    @BindView(R.id.drainOtherCycleRatioEt)
    TextView drainOtherCycleRatioEt;
    @BindView(R.id.drainTimeoutAlarmEt)
    TextView drainTimeoutAlarmEt;
    @BindView(R.id.drainAuxiliaryFlushVolumeEt)
    TextView drainAuxiliaryFlushVolumeEt;
    @BindView(R.id.drainAuxiliaryFlushNumEt)
    TextView drainAuxiliaryFlushNumEt;
    @BindView(R.id.drainEmptyWaitingSwitch)
    LabeledSwitch drainEmptyWaitingSwitch;
    @BindView(R.id.drainReminderIntervalEt)
    TextView drainReminderIntervalEt;
    @BindView(R.id.drainNegativeSwitch)
    LabeledSwitch drainNegativeSwitch;

    // 灌注参数
    @BindView(R.id.infusionTimeIntervalEt)
    TextView infusionTimeIntervalEt;
    @BindView(R.id.infusionThresholdEt)
    TextView infusionThresholdEt;
    @BindView(R.id.infusionMaximumAlertEt)
    TextView infusionMaximumAlertEt;
//    @BindView(R.id.infusionIsAllowedSwitch)
//    LabeledSwitch infusionIsAllowedSwitch;

    // 补液参数
    @BindView(R.id.rehydrationTimeIntervalEt)
    TextView rehydrationTimeIntervalEt;
    @BindView(R.id.rehydrationTargetValueEt)
    TextView rehydrationTargetValueEt;
    @BindView(R.id.rehydrationThresholdEt)
    TextView rehydrationThresholdEt;
    @BindView(R.id.rehydrationMinimumEt)
    TextView rehydrationMinimumEt;

    // 留腹
    @BindView(R.id.abdomenDeductSwitch)
    LabeledSwitch abdomenDeductSwitch;
    @BindView(R.id.abdomenUltSwitch)
    LabeledSwitch abdomenUltSwitch;

//    private String drainTimeIntervalStr;
//    private String drainThresholdStr;
//    private String drainZeroCycleRatioStr;
//    private String drainOtherCycleRatioStr;
//    private String drainTimeoutAlarmStr;
//    private String drainAuxiliaryFlushVolumeStr;
//    private String drainAuxiliaryFlushNumStr;
//    private String drainReminderIntervalStr;
//
//    private String infusionTimeIntervalStr;
//    private String infusionThresholdStr;
//    private String infusionMaximumAlertStr;
//
//    private String rehydrationTimeIntervalStr;
//    private String rehydrationTargetValueStr;
//    private String rehydrationThresholdStr;
//    private String rehydrationMinimumStr;
//
//    private boolean isDrainEmptyWaiting;
//    private boolean isDrainNegative;
//
//    private boolean isAbdomenDeduct;
//    private boolean isAbdomenUlt;

    private TreatmentParameterEniity treatmentParameterEniity;//治疗参数
    private DrainParameterBean drainParameterBean;
    private PerfusionParameterBean perfusionParameterBean;
    private SupplyParameterBean supplyParameterBean;
    private RetainParamBean retainParamBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_parameter_setting);
        ButterKnife.bind(this);
        initHeadTitleBar("治疗参数设置","确定");
        treatmentParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
        retainParamBean = PdproHelper.getInstance().getRetainParamBean();
    }

    private RetainParamBean getRetainParamBean() {
        if (retainParamBean == null) {
            retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        }
        return retainParamBean;
    }

    @OnClick({R.id.drainTimeIntervalEt, R.id.drainTimeIntervalRl, R.id.drainThresholdRl, R.id.drainThresholdEt,
            R.id.drainZeroCycleRatioEt, R.id.drainZeroCycleRatioRl, R.id.drainOtherCycleRatioRl, R.id.drainOtherCycleRatioEt,
    R.id.drainTimeoutAlarmEt, R.id.drainTimeoutAlarmRl,R.id.drainAuxiliaryFlushVolumeEt, R.id.drainAuxiliaryFlushVolumeRl,
    R.id.drainAuxiliaryFlushNumRl, R.id.drainAuxiliaryFlushNumEt, R.id.drainReminderIntervalEt, R.id.drainReminderIntervalRl,
    R.id.infusionTimeIntervalEt, R.id.infusionTimeIntervalRl,R.id.infusionThresholdRl, R.id.infusionThresholdEt,
    R.id.infusionMaximumAlertEt, R.id.infusionMaximumAlertRl,R.id.rehydrationTimeIntervalEt, R.id.rehydrationTimeIntervalRl,
    R.id.rehydrationThresholdEt, R.id.rehydrationThresholdRl,R.id.rehydrationTargetValueEt, R.id.rehydrationTargetValueRl,
    R.id.rehydrationMinimumEt, R.id.rehydrationMinimumRl, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.drainTimeIntervalRl:
            case R.id.drainTimeIntervalEt:
                alertNumberBoardDialog(drainTimeIntervalEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_TIME_INTERVAL);
                break;
            case R.id.drainThresholdRl:
            case R.id.drainThresholdEt:
                alertNumberBoardDialog(drainThresholdEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE);
                break;
            case R.id.drainZeroCycleRatioRl:
            case R.id.drainZeroCycleRatioEt:
                alertNumberBoardDialog(drainZeroCycleRatioEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE);
                break;
            case R.id.drainOtherCycleRatioRl:
            case R.id.drainOtherCycleRatioEt:
                alertNumberBoardDialog(drainOtherCycleRatioEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE);
                break;
            case R.id.drainTimeoutAlarmRl:
            case R.id.drainTimeoutAlarmEt:
                alertNumberBoardDialog(drainTimeoutAlarmEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM);
                break;
            case R.id.drainAuxiliaryFlushVolumeEt:
            case R.id.drainAuxiliaryFlushVolumeRl:
                alertNumberBoardDialog(drainAuxiliaryFlushVolumeEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME);
                break;
            case R.id.drainAuxiliaryFlushNumEt:
            case R.id.drainAuxiliaryFlushNumRl:
                alertNumberBoardDialog(drainAuxiliaryFlushNumEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER);
                break;
            case R.id.drainReminderIntervalEt:
            case R.id.drainReminderIntervalRl:
                alertNumberBoardDialog(drainReminderIntervalEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL);
                break;
            case R.id.infusionTimeIntervalEt:
            case R.id.infusionTimeIntervalRl:
                alertNumberBoardDialog(infusionTimeIntervalEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL);
                break;
            case R.id.infusionThresholdRl:
            case R.id.infusionThresholdEt:
                alertNumberBoardDialog(infusionThresholdEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE);
                break;
            case R.id.infusionMaximumAlertEt:
            case R.id.infusionMaximumAlertRl:
                alertNumberBoardDialog(infusionMaximumAlertEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE);
                break;
            case R.id.rehydrationTimeIntervalEt:
            case R.id.rehydrationTimeIntervalRl:
                alertNumberBoardDialog(rehydrationTimeIntervalEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL);
                break;
            case R.id.rehydrationThresholdEt:
            case R.id.rehydrationThresholdRl:
                alertNumberBoardDialog(rehydrationThresholdEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE);
                break;
            case R.id.rehydrationTargetValueEt:
            case R.id.rehydrationTargetValueRl:
                alertNumberBoardDialog(rehydrationTargetValueEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE);
                break;
            case R.id.rehydrationMinimumEt:
            case R.id.rehydrationMinimumRl:
                alertNumberBoardDialog(rehydrationMinimumEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT);
                break;
            case R.id.btn_submit:
                save();
                break;
        }
    }
    @BindView(R.id.powerIv)
    ImageView powerIv;
    @BindView(R.id.currentPower)
    TextView currentPower;
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {
            if (mReceiveDeviceBean.isAcPowerIn == 1) {
                powerIv.setImageResource(R.drawable.charging);
            } else {
                if (mReceiveDeviceBean.batteryLevel < 30) {
                    powerIv.setImageResource(R.drawable.poor_power);
                } else if (30 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 60 ) {
                    powerIv.setImageResource(R.drawable.low_power);
                } else if (60 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 80 ) {
                    powerIv.setImageResource(R.drawable.mid_power);
                } else {
                    powerIv.setImageResource(R.drawable.high_power);
                }
            }
            currentPower.setText(mReceiveDeviceBean.batteryLevel+"");
        });
    }
    @Override
    public void registerEvents() {

        if (MyApplication.chargeFlag == 1) {
            powerIv.setImageResource(R.drawable.charging);
        } else {
            if (MyApplication.batteryLevel < 30) {
                powerIv.setImageResource(R.drawable.poor_power);
            } else if (30 < MyApplication.batteryLevel &&MyApplication.batteryLevel < 60 ) {
                powerIv.setImageResource(R.drawable.low_power);
            } else if (60 < MyApplication.batteryLevel &&MyApplication.batteryLevel <= 80 ) {
                powerIv.setImageResource(R.drawable.mid_power);
            } else {
                powerIv.setImageResource(R.drawable.high_power);
            }
        }
        currentPower.setText(MyApplication.batteryLevel+"");
        sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
//        setCanNotEditNoClick2(drainThresholdEt);
//        setCanNotEditNoClick2(drainReminderIntervalEt);
//        setCanNotEditNoClick2(drainAuxiliaryFlushNumEt);
//        setCanNotEditNoClick2(drainTimeIntervalEt);
//        setCanNotEditNoClick2(drainTimeoutAlarmEt);
//        setCanNotEditNoClick2(drainAuxiliaryFlushVolumeEt);
//        setCanNotEditNoClick2(drainOtherCycleRatioEt);
//        setCanNotEditNoClick2(drainZeroCycleRatioEt);
//
//        setCanNotEditNoClick2(rehydrationMinimumEt);
//        setCanNotEditNoClick2(infusionThresholdEt);
//        setCanNotEditNoClick2(infusionMaximumAlertEt);
//        setCanNotEditNoClick2(infusionTimeIntervalEt);
//
//        setCanNotEditNoClick2(rehydrationThresholdEt);
//        setCanNotEditNoClick2(rehydrationTargetValueEt);
//        setCanNotEditNoClick2(rehydrationTimeIntervalEt);

    }

    private TreatmentParameterEniity getmParameterEniity() {
        if (treatmentParameterEniity == null) {
            treatmentParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
        }
        return treatmentParameterEniity;
    }

    public DrainParameterBean getDrainParameterBean() {
        if (drainParameterBean == null) {
            drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        }
        return drainParameterBean;
    }

    public PerfusionParameterBean getPerfusionParameterBean() {
        if (perfusionParameterBean == null) {
            perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        }
        return perfusionParameterBean;
    }

    public SupplyParameterBean getSupplyParameterBean() {
        if (supplyParameterBean == null) {
            supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();
        }
        return supplyParameterBean;
    }

    @Override
    public void initViewData() {

        // 引流
        Log.e("参数设置","drainTimeInterval--"+getDrainParameterBean().drainTimeInterval);
        drainTimeIntervalEt.setText(String.valueOf(getDrainParameterBean().drainTimeInterval));
        drainThresholdEt.setText(String.valueOf(getDrainParameterBean().drainThresholdValue));
        drainZeroCycleRatioEt.setText(String.valueOf(getDrainParameterBean().drainZeroCyclePercentage));
        drainOtherCycleRatioEt.setText(String.valueOf(getDrainParameterBean().drainOtherCyclePercentage));
        drainTimeoutAlarmEt.setText(String.valueOf(getDrainParameterBean().drainTimeoutAlarm));
        drainAuxiliaryFlushVolumeEt.setText(String.valueOf(getDrainParameterBean().drainRinseVolume));
        drainAuxiliaryFlushNumEt.setText(String.valueOf(getDrainParameterBean().drainRinseNumber));
        drainReminderIntervalEt.setText(String.valueOf(getDrainParameterBean().drainWarnTimeInterval));
        drainEmptyWaitingSwitch.setOn(getDrainParameterBean().isDrainManualEmptying);
        drainEmptyWaitingSwitch.setOnToggledListener((toggleableView, isOn) -> {
            getDrainParameterBean().isDrainManualEmptying = isOn;
        });
        drainNegativeSwitch.setOn(drainParameterBean.isNegpreDrain);
        drainNegativeSwitch.setOnToggledListener((toggleableView, isOn) -> {
            drainParameterBean.isNegpreDrain = isOn;
        });

        // 补液
        rehydrationTimeIntervalEt.setText(String.valueOf(getSupplyParameterBean().supplyTimeInterval));
        rehydrationThresholdEt.setText(String.valueOf(getSupplyParameterBean().supplyThresholdValue));
        rehydrationTargetValueEt.setText(String.valueOf(getSupplyParameterBean().supplyTargetProtectionValue));
        rehydrationMinimumEt.setText(String.valueOf(getSupplyParameterBean().supplyMinWeight));

        // 灌注
        infusionMaximumAlertEt.setText(String.valueOf(getPerfusionParameterBean().perfMaxWarningValue));
        infusionTimeIntervalEt.setText(String.valueOf(getPerfusionParameterBean().perfTimeInterval));
        infusionThresholdEt.setText(String.valueOf(getPerfusionParameterBean().perfThresholdValue));

        // 留腹
        abdomenDeductSwitch.setOn(retainParamBean.isAbdomenRetainingDeduct);
        abdomenDeductSwitch.setOnToggledListener((toggleableView, isOn) -> {
            retainParamBean.isAbdomenRetainingDeduct = isOn;
        });
        abdomenUltSwitch.setOn(retainParamBean.isZeroCycleUltrafiltration);
        abdomenUltSwitch.setOnToggledListener((toggleableView, isOn) -> {
            retainParamBean.isZeroCycleUltrafiltration = isOn;
        });

    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
                            drainTimeIntervalEt.setText(result);
                            getDrainParameterBean().drainTimeInterval = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
                            drainThresholdEt.setText(result);
                            getDrainParameterBean().drainThresholdValue = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE: //0周期引流比例  :  50-100
                            drainZeroCycleRatioEt.setText(result);
                            getDrainParameterBean().drainZeroCyclePercentage = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE: //其他周期引流比例  : 50-100
                            drainOtherCycleRatioEt.setText(result);
                            getDrainParameterBean().drainOtherCyclePercentage = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM: //引流/灌注超时报警  : 0-600
                            drainTimeoutAlarmEt.setText(result);
                            getDrainParameterBean().drainTimeoutAlarm = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME: //引流辅助冲洗 量  :  30-200
                            drainAuxiliaryFlushVolumeEt.setText(result);
                            getDrainParameterBean().drainRinseVolume = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER: //引流辅助冲洗 次数  :  1-3改成0-3
                            drainAuxiliaryFlushNumEt.setText(result);
                            getDrainParameterBean().drainRinseNumber = Integer.parseInt(result);
//                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_LATENCY_TIME)) {//最末引流等待  : 0-60
//                        etUnitLatencyTime.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL: //最末引流提醒间隔  : 0-60
                            drainReminderIntervalEt.setText(result);
                            getDrainParameterBean().drainWarnTimeInterval = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
                            infusionTimeIntervalEt.setText(result);
                            getPerfusionParameterBean().perfTimeInterval = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
                            getPerfusionParameterBean().perfThresholdValue = Integer.parseInt(result);
                            infusionThresholdEt.setText(result);
//                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PERFUSION_MIN_WEIGHT)) {//加热袋最低重量允许  :  100-1000
//
//                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PERFUSION_ALLOW_ABDOMINAL_VOLUME)) {//是否允许最末灌注减去留腹量  : 1000-3000

                            break;
                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE: //灌注最大警戒值  : 1000-3000
                            infusionMaximumAlertEt.setText(result);
                            getPerfusionParameterBean().perfMaxWarningValue = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
                            getSupplyParameterBean().supplyTimeInterval = Integer.parseInt(result);
                            rehydrationTimeIntervalEt.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
                            rehydrationThresholdEt.setText(result);
                            getSupplyParameterBean().supplyThresholdValue = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE: //补液目标保护值  :  0-500
                            rehydrationTargetValueEt.setText(result);
                            getSupplyParameterBean().supplyTargetProtectionValue = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT: //启动补液的加热袋最低值  : 500-10000
                            rehydrationMinimumEt.setText(result);
                            getSupplyParameterBean().supplyMinWeight = Integer.parseInt(result);
                            break;
                    }
                }
            }
        });
    }

    private void save() {
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.RETAIN_PARAM, retainParamBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.SUPPLY_PARAMETER, supplyParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRAIN_PARAMETER, drainParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.PERFUSION_PARAMETER, perfusionParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, treatmentParameterEniity);
        toastMessage("保存成功");

        init();
    }

    private void init() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialTreatmentParamBean mParamBean = new SerialTreatmentParamBean();

        SerialTreatmentDrainBean drain = new SerialTreatmentDrainBean();
        drain.drainFlowRate = getDrainParameterBean().drainThresholdValue;//引流流速阈值
        drain.drainFlowPeriod = getDrainParameterBean().drainTimeInterval;//引流流速周期 秒
        drain.drainRinseVolume = getDrainParameterBean().drainRinseVolume;//引流冲洗量
        drain.drainRinseTimes = getDrainParameterBean().drainRinseNumber;//引流次数
        drain.firstDrainPassRate = getDrainParameterBean().drainZeroCyclePercentage;//0周期引流合格率
        drain.drainPassRate = getDrainParameterBean().drainOtherCyclePercentage;//周期引流合格率
        drain.isFinalDrainEmpty = getDrainParameterBean().isDrainManualEmptying ? 1 : 0;//最末引流是否排空
        drain.isFinalDrainEmptyWait = getDrainParameterBean().isDrainManualEmptying ? 1 : 0;//最末引流排空是否等待
        drain.finalDrainEmptyWaitTime = getDrainParameterBean().alarmTimeInterval;//最末引流排空等待时间 30
        drain.isVaccumDrain = getDrainParameterBean().isNegpreDrain ? 1 : 0;//是否开启负压引流

        SerialTreatmentPerfuseBean perfuse = new SerialTreatmentPerfuseBean();
        perfuse.perfuseFlowRate = getPerfusionParameterBean().perfThresholdValue;//灌注流速阈值
        perfuse.perfuseFlowPeriod = getPerfusionParameterBean().perfTimeInterval;//灌注流速周期 秒
        perfuse.perfuseMaxVolume = getPerfusionParameterBean().perfMaxWarningValue;//最大灌注量

        SerialTreatmentSupplyBean supply = new SerialTreatmentSupplyBean();
        supply.supplyFlowRate = getSupplyParameterBean().supplyThresholdValue;//补液流速阈值
        supply.supplyFlowPeriod = getSupplyParameterBean().supplyTimeInterval;//补液流速周期 秒
        supply.supplyProtectVolume = getSupplyParameterBean().supplyTargetProtectionValue;//补液目标保护值
        supply.supplyMinVolume = getSupplyParameterBean().supplyMinWeight;//启动补液的加热袋重量最低值

        SerialTreatmentRetainBean retain = new SerialTreatmentRetainBean();
        retain.isFinalRetainDeduct = retainParamBean.isAbdomenRetainingDeduct ? 1 : 0;//是否末次流量扣除
        retain.isFiltCycleOnly = retainParamBean.isZeroCycleUltrafiltration ? 1 : 0;// 1 是否只统计循环周期的超滤量
        retain.parammodify = 0;

        mParamBean.drain = drain;
        mParamBean.perfuse = perfuse;
        mParamBean.supply = supply;
        mParamBean.retain = retain;

        mSerialRequestBean.method = "treatparam/config";
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        String sendData = new Gson().toJson(mRequestBean);
        sendToMainBoard(sendData);
    }

    @Override
    public void notifyByThemeChanged() {

    }
}