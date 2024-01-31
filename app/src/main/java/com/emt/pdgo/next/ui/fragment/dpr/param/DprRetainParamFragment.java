package com.emt.pdgo.next.ui.fragment.dpr.param;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.emt.pdgo.next.model.dpr.machine.param.RetainParam;
import com.emt.pdgo.next.ui.activity.dpr.DprParamSetActivity;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DprRetainParamFragment extends Fragment {

    @BindView(R.id.abdSwitch)
    LabeledSwitch abdSwitch;
    @BindView(R.id.fitSwitch)
    LabeledSwitch fitSwitch;

    private RetainParam param;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dpr_retain_param, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        click();
        return view;
    }

    private void initView() {
        param = DprParamSetActivity.retainParam;
        abdSwitch.setOn(param.isFinalRetainDeductl);
        fitSwitch.setOn(param.isFiltCycleOnly);
    }
    private void click() {
        abdSwitch.setOnToggledListener((toggleableView, isOn) -> {
            param.isFinalRetainDeductl = isOn;
            
        });
        fitSwitch.setOnToggledListener((toggleableView, isOn) -> {
            param.isFiltCycleOnly = isOn;
            
        });
    }

    private Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}