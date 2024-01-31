package com.emt.pdgo.next.ui.fragment.dpr.param;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.model.dpr.machine.param.DrainParam;
import com.emt.pdgo.next.ui.activity.dpr.DprParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DprDrainParamFragment extends BaseFragment {

    @BindView(R.id.drainFlowRateRl)
    RelativeLayout drainFlowRateRl;
    @BindView(R.id.drainFlowRateTv)
    TextView drainFlowRateTv;
    @BindView(R.id.drainFlowPeriodRl)
    RelativeLayout drainFlowPeriodRl;
    @BindView(R.id.drainFlowPeriodTv)
    TextView drainFlowPeriodTv;
    @BindView(R.id.vacDrainSwitch)
    LabeledSwitch vacDrainSwitch;
    @BindView(R.id.vacDrainTimeRl)
    RelativeLayout vacDrainTimeRl;
    @BindView(R.id.vacDrainTimeTv)
    TextView vacDrainTimeTv;
    @BindView(R.id.finalDrainEmptySwitch)
    LabeledSwitch finalDrainEmptySwitch;
    @BindView(R.id.finalDrainWaitSwitch)
    LabeledSwitch finalDrainWaitSwitch;
    @BindView(R.id.finalDrainEmptyWaitTimeRl)
    RelativeLayout finalDrainEmptyWaitTimeRl;
    @BindView(R.id.finalDrainEmptyWaitTimeTv)
    TextView finalDrainEmptyWaitTimeTv;
    @BindView(R.id.drainPassRateRl)
    RelativeLayout drainPassRateRl;
    @BindView(R.id.drainPassRateTv)
    TextView drainPassRateTv;

    private DrainParam drainParam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dpr_drain_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        click();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        drainParam = DprParamSetActivity.drainParam;
        drainFlowRateTv.setText(drainParam.drainFlowRate+"");
        drainFlowPeriodTv.setText(drainParam.drainFlowPeriod+"");
        vacDrainTimeTv.setText(drainParam.VaccumDraintimes+"");
        finalDrainEmptyWaitTimeTv.setText(drainParam.finalDrainEmptyWaitTime+"");
//        updateTextViewWidth(drainPassRateTv, drainParam.drainPassRate);
        drainPassRateTv.setText(drainParam.drainPassRate+"");
        finalDrainEmptySwitch.setOn(drainParam.isFinalDrainEmpty);
        vacDrainSwitch.setOn(drainParam.isVaccumDrain);
        finalDrainWaitSwitch.setOn(drainParam.isFinalDrainEmptyWait);
    }

    private void click() {
//        drainPassRateRl.setOnClickListener(v -> switchRate(drainPassRateTv, drainParam.drainPassRate));
        drainFlowRateRl.setOnClickListener(v -> {
            alertNumberBoardDialog(drainFlowRateTv.getText().toString(),PdGoConstConfig.drainFlowRate);
        });
        drainFlowPeriodRl.setOnClickListener(v -> {
            alertNumberBoardDialog(drainFlowPeriodTv.getText().toString(),PdGoConstConfig.drainFlowPeriod);
        });
        finalDrainEmptyWaitTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(finalDrainEmptyWaitTimeTv.getText().toString(),PdGoConstConfig.finalDrainEmptyWaitTime);
        });
        drainPassRateRl.setOnClickListener(v -> {
            alertNumberBoardDialog(drainPassRateTv.getText().toString(),PdGoConstConfig.drainPassRate);
        });
        vacDrainTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(vacDrainTimeTv.getText().toString(),PdGoConstConfig.VaccumDraintimes);
        });

        finalDrainEmptySwitch.setOnToggledListener((toggleableView, isOn) -> {
            drainParam.isFinalDrainEmpty = isOn;
            
        });
        vacDrainSwitch.setOnToggledListener((toggleableView, isOn) -> {
            drainParam.isVaccumDrain = isOn;
            
        });
        finalDrainWaitSwitch.setOnToggledListener((toggleableView, isOn) -> {
            drainParam.isFinalDrainEmptyWait = isOn;
            
        });
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                switch (mType) {
                    case PdGoConstConfig.drainFlowRate: //等待提醒间隔时间
                        drainFlowRateTv.setText(result);
                        drainParam.drainFlowRate = Integer.parseInt(result);
                        
                        break;
                    case PdGoConstConfig.drainFlowPeriod: //灌注警戒值
                        drainFlowPeriodTv.setText(result);
                        drainParam.drainFlowPeriod = Integer.parseInt(result);
                        
                        break;
                    case PdGoConstConfig.drainPassRate: //灌注警戒值
                        drainPassRateTv.setText(result);
                        drainParam.drainPassRate = Integer.parseInt(result);
                        
                        break;
                    case PdGoConstConfig.finalDrainEmptyWaitTime: //灌注警戒值
                        finalDrainEmptyWaitTimeTv.setText(result);
                        drainParam.finalDrainEmptyWaitTime = Integer.parseInt(result);
                        
                        break;
                    case PdGoConstConfig.VaccumDraintimes: //灌注警戒值
                        vacDrainTimeTv.setText(result);
                        drainParam.VaccumDraintimes = Integer.parseInt(result);
                        
                        break;
                }
            }
        });
    }

    private void switchRate(TextView mTextView, int currCycle) {

        switch (currCycle) {
            case 50://50%
                drainParam.drainPassRate = 75;
                updateTextViewWidth(mTextView, 75);
                break;
            case 75://75%
                drainParam.drainPassRate = 100;
                updateTextViewWidth(mTextView, 100);
                break;
            case 100://100%
                drainParam.drainPassRate = 120;
                updateTextViewWidth(mTextView, 120);
                break;
            case 120://120%
                drainParam.drainPassRate = 50;
                updateTextViewWidth(mTextView, 50);
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