package com.emt.pdgo.next.ui.activity;

import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_CHECK_TEMPERATURE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_FAILURE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_FINISH;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_NTPD_RUNNING;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_POOR;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_RESTART;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_RINSE_FAILURE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_RINSE_FINISH;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_RINSE_RUNNING;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_RINSE_START;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_START;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_DRAIN_TPD_RUNNING;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_FINAL_EMPTY_RUNNING;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_FINAL_EMPTY_START;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_PERFUSE_FAILURE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_PERFUSE_FINISH;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_PERFUSE_RUNNING;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_PERFUSE_START;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_PROCESS_FINISH;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_PROCESS_START;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_RETAIN_HIGH_TEMPERATURE_WARNING;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_RETAIN_LOW_TEMPERATURE_WARNING;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_RETAIN_OVER_LIMIT;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_RETAIN_TIME_OUT;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_RETAIN_TIME_OUT_OTHER_FAILURE_UNHANDLE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_RETAIN_TIME_OUT_SELFCHECK_FAILURE_UNHANDLE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_RETAIN_TIME_OUT_SUPPLY_FAILURE_UNHANDLE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_SELFCHECK_FAILURE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_SELFCHECK_FINISH;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_SELFCHECK_START;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_SUPPLY_FAILURE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_SUPPLY_FINISH;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_SUPPLY_RESTART;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_SUPPLY_START;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_TEMPERATURE_ARRIVE;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_TEMPERATURE_SET_FAILED;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_TEMPERATURE_SET_SUCCESS;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_VACCUM_DRAIN_FINISH;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.MSG_VACCUM_DRAIN_START;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.TOPIC_FINISH;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.TOPIC_SUCCESS;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.TOPIC_TREATMENT_ABORT;
import static com.emt.pdgo.next.common.config.CommandReceiveConfig.TOPIC_TREATMENT_FINISH;
import static com.emt.pdgo.next.util.EmtTimeUil.getCurrentTime;
import static com.emt.pdgo.next.util.EmtTimeUil.getYMDHMTime;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.PdGoDbManager;
import com.emt.pdgo.next.data.bean.AapdBean;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.ExpertBean;
import com.emt.pdgo.next.data.bean.KidBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RBean;
import com.emt.pdgo.next.data.bean.ReplayEntity;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.TpdBean;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
import com.emt.pdgo.next.data.entity.TreatmentHistoryBean;
import com.emt.pdgo.next.data.local.TreatmentHistory;
import com.emt.pdgo.next.data.serial.ReceivePublicBean;
import com.emt.pdgo.next.data.serial.ReceivePublicDataBean;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.data.serial.treatment.CheckPerBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveCycleBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveDrainFinishBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveDrainProcessFinishBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveDrainRinseFinishBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveDrainRinseRunningBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveDrainRinseStartBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveDrainRunningBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveDrainStartBean;
import com.emt.pdgo.next.data.serial.treatment.ReceivePerfuseBean;
import com.emt.pdgo.next.data.serial.treatment.ReceivePerfuseFailureBean;
import com.emt.pdgo.next.data.serial.treatment.ReceivePerfuseProcessFinishBean;
import com.emt.pdgo.next.data.serial.treatment.ReceivePerfuseRunningBean;
import com.emt.pdgo.next.data.serial.treatment.ReceivePerfuseStartBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveRetainFinishBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveRetainProcessFinishBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveRetainProcessStartBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveRetainSupplyBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveRetainSupplyFinishBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveSupplyStartBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveVaccumDrainFinishBean;
import com.emt.pdgo.next.data.serial.treatment.ReceiveVaccumDrainStartBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentDrainBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentParamBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentPerfuseBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentPrescriptBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentRetainBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentSupplyBean;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.PdEntity;
import com.emt.pdgo.next.model.mode.CcpdBean;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.upload.TreatmentDataUploadBean;
import com.emt.pdgo.next.net.bean.upload.TreatmentPrescriptionUploadBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.adapter.dpr.ViewPagerAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.dialog.SoundDialog;
import com.emt.pdgo.next.ui.dialog.TreatmentAlarmDialog;
import com.emt.pdgo.next.ui.fragment.TreatmentFragmentItem1;
import com.emt.pdgo.next.ui.fragment.TreatmentFragmentItem2;
import com.emt.pdgo.next.ui.fragment.TreatmentFragmentItem3;
import com.emt.pdgo.next.ui.fragment.apd.prescription.NormalPrescriptionFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.AapdFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.ExpertPrescriptionFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.InfantsPrescriptionFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.TpdPrescriptionFragment;
import com.emt.pdgo.next.ui.mode.fragment.CcpdFragment;
import com.emt.pdgo.next.ui.view.NoScrollViewPager;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.activity
 * @ClassName: TreatmentFragmentActivity
 * @Description: 治疗过程
 * @Author: chenjh
 * @CreateDate: 2019/12/9 10:41 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/9 10:41 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class TreatmentFragmentActivity extends BaseActivity {

    private String TAG = "TreatmentFragmentActivity";

    @BindView(R.id.viewpager)
    NoScrollViewPager mViewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.btn_item1)
    Button btnItem1;
    @BindView(R.id.btn_item2)
    Button btnItem2;
    @BindView(R.id.btn_item3)
    Button btnItem3;

    @BindView(R.id.tv_treatment_status)
    TextView tvTreatmentStatus;

    @BindView(R.id.btn_finish_treatment)
    Button btnFinishTreatment;

    @BindView(R.id.tv_cycle)
    TextView tvPeriod;
    @BindView(R.id.tv_max_cycle)
    TextView tvMaxCycle;
    @BindView(R.id.tv_nit_cycle)
    TextView tvNitCycle;
    @BindView(R.id.tv_cycle_label)
    TextView tvPeriodLabel;

    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_pause)
    ImageView ivPause;
    @BindView(R.id.iv_resume)
    ImageView ivResume;
    @BindView(R.id.tv_pause)
    TextView tvPause;
    @BindView(R.id.layout_pause)
    RelativeLayout layoutPause;

    @BindView(R.id.layout_curr_volume)
    LinearLayout layoutCurrVolume;
    @BindView(R.id.layout_time_and_temp)
    LinearLayout layoutTimeAndTemp;
    @BindView(R.id.layout_curr_time)
    RelativeLayout layoutCurrTime;
    @BindView(R.id.layout_curr_temperature)
    RelativeLayout layoutCurrTemperature;

    @BindView(R.id.ivRetainSupply)
    ImageView ivRetainSupply;

    @BindView(R.id.iv_volume)
    ImageView ivVolume;
    @BindView(R.id.iv_time)
    ImageView ivTime;
    @BindView(R.id.iv_temperature)
    ImageView ivTemperature;
    @BindView(R.id.iv_temp)
    ImageView ivTemp;
    @BindView(R.id.iv_retain_time)
    ImageView ivRetainTime;

    @BindView(R.id.tv_curr_volume_label)
    TextView tvCurrVolumeLabel;
    @BindView(R.id.tv_curr_time_label)
    TextView tvCurrTimeLabel;

    @BindView(R.id.tv_curr_temperature)
    TextView tvCurrTemperature;
    @BindView(R.id.tv_curr_temperature_label)
    TextView tvCurrTemperatureLabel;
    @BindView(R.id.tv_curr_temp_label)
    TextView tvCurrTempLabel;

    @BindView(R.id.ivExtraState)
    ImageView ivExtraState;

    @BindView(R.id.tv_curr_volume_unit)
    TextView tvCurrVolumeUnit;

    @BindView(R.id.tv_curr_volume)
    TextView tvCurrVolume;
    @BindView(R.id.tv_curr_time)
    TextView tvCurrTime;//灌注时间、引流时间

    @BindView(R.id.layout_curr_retain_time)
    LinearLayout layoutCurrRetainTime;//留腹时间布局
    @BindView(R.id.layout_curr_temp)
    LinearLayout layoutCurrTemp;//温度布局：在留腹阶段时候

    @BindView(R.id.tv_curr_temp)
    TextView tvCurrTemp;//当前温度：在留腹阶段时候
    @BindView(R.id.tv_curr_retain_time)
    TextView tvCurrRetainTime;//留腹时间：在留腹阶段时候
    @BindView(R.id.tv_update_temp)
    TextView tvUpdateTime;//修改下一个阶段液袋目标温度值：在留腹阶段时候
    @BindView(R.id.tv_curr_retain_time_label)
    TextView tvCurrRetainTimeLabel;

    @BindView(R.id.cycle_tv_title)
    TextView cycle_tv_title;

    private int perVol = 0; // 灌注量
    private int drainage = 0; // 引流量
    private int retainTime = 0; // 留腹时间
    private int drainTime = 0; // 引流时间
    private int drainRetain = 0; // 预估腹腔剩余液体
    private int perTime = 0; // 灌注时间
    private int flushVolume = 0; // 辅助冲洗量
    private int drainTarget = 0; // 引流目标值
    private int perRetain = 0; // 灌注后留腹量

    private String  cycles ="";
    private String inFlows="";
    private String inFlowTime="";
    private String leaveWombTime="";
    private String exhaustTime="";
    private String drainages="";

    private String auxiliaryFlushingVolume="";
    private String abdominalVolumeAfterInflow = "";
    private String drainageTargetValue = "";
    private String estimatedResidualAbdominalFluid = "";

    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.layout_left)
    LinearLayout layoutLeft;
    @BindView(R.id.layout_fragment)
    LinearLayout layoutFragment;

    @BindView(R.id.layout_skip_or_pause)
    RelativeLayout layoutSkipOrPause;

    private TreatmentFragmentItem2 fragmentItem2;
    private TreatmentFragmentItem3 mFragment3;

    private TreatmentFragmentItem1 fragmentItem1;

    private int mCurrentPosition = 0;
    private int mCurrentPosition2 = 0;

    private CompositeDisposable mCompositeDisposable;
    private CompositeDisposable reportCompositeDisposable;
    private Disposable countDownDisposable;
    private Disposable lastDrainDisposable;

    private CompositeDisposable compositeDisposable;

    private CompositeDisposable cmdDisposable;

    private String command;

    @BindView(R.id.tvTotalUltVol)
    TextView tvTotalUltVol;
    @BindView(R.id.totalUrlRl)
    RelativeLayout totalUrlRl;
    @BindView(R.id.tvTotalUrl)
    TextView tvTotalUrl;
    @BindView(R.id.ivTotalUrl)
    ImageView ivTotalUrl;
    @BindView(R.id.totalUrlUnitTv)
    TextView totalUrlUnitTv;

    @BindView(R.id.dormancyBtn)
    StateButton dormancyBtn;

    /**
     * 当前周期序数
     */
    public int currCycle = 0;

    /**
     * 最大的周期数（ 0周期；1首次灌注；2-n+1；n+2）
     */
    public int maxCycle = 0;

    public int startMaxCycle; // 最开始最大周期数

    public int currentStage = 3;//当前治疗阶段：1灌注、2留腹、3引流

    private int currDrainTime;      //当前引流计时 秒
    private int currPerfusionTime;  //当前灌注计时 秒
    private int currRetainTime;    //当前留腹倒计时 秒
    private int currAlarmTimeInterval;    //当前留腹倒计时 秒

    public int dialysateInitialValue;//透析液初始值
    public int lowFirstValue;

    private boolean isPause = false;//是否暂停

    public boolean isComplete = false;//是否完成治疗

    private boolean isShowAlarmDialog = false;//是否故障弹窗中

    private boolean isShowExceedsMaxValueDialog = false;//是否灌注量超过警戒值弹窗中

    private boolean isDrainManualEmptying = false;//是否最末引流排空等待中

    // aApd
    public AapdBean aapdBean;
    // 儿童模式
    public KidBean kidBean;
    // 专家
    public ExpertBean expertBean;
    // 常规
    public IpdBean entity;//治疗参数
    public TreatmentParameterEniity eniity;
    public TpdBean tpdBean;
    public CcpdBean ccpdBean;
    public DrainParameterBean drainParameterBean;
    public PerfusionParameterBean perfusionParameterBean;
    public SupplyParameterBean supplyParameterBean;
    public RetainParamBean retainParamBean;

    private String uuid = "";

    public static List<TreatmentHistoryBean> mTreatmentDataList  = new ArrayList<>();

    private TreatmentHistoryBean mHistoryBean;

    private List<PdEntity.PdInfoEntity> pdInfoEntities;

    public Gson myGson;

    private boolean isTiming = false;

    private TreatmentAlarmDialog powerFaultDialog;
    private TreatmentAlarmDialog powerOffDialog;

    private TreatmentAlarmDialog lowTemperatureDialog;
    private TreatmentAlarmDialog highTemperatureDialog;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    private CheckForLongPress1 mCheckForLongPress1;
    private volatile boolean isLongPressed = false;

    @BindView(R.id.moreData)
    StateButton moreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }
    private MonitorTouchCountDownTimer downTimer;

    @Override
    public void initAllViews() {
        APIServiceManage.getInstance().postApdCode("T0066");
        setContentView(R.layout.activity_fragment_treatment_main);
        ButterKnife.bind(this);
        mCheckForLongPress1 = new CheckForLongPress1();
        downTimer = new MonitorTouchCountDownTimer((long) PdproHelper.getInstance().autoSleep() * 60 * 1000, 1000);
        downTimer.start();
        myGson = new Gson();
        currentPower.setTextColor(Color.WHITE);
        drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();
        retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        pdInfoEntities = new ArrayList<>();
//        initTime();
        ActivityManager.getActivityManager().removeAllActivityExceptOne(TreatmentFragmentActivity.class);
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
    private final int maxCountdown = 2 * 60;//倒计时
    private int currCountdown = 0;//倒计时
    private CompositeDisposable soundCompositeDisposable;
    private Disposable disposable;
    private SoundDialog soundDialog;
    @SuppressLint("ClickableViewAccessibility")
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
        btnSound.setOnClickListener(view -> {
            if (soundDialog == null) {
                soundDialog = new SoundDialog(this);
            }
            soundDialog.closeClick(Dialog::dismiss)
                    .tsClick(pdproHelper -> {
                        PdproHelper.getInstance().updateTtsSoundOpen(!pdproHelper.getTtsSoundOpen());
                    })
                    .ringClick(soundDialog12 -> {
                        erasure();
                    }).show();
        });
        currentPower.setText(MyApplication.batteryLevel+"");
        sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
        compositeDisposable = new CompositeDisposable();
//        initTime();
        btnFinishTreatment.setOnLongClickListener(view -> {
            if (isFastClick()) {
                return true;
            }
            if (!isComplete) {
                earlyFinishTreatmentDialog();
            } else {
//                    doGoTOActivity(DataCollectionActivity.class);
                doGoCloseTOActivity(TreatmentEvaluationActivity.class,"");
            }
            return true;
        });
        dormancyBtn.setOnClickListener(v -> {
            simulateKey(26);
            sendToMainBoard(CommandDataHelper.getInstance().LedOpen("all",false,1,0));
        });
        tvCurrTemperature.setOnLongClickListener(view -> {
            alertNumberBoardDialog(String.valueOf(MyApplication.mTargetTemper), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE, true);
            return true;
        });
        dormancyBtn.setOnLongClickListener(view -> {
            alertNumberBoardDialog("", PdGoConstConfig.AUTO_SLEEP);
            return true;
        });
        moreData.setOnLongClickListener(view -> {
            switchFragment(0);
            showFragment(true);
            return true;
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
    }
    private ViewPagerAdapter pagerAdapter;
    private AapdFragment aapdFragment;


    @Override
    public void initViewData() {

        uuid = UUID.randomUUID().toString().replaceAll("-", "");
        MyApplication.treatmentRunning = true;
        MyApplication.apdTreat = true;
        mCompositeDisposable = new CompositeDisposable();
        reportCompositeDisposable = new CompositeDisposable();
        cmdDisposable = new CompositeDisposable();
        eniity = PdproHelper.getInstance().getTreatmentParameter();
        init();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentItem2 = new TreatmentFragmentItem2();
        fragmentList.add(fragmentItem2);
        // 多模式
        switch (MyApplication.apdMode) {
            case 1:
                entity = PdproHelper.getInstance().ipdBean();
                NormalPrescriptionFragment normalPrescriptionFragment = new NormalPrescriptionFragment();
                cycle_tv_title.setText("Ipd模式治疗中");
                fragmentList.add(normalPrescriptionFragment);
                break;
            case 2:
                tpdBean = PdproHelper.getInstance().tpdBean();
                TpdPrescriptionFragment tpdPrescriptionFragment = new TpdPrescriptionFragment();
                cycle_tv_title.setText("TPD模式治疗中");
                fragmentList.add(tpdPrescriptionFragment);
                break;
            case 3:
                ccpdBean = PdproHelper.getInstance().ccpdBean();
                CcpdFragment ccpdFragment = new CcpdFragment();
                cycle_tv_title.setText("CCPD模式治疗中");
                fragmentList.add(ccpdFragment);
                break;
            case 4:
                aapdBean = PdproHelper.getInstance().aapdBean();
                aapdFragment = new AapdFragment();
                cycle_tv_title.setText("aAPD模式治疗中");
                fragmentList.add(aapdFragment);
                break;
            case 7:
                kidBean = PdproHelper.getInstance().kidBean();
                InfantsPrescriptionFragment infantsPrescriptionFragment = new InfantsPrescriptionFragment();
                cycle_tv_title.setText("儿童模式治疗中");
                fragmentList.add(infantsPrescriptionFragment);
                break;
            case 8:
                expertBean = PdproHelper.getInstance().expertBean();
                ExpertPrescriptionFragment expertPrescriptionFragment = new ExpertPrescriptionFragment();
                cycle_tv_title.setText("专家模式治疗中");
                fragmentList.add(expertPrescriptionFragment);
                break;
        }
        // 单模式
//        fragmentItem1 = new TreatmentFragmentItem1();
//        fragmentItem2 = new TreatmentFragmentItem2();
        mFragment3 = new TreatmentFragmentItem3();
        fragmentList.add(mFragment3);
        List<String> titles = new ArrayList<>();
        titles.add("治疗周期");
        titles.add("处方修改");
        titles.add("治疗数据");
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList, titles);
        mViewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        MyApplication.mStartTime = getYMDHMTime(getCurrentTime());
        initTreatmentHistory();
        // 单模式
//        startTreat();
        // 多模式
        deliveryTherapy();
//        startTreat(MyApplication.isDebug ? 1 : 0);

    }

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
                int month = calendar.get(Calendar.MONTH) + 1;
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
                } else if (mType.equals(PdGoConstConfig.AUTO_SLEEP)) {
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.AUTO_SLEEP, result);
                    if (downTimer != null) {
                        downTimer.cancel();
                    }
                    downTimer = new MonitorTouchCountDownTimer((long) PdproHelper.getInstance().autoSleep() * 60 * 1000, 1000);
                    downTimer.start();
                }
            }
        });
    }

    public class MonitorTouchCountDownTimer extends CountDownTimer {

        public MonitorTouchCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
//            Log.e("TouchCountDownTimer", ""+ l/1000+"s");
        }

        @Override
        public void onFinish() {
            simulateKey(26);
            sendToMainBoard(CommandDataHelper.getInstance().LedOpen("all",false,1,0));
        }
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

    private int totalDrainVolume;    //累计引流量
    /***** 总灌注量  灌注量  即治疗量  *******/
    private int totalPerfusionVolume;//累计灌注量
    /**** 总超滤量 = 引流量-治疗量 = 总引流量-总灌注量 ***/
    private int mUltrafiltrationVolume;
    private int modifyCycle;
    public void initTreatmentHistory() {
        // 多模式
        switch (MyApplication.apdMode) {
            case 1:
                IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                if (ipdBean.abdomenRetainingVolumeFinally > 0) {
                    maxCycle = ipdBean.cycle + 1;
                } else {
                    maxCycle = ipdBean.cycle;
                }
//                maxCycle = ipdBean.cycle;
                break;
            case 2:
                TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                if (tpdBean.abdomenRetainingVolumeFinally > 0) {
                    maxCycle = tpdBean.cycle + 1;
                } else {
                    maxCycle = tpdBean.cycle;
                }
                break;
            case 3:
                CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                if (ccpdBean.abdomenRetainingVolumeFinally > 0) {
                    maxCycle = ccpdBean.cycle + 1;
                } else {
                    maxCycle = ccpdBean.cycle;
                }
                break;
            case 4:
                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
                if (aapdBean.finalVol == 0) {
                    maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6;
                } else {
                    maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6 + 1;
                }
                break;
            case 7:
                KidBean kidBean = PdproHelper.getInstance().kidBean();
                if (kidBean.abdomenRetainingVolumeFinally > 0) {
                    maxCycle = kidBean.cycle + 1;
                } else {
                    maxCycle = kidBean.cycle;
                }
                break;
            case 8:
                ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                if (expertBean.cycleMyself) {
                    maxCycle = Collections.max(expertBean.baseSupplyCycle)
                            >Collections.max(expertBean.osmSupplyCycle) ? Collections.max(expertBean.baseSupplyCycle) :
                            Collections.max(expertBean.osmSupplyCycle);
                } else {
                    if (expertBean.finalRetainVol > 0) {
                        maxCycle = expertBean.cycle + 1;
                    } else {
                        maxCycle = expertBean.cycle;
                    }
                }
//                if (expertBean.finalRetainVol > 0) {
//                    maxCycle = expertBean.cycle + 1;
//                } else {
//                    maxCycle = expertBean.cycle;
//                }
                break;
        }

        // 单模式
//        if (fragmentItem1.getActivity() != null) {
//            if (fragmentItem1.finalVol > 0) {
//                maxCycle = fragmentItem1.treatCycle + 1;
//            } else {
//                maxCycle = fragmentItem1.treatCycle;
//            }
//            if (eniity.firstPerfusionVolume > 0 && !isSum) {
//                treatedVol = treatedVol + eniity.firstPerfusionVolume+
//                        (currCycle - treatedCycle - 1) * fragmentItem1.perVol;
//                isSum = true;
//            } else {
//                // 500+500
//                treatedVol = treatedVol + (currCycle - treatedCycle) * fragmentItem1.perVol;
//            }
//            treatedCycle = currCycle;
//        }
        tvMaxCycle.setText(String.valueOf(maxCycle));
        if (mFragment3.getActivity() != null) {
            mFragment3.setMaxCycle(maxCycle);
        }
        tvMaxCycle.setText(String.valueOf(maxCycle));
        modifyCycle = maxCycle;
        for (int i = 0; i <= maxCycle; i++) {
            TreatmentHistoryBean mBean = new TreatmentHistoryBean();
            mBean.uuid = uuid;
            mBean.cycle = i;
            mBean.sn = PdproHelper.getInstance().getMachineSN();
            mBean.drainTargetVolume = drainTarget;
            mBean.waitingTime = retainTime;
            mBean.perfusionVolume = perVol;
            mBean.perfusionTime = perTime;
            mBean.waitingVolume = perRetain;
            mBean.drainVolume = drainage;
            mBean.drainTime = drainTime;
            mBean.predictedResidualLiquidVolume = drainRetain;
            mBean.rinsePerfusionVolume = flushVolume;
            mTreatmentDataList.add(mBean);
        }
    }

//    private void modify() {
//        maxCycle = MyApplication.isKid ? kidBean.cycle : treatmentParameterEniity.cycle ;
////        if (treatmentParameterEniity.firstPerfusionVolume > 0) {
////            maxCycle = maxCycle + 1;
////        }
//        if (MyApplication.isKid) {
//            if (kidBean.abdomenRetainingVolumeFinally > 0) {
//                maxCycle = maxCycle + 1;
//            }
//        } else {
//            if (treatmentParameterEniity.abdomenRetainingVolumeFinally > 0) {
//                maxCycle = maxCycle + 1;
//            }
//        }
//        tvMaxCycle.setText(String.valueOf(maxCycle));
//        if (mFragment3.getActivity() != null) {
//            mFragment3.setMaxCycle(maxCycle);
//        }
//        Log.e("治疗界面","修改后最大周期数"+maxCycle+"--最开始最大周期数--"+modifyCycle);
//        if (maxCycle > modifyCycle) {
//            try {
//                for (int i = modifyCycle+1; i < maxCycle+2; i++) {
//                    TreatmentHistoryBean mBean = new TreatmentHistoryBean();
//                    mBean.uuid = uuid;
//                    mBean.cycle = i;
//                    mBean.sn = PdproHelper.getInstance().getMachineSN();
//                    mBean.drainTargetVolume = drainTarget;
//                    mBean.waitingTime = retainTime;
//                    mBean.perfusionVolume = perVol;
//                    mBean.perfusionTime = perTime;
//                    mBean.waitingVolume = perRetain;
//                    mBean.drainVolume = drainage;
//                    mBean.drainTime = drainTime;
//                    mBean.predictedResidualLiquidVolume = drainRetain;
//                    mBean.rinsePerfusionVolume = flushVolume;
//                    mTreatmentDataList.add(mBean);
//                }
//                modifyCycle = maxCycle;
//            } catch (Exception e) {
//                saveFaultCodeLocal("修改处方--"+e.getMessage());
//            }
//        }
//    }

    private int endTime;
    private void getTreatmentDataUpload() {
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

        mTreatmentPrescription.totalPerfusionVolume = totalPerfusionVolume;
//        mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume;
        mTreatmentPrescription.totalUltrafiltrationVolume = mUltrafiltrationVolume;
        mTreatmentPrescription.peritonealDialysisFluidTotal = totalDrainVolume;
        mTreatmentPrescription.periodicities = maxCycle;

        switch (MyApplication.apdMode) {
            case 1:
                IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                mTreatmentPrescription.firstPerfusionVolume =  ipdBean.firstPerfusionVolume;
                mTreatmentPrescription.perCyclePerfusionVolume = ipdBean.perCyclePerfusionVolume;
                mTreatmentPrescription.abdomenRetainingVolumeFinally = ipdBean.abdomenRetainingVolumeFinally;
                mTreatmentPrescription.abdomenRetainingTime = ipdBean.abdomenRetainingTime;
                mTreatmentPrescription.abdomenRetainingVolumeLastTime = ipdBean.abdomenRetainingVolumeLastTime;
                mTreatmentPrescription.ultrafiltrationVolume = ipdBean.ultrafiltrationVolume;
                mTreatmentDataUpload.ultrafiltration = ipdBean.ultrafiltrationVolume;
                mTreatmentDataUpload.lastLeave = ipdBean.abdomenRetainingVolumeFinally;
                mTreatmentDataUpload.times = ipdBean.cycle;
                mTreatmentDataUpload.totalInjectAmount = ipdBean.peritonealDialysisFluidTotal;
                break;
            case 2:
                TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                mTreatmentPrescription.firstPerfusionVolume =  tpdBean.firstPerfusionVolume;
                mTreatmentPrescription.perCyclePerfusionVolume = tpdBean.perCyclePerfusionVolume;
                mTreatmentPrescription.abdomenRetainingVolumeFinally = tpdBean.abdomenRetainingVolumeFinally;
                mTreatmentPrescription.abdomenRetainingTime = tpdBean.abdomenRetainingTime;
                mTreatmentPrescription.abdomenRetainingVolumeLastTime = tpdBean.abdomenRetainingVolumeLastTime;
                mTreatmentPrescription.ultrafiltrationVolume = tpdBean.ultrafiltrationVolume;
                mTreatmentDataUpload.ultrafiltration = tpdBean.ultrafiltrationVolume;
                mTreatmentDataUpload.lastLeave = tpdBean.abdomenRetainingVolumeFinally;
                mTreatmentDataUpload.times = tpdBean.cycle;
                mTreatmentDataUpload.totalInjectAmount = tpdBean.peritonealDialysisFluidTotal;
                break;
            case 3:
                CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                mTreatmentPrescription.firstPerfusionVolume =  ccpdBean.firstPerfusionVolume;
                mTreatmentPrescription.perCyclePerfusionVolume = ccpdBean.perCyclePerfusionVolume;
                mTreatmentPrescription.abdomenRetainingVolumeFinally = ccpdBean.abdomenRetainingVolumeFinally;
                mTreatmentPrescription.abdomenRetainingTime = ccpdBean.abdomenRetainingTime;
                mTreatmentPrescription.abdomenRetainingVolumeLastTime = ccpdBean.abdomenRetainingVolumeLastTime;
                mTreatmentPrescription.ultrafiltrationVolume = ccpdBean.ultrafiltrationVolume;
                mTreatmentDataUpload.ultrafiltration = ccpdBean.ultrafiltrationVolume;
                mTreatmentDataUpload.lastLeave = ccpdBean.abdomenRetainingVolumeFinally;
                mTreatmentDataUpload.times = ccpdBean.cycle;
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
                mTreatmentPrescription.firstPerfusionVolume =  kidBean.firstPerfusionVolume;
                mTreatmentPrescription.perCyclePerfusionVolume = kidBean.perCyclePerfusionVolume;
                mTreatmentPrescription.abdomenRetainingVolumeFinally = kidBean.abdomenRetainingVolumeFinally;
                mTreatmentPrescription.abdomenRetainingTime = kidBean.abdomenRetainingTime;
                mTreatmentPrescription.abdomenRetainingVolumeLastTime = kidBean.abdomenRetainingVolumeLastTime;
                mTreatmentPrescription.ultrafiltrationVolume = kidBean.ultrafiltrationVolume;
                mTreatmentDataUpload.ultrafiltration = kidBean.ultrafiltrationVolume;
                mTreatmentDataUpload.lastLeave = kidBean.abdomenRetainingVolumeFinally;
                mTreatmentDataUpload.times = kidBean.cycle;
                mTreatmentDataUpload.totalInjectAmount = kidBean.peritonealDialysisFluidTotal;
                break;
//            case 8:
//                break;
            default:
                TreatmentParameterEniity entity = PdproHelper.getInstance().getTreatmentParameter();
                mTreatmentPrescription.firstPerfusionVolume =  entity.firstPerfusionVolume;
                mTreatmentPrescription.perCyclePerfusionVolume = entity.perCyclePerfusionVolume;
                mTreatmentPrescription.abdomenRetainingVolumeFinally = entity.abdomenRetainingVolumeFinally;
                mTreatmentPrescription.abdomenRetainingTime = entity.abdomenRetainingTime;
                mTreatmentPrescription.abdomenRetainingVolumeLastTime = entity.abdomenRetainingVolumeLastTime;
                mTreatmentPrescription.ultrafiltrationVolume = entity.ultrafiltrationVolume;
                mTreatmentDataUpload.ultrafiltration = entity.ultrafiltrationVolume;
                mTreatmentDataUpload.lastLeave = entity.abdomenRetainingVolumeFinally;
                mTreatmentDataUpload.times = entity.cycle;
                mTreatmentDataUpload.totalInjectAmount = entity.peritonealDialysisFluidTotal;
                break;
        }
        mTreatmentDataUpload.lastLeaveTime =  endTime;
        mTreatmentPrescription.equipmentUseTime = 0;
        mTreatmentPrescription.upWeightInitialValue = 0;
        mTreatmentPrescription.lowWeightInitialValue = 0;

        mTreatmentPrescription.plcId = "";//
        mTreatmentPrescription.buildId = 0;
        mTreatmentPrescription.buildValue = "";//1.2.1.1

        mTreatmentDataUpload.machineSN = PdproHelper.getInstance().getMachineSN();
        mTreatmentDataUpload.startTime = MyApplication.mStartTime;
        mTreatmentDataUpload.endTime = MyApplication.mEndTime;
        mTreatmentDataUpload.drainageTime = firstDrainTime;

        mTreatmentDataUpload.cycle = cycles;
        mTreatmentDataUpload.inFlow = inFlows;
        mTreatmentDataUpload.inFlowTime = inFlowTime;
        mTreatmentDataUpload.leaveWombTime = leaveWombTime;
        mTreatmentDataUpload.exhaustTime = exhaustTime;
        mTreatmentDataUpload.drainage = drainages;

        mTreatmentDataUpload.auxiliaryFlushingVolume = auxiliaryFlushingVolume;
        mTreatmentDataUpload.abdominalVolumeAfterInflow = abdominalVolumeAfterInflow;
        mTreatmentDataUpload.drainageTargetValue = drainageTargetValue;
        mTreatmentDataUpload.estimatedResidualAbdominalFluid = estimatedResidualAbdominalFluid;
        mTreatmentDataUpload.treatmentPrescriptionUploadVo = mTreatmentPrescription;

        setReplayEntity();
    }

    private void setReplayEntity() {
        ReplayEntity entity = PdproHelper.getInstance().replayEntity();
        entity.startTime = MyApplication.mStartTime;
        entity.endTime = EmtTimeUil.getTime();
        entity.drainageTime = firstDrainTime;
        entity.maxCycle = maxCycle;
        entity.totalPerfusionVolume = totalPerfusionVolume;
        entity.totalDrainVolume = totalDrainVolume;
        entity.totalUltrafiltrationVolume = mUltrafiltrationVolume;
        entity.cycle =  cycles;
        entity.inFlow = inFlows;
        entity.inFlowTime = inFlowTime;
        entity.leaveWombTime = leaveWombTime;
        entity.exhaustTime = exhaustTime;
        entity.drainage = drainages;
        entity.auxiliaryFlushingVolume = auxiliaryFlushingVolume;
        entity.abdominalVolumeAfterInflow = abdominalVolumeAfterInflow;
        entity.drainageTargetValue = drainageTargetValue;
        entity.estimatedResidualAbdominalFluid = estimatedResidualAbdominalFluid;
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.REPLAY_ENTITY, entity);
    }

    /**
     * 发送: 治疗指令
     */
    private void startTreat(int debug) {
        sendCommandInterval(CommandDataHelper.getInstance().startTreatment(0,debug),500);
    }

    private void modifyParma() {
        TreatmentParameterEniity mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
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
        prescript.apdmodify = 1;
        mSerialRequestBean.method = "apdpresci/config";
        mSerialRequestBean.params = prescript;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        sendToMainBoard(new Gson().toJson(mRequestBean));
    }

    private void modifyConfig() {
        PerfusionParameterBean perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        DrainParameterBean drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        SupplyParameterBean supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();
        RetainParamBean retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialTreatmentParamBean mParamBean = new SerialTreatmentParamBean();

        SerialTreatmentDrainBean drain = new SerialTreatmentDrainBean();
        drain.drainFlowRate = drainParameterBean.drainThresholdValue;//引流流速阈值
        drain.drainFlowPeriod = drainParameterBean.drainTimeInterval;//引流流速周期 秒
        drain.drainRinseVolume = drainParameterBean.drainRinseVolume;//引流冲洗量
        drain.drainRinseTimes = drainParameterBean.drainRinseNumber;//引流次数
        drain.firstDrainPassRate = drainParameterBean.drainZeroCyclePercentage;//0周期引流合格率
        drain.drainPassRate = drainParameterBean.drainOtherCyclePercentage;//周期引流合格率
        drain.isFinalDrainEmpty = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流是否排空
        drain.isFinalDrainEmptyWait = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流排空是否等待
        drain.finalDrainEmptyWaitTime = drainParameterBean.alarmTimeInterval;//最末引流排空等待时间 30
        drain.isVaccumDrain = drainParameterBean.isNegpreDrain ? 1 : 0;//是否开启负压引流

        SerialTreatmentPerfuseBean perfuse = new SerialTreatmentPerfuseBean();
        perfuse.perfuseFlowRate = perfusionParameterBean.perfThresholdValue;//灌注流速阈值
        perfuse.perfuseFlowPeriod = perfusionParameterBean.perfTimeInterval;//灌注流速周期 秒
        perfuse.perfuseMaxVolume = perfusionParameterBean.perfMaxWarningValue;//最大灌注量

        SerialTreatmentSupplyBean supply = new SerialTreatmentSupplyBean();
        supply.supplyFlowRate = supplyParameterBean.supplyThresholdValue;//补液流速阈值
        supply.supplyFlowPeriod = supplyParameterBean.supplyTimeInterval;//补液流速周期 秒
        supply.supplyProtectVolume = supplyParameterBean.supplyTargetProtectionValue;//补液目标保护值
        supply.supplyMinVolume = supplyParameterBean.supplyTargetProtectionValue;//启动补液的加热袋重量最低值

        SerialTreatmentRetainBean retain = new SerialTreatmentRetainBean();
        retain.isFinalRetainDeduct = retainParamBean.isAbdomenRetainingDeduct ? 1 : 0;//是否末次流量扣除
        retain.isFiltCycleOnly = retainParamBean.isZeroCycleUltrafiltration ? 1 : 0;// 1 是否只统计循环周期的超滤量
        retain.parammodify = 1;

        mParamBean.drain = drain;
        mParamBean.perfuse = perfuse;
        mParamBean.supply = supply;
        mParamBean.retain = retain;

        mSerialRequestBean.method = "treatparam/config";
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        String sendData = new Gson().toJson(mRequestBean);
        sendToMainBoard(sendData);
    }

    /**
     * 电源管理: 1、AC220V断开
     *
     * @param mSerialJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_AC_POWER_OFF)
    public void receiveAcPowerOff(String mSerialJson) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showPowerOffDialog();
            }
        });

    }

    /**
     * 电源管理: 2、AC220V连接
     *
     * @param mSerialJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_AC_POWER_ON)
    public void receiveAcPowerOn(String mSerialJson) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buzzerOff();
                //30分钟内来电，断电提醒弹窗还在，关闭弹窗
                if (powerOffDialog != null && powerOffDialog.isShowing()) {
                    powerOffDialog.dismiss();
                    powerOffDialog = null;
                }
                APIServiceManage.getInstance().postApdCode("E0013");
                speak("电源已恢复");
//                saveFaultCodeLocal("电源连接");
            }
        });

    }

    /**
     * 电源管理: 3、AC掉电超过30分钟（未能恢复市电供电），触发声光报警
     *
     * @param mSerialJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_AC_POWER_FAULT)
    public void receiveAcPowerFault(String mSerialJson) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showPowerFaultDialog();
//                saveFaultCodeLocal("掉电30分钟");
            }
        });

    }

    /**
     * 阀门故障
     */
    private void valueFaultDialog(String msg) {
        buzzer();

//        if (currCycle < 10) {
//            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"29");
//        } else {
//            APIServiceManage.getInstance().postApdCode("T" + currCycle + "29");
//        }
        speak(msg);
        isShowAlarmDialog = true;
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(msg)
                .setBtnFirst("长按跳过当前治疗", R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    buzzerOff();
                    isShowAlarmDialog = false;
                    if (currCycle < 10) {
                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"26");
                    } else {
                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "26");
                    }
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"26");
                    mCommonDialog13.dismiss();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(1);
                    buzzerOff();
                })
                .show();

    }

    private boolean isInitial ;
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {

            if (mReceiveDeviceBean != null) {
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
                tvCurrTemperature.setText(getTemp(mReceiveDeviceBean.temp) + "℃");
                tvCurrTemp.setText(getTemp(mReceiveDeviceBean.temp) + "℃");
                if (mFragment3.getActivity() != null) {
//                    mFragment3 = new TreatmentFragmentItem3();
//                    Log.e("mFragment3",""+mFragment3.currCycle);
                    mFragment3.setDeviceStatusInfo(mReceiveDeviceBean);
                }
                if (!isInitial) {
                    isInitial = true;
                    dialysateInitialValue = mReceiveDeviceBean.upper;
                    lowFirstValue = mReceiveDeviceBean.lower;
                }

//                if (mReceiveDeviceBean.vaccum.equals("00") || mReceiveDeviceBean.vaccum.equals("80")) {
////            tvValveNegpreDrain.setTextColor(Color.YELLOW);
////                    tvValveNegpreDrain.setBackgroundResource(R.color.orange);
//                } else if (mReceiveDeviceBean.vaccum.equals("01") || mReceiveDeviceBean.vaccum.equals("81")) {
////            tvValveNegpreDrain.setTextColor(Color.GREEN);
////                    tvValveNegpreDrain.setBackgroundResource(R.color.blue);
//                } else {
////            tvValveNegpreDrain.setTextColor(Color.RED);
////                    tvValveNegpreDrain.setBackgroundResource(R.color.red);
//                    runOnUiThread(()-> {
////                        if (!isShowAlarmDialog) valueFaultDialog("负压引流阀故障");
//                    });
//                }
////        Log.e("详细状态","vaccum--"+mReceiveDeviceBean.vaccum+
////                "supply"+mReceiveDeviceBean.supply);
//                if (mReceiveDeviceBean.supply1.equals("00") || mReceiveDeviceBean.supply1.equals("80")) {
////            tvValveSupply.setTextColor(Color.YELLOW);
////            tvValveSupply.setTextColor(Color.YELLOW);
////                    tvValveSupply.setBackgroundResource(R.color.orange);
//                } else if (mReceiveDeviceBean.supply1.equals("01") || mReceiveDeviceBean.supply1.equals("81")) {
////            tvValveSupply.setTextColor(Color.GREEN);
////                    tvValveSupply.setBackgroundResource(R.color.blue);
//                } else {
////            tvValveSupply.setTextColor(Color.RED);
////                    tvValveSupply.setBackgroundResource(R.color.red);
//                    runOnUiThread(()-> {
////                        if (!isShowAlarmDialog) valueFaultDialog("补液阀故障");
//                    });
//                }
//                if (mReceiveDeviceBean.perfuse.equals("00") || mReceiveDeviceBean.perfuse.equals("80")) {
////            tvValvePerfusion.setTextColor(Color.YELLOW);
////                    tvValvePerfusion.setBackgroundResource(R.color.orange);
//                } else if (mReceiveDeviceBean.perfuse.equals("01") || mReceiveDeviceBean.perfuse.equals("81")) {
////            tvValvePerfusion.setTextColor(Color.GREEN);
////                    tvValvePerfusion.setBackgroundResource(R.color.blue);
//                } else {
////            tvValvePerfusion.setTextColor(Color.RED);
////                    tvValvePerfusion.setBackgroundResource(R.color.red);
//                    runOnUiThread(()-> {
////                        if (!isShowAlarmDialog) valueFaultDialog("灌注阀故障");
//                    });
//                }
//                if (mReceiveDeviceBean.drain.equals("00") || mReceiveDeviceBean.drain.equals("80")) {
////            tvValveDrain.setTextColor(Color.YELLOW);
////                    tvValveDrain.setBackgroundResource(R.color.orange);
//                } else if (mReceiveDeviceBean.drain.equals("01") || mReceiveDeviceBean.drain.equals("81")) {
////            tvValveDrain.setTextColor(Color.GREEN);
////                    tvValveDrain.setBackgroundResource(R.color.blue);
//                } else {
////            tvValveDrain.setTextColor(Color.RED);
////                    tvValveDrain.setBackgroundResource(R.color.red);
//                    runOnUiThread(()-> {
//                        if (!isShowAlarmDialog) valueFaultDialog("引流阀故障");
//                    });
//                }
//                if (mReceiveDeviceBean.safe.equals("00") || mReceiveDeviceBean.safe.equals("80")) {
////            tvValveSafe.setTextColor(Color.YELLOW);
////                    tvValveSafe.setBackgroundResource(R.color.orange);
//                } else if (mReceiveDeviceBean.safe.equals("01") || mReceiveDeviceBean.safe.equals("81")) {
////            tvValveSafe.setTextColor(Color.GREEN);
////                    tvValveSafe.setBackgroundResource(R.color.blue);
//                } else {
////                    tvValveSafe.setBackgroundResource(R.color.red);
////            tvValveSafe.setTextColor(Color.RED);
//                    runOnUiThread(()-> {
//                        if (!isShowAlarmDialog) valueFaultDialog("安全阀故障");
//                    });
//                }
//                if (mReceiveDeviceBean.supply2.equals("00") || mReceiveDeviceBean.supply2.equals("80")) {
////            tvValveSupply2.setTextColor(Color.YELLOW);
////                    tvValveSupply2.setBackgroundResource(R.color.orange);
//                } else if (mReceiveDeviceBean.supply2.equals("01") || mReceiveDeviceBean.supply2.equals("81")) {
////            tvValveSupply2.setTextColor(Color.GREEN);
////                    tvValveSupply2.setBackgroundResource(R.color.blue);
//                } else {
////            tvValveSupply2.setTextColor(Color.RED);
////                    tvValveSupply2.setBackgroundResource(R.color.red);
//                    runOnUiThread(()-> {
//                        if (!isShowAlarmDialog) valueFaultDialog("末袋补液阀故障");
//                    });
//                }
            }
        });
    }
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_DEVICE_INFO)
    public void receiveDeviceInfo(String mSerialJson) {
        ReceiveDeviceBean mBean = JsonHelper.jsonToClass(mSerialJson, ReceiveDeviceBean.class);
        Log.e("治疗界面", "   --->接收设备信息：" + mSerialJson);
        runOnUiThread(() -> {
            if (mBean != null) {
                tvCurrTemperature.setText(getTemp(mBean.temp) + "℃");
                tvCurrTemp.setText(getTemp(mBean.temp) + "℃");
                if (mFragment3.getActivity() != null) {
                    mFragment3.setDeviceStatusInfo(mBean);
                }
            }
        });
    }

    private final BigDecimal mTen = new BigDecimal(10);

    private float getTemp(int temp) {
//        temp = temp-5;
        BigDecimal mTemp = new BigDecimal(temp);
        BigDecimal newTemp = mTemp.divide(mTen, 1, RoundingMode.HALF_UP);
        return newTemp.floatValue();
    }
    private TreatmentDataUploadBean mTreatmentDataUpload;


    /**
     * 处理治疗数据
     *
     * @param mSerialJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_TREATMENT_DATA)
    public void receiveTreatmentDData(String mSerialJson) {
        Log.e("治疗界面", "   处理治疗数据：" + mSerialJson);
        //{"data":{},"msg":"abort","topic":"treatment"}
        ReceivePublicBean mBean = JsonHelper.jsonToClass(mSerialJson, ReceivePublicBean.class);
        if (mBean.msg.contains(TOPIC_FINISH)
                || mBean.msg.contains(TOPIC_TREATMENT_FINISH)
                || mBean.msg.contains(TOPIC_SUCCESS)) {//完成治疗
            MyApplication.currCmd = "";
            speak("完成治疗");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    btnFinishTreatment.setVisibility(View.INVISIBLE);
                    layoutSkipOrPause.setVisibility(View.INVISIBLE);
                    initDb();
                    showCompleteDialog();
                }
            });
            stopCmdLoopTimer();
            stopLoopCountDown();
            stopLoopTimer();
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"08");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    initDb();
//                }
//            }).start();

        }

    }

    private void initDb() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        MyApplication.ApdTreatTime = EmtTimeUil.getTime(time);
        if (mTreatmentDataList.size() > 0) {
            for (int i = 0; i <= currCycle; i++) {
                TreatmentHistoryBean mBean = mTreatmentDataList.get(i);
                totalDrainVolume += mBean.drainVolume;
                totalPerfusionVolume += mBean.perfusionVolume;
            }
            tvTotalPerfusionVolume.setText(String.valueOf(totalPerfusionVolume));
            tvTotalDrainVolume.setText(String.valueOf(totalDrainVolume));
            tvTotalUltVol.setText(String.valueOf(totalDrainVolume-totalPerfusionVolume));
            mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume;
            MyApplication.ApdTotalDrainVol = totalDrainVolume;
            MyApplication.ApdTotalPerVol = totalPerfusionVolume;
            MyApplication.ApdTotalUltVol = totalDrainVolume-totalPerfusionVolume;
        }
        PdEntity hisPd = new PdEntity();
        TreatmentParameterEniity eniity = PdproHelper.getInstance().getTreatmentParameter();
        MyApplication.mEndTime = getYMDHMTime(getCurrentTime());
        Log.e("获取系统时间", "结束治疗时间: " + MyApplication.mEndTime);
        hisPd.startTime = MyApplication.mStartTime;
        hisPd.endTime = MyApplication.mEndTime;
        hisPd.totalDrainVol = totalDrainVolume;
        hisPd.totalPerVol = totalPerfusionVolume;
        hisPd.totalVol = eniity.peritonealDialysisFluidTotal;
        hisPd.cycle = eniity.cycle;
        hisPd.phone = MyApplication.phone;
        hisPd.finalAbdTime = eniity.abdomenRetainingVolumeFinally;
        hisPd.firstPerfusionVolume = eniity.firstPerfusionVolume;
        hisPd.perCyclePerfusionVolume = eniity.perCyclePerfusionVolume;
        hisPd.abdomenRetainingVolumeFinally = eniity.abdomenRetainingVolumeFinally;
        hisPd.abdomenRetainingTime = eniity.abdomenRetainingTime;
        hisPd.abdomenRetainingVolumeLastTime = eniity.abdomenRetainingVolumeLastTime;
        hisPd.ultrafiltrationVolume = eniity.ultrafiltrationVolume;
        hisPd.firstTime = firstDrainTime;
        hisPd.totalUltVol = mUltrafiltrationVolume;
        hisPd.pdInfoEntities = pdInfoEntities;
//        public int peritonealDialysisFluidTotal;
//        /***  处方：周期数 1～13  **/
//        public int periodicities;
//        /***  处方：首次灌注量 0～5000  **/
//        public int firstPerfusionVolume = 0;
//        /***  处方：周期灌注量 0～5000  **/
//        public int perCyclePerfusionVolume;
//        /***  处方：最末留腹量 0～5000  **/
//        public int abdomenRetainingVolumeFinally = 0;
//        /***  处方：留腹时间 0～600  **/
//        public int abdomenRetainingTime = 1;
//        /***  处方：上次最末留腹量 0～5000  **/
//        public int abdomenRetainingVolumeLastTime = 0;
//        /***  处方：预估超滤量 0～5000  **/
//        public int ultrafiltrationVolume = 0;
//        Log.e("治疗界面", "startTime--" + MyApplication.mStartTime + "--endTime--" + MyApplication.mEndTime);
        EmtDataBase
                .getInstance(TreatmentFragmentActivity.this)
                .getPdDao().insertPd(hisPd);

        if (cycles.equals("")) {
            cycles = "0";
        }
        if (inFlows.equals("")) {
            inFlows = "0";
        }
        if (inFlowTime.equals("")) {
            inFlowTime = "0";
        }
        if (leaveWombTime.equals("")) {
            leaveWombTime = "0";
        }
        if (drainages.equals("")) {
            drainages = "0";
        }

    }


    /**
     * 执行指令成功
     *
     * @param topic
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
    public void receiveCmdResultOk(String topic) {

        if (topic.contains(CommandSendConfig.METHOD_TREATMENT_START)) {//开始治疗
            MyApplication.currCmd = "";
            speak("开始治疗");
//            stopCmdLoopTimer();
        } else if (topic.equals(TOPIC_TREATMENT_ABORT) || topic.equals(TOPIC_TREATMENT_FINISH)) {//停止治疗
            stopCmdLoopTimer();
            stopLoopCountDown();
            stopLoopTimer();
            buzzerOff();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    initDb();
//                }
//            }).start();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initDb();
//                    btnFinishTreatment.setVisibility(View.INVISIBLE);
                    layoutSkipOrPause.setVisibility(View.INVISIBLE);
                    showCompleteDialog();
                }
            });
        } else if (topic.equals(CommandSendConfig.METHOD_TREATMENT_SKIP)) {//跳过当前阶段:跳过灌注、跳过留腹、跳过引流
//            buzzerOff();
        } else if (topic.equals(CommandSendConfig.METHOD_TREATMENT_FAILURE_CONTINUET)) {//跳过当前阶段:跳过灌注、跳过留腹、跳过引流
//            buzzerOff();
        }

    }

    /**
     * 单片机返回指令不执行
     *
     * @param bean
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_ERR)
    public void receiveCmdResultErr(ReceiveResultDataBean bean) {
        String topic = bean.result.topic;
        if (topic.contains(CommandSendConfig.METHOD_TREATMENT_START)) {//开始治疗
//            showTipsCommonDialog("开始治疗异常");
//            stopCmdLoopTimer();
            // 多模式
            deliveryTherapy();
            // 单模式
//            startTreat();
//            setTreatmentCmd();
        } else if (topic.contains(CommandSendConfig.METHOD_TREATMENT_ABORT)) {//终止治疗
//            showTipsCommonDialog("终止治疗异常");
            isComplete = true;
            stopCmdLoopTimer();
            stopLoopCountDown();
            stopLoopTimer();
            buzzerOff();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    initDb();
//                }
//            }).start();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initDb();
//                    btnFinishTreatment.setVisibility(View.INVISIBLE);
                    layoutSkipOrPause.setVisibility(View.INVISIBLE);
                    showCompleteDialog();
                }
            });
        }
    }

    /**
     * 加热温度到达
     *
     * @param
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_THERMOSTAT)
    public void receiveReceiveThermostat(ReceivePublicDataBean receiveBean) {
        if (receiveBean.publish.msg.contains(MSG_TEMPERATURE_ARRIVE)) {//加热温度到达
            Log.e("修改加热温度", "加热温度到达 : " + myGson.toJson(receiveBean.publish));
        }
    }

    /**
     * 修改加热温度
     *
     * @param
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_TREATMENT_TEMPERATURE_SET)
    public void receiveTemperatureSet(ReceivePublicDataBean receiveBean) {
        if (receiveBean.publish.msg.contains(MSG_TEMPERATURE_SET_FAILED)) {//温度设置失败
            Log.e("修改加热温度", "温度设置失败 : " + myGson.toJson(receiveBean.publish));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showTipsCommonDialog("温度设置失败");
                }
            });
        } else if (receiveBean.publish.msg.contains(MSG_TEMPERATURE_SET_SUCCESS)) {//温度设置成功
            Log.e("修改加热温度", "温度设置成功: " + myGson.toJson(receiveBean.publish));
        }
    }

    @Subscribe(code = RxBusCodeConfig.CHECK_PER)
    public void receiveCheck(ReceivePublicDataBean receiveBean) {
        CheckPerBean checkPerBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), CheckPerBean.class);
        if (Objects.equals(checkPerBean.drain, "fault")) {
            runOnUiThread(()->{
                if (!isShowAlarmDialog) {
                    drainValveFaultDialog();
                }
            });
        } else if (Objects.equals(checkPerBean.drain, "ok")){
            runOnUiThread(()-> {
                if (drainValveFaultAlarmDialog != null && drainValveFaultAlarmDialog.isShowing()) {
                    buzzerOff();
                    drainValveFaultAlarmDialog.dismiss();
                }
            });
        }
    }

    private int dayFlag;
    /**
     * 处理灌注数据
     *
     * @param receiveBean
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_TREATMENT_PERFUSION_DATA)
    public void receivePerfusionData(ReceivePublicDataBean receiveBean) {

        if (receiveBean.publish.msg.contains(MSG_CHECK_TEMPERATURE)) {//灌注前检查温度: 在灌注自检开始
            Log.e("处理灌注数据", "灌注前检查温度 : " + myGson.toJson(receiveBean.publish));
        } else if (receiveBean.publish.msg.contains(MSG_PROCESS_START)) {//1、灌注过程开始
            Log.e("处理灌注数据", "1、灌注过程开始: " + myGson.toJson(receiveBean.publish));
//            speak("开始灌注");
            ReceivePerfuseStartBean startBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseStartBean.class);
            currCycle = startBean.cycle;
//            tvCurrTime.setText("00:00");
            runOnUiThread(()-> {
                if (currCycle == 1) {
                    cycles += currCycle;
                } else if (currCycle > 1){
                    cycles += ","+currCycle;
                }
                switch (MyApplication.apdMode) {
                    case 1:
                        IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                        if (currCycle == maxCycle - 1 && ipdBean.isFinalSupply && ipdBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 4, 3, 2));
                        } else if (currCycle == 1) {
                            sendToMainBoard(CommandDataHelper.getInstance().special( 1, 3,  4, 1));
                        }
                        break;
                    case 2:
                        TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                        if (currCycle == maxCycle - 1 && tpdBean.isFinalSupply && tpdBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 4, 3, 2));
                        } else if (currCycle == 1) {
                            sendToMainBoard(CommandDataHelper.getInstance().special( 1, 3,  4, 1));
                        }
                        break;
                    case 3:
                        CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                        if (currCycle == maxCycle - 1 && ccpdBean.isFinalSupply && ccpdBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 4, 3, 2));
                        } else if (currCycle == 1) {
                            sendToMainBoard(CommandDataHelper.getInstance().special( 1, 3,  4, 1));
                        }
                        break;
                    case 4:
                        AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
                        if (aapdFragment != null) {
                            aapdFragment.setBackground(aapdBean, currCycle);
                        }
//                        if (currCycle == 1) {
//                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.isFinalSupply?1:2, aapdBean.a1 == 1?1:0, aapdBean.a1 == 0?1:0, aapdBean.p1));
//                        } else
                        if (aapdBean.c1 - startBean.cycle == 0 && aapdBean.c2 != 0&& aapdBean.p2 != 0 && aapdBean.r2 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a2 == 0?2:1, 3, 4, aapdBean.a2 == 0?2:1));
                        } else if (aapdBean.c2+aapdBean.c1 - startBean.cycle == 0 && aapdBean.c3 != 0 && aapdBean.p3 != 0 && aapdBean.r3 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a3 == 0?2:1, 3, 4, aapdBean.a3 == 0?2:1));
                        } else if (aapdBean.c3+aapdBean.c2+aapdBean.c1 - startBean.cycle == 0 && aapdBean.c4 != 0&& aapdBean.p4 != 0 && aapdBean.r4 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a4 == 0?2:1, 3, 4, aapdBean.a4 == 0?2:1));
                        } else if (aapdBean.c4+aapdBean.c3+aapdBean.c2+aapdBean.c1 - startBean.cycle == 0 && aapdBean.c5 != 0&& aapdBean.p5 != 0 && aapdBean.r5 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a5 == 0?2:1, 3, 4, aapdBean.a5 == 0?2:1));
                        } else if (aapdBean.c5+aapdBean.c4+aapdBean.c3+aapdBean.c2+aapdBean.c1 - startBean.cycle == 0 && aapdBean.c6 != 0&& aapdBean.p6 != 0 && aapdBean.r6 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a6 == 0?2:1, 3, 4, aapdBean.a6 == 0?2:1));
                        }
                        break;
                    case 7:
                        KidBean kidBean = PdproHelper.getInstance().kidBean();
                        if (currCycle == 1) {
                            sendToMainBoard(CommandDataHelper.getInstance().special( 1,3,4,1));
                        }else if (currCycle == maxCycle - 1 && kidBean.isFinalSupply && kidBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 4, 3, 2));
                        }
//                        sendToMainBoard(CommandDataHelper.getInstance().special(kidBean.isFinalSupply?1:2, kidBean.isFinalSupply?0:1, kidBean.isFinalSupply?1:0, kidBean.abdomenRetainingVolumeFinally));
                        break;
                    case 8:
                        ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                        if (expertBean.cycleMyself) {
                            if (maxCycle >= startBean.cycle+1) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(expertBean.baseSupplyCycle.contains(startBean.cycle+1)?2:1,
                                        3,
                                        4,
                                        expertBean.baseSupplyCycle.contains(startBean.cycle+1)?2:1
                                ));
                            }
                        }
                        break;
                }
                switchTreatmentStatus(1);
//                if (PdproHelper.getInstance().getUserParameterBean().isNight) {
//                    sendToMainBoard(CommandDataHelper.getInstance().LedOpen("perfuse", true,0));
//                }
                Log.e("处理灌注数据","currCycle--"+currCycle);
//                if (currCycle == 1) {
//                    cycles += currCycle;
//                } else if (currCycle > 1){
//                    cycles += ","+currCycle;
//                }
            });
            if (currCycle < 10) {
                APIServiceManage.getInstance().postApdCode("T0"+currCycle+"01");
            } else {
                APIServiceManage.getInstance().postApdCode("T" + currCycle + "01");
            }
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"01");
        } else if (receiveBean.publish.msg.contains(MSG_PROCESS_FINISH)) {//2、灌注过程完成
            Log.e("处理灌注数据", "2、灌注过程完成: " + myGson.toJson(receiveBean.publish));
            stopLoopTimer();
            speak("灌注完成");
            ReceivePerfuseProcessFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseProcessFinishBean.class);
            if (mBean != null) {
                runOnUiThread(()->{
                    currCycle = mBean.cycle;
                    if (currCycle < maxCycle) {
                        modifyDuringTreatment(mBean.cycle);
                    }
                    perTime = currPerfusionTime;
                    perVol = mBean.perfuse;
                    perRetain = mBean.retain;
                    if (currCycle == 1) {
                        inFlows += perVol;
                        inFlowTime += perTime / 60;
                        abdominalVolumeAfterInflow += mBean.retain;
                    } else if (currCycle > 1){
                        inFlows += ","+perVol;
                        inFlowTime += ","+ perTime / 60;
                        abdominalVolumeAfterInflow += ","+mBean.retain;
                    }
                    if (maxCycle == mBean.cycle) {
                        endTime = perTime / 60;
                    }
                    tvTotalPerfusionVolume.setText(String.valueOf(mBean.perfuse));
                    saveOrUpdatePerfusion(mBean);
                });
            }
//            switchTreatmentStatus(2);
        } else if (receiveBean.publish.msg.contains(MSG_SELFCHECK_START)) {//3、灌注自检开始
            Log.e("处理灌注数据", "3、灌注自检开始: " + myGson.toJson(receiveBean.publish));
//            showLoading("正在灌注自检中，请稍后...");
            speak("正在灌注自检中，请稍后...");
            // {"publish":{"topic":"treatment/perfuse","msg":"selfcheck start","data":{"cycle":1}},"sign":"70da56cc5270e3811809ed13bb163e4d"}
        } else if (receiveBean.publish.msg.contains(MSG_SELFCHECK_FINISH)) {//4、灌注自检完成
            Log.e("处理灌注数据", "4、灌注自检完成: " + myGson.toJson(receiveBean.publish));
//            dismissLoading();
            //{"publish":{"topic":"treatment/perfuse","msg":"selfcheck finish","data":{"cycle":1,"upperInc":1,"lowerInc":0}},"sign":"12d77af6d2f1866a42c0b9aef1d5f58d"}
        } else if (receiveBean.publish.msg.contains(MSG_SELFCHECK_FAILURE)) {//5、灌注自检失败
            Log.e("处理灌注数据", "5、灌注自检失败: " + myGson.toJson(receiveBean.publish));
//            dismissLoading();
            runOnUiThread(this::perfusionSelfCheckFaultDialog);
        } else if (receiveBean.publish.msg.contains(MSG_PERFUSE_START)) {//6、灌注开始
            Log.e("处理灌注数据", "6、灌注开始: " + myGson.toJson(receiveBean.publish));
            speak("开始灌注");
            ReceivePerfuseBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseBean.class);
            if (mBean != null) {
                currCycle = mBean.cycle;
//                if (1 == currentStage) {
//                    if (!PdproHelper.getInstance().getUserParameterBean().isNight) {
//                        CommandDataHelper.getInstance().LedOpen("perfuse", true,0);
//                    }
//                    currPerfusionTime = 0;
//                    startLoopTimer();
//                }
                if (null != mFragment3.getActivity()) {
                    runOnUiThread(() -> {
                        mFragment3.setPerfusionTargetVolume(String.valueOf(mBean.cycle), String.valueOf(mBean.perfuseTarget));
                    });
                }
                if (currCycle < 10) {
                    APIServiceManage.getInstance().postApdCode("T0"+currCycle+"21");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "21");
                }
//                APIServiceManage.getInstance().postApdCode("T"+currCycle+"21");
                if (!isTiming) {
//                    switchTreatmentStatus(1);
                    currPerfusionTime = 0;
                    startLoopTimer();
                }
            }
        } else if (receiveBean.publish.msg.contains(MSG_PERFUSE_RUNNING)) {//7、灌注进行中

            ReceivePerfuseRunningBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseRunningBean.class);
            String mCurrentVolume = String.valueOf(mBean.perfuse);
            Log.e("处理灌注数据", "7、灌注进行中: " + myGson.toJson(receiveBean.publish) + " ，实时灌注量：" + mCurrentVolume);
            currCycle = mBean.cycle;
            runOnUiThread(() -> {
                tvCurrVolume.setText(mCurrentVolume);
                mHistoryBean = mTreatmentDataList.get(currCycle);
                mHistoryBean.perfusionVolume = Integer.parseInt(mCurrentVolume);
                mHistoryBean.perfusionTime = currPerfusionTime;
                if (null != fragmentItem2.getActivity()) {
                    fragmentItem2.setHistoricalData(mTreatmentDataList);
                }
                if (null != mFragment3.getActivity()) {
                    mFragment3.setPerfusionTargetVolume(String.valueOf(mBean.cycle), String.valueOf(mBean.perfuseTarget));
                    mFragment3.setCurrPerfusionVolume(mCurrentVolume);
                    mFragment3.setWaittingVolume(String.valueOf(mBean.retain));
                }
//                if ((mTreatmentParameterEniity.perfusionWarningValue < Integer.valueOf(mCurrentVolume)) && !isShowExceedsMaxValueDialog) {
//                    perfusionMaxValueDialog();
//                }
            });
        } else if (receiveBean.publish.msg.contains(MSG_PERFUSE_FINISH)) {//8、灌注完成
            Log.e("处理灌注数据", "8、灌注完成: " + myGson.toJson(receiveBean.publish));
            //{"data":{"cycle":2.0,"retain":-34.0,"perfuseTarget":500.0,"perfuse":490.0},"msg":"perfuse finish","topic":"treatment/perfuse"}
//            speak("灌注完成");
        } else if (receiveBean.publish.msg.contains(MSG_PERFUSE_FAILURE)) {//9、灌注失败
            Log.e("处理灌注数据", "9、灌注失败: " + myGson.toJson(receiveBean.publish));
            ReceivePerfuseFailureBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseFailureBean.class);
            runOnUiThread(this::perfusionFaultDialog);
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"23");
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"29");
//            speak("灌注失败");
//            sendCommandInterval(CommandSendConfig.TREATMENT_START_PERFUSION_FAULT_ENSUREDISASSEMBLE, 200);
        } else if (receiveBean.publish.msg.contains(MSG_RETAIN_OVER_LIMIT)) {//10、灌注留腹超过限制
            Log.e("处理灌注数据", "10、灌注留腹超过限制:" + myGson.toJson(receiveBean.publish));
            ReceiveCycleBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveCycleBean.class);
            runOnUiThread(this::perfusionMaxValueDialog);
//            speak("灌注留腹超过限制");
        } else if (receiveBean.publish.msg.contains(MSG_RETAIN_HIGH_TEMPERATURE_WARNING)) {//高温警告
            Log.e("处理灌注数据", "高温警告:" + myGson.toJson(receiveBean.publish));
            runOnUiThread(this::showHeatAlertDialog);
//            speak("高温警告");
        } else if (receiveBean.publish.msg.contains(MSG_RETAIN_LOW_TEMPERATURE_WARNING)) {//低温警告
            Log.e("处理灌注数据", "低温警告:" + myGson.toJson(receiveBean.publish));
            runOnUiThread(this::showLowTemperatureAlertDialog);
//            speak("低温警告");
        } else if (receiveBean.publish.msg.contains(MSG_SUPPLY_START)) {//11、灌注补液开始
            //{"publish":{"topic":"treatment/perfuse","msg":"supply start","data":{"cycle":1,"upperWeight":2159,"supplyProtect":800,"supplyTarget":1305}},"sign":"1fc7d2f82746c81a36ce84308faa8d7f"}
            Log.e("处理补液数据", "11、灌注补液开始：" + myGson.toJson(receiveBean.publish));

            ReceiveSupplyStartBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveSupplyStartBean.class);
            runOnUiThread(() -> {
                ivVolume.setBackgroundResource(R.drawable.supply_zl);
                ivExtraState.setVisibility(View.VISIBLE);
                ivExtraState.setBackgroundResource(R.drawable.treatment_icon_volume_day);
                if (null != mFragment3.getActivity()) {
//                    sendToMainBoard(CommandDataHelper.getInstance().LedOpen("perfuse", true, dayFlag));
                    mFragment3.setSupplyTargetValue(mBean.supplyTarget, mBean.supplyProtect, mBean.supply);
                }
            });
//            speak("灌注补液开始");
        } else if (receiveBean.publish.msg.contains(MSG_SUPPLY_FINISH)) {//12、灌注补液完成
            Log.e("处理灌注补液数据", "12、灌注补液完成:" + myGson.toJson(receiveBean.publish));
//            speak("灌注补液完成");
            runOnUiThread(()->{
                ivVolume.setBackgroundResource(R.drawable.treatment_icon_volume_day);
                ivExtraState.setVisibility(View.GONE);
            });
//        } else if (receiveBean.publish.msg.contains(CommandReceiveConfig.TREATMENT_SUPPLY_STOP)) {//补液返回: 停止补液 TreatmentSupplyStop  [当前上位称重量]
//            Log.e("receiveSupplyData", "停止补液:" + myGson.toJson(receiveBean.publish));
        } else if (receiveBean.publish.msg.equals(MSG_SUPPLY_FAILURE)) {//13、灌注补液失败
            Log.e("处理灌注补液数据", "13、灌注补液失败:" + myGson.toJson(receiveBean.publish));
            runOnUiThread(this::supplyFaultDialog2);
//            speak("灌注补液失败");
        }
//        else if (receiveBean.publish.msg.equals("perfuse over temp")) {
//            showTipsCommonDialog("灌注温度过高");
//        }
        else {
            Log.e("处理灌注数据", "other、灌注:" + myGson.toJson(receiveBean.publish));
        }
    }

    /**
     * 处理留腹数据
     *
     * @param receiveBean
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_TREATMENT_RETAIN_DATA)
    public void receiveWaitingData(ReceivePublicDataBean receiveBean) {
        if (receiveBean.publish.msg.contains(MSG_PROCESS_START)) {//1、留腹过程开始
            Log.e("处理留腹数据", "1、留腹过程开始:" + myGson.toJson(receiveBean.publish));
            //{"data":{"cycle":1.0,"ultraFiltration":0.0,"currentDrainEstimate":0.0,"nextPefuseEstimate":500.0,"expectRetainTime":900.0,"retainTime":0.0},"msg":"process start","topic":"treatment/retain"}
            ReceiveRetainProcessStartBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainProcessStartBean.class);
//            APIServiceManage.getInstance().postApdCode("T" + currCycle + "31");
            if (currCycle < 10) {
                APIServiceManage.getInstance().postApdCode("T0"+currCycle+"31");
            } else {
                APIServiceManage.getInstance().postApdCode("T" + currCycle + "31");
            }
            /*
             * 单模式
             */
//            if (fragmentItem1 == null) {
//                TreatmentParameterEniity entity = PdproHelper.getInstance().getTreatmentParameter();
////                if (currCycle == 1) {
////                    sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
////                }
//                if (currCycle == maxCycle - 1 && entity.isFinalSupply && entity.abdomenRetainingVolumeFinally != 0) {
//                    sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
//                }
//            } else {
//                if (currCycle == maxCycle - 1 && fragmentItem1.isFinal && fragmentItem1.finalVol != 0) {
//                    sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
//                }
//            }

            // 多模式
                switch (MyApplication.apdMode) {
                    case 1:
                        IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                        if (currCycle == maxCycle - 1 && ipdBean.isFinalSupply && ipdBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                        } else if (currCycle < maxCycle) {
                            sendToMainBoard(CommandDataHelper.getInstance().special( 1, 3,  4, 1));
                        }
                        break;
                    case 2:
                        TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                        if (currCycle == maxCycle - 1 && tpdBean.isFinalSupply && tpdBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                        } else if (currCycle < maxCycle) {
                            sendToMainBoard(CommandDataHelper.getInstance().special( 1, 3,  4, 1));
                        }
                        break;
                    case 3:
                        CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                        if (currCycle == maxCycle - 1 && ccpdBean.isFinalSupply && ccpdBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                        } else if (currCycle < maxCycle) {
                            sendToMainBoard(CommandDataHelper.getInstance().special( 1, 3,  4, 1));
                        }
                        break;
                    case 4:
                        AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
                        if (aapdFragment != null) {
                            aapdFragment.setBackground(aapdBean, currCycle);
                        }
//                        if (currCycle == 1) {
//                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.isFinalSupply?1:2, aapdBean.a1 == 1?1:0, aapdBean.a1 == 0?1:0, aapdBean.p1));
//                        } else
                            if (aapdBean.c1 - mBean.cycle == 0 && aapdBean.c2 != 0&& aapdBean.p2 != 0 && aapdBean.r2 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a2 == 0?2:1, 3, 4, aapdBean.a2 == 0?2:1));
                        } else if (aapdBean.c2+aapdBean.c1 - mBean.cycle == 0 && aapdBean.c3 != 0 && aapdBean.p3 != 0 && aapdBean.r3 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a3 == 0?2:1, 3, 4, aapdBean.a3 == 0?2:1));
                        } else if (aapdBean.c3+aapdBean.c2+aapdBean.c1 - mBean.cycle == 0 && aapdBean.c4 != 0&& aapdBean.p4 != 0 && aapdBean.r4 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a4 == 0?2:1, 3, 4, aapdBean.a4 == 0?2:1));
                        } else if (aapdBean.c4+aapdBean.c3+aapdBean.c2+aapdBean.c1 - mBean.cycle == 0 && aapdBean.c5 != 0&& aapdBean.p5 != 0 && aapdBean.r5 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a5 == 0?2:1, 3, 4, aapdBean.a5 == 0?2:1));
                        } else if (aapdBean.c5+aapdBean.c4+aapdBean.c3+aapdBean.c2+aapdBean.c1 - mBean.cycle == 0 && aapdBean.c6 != 0&& aapdBean.p6 != 0 && aapdBean.r6 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a6 == 0?2:1, 3, 4, aapdBean.a6 == 0?2:1));
                        }
                        break;
                    case 7:
                        KidBean kidBean = PdproHelper.getInstance().kidBean();
                        if (currCycle < maxCycle) {
                            sendToMainBoard(CommandDataHelper.getInstance().special( 1,3,4,1));
                        }else if (currCycle == maxCycle - 1 && kidBean.isFinalSupply && kidBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                        }
//                        sendToMainBoard(CommandDataHelper.getInstance().special(kidBean.isFinalSupply?1:2, kidBean.isFinalSupply?0:1, kidBean.isFinalSupply?1:0, kidBean.abdomenRetainingVolumeFinally));
                        break;
                    case 8:
                        ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                        if (expertBean.cycleMyself) {
                            if (maxCycle >= mBean.cycle+1) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(expertBean.baseSupplyCycle.contains(mBean.cycle+1)?2:1,
                                        3,
                                        4,
                                        expertBean.baseSupplyCycle.contains(mBean.cycle+1)?2:1
                                ));
                            }
                        }
                        break;
                }
            if (!isTiming) {
                if (mBean != null) {
//                    currWaitingTime = mTreatmentParameterEniity.abdomenRetainingTime * 60;
                    currRetainTime = mBean.expectRetainTime;
//                    if (PdproHelper.getInstance().getUserParameterBean().isNight) {
//                        sendToMainBoard(CommandDataHelper.getInstance().LedOpen("retain", true, 0));
//                    }
//                    if (PdproHelper.getInstance().getUserParameterBean().isNight) {
//                        sendCommandInterval(CommandDataHelper.getInstance().LedOpen("retain", true,0),500);
//                    }
                    switchTreatmentStatus(2);
                }
            }
        } else if (receiveBean.publish.msg.contains(MSG_PROCESS_FINISH)) {//2、留腹过程完成
            Log.e("处理留腹数据", "2、留腹过程完成:" + myGson.toJson(receiveBean.publish));
            //{"data":{"cycle":1.0,"workTime":184.0,"expectRetainTime":180.0,"retainTime":180.0,"retainVolume":-75.0},"msg":"process finish","topic":"treatment/retain"}
            ReceiveRetainProcessFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainProcessFinishBean.class);
            stopLoopCountDown();
            retainTime = mBean.retainTime;
            runOnUiThread(()-> {
                saveRetain(mBean);
                modifyDuringTreatment(mBean.cycle);
                ivRetainSupply.setVisibility(View.GONE);
            });
            Log.e("留腹过程完成","currCycle--"+mBean.cycle);
            if (mBean.cycle == 1) {
                leaveWombTime += mBean.retainTime / 60;
            } else {
                leaveWombTime += "," + mBean.retainTime / 60;
            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    hisPdItems.add(hisPdItem);
//                }
//            });
//            runOnUiThread(()->ivRetainSupply.setVisibility(View.GONE));
//            speak("");
//            switchTreatmentStatus(3);//留腹过程完成以后，切换到引流
        } else if (receiveBean.publish.msg.contains(MSG_SELFCHECK_START)) {//3、留腹自检开始
            Log.e("处理留腹数据", "3、留腹自检开始:" + myGson.toJson(receiveBean.publish));
//            showLoading("正在留腹自检中，请稍后...");
//            speak("留腹自检中，请稍后");
        } else if (receiveBean.publish.msg.contains(MSG_SELFCHECK_FINISH)) {//4、留腹自检完成
            Log.e("处理留腹数据", "4、留腹自检完成:" + myGson.toJson(receiveBean.publish));
//            dismissLoading();
        } else if (receiveBean.publish.msg.contains(MSG_SELFCHECK_FAILURE)) {//5、留腹自检失败
            Log.e("处理留腹数据", "5、留腹自检失败:" + myGson.toJson(receiveBean.publish));
//            dismissLoading();
//            speak("留腹自检失败");
        } else if (receiveBean.publish.msg.contains(MSG_SUPPLY_START)) {//6、留腹补液开始
            Log.e("处理留腹数据", "6、留腹补液开始:" + myGson.toJson(receiveBean.publish));
            ReceiveRetainSupplyBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainSupplyBean.class);
//            speak("留腹自检完成");
            if (currCycle < 10) {
                APIServiceManage.getInstance().postApdCode("T0"+currCycle+"41");
            } else {
                APIServiceManage.getInstance().postApdCode("T" + currCycle + "41");
            }
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"41");
            runOnUiThread(() -> {
                if (null != mFragment3.getActivity()) {
                    mFragment3.setSupplyTargetValue(mBean.supplyTarget, mBean.supplyProtect,mBean.supply);
                }
                ivRetainSupply.setVisibility(View.VISIBLE);
            });
        } else if (receiveBean.publish.msg.contains(MSG_SUPPLY_RESTART)) {//7、留腹补液重新开始
            Log.e("处理留腹数据", "7、留腹补液重新开始:" + myGson.toJson(receiveBean.publish));
//            speak("留腹补液重新开始");
        } else if (receiveBean.publish.msg.contains(MSG_SUPPLY_FINISH)) {//8、留腹补液完成
            Log.e("处理留腹数据", "8、留腹补液完成:" + myGson.toJson(receiveBean.publish));
//            speak("留腹补液完成");
            runOnUiThread(()->{
                ivRetainSupply.setVisibility(View.GONE);
                ReceiveRetainSupplyFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainSupplyFinishBean.class);
                if (mFragment3.getActivity() != null) {
                    mFragment3.setSupply(mBean.supply);
                }
            });
        } else if (receiveBean.publish.msg.contains("supply run")) {
            runOnUiThread(()->{
                ReceiveRetainSupplyFinishBean finishBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainSupplyFinishBean.class);
                if (mFragment3.getActivity() != null) {
                    mFragment3.setSupply(finishBean.supply);
                }
            });
        } else if (receiveBean.publish.msg.equals(MSG_SUPPLY_FAILURE)) {//9、留腹补液失败
            Log.e("处理留腹数据", "9、留腹补液失败:" + myGson.toJson(receiveBean.publish));
//            speak("留腹补液失败");
            runOnUiThread(this::supplyFaultDialog);
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"43");
        } else if (receiveBean.publish.msg.contains(MSG_RETAIN_TIME_OUT)) {//10、留腹时间结束
            Log.e("处理留腹数据", "10、留腹时间结束:" + myGson.toJson(receiveBean.publish));
            //{"data":{"cycle":1.0,"expectRetainTime":180.0,"retainTime":180.0},"msg":"retain time out","topic":"treatment/retain"}
            currRetainTime = 0;
//            speak("留腹时间结束");
            stopLoopCountDown();
            runOnUiThread(()->{
                ivRetainSupply.setVisibility(View.GONE);
            });

            ReceiveRetainFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainFinishBean.class);
//            ReceiveCycleBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveCycleBean.class);
            saveOrUpdateWaiting(mBean);
        } else if (receiveBean.publish.msg.contains(MSG_RETAIN_TIME_OUT_SELFCHECK_FAILURE_UNHANDLE)) {//11.1、留腹时间结束但故障未处理:自检失败未处理
            Log.e("留腹时间结束但故障未处理", "1、自检失败未处理:" + myGson.toJson(receiveBean.publish));

        } else if (receiveBean.publish.msg.contains(MSG_RETAIN_TIME_OUT_SUPPLY_FAILURE_UNHANDLE)) {//11.2、留腹时间结束但故障未处理:补液失败未处理
            Log.e("留腹时间结束但故障未处理", "2、补液失败未处理:" + myGson.toJson(receiveBean.publish));
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"49");
            if (currCycle < 10) {
                APIServiceManage.getInstance().postApdCode("T0"+currCycle+"49");
            } else {
                APIServiceManage.getInstance().postApdCode("T" + currCycle + "49");
            }
        } else if (receiveBean.publish.msg.contains(MSG_RETAIN_TIME_OUT_OTHER_FAILURE_UNHANDLE)) {//11.3、留腹时间结束但故障未处理:其它类型失败未处理
            Log.e("留腹时间结束但故障未处理", "3、其它类型失败未处理:" + myGson.toJson(receiveBean.publish));
            sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));

        } else if (receiveBean.publish.msg.equals("retain drop fault")) { // 引流阀门故障
            runOnUiThread(()->{
                if (!isShowAlarmDialog) {
                    drainValveFaultDialog();
                }
            });
        } else if (receiveBean.publish.msg.equals("retain drop ok")) {
            runOnUiThread(()-> {
                if (drainValveFaultAlarmDialog != null && drainValveFaultAlarmDialog.isShowing()) {
                    buzzerOff();
                    drainValveFaultAlarmDialog.dismiss();
                }
            });
        }
    }

    private int firstDrainTime;

    /**
     * 处理引流数据
     *
     * @param receiveBean
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_TREATMENT_DRAIN_DATA)
    public void receiveDrainData(ReceivePublicDataBean receiveBean) {

        switch (receiveBean.publish.msg) {
            case MSG_PROCESS_START:
                //{"topic":"treatment/drain","msg":"process start","data":{"cycle":0}},"sign":"98d866f4e59c226d219a31c1a015fd03"}
//            runOnUiThread(() -> drainFaultDialog());
//                postApdCode("T"+receiveBean.);
                Log.e("处理引流数据", "1、引流过程开始: " + myGson.toJson(receiveBean.publish));
                ReceiveDrainStartBean bean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainStartBean.class);
                if (bean != null) {
                    currCycle = bean.cycle;
//                    tvCurrTime.setText("00:00");
                    modifyDuringTreatment(bean.cycle);
                    switchTreatmentStatus(3);
                    runOnUiThread(() -> {
                        if (null != mFragment3.getActivity()) {
                            mFragment3.setDrainTargetVolume(currCycle, bean.drainTarget + "");
                        }
                    });
                }
                break;
            case MSG_PROCESS_FINISH: {
                Log.e("处理引流数据", "2、引流过程完成: " + myGson.toJson(receiveBean.publish));
                stopLoopTimer();
                speak("完成引流");
// {"data":{"cycle":1.0,"workTime":4981.0,"drainTarget":496.0,"drain":3.0,"drainRetain":493.0,"rinseCount":0.0,"drainTotalRinse":0.0,"vaccumCount":0.0},"msg":"process finish","topic":"treatment/drain"}
                ReceiveDrainProcessFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainProcessFinishBean.class);
                if (mBean != null) {
                    currCycle = mBean.cycle;
                    Log.e("处理引流数据","currCycle--"+currCycle);
                    saveOrUpdateDrain(mBean);
                    drainRetain = mBean.drainRetain;
                    drainTarget = mBean.drainTarget;
                    drainTime = currDrainTime;
//                drainage = mBean.drain;
                    if (mBean.cycle == 1) {
                        drainageTargetValue += mBean.drainTarget+"";
                        exhaustTime += mBean.workTime / 60;
                        drainages += "," + drainage;
                        firstDrainTime = mBean.workTime / 60;
                        estimatedResidualAbdominalFluid = ","+mBean.drainRetain+"";
                    } else if (mBean.cycle == 0) {
                        firstDrainTime = mBean.workTime / 60;
                        drainages += drainage+"";
                        estimatedResidualAbdominalFluid = mBean.drainRetain+"";
//                        exhaustTime += mBean.workTime;
                    } else {
                        drainageTargetValue += ","+mBean.drainTarget;
                        exhaustTime += "," + mBean.workTime / 60;
                        drainages += "," + drainage;
                        estimatedResidualAbdominalFluid = ","+mBean.drainRetain+"";
//                        firstDrainTime = "," + mBean.workTime;
                    }
                    runOnUiThread(()->{
                        tvTotalDrainVolume.setText(String.valueOf(mBean.drain));
                    });
//                    drainage = drainage + mBean.drain;
                }//引流过程完成: {"data":{"cycle":1.0,"workTime":217.0,"drainTarget":0.0,"drain":524.0,"drainRetain":-524.0,"rinseCount":0.0,"drainTotalRinse":0.0,"vaccumCount":0.0},"msg":"process finish","topic":"treatment/drain"}

//            if (currCycle <= mTreatmentParameterEniity.cycle) {//引流阶段只有
//                switchTreatmentStatus(1);//引流完成以后，切换到灌注
//            }
                break;
            }
            case MSG_SELFCHECK_START:
                Log.e("处理引流数据", "3、引流自检开始: " + myGson.toJson(receiveBean.publish));
//            showLoading("正在引流自检中，请稍后...");
                speak("正在引流自检中，请稍后...");
                break;
            case MSG_SELFCHECK_FINISH:
                Log.e("处理引流数据", "4、引流自检完成: " + myGson.toJson(receiveBean.publish));
                //{"data":{"cycle":0.0,"upperInc":0.0,"lowerInc":0.0},"msg":"selfcheck finish","topic":"treatment/drain"}
//            dismissLoading();
                break;
            case MSG_SELFCHECK_FAILURE:
                Log.e("处理引流数据", "5、引流自检失败: " + myGson.toJson(receiveBean.publish));
//            dismissLoading();
                speak("引流自检失败");
                runOnUiThread(()->{
                    if (!isShowAlarmDialog) {
                        drainSelfCheckFaultDialog();
                    }
                });
                break;
            case MSG_DRAIN_START: //引流返回: 开始引流(非TPD模式或者TPD模式)
                if (!isTiming) {
                    //{"data":{"cycle":0.0,"drainMode":"NTPD","retain":0.0,"drainTarget":0.0,"drainPass":0.0},"msg":"drain start","topic":"treatment/drain"}
                    ReceiveDrainStartBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainStartBean.class);

                    Log.e("处理引流数据", "6、引流开始: " + myGson.toJson(receiveBean.publish));
                    speak("开始引流");
                    runOnUiThread(()-> {
                        currDrainTime = 0;
                        startLoopTimer();
                        if (currCycle < 10) {
                            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"11");
                        } else {
                            APIServiceManage.getInstance().postApdCode("T" + currCycle + "11");
                        }
//                        // 多模式
//                        switch (MyApplication.apdMode) {
//                            case 1:
//                            case 2:
//                            case 3:
//                            case 7:
//                                if (maxCycle > currCycle) {
//                                    revisePrescription();
//                                }
//                                break;
//                            case 4:
//                                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
//                                if (aapdFragment != null) {
//                                    aapdFragment.setBackground(aapdBean, currCycle);
//                                }
//                                if (aapdBean.c1 - mBean.cycle == 0 && aapdBean.c2 != 0&& aapdBean.p2 != 0 && aapdBean.r2 != 0) {
//                                    revisePrescription();
//                                } else if (aapdBean.c2+aapdBean.c1 - mBean.cycle == 0 && aapdBean.c3 != 0 && aapdBean.p3 != 0 && aapdBean.r3 != 0) {
//                                    revisePrescription();
//                                } else if (aapdBean.c3+aapdBean.c2+aapdBean.c1 - mBean.cycle == 0 && aapdBean.c4 != 0&& aapdBean.p4 != 0 && aapdBean.r4 != 0) {
//                                    revisePrescription();
//                                } else if (aapdBean.c4+aapdBean.c3+aapdBean.c2+aapdBean.c1 - mBean.cycle == 0 && aapdBean.c5 != 0&& aapdBean.p5 != 0 && aapdBean.r5 != 0) {
//                                    revisePrescription();
//                                } else if (aapdBean.c5+aapdBean.c4+aapdBean.c3+aapdBean.c2+aapdBean.c1 - mBean.cycle == 0 && aapdBean.c6 != 0&& aapdBean.p6 != 0 && aapdBean.r6 != 0) {
//                                    revisePrescription();
//                                }
//                                break;
//                            case 8:
//                                ExpertBean expertBean = PdproHelper.getInstance().expertBean();
//                                if (expertBean.cycleMyself) {
//                                    if (maxCycle >= mBean.cycle+1) {
//                                        revisePrescription();
//                                    }
//                                }
//                                break;
//
//                        }
//                        modifyDuringTreatment(mBean.cycle);
                    });
                    //{"data":{"cycle":0.0,"drainMode":"NTPD","retain":0.0,"drainTarget":0.0,"drainPass":0.0},"msg":"drain start","topic":"treatment/drain"}
//                    ReceiveDrainStartBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainStartBean.class);
                }
                break;
            case MSG_DRAIN_RESTART: {//引流返回: 引流重新开始
                //stopLoopTimer();
                speak("引流重新开始");
                isVacDrain = false;
                Log.e("处理引流数据", "7、引流重新开始: " + myGson.toJson(receiveBean.publish));
                ReceiveDrainStartBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainStartBean.class);
                break;
            }
            case MSG_DRAIN_NTPD_RUNNING:
            case MSG_DRAIN_TPD_RUNNING:
            case MSG_FINAL_EMPTY_RUNNING: {//引流返回: 引流进行中（NTPD进行中、TPD进行中、末次引流排空进行中）
                //stopLoopTimer();
                ReceiveDrainRunningBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainRunningBean.class);
                String mCurrentVolume = String.valueOf(mBean.drain);
                if (receiveBean.publish.msg.equals(MSG_DRAIN_NTPD_RUNNING)) {
                    Log.e("处理引流数据", "8、NTPD引流进行中: " + myGson.toJson(receiveBean.publish) + "，实时引流量: " + mCurrentVolume);
                    // {"data":{"cycle":0.0,"drain":11.0,"retain":-11.0},"msg":"drain ntpd running","topic":"treatment/drain"}
                } else if (receiveBean.publish.msg.equals(MSG_DRAIN_TPD_RUNNING)) {
                    Log.e("处理引流数据", "9、TPD引流进行中: " + myGson.toJson(receiveBean.publish) + "，实时引流量: " + mCurrentVolume);
                } else if (receiveBean.publish.msg.equals(MSG_FINAL_EMPTY_RUNNING)) {
                    Log.e("处理引流数据", "引流进行中: " + myGson.toJson(receiveBean.publish) + "，实时引流量: " + mCurrentVolume);
                }

//            Log.e("处理实时引流量", "周期：" + currCycle + "，实时引流量: " + mCurrentVolume);
                runOnUiThread(() -> {
                    tvCurrVolume.setText(mCurrentVolume);
//                    drainage =mBean.drain;
                    mHistoryBean = mTreatmentDataList.get(currCycle);
                    mHistoryBean.drainVolume = Integer.parseInt(mCurrentVolume);
                    mHistoryBean.drainTime = currDrainTime;
                    if (null != fragmentItem2.getActivity()) {
                        fragmentItem2.setHistoricalData(mTreatmentDataList);
                    }
                    if (null != mFragment3.getActivity()) {
                        mFragment3.setCurrDrainVolume(mCurrentVolume);
                        mFragment3.setWaittingVolume(String.valueOf(mBean.retain));
                    }
                });
                break;
            }
            case MSG_DRAIN_FINISH: {//引流返回: 引流完成(非TPD模式或者TPD模式)
                stopLoopTimer();
                Log.e("处理引流数据", "10、引流完成: " + myGson.toJson(receiveBean.publish));
//                speak("引流完成");
                ReceiveDrainFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainFinishBean.class);
                //{"data":{"cycle":0.0,"drainTarget":0.0,"drainPass":0.0,"drain":11.0,"retain":-11.0},"msg":"drain finish","topic":"treatment/drain"}
//                drainage = mBean.drain;
//                drainTarget = mBean.drainTarget;
//                drainTime = currDrainTime;
////                drainage = mBean.drain;
//                if (mBean.cycle == 1) {
//                    drainageTargetValue += mBean.drainTarget+"";
//                } else if (mBean.cycle > 1) {
//                    drainageTargetValue += ","+mBean.drainTarget;
//                }
//            if (mBean != null) {
//                currCycle = mBean.cycle;
//                saveOrUpdateDrain(mBean);
//            }
                break;
            }
            case MSG_DRAIN_FAILURE:
                Log.e("处理引流数据", "11、引流不合格: " + myGson.toJson(receiveBean.publish));
                runOnUiThread(this::drainFaultDialog);
                ReceiveDrainFinishBean mBean1 = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainFinishBean.class);
//                APIServiceManage.getInstance().postApdCode("T"+mBean1.cycle+"12");
//                speak("引流不合格");
                if (currCycle < 10) {
                    APIServiceManage.getInstance().postApdCode("T0"+currCycle+"09");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "09");
                }
                break;
            case MSG_DRAIN_RINSE_START: {//引流返回: 当前冲洗量、冲洗次数
                Log.e("处理引流数据", "12、引流小冲开始: " + myGson.toJson(receiveBean.publish));
                ReceiveDrainRinseStartBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainRinseStartBean.class);
//                speak("引流小冲开始");
                if (currCycle < 10) {
                    APIServiceManage.getInstance().postApdCode("T0"+currCycle+"12");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "12");
                }
//                APIServiceManage.getInstance().postApdCode("T"+mBean.cycle+"12");
                break;
            }
            case MSG_DRAIN_RINSE_RUNNING: {//引流返回: 引流小冲进行时
                Log.e("处理引流数据", "13、引流小冲进行中: " + myGson.toJson(receiveBean.publish));
                ReceiveDrainRinseRunningBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainRinseRunningBean.class);

                if (mBean != null) {
                    Log.e("处理引流数据", "当前留腹量 :" + mBean.retain + ",当前冲洗量 :" + mBean.rinse);
                    runOnUiThread(() -> {
                        if (null != mFragment3.getActivity()) {
                            mFragment3.setRinseData(String.valueOf(mBean.rinse), "0");//冲洗量和次数，未给次数
                        }
                    });
                }

                break;
            }
            case MSG_DRAIN_RINSE_FINISH: {//引流返回: 引流小冲完成
                ReceiveDrainRinseFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainRinseFinishBean.class);
                Log.e("处理引流数据", "14、引流小冲完成 :" + myGson.toJson(receiveBean.publish));
                //{"data":{"cycle":3.0,"rinseCount":1.0,"remainRinseTimes":2.0,"rinse":78.0,"totalRinse":78.0,"retain":85.0,"drainTarget":608.0,"drainPass":608.0},"msg":"drain rinse finish","topic":"treatment/drain"}
                if (mBean != null) {
                    if (null == mHistoryBean) {
                        mHistoryBean = mTreatmentDataList.get(currCycle);
                    }
                    mHistoryBean.drainTargetVolume = mBean.drainTarget;
                    flushVolume = mBean.rinse;
                    if (mBean.cycle == 1) {
                        auxiliaryFlushingVolume += mBean.rinse + "";
                    } else if (mBean.cycle > 1) {
                        auxiliaryFlushingVolume +=  "," +  mBean.rinse ;
                    }
//                    runOnUiThread(() -> {
//                        if (null != mFragment3.getActivity()) {
//                            mFragment3.setDrainTargetVolume(currCycle, mBean.drainTarget + "");
//                        }
//                    });
                }
//                speak("引流小冲完成");
                break;
            }
            case MSG_DRAIN_RINSE_FAILURE: //引流返回: 引流小冲失败
                Log.e("处理引流数据", "15、引流小冲失败 :" + myGson.toJson(receiveBean.publish));
                runOnUiThread(this::drainFlushFaultDialog);
//                speak("引流小冲失败");
                break;
            case MSG_VACCUM_DRAIN_START: {//引流返回: 负压引流任务
                Log.e("处理引流数据", "16、开始负压引流: " + myGson.toJson(receiveBean.publish));
//                speak("开始负压引流");
                speak("请改变体位或检查引流管路是否打折和压迫");
                runOnUiThread(()->{
                    if (currCycle < 10) {
                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"51");
                    } else {
                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "51");
                    }
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"51");
                    isVacDrain = true;
                    ivVolume.setBackgroundResource(R.drawable.neo_zl);
                    ivExtraState.setVisibility(View.VISIBLE);
                    ivExtraState.setBackgroundResource(R.drawable.drain_zl);
                });
                ReceiveVaccumDrainStartBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveVaccumDrainStartBean.class);
                break;
            }
            case MSG_VACCUM_DRAIN_FINISH: {//引流返回: 负压引流结束
                Log.e("处理引流数据", "17、负压引流完成: " + myGson.toJson(receiveBean.publish));
//                speak("负压引流完成");
                ReceiveVaccumDrainFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveVaccumDrainFinishBean.class);

                runOnUiThread(()->{
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"51");
                    ivVolume.setBackgroundResource(R.drawable.drain_zl);
                    ivExtraState.setVisibility(View.GONE);
                    drainage =  mBean.drain;
                    tvCurrVolume.setText(String.valueOf(mBean.drain));
//                    tvCurrVolumeLabel.setText(String.valueOf(drainage));
                });
                break;
            }
            case MSG_FINAL_EMPTY_START: {//引流返回: 开始排空等待(非TPD模式或者TPD模式)
                Log.e("处理引流数据", "18、开始末次引流排空: " + myGson.toJson(receiveBean.publish));
                ReceiveCycleBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveCycleBean.class);
                runOnUiThread(() -> {
                    startLoopLastDrainCountDown();
                    lastDrainDialog();
                });
//                speak("开始末次引流排空");
                break;
            }
            case MSG_DRAIN_POOR: //引流返回: 引流不足
                Log.e("处理引流数据", "引流不足:" + myGson.toJson(receiveBean.publish));
                speak("请改变体位");
                break;
                default:
                Log.e("处理引流数据", "other、引流:" + myGson.toJson(receiveBean.publish));
                break;
        }

    }

    private boolean isVacDrain;


    /**
     * 处理实时留腹量
     *
     * @param mCurrentVolume
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_TREATMENT_WAITING_CURRENT_VOLUME)
    public void receiveCurrenWaitingVolume(String mCurrentVolume) {
        Log.e("处理实时留腹量", "实时留腹量: " + mCurrentVolume);
        runOnUiThread(() -> {
            mHistoryBean = mTreatmentDataList.get(currCycle);
            mHistoryBean.waitingVolume = Integer.parseInt(mCurrentVolume);
            if (null != fragmentItem2.getActivity()) {
                fragmentItem2.setHistoricalData(mTreatmentDataList);
            }
            if (null != mFragment3.getActivity()) {
                mFragment3.setWaittingVolume(mCurrentVolume);
            }
        });
    }

    private void erasure() {
        if (!MyApplication.isBuzzerOff) {
            buzzerOff();
//                            if (isBuzzerOff) {
            currCountdown = 0;
            silencersLayout.setVisibility(View.VISIBLE);
            disposable = Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .take(maxCountdown - currCountdown)
                    .subscribe(aLong -> {
                        currCountdown++;
                        MyApplication.isBuzzerOff = true;
                        tvSilencers.setText("消音中("+(maxCountdown - currCountdown)+"s)");
                    }, throwable -> {

                    }, () -> {
                        //complete
                        runOnUiThread(()->{
                            MyApplication.isBuzzerOff = false;
                            currCountdown = 0;
                            if (isShowAlarmDialog) {
                                buzzer();
                            }
                            silencersLayout.setVisibility(View.GONE);
                        });
                    });
            if (soundCompositeDisposable == null) {
                soundCompositeDisposable = new CompositeDisposable();
            }
            soundCompositeDisposable.add(disposable);
        }
    }

    private void buzzer() {
        currCountdown = 0;
        MyApplication.isBuzzerOff = false;
        silencersLayout.setVisibility(View.GONE);
        buzzerOn();
    }

    private boolean isSleep; // 是否休眠
    /**
     * 引流自检失败
     */
    private void drainSelfCheckFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        buzzer();
        isShowAlarmDialog = true;
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_drain_selfcheck))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_drain), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment_selfcheck), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过引流
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    buzzerOff();
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd("treatment/failure_continue"));
                    //继续治疗
                    buzzerOff();
                    isShowAlarmDialog = false;
//                    sendCommandInterval(CommandDataHelper.getInstance().continueTreatmentCmd(), 200);
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    // mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    buzzerOff();
                    finishTreatmentDialog(3);
                })
                .show();
    }

    /**
     * 灌注自检失败
     */
    private void perfusionSelfCheckFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        buzzer();
        isShowAlarmDialog = true;
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_perfusion_selfcheck))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_perfusion), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment_selfcheck), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    isShowAlarmDialog = false;
                    sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP), 200);
                    buzzerOff();
                    mCommonDialog13.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    //继续治疗
                    buzzerOff();
                    isShowAlarmDialog = false;
//                    sendCommandInterval(CommandDataHelper.getInstance().continueTreatmentCmd(),200);
//                    sendCommandInterval(CommandDataHelper.getInstance().continueTreatmentCmd(), 200);
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    buzzerOff();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(1);
                })
                .show();
    }

    /**
     * 引流灌注超时报警
     */
    private void timeoutDialog(String msg) {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        buzzer();
//        if (currCycle < 10) {
//            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"29");
//        } else {
//            APIServiceManage.getInstance().postApdCode("T" + currCycle + "29");
//        }
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"29");
        speak(msg);
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(msg)
                .setBtnFirst("长按跳过", R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    buzzerOff();
                    isShowAlarmDialog = false;
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"26");
                    mCommonDialog13.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(1);
                    buzzerOff();
                })
                .show();

    }

    /**
     * 灌注不畅故障
     */
    private void perfusionFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        buzzer();
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"29");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "29");
        }
        isShowAlarmDialog = true;
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"29");
        speak("灌注不畅");
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
                    if (currCycle < 10) {
                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"26");
                    } else {
                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "26");
                    }
                    isShowAlarmDialog = false;
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"26");
                    mCommonDialog13.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(1);
                    buzzerOff();
                })
                .show();

    }

    private TreatmentAlarmDialog drainValveFaultAlarmDialog;
    // 引流阀故障
    private void drainValveFaultDialog() {

        if (drainValveFaultAlarmDialog != null && drainValveFaultAlarmDialog.isShowing()) {

        } else {
            if (isSleep) {
                simulateKey(82);
            }
            isShowAlarmDialog = true;
            buzzer();
            speak("引流阀门故障");
            drainValveFaultAlarmDialog = new TreatmentAlarmDialog(this);
            drainValveFaultAlarmDialog.setImageResId(R.drawable.icon_8_tis)
                    .setMessage("引流阀出现故障,请检查引流阀")
                    .setBtnFirst(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_blue)
                    .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                    .setBtnTwo("强制跳过", R.drawable.dialog_btn_yellow)
                    .setFirstLongClickListener(mCommonDialog1 -> {
//                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
                        refreshPauseOrResume();
                        buzzerOff();
                        isShowAlarmDialog = false;
                        mCommonDialog1.dismiss();
                    })
                    .silencersClickListener(mCommonDialog14 -> {
                        erasure();
                    })
                    .setThreeClickListener(mCommonDialog12 -> {
                        //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
                        buzzerOff();
                        isShowAlarmDialog = false;
                        mCommonDialog12.dismiss();
                    })

                    .setTwoClickListener(mCommonDialog13 -> {
                        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                        buzzerOff();
                        isShowAlarmDialog = false;
                        mCommonDialog13.dismiss();
                    });
            if (!TreatmentFragmentActivity.this.isFinishing()) {
                drainValveFaultAlarmDialog.show();
            }
        }
    }

    /**
     * 引流小冲失败故障
     */
    private void drainFlushFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        buzzer();
        isShowAlarmDialog = true;
        speak("引流小冲不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_drain));
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage("引流小冲不畅,可检查人体和引流管路\\n并尝试改变体位后继续治疗")
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_drain), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过引流
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    buzzerOff();
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    // mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(3);
                    buzzerOff();
                })
                .show();
    }

    /**
     * 引流不畅故障
     */
    private void drainFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        buzzer();
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"19");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "19");
        }
        isShowAlarmDialog = true;
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"19");
        speak("引流不畅");
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
                    isShowAlarmDialog = false;
                    if (currCycle < 10) {
                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"16");
                    } else {
                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "16");
                    }
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"16");
                    mCommonDialog13.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    // mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(3);
                    buzzerOff();
                })
                .show();
    }

    /**
     * 灌注补液不畅
     */
    private void supplyFaultDialog2() {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        buzzer();
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"49");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "49");
        }
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"49");
        speak("补液不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_supply));
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_supply))
                .setBtnFirst("长按跳过灌注", R.drawable.dialog_btn_blue)
                .setBtnTwo("终止治疗", R.drawable.dialog_btn_red)
                .setBtnThree("继续补液", R.drawable.dialog_btn_green)
                .setFirstLongClickListener(dialog -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    buzzerOff();
                    //强制跳过窗口
                    dialog.dismiss();
                })
                .setTwoClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
                    buzzerOff();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    mCommonDialog1.dismiss();
                })
                .show();
    }

    /**
     * 补液不畅故障
     */
    private void supplyFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        buzzer();
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"49");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "49");
        }
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"49");
        speak("补液不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_supply));
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_supply))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_complete_not_supply), R.drawable.dialog_btn_blue)
                .setBtnTwo("", R.drawable.white_shape)
                .setBtnThree("继续补液", R.drawable.dialog_btn_green)
                .setFirstLongClickListener(dialog -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd("treatment/nosupply"));
                    buzzerOff();
//                    sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP),200);
                    dialog.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    buzzerOff();
                    mCommonDialog1.dismiss();
                })
                .show();
    }

    /**
     * 最末引流提醒
     */
    private void lastDrainDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setTitle(getResources().getString(R.string.dialog_title))
                .setMessage(getResources().getString(R.string.dialog_status_fault_drain_last))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_complete_last_drain), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_view_data), R.drawable.dialog_btn_green)
                .setFirstLongClickListener(mCommonDialog1 -> {
//                    sendCommandInterval(CommandSendConfig.TREATMENT_DRAIN_EMPTY_WAITING_END, 200);
                    stopLoopLastDrainCountDown();
                    isShowAlarmDialog = false;
                    //完成最末引流
                    mCommonDialog1.dismiss();
                })
                .setTwoLongClickListener(mCommonDialog12 -> {
//                    buzzerOff();
                    //暂停治疗
                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
                    startLoopLastDrainCountDown();
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"00");
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog13 -> {
//                    buzzerOff();
                    //查看数据
                    switchViewData();
                    startLoopLastDrainCountDown();
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .show();
    }

    /**
     * 灌注警戒值警报
     */
    private void perfusionMaxValueDialog() {
        if (isSleep) {
            simulateKey(82);
        }
//        buzzer();
        isShowAlarmDialog = true;
        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
        mCommonDialog
                .setTitle(getResources().getString(R.string.dialog_title))
                .setMessage(getResources().getString(R.string.dialog_status_fault_perfusion_max))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_force_skip_window), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_view_data), R.drawable.dialog_btn_green)
//                .setCountdown(true, 31)//倒计时秒数
                .setFirstLongClickListener(mCommonDialog1 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));

                    buzzerOff();
                    //强制跳过窗口
                    mCommonDialog1.dismiss();
                    isShowAlarmDialog = false;
                    if (currCycle < 10) {
                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"02");
                    } else {
                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "02");
                    }
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"02");
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoLongClickListener(mCommonDialog12 -> {
                    //暂停治疗
                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
                    buzzerOff();
                    mCommonDialog12.dismiss();
                    isShowAlarmDialog = false;
                })
                .setThreeClickListener(mCommonDialog13 -> {
                    isShowAlarmDialog = false;
                    buzzerOff();
                    //查看数据
                    switchViewData();
                    mCommonDialog13.dismiss();
                })
                .show();
    }


    /**
     * 低温预警
     */
    private void showLowTemperatureAlertDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        buzzer();
        isShowAlarmDialog = true;
        speak(getResources().getString(R.string.dialog_status_fault_temperature_low));
        if (lowTemperatureDialog == null) {
            lowTemperatureDialog = new TreatmentAlarmDialog(this);
        }
        if (lowTemperatureDialog != null && lowTemperatureDialog.isShowing()) {//低温警告弹窗还在时，不再次弹出
            return;
        }
        lowTemperatureDialog
                .setImageResId(R.drawable.icon_8_jinbao)
                .setMessage(getResources().getString(R.string.dialog_status_fault_temperature_low))
//                .setBtnViewDataTxt(getResources().getString(R.string.dialog_btn_view_data))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_force_skip_window), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setViewDataClickListener(mCommonDialog1 -> {
                    buzzerOff();
                    //查看数据
                    switchViewData();
                    isShowAlarmDialog = false;
                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog12 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));

                    buzzerOff();
                    //强制跳过窗口
                    isShowAlarmDialog = false;
//                    CommandDataHelper.getInstance().continueTreatmentCmd();
                    mCommonDialog12.dismiss();
                })
                .silencersClickListener(mCommonDialog -> erasure())
                .setTwoLongClickListener(mCommonDialog13 -> {
                    //暂停治疗
                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
                    buzzerOff();
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .setThreeClickListener(mCommonDialog14 -> {
                    //终止治疗
                    //  mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
                    buzzerOff();
                })
                .show();

    }

    /**
     * 高温预警
     */
    private void showHeatAlertDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        buzzer();
        speak(getResources().getString(R.string.dialog_status_fault_temperature_high));
        if (highTemperatureDialog == null) {
            highTemperatureDialog = new TreatmentAlarmDialog(this);
        }
        if (highTemperatureDialog != null && highTemperatureDialog.isShowing()) {//高温警告弹窗还在时，不再次弹出
            return;
        }
        highTemperatureDialog
                .setImageResId(R.drawable.icon_8_jinbao)
                .setMessage(getResources().getString(R.string.dialog_status_fault_temperature_high))
//                .setBtnViewDataTxt(getResources().getString(R.string.dialog_btn_view_data))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_force_skip_window), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setViewDataClickListener(mCommonDialog1 -> {
                    //查看数据
                    buzzerOff();
                    switchViewData();
                    isShowAlarmDialog = false;
                    mCommonDialog1.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setFirstLongClickListener(mCommonDialog12 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));

                    buzzerOff();
                    //强制跳过窗口
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setTwoLongClickListener(mCommonDialog13 -> {
                    buzzerOff();
                    //暂停治疗
                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .setThreeClickListener(mCommonDialog14 -> {
                    //终止治疗
                    //  mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
                    buzzerOff();
                })
                .show();
    }

    /**
     * AC掉电提醒
     */
    private void showPowerOffDialog() {
        isShowAlarmDialog = true;
        if (isSleep) {
            simulateKey(82);
        }
        APIServiceManage.getInstance().postApdCode("E0012");
        buzzer();
        speak(getResources().getString(R.string.dialog_status_fault_ac_power_off));
        powerOffDialog = new TreatmentAlarmDialog(this);
        powerOffDialog
                .setImageResId(R.drawable.icon_8_jinbao)
                .setMessage(getResources().getString(R.string.dialog_status_fault_ac_power_off))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setBtnThree("继续治疗",R.drawable.dialog_btn_yellow)
                .setFirstLongClickListener(mCommonDialog12 -> {
                    refreshPauseOrResume();
                    isShowAlarmDialog = false;
                    buzzerOff();
                    //强制跳过窗口
                    powerOffDialog.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //终止治疗
                    //  mCommonDialog.dismiss();
//                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
                    buzzerOff();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    buzzerOff();
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    APIServiceManage.getInstance().postApdCode("E0011");
                    powerOffDialog.dismiss();
                })
                .show();
    }

    /**
     * AC掉电超过30分钟报警
     */
    private void showPowerFaultDialog() {
        isShowAlarmDialog = true;
        if (isSleep) {
            simulateKey(82);
        }
        buzzer();
        speak("掉电超过30分钟");
        powerFaultDialog = new TreatmentAlarmDialog(this);
        powerFaultDialog
                .setImageResId(R.drawable.icon_8_jinbao)
                .setMessage(getResources().getString(R.string.dialog_status_fault_ac_power_fault))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setBtnTwo("", R.drawable.white_shape)
                .setBtnThree("继续治疗",R.drawable.dialog_btn_yellow)
                .setFirstClickListener(mCommonDialog12 -> {
                    //终止治疗
                    //  mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
                    buzzerOff();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog -> {
                    isShowAlarmDialog = false;
                    buzzerOff();
                    mCommonDialog.dismiss();
                })
                .show();
    }
    private void addAPD() {
        String content = myGson.toJson(mTreatmentDataUpload);
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
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse<RBean>> call, @NonNull Throwable t) {
                saveFaultCodeLocal("治疗数据上传,"+t.getMessage());
            }
        });
    }

    private void postData() {
        getTreatmentDataUpload();
        addAPD();
    }

    /**
     * 治疗完成
     */
    private void showCompleteDialog() {
//        buzzerOff();
//        initDb();
        isComplete = true;
        MyApplication.mEndTime = EmtTimeUil.getTime();
        postData();
//        if (PdproHelper.getInstance().getUserParameterBean().awaken > 0) {
//            startCountDown(PdproHelper.getInstance().getUserParameterBean().awaken);
//        }
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"08");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "08");
        }
        speak("治疗结束,请取下人体端,关闭所有管夹");
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"09");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "09");
        }
        doGoCloseTOActivity(TreatmentEvaluationActivity.class,"");
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"08");
//        TreatmentAlarmDialog mCommonDialog = new TreatmentAlarmDialog(this);
//        mCommonDialog
//                .setImageResId(R.drawable.icon_8_dagou)
//                .setStatus(getResources().getString(R.string.dialog_status_complete))
//                .setMessage(getResources().getString(R.string.dialog_status_complete_tip), R.color.dialog_text_default)
//                .setBtnFirst(getResources().getString(R.string.dialog_btn_view_data), R.drawable.dialog_btn_blue)
//                .setBtnThree("直接下机", R.drawable.dialog_btn_green)
//                .setFirstClickListener(mCommonDialog12 -> {
//                    //查看数据
//                    switchViewData();
//                    speak("治疗结束,请取下人体端,关闭所有管夹");
//                    mCommonDialog12.dismiss();
//                })
//                .setThreeClickListener(mCommonDialog1 -> {
////                    doGoTOActivity(DataCollectionActivity.class);
//                    speak("治疗结束,请取下人体端,关闭所有管夹");
//                    if (currCycle < 10) {
//                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"09");
//                    } else {
//                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "09");
//                    }
////                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"09");
//                    doGoCloseTOActivity(TreatmentEvaluationActivity.class,"");
//                    //确定
//                    mCommonDialog1.dismiss();
//                })
//                .show();
    }


    /**
     * 暂停治疗 or 恢复治疗
     */
    private void showPauseDialog() {

        String tips = isPause ? "是否恢复治疗" : "是否暂停治疗";

        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent(tips)
                .setBtnFirst("确定")
                .setBtnTwo("取消")
                .setFirstClickListener(mCommonDialog -> {
                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(Dialog::dismiss)
                .show();

    }

    /**
     * 手动点击结束治疗按钮时候，提示"是否提前结束治疗"弹窗
     */
    private void earlyFinishTreatmentDialog() {
        String tips = isComplete ? "是否结束治疗" : getResources().getString(R.string.dialog_status_early);
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent(tips)
                .setBtnFirst("确定")
                .setBtnTwo("取消")
                .setFirstClickListener(mCommonDialog -> {
                    if (!isComplete) {
                        if (isShowAlarmDialog) {
                            RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_CLOSED_ALARM_DIALOG, "");
                        }
                        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
                        isShowAlarmDialog = false;
                    } else {
                        doGoCloseTOActivity(TreatmentEvaluationActivity.class,"");
                    }
//                    isComplete = true;
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(Dialog::dismiss)
                .show();

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
                    if (isShowAlarmDialog) {
                        RxBus.get().send(RxBusCodeConfig.EVENT_TREATMENT_CLOSED_ALARM_DIALOG, "");
                    }
//                    if (1 == mStage) {//灌注阶段执行终止治疗
////                        sendCommandInterval(CommandSendConfig.TREATMENT_START_PERFUSION_FAULT_SHUTDOWN, 200);
//                    } else if (3 == mStage) {//引流阶段执行终止治疗
////                        sendCommandInterval(CommandSendConfig.TREATMENT_START_DRAIN_FAULT_SHUTDOWN, 200);
//                    } else if (4 == mStage) {//补液阶段执行终止治疗
////                        sendCommandInterval(CommandSendConfig.TREATMENT_START_SUPPLYFAULT_SHUTDOWN, 200);
//                    }
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
                    isShowAlarmDialog = false;
                    MyApplication.isDebugFinish = true;
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(Dialog::dismiss)
                .show();

    }

    /**
     * 跳过当前阶段:跳过灌注、跳过留腹、跳过引流
     */
    private void skipCurrentStageDialog() {
        String tip = "";
        if (currentStage == 1) {//灌注
            tip = "确定跳过灌注？";
        } else if (currentStage == 2) {//留腹
            tip = "确定跳过留腹？";
        } else if (currentStage == 3) {//引流
            tip = "确定跳过引流？";
            if (isDrainManualEmptying) {
                tip = "确定跳过最末引流排空等待？";
            }
        }

        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent(tip)
                .setBtnFirst("确定")
                .setBtnTwo("取消")
                .setFirstClickListener(mCommonDialog -> {
//                    if (currentStage == 3 && isDrainManualEmptying) {//引流
////                        sendCommandInterval(CommandSendConfig.TREATMENT_DRAIN_EMPTY_WAITING_END, 200);
//                        isDrainManualEmptying = false;
//                        stopLoopLastDrainCountDown();
//                    } else {
////                        sendCommandInterval(CommandSendConfig.TREATMENT_START_PRECONTINUE, 1000);
//                    }
//                    sendCommandInterval(CommandDataHelper.getInstance().continueTreatmentCmd(), 200);
                    if (currentStage == 1) {//灌注
                        if (currCycle < 10) {
                            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"26");
                        } else {
                            APIServiceManage.getInstance().postApdCode("T" + currCycle + "26");
                        }
//                       APIServiceManage.getInstance().postApdCode("T"+currCycle+"26");
                    } else if (currentStage == 2) {//留腹
                        if (currCycle < 10) {
                            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"36");
                        } else {
                            APIServiceManage.getInstance().postApdCode("T" + currCycle + "36");
                        }
//                        APIServiceManage.getInstance().postApdCode("T"+currCycle+"36");
                    } else if (currentStage == 3) {//引流
                        if (currCycle < 10) {
                            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"16");
                        } else {
                            APIServiceManage.getInstance().postApdCode("T" + currCycle + "16");
                        }
//                        APIServiceManage.getInstance().postApdCode("T"+currCycle+"16");
                    }
//                    switchTreatmentStatus(currentStage);
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(Dialog::dismiss)
                .show();

    }


    /**
     * 开始cmd指令应答轮询计时
     */
    private void startCmdLoopTimer() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {

            @Override
            public void onNext(Long data) {

                runOnUiThread(() -> {
                    sendCommandInterval(command, 200);
                });

            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("指令应答开始轮询计时", "DisposableObserver, onError=" + throwable);
            }

            @Override
            public void onComplete() {
                Log.e("指令应答开始轮询计时", "DisposableObserver, onComplete");
            }
        };
        Observable.interval(0, 5000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        cmdDisposable.add(disposableObserver);
    }

    /**
     * 停止指令应答轮询获取计时
     */
    private void stopCmdLoopTimer() {
        cmdDisposable.clear();
    }
    private int time = 0;
    private void initTime() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    if (!isComplete) {
                        time ++ ;
//                        Log.e("计时开始", "time--" + time);
                        totalDrainVolume = 0;
                        totalPerfusionVolume = 0;
                        MyApplication.ApdTreatTime = EmtTimeUil.getTime(time);
                        if (mTreatmentDataList.size() > 0) {
                            for (int i = 0; i <= currCycle; i++) {
                                TreatmentHistoryBean mBean = mTreatmentDataList.get(i);
                                totalDrainVolume += mBean.drainVolume;
                                totalPerfusionVolume += mBean.perfusionVolume;
                            }
                            tvTotalPerfusionVolume.setText(String.valueOf(totalPerfusionVolume));
                            tvTotalDrainVolume.setText(String.valueOf(totalDrainVolume));
                            tvTotalUltVol.setText(String.valueOf(totalDrainVolume-totalPerfusionVolume));
                            mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume;
                            MyApplication.ApdTotalDrainVol = totalDrainVolume;
                            MyApplication.ApdTotalPerVol = totalPerfusionVolume;
                            MyApplication.ApdTotalUltVol = totalDrainVolume-totalPerfusionVolume;
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
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        compositeDisposable.add(disposableObserver);
    }

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
                        if (1 == currentStage) {
                            currPerfusionTime++;
                            tvCurrTime.setText(EmtTimeUil.getTime(currPerfusionTime));
//                            if (currPerfusionTime == PdproHelper.getInstance().getDrainParameterBean().drainTimeoutAlarm * 60) {
//                                if (!isShowAlarmDialog) {
//                                    timeoutDialog("灌注超时");
//                                }
//                            }
//                        Log.e("开始轮询计时", "currPerfusionTime: " + currPerfusionTime);
                        } else if (3 == currentStage) {
                            currDrainTime++;
//                            if (currDrainTime == PdproHelper.getInstance().getDrainParameterBean().drainTimeoutAlarm * 60) {
//                                if (!isShowAlarmDialog) {
//                                    timeoutDialog("引流超时");
//                                }
//                            }
                            tvCurrTime.setText(EmtTimeUil.getTime(currDrainTime));
//                        Log.e("开始轮询计时", "currDrainTime: " + currDrainTime);
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
        mCompositeDisposable.add(disposableObserver);
    }

    /**
     * 暂停/恢复轮询获取计时
     */
    private void suspendLoopTimer(boolean isPause) {
        isTiming = isPause;
    }

    /**
     * 停止轮询获取计时
     */
    private void stopLoopTimer() {
        isTiming = false;
        mCompositeDisposable.clear();
    }

    /**
     * 开始留腹倒计时
     */
    private void startLoopCountDown() {
        countDownDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(currRetainTime)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
//                        if (!isPause) {
                            currRetainTime--;
//                        if (isPause) {
//                            currRetainTime--;
//                        }
//                        Log.e("startLoopCountDown", "currWaitingTime:" + currWaitingTime);
                            runOnUiThread(() -> {
                                        String currTime = EmtTimeUil.getTime(currRetainTime);
                                        tvCurrRetainTime.setText(currTime);
                                        if (null != mFragment3.getActivity()) {
                                            mFragment3.setWaittingTime(currTime);
                                        }
                                    }
                            );
//                        }
                    }
                }, throwable -> {

                }, () -> {
                    //complete
                    Log.e("startLoopCountDown", "留腹倒计时完成");
                    currRetainTime = 0;
                    runOnUiThread(() -> {
                        String currTime = EmtTimeUil.getTime(currRetainTime);
                        if (null != mFragment3.getActivity()) {
                            mFragment3.setWaittingTime(currTime);
                        }
                    });
                });
        mCompositeDisposable.add(countDownDisposable);
    }

    /**
     * 手动结束留腹倒计时
     */
    private void stopLoopCountDown() {
        isTiming = false;
        if (countDownDisposable != null && !countDownDisposable.isDisposed()) {
            countDownDisposable.dispose();
            countDownDisposable = null;
            mCompositeDisposable.clear();
        }
    }

    /**
     * 开始最末引流提醒倒计时
     */
    private void startLoopLastDrainCountDown() {
        isTiming = true;
        currAlarmTimeInterval = entity.abdomenRetainingVolumeLastTime * 60;
        lastDrainDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(entity.abdomenRetainingVolumeLastTime * 60)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        currAlarmTimeInterval--;
                    }
                }, throwable -> {

                }, () -> {
                    //complete
                    Log.e("CountDown", "最末引流提醒倒计时结束");
                    lastDrainDialog();
                });
        mCompositeDisposable.add(lastDrainDisposable);
    }

    /**
     * 最末引流提醒倒计时结束
     */
    private void stopLoopLastDrainCountDown() {
        Log.e("CountDown", "手动最末引流提醒倒计时结束");
        if (lastDrainDisposable != null && !lastDrainDisposable.isDisposed()) {
            lastDrainDisposable.dispose();
            lastDrainDisposable = null;
            mCompositeDisposable.clear();
        }
    }

    /**
     * 刷新暂停治疗的状态
     */
    private void refreshPauseOrResume() {
        suspendLoopTimer(isPause);
        isPause = !isPause;
        runOnUiThread(() -> {
            ivPause.setVisibility(isPause ? View.GONE : View.VISIBLE);
            ivResume.setVisibility(isPause ? View.VISIBLE : View.GONE);
            tvPause.setText(isPause ? "恢复" : "暂停");
//            tvPause.setTextColor(isPause ? Color.RED : Color.WHITE);
            layoutPause.setBackgroundResource(isPause ? R.drawable.btn_bg_contact_engineer_normal : R.drawable.btn_bg_pause);
            if (isPause) {
                if (currCycle < 10) {
                    APIServiceManage.getInstance().postApdCode("T0"+currCycle+"01");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "01");
                }
//                APIServiceManage.getInstance().postApdCode("T"+currCycle+"01");
            } else {
                if (currCycle < 10) {
                    APIServiceManage.getInstance().postApdCode("T0"+currCycle+"00");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "00");
                }
//                APIServiceManage.getInstance().postApdCode("T"+currCycle+"00");
            }
//            if (currentStage == 2) {
//                isRetainTimePause = !isPause;
//            }
//            btnFinishTreatment.setVisibility(!isDrainManualEmptying && isPause ? View.VISIBLE : View.INVISIBLE);//最末排空等待时候，不显示结束治疗按钮
            sendToMainBoard(CommandDataHelper.getInstance().customCmd(isPause ? CommandSendConfig.METHOD_TREATMENT_PAUSE : CommandSendConfig.METHOD_TREATMENT_RESUME));
        });
    }

    /**
     * 查看数据····················
     */
    private void switchViewData() {
        switchFragment(0);
        showFragment(true);
    }


    /**
     * 刷新治疗的不同阶段界面：灌注、留腹、引流
     */
    private void switchTreatmentStatus(int mStage) {
        this.currentStage = mStage;
//        mCompositeDisposable.clear();
        runOnUiThread(() -> {
            String mPeriod = String.valueOf(currCycle);
            if (0 == currCycle) {
                mPeriod = " 0 ";
            } else if (10 > currCycle) {
//                mPeriod = "0" + currCycle;
                mPeriod = "" + currCycle;
            }
            tvPeriod.setText(mPeriod);
            if (1 == currentStage) {
                tvStatus.setText("灌注");
                tvCurrVolumeLabel.setText("当前灌注量");
                tvCurrTimeLabel.setText("灌注时间");
                tvCurrVolume.setText("0");
                tvCurrTime.setText("00:00");
                layoutCurrVolume.setVisibility(View.VISIBLE);
                layoutTimeAndTemp.setVisibility(View.VISIBLE);
                layoutCurrRetainTime.setVisibility(View.GONE);
                layoutCurrTemp.setVisibility(View.GONE);
                ivVolume.setBackgroundResource(R.drawable.treatment_icon_volume_day);
                ivExtraState.setVisibility(View.GONE);
//                if (!PdproHelper.getInstance().getUserParameterBean().isNight && !isSleep) {
////                    if (PdproHelper.getInstance().getUserParameterBean().isNight) {
//                        sendToMainBoard(CommandDataHelper.getInstance().LedOpen("perfuse", true,0,1));
////                    }
//                }
            } else if (2 == currentStage) {
                tvStatus.setText("留腹");
                tvCurrTimeLabel.setText("留腹时间");
                layoutCurrVolume.setVisibility(View.GONE);
                layoutTimeAndTemp.setVisibility(View.GONE);
                layoutCurrRetainTime.setVisibility(View.VISIBLE);
                layoutCurrTemp.setVisibility(View.VISIBLE);
//                if (!PdproHelper.getInstance().getUserParameterBean().isNight && !isSleep) {
////                    if (PdproHelper.getInstance().getUserParameterBean().isNight) {
//                    sendToMainBoard(CommandDataHelper.getInstance().LedOpen("retain", true,0,1));
////                    }
//                }
            } else if (3 == currentStage) {
                tvStatus.setText("引流");
                tvCurrVolumeLabel.setText("当前引流量");
                tvCurrTimeLabel.setText("引流时间");
                tvCurrVolume.setText("0");
                tvCurrTime.setText("00:00");
                ivVolume.setBackgroundResource(R.drawable.drain_zl);
                ivExtraState.setVisibility(View.GONE);
                layoutCurrVolume.setVisibility(View.VISIBLE);
                layoutTimeAndTemp.setVisibility(View.VISIBLE);
                layoutCurrRetainTime.setVisibility(View.GONE);
                layoutCurrTemp.setVisibility(View.GONE);
//                if (!PdproHelper.getInstance().getUserParameterBean().isNight && !isSleep) {
////                    if (PdproHelper.getInstance().getUserParameterBean().isNight) {
//                        sendToMainBoard(CommandDataHelper.getInstance().LedOpen("drain", true,0,1));
////                    }
//                }
            }
        });

//        if (1 == currentStage) {
//            currPerfusionTime = 0;
//            if (!PdproHelper.getInstance().getUserParameterBean().isNight) {
//                CommandDataHelper.getInstance().LedOpen("perfuse", true,0);
//            }
//            startLoopTimer();
//        }else
        if (2 == currentStage) {
            startLoopCountDown();
        }
//        else if (3 == currentStage) {
//            if (!PdproHelper.getInstance().getUserParameterBean().isNight) {
//                CommandDataHelper.getInstance().LedOpen("drain", true,0);
//            }
//            currDrainTime = 0;
//            startLoopTimer();
//        }
    }

    /**
     * 新增或者更新本地治疗数据:引流
     *
     * @param mBean
     */
    private void saveOrUpdateDrain(ReceiveDrainProcessFinishBean mBean) {

        try {
            mHistoryBean = mTreatmentDataList.get(currCycle);
//        refreshTreatmentHistoryData();
            TreatmentHistory mTreatmentHistory = PdGoDbManager.getInstance().findByUUIDAndPeriod(uuid, currCycle);
            if (mTreatmentHistory == null) {
                mTreatmentHistory = new TreatmentHistory();
            }
            mTreatmentHistory.uuid = uuid;
            mTreatmentHistory.sn = PdproHelper.getInstance().getMachineSN();
            mTreatmentHistory.cycle = mBean.cycle;
//        mTreatmentHistory.drainVolume = drainage;
            mTreatmentHistory.drainTime = mBean.workTime;
            mTreatmentHistory.drainTvVol = mBean.drainTarget;
            mTreatmentHistory.auFvVol = flushVolume;
            mTreatmentHistory.remain = mBean.drainRetain;
//        saveTreatmentHistory(mTreatmentHistory);
            if (isVacDrain) {
                mTreatmentHistory.drainVolume = drainage;
                mHistoryBean.drainVolume = drainage;
                isVacDrain = false;
            } else {
                mTreatmentHistory.drainVolume = mBean.drain;
                mHistoryBean.drainVolume = mBean.drain;
                drainage = mBean.drain;
            }
            mHistoryBean.cycle = mBean.cycle;
            mHistoryBean.drainTime = mBean.workTime;
            mHistoryBean.drainTargetVolume = mBean.drainTarget;
//        mHistoryBean.drainVolume = mBean.drain;
            mHistoryBean.rinsePerfusionVolume = mBean.drainTotalRinse;
            mHistoryBean.predictedResidualLiquidVolume = mBean.drainRetain;
            refreshTreatmentHistoryData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新本地治疗数据:灌注
     *
     * @param mBean
     */
    private void saveOrUpdatePerfusion(ReceivePerfuseProcessFinishBean mBean) {

        try {

            int perfusionTime = currPerfusionTime;
//        refreshTreatmentHistoryData();
            TreatmentHistory mTreatmentHistory = PdGoDbManager.getInstance().findByUUIDAndPeriod(uuid, currCycle);
            if (mTreatmentHistory == null) {
                mTreatmentHistory = new TreatmentHistory();
            }
            mTreatmentHistory.uuid = uuid;
            mTreatmentHistory.sn = PdproHelper.getInstance().getMachineSN();
            mTreatmentHistory.cycle = mBean.cycle;
            mTreatmentHistory.perfusionVolume = mBean.perfuse;
            mTreatmentHistory.perfusionTime = perfusionTime;
//        saveTreatmentHistory(mTreatmentHistory);
            mHistoryBean = mTreatmentDataList.get(mBean.cycle);
            mHistoryBean.cycle = mBean.cycle;
            mHistoryBean.perfusionVolume = mBean.perfuse;
            mHistoryBean.perfusionTime = perfusionTime;
            mHistoryBean.waitingVolume = mBean.retain;
            mHistoryBean.predictedResidualLiquidVolume = mBean.retain;
            refreshTreatmentHistoryData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存留腹数据
     *
     * @param mBean
     */
    private void saveRetain(ReceiveRetainProcessFinishBean mBean) {

        try {

            Disposable mainDisposable = Observable.timer(10, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            PdEntity.PdInfoEntity entity = new PdEntity.PdInfoEntity();
                            entity.cycle = currCycle;
                            entity.abdAfterVol = perRetain;
                            entity.abdTime = retainTime / 60;
                            entity.auFvVol = flushVolume;
                            entity.drainage = drainage;
                            entity.drainTime = drainTime / 60;
                            entity.preTime = perTime / 60;
                            entity.preVol = perVol;
                            entity.remain = drainRetain;
                            pdInfoEntities.add(entity);
                        }
                    });

            mCompositeDisposable.add(mainDisposable);

            mHistoryBean = mTreatmentDataList.get(currCycle);

            mHistoryBean.waitingTime = mBean.retainTime;

//        refreshTreatmentHistoryData();

            TreatmentHistory mTreatmentHistory = PdGoDbManager.getInstance().findByUUIDAndPeriod(uuid, currCycle);
            if (mTreatmentHistory == null) {
                mTreatmentHistory = new TreatmentHistory();
            }

            mTreatmentHistory.uuid = uuid;
            mTreatmentHistory.sn = PdproHelper.getInstance().getMachineSN();
            mTreatmentHistory.cycle = mBean.cycle;

            mTreatmentHistory.waitingTime = mBean.retainTime;
            mTreatmentHistory.abdAfterVol = mBean.retainVolume;

//        saveTreatmentHistory(mTreatmentHistory);

            refreshTreatmentHistoryData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增或者更新本地治疗数据:留腹
     *
     * @param mBean
     */
    private void saveOrUpdateWaiting(ReceiveRetainFinishBean mBean) {
        try {
            mHistoryBean.waitingTime = mBean.retainTime;
            refreshTreatmentHistoryData();
            TreatmentHistory mTreatmentHistory = PdGoDbManager.getInstance().findByUUIDAndPeriod(uuid, currCycle);
            mTreatmentHistory.waitingTime = mBean.retainTime;
            saveTreatmentHistory(mTreatmentHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTreatmentHistory(TreatmentHistory mHistory) {
        try {
            Disposable mainDisposable = Observable.timer(10, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mHistory.save();
                        }
                    });

            mCompositeDisposable.add(mainDisposable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新周期治疗数据
     */
    private void refreshTreatmentHistoryData() {
        runOnUiThread(() -> {
            if (null != fragmentItem2.getActivity()) {
                fragmentItem2.setHistoricalData(mTreatmentDataList);
            }
        });
    }

    private void alertNumberBoardDialog(String value, String type, boolean isMinus) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, isMinus);//"℃"
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                result = Float.valueOf(result) + "";//转换成浮点数
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE)) {//目标温度值  :  在34.0-40.0之间
                    if (!TextUtils.isEmpty(result)) {

                        MyApplication.mTargetTemper = Float.valueOf(result);
                        PdGoDbManager.getInstance().getTemperatureBoardTable().targetTemper = MyApplication.mTargetTemper;
                        //设置温度
                        sendToMainBoard(CommandDataHelper.getInstance().setTemperatureCmdJson((int) (MyApplication.mTargetTemper * 10)));

                    }
                }
            }
        });
    }
    public void RecoveryTouchEffect() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                //有了权限，具体的动作
                SharedPreferences setting = this.getSharedPreferences("SystemSetting", MODE_PRIVATE);
                int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", 0);
                AudioManager mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
                Settings.System.putInt(this.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, mSoundEffect);
                mAudioManager.loadSoundEffects();

            }

        }else{
            SharedPreferences setting = this.getSharedPreferences("SystemSetting", MODE_PRIVATE);
            int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", 0);
            AudioManager mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
            Settings.System.putInt(this.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, mSoundEffect);
            mAudioManager.loadSoundEffects();
        }

    }
    public void DisTouchEffectAndSaveState() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                //有了权限，具体的动作
                SharedPreferences setting = this.getSharedPreferences("SystemSetting", MODE_PRIVATE);
                int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", -1);

                if (mSoundEffect == -1) {
                    SharedPreferences.Editor editor = setting.edit();
                    mSoundEffect = Settings.System.getInt(this.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);

                    editor.putInt("SOUND_EFFECTS_ENABLED", mSoundEffect);
                    editor.commit();
                }

            }

        }else{
            SharedPreferences setting = this.getSharedPreferences("SystemSetting", MODE_PRIVATE);
            int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", -1);

            if (mSoundEffect == -1) {
                SharedPreferences.Editor editor = setting.edit();
                mSoundEffect = Settings.System.getInt(this.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);

                editor.putInt("SOUND_EFFECTS_ENABLED", mSoundEffect);
                editor.commit();
            }
        }

    }

    @OnClick({R.id.iv_logo,R.id.btnConfirm,
            R.id.layout_back,R.id.layout_pause, R.id.tv_update_temperature, R.id.tv_update_temp
    })
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.layout_back://
                if (!isComplete) {
                    // 单模式
//                    if (isConfirm) {
//                        isConfirm = false;
//                        if (null != fragmentItem1.getActivity()) {
//                            fragmentItem1.goBack( true);
//                        }
//                    } else {
//                        fragmentItem1.goBack( false);
//                    }
                    showFragment(false);
                }
                break;
//            case R.id.btn_finish_treatment:
//                if (isFastClick()) {
//                    return;
//                }
//                if (!isComplete) {
//                    earlyFinishTreatmentDialog();
//                } else {
////                    doGoTOActivity(DataCollectionActivity.class);
//                    doGoTOActivity(TreatmentEvaluationActivity.class);
//                }
//                break;
            case R.id.layout_pause:
                if (isFastClick()) {
                    return;
                }
                if (!isComplete) {
                    showPauseDialog();
                }
                break;
            case R.id.tv_update_temp:
            case R.id.tv_update_temperature:
                if (!isComplete) {
                    alertNumberBoardDialog(String.valueOf(MyApplication.mTargetTemper), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE, true);
                }
                break;
            case R.id.btnConfirm:
                if (maxCycle > currCycle ) {
                        final CommonDialog dialog = new CommonDialog(this);
                        dialog.setContent("是否修改")
                                .setBtnFirst("确定")
                                .setBtnTwo("取消")
                                .setFirstClickListener(mCommonDialog -> {
                                    isConfirm = true;
                                    revise();
                                    modifyDuringTreatment(currCycle);
                                    // 单模式
//                                    modify();
                                    mCommonDialog.dismiss();
                                })
                                .setTwoClickListener(mCommonDialog -> {
                                    isConfirm = false;
                                    mCommonDialog.dismiss();
                                });
                        if (!TreatmentFragmentActivity.this.isFinishing()) {
                            dialog.show();
                        }
                    } else {
                        toastMessage("治疗周期数要大于当前周期");
                    }
        }
    }

    public boolean isConfirm;

//    public void amend() {
//        if (treatmentParameterEniity.firstPerfusionVolume == 0) {
//
//        } else {
//            if (treatmentParameterEniity.firstPerfusionVolume >= treatmentParameterEniity.perCyclePerfusionVolume) {
//
//            } else {
//                toastMessage("每周期灌注量不能大于首次灌注量");
//            }
//        }
//    }

    @BindView(R.id.silencersLayout)
    LinearLayout silencersLayout;
    @BindView(R.id.tvSilencers)
    TextView tvSilencers;
    @BindView(R.id.btnSound)
    ImageView btnSound;

    @OnLongClick({R.id.layout_skip})
    public boolean onViewLongClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_skip:
                if (!isComplete && !isPause) {
                    skipCurrentStageDialog();
                    if (currCycle < 10) {
                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"02");
                    } else {
                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "02");
                    }
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"02");
                } else if (isPause){
                    showTipsCommonDialog("处于暂停状态，需要解除暂停后，方可跳过进入下一步");
                }
                break;
        }

        return false;
    }

    @BindView(R.id.tc_time)
    TextClock textClock;
    @BindView(R.id.totalDrainRl)
    RelativeLayout totalDrainRl;
    @BindView(R.id.tvTotalDrain)
    TextView tvTotalDrain;
    @BindView(R.id.tv_total_drain_volume_ml)
    TextView tv_total_drain_volume_ml;
    @BindView(R.id.totalPerRl)
    RelativeLayout totalPerRl;
    @BindView(R.id.totalPerTv)
    TextView totalPerTv;
    @BindView(R.id.tv_total_perfusion_volume_ml)
    TextView tv_total_perfusion_volume_ml;

    @Override
    public void notifyByThemeChanged() {

        runOnUiThread(() -> {

            MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());

            helper.setBackgroundResourceByAttr(layoutMain, R.attr.custom_attr_treatment_main_right_bg);
            helper.setBackgroundResourceByAttr(layoutLeft, R.attr.custom_attr_treatment_main_left_bg);

            helper.setTextColorByAttr(tvPeriod, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvPeriodLabel, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvStatus, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvNitCycle, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvMaxCycle, R.attr.custom_attr_common_text_color);


            helper.setBackgroundResourceByAttr(totalUrlRl, R.attr.custom_attr_treatment_item_volume_bg);
            helper.setBackgroundResourceByAttr(totalDrainRl, R.attr.custom_attr_treatment_item_volume_bg);
            helper.setBackgroundResourceByAttr(totalPerRl, R.attr.custom_attr_treatment_item_volume_bg);
            helper.setBackgroundResourceByAttr(layoutCurrVolume, R.attr.custom_attr_treatment_item_volume_bg);
            helper.setBackgroundResourceByAttr(layoutCurrTime, R.attr.custom_attr_treatment_item_time_bg);


//            helper.setBackgroundResourceByAttr(ivVolume, R.attr.custom_attr_treatment_icon_volume);
            helper.setBackgroundResourceByAttr(ivTime, R.attr.custom_attr_treatment_icon_time);

            helper.setTextColorByAttr(tvCurrVolumeLabel, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrTimeLabel, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrVolumeUnit, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrVolume, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrTime, R.attr.custom_attr_common_text_color);

            helper.setTextColorByAttr(tvTotalDrain, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrTimeLabel, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrVolumeUnit, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrVolume, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrTime, R.attr.custom_attr_common_text_color);

            helper.setTextColorByAttr(tvTotalUltVol, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvTotalUrl, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(totalUrlUnitTv, R.attr.custom_attr_common_text_color);


            helper.setTextColorByAttr(tvTotalDrainVolume, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvTotalDrainVolume, R.attr.custom_attr_common_text_color);

            helper.setTextColorByAttr(textClock, R.attr.custom_attr_common_text_color);


            helper.setBackgroundResourceByAttr(layoutCurrRetainTime, R.attr.custom_attr_treatment_item_time_bg);
            helper.setBackgroundResourceByAttr(ivRetainTime, R.attr.custom_attr_treatment_icon_time);
            helper.setTextColorByAttr(tvCurrRetainTime, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrRetainTimeLabel, R.attr.custom_attr_common_text_color);

            helper.setBackgroundResourceByAttr(layoutCurrTemperature, R.attr.custom_attr_treatment_item_temperature_bg);
            helper.setBackgroundResourceByAttr(layoutCurrTemp, R.attr.custom_attr_treatment_item_temperature_bg);
            helper.setBackgroundResourceByAttr(ivTemperature, R.attr.custom_attr_treatment_icon_temperature);
            helper.setBackgroundResourceByAttr(ivTemp, R.attr.custom_attr_treatment_icon_temperature);
            helper.setTextColorByAttr(tvCurrTemperature, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrTemp, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrTemperatureLabel, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrTempLabel, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvTotalDrain, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tv_total_drain_volume_ml, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvCurrVolumeUnit, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(totalPerTv, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvTotalPerfusionVolume, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tv_total_perfusion_volume_ml, R.attr.custom_attr_common_text_color);
        });

    }

    private void switchFragment(int mItem) {
        mCurrentPosition = mItem;
        mViewPager.setCurrentItem(mCurrentPosition, false);
    }

    public void showFragment(boolean isShowFragment) {
        layoutMain.setVisibility(isShowFragment ? View.GONE : View.VISIBLE);
        layoutFragment.setVisibility(isShowFragment ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isSleep = true;
        PdproHelper.getInstance().updateTtsSoundOpen(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSleep = false;
        PdproHelper.getInstance().updateTtsSoundOpen(true);
        if (getThemeTag() == 1) {
            if (currentStage == 1) {
                sendToMainBoard(CommandDataHelper.getInstance().LedOpen("perfuse", true,0,isShowAlarmDialog?0:1));
            } else if (currentStage == 2) {
                sendToMainBoard(CommandDataHelper.getInstance().LedOpen("retain", true,0,isShowAlarmDialog?0:1));
            } else if (currentStage == 3) {
                sendToMainBoard(CommandDataHelper.getInstance().LedOpen("drain", true,0,isShowAlarmDialog?0:1));
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
                downTimer.cancel();
                break;
            case MotionEvent.ACTION_UP:
                //抬起时启动定时
                downTimer.cancel();
                downTimer.start();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onDestroy() {

        RxBus.get().unRegister(this);
//        mCompositeDisposable.clear();
        MyApplication.treatmentRunning = false;//页面关闭时候，说明治疗结束了
        compositeDisposable.dispose();
        compositeDisposable.clear();
        if (powerOffDialog != null && powerOffDialog.isShowing()) {
            powerOffDialog.dismiss();
        }
        if (soundCompositeDisposable != null) {
            soundCompositeDisposable.clear();
        }
        if (powerFaultDialog != null && powerFaultDialog.isShowing()) {
            powerFaultDialog.dismiss();
        }
        if (lowTemperatureDialog != null && lowTemperatureDialog.isShowing()) {//低温警告弹窗还在时，关闭
            lowTemperatureDialog.dismiss();
        }
        if (highTemperatureDialog != null && highTemperatureDialog.isShowing()) {//高温警告弹窗还在时，关闭
            highTemperatureDialog.dismiss();
        }
//        if(handler!=null&&runnable!=null){
//            handler.removeCallbacks(runnable);
//        }
        reportCompositeDisposable.dispose();
        reportCompositeDisposable.clear();
        if (downTimer != null) {
            downTimer.cancel();
        }
        super.onDestroy();
    }
    @BindView(R.id.tv_total_perfusion_volume)
    TextView tvTotalPerfusionVolume;
    @BindView(R.id.tv_total_drain_volume)
    TextView tvTotalDrainVolume;

    private int treatedCycle;
    private int treatedVol = 500;
    private int inVol;
    public int oldVol;
    private boolean isSum;
    public int getVol() {
//        if (currCycle != 0) {
//            if (eniity.firstPerfusionVolume > 0 && !isSum) {
//                // 6000  500  4  1000
//                // curr = 2
//                // (500+1000+500)
//                if (currCycle == treatedCycle){
//                    inVol = oldVol + eniity.firstPerfusionVolume+
//                            (currCycle - treatedCycle - 1) * fragmentItem1.cancelPer;
//                } else {
//                    inVol = treatedVol + eniity.firstPerfusionVolume +
//                            (currCycle - treatedCycle - 1) * fragmentItem1.cancelPer;
//                }
//                isSum = true;
//                Log.e("isSum","firstPerfusionVolume"+eniity.firstPerfusionVolume+"---"
//                        +"currCycle"+currCycle+"---"
//                        +"modifyCycle"+treatedCycle+"---"
//                        +"cancelPer"+fragmentItem1.cancelPer+"---");
//            } else {
//                // 500+500
//                Log.e("getVol","加之前已经治疗的vol"+treatedVol);
//                if (currCycle == treatedCycle) {
//                    inVol = oldVol;
//                } else {
//                    inVol = treatedVol + (currCycle - treatedCycle) * fragmentItem1.cancelPer;
//                }
//                Log.e("else","firstPerfusionVolume"+eniity.firstPerfusionVolume+"---"
//                        +"currCycle"+currCycle+"---"
//                        +"modifyCycle"+treatedCycle+"---"
//                        +"cancelPer"+fragmentItem1.cancelPer+"---");
//            }
//        }
//        Log.e("getVol","已经治疗的vol"+inVol+",同一周期的"+oldVol);
        return 500;
    }

    private void initMode() {
        // 多模式
        switch (MyApplication.apdMode) {
            case 1:
                IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                if (ipdBean.abdomenRetainingVolumeFinally > 0) {
                    maxCycle = ipdBean.cycle + 1;
                } else {
                    maxCycle = ipdBean.cycle;
                }
//                maxCycle = ipdBean.cycle;
                break;
            case 2:
                TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                if (tpdBean.abdomenRetainingVolumeFinally > 0) {
                    maxCycle = tpdBean.cycle + 1;
                } else {
                    maxCycle = tpdBean.cycle;
                }
                break;
            case 3:
                CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                if (ccpdBean.abdomenRetainingVolumeFinally > 0) {
                    maxCycle = ccpdBean.cycle + 1;
                } else {
                    maxCycle = ccpdBean.cycle;
                }
                break;
            case 4:
                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
                if (aapdBean.finalVol == 0) {
                    maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6;
                } else {
                    maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6 + 1;
                }
                break;
            case 7:
                KidBean kidBean = PdproHelper.getInstance().kidBean();
                if (kidBean.abdomenRetainingVolumeFinally > 0) {
                    maxCycle = kidBean.cycle + 1;
                } else {
                    maxCycle = kidBean.cycle;
                }
                break;
            case 8:
                ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                if (expertBean.cycleMyself) {
                    maxCycle = Collections.max(expertBean.baseSupplyCycle)
                            >Collections.max(expertBean.osmSupplyCycle) ? Collections.max(expertBean.baseSupplyCycle) :
                            Collections.max(expertBean.osmSupplyCycle);
                } else {
                    if (expertBean.finalRetainVol > 0) {
                        maxCycle = expertBean.cycle + 1;
                    } else {
                        maxCycle = expertBean.cycle;
                    }
                }
//                if (expertBean.finalRetainVol > 0) {
//                    maxCycle = expertBean.cycle + 1;
//                } else {
//                    maxCycle = expertBean.cycle;
//                }
                break;
        }

        // 单模式
//        if (fragmentItem1.getActivity() != null) {
//            if (fragmentItem1.finalVol > 0) {
//                maxCycle = fragmentItem1.treatCycle + 1;
//            } else {
//                maxCycle = fragmentItem1.treatCycle;
//            }
//            if (eniity.firstPerfusionVolume > 0 && !isSum) {
//                treatedVol = treatedVol + eniity.firstPerfusionVolume+
//                        (currCycle - treatedCycle - 1) * fragmentItem1.perVol;
//                isSum = true;
//            } else {
//                // 500+500
//                treatedVol = treatedVol + (currCycle - treatedCycle) * fragmentItem1.perVol;
//            }
//            treatedCycle = currCycle;
//        }
        tvMaxCycle.setText(String.valueOf(maxCycle));
        if (mFragment3.getActivity() != null) {
            mFragment3.setMaxCycle(maxCycle);
        }
    }

    private void startTreat() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SerialTreatmentParamBean mParamBean = new SerialTreatmentParamBean();
        SerialTreatmentPrescriptBean prescript = new SerialTreatmentPrescriptBean();
        prescript.totalVolume = eniity.peritonealDialysisFluidTotal;//处方总灌注量
        prescript.cycle = eniity.cycle;//循环周期数
        prescript.firstPerfuseVolume = eniity.firstPerfusionVolume;//首次灌注量
        prescript.cyclePerfuseVolume = eniity.perCyclePerfusionVolume;//循环周期灌注量
        prescript.lastRetainVolume = eniity.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
        prescript.finalRetainVolume = eniity.abdomenRetainingVolumeFinally;//末次留腹量
        prescript.retainTime = eniity.abdomenRetainingTime;//60  //留腹时间
        prescript.ultraFiltrationVolume = eniity.ultrafiltrationVolume;//预估超滤量
        SerialTreatmentDrainBean drain = new SerialTreatmentDrainBean();
        drain.drainFlowRate = drainParameterBean.drainThresholdValue;//引流流速阈值
        drain.drainFlowPeriod = drainParameterBean.drainTimeInterval;//引流流速周期 秒
        drain.drainRinseVolume = drainParameterBean.drainRinseVolume;//引流冲洗量
        drain.drainRinseTimes = drainParameterBean.drainRinseNumber;//引流次数
        drain.firstDrainPassRate = drainParameterBean.drainZeroCyclePercentage;//0周期引流合格率
        drain.drainPassRate = drainParameterBean.drainOtherCyclePercentage;//周期引流合格率
        drain.isFinalDrainEmpty = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流是否排空
        drain.isFinalDrainEmptyWait = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流排空是否等待
        drain.finalDrainEmptyWaitTime = drainParameterBean.alarmTimeInterval;//最末引流排空等待时间 30
//        drain.isVaccumDrain = drainParameterBean.isNegpreDrain ? 1 : 0;//是否开启负压引流
        drain.isVaccumDrain = MyApplication.apdMode == 3 ? 1 : 0;//是否开启负压引流
        SerialTreatmentPerfuseBean perfuse = new SerialTreatmentPerfuseBean();
        perfuse.perfuseFlowRate = perfusionParameterBean.perfThresholdValue;//灌注流速阈值
        perfuse.perfuseFlowPeriod = perfusionParameterBean.perfTimeInterval;//灌注流速周期 秒
        perfuse.perfuseMaxVolume = perfusionParameterBean.perfMaxWarningValue;//最大灌注量
        SerialTreatmentSupplyBean supply = new SerialTreatmentSupplyBean();
        supply.supplyFlowRate = supplyParameterBean.supplyThresholdValue;//补液流速阈值
        supply.supplyFlowPeriod = supplyParameterBean.supplyTimeInterval;//补液流速周期 秒
        supply.supplyProtectVolume = supplyParameterBean.supplyTargetProtectionValue;//补液目标保护值
        supply.supplyMinVolume = supplyParameterBean.supplyTargetProtectionValue;//启动补液的加热袋重量最低值
        SerialTreatmentRetainBean retain = new SerialTreatmentRetainBean();
        retain.isFinalRetainDeduct = retainParamBean.isAbdomenRetainingDeduct ? 1 : 0;//是否末次流量扣除
        retain.isFiltCycleOnly = retainParamBean.isZeroCycleUltrafiltration ? 1 : 0;// 1 是否只统计循环周期的超滤量
        mParamBean.prescript = prescript;
        mParamBean.drain = drain;
        mParamBean.perfuse = perfuse;
        mParamBean.supply = supply;
        mParamBean.retain = retain;
        mSerialRequestBean.method = CommandSendConfig.METHOD_TREATMENT_START;
        mSerialRequestBean.params = mParamBean;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        String sendData = new Gson().toJson(mRequestBean);
//        sendData="{\"request\":{\"method\":\"treatment/start\",\"params\":{\"prescript\":{\"totalVol\":10000,\"cycle\":5,\"firstPerfVol\":1500,\"cyclePerfVol\":1000,\"lastRetainVol\":1500,\"finalRetainVol\":1500,\"retainTime\":120,\"filtVol\":500},\"drain\":{\"flowRate\":30,\"flowTime\":60,\"rinseVol\":200,\"rinseTimes\":3,\"firstPassRate\":60,\"cyclePassRate\":80,\"finalEmpty\":1,\"emptyWait\":1,\"waitTime\":30,\"negative\":1},\"perfuse\":{\"flowRate\":30,\"flowTime\":60,\"maxVol\":3000},\"supply\":{\"flowRate\":30,\"flowTime\":60,\"protectVol\":500,\"minVol\":500},\"retain\":{\"deduct\":1,\"cycleOnly\":1}}},\"sign\":\"b0acac8ae54f01de0cc112533d379e73\"}";
        command = sendData;
        sendCommandInterval(command,500);
    }

    public void modify() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SerialTreatmentParamBean mParamBean = new SerialTreatmentParamBean();
        SerialTreatmentPrescriptBean prescript = new SerialTreatmentPrescriptBean();
        //设置处方
        prescript.totalVolume = fragmentItem1.total;//处方总灌注量
        prescript.cycle = fragmentItem1.treatCycle - currCycle;//循环周期数
        prescript.firstPerfuseVolume = eniity.firstPerfusionVolume;//首次灌注量
        prescript.cyclePerfuseVolume = fragmentItem1.perVol;//循环周期灌注量
        prescript.lastRetainVolume = fragmentItem1.lastVol;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
        prescript.finalRetainVolume = fragmentItem1.finalVol;//末次留腹量
        prescript.retainTime = fragmentItem1.abdTime;//60  //留腹时间
        prescript.ultraFiltrationVolume = fragmentItem1.ultVol;//预估超滤量
        SerialTreatmentDrainBean drain = new SerialTreatmentDrainBean();
        drain.drainFlowRate = drainParameterBean.drainThresholdValue;//引流流速阈值
        drain.drainFlowPeriod = drainParameterBean.drainTimeInterval;//引流流速周期 秒
        drain.drainRinseVolume = drainParameterBean.drainRinseVolume;//引流冲洗量
        drain.drainRinseTimes = drainParameterBean.drainRinseNumber;//引流次数
        drain.firstDrainPassRate = drainParameterBean.drainZeroCyclePercentage;//0周期引流合格率
        drain.drainPassRate = drainParameterBean.drainOtherCyclePercentage;//周期引流合格率
        drain.isFinalDrainEmpty = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流是否排空
        drain.isFinalDrainEmptyWait = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流排空是否等待
        drain.finalDrainEmptyWaitTime = drainParameterBean.alarmTimeInterval;//最末引流排空等待时间 30
        drain.isVaccumDrain = drainParameterBean.isNegpreDrain ? 1 : 0;//是否开启负压引流
//        drain.isVaccumDrain = MyApplication.apdMode == 3 ? 1 : 0;//是否开启负压引流
        SerialTreatmentPerfuseBean perfuse = new SerialTreatmentPerfuseBean();
        perfuse.perfuseFlowRate = perfusionParameterBean.perfThresholdValue;//灌注流速阈值
        perfuse.perfuseFlowPeriod = perfusionParameterBean.perfTimeInterval;//灌注流速周期 秒
        perfuse.perfuseMaxVolume = perfusionParameterBean.perfMaxWarningValue;//最大灌注量
        SerialTreatmentSupplyBean supply = new SerialTreatmentSupplyBean();
        supply.supplyFlowRate = supplyParameterBean.supplyThresholdValue;//补液流速阈值
        supply.supplyFlowPeriod = supplyParameterBean.supplyTimeInterval;//补液流速周期 秒
        supply.supplyProtectVolume = supplyParameterBean.supplyTargetProtectionValue;//补液目标保护值
        supply.supplyMinVolume = supplyParameterBean.supplyTargetProtectionValue;//启动补液的加热袋重量最低值
        SerialTreatmentRetainBean retain = new SerialTreatmentRetainBean();
        retain.isFinalRetainDeduct = retainParamBean.isAbdomenRetainingDeduct ? 1 : 0;//是否末次流量扣除
        retain.isFiltCycleOnly = retainParamBean.isZeroCycleUltrafiltration ? 1 : 0;// 1 是否只统计循环周期的超滤量
        mParamBean.prescript = prescript;
        mParamBean.drain = drain;
        mParamBean.perfuse = perfuse;
        mParamBean.supply = supply;
        mParamBean.retain = retain;
        mSerialRequestBean.method = "prescription/modify";
        mSerialRequestBean.params = mParamBean;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        String sendData = new Gson().toJson(mRequestBean);
        command = sendData;
        sendCommandInterval(sendData,500);
    }

    /**
     * 发送治疗指令
     */
    private void deliveryTherapy() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SerialTreatmentParamBean mParamBean = new SerialTreatmentParamBean();
        SerialTreatmentPrescriptBean prescript = new SerialTreatmentPrescriptBean();
        switch (MyApplication.apdMode) {
            case 1:
                //设置处方
                prescript.totalVolume = entity.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = entity.cycle;//循环周期数
                prescript.firstPerfuseVolume = 0;//首次灌注量
                prescript.cyclePerfuseVolume = entity.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = entity.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = entity.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = entity.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = entity.ultrafiltrationVolume;//预估超滤量
                break;
            case 2:
                prescript.totalVolume = tpdBean.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = tpdBean.cycle;//循环周期数
                prescript.firstPerfuseVolume = tpdBean.firstPerfusionVolume;//首次灌注量
                prescript.cyclePerfuseVolume = tpdBean.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = tpdBean.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = tpdBean.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = tpdBean.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = tpdBean.ultrafiltrationVolume;//预估超滤量
                break;
            case 3:
                prescript.totalVolume = ccpdBean.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = ccpdBean.cycle;//循环周期数
                prescript.firstPerfuseVolume = ccpdBean.firstPerfusionVolume;//首次灌注量
                prescript.cyclePerfuseVolume = ccpdBean.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = ccpdBean.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = ccpdBean.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = ccpdBean.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = ccpdBean.ultrafiltrationVolume;//预估超滤量
                break;
            case 4:
                //设置处方
                prescript.totalVolume = aapdBean.total;//处方总灌注量
                prescript.cycle = aapdBean.c1+aapdBean.c2+aapdBean.c3+aapdBean.c4+aapdBean.c5+aapdBean.c6;//循环周期数
                prescript.firstPerfuseVolume = 0;//首次灌注量
                prescript.cyclePerfuseVolume = aapdBean.p1;//循环周期灌注量
                prescript.lastRetainVolume = 0;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = aapdBean.finalVol;//末次留腹量
                prescript.retainTime = aapdBean.r1;//60  //留腹时间
                prescript.ultraFiltrationVolume = 0;//预估超滤量
                break;
            case 7:
                prescript.totalVolume = kidBean.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = kidBean.cycle;//循环周期数
                prescript.firstPerfuseVolume = kidBean.firstPerfusionVolume;//首次灌注量
                prescript.cyclePerfuseVolume = kidBean.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = kidBean.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = kidBean.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = kidBean.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = kidBean.ultrafiltrationVolume;//预估超滤量
                break;
            case 8:
                prescript.totalVolume = expertBean.total;//处方总灌注量
                if (expertBean.cycleMyself) {
                    prescript.cycle = Collections.max(expertBean.baseSupplyCycle)
                            >Collections.max(expertBean.osmSupplyCycle) ? Collections.max(expertBean.baseSupplyCycle) :
                            Collections.max(expertBean.osmSupplyCycle);
                    prescript.cyclePerfuseVolume = expertBean.baseSupplyCycle.contains(1)
                            ? expertBean.baseSupplyVol : expertBean.osmSupplyVol;//循环周期灌注量
                } else {
                    prescript.cyclePerfuseVolume = expertBean.cycleVol;//循环周期灌注量
                    prescript.cycle = expertBean.cycle;//循环周期数
                }
                prescript.firstPerfuseVolume = expertBean.firstVol;//首次灌注量
//                prescript.cyclePerfuseVolume = expertBean.cycleVol;//循环周期灌注量
                prescript.lastRetainVolume = expertBean.lastRetainVol;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = expertBean.finalRetainVol;//末次留腹量
                prescript.retainTime = expertBean.retainTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = expertBean.ultVol;//预估超滤量
                break;
        }

        SerialTreatmentDrainBean drain = new SerialTreatmentDrainBean();
        drain.drainFlowRate = drainParameterBean.drainThresholdValue;//引流流速阈值
        drain.drainFlowPeriod = drainParameterBean.drainTimeInterval;//引流流速周期 秒
        drain.drainRinseVolume = drainParameterBean.drainRinseVolume;//引流冲洗量
        drain.drainRinseTimes = drainParameterBean.drainRinseNumber;//引流次数
        drain.firstDrainPassRate = drainParameterBean.drainZeroCyclePercentage;//0周期引流合格率
        drain.drainPassRate = drainParameterBean.drainOtherCyclePercentage;//周期引流合格率
        drain.isFinalDrainEmpty = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流是否排空
        drain.isFinalDrainEmptyWait = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流排空是否等待
        drain.finalDrainEmptyWaitTime = drainParameterBean.alarmTimeInterval;//最末引流排空等待时间 30
//        drain.isVaccumDrain = drainParameterBean.isNegpreDrain ? 1 : 0;//是否开启负压引流

        drain.isVaccumDrain = MyApplication.apdMode == 3 ? 1 : 0;//是否开启负压引流

        SerialTreatmentPerfuseBean perfuse = new SerialTreatmentPerfuseBean();
        perfuse.perfuseFlowRate = perfusionParameterBean.perfThresholdValue;//灌注流速阈值
        perfuse.perfuseFlowPeriod = perfusionParameterBean.perfTimeInterval;//灌注流速周期 秒
        perfuse.perfuseMaxVolume = perfusionParameterBean.perfMaxWarningValue;//最大灌注量

        SerialTreatmentSupplyBean supply = new SerialTreatmentSupplyBean();
        supply.supplyFlowRate = supplyParameterBean.supplyThresholdValue;//补液流速阈值
        supply.supplyFlowPeriod = supplyParameterBean.supplyTimeInterval;//补液流速周期 秒
        supply.supplyProtectVolume = supplyParameterBean.supplyTargetProtectionValue;//补液目标保护值
        supply.supplyMinVolume = supplyParameterBean.supplyTargetProtectionValue;//启动补液的加热袋重量最低值

        SerialTreatmentRetainBean retain = new SerialTreatmentRetainBean();
        retain.isFinalRetainDeduct = retainParamBean.isAbdomenRetainingDeduct ? 1 : 0;//是否末次流量扣除
        retain.isFiltCycleOnly = retainParamBean.isZeroCycleUltrafiltration ? 1 : 0;// 1 是否只统计循环周期的超滤量

        mParamBean.prescript = prescript;
        mParamBean.drain = drain;
        mParamBean.perfuse = perfuse;
        mParamBean.supply = supply;
        mParamBean.retain = retain;

        mSerialRequestBean.method = CommandSendConfig.METHOD_TREATMENT_START;
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        String sendData = new Gson().toJson(mRequestBean);
//        sendData="{\"request\":{\"method\":\"treatment/start\",\"params\":{\"prescript\":{\"totalVol\":10000,\"cycle\":5,\"firstPerfVol\":1500,\"cyclePerfVol\":1000,\"lastRetainVol\":1500,\"finalRetainVol\":1500,\"retainTime\":120,\"filtVol\":500},\"drain\":{\"flowRate\":30,\"flowTime\":60,\"rinseVol\":200,\"rinseTimes\":3,\"firstPassRate\":60,\"cyclePassRate\":80,\"finalEmpty\":1,\"emptyWait\":1,\"waitTime\":30,\"negative\":1},\"perfuse\":{\"flowRate\":30,\"flowTime\":60,\"maxVol\":3000},\"supply\":{\"flowRate\":30,\"flowTime\":60,\"protectVol\":500,\"minVol\":500},\"retain\":{\"deduct\":1,\"cycleOnly\":1}}},\"sign\":\"b0acac8ae54f01de0cc112533d379e73\"}";
        command = sendData;
        sendCommandInterval(command,500);
    }

    private void reviseCycle() {
        Log.e("reviseCycle","cycle--"+maxCycle+"---"+modifyCycle);
        try {
            if (maxCycle > modifyCycle) {
                for (int i = modifyCycle + 1; i < maxCycle + 1; i++) {
                    TreatmentHistoryBean mBean = new TreatmentHistoryBean();
                    mBean.uuid = uuid;
                    mBean.cycle = i;
                    mBean.sn = PdproHelper.getInstance().getMachineSN();
                    mTreatmentDataList.add(mBean);
                }
                if (null != fragmentItem2.getActivity()) {
                    fragmentItem2.mAdapter.notifyDataSetChanged();
                }
                modifyCycle = maxCycle;
            } else {
                if (mTreatmentDataList.size() > maxCycle + 1) {
                    mTreatmentDataList.subList(maxCycle + 1, mTreatmentDataList.size()).clear();
                }
                if (null != fragmentItem2.getActivity()) {
                    fragmentItem2.mAdapter.notifyDataSetChanged();
                }
                modifyCycle = maxCycle;
            }
        } catch (Exception e) {
            saveFaultCodeLocal("修改处方--"+e.getMessage());
        }
    }

    /**
     * 治疗过程中修改处方
     * @param cycle 当前周期数
     */
    private void modifyDuringTreatment(int cycle) {
        // 多模式
        switch (MyApplication.apdMode) {
            case 1:
            case 2:
            case 3:
            case 7:
                if (maxCycle > currCycle) {
                    revisePrescription();
                }
                break;
            case 4:
                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
                if (aapdFragment != null) {
                    aapdFragment.setBackground(aapdBean, currCycle);
                }
                if (aapdBean.c1 - cycle == 0 && aapdBean.c2 != 0&& aapdBean.p2 != 0 && aapdBean.r2 != 0) {
                    revisePrescription();
                } else if (aapdBean.c2+aapdBean.c1 - cycle == 0 && aapdBean.c3 != 0 && aapdBean.p3 != 0 && aapdBean.r3 != 0) {
                    revisePrescription();
                } else if (aapdBean.c3+aapdBean.c2+aapdBean.c1 - cycle == 0 && aapdBean.c4 != 0&& aapdBean.p4 != 0 && aapdBean.r4 != 0) {
                    revisePrescription();
                } else if (aapdBean.c4+aapdBean.c3+aapdBean.c2+aapdBean.c1 - cycle == 0 && aapdBean.c5 != 0&& aapdBean.p5 != 0 && aapdBean.r5 != 0) {
                    revisePrescription();
                } else if (aapdBean.c5+aapdBean.c4+aapdBean.c3+aapdBean.c2+aapdBean.c1 - cycle == 0 && aapdBean.c6 != 0&& aapdBean.p6 != 0 && aapdBean.r6 != 0) {
                    revisePrescription();
                }
                break;
            case 8:
                ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                if (expertBean.cycleMyself) {
                    if (maxCycle >= cycle+1) {
                        revisePrescription();
                    }
                }
                break;
        }
    }

    /**
     * 治疗中修改处方
     */
    // prescription/modify
    private void revisePrescription() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        SerialTreatmentParamBean mParamBean = new SerialTreatmentParamBean();

        SerialTreatmentPrescriptBean prescript = new SerialTreatmentPrescriptBean();
        switch (MyApplication.apdMode) {
            case 1:
                IpdBean treatmentParameterEniity = PdproHelper.getInstance().ipdBean();
                //设置处方
                prescript.totalVolume = treatmentParameterEniity.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = treatmentParameterEniity.cycle - currCycle;//循环周期数
                prescript.firstPerfuseVolume = treatmentParameterEniity.firstPerfusionVolume;//首次灌注量
                prescript.cyclePerfuseVolume = treatmentParameterEniity.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = treatmentParameterEniity.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = treatmentParameterEniity.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = treatmentParameterEniity.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = treatmentParameterEniity.ultrafiltrationVolume;//预估超滤量
                break;
            case 2:
                TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                //设置处方
                prescript.totalVolume = tpdBean.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = tpdBean.cycle - currCycle;//循环周期数
                prescript.firstPerfuseVolume = tpdBean.firstPerfusionVolume;//首次灌注量
                prescript.cyclePerfuseVolume = tpdBean.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = tpdBean.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = tpdBean.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = tpdBean.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = tpdBean.ultrafiltrationVolume;//预估超滤量
                break;
            case 3:
                CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                //设置处方
                prescript.totalVolume = ccpdBean.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = ccpdBean.cycle - currCycle;//循环周期数
                prescript.firstPerfuseVolume = ccpdBean.firstPerfusionVolume;//首次灌注量
                prescript.cyclePerfuseVolume = ccpdBean.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = ccpdBean.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = ccpdBean.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = ccpdBean.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = ccpdBean.ultrafiltrationVolume;//预估超滤量
                break;
            case 4:
                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
                //设置处方
                prescript.totalVolume = aapdBean.total;//处方总灌注量
//                prescript.apdmodify = aapdBean.a1;
                prescript.cycle = aapdBean.c1+aapdBean.c2+aapdBean.c3+aapdBean.c4+aapdBean.c5+aapdBean.c6 - currCycle;//循环周期数
                prescript.firstPerfuseVolume = 0;//首次灌注量
//                if (currCycle <= aapdBean.c1) {
//                    prescript.cyclePerfuseVolume = aapdBean.p1;//循环周期灌注量
//                    prescript.retainTime = aapdBean.r1;
//                } else
                // 1 c1=3 变p2
                if (currCycle <= aapdBean.c1) {
                    prescript.cyclePerfuseVolume = aapdBean.p2;//循环周期灌注量
                    prescript.retainTime = aapdBean.r2;
                } else if (currCycle <= aapdBean.c1+aapdBean.c2) {
                    prescript.cyclePerfuseVolume = aapdBean.p3;//循环周期灌注量
                    prescript.retainTime = aapdBean.r3;
                } else if (currCycle <= aapdBean.c1+aapdBean.c2+aapdBean.c3) {
                    prescript.cyclePerfuseVolume = aapdBean.p4;//循环周期灌注量
                    prescript.retainTime = aapdBean.r4;
                } else if (currCycle <= aapdBean.c1+aapdBean.c2+aapdBean.c3+ aapdBean.c4) {
                    prescript.cyclePerfuseVolume = aapdBean.p5;//循环周期灌注量
                    prescript.retainTime = aapdBean.r5;
                } else if (currCycle <= aapdBean.c1+aapdBean.c2+aapdBean.c3+aapdBean.c5) {
                    prescript.cyclePerfuseVolume = aapdBean.p6;//循环周期灌注量
                    prescript.retainTime = aapdBean.r6;
                }
                prescript.lastRetainVolume = 0;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = aapdBean.finalVol;//末次留腹量
//                prescript.retainTime = aapdBean.r1;//60  //留腹时间
                prescript.ultraFiltrationVolume = 0;//预估超滤量
                break;
            case 7:
                KidBean kidBean = PdproHelper.getInstance().kidBean();
                prescript.totalVolume = kidBean.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = kidBean.cycle - currCycle;//循环周期数
                prescript.firstPerfuseVolume = kidBean.firstPerfusionVolume;//首次灌注量
                prescript.cyclePerfuseVolume = kidBean.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = kidBean.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = kidBean.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = kidBean.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = kidBean.ultrafiltrationVolume;//预估超滤量
                break;
            case 8:
                ExpertBean expertBean = PdproHelper.getInstance().expertBean();
//                prescript.totalVolume = expertBean.total;//处方总灌注量
                prescript.totalVolume = expertBean.total;//处方总灌注量
                if (expertBean.cycleMyself) {
                    prescript.cycle = Collections.max(expertBean.baseSupplyCycle)
                            >Collections.max(expertBean.osmSupplyCycle) ? Collections.max(expertBean.baseSupplyCycle) - currCycle :
                            Collections.max(expertBean.osmSupplyCycle) - currCycle;
                    prescript.cyclePerfuseVolume = expertBean.baseSupplyCycle.contains(currCycle+1)
                            ? expertBean.baseSupplyVol : expertBean.osmSupplyVol;//循环周期灌注量
                } else {
                    prescript.cyclePerfuseVolume = expertBean.cycleVol;//循环周期灌注量
                    prescript.cycle = expertBean.cycle - currCycle;//循环周期数
                }
                prescript.firstPerfuseVolume = expertBean.firstVol;//首次灌注量
//                prescript.cyclePerfuseVolume = expertBean.cycleVol;//循环周期灌注量
                prescript.lastRetainVolume = expertBean.lastRetainVol;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = expertBean.finalRetainVol;//末次留腹量
                prescript.retainTime = expertBean.retainTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = expertBean.ultVol;//预估超滤量
                break;
        }

        SerialTreatmentDrainBean drain = new SerialTreatmentDrainBean();
        drain.drainFlowRate = drainParameterBean.drainThresholdValue;//引流流速阈值
        drain.drainFlowPeriod = drainParameterBean.drainTimeInterval;//引流流速周期 秒
        drain.drainRinseVolume = drainParameterBean.drainRinseVolume;//引流冲洗量
        drain.drainRinseTimes = drainParameterBean.drainRinseNumber;//引流次数
        drain.firstDrainPassRate = drainParameterBean.drainZeroCyclePercentage;//0周期引流合格率
        drain.drainPassRate = drainParameterBean.drainOtherCyclePercentage;//周期引流合格率
        drain.isFinalDrainEmpty = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流是否排空
        drain.isFinalDrainEmptyWait = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流排空是否等待
        drain.finalDrainEmptyWaitTime = drainParameterBean.alarmTimeInterval;//最末引流排空等待时间 30
//        drain.isVaccumDrain = drainParameterBean.isNegpreDrain ? 1 : 0;//是否开启负压引流
        drain.isVaccumDrain = MyApplication.apdMode == 3 ? 1 : 0;//是否开启负压引流

        SerialTreatmentPerfuseBean perfuse = new SerialTreatmentPerfuseBean();

        perfuse.perfuseFlowRate = perfusionParameterBean.perfThresholdValue;//灌注流速阈值
        perfuse.perfuseFlowPeriod = perfusionParameterBean.perfTimeInterval;//灌注流速周期 秒
        perfuse.perfuseMaxVolume = perfusionParameterBean.perfMaxWarningValue;//最大灌注量

        SerialTreatmentSupplyBean supply = new SerialTreatmentSupplyBean();
        supply.supplyFlowRate = supplyParameterBean.supplyThresholdValue;//补液流速阈值
        supply.supplyFlowPeriod = supplyParameterBean.supplyTimeInterval;//补液流速周期 秒
        supply.supplyProtectVolume = supplyParameterBean.supplyTargetProtectionValue;//补液目标保护值
        supply.supplyMinVolume = supplyParameterBean.supplyTargetProtectionValue;//启动补液的加热袋重量最低值

        SerialTreatmentRetainBean retain = new SerialTreatmentRetainBean();
        retain.isFinalRetainDeduct = retainParamBean.isAbdomenRetainingDeduct ? 1 : 0;//是否末次流量扣除
        retain.isFiltCycleOnly = retainParamBean.isZeroCycleUltrafiltration ? 1 : 0;// 1 是否只统计循环周期的超滤量

        mParamBean.prescript = prescript;
        mParamBean.drain = drain;
        mParamBean.perfuse = perfuse;
        mParamBean.supply = supply;
        mParamBean.retain = retain;

        mSerialRequestBean.method = "prescription/modify";
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        String sendData = new Gson().toJson(mRequestBean);
        command = sendData;
        sendCommandInterval(sendData,500);
    }

    public void revise() {
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, eniity);
        // 多模式
        switch (MyApplication.apdMode){
            case 1:
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, entity);
                break;
            case 2:
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.TPD_PARAMS, tpdBean);
                break;
            case 3:
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.CCPD_BEAN, ccpdBean);
                break;
            case 4:
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.aApd_PARAMS, aapdBean);
                break;
            case 7:
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.KID_PARAMS, kidBean);
                break;
            case 8:
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.EXPERT_PARAMS, expertBean);
                break;

        }
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRAIN_PARAMETER, drainParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.SUPPLY_PARAMETER, supplyParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.RETAIN_PARAM, retainParamBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.PERFUSION_PARAMETER, perfusionParameterBean);
        initMode();
        reviseCycle();
        toastMessage("修改成功");
    }

}
