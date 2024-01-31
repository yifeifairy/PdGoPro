package com.emt.pdgo.next.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.HisPrescriptionBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.wifi.WifiActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response; 

public class TreatmentModeActivity extends BaseActivity {

    @BindView(R.id.dprLayout)
    RelativeLayout dprLayout;

    @BindView(R.id.apdLayout)
    RelativeLayout apdLayout;

    @BindView(R.id.settingLayout)
    LinearLayout settingLayout;

    private String msg = EmtConstant.ACTIVITY_TREAT_MODE;
    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_treatment_mode);
        ButterKnife.bind(this);
        mCheckForLongPress1 = new CheckForLongPress1();
        compositeDisposable = new CompositeDisposable();
//        getToken();
    }
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tvConnWifi)
    TextView tvConnWifi;
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
        timing();
//        getToken();
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
        if (!checkConnectNetwork(this)) {
            tvConnWifi.setVisibility(View.VISIBLE);
            APIServiceManage.getInstance().postApdCode("Z1042");
        } else {
            APIServiceManage.getInstance().postApdCode("Z1041");
            tvConnWifi.setVisibility(View.GONE);
        }
//        dprLayout.setVisibility(MyApplication.versionMode == 0 ? View.INVISIBLE: View.VISIBLE);
        tvConnWifi.setOnClickListener(v -> doGoTOActivity(WifiActivity.class));
//        getRemotePd();

        apdLayout.setOnClickListener(v -> {
            MyApplication.isDpr = false;
            doGoTOActivity(PrescriptionActivity.class);
            APIServiceManage.getInstance().postApdCode("Z1030");
        });
        settingLayout.setOnClickListener(v -> doGoTOActivity(SettingActivity.class));
        
        ivLogo.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                Log.d("onTouch", "action down");
                    isLongPressed = true;
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                    ivLogo.postDelayed(mCheckForLongPress1, 5000);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d("onTouch", "action move");
                    break;
                case MotionEvent.ACTION_UP:
                    isLongPressed = false;
                    Log.d("onTouch", "action up");
                    break;
//                Log.d("onTouch", "action up");

            }
            return true;
        });
        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(false,"group1"));
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group2"),1000);
        sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(false,"group3"),2000);

    }

    private CompositeDisposable compositeDisposable;
    private void timing() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    if (!checkConnectNetwork(TreatmentModeActivity.this)) {
                        tvConnWifi.setVisibility(View.VISIBLE);
                    } else {
                        tvConnWifi.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.interval(0, 500, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        compositeDisposable.add(disposableObserver);
    }

    private Gson gson = new Gson();
    private void getRemotePd() {
        RetrofitUtil.getService().getRemotePrescription(PdproHelper.getInstance().getMachineSN()).enqueue(new Callback<MyResponse<HisPrescriptionBean>>() {
            @Override
            public void onResponse(Call<MyResponse<HisPrescriptionBean>> call, Response<MyResponse<HisPrescriptionBean>> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().getData().getCode().equals("10000")) {
                            if (response.body().getData().getRcpInfo() != null) {
                                tvUsername.setText(response.body().data.getRcpInfo().getUserName());
                                CacheUtils.getInstance().getACache().put(PdGoConstConfig.UID, response.body().data.getRcpInfo().getPatientId() + "");
                            }
                        } else {
                            Log.e("模式选择","data--"+gson.toJson(response.body()));
                            saveFaultCodeLocal("获取远程处方数据,"+response.body().getData().getMsg());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<HisPrescriptionBean>> call, Throwable t) {
                Log.e("治疗模式", t.getMessage() + "--url--"+call.request().url());
                saveFaultCodeLocal("获取远程处方数据,"+t.getMessage());
            }
        });
    }

    private CheckForLongPress1 mCheckForLongPress1;
    private volatile boolean isLongPressed = false;
    private class CheckForLongPress1 implements Runnable {

        @Override
        public void run() {
            //5s之后，查看isLongPressed的变量值：
            if (isLongPressed) {//没有做up事件
                Log.e("长按", "5s的事件触发");
                alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
            } else {
                ivLogo.removeCallbacks(mCheckForLongPress1);
            }
        }
    }
    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
//                    Logger.d(result);
                Calendar calendar = Calendar.getInstance();//取得当前时间的年月日 时分秒

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                String mMonth = "";

                if (month >= 10) {
                    mMonth = "" + month;
                } else {
                    mMonth = "0" + month;
                }

                //123加上月份
                String tempPwd = "123" + mMonth;
                Log.e("长按", "tempPwd：" + tempPwd);
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式的密码
                    if (tempPwd.equals(result)) {
                        doGoTOActivity(EngineerSettingActivity.class);
                    }
                }
            }
        });
    }

    @Override
    public void initViewData() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.System.canWrite(TreatmentModeActivity.this)){
//
//            } else {
//                allowModifySettings();
//            }
//        }
    }

    private static final int REQUEST_CODE_WRITE_SETTINGS = 1000;

    private void allowModifySettings() {
        // Settings.System.canWrite(MainActivity.this)
        // 检测是否拥有写入系统 Settings 的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(TreatmentModeActivity.this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this,
                        android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("请开启修改屏幕亮度权限");
                builder.setMessage("请点击允许开启");
                // 拒绝, 无法修改
                builder.setNegativeButton("拒绝",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(TreatmentModeActivity.this,
                                                "您已拒绝修系统Setting的屏幕亮度权限", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                builder.setPositiveButton("去开启",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 打开允许修改Setting 权限的界面
                                Intent intent = new Intent(
                                        Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri
                                        .parse("package:"
                                                + getPackageName()));
                                startActivityForResult(intent,
                                        REQUEST_CODE_WRITE_SETTINGS);
                            }
                        });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Settings.System.canWrite方法检测授权结果
                if (Settings.System.canWrite(getApplicationContext())) {

                } else {
                    toastMessage( "您已拒绝修系统Setting的屏幕亮度权限");
                }
            }
        }
    }
    @BindView(R.id.custom_id_app_background)
    RelativeLayout mRLayoutAppBackground;
    @BindView(R.id.iv_preparation_upper_part_bg)
    ImageView upbIv;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.iv_pipeline_connect)
    ImageView ivPc;
    @BindView(R.id.iv_num_1)
    ImageView ivNum1;
    @BindView(R.id.tv_pipeline)
    TextView tvPl;
    @BindView(R.id.ivNum2)
    ImageView ivNum2;
    @BindView(R.id.iv_num_2)
    ImageView ivN2;
    @BindView(R.id.tv_apd)
    TextView tvApd;

    @Override
    public void notifyByThemeChanged() {
        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
        helper.setBackgroundResourceByAttr(mRLayoutAppBackground, R.attr.custom_attr_app_bg);
        helper.setBackgroundResourceByAttr(ivN2, R.attr.custom_attr_preparation_item_icon_num_02);
        helper.setBackgroundResourceByAttr(ivNum2, R.attr.custom_attr_preparation_item_icon_03);
        helper.setBackgroundResourceByAttr(ivNum1, R.attr.custom_attr_preparation_item_icon_num_01);
        helper.setBackgroundResourceByAttr(ivPc, R.attr.custom_attr_preparation_item_icon_04);
        helper.setBackgroundResourceByAttr(ivHead, R.attr.custom_attr_preparation_icon_head);
        helper.setBackgroundResourceByAttr(upbIv, R.attr.custom_attr_preparation_upper_part_bg);

        helper.setTextColorByAttr(tvPl, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(tvUsername, R.attr.custom_attr_preparation_user_text_color);
        helper.setTextColorByAttr(tvApd, R.attr.custom_attr_common_text_color);

        helper.setBackgroundResourceByAttr(dprLayout, R.attr.custom_attr_preparation_item_bg);
        helper.setBackgroundResourceByAttr(apdLayout, R.attr.custom_attr_preparation_item_bg);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
//        MyApplication.isPipecartInstall = false;
        compositeDisposable.clear();
    }
}