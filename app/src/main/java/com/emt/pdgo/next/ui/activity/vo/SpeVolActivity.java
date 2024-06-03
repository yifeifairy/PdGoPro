package com.emt.pdgo.next.ui.activity.vo;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.bean.ExpertBean;
import com.emt.pdgo.next.ui.activity.MyDreamActivity;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.activity.local.LocalPrescriptionActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberDialog;
import com.emt.pdgo.next.ui.dialog.SpecialNumberDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeVolActivity extends BaseActivity {

    @BindView(R.id.totalVolTv)
    TextView totalVolTv;
    @BindView(R.id.totalVolLl)
    LinearLayout totalVolLl;
    @BindView(R.id.cycleVolTv)
    TextView cycleVolTv;
    @BindView(R.id.cycleVolLl)
    LinearLayout cycleVolLl;
    @BindView(R.id.cycleNumTv)
    TextView cycleNumTv;
    @BindView(R.id.cycleNumLl)
    LinearLayout cycleNumLl;
    @BindView(R.id.abdTimeTv)
    TextView abdTimeTv;
    @BindView(R.id.abdTimeLl)
    LinearLayout abdTimeLl;
    @BindView(R.id.finalVolTv)
    TextView finalVolTv;
    @BindView(R.id.finalVolLl)
    LinearLayout finalVolLl;
    @BindView(R.id.lastVolTv)
    TextView lastVolTv;
    @BindView(R.id.lastVolLl)
    LinearLayout lastVolLl;
    @BindView(R.id.ultVolTv)
    TextView ultVolTv;
    @BindView(R.id.ultVolLl)
    LinearLayout ultVolLl;
    @BindView(R.id.firstVolTv)
    TextView firstVolTv;
    @BindView(R.id.firstVolLl)
    LinearLayout firstVolLl;

    @BindView(R.id.stage_01_CycleVolLl)
    LinearLayout stage_01_CycleVolLl;
    @BindView(R.id.stage_01_CycleNumLl)
    LinearLayout stage_01_CycleNumLl;
    @BindView(R.id.stage_01_VolTv)
    TextView stage_01_VolTv;
    @BindView(R.id.stage_01_cycleNumTv)
    TextView stage_01_cycleNumTv;
    @BindView(R.id.stage_02_cycleVolTv)
    TextView stage_02_cycleVolTv;
    @BindView(R.id.stage_02_cycleNumTv)
    TextView stage_02_cycleNumTv;
    @BindView(R.id.stage_02_cycleVolLl)
    LinearLayout stage_02_cycleVolLl;
    @BindView(R.id.stage_02_cycleNumLl)
    LinearLayout stage_02_cycleNumLl;
    @BindView(R.id.isFinalBtn)
    Button isFinalBtn;
    @BindView(R.id.cycleMyself)
    CheckBox cycleMyself;

    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.btnBack)
    Button btnBack;

    @BindView(R.id.btnRecord)
    Button btnRecord;
    @BindView(R.id.btnRx)
    Button btnRx;
    @BindView(R.id.btnParam)
    Button btnParam;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_spe_vol);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {
        if (entity == null) {
            entity = PdproHelper.getInstance().expertBean();
        }
        btnBack.setOnClickListener(view -> onBackPressed());
        btnParam.setOnClickListener(view -> {
            doGoTOActivity(ApdParamSetActivity.class);
        });
        btnRecord.setOnClickListener(view -> {
            doGoTOActivity(MyDreamActivity.class);
        });
        btnRx.setOnClickListener(view -> {
            doGoTOActivity(LocalPrescriptionActivity.class);
        });
        btnNext.setOnClickListener(view -> {
            MyApplication.apdMode = 8;
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.EXPERT_PARAMS, entity);
            doGoTOActivity(PreHeatActivity.class);
        });
        stage_01_CycleNumLl.setOnClickListener(v -> {
            SpecialNumberDialog dialog = new SpecialNumberDialog(this, entity, 0, stage_01_cycleNumTv);
            dialog.show();
            dialog.setOnDialogResultListener((model, expertBean) -> {
                stage_01_cycleNumTv.setText(expertBean.baseSupplyCycle.toString());
                setSupplyTotal(expertBean);
            });
        });
        stage_02_cycleNumLl.setOnClickListener(v -> {
            SpecialNumberDialog dialog = new SpecialNumberDialog(this, entity, 1, stage_01_cycleNumTv);
            dialog.show();
            dialog.setOnDialogResultListener((model, expertBean) -> {
                stage_02_cycleNumTv.setText(expertBean.osmSupplyCycle.toString());
                setSupplyTotal(expertBean);
            });
        });
        stage_01_CycleVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_BASE_SUPPLY_VOL,  EmtConstant.cycleVolMin, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        stage_02_cycleVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_OSM_SUPPLY_VOL,EmtConstant.cycleVolMin, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });

        totalVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_TOTAL,EmtConstant.totalVolMin, EmtConstant.totalVolMax);
        });
        cycleVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_CYCLE_VOL,EmtConstant.cycleVolMin
                    ,entity.firstVol == 0 ? PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue: entity.firstVol);

        });
        cycleNumLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_CYCLE,EmtConstant.cycleNumMin, EmtConstant.cycleNumMax);

        });
        abdTimeLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_RETAIN_TIME,EmtConstant.abdTimeMin, EmtConstant.abdTimeMax);

        });
        finalVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_FINAL_RETAIN_VOL, EmtConstant.finalVolMin, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });


        lastVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_LAST_RETAIN_VOL, EmtConstant.lastAbdMin, EmtConstant.lastAbdMax);

        });
        firstVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME, 0, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        ultVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.EXPERT_ULT_VOL, EmtConstant.ultVolMin, EmtConstant.ultVolMax);
        });
    }
    private ExpertBean entity;
    @Override
    public void initViewData() {
        entity = PdproHelper.getInstance().expertBean();
        totalVolTv.setText(String.valueOf(entity.total));
        cycleVolTv.setText(String.valueOf(entity.cycleVol));
        cycleNumTv.setText(String.valueOf(entity.cycle));
        firstVolTv.setText(String.valueOf(entity.firstVol));
        ultVolTv.setText(String.valueOf(entity.ultVol));
        abdTimeTv.setText(String.valueOf(entity.retainTime));
        finalVolTv.setText(String.valueOf(entity.finalRetainVol));
        lastVolTv.setText(String.valueOf(entity.lastRetainVol));
        isFinalBtn.setText(entity.isFinalSupply? "最末袋(通道1):已开启": "最末袋(通道1):已关闭");
        stage_01_cycleNumTv.setText(entity.baseSupplyCycle.toString());
        stage_01_VolTv.setText(String.valueOf(entity.baseSupplyVol));
        setBg(entity.cycleMyself);
        cycleMyself.setChecked(entity.cycleMyself);
        cycleMyself.setOnCheckedChangeListener((compoundButton, b) -> {
            setBg(b);
        });
        stage_02_cycleNumTv.setText(entity.osmSupplyCycle.toString());
        stage_02_cycleVolTv.setText(String.valueOf(entity.osmSupplyVol));
    }

    private void setBg(boolean click) {
        totalVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        cycleNumLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        cycleVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
//        finalVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        firstVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        totalVolLl.setEnabled(!click);
        cycleNumLl.setEnabled(!click);
        cycleVolLl.setEnabled(!click);
//        finalVolLl.setEnabled(!click);
        firstVolLl.setEnabled(!click);
        stage_01_CycleNumLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_01_CycleVolLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_01_CycleNumLl.setEnabled(click);
        stage_01_CycleVolLl.setEnabled(click);
        stage_02_cycleVolLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_02_cycleNumLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_02_cycleVolLl.setEnabled(click);
        stage_02_cycleNumLl.setEnabled(click);
        if (click) {
            setSupplyTotal(entity);
        } else {
            setTotal();
        }
    }

    private void alertNumberBoardDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(this, type, min, max);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.EXPERT_TOTAL: //腹透液总量
                            totalVolTv.setText(result);
                            entity.total = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_CYCLE_VOL: //每周期灌注量
                            cycleVolTv.setText(result);
                            entity.cycleVol = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_CYCLE: //循环治疗周期数
                            cycleNumTv.setText(result);
                            entity.cycle = Integer.parseInt(result);
//                            setTotal();
                            setCyclePre();
                            break;
                        case PdGoConstConfig.EXPERT_RETAIN_TIME: //留腹时间
                            abdTimeTv.setText(result);
                            entity.retainTime = Integer.parseInt(result);
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.EXPERT_BASE_SUPPLY_VOL:
                            stage_01_VolTv.setText(result);
                            entity.baseSupplyVol = Integer.parseInt(result);
//                            setPeriodicities();
                            setSupplyTotal(entity);
                            break;
                        case PdGoConstConfig.EXPERT_OSM_SUPPLY_VOL:
                            stage_02_cycleVolTv.setText(result);
                            entity.osmSupplyVol = Integer.parseInt(result);
//                            setPeriodicities();
                            setSupplyTotal(entity);
                            break;
                        case PdGoConstConfig.EXPERT_FINAL_RETAIN_VOL: //末次留腹量
                            finalVolTv.setText(result);
                            entity.finalRetainVol = Integer.parseInt(result);
                            setTotal();
//                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_FINAL_SUPPLY: //末次留腹量
                            finalVolTv.setText(result);
                            entity.finalSupply = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_LAST_RETAIN_VOL: //上次留腹量
                            lastVolTv.setText(result);
                            entity.lastRetainVol = Integer.parseInt(result);
//                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
                            firstVolTv.setText(result);
                            entity.firstVol = Integer.parseInt(result);
                            setTotal();
//                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_ULT_VOL: //预估超滤量
                            ultVolTv.setText(result);
                            entity.ultVol = Integer.parseInt(result);
//                            setCycleNum();
                            break;
                    }
                }
            }
        });
    }

    private void setTotal() {
        int total = entity.cycle * entity.cycleVol + entity.firstVol +
                entity.finalRetainVol + 500;
        totalVolTv.setText(String.valueOf(total));
        entity.total = total;
    }

    private void setSupplyTotal(ExpertBean expertBean) {
        int total = expertBean.baseSupplyVol * expertBean.baseSupplyCycle.size()
                + expertBean.osmSupplyVol * expertBean.osmSupplyCycle.size() + 500 + expertBean.finalRetainVol;
        totalVolTv.setText(String.valueOf(total));
        expertBean.total = total;
    }

    private void setCyclePre() {
        double total = (entity.total
                - entity.firstVol ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - entity.finalRetainVol - 500) / Double.parseDouble(String.valueOf(entity.cycle)) / 50.00;

        int perCyclePerfusionVolume = (int) Math.floor(total);
        int cycleVol = perCyclePerfusionVolume * 50;

        cycleVolTv.setText(String.valueOf(cycleVol));
        entity.cycleVol = cycleVol;
    }

    /**
     * 设置治疗周期数
     */
    private void setCycleNum() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  (entity.total
                - entity.firstVol ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - entity.finalRetainVol - 500 ) / entity.cycleVol;
        if(cycle <= 0){
            cycle = 1;
        }
        if (entity.firstVol != 0) {
            cycle = cycle + 1;
        }
        entity.cycle = cycle;
        cycleNumTv.setText(String.valueOf(entity.cycle));
    }

    @Override
    public void notifyByThemeChanged() {

    }
}