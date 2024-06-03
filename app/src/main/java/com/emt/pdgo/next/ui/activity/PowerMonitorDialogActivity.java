package com.emt.pdgo.next.ui.activity;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.pdp.rmmit.pdp.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PowerMonitorDialogActivity extends BaseActivity {

    @BindView(R.id.btn_first)
    Button btnFirst;

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    @BindView(R.id.tv_countdown)
    TextView tvCountdown;

    private CompositeDisposable mCompositeDisposable;
    private Disposable countDownDisposable;
    private int currCountdown = 31;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_power_monitor_dialog);
        ButterKnife.bind(this);
        RxBus.get().register(this);
//        Window win = this.getWindow();
//        win.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.gravity = Gravity.CENTER;
////        lp.alpha=0.6f;
//        win.setAttributes(lp);
        //取消倒计时按钮
        if (MyApplication.state != 1) {
            sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group1"));
            sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group2"),500);
//                speak("请取出卡匣,关闭所有管夹");
            sendCommandInterval(CommandDataHelper.getInstance().isValveOpen(true,"group3"),1000);
        }
    }
    @Override
    public void registerEvents() {
    }

    @Override
    public void initViewData() {
        if (!MyApplication.treatmentRunning && mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
            startLoopCountDown();
        }
        runOnUiThread(()-> {
//            saveFaultCodeLocal("电源断开");
        });

    }

    @OnLongClick({R.id.btn_first})
    public boolean onViewLongClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_first:
//                sendToMainBoard(CommandDataHelper.getInstance().setPipecartCmdJson("pipecart/install"));
                finishPowerMonitorDialogActivity();
                break;
        }

        return false;
    }

    public void finishPowerMonitorDialogActivity() {
        ////{"request":{"method":"battery/off","params":{}},"sign":"b9bae37f52c2090f22dd25721cb7f8e1"}
        String mCommand = CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BATTERY_OFF);
        sendToMainBoard(mCommand);
        finish();
    }

    /**
     * 执行指令成功
     *
     * @param topic
     */
    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
    public void receiveCmdResultOk(String topic) {
        //{"publish":{"topic":"pipecart/install","msg":"ready","data":{}},"sign":"7c1093f2df698d9376ffa556a88dee01"}
        //安装管路数据: {"publish":{"topic":"pipecart/finish","msg":"success","data":{}},"sign":"a1919ef64ddb0d28310230783dc6e69a"}

    }

    /**
     * 开始留腹倒计时
     */
    private void startLoopCountDown() {
        countDownDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(currCountdown)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        currCountdown--;
//                        Log.e("startLoopCountDown", "currWaitingTime:" + currWaitingTime);
                        runOnUiThread(() -> {
                            tvCountdown.setText(" （" + currCountdown + "秒后自动关机）");
                        }

                        );
                    }
                }, throwable -> {

                }, () -> {
                    finishPowerMonitorDialogActivity();
                    //complete
                    Log.e("startLoopCountDown", "倒计时完成，发送关机指令切断电池供电");
                    currCountdown = 0;
//                    runOnUiThread(this::finishPowerMonitorDialogActivity);
                });
        mCompositeDisposable.add(countDownDisposable);
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_RECEIVE_AC_POWER_ON)
    public void receiveAcPowerOn(String mSerialJson) {
        runOnUiThread(this::finish);
        Log.e("receiveAcPowerOn","receiveAcPowerOn");
    }

    /**
     * 倒计时结束
     */
    private void stopLoopCountDown() {
        Log.e("stopLoopCountDown", "倒计时结束");
        if (countDownDisposable != null && !countDownDisposable.isDisposed()) {
            countDownDisposable.dispose();
            countDownDisposable = null;
            mCompositeDisposable.clear();
        }
    }


    @Override
    public void notifyByThemeChanged() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoopCountDown();
    }
}