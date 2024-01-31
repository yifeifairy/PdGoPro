package com.emt.pdgo.next.ui.activity.param;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.ToastUtils;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 引流参数设置界面
 *
 * @author chenjh
 * @date 2019/1/24 10:25
 */
public class DrainParameterActivity extends BaseActivity {

    @BindView(R.id.et_time_interval)
    EditText etTimeInterval;
    @BindView(R.id.et_threshold_value)
    EditText etThresholdValue;
    @BindView(R.id.et_zero_cycle_percentage)
    EditText etZeroCyclePercentage;
    @BindView(R.id.et_other_cycle_percentage)
    EditText etOtherCyclePercentage;
    @BindView(R.id.et_timeout_alarm)
    EditText etTimeoutAlarm;
    @BindView(R.id.et_rinse_volume)
    EditText etRinseVolume;
    @BindView(R.id.et_rinse_number)
    EditText etRinseNumber;
    @BindView(R.id.et_warn_time_interval)
    EditText etWarnTimeInterval;

    @BindView(R.id.layout_drain_emptying)
    RelativeLayout mLayoutDrainEmptying;

    @BindView(R.id.labeledSwitch_emptying)
    LabeledSwitch labeledSwitchEmptying;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_drain_parameter);
        ButterKnife.bind(this);
        initHeadTitleBar("引流参数设置", "保存");
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
        setCanNotEditNoClick2(etTimeInterval);
        setCanNotEditNoClick2(etThresholdValue);
        setCanNotEditNoClick2(etZeroCyclePercentage);
        setCanNotEditNoClick2(etOtherCyclePercentage);
        setCanNotEditNoClick2(etTimeoutAlarm);
        setCanNotEditNoClick2(etRinseVolume);
        setCanNotEditNoClick2(etRinseNumber);
        setCanNotEditNoClick2(etWarnTimeInterval);
        labeledSwitchEmptying.setOnToggledListener((toggleableView, isOn) -> {
//                Logger.d(" 最末引流排空等待: " + isOn);
            drainParameterBean.isDrainManualEmptying = isOn;
        });
    }

    private DrainParameterBean drainParameterBean;

    @Override
    public void initViewData() {
        drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();

        etTimeInterval.setText(String.valueOf(drainParameterBean.drainTimeInterval));
        etThresholdValue.setText(String.valueOf(drainParameterBean.drainThresholdValue));
        etZeroCyclePercentage.setText(String.valueOf(drainParameterBean.drainZeroCyclePercentage));
        etOtherCyclePercentage.setText(String.valueOf(drainParameterBean.drainOtherCyclePercentage));
        etTimeoutAlarm.setText(String.valueOf(drainParameterBean.drainTimeoutAlarm));
        etRinseVolume.setText(String.valueOf(drainParameterBean.drainRinseVolume));
        etRinseNumber.setText(String.valueOf(drainParameterBean.drainRinseNumber));
        etWarnTimeInterval.setText(String.valueOf(drainParameterBean.drainWarnTimeInterval));
        labeledSwitchEmptying.setOn(drainParameterBean.isDrainManualEmptying);
    }
    @OnClick({R.id.btn_submit, R.id.et_time_interval, R.id.et_threshold_value, R.id.et_zero_cycle_percentage,
            R.id.et_other_cycle_percentage, R.id.et_timeout_alarm, R.id.et_rinse_volume, R.id.et_rinse_number, R.id.et_warn_time_interval,
            R.id.layout_time_interval, R.id.layout_threshold_value, R.id.layout_zero_cycle_percentage, R.id.layout_other_cycle_percentage,
            R.id.layout_timeout_alarm, R.id.layout_rinse_volume, R.id.layout_rinse_number, R.id.layout_warn_time_interval, R.id.layout_drain_emptying})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                save();
                break;
            case R.id.layout_time_interval:
            case R.id.et_time_interval:
                alertNumberBoardDialog(etTimeInterval.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_TIME_INTERVAL);
                break;
            case R.id.layout_threshold_value:
            case R.id.et_threshold_value:
                alertNumberBoardDialog(etThresholdValue.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE);
                break;
            case R.id.layout_zero_cycle_percentage:
            case R.id.et_zero_cycle_percentage:
                alertNumberBoardDialog(etZeroCyclePercentage.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE);
                break;
            case R.id.layout_other_cycle_percentage:
            case R.id.et_other_cycle_percentage:
                alertNumberBoardDialog(etOtherCyclePercentage.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE);
                break;
            case R.id.layout_timeout_alarm:
            case R.id.et_timeout_alarm:
                alertNumberBoardDialog(etTimeoutAlarm.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM);
                break;
            case R.id.layout_rinse_volume:
            case R.id.et_rinse_volume:
                alertNumberBoardDialog(etRinseVolume.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME);
                break;
            case R.id.layout_rinse_number:
            case R.id.et_rinse_number:
                alertNumberBoardDialog(etRinseNumber.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER);
                break;
            case R.id.layout_drain_emptying://
                labeledSwitchEmptying.performClick();
                break;
            case R.id.layout_warn_time_interval:
            case R.id.et_warn_time_interval:
                alertNumberBoardDialog(etWarnTimeInterval.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL);
                break;
        }
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
                            etTimeInterval.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
                            etThresholdValue.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE: //0周期引流比例  :  50-100
                            etZeroCyclePercentage.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE: //其他周期引流比例  : 50-100
                            etOtherCyclePercentage.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM: //引流/灌注超时报警  : 0-600
                            etTimeoutAlarm.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME: //引流辅助冲洗 量  :  30-200
                            etRinseVolume.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER: //引流辅助冲洗 次数  :  1-3改成0-3
                            etRinseNumber.setText(result);
//                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_LATENCY_TIME)) {//最末引流等待  : 0-60
//                        etUnitLatencyTime.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL: //最末引流提醒间隔  : 0-60
                            etWarnTimeInterval.setText(result);
                            break;
                    }
                }
            }
        });
    }

    private void save() {

//        showLoadingDialog();

        drainParameterBean.drainTimeInterval = Integer.parseInt(etTimeInterval.getText().toString());
        drainParameterBean.drainThresholdValue = Integer.parseInt(etThresholdValue.getText().toString());
        drainParameterBean.drainZeroCyclePercentage = Integer.parseInt(etZeroCyclePercentage.getText().toString());
        drainParameterBean.drainOtherCyclePercentage = Integer.parseInt(etOtherCyclePercentage.getText().toString());
        drainParameterBean.drainTimeoutAlarm = Integer.parseInt(etTimeoutAlarm.getText().toString());
        drainParameterBean.drainRinseVolume = Integer.parseInt(etRinseVolume.getText().toString());
        drainParameterBean.drainRinseNumber = Integer.parseInt(etRinseNumber.getText().toString());
        drainParameterBean.isDrainManualEmptying = labeledSwitchEmptying.isOn();
        drainParameterBean.drainWarnTimeInterval = Integer.parseInt(etWarnTimeInterval.getText().toString());

        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRAIN_PARAMETER, drainParameterBean);

//        hideLoadingDialog();
//        showCommonDialog("引流参数保存成功");
        ToastUtils.showToast(DrainParameterActivity.this, "引流参数保存成功");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void notifyByThemeChanged() {

    }

}
