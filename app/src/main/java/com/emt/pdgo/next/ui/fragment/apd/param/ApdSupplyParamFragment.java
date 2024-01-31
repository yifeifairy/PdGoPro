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
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ApdSupplyParamFragment extends BaseFragment {

    // 补液参数
    @BindView(R.id.rehydrationTimeIntervalEt)
    TextView rehydrationTimeIntervalEt;
    @BindView(R.id.rehydrationTargetValueEt)
    TextView rehydrationTargetValueEt;
    @BindView(R.id.rehydrationThresholdEt)
    TextView rehydrationThresholdEt;
    @BindView(R.id.rehydrationMinimumEt)
    TextView rehydrationMinimumEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apd_supply_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        // 补液
        rehydrationTimeIntervalEt.setText(String.valueOf(ApdParamSetActivity.supplyParameterBean.supplyTimeInterval));
        rehydrationThresholdEt.setText(String.valueOf(ApdParamSetActivity.supplyParameterBean.supplyThresholdValue));
        rehydrationTargetValueEt.setText(String.valueOf(ApdParamSetActivity.supplyParameterBean.supplyTargetProtectionValue));
        rehydrationMinimumEt.setText(String.valueOf(ApdParamSetActivity.supplyParameterBean.supplyMinWeight));
    }

    @OnClick({R.id.rehydrationTimeIntervalEt, R.id.rehydrationTimeIntervalRl,
            R.id.rehydrationThresholdEt, R.id.rehydrationThresholdRl,R.id.rehydrationTargetValueEt, R.id.rehydrationTargetValueRl,
            R.id.rehydrationMinimumEt, R.id.rehydrationMinimumRl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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

        }
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
                            ApdParamSetActivity.supplyParameterBean.supplyTimeInterval = Integer.parseInt(result);
                            rehydrationTimeIntervalEt.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
                            rehydrationThresholdEt.setText(result);
                            ApdParamSetActivity.supplyParameterBean.supplyThresholdValue = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE: //补液目标保护值  :  0-500
                            rehydrationTargetValueEt.setText(result);
                            ApdParamSetActivity.supplyParameterBean.supplyTargetProtectionValue = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT: //启动补液的加热袋最低值  : 500-10000
                            rehydrationMinimumEt.setText(result);
                            ApdParamSetActivity.supplyParameterBean.supplyMinWeight = Integer.parseInt(result);
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