package com.emt.pdgo.next.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.activity
 * @ClassName: BindPhoneActivity
 * @Description: 短信接收评估, 绑定手机
 * @Author: chenjh
 * @CreateDate: 2020/1/6 6:14 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/6 6:14 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.root_view)
    RelativeLayout rootView;

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.layout_back)
    RelativeLayout layoutBack;

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.layout_phone)
    RelativeLayout layoutPhone;

    @BindView(R.id.et_sms_code)
    EditText etSmsCode;
    @BindView(R.id.btn_sms_code)
    Button btnSmsCode;
    @BindView(R.id.layout_sms_code)
    RelativeLayout layoutSmsCode;

    @BindView(R.id.btn_bind_phone)
    Button btnBindPhone;

    private String phone;
    private String authCode;

    private static MyTimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_bind_phone);
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
    }

    @Override
    public void initViewData() {
        timeCount = new MyTimeCount(60000, 1000);
        etPhone.clearFocus();
        etSmsCode.clearFocus();
        etPhone.setText(MyApplication.phone);
        etPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
                layoutPhone.setBackgroundResource(R.drawable.bg_edittext_normal_day);
            } else {
                // 此处为失去焦点时的处理内容
                if (TextUtils.isEmpty(etPhone.getText().toString().toString())) {
                    layoutPhone.setBackgroundResource(R.drawable.bg_edittext_focused_day);
                } else {
                    layoutPhone.setBackgroundResource(R.drawable.bg_edittext_gray);
                }

            }
        });

        etSmsCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
                layoutSmsCode.setBackgroundResource(R.drawable.bg_edittext_normal_day);
            } else {
                // 此处为失去焦点时的处理内容
                if (TextUtils.isEmpty(etSmsCode.getText().toString().toString())) {
                    layoutSmsCode.setBackgroundResource(R.drawable.bg_edittext_focused_day);
                } else {
                    layoutSmsCode.setBackgroundResource(R.drawable.bg_edittext_gray);
                }

            }
        });

        rootView.setOnTouchListener((arg0, arg1) -> {
            rootView.requestFocus();
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(etPhone.getWindowToken(), 0);
            return false;
        });
    }


    @OnClick({R.id.btn_back, R.id.btn_sms_code, R.id.btn_bind_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_sms_code://获取验证码短信
                phone = etPhone.getText().toString().trim();
                authCode = etSmsCode.getText().toString().trim();
                ApiSendCode();
                break;
            case R.id.btn_bind_phone://确定按钮：绑定账户
                MyApplication.phone = etPhone.getText().toString().trim();
                break;
        }
    }

    private void ApiSendCode() {
        timeCount.start();
//
//        if (!CheckUtil.checkNetworkConnection(this)) {
//            showCommonDialog("网络不可用");
//            return;
//        }
//        showLoadingDialog();
//
//        Subscription subscription = apiServiceManage.smsCodeApi(phone)
//                .subscribe(newSubscriber((Action1<BaseBean>) mBean -> {
//                    hideLoadingDialog();
//                    showCommonDialog("验证码发送成功");
//                    timeCount.start();
//                }));
//
//        mCompositeSubscription.add(subscription);

    }


    public class MyTimeCount extends CountDownTimer {


        public MyTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnSmsCode.setEnabled(false);
            btnSmsCode.setText(millisUntilFinished / 1000 + "S");
        }

        @Override
        public void onFinish() {
            btnSmsCode.setEnabled(true);
            btnSmsCode.setText("验证码");
        }

    }

    @Override
    public void notifyByThemeChanged() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
            timeCount.onFinish();
        }
    }
}
