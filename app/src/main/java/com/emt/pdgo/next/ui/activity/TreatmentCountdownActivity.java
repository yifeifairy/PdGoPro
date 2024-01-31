package com.emt.pdgo.next.ui.activity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.LoadingDialogFragment;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.activity
 * @ClassName: TreatmentCountdownActivity
 * @Description: 开始治疗倒数计时
 * @Author: chenjh
 * @CreateDate: 2020/1/3 11:53 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/3 11:53 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class TreatmentCountdownActivity extends BaseActivity {

    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.btnGoToTreatment)
    StateButton btnGoToTreatment;

    private ExoPlayer exoPlayer;
    @BindView(R.id.videoView)
    StyledPlayerView videoView;

    @BindView(R.id.btn_back)
    StateButton btnBack;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_treatment_countdown);
        ButterKnife.bind(this);
        initHeadTitleBar("");
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
        RxBus.get().register(this);
        btnBack.setOnClickListener(view -> {
            if (jumpMsg != null) {
                doGoCloseTOActivity(PreRinseActivity.class,jumpMsg);
            } else {
                ActivityManager.getActivityManager().removeActivity(this);
            }
        });
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

    private LoadingDialogFragment loadingDialogFragment;

    private String jumpMsg;
    private LoadingPress loadingPress;
    private volatile boolean isLoading = false;

    private class LoadingPress implements Runnable {

        @Override
        public void run() {
            if (isLoading) {//没有做up事件
                loadingDialogFragment.dismiss();
                doGoCloseTOActivity(TreatmentFragmentActivity.class,"");
            } else {
                tvTitle.removeCallbacks(loadingPress);
            }
            Log.e("LoadingPress",""+isLoading);
        }
    }

    @Override
    public void initViewData() {
        speak("请确保管路已连接人体后开始进入治疗");
        loadingPress = new LoadingPress();
        exoPlayer = new ExoPlayer.Builder(this).build();
        loadingDialogFragment = new LoadingDialogFragment();
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        btnGoToTreatment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLoading = true;
                        loadingDialogFragment.show(getSupportFragmentManager(),"倒计时");
                        btnGoToTreatment.postDelayed(loadingPress, 5000);
                        break;
                    case MotionEvent.ACTION_UP:
                        loadingDialogFragment.dismiss();
                        isLoading = false;
                        break;
                }
                return true;
            }
        });
        play("android.resource://" + getPackageName() + "/" + R.raw.tc);
    }

    private void play(String url) {
//        String uri = "android.resource://" + getPackageName() + "/" + R.raw.text;
        videoView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
    }

    @Override
    public void notifyByThemeChanged() {

    }
}
