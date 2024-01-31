package com.emt.pdgo.next.ui.mode.activity.cfpd;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.model.mode.CfpdBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CfpdActivity extends BaseActivity {

    @BindView(R.id.calciumSwitch)
    LabeledSwitch calciumSwitch;

    @BindView(R.id.calciumVolRl)
    RelativeLayout calciumVolRl;
    @BindView(R.id.calciumVolTv)
    TextView calciumVolTv;

    @BindView(R.id.firstPerVolRl)
    RelativeLayout firstPerVolRl;
    @BindView(R.id.firstPerVolTv)
    TextView firstPerVolTv;
    @BindView(R.id.continueAbdVolRl)
    RelativeLayout continueAbdVolRl;
    @BindView(R.id.continueAbdVolTv)
    TextView continueAbdVolTv;
    @BindView(R.id.emptyingTimeRl)
    RelativeLayout emptyingTimeRl;
    @BindView(R.id.emptyingTimeTv)
    TextView emptyingTimeTv;
    @BindView(R.id.treatDurationRl)
    RelativeLayout treatDurationRl;
    @BindView(R.id.treatDurationTv)
    TextView treatDurationTv;
    @BindView(R.id.continuePerRoteRl)
    RelativeLayout continuePerRoteRl;
    @BindView(R.id.continuePerRoteTv)
    TextView continuePerRoteTv;
    @BindView(R.id.continueDrainRoteRl)
    RelativeLayout continueDrainRoteRl;
    @BindView(R.id.continueDrainRoteTv)
    TextView continueDrainRoteTv;
    @BindView(R.id.limitTempTv)
    TextView limitTempTv;
    @BindView(R.id.limitTempRl)
    RelativeLayout limitTempRl;
    @BindView(R.id.totalVolTv)
    TextView totalVolTv;

    @BindView(R.id.btn_submit)
    StateButton btn_submit;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    private CfpdBean prescription;
    @BindView(R.id.btnNext)
    StateButton btnNext;
    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_cfpd);
        ButterKnife.bind(this);
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
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.cFpd, prescription);
        });
        btn_submit.setOnClickListener(v -> {
            doGoTOActivity(ApdParamSetActivity.class);
        });
        limitTempRl.setOnClickListener(v -> {
            alertNumberBoardDialog(limitTempTv.getText().toString(), PdGoConstConfig.DPR_LIMIT_TEMP,false);
        });
        firstPerVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(firstPerVolTv.getText().toString(), PdGoConstConfig.CFPD_FIRST_PER,true);
        });
        continueAbdVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continueAbdVolTv.getText().toString(), PdGoConstConfig.CFPD_LEAVE_BELLY,true);
        });
        continuePerRoteRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continuePerRoteTv.getText().toString(), PdGoConstConfig.DPR_PER_RATE,true);
        });
        continueDrainRoteRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continueDrainRoteTv.getText().toString(), PdGoConstConfig.DPR_DRAIN_RATE,true);

        });
        calciumSwitch.setOnToggledListener((toggleableView, isOn) -> {
//            if (isOn) prescription.calcium =1; else prescription.calcium = 0;
            prescription.calcium = isOn ? 1 : 0;
        });
        calciumVolTv.setOnClickListener(v -> {
            alertNumberBoardDialog(continueDrainRoteTv.getText().toString(), PdGoConstConfig.DPR_DRAIN_RATE,true);
        });
        treatDurationRl.setOnClickListener(v -> {
            alertNumberBoardDialog(treatDurationTv.getText().toString(), PdGoConstConfig.DPR_TREAT_DURATION,true);

        });
        emptyingTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(emptyingTimeTv.getText().toString(), PdGoConstConfig.DPR_BELLY_EMPTY,true);
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("1.5%")) {
                    prescription.con = 0;
                } else if (rb.getText().equals("2.5%")) {
                    prescription.con = 1;
                } else if (rb.getText().equals("4.25%")) {
                    prescription.con = 2;
                } else if (rb.getText().equals("7.5%")) {
                    prescription.con = 2;
                }
            }
        });
    }

    @Override
    public void initViewData() {
        initHeadTitleBar("CFPD模式","参数设置");
        prescription = PdproHelper.getInstance().getCfpdBean();
        totalVolTv.setText(String.valueOf(prescription.totalVolume));
        treatDurationTv.setText(String.valueOf(prescription.totalTime));
        continuePerRoteTv.setText(String.valueOf(prescription.perfuse_rate));
        continueAbdVolTv.setText(String.valueOf(prescription.continue_retain));
        continueDrainRoteTv.setText(String.valueOf(prescription.drain_rate));
        emptyingTimeTv.setText(String.valueOf(prescription.emptying_time));
        firstPerVolTv.setText(String.valueOf(prescription.firstpersuse));
        calciumVolTv.setText(String.valueOf(prescription.calcium));
        float temp = (float) prescription.lowtemperature / 10;
        limitTempTv.setText(String.valueOf(temp));
    }

    private void alertNumberBoardDialog(String value, String type, boolean isInteger) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, isInteger);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.DPR_TOTAL_AMOUNT: // 治疗总量
                            totalVolTv.setText(result);
                            prescription.totalVolume = Integer.parseInt(result);
                            break;
//                        case PdGoConstConfig.DPR_TOTAL_TIME: //治疗总时间
////                            int o = (int) (Double.parseDouble(result) * 100);
////                            result = Double.valueOf(result) + "" ;//转换成浮点数
//                            treatDurationTv.setText(result);
//                            prescription.totalTime = Integer.parseInt(result);
//                            setTotal();
//                            break;
                        case PdGoConstConfig.DPR_TREAT_DURATION:
                            treatDurationTv.setText(result);
                            prescription.totalTime = Integer.parseInt(result);
                            setTotal();
                            break;
                        case PdGoConstConfig.DPR_PER_RATE: //持续灌注速率
                            continuePerRoteTv.setText(result);
                            prescription.perfuse_rate = Integer.parseInt(result);
                            setTotal();
                            break;
                        case PdGoConstConfig.CFPD_LEAVE_BELLY: //持续留腹量
                            continueAbdVolTv.setText(result);
                            prescription.continue_retain = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.DPR_DRAIN_RATE: //持续引流速率
                            continueDrainRoteTv.setText(result);
                            prescription.drain_rate = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.DPR_BELLY_EMPTY: //腹腔排空时间
                            emptyingTimeTv.setText(result);
                            prescription.emptying_time = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.CFPD_FIRST_PER: //首次灌注量
                            firstPerVolTv.setText(result);
                            prescription.firstpersuse = Integer.parseInt(result);
                            MyApplication.cfpdFirstVol = Integer.parseInt(result);
                            setTotal();
                            break;
                        case PdGoConstConfig.DPR_LIMIT_TEMP:
                            float i = Float.parseFloat(result);
                            limitTempTv.setText(String.valueOf(i));
                            prescription.lowtemperature = (int) (i *10);
                            break;
                    }
                }
            }
        });
    }

    private void setTotal() {
        int total = prescription.firstpersuse + prescription.totalTime * 60 * prescription.perfuse_rate;
        prescription.totalVolume = total;
        totalVolTv.setText(String.valueOf(total));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.cfpdFirstVol = PdproHelper.getInstance().getCfpdBean().firstpersuse;
    }

    @Override
    public void notifyByThemeChanged() {

    }
}