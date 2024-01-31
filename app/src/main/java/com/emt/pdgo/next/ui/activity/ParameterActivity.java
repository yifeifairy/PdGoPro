package com.emt.pdgo.next.ui.activity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParameterActivity extends BaseActivity {

    @BindView(R.id.tv_zero_cycle_select)
    TextView tvZeroCycleSelect;
    @BindView(R.id.tv_other_cycle_select)
    TextView tvOtherCycleSelect;
    @BindView(R.id.tv_perfusion_warning_select)
    TextView tvPerfusionWarningSelect;

    @BindView(R.id.et_perfusion_warning_value)
    EditText etPerfusionWarningValue;
    @BindView(R.id.et_time_interval)
    EditText etTimeInterval;

    @BindView(R.id.layout_time_interval)
    LinearLayout layoutTimeInterval;


//    @BindView(R.id.layout_drain_threshold_zero_cycle)
//    RelativeLayout layoutDrainThresholdZeroCycle;
//    @BindView(R.id.layout_drain_threshold_other_cycle)
//    RelativeLayout layoutDrainThresholdOtherCycle;
//    @BindView(R.id.layout_perfusion_warning_value)
//    RelativeLayout layoutPerfusionWarningValue;

    @BindView(R.id.btn_back)
    StateButton btnBack;
    @BindView(R.id.btn_waiting)
    LabeledSwitch btnWaiting;//最末引流排空等待
    @BindView(R.id.btn_negpre_drain)
    LabeledSwitch btnNegpreDrain;//负压引流
    @BindView(R.id.btn_deduct)
    LabeledSwitch btnDeduct;//留腹扣除
    @BindView(R.id.btn_wake_up)
    LabeledSwitch btnWakeUp;//治疗结束报警唤醒
    @BindView(R.id.btn_ultrafiltration)
    LabeledSwitch btnUltrafiltration;//只计算循环透析治疗期间的超滤
    @BindView(R.id.dormancyLayout)
    RelativeLayout dormancyLayout;//屏幕休眠
    @BindView(R.id.btn_dormancy)
    LabeledSwitch btn_dormancy;
    @BindView(R.id.btn_touch_tone)
    LabeledSwitch btnTouchTone;//屏幕触控声

    @BindView(R.id.btnConfirm)
    StateButton btnConfirm;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_parameter);
        ButterKnife.bind(this);
        initHeadTitleBar("治疗参数");
//        btnBack.setVisibility(View.GONE);

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
    private String jumpMsg = "";
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
        APIServiceManage.getInstance().postApdCode("Z2032");
//        saveFaultCodeLocal("Z2032");
        btnWaiting.setOnToggledListener((toggleableView, isOn) -> {
            isDrainManualEmptying = isOn;
            showTimeInterval();
        });
//        btnDormancy.setEnabled(false);
        btnNegpreDrain.setOnToggledListener((toggleableView, isOn) -> {
            isNegpreDrain = isOn;
        });
        btnConfirm.setOnClickListener(v -> save());
        btnWakeUp.setOnToggledListener((toggleableView, isOn) -> {
            isAlarmWakeUp = isOn;
            showTimeInterval();
        });
        btnDeduct.setOnToggledListener((toggleableView, isOn) -> {
            isAbdomenRetainingDeduct = isOn;
        });
        btnUltrafiltration.setOnToggledListener((toggleableView, isOn) -> {
            isZeroCycleUltrafiltration = isOn;
        });
//        btnDormancy.setOnToggledListener((toggleableView, isOn) -> {
//            getmParameterEniity().isDormancy = isOn;
//        });
        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
//        btnBack.setOnClickListener(v -> {
//            if (jumpMsg.equals(EmtConstant.JUMP_WITH_PARAM_SPE)) {
//                doGoCloseTOActivity(PreHeatActivity.class);
//            } else {
//                ActivityManager.getActivityManager().removeActivity(this);
//            }
//        });
        dormancyLayout.setOnClickListener(v -> {
            doGoTOActivity(MyDreamActivity.class);
        });
        btnTouchTone.setOnToggledListener((toggleableView, isOn) -> {
            getmParameterEniity().isTouchTone = isOn;
            if (isOn) {
                recoveryTouchEffect();
            } else {
                disTouchEffectAndSaveState();
            }
        });
    }

    private boolean isDrainManualEmptying;
    private boolean isNegpreDrain;
    private boolean isAbdomenRetainingDeduct;
    private boolean isAlarmWakeUp;
    private boolean isZeroCycleUltrafiltration;
    private int perfusionWarningValue;
    private int alarmTimeInterval;
    @Override
    public void initViewData() {
        zeroCycle = getDrainParameterBean().drainZeroCyclePercentage;
        otherCycle = getDrainParameterBean().drainOtherCyclePercentage;
        isDrainManualEmptying = getDrainParameterBean().isDrainManualEmptying;
        isNegpreDrain = getDrainParameterBean().isNegpreDrain;
        isAbdomenRetainingDeduct = getRetainParamBean().isAbdomenRetainingDeduct;
        isAlarmWakeUp = getRetainParamBean().isAlarmWakeUp;
        isZeroCycleUltrafiltration = getRetainParamBean().isZeroCycleUltrafiltration;
        perfusionWarningValue = getPerfusionParameterBean().perfMaxWarningValue;
        alarmTimeInterval = getDrainParameterBean().alarmTimeInterval;
        setCanNotEditNoClick2(etPerfusionWarningValue);
        setCanNotEditNoClick2(etTimeInterval);

        etTimeInterval.setText(String.valueOf(getDrainParameterBean().alarmTimeInterval));
        btnWaiting.setOn(getDrainParameterBean().isDrainManualEmptying);
        btnNegpreDrain.setOn(getDrainParameterBean().isNegpreDrain);
        btnDeduct.setOn(getRetainParamBean().isAbdomenRetainingDeduct);
        btnWakeUp.setOn(getRetainParamBean().isAlarmWakeUp);
        btnUltrafiltration.setOn(getRetainParamBean().isZeroCycleUltrafiltration);
//        btnDormancy.setOn(getmParameterEniity().isDormancy);
        btnTouchTone.setOn(getmParameterEniity().isTouchTone);

        showTimeInterval();

        etPerfusionWarningValue.setText(String.valueOf(getPerfusionParameterBean().perfMaxWarningValue));

//        switchDrainThresholdZeroCycle(tvZeroCycleSelect, getmParameterEniity().drainThresholdZeroCycle);
//        switchDrainThresholdZeroCycle(tvOtherCycleSelect, getmParameterEniity().drainThresholdOtherCycle);

        updateTextViewWidth(tvZeroCycleSelect, getDrainParameterBean().drainZeroCyclePercentage);
        updateTextViewWidth(tvOtherCycleSelect, getDrainParameterBean().drainOtherCyclePercentage);
        if (1000 == getPerfusionParameterBean().perfMaxWarningValue) {
            currPerfusionWarningSelect = 1;
        } else if (2000 == getPerfusionParameterBean().perfMaxWarningValue) {
            currPerfusionWarningSelect = 2;
        } else if (3000 ==getPerfusionParameterBean().perfMaxWarningValue) {
            currPerfusionWarningSelect = 3;
        } else {
            currPerfusionWarningSelect = 4;
            etPerfusionWarningValue.setText(String.valueOf(getPerfusionParameterBean().perfMaxWarningValue));
            etPerfusionWarningValue.setVisibility(View.VISIBLE);
        }

        updateTextViewWidth2(tvPerfusionWarningSelect, currPerfusionWarningSelect);

    }

    private void save() {
//        showLoadingDialog();

//        mDrainParameterBean.timeInterval = Integer.parseInt(etTimeInterval.getText().toString());
//        mDrainParameterBean.thresholdValue = Integer.parseInt(etThresholdValue.getText().toString());
        getDrainParameterBean().drainZeroCyclePercentage = zeroCycle;
        getDrainParameterBean().drainOtherCyclePercentage = otherCycle;
//        mDrainParameterBean.timeoutAlarm = Integer.parseInt(etTimeoutAlarm.getText().toString());
//        mDrainParameterBean.rinseVolume = Integer.parseInt(etRinseVolume.getText().toString());
//        mDrainParameterBean.rinseNumber = Integer.parseInt(etRinseNumber.getText().toString());
//        mDrainParameterBean.isDrainManualEmptying = labeledSwitchEmptying.isOn();
//        mDrainParameterBean.warnTimeInterval = Integer.parseInt(etWarnTimeInterval.getText().toString());
        getDrainParameterBean().isDrainManualEmptying = isDrainManualEmptying;
        getDrainParameterBean().isNegpreDrain = isNegpreDrain;
        getRetainParamBean().isAbdomenRetainingDeduct = isAbdomenRetainingDeduct;
        getRetainParamBean().isAlarmWakeUp = isAlarmWakeUp;
        getRetainParamBean().isZeroCycleUltrafiltration = isZeroCycleUltrafiltration;
        getPerfusionParameterBean().perfMaxWarningValue = perfusionWarningValue;
        getDrainParameterBean().alarmTimeInterval = alarmTimeInterval;
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, getmParameterEniity());
//        hideLoadingDialog();
//        showCommonDialog("引流参数保存成功");

        toastMessage("参数保存成功");
    }


    @Override
    public void notifyByThemeChanged() {

    }

    public void disTouchEffectAndSaveState() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                //有了权限，具体的动作
                SharedPreferences setting = this.getSharedPreferences("SystemSetting", MODE_PRIVATE);
                int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", -1);

                if (mSoundEffect == -1) {
                    SharedPreferences.Editor editor = setting.edit();
                    mSoundEffect = Settings.System.getInt(this.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);

                    editor.putInt("SOUND_EFFECTS_ENABLED", mSoundEffect);
                    editor.commit();
                }

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

    private void recoveryTouchEffect() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            } else {
                //有了权限，具体的动作
                SharedPreferences setting = this.getSharedPreferences("SystemSetting", MODE_PRIVATE);
                int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", 0);
                AudioManager mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
                Settings.System.putInt(this.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, mSoundEffect);
                mAudioManager.loadSoundEffects();

            }

        }else{
            SharedPreferences setting = this.getSharedPreferences("SystemSetting", MODE_PRIVATE);
            int mSoundEffect = setting.getInt("SOUND_EFFECTS_ENABLED", 0);
            AudioManager mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
            Settings.System.putInt(this.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, mSoundEffect);
            mAudioManager.loadSoundEffects();
        }

    }

    private void showTimeInterval() {

        if (btnWaiting.isOn() && btnWakeUp.isOn()) {
            layoutTimeInterval.setVisibility(View.VISIBLE);
        } else {
            layoutTimeInterval.setVisibility(View.INVISIBLE);
        }

    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_WAITING_INTERVA)) {//等待提醒间隔时间
                    etTimeInterval.setText(result);
                    alarmTimeInterval = Integer.parseInt(result);
                } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_PERFUSION_WARNING_VALUE)) {//灌注警戒值
                    etPerfusionWarningValue.setText(result);
                    perfusionWarningValue = Integer.parseInt(result);
                }
            }
        });
    }

    @OnClick({R.id.layout_drain_threshold_zero_cycle,
            R.id.layout_drain_threshold_other_cycle, R.id.layout_perfusion_warning_value, R.id.et_time_interval, R.id.et_perfusion_warning_value})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_drain_threshold_zero_cycle://
                switchDrainThresholdZeroCycle(tvZeroCycleSelect, getDrainParameterBean().drainZeroCyclePercentage);
                break;
            case R.id.layout_drain_threshold_other_cycle://
                Log.e("layout","layout_drain_threshold_other_cycle");
                switchDrainThresholdOtherCycle(tvOtherCycleSelect, getDrainParameterBean().drainOtherCyclePercentage);
                break;
            case R.id.layout_perfusion_warning_value://
                switchPerfusionWarning(tvPerfusionWarningSelect, currPerfusionWarningSelect);
                break;
            case R.id.et_time_interval://
                alertNumberBoardDialog(etTimeInterval.getText().toString(), PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_WAITING_INTERVA);
                break;
            case R.id.et_perfusion_warning_value://
                alertNumberBoardDialog(etPerfusionWarningValue.getText().toString(), PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_PERFUSION_WARNING_VALUE);
                break;
        }
    }

    private void switchDrainThresholdZeroCycle(TextView mTextView, int currCycle) {

        switch (currCycle) {
            case 50://50%
                getDrainParameterBean().drainZeroCyclePercentage = 75;
                updateTextViewWidth(mTextView, 75);
                zeroCycle = 75;
                break;
            case 75://75%
                getDrainParameterBean().drainZeroCyclePercentage = 100;
                zeroCycle = 100;
                updateTextViewWidth(mTextView, 100);
                break;
            case 100://100%
                getDrainParameterBean().drainZeroCyclePercentage = 120;
                zeroCycle = 120;
                updateTextViewWidth(mTextView, 120);
                break;
            case 120://120%
                getDrainParameterBean().drainZeroCyclePercentage = 50;
                zeroCycle = 50;
                updateTextViewWidth(mTextView, 50);
                break;

        }


    }

    private int otherCycle;
    private int zeroCycle;
    private void switchDrainThresholdOtherCycle(TextView mTextView, int currCycle) {

        switch (currCycle) {
            case 50://50%
                getDrainParameterBean().drainOtherCyclePercentage = 75;
                updateTextViewWidth(mTextView, 75);
                otherCycle = 75;
                break;
            case 75://75%
                getDrainParameterBean().drainOtherCyclePercentage = 100;
                otherCycle = 100;
                updateTextViewWidth(mTextView, 100);
                break;
            case 100://100%
                getDrainParameterBean().drainOtherCyclePercentage = 120;
                otherCycle = 120;
                updateTextViewWidth(mTextView, 120);
                break;
            case 120://120%
                getDrainParameterBean().drainOtherCyclePercentage = 50;
                otherCycle = 50;
                updateTextViewWidth(mTextView, 50);
                break;
        }

    }

    private void switchPerfusionWarning(TextView mTextView, int currIndex) {

        switch (currIndex) {
            case 1://1升
                getPerfusionParameterBean().perfMaxWarningValue = 2000;
                currPerfusionWarningSelect = 2;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                break;
            case 2://2升
                getPerfusionParameterBean().perfMaxWarningValue = 3000;
                currPerfusionWarningSelect = 3;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                break;
            case 3://3升
                etPerfusionWarningValue.setText("");
                getPerfusionParameterBean().perfMaxWarningValue = 0;
                currPerfusionWarningSelect = 4;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                etPerfusionWarningValue.setVisibility(View.VISIBLE);
                break;
            case 4://其他
                getPerfusionParameterBean().perfMaxWarningValue = 1000;
                currPerfusionWarningSelect = 1;
                updateTextViewWidth2(mTextView, currPerfusionWarningSelect);
                etPerfusionWarningValue.setVisibility(View.INVISIBLE);
                break;

        }


    }

    private TreatmentParameterEniity mParameterEniity;
    private DrainParameterBean drainParameterBean;
    private PerfusionParameterBean perfusionParameterBean;
    private SupplyParameterBean supplyParameterBean;
    private RetainParamBean retainParamBean;
    public RetainParamBean getRetainParamBean() {
        if (retainParamBean == null) {
            retainParamBean = PdproHelper.getInstance().getRetainParamBean();
        }
        return retainParamBean;
    }
    public DrainParameterBean getDrainParameterBean() {
        if (drainParameterBean == null) {
            drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        }
        return drainParameterBean;
    }

    public PerfusionParameterBean getPerfusionParameterBean() {
        if (perfusionParameterBean == null) {
            perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();
        }
        return perfusionParameterBean;
    }

    public SupplyParameterBean getSupplyParameterBean() {
        if (supplyParameterBean == null) {
            supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();
        }
        return supplyParameterBean;
    }
    private TreatmentParameterEniity getmParameterEniity() {
        if (mParameterEniity == null) {
            mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
        }
        return mParameterEniity;
    }

    private int currPerfusionWarningSelect = 1;

    private void updateTextViewWidth(TextView mTextView, int currValue) {
        ViewGroup.LayoutParams mLayoutParams = mTextView.getLayoutParams();
        int mWidth = 0;
        switch (currValue) {
            case 50://50%
//                mWidth = ScreenUtil.dip2px(getActivity(), 40);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_cycle_select_width_50);
                break;
            case 75://75%
//                mWidth = ScreenUtil.dip2px(getActivity(), 100);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_cycle_select_width_75);
                break;
            case 100://100%
//                mWidth = ScreenUtil.dip2px(getActivity(), 150);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_cycle_select_width_100);
                break;
            case 120://120%
//                mWidth = ScreenUtil.dip2px(getActivity(), 220);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_cycle_select_width_120);
                break;

        }
        mLayoutParams.width = mWidth;
        mTextView.setLayoutParams(mLayoutParams);
    }


    private void updateTextViewWidth2(TextView mTextView, int currIndex) {
        ViewGroup.LayoutParams mLayoutParams = mTextView.getLayoutParams();
        int mWidth = 0;

        switch (currIndex) {
            case 1://1
//                mWidth = ScreenUtil.dip2px(getActivity(), 40);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_perfusion_warning_select_width_1000);
                break;
            case 2://2
//                mWidth = ScreenUtil.dip2px(getActivity(), 120);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_perfusion_warning_select_width_2000);
                break;
            case 3://3
//                mWidth = ScreenUtil.dip2px(getActivity(), 190);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_perfusion_warning_select_width_3000);
                break;
            case 4://
//                mWidth = ScreenUtil.dip2px(getActivity(), 280);
                mWidth = getResources().getDimensionPixelOffset(R.dimen.input_treatment_tv_perfusion_warning_select_width_other);
                break;

        }
        mLayoutParams.width = mWidth;
        mTextView.setLayoutParams(mLayoutParams);
    }

}