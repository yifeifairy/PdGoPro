package com.emt.pdgo.next.ui.activity.param;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.ReceiveResultDataBean;
import com.emt.pdgo.next.data.serial.SerialParamThermoStatBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.data.serial.receive.ThermostatBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.ToastUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * 温控板参数设置
 *
 * @author chenjh
 * @date 2019/2/1 12:18
 */
public class TemperatureControlParameterActivity extends BaseActivity {


    @BindView(R.id.et_t1)
    TextView etT1;
    @BindView(R.id.et_t2)
    TextView etT2;
    @BindView(R.id.et_t3)
    TextView etT3;
    @BindView(R.id.et_t4)
    TextView etT4;
    @BindView(R.id.et_t5)
    TextView etT5;
    @BindView(R.id.et_t6)
    TextView etT6;
    @BindView(R.id.et_t7)
    TextView etT7;
    @BindView(R.id.et_tae)
    TextView etTaE;
    @BindView(R.id.et_tbe)
    TextView etTbE;
    @BindView(R.id.et_tce)
    TextView etTcE;


    @BindView(R.id.latyout_t1)
    LinearLayout mLayoutT1;
    @BindView(R.id.latyout_t2)
    LinearLayout mLayoutT2;
    @BindView(R.id.latyout_t3)
    LinearLayout mLayoutT3;
    @BindView(R.id.latyout_t4)
    LinearLayout mLayoutT4;
    @BindView(R.id.latyout_t5)
    LinearLayout mLayoutT5;
    @BindView(R.id.latyout_t6)
    LinearLayout mLayoutT6;
    @BindView(R.id.latyout_t7)
    LinearLayout mLayoutT7;
    @BindView(R.id.latyout_tae)
    LinearLayout mLayoutTaE;
    @BindView(R.id.latyout_tbe)
    LinearLayout mLayoutTbE;
    @BindView(R.id.latyout_tce)
    LinearLayout mLayoutTcE;

    @BindView(R.id.tvT1Temp)
    TextView tvT0Temp;
    @BindView(R.id.tvT2Temp)
    TextView tvT1Temp;
    @BindView(R.id.tvT3Temp)
    TextView tvT2Temp;

    //最大温差 0.5<=T1<=2       T1：设定的参数值，Ta和Tb可接受最大温差，超过该温差T1温控板自检不通过
    private float T1 = 1.0f;

    //目标温度 34<=T2<=40       T2：设定的温度目标参数值
    private float T2 = 37.5f;

    //上下回差 0<=T3<=1         T3：设定的参数值，温控上下回差，腹透液袋表面温度低于（T2-T3）即启动加热，腹透液袋表面温度高于（T2+T3）即停止加热，但不报警
    private float T3 = 0f;

    //报警温度 T4=40.5 不可调    T4：设定的参数值，报警温度，腹透液袋表面温度上限，超过此温度T4即报警和停止治疗
    private float T4 = 40.5f;

    //加热板温度上限 40<=T5<=60  T5：设定的参数值，加热板表面温度上限，超过此温度T5即停止加热，但不报警
    private float T5 = 55.5f;

    //目标温度差 0<=T6<=5       T6：加热温度Td上升到靠近目标温度T2的接近温度差值T6
    private float T6 = 0.5f;

    //加热板温度调低值 1<=T7<=10 T7：动态调低加热板温度上限T5=T2+T7，降低加热板热量
    private float T7 = 3.5f;

    //Ta温度校正值 TaE TbE TcE -2<=TxE<=2
    private float TaE = 0f;

    //Tb温度校正值 TaE TbE TcE -2<=TxE<=2
    private float TbE = 0f;

    //Tc温度校正值 TaE TbE TcE -2<=TxE<=2
    private float TcE = 0f;

    private String mParameterInterpretation;

    private String[] mParameters;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_temperature_control_parameter);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        initHeadTitleBar("温控参数设置", "获取参数");
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)


    Button btnSave;
    @BindView(R.id.testLl)
    LinearLayout testLl;
    @BindView(R.id.testNum)
    TextView testNum;
    @BindView(R.id.testSwitch)
    SwitchCompat testSwitch;
    private void click() {
        testSwitch.setChecked(false);
        tsLl.setVisibility(View.INVISIBLE);
        scLl.setVisibility(View.INVISIBLE);
        testLl.setVisibility(View.INVISIBLE);
        testNum.setText(String.valueOf(PdproHelper.getInstance().valueTest()));
        testSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(isOpen,"group1"));
//                sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(isOpen,"group2"));
//                speak("请取出卡匣,关闭所有管夹");
                sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(isOpen,"group3"),1000);
                tsLl.setVisibility(View.VISIBLE);
                scLl.setVisibility(View.VISIBLE);
                testLl.setVisibility(View.VISIBLE);
                init();
                if (handler == null) {
                    handler = new Handler();
                }
                handler.postDelayed(runnable, PdproHelper.getInstance().valueTest() * 1000L);
            } else {
                if (disposableObserver != null) {
                    if (mCompositeDisposable != null) {
                        mCompositeDisposable.remove(disposableObserver);
                    }
                }
                testLl.setVisibility(View.INVISIBLE);
                tsLl.setVisibility(View.INVISIBLE);
                scLl.setVisibility(View.INVISIBLE);
                handler.removeCallbacks(runnable);
                per_s = per_f = safe_f = safe_s = sup_s = sup_f = su_f = su_s = drain_s = drain_f
                        = vac_f = vac_s = 0;
                per_f_num.setText(String.valueOf(per_f));
                per_c_num.setText(String.valueOf(per_s));
                safe_f_num.setText(String.valueOf(safe_f));
                safe_c_num.setText(String.valueOf(safe_s));
                supply_f_num.setText(String.valueOf(sup_f));
                supply_c_num.setText(String.valueOf(sup_s));
                su_f_num.setText(String.valueOf(su_f));
                su_c_num.setText(String.valueOf(su_s));
                drain_f_num.setText(String.valueOf(drain_f));
                drain_c_num.setText(String.valueOf(drain_s));
                vac_f_num.setText(String.valueOf(vac_f));
                vac_c_num.setText(String.valueOf(vac_s));
            }
        });
        testLl.setOnClickListener(view -> {
            NumberBoardDialog numberBoardDialog = new NumberBoardDialog(this,testNum.getText().toString(),PdGoConstConfig.VALUE_TEST,false,true);
            numberBoardDialog.show();
            numberBoardDialog.setOnDialogResultListener((type, result) -> {
                if (!TextUtils.isEmpty(result)) {
                    if (type.equals(PdGoConstConfig.VALUE_TEST)) {
                        if (handler == null) {
                            handler = new Handler();
                        }
                        handler.removeCallbacks(runnable);
                        if (disposableObserver != null) {
                            if (mCompositeDisposable != null) {
                                mCompositeDisposable.remove(disposableObserver);
                            }
                        }
                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.VALUE_TEST, result);
                        handler.postDelayed(runnable, PdproHelper.getInstance().valueTest() * 1000L);
                        init();
                        testNum.setText(String.valueOf(PdproHelper.getInstance().valueTest()));
                    }
                }
            });

        });
    }
    private boolean isOpen;
    private final Runnable runnable = new Runnable() {
         @Override
         public void run() {
             isOpen = !isOpen;
//             sendToMainBoard(CommandDataHelper.getInstance().valueOpen(isOpen));
             sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(isOpen,"group1"));
//             sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(isOpen,"group2"));
//                speak("请取出卡匣,关闭所有管夹");
             sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(isOpen,"group3"),1000);

             Log.e("温控界面","runnable---"+isOpen);
             handler.postDelayed(runnable, PdproHelper.getInstance().valueTest() * 1000L);
         }
     };

    private Handler handler;

    @Override
    public void registerEvents() {
        click();
        btnSave.setVisibility(View.VISIBLE);
        btnSave.setText("获取");
        btnBack.setOnClickListener(view -> onBackPressed());
        per_f_num.setText(String.valueOf(per_f));
        per_c_num.setText(String.valueOf(per_s));
        safe_f_num.setText(String.valueOf(safe_f));
        safe_c_num.setText(String.valueOf(safe_s));
        supply_f_num.setText(String.valueOf(sup_f));
        supply_c_num.setText(String.valueOf(sup_s));
        su_f_num.setText(String.valueOf(su_f));
        su_c_num.setText(String.valueOf(su_s));
        drain_f_num.setText(String.valueOf(drain_f));
        drain_c_num.setText(String.valueOf(drain_s));
        vac_f_num.setText(String.valueOf(vac_f));
        vac_c_num.setText(String.valueOf(vac_s));
    }

    @Override
    public void initViewData() {

//        sendToMainBoard(CommandDataHelper.getInstance().weightTareLowerCmdJson());
//        RxBus.get().register(this);
        mCompositeDisposable = new CompositeDisposable();

        mParameterInterpretation = getResources().getString(R.string.tip_parameter_interpretation);

        mParameters = getResources().getStringArray(R.array.tip_parameter_interpretations);

//        selectBaseModleSync();


        updateTemperatureData();

    }

    private Gson myGson = new Gson();
    private void updateTemperatureData(){
        statBean = new SerialParamThermoStatBean();

        statBean.p1 = (int) (T1 * 10);
        statBean.p2 = (int)(T2 * 10);
        statBean.p3 = (int)(T3 * 10);
        statBean.p4 = (int)(T4 * 10);
        statBean.p5 = (int)(T5 * 10);
        statBean.p6 = (int)(T6 * 10);
        statBean.p7 = (int)(T7 * 10);
        statBean.TaE = (int)(TaE * 10);
        statBean.TbE = (int)(TbE * 10);
        statBean.TcE = (int)(TcE * 10);

        etT1.setText(String.valueOf(T1));
        etT2.setText(String.valueOf(T2));
        etT3.setText(String.valueOf(T3));
        etT4.setText(String.valueOf(T4));
        etT5.setText(String.valueOf(T5));
        etT6.setText(String.valueOf(T6));
        etT7.setText(String.valueOf(T7));
        etTaE.setText(String.valueOf(TaE));
        etTbE.setText(String.valueOf(TbE));
        etTcE.setText(String.valueOf(TcE));
        sendToMainBoard(CommandDataHelper.getInstance().getThermostatCmdJson());
    }

    @OnClick({R.id.btnSave, R.id.et_t1, R.id.et_t2, R.id.et_t3, R.id.et_t4, R.id.et_t5, R.id.et_t6, R.id.et_t7, R.id.et_tae,R.id.et_tbe,R.id.et_tce,
            R.id.latyout_t1, R.id.latyout_t2, R.id.latyout_t3, R.id.latyout_t4, R.id.latyout_t5, R.id.latyout_t6, R.id.latyout_t7, R.id.latyout_tae,R.id.latyout_tbe,R.id.latyout_tce})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
//                showCommonDialog(mParameterInterpretation);
//                Intent intent = new Intent(this,CommonDialogActivity.class);
//                intent.putExtra("title","温控参数说明");
//                intent.putExtra("contents",mParameters);
//
//                startActivity(intent);
                sendToMainBoard(CommandDataHelper.getInstance().getThermostatCmdJson());
                break;
            case R.id.latyout_t1://
            case R.id.et_t1:
                //最大温度差
                alertNumberBoardDialog(etT1.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_MAX_DIFFERENCE, false);
                break;
            case R.id.latyout_t2:
            case R.id.et_t2:
                //"目标温度值";
                alertNumberBoardDialog(etT2.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGBTN, false);
                break;
            case R.id.latyout_t3:
            case R.id.et_t3:
                //"上下回差"
                alertNumberBoardDialog(etT3.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_UP_LOW_DIFFERENCE, false);
                break;
            case R.id.latyout_t4:
            case R.id.et_t4:
                //"报警温度值";
                ToastUtils.showToast(TemperatureControlParameterActivity.this, getResources().getString(R.string.toast_alarm_temperature));
                break;
            case R.id.latyout_t5:
            case R.id.et_t5:
                //"加热板温度上限";
                alertNumberBoardDialog(etT5.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_MAX_HEAT_PLATE, false);
                break;
            case R.id.latyout_t6:
            case R.id.et_t6:
                //"目标温度差";
                alertNumberBoardDialog(etT6.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGBTN_DIFFERENCE, false);
                break;
            case R.id.latyout_t7:
            case R.id.et_t7:
                //"加热板温度调低值";
                alertNumberBoardDialog(etT7.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_LOW_HEAT_PLATE, false);
                break;
            case R.id.latyout_tae:
            case R.id.et_tae:
                //"Ta温度校正值";
                alertNumberBoardDialog(etTaE.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TA_ADJUST, true);
                break;
            case R.id.latyout_tbe:
            case R.id.et_tbe:
                //"Tb温度校正值";
                alertNumberBoardDialog(etTbE.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TB_ADJUST, true);
                break;
            case R.id.latyout_tce:
            case R.id.et_tce:
                //"Tc温度校正值";
                alertNumberBoardDialog(etTcE.getText().toString(), PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TC_ADJUST, true);
                break;
        }
    }

    /**
     * 获取温控参数
     *
     * @param
     */
    @SuppressLint("SetTextI18n")
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_THERMOSTAT_SET)
    public void receiveReceiveThermostatSst(ReceiveResultDataBean dataBean) {
//        ThermostatBean bean = JsonHelper.jsonToClass(receiveData, ThermostatBean.class);
        ThermostatBean bean = JsonHelper.jsonToClass(myGson.toJson(dataBean.result.data), ThermostatBean.class);

        runOnUiThread(()-> {
            etT1.setText(String.valueOf((float) bean.p1 / 10));
            etT2.setText(String.valueOf((float) bean.p2 / 10));
            etT3.setText(String.valueOf((float) bean.p3 / 10));
            etT4.setText(String.valueOf((float) bean.p4 / 10));
            etT5.setText(String.valueOf((float) bean.p5 / 10));
            etT6.setText(String.valueOf((float) bean.p6 / 10));
            etT7.setText(String.valueOf((float) bean.p7 / 10));
            etTaE.setText(String.valueOf((float) bean.TaE / 10));
            etTbE.setText(String.valueOf((float) bean.TbE / 10));
            etTcE.setText(String.valueOf((float) bean.TcE / 10));

            statBean.p1 = bean.p1;
            statBean.p2 = bean.p2;
            statBean.p3 = bean.p3;
            statBean.p4 = bean.p4;
            statBean.p5 = bean.p5;
            statBean.p6 = bean.p6;
            statBean.p7 = bean.p7;
            statBean.TaE = bean.p2;
            statBean.TbE = bean.TbE;
            statBean.TcE = bean.TcE;

        });
    }

    private void alertNumberBoardDialog(String value, String type, boolean isMinus) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, isMinus);//"℃"
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    int i = (int) (Float.parseFloat(result) * 10);
                    result = Float.valueOf(result) + "" ;//转换成浮点数
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_MAX_DIFFERENCE: //最大温差  :  在0.5-2之间
                            etT1.setText(result);
                            statBean.p1 = i ;
                            thermostatSet();
//                        statBean.p1 = Float.valueOf(result);
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_1, result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGBTN: //目标温度值  :  在35-40之间
                            etT2.setText(result);
                            statBean.p2 = i;
                            thermostatSet();
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_2, result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_UP_LOW_DIFFERENCE: //上下回差  :  在0-1之间
                            etT3.setText(result);
                            statBean.p3 = i;
                            thermostatSet();
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_3, result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_MAX_HEAT_PLATE: //加热板温度上限  :  在40-60之间
                            etT5.setText(result);
                            statBean.p5 = i;
                            thermostatSet();
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_5, result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGBTN_DIFFERENCE: //目标温度差  :  在0-2之间
                            etT6.setText(result);
                            statBean.p6 = i;
                            thermostatSet();
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_6, result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_LOW_HEAT_PLATE: //加热板温度调低值  : 在1-10之间
                            etT7.setText(result);
                            statBean.p7 = i;
                            thermostatSet();
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_7, result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TA_ADJUST: //Ta温度校正值  :  在-2~2之间
                            etTaE.setText(result);
                            statBean.TaE = i;
                            thermostatSet();
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_TAE, result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TB_ADJUST: //Tb温度校正值  :  在-2~2之间
                            etTbE.setText(result);
                            statBean.TbE = i;
                            thermostatSet();
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_TBE, result);
                            break;
                        case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TC_ADJUST: //Tc温度校正值  :  在-2~2之间
                            etTcE.setText(result);
                            statBean.TcE = i;
                            thermostatSet();
//                        setTemperatureParam(CommandSendConfig.TEMPERATURE_SYS_PARAM_TCE, result);
                            break;
                    }
                }
            }
        });
    }
    private DisposableObserver<Long> disposableObserver;
    private void init() {
        disposableObserver = new DisposableObserver<Long>() {
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
        Observable.interval(0, PdproHelper.getInstance().valueTest() * 1000L, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposableObserver);
    }

    @BindView(R.id.per_c_num)
    TextView per_c_num;
    @BindView(R.id.per_f_num)
    TextView per_f_num;
    @BindView(R.id.safe_c_num)
    TextView safe_c_num;
    @BindView(R.id.safe_f_num)
    TextView safe_f_num;
    @BindView(R.id.supply_c_num)
    TextView supply_c_num;
    @BindView(R.id.supply_f_num)
    TextView supply_f_num;
    @BindView(R.id.su_c_num)
    TextView su_c_num;
    @BindView(R.id.su_f_num)
    TextView su_f_num;
    @BindView(R.id.drain_c_num)
    TextView drain_c_num;
    @BindView(R.id.drain_f_num)
    TextView drain_f_num;
    @BindView(R.id.vac_c_num)
    TextView vac_c_num;
    @BindView(R.id.vac_f_num)
    TextView vac_f_num;

    @BindView(R.id.scLl)
    LinearLayout scLl;
    @BindView(R.id.tsLl)
    LinearLayout tsLl;

    private int per_s, per_f, safe_s, safe_f, sup_s, sup_f, su_s, su_f, drain_s, drain_f, vac_s, vac_f;

    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdResultOk(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {
            MyApplication.currCmd = "";
            if (mReceiveDeviceBean != null) {
                tvT0Temp.setText(getTemp(mReceiveDeviceBean.temp) + "℃");
                tvT1Temp.setText(getTemp(mReceiveDeviceBean.isT1Err) + "℃");
                tvT2Temp.setText(getTemp(mReceiveDeviceBean.isT2Err) + "℃");

                if (testSwitch.isChecked()) {
                    if (mReceiveDeviceBean.perfuse.equals("00") || mReceiveDeviceBean.perfuse.equals("01")
                    || mReceiveDeviceBean.perfuse.equals("80") || mReceiveDeviceBean.perfuse.equals("81")) {
                        per_s ++;
                        per_c_num.setText(String.valueOf(per_s));
                    } else {
                        per_f ++;
                        per_f_num.setText(String.valueOf(per_f));
                        remo();
                    }
                    if (mReceiveDeviceBean.safe.equals("00") || mReceiveDeviceBean.safe.equals("01")
                            || mReceiveDeviceBean.safe.equals("80") || mReceiveDeviceBean.safe.equals("81")) {
                        safe_s ++;
                        safe_c_num.setText(String.valueOf(safe_s));
                    } else {
                        remo();
                        safe_f ++;
                        safe_f_num.setText(String.valueOf(safe_f));
                    }
                    if (mReceiveDeviceBean.supply1.equals("00") || mReceiveDeviceBean.supply1.equals("01")
                            || mReceiveDeviceBean.supply1.equals("80") || mReceiveDeviceBean.supply1.equals("81")) {
                        sup_s ++;
                        supply_c_num.setText(String.valueOf(sup_s));
                    } else {
                        sup_f ++;
                        supply_f_num.setText(String.valueOf(sup_f));
                        remo();
                    }
                    if (mReceiveDeviceBean.supply2.equals("00") || mReceiveDeviceBean.supply2.equals("01")
                            || mReceiveDeviceBean.supply2.equals("80") || mReceiveDeviceBean.supply2.equals("81")) {
                        su_s ++;
                        su_c_num.setText(String.valueOf(su_s));
                    } else {
                        su_f ++;
                        su_f_num.setText(String.valueOf(su_f));
                        remo();
                    }
                    if (mReceiveDeviceBean.drain.equals("00") || mReceiveDeviceBean.drain.equals("01")
                            || mReceiveDeviceBean.drain.equals("80") || mReceiveDeviceBean.drain.equals("81")) {
                        drain_s ++;
                        drain_c_num.setText(String.valueOf(drain_s));
                    } else {
                        drain_f ++;
                        drain_f_num.setText(String.valueOf(drain_f));
                        remo();
                    }
                    if (mReceiveDeviceBean.vaccum.equals("00") || mReceiveDeviceBean.vaccum.equals("01")
                            || mReceiveDeviceBean.vaccum.equals("80") || mReceiveDeviceBean.vaccum.equals("81")) {
                        vac_s ++;
                        vac_c_num.setText(String.valueOf(vac_s));
                    } else {
                        vac_f ++;
                        vac_f_num.setText(String.valueOf(vac_f));
                        remo();
                    }
//                    per_f_num.setText(String.valueOf(per_f));
//                    per_c_num.setText(String.valueOf(per_s));
//                    safe_f_num.setText(String.valueOf(safe_f));
//                    safe_c_num.setText(String.valueOf(safe_s));
//                    supply_f_num.setText(String.valueOf(sup_f));
//                    supply_c_num.setText(String.valueOf(sup_s));
//                    su_f_num.setText(String.valueOf(su_f));
//                    su_c_num.setText(String.valueOf(su_s));
//                    drain_f_num.setText(String.valueOf(drain_f));
//                    drain_c_num.setText(String.valueOf(drain_s));
//                    vac_f_num.setText(String.valueOf(vac_f));
//                    vac_c_num.setText(String.valueOf(vac_s));
                }
            }
        });

    }

    private void remo() {
//        handler.removeCallbacks(runnable);
//        if (disposableObserver != null) {
//            if (mCompositeDisposable != null) {
//                mCompositeDisposable.remove(disposableObserver);
//            }
//        }
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_DEVICE_INFO)
    public void receiveDevice(String mSerialJson) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(mSerialJson, ReceiveDeviceBean.class);
        Log.e("传感器设置界面", "   --->接收设备信息：" + mSerialJson);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mReceiveDeviceBean != null) {
                    tvT0Temp.setText(getTemp(mReceiveDeviceBean.temp) + "℃");
                    tvT1Temp.setText(getTemp(mReceiveDeviceBean.isT1Err) + "℃");
                    tvT2Temp.setText(getTemp(mReceiveDeviceBean.isT2Err) + "℃");
                }
            }
        });
    }
    private final BigDecimal mTen = new BigDecimal(10);
    private float getTemp(int temp) {
        BigDecimal mTemp = new BigDecimal(temp);
        BigDecimal newTemp = mTemp.divide(mTen, 1, RoundingMode.HALF_UP);
        return newTemp.floatValue();
    }

    /**
     * 发送指令给下位机设置温度参数
     *
     * @param mCommand
     * @param mParam
     */
    private void setTemperatureParam(String mCommand, String mParam) {
//        showLoadingDialog();
//
        sendToMainBoard(mCommand + " " + (int) (Float.valueOf(mParam) * 10));//下位机的值要为整数，所以乘10

    }

    private SerialParamThermoStatBean statBean;
    private void thermostatSet() {
        sendToMainBoard(CommandDataHelper.getInstance().setThermostatCmdJson(statBean));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void notifyByThemeChanged() {

    }

}
