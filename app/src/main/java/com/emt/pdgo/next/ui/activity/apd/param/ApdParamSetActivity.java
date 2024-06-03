package com.emt.pdgo.next.ui.activity.apd.param;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentDrainBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentParamBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentPerfuseBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentRetainBean;
import com.emt.pdgo.next.data.serial.treatment.SerialTreatmentSupplyBean;
import com.emt.pdgo.next.ui.adapter.dpr.ViewPagerAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.fragment.OtherFragment;
import com.emt.pdgo.next.ui.fragment.apd.param.ApdDrainParamFragment;
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

public class ApdParamSetActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.btnBack)
    Button btnBack;

    public static TreatmentParameterEniity treatmentParameterEniity;//治疗参数
    public static DrainParameterBean drainParameterBean;
    public static PerfusionParameterBean perfusionParameterBean;
    public static SupplyParameterBean supplyParameterBean;
    public static RetainParamBean retainParamBean;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_apd_param_set);
        ButterKnife.bind(this);
    }
    @Override
    public void registerEvents() {

        btnSave.setVisibility(View.VISIBLE);
        retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();
        drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        btnSave.setOnClickListener(v -> {
            save();
        });
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void save() {
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.RETAIN_PARAM, retainParamBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.SUPPLY_PARAMETER, supplyParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRAIN_PARAMETER, drainParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.PERFUSION_PARAMETER, perfusionParameterBean);
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, treatmentParameterEniity);
        toastMessage("保存成功");
//        init();
    }

    private void init() {
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        SerialTreatmentParamBean mParamBean = new SerialTreatmentParamBean();
        SerialTreatmentDrainBean drain = new SerialTreatmentDrainBean();
        drain.drainFlowRate = drainParameterBean.drainThresholdValue;//引流流速阈值
        drain.drainFlowPeriod = drainParameterBean.drainTimeInterval;//引流流速周期 秒
        drain.drainRinseVolume = drainParameterBean.drainRinseVolume;//引流冲洗量
        drain.drainRinseTimes = drainParameterBean.drainRinseNumber;//引流次数
        drain.firstDrainPassRate = drainParameterBean.drainZeroCyclePercentage;//0周期引流合格率
        drain.drainPassRate = drainParameterBean.drainOtherCyclePercentage;//周期引流合格率
        drain.isFinalDrainEmpty = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流是否排空
        drain.isFinalDrainEmptyWait = drainParameterBean.isDrainManualEmptying ? 1 : 0;//最末引流排空是否等待
        drain.finalDrainEmptyWaitTime = drainParameterBean.alarmTimeInterval;//最末引流排空等待时间 30
        drain.isVaccumDrain = drainParameterBean.isNegpreDrain ? 1 : 0;//是否开启负压引流

        SerialTreatmentPerfuseBean perfuse = new SerialTreatmentPerfuseBean();
        perfuse.perfuseFlowRate = perfusionParameterBean.perfThresholdValue;//灌注流速阈值
        perfuse.perfuseFlowPeriod = perfusionParameterBean.perfTimeInterval;//灌注流速周期 秒
        perfuse.perfuseMaxVolume = perfusionParameterBean.perfMaxWarningValue;//最大灌注量

        SerialTreatmentSupplyBean supply = new SerialTreatmentSupplyBean();
        supply.supplyFlowRate = supplyParameterBean.supplyThresholdValue;//补液流速阈值
        supply.supplyFlowPeriod = supplyParameterBean.supplyTimeInterval;//补液流速周期 秒
        supply.supplyProtectVolume = supplyParameterBean.supplyTargetProtectionValue;//补液目标保护值
        supply.supplyMinVolume = supplyParameterBean.supplyMinWeight;//启动补液的加热袋重量最低值

        SerialTreatmentRetainBean retain = new SerialTreatmentRetainBean();
        retain.isFinalRetainDeduct = retainParamBean.isAbdomenRetainingDeduct ? 1 : 0;//是否末次流量扣除
        retain.isFiltCycleOnly = retainParamBean.isZeroCycleUltrafiltration ? 1 : 0;// 1 是否只统计循环周期的超滤量
        retain.parammodify = 0;

        mParamBean.drain = drain;
        mParamBean.perfuse = perfuse;
        mParamBean.supply = supply;
        mParamBean.retain = retain;

        mSerialRequestBean.method = "treatparam/config";
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
        fragmentList.add(new ApdDrainParamFragment());
//        fragmentList.add(new ApdPerfusionParamFragment());
//        fragmentList.add(new ApdRetainParamFragment());
//        fragmentList.add(new ApdSupplyParamFragment());
        fragmentList.add(new OtherFragment());
        List<String> titles = new ArrayList<>();
        titles.add("引流参数");
//        titles.add("灌注参数");
//        titles.add("留腹参数");
//        titles.add("补液参数");
        titles.add("其他参数");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragmentList, titles);
        viewpager.setAdapter(adapter);
        viewpager.setNoScroll(false);
        viewpager.setOffscreenPageLimit(titles.size() - 1);
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public void notifyByThemeChanged() {

    }
}