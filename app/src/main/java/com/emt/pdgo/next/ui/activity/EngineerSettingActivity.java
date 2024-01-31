package com.emt.pdgo.next.ui.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.FirstRinseParameterBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.UserParameterBean;
import com.emt.pdgo.next.data.entity.CommandItem;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.dpr.DprExamineActivity;
import com.emt.pdgo.next.ui.activity.param.DrainParameterActivity;
import com.emt.pdgo.next.ui.activity.param.FaultCodeActivity;
import com.emt.pdgo.next.ui.activity.param.FirmwareUpgradeActivity;
import com.emt.pdgo.next.ui.activity.param.FirstRinseParameterActivity;
import com.emt.pdgo.next.ui.activity.param.LamplightActivity;
import com.emt.pdgo.next.ui.activity.param.OtherParamActivity;
import com.emt.pdgo.next.ui.activity.param.ParamDebugActivity;
import com.emt.pdgo.next.ui.activity.param.PerfusionParameterActivity;
import com.emt.pdgo.next.ui.activity.param.SNSetActivity;
import com.emt.pdgo.next.ui.activity.param.SupplyParameterActivity;
import com.emt.pdgo.next.ui.activity.param.TemperatureControlParameterActivity;
import com.emt.pdgo.next.ui.activity.param.TreatmentHistoryActivity;
import com.emt.pdgo.next.ui.activity.param.UploadDataTestActivity;
import com.emt.pdgo.next.ui.activity.param.UrlSetActivity;
import com.emt.pdgo.next.ui.activity.param.UserParameterActivity;
import com.emt.pdgo.next.ui.activity.param.ValveTestActivity;
import com.emt.pdgo.next.ui.activity.param.VoiceDebugActivity;
import com.emt.pdgo.next.ui.activity.param.VolumeActivity;
import com.emt.pdgo.next.ui.activity.param.WeighParameterActivity;
import com.emt.pdgo.next.ui.activity.wifi.WifiActivity;
import com.emt.pdgo.next.ui.adapter.CommandAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.mode.activity.ModeActivity;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EngineerSettingActivity extends BaseActivity {


    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.rv_set)
    RecyclerView rvSet;

    private List<CommandItem> mList;
    private CommandAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_engineer_setting);
        ButterKnife.bind(this);
        initHeadTitleBar("工程师设置", "关于");
//        Log.e("activity","int--"+ ActivityCompat.checkSelfPermission(EngineerSettingActivity.this,
//                "android.permission.WRITE_EXTERNAL_STORAGE"));
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

    @Override
    public void initViewData() {
        mList = new ArrayList<>();
        
        mList.add(new CommandItem("参数设置", "paramDebug"));
        mList.add(new CommandItem("灯光测试", "lamplightDebug"));
        mList.add(new CommandItem("声音测试", "voiceDebug"));

//        mList.add(new CommandItem("通信地址设置", "UrlSet"));
//        mList.add(new CommandItem("设备SN参数设置", "SNSet"));
//        mList.add(new CommandItem("网络参数设置", "NetSet"));
//
//        mList.add(new CommandItem("传感器数值校准", "WeighParameter"));
//        mList.add(new CommandItem("温控参数设置", "TemperatureControlParameter"));
//        mList.add(new CommandItem("预冲参数设置", "FirstRinseParameter"));

//        mList.add(new CommandItem("打开语音", "tsOpen"));
//        mList.add(new CommandItem("关闭语音", "tsClose"));
//        mList.add(new CommandItem("语音测试", "tsTest"));
//        mList.add(new CommandItem("打开蜂鸣器", "buzzerOpen"));
//        mList.add(new CommandItem("关闭蜂鸣器", "buzzerClose"));
//        mList.add(new CommandItem("引流参数设置", "DrainParameter"));
//        mList.add(new CommandItem("灌注参数设置", "PerfusionParameter"));
//        mList.add(new CommandItem("补液参数设置", "SupplyParameter"));
//        mList.add(new CommandItem("治疗记录", "tdRecord"));
//        mList.add(new CommandItem("用户参数设置", "UserParameter"));

        mList.add(new CommandItem("电机调试功能", "ValveTest"));
//        mList.add(new CommandItem("工程师调试设置", "Debug"));
//        mList.add(new CommandItem("输入治疗信息", "InputTreatment"));
//        mList.add(new CommandItem("体重血压", "InputBodyData"));
//        mList.add(new CommandItem("自检界面", "SelfCheck"));
//        mList.add(new CommandItem("扫码登录", "ScanCodeLogin"));
//        mList.add(new CommandItem("准备工作", "Preparation"));
//        mList.add(new CommandItem("预热程序", "PreHeat"));
//        mList.add(new CommandItem("管道连接", "PipelineConnection"));
//        mList.add(new CommandItem("处方设置", "prescriptionSettings"));
//        mList.add(new CommandItem("治疗模式","treatmentMode"));
////
//        mList.add(new CommandItem("管路预冲", "PreRinse"));
//        mList.add(new CommandItem("治疗开始倒计时", "Countdown"));
//        mList.add(new CommandItem("开始APD治疗", "TreatmentFragment"));
//        mList.add(new CommandItem("开始DPR治疗", "dprTeat"));
        mList.add(new CommandItem("数据上报测试", "uploadDataTest"));
//        mList.add(new CommandItem("数据收集", "DataCollection"));
//        mList.add(new CommandItem("治疗反馈", "TreatmentFeedback"));
//        mList.add(new CommandItem("治疗数据评估", "TreatmentEvaluation"));
        mList.add(new CommandItem("电池供电关机", "power_monitor"));
        mList.add(new CommandItem("无线和网络设置", "wirelessSetting"));
//        mList.add(new CommandItem("本地治疗数据", "localPd"));
//        mList.add(new CommandItem("息屏","sleep"));
        mList.add(new CommandItem("休眠","dormancy"));
        mList.add(new CommandItem("故障码查看","faultCode"));
//        mList.add(new CommandItem("其他参数设置","otherParamSet"));
//        mList.add(new CommandItem("打开灌注灯", "perfuse_led_open"));
//        mList.add(new CommandItem("关闭灌注灯", "perfuse_led_close"));
//        mList.add(new CommandItem("打开留腹灯", "retain_led_open"));
//        mList.add(new CommandItem("关闭留腹灯", "retain_led_close"));
//        mList.add(new CommandItem("打开引流灯", "drain_led_open"));
//        mList.add(new CommandItem("关闭引流灯", "drain_led_close"));
//        mList.add(new CommandItem("打开全部灯", "all_led_open"));
//        mList.add(new CommandItem("关闭全部灯", "all_led_close"));
//        mList.add(new CommandItem("固件升级","firmwareUpgrade"));
        mList.add(new CommandItem("系统升级","appUpdate"));
//        mList.add(new CommandItem("设置页面","setting"));
        mList.add(new CommandItem("声音设置","volume"));
//        mList.add(new CommandItem("恢复出厂设置", "factoryReset"));
        mAdapter = new CommandAdapter(this, R.layout.item_setting, mList);
        rvSet.setLayoutManager(new GridLayoutManager(this, 3));
        rvSet.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int dayFlag = 0;
            if ("SNSet".equals(mList.get(position).mCommand)) {
                doGoTOActivity(SNSetActivity.class);
            } else if ("tsTest".equals(mList.get(position).mCommand)) {
                speak("语音测试");
            } else if ("UrlSet".equals(mList.get(position).mCommand)) {
                doGoTOActivity(UrlSetActivity.class);
            } else if ("NetSet".equals(mList.get(position).mCommand)) {
//                net();
                doGoTOActivity(WifiActivity.class);
            } else if ("dormancy".equals(mList.get(position).mCommand)) {
//                simulateKeyByCommand();
                simulateKey(26);
            }else if ("buzzerOpen".equals(mList.get(position).mCommand)) {
                sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_ON));
            } else if ("volume".equals(mList.get(position).mCommand)) {
                doGoTOActivity(VolumeActivity.class);
            }else if ("buzzerClose".equals(mList.get(position).mCommand)) {
                sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_OFF));
            } else if ("tsOpen".equals(mList.get(position).mCommand)) {
                PdproHelper.getInstance().updateTtsSoundOpen(!PdproHelper.getInstance().getTtsSoundOpen());
            } else if ("tsClose".equals(mList.get(position).mCommand)) {
                PdproHelper.getInstance().updateTtsSoundOpen(!PdproHelper.getInstance().getTtsSoundOpen());
            } else if ("appUpdate".equals(mList.get(position).mCommand)) {
                appUpdate(true);
            } else if ("tdRecord".equals(mList.get(position).mCommand)) {
                doGoTOActivity(TreatmentHistoryActivity.class);
            }else if ("WeighParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(WeighParameterActivity.class);
            } else if ("TemperatureControlParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(TemperatureControlParameterActivity.class);
            } else if ("FirstRinseParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(FirstRinseParameterActivity.class);
            } else if ("uploadDataTest".equals(mList.get(position).mCommand)) {
                doGoTOActivity(UploadDataTestActivity.class);
            }else if ("DrainParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(DrainParameterActivity.class);
            } else if ("PerfusionParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(PerfusionParameterActivity.class);
            } else if ("SupplyParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(SupplyParameterActivity.class);
            } else if ("UserParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(UserParameterActivity.class);
            } else if ("ScanCodeLogin".equals(mList.get(position).mCommand)) {
                doGoTOActivity(ScanCodeLoginActivity.class);
            } else if ("Preparation".equals(mList.get(position).mCommand)) {
                doGoTOActivity(PreparationActivity.class);
            } else if ("ValveTest".equals(mList.get(position).mCommand)) {
                doGoTOActivity(ValveTestActivity.class);
            }  else if ("setting".equals(mList.get(position).mCommand)) {
                doGoTOActivity(SettingActivity.class);
            } else if ("InputTreatment".equals(mList.get(position).mCommand)) {
                doGoTOActivity(InputTreatmentInfoActivity.class);
            } else if ("PreHeat".equals(mList.get(position).mCommand)) {
                doGoTOActivity(PreHeatActivity.class);
            } else if ("PipelineConnection".equals(mList.get(position).mCommand)) {
                doGoTOActivity(PipelineConnectionActivity.class);
            } else if ("TreatmentFragment".equals(mList.get(position).mCommand)) {
                MyApplication.apdMode = 4;
                doGoCloseTOActivity(TreatmentFragmentActivity.class,"");
            } else if ("TreatmentFeedback".equals(mList.get(position).mCommand)) {
                doGoTOActivity(TreatmentFeedbackActivity.class);
            } else if ("PreRinse".equals(mList.get(position).mCommand)) {
                doGoTOActivity(PreRinseActivity.class);
            } else if ("Countdown".equals(mList.get(position).mCommand)) {
                doGoTOActivity(TreatmentCountdownActivity.class);
            } else if ("DataCollection".equals(mList.get(position).mCommand)) {
                doGoTOActivity(DataCollectionActivity.class);
            } else if ("TreatmentEvaluation".equals(mList.get(position).mCommand)) {
                doGoTOActivity(TreatmentEvaluationActivity.class);
            } else if ("power_monitor".equals(mList.get(position).mCommand)) {
                doGoTOActivity(PowerMonitorDialogActivity.class);
            } else if ("wirelessSetting".equals(mList.get(position).mCommand)) {
//                wirelessSetting();
                doGoTOActivity(WifiActivity.class);
            } else if ("dprTeat".equals(mList.get(position).mCommand)) {
                doGoTOActivity(DprExamineActivity.class);
            }else if ("perfuse_led_close".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("perfuse", false,dayFlag,2), 500);
//                    sendLedCommandInterval(CommandDataHelper.getInstance().LedOpen("supply", false), 500 );
            } else if ("perfuse_led_open".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("perfuse", true,dayFlag,2), 500);
            }else if ("retain_led_close".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("retain", false,dayFlag,2), 500);
//                    sendLedCommandInterval(CommandDataHelper.getInstance().LedOpen("supply", false), 500 );
            } else if ("retain_led_open".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("retain", true,dayFlag,2), 500);
            }else if ("drain_led_close".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("drain", false,dayFlag,2), 500);
//                    sendLedCommandInterval(CommandDataHelper.getInstance().LedOpen("supply", false), 500 );
            } else if ("drain_led_open".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("drain", true,dayFlag,2), 500);
            }else if ("all_led_close".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("all", false,dayFlag,2), 500);
//                    sendLedCommandInterval(CommandDataHelper.getInstance().LedOpen("supply", false), 500 );
            } else if ("all_led_open".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("all", true,dayFlag,2), 500);
            } else if ("firmwareUpgrade".equals(mList.get(position).mCommand)) {
                doGoTOActivity(FirmwareUpgradeActivity.class);
           } else if ("treatmentMode".equals(mList.get(position).mCommand)) {
                doGoCloseTOActivity(ModeActivity.class,"");
            } else if ("prescriptionSettings".equals(mList.get(position).mCommand)) {
                doGoTOActivity(PrescriptionActivity.class);
            } else if ("paramDebug".equals(mList.get(position).mCommand)) {
                doGoTOActivity(ParamDebugActivity.class);
            } else if ("lamplightDebug".equals(mList.get(position).mCommand)) {
                doGoTOActivity(LamplightActivity.class);
            } else if ("voiceDebug".equals(mList.get(position).mCommand)) {
                doGoTOActivity(VoiceDebugActivity.class);
            } else if ("factoryReset".equals(mList.get(position).mCommand)) {
                factoryReset();
            } else if ("faultCode".equals(mList.get(position).mCommand)) {
                doGoTOActivity(FaultCodeActivity.class);
            } else if("otherParamSet".equals(mList.get(position).mCommand)) {
                doGoTOActivity(OtherParamActivity.class);
            } else if("sleep".equals(mList.get(position).mCommand)) {
                goToSleep();
//                wakeup(this);
            } else if("localPd".equals(mList.get(position).mCommand)) {
                doGoTOActivity(MyDreamActivity.class);
//                try {
//                    Thread.sleep(10* 1000L);
//                    wakeup(this);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });

    }

//    private OutputStream os;
//    private void execute(String cmd) {
//        try {
//            if (os == null) {
//                os = Runtime.getRuntime().exec("su").getOutputStream();
//            }
//            os.write(cmd.getBytes());
//            os.flush();
////            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private void simulateKey(int keyCode) {
//        execute("input keyevent " + keyCode  + "\n");
//    }

    // 根据亮度值修改当前window亮度
    public void changeAppBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

    private void simulateKeyByCommand(){
        try{
            String keyCommand = "input keyevent 26";
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
        } catch(IOException e){
            Log.e("input", e.toString());
        }

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Instrumentation inst = new Instrumentation();
//                    inst.sendKeyDownUpSync(keyCode);
//                } catch (Exception e) {
//                    Log.e("hello", e.toString());
//                }
//            }
//
//        }.start();
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
//                    Logger.d(result);
                if (mType.equals(PdGoConstConfig.zeroClear)) {//工程师模式的密码
                    if ("303626".equals(result)) {

                    }
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void init(String birthday, String currentTime) {
        SimpleDateFormat formatBirthday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int yearBirthday = 0, monthBirthday = 0, dayBirthday = 0;
        try {
            if (birthday != null) {
                Date dateBirthday = formatBirthday.parse(birthday);
                Calendar calendarBirthday = Calendar.getInstance();
                assert dateBirthday != null;
                calendarBirthday.setTime(dateBirthday);
                yearBirthday = calendarBirthday.get(Calendar.YEAR);
                monthBirthday = calendarBirthday.get(Calendar.MONTH);
                dayBirthday = calendarBirthday.get(Calendar.DAY_OF_MONTH);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatMoment = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendarMoment = Calendar.getInstance();
        int yearMoment = 0, monthMoment = 0, dayMoment = 0;
        try {
            Date dateMoment = formatMoment.parse(currentTime);
            assert dateMoment != null;
            calendarMoment.setTime(dateMoment);
            yearMoment = calendarMoment.get(Calendar.YEAR);
            monthMoment = calendarMoment.get(Calendar.MONTH);
            dayMoment = calendarMoment.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (yearBirthday > yearMoment || yearBirthday == yearMoment && monthBirthday > monthMoment || yearBirthday == yearMoment && monthBirthday == monthMoment && dayBirthday > dayMoment) {
//            useTime.setText("??年??月??天");
        } else {
            int yearAge = yearMoment - yearBirthday;
            int monthAge = monthMoment - monthBirthday;
            int dayAge = dayMoment - dayBirthday;
            //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减
            if (dayAge < 0) {
                monthAge -= 1;
                calendarMoment.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数
                dayAge = dayAge + calendarMoment.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            if (monthAge < 0) {
                monthAge = (monthAge + 12) % 12;
                yearAge--;
            }
            String year, month, day;
//            if (yearAge < 10) {
//                year = "0" + yearAge + "年";
//            } else {
            year = yearAge + "年";
//            }
//            if (monthAge < 10) {
//                month = "0" + monthAge + "月";
//            } else {
            month = monthAge + "月";
//            }
//            if (dayAge < 10) {
//                day = "0" + dayAge + "天";
//            } else {
            day = dayAge + "天";
//            }
//            useTime.setText(year+month+day);
        }
    }

    private void goToSleep() {
//        DevicePolicyManager policyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        ComponentName adminReceiver = new ComponentName(context, ScreenOffAdminReceiver.class);
//        boolean admin = policyManager.isAdminActive(adminReceiver);
////        policyManager.lockNow();
//        if (admin) {
//            policyManager.lockNow();
//        }else {
//            Toast.makeText(context, "没有设备管理权限", Toast.LENGTH_LONG).show();
//        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,120*1000);
            }
        }
    }

    private void wakeup(Context context) {
        PowerManager mPowerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "tag");
//        mWakeLock.acquire(60*1000L /*10 minutes*/);
        mWakeLock.acquire(30*1000L /*10 minutes*/);
    }



    private void factoryReset() {
        FirstRinseParameterBean firstRinseParameterBean = PdproHelper.getInstance().getFirstRinseParameterBean();
        PerfusionParameterBean perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        DrainParameterBean drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        SupplyParameterBean supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();
        UserParameterBean userParameterBean = PdproHelper.getInstance().getUserParameterBean();
        RetainParamBean retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        IpdBean treatmentParameterEniity = PdproHelper.getInstance().ipdBean();
        // 处方
        treatmentParameterEniity.peritonealDialysisFluidTotal = 2800;
        treatmentParameterEniity.perCyclePerfusionVolume = 500;
        treatmentParameterEniity.cycle = 4;
        treatmentParameterEniity.firstPerfusionVolume = 0;
        treatmentParameterEniity.abdomenRetainingTime = 15;
        treatmentParameterEniity.abdomenRetainingVolumeFinally = 0;
        treatmentParameterEniity.abdomenRetainingVolumeLastTime = 0;
        treatmentParameterEniity.ultrafiltrationVolume = 0;
        // 预冲参数重置
        firstRinseParameterBean.firstvolume = 50;
        firstRinseParameterBean.secondvolume = 50;
        firstRinseParameterBean.supplyperiod = 60;
        firstRinseParameterBean.supplyspeed = 30;
        firstRinseParameterBean.supplyselect = 1;
        // 灌注参数重置
        perfusionParameterBean.perfAllowAbdominalVolume = false;
        perfusionParameterBean.perfMaxWarningValue = 5000;
        perfusionParameterBean.perfTimeInterval = 60;
        perfusionParameterBean.perfThresholdValue = 30;
        perfusionParameterBean.perfMinWeight = 100;
        // 引流参数重置
        drainParameterBean.drainTimeInterval = 60;
        drainParameterBean.drainThresholdValue = 30;
        drainParameterBean.drainZeroCyclePercentage = 100;
        drainParameterBean.drainOtherCyclePercentage = 75;
        drainParameterBean.drainTimeoutAlarm = 45;
        drainParameterBean.drainRinseVolume = 50;
        drainParameterBean.drainRinseNumber = 1;
        drainParameterBean.isDrainManualEmptying = false;
        drainParameterBean.drainWarnTimeInterval = 30;
        // 留腹参数
        retainParamBean.isAbdomenRetainingDeduct = false;
        retainParamBean.isZeroCycleUltrafiltration = false;
        retainParamBean.isAlarmWakeUp = false;
        // 补液参数重置
        supplyParameterBean.supplyTimeInterval = 60;
        supplyParameterBean.supplyThresholdValue = 30;
        supplyParameterBean.supplyTargetProtectionValue = 500;
        supplyParameterBean.supplyMinWeight = 500;
        // 开启语音
        PdproHelper.getInstance().updateTtsSoundOpen(true);
        // 用户参数设置
        userParameterBean.isHospital = false;
        userParameterBean.isNight = false;
        userParameterBean.formulaRrecommended = false;
        userParameterBean.underweight1 = 1f;
        userParameterBean.underweight2 = 2f;
        userParameterBean.underweight3 = 3.6f;
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.RETAIN_PARAM, retainParamBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.ttsSoundOpen, "true");
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, treatmentParameterEniity);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, userParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRAIN_PARAMETER, drainParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.PERFUSION_PARAMETER, perfusionParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.SUPPLY_PARAMETER, supplyParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.FIRST_RINSE_PARAMETER,firstRinseParameterBean);
        toastMessage("恢复成功");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void notifyByThemeChanged() {
        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
        helper.setBackgroundResourceByAttr(mAppBackground, R.attr.custom_attr_app_bg);
        if (mAdapter != null) mAdapter.notifyDataSetChanged(); //

        helper.setTextColorByAttr(tvTitle, R.attr.custom_attr_common_text_color);
    }

    private void wirelessSetting() {
//        Intent intent = new Intent("/");
//        ComponentName componentName = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
//        intent.setComponent(componentName);
//        intent.setAction("android.intent.action.VIEW");
//        startActivityForResult(intent,0);
        //3.0以上打开设置界面
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

//mobile 3G Data Network
        @SuppressLint("MissingPermission") NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        Log.e("工程师界面",mobile.toString());
//wifi
        @SuppressLint("MissingPermission") NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        Log.e("工程师界面",wifi.toString());

        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
    }


    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:
                doGoTOActivity(AboutActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
