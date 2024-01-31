package com.emt.pdgo.next.ui.fragment.apd.prescription.special;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.bean.TpdBean;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TpdPrescriptionFragment extends BaseFragment {

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

    @BindView(R.id.firstPerVolRl)
    RelativeLayout firstPerVolRl;
    @BindView(R.id.firstPerVolTv)
    TextView firstPerVolTv;

    private TpdBean entity;
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
        View view = inflater.inflate(R.layout.fragment_tpd_prescription, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        entity =  fragmentActivity.tpdBean;
        totalVolTv.setText(String.valueOf(entity.peritonealDialysisFluidTotal));
        cycleVolTv.setText(String.valueOf(entity.perCyclePerfusionVolume));
        cycleNumTv.setText(String.valueOf(entity.cycle));
        finalSupplyCheck.setChecked(entity.isFinalSupply);
        finalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            entity.isFinalSupply = isChecked;
        });
        firstPerVolTv.setText(String.valueOf(entity.firstPerfusionVolume));
        ultVolTv.setText(String.valueOf(entity.ultrafiltrationVolume));
        retainTimeTv.setText(String.valueOf(entity.abdomenRetainingTime));
        finalRetainVolTv.setText(String.valueOf(entity.abdomenRetainingVolumeFinally));
        lastRetainVolTv.setText(String.valueOf(entity.abdomenRetainingVolumeLastTime));
//        finalSupplyTv.setText(String.valueOf(entity.finalSupply));
        totalVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(totalVolTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL);

        });
        cycleVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(cycleVolTv.getText().toString(), PdGoConstConfig.TPD_CYCLE_VOL);

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
                            entity.peritonealDialysisFluidTotal = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.TPD_CYCLE_VOL: //每周期灌注量
                            cycleVolTv.setText(result);
                            entity.perCyclePerfusionVolume = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES: //循环治疗周期数
                            cycleNumTv.setText(result);
                            entity.cycle = Integer.parseInt(result);
//                            setTotal();
                            setCyclePre();
                            break;
                        case PdGoConstConfig.FINAL_SUPPLY: //末次留腹量
                            finalSupplyTv.setText(result);
//                            entity.finalSupply = Integer.parseInt(result);
//                            setTotal();
//                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME: //留腹时间
                            retainTimeTv.setText(result);
                            entity.abdomenRetainingTime = Integer.parseInt(result);
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY: //末次留腹量
                            finalRetainVolTv.setText(result);
                            entity.abdomenRetainingVolumeFinally = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME: //上次留腹量
                            lastRetainVolTv.setText(result);
                            entity.abdomenRetainingVolumeLastTime = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
                            firstPerVolTv.setText(result);
                            entity.firstPerfusionVolume = Integer.parseInt(result);
//                            setTotal();
                            MyApplication.TPD_FIRST_VOL = entity.firstPerfusionVolume;
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME: //预估超滤量
                            ultVolTv.setText(result);
                            entity.ultrafiltrationVolume = Integer.parseInt(result);
                            setCycleNum();
                            break;
                    }
                }
            }
        });
    }

    private void setCyclePre() {
        int perCyclePerfusionVolume = (entity.peritonealDialysisFluidTotal
                - entity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - entity.abdomenRetainingVolumeFinally - 500 ) / entity.cycle;
        cycleVolTv.setText(String.valueOf(perCyclePerfusionVolume));
        entity.perCyclePerfusionVolume = perCyclePerfusionVolume;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TPD_PARAMS, mParameterEniity);
//        setTreatTime();
        int time = (entity.abdomenRetainingTime * 60 * entity.cycle) + ((entity.firstPerfusionVolume + entity.abdomenRetainingVolumeFinally +
                entity.abdomenRetainingVolumeLastTime ) / 125);

        entity.treatTime = EmtTimeUil.getTime(time);
    }

    /**
     * 设置治疗周期数
     */
    private void setCycleNum() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  (entity.peritonealDialysisFluidTotal
                - entity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - entity.abdomenRetainingVolumeFinally - 500 ) / entity.perCyclePerfusionVolume;
        if(cycle <= 0){
            cycle = 1;
        }
        entity.cycle = cycle;
        cycleNumTv.setText(String.valueOf(entity.cycle));
//        setTreatTime();
        int time = (entity.abdomenRetainingTime * 60 * entity.cycle) + ((entity.firstPerfusionVolume + entity.abdomenRetainingVolumeFinally +
                entity.abdomenRetainingVolumeLastTime ) / 125);

        entity.treatTime = EmtTimeUil.getTime(time);
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