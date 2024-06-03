package com.emt.pdgo.next.ui.activity;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.constant.ConfigConst;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.FirstRinseParameterBean;
import com.emt.pdgo.next.data.bean.OtherParamBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.UserParameterBean;
import com.emt.pdgo.next.data.entity.CommandItem;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.PdEntity;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.activity.local.LocalPrescriptionActivity;
import com.emt.pdgo.next.ui.activity.param.DrainParameterActivity;
import com.emt.pdgo.next.ui.activity.param.FirstRinseParameterActivity;
import com.emt.pdgo.next.ui.activity.param.PerfusionParameterActivity;
import com.emt.pdgo.next.ui.activity.param.SNSetActivity;
import com.emt.pdgo.next.ui.activity.param.SupplyParameterActivity;
import com.emt.pdgo.next.ui.activity.param.TemperatureControlParameterActivity;
import com.emt.pdgo.next.ui.activity.param.TestLocationActivity;
import com.emt.pdgo.next.ui.activity.param.TtsTestActivity;
import com.emt.pdgo.next.ui.activity.param.UploadDataTestActivity;
import com.emt.pdgo.next.ui.activity.param.UrlSetActivity;
import com.emt.pdgo.next.ui.activity.param.UserParameterActivity;
import com.emt.pdgo.next.ui.activity.param.ValveTestActivity;
import com.emt.pdgo.next.ui.activity.param.VolumeActivity;
import com.emt.pdgo.next.ui.activity.param.WeighParameterActivity;
import com.emt.pdgo.next.ui.activity.wifi.WifiActivity;
import com.emt.pdgo.next.ui.adapter.CommandAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.pdp.rmmit.pdp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.rv_set)
    RecyclerView rvSet;


    private List<CommandItem> mList;
    private CommandAdapter mAdapter;


    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;
    @Override
    public void registerEvents() {
        btnSave.setVisibility(View.VISIBLE);
        btnSave.setText("关于");
        btnBack.setOnClickListener(view -> onBackPressed());
        btnSave.setOnClickListener(view -> doGoTOActivity(AboutActivity.class));
    }

    @Override
    public void initViewData() {
//        init(PdproHelper.getInstance().getLeaveTime(),EmtTimeUil.getTime());
//        zeroClear.setOnClickListener(v -> {
//            leaveTime.setText(EmtTimeUil.getTimeWithYmd1());
//            CacheUtils.getInstance().getACache().put(PdGoConstConfig.LeaveTime, EmtTimeUil.getTimeWithYmd1());
//            init(EmtTimeUil.getTimeWithYmd1(), EmtTimeUil.getTime());
//        });
//        initTime();
        mList = new ArrayList<>();

//        mList.add(new CommandItem("治疗界面", "TreatmentFragment"));

        mList.add(new CommandItem("网络参数设置", "NetSet"));
        mList.add(new CommandItem("声音设置","volume"));
//        mList.add(new CommandItem("历史治疗数据", "hisPdData"));
//        mList.add(new CommandItem("历史处方数据","hisRxData"));
        mList.add(new CommandItem("本机治疗数据", "localPdData"));
        mList.add(new CommandItem("本机处方数据", "localRxData"));
        mList.add(new CommandItem("恢复出厂设置", "factoryReset"));
//        mList.add(new CommandItem("添加模拟数据", "addMockData"));
//        mList.add(new CommandItem(msg,"dataSet"));
//        mList.add(new CommandItem("数据上报测试", "uploadDataTest"));

//        mList.add(new CommandItem("SN参数设置", "SNSet"));
//        mList.add(new CommandItem("传感器数值校准", "WeighParameter"));
//        mList.add(new CommandItem("温控参数设置", "TemperatureControlParameter"));
        mList.add(new CommandItem("预冲参数设置", "FirstRinseParameter"));
//        mList.add(new CommandItem("APD治疗参数设置", "apdParamSet"));
//
//        mList.add(new CommandItem("引流参数设置", "DrainParameter"));
//        mList.add(new CommandItem("灌注参数设置", "PerfusionParameter"));
//        mList.add(new CommandItem("补液参数设置", "SupplyParameter"));
//
//
//
//        mList.add(new CommandItem("电机调试功能", "ValveTest"));
//
//        mList.add(new CommandItem("TTS语音调试", "Tts"));
//        mList.add(new CommandItem("测试定位", "TestLocation"));

//
////        mList.add(new CommandItem("工程师调试设置", "Debug"));
//        mList.add(new CommandItem("输入治疗信息", "InputTreatment"));
//        mList.add(new CommandItem("体重血压", "InputBodyData"));
//        mList.add(new CommandItem("自检界面", "SelfCheck"));
//        mList.add(new CommandItem("扫码登录", "ScanCodeLogin"));
//        mList.add(new CommandItem("准备工作", "Preparation"));
//        mList.add(new CommandItem("预热程序", "PreHeat"));
//        mList.add(new CommandItem("管道连接", "PipelineConnection"));
////
//        mList.add(new CommandItem("管路预冲", "PreRinse"));
//        mList.add(new CommandItem("治疗开始倒计时", "Countdown"));
//        mList.add(new CommandItem("治疗界面", "TreatmentFragment"));
//
//        mList.add(new CommandItem("数据收集", "DataCollection"));
//        mList.add(new CommandItem("治疗反馈", "TreatmentFeedback"));
//        mList.add(new CommandItem("治疗数据评估", "TreatmentEvaluation"));
//        mList.add(new CommandItem("电池供电关机", "power_monitor"));

        mList.add(new CommandItem("系统升级","appUpdate"));
        mAdapter = new CommandAdapter(this, R.layout.item_setting, mList);
        rvSet.setLayoutManager(new GridLayoutManager(this, 3));
        rvSet.setAdapter(mAdapter);


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if ("SNSet".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(SNSetActivity.class);
                } else if ("UrlSet".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(UrlSetActivity.class);
                } else if ("NetSet".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(WifiActivity.class);
                } else if ("uploadDataTest".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(UploadDataTestActivity.class);
                } else if ("WeighParameter".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(WeighParameterActivity.class);
                } else if ("TemperatureControlParameter".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(TemperatureControlParameterActivity.class);
                } else if ("FirstRinseParameter".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(FirstRinseParameterActivity.class);
                    // apdParamSet
                } else if ("apdParamSet".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(ApdParamSetActivity.class);
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
                } else if ("Tts".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(TtsTestActivity.class);
                } else if ("InputTreatment".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(InputTreatmentInfoActivity.class);
                }  else if ("PreHeat".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(PreHeatActivity.class);
                } else if ("PipelineConnection".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(PipelineConnectionActivity.class);
                } else if ("TreatmentFragment".equals(mList.get(position).mCommand)) {
                    doGoCloseTOActivity(TreatmentFragmentActivity.class,"");
                } else if ("TreatmentFeedback".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(TreatmentFeedbackActivity.class);
                } else if ("PreRinse".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(PreRinseActivity.class);
                } else if ("Countdown".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(TreatmentCountdownActivity.class);
                } else if ("DataCollection".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(DataCollectionActivity.class);
                } else if ("localRxData".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(LocalPrescriptionActivity.class);
                } else if ("TreatmentEvaluation".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(TreatmentEvaluationActivity.class);
                } else if ("power_monitor".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(PowerMonitorDialogActivity.class);
                } else if ("TestLocation".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(TestLocationActivity.class);
                } else if ("volume".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(VolumeActivity.class);
                } else if ("dataSet".equals(mList.get(position).mCommand)) {
//                    setMobileDataState(SettingActivity.this, !getMobileDataState(SettingActivity.this));
                } else if ("appUpdate".equals(mList.get(position).mCommand)) {
                    appUpdate(true);
                } else if ("factoryReset".equals(mList.get(position).mCommand)) {
                    alertNumberBoardDialog("",PdGoConstConfig.RESET);
                } else if ("hisRxData".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(HistoricalPrescriptionActivity.class);
                } else if ("hisPdData".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(HistoricalTreatmentActivity.class);
                } else if ("localPdData".equals(mList.get(position).mCommand)) {
                    doGoTOActivity(MyDreamActivity.class);
                } else if ("addMockData".equals(mList.get(position).mCommand)) {
                    PdEntity pdEntity = new PdEntity();
                    pdEntity.totalUltVol = 2000;
                    pdEntity.totalDrainVol = 5000;
                    pdEntity.totalPerVol = 6000;
                    pdEntity.cycle = 9;
                    pdEntity.totalVol = 8000;
                    pdEntity.startTime = EmtTimeUil.getTime();
                    pdEntity.endTime = EmtTimeUil.getTime();
                    List<PdEntity.PdInfoEntity> pdInfoEntities = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        PdEntity.PdInfoEntity pdInfoEntity = new PdEntity.PdInfoEntity();
                        pdInfoEntity.abdTime =
                        pdInfoEntity.drainTime =
                        pdInfoEntity.preTime =
                        pdInfoEntity.preVol =
                        pdInfoEntity.abdAfterVol =
                        pdInfoEntity.auFvVol =
                        pdInfoEntity.drainTvVol =
                        pdInfoEntity.cycle =
                                pdInfoEntity.drainage =
                        pdInfoEntity.remain = i;
                        pdInfoEntities.add(pdInfoEntity);
                    }
                    pdEntity.pdInfoEntities = pdInfoEntities;
                    EmtDataBase.getInstance(SettingActivity.this).getPdDao()
                            .insertPd(pdEntity);
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

    @SuppressLint("SetTextI18n")
    private void initTime(String endTime, String startTime) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date end = formatter.parse(endTime);//结束时间
            Date start = formatter.parse(startTime);//开始时间
            // 这样得到的差值是微秒级别
            assert end != null;
            assert start != null;
            long diff = end.getTime() - start.getTime();
            long year = diff / (1000L * 60 * 60 * 24 * 365);
            long mouth = (diff - (1000L * 60 * 60 * 24 * 365));
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//            Log.e("initTime", "剩余: " + days + "天" + hours + "小时" + minutes + "分");
//            useTime.setText(hours+"时"+minutes+"分");
        } catch (Exception e) {
            Log.e("initTime", e.getMessage());
        }

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
                if (mType.equals(PdGoConstConfig.RESET)) {//工程师模式的密码
                    if (tempPwd.equals(result)) {
                        factoryReset();
                    }
                }
            }
        });
    }

    private void factoryReset() {
//        MyApplication.isReset = true;
        FirstRinseParameterBean firstRinseParameterBean = PdproHelper.getInstance().getFirstRinseParameterBean();
        PerfusionParameterBean perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        DrainParameterBean drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        SupplyParameterBean supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();
        UserParameterBean userParameterBean = PdproHelper.getInstance().getUserParameterBean();
        RetainParamBean retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        IpdBean treatmentParameterEniity = PdproHelper.getInstance().ipdBean();

        // 处方
        treatmentParameterEniity.peritonealDialysisFluidTotal = ConfigConst.peritonealDialysisFluidTotal;
        treatmentParameterEniity.perCyclePerfusionVolume = ConfigConst.perCyclePerfusionVolume;
        treatmentParameterEniity.cycle = ConfigConst.cycle;
        treatmentParameterEniity.firstPerfusionVolume = ConfigConst.firstPerfusionVolume;
        treatmentParameterEniity.abdomenRetainingTime = ConfigConst.abdomenRetainingTime;
        treatmentParameterEniity.abdomenRetainingVolumeFinally = ConfigConst.abdomenRetainingVolumeFinally;
        treatmentParameterEniity.abdomenRetainingVolumeLastTime = ConfigConst.abdomenRetainingVolumeLastTime;
        treatmentParameterEniity.ultrafiltrationVolume = ConfigConst.ultrafiltrationVolume;
        treatmentParameterEniity.isFinalSupply = ConfigConst.isFinalSupply;
        // 预冲参数重置
        firstRinseParameterBean.firstvolume = ConfigConst.firstVolume;
        firstRinseParameterBean.secondvolume = ConfigConst.secondVolume;
        firstRinseParameterBean.supplyperiod = ConfigConst.supplyPeriod;
        firstRinseParameterBean.supplyspeed = ConfigConst.supplySpeed;
        firstRinseParameterBean.supplyselect = ConfigConst.supplySelect;
        firstRinseParameterBean.supplychvolume = ConfigConst.supplyVol;
        // 灌注参数重置
        perfusionParameterBean.perfAllowAbdominalVolume = ConfigConst.perfAllowAbdominalVolume;
        perfusionParameterBean.perfMaxWarningValue = ConfigConst.perfMaxWarningValue;
        perfusionParameterBean.perfTimeInterval = ConfigConst.perfTimeInterval;
        perfusionParameterBean.perfThresholdValue = ConfigConst.perfThresholdValue;
        perfusionParameterBean.perfMinWeight = ConfigConst.perfMinWeight;
        // 引流参数重置
        drainParameterBean.drainTimeInterval = ConfigConst.drainTimeInterval;
        drainParameterBean.drainThresholdValue = ConfigConst.drainThresholdValue;
        drainParameterBean.drainZeroCyclePercentage = ConfigConst.drainZeroCyclePercentage;
        drainParameterBean.drainOtherCyclePercentage = ConfigConst.drainOtherCyclePercentage;
        drainParameterBean.drainTimeoutAlarm = ConfigConst.drainTimeoutAlarm;
        drainParameterBean.drainRinseVolume = ConfigConst.drainRinseVolume;
        drainParameterBean.drainRinseNumber = ConfigConst.drainRinseNumber;
        drainParameterBean.isDrainManualEmptying = ConfigConst.isDrainManualEmptying;
        drainParameterBean.drainWarnTimeInterval = ConfigConst.drainWarnTimeInterval;
        // 留腹参数
        retainParamBean.isAbdomenRetainingDeduct = ConfigConst.isAbdomenRetainingDeduct;
        retainParamBean.isZeroCycleUltrafiltration = ConfigConst.isZeroCycleUltrafiltration;
        retainParamBean.isAlarmWakeUp = false;
        // 补液参数重置
        supplyParameterBean.supplyTimeInterval = ConfigConst.supplyTimeInterval;
        supplyParameterBean.supplyThresholdValue = ConfigConst.supplyThresholdValue;
        supplyParameterBean.supplyTargetProtectionValue = ConfigConst.supplyTargetProtectionValue;
        supplyParameterBean.supplyMinWeight = ConfigConst.supplyMinWeight;
        // 开启语音
//        PdproHelper.getInstance().updateTtsSoundOpen(true);
        // 用户参数设置
        userParameterBean.isHospital = false;
        userParameterBean.isNight = false;
        userParameterBean.formulaRrecommended = false;
        userParameterBean.underweight1 = 1f;
        userParameterBean.underweight2 = 2f;
        userParameterBean.underweight3 = 3.6f;

        OtherParamBean otherParamBean = PdproHelper.getInstance().getOtherParamBean();
        otherParamBean.lower = EmtConstant.lower;
        otherParamBean.upper = EmtConstant.upper;
//        otherParamBean.isDebug = false;
//        otherParamBean.isHospital = false;
//        otherParamBean.perHeartWeight = 500;
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.OTHER_PARAMETER, otherParamBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.RETAIN_PARAM, retainParamBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.ttsSoundOpen, "true");
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, treatmentParameterEniity);
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, treatmentParameterEniity);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_PARAMETER, userParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRAIN_PARAMETER, drainParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.PERFUSION_PARAMETER, perfusionParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.SUPPLY_PARAMETER, supplyParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.FIRST_RINSE_PARAMETER,firstRinseParameterBean);
        toastMessage("恢复成功");
    }

    @Override
    public void notifyByThemeChanged() {
        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
        helper.setBackgroundResourceByAttr(mAppBackground, R.attr.custom_attr_app_bg);
        if (mAdapter != null) mAdapter.notifyDataSetChanged(); //

    }

}
