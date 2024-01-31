package com.emt.pdgo.next.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mario on 2017-03-05.
 */

public class RecyclerActivity extends BaseActivity {

    @BindView(R.id.btnNext)
    StateButton btnNext;

    @BindView(R.id.et_prescription_item1)
    TextView etPrescriptionItem1;
    @BindView(R.id.layout_prescription_item_1)
    RelativeLayout layoutPrescriptionItem1;
    @BindView(R.id.et_prescription_item2)
    TextView etPrescriptionItem2;
    @BindView(R.id.layout_prescription_item_2)
    RelativeLayout layoutPrescriptionItem2;
    @BindView(R.id.et_prescription_item3)
    TextView etPrescriptionItem3;
    @BindView(R.id.et_prescription_item4)
    TextView etPrescriptionItem4;
    @BindView(R.id.layout_prescription_item_4)
    RelativeLayout layoutPrescriptionItem4;
    @BindView(R.id.et_prescription_item5)
    TextView etPrescriptionItem5;
    @BindView(R.id.layout_prescription_item_5)
    RelativeLayout layoutPrescriptionItem5;
    @BindView(R.id.et_prescription_item6)
    TextView etPrescriptionItem6;
    @BindView(R.id.layout_prescription_item_6)
    RelativeLayout layoutPrescriptionItem6;

    @BindView(R.id.et_prescription_item7)
    TextView etPrescriptionItem7;
    @BindView(R.id.layout_prescription_item_7)
    RelativeLayout layoutPrescriptionItem7;
    @BindView(R.id.et_prescription_item8)
    TextView etPrescriptionItem8;
    @BindView(R.id.layout_prescription_item_8)
    RelativeLayout layoutPrescriptionItem8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_recycler);
        ButterKnife.bind(this);
        initHeadTitleBar("处方设置", "治疗参数");
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
        btnNext.setOnClickListener(v -> {
           doGoTOActivity(PreHeatActivity.class);
        });
    }

    @OnClick({R.id.btn_submit, R.id.et_prescription_item1, R.id.et_prescription_item2, R.id.et_prescription_item3, R.id.et_prescription_item4,
            R.id.et_prescription_item5, R.id.et_prescription_item6, R.id.et_prescription_item7, R.id.et_prescription_item8, R.id.btn_back,
            R.id.layout_prescription_item_1, R.id.layout_prescription_item_2, R.id.layout_prescription_item_3, R.id.layout_prescription_item_4,
            R.id.layout_prescription_item_5, R.id.layout_prescription_item_6, R.id.layout_prescription_item_7, R.id.layout_prescription_item_8,
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit://
                doGoTOActivity(ParameterSettingActivity.class);
//                doGoTOActivity(PipelineConnectionActivity.class);
                break;
            case R.id.et_prescription_item1://腹透液总量
            case R.id.layout_prescription_item_1://
                alertNumberBoardDialog(etPrescriptionItem1.getText().toString(), "0");
                break;
            case R.id.et_prescription_item2://每周期灌注量：
            case R.id.layout_prescription_item_2://
                alertNumberBoardDialog(etPrescriptionItem2.getText().toString(), "1");
                break;
            case R.id.et_prescription_item3://循环治疗周期数：
            case R.id.layout_prescription_item_3://
                alertNumberBoardDialog(etPrescriptionItem3.getText().toString(), "2");
                break;
            case R.id.et_prescription_item4://留腹时间：
            case R.id.layout_prescription_item_4://
                alertNumberBoardDialog(etPrescriptionItem4.getText().toString(), "3");
                break;
            case R.id.et_prescription_item5://末次留腹量：
            case R.id.layout_prescription_item_5://
                alertNumberBoardDialog(etPrescriptionItem5.getText().toString(), "4");
                break;
            case R.id.et_prescription_item6://上次留腹量：
            case R.id.layout_prescription_item_6://
                alertNumberBoardDialog(etPrescriptionItem6.getText().toString(), "5");
                break;

            case R.id.et_prescription_item7://首次灌注量：
            case R.id.layout_prescription_item_7://
                alertNumberBoardDialog(etPrescriptionItem7.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME);
                break;
            case R.id.et_prescription_item8://预估超滤量：
            case R.id.layout_prescription_item_8://
                alertNumberBoardDialog(etPrescriptionItem8.getText().toString(), "6");
                break;
//            case R.id.btn_back:
//                back();
//                Log.e("處方設置","btn_back--");
//                ActivityManager.getActivityManager().removeActivity(PrescriptionActivity.this);
//                break;
        }
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case "0": //腹透液总量
                            etPrescriptionItem1.setText(result);
                            break;
                        case "1": //每周期灌注量
                            etPrescriptionItem2.setText(result);
                            break;
                        case "2": //循环治疗周期数
                            etPrescriptionItem3.setText(result);
                            break;
                        case "3": //留腹时间
                            etPrescriptionItem4.setText(result);
                            break;
                        case "4": //末次留腹量
                            etPrescriptionItem5.setText(result);
                            break;
                        case "5": //上次留腹量
                            etPrescriptionItem6.setText(result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
                            etPrescriptionItem7.setText(result);
                            break;
                        case "6": //腹透液总量
                            etPrescriptionItem8.setText(result);
                            break;
                    }
                }
            }
        });
    }


    @Override
    public void initViewData() {

    }

    @Override
    public void notifyByThemeChanged() {
        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());


    }
}
