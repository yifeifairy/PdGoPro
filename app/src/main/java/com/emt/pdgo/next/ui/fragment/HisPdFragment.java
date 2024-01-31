//package com.emt.pdgo.next.ui.fragment;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.emt.pdgo.next.common.PdproHelper;
//import com.emt.pdgo.next.database.ext.PdInfoExt;
//import com.emt.pdgo.next.net.RetrofitUtil;
//import com.emt.pdgo.next.net.bean.HisTreatBean;
//import com.emt.pdgo.next.net.bean.MyResponse;
//import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
//import com.emt.pdgo.next.ui.adapter.HisPdAdapter;
//import com.emt.pdgo.next.ui.base.BaseFragment;
//import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
//import com.pdp.rmmit.pdp.R;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class HisPdFragment extends BaseFragment {
//
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerView;
//
//    private List<HisTreatBean.RowsDTO.InfoDTO> hisTreatBeans;
//    private List<HisTreatBean.RowsDTO.InfoDTO> infoDTOS;
//    private HisPdAdapter hisPdAdapter;
//    public TreatmentFragmentActivity mActivity;
//
////    private HisPdLocalAdapter hisPdLocalAdapter;
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mActivity = (TreatmentFragmentActivity) getActivity();
//    }
//    Unbinder unbinder;
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    @BindView(R.id.tv_total_drain_volume)
//    TextView tv_total_drain_volume;
//    @BindView(R.id.tv_total_perfusion_volume)
//    TextView tv_total_perfusion_volume;
//    @BindView(R.id.tv_curr_ultrafiltration_volume)
//    TextView tv_curr_ultrafiltration_volume;
//
//    @BindView(R.id.tvStartTime)
//    TextView tvStartTime;
//    @BindView(R.id.tvEndTime)
//    TextView tvEndTime;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_his_pd, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        initView();
//        return view;
//    }
//
//    @Override
//    public void notifyByThemeChanged() {
//
//    }
//    @BindView(R.id.ivPrePage)
//    ImageView ivPrePage;
//    @BindView(R.id.ivNextPage)
//    ImageView ivNextPage;
//    private boolean haveNet;
//    private void initView() {
//        if (checkConnectNetwork(getActivity())) {
//            initRecyclerview();
//            haveNet = true;
//            getPd(page);
//        } else {
//            haveNet = false;
//            if (null != mActivity) {
//                mActivity.initTreatmentHistory();
////                initLocalRv();
//            }
//        }
//
//        ivPrePage.setOnClickListener(v -> {
//            if (haveNet) {
//                if (page > 1) {
//                    page -- ;
//                    getPd(page);
//                }
//            } else {
////                initLocalRv();
//            }
//        });
//        ivNextPage.setOnClickListener(v -> {
//            if (haveNet) {
//                page ++;
//                Log.e("历史治疗界面","page--"+page);
//                getPd(page);
//            } else {
//                page ++;
////                initLocalRv();
//            }
//        });
//
//    }
//
////    private HisPdLocalAdapter localAdapter;
////    private List<PdInfoEntity> getPdInfo(int page) {
////        return EmtDataBase.getInstance(mActivity).getPdInfoDao()
////                .getPdInfoList(page);
////    }
//    @SuppressLint("NotifyDataSetChanged")
////    private void initLocalRv() {
////        if (localAdapter == null) {
////            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
////            localAdapter = new HisPdLocalAdapter(getPdInfo(page));
////            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
////            //添加Android自带的分割线
////            recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
////            recyclerView.setAdapter(localAdapter);
////        } else {
////            localAdapter.notifyDataSetChanged();
////        }
////    }
//
//    private List<PdInfoExt> pdInfoExtList;
//    private List<PdInfoExt> getPdExt() {
//        pdInfoExtList = new ArrayList<>();
////        if (getPdInfo(page).size() != 0) {
////            for (int i = 0; i < getPdInfo(page).size(); i++) {
////                Log.e("历史数据","cycle--"+getPdInfo(page).get(i).cycle);
////                List<String> cycle = Arrays.asList(getPdInfo(page).get(i).cycle.split(","));
////                List<String> preVol = Arrays.asList(getPdInfo(page).get(i).preVol.split(","));
////                List<String> preTime = Arrays.asList(getPdInfo(page).get(i).preTime.split(","));
////                List<String> auFvVol = Arrays.asList(getPdInfo(page).get(i).auFvVol.split(","));
////                List<String> abdAfterVol = Arrays.asList(getPdInfo(page).get(i).abdAfterVol.split(","));
////                List<String> abdTime = Arrays.asList(getPdInfo(page).get(i).abdTime.split(","));
////                List<String> drainTvVol = Arrays.asList(getPdInfo(page).get(i).drainTvVol.split(","));
////                List<String> drainage = Arrays.asList(getPdInfo(page).get(i).drainage.split(","));
////                List<String> drainTime = Arrays.asList(getPdInfo(page).get(i).drainTime.split(","));
////                List<String> remain = Arrays.asList(getPdInfo(page).get(i).remain.split(","));
////                for (int j = 0; j < cycle.size(); i++) {
////                    PdInfoExt infoExt = new PdInfoExt();
////                    infoExt.cycle = cycle.get(j);
////                    infoExt.preVol = preVol.get(j);
////                    infoExt.preTime = preTime.get(j);
////                    infoExt.auFvVol = auFvVol.get(j);
////                    infoExt.abdAfterVol = abdAfterVol.get(j);
////                    infoExt.abdTime = abdTime.get(j);
////                    infoExt.drainTvVol = drainTvVol.get(j);
////                    infoExt.drainage = drainage.get(j);
////                    infoExt.drainTime = drainTime.get(j);
////                    infoExt.remain = remain.get(j);
////                    pdInfoExtList.add(infoExt);
////                }
////            }
////        }
//        return pdInfoExtList;
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void initRecyclerview() {
//        if (hisPdAdapter == null) {
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            hisPdAdapter = new HisPdAdapter(hisTreatBeans);
//            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
//            //添加Android自带的分割线
//            recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));
//            recyclerView.setAdapter(hisPdAdapter);
//        } else {
//            hisPdAdapter.notifyDataSetChanged();
//        }
//
//
//    }
//    public int page = 1;
//    private int limit = 10;
//    private int total;
//    public void getPd(int page) {
//        hisTreatBeans = new ArrayList<>();
////        hisTreatBeans = new ArrayList<>();
//        RetrofitUtil.getService().getApdRecordData(PdproHelper.getInstance().getMachineSN(), 1, limit).enqueue(new Callback<MyResponse<HisTreatBean>>() {
//            @Override
//            public void onResponse(Call<MyResponse<HisTreatBean>> call, Response<MyResponse<HisTreatBean>> response) {
//                if (response.body() != null) {
//                    if (response.body().data != null) {
//                        if (response.body().getData().getRows() != null && response.body().getData().getRows().size() != 0) {
//                            total = response.body().getData().getTotal();
//                            tvStartTime.setText(response.body().getData().getRows().get(page-1).getStartTime());
//                            tvEndTime.setText(response.body().getData().getRows().get(page-1).getEndTime());
//                            hisTreatBeans.clear();
//                            infoDTOS = response.body().getData().getRows().get(page-1).getInfo();
//                            tv_total_drain_volume.setText(response.body().getData().getRows().get(page-1).getDrainage());
//                            tv_total_perfusion_volume.setText(response.body().getData().getRows().get(page-1).getInFlow());
//                            tv_curr_ultrafiltration_volume.setText(response.body().getData().getRows().get(page-1).getUltrafiltration());
//                            hisTreatBeans.addAll(infoDTOS);
//                            hisPdAdapter.setNewData(infoDTOS);
////                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyResponse<HisTreatBean>> call, Throwable t) {
//
//            }
//        });
////        Subscription subscription = APIServiceManage.getInstance().getApdRecordData(PdproHelper.getInstance().getMachineSN(), 1, limit)
////                .subscribe(mActivity.newSubscriber((Action1<MyResponse<HisTreatBean>>) mBean -> {
////                    if (mBean.getData() != null) {
//////                        if (mBean.getStatus() == 200) {
////                        if (mBean.getData().getRows() != null && mBean.getData().getRows().size() != 0) {
////                            total = mBean.getData().getTotal();
////                            tvStartTime.setText(mBean.getData().getRows().get(page-1).getStartTime());
////                            tvEndTime.setText(mBean.getData().getRows().get(page-1).getEndTime());
////                            hisTreatBeans.clear();
////                            infoDTOS = mBean.getData().getRows().get(page-1).getInfo();
////                            tv_total_drain_volume.setText(mBean.getData().getRows().get(page-1).getDrainage());
////                            tv_total_perfusion_volume.setText(mBean.getData().getRows().get(page-1).getInFlow());
////                            tv_curr_ultrafiltration_volume.setText(mBean.getData().getRows().get(page-1).getUltrafiltration());
////                            hisTreatBeans.addAll(infoDTOS);
////                            hisPdAdapter.setNewData(infoDTOS);
//////                            }
////                        }
////                    }
////                }));
////        mActivity.mCompositeSubscription.add(subscription);
//    }
//
//
//}