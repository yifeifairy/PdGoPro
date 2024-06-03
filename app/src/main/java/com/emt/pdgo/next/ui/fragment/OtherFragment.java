package com.emt.pdgo.next.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.interfaces.OnToggledListener;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberDialog;
import com.emt.pdgo.next.ui.view.ToggleableView;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OtherFragment extends BaseFragment {

    @BindView(R.id.label_1)
    TextView label_1;
    @BindView(R.id.label_2)
    TextView label_2;
    @BindView(R.id.label_3)
    TextView label_3;
    @BindView(R.id.label_4)
    TextView label_4;

    @BindView(R.id.abdTv)
    TextView abdTv;
    @BindView(R.id.abdSwitch)
    LabeledSwitch abdSwitch;
    @BindView(R.id.ultTv)
    TextView ultTv;
    @BindView(R.id.ultSwitch)
    LabeledSwitch ultSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue == 1000) {
            labelSelect(true,false,false,false);
        } else if (ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue == 2000) {
            labelSelect(false,true,false,false);

        } else if (ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue == 3000) {
            labelSelect(false,false,true,false);

        } else {
            labelSelect(false,false,false,true);
        }
        abdTv.setText(ApdParamSetActivity.retainParamBean.isAbdomenRetainingDeduct?"开":"关");
        ultTv.setText(ApdParamSetActivity.retainParamBean.isZeroCycleUltrafiltration?"开":"关");
        abdSwitch.setOn(ApdParamSetActivity.retainParamBean.isAbdomenRetainingDeduct);
        ultSwitch.setOn(ApdParamSetActivity.retainParamBean.isZeroCycleUltrafiltration);
        abdSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                ApdParamSetActivity.retainParamBean.isAbdomenRetainingDeduct = isOn;
                abdTv.setText(ApdParamSetActivity.retainParamBean.isAbdomenRetainingDeduct?"开":"关");
            }
        });
        ultSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                ApdParamSetActivity.retainParamBean.isZeroCycleUltrafiltration = isOn;
                ultTv.setText(ApdParamSetActivity.retainParamBean.isZeroCycleUltrafiltration?"开":"关");
            }
        });

        label_1.setOnClickListener(view -> {
            ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue = 1000;
            labelSelect(true,false,false,false);
        });
        label_2.setOnClickListener(view -> {
            ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue = 2000;
            labelSelect(false,true,false,false);
        });
        label_3.setOnClickListener(view -> {
            ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue = 3000;
            labelSelect(false,false,true,false);
        });
        label_4.setOnClickListener(view -> {
            numberDialog(PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE, EmtConstant.perfMaxWarningValueMin, EmtConstant.perfMaxWarningValueMax);
        });
    }

    private void labelSelect(boolean l1, boolean l2,boolean l3, boolean l4) {
        label_1.setSelected(l1);
        label_2.setSelected(l2);
        label_3.setSelected(l3);
        label_4.setSelected(l4);
        label_4.setText(l4?String.valueOf(ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue)+"g":"其他");
    }

    private void numberDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(getActivity(), type, min, max);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE:
                            int num = Integer.parseInt(result);
                            ApdParamSetActivity.perfusionParameterBean.perfMaxWarningValue = num;
                            if (num == 1000) {
                                labelSelect(true,false,false,false);
                            } else if (num == 2000) {
                                labelSelect(false,true,false,false);
                            } else if (num == 3000) {
                                labelSelect(false,false,true,false);
                            } else {
                                labelSelect(false,false,false,true);
                            }
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