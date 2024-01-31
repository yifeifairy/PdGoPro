package com.emt.pdgo.next.ui.activity.param;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VolumeActivity extends BaseActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.seekbar)
    SeekBar seekBar;

    @BindView(R.id.brightnessSeekbar)
    SeekBar brightnessSeekbar;

    @BindView(R.id.currBrightness)
    TextView currBrightness;

    @BindView(R.id.tv_curr_volume)
    TextView tvVolume;

    @BindView(R.id.ttsSwitch)
    LabeledSwitch ttsSwitch;

    @BindView(R.id.labeledSwitch)
    LabeledSwitch labeledSwitch;

    private AudioManager audioManager;
    private int maxVolume;
    private int currentVolume;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_volume);
        ButterKnife.bind(this);
        initHeadTitleBar("声音设置");
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
        brightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ModifySettingsScreenBrightness(VolumeActivity.this, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ttsSwitch.setOnToggledListener((toggleableView, isOn) -> {
            PdproHelper.getInstance().updateTtsSoundOpen(isOn);
            Log.e("tts","状态："+PdproHelper.getInstance().getTtsSoundOpen());
        });
        labeledSwitch.setOnToggledListener((toggleableView, isOn) -> {
            DisTouchEffectAndSaveState(isOn);
        });
    }

    @Override
    public void initViewData() {
        initVolume();
//        init();
//        setAppScreenBrightness(PdproHelper.getInstance().brightness());
        ttsSwitch.setOn(PdproHelper.getInstance().getTtsSoundOpen());
        try {
            int mode = Settings.System.getInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED);
            Log.e("labeledSwitch", mode+"");
            labeledSwitch.setOn(mode == 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyByThemeChanged() {

    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(VolumeActivity.this)){
                brightnessSeekbar.setProgress(getScreenBrightness(this));
                brightnessSeekbar.setMax(255);
            } else {
                allowModifySettings();
            }
        }
    }

    private void initVolume() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setView();
        seekBar.setMax(maxVolume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i,0);
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                tvVolume.setText("当前音量:"+currentVolume+"/"+maxVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void DisTouchEffectAndSaveState(boolean isOn) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                Settings.System.putInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, isOn?1:0);
            }

        }else{
            SharedPreferences setting = this.getSharedPreferences("SystemSetting", MODE_PRIVATE);
            int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", -1);

            if (mSoundEffect == -1) {
                SharedPreferences.Editor editor = setting.edit();
                mSoundEffect = Settings.System.getInt(this.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);

                editor.putInt("SOUND_EFFECTS_ENABLED", mSoundEffect);
                editor.commit();
            }
        }

    }

    private void setView() {
        seekBar.setProgress(currentVolume);
        tvVolume.setText("当前音量:"+currentVolume+"/"+maxVolume);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (--currentVolume >= 0) {
                    setView();
                } else {
                    currentVolume = 0;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (++currentVolume <= maxVolume) {
                    setView();
                } else {
                    currentVolume = maxVolume;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_MUTE:
                setView();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 1.获取系统默认屏幕亮度值 屏幕亮度值范围（0-255）
     * **/
    private int getScreenBrightness(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        int defVal = 125;
        return Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, defVal);
    }

    /**
     * 2.设置 APP界面屏幕亮度值方法
     * **/
    private void setAppScreenBrightness(int brightnessValue) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightnessValue / 255.0f;
        window.setAttributes(lp);
    }

    /**
     * 3.关闭光感，设置手动调节背光模式
     *
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC 自动调节屏幕亮度模式值为1
     *
     * SCREEN_BRIGHTNESS_MODE_MANUAL 手动调节屏幕亮度模式值为0
     * **/
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

    /**
     * 4.非系统签名应用，引导用户手动授权修改Settings 权限
     * **/
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1000;

    private void allowModifySettings() {
        // Settings.System.canWrite(MainActivity.this)
        // 检测是否拥有写入系统 Settings 的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(VolumeActivity.this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this,
                        android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("请开启修改屏幕亮度权限");
                builder.setMessage("请点击允许开启");
                // 拒绝, 无法修改
                builder.setNegativeButton("拒绝",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(VolumeActivity.this,
                                                "您已拒绝修系统Setting的屏幕亮度权限", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                builder.setPositiveButton("去开启",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 打开允许修改Setting 权限的界面
                                Intent intent = new Intent(
                                        Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri
                                        .parse("package:"
                                                + getPackageName()));
                                startActivityForResult(intent,
                                        REQUEST_CODE_WRITE_SETTINGS);
                            }
                        });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Settings.System.canWrite方法检测授权结果
                if (Settings.System.canWrite(getApplicationContext())) {
                    // 5.调用修改Settings屏幕亮度的方法 屏幕亮度值 200
                    ModifySettingsScreenBrightness(VolumeActivity.this, 20);
                    Toast.makeText(this, "系统屏幕亮度值" + getScreenBrightness(this),
                            Toast.LENGTH_SHORT).show();
                    init();
                } else {
                    Toast.makeText(VolumeActivity.this, "您已拒绝修系统Setting的屏幕亮度权限",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 5.修改Setting 中屏幕亮度值
     *
     * 修改Setting的值需要动态申请权限 <uses-permission
     * android:name="android.permission.WRITE_SETTINGS"/>
     * **/
    private void ModifySettingsScreenBrightness(Context context,
                                                int birghtessValue) {
        // 首先需要设置为手动调节屏幕亮度模式
        setScreenManualMode(context);

        ContentResolver contentResolver = context.getContentResolver();
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, birghtessValue);
    }

}