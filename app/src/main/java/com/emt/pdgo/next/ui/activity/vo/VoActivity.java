package com.emt.pdgo.next.ui.activity.vo;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.KidBean;
import com.emt.pdgo.next.data.bean.PdData;
import com.emt.pdgo.next.data.bean.RBean;
import com.emt.pdgo.next.data.bean.TpdBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.model.mode.CcpdBean;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.upload.TreatmentDataUploadBean;
import com.emt.pdgo.next.net.bean.upload.TreatmentPrescriptionUploadBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.TreatmentFeedbackActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.NetworkUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoActivity extends BaseActivity {

    @BindView(R.id.totalDrainTv)
    TextView totalDrainTv;
    @BindView(R.id.totalPerTv)
    TextView totalPerTv;
    @BindView(R.id.totalUltTv)
    TextView totalUltTv;
    @BindView(R.id.totalPdTv)
    TextView totalPdTv;
    @BindView(R.id.zeroDrainTv)
    TextView zeroDrainTv;
    @BindView(R.id.adbTimeTv)
    TextView adbTimeTv;
    @BindView(R.id.rxCycleTv)
    TextView rxCycleTv;
    @BindView(R.id.rlPdTv)
    TextView rlPdTv;

    @BindView(R.id.tipTv)
    TextView tipTv;

    @BindView(R.id.assessLayout)
    LinearLayout assessLayout;
    @BindView(R.id.powerOffLayout)
    LinearLayout powerOffLayout;

    @BindView(R.id.textClock)
    TextClock textClock;
    @BindView(R.id.powerIv)
    ImageView powerIv;
    @BindView(R.id.powerTv)
    TextView powerTv;
    @BindView(R.id.netTv)
    TextView netTv;
    @BindView(R.id.wifiIv)
    ImageView wifiIv;

    @BindView(R.id.downLayout)
    LinearLayout downLayout;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_vo);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void registerEvents() {
        assessLayout.setOnClickListener(view -> {
            Intent intent = new Intent(this, TreatmentFeedbackActivity.class);
            intent.putExtra("msg","0");
            startActivity(intent);
        });
        toEngAc(textClock);
        powerOffLayout.setOnClickListener(view -> {
            Intent intent = new Intent(this, TreatmentFeedbackActivity.class);
            intent.putExtra("msg","1");
            startActivity(intent);
        });
        downLayout.setOnClickListener(view -> {
            Intent intent = new Intent(this, TreatmentFeedbackActivity.class);
            intent.putExtra("msg","2");
            startActivity(intent);
        });
        netTv.setTextColor(NetworkUtils.getNetWorkState(this) == 0 ? Color.WHITE : Color.RED);
        wifiIv.setVisibility(NetworkUtils.getNetWorkState(this) == 1 ?View.VISIBLE:View.INVISIBLE);
    }

    private PdData pdData;
    private int cycle;
    @Override
    public void initViewData() {
        try {
            pdData = MyApplication.pdData;
            totalDrainTv.setText(String.valueOf(pdData.getTotalDarinVol()));
            totalPerTv.setText(String.valueOf(pdData.getTotalPerVol()));
            totalUltTv.setText(String.valueOf(pdData.getUltVol()));
            totalPdTv.setText(pdData.getDurationTime());
            rxCycleTv.setText(String.valueOf(pdData.getRxCycle()));
            if (pdData.getPdEntityDataList() != null) {
                if (pdData.getPdEntityDataList().size() != 0) {
                    if (pdData.getPdEntityDataList().size() > 1) {
                        int time = (pdData.getPdEntityDataList().size() - 1) * 60;
                        adbTimeTv.setText(String.valueOf(pdData.getTotalAbdTime() / time));
                    }
                    zeroDrainTv.setText(String.valueOf(pdData.getPdEntityDataList().get(0).getDrainage()));
                    rlPdTv.setText(String.valueOf(pdData.getPdEntityDataList().size() - 1));
                    cycle = pdData.getPdEntityDataList().size() - 1;
                }
            }
            speak("请将下列资料记录于日常 APD 治疗记录本上");
            tipTv.setVisibility(NetworkUtils.getNetWorkState(VoActivity.this) == -1 ? View.VISIBLE : View.GONE);
            postData();
            if (cycle < 10) {
                APIServiceManage.getInstance().postApdCode("T0" + cycle + "09");
            } else {
                APIServiceManage.getInstance().postApdCode("T" + cycle + "09");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPost;
    private int status;
    @Subscribe(code = RxBusCodeConfig.NET_STATUS)
    public void receiveNetStatus(String net) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status = Integer.parseInt(net);
                if (status != -1) {
                    if (!isPost) {
                        postData();
                    }
                    if (status == 1) {
                        wifiIv.setVisibility(View.VISIBLE);
                    } else if (status == 0) {
                        netTv.setTextColor(Color.WHITE);
                    }
                    tipTv.setVisibility(View.GONE);
                } else {
                    if (!isPost) {
                        tipTv.setVisibility(View.VISIBLE);
                    } else {
                        tipTv.setVisibility(View.GONE);
                    }
                    netTv.setTextColor(Color.RED);
                    wifiIv.setVisibility(View.INVISIBLE);
                }
                Log.e("net","voNet:"+status);
            }
        });
    }

    private void postData() {
        try {
            getTreatmentDataUpload();
            addAPD();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private TreatmentDataUploadBean mTreatmentDataUpload;
    private void getTreatmentDataUpload() {
        try {
            mTreatmentDataUpload = new TreatmentDataUploadBean();
            TreatmentPrescriptionUploadBean mTreatmentPrescription = new TreatmentPrescriptionUploadBean();
            mTreatmentPrescription.phone = MyApplication.phone;
            mTreatmentPrescription.recommendedWeight = 0;
            mTreatmentPrescription.weightBeforeTreatment = 0;
            mTreatmentPrescription.SBPBeforeTreatment = 0;
            mTreatmentPrescription.DBPBeforeTreatment = 0;
            mTreatmentPrescription.weightAfterTreatment = 0;
            mTreatmentPrescription.SBPAfterTreatment = 0;
            mTreatmentPrescription.DBPAfterTreatment = 0;
            mTreatmentPrescription.fastingBloodGlucose = 0f;
            mTreatmentPrescription.urineOutput = 0;
            mTreatmentPrescription.waterIntake = 0;

            mTreatmentPrescription.totalPerfusionVolume = pdData.getTotalPerVol();
//        mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume;
            mTreatmentPrescription.totalUltrafiltrationVolume = pdData.getUltVol();
            mTreatmentPrescription.peritonealDialysisFluidTotal = pdData.getTotalDarinVol();
            mTreatmentPrescription.periodicities = pdData.getRxCycle();
            switch (MyApplication.apdMode) {
                case 2:
                    TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                    mTreatmentPrescription.firstPerfusionVolume = tpdBean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = tpdBean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = tpdBean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = tpdBean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = tpdBean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = tpdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = tpdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = tpdBean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = cycle;
                    mTreatmentDataUpload.totalInjectAmount = tpdBean.peritonealDialysisFluidTotal;
                    break;
                case 3:
                    CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                    mTreatmentPrescription.firstPerfusionVolume = ccpdBean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = ccpdBean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = ccpdBean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = ccpdBean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = ccpdBean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = ccpdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = ccpdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = ccpdBean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = cycle;
                    mTreatmentDataUpload.totalInjectAmount = ccpdBean.peritonealDialysisFluidTotal;
                    break;
//            case 4:
//                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
//                mTreatmentPrescription.firstPerfusionVolume =  aapdBean.firstPerfusionVolume;
//                mTreatmentPrescription.perCyclePerfusionVolume = aapdBean.perCyclePerfusionVolume;
//                mTreatmentPrescription.abdomenRetainingVolumeFinally = aapdBean.abdomenRetainingVolumeFinally;
//                mTreatmentPrescription.abdomenRetainingTime = aapdBean.abdomenRetainingTime;
//                mTreatmentPrescription.abdomenRetainingVolumeLastTime = ccpdBean.abdomenRetainingVolumeLastTime;
//                mTreatmentPrescription.ultrafiltrationVolume = ccpdBean.ultrafiltrationVolume;
//                mTreatmentDataUpload.ultrafiltration = ccpdBean.ultrafiltrationVolume;
//                mTreatmentDataUpload.lastLeave = ccpdBean.abdomenRetainingVolumeFinally;
//                mTreatmentDataUpload.times = ccpdBean.cycle;
//                mTreatmentDataUpload.totalInjectAmount = ccpdBean.peritonealDialysisFluidTotal;
//                break;
                case 7:
                    KidBean kidBean = PdproHelper.getInstance().kidBean();
                    mTreatmentPrescription.firstPerfusionVolume = kidBean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = kidBean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = kidBean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = kidBean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = kidBean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = kidBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = kidBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = kidBean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = cycle;
                    mTreatmentDataUpload.totalInjectAmount = kidBean.peritonealDialysisFluidTotal;
                    break;
//            case 8:
//                break;
                default:
                    IpdBean bean = PdproHelper.getInstance().ipdBean();
//                TreatmentParameterEniity entity = PdproHelper.getInstance().getTreatmentParameter();
                    mTreatmentPrescription.firstPerfusionVolume = bean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = bean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = bean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = bean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = bean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = bean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = bean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = bean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = cycle;
                    mTreatmentDataUpload.totalInjectAmount = bean.peritonealDialysisFluidTotal;
                    break;
            }
//            mTreatmentDataUpload.lastLeaveTime = pdEntityDataList.get(currCycle).getPreTime();
            mTreatmentPrescription.equipmentUseTime = 0;
            mTreatmentPrescription.upWeightInitialValue = 0;
            mTreatmentPrescription.lowWeightInitialValue = 0;

            mTreatmentPrescription.plcId = "";//
            mTreatmentPrescription.buildId = 0;
            mTreatmentPrescription.buildValue = "";//1.2.1.1

            mTreatmentDataUpload.machineSN = PdproHelper.getInstance().getMachineSN();
            mTreatmentDataUpload.startTime = pdData.getStartTime();
            mTreatmentDataUpload.endTime = pdData.getEndTime();

            StringBuilder cycles = new StringBuilder();
            StringBuilder inFlows= new StringBuilder();
            StringBuilder inFlowTime=new StringBuilder();
            StringBuilder leaveWombTime=new StringBuilder();
            StringBuilder exhaustTime=new StringBuilder();
            StringBuilder drainages=new StringBuilder();

            StringBuilder auxiliaryFlushingVolume=new StringBuilder();
            StringBuilder abdominalVolumeAfterInflow = new StringBuilder();
            StringBuilder drainageTargetValue = new StringBuilder();
            StringBuilder estimatedResidualAbdominalFluid = new StringBuilder();
            if (pdData.getPdEntityDataList() != null) {
                for (int i = 0; i < pdData.getPdEntityDataList().size(); i++) {
                    if (i == 0) {
                        auxiliaryFlushingVolume.append(pdData.getPdEntityDataList().get(i).getUfVol());
                        drainages.append(pdData.getPdEntityDataList().get(i).getDrainage());
                        mTreatmentDataUpload.drainageTime = pdData.getPdEntityDataList().get(i).getDrainTime() / 60;
                    } else if (i == 1) {
                        cycles.append(pdData.getPdEntityDataList().get(i).getCycle());
                        inFlows.append(pdData.getPdEntityDataList().get(i).getPreVol());
                        inFlowTime.append(pdData.getPdEntityDataList().get(i).getPreTime() / 60);
                        leaveWombTime.append(pdData.getPdEntityDataList().get(i).getAbdTime() / 60);
                        exhaustTime.append(pdData.getPdEntityDataList().get(i).getDrainTime() / 60);
                        drainages.append(",").append(pdData.getPdEntityDataList().get(i).getDrainage());
                        auxiliaryFlushingVolume.append(",").append(pdData.getPdEntityDataList().get(i).getUfVol());
                        abdominalVolumeAfterInflow.append(pdData.getPdEntityDataList().get(i).getAbdVol());
                        drainageTargetValue.append(pdData.getPdEntityDataList().get(i).getDrainTargetVol());
                        estimatedResidualAbdominalFluid.append(pdData.getPdEntityDataList().get(i).getEstimate());
                    } else {
                        cycles.append(",").append(pdData.getPdEntityDataList().get(i).getCycle());
                        inFlows.append(",").append(pdData.getPdEntityDataList().get(i).getPreVol());
                        inFlowTime.append(",").append(pdData.getPdEntityDataList().get(i).getPreTime() / 60);
                        leaveWombTime.append(",").append(pdData.getPdEntityDataList().get(i).getAbdTime() / 60);
                        exhaustTime.append(",").append(pdData.getPdEntityDataList().get(i).getDrainTime() / 60);
                        drainages.append(",").append(pdData.getPdEntityDataList().get(i).getDrainage());
                        auxiliaryFlushingVolume.append(",").append(pdData.getPdEntityDataList().get(i).getUfVol());
                        abdominalVolumeAfterInflow.append(",").append(pdData.getPdEntityDataList().get(i).getAbdVol());
                        drainageTargetValue.append(",").append(pdData.getPdEntityDataList().get(i).getDrainTargetVol());
                        estimatedResidualAbdominalFluid.append(",").append(pdData.getPdEntityDataList().get(i).getEstimate());
                    }
                }
            }
            mTreatmentDataUpload.cycle = String.valueOf(cycles);
            mTreatmentDataUpload.inFlow = String.valueOf(inFlows);
            mTreatmentDataUpload.inFlowTime = String.valueOf(inFlowTime);
            mTreatmentDataUpload.leaveWombTime = String.valueOf(leaveWombTime);
            mTreatmentDataUpload.exhaustTime = String.valueOf(exhaustTime);
            mTreatmentDataUpload.drainage = String.valueOf(drainages);
            mTreatmentDataUpload.auxiliaryFlushingVolume = auxiliaryFlushingVolume.toString();
            mTreatmentDataUpload.abdominalVolumeAfterInflow = String.valueOf(abdominalVolumeAfterInflow);
            mTreatmentDataUpload.drainageTargetValue = String.valueOf(drainageTargetValue);
            mTreatmentDataUpload.estimatedResidualAbdominalFluid = String.valueOf(estimatedResidualAbdominalFluid);
            mTreatmentDataUpload.lastLeaveTime = pdData.getFinalTime();
            mTreatmentDataUpload.treatmentPrescriptionUploadVo = mTreatmentPrescription;
        } catch (Exception e) {
            Log.e("数据上传", e.getLocalizedMessage());
        }
    }

    private void addAPD() {
        Gson gson = new Gson();
        String content = gson.toJson(mTreatmentDataUpload);
        Log.e("治疗界面","上传数据--"+content);
        RequestBody params = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                content);
        RetrofitUtil.getService().postAPD(params).enqueue(new Callback<MyResponse<RBean>>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse<RBean>> call, @NonNull Response<MyResponse<RBean>> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().data.code.equals("10000")) {
                            Log.e("治疗界面", "治疗数据上传成功");
                        } else {
                            Log.e("治疗界面", "治疗数据上传"+response.body().data.msg);
                            saveFaultCodeLocal("治疗数据上传,"+response.body().data.msg);
                        }
                        isPost = true;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse<RBean>> call, @NonNull Throwable t) {
                saveFaultCodeLocal("治疗数据上传,"+t.getMessage());
            }
        });
    }

    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {
            if (mReceiveDeviceBean != null) {
                powerTv.setText(String.valueOf(mReceiveDeviceBean.batteryLevel + "%"));
                tempTv.setText(getTemp(mReceiveDeviceBean.temp) + "℃");
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
            }
        });
    }
    @BindView(R.id.tempTv)
    TextView tempTv;
    private final BigDecimal mTen = new BigDecimal(10);

    private float getTemp(int temp) {
//        temp = temp-5;
        BigDecimal mTemp = new BigDecimal(temp);
        BigDecimal newTemp = mTemp.divide(mTen, 1, RoundingMode.HALF_UP);
        return newTemp.floatValue();
    }

    private CompositeDisposable compositeDisposable;
    private void init() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
//                    tipTv.setVisibility(NetworkUtils.getNetWorkState(VoActivity.this)==-1?View.VISIBLE:View.GONE);
                    sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.interval(0, 6 * 1000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposableObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

    @Override
    public void notifyByThemeChanged() {

    }
}