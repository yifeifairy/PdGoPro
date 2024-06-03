package com.emt.pdgo.next.ui.activity.apd.prescrip;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.ExpertBean;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentPrescriptBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.dialog.SpecialNumberDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpertActivity extends BaseActivity {

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

    @BindView(R.id.baseSupplyCycleRl)
    RelativeLayout baseSupplyCycleRl;
    @BindView(R.id.baseSupplyCycleTv)
    TextView baseSupplyCycleTv;
    @BindView(R.id.baseSupplyVolRl)
    RelativeLayout baseSupplyVolRl;
    @BindView(R.id.baseSupplyVolTv)
    TextView baseSupplyVolTv;

    @BindView(R.id.specialSupplyCycleRl)
    RelativeLayout specialSupplyCycleRl;
    @BindView(R.id.specialSupplyCycleTv)
    TextView specialSupplyCycleTv;
    @BindView(R.id.specialSupplyVolRl)
    RelativeLayout specialSupplyVolRl;
    @BindView(R.id.specialSupplyVolTv)
    TextView specialSupplyVolTv;

    @BindView(R.id.baseSupplyLl)
    LinearLayout baseSupplyLl;
    @BindView(R.id.osmSupplyLl)
    LinearLayout osmSupplyLl;

    @BindView(R.id.finalSupplyCheck)
    CheckBox finalSupplyCheck;

    @BindView(R.id.cycleMyself)
    CheckBox cycleMyself;

    @BindView(R.id.btn_submit)
    StateButton btn_submit;
    private ExpertBean entity;
    @BindView(R.id.btnNext)
    StateButton btnNext;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_expert);
        ButterKnife.bind(this);
        initHeadTitleBar("专家模式","参数设置");
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
        btn_submit.setOnClickListener(v -> {
            doGoTOActivity(ApdParamSetActivity.class);
        });
        cycleMyself.setOnCheckedChangeListener((buttonView, isChecked) -> {
            entity.cycleMyself = isChecked;
            setBg(isChecked);
            if (isChecked) {
                setSupplyTotal(entity);
            } else {
                setCycleNum();
            }
        });
        baseSupplyCycleRl.setOnClickListener(v -> {
            SpecialNumberDialog dialog = new SpecialNumberDialog(this, entity, 0, baseSupplyCycleTv);
            dialog.show();
            dialog.setOnDialogResultListener((model, expertBean) -> {
                baseSupplyCycleTv.setText(expertBean.baseSupplyCycle.toString());
                setSupplyTotal(expertBean);
            });
        });
        baseSupplyVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(baseSupplyVolTv.getText().toString(), PdGoConstConfig.EXPERT_BASE_SUPPLY_VOL);
        });
        specialSupplyCycleRl.setOnClickListener(v -> {
            SpecialNumberDialog dialog = new SpecialNumberDialog(this, entity, 1, specialSupplyCycleTv);
            dialog.show();
            dialog.setOnDialogResultListener((model, expertBean) -> {
                specialSupplyCycleTv.setText(expertBean.osmSupplyCycle.toString());
                setSupplyTotal(expertBean);
            });
        });
        specialSupplyVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(specialSupplyVolTv.getText().toString(), PdGoConstConfig.EXPERT_OSM_SUPPLY_VOL);
        });

        totalVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(totalVolTv.getText().toString(), PdGoConstConfig.EXPERT_TOTAL);
        });
        cycleVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(cycleVolTv.getText().toString(), PdGoConstConfig.EXPERT_CYCLE_VOL);

        });
        cycleNumRl.setOnClickListener(v -> {
            alertNumberBoardDialog(cycleNumTv.getText().toString(), PdGoConstConfig.EXPERT_CYCLE);

        });
        retainTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(retainTimeTv.getText().toString(), PdGoConstConfig.EXPERT_RETAIN_TIME);

        });
        finalRetainVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(finalRetainVolTv.getText().toString(), PdGoConstConfig.EXPERT_FINAL_RETAIN_VOL);
        });

        finalSupplyRl.setOnClickListener(v -> {
            alertNumberBoardDialog(finalSupplyTv.getText().toString(), PdGoConstConfig.EXPERT_FINAL_SUPPLY);

        });
        lastRetainVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(lastRetainVolTv.getText().toString(), PdGoConstConfig.EXPERT_LAST_RETAIN_VOL);

        });
        firstPerVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(firstPerVolTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME);

        });
        ultVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(ultVolTv.getText().toString(), PdGoConstConfig.EXPERT_ULT_VOL);
        });

        btnNext.setOnClickListener(v -> {
            MyApplication.apdMode = 8;
            next();
        });
    }

    /**
     * 是否补液
     * @param click
     */
    private void setBg(boolean click) {
        totalVolRl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        cycleNumRl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        cycleVolRl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa15);
        finalRetainVolRl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        totalVolRl.setEnabled(!click);
        cycleNumRl.setEnabled(!click);
        cycleVolRl.setEnabled(!click);
        finalRetainVolRl.setEnabled(!click);
        baseSupplyVolRl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        baseSupplyCycleRl.setBackgroundResource(click?R.drawable.pa15:R.drawable.gray_shape);
        baseSupplyVolRl.setEnabled(click);
        baseSupplyCycleRl.setEnabled(click);
        specialSupplyVolRl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        specialSupplyCycleRl.setBackgroundResource(click?R.drawable.pa15:R.drawable.gray_shape);
        specialSupplyVolRl.setEnabled(click);
        specialSupplyCycleRl.setEnabled(click);
    }

    // 指令分离
    private void apdpresci() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SerialTreatmentPrescriptBean prescript = new SerialTreatmentPrescriptBean();
        prescript.totalVolume = entity.total;//处方总灌注量
        prescript.cycle = entity.cycle;//循环周期数
        prescript.firstPerfuseVolume = 0;//首次灌注量
        prescript.cyclePerfuseVolume = entity.cycleVol;//循环周期灌注量
        prescript.lastRetainVolume = entity.lastRetainVol;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
        prescript.finalRetainVolume = entity.finalRetainVol;//末次留腹量
        prescript.retainTime = entity.retainTime;//60  //留腹时间
        prescript.ultraFiltrationVolume = entity.ultVol;//预估超滤量
        prescript.apdmodify = 0;
        mSerialRequestBean.method = "apdpresci/config";
        mSerialRequestBean.params = prescript;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        sendToMainBoard(new Gson().toJson(mRequestBean));
    }

    private void next() {
        if (entity.baseSupplyCycle.size() == 0 && entity.cycleMyself && !entity.osmSupplyCycle.contains(1)) {
            toastMessage("请选择通道1周期");
        }else if (entity.osmSupplyCycle.size() == 0 && entity.cycleMyself && !entity.baseSupplyCycle.contains(1)) {
            toastMessage("请选择通道2周期");
        } else if(!entity.cycleMyself && entity.total == 0) {
            toastMessage("请选择治疗总量");
        } else {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.EXPERT_PARAMS, entity);
            apdpresci();
            APIServiceManage.getInstance().postApdCode("Z2031");
//        if (entity.finalSupply != 0) {
//            sendCommandInterval(CommandDataHelper.getInstance().special(
//                            0,0, entity.finalSupply)
//                    ,500);
//        }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    RxEntity hisRx = new RxEntity();
//                    hisRx.time = EmtTimeUil.getTime();
//                    hisRx.perVol = entity.total;
//                    hisRx.perCycleVol = entity.cycleVol;
//                    hisRx.treatCycle = entity.cycle;
//                    hisRx.firstPerVol = entity.firstVol;
//                    hisRx.abdTime = entity.retainTime;
//                    hisRx.endAbdVol = entity.finalRetainVol;
//                    hisRx.lastTimeAbdVol = entity.lastRetainVol;
//                    hisRx.ult = entity.ultVol;
//                    hisRx.ulTreatTime = "1小时";
////                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
//                    EmtDataBase
//                            .getInstance(ExpertActivity.this)
//                            .getRxDao()
//                            .insertRx(hisRx);
//                }
//            });
            doGoTOActivity(PreHeatActivity.class);
        }
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.CYCLE_PARAMS, cycleBean);
    }

    @Override
    public void initViewData() {
        entity = PdproHelper.getInstance().expertBean();
        totalVolTv.setText(String.valueOf(entity.total));
        cycleVolTv.setText(String.valueOf(entity.cycleVol));
        cycleNumTv.setText(String.valueOf(entity.cycle));
        firstPerVolTv.setText(String.valueOf(entity.firstVol));
        ultVolTv.setText(String.valueOf(entity.ultVol));
        retainTimeTv.setText(String.valueOf(entity.retainTime));
        finalSupplyTv.setText(String.valueOf(entity.finalSupply));
        finalRetainVolTv.setText(String.valueOf(entity.finalRetainVol));
        lastRetainVolTv.setText(String.valueOf(entity.lastRetainVol));
        finalSupplyTv.setText(String.valueOf(entity.finalSupply));
        baseSupplyCycleTv.setText(entity.baseSupplyCycle.toString());
        baseSupplyVolTv.setText(String.valueOf(entity.baseSupplyVol));
        setBg(entity.cycleMyself);
        cycleMyself.setChecked(entity.cycleMyself);
        specialSupplyCycleTv.setText(entity.osmSupplyCycle.toString());
        specialSupplyVolTv.setText(String.valueOf(entity.osmSupplyVol));

        finalSupplyCheck.setChecked(entity.isFinalSupply);
        finalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            entity.isFinalSupply = isChecked;
        });
    }

    private void setSupplyTotal(ExpertBean expertBean) {
        expertBean.total = expertBean.baseSupplyVol * expertBean.baseSupplyCycle.size()
                + expertBean.osmSupplyVol * expertBean.osmSupplyCycle.size();
        totalVolTv.setText(String.valueOf(
                expertBean.total
        ));
    }
    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
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
                            retainTimeTv.setText(result);
                            entity.retainTime = Integer.parseInt(result);
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.EXPERT_BASE_SUPPLY_VOL:
                            baseSupplyVolTv.setText(result);
                            entity.baseSupplyVol = Integer.parseInt(result);
//                            setPeriodicities();
                            setSupplyTotal(entity);
                            break;
                        case PdGoConstConfig.EXPERT_OSM_SUPPLY_VOL:
                            specialSupplyVolTv.setText(result);
                            entity.osmSupplyVol = Integer.parseInt(result);
//                            setPeriodicities();
                            setSupplyTotal(entity);
                            break;
                        case PdGoConstConfig.EXPERT_FINAL_RETAIN_VOL: //末次留腹量
                            finalRetainVolTv.setText(result);
                            entity.finalRetainVol = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_FINAL_SUPPLY: //末次留腹量
                            finalSupplyTv.setText(result);
                            entity.finalSupply = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_LAST_RETAIN_VOL: //上次留腹量
                            lastRetainVolTv.setText(result);
                            entity.lastRetainVol = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
                            firstPerVolTv.setText(result);
                            entity.firstVol = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_ULT_VOL: //预估超滤量
                            ultVolTv.setText(result);
                            entity.ultVol = Integer.parseInt(result);
                            setCycleNum();
                            break;
                    }
                }
            }
        });
    }

    private void setCyclePre() {
        int perCyclePerfusionVolume = (entity.total
                - entity.firstVol ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - entity.finalRetainVol - 500 ) / entity.cycle;
        cycleVolTv.setText(String.valueOf(perCyclePerfusionVolume));
        entity.cycleVol = perCyclePerfusionVolume;
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
        entity.cycle = cycle;
        cycleNumTv.setText(String.valueOf(entity.cycle));
    }

    @Override
    public void notifyByThemeChanged() {

    }
}