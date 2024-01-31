//package com.emt.pdgo.next.ui.adapter.local;
//
//import androidx.annotation.Nullable;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.emt.pdgo.next.database.entity.FaultMsgEntity;
//import com.pdp.rmmit.pdp.R;
//
//import java.util.List;
//
//public class FaultMsgAdapter extends BaseQuickAdapter<FaultMsgEntity, BaseViewHolder> {
//
//    public FaultMsgAdapter(@Nullable List<FaultMsgEntity> data) {
//        super(R.layout.fault_msg_recyclerview, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, FaultMsgEntity item) {
//        helper.setText(R.id.faultMsgTime, item.time);
//        helper.setText(R.id.faultMsgEvent, item.event);
//        helper.setText(R.id.faultMsgStatus, item.status);
//        helper.setText(R.id.faultMsgTv, item.msg);
//
//        int offset = helper.getLayoutPosition();
//        if (offset % 2 != 0) {
//            helper.getView(R.id.faultMsgLayout).setBackgroundColor(0xffffffff);
//        } else {
//            helper.getView(R.id.faultMsgLayout).setBackgroundColor(0xfff9f9f9);
//        }
//    }
//}
