package com.emt.pdgo.next.ui.activity.param;


import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TtsTestActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    @BindView(R.id.content_text)
    TextView speechText;

    private TextToSpeech mTTS;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_tts_test);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        initHeadTitleBar("TTS文字转语音功能测试");

        this.mContext = this;
        //监听器就直接传入本类
        this.mTTS = new TextToSpeech(mContext, this);

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


    }

    @Override
    public void notifyByThemeChanged() {

    }



    @OnClick({R.id.btn_speak})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_speak:
                speak("测试文字转语音");
                break;
        }
    }

    /**
     * 初始化
     *
     * @param status
     */
    @Override
    public void onInit(int status) {
        //判断是否转化成功
        if (status == TextToSpeech.SUCCESS) {
            //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
            mTTS.setPitch(0.5f);
//        mTTS.setPitch(1.0f);
            //设置语速
            mTTS.setSpeechRate(0.1f);
            //设置语言为中文
            int languageCode = mTTS.setLanguage(Locale.CHINESE);
            //判断是否支持这种语言，Android原生不支持中文，使用科大讯飞的tts引擎就可以了
            if (languageCode == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("TAG", "onInit: 不支持这种语言");
            } else {
                //不支持就改成英文
                mTTS.setLanguage(Locale.US);
            }
        }
    }

    /**
     * 播报方法，需要传入播报的内容
     *
     * @param text 播报的内容
     */
    public void speak(String text) {
//        //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
//        mTTS.setPitch(0.5f);
////        mTTS.setPitch(1.0f);
//        //设置语速
//        mTTS.setSpeechRate(0.1f);
//        mTTS.setSpeechRate(1.0f);
        mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    /**
     * 销毁播报方法，直接调用
     */
    public void stopTTS() {
        mTTS.stop();
        mTTS.shutdown();
        mTTS = null;
    }

}