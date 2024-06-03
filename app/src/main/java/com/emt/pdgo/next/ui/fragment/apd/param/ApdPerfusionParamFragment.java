package com.emt.pdgo.next.ui.fragment.apd.param;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberDialog;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ApdPerfusionParamFragment extends BaseFragment {

    // 灌注参数
    @BindView(R.id.infusionTimeIntervalEt)
    TextView infusionTimeIntervalEt;
    @BindView(R.id.infusionThresholdEt)
    TextView infusionThresholdEt;
    @BindView(R.id.infusionMaximumAlertEt)
    TextView infusionMaximumAlertEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apd_perfusion_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        // 灌注
        infusionMaximumAlertEt.setText(String.valueOf(ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue));
        infusionTimeIntervalEt.setText(String.valueOf(ApdParamSetActivity.perfusionParameterBean.perfTimeInterval));
        infusionThresholdEt.setText(String.valueOf(ApdParamSetActivity.perfusionParameterBean.perfThresholdValue));
    }

    @OnClick({
            R.id.infusionTimeIntervalEt, R.id.infusionTimeIntervalRl,R.id.infusionThresholdRl, R.id.infusionThresholdEt,
            R.id.infusionMaximumAlertEt, R.id.infusionMaximumAlertRl})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.infusionTimeIntervalEt:
            case R.id.infusionTimeIntervalRl:
                alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL, EmtConstant.perfTimeIntervalMin, EmtConstant.perfTimeIntervalMax);
                break;
            case R.id.infusionThresholdRl:
            case R.id.infusionThresholdEt:
                alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE,EmtConstant.perfThresholdValueMin, EmtConstant.perfThresholdValueMax);
                break;
            case R.id.infusionMaximumAlertEt:
            case R.id.infusionMaximumAlertRl:
                alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE,EmtConstant.perfMaxWarningValueMin, EmtConstant.perfMaxWarningValueMax);
                break;

        }
    }

    private void alertNumberBoardDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(getActivity(), type, min, max);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
                            infusionTimeIntervalEt.setText(result);
                            ApdParamSetActivity.perfusionParameterBean.perfTimeInterval = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
                            ApdParamSetActivity.perfusionParameterBean.perfThresholdValue = Integer.parseInt(result);
                            infusionThresholdEt.setText(result);
//                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PERFUSION_MIN_WEIGHT)) {//加热袋最低重量允许  :  100-1000
//
//                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PERFUSION_ALLOW_ABDOMINAL_VOLUME)) {//是否允许最末灌注减去留腹量  : 1000-3000

                            break;
                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE: //灌注最大警戒值  : 1000-3000
                            infusionMaximumAlertEt.setText(result);
                            ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue = Integer.parseInt(result);
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