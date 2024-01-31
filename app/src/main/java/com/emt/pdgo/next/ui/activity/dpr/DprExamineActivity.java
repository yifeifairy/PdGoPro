package com.emt.pdgo.next.ui.activity.dpr;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.ReceivePublicDataBean;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.data.serial.dpr.receive.BaseSupplyBean;
import com.emt.pdgo.next.data.serial.dpr.receive.BaseSupplyRunningBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprDrainFinishBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprDrainRunningBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprDrainStartBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprDrainVacuumBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprDrainVacuumRunningBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprPerFinishBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprPerOverTemp;
import com.emt.pdgo.next.data.serial.dpr.receive.DprPerRunningBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprPerStartBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprRunningBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprStartBean;
import com.emt.pdgo.next.data.serial.dpr.receive.LowerFlowRate;
import com.emt.pdgo.next.data.serial.dpr.receive.Supply2RunningBean;
import com.emt.pdgo.next.data.serial.dpr.receive.SupplyFinishBean;
import com.emt.pdgo.next.data.serial.dpr.receive.UpFlowRate;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean1;
import com.emt.pdgo.next.model.dpr.machine.Prescription;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.EngineerSettingActivity;
import com.emt.pdgo.next.ui.activity.TreatmentEvaluationActivity;
import com.emt.pdgo.next.ui.activity.TreatmentFeedbackActivity;
import com.emt.pdgo.next.ui.activity.TreatmentModeActivity;
import com.emt.pdgo.next.ui.activity.param.FaultCodeActivity;
import com.emt.pdgo.next.ui.adapter.dpr.ViewPagerAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.dialog.TreatmentAlarmDialog;
import com.emt.pdgo.next.ui.fragment.dpr.treat.GraphDataFragment;
import com.emt.pdgo.next.ui.fragment.dpr.treat.RegularFragment;
import com.emt.pdgo.next.ui.fragment.dpr.treat.ViewDataFragment;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class DprExamineActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;

    @BindView(R.id.tempSet)
    TextView tempSet;
    @BindView(R.id.drainRoteSet)
    TextView drainRoteSet;
    @BindView(R.id.perRoteSet)
    TextView perRoteSet;
    @BindView(R.id.tempTv)
    TextView tempTv;
    @BindView(R.id.drainRate)
    TextView drainRate;
    @BindView(R.id.preRate)
    TextView preRate;

    @BindView(R.id.tvTotalTreatTime)
    TextView tvTotalTreatTime;

    @BindView(R.id.btn_finish_treatment)
    StateButton btn_finish_treatment;

    @BindView(R.id.btn_fragment1)
    StateButton btn_fragment1;
    @BindView(R.id.viewData)
    StateButton viewData;

    @BindView(R.id.layout_pause)
    RelativeLayout layoutPause;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_pause)
    TextView tvPause;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.viewDataLayout)
    LinearLayout viewDataLayout;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.faultCodeBtn)
    StateButton faultCodeBtn;

    @BindView(R.id.goBackBtn)
    StateButton goBackBtn;
    @BindView(R.id.modifyTabLayout)
    TabLayout modifyTabLayout;
    @BindView(R.id.modifyViewpager)
    ViewPager modifyViewpager;

    @BindView(R.id.modifyLayout)
    LinearLayout modifyLayout;
    @BindView(R.id.modifyBackBtn)
    StateButton modifyBackBtn;

    @BindView(R.id.modifyConfirmBtn)
    StateButton modifyConfirmBtn;

    public Prescription prescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @BindView(R.id.powerIv)
    ImageView powerIv;
    @BindView(R.id.currentPower)
    TextView currentPower;
//    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
//    public void receiveCmdDeviceInfo(String bean) {
//        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
//        runOnUiThread(() -> {
//            if (mReceiveDeviceBean.isAcPowerIn == 1) {
//                powerIv.setImageResource(R.drawable.charging);
//            } else {
//                if (mReceiveDeviceBean.batteryLevel < 30) {
//                    powerIv.setImageResource(R.drawable.poor_power);
//                } else if (30 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 60 ) {
//                    powerIv.setImageResource(R.drawable.low_power);
//                } else if (60 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 80 ) {
//                    powerIv.setImageResource(R.drawable.mid_power);
//                } else {
//                    powerIv.setImageResource(R.drawable.high_power);
//                }
//            }
//            currentPower.setText(mReceiveDeviceBean.batteryLevel+"");
//        });
//    }
    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_dpr_examine);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        MyApplication.dprTreatRunning = true;
        ActivityManager.getActivityManager().removeAllActivityExceptOne(DprExamineActivity.class);
        compositeDisposable = new CompositeDisposable();
        mCheckForLongPress1 = new CheckForLongPress1();
        reportCompositeDisposable = new CompositeDisposable();
        drainAndPerTimingDisposable = new CompositeDisposable();
        initTime();
        init();
//        MyApplication.inTreatment = true;
        MyApplication.mStartTime = EmtTimeUil.getTime();
        drainRate.setText(String.valueOf(prescription.drain_rate));
        preRate.setText(String.valueOf(prescription.perfuse_rate));
//        viewDataLayout.setVisibility(View.INVISIBLE);
        prescription = PdproHelper.getInstance().getPrescription();
        initViewPage();
        initViewpage2();
    }

    private ViewDataFragment viewDataFragment;
    private GraphDataFragment graphDataFragment;
    private RegularFragment regularFragment;
    private void initViewPage() {
        List<Fragment> fragmentList = new ArrayList<>();
        graphDataFragment = new GraphDataFragment();
        viewDataFragment = new ViewDataFragment();
        fragmentList.add(graphDataFragment);
        fragmentList.add(viewDataFragment);
        List<String> titles = new ArrayList<>();
        titles.add("图形数据");
        titles.add("详细数据");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragmentList, titles);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
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

    private void initViewpage2() {
        List<Fragment> fragmentList = new ArrayList<>();
        regularFragment = new RegularFragment();
        fragmentList.add(regularFragment);
        List<String> titles = new ArrayList<>();
        titles.add("处方修改");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList, titles);
        modifyViewpager.setAdapter(adapter);
        modifyTabLayout.setupWithViewPager(modifyViewpager);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void registerEvents() {
        initHeadTitleBar("");
        modifyConfirmBtn.setOnClickListener(view -> {
            final CommonDialog dialog = new CommonDialog(this);
            dialog.setContent("是否修改")
                    .setBtnFirst("确定")
                    .setBtnTwo("取消")
                    .setFirstClickListener(mCommonDialog -> {
                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRP_PRESCRIPTION, prescription);
                        sendToMainBoard(CommandDataHelper.getInstance().config(Integer.parseInt(prescription.modify,16)));
                        mCommonDialog.dismiss();
                    })
                    .setTwoClickListener(mCommonDialog -> {
                        mCommonDialog.dismiss();
                    });
            if (!DprExamineActivity.this.isFinishing()) {
                dialog.show();
            }
        });
        ivLogo.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                Log.d("onTouch", "action down");
                    isLongPressed = true;
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                    ivLogo.postDelayed(mCheckForLongPress1, 5000);
                    break;
//                case MotionEvent.ACTION_MOVE:
//                    isLongPressed = true;
//                    break;
                case MotionEvent.ACTION_UP:
                    isLongPressed = false;
//                Log.d("onTouch", "action up");
                    break;

            }
            return true;
        });
        faultCodeBtn.setOnClickListener(v -> {
            doGoTOActivity(FaultCodeActivity.class);
        });
        goBackBtn.setOnClickListener(v -> {
            mainLayout.setVisibility(View.VISIBLE);
            viewDataLayout.setVisibility(View.GONE);
            modifyLayout.setVisibility(View.GONE);
        });
        modifyBackBtn.setOnClickListener(view -> {
            mainLayout.setVisibility(View.VISIBLE);
            viewDataLayout.setVisibility(View.GONE);
            modifyLayout.setVisibility(View.GONE);
        });
        viewData.setOnClickListener(v -> {
            mainLayout.setVisibility(View.GONE);
            viewDataLayout.setVisibility(View.VISIBLE);
            modifyLayout.setVisibility(View.GONE);
        });
        btnBack.setVisibility(View.GONE);
        btn_finish_treatment.setOnClickListener(v -> {
            showCompleteDialog();
        });
        tempSet.setOnClickListener(v -> {
            alertNumberBoardDialog(tempTv.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE,false);
        });
        perRoteSet.setOnClickListener(v -> {
            alertNumberBoardDialog(preRate.getText().toString(), PdGoConstConfig.DPR_PER_RATE,true);
        });
        drainRoteSet.setOnClickListener(v -> {
            alertNumberBoardDialog(drainRate.getText().toString(), PdGoConstConfig.DPR_DRAIN_RATE,true);
        });

        btn_fragment1.setOnClickListener(v -> {
            doGoTOActivity(DprPrescriptionActivity.class);
//            Intent intent = new Intent(this, DprPrescriptionActivity.class);
//            intent.putExtra(EmtConstant.JUMP_WITH_PARAM,"治疗中");
//            startActivity(intent);
        });

        layoutPause.setOnClickListener(v -> showPauseDialog());
    }

    private CompositeDisposable drainAndPerTimingDisposable;
    @Override
    public void initViewData() {
//        setTreatmentCmd();
        sendCommandInterval(CommandDataHelper.getInstance().startTreatment(1,MyApplication.isDebug ? 1 : 0),500);
    }
    public Gson myGson = new Gson();
    @BindView(R.id.tvTotalDrainVol)
    TextView tvTotalDrainVol;
    @BindView(R.id.tvTotalPerVol)
    TextView tvTotalPerVol;

    @BindView(R.id.currentDrainSpeed)
    TextView currentDrainSpeed;
    @BindView(R.id.currentPerSpeed)
    TextView currentPerSpeed;
    @BindView(R.id.speedStatus)
    TextView speedStatus;

    public int retain; // 留腹量
    public int drainTarget; // 目标引流量
    public int drainPass; // 引流通过量
    public int work_state; // 工作状态
    public int flowRate; // 流速
    public int drain; // 引流量
    public int perfuseTarget; // 灌注目标量
    public int perfuse; // 灌注量
    public int upperWeight; // 开始补液上位称重量
    public int totalPerVol; // 总灌注量
    public int totalDrainVolume; // 总引流量

    private int drainTiming;
    private int perTiming;
    private int dprTiming;

    private boolean isDrainRunning;
    private boolean isPerRunning;
    private boolean isDprRunning;

    private int index;
    @Subscribe(code = RxBusCodeConfig.EVENT_DPR)
    public void receive(ReceivePublicDataBean receiveBean) {
        // #define DPR_DRAIN_CYCLE0_STAGE                   6     //首周期引流
        //#define DPR_DRAIN_TIMING_STAGE                   7    //定时排空
        //#define DPR_DRAIN_FINAL_STAGE                    8   //引流完成
        //#define DPR_PERFUSE_FIRST_STAGE                  9    //首次或者定时到灌注
        //#define DPR_PERFUSE_SUPPLY_STAGE                 10   //DPR过程的补液
        //#define DPR_CONTINUE_STAGE                       11   //DPR持续阶段
        if (receiveBean.publish.topic.equals("dpr/treatment") && receiveBean.publish.msg.equals("finish")) {
            showCompleteDialog();
        }
        switch (receiveBean.publish.msg) {
            case "drain start": // 开始引流
                DprDrainStartBean drainStartBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprDrainStartBean.class);
                runOnUiThread(() -> {
                    switch (drainStartBean.work_state) {
                        case 6:
                            status.setText("初始排空");
                            break;
                        case 7:
                            status.setText("定时排空");
                            break;
                    }
                    if (null != viewDataFragment.getActivity()) {
                        viewDataFragment.setDrainPassVolTv(drainStartBean.drainPass);
                        viewDataFragment.setDrainTargetVVolTv(drainStartBean.drainTarget);
                    }
                });
                break;
            case "dpr drain running": // 引流中
                DprDrainRunningBean drainRunningBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprDrainRunningBean.class);
                runOnUiThread(() -> {
                    isDrainRunning = true;
                    if (!isTiming) {
                        drainTiming = 0;
                        startLoopTimer();
                    }

                    if (null != viewDataFragment.getActivity()) {
                        viewDataFragment.setRealtimeDrain(drainRunningBean.drain);
                        viewDataFragment.setRetain(drainRunningBean.retain);
                    }
                    currentRetainVol.setText(String.valueOf(drainRunningBean.retain));
                    currentDrainVol.setText(String.valueOf(drainRunningBean.drain));
                    if (null != graphDataFragment.getActivity()) {
                        if (drainTiming == 0 && isTiming) {
                            index++;
                            graphDataFragment.setData(time, new float[]{-drainRunningBean.drain});
                        } else if (drainTiming % 30 == 0 && isTiming) {
                            index++;
                            graphDataFragment.setData(time, new float[]{-drainRunningBean.drain});
                        }
//                        graphDataFragment.setData();
                    }
                });
                break;
            case "drain finish": // 引流结束
                DprDrainFinishBean dprDrainFinishBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprDrainFinishBean.class);
                runOnUiThread(() -> {
                    isDrainRunning = false;
                    switch (dprDrainFinishBean.work_state) {
                        case 8:
                            status.setText("完成排空");
                            break;
                    }
                });
                break;
            case "final empty start": // 最末引流开始
                runOnUiThread(()-> {
                    status.setText("最末引流开始");
                });
                break;
            case "final emptying running": // 最末引流排空中
                runOnUiThread(()-> {
                    status.setText("最末引流排空中");
                });
                break;
            case "final empty finish": // 最末引流排空结束
                runOnUiThread(()-> {
                    status.setText("最末引流排空结束");
                });
                break;
            case "drain poor": // 引流不足
                break;
            case "drain failure": // 引流失败
                runOnUiThread(this::drainFaultDialog);
                break;
            case "vaccum drain start": // 负压引流开始
                DprDrainVacuumBean vacStartBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprDrainVacuumBean.class);
//                    runOnUiThread(()-> {
//                        status.setText("负压引流开始");
//                    });
                break;
            case "vaccum drain restart": // 重新负压引流
                DprDrainVacuumBean vacRestartBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprDrainVacuumBean.class);

            case "vaccum drain finish":
                break;
            case "vaccum drain running": // 负压引流中
                DprDrainVacuumRunningBean vacuumRunningBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprDrainVacuumRunningBean.class);
                runOnUiThread(() -> {
                    if (null != viewDataFragment.getActivity()) {
                        viewDataFragment.setVacDrain(vacuumRunningBean);
                        viewDataFragment.setDrainTargetVVolTv(vacuumRunningBean.drainTarget);
                        viewDataFragment.setRetain(vacuumRunningBean.retain);
                    }
                });
                break;
            case "perfuse start": // 开始灌注
                DprPerStartBean perStartBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprPerStartBean.class);
                runOnUiThread(() -> {
                    switch (perStartBean.work_stage) {
                        case 9:
                            status.setText("首剂量灌注");
                            break;
                        default:
                            status.setText("持续灌注");
                            break;
                    }
                });
                break;
            case "perfuse running": // 灌注进行中
                runOnUiThread(() -> {
                    isPerRunning = true;
                    if (!isTiming) {
                        perTiming = 0;
                        startLoopTimer();
                    }
                    DprPerRunningBean perRunningBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprPerRunningBean.class);
                    currentPerVol.setText(String.valueOf(perRunningBean.perfuse));
                    currentRetainVol.setText(String.valueOf(perRunningBean.retain));
                    if (null != viewDataFragment.getActivity()) {
                        viewDataFragment.setRealtimePer(perRunningBean.perfuse);
                        viewDataFragment.setRetain(perRunningBean.retain);
                        viewDataFragment.setPerTargetVol(perRunningBean.perfuseTarget);
                    }
                    if (null != graphDataFragment.getActivity()) {
                        if (perTiming == 0 && isTiming) {
                            index++;
                            graphDataFragment.setData(index, new float[]{perRunningBean.perfuse});
                        } else if (perTiming % 30 == 0 && isTiming) {
                            index++;
                            graphDataFragment.setData(index, new float[]{perRunningBean.perfuse});
                        }
                    }
                });
                break;
            case "perfuse finish": // 结束灌注
                DprPerFinishBean perFinishBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprPerFinishBean.class);
                isPerRunning = false;
                break;
            case "basebag supply start": // 开始基础补液
                BaseSupplyBean baseSupplyBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), BaseSupplyBean.class);
                runOnUiThread(() -> {
                    if (null != viewDataFragment.getActivity()) {
                        viewDataFragment.setBaseSupplyTargetMsg(baseSupplyBean);
                    }
                    status.setText("开始基础补液");
                });
                break;
            case "endbag supply start": // 末袋补液（渗透剂）
                runOnUiThread(()-> {
                    status.setText("末袋补液（渗透剂）");
                });
            case "supply1 start": // 补液1开始
                break;
            case "basebag supply restart": // 重新补液
                break;
            case "endbag supply restart":
                break;
            case "supply finish": // 补液结束
                SupplyFinishBean supplyFinishBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), SupplyFinishBean.class);
                runOnUiThread(()-> {
                    currentSupply.setText(String.valueOf(supplyFinishBean.Supply2Volume));
                    specialVolTv.setText(String.valueOf(supplyFinishBean.Supply1Volume));
                });
                break;
            case "basebag supply running":
                BaseSupplyRunningBean baseSupplyRunningBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), BaseSupplyRunningBean.class);
                runOnUiThread(()->{
                    status.setText("正在基础补液...");
                    if (downTimer != null) {
                        downTimer.cancel();
                        isDownTiming = false;
                    }
                    if (null != viewDataFragment.getActivity()) {
                        viewDataFragment.setBaseSupplyMsg(baseSupplyRunningBean);
                    }
                    currentSupply.setText(String.valueOf(baseSupplyRunningBean.Supply1Volume));
                });
                break;
            case "endbaf supply running":
                Supply2RunningBean supply2RunningBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), Supply2RunningBean.class);
                runOnUiThread(()->{
                    if (downTimer != null) {
                        downTimer.cancel();
                        isDownTiming = false;
                    }
                    specialVolTv.setText(String.valueOf(supply2RunningBean.Supply2Volume));
                });
                break;
            case "supply1 failure": // 补液1失败
                runOnUiThread(()->{
                    supplyFaultDialog();
                });
                break;
            case "supply2 failure": // 补液2失败
                runOnUiThread(this::supplyFaultDialog);
                break;
            case "perfuse over temp": // 灌注超温
                DprPerOverTemp overTemp = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprPerOverTemp.class);
                runOnUiThread(()->{
                    if (downTimer != null) {
                        downTimer.cancel();
                        isDownTiming = false;
                    }
                });
                break;
            case "perfuse failure": // 灌注失败
                runOnUiThread(this::perfusionFaultDialog);
                break;
            case "dpr run start": // DPR治疗开始
                DprStartBean dprStartBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprStartBean.class);
                runOnUiThread(()->{
                    if (downTimer != null) {
                        downTimer.cancel();
                        isDownTiming = false;
                    }
                    status.setText("DPR治疗");
                });
                break;
            case "dpr run restart":
                runOnUiThread(()-> {
                    if (downTimer != null) {
                        downTimer.cancel();
                        isDownTiming = false;
                    }
                });
                break;
            case "dpr running": // DPR治疗进行中
                try {
                    String json = myGson.toJson(receiveBean.publish.data);
                    List<Integer> numbers = new ArrayList<>();
//                    Log.e("json", "数据--"+json);
                    if (json.contains("perfusevolum")) {
                        DprRunningBean runningBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), DprRunningBean.class);
                        runOnUiThread(() -> {
                            totalPerVol = runningBean.perfusevolum;
                            totalDrainVolume = runningBean.drainvolume;
                            tvTotalDrainVol.setText(String.valueOf(totalDrainVolume));
                            tvTotalPerVol.setText(String.valueOf(totalPerVol));
                            currentDrainVol.setText(String.valueOf(runningBean.dpr_drainvolume));
                            currentPerVol.setText(String.valueOf(runningBean.dpr_perfusecolume));
                            currentRetainVol.setText(String.valueOf(runningBean.retain));
                            if (runningBean.speedIndicate == 0) {
                                speedStatus.setText("过大");
                            } else if (runningBean.speedIndicate == 1) {
                                speedStatus.setText("过小");
                            } else if (runningBean.speedIndicate == 2) {
                                speedStatus.setText("正常");
                            } else if (runningBean.speedIndicate == 3) {
                                speedStatus.setText("异常");
                            }
                            isDprRunning = true;
                            if (!isTiming) {
                                dprTiming = 0;
                                startLoopTimer();
                            }
//                            if (null != graphDataFragment.getActivity()) {
//                                if (!isDownTiming) {
//                                    countDown(runningBean);
//                                }
////                            running(runningBean);
//                            }
                        });
                    } else if (json.contains("lower_flowRate")){
                        runOnUiThread(() -> {
                            numbers.add(up_flowRate);
                            LowerFlowRate lowerFlowRate = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), LowerFlowRate.class);
                            numbers.add(lowerFlowRate.lower_flowRate);
                            currentDrainSpeed.setText(String.valueOf(lowerFlowRate.lower_flowRate));
                            if (null != graphDataFragment.getActivity()) {
                                graphDataFragment.addEntry(numbers);
                            }
                        });
                    } else if (json.contains("up_flowRate")) {
                        runOnUiThread(() -> {
                            UpFlowRate upFlowRate = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), UpFlowRate.class);
                            up_flowRate = upFlowRate.up_flowRate;
                            currentPerSpeed.setText(String.valueOf(up_flowRate));
                        });
                    }
                } catch (Exception e) {
                    Log.e("异常", e.getLocalizedMessage());
                }
                break;
            case "timing drain": // 定时排空
                runOnUiThread(()->{
                    if (downTimer != null) {
                        downTimer.cancel();
                        isDownTiming = false;
                    }
                    status.setText("定时排空");
                });
                break;
        }

    }

    private int up_flowRate;

    private boolean isDownTiming;
    private CountDownTimer downTimer;
    private void countDown(DprRunningBean runningBean) {
        downTimer = new CountDownTimer(59 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isDownTiming = true;
            }

            @Override
            public void onFinish() {
//                List<Integer> numbers = new ArrayList<>();
//                numbers.add(runningBean.up_flowRate);
//                numbers.add(runningBean.lower_flowRate);
//                graphDataFragment.addEntry(numbers);
//                isDownTiming = false;
            }
        }.start();
    }

    private boolean isTiming; // 是否计时
    /**
     * 开始轮询计时 灌注 引流
     */
    private void startLoopTimer() {
        isTiming = true;
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {

            @Override
            public void onNext(Long data) {
                runOnUiThread(() -> {
                    if (isTiming) {
                        if (isDrainRunning) {
                            drainTiming ++;
                            drainTimeTv.setText(EmtTimeUil.getTime(drainTiming));
                        }
                        if (isPerRunning) {
                            perTiming ++;
                            perTimeTv.setText(EmtTimeUil.getTime(perTiming));
                        }
                        if (isDprRunning) {
                            dprTiming ++;
//                            perTimeTv.setText(EmtTimeUil.getTime(drainTiming));
                        }
                    }
                });

            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("开始轮询计时", "DisposableObserver, onError=" + throwable);
            }

            @Override
            public void onComplete() {
                Log.e("开始轮询计时", "DisposableObserver, onComplete");
            }
        };
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        drainAndPerTimingDisposable.add(disposableObserver);
    }

    /**
     * 渗透压换算
     */
    private void osmConverse() {

    }

    private boolean isTpd;

    /**
     * 补液不畅故障
     */
    private void supplyFaultDialog() {
        buzzerOn();
//        speak("补液不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_supply));
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_supply))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_complete_not_supply), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(dialog -> {
                    buzzerOff();
//                    sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP),200);
                    dialog.dismiss();
                })
                .setTwoClickListener(mCommonDialog1 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());

                    buzzerOff();
                    mCommonDialog1.dismiss();
                })
                .setThreeClickListener(mCommonDialog13 -> {
                    //终止治疗
                    // mCommonDialog.dismiss();
                    finishTreatmentDialog(4);
                    buzzerOff();
                })
                .show();
    }

    private boolean isPause = false;//是否暂停
    private void showPauseDialog() {

        String tips = isPause ? "是否恢复治疗" : "是否暂停治疗";

        final CommonDialog dialog = new CommonDialog(this);
        //                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
        dialog.setContent(tips)
                .setBtnFirst("确定")
                .setBtnTwo("取消")
                .setFirstClickListener(mCommonDialog -> {
                    refreshPauseOrResume();
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(Dialog::dismiss)
                .show();

    }

    private CompositeDisposable compositeDisposable;
    private int time = 0;
    private void initTime() {

        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    time ++;
                    tvTotalTreatTime.setText(EmtTimeUil.getTime(time));
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        compositeDisposable.add(disposableObserver);
    }
    @BindView(R.id.iv_pause)
    ImageView ivPause;
    /**
     * 刷新暂停治疗的状态
     */
    private void refreshPauseOrResume() {
//        suspendLoopTimer(isPause);
        isPause = !isPause;
        runOnUiThread(() -> {
            ivPause.setBackgroundResource(isPause ? R.drawable.icon_treatment_resume : R.drawable.icon_treatment_pause);
//            ivResume.setVisibility(isPause ? View.VISIBLE : View.GONE);
            tvPause.setText(isPause ? "恢复" : "暂停");
//            tvPause.setTextColor(isPause ? Color.RED : Color.WHITE);
            layoutPause.setBackgroundResource(isPause ? R.drawable.btn_bg_contact_engineer_normal : R.drawable.btn_bg_pause);
//            if (currentStage == 2) {
//                isRetainTimePause = !isPause;
//            }
//            btnFinishTreatment.setVisibility(!isDrainManualEmptying && isPause ? View.VISIBLE : View.INVISIBLE);//最末排空等待时候，不显示结束治疗按钮
            sendToMainBoard(CommandDataHelper.getInstance().customCmd(isPause ? CommandSendConfig.METHOD_TREATMENT_PAUSE : CommandSendConfig.METHOD_TREATMENT_RESUME));
        });
    }

    private void alertNumberBoardDialog(String value, String type, boolean isInteger) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, isInteger);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE: // 治疗总量
                            tempTv.setText(result);
                            break;
                        case PdGoConstConfig.DPR_PER_RATE: //持续灌注速率
                            preRate.setText(result);
                            break;
                        case PdGoConstConfig.DPR_DRAIN_RATE: //持续引流速率
                            drainRate.setText(result);
                            break;
                    }
                }
            }
        });
    }
    @BindView(R.id.currentDrainVol)
    TextView currentDrainVol;
    @BindView(R.id.drainTimeTv)
    TextView drainTimeTv;
    @BindView(R.id.currentSupply)
    TextView currentSupply;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.perTimeTv)
    TextView perTimeTv;
    @BindView(R.id.currentPerVol)
    TextView currentPerVol;
    @BindView(R.id.currentRetainVol)
    TextView currentRetainVol;

    @BindView(R.id.specialVolTv)
    TextView specialVolTv;

    /**
     * 引流不畅故障
     */
    private void drainFaultDialog() {
        buzzerOn();
//        speak("引流不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_drain));
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_drain))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_drain), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过引流
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    buzzerOff();
                    mCommonDialog13.dismiss();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    // mCommonDialog.dismiss();
                    finishTreatmentDialog(3);
                    buzzerOff();
                })
                .show();
    }

    /**
     * 灌注不畅故障
     */
    private void perfusionFaultDialog() {
        buzzerOn();
//        speak("灌注不畅");
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_perfusion))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_perfusion), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    buzzerOff();
                    mCommonDialog13.dismiss();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    finishTreatmentDialog(1);
                    buzzerOff();
                })
                .show();

    }

    /**
     * 单片机返回指令不执行
     *
     * @param bean
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_ERR)
    public void receiveCmdResultErr(ReceiveResultDataBean bean) {
        String topic = bean.result.topic;
    }

    /**
     * 故障时候点击终止治疗的二次确认弹窗
     */
    private void finishTreatmentDialog(int mStage) {

        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent(getResources().getString(R.string.dialog_status_early))
                .setBtnFirst("确定")
                .setBtnTwo("取消")
                .setFirstClickListener(mCommonDialog -> {
                    if (1 == mStage) {//灌注阶段执行终止治疗
//                        sendCommandInterval(CommandSendConfig.TREATMENT_START_PERFUSION_FAULT_SHUTDOWN, 200);
                    } else if (3 == mStage) {//引流阶段执行终止治疗
//                        sendCommandInterval(CommandSendConfig.TREATMENT_START_DRAIN_FAULT_SHUTDOWN, 200);
                    } else if (4 == mStage) {//补液阶段执行终止治疗
//                        sendCommandInterval(CommandSendConfig.TREATMENT_START_SUPPLYFAULT_SHUTDOWN, 200);
                    }
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(Dialog::dismiss)
                .show();

    }

    private void init() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {

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
        reportCompositeDisposable.add(disposableObserver);
    }
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean1 mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean1.class);
        runOnUiThread(() -> {
            if (mReceiveDeviceBean != null) {
                if (null != viewDataFragment.getActivity()) {
                    viewDataFragment.setDeviceStatusInfo(mReceiveDeviceBean);
                }
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
                currentPower.setText(mReceiveDeviceBean.batteryLevel+"");
            }
        });
    }
    private final BigDecimal mTen = new BigDecimal(10);
    private float getTemp(int temp) {
        BigDecimal mTemp = new BigDecimal(temp);
        BigDecimal newTemp = mTemp.divide(mTen, 1, RoundingMode.HALF_UP);
        return newTemp.floatValue();
    }
    private CompositeDisposable reportCompositeDisposable;
    private void showCompleteDialog() {
        status.setText("治疗完成");
        MyApplication.mStartTime = EmtTimeUil.getTime();
        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_dagou)
                .setStatus(getResources().getString(R.string.dialog_status_complete))
                .setMessage(getResources().getString(R.string.dialog_status_complete_tip), R.color.dialog_text_default)
                .setBtnFirst("治疗评估", R.drawable.dialog_btn_blue)
                .setBtnTwo("重新治疗",R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_confirm), R.drawable.dialog_btn_green)
                .setFirstClickListener(mCommonDialog12 -> {
                    //查看数据
                    doGoCloseTOActivity(TreatmentFeedbackActivity.class,"");
                    mCommonDialog12.dismiss();
                })
                .setTwoClickListener(mCommonDialog1 -> {
                    doGoCloseTOActivity(TreatmentModeActivity.class,"");
//                    speak("治疗结束,请取下人体端,关闭所有管夹");
                    mCommonDialog1.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
//                    doGoTOActivity(DataCollectionActivity.class);
//                    speak("治疗结束,请取下人体端,关闭所有管夹");
                    doGoCloseTOActivity(TreatmentEvaluationActivity.class,"");
                    //确定
                    mCommonDialog1.dismiss();
                })
                .show();
    }

    private boolean isComplete;
    private void earlyFinishTreatmentDialog() {
        String tips = isComplete ? "是否结束治疗" : getResources().getString(R.string.dialog_status_early);
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent(tips)
                .setBtnFirst("确定")
                .setBtnTwo("取消")
                .setFirstClickListener(mCommonDialog -> {
                    if (!isComplete) {

                    } else {
                        doGoCloseTOActivity(TreatmentEvaluationActivity.class,"");
                    }
//                    isComplete = true;
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(Dialog::dismiss)
                .show();

    }

    private CheckForLongPress1 mCheckForLongPress1;
    private volatile boolean isLongPressed = false;
    private class CheckForLongPress1 implements Runnable {

        @Override
        public void run() {
            //5s之后，查看isLongPressed的变量值：
            if (isLongPressed) {//没有做up事件
                Log.e("长按", "5s的事件触发");
                alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
            } else {
                ivLogo.removeCallbacks(mCheckForLongPress1);
            }
        }
    }

    private void alertNumberBoardDialog(String value, String type) {
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
                    }
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
        compositeDisposable.clear();
        reportCompositeDisposable.clear();
        if (downTimer != null) {
            downTimer.cancel();
            isDownTiming = false;
        }
    }

    @Override
    public void notifyByThemeChanged() {

    }

}