package com.emt.pdgo.next.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.data.bean.RBean;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.DelPdEntity;
import com.emt.pdgo.next.database.entity.PdEntity;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.upload.TreatmentDataUploadBean;
import com.emt.pdgo.next.net.bean.upload.TreatmentPrescriptionUploadBean;
import com.emt.pdgo.next.ui.adapter.LocalPdAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDreamActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private int page;

    private LocalPdAdapter localPdAdapter;
    private List<PdEntity> getPdInfo(int page) {
        return EmtDataBase.getInstance(MyDreamActivity.this).getPdDao()
                .getPdListById(page);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_my_dream);
        ButterKnife.bind(this);
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;

    @Override
    public void registerEvents() {
        btnSave.setVisibility(View.VISIBLE);
        btnSave.setText("清空");
        btnBack.setOnClickListener(view -> onBackPressed());
        btnSave.setOnClickListener(view -> {
            final CommonDialog dialog = new CommonDialog(this);
            dialog.setContent("确认清空？")
                    .setBtnFirst("确定")
                    .setBtnTwo("取消")
                    .setFirstClickListener(mCommonDialog -> {
                        DelPdEntity delRxEntity = new DelPdEntity();
                        delRxEntity.id = 1;
                        delRxEntity.num = getLocalRx().size() + 1;
                        Log.e("治疗","治疗删除--"+getLocalRx().size());
                        if (EmtDataBase.getInstance(MyDreamActivity.this).delPdDao().getDelPdById(1) == null) {
                            EmtDataBase
                                    .getInstance(MyDreamActivity.this)
                                    .delPdDao()
                                    .insertRx(delRxEntity);
                            Log.e("治疗","治疗删除--insert");
                        } else {
                            EmtDataBase
                                    .getInstance(MyDreamActivity.this)
                                    .delPdDao()
                                    .update(delRxEntity);
                            Log.e("治疗","治疗删除--update");
                        }
                        // 假清空
                        init(EmtDataBase.getInstance(MyDreamActivity.this).delPdDao().getDelPdById(1).num);

                        mCommonDialog.dismiss();
                    })
                    .setTwoClickListener(Dialog::dismiss)
                    .show();
        });
    }

    private int maxPage;
    private int minPage;

    @Override
    public void initViewData() {

        if (EmtDataBase.getInstance(MyDreamActivity.this).delPdDao().getDelPdById(1) == null) {
            minPage = 1;
        } else {
            minPage = EmtDataBase.getInstance(MyDreamActivity.this).delPdDao().getDelPdById(1).num;
        }
        maxPage = getLocalRx().size();
        page = maxPage;
        Log.e("myDream","min:"+minPage+",max:"+maxPage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        localPdAdapter = new LocalPdAdapter(getPdInfo(minPage == 1 ? minPage: maxPage));
        recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(localPdAdapter);
        localPdAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.ivNextPage) {
                if (page > minPage && page <= maxPage) {
                    page--;
                    Log.e("myDream","min:"+minPage+",max:"+maxPage+",page:"+page);
                    init(page);
                }
            } else if (view.getId() == R.id.ivPrePage) {
                if ( page < maxPage) {
                    page++;
                    Log.e("myDream","min:"+minPage+",max:"+maxPage+",page:"+page);
                    init(page);
                }
            } else if (view.getId() == R.id.btnUpload) {
                if (checkConnectNetwork(MyDreamActivity.this)) {
                    getToken();
                    replyUpload(EmtDataBase.getInstance(MyDreamActivity.this).getPdDao().getPdList().get(page-1));
                } else {
                    toastMessage("请连接网络");
                }
            }
        });
    }

    private List<PdEntity> getLocalRx() {
        return EmtDataBase
                .getInstance(MyDreamActivity.this)
                .getPdDao()
                .getPdList();
    }

    private TreatmentDataUploadBean treatmentDataUploadBean;
    private Gson gson = new Gson();
    private void replyUpload(PdEntity pdEntity) {
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
        treatmentDataUploadBean = new TreatmentDataUploadBean();
        TreatmentPrescriptionUploadBean mTreatmentPrescription = new TreatmentPrescriptionUploadBean();
        mTreatmentPrescription.phone = pdEntity.phone;
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

        mTreatmentPrescription.totalPerfusionVolume = pdEntity.totalPerVol;
        mTreatmentPrescription.totalUltrafiltrationVolume = pdEntity.totalUltVol;
        mTreatmentPrescription.peritonealDialysisFluidTotal = pdEntity.totalVol;
        mTreatmentPrescription.periodicities = pdEntity.cycle;
        mTreatmentPrescription.firstPerfusionVolume = pdEntity.firstPerfusionVolume;
        mTreatmentPrescription.perCyclePerfusionVolume = pdEntity.perCyclePerfusionVolume;
        mTreatmentPrescription.abdomenRetainingVolumeFinally = pdEntity.abdomenRetainingVolumeFinally;
        mTreatmentPrescription.abdomenRetainingTime = pdEntity.abdomenRetainingTime;
        mTreatmentPrescription.abdomenRetainingVolumeLastTime = pdEntity.abdomenRetainingVolumeLastTime;
        mTreatmentPrescription.ultrafiltrationVolume = pdEntity.ultrafiltrationVolume;

        mTreatmentPrescription.equipmentUseTime = 0;
        mTreatmentPrescription.upWeightInitialValue = 0;
        mTreatmentPrescription.lowWeightInitialValue = 0;

        mTreatmentPrescription.plcId = "";//
        mTreatmentPrescription.buildId = 0;
        mTreatmentPrescription.buildValue = "";//1.2.1.1

        treatmentDataUploadBean.machineSN = PdproHelper.getInstance().getMachineSN();
        treatmentDataUploadBean.totalInjectAmount = pdEntity.totalVol;

        treatmentDataUploadBean.startTime = pdEntity.startTime;
        treatmentDataUploadBean.endTime = pdEntity.endTime;
        treatmentDataUploadBean.ultrafiltration = pdEntity.totalUltVol;
        treatmentDataUploadBean.lastLeave = pdEntity.finalAbdTime;
        treatmentDataUploadBean.times = pdEntity.cycle;
        treatmentDataUploadBean.lastLeaveTime = pdEntity.finalTime;
        for (int i = 0; i < pdEntity.pdInfoEntities.size(); i++) {
            PdEntity.PdInfoEntity pdInfoEntity = pdEntity.pdInfoEntities.get(i);
            if (i == 0) {
                drainages.append(pdInfoEntity.drainage);
                auxiliaryFlushingVolume.append(pdInfoEntity.auFvVol);
                treatmentDataUploadBean.drainageTime = pdInfoEntity.drainTime;
            } else if (i == 1) {
                cycles.append(pdInfoEntity.cycle);
                inFlows.append(pdInfoEntity.preVol);
                inFlowTime.append(pdInfoEntity.preTime);
                leaveWombTime.append(pdInfoEntity.abdTime);
                exhaustTime.append(pdInfoEntity.drainTime);
                abdominalVolumeAfterInflow.append(pdInfoEntity.abdAfterVol);
                drainageTargetValue.append(pdInfoEntity.drainTvVol);
                estimatedResidualAbdominalFluid.append(pdInfoEntity.remain);
                drainages.append(",").append(pdInfoEntity.drainage);
                auxiliaryFlushingVolume.append(",").append(pdInfoEntity.auFvVol);
            } else {
                cycles.append(",").append(pdInfoEntity.cycle);
                inFlows.append(",").append(pdInfoEntity.preVol);
                inFlowTime.append(",").append(pdInfoEntity.preTime);
                leaveWombTime.append(",").append(pdInfoEntity.abdTime);
                exhaustTime.append(",").append(pdInfoEntity.drainTime);
                drainages.append(",").append(pdInfoEntity.drainage);
                auxiliaryFlushingVolume.append(",").append(pdInfoEntity.auFvVol);
                abdominalVolumeAfterInflow.append(",").append(pdInfoEntity.abdAfterVol);
                drainageTargetValue.append(",").append(pdInfoEntity.drainTvVol);
                estimatedResidualAbdominalFluid.append(",").append(pdInfoEntity.remain);
            }
        }
        treatmentDataUploadBean.cycle = String.valueOf(cycles);
        treatmentDataUploadBean.inFlow = String.valueOf(inFlows);
        treatmentDataUploadBean.inFlowTime = String.valueOf(inFlowTime);
        treatmentDataUploadBean.leaveWombTime = String.valueOf(leaveWombTime);
        treatmentDataUploadBean.exhaustTime = String.valueOf(exhaustTime);
        treatmentDataUploadBean.drainage = String.valueOf(drainages);
        treatmentDataUploadBean.auxiliaryFlushingVolume = auxiliaryFlushingVolume.toString();
        treatmentDataUploadBean.abdominalVolumeAfterInflow = String.valueOf(abdominalVolumeAfterInflow);
        treatmentDataUploadBean.drainageTargetValue = String.valueOf(drainageTargetValue);
        treatmentDataUploadBean.estimatedResidualAbdominalFluid = String.valueOf(estimatedResidualAbdominalFluid);
        treatmentDataUploadBean.treatmentPrescriptionUploadVo = mTreatmentPrescription;
        uploadTreatBean();
    }

    private void uploadTreatBean() {
        showLoading("上传中....");
        String content = gson.toJson(treatmentDataUploadBean);
        Log.e("治疗数据上传","json:"+content);
        RequestBody params = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                content);
        RetrofitUtil.getService().postAPD(params).enqueue(new Callback<MyResponse<RBean>>() {
            @Override
            public void onResponse(Call<MyResponse<RBean>> call, Response<MyResponse<RBean>> response) {
                dismissLoading();
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().status.equals("200")) {
                            Log.e("治疗界面", "治疗数据上传成功");
                            toastMessage("数据上传成功");
                        } else {
                            Log.e("治疗界面", "治疗数据上传"+response.body().message);
                            saveFaultCodeLocal("治疗数据上传,"+response.body().message);
                            toastMessage(response.body().message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<RBean>> call, Throwable t) {
                dismissLoading();
                Log.e("历史治疗数据", t.getMessage() + "--url--"+call.request().url());
                toastMessage("上传失败");
                saveFaultCodeLocal("治疗数据上传,"+t.getMessage());
            }
        });

    }

    @Override
    public void notifyByThemeChanged() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private void init(int page) {
//        pdListBeans.addAll(rowsDTOS);
        localPdAdapter.setNewData(getPdInfo(page));
        localPdAdapter.notifyDataSetChanged();
    }

}