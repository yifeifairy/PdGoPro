package com.emt.pdgo.next.ui.activity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.RBean;
import com.emt.pdgo.next.data.bean.ReplayEntity;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.HisTreatBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.upload.TreatmentDataUploadBean;
import com.emt.pdgo.next.net.bean.upload.TreatmentPrescriptionUploadBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.adapter.HisPdAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricalTreatmentActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private List<HisTreatBean.RowsDTO.InfoDTO> hisTreatBeans;
    private List<HisTreatBean.RowsDTO.InfoDTO> infoDTOS;
    private HisPdAdapter hisPdAdapter;

    @BindView(R.id.tv_total_drain_volume)
    TextView tv_total_drain_volume;
    @BindView(R.id.tv_total_perfusion_volume)
    TextView tv_total_perfusion_volume;
    @BindView(R.id.tv_curr_ultrafiltration_volume)
    TextView tv_curr_ultrafiltration_volume;

    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;

    @BindView(R.id.btnUpload)
    StateButton btnUpload;

    @Override
    public void notifyByThemeChanged() {

    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_historical_treatment);
        ButterKnife.bind(this);
        initHeadTitleBar("历史治疗数据");
    }
    @BindView(R.id.ivPrePage)
    ImageView ivPrePage;
    @BindView(R.id.ivNextPage)
    ImageView ivNextPage;
    private boolean haveNet;

//    @BindView(R.id.btn_back)
//    StateButton btnBack;
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
//        btnBack.setOnClickListener(v -> {
//            doGoCloseTOActivity(TreatmentModeActivity.class,"");
//        });
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
        btnUpload.setOnClickListener(v -> {
            if (entity.endTime.equals("1999-12-31 23:59:59")) {
                toastMessage("请先完成治疗");
            } else {
                replyUpload();
            }
        });
        ivPrePage.setOnClickListener(v -> {
            if (haveNet) {
                if (page > 1) {
                    page -- ;
                    getPd(page);
                }
            }
//            else {
//                if (page > 1) {
//                    page -- ;
//                    initLocalRecyclerview(page);
//                }
//            }
        });
        ivNextPage.setOnClickListener(v -> {
            if (haveNet) {
                if (page * limit < total) {
                    page++;
                    Log.e("历史治疗界面", "page--" + page);
                    getPd(page);
                }
            }
//            else {
//                page ++;
//                initLocalRecyclerview(page);
////                getAllTreatmentHistory();
//            }
        });
    }

    @Override
    public void initViewData() {
        if (checkConnectNetwork(this)) {
            haveNet = true;
            initRecyclerview();
            getPd(page);
        } else {
            haveNet = false;
//            initLocalRecyclerview(page);
        }
//        initRecyclerview();
    }


    private TreatmentParameterEniity mTreatmentParameterEniity;//治疗参数
    private TreatmentDataUploadBean treatmentDataUploadBean;

    ReplayEntity entity = PdproHelper.getInstance().replayEntity();
    private Gson gson = new Gson();
    private void replyUpload() {
        mTreatmentParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
        treatmentDataUploadBean = new TreatmentDataUploadBean();
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

        mTreatmentPrescription.totalPerfusionVolume = entity.totalPerfusionVolume;
        mTreatmentPrescription.totalUltrafiltrationVolume = entity.totalUltrafiltrationVolume;
        mTreatmentPrescription.peritonealDialysisFluidTotal = mTreatmentParameterEniity.peritonealDialysisFluidTotal;
        mTreatmentPrescription.periodicities = 0;
        mTreatmentPrescription.firstPerfusionVolume = 0;
        mTreatmentPrescription.perCyclePerfusionVolume = 0;
        mTreatmentPrescription.abdomenRetainingVolumeFinally = 0;
        mTreatmentPrescription.abdomenRetainingTime = 0;
        mTreatmentPrescription.abdomenRetainingVolumeLastTime = 0;
        mTreatmentPrescription.ultrafiltrationVolume = 0;

        mTreatmentPrescription.equipmentUseTime = 0;
        mTreatmentPrescription.upWeightInitialValue = 0;
        mTreatmentPrescription.lowWeightInitialValue = 0;

        mTreatmentPrescription.plcId = "";//
        mTreatmentPrescription.buildId = 0;
        mTreatmentPrescription.buildValue = "";//1.2.1.1

        treatmentDataUploadBean.machineSN = PdproHelper.getInstance().getMachineSN();
        treatmentDataUploadBean.totalInjectAmount = getmParameterEniity().peritonealDialysisFluidTotal;

        treatmentDataUploadBean.startTime = entity.startTime;
        treatmentDataUploadBean.endTime = entity.endTime;
        treatmentDataUploadBean.drainageTime = entity.drainageTime;
        treatmentDataUploadBean.ultrafiltration = mTreatmentParameterEniity.ultrafiltrationVolume;
        treatmentDataUploadBean.lastLeave = mTreatmentParameterEniity.abdomenRetainingVolumeLastTime;
        treatmentDataUploadBean.lastLeaveTime = mTreatmentParameterEniity.abdomenRetainingTime;
        treatmentDataUploadBean.times = entity.maxCycle;
        treatmentDataUploadBean.cycle = entity.cycle;
        treatmentDataUploadBean.inFlow = entity.inFlow;
        treatmentDataUploadBean.inFlowTime = entity.inFlowTime;
        treatmentDataUploadBean.leaveWombTime = entity.leaveWombTime;
        treatmentDataUploadBean.exhaustTime = entity.exhaustTime;
        treatmentDataUploadBean.drainage = entity.drainage;
        treatmentDataUploadBean.auxiliaryFlushingVolume = entity.auxiliaryFlushingVolume;
        treatmentDataUploadBean.abdominalVolumeAfterInflow = entity.abdominalVolumeAfterInflow;
        treatmentDataUploadBean.drainageTargetValue = entity.drainageTargetValue;
        treatmentDataUploadBean.estimatedResidualAbdominalFluid = entity.estimatedResidualAbdominalFluid;
        treatmentDataUploadBean.treatmentPrescriptionUploadVo = mTreatmentPrescription;
        uploadTreatBean();
    }

    private void uploadTreatBean() {
        String content = gson.toJson(treatmentDataUploadBean);

        RequestBody params = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                content);
        RetrofitUtil.getService().postAPD(params).enqueue(new Callback<MyResponse<RBean>>() {
            @Override
            public void onResponse(Call<MyResponse<RBean>> call, Response<MyResponse<RBean>> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().data.code.equals("10000")) {
                            Log.e("治疗界面", "治疗数据上传成功");
                            page = 1;
                            toastMessage("数据上传成功");
                            getPd(page);
                        } else {
                            Log.e("治疗界面", "治疗数据上传"+response.body().data.msg);
                            saveFaultCodeLocal("治疗数据上传,"+response.body().data.msg);
                            toastMessage(response.body().data.msg);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<RBean>> call, Throwable t) {
                Log.e("历史治疗数据", t.getMessage() + "--url--"+call.request().url());
                toastMessage("上传失败");
                saveFaultCodeLocal("治疗数据上传,"+t.getMessage());
            }
        });

    }

    private TreatmentParameterEniity mParameterEniity;
    private TreatmentParameterEniity getmParameterEniity() {
        if (mParameterEniity == null) {
            mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
        }
        return mParameterEniity;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerview() {

        if (hisPdAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            hisPdAdapter = new HisPdAdapter(hisTreatBeans);
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(hisPdAdapter);
        } else {
            hisPdAdapter.notifyDataSetChanged();
        }
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
//            //添加Android自带的分割线
//            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    private int page = 1;
    private int limit = 10;
    private int total;

    private void getPd(int page) {
        hisTreatBeans = new ArrayList<>();
        RetrofitUtil.getService().getApdRecordData(PdproHelper.getInstance().getMachineSN(), 1, limit).enqueue(new Callback<MyResponse<HisTreatBean>>() {
            @Override
            public void onResponse(Call<MyResponse<HisTreatBean>> call, Response<MyResponse<HisTreatBean>> response) {
                if (response.body() != null) {
                    if (response.body().data != null) {
                        if (response.body().getData().getRows() != null && response.body().getData().getRows().size() != 0) {
                            total = response.body().getData().getTotal();
                            tvStartTime.setText(response.body().getData().getRows().get(page-1).getStartTime());
                            tvEndTime.setText(response.body().getData().getRows().get(page-1).getEndTime());
                            hisTreatBeans.clear();
                            infoDTOS = response.body().getData().getRows().get(page-1).getInfo();
                            tv_total_drain_volume.setText(response.body().getData().getRows().get(page-1).getDrainage());
                            tv_total_perfusion_volume.setText(response.body().getData().getRows().get(page-1).getInFlow());
                            tv_curr_ultrafiltration_volume.setText(response.body().getData().getRows().get(page-1).getUltrafiltration());
                            hisTreatBeans.addAll(infoDTOS);
                            hisPdAdapter.setNewData(infoDTOS);
//                            }
                            Log.e("历史治疗数据","total--"+response.body().getData().getTotal());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<HisTreatBean>> call, Throwable t) {
                Log.e("历史治疗数据",""+t.getMessage());
                saveFaultCodeLocal("历史治疗数据,"+t.getMessage());
            }
        });
    }


}