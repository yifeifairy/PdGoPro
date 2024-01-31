//package com.emt.pdgo.next.ui.adapter;
//
//import android.view.View;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.lifesense.ble.bean.LsDeviceInfo;
//import com.pdp.rmmit.pdp.R;
//
//import java.util.List;
//
///**
// * @ProjectName:
// * @Package: com.emt.pdgo.next.ui.adapter
// * @ClassName: BluetoothDeviceAdapter
// * @Description: java类作用描述
// * @Author: chenjh
// * @CreateDate: 2020/1/16 7:40 PM
// * @UpdateUser: 更新者
// * @UpdateDate: 2020/1/16 7:40 PM
// * @UpdateRemark: 更新内容
// * @Version: 1.0
// */
//public class BluetoothDeviceAdapter extends BaseQuickAdapter<LsDeviceInfo, BaseViewHolder> {
//
//
//    private OnCommonClickListener mListener;
//
//    public interface OnCommonClickListener {
//        void onClick(LsDeviceInfo item);
//    }
//
//    public BluetoothDeviceAdapter(int layoutResId, List data, OnCommonClickListener mListener) {
//        super(layoutResId, data);
//        this.mListener = mListener;
//    }
//
//
//    @Override
//    protected void convert(BaseViewHolder helper, LsDeviceInfo item) {
//        String mDeviceType = "";
//        if ("01".equals(item.getDeviceType())) {
//            mDeviceType = "体重秤";
//        } else if ("08".equals(item.getDeviceType())) {
//            mDeviceType = "血压计";
//        } else {
//            mDeviceType = "";
//        }
//        helper.setText(R.id.tv_item1, mDeviceType);//周期
//        helper.setText(R.id.tv_item2, item.getDeviceName());//设备名称
//        helper.setText(R.id.tv_item3, item.getMacAddress());//mac地址
//        helper.setText(R.id.tv_item4, item.getPairStatus()+"");//配对状态标记，0 表示配对成功，非 0 表示配对失败
////        helper.setText(R.id.tv_item4, item.getPairStatus() == 0 ? "配对成功" : "" + item.getPairStatus());//配对状态标记，0 表示配对成功，非 0 表示配对失败
//
////        int offset = helper.getLayoutPosition();
////        if (offset % 2 != 0) {
////            helper.getView(R.id.layout_item).setBackgroundColor(0xffffffff);
////        } else {
////            helper.getView(R.id.layout_item).setBackgroundColor(0xfff9f9f9);
////        }
//        helper.getView(R.id.layout_item).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (mListener != null) {
//                    mListener.onClick(item);
//                    helper.setText(R.id.tv_item4, "配对中");
//                }
//            }
//        });
//    }
//
//
//}
//
