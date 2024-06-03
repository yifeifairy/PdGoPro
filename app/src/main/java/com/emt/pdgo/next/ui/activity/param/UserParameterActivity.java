package com.emt.pdgo.next.ui.activity.param;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.UserParameterBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.AnimatorActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户参数设置界面
 *
 * @author chenjh
 * @date 2019-08-26 17:15
 */
public class UserParameterActivity extends BaseActivity {

    @BindView(R.id.custom_id_app_background)
    LinearLayout mAppBackground;

    @BindView(R.id.ls_user_type)
    LabeledSwitch lsUserType;
    @BindView(R.id.ls_formula_recommended)
    LabeledSwitch lsFormulaRecommended;
    @BindView(R.id.ls_night)
    LabeledSwitch lsNight;

    @BindView(R.id.tv_lable_1)
    TextView tvLable1;
    @BindView(R.id.tv_lable_2)
    TextView tvLable2;
    @BindView(R.id.tv_lable_3)
    TextView tvLable3;
    @BindView(R.id.tv_lable_4)
    TextView tvLable4;
    @BindView(R.id.tv_lable_5)
    TextView tvLable5;
    @BindView(R.id.tv_lable_6)
    TextView tvLable6;
    @BindView(R.id.tv_lable_7)
    TextView tvLable7;
    @BindView(R.id.tv_lable_8)
    TextView tvLable8;
    @BindView(R.id.tv_lable_9)
    TextView tvLable9;
    @BindView(R.id.tvAutoLabel)
    TextView tvAutoLabel;
    @BindView(R.id.tv_unit_3)
    TextView tvUnit3;
    @BindView(R.id.tv_unit_4)
    TextView tvUnit4;
    @BindView(R.id.tv_unit_5)
    TextView tvUnit5;
    @BindView(R.id.tv_unit_6)
    TextView tvUnit6;
    @BindView(R.id.tv_unit_7)
    TextView tvUnit7;
    @BindView(R.id.tv_unit_8)
    TextView tvUnit8;

    @BindView(R.id.et_prescription_item3)
    EditText etPrescriptionItem3;
    @BindView(R.id.et_prescription_item4)
    EditText etPrescriptionItem4;
    @BindView(R.id.et_prescription_item5)
    EditText etPrescriptionItem5;
    @BindView(R.id.et_prescription_item6)
    EditText etPrescriptionItem6;
    @BindView(R.id.et_prescription_item7)
    EditText etPrescriptionItem7;
    @BindView(R.id.et_prescription_item8)
    EditText etPrescriptionItem8;

    @BindView(R.id.layout_prescription_item_1)
    RelativeLayout layoutPrescriptionItem1;
    @BindView(R.id.layout_prescription_item_2)
    RelativeLayout layoutPrescriptionItem2;
    @BindView(R.id.layout_prescription_item_3)
    RelativeLayout layoutPrescriptionItem3;
    @BindView(R.id.layout_prescription_item_4)
    RelativeLayout layoutPrescriptionItem4;
    @BindView(R.id.layout_prescription_item_5)
    RelativeLayout layoutPrescriptionItem5;
    @BindView(R.id.layout_prescription_item_6)
    RelativeLayout layoutPrescriptionItem6;
    @BindView(R.id.layout_prescription_item_7)
    RelativeLayout layoutPrescriptionItem7;
    @BindView(R.id.layout_prescription_item_8)
    RelativeLayout layoutPrescriptionItem8;
    @BindView(R.id.layout_prescription_item_9)
    RelativeLayout layoutPrescriptionItem9;
    @BindView(R.id.layoutAutoNight)
    RelativeLayout layoutAutoNight;

    @BindView(R.id.autoLightSwitch)
    LabeledSwitch autoLightSwitch;
    @BindView(R.id.autoLightLl)
    LinearLayout autoLightLl;

    @BindView(R.id.autoTime)
    EditText autoTime;

//    @BindView(R.id.btnConfirm)
//    StateButton btnConfirm;


    private UserParameterBean mUserParameterBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_user_parameter);
        ButterKnife.bind(this);
        initHeadTitleBar("用户参数设置","确定");
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
//        setCanNotEditNoClick2(etPrescriptionItem1);
//        setCanNotEditNoClick2(etPrescriptionItem2);
        setCanNotEditNoClick2(etPrescriptionItem3);
        setCanNotEditNoClick2(etPrescriptionItem4);
        setCanNotEditNoClick2(etPrescriptionItem5);
        setCanNotEditNoClick2(etPrescriptionItem6);
        setCanNotEditNoClick2(etPrescriptionItem7);
        setCanNotEditNoClick2(etPrescriptionItem8);
        setCanNotEditNoClick2(autoTime);

        lsUserType.setOnToggledListener((toggleableView, isOn) -> {
//                Logger.d(" 用户类型: " + isOn);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUserParameterBean.isHospital = isOn;
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, mUserParameterBean);
                    Log.e("修改医院用户", mUserParameterBean.isHospital + "");
                }
            });

        });

        autoLightSwitch.setOnToggledListener((toggleableView, isOn) -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUserParameterBean.isAutoNight = isOn;
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, mUserParameterBean);
                    if (isOn) {
                        autoLightLl.setVisibility(View.VISIBLE);
                    } else {
                        autoLightLl.setVisibility(View.GONE);
//                        if (countDownTimer != null) {
//                            countDownTimer.cancel();
//                        }
                    }
                }
            });
        });

//        btnConfirm.setOnClickListener(v -> {
//            if (mUserParameterBean.countdown != 0) {
//                startCountDown(mUserParameterBean.countdown);
//            } else {
//                toastMessage("请输入倒计时时间");
//            }
//        });

        lsFormulaRecommended.setOnToggledListener((toggleableView, isOn) -> {
//                Logger.d(" 是否配方建议: " + isOn);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUserParameterBean.formulaRrecommended = isOn;
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, mUserParameterBean);
                    Log.e("是否配方建议", mUserParameterBean.formulaRrecommended + "");
                }
            });

        });

        lsNight.setOnToggledListener((toggleableView, isOn) -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUserParameterBean.isNight = isOn;
                    PdproHelper.getInstance().updateTtsSoundOpen(isOn);
                    Log.e("用户参数", "isNight--"+mUserParameterBean.isNight+"--isOn--"+isOn);
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, mUserParameterBean);
                    Intent intent = new Intent(getContext(), AnimatorActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        });

    }

    @Override
    public void initViewData() {
//        if (PdproHelper.getInstance().getUserParameterBean().isNight && getThemeTag() == 1) {
//            lsNight.setOn(false);
//            PdproHelper.getInstance().getUserParameterBean().isNight = false;
//        }
        Log.e("用户参数模式", "模式"+getThemeTag()+"---"+PdproHelper.getInstance().getUserParameterBean().isNight);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUserParameterBean = PdproHelper.getInstance().getUserParameterBean();
                Log.e("是否医院用户", mUserParameterBean.isHospital + "");
                lsUserType.setOn(mUserParameterBean.isHospital);
                lsFormulaRecommended.setOn(mUserParameterBean.formulaRrecommended);
                etPrescriptionItem3.setText(String.valueOf(mUserParameterBean.underweight1));
                etPrescriptionItem5.setText(String.valueOf(mUserParameterBean.underweight2));
                etPrescriptionItem7.setText(String.valueOf(mUserParameterBean.underweight3));
                etPrescriptionItem4.setText(String.valueOf(mUserParameterBean.awaken));
                lsNight.setOn(mUserParameterBean.isNight);
            }
        });
        autoTime.setText(String.valueOf(mUserParameterBean.countdownTimer));
    }


    @OnClick({R.id.btn_submit, R.id.layout_prescription_item_1, R.id.layout_prescription_item_2, R.id.layout_prescription_item_9,
            R.id.et_prescription_item3, R.id.et_prescription_item4,R.id.et_prescription_item5, R.id.et_prescription_item7,R.id.layoutAutoNight,R.id.autoTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                save();
                break;
            case R.id.layout_prescription_item_1://

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lsUserType.setOn(!lsUserType.isOn());
                        mUserParameterBean.isHospital = lsUserType.isOn();
                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, mUserParameterBean);
                        Log.e("修改医院用户", mUserParameterBean.isHospital + "");
                    }
                });
                break;
            case R.id.layout_prescription_item_2://
                lsFormulaRecommended.setOn(!lsFormulaRecommended.isOn());
                mUserParameterBean.formulaRrecommended = lsFormulaRecommended.isOn();
                break;
            case R.id.et_prescription_item3://体重差值1
                alertNumberBoardDialog(etPrescriptionItem3.getText().toString(), PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_1);
                break;
            case R.id.et_prescription_item5://体重差值2
                alertNumberBoardDialog(etPrescriptionItem5.getText().toString(), PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_2);
                break;
            case R.id.et_prescription_item7://体重差值3
                alertNumberBoardDialog(etPrescriptionItem7.getText().toString(), PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_3);
                break;
            case R.id.autoTime:
                alertNumberBoardDialog(autoTime.getText().toString(), PdGoConstConfig.AUTO_SLEEP);
                break;
            case R.id.et_prescription_item4:
                alertNumberBoardDialog(etPrescriptionItem4.getText().toString(), PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_5);
                break;
        }
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
//                    Logger.d(result);
                switch (mType) {
                    case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_1: //体重差值1
                        mUserParameterBean.underweight1 = Float.parseFloat(result);
                        etPrescriptionItem3.setText(result);
                        break;
                    case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_2: //体重差值2
                        mUserParameterBean.underweight2 = Float.parseFloat(result);
                        etPrescriptionItem5.setText(result);
                        break;
                    case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_3: //体重差值3
                        mUserParameterBean.underweight3 = Float.parseFloat(result);
                        etPrescriptionItem7.setText(result);
                        break;
                    case PdGoConstConfig.AUTO_SLEEP:
                        mUserParameterBean.countdownTimer = Integer.parseInt(result);
                        autoTime.setText(result);
//
//                        startCountDown(mUserParameterBean.countdownTimer);
                        break;
                    case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_5:
                        mUserParameterBean.awaken = Integer.parseInt(result);
                        etPrescriptionItem4.setText(result);
                        break;
                }
            }
        });
    }


    private void save() {

//        showLoadingDialog();
//
//
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, mUserParameterBean);
//        if (mUserParameterBean.countdownTimer != 0 && autoLightLl.getVisibility() == View.VISIBLE) {
//            startCountDown(mUserParameterBean.countdownTimer);
//        } else {
//            toastMessage("请输入倒计时时间");
//        }
//
//        hideLoadingDialog();
//        showCommonDialog("用户参数保存成功");
        toastMessage( "参数保存成功");
//        startCountDown(PdproHelper.getInstance().getUserParameterBean().countdownTimer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @BindView(R.id.tv_tips)
    TextView tvTips;
    @Override
    public void notifyByThemeChanged() {

        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());

        helper.setBackgroundResourceByAttr(mAppBackground, R.attr.custom_attr_app_bg);

        helper.setBackgroundResourceByAttr(layoutPrescriptionItem1, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutPrescriptionItem2, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutPrescriptionItem3, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutPrescriptionItem4, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutPrescriptionItem5, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutPrescriptionItem6, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutPrescriptionItem7, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutPrescriptionItem8, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutPrescriptionItem9, R.attr.custom_attr_input_box_gray_bg);
        helper.setBackgroundResourceByAttr(layoutAutoNight, R.attr.custom_attr_input_box_gray_bg);

        helper.setTextColorByAttr(tvLable1, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvLable2, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvLable3, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvLable4, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvLable5, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvLable6, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvLable7, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvLable8, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvLable9, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvAutoLabel, R.attr.custom_attr_common_text_color);

        helper.setTextColorByAttr(tvTips, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvUnit3, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvUnit4, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvUnit5, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvUnit6, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvUnit7, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvUnit8, R.attr.custom_attr_common_text_color);

        helper.setTextColorByAttr(etPrescriptionItem3, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(etPrescriptionItem4, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(etPrescriptionItem5, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(etPrescriptionItem6, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(etPrescriptionItem7, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(etPrescriptionItem8, R.attr.custom_attr_common_text_color);


    }
}
