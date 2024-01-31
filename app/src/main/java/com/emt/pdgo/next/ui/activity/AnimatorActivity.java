package com.emt.pdgo.next.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.KeyStore;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mario on 2017-03-05.
 */

public class AnimatorActivity extends BaseActivity {

    @BindView(R.id.mode_change_animator_view)
    public ImageView mAnimatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        initAllViews();
//        initViewData();
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_animator);
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
//        Log.e("动画模式", "模式"+getThemeTag()+"--"+ PdproHelper.getInstance().getUserParameterBean().isNight);
        mAnimatorView.setImageResource(getThemeTag() == -1 ? R.drawable.custom_drawable_mode_translation_turn_day_v16 : R.drawable.custom_drawable_mode_translation_turn_night_v16);
        sendEmptyMessageDelayed(KeyStore.KEY_TAG_ANIMATOR_START, 300);
        if (getThemeTag() == 1) {
            PdproHelper.getInstance().getUserParameterBean().isNight = true;
            sendCommandInterval(CommandDataHelper.getInstance().LedOpen("all",false,1,1), 500);
        }
    }

    @Override
    public void notifyByThemeChanged() {
        // TODO
    }

    private InternalHandler mHandler;
    private class InternalHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) return ;
            switch (msg.what) {
                case KeyStore.KEY_SKIP_ANIMATOR_FINISH:
                    finish();
                    break;
                case KeyStore.KEY_TAG_ANIMATOR_START:
                    startAnimator();
                    break;
                case KeyStore.KEY_TAG_ANIMATOR_STOP:
                    stopAnimator();
                    break;
                default:
                    break;
            }
        }

        /**
         *
         * */
        private void startAnimator() {
            Drawable drawable = mAnimatorView.getDrawable();
            if (drawable == null || !(drawable instanceof AnimationDrawable)) return ;
            ((AnimationDrawable) drawable).start();
            sendEmptyMessageDelayed(KeyStore.KEY_TAG_ANIMATOR_STOP, 1760);
        }

        /**
         *
         * */
        private void stopAnimator() {
            sendEmptyMessageDelayed(KeyStore.KEY_SKIP_ANIMATOR_FINISH, 360);
            switchCurrentThemeTag(); //
            ((MyApplication) getApplication()).notifyByThemeChanged(); //
        }
    }

    /**
     * */
    private void sendEmptyMessageDelayed(int what, long delay) {
        if (mHandler == null) mHandler = new InternalHandler();
        mHandler.sendEmptyMessageDelayed(what, delay);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
