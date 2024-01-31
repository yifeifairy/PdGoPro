package com.emt.pdgo.next.ui.activity;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.net.APIServiceManage;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.QRCodeUtil;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ScanCodeLoginActivity extends BaseActivity {

    @BindView(R.id.iv_login_qrcode)
    ImageView ivLoginQrcode;

    @BindView(R.id.btn_engineer)
    Button btnEngineer;
    @BindView(R.id.btn_apd)
    Button btnApd;
    @BindView(R.id.btn_dpr)
    Button btnDpr;

    private String content = "扫码登录二维码内容";//"https://www.ckdcloud.com/login";//二维码内容
    private int width, height;//宽度，高度
    private String error_correction_level = "H";//容错率
    private String margin = "1";//空白边距
    private int color_black, color_white;//黑色色块，白色色块

    private Bitmap logoBitmap;//logo图片
    private Bitmap blackBitmap;//代替黑色色块的图片

    private Bitmap qrcode_bitmap;//生成的二维码

    private APIServiceManage apiServiceManage;

    private String url = "https://ejc.ckdcloud.com/api/ckdcloud/wx/getMachineQRCode";//正式线
//    private String url = "https://iot.ckdcloud.com/api/ckdcloud/wx/getMachineQRCode";//测试线

    private String machineSN;
//    private UserParameterBean mUserParameterBean;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
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
                if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式的密码
                    if (tempPwd.equals(result)) {
                        doGoTOActivity(EngineerSettingActivity.class);
                    }
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initAllViews();
//        registerEvents();
//        initViewData();
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
    public void initAllViews() {
        setContentView(R.layout.activity_scan_code_login);
        ButterKnife.bind(this);
        mCheckForLongPress1 = new CheckForLongPress1();
        width = 300;
        height = 300;
        color_white = Color.WHITE;
        color_black = Color.BLACK;
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
        ivLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                Log.d("onTouch", "action down");
                        isLongPressed = true;
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                        ivLogo.postDelayed(mCheckForLongPress1, 5000);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isLongPressed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongPressed = false;
//                Log.d("onTouch", "action up");
                        break;

                }
                return ScanCodeLoginActivity.super.onTouchEvent(motionEvent);
            }
        });
    }

    public void initViewData() {

//        mUserParameterBean = PdproHelper.getInstance().getUserParameterBean();
        logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        machineSN = PdproHelper.getInstance().getMachineSN();

        if (TextUtils.isEmpty(machineSN)) {
//            machineSN = "PN00001A";
//            machineSN = "PD000265";
            
            return;
        }
        Log.e("machineSN", machineSN);
        url = url + "?id=" + machineSN + "&url=pages/questionnaire/prepage/prepage";
        getMachineQRCode();


    }

    private void getMachineQRCode() {

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    InputStream inputStream = response.body().byteStream();//得到图片的流
                    qrcode_bitmap = BitmapFactory.decodeStream(inputStream);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivLoginQrcode.setImageBitmap(qrcode_bitmap);
                            handler.postDelayed(runnable, 5000);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void getMachineUserTemporary(String machineSn) {

        if (apiServiceManage == null) {
            apiServiceManage = new APIServiceManage();
        }
//        Log.e("getMachineUser", "获取设备绑定的用户信息:" + machineSn);
//        Subscription subscription = apiServiceManage.getMachineUserTemporary(machineSn)
//                .subscribe(newSubscriber(new Action1<MachineUserTemporaryBean>() {
//                    @Override
//                    public void call(MachineUserTemporaryBean result) {
//                        if (result != null) {
//                            if (result.isSuccess()) {
//                                Log.e("getMachineUser", "userName:" + result.info.userName + ",loginPhone:" + result.info.loginPhone);
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        handler.removeCallbacks(runnable);
//                                        UserInfoBean info = result.info;
//                                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.USER_INFO, info);//保存用户信息
//                                        Log.e("是否医院用户", PdproHelper.getInstance().getUserParameterBean().isHospital + "");
//                                        if (PdproHelper.getInstance().getUserParameterBean().isHospital) {//医院用户删除临时绑定关系
//                                            delTemporaryUser(machineSn);
//                                            Log.e("getMachineUser", "医院用户删除绑定关系");
//                                        } else {
//                                            Log.e("getMachineUser", "个人用户不删除绑定关系");
//                                        }
//                                    }
//                                });
//
//                            } else {
//                                handler.postDelayed(runnable, 5000);
//                            }
//                        } else {
//                            handler.postDelayed(runnable, 5000);
//                        }
//                    }
//                }));
//
//        mCompositeSubscription.add(subscription);
    }

    private void delTemporaryUser(String machineSn) {

        if (apiServiceManage == null) {
            apiServiceManage = new APIServiceManage();
        }

//        Subscription subscription = apiServiceManage.delTemporaryUser(machineSn)
//                .subscribe(newSubscriber(new Action1<String>() {
//                    @Override
//                    public void call(String result) {
//
//                    }
//                }));
//
//        mCompositeSubscription.add(subscription);
    }


    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            getMachineUserTemporary(machineSN);

        }
    };

    @Override
    public void notifyByThemeChanged() {

    }

    @OnClick({R.id.btn_skip_login, R.id.layout_setting, R.id.btn_apd, R.id.btn_engineer})
    public void onViewClicked(View view) {

        switch (view.getId()) {
//            case R.id.btn_skip_login://
//                doGoTOActivity(PreparationActivity.class);
//                break;
            case R.id.layout_setting://
                doGoTOActivity(SettingActivity.class);
                break;
            case R.id.btn_apd:
                doGoTOActivity(PreparationActivity.class);
                break;
            case R.id.btn_engineer:
                alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
                break;
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mUserParameterBean = PdproHelper.getInstance().getUserParameterBean();
//            }
//        });
//    }

    /**
     * 生成二维码并显示
     */
    private void generateQrcodeAndDisplay() {

        if (content.length() <= 0) {
            Toast.makeText(this, "你没有输入二维码内容哟！", Toast.LENGTH_SHORT).show();
            return;
        }
        qrcode_bitmap = QRCodeUtil.createQRCodeBitmap(content, width, height, "UTF-8",
                error_correction_level, margin, color_black, color_white, logoBitmap, 0.3F, blackBitmap);
        ivLoginQrcode.setImageBitmap(qrcode_bitmap);
    }

    public Bitmap stringtoBitmap(String string) {

//将字符串转换成Bitmap类型

        Bitmap bitmap = null;

        try {

            byte[] bitmapArray;

            bitmapArray = Base64.decode(string, Base64.DEFAULT);

            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return bitmap;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivLoginQrcode.setImageBitmap(null);
        QRCodeUtil.recycleImageView(ivLoginQrcode);
        if (null != logoBitmap && !logoBitmap.isRecycled()) {
            logoBitmap.recycle();
            logoBitmap = null;
        }
        if (null != qrcode_bitmap && !qrcode_bitmap.isRecycled()) {
            qrcode_bitmap.recycle();
            qrcode_bitmap = null;
        }
    }

    private class ReadTxtTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            AssetManager manager = getResources().getAssets();
            try {
                InputStream inputStream = manager.open("response.txt");
                InputStreamReader isr = new InputStreamReader(inputStream,
                        "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String length;
                while ((length = br.readLine()) != null) {
                    sb.append(length + "");
                }
                //关流
                br.close();
                isr.close();
                inputStream.close();

                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            content = result;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivLoginQrcode.setImageBitmap(stringtoBitmap(content));
                }
            });
        }
    }


}
