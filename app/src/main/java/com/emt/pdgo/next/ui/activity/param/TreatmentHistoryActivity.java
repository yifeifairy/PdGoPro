package com.emt.pdgo.next.ui.activity.param;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.PdGoDbManager;
import com.emt.pdgo.next.data.local.CommandRecord;
import com.emt.pdgo.next.data.local.TreatmentHistory;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.adapter.local.TreatmentHistoryAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class TreatmentHistoryActivity extends BaseActivity {

    private MarioResourceHelper helper;

    private CompositeDisposable mCompositeDisposable;

    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.rv_history)
    RecyclerView rv_history;

    private  List<TreatmentHistory> treatmentHistories;
    private TreatmentHistoryAdapter adapter;

    @Override
    public void notifyByThemeChanged() {
        helper.setBackgroundResourceByAttr(mAppBackground, R.attr.custom_attr_app_bg);

//        helper.setBackgroundResourceByAttr(layoutSN, R.attr.custom_attr_input_box_bg);
//        helper.setTextColorByAttr(etMachineSN, R.attr.custom_attr_common_text_color);
//        helper.setTextColorByAttr(labelTv, R.attr.custom_attr_common_text_color);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_treatment_history);
        ButterKnife.bind(this);
//        initHeadTitleBar("治疗记录");
        initHeadTitleBar("治疗记录", "添加");

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
    }

    @Override
    public void initViewData() {
        helper = MarioResourceHelper.getInstance(getContext());
        mCompositeDisposable = new CompositeDisposable();
        getAllCommandRecord();
        getAllTreatmentHistory();
    }

    private void getAllTreatmentHistory() {
        Flowable flowable = PdGoDbManager.getInstance().getAllTreatmentHistory();
        Disposable disposable = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<Object>) o-> {
                    if (o != null) {
                        treatmentHistories = (List<TreatmentHistory>) o;
                        Log.e("历史", "getAllTreatmentHistory--" + treatmentHistories.size());
                        if (adapter == null) {
                            rv_history.setLayoutManager(new LinearLayoutManager(this));
                            adapter = new TreatmentHistoryAdapter(treatmentHistories);

                            rv_history.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
                            //添加Android自带的分割线
                            rv_history.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                            rv_history.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                } );
        mCompositeDisposable.add(disposable);
    }

    private void getAllCommandRecord() {
        Flowable flowable = PdGoDbManager.getInstance().getAllCommandRecord();
        Disposable subscription = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<Object>) o -> {

                    if (null != o) {
                        List<CommandRecord> CommandRecordList = (List<CommandRecord>) o;
                        if (CommandRecordList.size() > 0) {
                            for (int i = 0; i < CommandRecordList.size(); i++) {
                                Log.e("getAllCommandRecord", " " + CommandRecordList.get(i).command);
                            }
                        }
                    }
                });

        mCompositeDisposable.add(subscription);

    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:

                break;
        }
    }

}
