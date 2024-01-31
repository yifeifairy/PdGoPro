package com.emt.pdgo.next.ui.dialog;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emt.pdgo.next.rxlibrary.rxjava.RxBuilder;
import com.emt.pdgo.next.rxlibrary.utils.RxLoadingDialog;
import com.pdp.rmmit.pdp.R;


/**
 * Created by bhm on 2018/5/14.
 */

public class MyLoadingDialog extends RxLoadingDialog {

    @Override
    public Dialog initDialog(RxBuilder builder) {
        LayoutInflater inflater = LayoutInflater.from(builder.getActivity());
        View v = inflater.inflate(R.layout.layout_dialog_app_loading, null);// 得到加载view
        ProgressBar pb_loading = (ProgressBar) v.findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.GONE);
        TextView textView = (TextView) v.findViewById(R.id.dialog_text_loading);
//        textView.setText("测试我的Loading");
        Dialog dialog = new Dialog(builder.getActivity(), R.style.loading_dialog);// 创建自定义样式dialog
        dialog.setCancelable(builder.isCancelable());// false不可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(builder.isCanceledOnTouchOutside());
        dialog.setContentView(v, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));// 设置布局
        return dialog;
    }
}
