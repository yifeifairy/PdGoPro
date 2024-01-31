package com.emt.pdgo.next.ui.activity.local;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.adapter.local.HisRxLocalAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalPrescriptionActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.clear)
    StateButton clear;

    @BindView(R.id.tvCurrPage)
    TextView tvCurrPage;

    private HisRxLocalAdapter hisRxLocalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_local_prescription);
        ButterKnife.bind(this);
        initHeadTitleBar("");
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
    @SuppressLint("NotifyDataSetChanged")
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
        clear.setOnClickListener(v -> {
            final CommonDialog dialog = new CommonDialog(this);
            //
            //                    doGoCloseTOActivity(TreatmentCountdownActivity.class,"");
            dialog.setContent("确认清空？")
                    .setBtnFirst("确定")
                    .setBtnTwo("取消")
                    .setFirstClickListener(mCommonDialog -> {
                        EmtDataBase
                                .getInstance(LocalPrescriptionActivity.this)
                                .getRxDao()
                                .delete();
                        hisRxLocalAdapter.setNewData(getLocalRx());
                        hisRxLocalAdapter.notifyDataSetChanged();
                        mCommonDialog.dismiss();
                    })
                    .setTwoClickListener(Dialog::dismiss)
                    .show();
        });
    }

    @Override
    public void initViewData() {
        initRecyclerLocal();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerLocal() {
        tvCurrPage.setText(1+"/"+1 +"页");
        if (hisRxLocalAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            hisRxLocalAdapter = new HisRxLocalAdapter(getLocalRx());
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(hisRxLocalAdapter);
        } else {
            hisRxLocalAdapter.notifyDataSetChanged();
        }
    }

    private List<RxEntity> getLocalRx() {
        return EmtDataBase
                .getInstance(LocalPrescriptionActivity.this)
                .getRxDao()
                .getRxList();
    }

    @Override
    public void notifyByThemeChanged() {

    }
}