package com.emt.pdgo.next.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.OtherParamBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Mario on 2017-03-02.
 */

public class LaunchActivity extends BaseActivity {

//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerView;

//    private FaultMsgAdapter adapter;

    @BindView(R.id.tvPerHeartWeight)
    TextView tvPerHeartWeight;
    @BindView(R.id.perHeartWeightRl)
    RelativeLayout perHeartWeightRl;
    @BindView(R.id.layout_right_menu)
    RelativeLayout layout_right_menu;

    private OtherParamBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        initHeadTitleBar("其他参数设置","保存");
//        initAllViews();
//        initAllDatum();
    }

    @Override
    public void initAllViews() {

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
        bean = PdproHelper.getInstance().getOtherParamBean();
        perHeartWeightRl.setOnClickListener(v -> {
            alertNumberBoardDialog("",PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_UPPER);
        });
        layout_right_menu.setOnClickListener(v -> {
            toastMessage("保存成功");
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.OTHER_PARAMETER, bean);
        });
    }

    @Override
    public void initViewData() {
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
    public void notifyByThemeChanged() {
        // TODO
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
//                    Logger.d(result);
                    if (mType.equals(PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_UPPER)) {//上位秤 称量系数
                        tvPerHeartWeight.setText(result);
                        bean.perHeartWeight = Integer.parseInt(result);
                    }
                }
            }
        });
    }

}
