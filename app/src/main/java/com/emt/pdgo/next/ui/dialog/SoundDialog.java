package com.emt.pdgo.next.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoundDialog extends Dialog {

    @BindView(R.id.seekbar)
    SeekBar seekBar;

    @BindView(R.id.brightnessSeekbar)
    SeekBar brightnessSeekbar;

    @BindView(R.id.btnClose)
    TextView btnClose;

    private Context context;

    private AudioManager audioManager;
    private int maxVolume;
    private int currentVolume;

    public SoundDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
    }

    public SoundDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected SoundDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @BindView(R.id.ts_tv)
    TextView tsTv;
    private void init(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar.setProgress(currentVolume);
        seekBar.setMax(maxVolume);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.System.canWrite(context)){
//                brightnessSeekbar.setProgress(getScreenBrightness(context));
//                brightnessSeekbar.setMax(255);
//            }
//        }
        brightnessSeekbar.setProgress(MyApplication.brightness);
        brightnessSeekbar.setMax(255);
        if (PdproHelper.getInstance().getTtsSoundOpen()) {
            findViewById(R.id.ts_iv).setBackgroundResource(R.mipmap.silencers);
            tsTv.setText("关闭语音");
        } else {
            findViewById(R.id.ts_iv).setBackgroundResource(R.mipmap.rining);
            tsTv.setText("开启语音");
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i,0);
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        brightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setAppScreenBrightness(progress);
                MyApplication.brightness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setScreenBrightness(Context context,
                                                int brightnessValue) {
        // 首先需要设置为手动调节屏幕亮度模式
        setScreenManualMode(context);

        ContentResolver contentResolver = context.getContentResolver();
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, brightnessValue);
    }

    private void setScreenManualMode(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getScreenBrightness(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        int defVal = 125;
        return Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, defVal);
    }

    private int getScreenBrightness() {
        ContentResolver contentResolver = context.getContentResolver();
        int defVal = 125;
        return Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, defVal);
    }

    /**
     * 2.设置 APP界面屏幕亮度值方法
     * **/
    private void setAppScreenBrightness(int brightnessValue) {
//        Window window = getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.screenBrightness = brightnessValue / 255.0f;
//        window.setAttributes(lp);
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightnessValue == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightnessValue <= 0 ? 1 : brightnessValue) / 255f;
        }
        window.setAttributes(lp);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_dialog);
        ButterKnife.bind(this);
        initView();
        init(context);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.btnClose, R.id.ring_layout, R.id.ts_layout})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btnClose:
                if (click != null) {
                    click.onClick(SoundDialog.this);
                }
                break;
            case R.id.ts_layout:
                if (tsClick != null) {
                    tsClick.onClick(PdproHelper.getInstance());
                    Log.e("是否true", ""+PdproHelper.getInstance().getTtsSoundOpen());
                    if (PdproHelper.getInstance().getTtsSoundOpen()) {
                        findViewById(R.id.ts_iv).setBackgroundResource(R.mipmap.silencers);
                        tsTv.setText("关闭语音");
                    } else {
                        findViewById(R.id.ts_iv).setBackgroundResource(R.mipmap.rining);
                        tsTv.setText("开启语音");
                    }
                }
                break;
            case R.id.ring_layout:
                if (ringClick != null) {
                    ringClick.onClick(SoundDialog.this);
                }
                break;
        }

    }
    private OnSoundDialogClick click;
    private OnSoundTsClick tsClick;
    private OnSoundDialogClick ringClick;

    public SoundDialog closeClick(OnSoundDialogClick click) {
        this.click = click;
        return this;
    }
    public SoundDialog tsClick(OnSoundTsClick click) {
        tsClick = click;
        return this;
    }
    public SoundDialog ringClick(OnSoundDialogClick click) {
        ringClick = click;
        return this;
    }
    public interface OnSoundDialogClick {
        void onClick(SoundDialog soundDialog);
    }

    public interface OnSoundTsClick {
        void onClick(PdproHelper pdproHelper);
    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        this.setCancelable(false);//按返回键不能退出
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialog_animation_style_left_and_right);
        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = ScreenUtil.dip2px(mContext, 384);
//        lp.height = ScreenUtil.dip2px(mContext, 238);
        lp.gravity = Gravity.CENTER;//设置dialog 布局中的位置
        window.setAttributes(lp);

    }


}
