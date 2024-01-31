package com.emt.pdgo.next.ui.activity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.HisPdListBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.adapter.HisRxAdapter;
import com.emt.pdgo.next.ui.adapter.local.HisRxLocalAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricalPrescriptionActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private List<HisPdListBean.RowsDTO> pdListBeans;
    private List<HisPdListBean.RowsDTO> rowsDTOS;
    private HisRxAdapter hisRxAdapter;

    private HisRxLocalAdapter hisRxLocalAdapter;

    private int curPage = 1;

    private int maxPage;

    private boolean haveNet;

    @BindView(R.id.ivPrePage)
    ImageView ivPrePage;
    @BindView(R.id.ivNextPage)
    ImageView ivNextPage;

    @BindView(R.id.tvCurrPage)
    TextView tvCurrPage;

    @Override
    public void notifyByThemeChanged() {

    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_historical_prescription);
        ButterKnife.bind(this);
        initHeadTitleBar("");
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
        ivPrePage.setOnClickListener(v -> {
            if (haveNet) {
                if (curPage > 1) {
                    curPage -- ;
                    getHisRx();
                }
            }
        });
        ivNextPage.setOnClickListener(v -> {
            if (haveNet && curPage < maxPage) {
                curPage ++;
                getHisRx();
            }
        });
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
        if (checkConnectNetwork(this)) {
            haveNet = true;
            initRecyclerView();
            getHisRx();
        } else {
            haveNet = false;
//            initRecyclerLocal();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerLocal() {
        tvCurrPage.setText(1+"/"+1 +"页");
        if (hisRxLocalAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            hisRxLocalAdapter = new HisRxLocalAdapter(getLocalRx());
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(hisRxLocalAdapter);
        } else {
            hisRxLocalAdapter.notifyDataSetChanged();
        }
    }

    private List<RxEntity> getLocalRx() {
        return EmtDataBase
                .getInstance(HistoricalPrescriptionActivity.this)
                .getRxDao()
                .getRxList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        if (hisRxAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            hisRxAdapter = new HisRxAdapter(pdListBeans);
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(hisRxAdapter);
        } else {
            hisRxAdapter.notifyDataSetChanged();
        }
    }
    private Gson gson = new Gson();
    private void getHisRx() {
        pdListBeans = new ArrayList<>();
        if (PdproHelper.getInstance().getUid() != null) {
            RetrofitUtil.getService().getHisRx(PdproHelper.getInstance().getUid(), curPage + "")
                    .enqueue(new Callback<MyResponse<HisPdListBean>>() {
                        @Override
                        public void onResponse(Call<MyResponse<HisPdListBean>> call, Response<MyResponse<HisPdListBean>> response) {
                            if (response.body() != null) {
//                            if (response.body().data != null) {
                                if (response.body().getStatus().equals("200")) {
                                    if (response.body().data != null) {
                                        if ((response.body().data.getTotal() % 10) == 0) {
                                            maxPage = response.body().data.getTotal() / 10;
                                        } else {
                                            maxPage = response.body().data.getTotal() / 10 + 1;
                                        }
                                        tvCurrPage.setText(curPage + "/" + maxPage + "页");
                                        pdListBeans.clear();
                                        if (response.body().getData().getRows() != null && response.body().getData().getRows().size() != 0) {
                                            rowsDTOS = response.body().data.getRows();
                                            pdListBeans.addAll(rowsDTOS);
                                            hisRxAdapter.setNewData(pdListBeans);
                                        }
                                    }
                                } else {
                                    Log.e("获取历史处方数据", "gson--" + gson.toJson(response.body()));
                                }
//                            }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse<HisPdListBean>> call, Throwable t) {
                            Log.e("历史处方数据", t.getMessage() + "--url--" + call.request().url());
                            saveFaultCodeLocal("历史处方数据," + t.getMessage());
                        }
                    });
        }
//        Subscription subscription = APIServiceManage.getInstance().getHisRx(PdproHelper.getInstance().getUid(),curPage+"")
//                        .subscribe(newSubscriber((Action1<MyResponse<HisPdListBean>>) bean-> {
//                            if (bean.getData() != null) {
//                                if (bean.getStatus().equals("200")) {
//                                    if ((bean.data.getTotal() % 10) == 0 ) {
//                                        maxPage = bean.data.getTotal() / 10;
//                                    } else {
//                                        maxPage = bean.data.getTotal() / 10 + 1;
//                                    }
//                                    tvCurrPage.setText(curPage+"/"+maxPage +"页");
//                                    pdListBeans.clear();
//                                    if (bean.getData().getRows() != null && bean.getData().getRows().size() != 0) {
//                                        rowsDTOS = bean.data.getRows();
//                                        pdListBeans.addAll(rowsDTOS);
//                                        hisRxAdapter.setNewData(pdListBeans);
//                                    }
//                                }
//                            }
//        } ));
//        mCompositeSubscription.add(subscription);
    }

}