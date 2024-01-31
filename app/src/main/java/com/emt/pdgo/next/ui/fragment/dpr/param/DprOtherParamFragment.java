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
import com.emt.pdgo.next.model.dpr.machine.param.DprOtherParam;
import com.emt.pdgo.next.ui.activity.dpr.DprParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DprOtherParamFragment extends BaseFragment {

    @BindView(R.id.totalRemainderThodRl)
    RelativeLayout totalRemainderThodRl;
    @BindView(R.id.totalRemainderThodTv)
    TextView totalRemainderThodTv;

    @BindView(R.id.perfuseDecDrainThodRl)
    RelativeLayout perfuseDecDrainThodRl;
    @BindView(R.id.perfuseDecDrainThodTv)
    TextView perfuseDecDrainThodTv;

    @BindView(R.id.dprsuppThresholdRl)
    RelativeLayout dprsuppThresholdRl;
    @BindView(R.id.dprsuppThresholdTv)
    TextView dprsuppThresholdTv;

    @BindView(R.id.dprFlowPeriodRl)
    RelativeLayout dprFlowPeriodRl;
    @BindView(R.id.dprFlowPeriodTv)
    TextView dprFlowPeriodTv;

    @BindView(R.id.faultTimeRl)
    RelativeLayout faultTimeRl;
    @BindView(R.id.faultTimeTv)
    TextView faultTimeTv;

    private DprOtherParam param;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dpr_other_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        click();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        param = DprParamSetActivity.otherParam;
        totalRemainderThodTv.setText(param.TotalRemainderThod+"");
        perfuseDecDrainThodTv.setText(param.PerfuseDecDrainThod+"");
        dprsuppThresholdTv.setText(param.DprSuppthreshold+"");
        dprFlowPeriodTv.setText(param.DprFlowPeriod+"");
        faultTimeTv.setText(param.FaultTime+"");
    }

    private void click() {
        totalRemainderThodRl.setOnClickListener(v -> {
            alertNumberBoardDialog(totalRemainderThodTv.getText().toString(), PdGoConstConfig.TotalRemainder);
        });
        perfuseDecDrainThodRl.setOnClickListener(v -> {
            alertNumberBoardDialog(perfuseDecDrainThodTv.getText().toString(), PdGoConstConfig.PerfuseDecDrain);
        });
        dprsuppThresholdRl.setOnClickListener(v -> {
            alertNumberBoardDialog(dprsuppThresholdTv.getText().toString(), PdGoConstConfig.DprSuppThreshold);
        });
        dprFlowPeriodRl.setOnClickListener(v -> {
            alertNumberBoardDialog(dprFlowPeriodTv.getText().toString(), PdGoConstConfig.DprFlowPeriod);
        });
        faultTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(faultTimeTv.getText().toString(), PdGoConstConfig.FaultTime);
        });
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                switch (mType) {
                    case PdGoConstConfig.TotalRemainder: //等待提醒间隔时间
                        totalRemainderThodTv.setText(result);
                        param.TotalRemainderThod = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.PerfuseDecDrain: //灌注警戒值
                        perfuseDecDrainThodTv.setText(result);
                        param.PerfuseDecDrainThod = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.DprSuppThreshold: //灌注警戒值
                        dprsuppThresholdTv.setText(result);
                        param.DprSuppthreshold = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.DprFlowPeriod: //灌注警戒值
                        dprFlowPeriodTv.setText(result);
                        param.DprFlowPeriod = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.FaultTime: //灌注警戒值
                        faultTimeTv.setText(result);
                        param.FaultTime = Integer.parseInt(result);
                        break;
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