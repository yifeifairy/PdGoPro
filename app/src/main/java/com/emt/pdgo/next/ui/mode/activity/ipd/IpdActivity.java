package com.emt.pdgo.next.ui.mode.activity.ipd;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IpdActivity extends BaseActivity {

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

    @BindView(R.id.btn_submit)
    StateButton btn_submit;
    @BindView(R.id.btnNext)
    StateButton btnNext;
    private IpdBean entity;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_ipd);
        ButterKnife.bind(this);
    }
    @BindView(R.id.powerIv)
    ImageView powerIv;
    @BindView(R.id.currentPower)
    TextView currentPower;
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {
            if (mReceiveDeviceBean.isAcPowerIn == 1) {
                powerIv.setImageResource(R.drawable.charging);
            } else {
                if (mReceiveDeviceBean.batteryLevel < 30) {
                    powerIv.setImageResource(R.drawable.poor_power);
                } else if (30 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 60 ) {
                    powerIv.setImageResource(R.drawable.low_power);
                } else if (60 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 80 ) {
                    powerIv.setImageResource(R.drawable.mid_power);
                } else {
                    powerIv.setImageResource(R.drawable.high_power);
                }
            }
            currentPower.setText(mReceiveDeviceBean.batteryLevel+"");
        });
    }
    @Override
    public void registerEvents() {
        if (MyApplication.chargeFlag == 1) {
            powerIv.setImageResource(R.drawable.charging);
        } else {
            if (MyApplication.batteryLevel < 30) {
                powerIv.setImageResource(R.drawable.poor_power);
            } else if (30 < MyApplication.batteryLevel &&MyApplication.batteryLevel < 60 ) {
                powerIv.setImageResource(R.drawable.low_power);
            } else if (60 < MyApplication.batteryLevel &&MyApplication.batteryLevel <= 80 ) {
                powerIv.setImageResource(R.drawable.mid_power);
            } else {
                powerIv.setImageResource(R.drawable.high_power);
            }
        }
        currentPower.setText(MyApplication.batteryLevel+"");
        sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
        btnNext.setOnClickListener(v -> {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, entity);
            new Thread(() -> {
                RxEntity hisRx = new RxEntity();
                hisRx.time = EmtTimeUil.getTime();
                hisRx.perVol = entity.peritonealDialysisFluidTotal;
                hisRx.perCycleVol = entity.perCyclePerfusionVolume;
                hisRx.treatCycle = entity.cycle;
                hisRx.firstPerVol = entity.firstPerfusionVolume;
                hisRx.abdTime = entity.abdomenRetainingTime;
                hisRx.endAbdVol = entity.abdomenRetainingVolumeFinally;
                hisRx.lastTimeAbdVol = entity.abdomenRetainingVolumeLastTime;
                hisRx.ult = entity.ultrafiltrationVolume;
                hisRx.ulTreatTime = "1小时";
//                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
                EmtDataBase
                        .getInstance(IpdActivity.this)
                        .getRxDao()
                        .insertRx(hisRx);
            });
            MyApplication.apdMode = 1;
            doGoTOActivity(PreHeatActivity.class);
        });
        btn_submit.setOnClickListener(v -> {
            doGoTOActivity(ApdParamSetActivity.class);
        });
        entity = PdproHelper.getInstance().ipdBean();
        entity.firstPerfusionVolume = 0;
//        entity.abdomenRetainingVolumeLastTime = 0;
//        entity.ultrafiltrationVolume = 0;
//        entity.peritonealDialysisFluidTotal = 1200;
        finalSupplyCheck.setChecked(entity.isFinalSupply);
        finalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            entity.isFinalSupply = isChecked;
        });
//        finalSupplyTv.setText(String.valueOf(entity.finalSupply));
        totalVolTv.setText(String.valueOf(entity.peritonealDialysisFluidTotal));
        cycleVolTv.setText(String.valueOf(entity.perCyclePerfusionVolume));
        cycleNumTv.setText(String.valueOf(entity.cycle));
//        firstPerVolTv.setText(String.valueOf(entity.firstPerfusionVolume));
        ultVolTv.setText(String.valueOf(entity.ultrafiltrationVolume));
        retainTimeTv.setText(String.valueOf(entity.abdomenRetainingTime));
        finalRetainVolTv.setText(String.valueOf(entity.abdomenRetainingVolumeFinally));
        lastRetainVolTv.setText(String.valueOf(entity.abdomenRetainingVolumeLastTime));

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

    @Override
    public void initViewData() {
        initHeadTitleBar("IPD模式","参数设置");
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
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
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME: //每周期灌注量
                            cycleVolTv.setText(result);
                            entity.perCyclePerfusionVolume = Integer.parseInt(result);
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
                            entity.cycle = Integer.parseInt(result);
//                            setTotal();
                            setCyclePre();
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
//                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
//                            firstPerVolTv.setText(result);
//                            entity.firstPerfusionVolume = Integer.parseInt(result);
////                            setTotal();
//                            setCycleNum();
//                            break;
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
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
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
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
//        if(entity.abdomenRetainingVolumeFinally>0){//0周期
//            entity.cycle =  entity.cycle + 1;
//        }
//        if (entity.cycle > 13) {
//            toastMessage("周期不能大于13");
//        }
        cycleNumTv.setText(String.valueOf(entity.cycle));
//        setTreatTime();
        int time = (entity.abdomenRetainingTime * 60 * entity.cycle) + ((entity.firstPerfusionVolume + entity.abdomenRetainingVolumeFinally +
                entity.abdomenRetainingVolumeLastTime ) / 125);

        entity.treatTime = EmtTimeUil.getTime(time);
    }

    @Override
    public void notifyByThemeChanged() {

    }
}