package com.emt.pdgo.next.ui.fragment.dpr.treat;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.model.dpr.machine.Prescription;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.ui.activity.dpr.DprExamineActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegularFragment extends BaseFragment {

    @BindView(R.id.firstPerVolRl)
    RelativeLayout firstPerVolRl;
    @BindView(R.id.firstPerVolTv)
    TextView firstPerVolTv;
    @BindView(R.id.continueAbdVolRl)
    RelativeLayout continueAbdVolRl;
    @BindView(R.id.continueAbdVolTv)
    TextView continueAbdVolTv;
    @BindView(R.id.emptyingTimeRl)
    RelativeLayout emptyingTimeRl;
    @BindView(R.id.emptyingTimeTv)
    TextView emptyingTimeTv;
    @BindView(R.id.treatDurationRl)
    RelativeLayout treatDurationRl;
    @BindView(R.id.treatDurationTv)
    TextView treatDurationTv;
    @BindView(R.id.continuePerRoteRl)
    RelativeLayout continuePerRoteRl;
    @BindView(R.id.continuePerRoteTv)
    TextView continuePerRoteTv;
    @BindView(R.id.continueDrainRoteRl)
    RelativeLayout continueDrainRoteRl;
    @BindView(R.id.continueDrainRoteTv)
    TextView continueDrainRoteTv;

    @BindView(R.id.limitTempTv)
    TextView limitTempTv;
    @BindView(R.id.limitTempRl)
    RelativeLayout limitTempRl;

//    @BindView(R.id.fixTimeRl)
//    RelativeLayout fixTimeRl;
//    @BindView(R.id.fixTimeTv)
//    TextView fixTimeTv;

    @BindView(R.id.totalVolTv)
    TextView totalVolTv;
    @BindView(R.id.totalVolRl)
    RelativeLayout totalVolRl;
    @BindView(R.id.emptySwitch)
    LabeledSwitch emptySwitch;

    private Prescription prescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regular, container, false);
        unbinder = ButterKnife.bind(this, view);
        RxBus.get().register(this);
        initView();
        return view;
    }

    private DprExamineActivity activity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity =  (DprExamineActivity) activity;
    }

    private int i0,i1, i2, i3, i4, i5, i6, i7;
    private void initView() {
        prescription = activity.prescription;

        totalVolTv.setText(String.valueOf(prescription.totalVolume));
        treatDurationTv.setText(String.valueOf(prescription.supplying_interval));
        continuePerRoteTv.setText(String.valueOf(prescription.perfuse_rate));
        continueAbdVolTv.setText(String.valueOf(prescription.continue_retain));
        continueDrainRoteTv.setText(String.valueOf(prescription.drain_rate));
        emptyingTimeTv.setText(String.valueOf(prescription.emptying_time));
        firstPerVolTv.setText(String.valueOf(prescription.firstpersuse));
        emptySwitch.setOn(prescription.empty_enable == 1);
        float temp = (float) prescription.lowtemperature / 10;
        limitTempTv.setText(String.valueOf(temp));
        prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
        firstPerVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(firstPerVolTv.getText().toString(), PdGoConstConfig.DPR_FIRST_PER,true);
        });
        totalVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(totalVolTv.getText().toString(), PdGoConstConfig.DPR_TOTAL_AMOUNT,true);
        });
        continueAbdVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continueAbdVolTv.getText().toString(), PdGoConstConfig.DPR_LEAVE_BELLY,true);
        });
        continuePerRoteRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continuePerRoteTv.getText().toString(), PdGoConstConfig.DPR_PER_RATE,true);
        });
        continueDrainRoteRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continueDrainRoteTv.getText().toString(), PdGoConstConfig.DPR_DRAIN_RATE,true);
        });
        treatDurationRl.setOnClickListener(v -> {
            alertNumberBoardDialog(treatDurationTv.getText().toString(), PdGoConstConfig.DPR_TREAT_DURATION,true);
        });
        emptyingTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(emptyingTimeTv.getText().toString(), PdGoConstConfig.DPR_BELLY_EMPTY,true);
        });
        emptySwitch.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) prescription.empty_enable = 1 ; else prescription.empty_enable = 0;
            i4 = 1;
            prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
        });
//        fixTimeRl.setOnClickListener(v -> {
//            alertNumberBoardDialog(fixTimeTv.getText().toString(), PdGoConstConfig.DPR_FIX_TIME,true);
//        });
        limitTempRl.setOnClickListener(v -> {
            alertNumberBoardDialog(limitTempTv.getText().toString(), PdGoConstConfig.DPR_LIMIT_TEMP,false);
        });
    }

    private void alertNumberBoardDialog(String value, String type, boolean isInteger) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, isInteger);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.DPR_TOTAL_AMOUNT: // 治疗总量
                            totalVolTv.setText(result);
                            prescription.totalVolume = Integer.parseInt(result);
                            i5 = 1;
                            prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
                            break;
                        case PdGoConstConfig.DPR_TREAT_DURATION:
                            treatDurationTv.setText(result);
                            prescription.supplying_interval = Integer.parseInt(result);
                            i0 = 1;
                            prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
//                            setTotalVolTv();
                            break;
//                        case PdGoConstConfig.DPR_TOTAL_TIME: //治疗总时间
//                            int o = (int) (Double.parseDouble(result) * 100);
//                            result = Double.valueOf(result) + "" ;//转换成浮点数
//                            treatDurationTv.setText(result);
//                            prescription.supplying_interval = o;
//                            break;
                        case PdGoConstConfig.DPR_PER_RATE: //持续灌注速率
                            continuePerRoteTv.setText(result);
                            prescription.perfuse_rate = Integer.parseInt(result);
                            i3 = 1;
                            prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
//                            setTotalVolTv();
                            break;
                        case PdGoConstConfig.DPR_LEAVE_BELLY: //持续留腹量
                            continueAbdVolTv.setText(result);
                            prescription.continue_retain = Integer.parseInt(result);
                            i7 = 1;
                            prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
                            break;
                        case PdGoConstConfig.DPR_DRAIN_RATE: //持续引流速率
                            continueDrainRoteTv.setText(result);
                            prescription.drain_rate = Integer.parseInt(result);
                            i2 = 1;
                            prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
                            break;
                        case PdGoConstConfig.DPR_BELLY_EMPTY: //腹腔排空时间
                            emptyingTimeTv.setText(result);
                            prescription.emptying_time = Integer.parseInt(result);
                            i1 = 1;
                            prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
                            break;
                        case PdGoConstConfig.DPR_FIRST_PER: //首次灌注量
                            firstPerVolTv.setText(result);
                            prescription.firstpersuse = Integer.parseInt(result);
                            MyApplication.firstVol = Integer.parseInt(result);
//                            setTotalVolTv();
                            break;
//                        case PdGoConstConfig.DPR_FIX_TIME: //首次灌注量
//                            fixTimeTv.setText(result);
//                            prescription.firstpersuse = Integer.parseInt(result);
//                            break;
                        case PdGoConstConfig.DPR_LIMIT_TEMP:
                            float i = Float.parseFloat(result);
                            limitTempTv.setText(String.valueOf(i));
                            prescription.lowtemperature = (int) (i *10);
                            i6 = 1;
                            prescription.modify = i7+i6+i5+i4+i3+i2+i1+i0+"";
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void notifyByThemeChanged() {

    }

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}