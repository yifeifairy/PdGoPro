package com.emt.pdgo.next.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.net.bean.HisPdListBean;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class HisRxAdapter extends BaseQuickAdapter<HisPdListBean.RowsDTO, BaseViewHolder> {
    public HisRxAdapter(@Nullable List<HisPdListBean.RowsDTO> data) {
        super(R.layout.item_rx_data, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, HisPdListBean.RowsDTO item) {
        helper.setText(R.id.tv_item1, item.getCreateTime());
        helper.setText(R.id.tv_item2, item.getIcodextrinTotal() + "");
        helper.setText(R.id.tv_item8, item.getInFlowCycle() + "");
        helper.setText(R.id.tv_item9, item.getCycle() + "");
        helper.setText(R.id.tv_item6, item.getFirstInFlow() + "");
        helper.setText(R.id.tv_item10, item.getRetentionTime() + "");
        helper.setText(R.id.tv_item7, item.getLastRetention() + "");
        helper.setText(R.id.tv_item12, item.getAgoRetention()+"");
        helper.setText(R.id.tv_item13, item.getPredictUlt()+"");
        helper.setText(R.id.tv_item14, item.getTreatTime()+"");

        int offset = helper.getLayoutPosition();
        if (offset % 2 != 0) {
            helper.getView(R.id.layout_item).setBackgroundColor(0xffffffff);
        } else {
            helper.getView(R.id.layout_item).setBackgroundColor(0xfff9f9f9);
        }
    }
}
