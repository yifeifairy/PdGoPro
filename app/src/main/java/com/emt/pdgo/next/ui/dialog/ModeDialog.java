package com.emt.pdgo.next.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.emt.pdgo.next.constant.EmtConstant;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModeDialog extends Dialog {

    @BindView(R.id.ipdTv)
    TextView ipdTv;
    @BindView(R.id.aApdTv)
    TextView aApdTv;
    @BindView(R.id.spTv)
    TextView spTv;
    @BindView(R.id.dprTv)
    TextView dprTv;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    public ModeDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_dialog);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        setCancelable(false);//按返回键不能退出
        btnConfirm.setVisibility(View.INVISIBLE);
        dprTv.setVisibility(EmtConstant.sn_name.equals("PD-GO-Next") ? View.VISIBLE : View.INVISIBLE);
        ipdTv.setOnClickListener(view -> {
            ipdTv.setSelected(!ipdTv.isSelected());
            aApdTv.setSelected(false);
            spTv.setSelected(false);
            dprTv.setSelected(false);
            btnConfirm.setVisibility(ipdTv.isSelected() ? View.VISIBLE : View.INVISIBLE);
        });
        aApdTv.setOnClickListener(view -> {
            aApdTv.setSelected(!aApdTv.isSelected());
            ipdTv.setSelected(false);
            spTv.setSelected(false);
            dprTv.setSelected(false);
            btnConfirm.setVisibility(aApdTv.isSelected() ? View.VISIBLE : View.INVISIBLE);
        });
        spTv.setOnClickListener(view -> {
            spTv.setSelected(!spTv.isSelected());
            ipdTv.setSelected(false);
            aApdTv.setSelected(false);
            dprTv.setSelected(false);
            btnConfirm.setVisibility(spTv.isSelected() ? View.VISIBLE : View.INVISIBLE);
        });
        dprTv.setOnClickListener(view -> {
            dprTv.setSelected(!dprTv.isSelected());
            ipdTv.setSelected(false);
            aApdTv.setSelected(false);
            spTv.setSelected(false);
            btnConfirm.setVisibility(dprTv.isSelected() ? View.VISIBLE : View.INVISIBLE);
        });
        btnConfirm.setOnClickListener(view -> {
            int mode = 1;
            if (ipdTv.isSelected()) {
                mode = 1;
            } else if (aApdTv.isSelected()) {
                mode = 2;
            } else if (spTv.isSelected()) {
                mode = 3;
            } else if (dprTv.isSelected()) {
                mode = 4;
            }
            onDialogResultListener.onResult(mode);
        });
        btnCancel.setOnClickListener(view -> dismiss());
    }

    public void setOnDialogResultListener(OnDialogResultListener onDialogResultListener) {
        this.onDialogResultListener = onDialogResultListener;
    }
    private OnDialogResultListener onDialogResultListener;
    public interface OnDialogResultListener {
        void onResult(int model);
    }



}
