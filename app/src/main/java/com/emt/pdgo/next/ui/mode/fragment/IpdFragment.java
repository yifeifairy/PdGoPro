package com.emt.pdgo.next.ui.mode.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IpdFragment extends BaseFragment {

    @BindView(R.id.totalVolRl)
    RelativeLayout totalVolRl;
    @BindView(R.id.totalVolTv)
    TextView totalVolTv;

    @BindView(R.id.cycleVolRl)
    RelativeLayout cycleVolRl;
    @BindView(R.id.cycleVolTv)
    TextView cycleVolTv;

    @BindView(R.id.cycleNumRl)
    RelativeLayout cycleNumRl;
    @BindView(R.id.cycleNumTv)
    TextView cycleNumTv;

    @BindView(R.id.retainTimeRl)
    RelativeLayout retainTimeRl;
    @BindView(R.id.retainTimeTv)
    TextView retainTimeTv;
    @BindView(R.id.finalSupplyCheck)
    CheckBox finalSupplyCheck;
    @BindView(R.id.finalRetainVolRl)
    RelativeLayout finalRetainVolRl;
    @BindView(R.id.finalRetainVolTv)
    TextView finalRetainVolTv;

    @BindView(R.id.lastRetainVolRl)
    RelativeLayout lastRetainVolRl;
    @BindView(R.id.lastRetainVolTv)
    TextView lastRetainVolTv;

    @BindView(R.id.finalSupplyRl)
    RelativeLayout finalSupplyRl;
    @BindView(R.id.finalSupplyTv)
    TextView finalSupplyTv;

    @BindView(R.id.ultVolRl)
    RelativeLayout ultVolRl;
    @BindView(R.id.ultVolTv)
    TextView ultVolTv;
    private TreatmentFragmentActivity fragmentActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.fragmentActivity =  (TreatmentFragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ipd, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }
    
    private void initView() {
        finalSupplyCheck.setChecked(fragmentActivity.entity.isFinalSupply);
        finalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            fragmentActivity.entity.isFinalSupply = isChecked;
        });
//        finalSupplyTv.setText(String.valueOf(fragmentActivity.entity.finalSupply));
        totalVolTv.setText(String.valueOf(fragmentActivity.entity.peritonealDialysisFluidTotal));
        cycleVolTv.setText(String.valueOf(fragmentActivity.entity.perCyclePerfusionVolume));
        cycleNumTv.setText(String.valueOf(fragmentActivity.entity.cycle));
//        firstPerVolTv.setText(String.valueOf(fragmentActivity.entity.firstPerfusionVolume));
        ultVolTv.setText(String.valueOf(fragmentActivity.entity.ultrafiltrationVolume));
        retainTimeTv.setText(String.valueOf(fragmentActivity.entity.abdomenRetainingTime));
        finalRetainVolTv.setText(String.valueOf(fragmentActivity.entity.abdomenRetainingVolumeFinally));
        lastRetainVolTv.setText(String.valueOf(fragmentActivity.entity.abdomenRetainingVolumeLastTime));

        totalVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(totalVolTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL);
        });
        cycleVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(cycleVolTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME);

        });
        cycleNumRl.setOnClickListener(v -> {
            alertNumberBoardDialog(cycleNumTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES);
        });
        retainTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(retainTimeTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME);

        });
        finalRetainVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(finalRetainVolTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY);
        });
        lastRetainVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(lastRetainVolTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME);

        });
//        firstPerVolRl.setOnClickListener(v -> {
//            alertNumberBoardDialog(firstPerVolTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME);
//
//        });
        ultVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(ultVolTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME);

        });

        finalSupplyRl.setOnClickListener(v -> {
            alertNumberBoardDialog(finalSupplyTv.getText().toString(), PdGoConstConfig.FINAL_SUPPLY);
        });
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL: //腹透液总量
                            totalVolTv.setText(result);
                            fragmentActivity.entity.peritonealDialysisFluidTotal = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME: //每周期灌注量
                            cycleVolTv.setText(result);
                            fragmentActivity.entity.perCyclePerfusionVolume = Integer.parseInt(result);
                            setCycleNum();
                            break;
//                        case PdGoConstConfig.FINAL_SUPPLY:
//                            finalSupplyTv.setText(result);
//                            fragmentActivity.entity.finalSupply = Integer.parseInt(result);
////                            setTotal();
////                            setCycleNum();
//                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES: //循环治疗周期数
                            cycleNumTv.setText(result);
                            fragmentActivity.entity.cycle = Integer.parseInt(result);
//                            setTotal();
                            setCyclePre();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME: //留腹时间
                            retainTimeTv.setText(result);
                            fragmentActivity.entity.abdomenRetainingTime = Integer.parseInt(result);
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY: //末次留腹量
                            finalRetainVolTv.setText(result);
                            fragmentActivity.entity.abdomenRetainingVolumeFinally = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME: //上次留腹量
                            lastRetainVolTv.setText(result);
                            fragmentActivity.entity.abdomenRetainingVolumeLastTime = Integer.parseInt(result);
                            setCycleNum();
                            break;
//                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
//                            firstPerVolTv.setText(result);
//                            fragmentActivity.entity.firstPerfusionVolume = Integer.parseInt(result);
////                            setTotal();
//                            setCycleNum();
//                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME: //预估超滤量
                            ultVolTv.setText(result);
                            fragmentActivity.entity.ultrafiltrationVolume = Integer.parseInt(result);
                            setCycleNum();
                            break;
                    }
                }
            }
        });
    }

    private void setCyclePre() {
        int perCyclePerfusionVolume = (fragmentActivity.entity.peritonealDialysisFluidTotal
                - fragmentActivity.entity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.fragmentActivity.entity.abdomenRetainingVolumeLastTime
                - fragmentActivity.entity.abdomenRetainingVolumeFinally - 500 ) / fragmentActivity.entity.cycle;
        cycleVolTv.setText(String.valueOf(perCyclePerfusionVolume));
        fragmentActivity.entity.perCyclePerfusionVolume = perCyclePerfusionVolume;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
//        setTreatTime();
        int time = (fragmentActivity.entity.abdomenRetainingTime * 60 * fragmentActivity.entity.cycle) + ((fragmentActivity.entity.firstPerfusionVolume + fragmentActivity.entity.abdomenRetainingVolumeFinally +
                fragmentActivity.entity.abdomenRetainingVolumeLastTime ) / 125);

        fragmentActivity.entity.treatTime = EmtTimeUil.getTime(time);
    }

    /**
     * 设置治疗周期数
     */
    private void setCycleNum() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  (fragmentActivity.entity.peritonealDialysisFluidTotal
                - fragmentActivity.entity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.fragmentActivity.entity.abdomenRetainingVolumeLastTime
                - fragmentActivity.entity.abdomenRetainingVolumeFinally - 500 ) / fragmentActivity.entity.perCyclePerfusionVolume;
        if(cycle <= 0){
            cycle = 1;
        }
        fragmentActivity.entity.cycle = cycle;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
//        if(fragmentActivity.entity.abdomenRetainingVolumeFinally>0){//0周期
//            fragmentActivity.entity.cycle =  fragmentActivity.entity.cycle + 1;
//        }
//        if (fragmentActivity.entity.cycle > 13) {
//            toastMessage("周期不能大于13");
//        }
        cycleNumTv.setText(String.valueOf(fragmentActivity.entity.cycle));
//        setTreatTime();
        int time = (fragmentActivity.entity.abdomenRetainingTime * 60 * fragmentActivity.entity.cycle) + ((fragmentActivity.entity.firstPerfusionVolume + fragmentActivity.entity.abdomenRetainingVolumeFinally +
                fragmentActivity.entity.abdomenRetainingVolumeLastTime ) / 125);

        fragmentActivity.entity.treatTime = EmtTimeUil.getTime(time);
    }

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void notifyByThemeChanged() {
        
    }
}