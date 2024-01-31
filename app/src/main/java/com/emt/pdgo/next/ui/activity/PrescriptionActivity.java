package com.emt.pdgo.next.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentPrescriptBean;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.HisPrescriptionBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionActivity extends BaseActivity {

    @BindView(R.id.et_prescription_item1)
    TextView etPrescriptionItem1;
    @BindView(R.id.layout_prescription_item_1)
    RelativeLayout layoutPrescriptionItem1;
    @BindView(R.id.et_prescription_item2)
    TextView etPrescriptionItem2;
    @BindView(R.id.layout_prescription_item_2)
    RelativeLayout layoutPrescriptionItem2;
    @BindView(R.id.et_prescription_item3)
    TextView etPrescriptionItem3;
    @BindView(R.id.layout_prescription_item_3)
    RelativeLayout layoutPrescriptionItem3;
    @BindView(R.id.et_prescription_item4)
    TextView etPrescriptionItem4;
    @BindView(R.id.layout_prescription_item_4)
    RelativeLayout layoutPrescriptionItem4;
    @BindView(R.id.et_prescription_item5)
    TextView etPrescriptionItem5;
    @BindView(R.id.layout_prescription_item_5)
    RelativeLayout layoutPrescriptionItem5;
    @BindView(R.id.et_prescription_item6)
    TextView etPrescriptionItem6;
    @BindView(R.id.layout_prescription_item_6)
    RelativeLayout layoutPrescriptionItem6;
    @BindView(R.id.et_prescription_item7)
    TextView etPrescriptionItem7;
    @BindView(R.id.layout_prescription_item_7)
    RelativeLayout layoutPrescriptionItem7;
    @BindView(R.id.et_prescription_item8)
    TextView etPrescriptionItem8;
    @BindView(R.id.layout_prescription_item_8)
    RelativeLayout layoutPrescriptionItem8;

    @BindView(R.id.et_prescription_item9)
    TextView etPrescriptionItem9;

    @BindView(R.id.btnNext)
    StateButton btnNext;

    @BindView(R.id.btn_back)
    StateButton btnBack;

    @BindView(R.id.finalSupplyCheck)
    CheckBox finalSupplyCheck;

    private String msg = EmtConstant.ACTIVITY_PRESCRIPTION;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_prescription);
        ButterKnife.bind(this);
        initHeadTitleBar("处方设置", "用户码");
        btnBack.setVisibility(View.GONE);
        if (checkConnectNetwork(this)) {
            if (!MyApplication.isRemoteShow) {
                getRemotePd();
            }
        }
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
    private CompositeDisposable mCompositeDisposable;
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
        mCompositeDisposable = new CompositeDisposable();
//        btnBack.setVisibility(View.INVISIBLE);
        settingLayout.setOnClickListener(v -> doGoTOActivity(SettingActivity.class));
        btnNext.setOnClickListener(v -> {
            if (checkConnectNetwork(this)) {
                addUserSnMachineRcp();
            }
            if (mParameterEniity.firstPerfusionVolume == 0) {
                next();
            } else {
                if (mParameterEniity.firstPerfusionVolume >= mParameterEniity.perCyclePerfusionVolume) {
                    next();
                } else {
                    toastMessage("每周期灌注量不能大于首次灌注量");
                }
            }
        });
    }

    private void next() {
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
//        apdpresci();
        APIServiceManage.getInstance().postApdCode("Z2031");
        Disposable mainDisposable = Observable.timer(10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    TreatmentParameterEniity mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
                    RxEntity hisRx = new RxEntity();
                    hisRx.time = EmtTimeUil.getTime();
                    hisRx.perVol = mParameterEniity.peritonealDialysisFluidTotal;
                    hisRx.perCycleVol = mParameterEniity.perCyclePerfusionVolume;
                    hisRx.treatCycle = mParameterEniity.cycle;
                    hisRx.firstPerVol = mParameterEniity.firstPerfusionVolume;
                    hisRx.abdTime = mParameterEniity.abdomenRetainingTime;
                    hisRx.endAbdVol = mParameterEniity.abdomenRetainingVolumeFinally;
                    hisRx.lastTimeAbdVol = mParameterEniity.abdomenRetainingVolumeLastTime;
                    hisRx.ult = mParameterEniity.ultrafiltrationVolume;
                    hisRx.ulTreatTime = mParameterEniity.treatTime;
//                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
                    EmtDataBase
                            .getInstance(PrescriptionActivity.this)
                            .getRxDao()
                            .insertRx(hisRx);
                });
        mCompositeDisposable.add(mainDisposable);
        doGoTOActivity(PreHeatActivity.class);
    }

    // 指令分离
    private void apdpresci() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SerialTreatmentPrescriptBean prescript = new SerialTreatmentPrescriptBean();
        prescript.totalVolume = mParameterEniity.peritonealDialysisFluidTotal;//处方总灌注量
        prescript.cycle = mParameterEniity.cycle;//循环周期数
        prescript.firstPerfuseVolume = mParameterEniity.firstPerfusionVolume;//首次灌注量
        prescript.cyclePerfuseVolume = mParameterEniity.perCyclePerfusionVolume;//循环周期灌注量
        prescript.lastRetainVolume = mParameterEniity.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
        prescript.finalRetainVolume = mParameterEniity.abdomenRetainingVolumeFinally;//末次留腹量
        prescript.retainTime = mParameterEniity.abdomenRetainingTime;//60  //留腹时间
        prescript.ultraFiltrationVolume = mParameterEniity.ultrafiltrationVolume;//预估超滤量
        prescript.apdmodify = 0;
        mSerialRequestBean.method = "apdpresci/config";
        mSerialRequestBean.params = prescript;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        sendToMainBoard(new Gson().toJson(mRequestBean));
    }

    @Override
    public void initViewData() {
        mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
        finalSupplyCheck.setChecked(mParameterEniity.isFinalSupply);
        finalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mParameterEniity.isFinalSupply = isChecked;
        });
        etPrescriptionItem1.setText(String.valueOf(mParameterEniity.peritonealDialysisFluidTotal));//腹透液总量
        etPrescriptionItem2.setText(String.valueOf(mParameterEniity.perCyclePerfusionVolume));//每周期灌注量
        etPrescriptionItem3.setText(String.valueOf(mParameterEniity.cycle));//循环治疗周期数
        etPrescriptionItem4.setText(String.valueOf(mParameterEniity.abdomenRetainingTime));//留腹时间
        etPrescriptionItem5.setText(String.valueOf(mParameterEniity.abdomenRetainingVolumeFinally));//末次留腹量
        etPrescriptionItem6.setText(String.valueOf(mParameterEniity.abdomenRetainingVolumeLastTime));//上次留腹量
        etPrescriptionItem7.setText(String.valueOf(mParameterEniity.firstPerfusionVolume));//首次灌注量
        etPrescriptionItem8.setText(String.valueOf(mParameterEniity.ultrafiltrationVolume));//预估超滤量
        etPrescriptionItem9.setText(String.valueOf(mParameterEniity.treatTime));
    }

    private Gson gson = new Gson();
    private void addUserSnMachineRcp() {
//        Map<String, Object> paramsMap = new HashMap<>();
//
////        paramsMap.put("username", "18582556015");
////        paramsMap.put("password", "123456");
//
//        paramsMap.put("machineSn",PdproHelper.getInstance().getMachineSN());
//        paramsMap.put("icodextrinTotal",mParameterEniity.peritonealDialysisFluidTotal);
//        paramsMap.put("inFlowCycle",mParameterEniity.perCyclePerfusionVolume);
//        paramsMap.put("cycle",mParameterEniity.cycle);
//        paramsMap.put("retentionTime",mParameterEniity.abdomenRetainingTime);
//        paramsMap.put("lastRetention",mParameterEniity.abdomenRetainingVolumeFinally);
//        paramsMap.put("treatTime",mParameterEniity.treatTime);
//        paramsMap.put("agoRetention",mParameterEniity.abdomenRetainingVolumeLastTime);
//        paramsMap.put("predictUlt",mParameterEniity.ultrafiltrationVolume);
//        paramsMap.put("firstInFlow",mParameterEniity.firstPerfusionVolume);
//
//        String content = gson.toJson(paramsMap);
//
//        RequestBody params = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                content);
//        RetrofitUtil.getService().addUserSnMachineRcp(params).enqueue(new Callback<MyResponse<CommonBean>>() {
//            @Override
//            public void onResponse(Call<MyResponse<CommonBean>> call, Response<MyResponse<CommonBean>> response) {
//                if (response.body() != null) {
//                    if (response.body().getData() != null) {
//                        if (response.body().getData().getCode().equals("10000")) {
//                            CacheUtils.getInstance().getACache().put(PdGoConstConfig.UID, response.body().getData().getPatientId()+"");
//                            Log.e("处方设置","添加处方成功--uid--"+response.body().getData().getPatientId());
//                        } else {
//                            Log.e("处方设置","添加处方"+response.body().getData().getMsg());
//                            saveFaultCodeLocal("添加处方,"+response.body().getData().getMsg());
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyResponse<CommonBean>> call, Throwable t) {
//                Log.e("处方设置", t.getMessage() + "--url--"+call.request().url());
//                saveFaultCodeLocal("添加处方,"+t.getMessage());
//            }
//        });
    }

    @BindView(R.id.settingLayout)
    LinearLayout settingLayout;

    private TreatmentParameterEniity mParameterEniity;

    @OnClick({R.id.btn_submit, R.id.et_prescription_item1, R.id.et_prescription_item2, R.id.et_prescription_item3, R.id.et_prescription_item4,
            R.id.et_prescription_item5, R.id.et_prescription_item6, R.id.et_prescription_item7, R.id.et_prescription_item8, R.id.btn_back,
            R.id.layout_prescription_item_1, R.id.layout_prescription_item_2, R.id.layout_prescription_item_3, R.id.layout_prescription_item_4,
            R.id.layout_prescription_item_5, R.id.layout_prescription_item_6, R.id.layout_prescription_item_7, R.id.layout_prescription_item_8,
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit://
                doGoTOActivity(BindPhoneActivity.class);
//                doGoTOActivity(PipelineConnectionActivity.class);
                break;
            case R.id.et_prescription_item1://腹透液总量
            case R.id.layout_prescription_item_1://
                alertNumberBoardDialog(etPrescriptionItem1.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL);
                break;
            case R.id.et_prescription_item2://每周期灌注量：
            case R.id.layout_prescription_item_2://
                alertNumberBoardDialog(etPrescriptionItem2.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME);
                break;
            case R.id.et_prescription_item3://循环治疗周期数：
            case R.id.layout_prescription_item_3://
                alertNumberBoardDialog(etPrescriptionItem3.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES);
                break;
            case R.id.et_prescription_item4://留腹时间：
            case R.id.layout_prescription_item_4://
                alertNumberBoardDialog(etPrescriptionItem4.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME);
                break;
            case R.id.et_prescription_item5://末次留腹量：
            case R.id.layout_prescription_item_5://
                alertNumberBoardDialog(etPrescriptionItem5.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY);
                break;
            case R.id.et_prescription_item6://上次留腹量：
            case R.id.layout_prescription_item_6://
                alertNumberBoardDialog(etPrescriptionItem6.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME);
                break;
            case R.id.et_prescription_item7://首次灌注量：
            case R.id.layout_prescription_item_7://
                alertNumberBoardDialog(etPrescriptionItem7.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME);
                break;
            case R.id.et_prescription_item8://预估超滤量：
            case R.id.layout_prescription_item_8://
                alertNumberBoardDialog(etPrescriptionItem8.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME);
                break;
//            case R.id.btn_back:
//                back();
//                Log.e("處方設置","btn_back--");
//                ActivityManager.getActivityManager().removeActivity(PrescriptionActivity.this);
//                break;
        }
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
                            etPrescriptionItem1.setText(result);
                            mParameterEniity.peritonealDialysisFluidTotal = Integer.parseInt(result);
                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME: //每周期灌注量
                            etPrescriptionItem2.setText(result);
                            mParameterEniity.perCyclePerfusionVolume = Integer.parseInt(result);
                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES: //循环治疗周期数
                            etPrescriptionItem3.setText(result);
                            mParameterEniity.cycle = Integer.parseInt(result);
//                            setTotal();
                            setCyclePre();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME: //留腹时间
                            etPrescriptionItem4.setText(result);
                            mParameterEniity.abdomenRetainingTime = Integer.parseInt(result);
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY: //末次留腹量
                            etPrescriptionItem5.setText(result);
                            mParameterEniity.abdomenRetainingVolumeFinally = Integer.parseInt(result);
//                            setTotal();
                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME: //上次留腹量
                            etPrescriptionItem6.setText(result);
                            mParameterEniity.abdomenRetainingVolumeLastTime = Integer.parseInt(result);
                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
                            etPrescriptionItem7.setText(result);
                            mParameterEniity.firstPerfusionVolume = Integer.parseInt(result);
                            MyApplication.FIRST_VOL = mParameterEniity.firstPerfusionVolume;
//                            setTotal();
                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME: //预估超滤量
                            etPrescriptionItem8.setText(result);
                            mParameterEniity.ultrafiltrationVolume = Integer.parseInt(result);
                            setPeriodicities();
                            break;
                    }
                }
            }
        });
    }
    /**
     * 预估治疗时间= （每周期留腹时间x周期数）+(（灌注量+赢流量+最末留腹量）➗125ml/s)
     */
    private void setTreatTime() {
        int time = (mParameterEniity.abdomenRetainingTime * 60 * mParameterEniity.cycle) + ((mParameterEniity.firstPerfusionVolume + mParameterEniity.abdomenRetainingVolumeFinally +
                mParameterEniity.abdomenRetainingVolumeLastTime ) / 125);

        mParameterEniity.treatTime = EmtTimeUil.getTime(time);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
        etPrescriptionItem9.setText(String.valueOf(mParameterEniity.treatTime));
    }

    private void setCyclePre() {
        int perCyclePerfusionVolume = (mParameterEniity.peritonealDialysisFluidTotal
                - mParameterEniity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.mParameterEniity.abdomenRetainingVolumeLastTime
                - mParameterEniity.abdomenRetainingVolumeFinally - 500 ) / mParameterEniity.cycle;
        etPrescriptionItem2.setText(String.valueOf(perCyclePerfusionVolume));
        mParameterEniity.perCyclePerfusionVolume = perCyclePerfusionVolume;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
        setTreatTime();
    }

    private void setTotal() {
        int total = (mParameterEniity.cycle) * mParameterEniity.perCyclePerfusionVolume
                + mParameterEniity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.mParameterEniity.abdomenRetainingVolumeLastTime
                + mParameterEniity.abdomenRetainingVolumeFinally + 500 ;
        etPrescriptionItem1.setText(String.valueOf(total));
        mParameterEniity.peritonealDialysisFluidTotal = total;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
        setTreatTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume");
        if(MyApplication.isReset){
            TreatmentParameterEniity mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
            etPrescriptionItem1.setText(String.valueOf(mParameterEniity.peritonealDialysisFluidTotal));//腹透液总量
            etPrescriptionItem2.setText(String.valueOf(mParameterEniity.perCyclePerfusionVolume));//每周期灌注量
            etPrescriptionItem3.setText(String.valueOf(mParameterEniity.cycle));//循环治疗周期数
            etPrescriptionItem4.setText(String.valueOf(mParameterEniity.abdomenRetainingTime));//留腹时间
            etPrescriptionItem5.setText(String.valueOf(mParameterEniity.abdomenRetainingVolumeFinally));//末次留腹量
            etPrescriptionItem6.setText(String.valueOf(mParameterEniity.abdomenRetainingVolumeLastTime));//上次留腹量
            etPrescriptionItem7.setText(String.valueOf(mParameterEniity.firstPerfusionVolume));//首次灌注量
            etPrescriptionItem8.setText(String.valueOf(mParameterEniity.ultrafiltrationVolume));//预估超滤量
            etPrescriptionItem9.setText(String.valueOf(mParameterEniity.treatTime));
            finalSupplyCheck.setChecked(mParameterEniity.isFinalSupply);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause","onPause");
    }

    /**
     * 设置治疗周期数
     */
    private void setPeriodicities() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  (mParameterEniity.peritonealDialysisFluidTotal
                - mParameterEniity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.mParameterEniity.abdomenRetainingVolumeLastTime
                - mParameterEniity.abdomenRetainingVolumeFinally - 500 ) / mParameterEniity.perCyclePerfusionVolume;
        if(cycle <= 0){
            cycle = 1;
        }
        mParameterEniity.cycle = cycle;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
//        if(mParameterEniity.abdomenRetainingVolumeFinally>0){//0周期
//            mParameterEniity.cycle =  mParameterEniity.cycle + 1;
//        }
//        if (mParameterEniity.cycle > 13) {
//            toastMessage("周期不能大于13");
//        }
        etPrescriptionItem3.setText(String.valueOf(mParameterEniity.cycle));
        setTreatTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(mCompositeSubscription!=null){
//            mCompositeSubscription.clear();
//        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    private CommonDialog dialog;
    private int predictUlt,agoRetention;
//    public CompositeSubscription mCompositeSubscription;
    private void getRemotePd() {
        APIServiceManage.getInstance().postApdCode("Z2000");
        RetrofitUtil.getService().getRemotePrescription(PdproHelper.getInstance().getMachineSN()).enqueue(new Callback<MyResponse<HisPrescriptionBean>>() {
            @Override
            public void onResponse(Call<MyResponse<HisPrescriptionBean>> call, Response<MyResponse<HisPrescriptionBean>> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().getData().getCode().equals("10000")) {
//                            APIServiceManage.getInstance().postApdCode("Z2001");
                            if (response.body().getData().getRcpInfo() != null) {
                                if (response.body().data.getRcpInfo().getPredictUlt().equals("")) {
                                    predictUlt = 0;
                                } else {
                                    predictUlt = (int) response.body().data.getRcpInfo().getPredictUlt();
                                }
                                if (response.body().data.getRcpInfo().getAgoRetention().equals("")) {
                                    agoRetention = 0;
                                } else {
                                    agoRetention = (int) response.body().data.getRcpInfo().getAgoRetention();
                                }
                                APIServiceManage.getInstance().postApdCode("Z2021");
                                CacheUtils.getInstance().getACache().put(PdGoConstConfig.UID, response.body().getData().getRcpInfo().getPatientId()+"");
                                if (mParameterEniity.peritonealDialysisFluidTotal == response.body().data.getRcpInfo().getIcodextrinTotal()
                                        &&
                                        mParameterEniity.perCyclePerfusionVolume == response.body().data.getRcpInfo().getInFlowCycle()
                                        &&
                                mParameterEniity.cycle == response.body().data.getRcpInfo().getCycle() &&
                                mParameterEniity.firstPerfusionVolume == response.body().data.getRcpInfo().getFirstInFlow()&&
                                mParameterEniity.abdomenRetainingTime == response.body().data.getRcpInfo().getRetentionTime()&&
                                mParameterEniity.abdomenRetainingVolumeFinally == response.body().data.getRcpInfo().getLastRetention()&&
                                mParameterEniity.ultrafiltrationVolume == predictUlt&&
                                mParameterEniity.abdomenRetainingVolumeLastTime == agoRetention
                                ){

                                } else {
                                    dialog = new CommonDialog(PrescriptionActivity.this);
                                    dialog.setContent("您有一份远程处方待确认")
                                            .setBtnFirst("确定")
                                            .setBtnTwo("取消")
                                            .setFirstClickListener(new CommonDialog.OnCommonClickListener() {
                                                @Override
                                                public void onClick(CommonDialog mCommonDialog) {
//                                                    TreatmentParameterEniity eniity = PdproHelper.getInstance().getTreatmentParameter();
//                                                    eniity.peritonealDialysisFluidTotal = response.body().data.getRcpInfo().getIcodextrinTotal();
//                                                    eniity.perCyclePerfusionVolume = response.body().data.getRcpInfo().getInFlowCycle();
//                                                    eniity.cycle = response.body().data.getRcpInfo().getCycle();
//                                                    eniity.firstPerfusionVolume = response.body().data.getRcpInfo().getFirstInFlow();
//                                                    eniity.abdomenRetainingTime = response.body().data.getRcpInfo().getRetentionTime();
//                                                    eniity.abdomenRetainingVolumeFinally = response.body().data.getRcpInfo().getLastRetention();
//                                                    eniity.ultrafiltrationVolume = response.body().data.getRcpInfo().getPredictUlt();
//                                                    eniity.abdomenRetainingVolumeLastTime = response.body().data.getRcpInfo().getAgoRetention();
//                                                    eniity.treatTime = response.body().data.getRcpInfo().getTreatTime();
//                                                    eniity.isFinalSupply = false;
//                                                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, eniity);
                                                Intent intent = new Intent(PrescriptionActivity.this, RemotePrescribingActivity.class);
                                                intent.putExtra("icodextrinTotal", response.body().data.getRcpInfo().getIcodextrinTotal());
                                                intent.putExtra("inFlowCycle",response.body().data.getRcpInfo().getInFlowCycle());
                                                intent.putExtra("cycle",response.body().data.getRcpInfo().getCycle());
                                                intent.putExtra("firstInFlow",response.body().data.getRcpInfo().getFirstInFlow());
                                                intent.putExtra("retentionTime",response.body().data.getRcpInfo().getRetentionTime());
                                                intent.putExtra("lastRetention",response.body().data.getRcpInfo().getLastRetention());
                                                intent.putExtra("agoRetention",agoRetention);
                                                intent.putExtra("predictUlt",predictUlt);
                                                intent.putExtra("treatTime",response.body().data.getRcpInfo().getTreatTime());
                                                startActivity(intent);
                                                finish();
//                                                    doGoTOActivity(RemotePrescribingActivity.class);
                                                    mCommonDialog.dismiss();
                                                }
                                            })
                                            .setTwoClickListener(Dialog::dismiss);
                                    if (!PrescriptionActivity.this.isFinishing()) {
                                        dialog.show();
                                        MyApplication.isRemoteShow = true;
                                    }
                                }
                            }
                        } else {
                            APIServiceManage.getInstance().postApdCode("Z2020");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<HisPrescriptionBean>> call, Throwable t) {

            }
        });
    }



    @Override
    public void notifyByThemeChanged() {

    }
}