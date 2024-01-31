package com.emt.pdgo.next.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.bean.UserInfoBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class PreparationActivity extends BaseActivity {

    @BindView(R.id.custom_id_app_background)
    RelativeLayout mRLayoutAppBackground;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    //    @BindView(R.id.tv_username)
    TextView tvUsername;

    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;


    @BindView(R.id.iv_preparation_upper_part_bg)
    ImageView ivPreparationUpperPartBg;

    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.iv_pre_heat)
    ImageView ivPreHeat;
    @BindView(R.id.iv_pipeline_connect)
    ImageView ivPipelineConnect;

    @BindView(R.id.iv_num_1)
    ImageView ivNum1;
    @BindView(R.id.iv_num_2)
    ImageView ivNum2;
    @BindView(R.id.iv_num_3)
    ImageView ivNum3;

    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_pre_heat)
    TextView tvPreHeat;
    @BindView(R.id.tv_pipeline)
    TextView tvPipeline;

    @BindView(R.id.tv_info_sub)
    TextView tvInfoSub;
    @BindView(R.id.tv_pre_heat_sub)
    TextView tvPreHeatSub;
    @BindView(R.id.tv_pipeline_sub)
    TextView tvPipelineSub;

    @BindView(R.id.layout_info)
    RelativeLayout layoutInfo;
    @BindView(R.id.layout_pre_heat)
    RelativeLayout layoutPreHeat;
    @BindView(R.id.lyout_pipeline_connect)
    RelativeLayout layoutPipelineConnect;

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    private CheckForLongPress1 mCheckForLongPress1;
    private volatile boolean isLongPressed = false;
    private class CheckForLongPress1 implements Runnable {

        @Override
        public void run() {
            //5s之后，查看isLongPressed的变量值：
            if (isLongPressed) {//没有做up事件
                Log.e("长按", "5s的事件触发");
                alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
            } else {
                ivLogo.removeCallbacks(mCheckForLongPress1);
            }
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
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式的密码
                    if (tempPwd.equals(result)) {
                        doGoTOActivity(EngineerSettingActivity.class);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_preparation);
        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();
        getToken();
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
        tvSetting.setOnClickListener(v -> doGoTOActivity(SettingActivity.class));
        ivLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                Log.d("onTouch", "action down");
                        isLongPressed = true;
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                        ivLogo.postDelayed(mCheckForLongPress1, 5000);
                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        isLongPressed = true;
//                        break;
                    case MotionEvent.ACTION_UP:
                        isLongPressed = false;
//                Log.d("onTouch", "action up");
                        break;

                }
                return true;
            }
        });
    }
    @BindView(R.id.tvConnWifi)
    TextView tvConnWifi;
    private CompositeDisposable compositeDisposable;
    private void timing() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    if (!checkConnectNetwork(PreparationActivity.this)) {
                        tvConnWifi.setVisibility(View.VISIBLE);
                    } else {
                        tvConnWifi.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.interval(0, 500, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        compositeDisposable.add(disposableObserver);
    }

    @Override
    public void initViewData() {
        timing();
        tvConnWifi.setOnClickListener(v -> net());
        mCheckForLongPress1 = new CheckForLongPress1();
        tvUsername = findViewById(R.id.tv_username);//使用 @BindView(R.id.tv_username)绑定setText无法显示出来
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserInfoBean userInfoBean = PdproHelper.getInstance().getUserInfoBean();
                Log.e("用户信息", "" + userInfoBean.userName);
                if (TextUtils.isEmpty(userInfoBean.userName)) {
                    tvUsername.setText("未登录");
                } else {
                    tvUsername.setText(userInfoBean.userName);
                }
            }
        });

    }

    @OnClick({R.id.layout_setting, R.id.btn_operation, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_setting:
                doGoTOActivity(SettingActivity.class);
                break;
            case R.id.btn_operation://开始操作按钮，进入设置治疗信息界面
                doGoTOActivity(PrescriptionActivity.class);
                break;
            case R.id.btn_back:
                ActivityManager.getActivityManager().removeActivity(PreparationActivity.this);
//                onBackPressed();
                break;
        }
    }

    @Override
    public void notifyByThemeChanged() {

        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());

        helper.setBackgroundResourceByAttr(mRLayoutAppBackground, R.attr.custom_attr_app_bg);
        helper.setBackgroundResourceByAttr(ivPreparationUpperPartBg, R.attr.custom_attr_preparation_upper_part_bg);
        helper.setBackgroundResourceByAttr(layoutInfo, R.attr.custom_attr_preparation_item_bg);
        helper.setBackgroundResourceByAttr(layoutPreHeat, R.attr.custom_attr_preparation_item_bg);
        helper.setBackgroundResourceByAttr(layoutPipelineConnect, R.attr.custom_attr_preparation_item_bg);

        helper.setBackgroundResourceByAttr(ivInfo, R.attr.custom_attr_preparation_item_icon_01);
        helper.setBackgroundResourceByAttr(ivPreHeat, R.attr.custom_attr_preparation_item_icon_02);
        helper.setBackgroundResourceByAttr(ivPipelineConnect, R.attr.custom_attr_preparation_item_icon_03);
        helper.setBackgroundResourceByAttr(ivNum1, R.attr.custom_attr_preparation_item_icon_num_01);
        helper.setBackgroundResourceByAttr(ivNum2, R.attr.custom_attr_preparation_item_icon_num_02);
        helper.setBackgroundResourceByAttr(ivNum3, R.attr.custom_attr_preparation_item_icon_num_03);


        helper.setTextColorByAttr(tvInfo, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvPreHeat, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvPipeline, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvInfoSub, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvPreHeatSub, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvPipelineSub, R.attr.custom_attr_common_text_color);

        helper.setBackgroundResourceByAttr(ivHead, R.attr.custom_attr_preparation_icon_head);
        helper.setTextColorByAttr(tvUsername, R.attr.custom_attr_preparation_user_text_color);

        helper.setBackgroundResourceByAttr(ivSetting, R.attr.custom_attr_icon_setting);
        helper.setTextColorByAttr(tvSetting, R.attr.custom_attr_common_text_color_2);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
//        MyApplication.isPipecartInstall = false;
        compositeDisposable.clear();
    }

}
