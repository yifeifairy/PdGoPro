package com.emt.pdgo.next.util;


import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;


/**
 *
 * tts语音合成操作类
 *
 * @author chenjh
 * @date 2019-07-29 15:52
 */
public class TtsHelper implements TextToSpeech.OnInitListener {


    private Context mContext;

    private TextToSpeech mTTS;

    private static TtsHelper instance;

    private TtsHelper(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static TtsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TtsHelper(context);
        }
        return instance;
    }
    public void initTts(){

        //监听器就直接传入本类
        this.mTTS = new TextToSpeech(mContext, this);
    }


    //释放资源成功
    public void onDestroy() {
        stopTTS();
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
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * 销毁播报方法，直接调用
     */
    public void stopTTS() {
        mTTS.stop();
        mTTS.shutdown();
        mTTS = null;
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

}
