package com.emt.pdgo.next.ui.activity.apd.prescrip;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.SettingActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApdActivity extends BaseActivity {

    @BindView(R.id.apd_rl)
    RelativeLayout apd_rl;
    @BindView(R.id.apd_tv)
    TextView apd_tv;


    @BindView(R.id.tpd_rl)
    RelativeLayout tpd_rl;
    @BindView(R.id.tpd_tv)
    TextView tpd_tv;


    @BindView(R.id.sp_rl)
    RelativeLayout sp_rl;
    @BindView(R.id.sp_tv)
    TextView sp_tv;

    @BindView(R.id.kid_rl)
    RelativeLayout kid_rl;
    @BindView(R.id.kid_tv)
    TextView kid_tv;

    @BindView(R.id.expert_rl)
    RelativeLayout expert_rl;
    @BindView(R.id.expert_tv)
    TextView expert_tv;

    @BindView(R.id.specialRl)
    RelativeLayout specialRl;

    @BindView(R.id.aapd_rl)
    RelativeLayout aapd_rl;
    @BindView(R.id.aapd_tv)
    TextView aapd_tv;

    @BindView(R.id.layout_setting)
    LinearLayout layout_setting;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;
    @BindView(R.id.tv_setting)
    TextView tv_setting;

    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_apd);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_apd);
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
        apd_rl.setOnClickListener(v -> doGoTOActivity(ApdNorActivity.class));
        tpd_rl.setOnClickListener(v -> doGoTOActivity(TpdActivity.class));
        aapd_rl.setOnClickListener(v -> doGoTOActivity(AapdActivity.class));
        kid_rl.setOnClickListener(v -> doGoTOActivity(KidActivity.class));
        expert_rl.setOnClickListener(v -> doGoTOActivity(ExpertActivity.class));
        sp_rl.setOnClickListener(v -> {
            isShow(isShow);
        });
        layout_setting.setOnClickListener(v -> doGoTOActivity(SettingActivity.class));
    }

    private void isShow(boolean show) {
        isShow = !show;
        specialRl.setVisibility(isShow?View.VISIBLE:View.GONE);
        sp_rl.setBackgroundResource(isShow ?R.drawable.gray_shape:R.drawable.key_board_selector_item_pressed_night);
    }

    @Override
    public void initViewData() {
        specialRl.setVisibility(View.VISIBLE);
        if (MyApplication.versionMode == 0) {
            appUpdate(false);
        }
//        isShow(true);
    }

    @Override
    public void notifyByThemeChanged() {

    }
}