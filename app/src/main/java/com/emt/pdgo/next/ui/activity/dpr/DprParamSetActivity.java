package com.emt.pdgo.next.ui.activity.dpr;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.dpr.param.ConfigBean;
import com.emt.pdgo.next.data.serial.dpr.param.DprConfigParam;
import com.emt.pdgo.next.data.serial.dpr.param.DrainConfigParam;
import com.emt.pdgo.next.data.serial.dpr.param.PerfuseConfigParam;
import com.emt.pdgo.next.data.serial.dpr.param.RetainConfigParam;
import com.emt.pdgo.next.data.serial.dpr.param.SupplyConfigParam;
import com.emt.pdgo.next.model.dpr.machine.param.DprOtherParam;
import com.emt.pdgo.next.model.dpr.machine.param.DrainParam;
import com.emt.pdgo.next.model.dpr.machine.param.PerfuseParam;
import com.emt.pdgo.next.model.dpr.machine.param.RetainParam;
import com.emt.pdgo.next.model.dpr.machine.param.SupplyParam;
import com.emt.pdgo.next.ui.adapter.dpr.ViewPagerAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.fragment.dpr.param.DprDrainParamFragment;
import com.emt.pdgo.next.ui.fragment.dpr.param.DprOtherParamFragment;
import com.emt.pdgo.next.ui.fragment.dpr.param.DprPerfusionParamFragment;
import com.emt.pdgo.next.ui.fragment.dpr.param.DprRetainParamFragment;
import com.emt.pdgo.next.ui.fragment.dpr.param.DprSupplyParamFragment;
import com.emt.pdgo.next.ui.view.NoScrollViewPager;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DprParamSetActivity extends BaseActivity {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.btnBack)
    Button btnBack;

    public static DrainParam drainParam;
    public static PerfuseParam perfuseParam;
    public static SupplyParam supplyParam;
    public static RetainParam retainParam;
    public static DprOtherParam otherParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_dpr_param_set);
        ButterKnife.bind(this);
        btnSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void registerEvents() {
        drainParam = PdproHelper.getInstance().getDrainParam();
        supplyParam = PdproHelper.getInstance().getSupplyParam();
        retainParam = PdproHelper.getInstance().getRetainParam();
        perfuseParam = PdproHelper.getInstance().getPerfuseParam();
        otherParam = PdproHelper.getInstance().getDprOtherParam();
        btnSave.setOnClickListener(v -> {
            save();
        });
        btnBack.setOnClickListener(view -> onBackPressed());
    }

    private void save() {
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DPR_DRAIN_PARAM, drainParam);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DPR_SUPPLY_PARAM, supplyParam);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DPR_RETAIN_PARAM, retainParam);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DPR_PERFUSION_PARA, perfuseParam);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DPR_OTHER_PARAM, otherParam);
        toastMessage("保存成功");

        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        ConfigBean mParamBean = new ConfigBean();

        DprConfigParam dpr = new DprConfigParam();
        dpr.FaultTime = otherParam.FaultTime;
        dpr.DprFlowPeriod = otherParam.DprFlowPeriod;
        dpr.DprSuppthreshold = otherParam.DprSuppthreshold;
        dpr.PerfuseDecDrainThod = otherParam.PerfuseDecDrainThod;
        dpr.TotalRemainderThod = otherParam.TotalRemainderThod;

        DrainConfigParam drain = new DrainConfigParam();
        drain.drainFlowRate = drainParam.drainFlowRate;
        drain.VaccumDraintimes = drainParam.VaccumDraintimes;
        drain.drainPassRate = drainParam.drainPassRate;
        drain.drainFlowPeriod = drainParam.drainFlowPeriod;
        drain.finalDrainEmptyWaitTime = drainParam.finalDrainEmptyWaitTime;
        drain.isFinalDrainEmpty = drainParam.isFinalDrainEmpty ? 1 : 0;
        drain.isFinalDrainEmptyWait = drainParam.isFinalDrainEmptyWait ? 1 : 0;
        drain.isVaccumDrain = drainParam.isVaccumDrain ? 1 : 0;

        PerfuseConfigParam perfuse = new PerfuseConfigParam();
        perfuse.perfuseFlowPeriod = perfuseParam.perfuseFlowPeriod;
        perfuse.perfuseFlowRate = perfuseParam.perfuseFlowRate;
        perfuse.perfuseMaxVolume = perfuseParam.perfuseMaxVolume;

        SupplyConfigParam supply = new SupplyConfigParam();
        supply.supplyFlowPeriod = supplyParam.supplyFlowPeriod;
        supply.supplyFlowRate = supplyParam.supplyFlowRate;
        supply.supplyMinVolume = supplyParam.supplyMinVolume;
        supply.supplyProtectVolume = supplyParam.supplyProtectVolume;

        RetainConfigParam retain = new RetainConfigParam();
        retain.isFiltCycleOnly = retainParam.isFiltCycleOnly ? 1 : 0;
        retain.isFinalRetainDeduct = retainParam.isFinalRetainDeductl ? 1 : 0;
        retain.parammodify = MyApplication.dprTreatRunning ? 1 : 0;

        mParamBean.drain = drain;
        mParamBean.dpr = dpr;
        mParamBean.perfuse = perfuse;
        mParamBean.retain = retain;
        mParamBean.supply = supply;

        mSerialRequestBean.method = "dprparam/config";
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        String sendData = new Gson().toJson(mRequestBean);
        sendToMainBoard(sendData);
    }


    @Override
    public void initViewData() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new DprDrainParamFragment());
        fragmentList.add(new DprPerfusionParamFragment());
        fragmentList.add(new DprRetainParamFragment());
        fragmentList.add(new DprSupplyParamFragment());
        fragmentList.add(new DprOtherParamFragment());
        List<String> titles = new ArrayList<>();
        titles.add("引流参数");
        titles.add("灌注参数");
        titles.add("留腹参数");
        titles.add("补液参数");
        titles.add("其他参数");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragmentList, titles);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(4);
        viewpager.setNoScroll(false);
        tabLayout.setupWithViewPager(viewpager);
    }

//    @OnClick({R.id.drainTimeIntervalEt, R.id.drainTimeIntervalRl, R.id.drainThresholdRl, R.id.drainThresholdEt,
//            R.id.drainZeroCycleRatioEt, R.id.drainZeroCycleRatioRl, R.id.drainOtherCycleRatioRl, R.id.drainOtherCycleRatioEt,
//            R.id.drainTimeoutAlarmEt, R.id.drainTimeoutAlarmRl,R.id.drainAuxiliaryFlushVolumeEt, R.id.drainAuxiliaryFlushVolumeRl,
//            R.id.drainAuxiliaryFlushNumRl, R.id.drainAuxiliaryFlushNumEt, R.id.drainReminderIntervalEt, R.id.drainReminderIntervalRl,
//            R.id.infusionTimeIntervalEt, R.id.infusionTimeIntervalRl,R.id.infusionThresholdRl, R.id.infusionThresholdEt,
//            R.id.infusionMaximumAlertEt, R.id.infusionMaximumAlertRl,R.id.rehydrationTimeIntervalEt, R.id.rehydrationTimeIntervalRl,
//            R.id.rehydrationThresholdEt, R.id.rehydrationThresholdRl,R.id.rehydrationTargetValueEt, R.id.rehydrationTargetValueRl,
//            R.id.rehydrationMinimumEt, R.id.rehydrationMinimumRl, R.id.btn_submit})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.drainTimeIntervalRl:
//            case R.id.drainTimeIntervalEt:
//                alertNumberBoardDialog(drainTimeIntervalEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_TIME_INTERVAL);
//                break;
//            case R.id.drainThresholdRl:
//            case R.id.drainThresholdEt:
//                alertNumberBoardDialog(drainThresholdEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE);
//                break;
//
//            case R.id.drainTimeoutAlarmRl:
//            case R.id.drainTimeoutAlarmEt:
//                alertNumberBoardDialog(drainTimeoutAlarmEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM);
//                break;
//            case R.id.drainAuxiliaryFlushVolumeEt:
//            case R.id.drainAuxiliaryFlushVolumeRl:
//                alertNumberBoardDialog(drainAuxiliaryFlushVolumeEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME);
//                break;
//            case R.id.drainAuxiliaryFlushNumEt:
//            case R.id.drainAuxiliaryFlushNumRl:
//                alertNumberBoardDialog(drainAuxiliaryFlushNumEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER);
//                break;
//            case R.id.drainReminderIntervalEt:
//            case R.id.drainReminderIntervalRl:
//                alertNumberBoardDialog(drainReminderIntervalEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL);
//                break;
//            case R.id.infusionTimeIntervalEt:
//            case R.id.infusionTimeIntervalRl:
//                alertNumberBoardDialog(infusionTimeIntervalEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL);
//                break;
//            case R.id.infusionThresholdRl:
//            case R.id.infusionThresholdEt:
//                alertNumberBoardDialog(infusionThresholdEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE);
//                break;
//            case R.id.infusionMaximumAlertEt:
//            case R.id.infusionMaximumAlertRl:
//                alertNumberBoardDialog(infusionMaximumAlertEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE);
//                break;
//            case R.id.rehydrationTimeIntervalEt:
//            case R.id.rehydrationTimeIntervalRl:
//                alertNumberBoardDialog(rehydrationTimeIntervalEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL);
//                break;
//            case R.id.rehydrationThresholdEt:
//            case R.id.rehydrationThresholdRl:
//                alertNumberBoardDialog(rehydrationThresholdEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE);
//                break;
//            case R.id.rehydrationTargetValueEt:
//            case R.id.rehydrationTargetValueRl:
//                alertNumberBoardDialog(rehydrationTargetValueEt.getText().toString(), PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE);
//                break;
//            case R.id.rehydrationMinimumEt:
//            case R.id.rehydrationMinimumRl:
//                alertNumberBoardDialog(rehydrationMinimumEt.getText().toString(),PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT);
//                break;
//            case R.id.btn_submit:
//                toastMessage("保存成功");
//                break;
//        }
//    }
//
//    private void alertNumberBoardDialog(String value, String type) {
//        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
//        dialog.show();
//        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
//            @Override
//            public void onResult(String mType, String result) {
//                if (!TextUtils.isEmpty(result)) {
//                    switch (mType) {
//                        case PdGoConstConfig.CHECK_TYPE_DRAIN_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
//                            drainTimeIntervalEt.setText(result);
//
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
//                            drainThresholdEt.setText(result);
//
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE: //0周期引流比例  :  50-100
//
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE: //其他周期引流比例  : 50-100
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM: //引流/灌注超时报警  : 0-600
//                            drainTimeoutAlarmEt.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME: //引流辅助冲洗 量  :  30-200
//                            drainAuxiliaryFlushVolumeEt.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER: //引流辅助冲洗 次数  :  1-3改成0-3
//                            drainAuxiliaryFlushNumEt.setText(result);
////                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_LATENCY_TIME)) {//最末引流等待  : 0-60
////                        etUnitLatencyTime.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL: //最末引流提醒间隔  : 0-60
//                            drainReminderIntervalEt.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
//                            infusionTimeIntervalEt.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
//                            infusionThresholdEt.setText(result);
////                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PERFUSION_MIN_WEIGHT)) {//加热袋最低重量允许  :  100-1000
////
////                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PERFUSION_ALLOW_ABDOMINAL_VOLUME)) {//是否允许最末灌注减去留腹量  : 1000-3000
//
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE: //灌注最大警戒值  : 1000-3000
//                            infusionMaximumAlertEt.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
//                            rehydrationTimeIntervalEt.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
//                            rehydrationThresholdEt.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE: //补液目标保护值  :  0-500
//                            rehydrationTargetValueEt.setText(result);
//                            break;
//                        case PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT: //启动补液的加热袋最低值  : 500-10000
//                            rehydrationMinimumEt.setText(result);
//                            break;
//                    }
//                }
//            }
//        });
//    }

    @Override
    public void notifyByThemeChanged() {

    }
}