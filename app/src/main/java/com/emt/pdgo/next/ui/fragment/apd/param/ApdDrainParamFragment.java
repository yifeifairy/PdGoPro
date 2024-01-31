package com.emt.pdgo.next.ui.fragment.apd.param;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ApdDrainParamFragment extends BaseFragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apd_drain_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        click();
        return view;
    }
    
    private void initView() {
        drainTimeIntervalEt.setText(String.valueOf(ApdParamSetActivity.drainParameterBean.drainTimeInterval));
        drainThresholdEt.setText(String.valueOf(ApdParamSetActivity.drainParameterBean.drainThresholdValue));
        drainZeroCycleRatioEt.setText(String.valueOf(ApdParamSetActivity.drainParameterBean.drainZeroCyclePercentage));
        drainOtherCycleRatioEt.setText(String.valueOf(ApdParamSetActivity.drainParameterBean.drainOtherCyclePercentage));
        drainTimeoutAlarmEt.setText(String.valueOf(ApdParamSetActivity.drainParameterBean.drainTimeoutAlarm));
        drainAuxiliaryFlushVolumeEt.setText(String.valueOf(ApdParamSetActivity.drainParameterBean.drainRinseVolume));
        drainAuxiliaryFlushNumEt.setText(String.valueOf(ApdParamSetActivity.drainParameterBean.drainRinseNumber));
        drainReminderIntervalEt.setText(String.valueOf(ApdParamSetActivity.drainParameterBean.drainWarnTimeInterval));
        drainEmptyWaitingSwitch.setOn(ApdParamSetActivity.drainParameterBean.isDrainManualEmptying);
        drainEmptyWaitingSwitch.setOnToggledListener((toggleableView, isOn) -> {
            ApdParamSetActivity.drainParameterBean.isDrainManualEmptying = isOn;
        });
        drainNegativeSwitch.setOn(ApdParamSetActivity.drainParameterBean.isNegpreDrain);
        drainNegativeSwitch.setOnToggledListener((toggleableView, isOn) -> {
            ApdParamSetActivity.drainParameterBean.isNegpreDrain = isOn;
        });
    }

    @OnClick({R.id.drainTimeIntervalEt, R.id.drainTimeIntervalRl, R.id.drainThresholdRl, R.id.drainThresholdEt,
            R.id.drainZeroCycleRatioEt, R.id.drainZeroCycleRatioRl, R.id.drainOtherCycleRatioRl, R.id.drainOtherCycleRatioEt,
            R.id.drainTimeoutAlarmEt, R.id.drainTimeoutAlarmRl,R.id.drainAuxiliaryFlushVolumeEt, R.id.drainAuxiliaryFlushVolumeRl,
            R.id.drainAuxiliaryFlushNumRl, R.id.drainAuxiliaryFlushNumEt, R.id.drainReminderIntervalEt, R.id.drainReminderIntervalRl})
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
        }
    }

    private void click() {

    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
                            drainTimeIntervalEt.setText(result);
                            ApdParamSetActivity.drainParameterBean.drainTimeInterval = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
                            drainThresholdEt.setText(result);
                            ApdParamSetActivity.drainParameterBean.drainThresholdValue = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE: //0周期引流比例  :  50-100
                            drainZeroCycleRatioEt.setText(result);
                            ApdParamSetActivity.drainParameterBean.drainZeroCyclePercentage = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE: //其他周期引流比例  : 50-100
                            drainOtherCycleRatioEt.setText(result);
                            ApdParamSetActivity.drainParameterBean.drainOtherCyclePercentage = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM: //引流/灌注超时报警  : 0-600
                            drainTimeoutAlarmEt.setText(result);
                            ApdParamSetActivity.drainParameterBean.drainTimeoutAlarm = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME: //引流辅助冲洗 量  :  30-200
                            drainAuxiliaryFlushVolumeEt.setText(result);
                            ApdParamSetActivity.drainParameterBean.drainRinseVolume = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER: //引流辅助冲洗 次数  :  1-3改成0-3
                            drainAuxiliaryFlushNumEt.setText(result);
                            ApdParamSetActivity.drainParameterBean.drainRinseNumber = Integer.parseInt(result);
//                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_LATENCY_TIME)) {//最末引流等待  : 0-60
//                        etUnitLatencyTime.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL: //最末引流提醒间隔  : 0-60
                            drainReminderIntervalEt.setText(result);
                            ApdParamSetActivity.drainParameterBean.drainWarnTimeInterval = Integer.parseInt(result);
                            break;
                    }
                }
            }
        });
    }

    private Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void notifyByThemeChanged() {

    }
}