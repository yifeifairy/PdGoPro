package com.emt.pdgo.next.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.CacheUtils;
import com.pdp.rmmit.pdp.R;

import butterknife.ButterKnife;

public class RemotePrescribingActivity extends BaseActivity {

//    @BindView(R.id.etPrescription_item1)
    TextView item1;
//    @BindView(R.id.etPrescription_item2)
    TextView item2;
//    @BindView(R.id.etPrescription_item3)
    TextView item3;
//    @BindView(R.id.etPrescription_item4)
    TextView item4;
//    @BindView(R.id.etPrescription_item5)
    TextView item5;
//    @BindView(R.id.etPrescription_item6)
    TextView item6;
//    @BindView(R.id.etPrescription_item7)
    TextView item7;
//    @BindView(R.id.etPrescription_item8)
    TextView item8;
//    @BindView(R.id.etPrescription_item9)
    TextView item9;

//    @BindView(R.id.remove)
    TextView remove;
//    @BindView(R.id.btnNext)
    StateButton btnNext;

    private int s1, s2, s3, s4,s5,s6,s7,s8;
    private String s9;
//
    private String msg = EmtConstant.ACTIVITY_REMOTE_PRESCRIBING;
    private IpdBean eniity;
    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_remote_prescribing);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        s1 = intent.getIntExtra("icodextrinTotal",0);
        s2 = intent.getIntExtra("inFlowCycle",0);
        s3 = intent.getIntExtra("cycle",0);
        s4 = intent.getIntExtra("firstInFlow",0);
        s5 = intent.getIntExtra("retentionTime",0);
        s6 = intent.getIntExtra("lastRetention",0);
        s7 = intent.getIntExtra("agoRetention",0);
        s8 = intent.getIntExtra("predictUlt",0);
        s9 = intent.getStringExtra("treatTime");
        item1 = findViewById(R.id.etPrescription_item1);
        item2 = findViewById(R.id.etPrescription_item2);
        item3 = findViewById(R.id.etPrescription_item3);
        item4 = findViewById(R.id.etPrescription_item4);
        item5 = findViewById(R.id.etPrescription_item5);
        item6 = findViewById(R.id.etPrescription_item6);
        item7 = findViewById(R.id.etPrescription_item7);
        item8 = findViewById(R.id.etPrescription_item8);
        item9 = findViewById(R.id.etPrescription_item9);
        eniity = PdproHelper.getInstance().ipdBean();
        item1.setText(s1+"");
        item2.setText(s2+"");
        item3.setText(s3+"");
        item4.setText(s4+"");
        item5.setText(s5+"");
        item6.setText(s6+"");
        item7.setText(s7+"");
        item8.setText(s8+"");
        item9.setText(s9+"");
//        item1.setText(String.valueOf(eniity.peritonealDialysisFluidTotal));
//        item2.setText(String.valueOf(eniity.perCyclePerfusionVolume));
//        item3.setText(String.valueOf(eniity.cycle));
//        item4.setText(String.valueOf(eniity.firstPerfusionVolume));
//        item5.setText(String.valueOf(eniity.abdomenRetainingTime));
//        item6.setText(String.valueOf(eniity.abdomenRetainingVolumeFinally));
//        item7.setText(String.valueOf(eniity.abdomenRetainingVolumeLastTime));
//        item8.setText(String.valueOf(eniity.ultrafiltrationVolume));
//        item9.setText(String.valueOf(eniity.treatTime));
        remove = findViewById(R.id.remove);
        btnNext = findViewById(R.id.btnNext);
    }
//    @BindView(R.id.powerIv)
//    ImageView powerIv;
//    @BindView(R.id.currentPower)
//    TextView currentPower;
//    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
//    public void receiveCmdDeviceInfo(String bean) {
//        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
//        runOnUiThread(() -> {
//            if (mReceiveDeviceBean.isAcPowerIn == 1) {
//                powerIv.setImageResource(R.drawable.charging);
//            } else {
//                if (mReceiveDeviceBean.batteryLevel < 30) {
//                    powerIv.setImageResource(R.drawable.poor_power);
//                } else if (30 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 60 ) {
//                    powerIv.setImageResource(R.drawable.low_power);
//                } else if (60 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 80 ) {
//                    powerIv.setImageResource(R.drawable.mid_power);
//                } else {
//                    powerIv.setImageResource(R.drawable.high_power);
//                }
//            }
//            currentPower.setText(mReceiveDeviceBean.batteryLevel+"");
//        });
//    }
    @Override
    public void registerEvents() {
//        if (MyApplication.chargeFlag == 1) {
//            powerIv.setImageResource(R.drawable.charging);
//        } else {
//            if (MyApplication.batteryLevel < 30) {
//                powerIv.setImageResource(R.drawable.poor_power);
//            } else if (30 < MyApplication.batteryLevel &&MyApplication.batteryLevel < 60 ) {
//                powerIv.setImageResource(R.drawable.low_power);
//            } else if (60 < MyApplication.batteryLevel &&MyApplication.batteryLevel <= 80 ) {
//                powerIv.setImageResource(R.drawable.mid_power);
//            } else {
//                powerIv.setImageResource(R.drawable.high_power);
//            }
//        }
//        currentPower.setText(MyApplication.batteryLevel+"");
//        sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
    }

    @Override
    public void initViewData() {

        remove.setOnClickListener(v -> {
            APIServiceManage.getInstance().postApdCode("Z2022");
            onBackPressed();
        });
        btnNext.setOnClickListener(v -> {
            eniity.peritonealDialysisFluidTotal = s1;
            eniity.perCyclePerfusionVolume = s2;
            eniity.cycle = s3;
            eniity.firstPerfusionVolume = s4;
            eniity.abdomenRetainingTime = s5;
            eniity.abdomenRetainingVolumeFinally = s6;
            eniity.abdomenRetainingVolumeLastTime = s7;
            eniity.ultrafiltrationVolume = s8;
            eniity.treatTime = s9;
            eniity.isFinalSupply = false;
            MyApplication.apdMode = 1;
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, eniity);
            APIServiceManage.getInstance().postApdCode("Z2023");
//            RxEntity hisRx = new RxEntity();
//            hisRx.time = EmtTimeUil.getTime();
//            hisRx.perVol = eniity.peritonealDialysisFluidTotal;
//            hisRx.perCycleVol = eniity.perCyclePerfusionVolume;
//            hisRx.treatCycle = eniity.cycle;
//            hisRx.firstPerVol = eniity.firstPerfusionVolume;
//            hisRx.abdTime = eniity.abdomenRetainingTime;
//            hisRx.endAbdVol = eniity.abdomenRetainingVolumeFinally;
//            hisRx.lastTimeAbdVol = eniity.abdomenRetainingVolumeLastTime;
//            hisRx.ult = eniity.ultrafiltrationVolume;
//            hisRx.ulTreatTime = eniity.treatTime;
////                            Log.e("处方设置","处方设置--"+hisRx.ulTreatTime);
//            EmtDataBase
//                    .getInstance(RemotePrescribingActivity.this)
//                    .getRxDao()
//                    .insertRx(hisRx);
//            HisRx hisRx = new HisRx();
//            hisRx.time = EmtTimeUil.getCurrentTime();
//            hisRx.perVol = eniity.peritonealDialysisFluidTotal;
//            hisRx.perCycleVol = eniity.perCyclePerfusionVolume;
//            hisRx.treatCycle = eniity.cycle;
//            hisRx.firstPerVol = eniity.firstPerfusionVolume;
//            hisRx.abdTime = eniity.abdomenRetainingTime;
//            hisRx.endAbdVol = eniity.abdomenRetainingVolumeFinally;
//            hisRx.lastTimeAbdVol = eniity.abdomenRetainingVolumeLastTime;
//            hisRx.ult = eniity.ultrafiltrationVolume;
//            hisRx.ulTreatTime = eniity.treatTime;
//            hisRx.save();
            MyApplication.versionMode = 0;
            doGoTOActivity(PreHeatActivity.class);
        });
    }

    @Override
    public void notifyByThemeChanged() {

    }
}