package com.emt.pdgo.next.ui.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.allenliu.versionchecklib.callback.APKDownloadListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.FaultCodeEntity;
import com.emt.pdgo.next.interfaces.ThemeChangeObserver;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.AppUpdateBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.upload.LoginYhTokenBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxjava.RxBaseActivity;
import com.emt.pdgo.next.ui.activity.AnimatorActivity;
import com.emt.pdgo.next.ui.activity.EngineerSettingActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.ClickUtil;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.GlobalDialogManager;
import com.emt.pdgo.next.util.TtsHelper;
import com.emt.pdgo.next.util.task.ConsumptionTask;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.BuildConfig;
import com.pdp.rmmit.pdp.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Mario on 2017-03-02.
 */

public abstract class BaseActivity extends RxBaseActivity implements ThemeChangeObserver {

    private final String KEY_MARIO_CACHE_THEME_TAG = "MarioCache_themeTag";

    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public CompositeSubscription mCompositeSubscription;

    private CompositeDisposable compositeDisposable;

    private CompositeDisposable cmdDisposable;

//    public StateButton btnBack;
//    private StateButton btnSubmit;
//    public RelativeLayout layoutRightMenu;
//
//    public TextView tvTitle;
    public CommonDialog mCommonDialog;

    private CommonDialog usbDialog;

//    private Breathe breatheView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupActivityBeforeCreate();
        super.onCreate(savedInstanceState);
//        RxBus.get().register(this);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        attributes.screenBrightness = MyApplication.brightness / 255.0f;
        window.setAttributes(attributes);
        mCompositeDisposable = new CompositeDisposable();
        compositeDisposable = new CompositeDisposable();
        cmdDisposable = new CompositeDisposable();
//        reentrantLock = new ReentrantLock(true);
//        RxBus.get().register(this);

        initAllViews();

//        if (NetworkUtils.isNetworkConnected(this)) {
//            if (NetworkUtils.isMobileConnected(this)) {
//                netIv.setImageResource(R.drawable.net);
////                wifiIv.setImageResource(R.drawable.no_wifi);
//            } else {
//                netIv.setImageResource(R.drawable.no_net);
//            }
//            Log.e("baseActivity","wifi---"+NetworkUtils.isWifiConnected(this));
//            Log.e("baseActivity","mobile---"+NetworkUtils.isMobileConnected(this));
//            if (NetworkUtils.isWifiConnected(this)) {
//                wifiIv.setImageResource(R.drawable.wifi);
//            } else {
//                wifiIv.setImageResource(R.drawable.no_wifi);
//            }
//        } else {
//            netIv.setImageResource(R.drawable.no_net);
//            wifiIv.setImageResource(R.drawable.no_wifi);
//        }
        registerEvents();
        initViewData();
        ActivityManager.getActivityManager().addActivity(this);
//        isNightOrDay();
//
//        lineUpTaskHelp = LineUpTaskHelp.getInstance();
//        registerReceiverWifi();
//        init();
    }

    //监听wifi变化
//    private void registerReceiverWifi() {
//        NetBroadcastReceiver wifiReceiver = new NetBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态
//        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
//        registerReceiver(wifiReceiver, filter);
//    }
//    @Subscribe(code = RxBusCodeConfig.NET_STATUS)
//    public void netStatus(int status) {
//        if (status == -1) {
//            netIv.setImageResource(R.drawable.no_net);
//            wifiIv.setImageResource(R.drawable.no_wifi);
//        } else {
//            if (status == 0) {
//                netIv.setImageResource(R.drawable.net);
////                wifiIv.setImageResource(R.drawable.no_wifi);
//            } else if (status == 1){
//                wifiIv.setImageResource(R.drawable.wifi);
//            }
//        }
//        Log.e("baseActivity","network--"+ status);
//    }

    public static void setMobileNetworkfromLollipop(Context context) {
        String command = null;
        int state = 0;
        try {
            // Get the current state of the mobile network.
            state = isMobileDataEnabledFromLollipop(context) ? 0 : 1;
            // Get the value of the "TRANSACTION_setDataEnabled" field.
            String transactionCode = getTransactionCode(context);
            // Android 5.1+ (API 22) and later.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                SubscriptionManager mSubscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                // Loop through the subscription list i.e. SIM list.
                for (int i = 0; i < mSubscriptionManager.getActiveSubscriptionInfoCountMax(); i++) {
                    if (transactionCode != null && transactionCode.length() > 0) {
                        // Get the active subscription ID for a given SIM card.
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        int subscriptionId = mSubscriptionManager.getActiveSubscriptionInfoList().get(i).getSubscriptionId();
                        // Execute the command via `su` to turn off
                        // mobile network for a subscription service.
                        command = "service call phone " + transactionCode + " i32 " + subscriptionId + " i32 " + state;
                        executeCommandViaSu(context, "-c", command);
                    }
                }
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                // Android 5.0 (API 21) only.
                if (transactionCode != null && transactionCode.length() > 0) {
                    // Execute the command via `su` to turn off mobile network.
                    command = "service call phone " + transactionCode + " i32 " + state;
                    executeCommandViaSu(context, "-c", command);
                }
            }
        } catch(Exception e) {
            // Oops! Something went wrong, so we throw the exception here.
            Log.e("setMobile",e.getLocalizedMessage());
        }
    }

    public static boolean isMobileDataEnabledFromLollipop(Context context) {
        boolean state = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            state = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
        }
        return state;
    }

    private static String getTransactionCode(Context context) throws Exception {
        try {
            final TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final Class<?> mTelephonyClass = Class.forName(mTelephonyManager.getClass().getName());
            final Method mTelephonyMethod = mTelephonyClass.getDeclaredMethod("getITelephony");
            mTelephonyMethod.setAccessible(true);
            final Object mTelephonyStub = mTelephonyMethod.invoke(mTelephonyManager);
            final Class<?> mTelephonyStubClass = Class.forName(mTelephonyStub.getClass().getName());
            final Class<?> mClass = mTelephonyStubClass.getDeclaringClass();
            final Field field = mClass.getDeclaredField("TRANSACTION_setDataEnabled");
            field.setAccessible(true);
            return String.valueOf(field.getInt(null));
        } catch (Exception e) {
            // The "TRANSACTION_setDataEnabled" field is not available,
            // or named differently in the current API level, so we throw
            // an exception and inform users that the method is not available.
            throw e;
        }
    }

    private static void executeCommandViaSu(Context context, String option, String command) {
        boolean success = false;
        String su = "su";
        for (int i=0; i < 3; i++) {
            // Default "su" command executed successfully, then quit.
            if (success) {
                break;
            }
            // Else, execute other "su" commands.
            if (i == 1) {
                su = "/system/xbin/su";
            } else if (i == 2) {
                su = "/system/bin/su";
            }
            try {
                // Execute command as "su".
                Runtime.getRuntime().exec(new String[]{su, option, command});
            } catch (IOException e) {
                success = false;
                // Oops! Cannot execute `su` for some reason.
                // Log error here.
            } finally {
                success = true;
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("onConfigurationChanged", "mcc:"+newConfig.mcc+"--mnc:" + newConfig.mnc);
    }

    /**
     * versionType: 3:PdGoNext;4:PdGoPro;5:PdGoDpr
     * @param isShow 顯示
     */
    public void appUpdate(boolean isShow) {
        RetrofitUtil.getService().appUpdate(BuildConfig.VERSION_CODE+"",
                "V"+ BuildConfig.VERSION_NAME,
                PdproHelper.getInstance().getMachineSN(),
                4+"").enqueue(new Callback<MyResponse<AppUpdateBean>>()
        {
            @Override
            public void onResponse(Call<MyResponse<AppUpdateBean>> call, Response<MyResponse<AppUpdateBean>> response) {
                if (response.body() != null) {
                    if (response.body().data != null) {
                        Log.e("版本升级--","版本升级--"+response.body().data.code+
                                "--msg--"+response.body().data.msg);
                        if (response.body().data.code.equals("10000")) {
                            AllenVersionChecker
                                    .getInstance()
                                    .downloadOnly(
                                            UIData.create().setDownloadUrl(response.body().data.url)
                                                    .setTitle("版本升级").setContent("有新版本是否更新")
                                    )
                                    .setShowNotification(false)
                                    .setApkDownloadListener(new APKDownloadListener() {
                                        @Override
                                        public void onDownloading(int progress) {

                                        }

                                        @Override
                                        public void onDownloadSuccess(File file) {

                                        }

                                        @Override
                                        public void onDownloadFail() {

                                        }
                                    })
                                    .executeMission(MyApplication.mContext);
                        } else {
                            if (isShow) {
                                toastMessage("已经是最新版了");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<AppUpdateBean>> call, Throwable t) {
                saveFaultCodeLocal("版本升级--"+t.getMessage());
                Log.e("版本升级--","版本升级--"+t.getMessage());
            }
        });
    }

    public boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public void toastMessage(String messages) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
        View view = inflater.inflate(R.layout.toast_item, null); //加載layout下的布局
//        ImageView iv = view.findViewById(R.id.tvImageToast);
//        iv.setImageResource(R.mipmap.atm);//显示的图片
//        TextView title = view.findViewById(R.id.tvTitleToast);
//        title.setText(titles); //toast的标题
        TextView text = view.findViewById(R.id.tvTextToast);
        text.setText(messages); //toast内容
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        toast.setDuration(Toast.LENGTH_SHORT);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }

    private boolean isDorN() {
        return getThemeTag() == -1;
    }

    private void isNightOrDay() {
        Disposable mainDisposable = Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Calendar calendar = Calendar.getInstance();//取得当前时间的年月日 时分秒
                        int mode = calendar.get(Calendar.HOUR_OF_DAY);
                        if (PdproHelper.getInstance().getUserParameterBean().isAutoNight) {
                            if (mode >= 6 && mode <= 18 && PdproHelper.getInstance().getUserParameterBean().isNight && isDorN()) { // 白天
                                PdproHelper.getInstance().getUserParameterBean().isNight = false;
                                Intent intent = new Intent(BaseActivity.this, AnimatorActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, PdproHelper.getInstance().getUserParameterBean());
                            } else if (mode < 6 || mode > 18 && !PdproHelper.getInstance().getUserParameterBean().isNight && !isDorN()){
                                PdproHelper.getInstance().getUserParameterBean().isNight = true;
                                Intent intent = new Intent(BaseActivity.this, AnimatorActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, PdproHelper.getInstance().getUserParameterBean());
                            }
                        }
                    }
                });

        compositeDisposable.add(mainDisposable);
    }

    public void saveFaultCodeLocal(String faultCode) {
        FaultCodeEntity entity = new FaultCodeEntity();
        entity.time = EmtTimeUil.getTime();
        entity.code = faultCode;
        EmtDataBase
                .getInstance(BaseActivity.this)
                .getFaultCodeDao()
                .insertFaultCode(entity);
    }


    public abstract void initAllViews();

    /*** 注册事件 */
    public abstract void registerEvents();

    /*** 准备界面的数据 */
    public abstract void initViewData();

//    // 消音
//    LinearLayout silencersLayout;
//    // 警报
//    LinearLayout ringingLayout;
//    TextView tvSilencers;

//    private CheckForLongPress mCheckForLongPress;
//    private volatile boolean isPressed = false;
//
//    private class CheckForLongPress implements Runnable {
//
//        @Override
//        public void run() {
//            //5s之后，查看isLongPressed的变量值：
//            if (isPressed) {//没有做up事件
//                alertNumberBoard("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
//            } else {
//                tvTitle.removeCallbacks(mCheckForLongPress);
//            }
//        }
//    }

//    private void alertNumberBoard(String value, String type) {
//        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false);
//        dialog.show();
//        dialog.setOnDialogResultListener((mType, result) -> {
//            if (!TextUtils.isEmpty(result)) {
////                    Logger.d(result);
//                Calendar calendar = Calendar.getInstance();//取得当前时间的年月日 时分秒
//
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH) + 1;
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//                int second = calendar.get(Calendar.SECOND);
//                String mMonth = "";
//
//                if (month >= 10) {
//                    mMonth = "" + month;
//                } else {
//                    mMonth = "0" + month;
//                }
//
//                //123加上月份
//                String tempPwd = "123" + mMonth;
//                Log.e("长按", "tempPwd：" + tempPwd);
//                if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式的密码
//                    if (tempPwd.equals(result)) {
//                        doGoTOActivity(EngineerSettingActivity.class);
//                    }
//                }
//            }
//        });
//    }
//    private ImageView btnSound;
    @SuppressLint("ClickableViewAccessibility")
    private void initHeadview() {
//        btnBack = findViewById(R.id.btn_back);
//        layoutRightMenu = findViewById(R.id.layout_right_menu);
//        btnSubmit = findViewById(R.id.btn_submit);
//        tvTitle = findViewById(R.id.tv_title);
//        btnSound = findViewById(R.id.btnSound);
//        silencersLayout = findViewById(R.id.silencersLayout);
//        ringingLayout = findViewById(R.id.ringLayout);
//        tvSilencers = findViewById(R.id.tvSilencers);
//        breatheView = findViewById(R.id.breathe);
//        mCheckForLongPress = new CheckForLongPress();
//
//        tvTitle.setOnTouchListener((View.OnTouchListener) (v, event) -> {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
////                Log.d("onTouch", "action down");
//                    isPressed = true;
////                ivLogo.postDelayed(mCheckForLongPress1, 1000);
//                    tvTitle.postDelayed(mCheckForLongPress, 5000);
//                    break;
////            case MotionEvent.ACTION_MOVE:
////                isLongPressed = true;
////                break;
//                case MotionEvent.ACTION_UP:
//                    isPressed = false;
////                Log.d("onTouch", "action up");
//                    break;
//
//            }
//
//            return true;
//        });

    }

//    private final Handler handler = new Handler();
//    private final Runnable runnable = () -> {
////        sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
//        doGoTOActivity(EngineerSettingActivity.class);
//    };

    /****
     * 二级菜单(有左侧按钮和标题。)
     *
     * @param title
     */
    public void initHeadNotBack(String title, String menuRight) {

        initHeadview();

//        tvTitle.setText(title);
//        btnBack.setVisibility(View.INVISIBLE);
//        if (TextUtils.isEmpty(menuRight)) {
//            layoutRightMenu.setVisibility(View.INVISIBLE);
//        } else {
//            btnSubmit.setText(menuRight);
//            btnSubmit.setVisibility(View.VISIBLE);
//            layoutRightMenu.setVisibility(View.VISIBLE);
//        }


    }

    /****
     * 二级菜单(有左侧按钮和标题。)
     *
     * @param title
     */
    public void initHeadTitleBar(String title) {

        initHeadview();

//        tvTitle.setText(title);
//        btnBack.setVisibility(View.VISIBLE);
//        layoutRightMenu.setVisibility(View.INVISIBLE);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityManager.getActivityManager().removeActivity(BaseActivity.this);
//            }
//        });

    }

    /****
     * 二级菜单(有左侧按钮和标题、声音按钮)
     *
     * @param title
     */

    public void initHeadTitleAndSound(String title) {

        initHeadview();
//        tvTitle.setText(title);
//        btnBack.setVisibility(View.VISIBLE);
//        layoutRightMenu.setVisibility(View.VISIBLE);
//        btnSubmit.setVisibility(View.GONE);
//        btnSound.setVisibility(View.VISIBLE);
//        layoutRightMenu.setOnClickListener(v -> {
//            SoundDialog soundDialog = new SoundDialog(this);
//            soundDialog.closeClick(Dialog::dismiss)
//                    .tsClick(pdproHelper -> {
//                        PdproHelper.getInstance().updateTtsSoundOpen(!pdproHelper.getTtsSoundOpen());
//                    })
//                    .ringClick(soundDialog12 -> {
//                        if (!MyApplication.isBuzzerOff) {
//                            buzzerOff();
////                            if (isBuzzerOff) {
//                            silencersLayout.setVisibility(View.VISIBLE);
//                            disposable = Observable.interval(1, TimeUnit.SECONDS)
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .take(maxCountdown - currCountdown)
//                                    .subscribe(aLong -> {
//                                        currCountdown++;
//                                        MyApplication.isBuzzerOff = true;
//                                        tvSilencers.setText("消音中("+(maxCountdown - currCountdown)+"s)");
//                                    }, throwable -> {
//
//                                    }, () -> {
//                                        //complete
//                                        runOnUiThread(()->{
//                                            MyApplication.isBuzzerOff = false;
//                                            currCountdown = 0;
//                                            if (MyApplication.isFailure) {
//                                                buzzerOn();
//                                            }
//                                            silencersLayout.setVisibility(View.INVISIBLE);
//                                        });
//                                    });
//                            mCompositeDisposable.add(disposable);
////                            }
//                        }
//                    }).show();
//        });
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityManager.getActivityManager().removeActivity(BaseActivity.this);
//            }
//        });

    }

    /****
     * 二级菜单(有左侧按钮和标题。)
     *
     * @param title
     */
    public void initHeadTitleBar(String title, View.OnClickListener backListener) {

        initHeadview();

//        tvTitle.setText(title);
//        btnBack.setVisibility(View.VISIBLE);
//        layoutRightMenu.setVisibility(View.INVISIBLE);
//        btnBack.setOnClickListener(backListener);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //有按下动作时取消定时
//                if (countDownTimer != null) {
//                    countDownTimer.cancel();
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                //抬起时启动定时
//                if (countDownTimer != null) {
//                    countDownTimer.cancel();
//                    countDownTimer.start();
//                } else {
//                    startCountDown(PdproHelper.getInstance().getUserParameterBean().countdownTimer);
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    public void ann() {
        try {
            OutputStream os = Runtime.getRuntime().exec("norcofz").getOutputStream();
//            OutputStream os = Runtime.getRuntime().exec("su").getOutputStream();
//            OutputStream os = Runtime.getRuntime().exec("/system/bin/sh").getOutputStream();
            os.write("mount -o remount".getBytes());
            os.flush();
//            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private OutputStream os;
    private void execute(String cmd) {
        try {
            OutputStream os = Runtime.getRuntime().exec("norcofz").getOutputStream();
//            OutputStream os = Runtime.getRuntime().exec("su").getOutputStream();
//            OutputStream os = Runtime.getRuntime().exec("/system/bin/sh").getOutputStream();
            os.write(cmd.getBytes());
            os.flush();
//            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            // 构建adb命令
//            String[] cmd = {"adb", "shell", "input", "key", "event","26"};
//
//            // 执行adb命令
//            Process process = Runtime.getRuntime().exec(cmd);
//
//            // 读取命令输出
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder output = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }
//
//            // 处理命令输出
//            String result = output.toString();
//            // 在这里可以对命令输出进行处理，例如解析包名等
//
//            // 关闭流和进程
//            reader.close();
//            process.destroy();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    public void simulateKey(int keyCode) {
        /*
         * 执行灭屏亮屏：adb shell input keyevent 26
         * 执行解锁屏幕：adb shell input keyevent 82
         */
        execute("input keyevent " + keyCode  + "\n");
    }

    public void setMobile(int i) {
        execute("settings put global mobile_data " + i  + "\n");
    }

    /****
     * 二级菜单(有左侧按钮和标题、右侧按钮。)
     *
     * @param title
     */
    public void initHeadTitleBar(String title, String menuRight) {

        initHeadview();

//        tvTitle.setText(title);
//        btnSubmit.setText(menuRight);
//        btnBack.setVisibility(View.VISIBLE);
//        btnSubmit.setVisibility(View.VISIBLE);
//        layoutRightMenu.setVisibility(View.VISIBLE);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityManager.getActivityManager().removeActivity(BaseActivity.this);
//            }
//        });
    }

    /****
     * 二级菜单(有左侧按钮和标题、右侧按钮。)
     *
     * @param title
     */
    public void initHeadTitleBar(String title, String menuRight, View.OnClickListener menuRightListener) {

        initHeadview();

//        tvTitle.setText(title);
//        btnSubmit.setText(menuRight);
//        btnBack.setVisibility(View.VISIBLE);
//        btnSubmit.setVisibility(View.VISIBLE);
////        layoutRightMenu.setVisibility(View.VISIBLE);
////        layoutRightMenu.setOnClickListener(menuRightListener);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityManager.getActivityManager().removeActivity(BaseActivity.this);
//            }
//        });
    }

    /****
     * 二级菜单(只有右侧按钮和标题。)
     *
     * @param title
     */
    public void initHeadTitleBarRight(String title, String menuRight, View.OnClickListener menuRightListener) {

        initHeadview();

//        tvTitle.setText(title);
//        btnSubmit.setText(menuRight);
//        btnBack.setVisibility(View.INVISIBLE);
//        btnSubmit.setVisibility(View.VISIBLE);
//        layoutRightMenu.setVisibility(View.VISIBLE);
//        layoutRightMenu.setOnClickListener(menuRightListener);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityManager.getActivityManager().removeActivity(BaseActivity.this);
//            }
//        });
    }

    /****
     * 二级菜单(有左侧按钮和标题，右侧按钮。)
     *
     * @param title
     */
    public void initHeadTitleBar(String title, String menuRight, View.OnClickListener backListener, View.OnClickListener menuRightListener) {

        initHeadview();

//        tvTitle.setText(title);
//        btnSubmit.setText(menuRight);
//        btnBack.setVisibility(View.VISIBLE);
//        btnSubmit.setVisibility(View.VISIBLE);
//        layoutRightMenu.setVisibility(View.VISIBLE);
//        layoutRightMenu.setOnClickListener(menuRightListener);
//        btnBack.setOnClickListener(backListener);
    }

//    public void setRightMenu(String menuRight) {
//        btnSubmit.setText(menuRight);
//    }

    /**
     * 设置不可编辑且有点击事件
     *
     * @param mEditText
     * @param mListener
     */
    public void setCanNotEditAndClick(EditText mEditText, View.OnClickListener mListener) {
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        mEditText.setOnClickListener(mListener);
    }

    /**
     * 设置不可编辑且无点击事件
     *
     * @param mEditText
     */
    public void setCanNotEditNoClick(EditText mEditText) {
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        // 如果之前没设置过点击事件，该处可省略
        mEditText.setOnClickListener(null);
    }

    /**
     * 设置不可编辑且无点击事件
     *
     * @param mEditText
     */
    public void setCanNotEditNoClick2(EditText mEditText) {
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        // 如果之前没设置过点击事件，该处可省略
    }

//    public void checkNet() {
//        netStatus = NetworkUtils.getNetWorkState(BaseActivity.this);
//    }

    /**
     *
     */
    private void setupActivityBeforeCreate() {
        ((MyApplication) getApplication()).registerObserver(this);
        loadingCurrentTheme();
//        onNetChange(NetworkUtils.getNetWorkState(BaseActivity.this));
    }

//    private int netStatus;
//    @Override
//    public void onNetChange(int netWorkState) {
//        runOnUiThread(() -> {
//            if (netWorkState == -1) {
//                netIv.setImageResource(R.drawable.no_net);
//                wifiIv.setImageResource(R.drawable.no_wifi);
//                Log.e("baseActivity","net--  无望");
//            } else {
//                if (netWorkState == 0) {
//                    netIv.setImageResource(R.drawable.net);
////                wifiIv.setImageResource(R.drawable.no_wifi);
//                } else if (netWorkState == 1){
//                    wifiIv.setImageResource(R.drawable.wifi);
//                }
//            }
//        });
//        if (netWorkState == -1) {
//            netIv.setImageResource(R.drawable.no_net);
//            wifiIv.setImageResource(R.drawable.no_wifi);
//        } else {
//            if (netWorkState == 0) {
//                netIv.setImageResource(R.drawable.net);
////                wifiIv.setImageResource(R.drawable.no_wifi);
//            } else if (netWorkState == 1){
//                wifiIv.setImageResource(R.drawable.wifi);
//            }
//        }
//        Log.e("baseActivity","net--  "+netWorkState);
//    }

    @Override
    public void loadingCurrentTheme() {
        switch (getThemeTag()) {
            case 1:
                setTheme(R.style.MarioTheme_Day);
                break;
            case -1:
                setTheme(R.style.MarioTheme_Night);
                break;
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        View status = findViewById(R.id.custom_id_title_status_bar);
        if (status != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            status.getLayoutParams().height = getStatusBarHeight();
        }
    }

    /**
     *
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     *
     */
    protected int getThemeTag() {
        SharedPreferences preferences = getSharedPreferences("MarioCache", Context.MODE_PRIVATE);
        return preferences.getInt(KEY_MARIO_CACHE_THEME_TAG, 1);
    }

    /**
     *
     */
    protected void setThemeTag(int tag) {
        SharedPreferences preferences = getSharedPreferences("MarioCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(KEY_MARIO_CACHE_THEME_TAG, tag);
        edit.commit();
    }

    /**
     *
     */
    public void switchCurrentThemeTag() {
        setThemeTag(0 - getThemeTag());
        loadingCurrentTheme();
    }

    /**
     *
     */
    public Context getContext() {
        return BaseActivity.this;
    }

    @Override
    protected void onDestroy() {
        ((MyApplication) getApplication()).unregisterObserver(this);
        super.onDestroy();
        compositeDisposable.clear();
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
        cmdDisposable.clear();
//        RxBus.get().unRegister(this);
        ActivityManager.getActivityManager().removeActivity(this);
    }

    /**
     * 跳转activity, 不关闭当前的activity
     *
     * @param ac
     */
    public void doGoTOActivity(Class ac) {
        Intent intent = new Intent(this, ac);
//        intent.putExtra(EmtConstant.JUMP_WITH_PARAM,EmtConstant.JUMP_WITH_PARAM_NOR);
        doIntentGoActivity(intent, false);
    }


    /**
     * 跳转activity, 关闭当前的activity
     *
     * @param ac
     */
    public void doGoCloseTOActivity(Class ac, String msg) {
        Intent intent = new Intent(this, ac);
        intent.putExtra(EmtConstant.JUMP_WITH_PARAM,msg);
        doIntentGoActivity(intent, true);
    }

    /***
     * Intent跳转Activity
     *
     * @param intent
     * @param isCloseActivity
     */
    public void doIntentGoActivity(Intent intent, boolean isCloseActivity) {
        startActivity(intent);
        if (isCloseActivity) {
            finish();
        }
    }

    // 去工程师界面
    public void toEngAc(View view) {
        ClickUtil clickUtil = new ClickUtil(view,5000);
        clickUtil.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                NumberBoardDialog dialog = new NumberBoardDialog(BaseActivity.this, "", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD, false);
                if (!BaseActivity.this.isFinishing()) {
                    dialog.show();
                }
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
        });
    }


    /**
     * 文字转语音播放
     *
     * @param content
     */
    public void speak(String content) {
//        if (PdproHelper.getInstance().getTtsSoundOpen()) {
////            BaiduTtsHelper.getInstance(BaseActivity.this).speak(content);
//            TtsHelper.getInstance(BaseActivity.this).speak(content);
//        }
        TtsHelper.getInstance(BaseActivity.this).speak(content);
    }

    /**
     * 发送数据到主板
     */
    public void sendToMainBoard(String mCommand) {
//        Log.e("sendToMainBoard", "isOpenMainSerial:" + MyApplication.isOpenMainSerial);
        if (!MyApplication.isOpenMainSerial) {
            Log.d("发送数据到主板", "MyApplication.isOpenMainSerial:" + false);
//            RxBus.get().send(RxBusCodeConfig.EVENT_ERROR_MAIN_BOARD, MainBoardService.OpenSerialError);
        } else {
            if (mCommand == null || "".equals(mCommand)) {
                return;
            }
            ConsumptionTask task = new ConsumptionTask();
            task.taskNo = "Task" + (MyApplication.index++); // 确保唯一性
            task.planNo = mCommand; // 将数据分组， 如果没有该需求的同学，可以不进行设置
            MyApplication.lineUpTaskHelp.addTask(task); // 添加到排队列表中去， 如果还有任务没完成，
//            RxBus.get().send(RxBusCodeConfig.EVENT_SEND_COMMAND, mCommand);
        }
    }

    /**
     * 发送数据到LED主板
     */
    protected synchronized void sendToLedBoard(String mCommand) {

        if (!MyApplication.isOpenLedSerial) {
            Log.e("发送数据到LED主板", "MyApplication.isOpenLedSerial:" + MyApplication.isOpenLedSerial);
//            RxBus.get().send(RxBusCodeConfig.EVENT_ERROR_MAIN_BOARD, MainBoardService.OpenSerialError);
        } else {
            if (mCommand == null || "".equals(mCommand)) {
                return;
            }
//            Log.e("发送数据到LED主板", "mCommand:" + mCommand);
            RxBus.get().send(RxBusCodeConfig.EVENT_SEND_LED_COMMAND, mCommand);
        }
    }

    private final int maxCountdown = 9 * 60;//倒计时
    private int currCountdown = 0;//倒计时
    private CompositeDisposable mCompositeDisposable;
    private Disposable disposable;

    public void sendCommandInterval(String mCommand, long delayMillis) {
        /**
         * 间隔delayMillis（毫秒）发送主板指令
         *
         * @param mCommand
         * @param delayMillis
         */
//        if (mainDisposable == null) {
        Disposable mainDisposable = Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> sendToMainBoard(mCommand));
//        }
        cmdDisposable.add(mainDisposable);
    }

    /**
     * 间隔delayMillis（毫秒）发送Led主板指令
     *
     * @param mCommand
     * @param delayMillis
     */
    public void sendLedCommandInterval(String mCommand, long delayMillis) {
        Disposable ledDisposable = Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        sendToLedBoard(mCommand);
                    }
                });

        compositeDisposable.add(ledDisposable);

    }

    /**
     * 关闭蜂鸣器
     */
    public void buzzerOff() {
//        ringingLayout.setVisibility(View.INVISIBLE);
//        breatheView.hide();
        if (!MyApplication.isBuzzerOff) {
            sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_OFF), 500);
//        sendCommandInterval(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_OFF), 100);
        }
    }

    /**
     * 打开蜂鸣器
     */
    public void buzzerOn() {
        MyApplication.isBuzzerOff = false;
//        Log.e("baseActivity","报警中"+MyApplication.isBuzzerOff);
//        if (!MyApplication.isBuzzerOff) {
//            Log.e("baseActivity","报警中");
        sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_ON));
//        }
    }

    //获取版本名
    public  String getVersionName() {
        PackageManager manager = MyApplication.mContext.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(MyApplication.mContext.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    //获取版本号
    public int getVersionCode() {
        PackageManager manager = MyApplication.mContext.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(MyApplication.mContext.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    // 获取移动数据状态
    public boolean getMobileDataState(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method method = telephonyManager.getClass().getDeclaredMethod("setDataEnabled");
            return (boolean) method.invoke(telephonyManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 设置移动数据状态
    public void setMobileDataState(Context context, boolean enabled) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method method = telephonyManager.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            method.invoke(telephonyManager,enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTipsCommonDialog(String tips) {

        runOnUiThread(() -> {
            if (!BaseActivity.this.isFinishing()) {
                if (mCommonDialog == null) {
                    mCommonDialog = new CommonDialog(BaseActivity.this);
                }
                mCommonDialog.setContent(tips)
                        .setBtnFirst("确定")
                        .setFirstClickListener(Dialog::dismiss)
                        .setTwoClickListener(Dialog::dismiss)
                        .show();
            }

        });

    }

    public void showLoading(String hintMsg) {
        if (!MyApplication.DEBUG) {
            runOnUiThread(() -> GlobalDialogManager.getInstance().show(getFragmentManager(), hintMsg));
        }
    }

    public void dismissLoading() {
        if (!MyApplication.DEBUG) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GlobalDialogManager.getInstance().dismiss();
                }
            });
        }
    }

    /**
     * 创建观察者
     *
     * @param onNext
     * @param <T>
     * @return
     */
    public <T> Subscriber newSubscriber(final Action1<? super T> onNext) {

        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        return new Subscriber<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                if (e instanceof RetrofitUtil.APIException) {
                    Log.e("onError", ((RetrofitUtil.APIException) e).message);
                }
            }

            @Override
            public void onNext(T t) {
                if (!mCompositeSubscription.isUnsubscribed()) {
                    onNext.call(t);
                }
            }

        };
    }

    private Gson gson;
    public void getToken() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("machineSn", PdproHelper.getInstance().getMachineSN());
        if (gson == null) {
            gson = new Gson();
        }
        String content = gson.toJson(paramsMap);

        RequestBody params = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                content);
        RetrofitUtil.getService().getToken(params).enqueue(new Callback<MyResponse<LoginYhTokenBean>>() {
            @Override
            public void onResponse(Call<MyResponse<LoginYhTokenBean>> call, Response<MyResponse<LoginYhTokenBean>> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().getData().getCode().equals("10000")) {
                            CacheUtils.getInstance().getACache().put(PdGoConstConfig.TOKEN, response.body().getData().getToken());
                            Log.e("获取token", response.body().getData().getMsg() + "--token--" + response.body().getData().getToken() + "--code--" + response.body().getData().getCode());
                        } else {
                            saveFaultCodeLocal("获取token失败,"+response.body().getData().getMsg());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse<LoginYhTokenBean>> call, Throwable t) {
                Log.e("获取token", t.getMessage() + "--url--"+call.request().url());
                saveFaultCodeLocal("获取token失败,"+t.getMessage());
            }
        });
    }

    public boolean checkConnectNetwork(Context context) {
//        APIServiceManage.getInstance().postApdCode("");
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo net = conn.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    public void net() {

        /**
         * private static final String EXTRA_PREFS_SHOW_BUTTON_BAR = "extra_prefs_show_button_bar";//是否显示button bar,传递值为true的话是显示
         private static final String EXTRA_PREFS_SET_NEXT_TEXT = "extra_prefs_set_next_text";//自定义按钮的名字，不传递的话，默认为下一步
         private static final String EXTRA_PREFS_SET_BACK_TEXT = "extra_prefs_set_back_text";//自定义按钮的名字，不传递的话，默认为上一步
         private static final String EXTRA_ENABLE_NEXT_ON_CONNECT = "wifi_enable_next_on_connect";//是否打开网络连接检测功能（如果连上wifi，则下一步按钮可被点击）
         */

        //调用系统网络配置界面
        Intent intent = new Intent();
        intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
        intent.putExtra("extra_prefs_show_button_bar", true);
        intent.putExtra("extra_prefs_set_next_text", "保存");
        intent.putExtra("extra_prefs_set_back_text", "取消");
        intent.putExtra("wifi_enable_next_on_connect", true);
        startActivity(intent);

    }

    /**
     * 发送数据
     */
    public void handleValve(String motor, boolean open) {

//        serialPortPlus.writeAndFlush(new byte[]{0x5E});
//        String cmdStr = "{\"request\":{\"method\":\"selfcheck/start\",\"params\":{\"upper\":600,\"lower\":600}},\"sign\":\"c726651f09fde161da47c41339943a14\"}";
        String cmdStr = "";

        if ("perfuse".equals(motor)) {
            cmdStr = open ? "{\"request\":{\"method\":\"valve/open\",\"params\":{\"id\":\"perfuse\"}},\"sign\":\"4d07743b815f4f2cb1622c93702d030e\"}" :
                    "{\"request\":{\"method\":\"valve/close\",\"params\":{\"id\":\"perfuse\"}},\"sign\":\"a2d2a69c9f32ee8f72e6fe59870a9466\"}";
        } else if ("safe".equals(motor)) {
            cmdStr = open ?
                    "{\"request\":{\"method\":\"valve/open\",\"params\":{\"id\":\"safe\"}},\"sign\":\"95d7bcb193615063a7a00b4273c050b5\"}" :
                    "{\"request\":{\"method\":\"valve/close\",\"params\":{\"id\":\"safe\"}},\"sign\":\"3812130b6ce80a2d29761ca0e4e229e4\"}";
        } else if ("supply".equals(motor)) {
            cmdStr = open ?
                    "{\"request\":{\"method\":\"valve/open\",\"params\":{\"id\":\"supply\"}},\"sign\":\"2bc35d2fc41534feb5725d2814f2241d\"}" :
                    "{\"request\":{\"method\":\"valve/close\",\"params\":{\"id\":\"supply\"}},\"sign\":\"66b499cec500f4f1989ef3b8ef75c76b\"}";
        } else if ("supply2".equals(motor)) {
            cmdStr = open ?
                    "{\"request\":{\"method\":\"valve/open\",\"params\":{\"id\":\"supply2\"}},\"sign\":\"46fe667918b9f6cfe6eeae94728d241a\"}" :
                    "{\"request\":{\"method\":\"valve/close\",\"params\":{\"id\":\"supply2\"}},\"sign\":\"2cc26b7f91a2ea17b13a40a41e31bac9\"}";
        } else if ("drain".equals(motor)) {
            cmdStr = open ?
                    "{\"request\":{\"method\":\"valve/open\",\"params\":{\"id\":\"drain\"}},\"sign\":\"7e716bfa11f7a7d5dacdbe7abe5da187\"}" :
                    "{\"request\":{\"method\":\"valve/close\",\"params\":{\"id\":\"drain\"}},\"sign\":\"7bae7b013f871d190382a9c927028dbe\"}";
        } else if ("vaccum".equals(motor)) {
            cmdStr = open ?
                    "{\"request\":{\"method\":\"valve/open\",\"params\":{\"id\":\"vaccum\"}},\"sign\":\"3dde6de8d889a9c1890890c4ca3af418\"}" :
                    "{\"request\":{\"method\":\"valve/close\",\"params\":{\"id\":\"vaccum\"}},\"sign\":\"a39faab7ae220ec75eb2123db37ee35b\"}";
        } else if ("group1".equals(motor)) {
            cmdStr = open ?
                    "{\"request\":{\"method\":\"valve/open\",\"params\":{\"id\":\"group1\"}},\"sign\":\"3dde6de8d889a9c1890890c4ca3af418\"}" :
                    "{\"request\":{\"method\":\"valve/close\",\"params\":{\"id\":\"group1\"}},\"sign\":\"a39faab7ae220ec75eb2123db37ee35b\"}";
        } else if ("group2".equals(motor)) {
            cmdStr = open ?
                    "{\"request\":{\"method\":\"valve/open\",\"params\":{\"id\":\"group1\"}},\"sign\":\"3dde6de8d889a9c1890890c4ca3af418\"}" :
                    "{\"request\":{\"method\":\"valve/close\",\"params\":{\"id\":\"group1\"}},\"sign\":\"a39faab7ae220ec75eb2123db37ee35b\"}";
        }
        sendCommandInterval(cmdStr,500);
    }

//    public int useDeviceTime = 0; // 设备使用时间
//    private void init() {
//        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
//            @Override
//            public void onNext(Long aLong) {
//
//                useDeviceTime ++;
//                if (useDeviceTime >= 60 * 60 && (useDeviceTime % (60 * 60) == 0)) {
////                    if (useDeviceTime > 0 && useDeviceTime % (10) == 0) {
////                        if (PdproHelper.getInstance().useDeviceTime() == 0) {
////                            CacheUtils.getInstance().getACache().put(PdGoConstConfig.useDeviceTime, 1);
////                        } else {
//                    Log.e("MyApplication","useDeviceTime:"+useDeviceTime);
//                    int time = PdproHelper.getInstance().useDeviceTime();
//                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.useDeviceTime, String.valueOf(time + 1));
////                        }
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
//        if (dtCompositeDisposable == null) {
//            dtCompositeDisposable = new CompositeDisposable();
//        }
//        Observable.interval(0, 1000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
//        dtCompositeDisposable.add(disposableObserver);
//    }

    private CompositeDisposable dtCompositeDisposable;

    public void initBeepSoundSus(int uri) {
        SoundPool sp = new SoundPool(10, AudioManager.STREAM_ALARM, 5);
        sp.load(this, uri, 1);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                Log.e("SoundPool","pool--"+i);
                soundPool.play(i, 100, 100, 1, 0, 1f);
            }
        });
//        sp.play(soundId, 1, 1, 0, 0, 1);
    }

}
