package com.emt.pdgo.next.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.data.entity.TreatmentHistoryBean;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.pdp.rmmit.pdp.R;

import java.util.List;

/**
 * Created by chenjh on 2020/1/13.
 */

public class TreatmentHistoricalDataAdapter extends BaseQuickAdapter<TreatmentHistoryBean, BaseViewHolder> {


    public TreatmentHistoricalDataAdapter(int layoutResId, List<TreatmentHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TreatmentHistoryBean item) {
        helper.setText(R.id.tv_item1, item.cycle + "");//周期
        helper.setText(R.id.tv_item2, item.perfusionVolume + "");//灌注量
        helper.setText(R.id.tv_item3, item.rinsePerfusionVolume + "");//辅助冲洗量
        helper.setText(R.id.tv_item4, item.waitingVolume + "");//灌注后留腹量
        helper.setText(R.id.tv_item5, item.drainTargetVolume + "");//引流\n目标值
        helper.setText(R.id.tv_item6, item.drainVolume + "");//引流量
        helper.setText(R.id.tv_item7, item.predictedResidualLiquidVolume + "");//预估腹腔\n剩余液体
        helper.setText(R.id.tv_item8, EmtTimeUil.getTime(item.perfusionTime));//灌注\n时间
        helper.setText(R.id.tv_item9, EmtTimeUil.getTime(item.waitingTime));//留腹\n时间
        helper.setText(R.id.tv_item10, EmtTimeUil.getTime(item.drainTime));//引流\n时间

        int offset = helper.getLayoutPosition();
        if (offset % 2 != 0) {
            helper.getView(R.id.layout_item).setBackgroundColor(0xffffffff);
        } else {
            helper.getView(R.id.layout_item).setBackgroundColor(0xfff9f9f9);
        }
    }


}

