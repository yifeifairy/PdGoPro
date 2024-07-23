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
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.bean.AapdBean;
import com.emt.pdgo.next.data.bean.DetailedBean;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.ExpertBean;
import com.emt.pdgo.next.data.bean.KidBean;
import com.emt.pdgo.next.data.bean.MsgBean;
import com.emt.pdgo.next.data.bean.OtherParamBean;
import com.emt.pdgo.next.data.bean.PdData;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RBean;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.TpdBean;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
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
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.interfaces.OnToggledListener;
import com.emt.pdgo.next.model.mode.CcpdBean;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.upload.TreatmentDataUploadBean;
import com.emt.pdgo.next.net.bean.upload.TreatmentPrescriptionUploadBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.vo.VoActivity;
import com.emt.pdgo.next.ui.adapter.MsgAdapter;
import com.emt.pdgo.next.ui.adapter.dpr.ViewPagerAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.dialog.NumberDialog;
import com.emt.pdgo.next.ui.dialog.SoundDialog;
import com.emt.pdgo.next.ui.dialog.SpecialNumberDialog;
import com.emt.pdgo.next.ui.dialog.TreatmentAlarmDialog;
import com.emt.pdgo.next.ui.fragment.DrainFragment;
import com.emt.pdgo.next.ui.fragment.SupplyFragment;
import com.emt.pdgo.next.ui.fragment.TreatmentFragmentItem2;
import com.emt.pdgo.next.ui.fragment.TreatmentFragmentItem3;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.AapdFragment;
import com.emt.pdgo.next.ui.view.NoScrollViewPager;
import com.emt.pdgo.next.ui.view.ToggleableView;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.ClickUtil;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.NetworkUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreatmentFragmentActivity extends BaseActivity {

    private String TAG = "TreatmentFragmentActivity";

    @BindView(R.id.viewpager)
    NoScrollViewPager mViewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.configViewpager)
    NoScrollViewPager configViewpager;

    @BindView(R.id.configTabLayout)
    TabLayout configTabLayout;

    @BindView(R.id.finishTreatment)
    LinearLayout finishTreatment;

    @BindView(R.id.btnRx)
    Button btnRx;
    @BindView(R.id.btnParam)
    Button btnParam;

    @BindView(R.id.pauseIv)
    ImageView pauseIv;
    @BindView(R.id.pauseTv)
    TextView pauseTv;

    @BindView(R.id.currentCycleTv)
    TextView tvPeriod;
    @BindView(R.id.maxCycleTv)
    TextView tvMaxCycle;
    @BindView(R.id.currentStateTv)
    TextView tvStatus;

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
    @BindView(R.id.firstVolTv)
    TextView firstVolTv;
    @BindView(R.id.firstVolLl)
    LinearLayout firstVolLl;
    @BindView(R.id.ultVolTv)
    TextView ultVolTv;
    @BindView(R.id.ultVolLl)
    LinearLayout ultVolLl;

    @BindView(R.id.finalSupplyLl)
    LinearLayout finalSupplyLl;
    @BindView(R.id.isFinalSwitch)
    LabeledSwitch isFinalSwitch;
    @BindView(R.id.isFinalTv)
    TextView isFinalTv;

    @BindView(R.id.currentVolTv)
    TextView currentVolTv;

    @BindView(R.id.stageIv)
    ImageView stageIv;
    @BindView(R.id.stageTimeLabelTv)
    TextView stageTimeLabelTv;
    @BindView(R.id.stageVolLabelTv)
    TextView stageVolLabelTv;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.tempTv)
    TextView tempTv;
    @BindView(R.id.assistIv)
    ImageView assistIv;

    @BindView(R.id.btnRevise)
    Button btnRevise;
    @BindView(R.id.reviseConfirm)
    Button reviseConfirm;

    @BindView(R.id.cycle_tv_title)
    TextView cycle_tv_title;

    @BindView(R.id.layout_main)
    LinearLayout layoutMain;

    @BindView(R.id.layout_fragment)
    LinearLayout layoutFragment;

    @BindView(R.id.pauseLayout)
    LinearLayout pauseLayout;
    @BindView(R.id.skipLayout)
    LinearLayout skipLayout;

    @BindView(R.id.viewDataBack)
    Button viewDataBack;
    @BindView(R.id.paramBack)
    Button paramBack;
    @BindView(R.id.paramConfirm)
    Button paramConfirm;
    @BindView(R.id.rxBackBtn)
    Button rxBackBtn;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    public DetailedBean detailedBean;
    private TreatmentFragmentItem2 fragmentItem2;
    private TreatmentFragmentItem3 mFragment3;

    private int mCurrentPosition = 0;
    private int mCurrentPosition2 = 0;

    private CompositeDisposable mCompositeDisposable;
    private CompositeDisposable reportCompositeDisposable;
    private Disposable countDownDisposable;
    private Disposable lastDrainDisposable;

    private CompositeDisposable compositeDisposable;

    private CompositeDisposable cmdDisposable;

    private String command;

    @BindView(R.id.configFragment)
    LinearLayout configFragment;

    @BindView(R.id.prescriptionFragment)
    LinearLayout prescriptionFragment;

    @BindView(R.id.ipdLayout)
    LinearLayout ipdLayout;
    @BindView(R.id.aapdLayout)
    LinearLayout aapdLayout;
    @BindView(R.id.svLayout)
    LinearLayout svLayout;

    @BindView(R.id.lock)
    ImageView lock;
    @BindView(R.id.unlock)
    ImageView unlock;

    @BindView(R.id.roteLl)
    LinearLayout roteLl;
    @BindView(R.id.roteTv)
    TextView roteTv;
    @BindView(R.id.roteName)
    TextView roteName;

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
    private int currdrainWarnTimeInterval;    //当前留腹倒计时 秒

    public int dialysateInitialValue;//透析液初始值
    public int lowFirstValue;

    private boolean isPause = false;//是否暂停

    public boolean isComplete = false;//是否完成治疗

    private boolean isShowAlarmDialog = false;//是否故障弹窗中

    private boolean isShowExceedsMaxValueDialog = false;//是否灌注量超过警戒值弹窗中

    private boolean isDrainManualEmptying = false;//是否最末引流排空等待中

    // aApd
    public AapdBean aapdBean ;
    // 儿童模式
    public KidBean kidBean ;
    // 专家
    public ExpertBean expertBean ;
    // 常规
    public IpdBean ipdBean ;//治疗参数
    public TreatmentParameterEniity eniity = PdproHelper.getInstance().getTreatmentParameter();
    public TpdBean tpdBean;
    public CcpdBean ccpdBean;
    public static DrainParameterBean drainParameterBean;
    public static PerfusionParameterBean perfusionParameterBean ;
    public static SupplyParameterBean supplyParameterBean;
    public static RetainParamBean retainParamBean ;

    private List<PdEntity.PdInfoEntity> pdInfoEntities;

    public Gson myGson;

    private boolean isTiming = false;

    private TreatmentAlarmDialog treatmentAlarmDialog;

    @BindView(R.id.viewDataLayout)
    LinearLayout viewDataLayout;

    @BindView(R.id.powerIv)
    ImageView powerIv;

    @BindView(R.id.conTv)
    TextView conTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }
    private MonitorTouchCountDownTimer downTimer;
//            = new MonitorTouchCountDownTimer((long) PdproHelper.getInstance().autoSleep() * 60 * 1000, 1000);

    @Override
    public void initAllViews() {
        APIServiceManage.getInstance().postApdCode("T0066");
        setContentView(R.layout.activity_fragment_treatment_main);
        ButterKnife.bind(this);
        if (otherParamBean == null) {
            otherParamBean = PdproHelper.getInstance().getOtherParamBean();
        }
        downTimer = new MonitorTouchCountDownTimer((long) otherParamBean.sleep * 60 * 1000, 1000);
        downTimer.start();
        myGson = new Gson();
        detailedBean = new DetailedBean();
        startTime = EmtTimeUil.getTime();
        treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        pdEntityDataList = new ArrayList<>();
        drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();
        retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        pdInfoEntities = new ArrayList<>();
        pdData = MyApplication.pdData;
        initRx();
        initRecyclerview();
        if (PdproHelper.getInstance().getOtherParamBean().isHospital) {
            btnRevise.setVisibility(View.VISIBLE);
        } else {
            btnRevise.setVisibility(View.GONE);
        }
        try {
            new Thread(() -> EmtDataBase
                    .getInstance(MyApplication.getInstance())
                    .getFaultCodeDao()
                    .delete()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isHospital(false);
//        initListener();
        initPreViewPage();
        initConfigViewPage();
        ActivityManager.getActivityManager().removeAllActivityExceptOne(TreatmentFragmentActivity.class);
    }

    private final int maxCountdown = 2 * 60;//倒计时
    private int currCountdown = 0;//倒计时
    private CompositeDisposable soundCompositeDisposable;
    private CompositeDisposable taskCompositeDisposable;
    private Disposable disposable;
    private Disposable taskDisposable;
    private SoundDialog soundDialog;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void registerEvents() {
        toEngAc(textClock);
        viewDataBack.setOnClickListener(view -> {
            showFragment(false,false,false,false);
        });
        ClickUtil clickUtil = new ClickUtil(tempTv,3000);
        clickUtil.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                     alertNumberBoardDialog(String.valueOf(PdproHelper.getInstance().targetTemper()), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE, true);
                }
            }
        });

        paramBack.setOnClickListener(view -> {
            showFragment(false,false,false,false);
        });
        paramConfirm.setOnClickListener(view -> {
            reviseConfirm();
        });
        rxBackBtn.setOnClickListener(view -> {
            showFragment(false,false,false,false);
            isHospital(false);
        });

        ClickUtil lockClick = new ClickUtil(lock, 3000);
        lockClick.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    String tips = "是否解锁";
                    final CommonDialog dialog = new CommonDialog(TreatmentFragmentActivity.this);
                    dialog.setContent(tips)
                            .setBtnFirst("确定")
                            .setBtnTwo("取消")
                            .setFirstClickListener(mCommonDialog -> {
                                unlock.setVisibility(View.VISIBLE);
                                lock.setVisibility(View.GONE);
                                unlock();
                                mCommonDialog.dismiss();
                            })
                            .setTwoClickListener(Dialog::dismiss)
                            .show();
                }
            }
        });
        ClickUtil unlockClick = new ClickUtil(unlock, 5000);
        unlockClick.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    String tips = "是否上锁";
                    final CommonDialog dialog = new CommonDialog(TreatmentFragmentActivity.this);
                    dialog.setContent(tips)
                            .setBtnFirst("确定")
                            .setBtnTwo("取消")
                            .setFirstClickListener(mCommonDialog -> {
                                unlock.setVisibility(View.GONE);
                                lock.setVisibility(View.VISIBLE);
                                lock();
                                mCommonDialog.dismiss();
                            })
                            .setTwoClickListener(Dialog::dismiss)
                            .show();
                }
            }
        });
        ClickUtil preClick = new ClickUtil(btnRevise, 3000);
        preClick.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    if (currCycle < maxCycle) {
                        isHospital(true);
                    } else {
                        toastMessage("最后周期不能修改");
                    }
                }
            }
        });

        reviseConfirm.setOnClickListener(view -> {
            reviseConfirm();
        });

        btnRx.setOnClickListener(view -> {
            showFragment(true,false,false,true);
        });
        btnParam.setOnClickListener(view -> {
            showFragment(true,false,true,false);
        });
//        ClickUtil configClick = new ClickUtil(btnParam, 3000);
//        configClick.setResultListener(new ClickUtil.ResultListener() {
//            @Override
//            public void onResult(boolean press) {
//                if (press) {
//                    showFragment(true,false,true,false);
//                }
//            }
//        });


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
//        currentPower.setText(MyApplication.batteryLevel + "");
//        sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
        compositeDisposable = new CompositeDisposable();
        initTime();
        ClickUtil finishClick = new ClickUtil(finishTreatment, 3000);
        finishClick.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    if (!isComplete) {
                        earlyFinishTreatmentDialog();
                    } else {
                        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
                    }
                }
            }
        });
//        finishTreatment.setOnClickListener(view -> {
//
//            if (!isComplete) {
//                earlyFinishTreatmentDialog();
//            } else {
//                sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
//            }
//
//        });
//        dormancyBtn.setOnClickListener(v -> {
//            simulateKey(26);
//            sendToMainBoard(CommandDataHelper.getInstance().LedOpen("all", false, 1, 0));
//        });
//        tvCurrTemperature.setOnLongClickListener(view -> {
//            alertNumberBoardDialog(String.valueOf(PdproHelper.getInstance().targetTemper()), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE, true);
//            return true;
//        });
        ClickUtil clickUtil1 = new ClickUtil(tvMaxCycle,3000);
        clickUtil1.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    alertNumberBoardDialog("", PdGoConstConfig.AUTO_SLEEP,false);
                }
            }
        });

        pauseLayout.setOnClickListener(view -> {
            if (isFastClick()) {
                return;
            }
            if (!isComplete) {
                if (!isClicking) {
                    showPauseDialog();
                }
            }
        });
        viewDataLayout.setOnClickListener(view -> {
            switchFragment(0);
            showFragment(true,true, false,false);
        });

//        ClickUtil ivLogoClick = new ClickUtil(ivLogo, 5000);
//        ivLogoClick.setResultListener(press -> {
//            if (press) {
//                alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
//            }
//        });
        ClickUtil skipClick = new ClickUtil(skipLayout, 3000);
        skipClick.setResultListener(press -> {
            if (press) {
                if (!isComplete && !isPause) {
                    if (!isClicking) {
                        skipCurrentStageDialog();
                        if (currCycle < 10) {
                            APIServiceManage.getInstance().postApdCode("T0" + currCycle + "02");
                        } else {
                            APIServiceManage.getInstance().postApdCode("T" + currCycle + "02");
                        }
                    }
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"02");
                } else if (isPause){
                    showTipsCommonDialog("处于暂停状态，需要解除暂停后，方可跳过进入下一步");
                }
            }
        });
    }
    private ViewPagerAdapter configPagerAdapter;
    private ViewPagerAdapter prePagerAdapter;
    private ViewPagerAdapter pagerAdapter;
    private AapdFragment aapdFragment;

    private String startTime;
    private String endTime;
    @Override
    public void initViewData() {
//        uuid = UUID.randomUUID().toString().replaceAll("-", "");
        MyApplication.treatmentRunning = true;
        MyApplication.apdTreat = true;
        mCompositeDisposable = new CompositeDisposable();
        reportCompositeDisposable = new CompositeDisposable();
        eniity = PdproHelper.getInstance().getTreatmentParameter();
        initTreatmentHistory();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentItem2 = new TreatmentFragmentItem2();
        fragmentList.add(fragmentItem2);
        mFragment3 = new TreatmentFragmentItem3();
//        trFragment = new TrFragment();
//        fragmentList.add(trFragment);
        fragmentList.add(mFragment3);
        List<String> titles = new ArrayList<>();
        titles.add("治疗周期");
//        titles.add("处方修改");
//        titles.add("参数修改");
        titles.add("数据查看");
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList, titles);
        mViewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(fragmentList.size() -1);
        MyApplication.mStartTime = getYMDHMTime(getCurrentTime());

        // 单模式
//        startTreat();
        // 多模式
        deliveryTherapy();
//        startTreat(MyApplication.isDebug ? 1 : 0);
//        rCountDownTimer.start();

        netTv.setTextColor(NetworkUtils.getNetWorkState(this) == 0 ? Color.WHITE : Color.RED);
        wifiIv.setVisibility(NetworkUtils.getNetWorkState(this) == 1 ?View.VISIBLE:View.INVISIBLE);
    }

//    private CountDownTimer rCountDownTimer = new CountDownTimer(3 * 1000, 1000) {
//        @Override
//        public void onTick(long l) {
//
//        }
//
//        @Override
//        public void onFinish() {
//            init();
//        }
//    };

    private void initConfigViewPage() {
        List<Fragment> fragmentList = new ArrayList<>();
        DrainFragment drainFragment = new DrainFragment();
        SupplyFragment supplyFragment = new SupplyFragment();
        fragmentList.add(drainFragment);
        fragmentList.add(supplyFragment);
        List<String> titles = new ArrayList<>();
        titles.add("引流参数设定");
        titles.add("其他参数设定");
        configPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList, titles);
        configViewpager.setAdapter(configPagerAdapter);
        configViewpager.setOffscreenPageLimit(fragmentList.size()-1);
        configTabLayout.setupWithViewPager(configViewpager);
    }

    private void initRx(){
        switch (MyApplication.apdMode) {
            case 1:
//                new Thread(() -> {
                if (ipdBean == null) {
                    ipdBean = PdproHelper.getInstance().ipdBean();
                }
                click();
                    RxEntity hisRx = new RxEntity();
                    hisRx.time = EmtTimeUil.getTime();
                    hisRx.perVol = ipdBean.peritonealDialysisFluidTotal;
                    hisRx.perCycleVol = ipdBean.perCyclePerfusionVolume;
                    hisRx.treatCycle = ipdBean.cycle;
                    hisRx.firstPerVol = ipdBean.firstPerfusionVolume;
                    hisRx.abdTime = ipdBean.abdomenRetainingTime;
                    hisRx.endAbdVol = ipdBean.abdomenRetainingVolumeFinally;
                    hisRx.lastTimeAbdVol = ipdBean.abdomenRetainingVolumeLastTime;
                    hisRx.ult = ipdBean.ultrafiltrationVolume;
                    hisRx.ulTreatTime = "1小时";
                    Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
                    EmtDataBase
                            .getInstance(TreatmentFragmentActivity.this)
                            .getRxDao()
                            .insertRx(hisRx);
//                });
                break;
//            case 2:
//                new Thread(() -> {
//                    RxEntity hisRx = new RxEntity();
//                    hisRx.time = EmtTimeUil.getTime();
//                    hisRx.perVol = tpdBean.peritonealDialysisFluidTotal;
//                    hisRx.perCycleVol = tpdBean.perCyclePerfusionVolume;
//                    hisRx.treatCycle = tpdBean.cycle;
//                    hisRx.firstPerVol = tpdBean.firstPerfusionVolume;
//                    hisRx.abdTime = tpdBean.abdomenRetainingTime;
//                    hisRx.endAbdVol = tpdBean.abdomenRetainingVolumeFinally;
//                    hisRx.lastTimeAbdVol = tpdBean.abdomenRetainingVolumeLastTime;
//                    hisRx.ult = tpdBean.ultrafiltrationVolume;
//                    hisRx.ulTreatTime = "1小时";
////                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
//                    EmtDataBase
//                            .getInstance(TreatmentFragmentActivity.this)
//                            .getRxDao()
//                            .insertRx(hisRx);
//                });
//                break;
//            case 3:
//                new Thread(() -> {
//                    RxEntity hisRx = new RxEntity();
//                    hisRx.time = EmtTimeUil.getTime();
//                    hisRx.perVol = ccpdBean.peritonealDialysisFluidTotal;
//                    hisRx.perCycleVol = ccpdBean.perCyclePerfusionVolume;
//                    hisRx.treatCycle = ccpdBean.cycle;
//                    hisRx.firstPerVol = ccpdBean.firstPerfusionVolume;
//                    hisRx.abdTime = ccpdBean.abdomenRetainingTime;
//                    hisRx.endAbdVol = ccpdBean.abdomenRetainingVolumeFinally;
//                    hisRx.lastTimeAbdVol = ccpdBean.abdomenRetainingVolumeLastTime;
//                    hisRx.ult = ccpdBean.ultrafiltrationVolume;
//                    hisRx.ulTreatTime = "1小时";
////                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
//                    EmtDataBase
//                            .getInstance(TreatmentFragmentActivity.this)
//                            .getRxDao()
//                            .insertRx(hisRx);
//                });
//                break;
//            case 4:
//                new Thread(() -> {
//                    RxEntity hisRx = new RxEntity();
//                    hisRx.time = EmtTimeUil.getTime();
//                    hisRx.perVol = aapdBean.total;
//                    hisRx.perCycleVol = aapdBean.bagVol;
//                    hisRx.treatCycle = aapdBean.c1;
//                    hisRx.firstPerVol = aapdBean.p1;
//                    hisRx.abdTime = aapdBean.r1;
//                    hisRx.endAbdVol = aapdBean.finalVol;
//                    hisRx.lastTimeAbdVol = 0;
//                    hisRx.ult = 0;
//                    hisRx.ulTreatTime = "1小时";
////                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
//                    EmtDataBase
//                            .getInstance(TreatmentFragmentActivity.this)
//                            .getRxDao()
//                            .insertRx(hisRx);
//                });
//                break;
//            case 7:
//                new Thread(() -> {
//                    RxEntity hisRx = new RxEntity();
//                    hisRx.time = EmtTimeUil.getTime();
//                    hisRx.perVol = kidBean.peritonealDialysisFluidTotal;
//                    hisRx.perCycleVol = kidBean.perCyclePerfusionVolume;
//                    hisRx.treatCycle = kidBean.cycle;
//                    hisRx.firstPerVol = kidBean.firstPerfusionVolume;
//                    hisRx.abdTime = kidBean.abdomenRetainingTime;
//                    hisRx.endAbdVol = kidBean.abdomenRetainingVolumeFinally;
//                    hisRx.lastTimeAbdVol = kidBean.abdomenRetainingVolumeLastTime;
//                    hisRx.ult = kidBean.ultrafiltrationVolume;
//                    hisRx.ulTreatTime = "1小时";
////                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
//                    EmtDataBase
//                            .getInstance(TreatmentFragmentActivity.this)
//                            .getRxDao()
//                            .insertRx(hisRx);
//                });
//                break;
//            case 8:
//                new Thread(() -> {
//                    RxEntity hisRx = new RxEntity();
//                    hisRx.time = EmtTimeUil.getTime();
//                    hisRx.perVol = expertBean.total;
//                    hisRx.perCycleVol = expertBean.cycleVol;
//                    hisRx.treatCycle = expertBean.cycle;
//                    hisRx.firstPerVol = expertBean.firstVol;
//                    hisRx.abdTime = expertBean.retainTime;
//                    hisRx.endAbdVol = expertBean.finalRetainVol;
//                    hisRx.lastTimeAbdVol = expertBean.lastRetainVol;
//                    hisRx.ult = expertBean.ultVol;
//                    hisRx.ulTreatTime = "1小时";
////                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
//                    EmtDataBase
//                            .getInstance(TreatmentFragmentActivity.this)
//                            .getRxDao()
//                            .insertRx(hisRx);
//                });
//                break;
        }
    }

    private void initPreViewPage() {
    }

    private boolean isClicking; // 在30S内是否点击过跳过或者暂停
    /**
     * 阶段转换30秒内不能点击
     */
    private final CountDownTimer skipTimer = new CountDownTimer(10 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            isClicking = true;
        }

        @Override
        public void onFinish() {
            isClicking = false;
            pauseLayout.setEnabled(true);
            skipLayout.setEnabled(true);
        }
    };

    /**
     * 阶段转换30秒内不可见
     */
    private final CountDownTimer stageTimer = new CountDownTimer(10 * 1000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
//            layoutSkipOrPause.setVisibility(View.VISIBLE);
            pauseLayout.setVisibility(View.VISIBLE);
            skipLayout.setVisibility(View.VISIBLE);
        }
    };

    private class MonitorTouchCountDownTimer extends CountDownTimer {

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
                    if (!isStart ) {
                        deliveryTherapy();
                    }
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
        Observable.interval(0, reportTime * 1000L, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        reportCompositeDisposable.add(disposableObserver);
    }

    private int totalDrainVolume;    //累计引流量
    /***** 总灌注量  灌注量  即治疗量  *******/
    private int totalPerfusionVolume;//累计灌注量
    /**** 总超滤量 = 引流量-治疗量 = 总引流量-总灌注量 ***/
    private int mUltrafiltrationVolume;
    private int totalAbdTime;
    public void initTreatmentHistory() {
        try {
            // 多模式
            switch (MyApplication.apdMode) {
                case 1:
//                    IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
//                    if (ipdBean.abdomenRetainingVolumeFinally > 0) {
//                        maxCycle = ipdBean.cycle + 1;
//                    } else {
//                        maxCycle = ipdBean.cycle;
//                    }
                    if (ipdBean == null) {
                        ipdBean = PdproHelper.getInstance().ipdBean();
                    }
                    maxCycle = ipdBean.cycle;
                    ipdLayout.setVisibility(View.VISIBLE);
                    aapdLayout.setVisibility(View.GONE);
                    svLayout.setVisibility(View.GONE);
//                maxCycle = ipdBean.cycle;
                    break;
                case 2:
//                    TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
//                    if (tpdBean.abdomenRetainingVolumeFinally > 0) {
//                        maxCycle = tpdBean.cycle + 1;
//                    } else {
//                        maxCycle = tpdBean.cycle;
//                    }
                    if (tpdBean == null) {
                        tpdBean =PdproHelper.getInstance().tpdBean();
                    }
                    maxCycle = tpdBean.cycle;
                    break;
                case 3:
//                    CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
//                    if (ccpdBean.abdomenRetainingVolumeFinally > 0) {
//                        maxCycle = ccpdBean.cycle + 1;
//                    } else {
//                        maxCycle = ccpdBean.cycle;
//                    }
                    if (ccpdBean == null) {
                        ccpdBean = PdproHelper.getInstance().ccpdBean();
                    }
                    maxCycle = ccpdBean.cycle;
                    break;
                case 4:
//                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
//                    if (aapdBean.finalVol == 0) {
//                        maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6;
//                    } else {
//                        maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6 + 1;
//                    }
                    ipdLayout.setVisibility(View.GONE);
                    aapdLayout.setVisibility(View.VISIBLE);
                    svLayout.setVisibility(View.GONE);
                    if (aapdBean == null) {
                        aapdBean = PdproHelper.getInstance().aapdBean();
                    }
                    setAapdBackground(aapdBean,currCycle);
                    aapdClick();
                    maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6;
                    break;
                case 7:
//                KidBean kidBean = PdproHelper.getInstance().kidBean();
//                    if (kidBean.abdomenRetainingVolumeFinally > 0) {
//                        maxCycle = kidBean.cycle + 1;
//                    } else {
//                        maxCycle = kidBean.cycle;
//                    }
                    if (kidBean == null) {
                        kidBean = PdproHelper.getInstance().kidBean();
                    }
                    maxCycle = kidBean.cycle;
                    break;
                case 8:
                    if (expertBean == null) {
                        expertBean = PdproHelper.getInstance().expertBean();
                    }
//                    ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                    if (expertBean.cycleMyself) {
                        maxCycle = Collections.max(expertBean.baseSupplyCycle)
                                > Collections.max(expertBean.osmSupplyCycle) ? Collections.max(expertBean.baseSupplyCycle) :
                                Collections.max(expertBean.osmSupplyCycle);
                    } else {
//                        if (expertBean.finalRetainVol > 0) {
//                            maxCycle = expertBean.cycle + 1;
//                        } else {
//                            maxCycle = expertBean.cycle;
//                        }
                        maxCycle = expertBean.cycle;
                    }
                    if (expertBean.osmSupplyCycle.contains(1)) {
                        conTv.setText(conStr+expertBean.con_2+"%");
                        curCon = expertBean.con_2;
                    } else {
                        conTv.setText(conStr+expertBean.con_1+"%");
                        curCon = expertBean.con_1;
                    }
                    conTv.setVisibility(View.VISIBLE);
                    ipdLayout.setVisibility(View.GONE);
                    aapdLayout.setVisibility(View.GONE);
                    svLayout.setVisibility(View.VISIBLE);
                    reviseConfirm.setVisibility(View.VISIBLE);
                    svClick();
                    setSvBg(expertBean.cycleMyself);
//                if (expertBean.finalRetainVol > 0) {
//                    maxCycle = expertBean.cycle + 1;
//                } else {
//                    maxCycle = expertBean.cycle;
//                }
                    break;
            }

            tvMaxCycle.setText(String.valueOf(maxCycle));
            if (mFragment3.getActivity() != null) {
                mFragment3.setMaxCycle(maxCycle);
            } else {
                if (detailedBean == null) {
                    detailedBean = new DetailedBean();
                }
                detailedBean.setMaxCycle(maxCycle);
            }

            if (pdData == null) {
                pdData = MyApplication.pdData;
            }
            pdData.setRxCycle(maxCycle);
        } catch (Exception e) {
            Log.e("治疗记录", e.getMessage());
        }
    }

    private String conStr = "当前浓度为:";
    private double curCon;

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

            mTreatmentPrescription.totalPerfusionVolume = totalPerfusionVolume;
//        mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume;
            mTreatmentPrescription.totalUltrafiltrationVolume = mUltrafiltrationVolume;
            mTreatmentPrescription.peritonealDialysisFluidTotal = totalDrainVolume;
            mTreatmentPrescription.periodicities = maxCycle;
            Log.e("DataUpload","总引流:"+totalDrainVolume+"总关注:"+totalPerfusionVolume+"超滤:"+mUltrafiltrationVolume);
            switch (MyApplication.apdMode) {
                case 1:
//                IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                    mTreatmentPrescription.firstPerfusionVolume = ipdBean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = ipdBean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = ipdBean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = ipdBean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = ipdBean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = ipdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = ipdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = ipdBean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = currCycle;
                    mTreatmentDataUpload.totalInjectAmount = ipdBean.peritonealDialysisFluidTotal;
                    break;
                case 2:
//                TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                    mTreatmentPrescription.firstPerfusionVolume = tpdBean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = tpdBean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = tpdBean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = tpdBean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = tpdBean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = tpdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = tpdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = tpdBean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = currCycle;
                    mTreatmentDataUpload.totalInjectAmount = tpdBean.peritonealDialysisFluidTotal;
                    break;
                case 3:
//                CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                    mTreatmentPrescription.firstPerfusionVolume = ccpdBean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = ccpdBean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = ccpdBean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = ccpdBean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = ccpdBean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = ccpdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = ccpdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = ccpdBean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = currCycle;
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
//                KidBean kidBean = PdproHelper.getInstance().kidBean();
                    mTreatmentPrescription.firstPerfusionVolume = kidBean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = kidBean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = kidBean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = kidBean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = kidBean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = kidBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = kidBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = kidBean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = currCycle;
                    mTreatmentDataUpload.totalInjectAmount = kidBean.peritonealDialysisFluidTotal;
                    break;
//            case 8:
//                break;
                default:
//                TreatmentParameterEniity entity = PdproHelper.getInstance().getTreatmentParameter();
                    mTreatmentPrescription.firstPerfusionVolume = ipdBean.firstPerfusionVolume;
                    mTreatmentPrescription.perCyclePerfusionVolume = ipdBean.perCyclePerfusionVolume;
                    mTreatmentPrescription.abdomenRetainingVolumeFinally = ipdBean.abdomenRetainingVolumeFinally;
                    mTreatmentPrescription.abdomenRetainingTime = ipdBean.abdomenRetainingTime;
                    mTreatmentPrescription.abdomenRetainingVolumeLastTime = ipdBean.abdomenRetainingVolumeLastTime;
                    mTreatmentPrescription.ultrafiltrationVolume = ipdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.ultrafiltration = ipdBean.ultrafiltrationVolume;
                    mTreatmentDataUpload.lastLeave = ipdBean.abdomenRetainingVolumeFinally;
                    mTreatmentDataUpload.times = currCycle;
                    mTreatmentDataUpload.totalInjectAmount = ipdBean.peritonealDialysisFluidTotal;
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
            mTreatmentDataUpload.startTime = startTime;
            mTreatmentDataUpload.endTime = endTime;

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
            for (int i = 0; i < pdData.getPdEntityDataList().size(); i++) {

                if (i == 0) {
                    auxiliaryFlushingVolume.append(pdData.getPdEntityDataList().get(i).getUfVol());
                    drainages.append(pdData.getPdEntityDataList().get(i).getDrainage());
                    mTreatmentDataUpload.drainageTime = pdData.getPdEntityDataList().get(i).getDrainTime() / 60;
                } else if (i == 1) {
                    cycles.append(pdData.getPdEntityDataList().get(i).getCycle());
                    inFlows.append(pdData.getPdEntityDataList().get(i).getPreVol());
                    inFlowTime.append(pdData.getPdEntityDataList().get(i).getPreTime()/ 60);
                    leaveWombTime.append(pdData.getPdEntityDataList().get(i).getAbdTime() / 60);
                    exhaustTime.append(pdData.getPdEntityDataList().get(i).getDrainTime()/ 60);
                    drainages.append(",").append(pdData.getPdEntityDataList().get(i).getDrainage());
                    auxiliaryFlushingVolume.append(",").append(pdData.getPdEntityDataList().get(i).getUfVol());
                    abdominalVolumeAfterInflow.append(pdData.getPdEntityDataList().get(i).getAbdVol());
                    drainageTargetValue.append(pdData.getPdEntityDataList().get(i).getDrainTargetVol());
                    estimatedResidualAbdominalFluid.append(pdData.getPdEntityDataList().get(i).getEstimate());
                } else {
                    cycles.append(",").append(pdData.getPdEntityDataList().get(i).getCycle());
                    inFlows.append(",").append(pdData.getPdEntityDataList().get(i).getPreVol());
                    inFlowTime.append(",").append(pdData.getPdEntityDataList().get(i).getPreTime()/ 60);
                    leaveWombTime.append(",").append(pdData.getPdEntityDataList().get(i).getAbdTime()/ 60);
                    exhaustTime.append(",").append(pdData.getPdEntityDataList().get(i).getDrainTime()/ 60);
                    drainages.append(",").append(pdData.getPdEntityDataList().get(i).getDrainage());
                    auxiliaryFlushingVolume.append(",").append(pdData.getPdEntityDataList().get(i).getUfVol());
                    abdominalVolumeAfterInflow.append(",").append(pdData.getPdEntityDataList().get(i).getAbdVol());
                    drainageTargetValue.append(",").append(pdData.getPdEntityDataList().get(i).getDrainTargetVol());
                    estimatedResidualAbdominalFluid.append(",").append(pdData.getPdEntityDataList().get(i).getEstimate());
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
            mTreatmentDataUpload.lastLeaveTime = finalPerTime;
            mTreatmentDataUpload.treatmentPrescriptionUploadVo = mTreatmentPrescription;

        } catch (Exception e) {
            Log.e("数据上传", e.getLocalizedMessage());
        }
    }

    @BindView(R.id.netTv)
    TextView netTv;
    @BindView(R.id.wifiIv)
    ImageView wifiIv;

    private int status;
    @Subscribe(code = RxBusCodeConfig.NET_STATUS)
    public void receiveNetStatus(String net) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status = Integer.parseInt(net);
                if (status != -1) {
                    if (status == 1) {
                        wifiIv.setVisibility(View.VISIBLE);
                    } else if (status == 0) {
                        netTv.setTextColor(Color.WHITE);
                        wifiIv.setVisibility(View.INVISIBLE);
                    }
                } else {
                    netTv.setTextColor(Color.RED);
                    wifiIv.setVisibility(View.INVISIBLE);
                }
                Log.e("net","trNet:"+status);
            }
        });

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
                stopSound();
                //30分钟内来电，断电提醒弹窗还在，关闭弹窗
                if (treatmentAlarmDialog != null && treatmentAlarmDialog.isShowing()) {
                    treatmentAlarmDialog.dismiss();
                }
                APIServiceManage.getInstance().postApdCode("E0013");
//                speak("电源已恢复");
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
        silencer();

//        if (currCycle < 10) {
//            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"29");
//        } else {
//            APIServiceManage.getInstance().postApdCode("T" + currCycle + "29");
//        }
        speak(msg);
        isShowAlarmDialog = true;
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(msg)
                .setBtnFirst("长按跳过", R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(msg);
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
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
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
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
                    stopSound();
                })
                .show();

    }

    private boolean isInitial ;
    private boolean isEndPre;
    private int curUpperWeight;
    private boolean isEndSupply;
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

                powerTv.setText(String.valueOf(mReceiveDeviceBean.batteryLevel));
//                tempTv.setText(getTemp(mReceiveDeviceBean.temp) + "℃");
                tempTv.setText(getTemp(mReceiveDeviceBean.temp) + "℃");
                if (mReceiveDeviceBean.supply1.equals("01") || mReceiveDeviceBean.supply1.equals("81")) {
                    isEndSupply = false;
                } else if (mReceiveDeviceBean.supply2.equals("01") || mReceiveDeviceBean.supply2.equals("81")) {
                    isEndSupply = true;
                }
                if (mFragment3.getActivity() != null) {
//                    mFragment3 = new TreatmentFragmentItem3();
//                    Log.e("mFragment3",""+mFragment3.currCycle);
                    mFragment3.setDeviceStatusInfo(mReceiveDeviceBean);
                } else {
                    if (detailedBean == null) {
                        detailedBean = new DetailedBean();
                    }
                    detailedBean.setVaccum(mReceiveDeviceBean.vaccum);
                    detailedBean.setBatteryLevel(mReceiveDeviceBean.batteryLevel);
                    detailedBean.setSupply1(mReceiveDeviceBean.supply1);
                    detailedBean.setSupply2(mReceiveDeviceBean.supply2);
                    detailedBean.setPerfuse(mReceiveDeviceBean.perfuse);
                    detailedBean.setDrain(mReceiveDeviceBean.drain);
                    detailedBean.setSafe(mReceiveDeviceBean.safe);
                    detailedBean.setIsAcPowerIn(mReceiveDeviceBean.isAcPowerIn);
                    detailedBean.setTemp(mReceiveDeviceBean.temp);
                    detailedBean.setIsT0Err(mReceiveDeviceBean.isT0Err);
                    detailedBean.setIsT1Err(mReceiveDeviceBean.isT1Err);
                    detailedBean.setIsT2Err(mReceiveDeviceBean.isT2Err);
                    detailedBean.setUpper(mReceiveDeviceBean.upper - PdproHelper.getInstance().getOtherParamBean().upper);
                    detailedBean.setLower(mReceiveDeviceBean.lower - PdproHelper.getInstance().getOtherParamBean().lower);
                }
                if (!isInitial) {
                    isInitial = true;
                    if (mFragment3.getActivity() != null) {
                        mFragment3.setFirst(mReceiveDeviceBean.upper - PdproHelper.getInstance().getOtherParamBean().upper,
                                mReceiveDeviceBean.lower - PdproHelper.getInstance().getOtherParamBean().lower);
                    }
                    curUpperWeight = mReceiveDeviceBean.upper - PdproHelper.getInstance().getOtherParamBean().upper;
                    dialysateInitialValue = mReceiveDeviceBean.upper - PdproHelper.getInstance().getOtherParamBean().upper;
                    lowFirstValue = mReceiveDeviceBean.lower - PdproHelper.getInstance().getOtherParamBean().lower;
                }

//                if (mReceiveDeviceBean.temp >= 401) {
////                    runOnUiThread(() -> {
//                        MsgBean msgBean = new MsgBean();
//                        msgBean.setMsg("温度过高,请注意!!!");
//                        if (msgBeanList == null) {
//                            msgBeanList = new ArrayList<>();
//                        }
//                        msgBeanList.add(msgBean);
//                        if (msgAdapter != null) {
//                            recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
//                        }
////                    });
//                } else if (mReceiveDeviceBean.temp <= 338) {
////                    runOnUiThread(() -> {
//                        MsgBean msgBean = new MsgBean();
//                        msgBean.setMsg("温度过低,请注意!!!");
//                        if (msgBeanList == null) {
//                            msgBeanList = new ArrayList<>();
//                        }
//                        msgBeanList.add(msgBean);
//                        if (msgAdapter != null) {
//                            recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
//                        }
////                    });
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
                tempTv.setText(getTemp(mBean.temp) + "℃");
                if (mFragment3.getActivity() != null) {
                    mFragment3.setDeviceStatusInfo(mBean);
                } else {
                    if (detailedBean == null) {
                        detailedBean = new DetailedBean();
                    }
                    detailedBean.setVaccum(mBean.vaccum);
                    detailedBean.setBatteryLevel(mBean.batteryLevel);
                    detailedBean.setSupply1(mBean.supply1);
                    detailedBean.setSupply2(mBean.supply2);
                    detailedBean.setPerfuse(mBean.perfuse);
                    detailedBean.setDrain(mBean.drain);
                    detailedBean.setSafe(mBean.safe);
                    detailedBean.setIsAcPowerIn(mBean.isAcPowerIn);
                    detailedBean.setTemp(mBean.temp);
                    detailedBean.setIsT0Err(mBean.isT0Err);
                    detailedBean.setIsT1Err(mBean.isT1Err);
                    detailedBean.setIsT2Err(mBean.isT2Err);
                    detailedBean.setUpper(mBean.upper);
                    detailedBean.setLower(mBean.lower);
                }
            }
        });
    }

    private final BigDecimal mTen = new BigDecimal(10);

    private float getTemp(int temp) {
        temp = temp-20;
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
//            speak("完成治疗");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    btnFinishTreatment.setVisibility(View.INVISIBLE);
                    pauseLayout.setVisibility(View.INVISIBLE);
                    skipLayout.setVisibility(View.INVISIBLE);
                    initDb();
                    showCompleteDialog();
                }
            });
            stopCmdLoopTimer();
            stopLoopCountDown();
            stopLoopTimer();
        }

    }
    private PdData pdData;
    public List<PdData.PdEntityData> pdEntityDataList;
    private PdData.PdEntityData pdEntityData;
    private PdEntity pdEntity;
    private PdEntity.PdInfoEntity pdInfoEntity;

    private void initDb() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        try {
            new Thread(() -> {
                if (pdEntity == null) {
                    pdEntity = new PdEntity();
                }
                endTime = EmtTimeUil.getTime();
                Log.e("获取系统时间", "结束治疗时间: " + endTime);
                pdEntity.startTime = startTime;
                pdEntity.endTime = endTime;
                pdEntity.totalDrainVol = totalDrainVolume;
                pdEntity.totalPerVol = totalPerfusionVolume;

                pdEntity.phone = MyApplication.phone;
                if (MyApplication.apdMode == 1) {
                    pdEntity.totalVol = ipdBean.peritonealDialysisFluidTotal;

                    pdEntity.cycle = currCycle;
                    pdEntity.finalAbdTime = ipdBean.abdomenRetainingVolumeFinally;
                    pdEntity.firstPerfusionVolume = ipdBean.firstPerfusionVolume;
                    pdEntity.perCyclePerfusionVolume = ipdBean.perCyclePerfusionVolume;
                    pdEntity.abdomenRetainingVolumeFinally = ipdBean.abdomenRetainingVolumeFinally;
                    pdEntity.abdomenRetainingTime = ipdBean.abdomenRetainingTime;
                    pdEntity.abdomenRetainingVolumeLastTime = ipdBean.abdomenRetainingVolumeLastTime;
                    pdEntity.ultrafiltrationVolume = ipdBean.ultrafiltrationVolume;
                }
                Log.e("initDb","总引流:"+totalDrainVolume+"总关注:"+totalPerfusionVolume+"超滤:"+mUltrafiltrationVolume);
                pdEntity.firstTime = 0;
                pdEntity.finalTime = finalPerTime;
                pdEntity.totalUltVol = mUltrafiltrationVolume;
                pdEntity.pdInfoEntities = pdInfoEntities;
                EmtDataBase
                        .getInstance(TreatmentFragmentActivity.this)
                        .getPdDao().insertPd(pdEntity);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isStart;

    /**
     * 执行指令成功
     *
     * @param topic
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
    public void receiveCmdResultOk(String topic) {
        if (topic.contains(CommandSendConfig.METHOD_TREATMENT_START)) {//开始治疗
            MyApplication.currCmd = "";
            speak("治疗开始");
            isStart = true;
//            stopCmdLoopTimer();
        } else if (topic.equals(TOPIC_TREATMENT_ABORT) || topic.equals(TOPIC_TREATMENT_FINISH)) {//停止治疗
            stopCmdLoopTimer();
            stopLoopCountDown();
            stopLoopTimer();
            stopSound();
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
                    pauseLayout.setVisibility(View.INVISIBLE);
                    skipLayout.setVisibility(View.INVISIBLE);
                    showCompleteDialog();
                }
            });
        } else {

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
        if (topic.contains(CommandSendConfig.METHOD_TREATMENT_START) && bean.result.msg.equals("treatment task is running")) {//开始治疗
            isStart = true;
//            showTipsCommonDialog("开始治疗异常");
//            stopCmdLoopTimer();
            // 多模式
//            deliveryTherapy();
            // 单模式
//            startTreat();
//            setTreatmentCmd();
        } else if (topic.contains(CommandSendConfig.METHOD_TREATMENT_ABORT)) {//终止治疗
//            showTipsCommonDialog("终止治疗异常");
//            isComplete = true;
//            stopCmdLoopTimer();
//            stopLoopCountDown();
//            stopLoopTimer();
//            stopSound();
////            new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    initDb();
////                }
////            }).start();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    initDb();
////                    btnFinishTreatment.setVisibility(View.INVISIBLE);
//                    pauseLayout.setVisibility(View.INVISIBLE);
//                    skipLayout.setVisibility(View.INVISIBLE);
//                    showCompleteDialog();
//                }
//            });
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
                    drainValveFaultDialog("");
                }
            });
        } else if (Objects.equals(checkPerBean.drain, "ok")){
            runOnUiThread(()-> {
                if (treatmentAlarmDialog == null) {
                    treatmentAlarmDialog = new TreatmentAlarmDialog(this);
                }
                if (treatmentAlarmDialog.isShowing()) {
                    stopSound();
                    treatmentAlarmDialog.dismiss();
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

        try {

            if (receiveBean.publish.msg.contains(MSG_CHECK_TEMPERATURE)) {//灌注前检查温度: 在灌注自检开始
                Log.e("处理灌注数据", "灌注前检查温度 : " + myGson.toJson(receiveBean.publish));
            } else if (receiveBean.publish.msg.contains(MSG_PROCESS_START)) {//1、灌注过程开始
                Log.e("处理灌注数据", "1、灌注过程开始: " + myGson.toJson(receiveBean.publish));
//            speak("开始灌注");
                ReceivePerfuseStartBean startBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseStartBean.class);
                currCycle = startBean.cycle;
//            tvCurrTime.setText("00:00");
                runOnUiThread(() -> {
                    pauseLayout.setVisibility(View.INVISIBLE);
                    skipLayout.setVisibility(View.INVISIBLE);
                    stageTimer.start();

                    pdEntityData = new PdData.PdEntityData();
                    if (pdEntityDataList == null) {
                        pdEntityDataList = new ArrayList<>();
                    }
                    pdEntityData.setCycle(startBean.cycle);
                    pdEntityDataList.add(pdEntityData);
                    
                    pdInfoEntity = new PdEntity.PdInfoEntity();
                    if (pdInfoEntities == null) {
                        pdInfoEntities = new ArrayList<>();
                    }
                    pdInfoEntity.cycle = startBean.cycle;
                    pdInfoEntities.add(pdInfoEntity);
                    
                    switch (MyApplication.apdMode) {
                        case 1:
//                        IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                            if (currCycle >= maxCycle  && ipdBean.isFinalSupply) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(2, 4, 3, 2));
                            } else if (currCycle == 1) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
                            }
                            break;
                        case 2:
//                        TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                            if (currCycle == maxCycle && tpdBean.isFinalSupply && tpdBean.abdomenRetainingVolumeFinally != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(2, 4, 3, 2));
                            } else if (currCycle == 1) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
                            }
                            break;
                        case 3:
//                        CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                            if (currCycle == maxCycle  && ccpdBean.isFinalSupply && ccpdBean.abdomenRetainingVolumeFinally != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(2, 4, 3, 2));
                            } else if (currCycle == 1) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
                            }
                            break;
                        case 4:
//                        AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
//                            if (aapdFragment.getActivity() != null) {
//                                aapdFragment.setBackground(aapdBean, currCycle);
//                            }
                            if (aapdBean == null) {
                                aapdBean = PdproHelper.getInstance().aapdBean();
                            }
                            setAapdBackground(aapdBean, currCycle);
//                        if (currCycle == 1) {
//                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.isFinalSupply?1:2, aapdBean.a1 == 1?1:0, aapdBean.a1 == 0?1:0, aapdBean.p1));
//                        } else
                            if (aapdBean.c1 - startBean.cycle == 0 && aapdBean.c2 != 0 && aapdBean.p2 != 0 && aapdBean.r2 != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a2 == 0 ? 2 : 1, 3, 4, aapdBean.a2 == 0 ? 2 : 1));
                            } else if (aapdBean.c2 + aapdBean.c1 - startBean.cycle == 0 && aapdBean.c3 != 0 && aapdBean.p3 != 0 && aapdBean.r3 != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a3 == 0 ? 2 : 1, 3, 4, aapdBean.a3 == 0 ? 2 : 1));
                            } else if (aapdBean.c3 + aapdBean.c2 + aapdBean.c1 - startBean.cycle == 0 && aapdBean.c4 != 0 && aapdBean.p4 != 0 && aapdBean.r4 != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a4 == 0 ? 2 : 1, 3, 4, aapdBean.a4 == 0 ? 2 : 1));
                            } else if (aapdBean.c4 + aapdBean.c3 + aapdBean.c2 + aapdBean.c1 - startBean.cycle == 0 && aapdBean.c5 != 0 && aapdBean.p5 != 0 && aapdBean.r5 != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a5 == 0 ? 2 : 1, 3, 4, aapdBean.a5 == 0 ? 2 : 1));
                            } else if (aapdBean.c5 + aapdBean.c4 + aapdBean.c3 + aapdBean.c2 + aapdBean.c1 - startBean.cycle == 0 && aapdBean.c6 != 0 && aapdBean.p6 != 0 && aapdBean.r6 != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a6 == 0 ? 2 : 1, 3, 4, aapdBean.a6 == 0 ? 2 : 1));
                            }
                            break;
                        case 7:
//                        KidBean kidBean = PdproHelper.getInstance().kidBean();
                            if (currCycle == 1) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
                            } else if (currCycle == maxCycle  && kidBean.isFinalSupply && kidBean.abdomenRetainingVolumeFinally != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(2, 4, 3, 2));
                            }
//                        sendToMainBoard(CommandDataHelper.getInstance().special(kidBean.isFinalSupply?1:2, kidBean.isFinalSupply?0:1, kidBean.isFinalSupply?1:0, kidBean.abdomenRetainingVolumeFinally));
                            break;
                        case 8:
//                        ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                            if (expertBean == null) {
                                expertBean = PdproHelper.getInstance().expertBean();
                            }
                            if (expertBean.cycleMyself) {
                                if (maxCycle >= startBean.cycle ) {
                                    sendToMainBoard(CommandDataHelper.getInstance().special(expertBean.baseSupplyCycle.contains(startBean.cycle + 1) ? 2 : 1,
                                            3,
                                            4,
                                            expertBean.baseSupplyCycle.contains(startBean.cycle) ? 2 : 1
                                    ));
                                }
                            }else {
                                if (currCycle == maxCycle  && expertBean.isFinalSupply && expertBean.finalRetainVol != 0) {
                                    sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                                }
                            }
                            break;
                    }
                    switchTreatmentStatus(1);
//                if (PdproHelper.getInstance().getUserParameterBean().isNight) {
//                    sendToMainBoard(CommandDataHelper.getInstance().LedOpen("perfuse", true,0));
//                }
                    Log.e("处理灌注数据", "currCycle--" + currCycle);
//                if (currCycle == 1) {
//                    cycles += currCycle;
//                } else if (currCycle > 1){
//                    cycles += ","+currCycle;
//                }
                });
                if (currCycle < 10) {
                    APIServiceManage.getInstance().postApdCode("T0" + currCycle + "01");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "01");
                }
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"01");
            } else if (receiveBean.publish.msg.equals("low drop fault")) { // 引流阀门故障
                runOnUiThread(() -> {
                    if (!isShowAlarmDialog) {
                        drainValveFaultDialog("可能出现漏液,请检查引流阀");
                    }
                });
            } else if (receiveBean.publish.msg.contains("perfuse temp over 41")) { // 引流阀门故障
                runOnUiThread(() -> {
                    MsgBean msgBean = new MsgBean();
                    msgBean.setMsg("温度过高,请注意!!!");
                    if (msgBeanList == null) {
                        msgBeanList = new ArrayList<>();
                    }
                    msgBeanList.add(msgBean);
                    if (msgAdapter != null) {
                        recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
                    }
                });
            } else if (receiveBean.publish.msg.contains("perfuse temp down 34")) { // 引流阀门故障
                runOnUiThread(() -> {
                    MsgBean msgBean = new MsgBean();
                    msgBean.setMsg("温度过低,请注意!!!");
                    if (msgBeanList == null) {
                        msgBeanList = new ArrayList<>();
                    }
                    msgBeanList.add(msgBean);
                    if (msgAdapter != null) {
                        recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
                    }
                });
            } else if (receiveBean.publish.msg.contains("perfuse temp recove ok")) { // 引流阀门故障
                runOnUiThread(() -> {
                    MsgBean msgBean = new MsgBean();
                    msgBean.setMsg("温度已恢复正常");
                    if (msgBeanList == null) {
                        msgBeanList = new ArrayList<>();
                    }
                    msgBeanList.add(msgBean);
                    if (msgAdapter != null) {
                        recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
                    }
                });
            } else if (receiveBean.publish.msg.contains("perfuse temp ok")) { // 引流阀门故障
                runOnUiThread(() -> {
//                    MsgBean msgBean = new MsgBean();
//                    msgBean.setMsg("温度已恢复正常");
//                    if (msgBeanList == null) {
//                        msgBeanList = new ArrayList<>();
//                    }
//                    msgBeanList.add(msgBean);
//                    if (msgAdapter != null) {
//                        recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
//                    }
                });
            } else if (receiveBean.publish.msg.contains(MSG_PROCESS_FINISH)) {//2、灌注过程完成
                Log.e("处理灌注数据", "2、灌注过程完成: " + myGson.toJson(receiveBean.publish));
                stopLoopTimer();
//                speak("灌注完成");
                ReceivePerfuseProcessFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseProcessFinishBean.class);
                if (mBean != null) {
                    runOnUiThread(() -> {
                        if (pdEntityDataList.size() != mBean.cycle + 1) {
                            pdEntityData = new PdData.PdEntityData();
                            if (pdEntityDataList == null) {
                                pdEntityDataList = new ArrayList<>();
                            }
                            pdEntityData.setCycle(mBean.cycle);
                            pdEntityDataList.add(pdEntityData);
                        }
                        pdEntityData.setPreTime(currPerfusionTime);
                        pdEntityData.setPreVol(mBean.perfuse);
                        pdEntityData.setAbdVol(mBean.retain);

                        int time = pdEntityData.getPreTime() ;
                        if (time !=  0) {
                            roteTv.setText(String.valueOf(Math.max(pdEntityData.getPreVol(),0) / time * 60));
                        }

                        if (pdInfoEntities.size() != mBean.cycle + 1) {
                            pdInfoEntity = new PdEntity.PdInfoEntity();
                            if (pdInfoEntities == null) {
                                pdInfoEntities = new ArrayList<>();
                            }
                            pdInfoEntity.cycle = mBean.cycle;
                            pdInfoEntities.add(pdInfoEntity);
                        }
                        pdInfoEntity.preTime = currPerfusionTime / 60;
                        pdInfoEntity.preVol = mBean.perfuse;
                        pdInfoEntity.remain = mBean.retain;
                        if (mFragment3.getActivity() != null) {
                            mFragment3.setCurrPerfusionVolume(mBean.perfuse);
                            mFragment3.setWaittingVolume(mBean.retain);
                        }
                        if (mBean.cycle > maxCycle) {
                            finalPerTime = currPerfusionTime / 60;
                        }
                        currCycle = mBean.cycle;
                        if (currCycle < maxCycle) {
                            modifyDuringTreatment(mBean.cycle);
                        }
                        currentVolTv.setText(String.valueOf(mBean.perfuse));
                    });
                }
//            switchTreatmentStatus(2);
            } else if (receiveBean.publish.msg.contains(MSG_SELFCHECK_START)) {//3、灌注自检开始
                Log.e("处理灌注数据", "3、灌注自检开始: " + myGson.toJson(receiveBean.publish));
//            showLoading("正在灌注自检中，请稍后...");
//                speak("正在灌注自检中，请稍后...");
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
//                speak("开始灌注");
                ReceivePerfuseBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseBean.class);
                if (mBean != null) {
                    currCycle = mBean.cycle;
//                    if (detailedBean == null) {
//                        detailedBean = new DetailedBean();
//                    }
//                    detailedBean.setCurrCycle(mBean.cycle);
//                    detailedBean.setPerTargetVolume(mBean.perfuseTarget);
                    if (currCycle < 10) {
                        APIServiceManage.getInstance().postApdCode("T0" + currCycle + "21");
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
            } else if (receiveBean.publish.msg.contains("perfuse disturb")) {
                runOnUiThread(()-> {
                    faultDialog("请不要干扰上位称(灌注异常)","");
                });
            } else if (receiveBean.publish.msg.contains(MSG_PERFUSE_RUNNING)) {//7、灌注进行中
                ReceivePerfuseRunningBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceivePerfuseRunningBean.class);
                String mCurrentVolume = String.valueOf(Math.max(mBean.perfuse, 0));
                Log.e("处理灌注数据", "7、灌注进行中: " + myGson.toJson(receiveBean.publish) + " ，实时灌注量：" + mCurrentVolume);
                currCycle = mBean.cycle;
                runOnUiThread(() -> {
                    if (MyApplication.apdMode == 8) {
                        conTv.setText(conStr+mixCon(curCon, curUpperWeight, isEndSupply?expertBean.con_2 : expertBean.con_1, Math.max(mBean.perfuse, 0))+"%");

                    }
                    currentVolTv.setText(mCurrentVolume);
                    if (pdEntityDataList.size() != mBean.cycle + 1) {
                        pdEntityData = new PdData.PdEntityData();
                        if (pdEntityDataList == null) {
                            pdEntityDataList = new ArrayList<>();
                        }
                        pdEntityData.setCycle(mBean.cycle);
                        pdEntityDataList.add(pdEntityData);
                    }
                    pdEntityDataList.get(mBean.cycle).setPreVol(mBean.perfuse);
                    if (pdInfoEntities.size() != mBean.cycle + 1) {
                        pdInfoEntity = new PdEntity.PdInfoEntity();
                        if (pdInfoEntities == null) {
                            pdInfoEntities = new ArrayList<>();
                        }
                        pdInfoEntity.cycle = mBean.cycle;
                        pdInfoEntities.add(pdInfoEntity);
                    }
                    pdInfoEntities.get(mBean.cycle).preVol = mBean.perfuse;

                    if (null != mFragment3.getActivity()) {
                        mFragment3.setPerfusionTargetVolume(mBean.cycle, mBean.perfuseTarget);
                        mFragment3.setCurrPerfusionVolume(mBean.perfuse);
                        mFragment3.setWaittingVolume(mBean.retain);
                    } else {
    //                if ((mTreatmentParameterEniity.perfusionWarningValue < Integer.valueOf(mCurrentVolume)) && !isShowExceedsMaxValueDialog) {
    //                    perfusionMaxValueDialog();
    //                }
                        if (detailedBean == null) {
                            detailedBean = new DetailedBean();
                        }
                        detailedBean.setCurrCycle(mBean.cycle);
                        detailedBean.setPerTargetVolume(mBean.perfuseTarget);
                        detailedBean.setRetain(mBean.retain);
                        detailedBean.setCurrCyclePerfusionVolume(mBean.perfuse);
                    }
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
                    stageIv.setImageResource(R.drawable.supply_zl);
                    assistIv.setVisibility(View.VISIBLE);
                    assistIv.setImageResource(R.drawable.treatment_icon_volume_day);
                    if (null != mFragment3.getActivity()) {
    //                    sendToMainBoard(CommandDataHelper.getInstance().LedOpen("perfuse", true, dayFlag));
                        mFragment3.setSupplyTargetValue(mBean.supplyTarget, mBean.supplyProtect, mBean.supply);
                    } else {
                        if (detailedBean == null) {
                            detailedBean = new DetailedBean();
                        }
                        detailedBean.setmSupplyTargetValue(mBean.supplyTarget);
                        detailedBean.setmSupplyTargetProtectionValue(mBean.supplyProtect);
                        detailedBean.setSupplyVol(mBean.supply);
                    }
                });
//            speak("灌注补液开始");
            } else if (receiveBean.publish.msg.contains(MSG_SUPPLY_FINISH)) {//12、灌注补液完成
                Log.e("处理灌注补液数据", "12、灌注补液完成:" + myGson.toJson(receiveBean.publish));
//            speak("灌注补液完成");
                runOnUiThread(() -> {
                    stageIv.setImageResource(R.drawable.treatment_icon_volume_day);
                    assistIv.setVisibility(View.GONE);
//                    curCon = mixCon(curCon, mBean.upperWeight - mBean.supply, isEndSupply ? expertBean.con_2 : expertBean.con_1, mBean.supply);

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
        } catch (Exception e) {
            Log.e("灌注数据", e.getLocalizedMessage());
        }
    }

    /**
     * 处理留腹数据
     *
     * @param receiveBean
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_TREATMENT_RETAIN_DATA)
    public void receiveWaitingData(ReceivePublicDataBean receiveBean) {
        try {
            if (receiveBean.publish.msg.contains(MSG_PROCESS_START)) {//1、留腹过程开始
                Log.e("处理留腹数据", "1、留腹过程开始:" + myGson.toJson(receiveBean.publish));
                //{"data":{"cycle":1.0,"ultraFiltration":0.0,"currentDrainEstimate":0.0,"nextPefuseEstimate":500.0,"expectRetainTime":900.0,"retainTime":0.0},"msg":"process start","topic":"treatment/retain"}
                ReceiveRetainProcessStartBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainProcessStartBean.class);
//            APIServiceManage.getInstance().postApdCode("T" + currCycle + "31");
                if (currCycle < 10) {
                    APIServiceManage.getInstance().postApdCode("T0" + currCycle + "31");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "31");
                }
                runOnUiThread(() -> {
                    pauseLayout.setVisibility(View.INVISIBLE);
                    skipLayout.setVisibility(View.INVISIBLE);
                    stageTimer.start();
                });
                        // 多模式
                switch (MyApplication.apdMode) {
                    case 1:
//                        IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                        if (currCycle >= maxCycle  && ipdBean.isFinalSupply) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                        } else if (currCycle < maxCycle) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
                        }
                        break;
                    case 2:
//                        TpdBean tpdBean = PdproHelper.getInstance().tpdBean();
                        if (currCycle == maxCycle  && tpdBean.isFinalSupply && tpdBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                        } else if (currCycle < maxCycle) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
                        }
                        break;
                    case 3:
//                        CcpdBean ccpdBean = PdproHelper.getInstance().ccpdBean();
                        if (currCycle == maxCycle  && ccpdBean.isFinalSupply && ccpdBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                        } else if (currCycle < maxCycle) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
                        }
                        break;
                    case 4:
//                        AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
//                        if (aapdFragment.getActivity() != null) {
//                            aapdFragment.setBackground(aapdBean, currCycle);
//                        }
                        if (aapdBean == null) {
                            aapdBean = PdproHelper.getInstance().aapdBean();
                        }
                        setAapdBackground(aapdBean,currCycle);
//                        if (currCycle == 1) {
//                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.isFinalSupply?1:2, aapdBean.a1 == 1?1:0, aapdBean.a1 == 0?1:0, aapdBean.p1));
//                        } else
                        if (aapdBean.c1 - mBean.cycle == 0 && aapdBean.c2 != 0 && aapdBean.p2 != 0 && aapdBean.r2 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a2 == 0 ? 2 : 1, 3, 4, aapdBean.a2 == 0 ? 2 : 1));
                        } else if (aapdBean.c2 + aapdBean.c1 - mBean.cycle == 0 && aapdBean.c3 != 0 && aapdBean.p3 != 0 && aapdBean.r3 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a3 == 0 ? 2 : 1, 3, 4, aapdBean.a3 == 0 ? 2 : 1));
                        } else if (aapdBean.c3 + aapdBean.c2 + aapdBean.c1 - mBean.cycle == 0 && aapdBean.c4 != 0 && aapdBean.p4 != 0 && aapdBean.r4 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a4 == 0 ? 2 : 1, 3, 4, aapdBean.a4 == 0 ? 2 : 1));
                        } else if (aapdBean.c4 + aapdBean.c3 + aapdBean.c2 + aapdBean.c1 - mBean.cycle == 0 && aapdBean.c5 != 0 && aapdBean.p5 != 0 && aapdBean.r5 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a5 == 0 ? 2 : 1, 3, 4, aapdBean.a5 == 0 ? 2 : 1));
                        } else if (aapdBean.c5 + aapdBean.c4 + aapdBean.c3 + aapdBean.c2 + aapdBean.c1 - mBean.cycle == 0 && aapdBean.c6 != 0 && aapdBean.p6 != 0 && aapdBean.r6 != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(aapdBean.a6 == 0 ? 2 : 1, 3, 4, aapdBean.a6 == 0 ? 2 : 1));
                        }
                        break;
                    case 7:
//                        KidBean kidBean = PdproHelper.getInstance().kidBean();
                        if (currCycle < maxCycle) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(1, 3, 4, 1));
                        } else if (currCycle == maxCycle  && kidBean.isFinalSupply && kidBean.abdomenRetainingVolumeFinally != 0) {
                            sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
                        }
//                        sendToMainBoard(CommandDataHelper.getInstance().special(kidBean.isFinalSupply?1:2, kidBean.isFinalSupply?0:1, kidBean.isFinalSupply?1:0, kidBean.abdomenRetainingVolumeFinally));
                        break;
                    case 8:
//                        ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                        if (expertBean.cycleMyself) {
                            if (maxCycle >= mBean.cycle ) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(expertBean.baseSupplyCycle.contains(mBean.cycle + 1) ? 2 : 1,
                                        3,
                                        4,
                                        expertBean.baseSupplyCycle.contains(mBean.cycle ) ? 2 : 1
                                ));
                            }
                        } else {
                            if (currCycle == maxCycle  && expertBean.isFinalSupply && expertBean.finalRetainVol != 0) {
                                sendToMainBoard(CommandDataHelper.getInstance().special(2, 3, 4, 2));
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

                runOnUiThread(() -> {
                    if (pdEntityDataList.size() != mBean.cycle + 1) {
                        pdEntityData = new PdData.PdEntityData();
                        if (pdEntityDataList == null) {
                            pdEntityDataList = new ArrayList<>();
                        }
                        pdEntityData.setCycle(mBean.cycle);
                        pdEntityDataList.add(pdEntityData);
                    }
                    pdEntityDataList.get(mBean.cycle).setAbdTime(mBean.retainTime);
                    pdEntityDataList.get(mBean.cycle).setAbdVol(mBean.retainVolume);
                    if (pdInfoEntities.size() != mBean.cycle + 1) {
                        pdInfoEntity = new PdEntity.PdInfoEntity();
                        if (pdInfoEntities == null) {
                            pdInfoEntities = new ArrayList<>();
                        }
                        pdInfoEntity.cycle = mBean.cycle;
                        pdInfoEntities.add(pdInfoEntity);
                    }
                    pdInfoEntities.get(mBean.cycle).abdTime = mBean.retainTime / 60;
                    pdInfoEntities.get(mBean.cycle).abdAfterVol = mBean.retainVolume;

                    modifyDuringTreatment(mBean.cycle);
                    assistIv.setVisibility(View.GONE);

                });
                Log.e("留腹过程完成", "currCycle--" + mBean.cycle);
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
                    APIServiceManage.getInstance().postApdCode("T0" + currCycle + "41");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "41");
                }
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"41");
                runOnUiThread(() -> {
                    if (null != mFragment3.getActivity()) {
                        mFragment3.setSupplyTargetValue(mBean.supplyTarget, mBean.supplyProtect,mBean.supply);
                    } else {
                        if (detailedBean == null) {
                            detailedBean = new DetailedBean();
                        }
                        detailedBean.setmSupplyTargetValue(mBean.supplyTarget);
                        detailedBean.setmSupplyTargetProtectionValue(mBean.supplyProtect);
                        detailedBean.setSupplyVol(mBean.supply);
                    }
                    assistIv.setVisibility(View.VISIBLE);
                    assistIv.setImageResource(R.drawable.supply_zl);
                });
            } else if (receiveBean.publish.msg.contains(MSG_SUPPLY_RESTART)) {//7、留腹补液重新开始
                Log.e("处理留腹数据", "7、留腹补液重新开始:" + myGson.toJson(receiveBean.publish));
//            speak("留腹补液重新开始");
            } else if (receiveBean.publish.msg.contains(MSG_SUPPLY_FINISH)) {//8、留腹补液完成
                Log.e("处理留腹数据", "8、留腹补液完成:" + myGson.toJson(receiveBean.publish));
//            speak("留腹补液完成");
                runOnUiThread(() -> {
                    assistIv.setVisibility(View.GONE);
                    ReceiveRetainSupplyFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainSupplyFinishBean.class);
                    curUpperWeight = mBean.upperWeight;
                    if (mFragment3.getActivity() != null) {
                        mFragment3.setSupply(mBean.supply);
                    } else {
                        if (detailedBean == null) {
                            detailedBean = new DetailedBean();
                        }
                        detailedBean.setmSupplyTargetValue(mBean.supplyTarget);
                        detailedBean.setmSupplyTargetProtectionValue(mBean.supplyProtect);
                        detailedBean.setSupplyVol(mBean.supply);
                    }
                    if (MyApplication.apdMode == 8) {
                        conTv.setText(conStr + mixCon(curCon, mBean.upperWeight - mBean.supply, isEndSupply ? expertBean.con_2 : expertBean.con_1, mBean.supply) + "%");
                        curCon = mixCon(curCon, mBean.upperWeight - mBean.supply, isEndSupply ? expertBean.con_2 : expertBean.con_1, mBean.supply);
                    }
                });
            } else if (receiveBean.publish.msg.contains("supply run")) {
                runOnUiThread(() -> {
                    ReceiveRetainSupplyFinishBean finishBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainSupplyFinishBean.class);
                    if (mFragment3.getActivity() != null) {
                        mFragment3.setSupply(finishBean.supply);
                    } else {
                        if (detailedBean == null) {
                            detailedBean = new DetailedBean();
                        }
                        detailedBean.setmSupplyTargetValue(finishBean.supplyTarget);
                        detailedBean.setmSupplyTargetProtectionValue(finishBean.supplyProtect);
                        detailedBean.setSupplyVol(finishBean.supply);
                    }
                    if (MyApplication.apdMode == 8) {
                        conTv.setText(conStr + mixCon(curCon, finishBean.upperWeight - finishBean.supply, isEndSupply ? expertBean.con_2 : expertBean.con_1, finishBean.supply) + "%");
                    }
                });
            } else if (receiveBean.publish.msg.equals(MSG_SUPPLY_FAILURE)) {//9、留腹补液失败
                Log.e("处理留腹数据", "9、留腹补液失败:" + myGson.toJson(receiveBean.publish));
//            speak("留腹补液失败");
                runOnUiThread(this::supplyFaultDialog);
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"43");
            } else if (receiveBean.publish.msg.equals(MSG_RETAIN_TIME_OUT)) {//10、留腹时间结束
                Log.e("处理留腹数据", "10、留腹时间结束:" + myGson.toJson(receiveBean.publish));
                //{"data":{"cycle":1.0,"expectRetainTime":180.0,"retainTime":180.0},"msg":"retain time out","topic":"treatment/retain"}
                currRetainTime = 0;
//            speak("留腹时间结束");
                stopLoopCountDown();
                runOnUiThread(() -> {
                    assistIv.setVisibility(View.GONE);
                });

                ReceiveRetainFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveRetainFinishBean.class);
//            ReceiveCycleBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveCycleBean.class);
            } else if (receiveBean.publish.msg.contains(MSG_RETAIN_TIME_OUT_SELFCHECK_FAILURE_UNHANDLE)) {//11.1、留腹时间结束但故障未处理:自检失败未处理
                Log.e("留腹时间结束但故障未处理", "1、自检失败未处理:" + myGson.toJson(receiveBean.publish));

            } else if (receiveBean.publish.msg.contains(MSG_RETAIN_TIME_OUT_SUPPLY_FAILURE_UNHANDLE)) {//11.2、留腹时间结束但故障未处理:补液失败未处理
                Log.e("留腹时间结束但故障未处理", "2、补液失败未处理:" + myGson.toJson(receiveBean.publish));
//            APIServiceManage.getInstance().postApdCode("T"+currCycle+"49");
                if (currCycle < 10) {
                    APIServiceManage.getInstance().postApdCode("T0" + currCycle + "49");
                } else {
                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "49");
                }
                runOnUiThread(this::supplyFaultDialog);
            } else if (receiveBean.publish.msg.contains(MSG_RETAIN_TIME_OUT_OTHER_FAILURE_UNHANDLE)) {//11.3、留腹时间结束但故障未处理:其它类型失败未处理
                Log.e("留腹时间结束但故障未处理", "3、其它类型失败未处理:" + myGson.toJson(receiveBean.publish));
                runOnUiThread(()-> {
                    otherFailure("留腹时间到补液超时");
                });

            } else if (receiveBean.publish.msg.equals("retain time out unhandle")) {//11.3、留腹时间结束但故障未处理:其它类型失败未处理
                Log.e("留腹时间结束但故障未处理", "暂停导致留腹超时:" + myGson.toJson(receiveBean.publish));
                runOnUiThread(()-> {
                    pauseFailure("请恢复治疗");
                });

            } else if (receiveBean.publish.msg.equals("retain time out drop fault unhandle")) {//11.3、留腹时间结束但故障未处理:其它类型失败未处理
                Log.e("留腹时间结束但故障未处理", "超时后检测到的未处理的液体泄漏:" + myGson.toJson(receiveBean.publish));
                runOnUiThread(()-> {
                    otherFailure("液体泄漏未处理");
                });

            } else if (receiveBean.publish.msg.equals("retain time out supply run unhandle")) {//11.3、留腹时间结束但故障未处理:其它类型失败未处理
                Log.e("留腹时间结束但故障未处理", "暂停键导致补液超时:" + myGson.toJson(receiveBean.publish));
                runOnUiThread(()-> {
                    pauseFailure("暂停导致补液超时");
                });

            } else if (receiveBean.publish.msg.equals("retain drop fault")) { // 引流阀门故障
                runOnUiThread(() -> {
//                    if (!isShowAlarmDialog) {
                        drainValveFaultDialog("可能出现漏液,请检查引流阀");
//                    }
                });
            } else if (receiveBean.publish.msg.equals("retain drop ok")) {
                runOnUiThread(() -> {
                    if (treatmentAlarmDialog == null) {
                        treatmentAlarmDialog = new TreatmentAlarmDialog(this);
                    }
                    if (treatmentAlarmDialog.isShowing()) {
                        stopSound();
                        treatmentAlarmDialog.dismiss();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("刘富数据", e.getLocalizedMessage());
        }
    }

    private int finalPerTime; // 最末灌注时间

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
                            mFragment3.setDrainTargetVolume(currCycle, bean.drainTarget , bean.drainPass);
                        } else {
                            if (detailedBean == null) {
                                detailedBean = new DetailedBean();
                            }
                            detailedBean.setDrainTarget(bean.drainTarget);
                            detailedBean.setCurrCycle(bean.cycle);
                        }
//                        if (bean.cycle == 0) {
//                            pdEntityData = new PdData.PdEntityData();
//                            pdEntityData.setCycle(0);
//                            if (pdEntityDataList == null) {
//                                pdEntityDataList = new ArrayList<>();
//                            }
//                            pdEntityDataList.add(pdEntityData);
//                        }
                        pauseLayout.setVisibility(View.INVISIBLE);
                        skipLayout.setVisibility(View.INVISIBLE);
                        stageTimer.start();
                    });
                }
                break;
            case "drain disturb":
                runOnUiThread(()-> {
                    faultDialog("请不要干扰下位称（引流异常）","");
                });
                break;
            case MSG_PROCESS_FINISH: {
                Log.e("处理引流数据", "2、引流过程完成: " + myGson.toJson(receiveBean.publish));
                stopLoopTimer();
//                speak("完成引流");
// {"data":{"cycle":1.0,"workTime":4981.0,"drainTarget":496.0,"drain":3.0,"drainRetain":493.0,"rinseCount":0.0,"drainTotalRinse":0.0,"vaccumCount":0.0},"msg":"process finish","topic":"treatment/drain"}
                ReceiveDrainProcessFinishBean mBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainProcessFinishBean.class);
                if (mBean != null) {
                    currCycle = mBean.cycle;
                    Log.e("处理引流数据","currCycle--"+currCycle);
                    runOnUiThread(()->{
                        if (pdEntityDataList.size() != mBean.cycle + 1) {
                            pdEntityData = new PdData.PdEntityData();
                            if (pdEntityDataList == null) {
                                pdEntityDataList = new ArrayList<>();
                            }
                            pdEntityData.setCycle(mBean.cycle);
                            pdEntityDataList.add(pdEntityData);
                        }
                        pdEntityDataList.get(mBean.cycle).setDrainage(mBean.drain);
                        pdEntityDataList.get(mBean.cycle).setDrainTargetVol(mBean.drainTarget);
                        pdEntityDataList.get(mBean.cycle).setDrainTime(mBean.workTime);

                        int time = pdEntityDataList.get(currCycle).getDrainTime() ;
                        if (time !=  0) {
                            roteTv.setText(String.valueOf(Math.max(pdEntityDataList.get(currCycle).getDrainage(),0) / time * 60));
                        }
                        if (pdInfoEntities.size() != mBean.cycle + 1) {
                            pdInfoEntity = new PdEntity.PdInfoEntity();
                            if (pdInfoEntities == null) {
                                pdInfoEntities = new ArrayList<>();
                            }
                            pdInfoEntity.cycle = mBean.cycle;
                            pdInfoEntities.add(pdInfoEntity);
                        }
                        pdInfoEntities.get(mBean.cycle).drainage =pdEntityDataList.get(mBean.cycle).getDrainage();
                        pdInfoEntities.get(mBean.cycle).drainTime = pdEntityDataList.get(mBean.cycle).getDrainTime() / 60;


                        if (mFragment3.getActivity() != null) {
                            mFragment3.setCurrDrainVolume(mBean.drain);
                        }
                        currentVolTv.setText(String.valueOf(mBean.drain));
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
//                speak("正在引流自检中，请稍后...");
                break;
            case MSG_SELFCHECK_FINISH:
                Log.e("处理引流数据", "4、引流自检完成: " + myGson.toJson(receiveBean.publish));
                //{"data":{"cycle":0.0,"upperInc":0.0,"lowerInc":0.0},"msg":"selfcheck finish","topic":"treatment/drain"}
//            dismissLoading();
                break;
            case MSG_SELFCHECK_FAILURE:
                Log.e("处理引流数据", "5、引流自检失败: " + myGson.toJson(receiveBean.publish));
//            dismissLoading();
//                speak("引流自检失败");
                runOnUiThread(()->{
                    if (!isShowAlarmDialog) {
                        drainSelfCheckFaultDialog();
                    }
                });
                break;
            case MSG_DRAIN_START: //引流返回: 开始引流(非TPD模式或者TPD模式)
                    //{"data":{"cycle":0.0,"drainMode":"NTPD","retain":0.0,"drainTarget":0.0,"drainPass":0.0},"msg":"drain start","topic":"treatment/drain"}
                    ReceiveDrainStartBean drainStartBean = JsonHelper.jsonToClass(myGson.toJson(receiveBean.publish.data), ReceiveDrainStartBean.class);
                    Log.e("处理引流数据", "6、引流开始: " + myGson.toJson(receiveBean.publish));
//                    speak("开始引流");
                    runOnUiThread(()-> {
                        if (drainStartBean.cycle == 0) {
                            if (null != mFragment3.getActivity()) {
                                mFragment3.setDrainTargetVolume(currCycle, drainStartBean.drainTarget , drainStartBean.drainPass);
                            } else {
                                if (detailedBean == null) {
                                    detailedBean = new DetailedBean();
                                }
                                detailedBean.setDrainTarget(drainStartBean.drainTarget);
                                detailedBean.setCurrCycle(drainStartBean.cycle);
                            }
                            pauseLayout.setVisibility(View.INVISIBLE);
                            skipLayout.setVisibility(View.INVISIBLE);
                            stageTimer.start();
                            pdEntityData = new PdData.PdEntityData();
                            pdEntityData.setCycle(0);
                            if (pdEntityDataList == null) {
                                pdEntityDataList = new ArrayList<>();
                            }
                            pdEntityDataList.add(pdEntityData);

                            pdInfoEntity = new PdEntity.PdInfoEntity();
                            pdInfoEntity.cycle = 0;
                            if (pdInfoEntities == null) {
                                pdInfoEntities = new ArrayList<>();
                            }
                            pdInfoEntities.add(pdInfoEntity);
                        }
                        if (!isTiming) {
                            currDrainTime = 0;
                            startLoopTimer();
                        }
//                        if (drainStartBean.cycle == 0) {
//                            pauseLayout.setVisibility(View.INVISIBLE);
//                            skipLayout.setVisibility(View.INVISIBLE);
//                            stageTimer.start();
//                            pdEntityData = new PdData.PdEntityData();
//                            pdEntityData.setCycle(0);
//                            if (pdEntityDataList == null) {
//                                pdEntityDataList = new ArrayList<>();
//                            }
//                            pdEntityDataList.add(pdEntityData);
//
//                            pdInfoEntity = new PdEntity.PdInfoEntity();
//                            pdInfoEntity.cycle = 0;
//                            if (pdInfoEntities == null) {
//                                pdInfoEntities = new ArrayList<>();
//                            }
//                            pdInfoEntities.add(pdInfoEntity);
//                        }
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

                break;
            case MSG_DRAIN_RESTART: {//引流返回: 引流重新开始
                //stopLoopTimer();
//                speak("引流重新开始");
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
                String mCurrentVolume = String.valueOf(Math.max(mBean.drain,0));
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
                    currentVolTv.setText(mCurrentVolume);
//                    drainage =mBean.drain;
//                    if (pdEntityData == null) {
//                        pdEntityData = new PdData.PdEntityData();
//                    }
                    if (pdEntityDataList != null) {
                        if (pdEntityDataList.size() == mBean.cycle + 1) {
                            pdEntityDataList.get(mBean.cycle).setEstimate(mBean.retain);
                            pdEntityDataList.get(mBean.cycle).setDrainage(mBean.drain);
                        }
                        if (pdInfoEntities.size() == mBean.cycle + 1) {
                            pdInfoEntities.get(mBean.cycle).drainage = pdEntityDataList.get(mBean.cycle).getDrainage();
                            pdInfoEntities.get(mBean.cycle).remain = pdEntityDataList.get(mBean.cycle).getEstimate();
                        }
//                    pdInfoEntity = pdInfoEntities.get(currCycle);
                    }
                    if (null != mFragment3.getActivity()) {
                        mFragment3.setCurrDrainVolume(mBean.drain);
                        mFragment3.setWaittingVolume(mBean.retain);
                    } else {
                        if (detailedBean == null) {
                            detailedBean = new DetailedBean();
                        }
                        detailedBean.setCurrDrainVol(mBean.drain);
                        detailedBean.setRetain(mBean.retain);
                    }
                });
                break;
            }
            case "up drop fault":
                runOnUiThread(() -> {
                    if (!isShowAlarmDialog) {
                        drainValveFaultDialog("可能出现漏液,请检查灌注阀");
                    }
                });
                break;
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
//                if (currCycle < 10) {
//                    APIServiceManage.getInstance().postApdCode("T0"+currCycle+"09");
//                } else {
//                    APIServiceManage.getInstance().postApdCode("T" + currCycle + "09");
//                }
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
                runOnUiThread(()-> {
                    if (null != mFragment3.getActivity()) {
                        mFragment3.setRinseNum(mBean.rinseTimes);
                    } else {
                        if (detailedBean == null) {
                            detailedBean = new DetailedBean();
                        }
                        detailedBean.setCurrRinseNum(mBean.rinseTimes);
                    }
                });
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
                            mFragment3.setRinseData(mBean.rinse);//冲洗量和次数，未给次数
                        } else {
                            if (detailedBean == null) {
                                detailedBean = new DetailedBean();
                            }
                            detailedBean.setCurrRinsePerfusionVolume(mBean.rinse);
                            detailedBean.setCurrRinseNum(0);
                        }
                        if (pdEntityDataList.size()  == mBean.cycle + 1 ) {
                            pdEntityDataList.get(mBean.cycle).setUfVol(mBean.rinse);
                        }
                        if (pdInfoEntities.size() == mBean.cycle + 1) {
                            pdInfoEntities.get(mBean.cycle).auFvVol = mBean.rinse;
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
                    if (null != pdEntityDataList) {
                        if (pdEntityDataList.size() == mBean.cycle + 1) {
                            pdEntityDataList.get(mBean.cycle).setUfVol(mBean.rinse);
                            pdEntityDataList.get(mBean.cycle).setDrainTargetVol(mBean.drainTarget);
                        }
                        if (pdInfoEntities.size() == mBean.cycle + 1) {
                            pdInfoEntities.get(mBean.cycle).auFvVol = mBean.rinse;
                            pdInfoEntities.get(mBean.cycle).drainTvVol = mBean.drainTarget;
                        }
                    }

                    runOnUiThread(() -> {
                        if (null != mFragment3.getActivity()) {
                            mFragment3.setDrainTargetVolume(currCycle, mBean.drainTarget , mBean.drainPass);
                        }

                    });
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
//                speak("请改变体位或检查引流管路是否打折和压迫");
                runOnUiThread(()->{
                    if (currCycle < 10) {
                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"51");
                    } else {
                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "51");
                    }
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"51");
                    isVacDrain = true;
                    assistIv.setVisibility(View.VISIBLE);
                    assistIv.setImageResource(R.drawable.neo_zl);
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
                    assistIv.setVisibility(View.GONE);
                    currentVolTv.setText(String.valueOf(mBean.drain));
//                    currentVolTvLabel.setText(String.valueOf(drainage));
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
//                speak("请改变体位");
                break;
                default:
                Log.e("处理引流数据", "other、引流:" + myGson.toJson(receiveBean.publish));
                break;
        }

    }

    private boolean isVacDrain;


//    /**
//     * 处理实时留腹量
//     *
//     * @param mCurrentVolume
//     */
//    @Subscribe(code = RxBusCodeConfig.EVENT_TREATMENT_WAITING_CURRENT_VOLUME)
//    public void receiveCurrenWaitingVolume(String mCurrentVolume) {
//        Log.e("处理实时留腹量", "实时留腹量: " + mCurrentVolume);
//        runOnUiThread(() -> {
//            if (pdEntityDataList != null) {
//                if (pdEntityDataList.size() == currCycle + 1) {
//                    pdEntityDataList.get(currCycle).setAbdVol(Integer.parseInt(mCurrentVolume));
//                }
//
//                if (null != fragmentItem2.getActivity()) {
//                    fragmentItem2.setHistoricalData(pdEntityDataList);
//                }
//                if (null != mFragment3.getActivity()) {
//                    mFragment3.setWaittingVolume(Integer.parseInt(mCurrentVolume));
//                } else {
//                    if (detailedBean == null) {
//                        detailedBean = new DetailedBean();
//                    }
//                    detailedBean.setRetain(Integer.parseInt(mCurrentVolume));
//                }
//            }
//        });
//    }

    private void erasure() {
        if (!MyApplication.isBuzzerOff) {
            stopSound();
//                            if (isBuzzerOff) {
            currCountdown = 0;
            silencersLayout.setVisibility(View.VISIBLE);
            disposable = Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .take(maxCountdown - currCountdown)
                    .subscribe(aLong -> {
                        currCountdown++;
                        Log.e("currCountdown","currCountdown："+currCountdown+",maxCountdown:"+maxCountdown);
                        MyApplication.isBuzzerOff = true;
                        tvSilencers.setText("消音中("+(maxCountdown - currCountdown)+"s)");
                    }, throwable -> {
                        Log.e("currCountdown","throwable："+throwable.getLocalizedMessage());
                    }, () -> {
                        //complete
                        runOnUiThread(()->{
                            MyApplication.isBuzzerOff = false;
                            currCountdown = 0;
                            if (isShowAlarmDialog) {
                                silencer();
                            }
                            Log.e("currCountdown","complete");
                            silencersLayout.setVisibility(View.INVISIBLE);
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
        silencersLayout.setVisibility(View.INVISIBLE);
        if (soundCompositeDisposable != null) {
            if (disposable != null) {
                soundCompositeDisposable.remove(disposable);
            }
        }
        buzzerOn();
    }

    private void silencer() {
        currCountdown = 0;
        MyApplication.isBuzzerOff = false;
        silencersLayout.setVisibility(View.INVISIBLE);
        if (soundCompositeDisposable != null) {
            if (disposable != null) {
                soundCompositeDisposable.remove(disposable);
            }
        }
        loopSoundPool();
    }

    private boolean isSleep = true; // 是否休眠
    /**
     * 引流自检失败
     */
    private void drainSelfCheckFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        silencer();
        isShowAlarmDialog = true;
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_drain_selfcheck))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_drain), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment_selfcheck), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    stopSound();
                    //跳过引流
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_drain_selfcheck));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd("treatment/failure_continue"));
                    //继续治疗
                    isShowAlarmDialog = false;
//                    sendCommandInterval(CommandDataHelper.getInstance().continueTreatmentCmd(), 200);
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    // mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    stopSound();
                    finishTreatmentDialog(3);
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 灌注自检失败
     */
    private void perfusionSelfCheckFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        silencer();
        isShowAlarmDialog = true;
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_perfusion_selfcheck))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_perfusion), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment_selfcheck), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    isShowAlarmDialog = false;
                    stopSound();
                    sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP), 200);
                    mCommonDialog13.dismiss();
                })
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_perfusion_selfcheck));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    //继续治疗
                    isShowAlarmDialog = false;
//                    sendCommandInterval(CommandDataHelper.getInstance().continueTreatmentCmd(),200);
//                    sendCommandInterval(CommandDataHelper.getInstance().continueTreatmentCmd(), 200);
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    stopSound();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(1);
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 引流灌注超时报警
     */
    private void timeoutDialog(String msg) {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        silencer();
//        if (currCycle < 10) {
//            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"29");
//        } else {
//            APIServiceManage.getInstance().postApdCode("T" + currCycle + "29");
//        }
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"29");
        speak(msg);
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(msg)
                .setBtnFirst("长按跳过", R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
                    isShowAlarmDialog = false;
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"26");
                    mCommonDialog13.dismiss();
                })
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(msg);
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
                    stopSound();
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    isShowAlarmDialog = true;
                    stopSound();
                    finishTreatmentDialog(1);

                })
                .show();

    }

    private void faultDialog(String faultMsg, String faultCode) {
        if (isSleep) {
            simulateKey(82);
        }
        silencer();
//        if (currCycle < 10) {
//            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"29");
//        } else {
//            APIServiceManage.getInstance().postApdCode("T" + currCycle + "29");
//        }

        speak(faultMsg);
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(faultMsg)
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_perfusion), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(faultMsg);
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
//                    if (currCycle < 10) {
//                        APIServiceManage.getInstance().postApdCode("T0"+currCycle+"26");
//                    } else {
//                        APIServiceManage.getInstance().postApdCode("T" + currCycle + "26");
//                    }
//                    isShowAlarmDialog = false;
//                    APIServiceManage.getInstance().postApdCode("T"+currCycle+"26");
                    mCommonDialog13.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    stopSound();
                    finishTreatmentDialog(1);
//                    stopSound();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 灌注不畅故障
     */
    private void perfusionFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        silencer();
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"29");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "29");
        }
        isShowAlarmDialog = true;
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"29");
        speak("灌注不畅");

//        if (msgAdapter != null) {
//            recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
//        }
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_perfusion))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_perfusion), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_perfusion));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过灌注
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
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
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    mCommonDialog1.dismiss();
                    stopSound();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(1);
//                    stopSound();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));

    }

    // 阀门故障
    private void drainValveFaultDialog(String msg) {

        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        silencer();
        speak(msg);
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog.setImageResId(R.drawable.icon_8_tis)
                .setMessage(msg)
                .setBtnFirst(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_blue)
                .setBtnThree(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_skip_perfusion), R.drawable.dialog_btn_yellow)
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(msg);
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog1 -> {
//                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
                    stopSound();
                    refreshPauseOrResume();
                    isShowAlarmDialog = false;
                    mCommonDialog1.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_ABORT));
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })

                .setTwoLongClickListener(mCommonDialog13 -> {
                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                });
        if (!TreatmentFragmentActivity.this.isFinishing()) {
            treatmentAlarmDialog.show();
        }
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 引流小冲失败故障
     */
    private void drainFlushFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        initBeepSoundSus(R.raw.medium_alarm);
        String msg = "引流小冲不畅,可检查人体和引流管路并尝试改变体位后继续治疗";
        isShowAlarmDialog = true;
        speak("引流小冲不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_drain));
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(msg)
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_drain), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过引流
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
//                    stopSound();
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(msg);
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //继续治疗
//                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
//                    stopSound();
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    // mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(3);
//                    stopSound();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 引流不畅故障
     */
    private void drainFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        silencer();
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"19");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "19");
        }
        isShowAlarmDialog = true;
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"19");
        speak("引流不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_drain));
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_drain))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_skip_drain), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_continue_treatment), R.drawable.dialog_btn_green)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_drain));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog13 -> {
                    //跳过引流
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
                    stopSound();
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
                    stopSound();
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    //终止治疗
                    // mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    finishTreatmentDialog(3);
                    stopSound();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 灌注补液不畅
     */
    private void supplyFaultDialog2() {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        initBeepSoundSus(R.raw.medium_alarm);
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"49");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "49");
        }
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"49");
        speak("补液不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_supply));
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_supply))
                .setBtnFirst("长按跳过", R.drawable.dialog_btn_blue)
                .setBtnTwo("终止治疗", R.drawable.dialog_btn_red)
                .setBtnThree("继续补液", R.drawable.dialog_btn_green)
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_supply));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(dialog -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
//                    stopSound();
                    //强制跳过窗口
                    dialog.dismiss();
                })
                .setTwoClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    stopSound();
                    mCommonDialog1.dismiss();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 补液不畅故障
     */
    private void supplyFaultDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        initBeepSoundSus(R.raw.medium_alarm);
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"49");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "49");
        }
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"49");
        speak("补液不畅");
//        speak(getResources().getString(R.string.dialog_status_fault_supply));
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(getResources().getString(R.string.dialog_status_fault_supply))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_complete_not_supply), R.drawable.dialog_btn_blue)
                .setBtnTwo("终止治疗", R.drawable.dialog_btn_red)
                .setBtnThree("继续补液", R.drawable.dialog_btn_green)
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_supply));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(dialog -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
//                    stopSound();
//                    sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP),200);
                    dialog.dismiss();
                })
                .setTwoClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
//                    stopSound();
                    mCommonDialog1.dismiss();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    private void pauseFailure(String msg) {
        if (isSleep) {
            simulateKey(82);
        }
//        silencer();
        initBeepSoundSus(R.raw.medium_alarm);
        speak(msg);
//        speak(getResources().getString(R.string.dialog_status_fault_supply));
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(msg)
                .setBtnFirst(getResources().getString(R.string.dialog_btn_complete_not_supply), R.drawable.dialog_btn_blue)
                .setBtnTwo("终止治疗", R.drawable.dialog_btn_red)
                .setBtnThree("恢复治疗", R.drawable.dialog_btn_green)
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(msg);
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(dialog -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
//                    stopSound();
//                    sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP),200);
                    dialog.dismiss();
                })
                .setTwoClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog1 -> {
//                    isShowAlarmDialog = false;
                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
                    mCommonDialog1.dismiss();
//                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    private void otherFailure(String msg) {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        initBeepSoundSus(R.raw.medium_alarm);
        if (currCycle < 10) {
            APIServiceManage.getInstance().postApdCode("T0"+currCycle+"49");
        } else {
            APIServiceManage.getInstance().postApdCode("T" + currCycle + "49");
        }
//        APIServiceManage.getInstance().postApdCode("T"+currCycle+"49");
        speak(msg);
//        speak(getResources().getString(R.string.dialog_status_fault_supply));
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_tis)
                .setMessage(msg)
                .setBtnFirst(getResources().getString(R.string.dialog_btn_complete_not_supply), R.drawable.dialog_btn_blue)
                .setBtnTwo("终止治疗", R.drawable.dialog_btn_red)
                .setBtnThree("继续治疗", R.drawable.dialog_btn_green)
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(msg);
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(dialog -> {
                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP));
//                    stopSound();
//                    sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_TREATMENT_SKIP),200);
                    dialog.dismiss();
                })
                .setTwoClickListener(mCommonDialog1 -> {
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog1 -> {
//                    isShowAlarmDialog = false;
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
//                    stopSound();
                    mCommonDialog1.dismiss();
//                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 最末引流提醒
     */
    private void lastDrainDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        initBeepSoundSus(R.raw.medium_alarm);
        treatmentAlarmDialog
                .setTitle(getResources().getString(R.string.dialog_title))
                .setMessage(getResources().getString(R.string.dialog_status_fault_drain_last))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_complete_last_drain), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_view_data), R.drawable.dialog_btn_green)
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_drain_last));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog1 -> {
//                    sendCommandInterval(CommandSendConfig.TREATMENT_DRAIN_EMPTY_WAITING_END, 200);
                    stopLoopLastDrainCountDown();
//                    isShowAlarmDialog = false;
//                    //完成最末引流
//                    mCommonDialog1.dismiss();
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .setTwoLongClickListener(mCommonDialog12 -> {
//                    stopSound();
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
//                    stopSound();
                    //查看数据
                    switchViewData();
                    startLoopLastDrainCountDown();
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * 灌注警戒值警报
     */
    private void perfusionMaxValueDialog() {
        if (isSleep) {
            simulateKey(82);
        }
//        silencer();
        isShowAlarmDialog = true;
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }

        initBeepSoundSus(R.raw.medium_alarm);
//        disposeDownTimer.start();
        treatmentAlarmDialog
                .setTitle(getResources().getString(R.string.dialog_title))
                .setMessage(getResources().getString(R.string.dialog_status_fault_perfusion_max))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_force_skip_window), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_view_data), R.drawable.dialog_btn_green)
//                .setCountdown(true, 31)//倒计时秒数
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_perfusion_max));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog1 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));

//                    stopSound();
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
//                .silencersClickListener(mCommonDialog14 -> {
//                    erasure();
//                })
                .setTwoLongClickListener(mCommonDialog12 -> {
                    //暂停治疗
                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
//                    stopSound();
                    mCommonDialog12.dismiss();
                    isShowAlarmDialog = false;
                })
                .setThreeClickListener(mCommonDialog13 -> {
                    isShowAlarmDialog = false;
//                    stopSound();
                    //查看数据
                    switchViewData();
                    mCommonDialog13.dismiss();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

//    private CountDownTimer disposeDownTimer = new CountDownTimer(10 * 1000, 1000) {
//        @Override
//        public void onTick(long l) {
//
//        }
//
//        @Override
//        public void onFinish() {
//            if (treatmentAlarmDialog != null && treatmentAlarmDialog.isShowing()) {
//                treatmentAlarmDialog.dismiss();
//            }
//            MsgBean msgBean = new MsgBean();
//            msgBean.setMsg(disposeMsg);
//            if (msgBeanList == null) {
//                msgBeanList = new ArrayList<>();
//            }
//            msgBeanList.add(msgBean);
//
//            if (msgAdapter != null) {
//                recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
//            }
//        }
//    };

    /**
     * 低温预警
     */
    private void showLowTemperatureAlertDialog() {
        if (isSleep) {
            simulateKey(82);
        }
//        silencer();
        initBeepSoundSus(R.raw.medium_alarm);
        isShowAlarmDialog = true;
        speak(getResources().getString(R.string.dialog_status_fault_temperature_low));
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_jinbao)
                .setMessage(getResources().getString(R.string.dialog_status_fault_temperature_low))
//                .setBtnViewDataTxt(getResources().getString(R.string.dialog_btn_view_data))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_force_skip_window), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_temperature_low));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog12 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));

//                    stopSound();
                    //强制跳过窗口
                    isShowAlarmDialog = false;
//                    CommandDataHelper.getInstance().continueTreatmentCmd();
                    mCommonDialog12.dismiss();
                })
                .silencersClickListener(mCommonDialog -> erasure())
                .setTwoLongClickListener(mCommonDialog13 -> {
                    //暂停治疗
                    refreshPauseOrResume();//刷新按钮状态和发送"暂停治疗"或者"恢复治疗"指令
//                    stopSound();
                    isShowAlarmDialog = false;
                    mCommonDialog13.dismiss();
                })
                .setThreeClickListener(mCommonDialog14 -> {
                    //终止治疗
                    //  mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));

    }

    /**
     * 高温预警
     */
    private void showHeatAlertDialog() {
        if (isSleep) {
            simulateKey(82);
        }
        isShowAlarmDialog = true;
        silencer();
        speak(getResources().getString(R.string.dialog_status_fault_temperature_high));
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_jinbao)
                .setMessage(getResources().getString(R.string.dialog_status_fault_temperature_high))
//                .setBtnViewDataTxt(getResources().getString(R.string.dialog_btn_view_data))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_force_skip_window), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_yellow)
                .setBtnThree(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setViewDataClickListener(mCommonDialog1 -> {
                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_temperature_high));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setFirstLongClickListener(mCommonDialog12 -> {
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));

                    stopSound();
                    //强制跳过窗口
                    isShowAlarmDialog = false;
                    mCommonDialog12.dismiss();
                })
                .setTwoLongClickListener(mCommonDialog13 -> {
                    stopSound();
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
                    stopSound();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
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
//        silencer();
        initBeepSoundSus(R.raw.low_alarm);
        speak(getResources().getString(R.string.dialog_status_fault_ac_power_off));
        if (currentStage == 1) {
            refreshPauseOrResume();
        }
        MsgBean msgBean = new MsgBean();
        msgBean.setMsg("现在处于暂停状态!!!");
        if (msgBeanList == null) {
            msgBeanList = new ArrayList<>();
        }
        msgBeanList.add(msgBean);
        if (msgAdapter != null) {
            recyclerview.scrollToPosition(msgAdapter.getItemCount() - 1);
        }
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_jinbao)
                .setMessage(getResources().getString(R.string.dialog_status_fault_ac_power_off))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_suspend_treatment), R.drawable.dialog_btn_blue)
                .setBtnTwo(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setBtnThree("继续治疗",R.drawable.dialog_btn_yellow)
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_ac_power_off));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstLongClickListener(mCommonDialog12 -> {
                    refreshPauseOrResume();
                    isShowAlarmDialog = false;
//                    stopSound();
                    //强制跳过窗口
                    mCommonDialog12.dismiss();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setTwoClickListener(mCommonDialog12 -> {
                    //终止治疗
                    //  mCommonDialog.dismiss();
//                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .setThreeClickListener(mCommonDialog1 -> {
//                    stopSound();
                    sendToMainBoard(CommandDataHelper.getInstance().continueTreatmentCmd());
                    APIServiceManage.getInstance().postApdCode("E0011");
                    mCommonDialog1.dismiss();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
    }

    /**
     * AC掉电超过30分钟报警
     */
    private void showPowerFaultDialog() {
        isShowAlarmDialog = true;
//        if (isSleep) {
//            simulateKey(82);
//        }
//        silencer();
        initBeepSoundSus(R.raw.medium_alarm);
        speak("掉电超过30分钟");
        if (treatmentAlarmDialog == null) {
            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
        }
        if (treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        treatmentAlarmDialog
                .setImageResId(R.drawable.icon_8_jinbao)
                .setMessage(getResources().getString(R.string.dialog_status_fault_ac_power_fault))
                .setBtnFirst(getResources().getString(R.string.dialog_btn_stop_treatment), R.drawable.dialog_btn_red)
                .setBtnTwo("", R.drawable.white_shape)
                .setBtnThree("继续治疗",R.drawable.dialog_btn_yellow)
                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
                    //查看数据
//                    switchViewData();
                    speak(getResources().getString(R.string.dialog_status_fault_ac_power_fault));
//                    isShowAlarmDialog = false;
//                    mCommonDialog1.dismiss();
                })
                .setFirstClickListener(mCommonDialog12 -> {
                    //终止治疗
                    //  mCommonDialog.dismiss();
                    isShowAlarmDialog = true;
                    earlyFinishTreatmentDialog();
//                    stopSound();
                })
                .silencersClickListener(mCommonDialog14 -> {
                    erasure();
                })
                .setThreeClickListener(mCommonDialog -> {
                    isShowAlarmDialog = false;
//                    stopSound();
                    mCommonDialog.dismiss();
                })
                .show();
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.SKIP_STATUS));
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
//        stopSound();
//        initDb();
        try {
            initBeepSoundSus(R.raw.low_alarm);
            isComplete = true;
//        MyApplication.mEndTime = EmtTimeUil.getTime();
            if (pdData == null) {
                pdData = MyApplication.pdData;
            }

            if (pdEntityDataList == null) {
                pdEntityDataList = new ArrayList<>();
            }
            for (int i = 0; i < pdEntityDataList.size(); i++) {
                totalDrainVolume += pdEntityDataList.get(i).getDrainage();
                totalPerfusionVolume += pdEntityDataList.get(i).getPreVol();
                totalAbdTime += pdEntityDataList.get(i).getAbdTime();
                if (MyApplication.apdMode == 1) {
                    if (ipdBean.abdomenRetainingVolumeFinally == 0) {
                        mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume - pdEntityDataList.get(0).getDrainage();
                    } else {
                        mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume - pdEntityDataList.get(0).getDrainage()
                                + pdEntityDataList.get(currCycle).getPreVol();
                    }
                }
            }
            endTime = EmtTimeUil.getTime();
            pdData.setFinalTime(finalPerTime);
            pdData.setDurationTime(EmtTimeUil.getTime(time));
            pdData.setTotalDarinVol(totalDrainVolume);
            pdData.setTotalPerVol(totalPerfusionVolume);
            pdData.setUltVol(mUltrafiltrationVolume);
            pdData.setEndTime(endTime);
            pdData.setTotalAbdTime(totalAbdTime);
            pdData.setStartTime(startTime);
            Log.e("showCompleteDialog", "总引流:" + totalDrainVolume + "总关注:" + totalPerfusionVolume + "超滤:" + mUltrafiltrationVolume + "time:" + time);
            pdData.setPdEntityDataList(pdEntityDataList);
//        postData();
//        if (PdproHelper.getInstance().getUserParameterBean().awaken > 0) {
//            startCountDown(PdproHelper.getInstance().getUserParameterBean().awaken);
//        }
            if (currCycle < 10) {
                APIServiceManage.getInstance().postApdCode("T0" + currCycle + "08");
            } else {
                APIServiceManage.getInstance().postApdCode("T" + currCycle + "08");
            }
//        speak("治疗结束,请取下人体端,关闭所有管夹");
//        if (treatmentAlarmDialog == null) {
//            treatmentAlarmDialog = new TreatmentAlarmDialog(this);
//        }
//        if (treatmentAlarmDialog.isShowing()) {
//            treatmentAlarmDialog.dismiss();
//        }
//        treatmentAlarmDialog
//                .setImageResId(R.drawable.icon_8_dagou)
//                .setStatus(getResources().getString(R.string.dialog_status_complete))
//                .setMessage(getResources().getString(R.string.dialog_status_complete_tip), R.color.dialog_text_default)
//                .setBtnFirst(getResources().getString(R.string.dialog_btn_view_data), R.drawable.dialog_btn_blue)
//                .setBtnThree("确认", R.drawable.dialog_btn_green)
//                .setViewDataClickListener(mCommonDialog1 -> {
//                    stopSound();
//                    //查看数据
////                    switchViewData();
//                    speak(getResources().getString(R.string.dialog_status_complete_tip));
////                    isShowAlarmDialog = false;
////                    mCommonDialog1.dismiss();
//                })
//                .setFirstClickListener(mCommonDialog12 -> {
//                    //查看数据
//                    switchViewData();
////                    speak("治疗结束,请取下人体端,关闭所有管夹");
//                    mCommonDialog12.dismiss();
//                })
//                .setThreeClickListener(mCommonDialog1 -> {
////                    doGoTOActivity(DataCollectionActivity.class);
////                    speak("治疗结束,请取下人体端,关闭所有管夹");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        doGoCloseTOActivity(VoActivity.class, "");
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
                    }
//                    else {
//                        doGoCloseTOActivity(TreatmentEvaluationActivity.class,"");
//                    }
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
                    initDb();
                    showCompleteDialog();
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
//                    stageTimer.cancel();
                    pauseLayout.setEnabled(false);
                    skipLayout.setEnabled(false);
                    skipTimer.start();
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

            }

            @Override
            public void onComplete() {
                Log.e("指令应答开始轮询计时", "DisposableObserver, onComplete");
            }
        };
        Observable.interval(0, 5000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        if (cmdDisposable == null) {
            cmdDisposable = new CompositeDisposable();
        }
        cmdDisposable.add(disposableObserver);
    }

    /**
     * 停止指令应答轮询获取计时
     */
    private void stopCmdLoopTimer() {
        if (cmdDisposable == null) {
            cmdDisposable = new CompositeDisposable();
        }
        cmdDisposable.clear();
    }
    private int time;
    private int reportTime = 6;
    private void initTime() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    if (!isComplete) {
                        time ++ ;
//                        if (null != fragmentItem2.getActivity()) {
//                            if (pdEntityDataList == null) {
//                                pdEntityDataList = new ArrayList<>();
//                            }
//                            fragmentItem2.setHistoricalData(pdEntityDataList);
//                        }
                        if (time > reportTime && time % reportTime == 0) {
                            if (!isStart ) {
                                deliveryTherapy();
                            }
                            sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
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

    private DisposableObserver<Long> disposableObserver;
    /**
     * 开始轮询计时 灌注 引流
     */
    private void startLoopTimer() {
        isTiming = true;
        disposableObserver = new DisposableObserver<Long>() {

            @Override
            public void onNext(Long data) {

                runOnUiThread(() -> {
//                    if (isTiming) {
                        if (1 == currentStage) {
                            currPerfusionTime++;
                            if (pdEntityDataList.size() == currCycle + 1) {
                                pdEntityDataList.get(currCycle).setPreTime(currPerfusionTime);
//                                int time = pdEntityDataList.get(currCycle).getPreTime();
                                if (currPerfusionTime != 0) {
                                    roteTv.setText(String.valueOf(Math.max(pdEntityDataList.get(currCycle).getPreVol(),0) / currPerfusionTime * 60));
                                }
                            }
                            if (pdInfoEntities.size() == currCycle + 1) {
                                pdInfoEntities.get(currCycle).preTime = currPerfusionTime / 60;
                            }

                            timeTv.setText(EmtTimeUil.getTimeMin(currPerfusionTime));
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
                            if (pdEntityDataList.size() == currCycle + 1) {
                                pdEntityDataList.get(currCycle).setDrainTime(currDrainTime);
                                if (currDrainTime !=  0) {
                                    roteTv.setText(String.valueOf(Math.max(pdEntityDataList.get(currCycle).getDrainage(),0) / currDrainTime * 60));
                                }
                                Log.e("引流速率","currDrainTime:"+currDrainTime+",Drainage:"+pdEntityDataList.get(currCycle).getDrainage()
                                +",rote:"+Math.max(pdEntityDataList.get(currCycle).getDrainage(),0) / currDrainTime * 60);
                            }
                            if (pdInfoEntities.size() == currCycle + 1) {
                                pdInfoEntities.get(currCycle).drainTime = currDrainTime / 60;
                            }
                            timeTv.setText(EmtTimeUil.getTimeMin(currDrainTime));
//                        Log.e("开始轮询计时", "currDrainTime: " + currDrainTime);
                        }
                    if (null != fragmentItem2.getActivity()) {
                        fragmentItem2.setHistoricalData(pdEntityDataList);
                    }
//                    }
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
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
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
        if (disposableObserver != null && mCompositeDisposable != null) {
            mCompositeDisposable.remove(disposableObserver);
        }
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
                                        timeTv.setText(currTime);
                                        if (pdEntityDataList.size() == currCycle + 1) {
                                            pdEntityDataList.get(currCycle).setAbdTime(currRetainTime);
                                        }
                                        if (null != mFragment3.getActivity()) {
                                            mFragment3.setWaittingTime(currTime);
                                        } else {
                                            if (detailedBean == null) {
                                                detailedBean = new DetailedBean();
                                            }
                                            detailedBean.setRetainTime(currTime);
                                        }
//                                        if (null != fragmentItem2.getActivity()) {
//                                            fragmentItem2.setHistoricalData(pdEntityDataList);
//                                        }
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
                        } else {
                            if (detailedBean == null) {
                                detailedBean = new DetailedBean();
                            }
                            detailedBean.setRetainTime(currTime);
                        }
                        startAbdTime();
                    });
                });
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(countDownDisposable);
    }

    private DisposableObserver<Long> ctDisposableObserver;
    private void startAbdTime() {
        if (countDownDisposable != null) {
            mCompositeDisposable.remove(countDownDisposable);
        }
        ctDisposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    if (currentStage == 2) {
                        currRetainTime ++ ;
                        String currTime = EmtTimeUil.getTime(currRetainTime);
                        timeTv.setText(currTime);
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
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS).subscribe(ctDisposableObserver);
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(ctDisposableObserver);
    }

    /**
     * 手动结束留腹倒计时
     */
    private void stopLoopCountDown() {
        isTiming = false;
        if (countDownDisposable != null) {
            mCompositeDisposable.remove(countDownDisposable);
        }
        if (ctDisposableObserver != null) {
            mCompositeDisposable.remove(ctDisposableObserver);
        }
    }

    /**
     * 开始最末引流提醒倒计时
     */
    private int fdt;
    private void startLoopLastDrainCountDown() {
        fdt = PdproHelper.getInstance().getDrainParameterBean().drainWarnTimeInterval * 60;
//        isTiming = true;
//        currdrainWarnTimeInterval = time;
        lastDrainDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(fdt)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        fdt--;
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
        if (lastDrainDisposable != null) {
            mCompositeDisposable.remove(lastDrainDisposable);
        }
    }

    /**
     * 暂停时间过长提示
     */
    private final CountDownTimer countDownTimer = new CountDownTimer(40 * 60 * 1000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            final CommonDialog dialog = new CommonDialog(TreatmentFragmentActivity.this);
            dialog.setContent("暂停的太久了，请恢复治疗")
                    .setBtnFirst("恢复")
                    .setBtnTwo("取消")
                    .setFirstClickListener(mCommonDialog -> {
                        refreshPauseOrResume();
                        mCommonDialog.dismiss();
                    })
                    .setTwoClickListener(mCommonDialog -> {
                        mCommonDialog.dismiss();
                    });
            if (!TreatmentFragmentActivity.this.isFinishing()) {
                dialog.show();
            }
        }
    };

    /**
     * 刷新暂停治疗的状态
     */
    private void refreshPauseOrResume() {
        suspendLoopTimer(isPause);
        isPause = !isPause;
        runOnUiThread(() -> {
            pauseLayout.setEnabled(false);
            skipLayout.setEnabled(false);
            skipTimer.start();
            if (isPause) {
                countDownTimer.start();
            } else {
                countDownTimer.cancel();
            }
            pauseTv.setText(isPause ? "恢复\n治疗" : "暂停\n治疗");
            pauseLayout.setBackgroundResource(isPause ? R.drawable.green_root_shape : R.drawable.keep_shape);
            pauseIv.setImageResource(isPause ? R.mipmap.keep : R.mipmap.pause);
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

//    private int index;
//    private void sendToMainBoard(String mCommand) {
//        if (mCommand == null || "".equals(mCommand)) {
//            return;
//        }
//        ConsumptionTask task = new ConsumptionTask();
//        task.taskNo = "Task" + (index++); // 确保唯一性
//        task.planNo = mCommand; // 将数据分组， 如果没有该需求的同学，可以不进行设置
//        lineUpTaskHelp.addTask(task); // 添加到排队列表中去， 如果还有任务没完成，
//    }
//    private void sendCommandInterval(String mCommand, long delayMillis) {
//       sendToMainBoard(mCommand);
//    }
//    private void stopSound() {
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_OFF));
//    }
//    private void buzzerOn() {
//        MyApplication.isBuzzerOff = false;
////        Log.e("baseActivity","报警中"+MyApplication.isBuzzerOff);
////        if (!MyApplication.isBuzzerOff) {
////            Log.e("baseActivity","报警中");
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_ON));
//    }
    /**
     * 查看数据····················
     */
    private void switchViewData() {
        switchFragment(0);
        showFragment(true,true,false,false);
    }

    /**
     * 刷新治疗的不同阶段界面：灌注、留腹、引流
     */
    private void switchTreatmentStatus(int mStage) {
        this.currentStage = mStage;
//        mCompositeDisposable.clear();
        runOnUiThread(() -> {
            String mPeriod = String.valueOf(currCycle);
            if (detailedBean == null) {
                detailedBean = new DetailedBean();
            }
            detailedBean.setStage(mStage);
            tvPeriod.setText(mPeriod);
            if (1 == currentStage) {
                tvStatus.setText("灌\n注");
                tvStatus.setBackgroundResource(R.drawable.per_shape);
                stageVolLabelTv.setText("当前灌注量");
                stageTimeLabelTv.setText("灌注时间");
                currentVolTv.setText("0");
                timeTv.setText("0");
                roteLl.setVisibility(View.VISIBLE);
                roteName.setText("灌注速率");
                roteTv.setText("0");
                stageIv.setImageResource(R.drawable.treatment_icon_volume_day);
                assistIv.setVisibility(View.GONE);
//                if (!PdproHelper.getInstance().getUserParameterBean().isNight && !isSleep) {
////                    if (PdproHelper.getInstance().getUserParameterBean().isNight) {
//                        sendToMainBoard(CommandDataHelper.getInstance().LedOpen("perfuse", true,0,1));
////                    }
//                }
            } else if (2 == currentStage) {
                tvStatus.setText("留\n腹");
                tvStatus.setBackgroundResource(R.drawable.abd_shape);
                stageVolLabelTv.setText("当前留腹量");
                stageTimeLabelTv.setText("留腹时间");
                if (pdEntityDataList.size()  == currCycle + 1) {
                    currentVolTv.setText(String.valueOf(Math.max(pdEntityDataList.get(currCycle).getAbdVol(), 0)));
                }
                roteLl.setVisibility(View.GONE);
//                currentVolTv.setText("0");
                timeTv.setText("0");
                stageIv.setImageResource(R.drawable.abd);
                assistIv.setVisibility(View.GONE);
            } else if (3 == currentStage) {
                tvStatus.setText("引\n流");
                tvStatus.setBackgroundResource(R.drawable.drain_shape);
                stageVolLabelTv.setText("当前引流量");
                stageTimeLabelTv.setText("引流时间");
                currentVolTv.setText("0");
                timeTv.setText("0");
                roteLl.setVisibility(View.VISIBLE);
                roteName.setText("引流速率");
                roteTv.setText("0");
                stageIv.setImageResource(R.drawable.drain_zl);
                assistIv.setVisibility(View.GONE);
            }
        });

        if (2 == currentStage) {
            startLoopCountDown();
        }
    }

    // alertNumberBoardDialog(String.valueOf(PdproHelper.getInstance().targetTemper()), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE, true);
    //                修改温度
    private void alertNumberBoardDialog(String value, String type, boolean isMinus) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, isMinus);//"℃"
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {

                if (mType.equals(PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE)) {//目标温度值  :  在34.0-40.0之间
                    result = Float.valueOf(result) + "";//转换成浮点数
                    if (!TextUtils.isEmpty(result)) {
                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.targetTemper, Float.valueOf(result)+"");
                        //设置温度
                        sendToMainBoard(CommandDataHelper.getInstance().setTemperatureCmdJson((int) (PdproHelper.getInstance().targetTemper() * 10 + 20)));

                    }
                } else if (mType.equals(PdGoConstConfig.AUTO_SLEEP)) {
                    if (otherParamBean == null) {
                        otherParamBean = PdproHelper.getInstance().getOtherParamBean();
                    }
                    otherParamBean.sleep = Integer.parseInt(result);
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.OTHER_PARAMETER, otherParamBean);
                    if (downTimer != null) {
                        downTimer.cancel();
                    }
                    downTimer = new MonitorTouchCountDownTimer((long) PdproHelper.getInstance().getOtherParamBean().sleep * 60 * 1000, 1000);
                    downTimer.start();
                }
            }
        });
    }

    private OtherParamBean otherParamBean;

    private void reviseConfirm() {
        Log.e("灌注","rate:"+PdproHelper.getInstance().getPerfusionParameterBean().perfTimeInterval
        +",value:"+PdproHelper.getInstance().getPerfusionParameterBean().perfThresholdValue);
        if (maxCycle > currCycle ) {
            if (MyApplication.apdMode == 1) {
                if (ipdBean.cycle < currCycle) {
                    toastMessage("修改后的周期数要大于当前周期");
                } else if (ipdBean.firstPerfusionVolume > 0 && ipdBean.perCyclePerfusionVolume > ipdBean.firstPerfusionVolume) {
                            toastMessage("周期灌注量不能大于首周期灌注量");
                } else {
                    submit();
                }
            } else if (MyApplication.apdMode == 2) {
                if (tpdBean.cycle < currCycle) {
                    toastMessage("修改后的周期数要大于当前周期");
                }
//                        else if (tpdBean.cycle == currCycle && currentStage == 3) {
//                            toastMessage("最末引流期间不能修改");
//                        }
                else {
                    submit();
                }
            } else if (MyApplication.apdMode == 3) {
                if (ccpdBean.cycle < currCycle) {
                    toastMessage("修改后的周期数要大于当前周期");
                }
//                        else if (ccpdBean.cycle == currCycle && currentStage == 3) {
//                            toastMessage("最末引流期间不能修改");
//                        }
                else {
                    submit();
                }
            } else if (MyApplication.apdMode == 4) {
                int cycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4
                        + aapdBean.c5 + aapdBean.c6 + aapdBean.c7;
                if (cycle < currCycle) {
                    toastMessage("修改后的周期数要大于当前周期");
                } else if (aapdBean.c2 != 0 && aapdBean.r2 != 0 && aapdBean.p2 != 0) {
                    if (aapdBean.c3 != 0 && aapdBean.r3 != 0 && aapdBean.p3 != 0) {
                        if (aapdBean.c4 != 0 && aapdBean.r4 != 0 && aapdBean.p4 != 0) {
                            submit();
                        } else if (aapdBean.c4 == 0 && aapdBean.r4 == 0 && aapdBean.p4 == 0) {
                            submit();
                        } else {
                            if (aapdBean.c4 == 0 ) {
                                toastMessage("第4组周期数不能为0");
                            } else if (aapdBean.r4 == 0) {
                                toastMessage("第4组留腹时间不能为0");
                            } else if (aapdBean.p4 == 0) {
                                toastMessage("第4组灌注量不能为0");
                            }
//                            toastMessage("第四组未设置正确");
                        }
                    } else {
                        if (aapdBean.c3 == 0 ) {
                            toastMessage("第3组周期数不能为0");
                        } else if (aapdBean.r3 == 0) {
                            toastMessage("第3组留腹时间不能为0");
                        } else if (aapdBean.p3 == 0) {
                            toastMessage("第3组灌注量不能为0");
                        }
                    }
                } else if (aapdBean.c2 == 0 && aapdBean.r2 == 0 && aapdBean.p2 == 0) {
                    if (aapdBean.c3 != 0 && aapdBean.r3 != 0 && aapdBean.p3 != 0) {
                        if (aapdBean.c2 == 0 ) {
                            toastMessage("第2组周期数不能为0");
                        } else if (aapdBean.r2 == 0) {
                            toastMessage("第2组留腹时间不能为0");
                        } else if (aapdBean.p2 == 0) {
                            toastMessage("第2组灌注量不能为0");
                        }
//                        toastMessage("第2组未设置正确");
                    } else if (aapdBean.c4 != 0 && aapdBean.r4 != 0 && aapdBean.p4 != 0) {
//                        toastMessage("第2组未设置正确");
                        if (aapdBean.c2 == 0 ) {
                            toastMessage("第2组周期数不能为0");
                        } else if (aapdBean.r2 == 0) {
                            toastMessage("第2组留腹时间不能为0");
                        } else if (aapdBean.p2 == 0) {
                            toastMessage("第2组灌注量不能为0");
                        }
                    }
                } else {
                    if (aapdBean.c2 == 0 ) {
                        toastMessage("第2组周期数不能为0");
                    } else if (aapdBean.r2 == 0) {
                        toastMessage("第2组留腹时间不能为0");
                    } else if (aapdBean.p2 == 0) {
                        toastMessage("第2组灌注量不能为0");
                    }
                }
            } else if (MyApplication.apdMode == 7) {
                if (kidBean.cycle < currCycle) {
                    toastMessage("修改后的周期数要大于当前周期");
                }
//                        else if (kidBean.cycle == currCycle && currentStage == 3) {
//                            toastMessage("最末引流期间不能修改");
//                        }
                else {
                    submit();
                }
            } else if (MyApplication.apdMode == 8) {
                if (expertBean.cycleMyself) {
                    int cycle = Collections.max(expertBean.baseSupplyCycle)
                            > Collections.max(expertBean.osmSupplyCycle) ? Collections.max(expertBean.baseSupplyCycle) :
                            Collections.max(expertBean.osmSupplyCycle);
                    if (cycle < currCycle) {
                        toastMessage("修改后的周期数要大于当前周期");
                    }
//                            else if (cycle == currCycle && currentStage == 3) {
//                                toastMessage("最末引流期间不能修改");
//                            }
                    else {
                        submit();
                    }
                } else {
                    if (expertBean.cycle < currCycle) {
                        toastMessage("修改后的周期数要大于当前周期");
                    }
//                            else if (expertBean.cycle == currCycle && currentStage == 3) {
//                                toastMessage("最末引流期间不能修改");
//                            }
                    else {
                        submit();
                    }
                }

            }
        } else {
            toastMessage("最后一个周期不能修改");
        }
    }

    private void submit() {
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
                    isHospital(false);
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(mCommonDialog -> {
                    isConfirm = false;
                    mCommonDialog.dismiss();
                });
        if (!TreatmentFragmentActivity.this.isFinishing()) {
            dialog.show();
        }
    }

    public boolean isConfirm;

    @BindView(R.id.silencersLayout)
    LinearLayout silencersLayout;
    @BindView(R.id.tvSilencers)
    TextView tvSilencers;
    @BindView(R.id.btnSoundIv)
    ImageView btnSound;

    @BindView(R.id.textClock)
    TextClock textClock;

    @BindView(R.id.powerTv)
    TextView powerTv;

    @Override
    public void notifyByThemeChanged() {

        runOnUiThread(() -> {
            MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
            helper.setBackgroundResourceByAttr(layoutMain, R.attr.custom_attr_treatment_main_right_bg);
            helper.setTextColorByAttr(tvPeriod, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvStatus, R.attr.custom_attr_common_text_color);
            helper.setTextColorByAttr(tvMaxCycle, R.attr.custom_attr_common_text_color);
        });
    }

    private void switchFragment(int mItem) {
        mCurrentPosition = mItem;
        mViewPager.setCurrentItem(mCurrentPosition, false);
    }

    public void showFragment(boolean isShowFragment, boolean isShowData,
                             boolean isShowConfig, boolean isShowPre) {
        layoutMain.setVisibility(isShowFragment ? View.GONE : View.VISIBLE);
        layoutFragment.setVisibility(isShowData ? View.VISIBLE : View.GONE);
        prescriptionFragment.setVisibility(isShowPre ? View.VISIBLE : View.GONE);
        configFragment.setVisibility(isShowConfig ? View.VISIBLE : View.GONE);
    }

    private boolean emmy;
    @Override
    protected void onPause() {
        super.onPause();
//        isSleep = true;
        emmy = true;
        Log.e("onPause","onPause:" + emmy);
//        PdproHelper.getInstance().updateTtsSoundOpen(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        isSleep = false;
        emmy = true;
        runOnUiThread(()->{
            if (PdproHelper.getInstance().getOtherParamBean().isHospital) {
                btnRevise.setVisibility(View.VISIBLE);
            } else {
                btnRevise.setVisibility(View.GONE);
            }
        });
        PdproHelper.getInstance().updateTtsSoundOpen(true);
        if (getThemeTag() == 1) {
            if (currentStage == 1) {
                sendToMainBoard(CommandDataHelper.getInstance().LedOpen("perfuse", true,0,isShowAlarmDialog?0:1));
            } else if (currentStage == 2) {
                sendToMainBoard(CommandDataHelper.getInstance().LedOpen("retain", true, 0, isShowAlarmDialog ? 0 : 1));
            } else if (currentStage == 3) {
                if (currCycle != 0) {
                    sendToMainBoard(CommandDataHelper.getInstance().LedOpen("drain", true, 0, isShowAlarmDialog ? 0 : 1));
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
                downTimer.cancel();
                Log.e("downTimer","ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                //抬起时启动定时
                downTimer.cancel();
                downTimer.start();
                Log.e("downTimer","ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @BindView(R.id.apTotalVolRl)
    RelativeLayout apTotalVolRl;
    @BindView(R.id.apTotalVolTv)
    TextView apTotalVolTv;
    @BindView(R.id.apFinalRetainVolRl)
    RelativeLayout apFinalRetainVolRl;
    @BindView(R.id.apFinalRetainVolTv)
    TextView apFinalRetainVolTv;

    @BindView(R.id.apCheckboxLl)
    LinearLayout apCheckboxLl;

    @BindView(R.id.group_1_tv)
    TextView group_1_tv;

    @BindView(R.id.per_01_rl)
    RelativeLayout per_01_rl;
    @BindView(R.id.per_01_tv)
    TextView per_01_tv;

    @BindView(R.id.retain_01_rl)
    RelativeLayout retain_01_rl;
    @BindView(R.id.retain_01_tv)
    TextView retain_01_tv;

    @BindView(R.id.cycle_01_rl)
    RelativeLayout cycle_01_rl;
    @BindView(R.id.cycle_01_tv)
    TextView cycle_01_tv;

    @BindView(R.id.group_2_tv)
    TextView group_2_tv;

    @BindView(R.id.per_02_rl)
    RelativeLayout per_02_rl;
    @BindView(R.id.per_02_tv)
    TextView per_02_tv;

    @BindView(R.id.retain_02_rl)
    RelativeLayout retain_02_rl;
    @BindView(R.id.retain_02_tv)
    TextView retain_02_tv;

    @BindView(R.id.cycle_02_rl)
    RelativeLayout cycle_02_rl;
    @BindView(R.id.cycle_02_tv)
    TextView cycle_02_tv;

    @BindView(R.id.group_3_tv)
    TextView group_3_tv;

    @BindView(R.id.per_03_rl)
    RelativeLayout per_03_rl;
    @BindView(R.id.per_03_tv)
    TextView per_03_tv;

    @BindView(R.id.retain_03_rl)
    RelativeLayout retain_03_rl;
    @BindView(R.id.retain_03_tv)
    TextView retain_03_tv;

    @BindView(R.id.cycle_03_rl)
    RelativeLayout cycle_03_rl;
    @BindView(R.id.cycle_03_tv)
    TextView cycle_03_tv;

    @BindView(R.id.group_4_tv)
    TextView group_4_tv;

    @BindView(R.id.per_04_rl)
    RelativeLayout per_04_rl;
    @BindView(R.id.per_04_tv)
    TextView per_04_tv;

    @BindView(R.id.retain_04_rl)
    RelativeLayout retain_04_rl;
    @BindView(R.id.retain_04_tv)
    TextView retain_04_tv;

    @BindView(R.id.cycle_04_rl)
    RelativeLayout cycle_04_rl;
    @BindView(R.id.cycle_04_tv)
    TextView cycle_04_tv;

    @BindView(R.id.rg1)
    RadioGroup rg1;
    @BindView(R.id.rb1C1)
    RadioButton rb1c1;
    @BindView(R.id.rb1C2)
    RadioButton rb1c2;

    @BindView(R.id.rg2)
    RadioGroup rg2;
    @BindView(R.id.rb2C1)
    RadioButton rb2c1;
    @BindView(R.id.rb2C2)
    RadioButton rb2c2;

    @BindView(R.id.rg3)
    RadioGroup rg3;
    @BindView(R.id.rb3C1)
    RadioButton rb3c1;
    @BindView(R.id.rb3C2)
    RadioButton rb3c2;

    @BindView(R.id.rg4)
    RadioGroup rg4;
    @BindView(R.id.rb4C1)
    RadioButton rb4c1;
    @BindView(R.id.rb4C2)
    RadioButton rb4c2;

    @BindView(R.id.apFinalSupplyCheck)
    CheckBox apFinalSupplyCheck;

    @BindView(R.id.bag_rl)
    RelativeLayout bag_rl;
    @BindView(R.id.bag_tv)
    TextView bag_tv;

    private void aapdClick() {
        apTotalVolTv.setText(String.valueOf(aapdBean.total));
        apFinalRetainVolTv.setText(String.valueOf(aapdBean.finalVol));

        per_01_tv.setText(String.valueOf(aapdBean.p1));
        per_02_tv.setText(String.valueOf(aapdBean.p2));
        per_03_tv.setText(String.valueOf(aapdBean.p3));
        per_04_tv.setText(String.valueOf(aapdBean.p4));

        bag_tv.setText(String.valueOf(aapdBean.bagVol));

        retain_01_tv.setText(String.valueOf(aapdBean.r1));
        retain_02_tv.setText(String.valueOf(aapdBean.r2));
        retain_03_tv.setText(String.valueOf(aapdBean.r3));
        retain_04_tv.setText(String.valueOf(aapdBean.r4));

        cycle_01_tv.setText(String.valueOf(aapdBean.c1));
        cycle_02_tv.setText(String.valueOf(aapdBean.c2));
        cycle_03_tv.setText(String.valueOf(aapdBean.c3));
        cycle_04_tv.setText(String.valueOf(aapdBean.c4));

        apFinalSupplyCheck.setChecked(aapdBean.isFinalSupply);
        apFinalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            aapdBean.isFinalSupply = isChecked;
        });

        rg1.check(aapdBean.a1 == 0 ? R.id.rb1C1: R.id.rb1C2);
        rg2.check(aapdBean.a2 == 0 ? R.id.rb2C1: R.id.rb2C2);
        rg3.check(aapdBean.a3 == 0 ? R.id.rb3C1: R.id.rb3C2);
        rg4.check(aapdBean.a4 == 0 ? R.id.rb4C1: R.id.rb4C2);

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    aapdBean.a1 = 0;
                } else if (rb.getText().equals("通道2")) {
                    aapdBean.a1 = 1;
                }
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    aapdBean.a2 = 0;
                } else if (rb.getText().equals("通道2")) {
                    aapdBean.a2 = 1;
                }
            }
        });
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    aapdBean.a3 = 0;
                } else if (rb.getText().equals("通道2")) {
                    aapdBean.a3 = 1;
                }
            }
        });
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    aapdBean.a4 = 0;
                } else if (rb.getText().equals("通道2")) {
                    aapdBean.a4 = 1;
                }
            }
        });

        per_01_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_p1,EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        per_02_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog( PdGoConstConfig.aApd_p2,EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        per_03_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_p3,EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        per_04_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_p4,EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });

        bag_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_bag,EmtConstant.cycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });

        retain_01_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog( PdGoConstConfig.aApd_r1, EmtConstant.aapdAbdTimeMin,EmtConstant.abdTimeMax);
        });
        retain_02_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_r2, EmtConstant.aapdAbdTimeMin,EmtConstant.abdTimeMax);
        });
        retain_03_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog( PdGoConstConfig.aApd_r3, EmtConstant.aapdAbdTimeMin,EmtConstant.abdTimeMax);
        });
        retain_04_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_r4, EmtConstant.aapdAbdTimeMin,EmtConstant.abdTimeMax);
        });

        cycle_01_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_c1, EmtConstant.aapdCycleNumMin,EmtConstant.cycleNumMax);
        });
        cycle_02_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_c2,EmtConstant.aapdCycleNumMin,EmtConstant.cycleNumMax);
        });
        cycle_03_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_c3, EmtConstant.aapdCycleNumMin,EmtConstant.cycleNumMax);
        });
        cycle_04_rl.setOnClickListener(v -> {
            aapdNumberBoardDialog(PdGoConstConfig.aApd_c4, EmtConstant.aapdCycleNumMin,EmtConstant.cycleNumMax);
        });

        apFinalRetainVolRl.setOnClickListener(v -> {
            aapdNumberBoardDialog( PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY,EmtConstant.cycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
    }

    private void aapdNumberBoardDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(this, type, min, max);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                if (aapdBean == null) {
                    aapdBean = PdproHelper.getInstance().aapdBean();
                }
                switch (mType) {
                    case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY:
                        apFinalRetainVolTv.setText(result);
                        aapdBean.finalVol = Integer.parseInt(result);
                        setAapdTotal();
                        break;
                    case PdGoConstConfig.aApd_bag:
                        bag_tv.setText(result);
                        aapdBean.bagVol = Integer.parseInt(result);
                        setAapdCycleNum();
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_p1:
                        per_01_tv.setText(result);
                        aapdBean.p1 = Integer.parseInt(result);
//                        if (bean.p1 == 0) {
//                            bean.c1 = 0;
//                            bean.r1 = 0;
//                        }
                        setAapdCycleNum();
                        break;
                    case PdGoConstConfig.aApd_p2:
                        per_02_tv.setText(result);
                        aapdBean.p2 = Integer.parseInt(result);
                        if (aapdBean.p2 == 0) {
                            aapdBean.c2 = 0;
                            aapdBean.r2 = 0;
                            cycle_02_tv.setText(String.valueOf(aapdBean.c2));
                            retain_02_tv.setText(String.valueOf(aapdBean.r2));
                        }
                        setAapdTotal();
                        break;
                    case PdGoConstConfig.aApd_p3:
                        per_03_tv.setText(result);
                        aapdBean.p3 = Integer.parseInt(result);
                        if (aapdBean.p3 == 0) {
                            aapdBean.c3 = 0;
                            aapdBean.r3 = 0;
                            cycle_03_tv.setText(String.valueOf(aapdBean.c3));
                            retain_03_tv.setText(String.valueOf(aapdBean.r3));
                        }
                        setAapdTotal();
                        break;
                    case PdGoConstConfig.aApd_p4:
                        per_04_tv.setText(result);
                        aapdBean.p4 = Integer.parseInt(result);
                        if (aapdBean.p4 == 0) {
                            aapdBean.c4 = 0;
                            aapdBean.r4 = 0;
                            cycle_04_tv.setText(String.valueOf(aapdBean.c4));
                            retain_04_tv.setText(String.valueOf(aapdBean.r4));
                        }
                        setAapdTotal();
                        break;
                    case PdGoConstConfig.aApd_r1:
                        retain_01_tv.setText(result);
                        aapdBean.r1 = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.aApd_r2:
                        retain_02_tv.setText(result);
                        aapdBean.r2 = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.aApd_r3:
                        retain_03_tv.setText(result);
                        aapdBean.r3 = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.aApd_r4:
                        retain_04_tv.setText(result);
                        aapdBean.r4 = Integer.parseInt(result);
                        break;

                    case PdGoConstConfig.aApd_c1:
                        cycle_01_tv.setText(result);
                        aapdBean.c1 = Integer.parseInt(result);
//                        setTotal();
                        setAapdCyclePre();
                        break;
                    case PdGoConstConfig.aApd_c2:
                        cycle_02_tv.setText(result);
                        aapdBean.c2 = Integer.parseInt(result);
                        setAapdTotal();
                        break;
                    case PdGoConstConfig.aApd_c3:
                        cycle_03_tv.setText(result);
                        aapdBean.c3 = Integer.parseInt(result);
                        setAapdTotal();
                        break;
                    case PdGoConstConfig.aApd_c4:
                        cycle_04_tv.setText(result);
                        aapdBean.c4 = Integer.parseInt(result);
                        setAapdTotal();
                        break;
                }
            }
        });
    }

    private void setAapdBackground(AapdBean bean, int cycle) {
        if (cycle >= bean.c1) {
            group_1_tv.setBackgroundResource(R.drawable.gray_shape);
            per_01_rl.setBackgroundResource(R.drawable.gray_shape);
            bag_rl.setBackgroundResource(R.drawable.gray_shape);
            retain_01_rl.setBackgroundResource(R.drawable.gray_shape);
            cycle_01_rl.setBackgroundResource(R.drawable.gray_shape);

            per_01_rl.setEnabled(false);
            bag_rl.setEnabled(false);
            retain_01_rl.setEnabled(false);
            cycle_01_rl.setEnabled(false);
        }
        if (cycle >= bean.c1+bean.c2) {
            group_2_tv.setBackgroundResource(R.drawable.gray_shape);
            per_02_rl.setBackgroundResource(R.drawable.gray_shape);
            retain_02_rl.setBackgroundResource(R.drawable.gray_shape);
            cycle_02_rl.setBackgroundResource(R.drawable.gray_shape);

            rg2.setBackgroundResource(R.drawable.gray_shape);
            rg2.setEnabled(false);
            per_02_rl.setEnabled(false);
            retain_02_rl.setEnabled(false);
            cycle_02_rl.setEnabled(false);
        }
        if (cycle >= bean.c1+bean.c2+bean.c3) {
            group_3_tv.setBackgroundResource(R.drawable.gray_shape);
            per_03_rl.setBackgroundResource(R.drawable.gray_shape);
            retain_03_rl.setBackgroundResource(R.drawable.gray_shape);
            cycle_03_rl.setBackgroundResource(R.drawable.gray_shape);
            rg3.setBackgroundResource(R.drawable.gray_shape);
            rg3.setEnabled(false);
            per_03_rl.setEnabled(false);
            retain_03_rl.setEnabled(false);
            cycle_03_rl.setEnabled(false);

        }

        if (cycle >= bean.c1+bean.c2+bean.c3+bean.c4) {
            group_4_tv.setBackgroundResource(R.drawable.gray_shape);
            per_04_rl.setBackgroundResource(R.drawable.gray_shape);
            retain_04_rl.setBackgroundResource(R.drawable.gray_shape);
            cycle_04_rl.setBackgroundResource(R.drawable.gray_shape);
            rg4.setBackgroundResource(R.drawable.gray_shape);
            rg4.setEnabled(false);
            per_04_rl.setEnabled(false);
            retain_04_rl.setEnabled(false);
            cycle_04_rl.setEnabled(false);
        }

//        if (cycle >= bean.c1+bean.c2+bean.c3+bean.c4+bean.c5) {
//            group_5_tv.setBackgroundResource(R.drawable.gray_shape);
//            per_05_rl.setBackgroundResource(R.drawable.gray_shape);
//            retain_05_rl.setBackgroundResource(R.drawable.gray_shape);
//            cycle_05_rl.setBackgroundResource(R.drawable.gray_shape);
//            rg5.setBackgroundResource(R.drawable.gray_shape);
//            rg5.setEnabled(false);
//            per_05_rl.setEnabled(false);
//            retain_05_rl.setEnabled(false);
//            cycle_05_rl.setEnabled(false);
//        }
//
//        if (cycle >= bean.c1+bean.c2+bean.c3+bean.c4+bean.c5+bean.c6) {
//            group_6_tv.setBackgroundResource(R.drawable.gray_shape);
//            per_06_rl.setBackgroundResource(R.drawable.gray_shape);
//            retain_06_rl.setBackgroundResource(R.drawable.gray_shape);
//            cycle_06_rl.setBackgroundResource(R.drawable.gray_shape);
//            rg6.setBackgroundResource(R.drawable.gray_shape);
//            rg6.setEnabled(false);
//            per_06_rl.setEnabled(false);
//            retain_06_rl.setEnabled(false);
//            cycle_06_rl.setEnabled(false);
//        }
    }

    @BindView(R.id.svTotalVolTv)
    TextView svTotalVolTv;
    @BindView(R.id.svTotalVolLl)
    LinearLayout svTotalVolLl;
    @BindView(R.id.svCycleVolTv)
    TextView svCycleVolTv;
    @BindView(R.id.svCycleVolLl)
    LinearLayout svCycleVolLl;
    @BindView(R.id.svCycleNumTv)
    TextView svCycleNumTv;
    @BindView(R.id.svCycleNumLl)
    LinearLayout svCycleNumLl;
    @BindView(R.id.svAbdTimeTv)
    TextView svAbdTimeTv;
    @BindView(R.id.svAbdTimeLl)
    LinearLayout svAbdTimeLl;
    @BindView(R.id.svFinalVolTv)
    TextView svFinalVolTv;
    @BindView(R.id.svFinalVolLl)
    LinearLayout svFinalVolLl;
    @BindView(R.id.svLastVolTv)
    TextView svLastVolTv;
    @BindView(R.id.svLastVolLl)
    LinearLayout svLastVolLl;
    @BindView(R.id.svUltVolTv)
    TextView svUltVolTv;
    @BindView(R.id.svUltVolLl)
    LinearLayout svUltVolLl;
    @BindView(R.id.svFirstVolTv)
    TextView svFirstVolTv;
    @BindView(R.id.svFirstVolLl)

    LinearLayout svFirstVolLl;

    @BindView(R.id.con_1_1_5)
    TextView con_1_1_5;
    @BindView(R.id.con_1_2_5)
    TextView con_1_2_5;
    @BindView(R.id.con_1_4_25)
    TextView con_1_4_25;
    @BindView(R.id.con_1_other)
    TextView con_1_other;
    @BindView(R.id.con_2_1_5)
    TextView con_2_1_5;
    @BindView(R.id.con_2_2_5)
    TextView con_2_2_5;
    @BindView(R.id.con_2_4_25)
    TextView con_2_4_25;
    @BindView(R.id.con_2_other)
    TextView con_2_other;

    @BindView(R.id.stage_01_CycleVolLl)
    LinearLayout stage_01_CycleVolLl;
    @BindView(R.id.stage_01_CycleNumLl)
    LinearLayout stage_01_CycleNumLl;
    @BindView(R.id.stage_01_VolTv)
    TextView stage_01_VolTv;
    @BindView(R.id.stage_01_cycleNumTv)
    TextView stage_01_cycleNumTv;
    @BindView(R.id.stage_02_cycleVolTv)
    TextView stage_02_cycleVolTv;
    @BindView(R.id.stage_02_cycleNumTv)
    TextView stage_02_cycleNumTv;
    @BindView(R.id.stage_02_cycleVolLl)
    LinearLayout stage_02_cycleVolLl;
    @BindView(R.id.stage_02_cycleNumLl)
    LinearLayout stage_02_cycleNumLl;

    private void setSvBg(boolean click) {
        svTotalVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        svCycleNumLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        svCycleVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
//        finalVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        svFirstVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        svTotalVolLl.setEnabled(!click);
        svCycleNumLl.setEnabled(!click);
        svCycleVolLl.setEnabled(!click);
//        finalVolLl.setEnabled(!click);
        svFirstVolLl.setEnabled(!click);
        stage_01_CycleNumLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_01_CycleVolLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_01_CycleNumLl.setEnabled(click);
        stage_01_CycleVolLl.setEnabled(click);
        stage_02_cycleVolLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_02_cycleNumLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_02_cycleVolLl.setEnabled(click);
        stage_02_cycleNumLl.setEnabled(click);
        if (expertBean == null) {
            expertBean = PdproHelper.getInstance().expertBean();
        }
        if (click) {
            setSvSupplyTotal(expertBean);
        } else {
            setSvTotal(expertBean);
        }
    }

    private void svClick() {
        if (expertBean == null) {
            expertBean = PdproHelper.getInstance().expertBean();
        }

        svCycleVolTv.setText(String.valueOf(expertBean.cycleVol));
        svCycleNumTv.setText(String.valueOf(expertBean.cycle));
        svUltVolTv.setText(String.valueOf(expertBean.ultVol));
        svFirstVolTv.setText(String.valueOf(expertBean.firstVol));
        svFinalVolTv.setText(String.valueOf(expertBean.finalRetainVol));
        svLastVolTv.setText(String.valueOf(expertBean.lastRetainVol));
        svAbdTimeTv.setText(String.valueOf(expertBean.retainTime));
        if (expertBean.con_1 == 1.5) {
            setCon1(true,false,false,false);
        } else if (expertBean.con_1 == 2.5) {
            setCon1(false,true,false,false);
        } else if (expertBean.con_1 == 4.25) {
            setCon1(false,false,true,false);
        } else {
            setCon1(false,false,false,true);
            con_1_other.setText(expertBean.con_1+"%");
        }
        if (expertBean.con_2 == 1.5) {
            setCon2(true,false,false,false);
        } else if (expertBean.con_2 == 2.5) {
            setCon2(false,true,false,false);
        } else if (expertBean.con_2 == 4.25) {
            setCon2(false,false,true,false);
        } else {
            setCon2(false,false,false,true);
            con_2_other.setText(expertBean.con_2+"%");
        }
        stage_01_cycleNumTv.setText(expertBean.baseSupplyCycle.toString());
        stage_02_cycleNumTv.setText(expertBean.osmSupplyCycle.toString());
        stage_01_VolTv.setText(String.valueOf(expertBean.baseSupplyVol));
        stage_02_cycleVolTv.setText(String.valueOf(expertBean.osmSupplyVol));
        stage_01_CycleNumLl.setOnClickListener(v -> {
            SpecialNumberDialog dialog = new SpecialNumberDialog(this, expertBean, 0, stage_01_cycleNumTv);
            dialog.show();
            dialog.setOnDialogResultListener((model, expertBean) -> {
                stage_01_cycleNumTv.setText(expertBean.baseSupplyCycle.toString());
                setSvSupplyTotal(expertBean);
            });
        });
        stage_02_cycleNumLl.setOnClickListener(v -> {
            SpecialNumberDialog dialog = new SpecialNumberDialog(this, expertBean, 1, stage_01_cycleNumTv);
            dialog.show();
            dialog.setOnDialogResultListener((model, expertBean) -> {
                stage_02_cycleNumTv.setText(expertBean.osmSupplyCycle.toString());
                setSvSupplyTotal(expertBean);
            });
        });
        stage_01_CycleVolLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_BASE_SUPPLY_VOL,  EmtConstant.cycleVolMin, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        stage_02_cycleVolLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_OSM_SUPPLY_VOL,EmtConstant.cycleVolMin, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });

        svTotalVolLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_TOTAL,EmtConstant.totalVolMin, EmtConstant.totalVolMax);
        });
        svCycleVolLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_CYCLE_VOL,EmtConstant.cycleVolMin
                    ,expertBean.firstVol == 0 ? PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue: expertBean.firstVol);

        });
        con_1_1_5.setOnClickListener(view -> {
            setCon1(!con_1_1_5.isSelected(),false,false,false);
        });
        con_1_2_5.setOnClickListener(view -> {
            setCon1(false,!con_1_2_5.isSelected(),false,false);
        });
        con_1_4_25.setOnClickListener(view -> {
            setCon1(false,false,!con_1_4_25.isSelected(),false);
        });
        con_1_other.setOnClickListener(view -> {
            setCon1(false,false,false,!con_1_other.isSelected());
            arNumD(con_1_other.getText().toString(),"con_1");
        });
        con_2_1_5.setOnClickListener(view -> {
            setCon2(!con_2_1_5.isSelected(),false,false,false);
        });
        con_2_2_5.setOnClickListener(view -> {
            setCon2(false,!con_2_2_5.isSelected(),false,false);
        });
        con_2_4_25.setOnClickListener(view -> {
            setCon2(false,false,!con_2_4_25.isSelected(),false);
        });
        con_2_other.setOnClickListener(view -> {
            setCon2(false,false,false,!con_2_other.isSelected());
            arNumD(con_2_other.getText().toString(),"con_2");
        });
        svCycleNumLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_CYCLE,EmtConstant.cycleNumMin, EmtConstant.cycleNumMax);

        });
        svAbdTimeLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_RETAIN_TIME,EmtConstant.abdTimeMin, EmtConstant.abdTimeMax);

        });
        svFinalVolLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_FINAL_RETAIN_VOL, EmtConstant.finalVolMin, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });


        svLastVolLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_LAST_RETAIN_VOL, EmtConstant.lastAbdMin, EmtConstant.lastAbdMax);

        });
        svFirstVolLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME, 0, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        svUltVolLl.setOnClickListener(v -> {
            alertSvNumberDialog(PdGoConstConfig.EXPERT_ULT_VOL, EmtConstant.ultVolMin, EmtConstant.ultVolMax);
        });
    }
    private void setCon1(boolean c1, boolean c2, boolean c3, boolean c4) {
        con_1_1_5.setSelected(c1);
        con_1_2_5.setSelected(c2);
        con_1_4_25.setSelected(c3);
        con_1_other.setSelected(c4);
//        con_1_other.setText(c4? entity.con_1 + "%" : "其他");
    }
    private void setCon2(boolean c1, boolean c2, boolean c3, boolean c4) {
        con_2_1_5.setSelected(c1);
        con_2_2_5.setSelected(c2);
        con_2_4_25.setSelected(c3);
        con_2_other.setSelected(c4);
//        con_2_other.setText(c4? entity.con_2 + "%" : "其他");
    }
    private void arNumD(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, true);



        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            double c = Double.parseDouble(result);

            if (!TextUtils.isEmpty(result)) {
                switch (mType) {
                    case "con_1":
                        con_1_other.setText(c +"%");
                        expertBean.con_1 = c;
                        break;
                    case "con_2":
                        con_2_other.setText(c +"%");
                        expertBean.con_2 = c;
                        break;
                }
            }
        });
    }
    private void alertSvNumberDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(this, type, min, max);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    if (expertBean == null) {
                        expertBean = PdproHelper.getInstance().expertBean();
                    }
                    switch (mType) {
                        case PdGoConstConfig.EXPERT_TOTAL: //腹透液总量
                            svTotalVolTv.setText(result);
                            expertBean.total = Integer.parseInt(result);
                            setSvCycleNum(expertBean);
                            break;
                        case PdGoConstConfig.EXPERT_CYCLE_VOL: //每周期灌注量
                            svCycleVolTv.setText(result);
                            expertBean.cycleVol = Integer.parseInt(result);
                            setSvCycleNum(expertBean);
                            break;
                        case PdGoConstConfig.EXPERT_CYCLE: //循环治疗周期数
                            svCycleNumTv.setText(result);
                            expertBean.cycle = Integer.parseInt(result);
//                            setTotal();
                            setSvCyclePre(expertBean);
                            break;
                        case PdGoConstConfig.EXPERT_RETAIN_TIME: //留腹时间
                            svAbdTimeTv.setText(result);
                            expertBean.retainTime = Integer.parseInt(result);
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.EXPERT_BASE_SUPPLY_VOL:
                            stage_01_VolTv.setText(result);
                            expertBean.baseSupplyVol = Integer.parseInt(result);
//                            setPeriodicities();
                            setSvSupplyTotal(expertBean);
                            break;
                        case PdGoConstConfig.EXPERT_OSM_SUPPLY_VOL:
                            stage_02_cycleVolTv.setText(result);
                            expertBean.osmSupplyVol = Integer.parseInt(result);
//                            setPeriodicities();
                            setSvSupplyTotal(expertBean);
                            break;
                        case PdGoConstConfig.EXPERT_FINAL_RETAIN_VOL: //末次留腹量
                            svFinalVolTv.setText(result);
                            expertBean.finalRetainVol = Integer.parseInt(result);
                            setSvTotal(expertBean);
//                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_FINAL_SUPPLY: //末次留腹量
                            svFinalVolTv.setText(result);
                            expertBean.finalSupply = Integer.parseInt(result);
//                            setTotal();
                            setSvCycleNum(expertBean);
                            break;
                        case PdGoConstConfig.EXPERT_LAST_RETAIN_VOL: //上次留腹量
                            svLastVolTv.setText(result);
                            expertBean.lastRetainVol = Integer.parseInt(result);
//                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
                            svFirstVolTv.setText(result);
                            expertBean.firstVol = Integer.parseInt(result);
                            setSvTotal(expertBean);
//                            setCycleNum();
                            break;
                        case PdGoConstConfig.EXPERT_ULT_VOL: //预估超滤量
                            svUltVolTv.setText(result);
                            expertBean.ultVol = Integer.parseInt(result);
//                            setCycleNum();
                            break;
                    }
                }
            }
        });
    }

    private void setSvCyclePre(ExpertBean expertBean) {
        double total = (expertBean.total
                - expertBean.firstVol ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - expertBean.finalRetainVol - 500) / Double.parseDouble(String.valueOf(expertBean.cycle)) / 50.00;

        int perCyclePerfusionVolume = (int) Math.floor(total);
        int cycleVol = perCyclePerfusionVolume * 50;

        svCycleVolTv.setText(String.valueOf(cycleVol));
        expertBean.cycleVol = cycleVol;
    }
    private void setSvCycleNum(ExpertBean expertBean) {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  (expertBean.total
                - expertBean.firstVol ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - expertBean.finalRetainVol - EmtConstant.dep ) / expertBean.cycleVol;
        if(cycle <= 0){
            cycle = 1;
        }
        if (expertBean.firstVol != 0) {
            cycle = cycle + 1;
        }
        expertBean.cycle = cycle;
        svCycleNumTv.setText(String.valueOf(expertBean.cycle));
    }
    private void setSvTotal(ExpertBean expertBean) {
        int total = expertBean.cycle * expertBean.cycleVol + expertBean.firstVol +
                expertBean.finalRetainVol + EmtConstant.dep;
        svTotalVolTv.setText(String.valueOf(total));
        expertBean.total = total;
    }
    private void setSvSupplyTotal(ExpertBean expertBean) {
        int total = expertBean.baseSupplyVol * expertBean.baseSupplyCycle.size()
                + expertBean.osmSupplyVol * expertBean.osmSupplyCycle.size() + EmtConstant.dep + expertBean.finalRetainVol;
        svTotalVolTv.setText(String.valueOf(total));
        expertBean.total = total;
    }

    @Override
    protected void onDestroy() {
        RxBus.get().unRegister(this);
//        mCompositeDisposable.clear();
        MyApplication.treatmentRunning = false;//页面关闭时候，说明治疗结束了
        compositeDisposable.dispose();
        compositeDisposable.clear();
        if (treatmentAlarmDialog != null && treatmentAlarmDialog.isShowing()) {
            treatmentAlarmDialog.dismiss();
        }
        if (soundCompositeDisposable != null) {
            soundCompositeDisposable.clear();
        }
        if (taskCompositeDisposable != null) {
            taskCompositeDisposable.clear();
        }
        if (reportCompositeDisposable != null) {
            reportCompositeDisposable.dispose();
            reportCompositeDisposable.clear();
        }
        if (downTimer != null) {
            downTimer.cancel();
        }
        super.onDestroy();
    }

    private void initMode() {
        // 多模式
        switch (MyApplication.apdMode) {
            case 1:
                maxCycle = ipdBean.cycle;
//                maxCycle = ipdBean.cycle;
                break;
            case 2:
                maxCycle = tpdBean.cycle;
                break;
            case 3:
                maxCycle = ccpdBean.cycle;
                break;
            case 4:
                if (aapdBean.finalVol == 0) {
                    maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6;
                } else {
                    maxCycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6 + 1;
                }
                break;
            case 7:
                maxCycle = kidBean.cycle;
                break;
            case 8:
                if (expertBean.cycleMyself) {
                    maxCycle = Collections.max(expertBean.baseSupplyCycle)
                            >Collections.max(expertBean.osmSupplyCycle) ? Collections.max(expertBean.baseSupplyCycle) :
                            Collections.max(expertBean.osmSupplyCycle);
                } else {
                    maxCycle = expertBean.cycle;
                }
                break;
        }
        if (pdData == null) {
            pdData = MyApplication.pdData;
        }
        pdData.setRxCycle(maxCycle);
        tvMaxCycle.setText(String.valueOf(maxCycle));
        if (mFragment3.getActivity() != null) {
            mFragment3.setMaxCycle(maxCycle);
        } else {
            if (detailedBean == null) {
                detailedBean = new DetailedBean();
            }
            detailedBean.setMaxCycle(maxCycle);
        }
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
                prescript.totalVolume = ipdBean.peritonealDialysisFluidTotal;//处方总灌注量
                prescript.cycle = ipdBean.cycle;//循环周期数
                prescript.firstPerfuseVolume = ipdBean.firstPerfusionVolume;//首次灌注量
                prescript.cyclePerfuseVolume = ipdBean.perCyclePerfusionVolume;//循环周期灌注量
                prescript.lastRetainVolume = ipdBean.abdomenRetainingVolumeLastTime;//上次最末留腹量
//        prescript.lastRetainVolume = 100;
                prescript.finalRetainVolume = ipdBean.abdomenRetainingVolumeFinally;//末次留腹量
                prescript.retainTime = ipdBean.abdomenRetainingTime;//60  //留腹时间
                prescript.ultraFiltrationVolume = ipdBean.ultrafiltrationVolume;//预估超滤量
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
        drain.finalDrainEmptyWaitTime = drainParameterBean.drainWarnTimeInterval;//最末引流排空等待时间 30
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
        sendCommandInterval(command,800);
    }

    private void reviseCycle() {
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
//                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
//                if (aapdFragment.getActivity() != null) {
//                    aapdFragment.setBackground(aapdBean, currCycle);
//                }
                if (aapdBean == null) {
                    aapdBean = PdproHelper.getInstance().aapdBean();
                }
                setAapdBackground(aapdBean, currCycle);
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
//                ExpertBean expertBean = PdproHelper.getInstance().expertBean();
                if (expertBean.cycleMyself) {
                    if (maxCycle >= cycle) {
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
        try {
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
//                AapdBean aapdBean = PdproHelper.getInstance().aapdBean();
                    //设置处方
                    prescript.totalVolume = aapdBean.total;//处方总灌注量
//                prescript.apdmodify = aapdBean.a1;
                    prescript.cycle = aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4 + aapdBean.c5 + aapdBean.c6 - currCycle;//循环周期数
                    prescript.firstPerfuseVolume = 0;//首次灌注量
//                if (currCycle <= aapdBean.c1) {
//                    prescript.cyclePerfuseVolume = aapdBean.p1;//循环周期灌注量
//                    prescript.retainTime = aapdBean.r1;
//                } else
                    // 1 c1=3 变p2
                    if (currCycle <= aapdBean.c1) {
                        prescript.cyclePerfuseVolume = aapdBean.p2;//循环周期灌注量
                        prescript.retainTime = aapdBean.r2;
                    } else if (currCycle <= aapdBean.c1 + aapdBean.c2) {
                        prescript.cyclePerfuseVolume = aapdBean.p3;//循环周期灌注量
                        prescript.retainTime = aapdBean.r3;
                    } else if (currCycle <= aapdBean.c1 + aapdBean.c2 + aapdBean.c3) {
                        prescript.cyclePerfuseVolume = aapdBean.p4;//循环周期灌注量
                        prescript.retainTime = aapdBean.r4;
                    } else if (currCycle <= aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c4) {
                        prescript.cyclePerfuseVolume = aapdBean.p5;//循环周期灌注量
                        prescript.retainTime = aapdBean.r5;
                    } else if (currCycle <= aapdBean.c1 + aapdBean.c2 + aapdBean.c3 + aapdBean.c5) {
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
                                > Collections.max(expertBean.osmSupplyCycle) ? Collections.max(expertBean.baseSupplyCycle) - currCycle :
                                Collections.max(expertBean.osmSupplyCycle) - currCycle;
                        prescript.cyclePerfuseVolume = expertBean.baseSupplyCycle.contains(currCycle + 1)
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
            drain.finalDrainEmptyWaitTime = drainParameterBean.drainWarnTimeInterval;//最末引流排空等待时间 30
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
            sendCommandInterval(sendData, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void revise() {
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, eniity);
        try {
            // 多模式
            switch (MyApplication.apdMode) {
                case 1:
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, ipdBean);
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
            drainParameterBean.drainTimeInterval = PdproHelper.getInstance().getDrainParameterBean().drainTimeInterval;
            drainParameterBean.drainThresholdValue = PdproHelper.getInstance().getDrainParameterBean().drainThresholdValue;

            supplyParameterBean.supplyTimeInterval = PdproHelper.getInstance().getSupplyParameterBean().supplyTimeInterval;
            supplyParameterBean.supplyThresholdValue = PdproHelper.getInstance().getSupplyParameterBean().supplyThresholdValue;

            perfusionParameterBean.perfThresholdValue = PdproHelper.getInstance().getPerfusionParameterBean().perfThresholdValue;
            perfusionParameterBean.perfTimeInterval = PdproHelper.getInstance().getPerfusionParameterBean().perfTimeInterval;
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRAIN_PARAMETER, drainParameterBean);
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.SUPPLY_PARAMETER, supplyParameterBean);
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.RETAIN_PARAM, retainParamBean);
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.PERFUSION_PARAMETER, perfusionParameterBean);
            Log.e("灌注xiu===","rate:"+PdproHelper.getInstance().getPerfusionParameterBean().perfTimeInterval
                    +",value:"+PdproHelper.getInstance().getPerfusionParameterBean().perfThresholdValue);
            initMode();
            reviseCycle();
            toastMessage("修改成功");
        } catch (Exception e) {
            Log.e("修改处方",e.getLocalizedMessage());
        }
    }

    private void lock() {
        viewDataLayout.setEnabled(false);
        finishTreatment.setEnabled(false);
        pauseLayout.setEnabled(false);
        skipLayout.setEnabled(false);
        btnParam.setEnabled(false);
        btnRx.setEnabled(false);
    }

    private void unlock() {
        viewDataLayout.setEnabled(true);
        finishTreatment.setEnabled(true);
        pauseLayout.setEnabled(true);
        skipLayout.setEnabled(true);
        btnParam.setEnabled(true);
        btnRx.setEnabled(true);
    }

    private void click() {
        totalVolTv.setText(String.valueOf(ipdBean.peritonealDialysisFluidTotal));//腹透液总量
        cycleVolTv.setText(String.valueOf(ipdBean.perCyclePerfusionVolume));//每周期灌注量
        cycleNumTv.setText(String.valueOf(ipdBean.cycle));//循环治疗周期数
        abdTimeTv.setText(String.valueOf(ipdBean.abdomenRetainingTime));//留腹时间
        finalVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeFinally));//末次留腹量
        lastVolTv.setText(String.valueOf(ipdBean.abdomenRetainingVolumeLastTime));//上次留腹量
        firstVolTv.setText(String.valueOf(ipdBean.firstPerfusionVolume));//首次灌注量
        ultVolTv.setText(String.valueOf(ipdBean.ultrafiltrationVolume));//预估超滤量
        isFinalSwitch.setOn(ipdBean.isFinalSupply);
        isFinalSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                ipdBean.isFinalSupply = isOn;
                isFinalTv.setText(ipdBean.isFinalSupply?"开":"关");
            }
        });
        isFinalTv.setText(ipdBean.isFinalSupply?"开":"关");
        totalVolLl.setOnClickListener(v -> {
            alertNumberDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL, EmtConstant.totalVolMin, EmtConstant.totalVolMax);
        });
        cycleVolLl.setOnClickListener(v -> {
            alertNumberDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME, EmtConstant.cycleVolMin
                    ,ipdBean.firstPerfusionVolume == 0 ? PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue: ipdBean.firstPerfusionVolume);

        });
        cycleNumLl.setOnClickListener(v -> {
            alertNumberDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES, EmtConstant.cycleNumMin, EmtConstant.cycleNumMax);
        });
        abdTimeLl.setOnClickListener(v -> {
            alertNumberDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME, EmtConstant.abdTimeMin, EmtConstant.abdTimeMax);

        });
        finalVolLl.setOnClickListener(v -> {
            alertNumberDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY, EmtConstant.finalVolMin, PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        lastVolLl.setOnClickListener(v -> {
            alertNumberDialog(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME, EmtConstant.lastAbdMin, EmtConstant.lastAbdMax);
        });
    }

    private void isHospital(boolean isHospital) {
        if (MyApplication.apdMode == 1) {
            totalVolLl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            cycleVolLl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            cycleNumLl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            abdTimeLl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            finalVolLl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            ultVolLl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            finalSupplyLl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);

            totalVolLl.setEnabled(isHospital);
            cycleVolLl.setEnabled(isHospital);
            cycleNumLl.setEnabled(isHospital);
            abdTimeLl.setEnabled(isHospital);
            finalVolLl.setEnabled(isHospital);
            isFinalSwitch.setEnabled(isHospital);
            ultVolLl.setEnabled(isHospital);
            finalSupplyLl.setEnabled(isHospital);
            lastVolLl.setEnabled(false);
            firstVolLl.setEnabled(false);
        } else if (MyApplication.apdMode == 4) {

            per_01_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            per_02_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            per_03_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            per_04_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);

            apTotalVolRl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            bag_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            apCheckboxLl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            retain_01_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            retain_02_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            retain_03_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            retain_04_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);

            cycle_01_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            cycle_02_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            cycle_03_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            cycle_04_rl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);

            apFinalRetainVolRl.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);

            rg1.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            rg2.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            rg3.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);
            rg4.setBackgroundResource(isHospital ? R.drawable.pa7 : R.drawable.gray_shape);

            per_01_rl.setEnabled(isHospital);
            per_02_rl.setEnabled(isHospital);
            per_03_rl.setEnabled(isHospital);
            per_04_rl.setEnabled(isHospital);

            bag_rl.setEnabled(isHospital);
            apTotalVolRl.setEnabled(isHospital);
            retain_01_rl.setEnabled(isHospital);
            retain_02_rl.setEnabled(isHospital);
            retain_03_rl.setEnabled(isHospital);
            retain_04_rl.setEnabled(isHospital);

            cycle_01_rl.setEnabled(isHospital);
            cycle_02_rl.setEnabled(isHospital);
            cycle_03_rl.setEnabled(isHospital);
            cycle_04_rl.setEnabled(isHospital);

            apFinalSupplyCheck.setEnabled(isHospital);

//            rg1.setEnabled(isHospital);
//            rg2.setEnabled(isHospital);
//            rg3.setEnabled(isHospital);
//            rg4.setEnabled(isHospital);
            rb2c1.setEnabled(isHospital);
            rb2c2.setEnabled(isHospital);
            rb3c1.setEnabled(isHospital);
            rb3c2.setEnabled(isHospital);
            rb4c1.setEnabled(isHospital);
            rb4c2.setEnabled(isHospital);

            apFinalRetainVolRl.setEnabled(isHospital);
        }

        reviseConfirm.setVisibility(isHospital? View.VISIBLE:View.INVISIBLE);
    }

    private MsgAdapter msgAdapter;
    private List<MsgBean> msgBeanList;
    private void initRecyclerview() {
        if (msgAdapter == null) {
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            if (msgBeanList == null) {
                msgBeanList = new ArrayList<>();
            }
            msgAdapter = new MsgAdapter(msgBeanList);
//            recyclerview.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
//            //添加Android自带的分割线
//            recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerview.setAdapter(msgAdapter);
            msgAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.btnPause) {
                    msgBeanList.remove(position);
                    msgAdapter.removeData(position);
                    refreshPauseOrResume();
                } else if (view.getId() == R.id.btnDestroy) {
                    finishTreatmentDialog(1);
                } else if (view.getId() == R.id.btnClear) {
                    msgBeanList.remove(position);
                    msgAdapter.removeData(position);
                }
            });
        } else {
            msgAdapter.notifyDataSetChanged();
        }
    }

    private void alertNumberDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(this, type, min, max);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL: //腹透液总量
                            totalVolTv.setText(result);
                            ipdBean.peritonealDialysisFluidTotal = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME: //每周期灌注量
                            cycleVolTv.setText(result);
                            ipdBean.perCyclePerfusionVolume = Integer.parseInt(result);
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
                            ipdBean.cycle = Integer.parseInt(result);
//                            setTotal();
                            setCyclePre();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME: //留腹时间
                            abdTimeTv.setText(result);
                            ipdBean.abdomenRetainingTime = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY: //末次留腹量
                            finalVolTv.setText(result);
                            ipdBean.abdomenRetainingVolumeFinally = Integer.parseInt(result);
                            setCycleNum();
//                            setTotal();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME: //上次留腹量
                            lastVolTv.setText(result);
                            ipdBean.abdomenRetainingVolumeLastTime = Integer.parseInt(result);
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
//                            firstVolTv.setText(result);
//                            ipdBean.firstPerfusionVolume = Integer.parseInt(result);
//                            setTotal();
                            setCycleNum();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME: //预估超滤量
                            ultVolTv.setText(result);
                            ipdBean.ultrafiltrationVolume = Integer.parseInt(result);
                            setCycleNum();
                            break;
                    }
                }
            }
        });
    }

    private void setTotal() {
        int total = ipdBean.cycle * ipdBean.perCyclePerfusionVolume + ipdBean.firstPerfusionVolume +
                ipdBean.abdomenRetainingVolumeFinally + EmtConstant.dep;
        totalVolTv.setText(String.valueOf(total));
        ipdBean.peritonealDialysisFluidTotal = total;
    }

    private void setCyclePre() {
        int cycle = ipdBean.firstPerfusionVolume == 0 ? ipdBean.cycle : ipdBean.cycle - 1;
        double total = (ipdBean.peritonealDialysisFluidTotal
                - ipdBean.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - ipdBean.abdomenRetainingVolumeFinally - EmtConstant.dep) / Double.parseDouble(String.valueOf(cycle)) / 50.00;

        int perCyclePerfusionVolume = (int) Math.floor(total);
        int cycleVol = perCyclePerfusionVolume * 50;
        cycleVolTv.setText(String.valueOf(cycleVol));
        ipdBean.perCyclePerfusionVolume = cycleVol;
    }

    private void setCycleNum() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  (ipdBean.peritonealDialysisFluidTotal
                - ipdBean.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.entity.abdomenRetainingVolumeLastTime
                - ipdBean.abdomenRetainingVolumeFinally - EmtConstant.dep ) / ipdBean.perCyclePerfusionVolume;
        if(cycle <= 0){
            cycle = 1;
        }
        if (ipdBean.firstPerfusionVolume != 0) {
            cycle = cycle + 1;
        }
        ipdBean.cycle = cycle;
        cycleNumTv.setText(String.valueOf(ipdBean.cycle));
    }

    private void setAapdCycleNum() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  aapdBean.bagVol / aapdBean.p1;
        if(cycle <= 0){
            cycle = 1;
        }
        aapdBean.c1 = cycle;
        cycle_01_tv.setText(String.valueOf(aapdBean.c1));
    }

    private void setAapdCyclePre() {
        double total = aapdBean.bagVol / Double.parseDouble(String.valueOf(aapdBean.c1)) / 50.00;

        int perCyclePerfusionVolume = (int) Math.floor(total);
        int cycleVol = perCyclePerfusionVolume * 50;
        per_01_tv.setText(String.valueOf(cycleVol));
        aapdBean.p1 = cycleVol;
    }

    private void setAapdTotal() {
        aapdBean.total = aapdBean.bagVol + aapdBean.p2 * aapdBean.c2 + aapdBean.p3 * aapdBean.c3 + aapdBean.p4 * aapdBean.c4
                + aapdBean.p5 * aapdBean.c5 + aapdBean.p6 * aapdBean.c6 + aapdBean.finalVol + EmtConstant.dep;
        apTotalVolTv.setText(String.valueOf(aapdBean.total));
    }


    private double mixCon(double c1, int v1, double c2, int v2) {
        double d = (c1 * v1 + c2 * v2) / (v1 + v2);
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private SoundPool soundPool;
    private int pool;
    /**
     * 重复报警
     */
    private void loopSoundPool() {
        soundPool = new SoundPool(10, AudioManager.STREAM_ALARM, 5);
        soundPool.load(this, R.raw.high_alarm, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                pool = i;
                soundPool.play(i, 100, 100, 1, -1, 1f);
            }
        });
    }

    private void stopSound() {
        if (soundPool != null) {
            soundPool.stop(pool);
        }
    }

}
