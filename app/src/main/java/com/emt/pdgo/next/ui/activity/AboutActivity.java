package com.emt.pdgo.next.ui.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.BuildConfig;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {


    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.useDeviceTv)
    TextView useDeviceTv;
    @BindView(R.id.useDeviceRl)
    RelativeLayout useDeviceRl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_unit_max_warning_value)
    TextView tvV;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initHeadTitleBar("关于");
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
        useDeviceRl.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                Log.d("onTouch", "action down");
                    isLongPressed = true;
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                    useDeviceRl.postDelayed(mCheckForLongPress1, 5000);
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
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initViewData() {
        mCheckForLongPress1 = new CheckForLongPress1();
//        tvVersion.setText("V"+BuildConfig.VERSION_NAME);
        tvV.setText("V"+ BuildConfig.VERSION_NAME);
        useDeviceTv.setText(PdproHelper.getInstance().useDeviceTime()+"小时");
    }
    private CheckForLongPress1 mCheckForLongPress1;
    private volatile boolean isLongPressed = false;
    private class CheckForLongPress1 implements Runnable {

        @Override
        public void run() {
            //5s之后，查看isLongPressed的变量值：
            if (isLongPressed) {//没有做up事件
                Log.e("长按", "5s的事件触发");
                alertNumberBoardDialog("", PdGoConstConfig.zeroClear);
            } else {
                useDeviceRl.removeCallbacks(mCheckForLongPress1);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this.getContext(), value, type, false);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
//                    Logger.d(result);
                if (mType.equals(PdGoConstConfig.zeroClear)) {//工程师模式的密码
                    if ("303626".equals(result)) {
                        useDeviceTime = 0;
                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.useDeviceTime, 0+"");
                        useDeviceTv.setText(PdproHelper.getInstance().useDeviceTime()+"小时");
                    }
                }
            }
        });
    }

    @Override
    public void notifyByThemeChanged() {
        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
        helper.setBackgroundResourceByAttr(mAppBackground, R.attr.custom_attr_app_bg);


        helper.setTextColorByAttr(tvTitle, R.attr.custom_attr_common_text_color);
    }



}
