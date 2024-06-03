package com.emt.pdgo.next.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandReceiveConfig;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.serial.ReceivePublicBean;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.ScreenUtil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PipelineConnectionSkipBootActivity extends BaseActivity {

    @BindView(R.id.btn_enter_prerinse)
    Button btnEnterPrerinse;

    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.iv_4)
    ImageView iv4;
    @BindView(R.id.iv_5)
    ImageView iv5;
    @BindView(R.id.iv_6)
    ImageView iv6;

    private boolean isSpeak;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_pipeline_connection_skip_boot);
        ButterKnife.bind(this);
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        int sw = this.getResources().getConfiguration().smallestScreenWidthDp;
//        Log.e(TAG,"屏幕分辨率:" + width + "*" + height+",density:"+dm.density+",dpi:"+dm.densityDpi+",sw:"+sw);
        if ((480 == width && 800 == height) || (800 == width && 480 == height)) {

        } else {
            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.mipmap.icon_next2); //获取图片
            int bounds = ScreenUtil.dip2px(this, 40);
            drawable.setBounds(bounds, bounds, bounds, bounds);  //设置图片参数
            btnEnterPrerinse.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    @Override
    public void registerEvents() {
        RxBus.get().register(this);
    }

    private String msg = EmtConstant.ACTIVITY_PIPELINE_CONNECTION_SKIP_BOOT;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.title)
    TextView title;
    private String jumpMsg;

    @Override
    public void initViewData() {
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        title.setText("请确认已正确完成以下操作");
        btnBack.setOnClickListener(v -> {
//            sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson("pipecart/finish"));
//            if (isSpeak) {
//                toastMessage("正在完成安装卡匣");
//            } else {
            if (jumpMsg == null) {
                onBackPressed();
            } else {
                switch (jumpMsg) {
                    case EmtConstant.ACTIVITY_PRE_HEAT:
                    case EmtConstant.ACTIVITY_PRE_RINSE:
//                    sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson("pipecart/install"));
                        doGoCloseTOActivity(PreHeatActivity.class, msg);
                        break;
                    case EmtConstant.ACTIVITY_PIPELINE_CONNECTION:
                        doGoCloseTOActivity(PipelineConnectionActivity.class, msg);
                        break;
                    default:
                        ActivityManager.getActivityManager().removeActivity(this);
                        break;
                }
            }
//            }
//            onBackPressed();
//            doGoCloseTOActivity(PipelineConnectionActivity.class,"");
        });
        btnEnterPrerinse.setEnabled(false);
        countDownTimer.start();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL));//调用装管路
//                speak("请插入卡匣，安装管路,未连接的管路请关闭管夹");
//            }
//        },EmtConstant.DELAY_TIME);
        toEngAc(title);
        speak("关闭所有管夹,插入卡匣,安装好所有管路并放置腹透液,废液卡管");
        Glide.with(this).asGif().skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).load(R.raw.supply).into(iv1);
        Glide.with(this).asGif().skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).load(R.raw.insert_cass).into(iv2);
        Glide.with(this).asGif().skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).load(R.raw.conn_supply).into(iv3);
        Glide.with(this).asGif().skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).load(R.raw.conn_preheart).into(iv4);
        Glide.with(this).asGif().skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).load(R.raw.conn_final).into(iv5);
        Glide.with(this).asGif().skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).load(R.raw.stuck).into(iv6);
        sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group1"),1000);
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group2"),1000);
//                speak("请取出卡匣,关闭所有管夹");
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group3"),1000);
//        sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL), 1800);//调用装管路
    }

    private final CountDownTimer countDownTimer = new CountDownTimer(10 * 1000,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            btnEnterPrerinse.setEnabled(true);
            btnEnterPrerinse.setBackgroundResource(R.drawable.bg_round_blue);
        }
    };

    @Override
    public void notifyByThemeChanged() {

    }

//    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_STOP_HEART)
//    public void receive(ReceiveResultDataBean dataBean) {
//        sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL));
//    }

    @OnClick({R.id.btn_enter_prerinse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_enter_prerinse://完成，进入预冲
//                speak("安装卡匣完成,请稍等");
//                if (!isSpeak) {
//                    isSpeak = true;
//                    speak("安装卡匣完成,请稍等");
//                } else {
//                    toastMessage("正在安装卡匣,请稍等");
//                }
//                sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group1"));
//                sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group2"),500);
////                speak("请取出卡匣,关闭所有管夹");
//                sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group3"),500);
//                pipecartFinish();
                doGoCloseTOActivity(PreRinseActivity.class,msg);
                break;
        }
    }

    private void pipecartFinish() {
//        if (MyApplication.isPipecartInstall) {
////            speak("安装卡匣完成,请稍等");
//            sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_FINISH));//调用装管路完成
////            showLoading("安装卡匣完成，请稍等...");
//        } else {
////            dismissLoading();
//            sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_FINISH));//调用装管路完成
//        }
        sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_FINISH), 500);
    }

    private boolean isSend = false;

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
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        CommonDialog mCommonDialog = new CommonDialog(PipelineConnectionSkipBootActivity.this);
//
//                        mCommonDialog.setContent("加热还未正常停止")
//                                .setBtnFirst("重新安装")
//                                .setFirstClickListener(new CommonDialog.OnCommonClickListener() {
//                                    @Override
//                                    public void onClick(CommonDialog mCommonDialog) {
//                                        sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL));//调用装管路
//                                        mCommonDialog.dismiss();
//                                    }
//                                })
//                                .setTwoClickListener(Dialog::dismiss)
//                                .show();
//                    }
//                });
                sendCommandInterval(CommandDataHelper.getInstance().stopPreheatCmdJson(), 500);
                if (!isSend && !isSpeak) {
                    isSend = true;
//                sendCommandInterval(CommandDataHelper.getInstance().stopPreheatCmdJson(), 500);
                    sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_FINISH), 1500);
                }
            }
        } else if (topic.contains(CommandSendConfig.METHOD_PIPECART_FINISH)) {//调用装管路完成
//            showTipsCommonDialog("装管路完成异常");
            sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL), 500);//调用装管路完成
        }
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
            if (isSpeak) {
                sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_FINISH), 500);
            } else {
                speak("请插入卡匣，安装管路,未连接的管路请关闭管夹");
            }
        } else if (topic.contains(CommandSendConfig.METHOD_PIPECART_FINISH)) {//调用装管路完成

        } else if (topic.contains("valve/open")) {

        }
//        else if (topic.contains("preheat/stop")) {
//            sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson("pipecart/install"));
//        }

    }

    /**
     * 安装管路
     *
     * @param mPublicJson
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_PIPECART)
    public void receivePipecartResult(String mPublicJson) {
        Log.e("跳过引导安装管路界面", "" + mPublicJson);
        ReceivePublicBean mBean = JsonHelper.jsonToClass(mPublicJson, ReceivePublicBean.class);
        if (mBean != null) {
            if (mBean.topic.equals(CommandSendConfig.METHOD_PIPECART_FINISH)) {//
                //安装管路数据: {"publish":{"topic":"pipecart/finish","msg":"success","data":{}},"sign":"a1919ef64ddb0d28310230783dc6e69a"}
                if (mBean.msg.equals(CommandReceiveConfig.MSG_SUCCESS)) {
                    Log.e("安装管路", "success");
//                    dismissLoading();
                    APIServiceManage.getInstance().postApdCode("Z4019");
                    doGoCloseTOActivity(PreRinseActivity.class, EmtConstant.ACTIVITY_PIPELINE_CONNECTION_SKIP_BOOT);
                } else if (mBean.msg.equals("valve close exception")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isSpeak = false;
                            APIServiceManage.getInstance().postApdCode("Z4031");
                            showTipsCommonDialog("阀门关闭故障");
                        }
                    });
                } else {
//                    dismissLoading();
                    Log.e("安装管路", "安装管路失败");
                    speak("安装管路失败");
                    showTipsCommonDialog("安装管路失败");
                }
            } else if (mBean.topic.equals("pipecart/install") && mBean.msg.equals("valve open exception")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isSpeak = false;
                        showTipsCommonDialog("阀门开启故障");
                    }
                });
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
//        Glide.with(this).pauseAllRequests();
    }

}
