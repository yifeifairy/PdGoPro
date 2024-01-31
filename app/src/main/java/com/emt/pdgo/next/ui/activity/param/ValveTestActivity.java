package com.emt.pdgo.next.ui.activity.param;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.entity.CommandItem;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.adapter.CommandAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ValveTestActivity extends BaseActivity {

    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.rv_value)
    RecyclerView rvSet;

    private List<CommandItem> mList;
    private CommandAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_valve_test);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        initHeadTitleBar("阀功能测试");
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
        mList = new ArrayList<>();


        mList.add(new CommandItem("开启灌注", CommandSendConfig.PERFUSE_VALVE_OPEN));
        mList.add(new CommandItem("关闭灌注", CommandSendConfig.PERFUSE_VALVE_CLOSE));

        mList.add(new CommandItem("开启安全阀", CommandSendConfig.SAFE_VALVE_OPEN));
        mList.add(new CommandItem("关闭安全阀", CommandSendConfig.SAFE_VALVE_CLOSE));

        mList.add(new CommandItem("开启补液", CommandSendConfig.SUPPLY_VALVE_OPEN));
        mList.add(new CommandItem("停止补液", CommandSendConfig.SUPPLY_VALVE_CLOSE));

        mList.add(new CommandItem("开启末袋补液", CommandSendConfig.SUPPLY2_VALVE_OPEN));
        mList.add(new CommandItem("停止末袋补液", CommandSendConfig.SUPPLY2_VALVE_CLOSE));

//        mList.add(new CommandItem("停止灌注和安全阀", CommandSendConfig.GROUP1_VALVE_OPEN));
//        mList.add(new CommandItem("开启灌注安全阀", CommandSendConfig.GROUP1_VALVE_CLOSE));
//
//        mList.add(new CommandItem("停止补液和末袋阀", CommandSendConfig.GROUP2_VALVE_OPEN));
//        mList.add(new CommandItem("开启补液和末袋阀", CommandSendConfig.GROUP2_VALVE_CLOSE));

        mList.add(new CommandItem("开启引流", CommandSendConfig.DRAIN_VALVE_OPEN));
        mList.add(new CommandItem("停止引流", CommandSendConfig.DRAIN_VALVE_CLOSE));

        mList.add(new CommandItem("开启负压引流", CommandSendConfig.VACCUM_VALVE_OPEN));
        mList.add(new CommandItem("停止负压引流", CommandSendConfig.VACCUM_VALVE_CLOSE));

//        mList.add(new CommandItem("组1开启", CommandSendConfig.GROUP1_VALVE_OPEN));
//        mList.add(new CommandItem("组1关闭", CommandSendConfig.GROUP1_VALVE_CLOSE));
//
//        mList.add(new CommandItem("组2开启", CommandSendConfig.GROUP2_VALVE_OPEN));
//        mList.add(new CommandItem("组2关闭", CommandSendConfig.GROUP2_VALVE_CLOSE));
//
//        mList.add(new CommandItem("组3开启", "group3_open"));
//        mList.add(new CommandItem("组3关闭", "group3_close"));

        mAdapter = new CommandAdapter(this, R.layout.item_setting, mList);
        rvSet.setLayoutManager(new GridLayoutManager(this, 4));
        rvSet.setAdapter(mAdapter);


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


                String mCommand = mList.get(position).mCommand;

                if (CommandSendConfig.PERFUSE_VALVE_OPEN.equals(mCommand)) {
                    handleValve("perfuse", true);
                } else if (CommandSendConfig.PERFUSE_VALVE_CLOSE.equals(mCommand)) {
                    handleValve("perfuse", false);
                } else if (CommandSendConfig.SAFE_VALVE_OPEN.equals(mCommand)) {
                    handleValve("safe", true);
                } else if (CommandSendConfig.SAFE_VALVE_CLOSE.equals(mCommand)) {
                    handleValve("safe", false);
                } else if (CommandSendConfig.SUPPLY_VALVE_OPEN.equals(mCommand)) {
                    handleValve("supply", true);
                } else if (CommandSendConfig.SUPPLY_VALVE_CLOSE.equals(mCommand)) {
                    handleValve("supply", false);
                } else if (CommandSendConfig.SUPPLY2_VALVE_OPEN.equals(mCommand)) {
                    handleValve("supply2", true);
                } else if (CommandSendConfig.SUPPLY2_VALVE_CLOSE.equals(mCommand)) {
                    handleValve("supply2", false);
                } else if (CommandSendConfig.DRAIN_VALVE_OPEN.equals(mCommand)) {
                    handleValve("drain", true);
                } else if (CommandSendConfig.DRAIN_VALVE_CLOSE.equals(mCommand)) {
                    handleValve("drain", false);
                } else if (CommandSendConfig.VACCUM_VALVE_OPEN.equals(mCommand)) {
                    handleValve("vaccum", true);
                } else if (CommandSendConfig.VACCUM_VALVE_CLOSE.equals(mCommand)) {
                    handleValve("vaccum", false);
                } else if (CommandSendConfig.GROUP1_VALVE_OPEN.equals(mCommand)) {
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group1"));
                } else if (CommandSendConfig.GROUP1_VALVE_CLOSE.equals(mCommand)) {
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group1"));
                } else if (CommandSendConfig.GROUP2_VALVE_OPEN.equals(mCommand)) {
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group2"));
                } else if (CommandSendConfig.GROUP2_VALVE_CLOSE.equals(mCommand)) {
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group2"));
                }else if ("group3_open".equals(mCommand)) {
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group3"));
                } else if ("group3_close".equals(mCommand)) {
                    sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group3"));
                }

            }
        });
    }


    @Override
    public void notifyByThemeChanged() {

    }
}