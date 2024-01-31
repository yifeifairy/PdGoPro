package com.emt.pdgo.next.ui.activity.param;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.FirstRinseParameterBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预冲参数设置界面
 *
 * @author chenjh
 * @date 2019/3/14 10:08
 */
public class FirstRinseParameterActivity extends BaseActivity {

    @BindView(R.id.et_emptying_time)
    EditText etEmptyingTime;
    @BindView(R.id.et_preset_weight)
    EditText etPresetWeight;
    @BindView(R.id.et_preset_weight_loss)
    EditText etPresetWeightLoss;
    @BindView(R.id.et_preset_weight2)
    EditText etPresetWeight2;
    @BindView(R.id.et_preset_weight3)
    EditText etPresetWeight3;

    @BindView(R.id.labeledSwitch)
    LabeledSwitch labeledSwitch;

    private FirstRinseParameterBean mFirstRinseParameterBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_first_rinse_parameter);
        ButterKnife.bind(this);
        initHeadTitleBar("预冲参数设置", "保存");
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
        setCanNotEditNoClick2(etEmptyingTime);
        setCanNotEditNoClick2(etPresetWeight);
        setCanNotEditNoClick2(etPresetWeightLoss);
        setCanNotEditNoClick2(etPresetWeight2);
        setCanNotEditNoClick2(etPresetWeight3);
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
    }

    @Override
    public void initViewData() {
        mFirstRinseParameterBean = PdproHelper.getInstance().getFirstRinseParameterBean();
        labeledSwitch.setOn(mFirstRinseParameterBean.supplyselect == 1);
        etEmptyingTime.setText(String.valueOf(mFirstRinseParameterBean.firstvolume));
        etPresetWeight.setText(String.valueOf(mFirstRinseParameterBean.secondvolume));
        etPresetWeightLoss.setText(String.valueOf(mFirstRinseParameterBean.supplyperiod));
        etPresetWeight2.setText(String.valueOf(mFirstRinseParameterBean.supplyspeed));
//        etPresetWeight3.setText(String.valueOf(mFirstRinseParameterBean.supply_rate));
        labeledSwitch.setOnToggledListener((toggleableView, isOn) -> {
            mFirstRinseParameterBean.supplyselect = isOn ? 1 : 0;
        });
    }

    @OnClick({R.id.btn_submit, R.id.layout_emptying_time, R.id.et_emptying_time, R.id.layout_preset_weight, R.id.et_preset_weight, R.id.layout_preset_weight_loss, R.id.et_preset_weight_loss})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                save();
                break;
            case R.id.layout_emptying_time://排空时间
            case R.id.et_emptying_time://排空时间
                alertNumberBoardDialog(etEmptyingTime.getText().toString(), PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_EMPTYING_TIME);
                break;
            case R.id.layout_preset_weight://预设增量1
            case R.id.et_preset_weight://预设增量1
                alertNumberBoardDialog(etPresetWeight.getText().toString(), PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT);
                break;
            case R.id.layout_preset_weight_loss://预设增量2
            case R.id.et_preset_weight_loss://
                alertNumberBoardDialog(etPresetWeightLoss.getText().toString(), PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT_LOSS);
                break;
            case R.id.layout_preset_weight2:
            case R.id.et_preset_weight2:
                alertNumberBoardDialog(etPresetWeight2.getText().toString(), PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT3);
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
                    if (mType.equals(PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_EMPTYING_TIME)) {//排空时间 s :  8-15
                        etEmptyingTime.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT)) {//预设增量1[上位秤增加重量X1克] g 范围 50-100
                        etPresetWeight.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT_LOSS)) {//预设减重 [上位秤减少重量X1克] g 范围 50-100
                        etPresetWeightLoss.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT3)) {//预设增量1[上位秤增加重量X1克] g 范围 50-100
                        etPresetWeight2.setText(result);
//                        etPresetWeight3.setText(String.valueOf(Integer.valueOf(result)));
                    }
                }
            }
        });
    }

    private void save() {
        mFirstRinseParameterBean.firstvolume = Integer.parseInt(etEmptyingTime.getText().toString());
        mFirstRinseParameterBean.secondvolume = Integer.parseInt(etPresetWeight.getText().toString());
        mFirstRinseParameterBean.supplyspeed = Integer.parseInt(etPresetWeightLoss.getText().toString());
        mFirstRinseParameterBean.supplyperiod = Integer.parseInt(etPresetWeight2.getText().toString());
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.FIRST_RINSE_PARAMETER, mFirstRinseParameterBean);
        toastMessage( "参数保存成功");
    }

    @Override
    public void notifyByThemeChanged() {

    }

}
