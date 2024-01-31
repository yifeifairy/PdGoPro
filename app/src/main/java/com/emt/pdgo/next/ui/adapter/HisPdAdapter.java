package com.emt.pdgo.next.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.net.bean.HisTreatBean;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class HisPdAdapter extends BaseQuickAdapter<HisTreatBean.RowsDTO.InfoDTO, BaseViewHolder> {

    public HisPdAdapter(@Nullable List<HisTreatBean.RowsDTO.InfoDTO> data) {
        super(R.layout.his_pd_recyclerview, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HisTreatBean.RowsDTO.InfoDTO item) {
        helper.setText(R.id.tvCycle, item.getCurTime() + "");//周期
        helper.setText(R.id.tvPre, item.getInFlowItem() + "");//灌注量
        helper.setText(R.id.preTime, item.getInFlowTimeItem() + "");//灌注时间
        helper.setText(R.id.auFv, item.getAuxItems() + "");//辅助冲洗量
        helper.setText(R.id.abdAfter, item.getAbdominalItems() + "");//灌注后\n留腹量
        helper.setText(R.id.abdTime, item.getLeaveWombTimeItems() + "");//留腹\n时间
        helper.setText(R.id.drainTv, item.getDrainageTargetItems() + "");//引流\n目标值
        helper.setText(R.id.drainage, item.getDrainageItems());//引流量
        helper.setText(R.id.drainTime, item.getExhaustTimeItems());//引流\n时间
        helper.setText(R.id.remain, item.getEstimatedItems());//预估腹腔\n剩余液体

        int offset = helper.getLayoutPosition();
        if (offset % 2 != 0) {
            helper.getView(R.id.layout_item).setBackgroundColor(0xffffffff);
        } else {
            helper.getView(R.id.layout_item).setBackgroundColor(0xfff9f9f9);
        }
    }
}
