package com.emt.pdgo.next.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PipelineConnectionSkipBootActivity extends BaseActivity {

    @BindView(R.id.btn_enter_prerinse)
    StateButton btnEnterPrerinse;

    private boolean isSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initAllViews();
//        registerEvents();
//        initViewData();
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
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_next2); //获取图片
            int bounds = ScreenUtil.dip2px(this, 40);
            drawable.setBounds(bounds, bounds, bounds, bounds);  //设置图片参数
            btnEnterPrerinse.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
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

    private String msg = EmtConstant.ACTIVITY_PIPELINE_CONNECTION_SKIP_BOOT;
    @BindView(R.id.btn_back)
    StateButton btnBack;
    private String jumpMsg;

    @Override
    public void initViewData() {
        initHeadTitleBar("请确认已正确 完成以下操作");
//        speak("请插入卡匣，安装管路,未连接的管路请关闭管夹");
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        btnBack.setOnClickListener(v -> {
//            sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson("pipecart/finish"));
            if (isSpeak) {
                toastMessage("正在完成安装卡匣");
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
//            doGoCloseTOActivity(PipelineConnectionActivity.class,"");
        });

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL));//调用装管路
//                speak("请插入卡匣，安装管路,未连接的管路请关闭管夹");
//            }
//        },EmtConstant.DELAY_TIME);
//        speak("请插入卡匣，安装管路,未连接的管路请关闭管夹");
        sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL), 1800);//调用装管路
    }


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
                if (!isSpeak) {
                    isSpeak = true;
                    speak("安装卡匣完成,请稍等");
                } else {
                    toastMessage("正在安装卡匣,请稍等");
                }
                pipecartFinish();
                break;
            case R.id.btn_back:
                ActivityManager.getActivityManager().removeActivity(this);
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
    }

}
