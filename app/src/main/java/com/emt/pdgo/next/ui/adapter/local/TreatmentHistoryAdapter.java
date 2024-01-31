package com.emt.pdgo.next.ui.adapter.local;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.data.local.TreatmentHistory;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class TreatmentHistoryAdapter extends BaseQuickAdapter<TreatmentHistory, BaseViewHolder> {

    public TreatmentHistoryAdapter(@Nullable List<TreatmentHistory> data) {
        super(R.layout.his_pd_recyclerview,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TreatmentHistory item) {
        helper.setText(R.id.tvCycle, item.cycle + "");//周期
        helper.setText(R.id.tvPre, item.perfusionVolume + "");//灌注量
        helper.setText(R.id.preTime, item.perfusionTime + "");//灌注时间
        helper.setText(R.id.auFv, item.auFvVol + "");//辅助冲洗量
        helper.setText(R.id.abdAfter, item.abdAfterVol + "");//灌注后\n留腹量
        helper.setText(R.id.abdTime, item.waitingTime + "");//留腹\n时间
        helper.setText(R.id.drainTv, item.drainTvVol + "");//引流\n目标值
        helper.setText(R.id.drainage, item.drainVolume+"");//引流量
        helper.setText(R.id.drainTime, item.drainTime+"");//引流\n时间
        helper.setText(R.id.remain, item.remain+"");//预估腹腔\n剩余液体

        int offset = helper.getLayoutPosition();
        if (offset % 2 != 0) {
            helper.getView(R.id.layout_item).setBackgroundColor(0xffffffff);
        } else {
            helper.getView(R.id.layout_item).setBackgroundColor(0xfff9f9f9);
        }
    }
}
