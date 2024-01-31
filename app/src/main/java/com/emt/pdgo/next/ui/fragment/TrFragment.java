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

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.ui.activity.MyDreamActivity;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TrFragment extends BaseFragment {


    Unbinder unbinder;

    @BindView(R.id.tv_zero_cycle_select)
    TextView tvZeroCycleSelect;
    @BindView(R.id.tv_other_cycle_select)
    TextView tvOtherCycleSelect;
    @BindView(R.id.tv_perfusion_warning_select)
    TextView tvPerfusionWarningSelect;

    @BindView(R.id.et_perfusion_warning_value)
    TextView etPerfusionWarningValue;
    @BindView(R.id.et_time_interval)
    TextView etTimeInterval;

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


    @BindView(R.id.dormancyLayout)
    LinearLayout dormancyLayout;

    private int currPerfusionWarningSelect = 1;


    public TreatmentFragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (TreatmentFragmentActivity) getActivity();
    }

    @BindView(R.id.autoTime)
    EditText autoTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tr, container, false);
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
//    private boolean isDrainManualEmptying;
//    private boolean isNegpreDrain;
//    private boolean isAbdomenRetainingDeduct;
//    private boolean isAlarmWakeUp;
//    private boolean isZeroCycleUltrafiltration;
//    private int perfusionWarningValue;
//    private int alarmTimeInterval;
//    private int otherCycle;
//    private int zeroCycle;

    public void initViewData() {
//        zeroCycle = mActivity.getmParameterEniity().drainZeroCyclePercentage;
//        otherCycle = mActivity.getmParameterEniity().drainOtherCyclePercentage;
//        isDrainManualEmptying = mActivity.getmParameterEniity().isDrainManualEmptying;
//        isNegpreDrain = mActivity.getmParameterEniity().isNegpreDrain;
//        isAbdomenRetainingDeduct = mActivity.getmParameterEniity().isAbdomenRetainingDeduct;
//        isAlarmWakeUp = mActivity.getmParameterEniity().isAlarmWakeUp;
//        isZeroCycleUltrafiltration = mActivity.getmParameterEniity().isZeroCycleUltrafiltration;
//        perfusionWarningValue = mActivity.getmParameterEniity().perfMaxWarningValue;
//        alarmTimeInterval = mActivity.getmParameterEniity().alarmTimeInterval;

        dormancyLayout.setOnClickListener(v -> {
            mActivity.doGoTOActivity(MyDreamActivity.class);
        });
//        setCanNotEditNoClick2(etPerfusionWarningValue);
//        setCanNotEditNoClick2(etTimeInterval);

//        etPerfusionWarningValue.setText(String.valueOf(mActivity.perfusionParameterBean.perfMaxWarningValue));
        etTimeInterval.setText(String.valueOf(mActivity.drainParameterBean.alarmTimeInterval));

        btnWaiting.setOn(mActivity.drainParameterBean.isDrainManualEmptying);
        btnNegpreDrain.setOn(mActivity.drainParameterBean.isNegpreDrain);
        btnDeduct.setOn(mActivity.retainParamBean.isAbdomenRetainingDeduct);
        btnWakeUp.setOn(mActivity.retainParamBean.isAlarmWakeUp);
        btnUltrafiltration.setOn(mActivity.retainParamBean.isZeroCycleUltrafiltration);
//        btnDormancy.setOn(mActivity.entity.isDormancy);
//        btnTouchTone.setOn(mActivity.entity.isTouchTone);

//        showTimeInterval();

        etPerfusionWarningValue.setText(String.valueOf(mActivity.perfusionParameterBean.perfMaxWarningValue));

        updateTextViewWidth(tvZeroCycleSelect, mActivity.drainParameterBean.drainZeroCyclePercentage);
        updateTextViewWidth(tvOtherCycleSelect, mActivity.drainParameterBean.drainOtherCyclePercentage);


        if (1000 == mActivity.perfusionParameterBean.perfMaxWarningValue) {
            currPerfusionWarningSelect = 1;
        } else if (2000 == mActivity.perfusionParameterBean.perfMaxWarningValue) {
            currPerfusionWarningSelect = 2;
        } else if (3000 == mActivity.perfusionParameterBean.perfMaxWarningValue) {
            currPerfusionWarningSelect = 3;
        } else {
            currPerfusionWarningSelect = 4;
            etPerfusionWarningValue.setText(String.valueOf(mActivity.perfusionParameterBean.perfMaxWarningValue));
            etPerfusionWarningValue.setVisibility(View.VISIBLE);
        }

        updateTextViewWidth2(tvPerfusionWarningSelect, currPerfusionWarningSelect);

    }

    public void registerListener() {
        setCanNotEditNoClick2(autoTime);
        btnWaiting.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.drainParameterBean.isDrainManualEmptying = isOn;
//            showTimeInterval();
        });
        autoTime.setOnClickListener(view -> {
            alertNumberBoardDialog(autoTime.getText().toString(), PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_4);
        });
        btnNegpreDrain.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.drainParameterBean.isNegpreDrain = isOn;
            // mActivity.amend();
//mActivity.setModifyConfig();


        });
        btnWakeUp.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.retainParamBean.isAlarmWakeUp = isOn;
            showTimeInterval();
        });
        btnDeduct.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.retainParamBean.isAbdomenRetainingDeduct = isOn;
            // mActivity.amend();
//mActivity.setModifyConfig();

        });
        btnUltrafiltration.setOnToggledListener((toggleableView, isOn) -> {
            mActivity.retainParamBean.isZeroCycleUltrafiltration = isOn;
            // mActivity.amend();
//mActivity.setModifyConfig();

        });
        btnDormancy.setEnabled(false);
//        btnDormancy.setOnToggledListener((toggleableView, isOn) -> {
//            mActivity.getmParameterEniity().isDormancy = isOn;
//        });
//        btnDormancy.setOnClickListener(v -> {
//            mActivity.doGoTOActivity(MyDreamActivity.class);
//        });
//        btnTouchTone.setOnToggledListener((toggleableView, isOn) -> {
//            mActivity.entity.isTouchTone = isOn;
//            if (isOn) {
//                mActivity.RecoveryTouchEffect();
//            } else {
//                mActivity.DisTouchEffectAndSaveState();
//            }
//        });
    }

    private void showTimeInterval() {

        if (btnWakeUp.isOn()) {
            layoutTimeInterval.setVisibility(View.VISIBLE);
        } else {
            layoutTimeInterval.setVisibility(View.INVISIBLE);
        }

    }

    private void save() {
//        showLoadingDialog();

//        mDrainParameterBean.timeInterval = Integer.parseInt(etTimeInterval.getText().toString());
//        mDrainParameterBean.thresholdValue = Integer.parseInt(etThresholdValue.getText().toString());
//        mActivity.getmParameterEniity().drainZeroCyclePercentage = zeroCycle;
//        mActivity.getmParameterEniity().drainOtherCyclePercentage = otherCycle;
////        mDrainParameterBean.timeoutAlarm = Integer.parseInt(etTimeoutAlarm.getText().toString());
////        mDrainParameterBean.rinseVolume = Integer.parseInt(etRinseVolume.getText().toString());
////        mDrainParameterBean.rinseNumber = Integer.parseInt(etRinseNumber.getText().toString());
////        mDrainParameterBean.isDrainManualEmptying = labeledSwitchEmptying.isOn();
////        mDrainParameterBean.warnTimeInterval = Integer.parseInt(etWarnTimeInterval.getText().toString());
//        mActivity.getmParameterEniity().isDrainManualEmptying = isDrainManualEmptying;
//        mActivity.getmParameterEniity().isNegpreDrain = isNegpreDrain;
//        mActivity.getmParameterEniity().isAbdomenRetainingDeduct = isAbdomenRetainingDeduct;
//        mActivity.getmParameterEniity().isAlarmWakeUp = isAlarmWakeUp;
//        mActivity.getmParameterEniity().isZeroCycleUltrafiltration = isZeroCycleUltrafiltration;
//        mActivity.getmParameterEniity().perfMaxWarningValue = perfusionWarningValue;
//        mActivity.getmParameterEniity().alarmTimeInterval = alarmTimeInterval;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mActivity.getmParameterEniity());
////        hideLoadingDialog();
////        showCommonDialog("引流参数保存成功");
//        mActivity.toastMessage("参数保存成功");
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_WAITING_INTERVA)) {//等待提醒间隔时间
                    etTimeInterval.setText(result);
                    mActivity.drainParameterBean.alarmTimeInterval = Integer.parseInt(result);
                    // mActivity.amend();
//mActivity.setModifyConfig();

                } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_PERFUSION_WARNING_VALUE)) {//灌注警戒值
                    etPerfusionWarningValue.setText(result);
                    mActivity.perfusionParameterBean.perfMaxWarningValue = Integer.parseInt(result);
                    // mActivity.amend();
//mActivity.setModifyConfig();

                } else if (mType.equals(PdGoConstConfig.AUTO_SLEEP)) {//灌注警戒值
                    autoTime.setText(result);
                    mActivity.retainParamBean.time = Integer.parseInt(result);
                    // mActivity.amend();
//mActivity.setModifyConfig();
                }
            }
        });
    }

    private boolean isDrainManualEmptying,isNegpreDrain,isAbdomenRetainingDeduct,isAlarmWakeUp,isZeroCycleUltrafiltration;
    private int drainZeroCyclePercentage,drainOtherCyclePercentage;

    public void isConfirm() {
        DrainParameterBean drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        btnWaiting.setOn(drainParameterBean.isDrainManualEmptying);
        btnNegpreDrain.setOn(drainParameterBean.isNegpreDrain);
        updateTextViewWidth(tvZeroCycleSelect, drainParameterBean.drainZeroCyclePercentage);
        updateTextViewWidth(tvOtherCycleSelect, drainParameterBean.drainOtherCyclePercentage);
        etTimeInterval.setText(String.valueOf(drainParameterBean.alarmTimeInterval));
//        layoutTimeInterval.setVisibility(drainParameterBean.isDrainManualEmptying ? View.VISIBLE : View.GONE);
        PerfusionParameterBean perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        if (1000 == perfusionParameterBean.perfMaxWarningValue) {
            currPerfusionWarningSelect = 1;
        } else if (2000 ==perfusionParameterBean.perfMaxWarningValue) {
            currPerfusionWarningSelect = 2;
        } else if (3000 == perfusionParameterBean.perfMaxWarningValue) {
            currPerfusionWarningSelect = 3;
        } else {
            currPerfusionWarningSelect = 4;
            etPerfusionWarningValue.setText(String.valueOf(perfusionParameterBean.perfMaxWarningValue));
            etPerfusionWarningValue.setVisibility(View.VISIBLE);
        }
        updateTextViewWidth2(tvPerfusionWarningSelect, currPerfusionWarningSelect);

        RetainParamBean retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        btnDeduct.setOn(retainParamBean.isAbdomenRetainingDeduct);
        btnWakeUp.setOn(retainParamBean.isAlarmWakeUp);
        btnUltrafiltration.setOn(retainParamBean.isZeroCycleUltrafiltration);
    }

    @OnClick({R.id.layout_drain_threshold_zero_cycle, R.id.layout_drain_threshold_other_cycle, R.id.layout_perfusion_warning_value, R.id.et_time_interval, R.id.et_perfusion_warning_value})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_drain_threshold_zero_cycle://
                switchDrainThresholdZeroCycle(tvZeroCycleSelect, mActivity.drainParameterBean.drainZeroCyclePercentage);
                break;
            case R.id.layout_drain_threshold_other_cycle://
                switchDrainThresholdOtherCycle(tvOtherCycleSelect, mActivity.drainParameterBean.drainOtherCyclePercentage);
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
                mActivity.drainParameterBean.drainZeroCyclePercentage = 75;
                updateTextViewWidth(mTextView, 75);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
            case 75://75%
                mActivity.drainParameterBean.drainZeroCyclePercentage = 100;
                updateTextViewWidth(mTextView, 100);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
            case 100://100%
                mActivity.drainParameterBean.drainZeroCyclePercentage = 120;
                updateTextViewWidth(mTextView, 120);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
            case 120://120%
                mActivity.drainParameterBean.drainZeroCyclePercentage = 50;
                updateTextViewWidth(mTextView, 50);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;

        }


    }

    private void switchDrainThresholdOtherCycle(TextView mTextView, int currCycle) {

        switch (currCycle) {
            case 50://50%
                mActivity.drainParameterBean.drainOtherCyclePercentage = 75;
                updateTextViewWidth(mTextView, 75);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
            case 75://75%
                mActivity.drainParameterBean.drainOtherCyclePercentage = 100;
                updateTextViewWidth(mTextView, 100);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
            case 100://100%
                mActivity.drainParameterBean.drainOtherCyclePercentage = 120;
                updateTextViewWidth(mTextView, 120);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
            case 120://120%
                mActivity.drainParameterBean.drainOtherCyclePercentage = 50;
                updateTextViewWidth(mTextView, 50);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
        }

    }

    private void switchPerfusionWarning(TextView mTextView, int currIndex) {

        switch (currIndex) {
            case 1://1升
                mActivity.perfusionParameterBean.perfMaxWarningValue = 2000;
                currPerfusionWarningSelect = 2;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
            case 2://2升
                mActivity.perfusionParameterBean.perfMaxWarningValue = 3000;
                currPerfusionWarningSelect = 3;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                // mActivity.amend();
//mActivity.setModifyConfig();

                break;
            case 3://3升
//                etPerfusionWarningValue.setText("");
                mActivity.perfusionParameterBean.perfMaxWarningValue = 4000;
                currPerfusionWarningSelect = 4;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                etPerfusionWarningValue.setVisibility(View.VISIBLE);
                etPerfusionWarningValue.setText(String.valueOf(mActivity.perfusionParameterBean.perfMaxWarningValue));
                break;
            case 4://其他
                mActivity.perfusionParameterBean.perfMaxWarningValue = 1000;
                currPerfusionWarningSelect = 1;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                etPerfusionWarningValue.setVisibility(View.INVISIBLE);
                // mActivity.amend();
//mActivity.setModifyConfig();

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