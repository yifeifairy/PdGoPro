package com.emt.pdgo.next.ui.activity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.DrainParameterBean;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.RetainParamBean;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.data.bean.TreatmentParameterEniity;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.fragment.InputTreatmentInfoFragmentItem2;
import com.emt.pdgo.next.ui.fragment.InputTreatmentInfoFragmentItem3;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.activity
 * @ClassName: InputTreatmentInfoActivity
 * @Description: 输入治疗信息
 * @Author: chenjh
 * @CreateDate: 2019/12/11 10:45 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/11 10:45 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class InputTreatmentInfoActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.btn_item1)
    Button btnItem1;
    @BindView(R.id.btn_item2)
    Button btnItem2;

    @BindView(R.id.btn_back)
    Button btnBack;
//    @BindView(R.id.btn_item3)
//    Button btnItem3;


//    private InputTreatmentInfoFragmentItem1 mWeightParameterFragment;
    private InputTreatmentInfoFragmentItem2 mPrescriptionFragment;
    private InputTreatmentInfoFragmentItem3 mTreatmentParameterFragment;

    private InputTreatmentInfoPageAdapter mPageAdapter;

//    private BluetoothDialog bluetoothDialog = null;

    private int mCurrentPosition = 0;

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
    public TreatmentParameterEniity getmParameterEniity() {
        if (mParameterEniity == null) {
            mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
        }
        return mParameterEniity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_input_treatment_info);
        ButterKnife.bind(this);
        initHeadTitleBar("治疗信息", "确定");
        btnBack.setVisibility(View.GONE);
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

        mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();

        mPageAdapter = new InputTreatmentInfoPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.setCurrentItem(0, false);

        btnItem1.setSelected(true);

    }


    @OnClick({R.id.btn_item1, R.id.btn_item2, R.id.btn_submit, R.id.layout_right_menu})
//    @OnClick({R.id.btn_item1, R.id.btn_item2, R.id.btn_item3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
            case R.id.layout_right_menu://
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.RETAIN_PARAM, retainParamBean);
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRAIN_PARAMETER, drainParameterBean);
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.PERFUSION_PARAMETER, perfusionParameterBean);
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.SUPPLY_PARAMETER, supplyParameterBean);
                CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
                doGoTOActivity(PreHeatActivity.class);
//                doGoTOActivity(PipelineConnectionActivity.class);
                break;
            case R.id.btn_item1://
                mCurrentPosition = 0;
                mViewPager.setCurrentItem(0, false);
                selectNavSelection(0);
                break;
            case R.id.btn_item2://
                mCurrentPosition = 1;
                mViewPager.setCurrentItem(1, false);
                selectNavSelection(1);
                break;
        }
    }

    public void DisTouchEffectAndSaveState() {
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

    public void RecoveryTouchEffect() {

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

    /**
     * 连接蓝牙设备 获取体检数据
     */
//    public void showBluetoothDialog() {
//
//        if (bluetoothDialog != null) {
//            bluetoothDialog.dismiss();
//            bluetoothDialog = null;
//        }
//
//        bluetoothDialog = BluetoothDialog.instance();
////        bluetoothDialog = BluetoothDialog.instance(BluetoothDialog.DEVICE_BLOOD);
//        if (bluetoothDialog == null) {
//            return;
//        }
//        bluetoothDialog.show(getSupportFragmentManager(), "bluetoothDialog");
//
//    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    private void selectNavSelection(int index) {

        switch (index) {
            case 0:
                btnItem1.setSelected(true);
                btnItem2.setSelected(false);
                break;
            case 1:
                btnItem1.setSelected(false);
                btnItem2.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        switch (position) {
            case 0:
            case 1:
            case 2:
                selectNavSelection(position);
                break;

        }

    }


    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void notifyByThemeChanged() {

    }


    private class InputTreatmentInfoPageAdapter extends FragmentPagerAdapter {

        public InputTreatmentInfoPageAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    if (mPrescriptionFragment == null) {
                        mPrescriptionFragment = new InputTreatmentInfoFragmentItem2();
                    }
                    fragment = mPrescriptionFragment;
                    break;
                case 1:
                    if (mTreatmentParameterFragment == null) {
                        mTreatmentParameterFragment = new InputTreatmentInfoFragmentItem3();
                    }
                    fragment = mTreatmentParameterFragment;
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
