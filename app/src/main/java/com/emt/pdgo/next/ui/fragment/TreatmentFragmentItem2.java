package com.emt.pdgo.next.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.data.bean.PdData;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.adapter.TreatmentHistoricalDataAdapter;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.fragment
 * @ClassName: TreatmentFragmentItem1
 * @Description: 治疗主界面
 * @Author: chenjh
 * @CreateDate: 2019/12/9 10:40 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/9 10:40 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class TreatmentFragmentItem2 extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_total_perfusion_volume)
    TextView tvTotalPerfusionVolume;
    @BindView(R.id.tv_total_drain_volume)
    TextView tvTotalDrainVolume;
    @BindView(R.id.tv_curr_ultrafiltration_volume)
    TextView tvCurrUltrafiltrationVolume;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    public TreatmentHistoricalDataAdapter mAdapter;

    public TreatmentFragmentActivity mActivity;

    private int totalDrainVolume;    //累计引流量
    /***** 总灌注量  灌注量  即治疗量  *******/
    private int totalPerfusionVolume;//累计灌注量
    /**** 总超滤量 = 引流量-治疗量 = 总引流量-总灌注量 ***/
    private int mUltrafiltrationVolume;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (TreatmentFragmentActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatment_item2, container, false);

        unbinder = ButterKnife.bind(this, view);
        initView();
        initViewData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {

    }

    private void initViewData() {
        if (null != mActivity) {
//            mActivity.initTreatmentHistory();
            if (mActivity.pdEntityDataList != null) {
                setHistoricalData(mActivity.pdEntityDataList);
            }
        }
    }

    public void setHistoricalData(List<PdData.PdEntityData> pdEntityDataList) {
        try {
            if (mAdapter == null) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mAdapter = new TreatmentHistoricalDataAdapter(R.layout.item_treatment_historical_data2, pdEntityDataList);
                mRecyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
                //添加Android自带的分割线
                mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
            totalDrainVolume = 0;
            totalPerfusionVolume = 0;
            for (int i = 0; i < pdEntityDataList.size(); i++) {
                PdData.PdEntityData mBean = pdEntityDataList.get(i);
                totalDrainVolume += mBean.getDrainage();
                totalPerfusionVolume += mBean.getPreVol();
            }
            if (MyApplication.apdMode == 1) {
                if (pdEntityDataList.size() > 0) {
                    if (mActivity.ipdBean.abdomenRetainingVolumeFinally == 0) {
                        mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume - pdEntityDataList.get(0).getDrainage();
                    } else {
                        mUltrafiltrationVolume = totalDrainVolume - totalPerfusionVolume - pdEntityDataList.get(0).getDrainage()
                                + pdEntityDataList.get(mActivity.currCycle).getPreVol();
                    }
                }
            }
            tvTotalPerfusionVolume.setText(String.valueOf(totalPerfusionVolume));
            tvTotalDrainVolume.setText(String.valueOf(totalDrainVolume));
            tvCurrUltrafiltrationVolume.setText(String.valueOf(mUltrafiltrationVolume));
            //本次治疗总超滤量= 总引流量 - 总灌注量 - 上次最末留腹量 + 本次最末留腹量（）
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void notifyByThemeChanged() {

    }

}
