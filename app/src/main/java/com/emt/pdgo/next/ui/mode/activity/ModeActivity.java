package com.emt.pdgo.next.ui.mode.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.TpdBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.HisPrescriptionBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.BindPhoneActivity;
import com.emt.pdgo.next.ui.activity.EngineerSettingActivity;
import com.emt.pdgo.next.ui.activity.HistoricalPrescriptionActivity;
import com.emt.pdgo.next.ui.activity.HistoricalTreatmentActivity;
import com.emt.pdgo.next.ui.activity.RemotePrescribingActivity;
import com.emt.pdgo.next.ui.activity.SettingActivity;
import com.emt.pdgo.next.ui.activity.apd.prescrip.AapdActivity;
import com.emt.pdgo.next.ui.activity.apd.prescrip.ExpertActivity;
import com.emt.pdgo.next.ui.activity.apd.prescrip.KidActivity;
import com.emt.pdgo.next.ui.activity.apd.prescrip.TpdActivity;
import com.emt.pdgo.next.ui.activity.dpr.DprPrescriptionActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.mode.activity.ccpd.CcpdActivity;
import com.emt.pdgo.next.ui.mode.activity.cfpd.CfpdActivity;
import com.emt.pdgo.next.ui.mode.activity.ipd.IpdActivity;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModeActivity extends BaseActivity {

    @BindView(R.id.ipdMode)
    LinearLayout ipdMode;

    @BindView(R.id.tpdMode)
    LinearLayout tpdMode;


    @BindView(R.id.cCpdMode)
    LinearLayout cCpdMode;

    @BindView(R.id.aApdMode)
    LinearLayout aApdMode;

    @BindView(R.id.cFpdMode)
    LinearLayout cFpdMode;

    @BindView(R.id.dprMode)
    LinearLayout dprMode;

    @BindView(R.id.kidMode)
    LinearLayout kidMode;

    @BindView(R.id.expertMode)
    LinearLayout expertMode;

    @BindView(R.id.specialLayout)
    LinearLayout specialLayout;
    @BindView(R.id.specialMode)
    LinearLayout specialMode;
    @BindView(R.id.specialTV)
    TextView specialTV;
    @BindView(R.id.bindLayout)
    LinearLayout bindLayout;

    @BindView(R.id.settingLayout)
    LinearLayout settingLayout;

    @BindView(R.id.dataHisLayout)
    LinearLayout dataHisLayout;

    @BindView(R.id.rxHisLayout)
    LinearLayout rxHisLayout;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_mode);
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
        init();
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
        settingLayout.setOnClickListener(v -> {
            doGoTOActivity(SettingActivity.class);
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        dataHisLayout.setOnClickListener(v -> {
            doGoTOActivity(HistoricalTreatmentActivity.class);
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        rxHisLayout.setOnClickListener(v -> {doGoTOActivity(HistoricalPrescriptionActivity.class);
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });

        ipdMode.setOnClickListener(v -> {
            doGoTOActivity(IpdActivity.class);
            MyApplication.isDpr = false;
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        aApdMode.setOnClickListener(v -> {doGoTOActivity(AapdActivity.class);
            MyApplication.isDpr = false;
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        kidMode.setOnClickListener(v -> {
            doGoTOActivity(KidActivity.class);
            MyApplication.isDpr = false;
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        expertMode.setOnClickListener(v -> {
            doGoTOActivity(ExpertActivity.class);
            MyApplication.isDpr = false;
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        tpdMode.setOnClickListener(v -> {
            doGoTOActivity(TpdActivity.class);
            MyApplication.isDpr = false;
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        cCpdMode.setOnClickListener(v -> {
            doGoTOActivity(CcpdActivity.class);
            MyApplication.isDpr = false;
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        dprMode.setOnClickListener(v -> {
            doGoTOActivity(DprPrescriptionActivity.class);
            MyApplication.isDpr = true;
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        cFpdMode.setOnClickListener(v -> {
            doGoTOActivity(CfpdActivity.class);
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }
        });
        specialMode.setOnClickListener(view -> {
            specialLayout.setVisibility(specialLayout.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
            specialTV.setBackgroundResource(specialLayout.getVisibility()==View.VISIBLE?R.drawable.gray_blue_shape:R.drawable.gray_shape_stroke);
            kidMode.setVisibility(specialLayout.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
            cCpdMode.setVisibility(specialLayout.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
            tpdMode.setVisibility(specialLayout.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
            ipdMode.setVisibility(specialLayout.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
        });
        bindLayout.setOnClickListener(v -> {doGoTOActivity(BindPhoneActivity.class);
            if (remoteDisposable != null) {
                remoteDisposable.dispose();
                remoteDisposable.clear();
            }});

        ivLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                Log.d("onTouch", "action down");
                        isPressed = true;
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                        ivLogo.postDelayed(mCheckForLongPress, 5000);
                        break;
//            case MotionEvent.ACTION_MOVE:
//                isLongPressed = true;
//                break;
                    case MotionEvent.ACTION_UP:
                        isPressed = false;
//                Log.d("onTouch", "action up");
                        break;

                }

                return true;
            }
        });

    }

    @Override
    public void initViewData() {
        mCheckForLongPress = new CheckForLongPress();
    }
    private CheckForLongPress mCheckForLongPress;
    private volatile boolean isPressed = false;

    private class CheckForLongPress implements Runnable {

        @Override
        public void run() {
            //5s之后，查看isLongPressed的变量值：
            if (isPressed) {//没有做up事件
                alertNumberBoard("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
            } else {
                ivLogo.removeCallbacks(mCheckForLongPress);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (remoteDisposable != null) {
            remoteDisposable.dispose();
            remoteDisposable.clear();
        }
    }

    private boolean click;
    private CommonDialog dialog;
    private double predictUlt,agoRetention;
    private void getRemotePd() {
        APIServiceManage.getInstance().postApdCode("Z2000");
        RetrofitUtil.getService().getRemotePrescription(PdproHelper.getInstance().getMachineSN()).enqueue(new Callback<MyResponse<HisPrescriptionBean>>() {
            @Override
            public void onResponse(Call<MyResponse<HisPrescriptionBean>> call, Response<MyResponse<HisPrescriptionBean>> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().getData().getCode().equals("10000")) {
                            TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                            if (response.body().getData().getRcpInfo() != null) {
                                if (response.body().data.getRcpInfo().getPredictUlt().equals("")) {
                                    predictUlt = 0;
                                } else {
                                    predictUlt = (double) response.body().data.getRcpInfo().getPredictUlt();
                                }
                                if (response.body().data.getRcpInfo().getAgoRetention().equals("")) {
                                    agoRetention = 0;
                                } else {
                                    agoRetention = (double) response.body().data.getRcpInfo().getAgoRetention();
                                }
                                CacheUtils.getInstance().getACache().put(PdGoConstConfig.UID, response.body().data.getRcpInfo().getPatientId() + "");
                                if (tpdBean.peritonealDialysisFluidTotal == response.body().data.getRcpInfo().getIcodextrinTotal()
                                        &&
                                        tpdBean.perCyclePerfusionVolume == response.body().data.getRcpInfo().getInFlowCycle()
                                        &&
                                        tpdBean.cycle == response.body().data.getRcpInfo().getCycle() &&
                                        tpdBean.firstPerfusionVolume == response.body().data.getRcpInfo().getFirstInFlow() &&
                                        tpdBean.abdomenRetainingTime == response.body().data.getRcpInfo().getRetentionTime() &&
                                        tpdBean.abdomenRetainingVolumeFinally == response.body().data.getRcpInfo().getLastRetention() &&
                                        tpdBean.ultrafiltrationVolume == predictUlt&&
                                        tpdBean.abdomenRetainingVolumeLastTime == agoRetention
                                ) {
                                    APIServiceManage.getInstance().postApdCode("Z2020");
                                } else {
                                    APIServiceManage.getInstance().postApdCode("Z2021");
                                    dialog = new CommonDialog(ModeActivity.this);
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
                                                    Intent intent = new Intent(ModeActivity.this, RemotePrescribingActivity.class);
                                                    intent.putExtra("icodextrinTotal", response.body().data.getRcpInfo().getIcodextrinTotal());
                                                    intent.putExtra("inFlowCycle", response.body().data.getRcpInfo().getInFlowCycle());
                                                    intent.putExtra("cycle", response.body().data.getRcpInfo().getCycle());
                                                    intent.putExtra("firstInFlow", response.body().data.getRcpInfo().getFirstInFlow());
                                                    intent.putExtra("retentionTime", response.body().data.getRcpInfo().getRetentionTime());
                                                    intent.putExtra("lastRetention", response.body().data.getRcpInfo().getLastRetention());
                                                    intent.putExtra("agoRetention", (int) agoRetention);
                                                    intent.putExtra("predictUlt", (int)predictUlt);
                                                    intent.putExtra("treatTime", response.body().data.getRcpInfo().getTreatTime());
                                                    startActivity(intent);
//                                                    doGoTOActivity(RemotePrescribingActivity.class);
                                                    mCommonDialog.dismiss();
                                                }
                                            }).setTwoClickListener(new CommonDialog.OnCommonClickListener() {
                                                @Override
                                                public void onClick(CommonDialog mCommonDialog) {
                                                    mCommonDialog.dismiss();
                                                }
                                            });
                                    if (!ModeActivity.this.isFinishing()) {
                                        dialog.show();
                                        click = true;
                                        MyApplication.isRemoteShow = true;
                                    }
                                }
                            } else {
                                APIServiceManage.getInstance().postApdCode("Z2020");
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse<HisPrescriptionBean>> call, Throwable t) {
                Log.e("modeActivity", t.getMessage());
            }
        });
    }

    private void alertNumberBoard(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
//                    Logger.d(result);
                Calendar calendar = Calendar.getInstance();//取得当前时间的年月日 时分秒

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                String mMonth = "";

                if (month >= 10) {
                    mMonth = "" + month;
                } else {
                    mMonth = "0" + month;
                }

                //123加上月份
                String tempPwd = "123" + mMonth;
                Log.e("长按", "tempPwd：" + tempPwd);
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式的密码
                    if (tempPwd.equals(result)) {
                        doGoTOActivity(EngineerSettingActivity.class);
                        if (remoteDisposable != null) {
                            remoteDisposable.dispose();
                            remoteDisposable.clear();
                        }
                    }
                }
            }
        });
    }

    private CompositeDisposable remoteDisposable;
    private void init() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    if (checkConnectNetwork(ModeActivity.this) ){
                        if (!click && !MyApplication.isRemoteShow) {
                            getRemotePd();
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.interval(0, 3000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        if (remoteDisposable == null) {
            remoteDisposable = new CompositeDisposable();
        }
        remoteDisposable.add(disposableObserver);
    }

    @Override
    public void notifyByThemeChanged() {

    }
}