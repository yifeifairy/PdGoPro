package com.emt.pdgo.next.ui.activity.param;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class WeighParameterActivity extends BaseActivity {

    private String TAG = "WeighParameterActivity";

    @BindView(R.id.tv_up_weight)
    TextView tvUpWeight;
    @BindView(R.id.tv_low_weight)
    TextView tvLowWeight;
    @BindView(R.id.et_up_weigh_ratio)
    EditText etUpWeighRatio;
    @BindView(R.id.et_low_weigh_ratio)
    EditText etLowWeighRatio;

    @BindView(R.id.btn_up_peeled)
    Button btnUpPeeled;
    @BindView(R.id.btn_low_peeled)
    Button btnLowPeeled;
    @BindView(R.id.btn_up_low_peeled)
    Button btnUpLowPeeled;

    private CompositeDisposable mCompositeDisposable;
    //
    //上位秤 校准值
    private int upperCalibration = 0;
    //下位秤 校准值
    private int lowerCalibration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_weigh_parameter);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        initHeadTitleBar("传感器数值校准");
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
        setCanNotEditNoClick2(etUpWeighRatio);
        setCanNotEditNoClick2(etLowWeighRatio);

    }

    @Override
    public void initViewData() {
        mCompositeDisposable = new CompositeDisposable();
//        upWeighRatio = PdGoDbManager.getInstance().getMainBoardTable().upWeigherRatio;
//        lowWeighRatio = PdGoDbManager.getInstance().getMainBoardTable().lowWeigherRatio;
        etUpWeighRatio.setText(upperCalibration + "");
        etLowWeighRatio.setText(lowerCalibration + "");
        init();
//        tvUpWeight.setText("3156");
//        tvUpWeight.setText("1000");
//        sendToMainBoard(CommandDataHelper.getInstance().weightTareLowerCmdJson());
    }

    @OnClick({R.id.btn_up_peeled, R.id.btn_low_peeled, R.id.btn_up_low_peeled, R.id.layout_up_weigh_ratio, R.id.layout_low_weigh_ratio, R.id.et_up_weigh_ratio, R.id.et_low_weigh_ratio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_up_peeled:
                showZeroCalibDialog("upper");
//                sendToMainBoard(CommandDataHelper.getInstance().weightZeroCalibUpperCmdJson());
                break;
            case R.id.btn_low_peeled:
                showZeroCalibDialog("lower");
//                sendToMainBoard(CommandDataHelper.getInstance().weightZeroCalibLowerCmdJson());
                break;
            case R.id.btn_up_low_peeled:
                sendToMainBoard(CommandDataHelper.getInstance().weightTareAllCmdJson());
                break;
            case R.id.et_up_weigh_ratio:
            case R.id.layout_up_weigh_ratio:
                alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_UPPER);
                break;
            case R.id.et_low_weigh_ratio:
            case R.id.layout_low_weigh_ratio:
                alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_LOWER);
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
//                    Logger.d(result);
                    if (mType.equals(PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_UPPER)) {//上位秤 称量系数
                        etUpWeighRatio.setText(result);
                        sendToMainBoard(CommandDataHelper.getInstance().setUpperWeightCmdJson(Integer.parseInt(result)));
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_LOWER)) {//下位秤 称量系数
                        etLowWeighRatio.setText(result);
                        sendToMainBoard(CommandDataHelper.getInstance().setLowerWeightCmdJson(Integer.parseInt(result)));
                    }
                }
            }
        });
    }

    private void showZeroCalibDialog(final String id) {

        String tips = id.equals("upper") ? "是否上位称标零" : "是否下位称标零";

        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent(tips)
                .setBtnFirst("确定")
                .setBtnTwo("取消")
                .setFirstClickListener(new CommonDialog.OnCommonClickListener() {
                    @Override
                    public void onClick(CommonDialog mCommonDialog) {
                        if (id.equals("upper")) {
                            sendToMainBoard(CommandDataHelper.getInstance().weightZeroCalibUpperCmdJson());
                        } else {
                            sendToMainBoard(CommandDataHelper.getInstance().weightZeroCalibLowerCmdJson());
                        }
                        mCommonDialog.dismiss();
                    }
                })
                .setTwoClickListener(mCommonDialog -> mCommonDialog.dismiss());
        if (!WeighParameterActivity.this.isFinishing()) {
            dialog.show();
        }


    }
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
        mCompositeDisposable.add(disposableObserver);
    }
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDevice(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {
            MyApplication.currCmd = "";
            if (mReceiveDeviceBean != null) {
                tvUpWeight.setText(String.valueOf(mReceiveDeviceBean.upper));
                tvLowWeight.setText(String.valueOf(mReceiveDeviceBean.lower));
//                Log.e("皮重","上位称:"+PdproHelper.getInstance().getOtherParamBean().upper);
            }
        });
    }
    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_DEVICE_INFO)
    public void receiveDevice(String mSerialJson) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(mSerialJson, ReceiveDeviceBean.class);
        Log.e("传感器设置界面", "   --->接收设备信息：" + mSerialJson);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvUpWeight.setText(String.valueOf(mReceiveDeviceBean.upper ));
                tvLowWeight.setText(String.valueOf(mReceiveDeviceBean.lower ));
            }
        });
    }

    /**
     * 执行指令成功
     *
     * @param topic
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
    public void receiveCmdResultOk(String topic) {

        if (topic.contains(CommandSendConfig.METHOD_WEIGH_ZERO_CLEAR)) {//去皮

        } else if (topic.contains(CommandSendConfig.METHOD_WEIGH_ZERO_CALIB)) {//标零

        } else if (topic.contains(CommandSendConfig.METHOD_WEIGH_RANGE_CALIB)) {//标定

        }

    }

//    /**
//     * 单片机返回指令不执行
//     *
//     * @param bean
//     */
//    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_ERR)
//    public void receiveCmdResultErr(ReceiveResultDataBean bean) {
//        String topic = bean.result.topic;
//        if (topic.contains(CommandSendConfig.METHOD_WEIGH_ZERO_CLEAR)) {//去皮
//
//        } else if (topic.contains(CommandSendConfig.METHOD_WEIGH_ZERO_CALIB)) {//标零
//
//        } else if (topic.contains(CommandSendConfig.METHOD_WEIGH_RANGE_CALIB)) {//标定
//
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
        mCompositeDisposable.clear();
    }

    @Override
    public void notifyByThemeChanged() {

    }

}
