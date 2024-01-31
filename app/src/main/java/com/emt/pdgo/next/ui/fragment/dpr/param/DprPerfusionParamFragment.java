package com.emt.pdgo.next.ui.fragment.dpr.param;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.model.dpr.machine.param.PerfuseParam;
import com.emt.pdgo.next.ui.activity.dpr.DprParamSetActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DprPerfusionParamFragment extends Fragment {

    @BindView(R.id.preFlowRateRl)
    RelativeLayout preFlowRateRl;
    @BindView(R.id.preFlowRateTv)
    TextView preFlowRateTv;
    @BindView(R.id.preFlowPeriodRl)
    RelativeLayout preFlowPeriodRl;
    @BindView(R.id.preFlowPeriodTv)
    TextView preFlowPeriodTv;
    @BindView(R.id.preMaxVolRl)
    RelativeLayout preMaxVolRl;
    @BindView(R.id.preMaxVolTv)
    TextView preMaxVolTv;

    private PerfuseParam param;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dpr_perfusion_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        click();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        param = DprParamSetActivity.perfuseParam;
        preFlowRateTv.setText(param.perfuseFlowRate+"");
        preFlowPeriodTv.setText(param.perfuseFlowPeriod+"");
        preMaxVolTv.setText(param.perfuseMaxVolume+"");
    }

    private void click() {
        preFlowPeriodRl.setOnClickListener(v -> {
            alertNumberBoardDialog(preFlowPeriodTv.getText().toString(),PdGoConstConfig.perfuseFlowPeriod);
        });
        preFlowRateRl.setOnClickListener(v -> {
            alertNumberBoardDialog(preFlowRateTv.getText().toString(),PdGoConstConfig.perfuseFlowRate);
        });
        preMaxVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(preMaxVolTv.getText().toString(),PdGoConstConfig.perfuseMaxVolume);
        });
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                switch (mType) {
                    case PdGoConstConfig.perfuseFlowRate: //等待提醒间隔时间
                        preFlowRateTv.setText(result);
                        param.perfuseFlowRate = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.perfuseFlowPeriod: //灌注警戒值
                        preFlowPeriodTv.setText(result);
                        param.perfuseFlowPeriod = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.perfuseMaxVolume: //灌注警戒值
                        preMaxVolTv.setText(result);
                        param.perfuseMaxVolume = Integer.parseInt(result);
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

}