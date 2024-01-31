package com.emt.pdgo.next.ui.activity.apd;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.HisPrescriptionBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.EngineerSettingActivity;
import com.emt.pdgo.next.ui.activity.RemotePrescribingActivity;
import com.emt.pdgo.next.ui.activity.SettingActivity;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.adapter.dpr.ViewPagerAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.fragment.apd.prescription.NormalPrescriptionFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.SpecialPrescriptionFragment;
import com.emt.pdgo.next.ui.view.NoScrollViewPager;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.android.material.tabs.TabLayout;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApdPrescriptionActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.btn_submit)
    StateButton btnSubmit;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.layout_setting)
    LinearLayout layoutSetting;

    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;

    public String msg = "ApdPrescriptionActivity";

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_apd_prescription);
        ButterKnife.bind(this);
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
    private CheckForLongPress1 mCheckForLongPress1;
    private volatile boolean isLongPressed = false;
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
        btnSubmit.setOnClickListener(v -> doGoTOActivity(ApdParamSetActivity.class));
        tv_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                Log.d("onTouch", "action down");
                        isLongPressed = true;
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                        tv_title.postDelayed(mCheckForLongPress1, 5000);
                        break;
//            case MotionEvent.ACTION_MOVE:
//                isLongPressed = true;
//                break;
                    case MotionEvent.ACTION_UP:
                        isLongPressed = false;
//                Log.d("onTouch", "action up");
                        break;

                }

                return true;
            }
        });
        layoutSetting.setOnClickListener(v -> doGoTOActivity(SettingActivity.class));
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
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式的密码
                    if (tempPwd.equals(result)) {
                        doGoTOActivity(EngineerSettingActivity.class);
                    }
                }
            }
        });
    }
    private class CheckForLongPress1 implements Runnable {

        @Override
        public void run() {
            //5s之后，查看isLongPressed的变量值：
            if (isLongPressed) {//没有做up事件
                Log.e("长按", "5s的事件触发");
                alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
            } else {
                tv_title.removeCallbacks(mCheckForLongPress1);
            }
        }
    }

    @Override
    public void initViewData() {
        initHeadTitleBar("APD处方","治疗参数");
        initViewPage();
        init();
        if (MyApplication.versionMode == 2) {
            btnBack.setVisibility(View.VISIBLE);
        } else {
            btnBack.setVisibility(View.GONE);
        }
        mCheckForLongPress1 = new CheckForLongPress1();
    }

    private void init() {
        if (checkConnectNetwork(this) ){
            if (MyApplication.versionMode == 0) {
                appUpdate(false);
            }
            if (!MyApplication.isRemoteShow) {
                getRemotePd();
            }
        }
    }

    private CommonDialog dialog;
    private int predictUlt,agoRetention;
    //    public CompositeSubscription mCompositeSubscription;
    private void getRemotePd() {
        APIServiceManage.getInstance().postApdCode("Z2000");
        RetrofitUtil.getService().getRemotePrescription(PdproHelper.getInstance().getMachineSN()).enqueue(new Callback<MyResponse<HisPrescriptionBean>>() {
            @Override
            public void onResponse(Call<MyResponse<HisPrescriptionBean>> call, Response<MyResponse<HisPrescriptionBean>> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().getData().getCode().equals("10000")) {
                            APIServiceManage.getInstance().postApdCode("Z2001");
                            if (response.body().getData().getRcpInfo() != null) {
                                if (response.body().data.getRcpInfo().getPredictUlt().equals("")) {
                                    predictUlt = 0;
                                } else {
                                    predictUlt = (int) response.body().data.getRcpInfo().getPredictUlt();
                                }
                                if (response.body().data.getRcpInfo().getAgoRetention().equals("")) {
                                    agoRetention = 0;
                                } else {
                                    agoRetention = (int) response.body().data.getRcpInfo().getAgoRetention();
                                }
                                APIServiceManage.getInstance().postApdCode("Z2001");
                                CacheUtils.getInstance().getACache().put(PdGoConstConfig.UID, response.body().data.getRcpInfo().getPatientId()+"");
                                dialog = new CommonDialog(ApdPrescriptionActivity.this);
                                dialog.setContent("您有一份远程处方待确认")
                                        .setBtnFirst("确定")
                                        .setBtnTwo("取消")
                                        .setFirstClickListener(new CommonDialog.OnCommonClickListener() {
                                            @Override
                                            public void onClick(CommonDialog mCommonDialog) {
                                                Intent intent = new Intent(ApdPrescriptionActivity.this, RemotePrescribingActivity.class);
                                                intent.putExtra("icodextrinTotal", response.body().data.getRcpInfo().getIcodextrinTotal());
                                                intent.putExtra("inFlowCycle",response.body().data.getRcpInfo().getInFlowCycle());
                                                intent.putExtra("cycle",response.body().data.getRcpInfo().getCycle());
                                                intent.putExtra("firstInFlow",response.body().data.getRcpInfo().getFirstInFlow());
                                                intent.putExtra("retentionTime",response.body().data.getRcpInfo().getRetentionTime());
                                                intent.putExtra("lastRetention",response.body().data.getRcpInfo().getLastRetention());
                                                intent.putExtra("agoRetention",agoRetention);
                                                intent.putExtra("predictUlt",predictUlt);
                                                intent.putExtra("treatTime",response.body().data.getRcpInfo().getTreatTime());
                                                startActivity(intent);
                                                mCommonDialog.dismiss();
                                            }
                                        })
                                        .setTwoClickListener(Dialog::dismiss);
                                if (!ApdPrescriptionActivity.this.isFinishing()) {
                                    dialog.show();
                                    MyApplication.isRemoteShow = true;
                                }
                            }
                        } else {
                            APIServiceManage.getInstance().postApdCode("Z2020");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<HisPrescriptionBean>> call, Throwable t) {

            }
        });
    }

    private void initViewPage() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new NormalPrescriptionFragment());
        fragmentList.add(new SpecialPrescriptionFragment());
        List<String> titles = new ArrayList<>();
        titles.add("常规APD处方");
        titles.add("特殊APD处方");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList, titles);
        viewpager.setAdapter(adapter);
        viewpager.setNoScroll(false);
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public void notifyByThemeChanged() {
        runOnUiThread(() -> {

            MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
            helper.setBackgroundResourceByAttr(ivSetting, R.attr.custom_attr_icon_setting);
            helper.setTextColorByAttr(tvSetting, R.attr.custom_attr_common_text_color);
        });
    }
}