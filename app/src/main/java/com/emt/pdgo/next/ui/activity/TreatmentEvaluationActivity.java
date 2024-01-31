package com.emt.pdgo.next.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.entity.TreatmentHistoryBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.adapter.TreatmentHistoricalDataAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TreatmentEvaluationActivity extends BaseActivity {

    Unbinder unbinder;
    @BindView(R.id.tv_total_perfusion_volume)
    TextView tvTotalPerfusionVolume;
    @BindView(R.id.tv_total_drain_volume)
    TextView tvTotalDrainVolume;
    @BindView(R.id.tv_curr_ultrafiltration_volume)
    TextView tvCurrUltrafiltrationVolume;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.assess)
    StateButton assess;

    @BindView(R.id.powerOff)
    StateButton powerOff;

    private TreatmentHistoricalDataAdapter mAdapter;

    private int totalDrainVolume;    //累计引流量
    /***** 总灌注量  灌注量  即治疗量  *******/
    private int totalPerfusionVolume;//累计灌注量
    /**** 总超滤量 = 引流量-治疗量 = 总引流量-总灌注量 ***/
    private int mUltrafiltrationVolume;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_treatment_evaluation);
        ButterKnife.bind(this);
        speak("请关闭人体端导管");
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
        assess.setOnClickListener(view -> doGoTOActivity(TreatmentFeedbackActivity.class));
        powerOff.setOnClickListener(view -> {
            openValve();
        });
        MyApplication.treatmentRunning = false;
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent("请关闭人体端开关,关上碘伏帽,取出卡匣后关闭电源")
                .setBtnFirst("确定")
                .setFirstClickListener(mCommonDialog -> {
                    String mCommand = CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BATTERY_OFF);
                    sendToMainBoard(mCommand);
                    mCommonDialog.dismiss();
                });
        if (!TreatmentEvaluationActivity.this.isFinishing()) {
            dialog.show();
        }
    }

    private void openValve() {
        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group1"));
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group2"),1000);
//                speak("请取出卡匣,关闭所有管夹");
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group3"),2000);
    }

    public void initViewData(){
//
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TreatmentHistoricalDataAdapter(R.layout.item_treatment_historical_data2, TreatmentFragmentActivity.mTreatmentDataList);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
        //添加Android自带的分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
//        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
//            mActivity.initTreatmentHistory();
        setHistoricalData(TreatmentFragmentActivity.mTreatmentDataList);
    }

    private void setHistoricalData(List<TreatmentHistoryBean> dataList) {

        try {

            totalDrainVolume = 0;
            totalPerfusionVolume = 0;
            for (int i = 0; i < dataList.size(); i++) {
                TreatmentHistoryBean mBean = dataList.get(i);
                totalDrainVolume += mBean.drainVolume;
                totalPerfusionVolume += mBean.perfusionVolume;
            }
            tvTotalPerfusionVolume.setText(String.valueOf(totalPerfusionVolume));
            tvTotalDrainVolume.setText(String.valueOf(totalDrainVolume));
            tvCurrUltrafiltrationVolume.setText(String.valueOf(totalDrainVolume - totalPerfusionVolume));
            //本次治疗总超滤量= 总引流量 - 总灌注量 - 上次最末留腹量 + 本次最末留腹量（）
            if (mAdapter != null) {
//            mAdapter.setNewData(dataList);
                mAdapter.notifyDataSetChanged();
            }
            MyApplication.ApdTotalDrainVol = totalDrainVolume;
            MyApplication.ApdTotalPerVol = totalPerfusionVolume;
            MyApplication.ApdTotalUltVol = totalDrainVolume - totalPerfusionVolume;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void notifyByThemeChanged() {

    }

}
