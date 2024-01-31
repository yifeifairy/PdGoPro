package com.emt.pdgo.next.ui.activity.param;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.bean.upload.TreatmentDataUploadBean;
import com.emt.pdgo.next.net.bean.upload.TreatmentPrescriptionUploadBean;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadDataTestActivity extends BaseActivity {

    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.btn_upload)
    Button btnUpload;

    @BindView(R.id.tv_url)
    EditText tvUrl;

    @BindView(R.id.tv_sn)
    TextView tvSn;

    @BindView(R.id.token)
    Button btnToken;

    @BindView(R.id.receiveMsg)
    TextView receiveMsg;

    @BindView(R.id.btnStatus)
    Button btnStatus;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    private String token;
    private String url;

    // 上传治疗接口
    private String addApd = "/api/pro/treatmentRecord/addAPD";

    public TreatmentDataUploadBean mTreatmentDataUpload;

    public TreatmentPrescriptionUploadBean mTreatmentPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_upload_data_test);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        initHeadTitleBar("接口测试");
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                try {
                    hideKeyboard(v.getWindowToken());
                } catch (Exception e) {
                    Log.e("异常" , e.getMessage());
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
        btnToken.setOnClickListener(v -> {
            url = tvUrl.getText().toString().equals("") ? "ejc.ckdcloud.com":tvUrl.getText().toString();
            token();
        });
        btnConfirm.setOnClickListener(view -> {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.PDP_APP_SERVER_URL.toLowerCase(), "https://" + tvUrl.getText().toString()+"/api/");
        });
        btnStatus.setOnClickListener(v -> {
            url = tvUrl.getText().toString().equals("") ? "ejc.ckdcloud.com":tvUrl.getText().toString();
            Map<String, String> map = new HashMap<>();
            map.put("machineSn", machineSN);
            map.put("faultCode", "E0099");
            map.put("faultTime", EmtTimeUil.getTime());
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType,gson.toJson(map));
            Request request
                    = new Request.Builder()
                    .url("https://"+url+"/api/ckdcloud/wx/saveApdFaultCode")
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(e.getLocalizedMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try {
                        runOnUiThread(response.body().string());
                    } catch (Exception e) {
                        runOnUiThread(e.getLocalizedMessage());
                    }
                }
            });
        });

    }

    private String machineSN;
    @SuppressLint("SetTextI18n")
    @Override
    public void initViewData() {
        gson = new Gson();
        machineSN = PdproHelper.getInstance().getMachineSN();
        if (!TextUtils.isEmpty(machineSN)) {
            tvSn.setText(machineSN);
        } else {
            tvSn.setText("");
        }
    }

    private void token() {
        Map<String, String> map = new HashMap<>();
        map.put("machineSn", machineSN);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType,gson.toJson(map));
        Request request = new Request.Builder()
                .url("https://"+url+"/api/auth/jwt/machineSnAndPhoneToken")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                runOnUiThread(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                try {
                    runOnUiThread(response.body().string());
                } catch (Exception e) {
                    runOnUiThread(e.getLocalizedMessage());
                }
            }
        });
    }

    private void runOnUiThread(String msg) {
        runOnUiThread(()-> {
            receiveMsg.setText(String.format("返回的信息: %s", msg));
        });
    }

    @Override
    public void notifyByThemeChanged() {

    }

    @OnClick({R.id.btn_upload})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_upload:
                url = tvUrl.getText().toString().equals("") ? "ejc.ckdcloud.com":tvUrl.getText().toString();
                testUpload();
//                postApd();
                break;
        }
    }

    private void testUpload() {
        initTreatmentDataUploadBean();
        postApd();
    }
    private Gson gson;
    private void postApd() {
        String content = gson.toJson(mTreatmentDataUpload);
        Log.e("数据上报","请求头--"+content);
        RequestBody params = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                content);
        OkHttpClient client = new OkHttpClient();
        Request request;
        if (CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.TOKEN) == null) {
            request = new Request.Builder()
                    .url("https://" + url + addApd)
                    .post(params)
                    .build();
        } else {
            request = new Request.Builder()
                    .url("https://" + url + addApd)
                    .addHeader("Authorization", CacheUtils.getInstance().getACache().getAsString(PdGoConstConfig.TOKEN))
                    .post(params)
                    .build();
        }
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                runOnUiThread(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                try {
                    runOnUiThread(response.body().string());
                } catch (Exception e) {
                    runOnUiThread(e.getLocalizedMessage());
                }
            }
        });
    }

    /**
     * 初始化 测试上传资料数据
     */
    private void initTreatmentDataUploadBean() {
        mTreatmentDataUpload = new TreatmentDataUploadBean();

        mTreatmentPrescription = new TreatmentPrescriptionUploadBean();
        mTreatmentPrescription.phone = MyApplication.phone;
        mTreatmentPrescription.recommendedWeight = 60;
        mTreatmentPrescription.weightBeforeTreatment = 65;
        mTreatmentPrescription.SBPBeforeTreatment = 80;
        mTreatmentPrescription.DBPBeforeTreatment = 120;
        mTreatmentPrescription.weightAfterTreatment = 64;
        mTreatmentPrescription.SBPAfterTreatment = 75;
        mTreatmentPrescription.DBPAfterTreatment = 110;
        mTreatmentPrescription.fastingBloodGlucose = 4.3f;
        mTreatmentPrescription.urineOutput = 1200;
        mTreatmentPrescription.waterIntake = 1200;
        mTreatmentPrescription.totalPerfusionVolume = 12000;
        mTreatmentPrescription.totalUltrafiltrationVolume = 2000;
        mTreatmentPrescription.peritonealDialysisFluidTotal = 8000;
        mTreatmentPrescription.periodicities = 5;
        mTreatmentPrescription.firstPerfusionVolume = 2000;
        mTreatmentPrescription.perCyclePerfusionVolume = 1500;
        mTreatmentPrescription.abdomenRetainingVolumeFinally = 500;
        mTreatmentPrescription.abdomenRetainingTime = 1200;
        mTreatmentPrescription.abdomenRetainingVolumeLastTime = 500;
        mTreatmentPrescription.ultrafiltrationVolume = 1500;
        mTreatmentPrescription.equipmentUseTime = 30;
        mTreatmentPrescription.upWeightInitialValue = 2267;
        mTreatmentPrescription.lowWeightInitialValue = 3330;
        mTreatmentPrescription.plcId = "20220801";//
        mTreatmentPrescription.buildId = 2;
        mTreatmentPrescription.buildValue = "2022010201";//1.2.1.1
        mTreatmentDataUpload.machineSN = PdproHelper.getInstance().getMachineSN();
        mTreatmentDataUpload.totalInjectAmount = 8000;
        mTreatmentDataUpload.startTime = EmtTimeUil.getTime();
        mTreatmentDataUpload.endTime = EmtTimeUil.getTime();
        mTreatmentDataUpload.drainageTime = 23;
        mTreatmentDataUpload.ultrafiltration = 700;
        mTreatmentDataUpload.lastLeave = 1000;
        mTreatmentDataUpload.lastLeaveTime = 12;
        mTreatmentDataUpload.times = 5;
        mTreatmentDataUpload.cycle = "1,2,3,4,5";
        mTreatmentDataUpload.inFlow = "2000,1000,1000,1000,1000";
        mTreatmentDataUpload.inFlowTime = "21,13,11,14,15";
        mTreatmentDataUpload.leaveWombTime = "30,30,30,30,30";
        mTreatmentDataUpload.exhaustTime = "11,16,21,18,22";
        mTreatmentDataUpload.drainage = "1600,1400,950,860,1130,1760";
        mTreatmentDataUpload.auxiliaryFlushingVolume = "1600,1400,950,860,1130";
        mTreatmentDataUpload.abdominalVolumeAfterInflow = "1600,1400,950,860,1130";
        mTreatmentDataUpload.drainageTargetValue = "1600,1400,950,860,1130";
        mTreatmentDataUpload.estimatedResidualAbdominalFluid = "1600,1400,950,860,1130";
        mTreatmentDataUpload.treatmentPrescriptionUploadVo = mTreatmentPrescription;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCompositeSubscription!=null){
            mCompositeSubscription.clear();
        }
    }

}