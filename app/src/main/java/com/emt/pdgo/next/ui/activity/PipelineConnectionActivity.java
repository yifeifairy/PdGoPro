package com.emt.pdgo.next.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandReceiveConfig;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.serial.ReceivePublicBean;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.ScreenUtil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PipelineConnectionActivity extends BaseActivity {

    @BindView(R.id.layout_step_1)
    RelativeLayout layoutStep1;
    @BindView(R.id.layout_step_2)
    RelativeLayout layoutStep2;
    @BindView(R.id.layout_step_3)
    RelativeLayout layoutStep3;
    @BindView(R.id.layout_step_4)
    RelativeLayout layoutStep4;
    @BindView(R.id.layout_step_5)
    RelativeLayout layoutStep5;
    @BindView(R.id.layout_step_6)
    RelativeLayout layoutStep6;

    @BindView(R.id.btn_skip_boot)
    StateButton btnSkipBoot;
    @BindView(R.id.btn_enter_prerinse)
    StateButton btnEnterPrerinse;


    @BindView(R.id.btn_pre)
    StateButton btnPre;
    @BindView(R.id.btn_next)
    StateButton btnNext;

    private int currStep = 1;

    private String jumpMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_pipeline_connection);
        ButterKnife.bind(this);
        float[] preRadius = new float[8];
        preRadius[0] = 0f;
        preRadius[1] = 0f;
        preRadius[2] = 100f;
        preRadius[3] = 100f;
        preRadius[4] = 100f;
        preRadius[5] = 100f;
        preRadius[6] = 0f;
        preRadius[7] = 0f;
        btnPre.setRadius(preRadius);

        float[] nextRadius = new float[8];
        nextRadius[0] = 100f;
        nextRadius[1] = 100f;
        nextRadius[2] = 0f;
        nextRadius[3] = 0f;
        nextRadius[4] = 0f;
        nextRadius[5] = 0f;
        nextRadius[6] = 100f;
        nextRadius[7] = 100f;
        btnNext.setRadius(nextRadius);

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        int sw = this.getResources().getConfiguration().smallestScreenWidthDp;
//        Log.e(TAG,"屏幕分辨率:" + width + "*" + height+",density:"+dm.density+",dpi:"+dm.densityDpi+",sw:"+sw);
        if ((480 == width && 800 == height) || (800 == width && 480 == height)) {

        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_next2); //获取图片
            int bounds = ScreenUtil.dip2px(this, 40);
            drawable.setBounds(bounds, bounds, bounds, bounds);  //设置图片参数
            btnSkipBoot.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

//            drawable.setBounds(bounds, bounds, bounds, bounds);  //设置图片参数
            btnEnterPrerinse.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
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

    @BindView(R.id.btn_submit)
    StateButton btnSubmit;
    @BindView(R.id.btn_back)
    StateButton btnBack;
    @Override
    public void initViewData() {
        initHeadTitleBar("管路连接");
        tvTitle.setTextColor(Color.WHITE);
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        btnBack.setOnClickListener(v -> {
            if (isSpeak) {
                toastMessage("正在完成安装卡匣");
            } else {
                if (jumpMsg != null) {
                    if (jumpMsg.equals(EmtConstant.ACTIVITY_PIPELINE_CONNECTION_SKIP_BOOT)) {
                        doGoCloseTOActivity(PreHeatActivity.class,msg);
                    } else if (jumpMsg.equals(EmtConstant.ACTIVITY_PRE_HEAT)) {
                        doGoCloseTOActivity(PreHeatActivity.class,msg);
                    } else {
                        ActivityManager.getActivityManager().removeActivity(this);
                    }
                } else {
                    ActivityManager.getActivityManager().removeActivity(this);
                }
            }
        });
        speak("补液放置");
        APIServiceManage.getInstance().postApdCode("Z4010");
        exoPlayer = new ExoPlayer.Builder(this).build();
        play("android.resource://" + getPackageName() + "/" + R.raw.plc_01);
    }

    @Override
    public void notifyByThemeChanged() {

    }

    private String msg = EmtConstant.ACTIVITY_PIPELINE_CONNECTION;
    @OnClick({R.id.btn_skip_boot, R.id.btn_enter_prerinse, R.id.btn_pre, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pre://上一步
                previousStep();
                break;
            case R.id.btn_next://下一步
                nextStep();
                break;
            case R.id.btn_skip_boot://跳过引导
                pipecartInstall();
                APIServiceManage.getInstance().postApdCode("Z4020");
                speak("跳过引导");
                doGoCloseTOActivity(PipelineConnectionSkipBootActivity.class,msg);
                break;
            case R.id.btn_enter_prerinse://完成，进入预冲
                sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group3"));
                doGoCloseTOActivity(PreRinseActivity.class,msg);
                break;
        }
    }
    private boolean isSpeak;
    private void previousStep() {
        currStep = currStep - 1;
        Log.e("准备工作","当前步骤--上一步--"+currStep);
        switch (currStep) {
            case 1:
                btnPre.setVisibility(View.INVISIBLE);
                layoutStep2.setVisibility(View.GONE);
                layoutStep1.setVisibility(View.VISIBLE);
                speak("补液放置");
                APIServiceManage.getInstance().postApdCode("Z4011");
//                saveFaultCodeLocal("Z4011");
                play("android.resource://" + getPackageName() + "/" + R.raw.plc_01);
                break;
            case 2:
                layoutStep3.setVisibility(View.GONE);
                layoutStep2.setVisibility(View.VISIBLE);
                speak("请插入卡匣");
//                APIServiceManage.getInstance().postApdCode("Z4012");
//                saveFaultCodeLocal("Z4012");
                pipecartInstall();
                play2();
                break;
            case 3:
                layoutStep4.setVisibility(View.GONE);
                layoutStep3.setVisibility(View.VISIBLE);
                speak("连接补液袋");
//                APIServiceManage.getInstance().postApdCode("Z4013");
//                saveFaultCodeLocal("Z4013");
                play3();
                break;
            case 4:
                layoutStep5.setVisibility(View.GONE);
                layoutStep4.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                speak("连接加热袋");
//                APIServiceManage.getInstance().postApdCode("Z4014");
//                saveFaultCodeLocal("Z4014");
                play4();
                break;
            case 5:
                layoutStep6.setVisibility(View.GONE);
                layoutStep5.setVisibility(View.VISIBLE);
//                layoutNextStep.setVisibility(View.VISIBLE);
                btnEnterPrerinse.setVisibility(View.GONE);
//                btnSkipBoot.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                speak("连接废液袋");
                play5();
                break;
        }
    }

    @BindView(R.id.videoImage)
    ImageView videoImage;

    @BindView(R.id.useless_ll01)
    LinearLayout useless_ll01;
    @BindView(R.id.useless_ll02)
    LinearLayout useless_ll02;

    private void nextStep() {
        currStep = currStep + 1;
        Log.e("准备工作","当前步骤--下一步--"+currStep);
        switch (currStep) {
            case 2:
                layoutStep1.setVisibility(View.GONE);
                layoutStep2.setVisibility(View.VISIBLE);
//                layoutPreviousStep.setVisibility(View.VISIBLE);
//                btnSkipBoot.setVisibility(View.VISIBLE);
//                
//                pipecartInstall();
                sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group1"));
                sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group2"),1000);
                APIServiceManage.getInstance().postApdCode("Z4012");
                speak("请插入卡匣");
                play2();
                break;
            case 3:
                layoutStep2.setVisibility(View.GONE);
                layoutStep3.setVisibility(View.VISIBLE);
                speak("连接补液袋");
//                pipecartInstall();
//                pipecartFinish();
                sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group1"));
                sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group2"),200);

                APIServiceManage.getInstance().postApdCode("Z4013");
                play3();
                break;
            case 4:
                layoutStep3.setVisibility(View.GONE);
                layoutStep4.setVisibility(View.VISIBLE);
                speak("连接加热袋");
                APIServiceManage.getInstance().postApdCode("Z4014");
                play4();
                break;
            case 5:
                layoutStep4.setVisibility(View.GONE);
                layoutStep5.setVisibility(View.VISIBLE);
                speak("连接废液袋");
                APIServiceManage.getInstance().postApdCode("Z4016");
                play5();
                break;
            case 6:
                btnSkipBoot.setVisibility(View.GONE);
                btnEnterPrerinse.setVisibility(View.VISIBLE);
//                layoutNextStep.setVisibility(View.GONE);
                btnNext.setVisibility(View.INVISIBLE);
                layoutStep5.setVisibility(View.GONE);
                layoutStep6.setVisibility(View.VISIBLE);
                speak("废液卡管");
                sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group3"));
                APIServiceManage.getInstance().postApdCode("Z4015");
                play6();
                break;
        }
    }

    private void play4() {
        setUseless(false);
        videoImage.setImageResource(R.drawable.conn_supply);
        videoView.setPlayer(exoPlayer);
        MediaItem mediaItem1 = MediaItem.fromUri("android.resource://" + getPackageName() + "/" + R.raw.plc_0402);
//        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setMediaItem(mediaItem1);
        exoPlayer.prepare();
        exoPlayer.play();
    }
    private void play6() {
        setUseless(true);
//        play("android.resource://" + getPackageName() + "/" + R.raw.plc_05);
        videoView.setPlayer(exoPlayer);

//                MediaItem mediaItem = MediaItem.fromUri(url);
        MediaItem item1 = MediaItem.fromUri("android.resource://" + getPackageName() + "/" + R.raw.plc_06);
        exoPlayer.setMediaItem(item1);
        exoPlayer.prepare();
        exoPlayer.play();
    }
    private void play5() {
        setUseless(false);
        videoImage.setImageResource(R.drawable.conn_lazy);
//        exoPlayer = new ExoPlayer.Builder(this).build();
        videoView.setPlayer(exoPlayer);
        MediaItem mediaItem1 = MediaItem.fromUri("android.resource://" + getPackageName() + "/" + R.raw.plc_0502);
//        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setMediaItem(mediaItem1);
        exoPlayer.prepare();
        exoPlayer.play();
    }
    private void play3() {
        setUseless(false);
        videoImage.setImageResource(R.drawable.conn_heart);
//        exoPlayer = new ExoPlayer.Builder(this).build();
        videoView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri("android.resource://" + getPackageName() + "/" + R.raw.plc_03);
        exoPlayer.setMediaItem(mediaItem);
//        exoPlayer.addMediaItem(mediaItem1);
        exoPlayer.prepare();
        exoPlayer.play();
    }
    private void play2() {
//        exoPlayer = new ExoPlayer.Builder(this).build();
        setUseless(true);
        videoView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri("android.resource://" + getPackageName() + "/" + R.raw.plc_02);
        exoPlayer.setMediaItem(mediaItem);
//        exoPlayer.addMediaItem(mediaItem1);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void setUseless(boolean useless) {
        useless_ll01.setVisibility(useless ? View.GONE : View.INVISIBLE);
        useless_ll02.setVisibility(useless ? View.GONE : View.INVISIBLE);
        videoImage.setVisibility(useless ? View.GONE : View.VISIBLE);
    }

    private ExoPlayer exoPlayer;
    @BindView(R.id.videoView)
    StyledPlayerView videoView;
    private void play(String url) {
//        exoPlayer = new ExoPlayer.Builder(this).build();
//        String uri = "android.resource://" + getPackageName() + "/" + R.raw.text;
        setUseless(true);
        videoView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void pipecartInstall() {
        Log.e("安装管路","是否安装完成--"+MyApplication.isPipecartInstall);
        if (!MyApplication.isPipecartInstall) {
            MyApplication.isPipecartInstall = true;
            sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL));//调用装管路
        }
    }

    private void pipecartFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_FINISH),500);
            }
        });

    }

    /**
     * 执行指令成功
     *
     * @param topic
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
    public void receiveCmdResultOk(String topic) {
        //{"publish":{"topic":"pipecart/install","msg":"ready","data":{}},"sign":"7c1093f2df698d9376ffa556a88dee01"}
        //安装管路数据: {"publish":{"topic":"pipecart/finish","msg":"success","data":{}},"sign":"a1919ef64ddb0d28310230783dc6e69a"}
        if (topic.contains(CommandSendConfig.METHOD_PIPECART_INSTALL)) {//调用装管路
            Log.e("执行指令成功","调用装管路");
        } else if (topic.contains(CommandSendConfig.METHOD_PIPECART_FINISH)) {//调用装管路完成
            Log.e("执行指令成功","调用装管路完成");
        } if (topic.contains("manualrinse/start")) { // 开始手动预冲

        }

    }

    /**
     * 安装管路
     *
     * @param mPublicJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_PIPECART)
    public void receivePipecartResult(String mPublicJson) {
        Log.e("安装管路界面", "" + mPublicJson);
        ReceivePublicBean mBean = JsonHelper.jsonToClass(mPublicJson, ReceivePublicBean.class);
        if (mBean != null) {
            if (mBean.topic.equals(CommandSendConfig.METHOD_PIPECART_INSTALL)) {//调用装管路
                //{"publish":{"topic":"pipecart/install","msg":"ready","data":{}},"sign":"7c1093f2df698d9376ffa556a88dee01"}
                if (mBean.msg.equals(CommandReceiveConfig.MSG_READY)) {
                    Log.e("安装管路", "ready");
                } else if (mBean.msg.equals("valve open exception")){
                    Log.e("安装管路","msg--"+mBean.msg);
                    MyApplication.isPipecartInstall = false;
                    speak("阀门开启异常");
                    showTipsCommonDialog("阀门开启异常");
                }
            } else if (mBean.topic.equals(CommandSendConfig.METHOD_PIPECART_FINISH)) {//
                //安装管路数据: {"publish":{"topic":"pipecart/finish","msg":"success","data":{}},"sign":"a1919ef64ddb0d28310230783dc6e69a"}
                if (mBean.msg.equals(CommandReceiveConfig.MSG_SUCCESS)) {
                    Log.e("安装管路", "success");
                    dismissLoading();
//                    speak("安装管路成功");
                    APIServiceManage.getInstance().postApdCode("Z4019");
//                    saveFaultCodeLocal("Z4019");
//                    doGoCloseTOActivity(PreRinseActivity.class,msg);
                } else {
                    dismissLoading();
                    Log.e("安装管路","安装管路失败");
                    speak("管路安装失败");
                    APIServiceManage.getInstance().postApdCode("Z4031");
//                    saveFaultCodeLocal("Z4031");
                    MyApplication.isPipecartInstall = false;
                    showTipsCommonDialog("管路安装失败,请检查管路是否连接成功");
//                    showTipsCommonDialog("安装管路失败");
                }
            }
        }

    }


    //    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_ERR)
//    public void receiveCmdResultErr(ReceiveResultDataBean bean) {
//        String topic = bean.result.topic;
//        if (topic.contains(CommandSendConfig.METHOD_PIPECART_INSTALL)) {//调用装管路
////            showTipsCommonDialog("装管路异常");
////            speak("装管路异常");
//            if (bean.result.msg.equals("preheat task is running")) {
//                sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
//            }
//            MyApplication.isPipecartInstall = false;
//        } else if (topic.contains(CommandSendConfig.METHOD_PIPECART_FINISH)) {//调用装管路完成
//            showTipsCommonDialog("装管路完成异常,请检查卡匣是否插入");
//            MyApplication.isPipecartInstall = false;
//            speak("装管路完成异常,请检查卡匣是否插入");
//        }
//    }
    /**
     * 单片机返回指令不执行
     *
     * @param bean
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_ERR)
    public void receiveCmdResultErr(ReceiveResultDataBean bean) {
        String topic = bean.result.topic;
        if (topic.contains(CommandSendConfig.METHOD_PIPECART_INSTALL)) {//调用装管路
//            showTipsCommonDialog("装管路异常");
            if (bean.result.msg.equals("preheat task is running")) {
                sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL),500);

            }
        } else if (topic.contains(CommandSendConfig.METHOD_PIPECART_FINISH)) {//调用装管路完成
//            showTipsCommonDialog("装管路完成异常");
            sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL),500);//调用装管路完成
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("管路连接", "onDestroy");
        RxBus.get().unRegister(this);
        MyApplication.isPipecartInstall = false;
        exoPlayer.release();
    }

}