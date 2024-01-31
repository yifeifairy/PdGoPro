package com.emt.pdgo.next.ui.adapter.local;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.database.entity.PdEntity;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class HisPdLocalAdapter extends BaseQuickAdapter<PdEntity.PdInfoEntity, BaseViewHolder> {

    public HisPdLocalAdapter(@Nullable List<PdEntity.PdInfoEntity> data) {
        super(R.layout.his_pd_recyclerview, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PdEntity.PdInfoEntity item) {

        helper.setText(R.id.tvCycle, item.cycle + "");//周期
        helper.setText(R.id.tvPre, item.preVol + "");//灌注量
        helper.setText(R.id.preTime, item.preTime + "");//灌注时间
        helper.setText(R.id.auFv, item.auFvVol + "");//辅助冲洗量
        helper.setText(R.id.abdAfter, item.abdAfterVol + "");//灌注后\n留腹量
        helper.setText(R.id.abdTime, item.abdTime + "");//留腹\n时间
        helper.setText(R.id.drainTv, item.drainTvVol + "");//引流\n目标值
        helper.setText(R.id.drainage, String.valueOf(item.drainage));//引流量
        helper.setText(R.id.drainTime, String.valueOf(item.drainTime));//引流\n时间
        helper.setText(R.id.remain, String.valueOf(item.remain));//预估腹腔\n剩余液体

        int offset = helper.getLayoutPosition();
        if (offset % 2 != 0) {
            helper.getView(R.id.layout_item).setBackgroundColor(0xffffffff);
        } else {
            helper.getView(R.id.layout_item).setBackgroundColor(0xfff9f9f9);
        }
    }
}
