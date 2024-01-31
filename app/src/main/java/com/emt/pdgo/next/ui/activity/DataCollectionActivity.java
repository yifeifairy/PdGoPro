package com.emt.pdgo.next.ui.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.activity
 * @ClassName: DataCollectionActivity
 * @Description: 数据收集
 * @Author: chenjh
 * @CreateDate: 2019/12/19 6:22 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/19 6:22 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class DataCollectionActivity extends BaseActivity {

    //尿量
    @BindView(R.id.et_body_data_item1)
    EditText etBodyDataItem1;
    @BindView(R.id.layout_body_data_item_1)
    RelativeLayout layoutBodyDataItem1;
    //饮水量
    @BindView(R.id.et_body_data_item2)
    EditText etBodyDataItem2;
    @BindView(R.id.layout_body_data_item_2)
    RelativeLayout layoutBodyDataItem2;
    //血糖
    @BindView(R.id.et_body_data_item3)
    EditText etBodyDataItem3;
    @BindView(R.id.layout_body_data_item_3)
    RelativeLayout layoutBodyDataItem3;
    //治疗后体重
    @BindView(R.id.et_body_data_item4)
    EditText etBodyDataItem4;
    @BindView(R.id.layout_body_data_item_4)
    RelativeLayout layoutBodyDataItem4;
    //治疗后舒张压
    @BindView(R.id.et_body_data_item5)
    EditText etBodyDataItem5;
    @BindView(R.id.layout_body_data_item_5)
    RelativeLayout layoutBodyDataItem5;
    //治疗后收缩压
    @BindView(R.id.et_body_data_item6)
    EditText etBodyDataItem6;
    @BindView(R.id.layout_body_data_item_6)
    RelativeLayout layoutBodyDataItem6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_data_collection);
        ButterKnife.bind(this);
        initHeadTitleBar("数据收集", "跳过");
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
        setCanNotEditNoClick2(etBodyDataItem1);
        setCanNotEditNoClick2(etBodyDataItem2);
        setCanNotEditNoClick2(etBodyDataItem3);
        setCanNotEditNoClick2(etBodyDataItem4);
        setCanNotEditNoClick2(etBodyDataItem5);
        setCanNotEditNoClick2(etBodyDataItem6);
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

    @OnClick({R.id.btn_submit, R.id.layout_right_menu, R.id.btn_save, R.id.et_body_data_item1, R.id.layout_body_data_item_1, R.id.et_body_data_item2, R.id.layout_body_data_item_2, R.id.et_body_data_item3, R.id.layout_body_data_item_3,
            R.id.et_body_data_item4, R.id.layout_body_data_item_4, R.id.et_body_data_item5, R.id.layout_body_data_item_5, R.id.et_body_data_item6, R.id.layout_body_data_item_6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
            case R.id.layout_right_menu://跳过
                doGoTOActivity(TreatmentFeedbackActivity.class);
                break;
            case R.id.btn_save://保存

                break;
            case R.id.et_body_data_item1://尿量
            case R.id.layout_body_data_item_1:
                alertNumberBoardDialog(etBodyDataItem1.getText().toString(), PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_URINE_OUTPUT);
                break;
            case R.id.et_body_data_item2://饮水量
            case R.id.layout_body_data_item_2:
                alertNumberBoardDialog(etBodyDataItem2.getText().toString(), PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_WATER_INTAKE);
                break;
            case R.id.et_body_data_item3://血糖
            case R.id.layout_body_data_item_3:
                alertNumberBoardDialog(etBodyDataItem3.getText().toString(), PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_FASTING_BLOOD_GLUCOSE);
                break;
            case R.id.et_body_data_item4://体重
            case R.id.layout_body_data_item_4:
                alertNumberBoardDialog(etBodyDataItem4.getText().toString(), PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_WEIGHT);
                break;
            case R.id.et_body_data_item5://收缩压
            case R.id.layout_body_data_item_5:
                alertNumberBoardDialog(etBodyDataItem5.getText().toString(), PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_SYSTOLIC_BLOOD_PRESSURE);
                break;
            case R.id.et_body_data_item6://舒张压
            case R.id.layout_body_data_item_6:
                alertNumberBoardDialog(etBodyDataItem6.getText().toString(), PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_DIASTOLIC_BLOOD_PRESSURE);
                break;
        }
    }


    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_URINE_OUTPUT)) {//尿量
                        etBodyDataItem1.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_WATER_INTAKE)) {//饮水量
                        etBodyDataItem2.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_FASTING_BLOOD_GLUCOSE)) {//血糖
                        etBodyDataItem3.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_WEIGHT)) {//体重
                        etBodyDataItem4.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_SYSTOLIC_BLOOD_PRESSURE)) {//收缩压
                        etBodyDataItem5.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_DIASTOLIC_BLOOD_PRESSURE)) {//舒张压
                        etBodyDataItem6.setText(result);
                    }
                }
            }
        });
    }

    @Override
    public void notifyByThemeChanged() {

    }

}
