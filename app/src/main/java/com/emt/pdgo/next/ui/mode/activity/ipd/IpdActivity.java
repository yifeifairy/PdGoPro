package com.emt.pdgo.next.ui.mode.activity.ipd;

import android.app.Dialog;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IpdActivity extends BaseActivity {

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

    @BindView(R.id.isFinalBtn)
    Button isFinalBtn;

    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;
    private IpdBean ipdBean;

    @BindView(R.id.ipdBtn)
    Button ipdBtn;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_ipd);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {

//        btnNext.setOnClickListener(v -> {
//            CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, ipdBean);
//            MyApplication.apdMode = 1;
//            doGoTOActivity(PreHeatActivity.class);
//        });
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        if (ipdBean == null) {
            ipdBean = PdproHelper.getInstance().ipdBean();
        }
        isFinalBtn.setText(ipdBean.isFinalSupply? "最末袋(通道1):已开启": "最末袋(通道1):已关闭");
        totalVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL, EmtConstant.totalVolMin, EmtConstant.totalVolMax);
        });
        cycleVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME, EmtConstant.cycleVolMin
                    ,ipdBean.firstPerfusionVolume == 0 ? PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue: ipdBean.firstPerfusionVolume);
        });
        cycleNumLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES, EmtConstant.cycleNumMin, EmtConstant.cycleNumMax);
        });
        abdTimeLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME, EmtConstant.abdTimeMin, EmtConstant.abdTimeMax);

        });
        finalVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY, EmtConstant.finalVolMin, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        lastVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME, EmtConstant.lastAbdMin, EmtConstant.lastAbdMax);

        });

        btnNext.setOnClickListener(view -> {
            if (ipdBean.firstPerfusionVolume != 0 && ipdBean.perCyclePerfusionVolume > ipdBean.firstPerfusionVolume) {
                toastMessage("循环灌注量不能大于首次灌注量");
            } else {
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, ipdBean);
                doGoTOActivity(PreHeatActivity.class);
            }
        });

        isFinalBtn.setOnClickListener(view -> {
            String tips = ipdBean.isFinalSupply ? "是否关闭" : "是否开启";
            final CommonDialog dialog = new CommonDialog(this);
            dialog.setContent(tips)
                    .setBtnFirst("确定")
                    .setBtnTwo("取消")
                    .setFirstClickListener(mCommonDialog -> {
                        ipdBean.isFinalSupply = !ipdBean.isFinalSupply;
                        isFinalBtn.setText(ipdBean.isFinalSupply? "最末袋(通道1):已开启": "最末袋(通道1):已关闭");
                        mCommonDialog.dismiss();
                    })
                    .setTwoClickListener(Dialog::dismiss)
                    .show();
        });

        firstVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME, 0, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });

        ultVolLl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME, EmtConstant.ultVolMin, EmtConstant.ultVolMax);
        });

    }

    @Override
    public void initViewData() {
        initHeadTitleBar("","参数设置");
        if (ipdBean == null) {
            ipdBean = PdproHelper.getInstance().ipdBean();
        }
        totalVolTv.setText(String.valueOf(ipdBean.peritonealDialysisFluidTotal));//腹透液总量
        cycleVolTv.setText(String.valueOf(ipdBean.perCyclePerfusionVolume));//每周期灌注量
        cycleNumTv.setText(String.valueOf(ipdBean.cycle));//循环治疗周期数
        abdTimeTv.setText(String.valueOf(ipdBean.abdomenRetainingTime));//留腹时间
        finalVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeFinally));//末次留腹量
        lastVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeLastTime));//上次留腹量
        firstVolTv.setText(String.valueOf(ipdBean.firstPerfusionVolume));//首次灌注量
        ultVolTv.setText(String.valueOf(ipdBean.ultrafiltrationVolume));//预估超滤量

        ipdBtn.setText(ipdBean.firstPerfusionVolume == 0 ?"IPD模式" : "TPD模式");
    }

    private void alertNumberBoardDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(this, type, min, max);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL: //腹透液总量
                            totalVolTv.setText(result);
                            ipdBean.peritonealDialysisFluidTotal = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME: //每周期灌注量
                            cycleVolTv.setText(result);
                            ipdBean.perCyclePerfusionVolume = Integer.parseInt(result);
                            setCycleNum();
                            break;
//                        case PdGoConstConfig.FINAL_SUPPLY:
//                            finalSupplyTv.setText(result);
//                            entity.finalSupply = Integer.parseInt(result);
////                            setTotal();
////                            setCycleNum();
//                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES: //循环治疗周期数
                            cycleNumTv.setText(result);
                            ipdBean.cycle = Integer.parseInt(result);
//                            setTotal();
                            setCyclePre();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME: //留腹时间
                            abdTimeTv.setText(result);
                            ipdBean.abdomenRetainingTime = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY: //末次留腹量
                            finalVolTv.setText(result);
                            ipdBean.abdomenRetainingVolumeFinally = Integer.parseInt(result);
                            setCycleNum();
//                            setTotal();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME: //上次留腹量
                            lastVolTv.setText(result);
                            ipdBean.abdomenRetainingVolumeLastTime = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
                            firstVolTv.setText(result);
                            ipdBean.firstPerfusionVolume = Integer.parseInt(result);
//                            setTotal();
                            ipdBtn.setText(ipdBean.firstPerfusionVolume == 0 ?"IPD模式" : "TPD模式");
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME: //预估超滤量
                            ultVolTv.setText(result);
                            ipdBean.ultrafiltrationVolume = Integer.parseInt(result);
                            setCycleNum();
                            break;
                    }
                }
            }
        });
    }

    private void setTotal() {
        int total = ipdBean.cycle * ipdBean.perCyclePerfusionVolume + ipdBean.firstPerfusionVolume +
                ipdBean.abdomenRetainingVolumeFinally + EmtConstant.dep;
        totalVolTv.setText(String.valueOf(total));
        ipdBean.peritonealDialysisFluidTotal = total;
    }

    private void setCyclePre() {

        int cycle = ipdBean.firstPerfusionVolume == 0 ? ipdBean.cycle : ipdBean.cycle - 1;
        double total = (ipdBean.peritonealDialysisFluidTotal
                - ipdBean.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - ipdBean.abdomenRetainingVolumeFinally - EmtConstant.dep) / Double.parseDouble(String.valueOf(cycle)) / 50.00;

        int perCyclePerfusionVolume = (int) Math.floor(total);
        int cycleVol = perCyclePerfusionVolume * 50;
        cycleVolTv.setText(String.valueOf(cycleVol));
        ipdBean.perCyclePerfusionVolume = cycleVol;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
//        setTreatTime();
//        int time = (ipdBean.abdomenRetainingTime * 60 * ipdBean.cycle) + ((ipdBean.firstPerfusionVolume + ipdBean.abdomenRetainingVolumeFinally +
//                ipdBean.abdomenRetainingVolumeLastTime ) / 125);
//
//        ipdBean.treatTime = EmtTimeUil.getTime(time);
    }

    /**
     * 设置治疗周期数
     */
    private void setCycleNum() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除250)/每周期灌注量)
        int cycle =  (ipdBean.peritonealDialysisFluidTotal
                - ipdBean.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - ipdBean.abdomenRetainingVolumeFinally - EmtConstant.dep ) / ipdBean.perCyclePerfusionVolume;
        if(cycle <= 0){
            cycle = 1;
        }
        if (ipdBean.firstPerfusionVolume != 0) {
            cycle = cycle + 1;
        }
        ipdBean.cycle = cycle;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
//        if(entity.abdomenRetainingVolumeFinally>0){//0周期
//            entity.cycle =  entity.cycle + 1;
//        }
//        if (entity.cycle > 13) {
//            toastMessage("周期不能大于13");
//        }
        cycleNumTv.setText(String.valueOf(ipdBean.cycle));
//        setTreatTime();
//        int time = (ipdBean.abdomenRetainingTime * 60 * ipdBean.cycle) + ((ipdBean.firstPerfusionVolume + ipdBean.abdomenRetainingVolumeFinally +
//                ipdBean.abdomenRetainingVolumeLastTime ) / 125);
//
//        ipdBean.treatTime = EmtTimeUil.getTime(time);
    }

    @Override
    public void notifyByThemeChanged() {

    }
}