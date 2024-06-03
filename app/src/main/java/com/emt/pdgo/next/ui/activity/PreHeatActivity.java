package com.emt.pdgo.next.ui.activity;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandReceiveConfig;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.serial.ReceivePublicBean;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class PreHeatActivity extends BaseActivity {

    private final String TAG = "PreHeatActivity";

    @BindView(R.id.currTemp)
    TextView currTemp;
    @BindView(R.id.targetTemp)
    TextView targetTemp;

    @BindView(R.id.addTemp)
    ImageView addTemp;
    @BindView(R.id.delTemp)
    ImageView delTemp;

    @BindView(R.id.guideIv)
    ImageView guideIv;
    @BindView(R.id.noGuideIv)
    ImageView noGuideIv;

    @BindView(R.id.tipView)
    TextView tipView;

    private float maxTemperature = 40.0f;
    private float minTemperature = 34.0f;

    private float currTemperature;

    private CommonDialog preHeatHintDialog;

    private CompositeDisposable compositeDisposable;

    private boolean isPreheatComplete;//预热完成

    private String jumpMsg;

    private String msg = EmtConstant.ACTIVITY_PRE_HEAT;

    @BindView(R.id.heartIv)
    ImageView heartIv;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_pre_heat);
        ButterKnife.bind(this);
        speak("开始预热,请关闭管夹,放置腹透液");
        compositeDisposable = new CompositeDisposable();
    }

    private int temp;
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {
            Log.e("预热返回", " 温度：" + mReceiveDeviceBean.temp + " ,上位秤重量：" + mReceiveDeviceBean.upper);
            temp = mReceiveDeviceBean.temp - 20;
            currTemperature = (float) temp / 10;
            if (!MyApplication.fistHeat) {
                currTemp.setText("33.0");
            } else {
                currTemp.setText(String.valueOf(currTemperature));
            }
            if (!isPreheatComplete) {
                //与本次治疗量比较
                compareWeight(mReceiveDeviceBean.upper - PdproHelper.getInstance().getOtherParamBean().upper);
            }
        });
    }
    @Override
    public void registerEvents() {
        RxBus.get().register(this);
        addTemp.setOnClickListener(view -> {
            addTemperature();
            countDownTimer.start();
        });
        delTemp.setOnClickListener(view -> {
            subtractTemperature();
            countDownTimer.start();
        });
        guideIv.setOnClickListener(view -> {
            Log.e("加热模块","debug:"+PdproHelper.getInstance().getOtherParamBean().isDebug);
            if (PdproHelper.getInstance().getOtherParamBean().isDebug) {
                doGoCloseTOActivity(PipelineConnectionActivity.class, msg);

            } else {
                if (temp >= 340) {
//                sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
                    doGoCloseTOActivity(PipelineConnectionActivity.class, msg);
                } else {
                    showTipsCommonDialog("温度不能低于34摄氏度");
                    APIServiceManage.getInstance().postApdCode("Z3015");
                }
            }
        });
        noGuideIv.setOnClickListener(view -> {
            if (PdproHelper.getInstance().getOtherParamBean().isDebug) {
                doGoCloseTOActivity(PipelineConnectionSkipBootActivity.class, msg);
            } else {
                if (temp >= 340) {
//                sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
                    doGoCloseTOActivity(PipelineConnectionSkipBootActivity.class, msg);
                } else {
                    showTipsCommonDialog("温度不能低于34摄氏度");
                    APIServiceManage.getInstance().postApdCode("Z3015");
                }
            }
        });

    }

    private final CountDownTimer countDownTimer = new CountDownTimer(500,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            addTemp.setEnabled(true);
            delTemp.setEnabled(true);
        }
    };

    @BindView(R.id.btnBack)
    Button btnBack;

//    private float targetTemper = 37.0f;
    @SuppressLint("SetTextI18n")
    @Override
    public void initViewData() {
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        targetTemp.setText(PdproHelper.getInstance().targetTemper() + "℃");
        if (!MyApplication.fistHeat) {
            if (PdproHelper.getInstance().getOtherParamBean().isDebug) {
                noGuideIv.setVisibility(View.VISIBLE);
                guideIv.setVisibility(View.VISIBLE);
            }
            stageTimer.start();
        } else {
            preheatStartTemperature();
            noGuideIv.setVisibility(View.VISIBLE);
            guideIv.setVisibility(View.VISIBLE);
        }
        init();
        Glide.with(this).asGif().load(R.raw.pre_heart).skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(heartIv);
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    private final CountDownTimer stageTimer = new CountDownTimer(90 * 1000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            MyApplication.fistHeat = true;
            preheatStartTemperature();
            noGuideIv.setVisibility(View.VISIBLE);
            guideIv.setVisibility(View.VISIBLE);
        }
    };

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
        if (compositeDisposable== null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposableObserver);
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_PREHEAT_DATA)
    public void receivePreHeatData(String receiveData) {
        if (receiveData.contains(CommandReceiveConfig.PREHEAT_FINISH)) {//预热返回 :  加热到的设置目标值
            runOnUiThread(() -> {
                switchTips(2);
                //预热完成
            });
            Log.e("预热返回", "加热到的设置目标值：" + receiveData);
        } else if (receiveData.contains(CommandReceiveConfig.PREHEAT_NO_HEATING_TARGET)) {//预热返回 : 无加热对象，提示需要放置液袋到加热托盘上
            Log.e("预热返回", "无加热对象，提示需要放置液袋到加热托盘上");
        } else if (receiveData.contains(CommandReceiveConfig.MSG_TARGET_TEMPERATURE_SET_FAILED)) {//预加热进程中，设置温度失败
            Log.e("预热返回", "预加热进程中，设置温度失败");
        } else if (receiveData.contains(CommandReceiveConfig.PREHEAT_THERMOSTAT_FAULT)) {//预热返回 : 恒温器故障
            Log.e("预热返回", "加热出现故障");
        } else if (receiveData.contains(CommandReceiveConfig.PREHEAT_TARGET_READY)) {//预热返回 : 找到加热对象
            Log.e("预热返回", "找到加热对象");
//            buzzerOff();
            runOnUiThread(() -> {

            });
        } else {
            Log.e("预热返回", "其他--"+receiveData);
        }
    }

    /**
     * 增加目标温度 预热设置温度范围为：34度--40度 ,每次变化0.1 摄氏度℃
     */
    @SuppressLint("SetTextI18n")
    private void addTemperature() {
        if (PdproHelper.getInstance().targetTemper() > maxTemperature) {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.targetTemper, maxTemperature+"");
        }
        BigDecimal scales = new BigDecimal("0.5");
        BigDecimal currTarget = new BigDecimal(PdproHelper.getInstance().targetTemper() );

        Log.e("设置温度", "targetTemper：" + PdproHelper.getInstance().targetTemper() );
        if (PdproHelper.getInstance().targetTemper()  < maxTemperature) {
            currTarget = currTarget.add(scales).setScale(1, RoundingMode.HALF_UP);
            Log.e("设置温度", "温度：" + currTarget.floatValue());
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.targetTemper, currTarget.floatValue()+"");
            targetTemp.setText(currTarget.floatValue() + "℃");
            preheatSetTemperature();
        }
    }

    /**
     * 减低目标温度 预热设置温度范围为：34度--40度
     */
    private void subtractTemperature() {
        if (PdproHelper.getInstance().targetTemper() < minTemperature) {
//            targetTemper = minTemperature;
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.targetTemper, minTemperature+"");
        }
        BigDecimal scales = new BigDecimal("0.5");
        BigDecimal currTarget = new BigDecimal(PdproHelper.getInstance().targetTemper());
        Log.e("设置温度", "targetTemper：" + PdproHelper.getInstance().targetTemper());
        if (PdproHelper.getInstance().targetTemper() <= maxTemperature && PdproHelper.getInstance().targetTemper() > minTemperature) {
            currTarget = currTarget.subtract(scales).setScale(1, BigDecimal.ROUND_HALF_UP);
            Log.e("设置温度", "温度：" + currTarget.floatValue());
//            targetTemper = currTarget.floatValue();
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.targetTemper, currTarget.floatValue()+"");
            targetTemp.setText(currTarget.floatValue() + "℃");

            preheatSetTemperature();
        }
    }

    private void preheatSetTemperature() {
        sendCommandInterval(CommandDataHelper.getInstance().setTemperatureCmdJson((int) (PdproHelper.getInstance().targetTemper() * 10 + 20)), 1000);
    }

    private void preheatStartTemperature() {
        sendCommandInterval(CommandDataHelper.getInstance().setPreheatCmdJson((int) (PdproHelper.getInstance().targetTemper() * 10 + 20), PdproHelper.getInstance().getOtherParamBean().perHeartWeight),500);
    }

    private boolean isNormal;
    private void switchTips(int mType) {

        switch (mType) {
            case 0://警告
//                layoutState.setBackgroundResource(R.drawable.icon_pre_heat_warm_bg);
//                ivLabel.setBackgroundResource(R.drawable.icon_pre_heat_warm);
//                tvTipsTitle.setText("警告！");
                tipView.setText("您放置分腹透液容量不足,请重新放置!");
                APIServiceManage.getInstance().postApdCode("Z3011");
//                tvHeartStatus.setText("警告！您放置分腹透液容量不足，请重新放置！");
//                tvHeartStatus.setTextColor(Color.RED);
                isNormal = false;
                buzzerOn();
//                ivTips.setBackgroundResource(R.drawable.icon_pre_heat_tip_1);
//                tipView.setText("请勿触碰液体袋和托盘!");
//                ivWarning.setBackgroundResource(R.drawable.exclamation);
                break;
            case 1://正常
//                layoutState.setBackgroundResource(R.drawable.icon_pre_heat_state_bg);
//                ivLabel.setBackgroundResource(R.drawable.icon_pre_heat_state);
//                tvTipsTitle.setText("腹透液放置：");
//                tvTipsContent.setText("请在透析液托盘上放置一袋\n腹透液");
//                tvHeartStatus.setText("机器开始加热，请不要触摸铝板，小心烫伤 ！！！");
//                tvHeartStatus.setTextColor(Color.parseColor("#00C6FB"));
//                ivTips.setBackgroundResource(R.drawable.tip_warn);
                tipView.setText("机器开始加热,请不要触摸铝板,小心烫伤!!!");
//                ivWarning.setBackgroundResource(R.drawable.heating_warning);
                break;
            case 2://预热完成
                APIServiceManage.getInstance().postApdCode("Z3012");
//                layoutState.setBackgroundResource(R.drawable.icon_pre_heat_complete_bg);
//                ivLabel.setBackgroundResource(R.drawable.icon_pre_heat_state);
//                tvTipsTitle.setText("预热完成");
                speak("预热完成");
//                tvTipsContent.setText("请留意预热结束提醒！");
//                rinseLayout.setVisibility(View.VISIBLE);
////                btnPipelineConnection.setVisibility(View.VISIBLE);
//                nextRinseLayout.setVisibility(View.VISIBLE);
                tipView.setText("预热完成.请留意预热结束提醒!");
//                tvHeartStatus.setTextColor(Color.parseColor("#0089FF"));
////                tvHeartTips.setText("请勿触碰液体袋和托盘！");
//                tvHeartTips.setTextColor(Color.parseColor("#00FF47"));
                isPreheatComplete = true;
//                ivTips.setBackgroundResource(R.drawable.icon_pre_heat_tip_1);
//                tvWarning.setText("请勿触碰液体袋和托盘！");
//                ivWarning.setBackgroundResource(R.drawable.exclamation);
                break;
            case 3://正常
//                layoutState.setBackgroundResource(R.drawable.icon_pre_heat_state_bg);
//                ivLabel.setBackgroundResource(R.drawable.icon_pre_heat_state);
//                tvTipsTitle.setText("腹透液放置：");
//                tvTipsContent.setText("请在透析液托盘上放置一袋\n腹透液");
//                tvHeartStatus.setText("机器开始加热，请不要触摸铝板，小心烫伤 ！！！");
//                tvHeartStatus.setTextColor(Color.parseColor("#00C6FB"));
//                ivTips.setBackgroundResource(R.drawable.tip_warn);
                tipView.setText("平衡温度中...");
//                ivWarning.setBackgroundResource(R.drawable.heating_warning);
                break;

        }

    }

    /**
     * 比较上位称和治疗量
     *
     * @param upWeight 上位称重量
     */
    private void compareWeight(int upWeight) {
        int dialysis;
        if (MyApplication.apdMode == 1) {
            dialysis = PdproHelper.getInstance().ipdBean().firstPerfusionVolume == 0? PdproHelper.getInstance().ipdBean().perCyclePerfusionVolume :PdproHelper.getInstance().ipdBean().firstPerfusionVolume ;
        } else if (MyApplication.apdMode == 2) {
            dialysis = PdproHelper.getInstance().tpdBean().perCyclePerfusionVolume;
        } else if (MyApplication.apdMode == 3) {
            dialysis = PdproHelper.getInstance().ccpdBean().perCyclePerfusionVolume;
        } else if (MyApplication.apdMode == 4) {
            dialysis = PdproHelper.getInstance().aapdBean().bagVol;
        } else if (MyApplication.apdMode == 7 ) {
            dialysis = PdproHelper.getInstance().kidBean().perCyclePerfusionVolume;
        } else if (MyApplication.apdMode == 8) {
            dialysis = PdproHelper.getInstance().expertBean().cycleVol;
        } else {
            dialysis = 2000;
        }
        if (preHeatHintDialog == null) {
            preHeatHintDialog = new CommonDialog(PreHeatActivity.this);
        }
        if (upWeight < (dialysis - 5)) {
            //当上位称的重量小于本次治疗量的时候提示用户
            if (!preHeatHintDialog.isShowing()) {
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
                switchTips(0);
            }
        } else {
            if (!isNormal) {
                buzzerOff();
            }
            isNormal = true;
            switchTips(MyApplication.fistHeat ? 1 : 3);
            if (preHeatHintDialog != null && preHeatHintDialog.isShowing()) {
                preHeatHintDialog.dismiss();
            }
        }

    }

    @Override
    protected void onDestroy() {
        sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
        RxBus.get().unRegister(this);
        compositeDisposable.dispose();
//        Glide.with(this).pauseRequests();
        compositeDisposable.clear();
        if (stageTimer != null) {
            stageTimer.cancel();
        }
        super.onDestroy();

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
            APIServiceManage.getInstance().postApdCode("Z3010");

        } else if (topic.contains(CommandSendConfig.METHOD_PREHEAT_STOP)) {//停止预热

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
            sendCommandInterval(CommandDataHelper.getInstance().setPreheatCmdJson((int) (PdproHelper.getInstance().targetTemper() * 10), PdproHelper.getInstance().getOtherParamBean().perHeartWeight),500);
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
                    sendToMainBoard(CommandDataHelper.getInstance().setPreheatCmdJson((int) (PdproHelper.getInstance().targetTemper() * 10), PdproHelper.getInstance().getOtherParamBean().perHeartWeight));
                }
            }
        }

    }

}
