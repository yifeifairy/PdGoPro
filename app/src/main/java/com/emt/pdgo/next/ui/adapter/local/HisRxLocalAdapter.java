package com.emt.pdgo.next.ui.adapter.local;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class HisRxLocalAdapter extends BaseQuickAdapter<RxEntity, BaseViewHolder> {

    public HisRxLocalAdapter(@Nullable List<RxEntity> data) {
        super(R.layout.item_rx_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RxEntity item) {
        helper.setText(R.id.tv_item1, item.time);
        helper.setText(R.id.tv_item2, item.perVol + "");
        helper.setText(R.id.tv_item8, item.perCycleVol + "");
        helper.setText(R.id.tv_item9, item.treatCycle + "");
        helper.setText(R.id.tv_item6, item.firstPerVol + "");
        helper.setText(R.id.tv_item10, item.abdTime + "");
        helper.setText(R.id.tv_item7, item.endAbdVol + "");
        helper.setText(R.id.tv_item12, item.lastTimeAbdVol+"");
        helper.setText(R.id.tv_item13, item.ult+"");
        helper.setText(R.id.tv_item14, item.ulTreatTime+"").addOnClickListener(R.id.applyBtn);;

        int offset = helper.getLayoutPosition();
        if (offset % 2 != 0) {
            helper.getView(R.id.layout_item).setBackgroundColor(0xffffffff);
        } else {
            helper.getView(R.id.layout_item).setBackgroundColor(0xfff9f9f9);
        }
    }
}
