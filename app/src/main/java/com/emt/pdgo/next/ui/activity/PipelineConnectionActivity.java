package com.emt.pdgo.next.ui.activity;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PipelineConnectionActivity extends BaseActivity {

    @BindView(R.id.previousIv)
    ImageView previousIv;
    @BindView(R.id.nextIv)
    ImageView nextIv;
    @BindView(R.id.tipTv)
    TextView tipTv;
    @BindView(R.id.detailTv)
    TextView detailTv;
    @BindView(R.id.btnSkip)
    Button btnSkip;

    private int currStep = 1;

    private String jumpMsg;

    @BindView(R.id.imageview)
    ImageView imageView;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_pipeline_connection);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {
        RxBus.get().register(this);
        boolean isFinalSupply = false;
        switch (MyApplication.apdMode) {
            case 1:
                isFinalSupply = PdproHelper.getInstance().ipdBean().isFinalSupply;
                break;
            case 4:
                isFinalSupply = PdproHelper.getInstance().aapdBean().isFinalSupply;
                break;
            case 8:
                isFinalSupply = PdproHelper.getInstance().expertBean().isFinalSupply;
                break;
        }
        Glide.with(this).asGif().load(R.raw.supply).skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        btnSkip.setOnClickListener(view -> {
            doGoCloseTOActivity(PipelineConnectionSkipBootActivity.class,msg);
        });
        sendCommandInterval(CommandDataHelper.getInstance().stopPreheatCmdJson(),500);
        boolean finalIsFinalSupply = isFinalSupply;
        previousIv.setOnClickListener(view -> {
            nextIv.setEnabled(false);
            previousIv.setEnabled(false);
            countDownTimer.start();
            currStep --;
            Log.e("上一步","当前步骤:"+currStep);
            switch (currStep) {
                case 0:
//                    onBackPressed();
                    doGoCloseTOActivity(PreHeatActivity.class,"");
                    break;
                case 1:
                    speak("请关闭管夹,放置补液袋");
                    tipTv.setText("补液放置");
                    Glide.with(this).asGif().load(R.raw.supply).skipMemoryCache(true)                      //禁止Glide内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    detailTv.setText("请在滴液杆上悬挂补液用腹透液");
                    break;
                case 2:
                    speak("插入卡匣");
                    tipTv.setText("插入卡匣");
//                    pipeCartInstall();
                    Glide.with(this).asGif().load(R.raw.insert_cass).skipMemoryCache(true)                      //禁止Glide内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group1"));
//                    sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group2"),500);
                    detailTv.setText("请取出专用管路,按正确的方式插入控制系统");
                    break;
                case 3:
                    speak("请关闭管夹,连接补液袋");
                    tipTv.setText("连接补液袋");
                    Glide.with(this).asGif().load(R.raw.conn_supply).skipMemoryCache(true)                      //禁止Glide内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group1"));
//                    sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group2"),500);
                    detailTv.setText("请依次将补液用腹透液与管路连接，确保未使用的补液管处于关闭状态.");
                    break;
                case 4:
                    speak(finalIsFinalSupply ?"请关闭管夹,连接最末袋":"请关闭管夹,连接加热袋");
                    tipTv.setText(finalIsFinalSupply ?"连接最末袋":"连接加热袋");
                    detailTv.setText(finalIsFinalSupply ? "请将末袋管路与最末袋透析液连接，并保持末代管路夹处于关闭状态":
                            "请将透析液托盘上的腹透液与加热管路连接，确保未使用的管路处于关闭状态");
                    if (finalIsFinalSupply) {
                        Glide.with(this).asGif().load(R.raw.conn_final).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    } else {
                        Glide.with(this).asGif().load(R.raw.conn_preheart).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    }
                    break;
                case 5:
                    speak(finalIsFinalSupply ?"请关闭管夹,连接加热袋":"废液卡管");
                    tipTv.setText(finalIsFinalSupply ?"连接加热袋":"废液卡管");
                    detailTv.setText(finalIsFinalSupply ? "请将透析液托盘上的腹透液与加热管路连接，确保未使用的管路处于关闭状态":
                            "请将废液端管路卡进排废阀门中。");
                    if (!finalIsFinalSupply) {
                        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true, "group3"));
                    }
                    if (finalIsFinalSupply) {
                        Glide.with(this).asGif().load(R.raw.conn_preheart).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    } else {
                        Glide.with(this).load(R.raw.stuck).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    }
                    break;
                case 6:
                    speak(finalIsFinalSupply ?"废液卡管":"请关闭管夹,连接废液袋");
                    tipTv.setText(finalIsFinalSupply ?"废液卡管":"连接废液袋");
                    detailTv.setText(finalIsFinalSupply ? "请将废液端管路卡进排废阀门中。":
                            "请将专用废液袋与引流端管路连接确保未连接的管路处于关闭状,并平放于透出液托盘上。");
                    if (finalIsFinalSupply) {
                        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group3"));
                        Glide.with(this).load(R.raw.stuck).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    } else {
                        Glide.with(this).load(R.raw.conn_fy).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    }

                    break;
                case 7:
                    Glide.with(this).load(R.raw.conn_fy).skipMemoryCache(true)                      //禁止Glide内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    break;
            }
        });

        nextIv.setOnClickListener(view -> {
            nextIv.setEnabled(false);
            previousIv.setEnabled(false);
            countDownTimer.start();
            currStep ++;
            Log.e("下一步","当前步骤:"+currStep);
            switch (currStep) {
//                case 0:
//                    doGoCloseTOActivity(PreHeatActivity.class, EmtConstant.ACTIVITY_PIPELINE_CONNECTION);
//                    break;
//                case 1:
//                    speak("补液放置");
//                    tipTv.setText("补液放置");
//                    detailTv.setText("请在滴液杆上悬挂补液用腹透液");
//                    break;
                case 2:
                    speak("插入卡匣");
                    tipTv.setText("插入卡匣");
//                    pipeCartInstall();
                    Glide.with(this).asGif().load(R.raw.insert_cass).skipMemoryCache(true)                      //禁止Glide内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group1"));
//                    sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group2"),500);

                    detailTv.setText("请取出专用管路,按正确的方式插入控制系统");
                    break;
                case 3:
                    speak("请关闭管夹,连接补液袋");
                    tipTv.setText("连接补液袋");
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group1"));
//                    sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group2"),500);
                    Glide.with(this).asGif().load(R.raw.conn_supply).skipMemoryCache(true)                      //禁止Glide内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    detailTv.setText("请依次将补液用腹透液与管路连接，确保未使用的补液管处于关闭状态.");
                    break;
                case 4:
                    speak(finalIsFinalSupply ? "请关闭管夹,连接最末袋" : "请关闭管夹,连接加热袋");
                    tipTv.setText(finalIsFinalSupply ? "连接最末袋" : "连接加热袋");
                    detailTv.setText(finalIsFinalSupply ? "请将末袋管路与最末袋透析液连接，并保持末代管路夹处于关闭状态" :
                            "请将透析液托盘上的腹透液与加热管路连接，确保未使用的管路处于关闭状态");
                    if (finalIsFinalSupply) {
                        Glide.with(this).asGif().load(R.raw.conn_final).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    } else {
                        Glide.with(this).load(R.raw.conn_preheart).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    }
                    break;
                case 5:
                    speak(finalIsFinalSupply ? "请关闭管夹,连接加热袋" : "废液卡管");
                    tipTv.setText(finalIsFinalSupply ? "连接加热袋" : "废液卡管");
                    detailTv.setText(finalIsFinalSupply ? "请将透析液托盘上的腹透液与加热管路连接，确保未使用的管路处于关闭状态" :
                            "请将专用废液袋与引流端管路连接确保未连接的管路处于关闭状,并平放于透出液托盘上。");
//                    if (!finalIsFinalSupply) {
//                        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true, "group3"));
//                    }
                    if (finalIsFinalSupply) {
                        Glide.with(this).asGif().load(R.raw.conn_preheart).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    } else {
                        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true, "group3"));
                        Glide.with(this).load(R.raw.stuck).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    }
                    break;
                case 6:
//                    if (!finalIsFinalSupply) {
//                        pipeCartFinish();
//                    }
                    speak(finalIsFinalSupply ? "废液卡管" : "请关闭管夹,连接废液袋");
                    tipTv.setText(finalIsFinalSupply ? "废液卡管" : "连接废液袋");
                    detailTv.setText(finalIsFinalSupply ? "请将废液端管路卡进排废阀门中。" :
                            "请将专用废液袋与引流端管路连接确保未连接的管路处于关闭状,并平放于透出液托盘上。");

                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(finalIsFinalSupply, "group3"));
                    if (finalIsFinalSupply) {
                        Glide.with(this).load(R.raw.stuck).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    } else {
                        Glide.with(this).load(R.raw.conn_fy).skipMemoryCache(true)                      //禁止Glide内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    }
//                    Glide.with(this).load(R.raw.btn_bg_prerinse_normal).skipMemoryCache(true)                      //禁止Glide内存缓存
//                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                    break;
                case 7:
                   if (finalIsFinalSupply) {
//                       pipeCartFinish();
                       sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false, "group3"));
                       speak("请关闭管夹,连接废液袋");
                       tipTv.setText("连接废液袋");
                       detailTv.setText("请将专用废液袋与引流端管路连接确保未连接的管路处于关闭状,并平放于透出液托盘上。");
                       Glide.with(this).load(R.raw.conn_fy).skipMemoryCache(true)                      //禁止Glide内存缓存
                               .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
                   } else {
                       doGoCloseTOActivity(PreRinseActivity.class,msg);
                   }
                    break;
                case 8:
                    doGoCloseTOActivity(PreRinseActivity.class,msg);
                    break;
            }
        });

    }

    private String  msg = EmtConstant.ACTIVITY_PIPELINE_CONNECTION;

    private final CountDownTimer countDownTimer = new CountDownTimer(500,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            nextIv.setEnabled(true);
            previousIv.setEnabled(true);
        }
    };

    @Override
    public void initViewData() {
        MyApplication.state = 2;
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        speak("请关闭管夹,放置补液袋");
        APIServiceManage.getInstance().postApdCode("Z4010");

    }

    @Override
    public void notifyByThemeChanged() {

    }

//    private void pipeCartInstall() {
//        sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL));//调用装管路
//    }
//
//    private void pipeCartFinish() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_FINISH));
//            }
//        });
//
//    }
//
//    /**
//     * 执行指令成功
//     *
//     * @param topic
//     */
//    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
//    public void receiveCmdResultOk(String topic) {
//        //{"publish":{"topic":"pipecart/install","msg":"ready","data":{}},"sign":"7c1093f2df698d9376ffa556a88dee01"}
//        //安装管路数据: {"publish":{"topic":"pipecart/finish","msg":"success","data":{}},"sign":"a1919ef64ddb0d28310230783dc6e69a"}
//        if (topic.contains(CommandSendConfig.METHOD_PIPECART_INSTALL)) {//调用装管路
//            Log.e("执行指令成功","调用装管路");
////            MyApplication.isPipecartInstall = true;
//        } else if (topic.contains(CommandSendConfig.METHOD_PIPECART_FINISH)) {//调用装管路完成
//            Log.e("执行指令成功","调用装管路完成");
////            MyApplication.isPipecartInstall = false;
//        } if (topic.contains("manualrinse/start")) { // 开始手动预冲
//
//        }
//
//    }
//    private String msg = EmtConstant.ACTIVITY_PIPELINE_CONNECTION;
//    /**
//     * 安装管路
//     *
//     * @param mPublicJson
//     */
//    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_PIPECART)
//    public void receivePipecartResult(String mPublicJson) {
//        Log.e("安装管路界面", "" + mPublicJson);
//        ReceivePublicBean mBean = JsonHelper.jsonToClass(mPublicJson, ReceivePublicBean.class);
//        if (mBean != null) {
//            if (mBean.topic.equals(CommandSendConfig.METHOD_PIPECART_INSTALL)) {//调用装管路
//                //{"publish":{"topic":"pipecart/install","msg":"ready","data":{}},"sign":"7c1093f2df698d9376ffa556a88dee01"}
//                if (mBean.msg.equals(CommandReceiveConfig.MSG_READY)) {
//                    Log.e("安装管路", "ready");
//                } else if (mBean.msg.equals("valve open exception")){
//                    Log.e("安装管路","msg--"+mBean.msg);
//                    MyApplication.isPipecartInstall = false;
//                    speak("阀门开启异常");
//                    showTipsCommonDialog("阀门开启异常");
//                }
//            } else if (mBean.topic.equals(CommandSendConfig.METHOD_PIPECART_FINISH)) {//
//                //安装管路数据: {"publish":{"topic":"pipecart/finish","msg":"success","data":{}},"sign":"a1919ef64ddb0d28310230783dc6e69a"}
//                if (mBean.msg.equals(CommandReceiveConfig.MSG_SUCCESS)) {
//                    Log.e("安装管路", "success");
//                    dismissLoading();
////                    speak("安装管路成功");
//                    APIServiceManage.getInstance().postApdCode("Z4019");
////                    saveFaultCodeLocal("Z4019");
////                    doGoCloseTOActivity(PreRinseActivity.class,msg);
//                } else {
//                    dismissLoading();
//                    Log.e("安装管路","安装管路失败");
//                    speak("管路安装失败");
//                    APIServiceManage.getInstance().postApdCode("Z4031");
////                    saveFaultCodeLocal("Z4031");
//                    MyApplication.isPipecartInstall = false;
//                    showTipsCommonDialog("管路安装失败,请检查管路是否连接成功");
////                    showTipsCommonDialog("安装管路失败");
//                }
//            }
//        }
//
//    }
//
//    /**
//     * 单片机返回指令不执行
//     *
//     * @param bean
//     */
//    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_ERR)
//    public void receiveCmdResultErr(ReceiveResultDataBean bean) {
//        String topic = bean.result.topic;
//        if (topic.contains(CommandSendConfig.METHOD_PIPECART_INSTALL)) {//调用装管路
////            showTipsCommonDialog("装管路异常");
//            if (bean.result.msg.equals("preheat task is running")) {
//                sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL),500);
//
//            }
//        } else if (topic.contains(CommandSendConfig.METHOD_PIPECART_FINISH)) {//调用装管路完成
////            showTipsCommonDialog("装管路完成异常");
//            sendCommandInterval(CommandDataHelper.getInstance().setPipecartCmdJson(CommandSendConfig.METHOD_PIPECART_INSTALL),500);//调用装管路完成
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
//        Glide.with(this).clear(imageView);

    }

}