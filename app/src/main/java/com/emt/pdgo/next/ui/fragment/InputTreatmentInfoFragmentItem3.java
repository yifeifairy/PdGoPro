package com.emt.pdgo.next.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.ui.activity.InputTreatmentInfoActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.fragment
 * @ClassName: InputTreatmentInfoFragmentItem1
 * @Description: java类作用描述
 * @Author: chenjh
 * @CreateDate: 2019/12/12 10:33 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/12 10:33 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class InputTreatmentInfoFragmentItem3 extends BaseFragment {


    Unbinder unbinder;

    @BindView(R.id.tv_zero_cycle_select)
    TextView tvZeroCycleSelect;
    @BindView(R.id.tv_other_cycle_select)
    TextView tvOtherCycleSelect;
    @BindView(R.id.tv_perfusion_warning_select)
    TextView tvPerfusionWarningSelect;

    @BindView(R.id.et_perfusion_warning_value)
    EditText etPerfusionWarningValue;
    @BindView(R.id.et_time_interval)
    EditText etTimeInterval;

    @BindView(R.id.layout_time_interval)
    LinearLayout layoutTimeInterval;


//    @BindView(R.id.layout_drain_threshold_zero_cycle)
//    RelativeLayout layoutDrainThresholdZeroCycle;
//    @BindView(R.id.layout_drain_threshold_other_cycle)
//    RelativeLayout layoutDrainThresholdOtherCycle;
//    @BindView(R.id.layout_perfusion_warning_value)
//    RelativeLayout layoutPerfusionWarningValue;


    @BindView(R.id.btn_waiting)
    LabeledSwitch btnWaiting;//最末引流排空等待
    @BindView(R.id.btn_negpre_drain)
    LabeledSwitch btnNegpreDrain;//负压引流
    @BindView(R.id.btn_deduct)
    LabeledSwitch btnDeduct;//留腹扣除
    @BindView(R.id.btn_wake_up)
    LabeledSwitch btnWakeUp;//治疗结束报警唤醒
    @BindView(R.id.btn_ultrafiltration)
    LabeledSwitch btnUltrafiltration;//只计算循环透析治疗期间的超滤
    @BindView(R.id.btn_dormancy)
    LabeledSwitch btnDormancy;//屏幕休眠
    @BindView(R.id.btn_touch_tone)
    LabeledSwitch btnTouchTone;//屏幕触控声

    private int currPerfusionWarningSelect = 1;


    public InputTreatmentInfoActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (InputTreatmentInfoActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_treatment_info_item3, container, false);
        unbinder = ButterKnife.bind(this, view);
        registerListener();
        initViewData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void initViewData() {
        setCanNotEditNoClick2(etPerfusionWarningValue);
        setCanNotEditNoClick2(etTimeInterval);

        etTimeInterval.setText(String.valueOf(mActivity.getDrainParameterBean().alarmTimeInterval));

        btnWaiting.setOn(mActivity.getDrainParameterBean().isDrainManualEmptying);
        btnNegpreDrain.setOn(mActivity.getDrainParameterBean().isNegpreDrain);
        btnDeduct.setOn(mActivity.getRetainParamBean().isAbdomenRetainingDeduct);
        btnWakeUp.setOn(mActivity.getRetainParamBean().isAlarmWakeUp);
        btnUltrafiltration.setOn(mActivity.getRetainParamBean().isZeroCycleUltrafiltration);
        btnDormancy.setOn(mActivity.getmParameterEniity().isDormancy);
        btnTouchTone.setOn(mActivity.getmParameterEniity().isTouchTone);

        showTimeInterval();

        etPerfusionWarningValue.setText(String.valueOf(mActivity.getPerfusionParameterBean().perfMaxWarningValue));

        updateTextViewWidth(tvZeroCycleSelect, mActivity.getDrainParameterBean().drainZeroCyclePercentage);
        updateTextViewWidth(tvOtherCycleSelect, mActivity.getDrainParameterBean().drainOtherCyclePercentage);


        if (1000 == mActivity.getPerfusionParameterBean().perfMaxWarningValue) {
            currPerfusionWarningSelect = 1;
        } else if (2000 == mActivity.getPerfusionParameterBean().perfMaxWarningValue) {
            currPerfusionWarningSelect = 2;
        } else if (3000 == mActivity.getPerfusionParameterBean().perfMaxWarningValue) {
            currPerfusionWarningSelect = 3;
        } else {
            currPerfusionWarningSelect = 4;
            etPerfusionWarningValue.setText(String.valueOf(mActivity.getPerfusionParameterBean().perfMaxWarningValue));
            etPerfusionWarningValue.setVisibility(View.VISIBLE);
        }

        updateTextViewWidth2(tvPerfusionWarningSelect, currPerfusionWarningSelect);

    }

    public void registerListener() {
        btnWaiting.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.getDrainParameterBean().isDrainManualEmptying = isOn;
            showTimeInterval();
        });
        btnNegpreDrain.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.getDrainParameterBean().isNegpreDrain = isOn;
        });
        btnWakeUp.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.getRetainParamBean().isAlarmWakeUp = isOn;
            showTimeInterval();
        });
        btnDeduct.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.getRetainParamBean().isAbdomenRetainingDeduct = isOn;
        });
        btnUltrafiltration.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.getRetainParamBean().isZeroCycleUltrafiltration = isOn;
        });
        btnDormancy.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.getmParameterEniity().isDormancy = isOn;
        });
        btnTouchTone.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.getmParameterEniity().isTouchTone = isOn;
            if (isOn) {
                if (mActivity != null) {
                    mActivity.RecoveryTouchEffect();
                }
            } else {
                if (mActivity != null) {
                    mActivity.DisTouchEffectAndSaveState();
                }
            }
        });
    }

    private void showTimeInterval() {

        if (btnWaiting.isOn() && btnWakeUp.isOn()) {
            layoutTimeInterval.setVisibility(View.VISIBLE);
        } else {
            layoutTimeInterval.setVisibility(View.INVISIBLE);
        }

    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_WAITING_INTERVA)) {//等待提醒间隔时间
                    etTimeInterval.setText(result);
                    mActivity.getDrainParameterBean().alarmTimeInterval = Integer.parseInt(result);
                } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_PERFUSION_WARNING_VALUE)) {//灌注警戒值
                    etPerfusionWarningValue.setText(result);
                    mActivity.getPerfusionParameterBean().perfMaxWarningValue = Integer.parseInt(result);
                }
            }
        });
    }

    @OnClick({R.id.layout_drain_threshold_zero_cycle, R.id.layout_drain_threshold_other_cycle, R.id.layout_perfusion_warning_value, R.id.et_time_interval, R.id.et_perfusion_warning_value})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_drain_threshold_zero_cycle://
                switchDrainThresholdZeroCycle(tvZeroCycleSelect, mActivity.getDrainParameterBean().drainZeroCyclePercentage);
                break;
            case R.id.layout_drain_threshold_other_cycle://
                switchDrainThresholdOtherCycle(tvOtherCycleSelect, mActivity.getDrainParameterBean().drainOtherCyclePercentage);
                break;
            case R.id.layout_perfusion_warning_value://
                switchPerfusionWarning(tvPerfusionWarningSelect, currPerfusionWarningSelect);
                break;
            case R.id.et_time_interval://
                alertNumberBoardDialog(etTimeInterval.getText().toString(), PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_WAITING_INTERVA);
                break;
            case R.id.et_perfusion_warning_value://
                alertNumberBoardDialog(etPerfusionWarningValue.getText().toString(), PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_PERFUSION_WARNING_VALUE);
                break;
        }
    }

    private void switchDrainThresholdZeroCycle(TextView mTextView, int currCycle) {

        switch (currCycle) {
            case 50://50%
                mActivity.getDrainParameterBean().drainZeroCyclePercentage = 75;
                updateTextViewWidth(mTextView, 75);
                break;
            case 75://75%
                mActivity.getDrainParameterBean().drainZeroCyclePercentage = 100;
                updateTextViewWidth(mTextView, 100);
                break;
            case 100://100%
                mActivity.getDrainParameterBean().drainZeroCyclePercentage = 120;
                updateTextViewWidth(mTextView, 120);
                break;
            case 120://120%
                mActivity.getDrainParameterBean().drainZeroCyclePercentage = 50;
                updateTextViewWidth(mTextView, 50);
                break;

        }


    }

    private void switchDrainThresholdOtherCycle(TextView mTextView, int currCycle) {

        switch (currCycle) {
            case 50://50%
                mActivity.getDrainParameterBean().drainOtherCyclePercentage = 75;
                updateTextViewWidth(mTextView, 75);
                break;
            case 75://75%
                mActivity.getDrainParameterBean().drainOtherCyclePercentage = 100;
                updateTextViewWidth(mTextView, 100);
                break;
            case 100://100%
                mActivity.getDrainParameterBean().drainOtherCyclePercentage = 120;
                updateTextViewWidth(mTextView, 120);
                break;
            case 120://120%
                mActivity.getDrainParameterBean().drainOtherCyclePercentage = 50;
                updateTextViewWidth(mTextView, 50);
                break;
        }

    }

    private void switchPerfusionWarning(TextView mTextView, int currIndex) {

        switch (currIndex) {
            case 1://1升
                mActivity.getPerfusionParameterBean().perfMaxWarningValue = 2000;
                currPerfusionWarningSelect = 2;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                break;
            case 2://2升
                mActivity.getPerfusionParameterBean().perfMaxWarningValue = 3000;
                currPerfusionWarningSelect = 3;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                break;
            case 3://3升
                etPerfusionWarningValue.setText("");
                mActivity.getPerfusionParameterBean().perfMaxWarningValue = 0;
                currPerfusionWarningSelect = 4;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                etPerfusionWarningValue.setVisibility(View.VISIBLE);
                break;
            case 4://其他
                mActivity.getPerfusionParameterBean().perfMaxWarningValue = 1000;
                currPerfusionWarningSelect = 1;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                etPerfusionWarningValue.setVisibility(View.INVISIBLE);
                break;

        }


    }

    private void updateTextViewWidth(TextView mTextView, int currValue) {
        ViewGroup.LayoutParams mLayoutParams = mTextView.getLayoutParams();
        int mWidth = 0;

        switch (currValue) {
            case 50://50%
//                mWidth = ScreenUtil.dip2px(getActivity(), 40);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_cycle_select_width_50);
                break;
            case 75://75%
//                mWidth = ScreenUtil.dip2px(getActivity(), 100);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_cycle_select_width_75);
                break;
            case 100://100%
//                mWidth = ScreenUtil.dip2px(getActivity(), 150);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_cycle_select_width_100);
                break;
            case 120://120%
//                mWidth = ScreenUtil.dip2px(getActivity(), 220);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_cycle_select_width_120);
                break;

        }
        mLayoutParams.width = mWidth;
        mTextView.setLayoutParams(mLayoutParams);
    }


    private void updateTextViewWidth2(TextView mTextView, int currIndex) {
        ViewGroup.LayoutParams mLayoutParams = mTextView.getLayoutParams();
        int mWidth = 0;

        switch (currIndex) {
            case 1://1
//                mWidth = ScreenUtil.dip2px(getActivity(), 40);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_perfusion_warning_select_width_1000);
                break;
            case 2://2
//                mWidth = ScreenUtil.dip2px(getActivity(), 120);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_perfusion_warning_select_width_2000);
                break;
            case 3://3
//                mWidth = ScreenUtil.dip2px(getActivity(), 190);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_perfusion_warning_select_width_3000);
                break;
            case 4://
//                mWidth = ScreenUtil.dip2px(getActivity(), 280);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_perfusion_warning_select_width_other);
                break;

        }
        mLayoutParams.width = mWidth;
        mTextView.setLayoutParams(mLayoutParams);
    }


    @Override
    public void notifyByThemeChanged() {

    }
}
