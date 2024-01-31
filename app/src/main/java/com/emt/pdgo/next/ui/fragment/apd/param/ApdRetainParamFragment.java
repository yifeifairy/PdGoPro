package com.emt.pdgo.next.ui.fragment.apd.param;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ApdRetainParamFragment extends BaseFragment {

    // 留腹
    @BindView(R.id.abdomenDeductSwitch)
    LabeledSwitch abdomenDeductSwitch;
    @BindView(R.id.abdomenUltSwitch)
    LabeledSwitch abdomenUltSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apd_retain_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        abdomenDeductSwitch.setOn(ApdParamSetActivity.retainParamBean.isAbdomenRetainingDeduct);
        abdomenDeductSwitch.setOnToggledListener((toggleableView, isOn) -> {
            ApdParamSetActivity.retainParamBean.isAbdomenRetainingDeduct = isOn;
        });
        abdomenUltSwitch.setOn(ApdParamSetActivity.retainParamBean.isZeroCycleUltrafiltration);
        abdomenUltSwitch.setOnToggledListener((toggleableView, isOn) -> {
            ApdParamSetActivity.retainParamBean.isZeroCycleUltrafiltration = isOn;
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