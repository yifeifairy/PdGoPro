package com.emt.pdgo.next.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.data.bean.MsgBean;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class MsgAdapter extends BaseQuickAdapter<MsgBean, BaseViewHolder> {

    public MsgAdapter(@Nullable List<MsgBean> data) {
        super(R.layout.warn_msg_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgBean item) {
        helper.setText(R.id.tipTv, item.getMsg()).addOnClickListener(R.id.btnPause)
                .addOnClickListener(R.id.btnDestroy).addOnClickListener(R.id.btnClear);

//        int offset = helper.getLayoutPosition();
//        if (offset % 2 != 0) {
//            helper.getView(R.id.layout_item).setBackgroundColor(0xffffffff);
//        } else {
//            helper.getView(R.id.layout_item).setBackgroundColor(0xfff9f9f9);
//        }
    }

    public void removeData(final int position) {
        notifyItemRemoved(position);
    }
}
