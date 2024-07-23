package com.emt.pdgo.next.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandReceiveConfig;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.bean.FirstRinseParameterBean;
import com.emt.pdgo.next.data.bean.MuanlPreBean;
import com.emt.pdgo.next.data.serial.ManSupplyBean;
import com.emt.pdgo.next.data.serial.ManSupplyRunBean;
import com.emt.pdgo.next.data.serial.ReceivePublicBean;
import com.emt.pdgo.next.data.serial.ReceivePublicDataBean;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.data.serial.SerialParamAutoRinseBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveRinseBean;
import com.emt.pdgo.next.data.serial.receive.Stage1Bean;
import com.emt.pdgo.next.data.serial.receive.Stage2Bean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.activity
 * @ClassName: PreRinseActivity
 * @Description: 预冲洗
 * @Author: chenjh
 * @CreateDate: 2019/12/10 6:29 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/10 6:29 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class PreRinseActivity extends BaseActivity {

    @BindView(R.id.autoPreLayout)
    RelativeLayout autoPreLayout;
    @BindView(R.id.tvAutoPre)
    TextView tvAutoPre;

    @BindView(R.id.manualLayout)
    RelativeLayout manualLayout;
    @BindView(R.id.tvManualPre)
    TextView tvManualPre;

    @BindView(R.id.tvManualPreTips)
    TextView tvManualPreTips;

    @BindView(R.id.btnFinish)
    Button btnFinish;

    private boolean isAuto;
    private boolean isMuPre; // 手动预冲

    private boolean isLongPressed;
    private PressAndHold pressAndHold;

    @BindView(R.id.imageview)
    ImageView imageView;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_pre_rinse);
        ButterKnife.bind(this);
        APIServiceManage.getInstance().postApdCode("Z5011");
        speak("请打开所有管夹,确认人体端和引流管短接，透析液袋的折芯用力折断，便于液体流动");
        pressAndHold = new PressAndHold();
        autoPreLayout.setEnabled(false);
        manualLayout.setEnabled(false);
        autoPreLayout.setBackgroundResource(R.drawable.gray_shape);
        manualLayout.setBackgroundResource(R.drawable.gray_shape);
        startCountDownTimer.start();
    }

    private final CountDownTimer startCountDownTimer = new CountDownTimer(10*1000,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            autoPreLayout.setEnabled(true);
            manualLayout.setEnabled(true);
            autoPreLayout.setBackgroundResource(R.drawable.btn_prerinse_select);
            manualLayout.setBackgroundResource(R.drawable.btn_prerinse_select);
        }
    };

    private class PressAndHold implements Runnable {
        @Override
        public void run() {
            //5s之后，查看isLongPressed的变量值：
            if (isLongPressed) {//没有做up事件
                if (!isAuto && !isMuPre) {
                    tvManualPreTips.setText("点击关闭");
                    speak("手动预冲开始,请打开所有管路夹");
                    APIServiceManage.getInstance().postApdCode("Z5016");
                    isMuPre = true;
                    progressLayout.setVisibility(View.INVISIBLE);
                    btnFinish.setVisibility(View.INVISIBLE);
                    tvManualPre.setTextColor(Color.parseColor("#0089FF"));
                    tvManualPreTips.setTextColor(Color.parseColor("#0089FF"));
                    startManualPre();
                    manualLayout.setEnabled(false);
                    autoPreLayout.setEnabled(false);
                    countDownTimer.start();
                    btnBack.setVisibility(View.GONE);
                }
            } else {
                Log.e("长按", "5s的事件未触发");
                manualLayout.removeCallbacks(pressAndHold);
            }
        }
    }

    private final CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            manualLayout.setEnabled(true);
        }
    };

    /**
     * 执行指令成功
     *
     * @param topic
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
    public void receiveCmdResultOk(String topic) {
        if (topic.contains(CommandSendConfig.METHOD_RINSE_AUTO_FAILURE_CONTINUE)) {//继续自动预冲指令
        } else if (topic.equals(CommandSendConfig.METHOD_RINSE_AUTO_ABORT)) {//停止自动预冲指令
            speak("正在停止自动预冲中，请稍等");
        } else if (topic.equals("manualrinse/abort")) {
            runOnUiThread(()->{
                tvManualPreTips.setText("长按开启");
                speak("手动预冲完成");
                APIServiceManage.getInstance().postApdCode("Z5014");
                isMuPre = false;
                btnFinish.setVisibility(View.VISIBLE);
//                        doGoTOActivity(TreatmentCountdownActivity.class);
                autoPreLayout.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);
                tvManualPre.setTextColor(Color.parseColor("#464A6A"));
                tvManualPreTips.setTextColor(Color.parseColor("#464A6A"));
            });
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void registerEvents() {
        RxBus.get().register(this);
        btnFinish.setOnClickListener(view -> {
            doGoTOActivity(TreatmentCountdownActivity.class);
        });
        Glide.with(this).asGif().skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).load(R.raw.open_case).into(imageView);
        autoPreLayout.setOnClickListener(v -> {
            if (!isAuto && !isMuPre) {
                sendCommandFirstRinseAuto();
                btnBack.setVisibility(View.INVISIBLE);
                isAuto = true;
                btnFinish.setVisibility(View.INVISIBLE);
                autoPreLayout.setSelected(true);
                tvAutoPre.setTextColor(Color.parseColor("#0089FF"));
                manualLayout.setEnabled(false);//自动预冲时，先设置手动预冲不能点击
                APIServiceManage.getInstance().postApdCode("Z5012");
            }
        });

        manualLayout.setOnTouchListener((view1, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("onTouch","ACTION_DOWN");
                    if (!isLongPressed) {
                        isLongPressed = true;
                        manualLayout.postDelayed(pressAndHold, 3000);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isLongPressed = false;
                    manualLayout.removeCallbacks(pressAndHold);
                    Log.e("onTouch", "ACTION_UP");
                    break;
            }
            return false;
        });
        manualLayout.setOnClickListener(view -> {
            if (!isAuto && isMuPre) {
                sendToMainBoard(CommandDataHelper.getInstance().customCmd("manualrinse/stop"));
                tvManualPreTips.setText("停止中，请稍等...");
//                startManualPre();
            }
        });
    }

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressLayout)
    RelativeLayout progressLayout;
    @BindView(R.id.progressTv)
    TextView progressTv;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.title)
    TextView title;
    private String jumpMsg = "";
    private String msg = EmtConstant.ACTIVITY_PRE_RINSE;
    @Override
    public void initViewData() {
        gson = new Gson();
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        title.setText("请选择预冲模式");
        toEngAc(title);
        btnBack.setOnClickListener(v -> {
            if (jumpMsg != null) {
                APIServiceManage.getInstance().postApdCode("Z4041");
                if (jumpMsg.equals(EmtConstant.ACTIVITY_PIPELINE_CONNECTION)) {
                    doGoCloseTOActivity(PipelineConnectionActivity.class,msg);
                } else if (jumpMsg.equals(EmtConstant.ACTIVITY_PIPELINE_CONNECTION_SKIP_BOOT)){
                    doGoCloseTOActivity(PipelineConnectionSkipBootActivity.class,msg);
                } else {
                    ActivityManager.getActivityManager().removeActivity(this);
                }
            } else {
                ActivityManager.getActivityManager().removeActivity(this);
            }
//            onBackPressed();
        });
        sendToMainBoard(CommandDataHelper.getInstance().stopPreheatCmdJson());
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group1"),1000);
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group2"),1000);

        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false, "group3"),1000);
    }

    private final CountDownTimer stopCountDownTimer = new CountDownTimer(5000,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            runOnUiThread(() -> {
                tvManualPreTips.setText("长按开启");
                speak("手动预冲完成");
                APIServiceManage.getInstance().postApdCode("Z5014");
                isMuPre = false;
                btnFinish.setVisibility(View.VISIBLE);
//                        doGoTOActivity(TreatmentCountdownActivity.class);
                autoPreLayout.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);
                tvManualPre.setTextColor(Color.parseColor("#464A6A"));
                tvManualPreTips.setTextColor(Color.parseColor("#464A6A"));
            });
        }
    };

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_OTHER)
    public void receiveOther(String mSerialJson) {
        ReceivePublicDataBean mBean = JsonHelper.jsonToClass(mSerialJson, ReceivePublicDataBean.class);
        if (mBean.publish.topic.equals("manualrinse/process")) {
            switch (mBean.publish.msg) {
                case "valve open exception":
                    showManualAlarmDialog("阀门打开故障");
                    break;
                case "yalve auto close exception":
                    showManualAlarmDialog("结束时候阀门自动关闭故障");
                    break;
                case "yalve manual close exception":
                    showManualAlarmDialog("结束时候阀门手动关闭故障");
                    break;
                case "supply run":
                    runOnUiThread(()-> {
                        stopCountDownTimer.cancel();
                        tvManualPreTips.setText("治疗前配置");
                    });
                    break;
                case "supply finish":
                    runOnUiThread(()-> {
                        tvManualPreTips.setText("长按开启");
                        speak("手动预冲完成");
                        APIServiceManage.getInstance().postApdCode("Z5014");
                        isMuPre = false;
                        btnFinish.setVisibility(View.VISIBLE);
//                        doGoTOActivity(TreatmentCountdownActivity.class);
                        autoPreLayout.setEnabled(true);
                        btnBack.setVisibility(View.VISIBLE);
                        tvManualPre.setTextColor(Color.parseColor("#464A6A"));
                        tvManualPreTips.setTextColor(Color.parseColor("#464A6A"));
                    });
                    break;
                case "supply failure":
                    runOnUiThread(()->{
                        runOnUiThread(()-> {
//                            initBeepSoundSus(R.raw.medium_alarm)();
                            initBeepSoundSus(R.raw.medium_alarm);
                            final CommonDialog dialog = new CommonDialog(this);
                            dialog.setContent("补液失败")
                                    .setBtnFirst("重新补液")
                                    .setBtnTwo("停止补液")
                                    .setFirstClickListener(mCommonDialog -> {
//                    // buzzerOff();
//                    sendToMainBoard(CommandDataHelper.getInstance().abortAutoRinseCmd());
//                    mCommonDialog.dismiss();
                                        sendCommandInterval(CommandDataHelper.getInstance().customCmd("manualrinse/failure_continue"), 500 );
                                        // buzzerOff();
                                        mCommonDialog.dismiss();
                                    })
                                    .setTwoClickListener(mCommonDialog -> {
//                    sendCommandInterval(CommandDataHelper.getInstance().abortAutoRinseCmd(), 100);
                                        sendToMainBoard(CommandDataHelper.getInstance().customCmd("manualrinse/abort"));
                                        // buzzerOff();
                                        tvManualPreTips.setText("停止中，请稍等...");
                                        mCommonDialog.dismiss();
//                    doGoCloseTOActivity(TreatmentCountdownActivity.class,"");
                                    })
                                    .show();
                        });
                    });
                    break;
                case "stop":
                    MuanlPreBean bean = JsonHelper.jsonToClass(gson.toJson(mBean.publish.data), MuanlPreBean.class);
                    //                        tvManualPreTips.setText("长按开启");
                    //                        speak("手动预冲完成");
                    //                        APIServiceManage.getInstance().postApdCode("Z5014");
                    //                        isMuPre = false;
                    //                        btnFinish.setVisibility(View.VISIBLE);
                    ////                        doGoTOActivity(TreatmentCountdownActivity.class);
                    //                        autoPreLayout.setEnabled(true);
                    //                        btnBack.setVisibility(View.VISIBLE);
                    //                        tvManualPre.setTextColor(Color.parseColor("#464A6A"));
                    //                        tvManualPreTips.setTextColor(Color.parseColor("#464A6A"));
                    //                        MyApplication.rinseVol = bean.rinsevalume;
                    runOnUiThread(stopCountDownTimer::start);
                    break;
            }
        }
    }

    private void showManualAlarmDialog(String message) {
//        MyApplication.speak("自动预冲故障");
//        String mFirst = "重新预冲";
//        sendToMainBoard(CommandDataHelper.getInstance().customCmd("manualrinse/stop"));
        runOnUiThread(()->{
            initBeepSoundSus(R.raw.medium_alarm);
            String mFirst = "停止预冲";
//        String mTwo = "跳过";
//        btnBack.setVisibility(View.VISIBLE);
            CommonDialog dialog = new CommonDialog(this);
            dialog.setContent(message)
                    .setBtnFirst(mFirst)
//                .setBtnTwo(mTwo)
                    .setFirstClickListener(mCommonDialog -> {
//                    // buzzerOff();
//                        sendToMainBoard(CommandDataHelper.getInstance().customCmd("manualrinse/stop"));
//                    sendCommandInterval(CommandDataHelper.getInstance().continueAutoRinseCmd(), 100);
//                    mCommonDialog.dismiss();
                        // buzzerOff();
                        mCommonDialog.dismiss();
                    });
//                .setTwoClickListener(mCommonDialog -> {
//                    sendToMainBoard(CommandDataHelper.getInstance().customCmd("manualrinse/stop"));
//                    // buzzerOff();
////                    btnAuto.setSelected(false);
////                    manualLayout.setEnabled(true);
////                    btnManual.setEnabled(true);
////                    tvAutoPre.setTextColor(Color.parseColor("#464A6A"));
//                    mCommonDialog.dismiss();
//                    doGoCloseTOActivity(TreatmentCountdownActivity.class,"");
//                });
            if (!PreRinseActivity.this.isFinishing()) {
                dialog.show();
            }
        });

    }

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_Manual_PRE)
    public void receiveManualPre(String mPublicJson) {
//        Gson gson = new Gson();
        ReceivePublicBean mBean = JsonHelper.jsonToClass(mPublicJson, ReceivePublicBean.class);
//        ReceivePublicDataBean dataBean = (ReceivePublicDataBean) mBean.data;
        if (mBean.msg.equals("stop") && mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            MuanlPreBean bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), MuanlPreBean.class);
            runOnUiThread(() -> {
                tvManualPreTips.setText("长按开启");
                speak("手动预冲结束");
                isMuPre = false;
                btnFinish.setVisibility(View.VISIBLE);
                autoPreLayout.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);
                tvManualPre.setTextColor(Color.parseColor("#464A6A"));
                tvManualPreTips.setTextColor(Color.parseColor("#464A6A"));
                doGoTOActivity(TreatmentCountdownActivity.class);
                MyApplication.rinseVol = bean.rinsevalume;
            });
        } else if (mBean.msg.equals("stage1 running")&& mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            Stage1Bean stage1Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage1Bean.class);
            runOnUiThread(()->{

            });
        } else if (mBean.msg.equals("stage1 finish")&& mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            Stage1Bean stage1Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage1Bean.class);
            runOnUiThread(()->{

            });
        } else if (mBean.msg.equals("stage2 running")&& mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            Stage2Bean stage2Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage2Bean.class);
            runOnUiThread(()->{

            });
        } else if (mBean.msg.equals("stage2 finish")&& mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            Stage2Bean stage2Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage2Bean.class);
            runOnUiThread(()->{

            });
        } else if (mBean.msg.equals("supply start") && mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            ManSupplyBean supplyBean = JsonHelper.jsonToClass(gson.toJson(mBean.data), ManSupplyBean.class);
            speak("开始补液");
            runOnUiThread(()->{

            });
        } else if (mBean.msg.equals("supply restart") && mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            ManSupplyBean supplyBean = JsonHelper.jsonToClass(gson.toJson(mBean.data), ManSupplyBean.class);
            speak("重新开始补液");
            runOnUiThread(()->{

            });
        } else if (mBean.msg.equals("supply run") && mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            ManSupplyRunBean runBean = JsonHelper.jsonToClass(gson.toJson(mBean.data), ManSupplyRunBean.class);
            runOnUiThread(()->{
                MyApplication.rinseSupplyVol = runBean.SupplyVolume;
            });
        } else if (mBean.msg.equals("supply finish") && mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            runOnUiThread(()->{

            });
        }
//        else if (mBean.topic.equals("manualrinse")){
//            switch (dataBean.publish.msg) {
//                case "valve open exception":
//                    showManualAlarmDialog("阀门打开故障");
//                    break;
//                case "yalve auto close exception":
//                    showManualAlarmDialog("结束时候阀门自动关闭故障");
//                    break;
//                case "yalve manual close exception":
//                    showManualAlarmDialog("结束时候阀门手动关闭故障");
//                    break;
//            }
//
//        }
        else if (mBean.msg.equals("supply failure") && mBean.topic.equals(CommandReceiveConfig.TOPIC_MANUAL_RINSE_PROCESS +"/process")) {
            runOnUiThread(()-> {
                initBeepSoundSus(R.raw.medium_alarm);
                final CommonDialog dialog = new CommonDialog(this);
                dialog.setContent("补液失败")
                        .setBtnFirst("重新补液")
                        .setBtnTwo("停止补液")
                        .setFirstClickListener(mCommonDialog -> {
//                    // buzzerOff();
//                    sendToMainBoard(CommandDataHelper.getInstance().abortAutoRinseCmd());
//                    mCommonDialog.dismiss();
                            sendCommandInterval(CommandDataHelper.getInstance().customCmd("manualrinse/failure_continue"), 500 );
                            // buzzerOff();
                            mCommonDialog.dismiss();
                        })
                        .setTwoClickListener(mCommonDialog -> {
//                    sendCommandInterval(CommandDataHelper.getInstance().abortAutoRinseCmd(), 100);
                            sendToMainBoard(CommandDataHelper.getInstance().customCmd("manualrinse/abort"));
                            // buzzerOff();
                            tvManualPreTips.setText("停止中，请稍等...");
                            mCommonDialog.dismiss();
//                    doGoCloseTOActivity(TreatmentCountdownActivity.class,"");
                        })
                        .show();
            });
        }
    }

    private void startManualPre() {

        FirstRinseParameterBean mFirstRinseParameterBean = PdproHelper.getInstance().getFirstRinseParameterBean();
//        SerialParamManualRinseBean mParamBean = new SerialParamManualRinseBean();
//        mParamBean.firstvolume = mFirstRinseParameterBean.firstvolume;
//        mParamBean.secondvolume = mFirstRinseParameterBean.secondvolume;
//        mParamBean.supplyspeed = mFirstRinseParameterBean.supplyspeed;
//        mParamBean.supplyperiod = mFirstRinseParameterBean.supplyperiod;
        SerialParamAutoRinseBean mParamBean = new SerialParamAutoRinseBean();
        mParamBean.firstvolume = mFirstRinseParameterBean.firstvolume;
        mParamBean.secondvolume = mFirstRinseParameterBean.secondvolume;
        mParamBean.supplyspeed = mFirstRinseParameterBean.supplyspeed;
        mParamBean.supplyperiod = mFirstRinseParameterBean.supplyperiod;
        mParamBean.supplyselect = mFirstRinseParameterBean.supplyselect;
        mParamBean.supplychvolume = mFirstRinseParameterBean.supplychvolume;
        mParamBean.firstpervolume = PdproHelper.getInstance().ipdBean().firstPerfusionVolume == 0?
                PdproHelper.getInstance().ipdBean().perCyclePerfusionVolume:PdproHelper.getInstance().ipdBean().firstPerfusionVolume;
        mParamBean.protectvolume = PdproHelper.getInstance().getSupplyParameterBean().supplyTargetProtectionValue;

        sendToMainBoard(CommandDataHelper.getInstance().startManualRinseCmd2(mParamBean));
    }

    /**
     * 发送自动预冲命令,自动预冲工作机制: 打开NE002+NE003，通过时间控制， 将80%的气泡排入预冲袋，(这只能大量实验去验证)。无任何输入反馈效果。
     */
    private void sendCommandFirstRinseAuto() {
        isFail = false;
        FirstRinseParameterBean mFirstRinseParameterBean = PdproHelper.getInstance().getFirstRinseParameterBean();
        SerialParamAutoRinseBean mParamBean = new SerialParamAutoRinseBean();
        mParamBean.firstvolume = mFirstRinseParameterBean.firstvolume;
        mParamBean.secondvolume = mFirstRinseParameterBean.secondvolume;
        mParamBean.supplyspeed = mFirstRinseParameterBean.supplyspeed;
        mParamBean.supplyperiod = mFirstRinseParameterBean.supplyperiod;
        mParamBean.supplyselect = mFirstRinseParameterBean.supplyselect;
        mParamBean.supplychvolume = mFirstRinseParameterBean.supplychvolume;
        mParamBean.firstpervolume = PdproHelper.getInstance().ipdBean().firstPerfusionVolume == 0?
                PdproHelper.getInstance().ipdBean().perCyclePerfusionVolume:PdproHelper.getInstance().ipdBean().firstPerfusionVolume;
        mParamBean.protectvolume = PdproHelper.getInstance().getSupplyParameterBean().supplyTargetProtectionValue;
        sendCommandInterval(CommandDataHelper.getInstance().startAutoRinseCmd(mParamBean), 500);
    }

    /**
     * 单片机返回指令不执行
     *
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_ERR)
    public void receiveCmdResultErr(ReceiveResultDataBean bean) {
        String topic = bean.result.topic;
        if (topic.contains(CommandSendConfig.METHOD_RINSE_AUTO_FAILURE_CONTINUE)) {//继续自动预冲指令
            sendCommandInterval(CommandDataHelper.getInstance().customCmd("autorinse/failure_continue"), 500);
        } else if (topic.equals(CommandSendConfig.METHOD_RINSE_AUTO_ABORT)) {//停止自动预冲指令

        } else if (topic.equals("autorinse/start")) {
            sendCommandFirstRinseAuto();
        } else if (bean.result.msg.equals("preheat task is running")) {

        }
    }

    private Gson gson;
    private int failNum; // 自动预冲失败次数
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_AUTO_RINSE_DATA)
    public void receiveFirstrinseData(String mPublicJson) {
        ReceivePublicBean mBean = JsonHelper.jsonToClass(mPublicJson, ReceivePublicBean.class);
        if (mBean != null) {
            // CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS
            if (mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS) && mBean.msg.equals(CommandReceiveConfig.MSG_START)) {//
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        speak("自动预冲开始,请打开管夹");
                        progressLayout.setVisibility(View.VISIBLE);
                        progressBar.setProgress(0);
                        progressTv.setText("0%");
                        tvAutoPre.setText("预冲中...");
                    }
                });
                // CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS
            } else if (mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS) && mBean.msg.equals(CommandReceiveConfig.MSG_FINISH)) {//
                Gson gson = new Gson();
                ReceiveRinseBean bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), ReceiveRinseBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        APIServiceManage.getInstance().postApdCode("Z5014");
                        tvAutoPre.setTextColor(Color.parseColor("#464A6A"));
//                                doGoCloseTOActivity(TreatmentCountdownActivity.class,"");
                        btnBack.setVisibility(View.VISIBLE);
                        isAuto = false;
                        progressLayout.setVisibility(View.INVISIBLE);
                        tvAutoPre.setText("自动预冲");
//                        btnFinish.setVisibility(View.GONE);
                        manualLayout.setEnabled(true);
                        autoPreLayout.setSelected(false);
                        speak("自动预冲结束");
                        MyApplication.rinseVol = bean.rinsevalume;
                        btnFinish.setVisibility(isFail ? View.INVISIBLE : View.VISIBLE);
//                        doGoTOActivity(TreatmentCountdownActivity.class);
                    }
                });
            } else if (mBean.msg.equals(CommandReceiveConfig.MSG_TIME_OUT) || mBean.msg.equals(CommandReceiveConfig.MSG_FAILURE)) {//
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        failNum += 1;
////                        if (failNum < 3) {
//                            isAutoFailure = true;
//                            initBeepSoundSus(R.raw.medium_alarm)();
                            String message = "自动预冲故障";
                            switch (mBean.topic) {
                                case CommandReceiveConfig.TOPIC_AUTO_RINSE_SUPPLY: //返回步骤二故障  //auto rinse supply: time out!
                                    message = "请检查补液端管路。";
//                                    sendCommandFirstRinseAuto();
                                    break;
                                case CommandReceiveConfig.TOPIC_AUTO_RINSE_PERFUSE: //返回步骤三故障
                                    message = "预冲失败，请检查预冲袋管夹是否打开。";
//                                    sendCommandFirstRinseAuto();
                                    break;
                                case CommandReceiveConfig.TOPIC_AUTO_RINSE_DRAIN: //返回步骤四故障
                                    message = "预冲失败，请检查废液端管路。";
//                                    sendCommandFirstRinseAuto();
                                    break;
                                case "autorinse/stage1":
                                    message = "加热或废液端管路夹未打开";
//                                    sendCommandFirstRinseAuto();
                                    break;
                                case "autorinse/stage2":
                                    message = "短接管路夹未打开";
//                                    sendCommandFirstRinseAuto();
                                    break;
                                case "autorinse/stage0":
                                    message = "补液端管路夹未打开";
//                                    sendCommandFirstRinseAuto();
                                    break;
                            }
//                            if (failNum > 1) {
////                                showTipsCommonDialog(message);
////                                btnBack.setVisibility(View.VISIBLE);
                                APIServiceManage.getInstance().postApdCode("Z5015");
                                speak("自动预冲失败，请检查管夹是否打开");
//                                sendCommandInterval(CommandDataHelper.getInstance().abortAutoRinseCmd(), 100 );
                                showAlarmDialog(message);
//                                showTipsCommonDialog(message);
//                            } else {
//                                APIServiceManage.getInstance().postApdCode("Z5016");
//                                sendCommandInterval(CommandDataHelper.getInstance().customCmd("autorinse/failure_continue"), 500);
//                            }
                    }
                });

            } else if (mBean.msg.equals("valve open exception")) {//
                Log.e("预冲返回", "valve open exception");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showTipsCommonDialog("阀门开启故障");
                    }
                });
            } else if (mBean.msg.equals("stage0 running") &&mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS)) {
                Stage1Bean stage1Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage1Bean.class);
                runOnUiThread(()->{
                    progressBar.setProgress(10);
                    progressTv.setText("10%");
                });
            } else if (mBean.msg.equals("stage0 finish") &&mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS)) {
                Stage1Bean stage1Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage1Bean.class);
                runOnUiThread(()->{
                    progressBar.setProgress(25);
                    progressTv.setText("25%");
                });
            } else if (mBean.msg.equals("stage1 running") &&mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS)) {
                Stage1Bean stage1Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage1Bean.class);
                runOnUiThread(()->{
                    progressBar.setProgress(10);
                    progressTv.setText("30%");
                });
            } else if (mBean.msg.equals("stage1 finish") &&mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS)) {
                Stage1Bean stage1Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage1Bean.class);
                runOnUiThread(()->{
                    progressBar.setProgress(25);
                    progressTv.setText("45%");
                });
            } else if (mBean.msg.equals("stage2 running") && mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS)) {
                Stage2Bean stage2Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage2Bean.class);
                runOnUiThread(()->{
                    progressBar.setProgress(50);
                    progressTv.setText("50%");
                });
            } else if (mBean.msg.equals("stage2 finish") && mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS)) {
                Stage2Bean stage2Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), Stage2Bean.class);
                runOnUiThread(()->{
                    progressBar.setProgress(75);
                    progressTv.setText("75%");
                });
            } else if (mBean.msg.equals("supply run") && mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS)) {
                ManSupplyRunBean stage2Bean = JsonHelper.jsonToClass(gson.toJson(mBean.data), ManSupplyRunBean.class);
                runOnUiThread(()->{
                    MyApplication.rinseSupplyVol = stage2Bean.SupplyVolume;
                    tvAutoPre.setText("治疗前配置");
                });
            } else if (mBean.msg.equals("supply start") && mBean.topic.equals(CommandReceiveConfig.TOPIC_AUTO_RINSE_PROCESS)) {
                ManSupplyBean supplyBean = JsonHelper.jsonToClass(gson.toJson(mBean.data), ManSupplyBean.class);
                speak("开始补液");
                runOnUiThread(()->{
                    tvAutoPre.setText("治疗前配置");
                });
            }
        }


    }

    /**
     * 故障弹出警告框
     *
     * @param message
     */

    private boolean isFail;
    private void showAlarmDialog(String message) {
        String mFirst = "重新预冲";
        String mTwo = "结束预冲";
        initBeepSoundSus(R.raw.medium_alarm);
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent(message)
                .setBtnFirst(mFirst)
                .setBtnTwo(mTwo)
                .setFirstClickListener(mCommonDialog -> {
//                    // buzzerOff();
//                    sendToMainBoard(CommandDataHelper.getInstance().abortAutoRinseCmd());
//                    mCommonDialog.dismiss();
                    APIServiceManage.getInstance().postApdCode("Z5016");
                    sendToMainBoard(CommandDataHelper.getInstance().customCmd("autorinse/failure_continue"));
                    // buzzerOff();
//                    sendCommandInterval(CommandDataHelper.getInstance().abortAutoRinseCmd(), 100 );
                    mCommonDialog.dismiss();
                })
                .setTwoClickListener(mCommonDialog -> {
//                    sendCommandInterval(CommandDataHelper.getInstance().abortAutoRinseCmd(), 100);
                    sendToMainBoard(CommandDataHelper.getInstance().abortAutoRinseCmd());
                    // buzzerOff();
                    isFail = true;
//                    btnAuto.setSelected(false);
//                    manualLayout.setEnabled(true);
//                    btnManual.setEnabled(true);
//                    tvAutoPre.setTextColor(Color.parseColor("#464A6A"));
                    mCommonDialog.dismiss();
//                    doGoCloseTOActivity(TreatmentCountdownActivity.class,"");
                })
                .show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startCountDownTimer.cancel();
        RxBus.get().unRegister(this);
    }


    @Override
    public void notifyByThemeChanged() {

    }
}
