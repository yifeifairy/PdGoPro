package com.emt.pdgo.next.ui.activity;


import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandReceiveConfig;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.SelfCheckDeviceStatus;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.data.serial.receive.selfcheck.ReceiveThermostattBean;
import com.emt.pdgo.next.data.serial.receive.selfcheck.ReceiveValveBean;
import com.emt.pdgo.next.data.serial.receive.selfcheck.ReceiveWeightBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.mode.activity.ModeActivity;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.CmdQueueHelper;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.ScreenUtil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;


public class SelfCheckActivity extends BaseActivity implements View.OnTouchListener {

    private String TAG = "SelfCheckActivity";

    //@SuppressLint("NonConstantResourceId")
    @BindView(R.id.root_view)
    RelativeLayout rootView;

    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.tv_mode_progress)
    TextView tvModeProgress;

    @BindView(R.id.layout_progress)
    RelativeLayout layoutProgress;

    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @BindView(R.id.tv_warn_content)
    TextView tvWarnContent;

    @BindView(R.id.btn_back)
    Button btnBack;

    @BindView(R.id.layout_main)
    RelativeLayout layoutMain;
    @BindView(R.id.layout_report_include)
    RelativeLayout layoutReportInclude;
    @BindView(R.id.layout_warn)
    RelativeLayout layoutWarn;
    @BindView(R.id.layout_contact_engineer)
    RelativeLayout layoutContactEngineer;
    @BindView(R.id.rv_report)
    RecyclerView rvReport;


    @BindView(R.id.tv_report_zh)
    TextView tvReportZh;
    @BindView(R.id.tv_report_line)
    TextView tvReportLine;
    @BindView(R.id.tv_report_es)
    TextView tvReportEs;
    @BindView(R.id.layout_report)
    RelativeLayout layoutReport;
    @BindView(R.id.layout_report_title)
    RelativeLayout layoutReportTitle;
    @BindView(R.id.view_report)
    RelativeLayout viewReport;

    @BindView(R.id.iv_alarm_system)
    ImageView ivAlarmSystem;
    @BindView(R.id.tv_alarm_system)
    TextView tvAlarmSystem;
    @BindView(R.id.iv_module_heating)
    ImageView ivModuleHeating;
    @BindView(R.id.tv_module_heating)
    TextView tvModuleHeating;
    @BindView(R.id.iv_module_control)
    ImageView ivModuleControl;
    @BindView(R.id.tv_module_control)
    TextView tvModuleControl;
    @BindView(R.id.iv_module_supply)
    ImageView ivModuleSupply;
    @BindView(R.id.tv_module_supply)
    TextView tvModuleSupply;
    @BindView(R.id.iv_module_perfusion)
    ImageView ivModulePerfusion;
    @BindView(R.id.tv_module_perfusion)
    TextView tvModulePerfusion;
    @BindView(R.id.iv_module_drain)
    ImageView ivModuleDrain;
    @BindView(R.id.tv_module_drain)
    TextView tvModuleDrain;
    @BindView(R.id.iv_module_upper_weight)
    ImageView ivModuleUpperWeight;
    @BindView(R.id.tv_module_upper_weight)
    TextView tvModuleUpperWeight;
    @BindView(R.id.iv_module_lower_weight)
    ImageView ivModuleLowerWeight;
    @BindView(R.id.tv_module_lower_weight)
    TextView tvModuleLowerWeight;

    @BindView(R.id.tvT0)
    TextView tvT0;
    @BindView(R.id.ivT0)
    ImageView ivT0;
    @BindView(R.id.tvT1)
    TextView tvT1;
    @BindView(R.id.ivT1)
    ImageView ivT1;
    @BindView(R.id.tvT2)
    TextView tvT2;
    @BindView(R.id.ivT2)
    ImageView ivT2;
    @BindView(R.id.tvPerfusion)
    TextView tvPerfusion;
    @BindView(R.id.ivPerfusion)
    ImageView ivPerfusion;
    @BindView(R.id.tvRehydration)
    TextView tvRehydration;
    @BindView(R.id.ivRehydration)
    ImageView ivRehydration;
    @BindView(R.id.tvDrainage)
    TextView tvDrainage;
    @BindView(R.id.ivDrainage)
    ImageView ivDrainage;
    @BindView(R.id.tvPressure)
    TextView tvPressure;
    @BindView(R.id.ivPressure)
    ImageView ivPressure;
    @BindView(R.id.tvSafe)
    TextView tvSafe;
    @BindView(R.id.ivSafe)
    ImageView ivSafe;
    @BindView(R.id.tvRehydration2)
    TextView tvRehydration2;
    @BindView(R.id.ivRehydration2)
    ImageView ivRehydration2;

    @BindView(R.id.btn_contact_engineer)
    StateButton btnContactEngineer;
    @BindView(R.id.btn_re_selfcheck)
    StateButton btnReSelfcheck;
    @BindView(R.id.btn_re_selfcheck2)
    StateButton btnReSelfcheck2;

    @BindView(R.id.btn_ok)
    StateButton btnOk;

    @BindView(R.id.tc_time)
    TextClock tcTime;

    private CompositeDisposable mCompositeDisposable;

    private SelfCheckDeviceStatus mSelfCheckDeviceStatus;//自检返回的模块状态


    private boolean isAutoCheck; // 是否自动自检
    private int errTime; // 自检失败次数
    private int maxErrTime = 5;
    private final CountDownTimer countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            isAutoCheck = false;
//            Log.e("倒计时", "时间--"+l);
        }

        @Override
        public void onFinish() {
            if (!selfCheckOK) {
                isAutoCheck = true;
                checkFailContent = "";
                layoutReportInclude.setVisibility(View.GONE);
                layoutMain.setVisibility(View.VISIBLE);
                doSelfCheck();
            }
        }
    };

    /******** 自检通过 *********/
    private boolean selfCheckOK = false;

    private String checkFailContent = "";

    private boolean firstStart = true;

    private int maxCountdown = 100;//倒计时
    private int currCountdown = 0;//倒计时

    private Disposable progressDisposable;

    private CompositeDisposable cmdDisposable;
    private String command_topic;
    private String command;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.REQUEST_INSTALL_PACKAGES",
            "android.permission.MODIFY_PHONE_STATE",
            "android.permission.WRITE_SETTINGS",
            "android.permission.MANAGE_EXTERNAL_STORAGE"};

    private CheckForLongPress1 mCheckForLongPress1;
    private volatile boolean isLongPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_self_check);
        ButterKnife.bind(this);
        RxBus.get().register(this);
//        Log.e("自检","{\"" + "json" + "\"}");
        ActivityManager.getActivityManager().removeAllActivityExceptOne(SelfCheckActivity.class);
        mCompositeDisposable = new CompositeDisposable();
//        doGoCloseTOActivity(TreatmentFragmentActivity.class,"");
        getToken();
    }

    @BindView(R.id.powerIv)
    ImageView powerIv;
    @BindView(R.id.currentPower)
    TextView currentPower;
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {

            MyApplication.chargeFlag = mReceiveDeviceBean.isAcPowerIn;
            MyApplication.batteryLevel = mReceiveDeviceBean.batteryLevel;
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void registerEvents() {
        APIServiceManage.getInstance().postApdCode("Z0000");
        ivLogo.setOnTouchListener(this);
        currentPower.setTextColor(Color.WHITE);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initViewData() {
        MyApplication.sendSelfCheck = false;
        cmdDisposable = new CompositeDisposable();
        mSelfCheckDeviceStatus = new SelfCheckDeviceStatus();
        mSelfCheckDeviceStatus.initModule();
        mCheckForLongPress1 = new CheckForLongPress1();
//        doGoCloseTOActivity(TreatmentFragmentActivity.class,"");
        btnReSelfcheck.setOnClickListener(view -> {
            if (!isAutoCheck) {
                countDownTimer.cancel();
                checkFailContent = "";
                layoutReportInclude.setVisibility(View.GONE);
                layoutMain.setVisibility(View.VISIBLE);
                doSelfCheck();
            }
        });
        runOnUiThread(() -> {
            Log.e("是否医院用户", PdproHelper.getInstance().getUserParameterBean().isHospital + "");
            if (PdproHelper.getInstance().getUserParameterBean().isHospital) {//医院用户删除之前保存的用户信息，每次开机删除
                CacheUtils.getInstance().getACache().remove(PdGoConstConfig.USER_INFO);
            }
            MyApplication.startSerialPortService();
        });

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(SelfCheckActivity.this,
                    "android.permission.MANAGE_EXTERNAL_STORAGE");

            if (permission != PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，申请权限
                ActivityCompat.requestPermissions(SelfCheckActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {

            }
//            if (ActivityCompat.checkSelfPermission(SelfCheckActivity.this,
//                    "android.permission.WRITE_SETTINGS")!= PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(SelfCheckActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        printResolution(SelfCheckActivity.this);

    }

    /**
     * 打印不包括虚拟按键的分辨率、屏幕密度dpi、最小宽度sw
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void printResolution(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        int sw = context.getResources().getConfiguration().smallestScreenWidthDp;
        Log.e(TAG, "屏幕分辨率:" + width + "*" + height + ",density:" + dm.density + ",dpi:" + dm.densityDpi + ",sw:" + sw);
        Log.e(TAG, "系统定制商:" + Build.BRAND + "，硬件制造商:" + Build.MANUFACTURER + ",主板:" + Build.BOARD + ",设备参数:" + Build.DEVICE + ",显示屏参数:" + Build.DISPLAY);
//
//        Log.e(TAG, "MODEL:" + Build.MODEL + "，PRODUCT:" + Build.PRODUCT + ",硬件名称:" + Build.HARDWARE);
        if ((480 == width && 800 == height) || (800 == width && 480 == height)) {

        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.icon_refresh); //获取图片
            int bounds = ScreenUtil.dip2px(this, 40);
            drawable.setBounds(bounds, bounds, bounds, bounds);  //设置图片参数
            btnReSelfcheck.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//            Drawable drawable2 = getResources().getDrawable(R.drawable.icon_subtract2); //获取图片
//
//            drawable2.setBounds(bounds, bounds, bounds, bounds);  //设置图片参数
            btnReSelfcheck2.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }

        DisplayManager mDisplayManager; // 屏幕管理类
        mDisplayManager = (DisplayManager) this
                .getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();

        int screenNum = displays.length;
//        Log.e(TAG, "当前屏幕个数:" + displays.length);
        if (screenNum < 2) { //单个屏幕
            return;
        }
        getDisplayWH(displays);

//        MyPresentation differentDislay = new MyPresentation(this,displays[1]);
//
//        differentDislay.getWindow().setType(
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        differentDislay.show();
//

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getDisplayWH(Display[] displays) {
        Display display = displays[0];  //0表示主屏，1 表示副屏
        Display display1 = displays[1];  //0表示主屏，1 表示副屏

        int width = display.getMode().getPhysicalWidth();
        int height = display.getMode().getPhysicalHeight();
        int width1 = display1.getMode().getPhysicalWidth();
        int height1 = display1.getMode().getPhysicalHeight();

        Log.e(TAG, "主屏的高度x宽度:" + width + " x " + height);

        Log.e(TAG, "副屏的高度x宽度:" + width1 + " x " + height1);


    }


    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.iv_logo, R.id.btn_contact_engineer, R.id.btn_re_selfcheck, R.id.btn_back, R.id.btn_re_selfcheck2, R.id.btn_ok, R.id.iv_saomiao})
    public void onViewClicked(View view) {

        switch (view.getId()) {
//            case R.id.iv_logo:
//                doGoTOActivity(SettingActivity.class);
//                break;
            case R.id.btn_contact_engineer:
                layoutContactEngineer.setVisibility(View.VISIBLE);
                break;
//            case R.id.btn_re_selfcheck:
//                checkFailContent = "";
//                layoutReportInclude.setVisibility(View.GONE);
//                layoutMain.setVisibility(View.VISIBLE);
//                doSelfCheck();
//                break;
            case R.id.btn_back:
                layoutContactEngineer.setVisibility(View.GONE);
                break;
            case R.id.btn_re_selfcheck2:
                checkFailContent = "";
                layoutReportInclude.setVisibility(View.GONE);
                layoutContactEngineer.setVisibility(View.GONE);
                layoutMain.setVisibility(View.VISIBLE);
                doSelfCheck();
                break;
            case R.id.btn_ok:
                if (PdproHelper.getInstance().getUserParameterBean().isHospital) {//医院用户每次都要扫码登录
                    doGoCloseTOActivity(ScanCodeLoginActivity.class,"");
                } else {
                    if (TextUtils.isEmpty(PdproHelper.getInstance().getUserInfoBean().loginPhone)) {//未登录
                        doGoCloseTOActivity(ScanCodeLoginActivity.class,"");
                    } else {
                        doGoCloseTOActivity(PreparationActivity.class,"");
                    }
                }
                break;
            case R.id.iv_saomiao:
//                doGoTOActivity(WeighParameterActivity.class);
//                doGoTOActivity(PowerMonitorDialogActivity.class);
//                testUpload();
//                speak("开始自检");
                break;
        }
    }

    private void initModule() {

        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());

        helper.setBackgroundResourceByAttr(ivDrainage, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivT0, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivT1, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivT2, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivPerfusion, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivRehydration, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivPressure, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivSafe, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivRehydration2, R.attr.custom_attr_self_check_report_icon_right);

        helper.setBackgroundResourceByAttr(ivModulePerfusion, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivModuleDrain, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivModuleSupply, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivModuleLowerWeight, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivModuleUpperWeight, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivModuleControl, R.attr.custom_attr_self_check_report_icon_right);
        helper.setBackgroundResourceByAttr(ivModuleHeating, R.attr.custom_attr_self_check_report_icon_right);

        helper.setTextColorByAttr(tvModulePerfusion, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvModuleDrain, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvModuleSupply, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvModuleLowerWeight, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvModuleUpperWeight, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvModuleControl, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvModuleHeating, R.attr.custom_attr_common_text_color);

        helper.setTextColorByAttr(tvT0, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvT1, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvT2, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvPerfusion, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvRehydration, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvDrainage, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvPressure, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvSafe, R.attr.custom_attr_common_text_color);

        layoutWarn.setVisibility(View.GONE);
        btnContactEngineer.setVisibility(View.GONE);
        btnReSelfcheck.setVisibility(View.GONE);
        btnOk.setVisibility(View.VISIBLE);
        layoutReportInclude.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyByThemeChanged() {

        runOnUiThread(() -> {
            MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
            helper.setBackgroundResourceByAttr(rootView, R.attr.custom_attr_self_check_bg);

            progressBar.setProgressDrawable(helper.getDrawableByAttr(R.attr.custom_attr_user_photo_place_holder));
            helper.setTextColorByAttr(tvMode, R.attr.custom_attr_self_check_progress_text_color);
            helper.setTextColorByAttr(tvModeProgress, R.attr.custom_attr_self_check_progress_text_color);

            helper.setBackgroundResourceByAttr(viewReport, R.attr.custom_attr_self_check_bg);
            helper.setBackgroundResourceByAttr(layoutReport, R.attr.custom_attr_self_check_report_bg);

            helper.setTextColorByAttr(tvReportZh, R.attr.custom_attr_self_check_report_title_color);
            helper.setTextColorByAttr(tvReportLine, R.attr.custom_attr_self_check_report_title_line_color);

            moduleViewThemeChanged(ivAlarmSystem, tvAlarmSystem, mSelfCheckDeviceStatus.alarmSystemOK);
            moduleViewThemeChanged(ivModuleHeating, tvModuleHeating, mSelfCheckDeviceStatus.moduleHeatingOK);
            moduleViewThemeChanged(ivModuleControl, tvModuleControl, mSelfCheckDeviceStatus.moduleControlOK);
            moduleViewThemeChanged(ivModuleDrain, tvModuleDrain, mSelfCheckDeviceStatus.moduleDrainOK && mSelfCheckDeviceStatus.moduleNegOK);
            moduleViewThemeChanged(ivModulePerfusion, tvModulePerfusion, mSelfCheckDeviceStatus.modulePerfusionOK);
            moduleViewThemeChanged(ivModuleSupply, tvModuleSupply, mSelfCheckDeviceStatus.moduleSupplyOK);
            moduleViewThemeChanged(ivModuleUpperWeight, tvModuleUpperWeight, mSelfCheckDeviceStatus.moduleUpperWeightOK);
            moduleViewThemeChanged(ivModuleLowerWeight, tvModuleLowerWeight, mSelfCheckDeviceStatus.moduleLowerWeightOK);

            moduleViewThemeChanged(ivT0, tvT0, mSelfCheckDeviceStatus.t0);
            moduleViewThemeChanged(ivT1, tvT1, mSelfCheckDeviceStatus.t1);
            moduleViewThemeChanged(ivT2, tvT2, mSelfCheckDeviceStatus.t2);
            moduleViewThemeChanged(ivPerfusion, tvPerfusion, mSelfCheckDeviceStatus.modulePerfusionOK);
            moduleViewThemeChanged(ivRehydration, tvRehydration, mSelfCheckDeviceStatus.moduleSupplyOK);
            moduleViewThemeChanged(ivDrainage, tvDrainage, mSelfCheckDeviceStatus.moduleDrainOK);
            moduleViewThemeChanged(ivPressure, tvPressure, mSelfCheckDeviceStatus.moduleNegOK);
            moduleViewThemeChanged(ivSafe, tvSafe, mSelfCheckDeviceStatus.moduleSafeOK);
            moduleViewThemeChanged(ivRehydration2, tvRehydration2, mSelfCheckDeviceStatus.moduleSafeOK);

        });

    }

    public void moduleViewThemeChanged(ImageView iv, TextView tv, boolean flag) {

        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());

        if (flag) {
            helper.setBackgroundResourceByAttr(iv, R.attr.custom_attr_self_check_report_icon_right);
            helper.setTextColorByAttr(tv, R.attr.custom_attr_common_text_color);
        } else {
            iv.setBackgroundResource(R.drawable.icon_error);
            tv.setTextColor(getResources().getColor(R.color.text_warning));
        }

    }


    private void doSelfCheck() {//发送自检
        errTime += 1;
        Log.e(TAG, " doSelfCheck 发送自检：" + "失败次数--" + errTime);
        selfCheckOK = false;
        mSelfCheckDeviceStatus.initModule();
        if (errTime < maxErrTime ) {
            refreshSelfCheckStatus(1);
            sendCmd(CommandDataHelper.getInstance().selfCheckCmdJson(0, 0));
        } else {
            showTipsCommonDialog("自检失败次数过多，请联系工程师");
            btnBack.setVisibility(View.GONE);
            layoutContactEngineer.setVisibility(View.VISIBLE);
        }
    }

    private void refreshSelfCheckStatus(int status) {

        SelfCheckActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                switch (status) {
                    case 0:
                        tvStatus.setText("正在通信中....");
                        tvStatus.setTextColor(getResources().getColor(R.color.white));
                        progressBar.setVisibility(View.GONE);
                        layoutProgress.setVisibility(View.GONE);
                        break;
                    case 1://阀
                        tvStatus.setText("正在自检中....");
                        tvStatus.setTextColor(getResources().getColor(R.color.white));
                        tvMode.setText("正在检测动力系统...");
                        tvModeProgress.setText("0%");
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.VISIBLE);
                        layoutProgress.setVisibility(View.VISIBLE);
                        startLoopProgress();
                        break;
                    case 2://秤
                        tvStatus.setText("正在自检中....");
                        tvStatus.setTextColor(getResources().getColor(R.color.white));
                        tvMode.setText("正在检测平衡系统...");
                        tvModeProgress.setText("0%");
                        layoutProgress.setVisibility(View.VISIBLE);
                        startLoopProgress();
                        break;
                    case 3://温控
                        tvStatus.setText("正在自检中....");
                        tvStatus.setTextColor(getResources().getColor(R.color.white));
                        tvMode.setText("正在检测温控系统...");
                        tvModeProgress.setText("0%");
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.VISIBLE);
                        layoutProgress.setVisibility(View.VISIBLE);
                        startLoopProgress();
                        break;
                }

            }
        });

    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
//            Log.e(TAG, " runnable: " + MyApplication.currCmd);
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            if (MyApplication.currCmd.equals(CommandSendConfig.METHOD_STATUS_ON)) {
                sendCmd(CommandDataHelper.getInstance().setStatusOn());
                handler.postDelayed(this, 3000);
                Log.e("自检界面","currCmd---"+CommandSendConfig.METHOD_STATUS_ON);
            } else if (MyApplication.currCmd.equals(CommandSendConfig.METHOD_SELFCHECK_START)) {
                sendCmd(CommandDataHelper.getInstance().selfCheckCmdJson(0, 0));
                handler.postDelayed(this, 3000);
//                APIServiceManage.getInstance().postApdCode("Z1000");
                Log.e("自检界面","currCmd---"+CommandSendConfig.METHOD_SELFCHECK_START);
            }
            Log.e("自检界面","currCmd---"+MyApplication.currCmd);
        }
    };

    /**
     * 打开串口成功，然后进行握手通信
     *
     * @param mData
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_MAIN_BOARD_OK)
    public void receivemMainBoardOk(String mData) {
        Log.e(TAG, " 打开串口成功 ");
        MyApplication.currCmd = CommandSendConfig.METHOD_STATUS_ON;
        sendCmd(CommandDataHelper.getInstance().setStatusOn());
//        if (getThemeTag() == -1 && PdproHelper.getInstance().getUserParameterBean().isNight) {
//            sendCommandInterval(CommandDataHelper.getInstance().LedOpen("all",false,1), 500);
//        }
        handler.postDelayed(runnable, 3000);
    }

    /**
     * 获取设备信息
     *
     * @param mSerialJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_DEVICE_INFO)
    public void receiveDeviceInfo(String mSerialJson) {
        ReceiveDeviceBean mBean = JsonHelper.jsonToClass(mSerialJson, ReceiveDeviceBean.class);
//        Log.e("自检界面", "   --->接收设备信息：" + mSerialJson);
    }

    /**
     * 执行指令成功
     *
     * @param topic
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
    public void receiveCmdResultOk(String topic) {

        if (topic.contains(CommandSendConfig.METHOD_SELFCHECK_START)) {//自检
            speak("设备自检");
            refreshSelfCheckStatus(1);
            MyApplication.currCmd = "";
            runOnUiThread(()-> {
                APIServiceManage.getInstance().postApdCode("Z1000");
//                saveFaultCodeLocal("设备开机");
            });
            handler.removeCallbacks(runnable);
        } else if (topic.contains(CommandSendConfig.METHOD_STATUS_ON)) {
//            APIServiceManage.getInstance().postApdCode("Z0000");
//            sendCmd(CommandDataHelper.getInstance().selfCheckCmdJson(0, 0));
            MyApplication.currCmd = CommandSendConfig.METHOD_SELFCHECK_START;
            runOnUiThread(()-> {
//                saveFaultCodeLocal("设备开机");
            });
        } else if (topic.contains(CommandSendConfig.METHOD_STATUS_OFF)) {

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
        if (topic.contains(CommandSendConfig.METHOD_SELFCHECK_START)) {//自检
            if (bean.result.msg.equals("pipecart instatll task is running")) {
                sendCmd(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_FINISH));//调用装管路
            } else if (bean.result.msg.equals("auto rinse task is running")) {
                sendCmd(CommandDataHelper.getInstance().abortAutoRinseCmd());
            }
            MyApplication.currCmd = "";
            handler.removeCallbacks(runnable);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stopLoopProgress();
                    speak("自检异常");
                    // 自检未通过时 显示重新自检按钮
                    setWarnContent();
                    errTime += 1;
                    if (errTime < maxErrTime) {
                        countDownTimer.start();
                    } else {
                        btnBack.setVisibility(View.GONE);
                        countDownTimer.cancel();
                        layoutContactEngineer.setVisibility(View.VISIBLE);
                    }
                    btnContactEngineer.setVisibility(View.VISIBLE);
                    btnReSelfcheck.setVisibility(View.VISIBLE);
                    btnOk.setVisibility(View.GONE);
                    layoutReportInclude.setVisibility(View.VISIBLE);
                }
            });

        } else {

        }
    }

    /**
     * 打开串口成功，然后进行握手通信
     *
     * @param mData
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_FIRST_WEIGH_TARE_ALL_OK)
    public void receivemFirstWeighTare(String mData) {
        Log.e(TAG, "  打开串口成功，然后进行握手通信：");
//        refreshSelfCheckStatus(1);
        selfCheckOK = false;
//        String msg = "{\"request\":{\"method\":\"selfcheck/start\",\"params\":{\"upper\":600,\"lower\":600}},\"sign\":\"c726651f09fde161da47c41339943a14\"}";
        sendCmd(CommandDataHelper.getInstance().selfCheckCmdJson(0, 0));
//        MyApplication.currCmd = "";

//        sendCommandInterval(CommandSendConfig.GETSTATUS_OFF, 1000);
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
     * 主板错误
     *
     * @param mDataError
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_MAIN_BOARD_ERROR)
    public void receivemMainBoardError(String mDataError) {
        stopLoopSubscription();
        selfCheckOK = false;
        String errorStr = "";
        Log.e(TAG, "  handleMainBoardError：" + mDataError);
//        if (mDataError.equals(MainBoardService.OpenSerialError)) {
//            errorStr = "控制板连接失败";
//        } else if (mDataError.equals(MainBoardService.ReadDataError)) {
//            errorStr = "主板串口读取数据失败";
//        } else if (mDataError.equals(MainBoardService.SendDataError)) {
//            errorStr = "主板串口发送数据失败";
//        } else if (mDataError.equals(MainBoardService.SerialClosed)) {
//            errorStr = "主板串口已关闭";
//        } else if (mDataError.equals(MainBoardService.ReadDataTimeOut)) {
//            errorStr = "控制板故障，连接失败";
//        }
        tvStatus.setText(errorStr);
        tvStatus.setTextColor(getResources().getColor(R.color.text_warning));
//        layoutReport.setVisibility(View.VISIBLE);
    }

    /**
     * 握手成功
     *
     * @param value
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_COMMAND_HELLO)
    public void receiveHello(String value) {
        Log.e(TAG, "  receiveHello --->握手成功：" + value);
        MyApplication.hasHello = true;
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_SELF_CHECK_VALVE)
    public void receiveSelfCheckValveData(String receiveData) {//阀自检

        Log.e(TAG, "  收到阀自检结果处理：" + receiveData);
//        if (receiveData.equals("{}")) {
//            sendCommandInterval(CommandDataHelper.getInstance().valueOpen(false), 200);
//        }
//        refreshSelfCheckStatus(2);
        ReceiveValveBean mBean = JsonHelper.jsonToClass(receiveData, ReceiveValveBean.class);
        if (CommandReceiveConfig.MSG_OK.equals(mBean.perfuse)) {//灌注阀正常
            mSelfCheckDeviceStatus.modulePerfusionOK = true;
        } else {
            APIServiceManage.getInstance().postApdCode("Z1015");
            runOnUiThread(()->{
//                saveFaultCodeLocal("灌注阀异常");
            });
//            sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
        }
        if (CommandReceiveConfig.MSG_OK.equals(mBean.supply)) {//补液阀正常
            mSelfCheckDeviceStatus.moduleSupplyOK = true;

        }else {
            APIServiceManage.getInstance().postApdCode("Z1014");
            runOnUiThread(()->{
//                saveFaultCodeLocal("补液阀异常");
            });
//            sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
        }
        if (CommandReceiveConfig.MSG_OK.equals(mBean.supply2)) {//末袋补液阀正常
            mSelfCheckDeviceStatus.moduleSupply2OK = true;
        }else {
//            sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
            runOnUiThread(()->{
//                saveFaultCodeLocal("末袋补液阀异常");
            });
        }
        if (CommandReceiveConfig.MSG_OK.equals(mBean.safe)) {//安全阀正常
            mSelfCheckDeviceStatus.moduleSafeOK = true;
        }else {
//            sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
            runOnUiThread(()->{
//                saveFaultCodeLocal("安全阀异常");
            });
        }
        if (CommandReceiveConfig.MSG_OK.equals(mBean.drain)) {//引流阀正常
            mSelfCheckDeviceStatus.moduleDrainOK = true;
        }else {
            APIServiceManage.getInstance().postApdCode("Z1016");
            runOnUiThread(()->{
//                saveFaultCodeLocal("引流阀异常");
            });
//            sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
        }
        if (CommandReceiveConfig.MSG_OK.equals(mBean.vaccum)) {//负压引流阀正常
            mSelfCheckDeviceStatus.moduleNegOK = true;
        }else {
            runOnUiThread(()->{
//                saveFaultCodeLocal("负压引流阀异常");
            });
//            sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
        }

        //阀自检完成
        refreshSelfCheckStatus(2);

    }

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_SELF_CHECK_WEIGHT)
    public void receiveSelfCheckWeightData(String receiveData) {//称自检
        ReceiveWeightBean mBean = JsonHelper.jsonToClass(receiveData, ReceiveWeightBean.class);

        Log.e(TAG, "  收到称自检结果：");
        if (CommandReceiveConfig.MSG_OK.equals(mBean.upper)) {//上位秤自检通过
            mSelfCheckDeviceStatus.moduleUpperWeightOK = true;
        } else {
            APIServiceManage.getInstance().postApdCode("Z1017");
            runOnUiThread(()->{
//                saveFaultCodeLocal("上位秤异常");
            });
//            sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
        }
        if (CommandReceiveConfig.MSG_OK.equals(mBean.lower)) {//下位秤自检通过
            mSelfCheckDeviceStatus.moduleLowerWeightOK = true;
        } else {
            APIServiceManage.getInstance().postApdCode("Z1018");
//            sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
            runOnUiThread(()->{
//                saveFaultCodeLocal("下位秤异常");
            });
        }

        layoutMain.postDelayed(new Runnable() {
            @Override
            public void run() {
                //秤自检完成
                refreshSelfCheckStatus(3);
            }
        }, 1500);

    }

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_SELF_CHECK_THERMOSTAT)
    public void receiveSelfCheckThermostatData(String receiveData) {//温控自检
        ReceiveThermostattBean mBean = JsonHelper.jsonToClass(receiveData, ReceiveThermostattBean.class);
        Log.e(TAG, "  收到温控自检结果");

        layoutMain.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "  处理---温控自检结果");

//                if (CommandReceiveConfig.MSG_OK.equals(mBean.T0) && CommandReceiveConfig.MSG_OK.equals(mBean.T1) && CommandReceiveConfig.MSG_OK.equals(mBean.T2)) {//自检通过
//                    mSelfCheckDeviceStatus.moduleControlOK = true;
//                }
//                else {
//                    if (!mBean.T0.equals("ok")) {
//                        mSelfCheckDeviceStatus.t0 = false;
//                    } else if (! mBean.T1.equals("ok")) {
//                        mSelfCheckDeviceStatus.t1 = false;
//                    } else {
//                        mSelfCheckDeviceStatus.t2 = false;
//                    }
//                }
                if ("ok".equals(mBean.T0)) {
//                    postApdCode("Z1012");
                    mSelfCheckDeviceStatus.t0 = true;
                }else {
//                    sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
                    APIServiceManage.getInstance().postApdCode("Z1012");
//                    saveFaultCodeLocal("t0传感器异常");
                }
                if ("ok".equals(mBean.T1)) {
                    mSelfCheckDeviceStatus.t1 = true;
                }else {
//                    sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
//                    saveFaultCodeLocal("t1传感器异常");
                }
                if ("ok".equals(mBean.T2)) {
                    mSelfCheckDeviceStatus.t2 = true;
                }else {
//                    sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
//                    saveFaultCodeLocal("t2传感器异常");
                }
                if ("no".equals(mBean.overtemp)) {//
                    mSelfCheckDeviceStatus.moduleHeatingOK = true;
                }else {
//                    sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
//                    saveFaultCodeLocal("加热异常");
                }

                stopLoopSubscription();
                selfCheckOK = mSelfCheckDeviceStatus.selfCheckResult();
                if (selfCheckOK) {
                    APIServiceManage.getInstance().postApdCode("Z1010");
                    speak("自检通过");

                    checkOk();
                    if (MyApplication.getInstance().usbActivityIsRunning) {
                        ActivityManager.getActivityManager().removeActivity(USBDiskActivity.class);
                    }
                    countDownTimer.cancel();
//                    MyApplication.isDpr = true;
                    doGoCloseTOActivity(ModeActivity.class, "");
//                    }
                } else {
                    String signup = "";
                    speak("自检失败");
//                    sendCmd(CommandDataHelper.getInstance().customCmd("selfcheck/stop"));
                    // 自检未通过时 显示重新自检按钮
                    setWarnContent();
                    btnReSelfcheck.setVisibility(View.VISIBLE);
                    btnOk.setVisibility(View.GONE);
                    errTime +=1;
                    if (errTime < maxErrTime) {
                        countDownTimer.start();
                    } else {
                        countDownTimer.cancel();
                        layoutContactEngineer.setVisibility(View.VISIBLE);
                    }
                    layoutReportInclude.setVisibility(View.VISIBLE);
//                    APIServiceManage.getInstance().postApdCode("Z1010");
//                    saveFaultCodeLocal("自检失败");
                }

            }
        }, 2000);

    }

    private void checkOk() {
        initModule();
    }

    private void setWarnContent() {

//        if (!mSelfCheckDeviceStatus.modulePerfusionOK) {//灌注阀故障
//            ivModulePerfusion.setBackgroundResource(R.drawable.icon_error);
//            tvModulePerfusion.setTextColor(getResources().getColor(R.color.text_warning));
//            checkFailContent = "灌注阀故障、";
//        }
        if (!mSelfCheckDeviceStatus.modulePerfusionOK) {//灌注阀故障
            ivPerfusion.setBackgroundResource(R.drawable.icon_error);
            tvPerfusion.setTextColor(getResources().getColor(R.color.text_warning));
            checkFailContent = "灌注阀故障、";
        }
//        if (!mSelfCheckDeviceStatus.moduleDrainOK
//                || !mSelfCheckDeviceStatus.moduleNegOK) {//引流阀故障
//            ivModuleDrain.setBackgroundResource(R.drawable.icon_error);
//            tvModuleDrain.setTextColor(getResources().getColor(R.color.text_warning));
//
//            if (!mSelfCheckDeviceStatus.moduleDrainOK) {
//                checkFailContent += "引流阀故障、";
//            }
//            if (!mSelfCheckDeviceStatus.moduleNegOK) {
//                checkFailContent += "负压引流阀故障、";
//            }
//        }
        if (!mSelfCheckDeviceStatus.moduleDrainOK) {
            ivDrainage.setBackgroundResource(R.drawable.icon_error);
            tvDrainage.setTextColor(getResources().getColor(R.color.text_warning));
            checkFailContent += "引流阀故障、";
        }
        if (!mSelfCheckDeviceStatus.moduleNegOK) {
            ivPressure.setBackgroundResource(R.drawable.icon_error);
            tvPressure.setTextColor(getResources().getColor(R.color.text_warning));
            checkFailContent += "负压引流阀故障、";
        }
//        if (!mSelfCheckDeviceStatus.moduleSupplyOK) {//补液阀故障
//            ivModuleSupply.setBackgroundResource(R.drawable.icon_error);
//            tvModuleSupply.setTextColor(getResources().getColor(R.color.text_warning));
//            checkFailContent += "补液阀故障、";
//        }
        if (!mSelfCheckDeviceStatus.moduleSupplyOK) {//补液阀故障
            ivRehydration.setBackgroundResource(R.drawable.icon_error);
            tvRehydration.setTextColor(getResources().getColor(R.color.text_warning));
            checkFailContent += "补液阀故障、";
        }
        if (!mSelfCheckDeviceStatus.moduleSupply2OK) {//末袋补液阀故障
            ivRehydration2.setBackgroundResource(R.drawable.icon_error);
            tvRehydration2.setTextColor(getResources().getColor(R.color.text_warning));
            checkFailContent += "末袋补液阀故障、";
        }
        if (!mSelfCheckDeviceStatus.moduleSafeOK) {//安全阀故障
            ivSafe.setBackgroundResource(R.drawable.icon_error);
            tvSafe.setTextColor(getResources().getColor(R.color.text_warning));
            checkFailContent += "安全阀故障、";
        }

//        Log.e("自检", "OK---" + mSelfCheckDeviceStatus.moduleUpperWeightOK);
        if (!mSelfCheckDeviceStatus.moduleUpperWeightOK) {//上位秤自检未通过
            ivModuleUpperWeight.setBackgroundResource(R.drawable.icon_error);
            tvModuleUpperWeight.setTextColor(getResources().getColor(R.color.text_warning));
//            checkFailContent += "上位秤故障、";
            checkFailContent += "透析液模块(上位秤)故障、";
        }
        if (!mSelfCheckDeviceStatus.moduleLowerWeightOK) {//下位秤自检未通过
            ivModuleLowerWeight.setBackgroundResource(R.drawable.icon_error);
            tvModuleLowerWeight.setTextColor(getResources().getColor(R.color.text_warning));
//            checkFailContent += "下位秤故障、";
            checkFailContent += "废液(下位秤)故障、";
        }

        if (!mSelfCheckDeviceStatus.t0) {//t0自检未通过
            ivT0.setBackgroundResource(R.drawable.icon_error);
            tvT0.setTextColor(getResources().getColor(R.color.text_warning));
//            checkFailContent += "下位秤故障、";
            checkFailContent += "T0故障、";
        }
        if (!mSelfCheckDeviceStatus.t1) {//t1自检未通过
            ivT1.setBackgroundResource(R.drawable.icon_error);
            tvT1.setTextColor(getResources().getColor(R.color.text_warning));
//            checkFailContent += "下位秤故障、";
            checkFailContent += "T1故障、";
        }
        if (!mSelfCheckDeviceStatus.t2) {//t2自检未通过
            ivT2.setBackgroundResource(R.drawable.icon_error);
            tvT2.setTextColor(getResources().getColor(R.color.text_warning));
//            checkFailContent += "下位秤故障、";
            checkFailContent += "T2故障、";
        }


//        if (!mSelfCheckDeviceStatus.moduleControlOK) {//温控通信故障
//            ivModuleControl.setBackgroundResource(R.drawable.icon_error);
//            tvModuleControl.setTextColor(getResources().getColor(R.color.text_warning));
//            Log.e(TAG, "  自检返回，温度传感器故障：");
////            checkFailContent += "温度传感器故障、";
//            checkFailContent += "控制模块(温度传感器)";
//            if (!mSelfCheckDeviceStatus.t0) {
//                checkFailContent += "t0故障、";
//            }
//            if (!mSelfCheckDeviceStatus.t1) {
//                checkFailContent += "t1故障、";
//            }
//            if (!mSelfCheckDeviceStatus.t2) {
//                checkFailContent += "t2故障、";
//            }
//        }
        if (!mSelfCheckDeviceStatus.moduleHeatingOK) {//温控板自检未通过
            ivModuleHeating.setBackgroundResource(R.drawable.icon_error);
            tvModuleHeating.setTextColor(getResources().getColor(R.color.text_warning));
            Log.e(TAG, "  自检返回，加热板故障：");
//            checkFailContent += "加热板故障、";
            checkFailContent += "加热模块故障、";
        }

        if (!TextUtils.isEmpty(checkFailContent) && checkFailContent.endsWith("、")) {
            checkFailContent = checkFailContent.substring(0, checkFailContent.length() - 1);
        }

        tvWarnContent.setText(checkFailContent);
        layoutWarn.setVisibility(View.VISIBLE);

    }


    /**
     * 开始轮询计时自检的时间。
     */
    private void startLoopSubscription() {

    }

    /**
     * 停止轮询获取计时
     */
    private void stopLoopSubscription() {


    }

    /**
     * 开始轮询计时
     */
    private void startLoopProgress() {
        currCountdown = 0;
        stopLoopProgress();
        progressDisposable = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .take(maxCountdown)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        currCountdown++;
                        SelfCheckActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(currCountdown);
                                tvModeProgress.setText(currCountdown + "%");
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //complete
                        currCountdown = 0;
                    }
                });
        mCompositeDisposable.add(progressDisposable);
    }


    /**
     * 停止轮询获取计时
     */
    private void stopLoopProgress() {
        if (progressDisposable != null && !progressDisposable.isDisposed()) {
            progressDisposable.dispose();
            progressDisposable = null;
            mCompositeDisposable.clear();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
        CmdQueueHelper.getInstance().clear();
        Log.e("自检","onDestroy");
        mCompositeDisposable.clear();
        handler.removeCallbacks(runnable);
        countDownTimer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                        countDownTimer.cancel();
                        doGoTOActivity(EngineerSettingActivity.class);
                    }
                }
            }
        });
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.d("onTouch", "action down");
                isLongPressed = true;
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                ivLogo.postDelayed(mCheckForLongPress1, 5000);
                break;
//            case MotionEvent.ACTION_MOVE:
//                isLongPressed = true;
//                break;
            case MotionEvent.ACTION_UP:
                isLongPressed = false;
//                Log.d("onTouch", "action up");
                break;

        }
        return true;
    }

}