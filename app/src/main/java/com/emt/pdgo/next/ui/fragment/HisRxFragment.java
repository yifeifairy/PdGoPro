package com.emt.pdgo.next.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.HisPdListBean;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.adapter.HisRxAdapter;
import com.emt.pdgo.next.ui.adapter.local.HisRxLocalAdapter;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HisRxFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private List<HisPdListBean.RowsDTO> pdListBeans;
    private List<HisPdListBean.RowsDTO> rowsDTOS;
    private HisRxAdapter hisRxAdapter;

    private HisRxLocalAdapter hisRxLocalAdapter;
    private int curPage = 1;
    private int maxPage;
    private boolean haveNet;

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @BindView(R.id.ivPrePage)
    ImageView ivPrePage;
    @BindView(R.id.ivNextPage)
    ImageView ivNextPage;

    @BindView(R.id.tvCurrPage)
    TextView tvCurrPage;

    private TreatmentFragmentActivity fragmentActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_his_rx, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void notifyByThemeChanged() {

    }

    private void initView() {
        if (checkConnectNetwork(Objects.requireNonNull(getContext()))) {
            haveNet = true;
            initRecyclerView();
            getHisRx();
        } else {
            haveNet = false;
            initRecyclerLocal();
        }

        ivPrePage.setOnClickListener(v -> {
            if (haveNet) {
                if (curPage > 1) {
                    curPage -- ;
                    getHisRx();
                }
            }
        });
        ivNextPage.setOnClickListener(v -> {
            if (haveNet && curPage <= maxPage) {
                curPage ++;
                getHisRx();
            }
        });
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentActivity = (TreatmentFragmentActivity) getActivity();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerLocal() {
        if (hisRxLocalAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            hisRxLocalAdapter = new HisRxLocalAdapter(getLocalRx());
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(hisRxLocalAdapter);
        } else {
            hisRxLocalAdapter.notifyDataSetChanged();
        }
    }

    private List<RxEntity> getLocalRx() {
        return EmtDataBase
                .getInstance(getActivity())
                .getRxDao()
                .getRxList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        if (hisRxAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            hisRxAdapter = new HisRxAdapter(pdListBeans);
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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

                        }
                    });
        }
//        Subscription subscription = APIServiceManage.getInstance().getHisRx(PdproHelper.getInstance().getUid(),curPage+"")
//                .subscribe(fragmentActivity.newSubscriber((Action1<MyResponse<HisPdListBean>>) bean-> {
//                    if (bean.getData() != null) {
//                        if (bean.getStatus().equals("200")) {
//                            if ((bean.data.getTotal() % 10) == 0 ) {
//                                maxPage = bean.data.getTotal() / 10;
//                            } else {
//                                maxPage = bean.data.getTotal() / 10 + 1;
//                            }
//                            tvCurrPage.setText(curPage+"/"+maxPage +"页");
//                            pdListBeans.clear();
//                            if (bean.getData().getRows() != null && bean.getData().getRows().size() != 0) {
//                                rowsDTOS = bean.data.getRows();
//                                pdListBeans.addAll(rowsDTOS);
//                                hisRxAdapter.setNewData(pdListBeans);
//                            }
//                        }
//                    }
//                } ));
//        fragmentActivity.mCompositeSubscription.add(subscription);
    }

}