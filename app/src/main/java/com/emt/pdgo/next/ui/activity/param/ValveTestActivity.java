package com.emt.pdgo.next.ui.activity.param;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class ValveTestActivity extends BaseActivity {

    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.rv_value)
    RecyclerView rvSet;

    @BindView(R.id.tv_valve_negpre_drain)
    TextView tvValveNegpreDrain;
    @BindView(R.id.tv_valve_supply)
    TextView tvValveSupply;
    @BindView(R.id.tv_valve_perfusion)
    TextView tvValvePerfusion;
    @BindView(R.id.tv_valve_supply2)
    TextView tvValveSupply2;
    @BindView(R.id.tv_valve_safe)
    TextView tvValveSafe;
    @BindView(R.id.tv_valve_drain)
    TextView tvValveDrain;

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
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;
    @Override
    public void registerEvents() {
//        btnSave.setVisibility(View.VISIBLE);
//        btnSave.setText("保存");
        btnBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void initViewData() {
        init();
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
    private CompositeDisposable compositeDisposable;
    private void init() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.interval(0, EmtConstant.REPORT_DELAY_TIME, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposableObserver);
    }

    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {
            switch (mReceiveDeviceBean.vaccum) {
                case "00":
                case "80":
//            tvValveNegpreDrain.setTextColor(Color.YELLOW);
                    tvValveNegpreDrain.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveNegpreDrain.setTextColor(Color.GREEN);
                    tvValveNegpreDrain.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveNegpreDrain.setTextColor(Color.GREEN);
                    tvValveNegpreDrain.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValveNegpreDrain.setTextColor(Color.RED);
                    tvValveNegpreDrain.setBackgroundResource(R.color.red);
                    break;
            }
//        Log.e("详细状态","vaccum--"+mDeviceStatusInfo.vaccum+
//                "supply"+mDeviceStatusInfo.supply);
            switch (mReceiveDeviceBean.supply1) {
                case "00":
                case "80":
//            tvValveSupply.setTextColor(Color.YELLOW);
//            tvValveSupply.setTextColor(Color.YELLOW);
                    tvValveSupply.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSupply.setTextColor(Color.GREEN);
                    tvValveSupply.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSupply.setTextColor(Color.GREEN);
                    tvValveSupply.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValveSupply.setTextColor(Color.RED);
                    tvValveSupply.setBackgroundResource(R.color.red);
                    break;
            }
            switch (mReceiveDeviceBean.perfuse) {
                case "00":
                case "80":
//            tvValvePerfusion.setTextColor(Color.YELLOW);
                    tvValvePerfusion.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValvePerfusion.setTextColor(Color.GREEN);
                    tvValvePerfusion.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValvePerfusion.setTextColor(Color.GREEN);
                    tvValvePerfusion.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValvePerfusion.setTextColor(Color.RED);
                    tvValvePerfusion.setBackgroundResource(R.color.red);
                    break;
            }
            switch (mReceiveDeviceBean.drain) {
                case "00":
                case "80":
//            tvValveDrain.setTextColor(Color.YELLOW);
                    tvValveDrain.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveDrain.setTextColor(Color.GREEN);
                    tvValveDrain.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveDrain.setTextColor(Color.GREEN);
                    tvValveDrain.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValveDrain.setTextColor(Color.RED);
                    tvValveDrain.setBackgroundResource(R.color.red);
                    break;
            }
            switch (mReceiveDeviceBean.safe) {
                case "00":
                case "80":
//            tvValveSafe.setTextColor(Color.YELLOW);
                    tvValveSafe.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSafe.setTextColor(Color.GREEN);
                    tvValveSafe.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSafe.setTextColor(Color.GREEN);
                    tvValveSafe.setBackgroundResource(R.color.darkblue);
                    break;
                default:
                    tvValveSafe.setBackgroundResource(R.color.red);
//            tvValveSafe.setTextColor(Color.RED);
                    break;
            }
            switch (mReceiveDeviceBean.supply2) {
                case "00":
                case "80":
//            tvValveSupply2.setTextColor(Color.YELLOW);
                    tvValveSupply2.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSupply2.setTextColor(Color.GREEN);
                    tvValveSupply2.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSupply2.setTextColor(Color.GREEN);
                    tvValveSupply2.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValveSupply2.setTextColor(Color.RED);
                    tvValveSupply2.setBackgroundResource(R.color.red);
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }
    @Override
    public void notifyByThemeChanged() {

    }
}