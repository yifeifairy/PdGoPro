package com.emt.pdgo.next.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.interfaces.OnToggledListener;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.view.ToggleableView;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DrainFragment extends BaseFragment {

    @BindView(R.id.label_1)
    TextView label_1;
    @BindView(R.id.label_2)
    TextView label_2;
    @BindView(R.id.label_3)
    TextView label_3;
    @BindView(R.id.label_4)
    TextView label_4;

    @BindView(R.id.label_5)
    TextView label_5;
    @BindView(R.id.label_6)
    TextView label_6;
    @BindView(R.id.label_7)
    TextView label_7;
    @BindView(R.id.label_8)
    TextView label_8;

    @BindView(R.id.label_9)
    TextView label_9;
    @BindView(R.id.label_10)
    TextView label_10;
    @BindView(R.id.label_11)
    TextView label_11;
    @BindView(R.id.label_12)
    TextView label_12;

    @BindView(R.id.emptyTimeLl)
    LinearLayout emptyTimeLl;

    @BindView(R.id.emptySwitch)
    LabeledSwitch emptySwitch;
    @BindView(R.id.emptyTv)
    TextView emptyTv;

    @BindView(R.id.negSwitch)
    LabeledSwitch negSwitch;
    @BindView(R.id.negTv)
    TextView negTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drain, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        negSwitch.setOn(TreatmentFragmentActivity.drainParameterBean.isNegpreDrain);
        negTv.setText(TreatmentFragmentActivity.drainParameterBean.isNegpreDrain?"开":"关");
        emptySwitch.setOn(TreatmentFragmentActivity.drainParameterBean.isDrainManualEmptying);
        emptyTv.setText(TreatmentFragmentActivity.drainParameterBean.isDrainManualEmptying?"开":"关");
        emptyTimeLl.setVisibility(TreatmentFragmentActivity.drainParameterBean.isDrainManualEmptying?View.VISIBLE:View.GONE);
        if (TreatmentFragmentActivity.drainParameterBean.drainZeroCyclePercentage == 25) {
            zeroSelect(true,false,false,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainZeroCyclePercentage == 50) {
            zeroSelect(false,true,false,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainZeroCyclePercentage == 75) {
            zeroSelect(false,false,true,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainZeroCyclePercentage == 100) {
            zeroSelect(false,false,false,true);
        }

        if (TreatmentFragmentActivity.drainParameterBean.drainOtherCyclePercentage == 25) {
            otherSelect(true,false,false,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainOtherCyclePercentage == 50) {
            otherSelect(false,true,false,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainOtherCyclePercentage == 75) {
            otherSelect(false,false,true,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainOtherCyclePercentage == 100) {
            otherSelect(false,false,false,true);
        }

        if (TreatmentFragmentActivity.drainParameterBean.drainWarnTimeInterval == 10) {
            emptySelect(true,false,false,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainWarnTimeInterval == 20) {
            emptySelect(false,true,false,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainWarnTimeInterval == 30) {
            emptySelect(false,false,true,false);
        } else if (TreatmentFragmentActivity.drainParameterBean.drainWarnTimeInterval == 60) {
            emptySelect(false,false,false,true);
        }

        negSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                TreatmentFragmentActivity.drainParameterBean.isNegpreDrain = isOn;
                negSwitch.setOn(TreatmentFragmentActivity.drainParameterBean.isNegpreDrain);
                negTv.setText(TreatmentFragmentActivity.drainParameterBean.isNegpreDrain?"开":"关");
            }
        });
        emptySwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                TreatmentFragmentActivity.drainParameterBean.isDrainManualEmptying = isOn;
                emptySwitch.setOn(TreatmentFragmentActivity.drainParameterBean.isDrainManualEmptying);
                emptyTv.setText(TreatmentFragmentActivity.drainParameterBean.isDrainManualEmptying?"开":"关");
                emptyTimeLl.setVisibility(TreatmentFragmentActivity.drainParameterBean.isDrainManualEmptying?View.VISIBLE:View.GONE);
            }
        });

        label_1.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainZeroCyclePercentage = 25;
            zeroSelect(true,false,false,false);
        });
        label_2.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainZeroCyclePercentage = 50;
            zeroSelect(false,true,false,false);
        });
        label_3.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainZeroCyclePercentage = 75;
            zeroSelect(false,false,true,false);
        });
        label_4.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainZeroCyclePercentage = 100;
            zeroSelect(false,false,false,true);
        });

        label_5.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainOtherCyclePercentage = 25;
            otherSelect(true,false,false,false);
        });
        label_6.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainOtherCyclePercentage = 50;
            otherSelect(false,true,false,false);
        });
        label_7.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainOtherCyclePercentage = 75;
            otherSelect(false,false,true,false);
        });
        label_8.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainOtherCyclePercentage = 100;
            otherSelect(false,false,false,true);
        });

        label_9.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainWarnTimeInterval = 10;
            emptySelect(true,false,false,false);
        });
        label_10.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainWarnTimeInterval = 20;
            emptySelect(false,true,false,false);
        });
        label_11.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainWarnTimeInterval = 30;
            emptySelect(false,false,true,false);
        });
        label_12.setOnClickListener(view -> {
            TreatmentFragmentActivity.drainParameterBean.drainWarnTimeInterval = 60;
            emptySelect(false,false,false,true);
        });

    }

    private void zeroSelect(boolean l1, boolean l2,boolean l3, boolean l4) {
        label_1.setSelected(l1);
        label_2.setSelected(l2);
        label_3.setSelected(l3);
        label_4.setSelected(l4);
    }

    private void otherSelect(boolean l5, boolean l6,boolean l7, boolean l8) {
        label_5.setSelected(l5);
        label_6.setSelected(l6);
        label_7.setSelected(l7);
        label_8.setSelected(l8);
    }

    private void emptySelect(boolean l9, boolean l10,boolean l11, boolean l12) {
        label_9.setSelected(l9);
        label_10.setSelected(l10);
        label_11.setSelected(l11);
        label_12.setSelected(l12);
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