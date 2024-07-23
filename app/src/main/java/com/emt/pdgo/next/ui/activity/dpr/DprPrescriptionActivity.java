package com.emt.pdgo.next.ui.activity.dpr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.serial.SerialRequestBean;
import com.emt.pdgo.next.data.serial.SerialRequestMainBean;
import com.emt.pdgo.next.data.serial.dpr.param.ConfigBean;
import com.emt.pdgo.next.data.serial.dpr.param.DprConfigParam;
import com.emt.pdgo.next.data.serial.dpr.param.DrainConfigParam;
import com.emt.pdgo.next.data.serial.dpr.param.ParamBean;
import com.emt.pdgo.next.data.serial.dpr.param.PerfuseConfigParam;
import com.emt.pdgo.next.data.serial.dpr.param.RetainConfigParam;
import com.emt.pdgo.next.data.serial.dpr.param.SupplyConfigParam;
import com.emt.pdgo.next.model.dpr.machine.Prescription;
import com.emt.pdgo.next.model.dpr.machine.param.DprOtherParam;
import com.emt.pdgo.next.model.dpr.machine.param.DrainParam;
import com.emt.pdgo.next.model.dpr.machine.param.PerfuseParam;
import com.emt.pdgo.next.model.dpr.machine.param.RetainParam;
import com.emt.pdgo.next.model.dpr.machine.param.SupplyParam;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.MathUtil;
import com.emt.pdgo.next.util.helper.MD5Helper;
import com.github.gzuliyujiang.wheelpicker.TimePicker;
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnTimePickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity;
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter;
import com.github.gzuliyujiang.wheelpicker.widget.TimeWheelLayout;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DprPrescriptionActivity extends BaseActivity {

    @BindView(R.id.btnNext)
    StateButton btnNext;

    @BindView(R.id.tvNorSel1)
    TextView tvNorSel1;
    @BindView(R.id.tvNorSel2)
    TextView tvNorSel2;
    @BindView(R.id.tvNorSel3)
    TextView tvNorSel3;
    @BindView(R.id.tvNorSel4)
    TextView tvNorSel4;
    @BindView(R.id.tvNorSel5)
    TextView tvNorSel5;
    @BindView(R.id.tvNorSel6)
    TextView tvNorSel6;

    @BindView(R.id.tvPenSel1)
    TextView tvPenSel1;

    @BindView(R.id.tvPenSel2)
    TextView tvPenSel2;
    @BindView(R.id.tvPenSel3)
    TextView tvPenSel3;

    @BindView(R.id.tvPenSel4)
    TextView tvPenSel4;
    @BindView(R.id.tvPenSel5)
    TextView tvPenSel5;

    @BindView(R.id.tvPenSel6)
    TextView tvPenSel6;

    @BindView(R.id.norTreatVolRl)
    RelativeLayout norTreatVolRl;
    @BindView(R.id.norTreatVolTv)
    TextView norTreatVolTv;

    @BindView(R.id.penTreatStartTimeTv)
    TextView penTreatStartTimeTv;
    @BindView(R.id.penTreatEndTimeTv)
    TextView penTreatEndTimeTv;
    @BindView(R.id.penTreatVolRl)
    RelativeLayout penTreatVolRl;
    @BindView(R.id.penTreatVolTv)
    TextView penTreatVolTv;
    @BindView(R.id.penIntervalTimeRl)
    RelativeLayout penIntervalTimeRl;
    @BindView(R.id.penIntervalTimeTv)
    TextView penIntervalTimeTv;
    @BindView(R.id.penNumTimeRl)
    RelativeLayout penNumTimeRl;
    @BindView(R.id.penNumTimeTv)
    TextView penNumTimeTv;
    @BindView(R.id.penTreatOsmRl)
    RelativeLayout penTreatOsmRl;
    @BindView(R.id.penTreatOsmTv)
    TextView penTreatOsmTv;

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

    @BindView(R.id.fixTimeRl)
    RelativeLayout fixTimeRl;
    @BindView(R.id.fixTimeTv)
    TextView fixTimeTv;

    @BindView(R.id.totalVolTv)
    TextView totalVolTv;

    @BindView(R.id.specialLayout)
    LinearLayout specialLayout;
    @BindView(R.id.specialSwitch)
    LabeledSwitch specialSwitch;

    @BindView(R.id.calciumRl)
    RelativeLayout calciumRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.totalVolRl)
    RelativeLayout totalVolRl;

    @BindView(R.id.emptySwitch)
    LabeledSwitch emptySwitch;

    private String jumpMsg;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_dpr_prescription);
        ButterKnife.bind(this);
        btnSave.setText("参数设置");
        btnSave.setVisibility(View.VISIBLE);
    }
    private int i = 0;
    private int j = 0;
    @SuppressLint("ResourceAsColor")
    @Override
    public void registerEvents() {
//        sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
        specialLayout.setVisibility(View.GONE);
        specialSwitch.setOnToggledListener((toggleableView, isOn) -> {
            specialLayout.setVisibility(isOn ? View.VISIBLE : View.GONE);
        });
        emptySwitch.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) prescription.empty_enable = 1 ; else prescription.empty_enable = 0;
        });
        fixTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(fixTimeTv.getText().toString(), PdGoConstConfig.DPR_FIX_TIME,true);
        });
        limitTempRl.setOnClickListener(v -> {
            alertNumberBoardDialog(limitTempTv.getText().toString(), PdGoConstConfig.DPR_LIMIT_TEMP,false);
        });
        btnSave.setOnClickListener(v -> {
            doGoTOActivity(DprParamSetActivity.class);
        });
        btnBack.setOnClickListener(view -> onBackPressed());
        btnNext.setOnClickListener(v -> {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.DRP_PRESCRIPTION, prescription);
//            dpdpreci();
//            if (MyApplication.dprTreatRunning) {
//                sendToMainBoard(CommandDataHelper.getInstance().config(1));
//                if (specialSwitch.isOn()) {
//                    MathUtil.getFs(prescription.nor_liquid, prescription.sel_liquid);
//                    sendCommandInterval(CommandDataHelper.getInstance().special(
//                                    1, MyApplication.supply1, MyApplication.supply2, 0)
//                            , 500);
//                }
//            } else {
//                MyApplication.isDpr = true;
//                sendToMainBoard(CommandDataHelper.getInstance().config(0));
//                if (specialSwitch.isOn()) {
//                    MathUtil.getFs(prescription.nor_liquid, prescription.sel_liquid);
//                    sendCommandInterval(CommandDataHelper.getInstance().special(
//                                    0, MyApplication.supply1, MyApplication.supply2, 0)
//                            , 500);
//                }
//                doGoTOActivity(PreHeatActivity.class);
//            }
            sendToMainBoard(CommandDataHelper.getInstance().config(0));
            if (specialSwitch.isOn()) {
                MathUtil.getFs(prescription.nor_liquid, prescription.sel_liquid);
                sendCommandInterval(CommandDataHelper.getInstance().dprSpecial(
                                0, MyApplication.supply1, MyApplication.supply2, specialSwitch.isOn()
                                        ?1:0)
                        , 500);
            }
            doGoTOActivity(PreHeatActivity.class);
        });
        calciumRl.setVisibility(View.GONE);
        firstPerVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(firstPerVolTv.getText().toString(), PdGoConstConfig.DPR_FIRST_PER,true);
        });
        totalVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(totalVolTv.getText().toString(), PdGoConstConfig.DPR_TOTAL_AMOUNT,true);
        });
        continueAbdVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continueAbdVolTv.getText().toString(), PdGoConstConfig.DPR_LEAVE_BELLY,true);
        });
        continuePerRoteRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continuePerRoteTv.getText().toString(), PdGoConstConfig.DPR_PER_RATE,true);
        });
        continueDrainRoteRl.setOnClickListener(v -> {
            alertNumberBoardDialog(continueDrainRoteTv.getText().toString(), PdGoConstConfig.DPR_DRAIN_RATE,true);
        });
        treatDurationRl.setOnClickListener(v -> {
            alertNumberBoardDialog(treatDurationTv.getText().toString(), PdGoConstConfig.DPR_TREAT_DURATION,true);
        });
        emptyingTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(emptyingTimeTv.getText().toString(), PdGoConstConfig.DPR_BELLY_EMPTY,true);
        });
        norTreatVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog(norTreatVolTv.getText().toString(), PdGoConstConfig.DPR_NOR_VOL,true);
        });
        penTreatVolRl.setOnClickListener(v -> {
            if (penTreatVolRl.isSelected()) {
                alertNumberBoardDialog(penTreatVolTv.getText().toString(), PdGoConstConfig.DPR_PER_VOL,true);
            } else {
                toastMessage("请选择渗透剂类型");
            }
        });
        penTreatOsmRl.setOnClickListener(v -> {
            alertNumberBoardDialog(penTreatOsmTv.getText().toString(), PdGoConstConfig.DPR_OSM,true);
        });
        penNumTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(penNumTimeTv.getText().toString(), PdGoConstConfig.DPR_NUM_TIME,true);
        });
        penIntervalTimeRl.setOnClickListener(v -> {
            alertNumberBoardDialog(penIntervalTimeTv.getText().toString(), PdGoConstConfig.DPR_INTER_TIME,true);
        });
        penTreatStartTimeTv.setOnClickListener(v -> {
            TimePicker timePicker = new TimePicker(this);
            TimeWheelLayout layout = timePicker.getWheelLayout();
            layout.setTimeMode(TimeMode.HOUR_24_NO_SECOND);
            layout.setTimeFormatter(new UnitTimeFormatter());
            layout.setDefaultValue(TimeEntity.now());
            layout.setResetWhenLinkage(false);
            timePicker.setOnTimePickedListener(new OnTimePickedListener() {
                @Override
                public void onTimePicked(int hour, int minute, int second) {
                    penTreatStartTimeTv.setText(hour+"时"+minute+"分");
                    prescription.startTime = hour+"时"+minute+"分";

                }
            });
            timePicker.show();
        });
        penTreatEndTimeTv.setOnClickListener(v -> {
            TimePicker timePicker = new TimePicker(this);
            TimeWheelLayout layout = timePicker.getWheelLayout();
            layout.setTimeMode(TimeMode.HOUR_24_NO_SECOND);
            layout.setTimeFormatter(new UnitTimeFormatter());
            layout.setDefaultValue(TimeEntity.now());
            layout.setResetWhenLinkage(false);
            timePicker.setOnTimePickedListener(new OnTimePickedListener() {
                @Override
                public void onTimePicked(int hour, int minute, int second) {
                    penTreatEndTimeTv.setText(hour+"时"+minute+"分");
                    prescription.endTime = hour+"时"+minute+"分";

                }
            });
            timePicker.show();
        });

        tvNorSel1.setOnClickListener(v -> {
//            tvNorSel1.setSelected(false);
            tvNorSel2.setSelected(false);
            tvNorSel3.setSelected(false);
            tvNorSel4.setSelected(false);
            tvNorSel5.setSelected(false);
            tvNorSel6.setSelected(false);
            tvNorSel1.setSelected(!tvNorSel1.isSelected());
            tvNorSel6.setVisibility(tvNorSel1.isSelected() ? View.INVISIBLE : View.VISIBLE);
            if (tvNorSel1.isSelected()) {
                i = 1;
                setOsm(i);
            } else {
                i = 0;
                setOsm(i);
            }
            calciumRl.setVisibility(tvNorSel1.isSelected() ? View.VISIBLE : View.GONE);
        });
        tvNorSel2.setOnClickListener(v -> {
            tvNorSel1.setSelected(false);
//            tvNorSel2.setSelected(false);
            tvNorSel3.setSelected(false);
            tvNorSel4.setSelected(false);
            tvNorSel5.setSelected(false);
            tvNorSel6.setSelected(false);
            tvNorSel2.setSelected(!tvNorSel2.isSelected());
            tvNorSel6.setVisibility(tvNorSel2.isSelected() ? View.INVISIBLE : View.VISIBLE);
            penTreatOsmTv.setText(String.valueOf((prescription.nor_liquid * 2 / 1000) * 55));
            if (tvNorSel2.isSelected()) {
                i = 2;
                setOsm(i);
            } else {
                i = 0;
                setOsm(i);
            }
            calciumRl.setVisibility(tvNorSel2.isSelected() ? View.VISIBLE : View.GONE);
        });
        tvNorSel3.setOnClickListener(v -> {
            tvNorSel1.setSelected(false);
            tvNorSel2.setSelected(false);
//            tvNorSel3.setSelected(false);
            tvNorSel4.setSelected(false);
            tvNorSel5.setSelected(false);
            tvNorSel6.setSelected(false);
            tvNorSel3.setSelected(!tvNorSel3.isSelected());
            tvNorSel6.setVisibility(tvNorSel3.isSelected() ? View.INVISIBLE : View.VISIBLE);
            if (tvNorSel3.isSelected()) {
                i = 3;
                setOsm(i);
            } else {
                i = 0;
                setOsm(i);
            }
            calciumRl.setVisibility(tvNorSel3.isSelected() ? View.VISIBLE : View.GONE);
        });
        tvNorSel4.setOnClickListener(v -> {
            tvNorSel1.setSelected(false);
            tvNorSel2.setSelected(false);
            tvNorSel3.setSelected(false);
//            tvNorSel4.setSelected(false);
            tvNorSel5.setSelected(false);
            tvNorSel6.setSelected(false);
            tvNorSel4.setSelected(!tvNorSel4.isSelected());
            tvNorSel6.setVisibility(tvNorSel4.isSelected() ? View.INVISIBLE : View.VISIBLE);
            if (tvNorSel4.isSelected()) {
                i = 4;
                setOsm(i);
            } else {
                i = 0;
                setOsm(i);
            }
            calciumRl.setVisibility(View.GONE);
        });
        tvNorSel5.setOnClickListener(v -> {
            tvNorSel1.setSelected(false);
            tvNorSel2.setSelected(false);
            tvNorSel3.setSelected(false);
            tvNorSel4.setSelected(false);
//            tvNorSel5.setSelected(false);
            tvNorSel6.setSelected(false);
            tvNorSel5.setSelected(!tvNorSel5.isSelected());
            tvNorSel6.setVisibility(tvNorSel5.isSelected() ? View.INVISIBLE : View.VISIBLE);
            if (tvNorSel5.isSelected()) {
                i = 5;
                setOsm(i);
            } else {
                i = 0;
                setOsm(i);
            }
            calciumRl.setVisibility(View.GONE);
        });
        tvNorSel6.setOnClickListener(v -> {
            tvNorSel1.setSelected(false);
            tvNorSel2.setSelected(false);
            tvNorSel3.setSelected(false);
            tvNorSel4.setSelected(false);
            tvNorSel5.setSelected(false);
//            tvNorSel6.setSelected(false);
            tvNorSel6.setSelected(!tvNorSel6.isSelected());
            i = 0;
            setOsm(i);
            calciumRl.setVisibility(View.GONE);
        });

        tvPenSel1.setOnClickListener(v -> {
            tvPenSel4.setSelected(false);
            tvPenSel2.setSelected(false);
            tvPenSel3.setSelected(false);
            tvPenSel5.setSelected(false);
            tvPenSel6.setSelected(false);
            tvPenSel1.setSelected(!tvPenSel1.isSelected());
            penTreatVolRl.setSelected(tvPenSel1.isSelected());

        });

        tvPenSel4.setOnClickListener(v -> {
            tvPenSel1.setSelected(false);
            tvPenSel2.setSelected(false);
            tvPenSel3.setSelected(false);
            tvPenSel5.setSelected(false);
            tvPenSel6.setSelected(false);
            tvPenSel4.setSelected(!tvPenSel4.isSelected());
            penTreatVolRl.setSelected(tvPenSel4.isSelected());
            if (tvPenSel4.isSelected()) {
                j = 4;
            } else {
                j = 0;
            }
        });
        tvPenSel2.setOnClickListener(v -> {
            tvPenSel1.setSelected(false);
            tvPenSel4.setSelected(false);
            tvPenSel3.setSelected(false);
            tvPenSel5.setSelected(false);
            tvPenSel6.setSelected(false);
            tvPenSel2.setSelected(!tvPenSel2.isSelected());
            penTreatVolRl.setSelected(tvPenSel2.isSelected());
        });
        tvPenSel3.setOnClickListener(v -> {
            tvPenSel1.setSelected(false);
            tvPenSel2.setSelected(false);
            tvPenSel4.setSelected(false);
            tvPenSel5.setSelected(false);
            tvPenSel6.setSelected(false);
            tvPenSel3.setSelected(!tvPenSel3.isSelected());
            penTreatVolRl.setSelected(tvPenSel3.isSelected());
            if (tvPenSel3.isSelected()) {
                j = 3;
            } else {
                j = 0;
            }
        });

        tvPenSel5.setOnClickListener(v -> {
            tvPenSel1.setSelected(false);
            tvPenSel4.setSelected(false);
            tvPenSel3.setSelected(false);
            tvPenSel6.setSelected(false);
            tvPenSel2.setSelected(false);
            tvPenSel5.setSelected(!tvPenSel5.isSelected());
            penTreatVolRl.setSelected(tvPenSel5.isSelected());
            if (tvPenSel5.isSelected()) {
                j = 5;
            } else {
                j = 0;
            }
        });
        tvPenSel6.setOnClickListener(v -> {
            tvPenSel1.setSelected(false);
            tvPenSel2.setSelected(false);
            tvPenSel4.setSelected(false);
            tvPenSel5.setSelected(false);
            tvPenSel3.setSelected(false);
            tvPenSel6.setSelected(!tvPenSel6.isSelected());
            penTreatVolRl.setSelected(tvPenSel6.isSelected());
        });
    }

    private Prescription prescription;

    @Override
    public void initViewData() {
        prescription = PdproHelper.getInstance().getPrescription();
        totalVolTv.setText(String.valueOf(prescription.totalVolume));
        treatDurationTv.setText(String.valueOf(prescription.supplying_interval));
        continuePerRoteTv.setText(String.valueOf(prescription.perfuse_rate));
        continueAbdVolTv.setText(String.valueOf(prescription.continue_retain));
        continueDrainRoteTv.setText(String.valueOf(prescription.drain_rate));
        emptyingTimeTv.setText(String.valueOf(prescription.emptying_time));
        firstPerVolTv.setText(String.valueOf(prescription.firstpersuse));
        norTreatVolTv.setText(String.valueOf(prescription.nor_liquid));
        penTreatVolTv.setText(String.valueOf(prescription.sel_liquid));
        penIntervalTimeTv.setText(String.valueOf(prescription.inter_time));
        penTreatOsmTv.setText(String.valueOf(prescription.osm));
        penNumTimeTv.setText(String.valueOf(prescription.num));
//        totalVolTv.setText(String.valueOf(prescription.nor_liquid + prescription.sel_liquid));
        penTreatStartTimeTv.setText(prescription.startTime);
        emptySwitch.setOn(prescription.empty_enable == 1);
        float temp = (float) prescription.lowtemperature / 10;
        limitTempTv.setText(String.valueOf(temp));
        penTreatEndTimeTv.setText(prescription.endTime);
//        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
//        if (jumpMsg != null) {
//            if (jumpMsg.equals(EmtConstant.ACTIVITY_TREAT_MODE)) {
//                btnNext.setVisibility(View.VISIBLE);
//                config();
//            } else {
//                btnNext.setVisibility(View.INVISIBLE);
//            }
//        }
        if (!MyApplication.dprTreatRunning) {
            config();
        }
    }

    private void config() {
        DrainParam drainParam = PdproHelper.getInstance().getDrainParam();
        SupplyParam supplyParam = PdproHelper.getInstance().getSupplyParam();
        RetainParam retainParam = PdproHelper.getInstance().getRetainParam();
        PerfuseParam perfuseParam = PdproHelper.getInstance().getPerfuseParam();
        DprOtherParam otherParam = PdproHelper.getInstance().getDprOtherParam();
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();

        SerialRequestBean mSerialRequestBean = new SerialRequestBean();

        ConfigBean mParamBean = new ConfigBean();

        DprConfigParam dpr = new DprConfigParam();
        dpr.FaultTime = otherParam.FaultTime;
        dpr.DprFlowPeriod = otherParam.DprFlowPeriod;
        dpr.DprSuppthreshold = otherParam.DprSuppthreshold;
        dpr.PerfuseDecDrainThod = otherParam.PerfuseDecDrainThod;
        dpr.TotalRemainderThod = otherParam.TotalRemainderThod;

        DrainConfigParam drain = new DrainConfigParam();
        drain.drainFlowRate = drainParam.drainFlowRate;
        drain.VaccumDraintimes = drainParam.VaccumDraintimes;
        drain.drainPassRate = drainParam.drainPassRate;
        drain.drainFlowPeriod = drainParam.drainFlowPeriod;
        drain.finalDrainEmptyWaitTime = drainParam.finalDrainEmptyWaitTime;
        drain.isFinalDrainEmpty = drainParam.isFinalDrainEmpty ? 1 : 0;
        drain.isFinalDrainEmptyWait = drainParam.isFinalDrainEmptyWait ? 1 : 0;
        drain.isVaccumDrain = drainParam.isVaccumDrain ? 1 : 0;

        PerfuseConfigParam perfuse = new PerfuseConfigParam();
        perfuse.perfuseFlowPeriod = perfuseParam.perfuseFlowPeriod;
        perfuse.perfuseFlowRate = perfuseParam.perfuseFlowRate;
        perfuse.perfuseMaxVolume = perfuseParam.perfuseMaxVolume;

        SupplyConfigParam supply = new SupplyConfigParam();
        supply.supplyFlowPeriod = supplyParam.supplyFlowPeriod;
        supply.supplyFlowRate = supplyParam.supplyFlowRate;
        supply.supplyMinVolume = supplyParam.supplyMinVolume;
        supply.supplyProtectVolume = supplyParam.supplyProtectVolume;

        RetainConfigParam retain = new RetainConfigParam();
        retain.isFiltCycleOnly = retainParam.isFiltCycleOnly ? 1 : 0;
        retain.isFinalRetainDeduct = retainParam.isFinalRetainDeductl ? 1 : 0;
        retain.parammodify =  0;

        mParamBean.drain = drain;
        mParamBean.dpr = dpr;
        mParamBean.perfuse = perfuse;
        mParamBean.retain = retain;
        mParamBean.supply = supply;

        mSerialRequestBean.method = "dprparam/config";
        mSerialRequestBean.params = mParamBean;

        String md5Json = new Gson().toJson(mSerialRequestBean);

        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);

        String sendData = new Gson().toJson(mRequestBean);
        sendToMainBoard(sendData);
    }

    private void dpdpreci() {
        Prescription prescription = PdproHelper.getInstance().getPrescription();
        SerialRequestMainBean mRequestBean = new SerialRequestMainBean();
        SerialRequestBean mSerialRequestBean = new SerialRequestBean();
        ParamBean paramBean = new ParamBean();
        paramBean.dprmodify =  0;
        paramBean.drain_rate = prescription.drain_rate;
        paramBean.emptying_time = prescription.emptying_time;
        paramBean.perfuse_rate = prescription.perfuse_rate;
        paramBean.firstperfuse = prescription.firstpersuse;
        paramBean.continue_retain = prescription.continue_retain;
        paramBean.empty_enable = prescription.empty_enable;
        paramBean.totalVolume = prescription.totalVolume;
        paramBean.supplying_interval = prescription.supplying_interval;
        paramBean.lowtemperature = prescription.lowtemperature;
        mSerialRequestBean.method = "dprpresci/config";
        mSerialRequestBean.params = paramBean;
        String md5Json = new Gson().toJson(mSerialRequestBean);
        mRequestBean.request = mSerialRequestBean;
        mRequestBean.sign = MD5Helper.Md5(md5Json);
        sendToMainBoard(new Gson().toJson(mRequestBean));
    }

//    @OnClick({R.id.btn_submit})
//    public void onViewClicked(View view) {
//        if (view.getId() == R.id.btn_submit) {//
//
//            doGoTOActivity(ParameterSettingActivity.class);
////                doGoTOActivity(PipelineConnectionActivity.class);
//        }
//    }

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
                        case PdGoConstConfig.DPR_TREAT_DURATION:
                            treatDurationTv.setText(result);
                            prescription.supplying_interval = Integer.parseInt(result);
//                            setTotalVolTv();
                            break;
//                        case PdGoConstConfig.DPR_TOTAL_TIME: //治疗总时间
//                            int o = (int) (Double.parseDouble(result) * 100);
//                            result = Double.valueOf(result) + "" ;//转换成浮点数
//                            treatDurationTv.setText(result);
//                            prescription.supplying_interval = o;
//                            break;
                        case PdGoConstConfig.DPR_PER_RATE: //持续灌注速率
                            continuePerRoteTv.setText(result);
                            prescription.perfuse_rate = Integer.parseInt(result);
//                            setTotalVolTv();
                            break;
                        case PdGoConstConfig.DPR_LEAVE_BELLY: //持续留腹量
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
                        case PdGoConstConfig.DPR_FIRST_PER: //首次灌注量
                            firstPerVolTv.setText(result);
                            prescription.firstpersuse = Integer.parseInt(result);
                            MyApplication.firstVol = Integer.parseInt(result);
//                            setTotalVolTv();
                            break;
                        case PdGoConstConfig.DPR_FIX_TIME: //首次灌注量
                            fixTimeTv.setText(result);
                            prescription.firstpersuse = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.DPR_NOR_VOL: //首次灌注量
                            norTreatVolTv.setText(result);
                            prescription.nor_liquid = Integer.parseInt(result);
//                            setTotalVolTv();
                            break;
                        case PdGoConstConfig.DPR_PER_VOL: //首次灌注量
                            penTreatVolTv.setText(result);
                            prescription.sel_liquid = Integer.parseInt(result);
//                            setTotalVolTv();
                            setOsm(i);
                            break;
                        case PdGoConstConfig.DPR_OSM: //首次灌注量
                            penTreatOsmTv.setText(result);
                            prescription.osm = Integer.parseInt(result);
                            
                            break;
                        case PdGoConstConfig.DPR_INTER_TIME: //首次灌注量
                            penIntervalTimeTv.setText(result);
                            prescription.inter_time = Integer.parseInt(result);
                            
                            break;
                        case PdGoConstConfig.DPR_NUM_TIME: //首次灌注量
                            penNumTimeTv.setText(result);
                            prescription.num = Integer.parseInt(result);
                            break;
                        case PdGoConstConfig.DPR_LIMIT_TEMP:
                            float i = Float.parseFloat(result);
                            limitTempTv.setText(String.valueOf(i));
                            prescription.lowtemperature = (int) (i *10);
                            break;
//                        case PdGoConstConfig.DPR_TARGET_TEMP: //目标温度
//                            int i = (int) (Float.parseFloat(result) * 10);
//                            result = Float.valueOf(result) + "" ;//转换成浮点数
//                            etPrescriptionItem8.setText(result);
//                            prescription.target_temperature = i;
//                            break;
//                        case PdGoConstConfig.DPR_LIMIT_TEMP: //下限温度
//                            int j = (int) (Float.parseFloat(result) * 10);
//                            result = Float.valueOf(result) + "" ;//转换成浮点数
//                            etPrescriptionItem9.setText(result);
//                            prescription.lowtemperature = j;
//                            break;
                    }
                }
            }
        });
    }

    private void setOsm(int i) {
        switch (i) {
            case 1:
                penTreatOsmTv.setText(String.valueOf(346 + prescription.sel_liquid  * (55 / 22)));
                break;
            case 2:
                penTreatOsmTv.setText(String.valueOf(395 + (prescription.sel_liquid * (55 / 22))));
                break;
            case 3:
                penTreatOsmTv.setText(String.valueOf(485 + (prescription.sel_liquid * (55 / 22))));
                break;
            case 4:
                penTreatOsmTv.setText(String.valueOf(365 + (prescription.sel_liquid * (55 / 22))));
                break;
            case 5:
                if (j == 3 || j == 4 || j == 5) {
                    Log.e("dpr处方", "j---"+j);
                } else {
                    penTreatOsmTv.setText(String.valueOf(284 + (prescription.sel_liquid * (55 / 22))));
                }
                break;
            default:
                penTreatOsmTv.setText(String.valueOf(0));
                break;
        }
    }

    private void setTotalVolTv() {
        int total;
        if (specialLayout.getVisibility() == View.VISIBLE) {
            total = prescription.nor_liquid + prescription.sel_liquid +
                    prescription.firstpersuse + prescription.totalTime * 60 * prescription.perfuse_rate;
        } else {
            total = prescription.firstpersuse + prescription.totalTime * 60 * prescription.perfuse_rate;
        }
        totalVolTv.setText(String.valueOf(total));
        prescription.totalVolume = total;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.firstVol = PdproHelper.getInstance().getPrescription().firstpersuse;
    }

    @Override
    public void notifyByThemeChanged() {

    }
}