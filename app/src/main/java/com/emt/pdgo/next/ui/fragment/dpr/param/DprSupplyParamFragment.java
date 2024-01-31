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
import com.emt.pdgo.next.model.dpr.machine.param.SupplyParam;
import com.emt.pdgo.next.ui.activity.dpr.DprParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DprSupplyParamFragment extends BaseFragment {

    @BindView(R.id.supplyFlowRateRl)
    RelativeLayout supplyFlowRateRl;
    @BindView(R.id.supplyFlowRateTv)
    TextView supplyFlowRateTv;

    @BindView(R.id.supplyFlowPeriodRl)
    RelativeLayout supplyFlowPeriodRl;
    @BindView(R.id.supplyFlowPeriodTv)
    TextView supplyFlowPeriodTv;

    @BindView(R.id.supplyProtectVolumeRl)
    RelativeLayout supplyProtectVolumeRl;
    @BindView(R.id.supplyProtectVolumeTv)
    TextView supplyProtectVolumeTv;

    @BindView(R.id.supplyMinVolumeRl)
    RelativeLayout supplyMinVolumeRl;
    @BindView(R.id.supplyMinVolumeTv)
    TextView supplyMinVolumeTv;

    private SupplyParam param;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dpr_supply_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        click();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        param = DprParamSetActivity.supplyParam;
        supplyFlowRateTv.setText(param.supplyFlowRate+"");
        supplyFlowPeriodTv.setText(param.supplyFlowPeriod+"");
        supplyProtectVolumeTv.setText(param.supplyProtectVolume+"");
        supplyMinVolumeTv.setText(param.supplyMinVolume+"");
    }

    private void click() {
        supplyFlowRateRl.setOnClickListener(v -> {
            alertNumberBoardDialog(supplyFlowRateTv.getText().toString(),PdGoConstConfig.supplyFlowRate);
        });
        supplyFlowPeriodRl.setOnClickListener(v -> {
            alertNumberBoardDialog(supplyFlowPeriodTv.getText().toString(),PdGoConstConfig.supplyFlowPeriod);
        });
        supplyProtectVolumeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(supplyProtectVolumeTv.getText().toString(),PdGoConstConfig.supplyProtectVolume);
        });
        supplyMinVolumeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(supplyMinVolumeTv.getText().toString(),PdGoConstConfig.supplyMinVolume);
        });
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                switch (mType) {
                    case PdGoConstConfig.supplyFlowRate: //等待提醒间隔时间
                        supplyFlowRateTv.setText(result);
                        param.supplyFlowRate = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.supplyFlowPeriod: //灌注警戒值
                        supplyFlowPeriodTv.setText(result);
                        param.supplyFlowPeriod = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.supplyProtectVolume: //灌注警戒值
                        supplyProtectVolumeTv.setText(result);
                        param.supplyProtectVolume = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.supplyMinVolume: //灌注警戒值
                        supplyMinVolumeTv.setText(result);
                        param.supplyMinVolume = Integer.parseInt(result);
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