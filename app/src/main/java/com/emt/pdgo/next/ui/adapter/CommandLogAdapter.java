package com.emt.pdgo.next.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.data.CommandLogItem;
import com.pdp.rmmit.pdp.R;

import java.util.List;

/**
 * 指令日志适配器
 *
 * @author chenjh
 * @date 2019/4/16 11:11
 */
public class CommandLogAdapter extends BaseQuickAdapter<CommandLogItem, BaseViewHolder> {


    public CommandLogAdapter(int layoutResId) {
        super(layoutResId);
    }
    public CommandLogAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommandLogItem item) {
        helper.setText(R.id.tv_item_name, item.time);
        helper.setText(R.id.tv_item_value, item.command);
    }

}