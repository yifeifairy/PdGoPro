package com.emt.pdgo.next.ui.activity.apd.prescrip;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.KidBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.MathUtil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KidActivity extends BaseActivity {

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

    @BindView(R.id.weightRl)
    RelativeLayout weightRl;
    @BindView(R.id.weightTV)
    TextView weightTV;
    @BindView(R.id.heightRl)
    RelativeLayout heightRl;
    @BindView(R.id.heightTv)
    TextView heightTV;

    @BindView(R.id.maleChecked)
    RadioButton maleChecked;
    @BindView(R.id.femaleChecked)
    RadioButton femaleChecked;
    @BindView(R.id.secrecyChecked)
    RadioButton secrecyChecked;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.bsaRl)
    RelativeLayout bsaRl;
    @BindView(R.id.bsaTv)
    TextView bsaTv;

    @BindView(R.id.checkbox)
    CheckBox checkBox;

    @BindView(R.id.sexLl)
    LinearLayout sexLl;
    @BindView(R.id.btnNext)
    StateButton btnNext;
    @BindView(R.id.finalSupplyCheck)
    CheckBox finalSupplyCheck;
    @BindView(R.id.btn_submit)
    StateButton btn_submit;
    private KidBean entity;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_kid);
        ButterKnife.bind(this);
        initHeadTitleBar("儿童模式","参数设置");
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
        btn_submit.setOnClickListener(v -> {
            doGoTOActivity(ApdParamSetActivity.class);
        });
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
    }

    @Override
    public void initViewData() {
        entity =  PdproHelper.getInstance().kidBean();
        isCheck(false);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isCheck(isChecked);
        });
        finalSupplyCheck.setChecked(entity.isFinalSupply);
        finalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            entity.isFinalSupply = isChecked;
        });
        totalVolTv.setText(String.valueOf(entity.peritonealDialysisFluidTotal));
        cycleVolTv.setText(String.valueOf(entity.perCyclePerfusionVolume));
        cycleNumTv.setText(String.valueOf(entity.cycle));
        firstPerVolTv.setText(String.valueOf(entity.firstPerfusionVolume));
        ultVolTv.setText(String.valueOf(entity.ultrafiltrationVolume));
        retainTimeTv.setText(String.valueOf(entity.abdomenRetainingTime));
        finalRetainVolTv.setText(String.valueOf(entity.abdomenRetainingVolumeFinally));
        lastRetainVolTv.setText(String.valueOf(entity.abdomenRetainingVolumeLastTime));
        weightTV.setText(String.valueOf(entity.weight));
        heightTV.setText(String.valueOf(entity.height));
        bsaTv.setText(String.valueOf(entity.bsa));
        if (entity.sex == 0) {
            maleChecked.setChecked(true);
        } else if (entity.sex == 1) {
            femaleChecked.setChecked(true);
        } else if (entity.sex == 2) {
            secrecyChecked.setChecked(true);
        }
        totalVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(totalVolTv.getText().toString(), PdGoConstConfig.KID_TOTAL,true);
        });
        cycleVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(cycleVolTv.getText().toString(), PdGoConstConfig.KID_CYCLE_VOL,true);
        });
        cycleNumRl.setOnClickListener(v -> {
            alertNumberBoardDialog(cycleNumTv.getText().toString(), PdGoConstConfig.KID_CYCLE,true);
        });
        retainTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(retainTimeTv.getText().toString(), PdGoConstConfig.KID_RETAIN_TIME,true);
        });
        finalRetainVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(finalRetainVolTv.getText().toString(), PdGoConstConfig.KID_FINAL_RETAIN_VOL,true);
        });
        lastRetainVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(lastRetainVolTv.getText().toString(), PdGoConstConfig.KID_LAST_RETAIN_VOL,true);
        });
        firstPerVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(firstPerVolTv.getText().toString(), PdGoConstConfig.KID_FIRST_VOL,true);
        });
        ultVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(ultVolTv.getText().toString(), PdGoConstConfig.KID_EST_ULT_VOL,true);

        });
        heightRl.setOnClickListener(v -> {
            alertNumberBoardDialog(heightTV.getText().toString(), PdGoConstConfig.HEIGHT,true);
        });
        weightRl.setOnClickListener(v -> {
            alertNumberBoardDialog(weightTV.getText().toString(), PdGoConstConfig.WEIGHT,true);

        });
        bsaRl.setOnClickListener(v -> {
            alertNumberBoardDialog(bsaTv.getText().toString(), PdGoConstConfig.KID_BSA,false);
        });
        finalSupplyTv.setText(String.valueOf(entity.finalSupply));

        finalSupplyRl.setOnClickListener(v -> {
            alertNumberBoardDialog(finalSupplyTv.getText().toString(), PdGoConstConfig.KID_FINAL_SUPPLY_VOL,true);
        });
        btnNext.setOnClickListener(v -> {
            if (entity.firstPerfusionVolume == 0) {
                next();
            } else {
                if (entity.firstPerfusionVolume >= entity.perCyclePerfusionVolume) {
                    next();
                } else {
                    toastMessage("每周期灌注量不能大于首次灌注量");
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("男")) {
                    entity.sex = 0;
                    setBas();
                } else if (rb.getText().equals("女")) {
                    entity.sex = 1;
                    setBas();
                } else if (rb.getText().equals("保密")) {
                    entity.sex = 2;
                    setBas();
                }
            }
        });
    }

    private void next() {
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.KID_PARAMS, entity);
//        new Thread(() -> {
//            RxEntity hisRx = new RxEntity();
//            hisRx.time = EmtTimeUil.getTime();
//            hisRx.perVol = entity.peritonealDialysisFluidTotal;
//            hisRx.perCycleVol = entity.perCyclePerfusionVolume;
//            hisRx.treatCycle = entity.cycle;
//            hisRx.firstPerVol = entity.firstPerfusionVolume;
//            hisRx.abdTime = entity.abdomenRetainingTime;
//            hisRx.endAbdVol = entity.abdomenRetainingVolumeFinally;
//            hisRx.lastTimeAbdVol = entity.abdomenRetainingVolumeLastTime;
//            hisRx.ult = entity.ultrafiltrationVolume;
//            hisRx.ulTreatTime = "1小时";
////                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
//            EmtDataBase
//                    .getInstance(KidActivity.this)
//                    .getRxDao()
//                    .insertRx(hisRx);
//        });
        MyApplication.apdMode = 7;
        APIServiceManage.getInstance().postApdCode("Z2031");
        doGoTOActivity(PreHeatActivity.class);
    }

    private void isCheck(boolean isCheck) {
        sexLl.setVisibility(isCheck ? View.VISIBLE : View.GONE);
        heightRl.setVisibility(isCheck ? View.VISIBLE : View.INVISIBLE);
    }

    private void alertNumberBoardDialog(String value, String type, boolean integer) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, integer);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.KID_TOTAL: //腹透液总量
                            totalVolTv.setText(result);
                            entity.peritonealDialysisFluidTotal = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.KID_CYCLE_VOL: //每周期灌注量
                            cycleVolTv.setText(result);
                            entity.perCyclePerfusionVolume = Integer.parseInt(result);
                            entity.bsa = Double.parseDouble(MathUtil.decimal(entity.perCyclePerfusionVolume,200));
//                            entity.bsa = (double)(entity.perCyclePerfusionVolume / 200);
//                            setBas();
                            bsaTv.setText(String.valueOf(entity.bsa));
                            setCycleNum();
                            break;
                        case PdGoConstConfig.KID_CYCLE: //循环治疗周期数
                            cycleNumTv.setText(result);
                            entity.cycle = Integer.parseInt(result);
                            setTotal();
//                            setCyclePre();
                            break;
                        case PdGoConstConfig.KID_FINAL_SUPPLY_VOL: //末次留腹量
                            finalSupplyTv.setText(result);
                            entity.finalSupply = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.KID_RETAIN_TIME: //留腹时间
                            retainTimeTv.setText(result);
                            entity.abdomenRetainingTime = Integer.parseInt(result);
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.KID_FINAL_RETAIN_VOL: //末次留腹量
                            finalRetainVolTv.setText(result);
                            entity.abdomenRetainingVolumeFinally = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.KID_LAST_RETAIN_VOL: //上次留腹量
                            lastRetainVolTv.setText(result);
                            entity.abdomenRetainingVolumeLastTime = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.KID_FIRST_VOL: //首次灌注量
                            firstPerVolTv.setText(result);
                            entity.firstPerfusionVolume = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.KID_EST_ULT_VOL: //预估超滤量
                            ultVolTv.setText(result);
                            entity.ultrafiltrationVolume = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.HEIGHT:
                            heightTV.setText(result);
                            entity.height = Integer.parseInt(result);
                            setBas();
                            break;
                        case PdGoConstConfig.WEIGHT:
                            weightTV.setText(result);
                            entity.weight = Integer.parseInt(result);
                            setBas();
                            break;
                        case PdGoConstConfig.KID_BSA:
                            double r = Double.parseDouble(result);
                            bsaTv.setText(String.valueOf(r));
                            entity.bsa = r;
                            entity.perCyclePerfusionVolume = (int) (200 * entity.bsa);
//                            entity.perCyclePerfusionVolume = Integer.parseInt(MathUtil.decimal((int)entity.bsa * 10000,200 *10000));
                            cycleVolTv.setText(String.valueOf(entity.perCyclePerfusionVolume));
                            setCycleNum();
                            break;
                    }
                }
            }
        });
    }

    private void setTotal() {
        int total = (entity.cycle) * entity.perCyclePerfusionVolume
                + entity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime
                + entity.abdomenRetainingVolumeFinally + 500 ;
        totalVolTv.setText(String.valueOf(total));
        entity.peritonealDialysisFluidTotal = total;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
        setTreatTime();
    }

    /**
     * 体表面积
     * S(m2)男=0.0057×身高（cm）+0.0121×体重（kg）+0.0882，
     *  S(m2)女=0.0073×身高（cm）+0.0127×体重（kg）-0.2106，
     *  不分男女BSA公式：S(m2)=0.0061*身高（cm）+0.0124*体重（kg）-0.0099；
     */
    private void setBas() {
        switch (entity.sex) {
            case 0:
                entity.bsa = (double) (57 * entity.height + 121 * entity.weight + 882) / 10000;
                entity.perCyclePerfusionVolume = (int) (200 * entity.bsa);
                cycleVolTv.setText(String.valueOf(entity.perCyclePerfusionVolume));
                bsaTv.setText(String.valueOf(entity.bsa));
                setCycleNum();
                break;
            case 1:
                entity.bsa = (double) (73 * entity.height + 127 * entity.weight - 2106)/1000;
                entity.perCyclePerfusionVolume = (int) (200 * entity.bsa);
                cycleVolTv.setText(String.valueOf(entity.perCyclePerfusionVolume));
                bsaTv.setText(String.valueOf(entity.bsa));
                setCycleNum();
                break;
            case 2:
                entity.bsa = (double) (61 * entity.height + 124 * entity.weight - 99)/10000;
                entity.perCyclePerfusionVolume = (int) (200 * entity.bsa);
                cycleVolTv.setText(String.valueOf(entity.perCyclePerfusionVolume));
                bsaTv.setText(String.valueOf(entity.bsa));
                setCycleNum();
                break;
        }
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
        Log.e("儿童模式","cycle--"+entity.cycle);
//        setTreatTime();
        int time = (entity.abdomenRetainingTime * 60 * entity.cycle) + ((entity.firstPerfusionVolume + entity.abdomenRetainingVolumeFinally +
                entity.abdomenRetainingVolumeLastTime ) / 125);

        entity.treatTime = EmtTimeUil.getTime(time);
    }

    private void setTreatTime() {
        int time = (entity.abdomenRetainingTime * 60 * entity.cycle) + ((entity.firstPerfusionVolume + entity.abdomenRetainingVolumeFinally +
                entity.abdomenRetainingVolumeLastTime ) / 125);

        entity.treatTime = EmtTimeUil.getTime(time);
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, entity);
//        tre.setText(String.valueOf(entity.treatTime));
    }

    @Override
    public void notifyByThemeChanged() {

    }
}