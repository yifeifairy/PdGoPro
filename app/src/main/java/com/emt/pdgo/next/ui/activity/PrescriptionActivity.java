package com.emt.pdgo.next.ui.activity;

import android.app.Dialog;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.HisPrescriptionBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.activity.apd.prescrip.AapdActivity;
import com.emt.pdgo.next.ui.activity.dpr.DprPrescriptionActivity;
import com.emt.pdgo.next.ui.activity.local.LocalPrescriptionActivity;
import com.emt.pdgo.next.ui.activity.vo.SpeVolActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.ModeDialog;
import com.emt.pdgo.next.ui.mode.activity.ipd.IpdActivity;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.ClickUtil;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionActivity extends BaseActivity {

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

    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.reviseBtn)
    Button reviseBtn;

    @BindView(R.id.isFinalBtn)
    Button isFinalBtn;

    @BindView(R.id.btnRecord)
    Button btnRecord;
    @BindView(R.id.btnRx)
    Button btnRx;
    @BindView(R.id.btnParam)
    Button btnParam;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.ipdTv)
    TextView ipdTv;

    @BindView(R.id.settingIv)
    ImageView settingIv;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_prescription);
        ButterKnife.bind(this);

        if (checkConnectNetwork(this)) {
            if (!MyApplication.isRemoteShow) {
                getRemotePd();
            }
        }
    }

    @Override
    public void registerEvents() {
        btnNext.setOnClickListener(view -> {
            doGoTOActivity(PreHeatActivity.class);
        });
        settingIv.setOnClickListener(view -> doGoTOActivity(SettingActivity.class));
        ClickUtil clickUtil = new ClickUtil(reviseBtn, 3000);
        clickUtil.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    doGoTOActivity(IpdActivity.class);
                }
            }
        });
        toEngAc(title);
        btnParam.setOnClickListener(view -> {
            doGoTOActivity(ApdParamSetActivity.class);
        });
        btnRecord.setOnClickListener(view -> {
            doGoTOActivity(MyDreamActivity.class);
        });
        btnRx.setOnClickListener(view -> {
            doGoTOActivity(LocalPrescriptionActivity.class);
        });
        ipdTv.setOnClickListener(view -> {
            ModeDialog modeDialog = new ModeDialog(this);
            modeDialog.setOnDialogResultListener(new ModeDialog.OnDialogResultListener() {
                @Override
                public void onResult(int model) {
                    if (model == 1) {
                        modeDialog.dismiss();
                    } else if (model == 2) {
                        doGoTOActivity(AapdActivity.class);
                        modeDialog.dismiss();
                    } else if (model == 3) {
                        doGoTOActivity(SpeVolActivity.class);
                        modeDialog.dismiss();
                    } else if (model == 4) {
                        doGoTOActivity(DprPrescriptionActivity.class);
                        modeDialog.dismiss();
                    }
                }
            });
            modeDialog.show();
        });
    }

    private IpdBean ipdBean;
    @Override
    public void initViewData() {
        ipdBean = PdproHelper.getInstance().ipdBean();
        totalVolTv.setText(String.valueOf(ipdBean.peritonealDialysisFluidTotal));//腹透液总量
        cycleVolTv.setText(String.valueOf(ipdBean.perCyclePerfusionVolume));//每周期灌注量
        cycleNumTv.setText(String.valueOf(ipdBean.cycle));//循环治疗周期数
        abdTimeTv.setText(String.valueOf(ipdBean.abdomenRetainingTime));//留腹时间
        finalVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeFinally));//末次留腹量
        lastVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeLastTime));//上次留腹量
        firstVolTv.setText(String.valueOf(ipdBean.firstPerfusionVolume));//首次灌注量
        ultVolTv.setText(String.valueOf(ipdBean.ultrafiltrationVolume));//预估超滤量
        isFinalBtn.setText(ipdBean.isFinalSupply? "最末袋(通道1):已开启": "最末袋(通道1):已关闭");

        ipdTv.setText(ipdBean.firstPerfusionVolume == 0 ?"IPD模式" : "TPD模式");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("onResume","onResume");
//        if(MyApplication.isReset){
            ipdBean = PdproHelper.getInstance().ipdBean();
            totalVolTv.setText(String.valueOf(ipdBean.peritonealDialysisFluidTotal));//腹透液总量
            cycleVolTv.setText(String.valueOf(ipdBean.perCyclePerfusionVolume));//每周期灌注量
            cycleNumTv.setText(String.valueOf(ipdBean.cycle));//循环治疗周期数
            abdTimeTv.setText(String.valueOf(ipdBean.abdomenRetainingTime));//留腹时间
            finalVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeFinally));//末次留腹量
            lastVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeLastTime));//上次留腹量
            firstVolTv.setText(String.valueOf(ipdBean.firstPerfusionVolume));//首次灌注量
            ultVolTv.setText(String.valueOf(ipdBean.ultrafiltrationVolume));//预估超滤量
            isFinalBtn.setText(ipdBean.isFinalSupply? "最末袋(通道1):已开启": "最末袋(通道1):已关闭");
//        }
        ipdTv.setText(ipdBean.firstPerfusionVolume == 0 ?"IPD模式" : "TPD模式");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    private CommonDialog dialog;
    private double predictUlt,agoRetention;
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
                                try {
                                    if (response.body().data.getRcpInfo().getPredictUlt().equals("")) {
                                        predictUlt = 0;
                                    } else {
                                        predictUlt = (int) response.body().data.getRcpInfo().getPredictUlt();
                                    }
                                    if (response.body().data.getRcpInfo().getAgoRetention().equals("")) {
                                        agoRetention = 0;
                                    } else {
                                        agoRetention = (double) response.body().data.getRcpInfo().getAgoRetention();
                                    }
                                    APIServiceManage.getInstance().postApdCode("Z2021");
                                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.UID, response.body().getData().getRcpInfo().getPatientId() + "");
                                    if (ipdBean.peritonealDialysisFluidTotal == response.body().data.getRcpInfo().getIcodextrinTotal()
                                            &&
                                            ipdBean.perCyclePerfusionVolume == response.body().data.getRcpInfo().getInFlowCycle()
                                            &&
                                            ipdBean.cycle == response.body().data.getRcpInfo().getCycle() &&
                                            ipdBean.firstPerfusionVolume == response.body().data.getRcpInfo().getFirstInFlow() &&
                                            ipdBean.abdomenRetainingTime == response.body().data.getRcpInfo().getRetentionTime() &&
                                            ipdBean.abdomenRetainingVolumeFinally == response.body().data.getRcpInfo().getLastRetention() &&
                                            ipdBean.ultrafiltrationVolume == predictUlt &&
                                            ipdBean.abdomenRetainingVolumeLastTime == agoRetention
                                    ) {

                                    } else {
                                        dialog = new CommonDialog(PrescriptionActivity.this);
                                        dialog.setContent("您有一份远程处方是否应用")
                                                .setBtnFirst("应用")
                                                .setBtnTwo("取消")
                                                .setFirstClickListener(new CommonDialog.OnCommonClickListener() {
                                                    @Override
                                                    public void onClick(CommonDialog mCommonDialog) {
                                                        ipdBean.peritonealDialysisFluidTotal = response.body().data.getRcpInfo().getIcodextrinTotal();
                                                        ipdBean.perCyclePerfusionVolume = response.body().data.getRcpInfo().getInFlowCycle();
                                                        ipdBean.cycle = response.body().data.getRcpInfo().getCycle();
                                                        ipdBean.firstPerfusionVolume = response.body().data.getRcpInfo().getFirstInFlow();
                                                        ipdBean.abdomenRetainingTime = response.body().data.getRcpInfo().getRetentionTime();
                                                        ipdBean.abdomenRetainingVolumeFinally = response.body().data.getRcpInfo().getLastRetention();
                                                        ipdBean.abdomenRetainingVolumeLastTime = (int) agoRetention;
                                                        ipdBean.ultrafiltrationVolume = (int) predictUlt;

                                                        totalVolTv.setText(String.valueOf(ipdBean.peritonealDialysisFluidTotal));//腹透液总量
                                                        cycleVolTv.setText(String.valueOf(ipdBean.perCyclePerfusionVolume));//每周期灌注量
                                                        cycleNumTv.setText(String.valueOf(ipdBean.cycle));//循环治疗周期数
                                                        abdTimeTv.setText(String.valueOf(ipdBean.abdomenRetainingTime));//留腹时间
                                                        finalVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeFinally));//末次留腹量
                                                        lastVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeLastTime));//上次留腹量
                                                        firstVolTv.setText(String.valueOf(ipdBean.firstPerfusionVolume));//首次灌注量
                                                        ultVolTv.setText(String.valueOf(ipdBean.ultrafiltrationVolume));//预估超滤量
                                                        ipdTv.setText(ipdBean.firstPerfusionVolume == 0 ?"IPD模式" : "TPD模式");
                                                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, ipdBean);
                                                        mCommonDialog.dismiss();
                                                    }
                                                })
                                                .setTwoClickListener(Dialog::dismiss);
                                        if (!PrescriptionActivity.this.isFinishing()) {
                                            dialog.show();
                                            MyApplication.isRemoteShow = true;
                                        }
                                    }
                                }catch (Exception e) {
                                    Log.e("远程处方", e.getLocalizedMessage());
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