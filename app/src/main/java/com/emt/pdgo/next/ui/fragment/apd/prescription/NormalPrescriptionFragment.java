package com.emt.pdgo.next.ui.fragment.apd.prescription;

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
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentPrescriptBean;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.activity.apd.ApdPrescriptionActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NormalPrescriptionFragment extends BaseFragment {

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

    private ApdPrescriptionActivity activity;
    
    private CompositeDisposable compositeDisposable;

    private TreatmentFragmentActivity fragmentActivity;
    private IpdBean entity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (MyApplication.apdTreat) {
            this.fragmentActivity =  (TreatmentFragmentActivity) activity;

        } else {
            this.activity =  (ApdPrescriptionActivity) activity;
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_normal_prescription, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        compositeDisposable = new CompositeDisposable();
//        entity.cycle = 6;
//        entity.perCyclePerfusionVolume = 200;
//        entity.abdomenRetainingTime = 60;
//        entity.abdomenRetainingVolumeFinally = 0;
        entity = MyApplication.apdTreat ? fragmentActivity.entity :PdproHelper.getInstance().ipdBean();
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

    private Gson gson = new Gson();
    private void addUserSnMachineRcp() {
//        Map<String, Object> paramsMap = new HashMap<>();
//
////        paramsMap.put("username", "18582556015");
////        paramsMap.put("password", "123456");
//
//        paramsMap.put("machineSn",PdproHelper.getInstance().getMachineSN());
//        paramsMap.put("icodextrinTotal",entity.peritonealDialysisFluidTotal);
//        paramsMap.put("inFlowCycle",entity.perCyclePerfusionVolume);
//        paramsMap.put("cycle",entity.cycle);
//        paramsMap.put("retentionTime",entity.abdomenRetainingTime);
//        paramsMap.put("lastRetention",entity.abdomenRetainingVolumeFinally);
//        paramsMap.put("treatTime",entity.treatTime);
//        paramsMap.put("agoRetention",entity.abdomenRetainingVolumeLastTime);
//        paramsMap.put("predictUlt",entity.ultrafiltrationVolume);
//        paramsMap.put("firstInFlow",entity.firstPerfusionVolume);
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
//                            activity.saveFaultCodeLocal("添加处方,"+response.body().getData().getMsg());
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyResponse<CommonBean>> call, Throwable t) {
//                Log.e("处方设置", t.getMessage() + "--url--"+call.request().url());
//                activity.saveFaultCodeLocal("添加处方,"+t.getMessage());
//            }
//        });
    }

    // 指令分离
    private void apdpresci() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SerialTreatmentPrescriptBean prescript = new SerialTreatmentPrescriptBean();
        prescript.totalVolume = entity.peritonealDialysisFluidTotal;//处方总灌注量
        prescript.cycle = entity.cycle;//循环周期数
        prescript.firstPerfuseVolume = entity.firstPerfusionVolume;//首次灌注量
        prescript.cyclePerfuseVolume = entity.perCyclePerfusionVolume;//循环周期灌注量
        prescript.lastRetainVolume = entity.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
        prescript.finalRetainVolume = entity.abdomenRetainingVolumeFinally;//末次留腹量
        prescript.retainTime = entity.abdomenRetainingTime;//60  //留腹时间
        prescript.ultraFiltrationVolume = entity.ultrafiltrationVolume;//预估超滤量
        prescript.apdmodify = 0;
        mSerialRequestBean.method = "apdpresci/config";
        mSerialRequestBean.params = prescript;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        activity.sendToMainBoard(new Gson().toJson(mRequestBean));
    }

    private void next() {
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, entity);
        apdpresci();
        APIServiceManage.getInstance().postApdCode("Z2031");
        Disposable mainDisposable = Observable.timer(10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
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
                        hisRx.ulTreatTime = entity.treatTime;
//                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
                        EmtDataBase
                                .getInstance(activity)
                                .getRxDao()
                                .insertRx(hisRx);
                    }
                });
        compositeDisposable.add(mainDisposable);
        activity.doGoTOActivity(PreHeatActivity.class);
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