package com.emt.pdgo.next.ui.activity.param;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.entity.CommandItem;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.adapter.CommandAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParamDebugActivity extends BaseActivity {

    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.rv_set)
    RecyclerView rvSet;

    private List<CommandItem> mList;
    private CommandAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_param_debug);
        ButterKnife.bind(this);
        initHeadTitleBar("参数设置调试");
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
        mList.add(new CommandItem("设备SN参数设置", "SNSet"));
//        mList.add(new CommandItem("通信地址设置", "UrlSet"));
        mList.add(new CommandItem("传感器数值校准", "WeighParameter"));
        mList.add(new CommandItem("网络参数设置", "NetSet"));
        mList.add(new CommandItem("温控参数设置", "TemperatureControlParameter"));
        mList.add(new CommandItem("预冲参数设置", "FirstRinseParameter"));
        mList.add(new CommandItem("引流参数设置", "DrainParameter"));
        mList.add(new CommandItem("灌注参数设置", "PerfusionParameter"));
        mList.add(new CommandItem("补液参数设置", "SupplyParameter"));
        mList.add(new CommandItem("其他参数设置","otherParamSet"));
        mAdapter = new CommandAdapter(this, R.layout.item_setting, mList);
        rvSet.setLayoutManager(new GridLayoutManager(this, 3));
        rvSet.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if ("WeighParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(WeighParameterActivity.class);
            } else if ("TemperatureControlParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(TemperatureControlParameterActivity.class);
            } else if ("FirstRinseParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(FirstRinseParameterActivity.class);
            }else if ("DrainParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(DrainParameterActivity.class);
            } else if ("PerfusionParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(PerfusionParameterActivity.class);
            } else if ("SupplyParameter".equals(mList.get(position).mCommand)) {
                doGoTOActivity(SupplyParameterActivity.class);
            } else if ("NetSet".equals(mList.get(position).mCommand)) {
                net();
            } else if ("UrlSet".equals(mList.get(position).mCommand)) {
                doGoTOActivity(UrlSetActivity.class);
            }else if ("SNSet".equals(mList.get(position).mCommand)) {
                doGoTOActivity(SNSetActivity.class);
            } else if("otherParamSet".equals(mList.get(position).mCommand)) {
                doGoTOActivity(OtherParamActivity.class);
            }
        });
    }

    @Override
    public void notifyByThemeChanged() {
        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
        helper.setBackgroundResourceByAttr(mAppBackground, R.attr.custom_attr_app_bg);
        if (mAdapter != null) mAdapter.notifyDataSetChanged(); //

        helper.setTextColorByAttr(tvTitle, R.attr.custom_attr_common_text_color);
    }
}