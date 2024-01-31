package com.emt.pdgo.next.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandReceiveConfig;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.PdGoDbManager;
import com.emt.pdgo.next.data.serial.ReceivePublicBean;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.ScreenUtil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class PreHeatActivity extends BaseActivity {

    private final String TAG = "PreHeatActivity";

    @BindView(R.id.tv_curr_temperature)
    TextView tvCurrTemperature;
    @BindView(R.id.tv_target_temperature)
    TextView tvTargetTemperature;

    @BindView(R.id.layout_state)
    RelativeLayout layoutState;
    @BindView(R.id.iv_label)
    ImageView ivLabel;
    @BindView(R.id.tv_tips_title)
    TextView tvTipsTitle;
    @BindView(R.id.tv_tips_content)
    TextView tvTipsContent;


    @BindView(R.id.btn_add1)
    StateButton btnAdd1;
    @BindView(R.id.btn_subtract1)
    StateButton btnSubtract1;

    private float maxTemperature = 40.0f;
    private float minTemperature = 34.0f;

    private float currTemperature;

    private int currUpWeight = 0;

    //是否可以显示未放透析液袋提示
    private boolean canShowPreHeatHintDialog = false;
    //正在显示 提示未放透析液袋对话框
    private boolean isShowHintDialog = false;

    private ReceiveDeviceBean mReceiveDeviceBean;

    private CommonDialog preHeatHintDialog;

    private CompositeDisposable mCompositeDisposable;
    private CompositeDisposable compositeDisposable;
    private Disposable mShowPreHeatHintDialogDisposable;
    private Disposable delayJudgeTargetTemperDisposable;

    private boolean isPreheatComplete;//预热完成
    private boolean isStartPreheat;//开始预热

//    private BigDecimal scales = new BigDecimal("0.1");//每次加减0.1度

    private String jumpMsg;

    private String msg = EmtConstant.ACTIVITY_PRE_HEAT;

    @BindView(R.id.nextRinseLayout)
    LinearLayout nextRinseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_pre_heat);
        ButterKnife.bind(this);
        initHeadTitleBar("预热程序");
        mCompositeDisposable = new CompositeDisposable();
        compositeDisposable = new CompositeDisposable();
    }
    private ExoPlayer exoPlayer;
    @BindView(R.id.videoView)
    StyledPlayerView videoView;
    @BindView(R.id.tvHeartTips)
    TextView tvHeartTips;
    @BindView(R.id.tvHeartStatus)
    TextView tvHeartStatus;

    private void showAlarmDialog(String message) {
//        MyApplication.speak("自动预冲故障");
//        String mFirst = "重新预冲";
        String mFirst = "确定";
        String mTwo = "取消";
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent(message)
                .setBtnFirst(mFirst)
                .setBtnTwo(mTwo)
                .setFirstClickListener(mCommonDialog -> {
                    APIServiceManage.getInstance().postApdCode("Z3013");
                    doGoCloseTOActivity(PipelineConnectionSkipBootActivity.class,msg);
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(Dialog::dismiss);
        if (!PreHeatActivity.this.isFinishing()) {
            dialog.show();
        }

    }
    @BindView(R.id.powerIv)
    ImageView powerIv;
    @BindView(R.id.currentPower)
    TextView currentPower;
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        this.mReceiveDeviceBean = mReceiveDeviceBean;
        PreHeatActivity.this.runOnUiThread(this::handleDeviceStatus);
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
        RxBus.get().register(this);
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
    @BindView(R.id.btn_back)
    StateButton btnBack;

    @SuppressLint("SetTextI18n")
    @Override
    public void initViewData() {
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        exoPlayer = new ExoPlayer.Builder(this).build();
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.pre_heart;
        videoView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        MyApplication.mTargetTemper = PdGoDbManager.getInstance().getTemperatureBoardTable().targetTemper;
        if (MyApplication.mTargetTemper == 0f) {

            MyApplication.mTargetTemper = 37.0f;
        }
        tvTargetTemperature.setText(MyApplication.mTargetTemper + "℃");

        preheatStartTemperature();

        sendShowPreHeatHintDialog(10);

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        int sw = this.getResources().getConfiguration().smallestScreenWidthDp;
//        Log.e(TAG,"屏幕分辨率:" + width + "*" + height+",density:"+dm.density+",dpi:"+dm.densityDpi+",sw:"+sw);
        if ((480 == width && 800 == height) || (800 == width && 480 == height) || (1024 == width && 552 == height) || (1024 == height && 552 == width)) {

        } else {
            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.drawable.icon_add2); //获取图片
            int bounds = ScreenUtil.dip2px(this, 40);
            drawable.setBounds(bounds, bounds, bounds, bounds);  //设置图片参数
            btnAdd1.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            Drawable drawable2 = getResources().getDrawable(R.drawable.icon_subtract2); //获取图片

            drawable2.setBounds(bounds, bounds, bounds, bounds);  //设置图片参数
            btnSubtract1.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable2, null);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 60 * 1000); // 延时1分钟

////        startLoopSubscription();
    }

    @OnClick({R.id.btn_back, R.id.btn_subtract1, R.id.btn_add1, R.id.tv_target_temperature})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_subtract1:
                if(isPreheatComplete){
                    return;
                }
                subtractTemperature();
                break;
            case R.id.btn_add1:
                if(isPreheatComplete){
                    return;
                }
                addTemperature();
                break;
            case R.id.tv_target_temperature:
                if(isPreheatComplete){
                    return;
                }
                alertNumberBoardDialog(String.valueOf(MyApplication.mTargetTemper), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE, true);
                break;
        }
    }


    private void alertNumberBoardDialog(String value, String type, boolean isMinus) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, isMinus);//"℃"
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    result = Float.valueOf(result) + "";//转换成浮点数
                    if (mType.equals(PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE)) {//目标温度值  :  在34.0-40.0之间
                        if (!TextUtils.isEmpty(result)) {
                            tvTargetTemperature.setText(result);
                            MyApplication.mTargetTemper = Float.parseFloat(result);
                            PdGoDbManager.getInstance().getTemperatureBoardTable().targetTemper = MyApplication.mTargetTemper;
                            preheatSetTemperature();
                        }
                    }
                }
            }
        });
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
        Observable.interval(0, EmtConstant.REPORT_DELAY_TIME, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        compositeDisposable.add(disposableObserver);
    }
//    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
//    public void receiveCmdDeviceInfo(String bean) {
//        this.mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
//        PreHeatActivity.this.runOnUiThread(this::handleDeviceStatus);
//    }

    /**
     * 获取设备信息
     *
     * @param mSerialJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_DEVICE_INFO)
    public void receiveDeviceInfo(String mSerialJson) {
        ReceiveDeviceBean mBean = JsonHelper.jsonToClass(mSerialJson, ReceiveDeviceBean.class);
//        Log.e("预热界面", "   --->接收设备信息：" + mSerialJson);
        this.mReceiveDeviceBean = mBean;
        PreHeatActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleDeviceStatus();
            }
        });
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_PREHEAT_DATA)
    public void receivePreHeatData(String receiveData) {
        if (receiveData.contains(CommandReceiveConfig.PREHEAT_FINISH)) {//预热返回 :  加热到的设置目标值
            runOnUiThread(() -> {
//                speak("预热完成");
                isStartPreheat = false;
                switchTips(2);
                //预热完成

            });
            Log.e("预热返回", "加热到的设置目标值：" + receiveData);
        } else if (receiveData.contains(CommandReceiveConfig.PREHEAT_NO_HEATING_TARGET)) {//预热返回 : 无加热对象，提示需要放置液袋到加热托盘上
            Log.e("预热返回", "无加热对象，提示需要放置液袋到加热托盘上");
//            runOnUiThread(() -> {
//                if (View.VISIBLE == rinseLayout.getVisibility()) {
//                    rinseLayout.setVisibility(View.GONE);
//                }
//            });
//            APIServiceManage.getInstance().postApdCode("Z3011");
//            saveFaultCodeLocal("Z3011");
        } else if (receiveData.contains(CommandReceiveConfig.MSG_TARGET_TEMPERATURE_SET_FAILED)) {//预加热进程中，设置温度失败
            Log.e("预热返回", "预加热进程中，设置温度失败");
            speak("温度设置失败");
        } else if (receiveData.contains(CommandReceiveConfig.PREHEAT_THERMOSTAT_FAULT)) {//预热返回 : 恒温器故障
            Log.e("预热返回", "加热出现故障");
//            handleDeviceStatus
        } else if (receiveData.contains(CommandReceiveConfig.PREHEAT_TARGET_READY)) {//预热返回 : 找到加热对象
            Log.e("预热返回", "找到加热对象");
            buzzerOff();
            runOnUiThread(() -> {
                if (View.GONE == rinseLayout.getVisibility()) {
                    rinseLayout.setVisibility(View.VISIBLE);
                }
                if (View.GONE == nextRinseLayout.getVisibility()) {
                    nextRinseLayout.setVisibility(View.VISIBLE);
                }
            });
//            preheatStartTemperature();
        } else {
            Log.e("预热返回", "其他--"+receiveData);
        }
    }

    /**
     * 增加目标温度 预热设置温度范围为：34度--40度 ,每次变化0.1 摄氏度℃
     */
    @SuppressLint("SetTextI18n")
    private void addTemperature() {
        if (MyApplication.mTargetTemper > maxTemperature) {
            MyApplication.mTargetTemper = maxTemperature;
        }
        BigDecimal scales = new BigDecimal("0.5");
        BigDecimal currTarget = new BigDecimal(MyApplication.mTargetTemper);

        Log.e("设置温度", "MyApplication.mTargetTemper：" + MyApplication.mTargetTemper);
        if (MyApplication.mTargetTemper < maxTemperature) {
            currTarget = currTarget.add(scales).setScale(1, RoundingMode.HALF_UP);
            Log.e("设置温度", "温度：" + currTarget.floatValue());
            MyApplication.mTargetTemper = currTarget.floatValue();
            tvTargetTemperature.setText(currTarget.floatValue() + "℃");
            PdGoDbManager.getInstance().getTemperatureBoardTable().targetTemper = MyApplication.mTargetTemper;
            preheatSetTemperature();
        }
    }

    /**
     * 减低目标温度 预热设置温度范围为：34度--40度
     */
    private void subtractTemperature() {
        if (MyApplication.mTargetTemper < minTemperature) {
            MyApplication.mTargetTemper = minTemperature;
        }
        BigDecimal scales = new BigDecimal(0.5);
        BigDecimal currTarget = new BigDecimal(MyApplication.mTargetTemper);
//        if (MyApplication.mTargetTemper <= maxTemperature) {
//            MyApplication.mTargetTemper = MyApplication.mTargetTemper - 0.5f;
//        }
        Log.e("设置温度", "MyApplication.mTargetTemper：" + MyApplication.mTargetTemper);
        if (MyApplication.mTargetTemper <= maxTemperature && MyApplication.mTargetTemper > minTemperature) {

            currTarget = currTarget.subtract(scales).setScale(1, BigDecimal.ROUND_HALF_UP);
            Log.e("设置温度", "温度：" + currTarget.floatValue());
            MyApplication.mTargetTemper = currTarget.floatValue();
            tvTargetTemperature.setText(currTarget.floatValue() + "℃");
            PdGoDbManager.getInstance().getTemperatureBoardTable().targetTemper = MyApplication.mTargetTemper;
            preheatSetTemperature();
        }


    }

    @BindView(R.id.rinseLayout)
    LinearLayout rinseLayout;
    private void handleDeviceStatus() {
        Log.e("预热返回", " 温度：" + mReceiveDeviceBean.temp + " ,上位秤重量：" + mReceiveDeviceBean.upper);
        rinseLayout.setOnClickListener(view -> {
            if (mReceiveDeviceBean.temp >= 340) {
//                sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
                doGoCloseTOActivity(PipelineConnectionActivity.class,msg);
            } else {
                showTipsCommonDialog("温度不能低于34摄氏度");
                APIServiceManage.getInstance().postApdCode("Z3015");
            }
        });
        nextRinseLayout.setOnClickListener(view -> {
            if (mReceiveDeviceBean.temp >= 340) {
//                sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
                doGoCloseTOActivity(PipelineConnectionSkipBootActivity.class,msg);
            } else {
                showTipsCommonDialog("温度不能低于34摄氏度");
                APIServiceManage.getInstance().postApdCode("Z3015");
            }
        });
        currTemperature = (float) mReceiveDeviceBean.temp / 10;
        currUpWeight = mReceiveDeviceBean.upper;
//        currDownWeight = MyApplication.getDeviceStatusInfo().lowWeight;
//        Logger.e(MyApplication.getDeviceStatusInfo().toString());
        tvCurrTemperature.setText(String.valueOf(currTemperature));
        if (!isPreheatComplete) {
            //与本次治疗量比较
            compareWeight(currUpWeight);
//            if (!compareWeight(currUpWeight)) {
//                if (mReceiveDeviceBean.temp <= currTemperature * 10 ) {
//                    switchTips(2);
//                    sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
//                }
//            }
        }

//        if ((currTemperature * 10) >= mReceiveDeviceBean.temp) {
//            switchTips(2);
//            CommandDataHelper.getInstance().stopPreheatCmdJson();
//        }

    }


    private void preheatSetTemperature() {
//        sendCommandInterval(CommandSendConfig.PREHEAT_SET + " " + (int) (MyApplication.mTargetTemper * 10), 1000);
//        sendCommandInterval(CommandDataHelper.getInstance().setPreheatCmd((int) (MyApplication.mTargetTemper * 10), PdGoDbManager.getInstance().getMainBoardTable().dialysisValue), 1000);
        sendCommandInterval(CommandDataHelper.getInstance().setTemperatureCmdJson((int) (MyApplication.mTargetTemper * 10)), 1000);
    }

    private void preheatStartTemperature() {
//        sendCommandInterval(CommandSendConfig.PREHEAT_START + " " + (int) (PdGoDbManager.getInstance().getTemperatureBoardTable().targetTemper * 10) + " "
//                + PdGoDbManager.getInstance().getMainBoardTable().dialysisValue, 1000);
        sendCommandInterval(CommandDataHelper.getInstance().setPreheatCmdJson((int) (MyApplication.mTargetTemper * 10), PdproHelper.getInstance().getOtherParamBean().perHeartWeight),500);
    }

    @BindView(R.id.tvWarning)
    TextView tvWarning;
    @BindView(R.id.ivWarning)
    ImageView ivWarning;
    @BindView(R.id.ivTips)
    ImageView ivTips;

    private boolean isNormal;
    private void switchTips(int mType) {

        switch (mType) {
            case 0://警告
                layoutState.setBackgroundResource(R.drawable.icon_pre_heat_warm_bg);
                ivLabel.setBackgroundResource(R.drawable.icon_pre_heat_warm);
                tvTipsTitle.setText("警告！");
                tvTipsContent.setText("您放置分腹透液容量不足，\n请重新放置！");
                APIServiceManage.getInstance().postApdCode("Z3011");
                tvHeartStatus.setText("警告！您放置分腹透液容量不足，请重新放置！");
                tvHeartStatus.setTextColor(Color.RED);
                isNormal = false;
                buzzerOn();
                ivTips.setBackgroundResource(R.drawable.icon_pre_heat_tip_1);
                tvWarning.setText("请勿触碰液体袋和托盘！");
                ivWarning.setBackgroundResource(R.drawable.exclamation);
                break;
            case 1://正常
                layoutState.setBackgroundResource(R.drawable.icon_pre_heat_state_bg);
                ivLabel.setBackgroundResource(R.drawable.icon_pre_heat_state);
                tvTipsTitle.setText("腹透液放置：");
                tvTipsContent.setText("请在透析液托盘上放置一袋\n腹透液");

                tvHeartStatus.setText("机器开始加热，请不要触摸铝板，小心烫伤 ！！！");
                tvHeartStatus.setTextColor(Color.parseColor("#00C6FB"));
//                tvHeartTips.setText("请勿触碰液体袋和托盘！");
//                tvHeartTips.setTextColor(Color.parseColor("#00FF47"));

                ivTips.setBackgroundResource(R.drawable.tip_warn);
                tvWarning.setText("机器开始加热，请不要触摸铝板，小心烫伤 ！！！");
                ivWarning.setBackgroundResource(R.drawable.heating_warning);
                break;
            case 2://预热完成
                APIServiceManage.getInstance().postApdCode("Z3012");
                layoutState.setBackgroundResource(R.drawable.icon_pre_heat_complete_bg);
                ivLabel.setBackgroundResource(R.drawable.icon_pre_heat_state);
                tvTipsTitle.setText("预热完成");
                speak("预热完成");
                tvTipsContent.setText("请留意预热结束提醒！");
                rinseLayout.setVisibility(View.VISIBLE);
//                btnPipelineConnection.setVisibility(View.VISIBLE);
                nextRinseLayout.setVisibility(View.VISIBLE);
                tvHeartStatus.setText("预热完成。请留意预热结束提醒！");
                tvHeartStatus.setTextColor(Color.parseColor("#0089FF"));
//                tvHeartTips.setText("请勿触碰液体袋和托盘！");
//                tvHeartTips.setTextColor(Color.parseColor("#00FF47"));
                isPreheatComplete = true;
                ivTips.setBackgroundResource(R.drawable.icon_pre_heat_tip_1);
                tvWarning.setText("请勿触碰液体袋和托盘！");
                ivWarning.setBackgroundResource(R.drawable.exclamation);
                break;

        }

    }

    /**
     * 比较上位称和治疗量
     *
     * @param upWeight 上位称重量
     * @return
     */
    private boolean compareWeight(int upWeight) {

        int dialysis = PdproHelper.getInstance().getOtherParamBean().perHeartWeight;

        if (preHeatHintDialog == null) {
            preHeatHintDialog = new CommonDialog(PreHeatActivity.this);
        }

        if (upWeight < (dialysis - 5)) {
            //当上位称的重量小于本次治疗量的时候提示用户
            if (View.VISIBLE == rinseLayout.getVisibility()) {
//                rinseLayout.setEnabled(false);
//                rinseLayout.setVisibility(View.GONE);
            }

            //  预热界面时，在进入此界面时10秒钟内未检测到合格的腹透重量在进行提示，
            // 当然这10S内没和格的重量是不能显示结束预热和进行下一步操作及界面的
            if (canShowPreHeatHintDialog) {
//                if (!isShowHintDialog) {
                    //第一次显示
                    preHeatHintDialog
                            .setContent("感应到加热盘上的腹透液小于设定的治疗量，\n请检查处方或者腹透液")
                            .setBtnFirst("确定")
                            .setFirstClickListener(mCommonDialog -> {
                                buzzerOff();
                                mCommonDialog.dismiss();
                            });
                    if (!PreHeatActivity.this.isFinishing()) {
                        preHeatHintDialog.show();
                    }
//                }  //已经处于显示状态

                isShowHintDialog = true;
                switchTips(0);
            }
            return true;
        } else {
            if (!isNormal) {
                buzzerOff();
            }
            isNormal = true;
            switchTips(1);
            if (isShowHintDialog) {
                preHeatHintDialog.dismiss();
            }
            isShowHintDialog = false;
            preHeatHintDialog = null;

            if (View.GONE == rinseLayout.getVisibility()) {
                rinseLayout.setVisibility(View.VISIBLE);
            }
            // 监听到上位称有重量时  开始倒8分钟计时 计算可以判断温度是否达到了目标温度
            startDelayJudgeTargetTemperSubscription();
            return false;
        }

    }

    public void sendShowPreHeatHintDialog(long delaySeconds) {

        mShowPreHeatHintDialogDisposable = Observable.timer(delaySeconds, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //complete
                        canShowPreHeatHintDialog = true;
                    }
                });
        mCompositeDisposable.add(mShowPreHeatHintDialogDisposable);
    }


    /**
     * 延迟判断温度是否到达目标温度
     */
    private void startDelayJudgeTargetTemperSubscription() {

        delayJudgeTargetTemperDisposable = Observable.timer(8, TimeUnit.MINUTES, AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //complete
                        mCompositeDisposable.clear();
                    }
                });

        mCompositeDisposable.add(delayJudgeTargetTemperDisposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
        mCompositeDisposable.dispose();
        mCompositeDisposable.clear();
        compositeDisposable.dispose();
        sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
        exoPlayer.release();
        compositeDisposable.clear();
    }


    @Override
    public void notifyByThemeChanged() {

    }

    /**
     * 执行指令成功
     *
     * @param topic
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
    public void receiveCmdResultOk(String topic) {

        if (topic.contains(CommandSendConfig.METHOD_PREHEAT_START)) {//开始预热
            speak("开始预热,请放置腹透液");
            APIServiceManage.getInstance().postApdCode("Z3010");
            isStartPreheat = true;
        } else if (topic.contains(CommandSendConfig.METHOD_PREHEAT_STOP)) {//停止预热
            isStartPreheat = false;
        }

    }

    /**
     * 单片机返回指令不执行
     *
     * @param bean
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_ERR)
    public void receiveCmdResultErr(ReceiveResultDataBean bean) {
        if (bean.result.topic.contains(CommandSendConfig.METHOD_PREHEAT_START)) {//开始预热
//            showTipsCommonDialog("开始预热异常");
            sendCommandInterval(CommandDataHelper.getInstance().setPreheatCmdJson((int) (MyApplication.mTargetTemper * 10), PdproHelper.getInstance().getOtherParamBean().perHeartWeight),500);
        }
//        else if (topic.contains(CommandSendConfig.METHOD_PREHEAT_STOP)) {//停止预热
////            showTipsCommonDialog("停止预热异常");
//        }
    }

    /**
     * 安装管路
     *
     * @param mPublicJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_PIPECART)
    public void receivePipecartResult(String mPublicJson) {
        ReceivePublicBean mBean = JsonHelper.jsonToClass(mPublicJson, ReceivePublicBean.class);
        if (mBean != null) {
            if (mBean.topic.equals(CommandSendConfig.METHOD_PIPECART_FINISH)) {//
                //安装管路数据: {"publish":{"topic":"pipecart/finish","msg":"success","data":{}},"sign":"a1919ef64ddb0d28310230783dc6e69a"}
                if (mBean.msg.equals(CommandReceiveConfig.MSG_SUCCESS)) {
                    sendToMainBoard(CommandDataHelper.getInstance().setPreheatCmdJson((int) (MyApplication.mTargetTemper * 10), PdproHelper.getInstance().getOtherParamBean().perHeartWeight));
                }
            }
        }

    }

}
