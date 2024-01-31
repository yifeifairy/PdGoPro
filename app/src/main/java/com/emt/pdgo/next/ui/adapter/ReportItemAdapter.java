package com.emt.pdgo.next.ui.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pdp.rmmit.pdp.R;
import com.emt.pdgo.next.data.entity.ReportItem;

import java.util.List;

/**
 *
 * @ProjectName:
 * @Package:        com.emt.pdgo.next.ui.adapter
 * @ClassName:      ReportItemAdapter
 * @Description:    自检模块的Adapter
 * @Author:         chenjh
 * @CreateDate:     2019/11/14 4:07 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/11/14 4:07 PM
 * @UpdateRemark:   更新内容
 * @Version:        1.0
 */
public class ReportItemAdapter extends BaseQuickAdapter<ReportItem, BaseViewHolder> {


    public ReportItemAdapter(@Nullable List<ReportItem> data) {
        super(R.layout.item_report, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportItem item) {
        helper.setText(R.id.tv_module, item.module);
        helper.setTextColor(R.id.tv_module, item.isNormal ? 0xff464A6A:0xffFF4753);
        helper.setBackgroundRes(R.id.iv_status, item.isNormal ? R.drawable.icon_right_day :R.drawable.icon_error);
    }

}
