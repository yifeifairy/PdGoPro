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

public class LamplightActivity extends BaseActivity {

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
        setContentView(R.layout.activity_lamplight);
        ButterKnife.bind(this);
        initHeadTitleBar("灯光测试");
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
        mList.add(new CommandItem("打开灌注灯", "perfuse_led_open"));
        mList.add(new CommandItem("关闭灌注灯", "perfuse_led_close"));
        mList.add(new CommandItem("打开留腹灯", "retain_led_open"));
        mList.add(new CommandItem("关闭留腹灯", "retain_led_close"));
        mList.add(new CommandItem("打开引流灯", "drain_led_open"));
        mList.add(new CommandItem("关闭引流灯", "drain_led_close"));
        mList.add(new CommandItem("打开全部灯", "all_led_open"));
        mList.add(new CommandItem("关闭全部灯", "all_led_close"));

        mAdapter = new CommandAdapter(this, R.layout.item_setting, mList);
        rvSet.setLayoutManager(new GridLayoutManager(this, 3));
        rvSet.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int dayFlag = 0;
            if ("perfuse_led_close".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("perfuse", false,dayFlag,2), 500);
//                    sendLedCommandInterval(CommandDataHelper.getInstance().LedOpen("supply", false), 500 );
            } else if ("perfuse_led_open".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("perfuse", true,dayFlag,2), 500);
            }else if ("retain_led_close".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("retain", false,dayFlag,2), 500);
//                    sendLedCommandInterval(CommandDataHelper.getInstance().LedOpen("supply", false), 500 );
            } else if ("retain_led_open".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("retain", true,dayFlag,2), 500);
            }else if ("drain_led_close".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("drain", false,dayFlag,2), 500);
//                    sendLedCommandInterval(CommandDataHelper.getInstance().LedOpen("supply", false), 500 );
            } else if ("drain_led_open".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("drain", true,dayFlag,2), 500);
            }else if ("all_led_close".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("all", false,dayFlag,2), 500);
//                    sendLedCommandInterval(CommandDataHelper.getInstance().LedOpen("supply", false), 500 );
            } else if ("all_led_open".equals(mList.get(position).mCommand)) {
                sendCommandInterval(CommandDataHelper.getInstance().LedOpen("all", true,dayFlag,2), 500);
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