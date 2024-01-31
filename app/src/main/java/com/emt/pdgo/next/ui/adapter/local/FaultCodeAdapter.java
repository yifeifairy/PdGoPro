package com.emt.pdgo.next.ui.adapter.local;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.database.entity.FaultCodeEntity;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class FaultCodeAdapter extends BaseQuickAdapter<FaultCodeEntity, BaseViewHolder> {

    public FaultCodeAdapter(@Nullable List<FaultCodeEntity> data) {
        super(R.layout.fault_code_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FaultCodeEntity item) {

        helper.setText(R.id.faultCodeTime, item.time);
        helper.setText(R.id.faultCode, item.code);

        int offset = helper.getLayoutPosition();
        if (offset % 2 != 0) {
            helper.getView(R.id.faultCodeItem).setBackgroundColor(0xffffffff);
        } else {
            helper.getView(R.id.faultCodeItem).setBackgroundColor(0xfff9f9f9);
        }
        TextView time = helper.getView(R.id.faultCodeTime);
        TextView textView = helper.getView(R.id.faultCode);
        if (item.code.contains("failure") || item.code.contains("err") || item.code.contains("exception")) {
            textView.setTextColor(Color.RED);
            time.setTextColor(Color.RED);
        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.text_default));
            time.setTextColor(mContext.getResources().getColor(R.color.text_default));
        }
    }

}
