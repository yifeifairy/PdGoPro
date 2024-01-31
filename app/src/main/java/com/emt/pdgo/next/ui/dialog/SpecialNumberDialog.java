package com.emt.pdgo.next.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.data.bean.ExpertBean;
import com.emt.pdgo.next.ui.adapter.KeyBoardAdapter;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialNumberDialog extends Dialog {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.submit)
    Button btnSubmit;

    @BindView(R.id.key_board_del)
    RelativeLayout keyBoardDel;

    private List<Integer> cycles;

    private KeyBoardAdapter adapter;


    public SpecialNumberDialog(@NonNull Context context, ExpertBean expertBean, int supplyModel, TextView textView) {
        super(context, R.style.CustomDialog);
        initView(context, expertBean, supplyModel, textView);
    }
    
    private void initView(Context context, ExpertBean expertBean, int supplyModel, TextView textView) {
        View view = View.inflate(context, R.layout.special_number_layout, null);
        Window window = this.getWindow();
        window.setContentView(view);
        ButterKnife.bind(this, view);
        this.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        this.setCancelable(false);//按返回键不能退出

        Window window1 = this.getWindow();
//        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation_style_left_and_right);
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.gravity = Gravity.CENTER;//设置dialog 布局中的位置
        window1.setAttributes(lp);
        cycles = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            cycles.add(i);
        }
        recyclerview.setLayoutManager(new GridLayoutManager(context,3));
//        hisPdAdapter = new HisPdAdapter(hisTreatBeans);
        recyclerview.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
        adapter = new KeyBoardAdapter(context,cycles, expertBean, supplyModel);
        recyclerview.setAdapter(adapter);

        adapter.setmOnItemClickListener((bean, supplyModel1, isCheck, position, valueList) -> {
            if (supplyModel1 == 0) {
                if (isCheck) {
                    if (!bean.baseSupplyCycle.contains(valueList.get(position)) && !bean.osmSupplyCycle.contains(valueList.get(position))) {
                        bean.baseSupplyCycle.add(valueList.get(position));
                    }
                } else {
                    if (bean.baseSupplyCycle.contains(valueList.get(position))) {
                        bean.baseSupplyCycle.remove((Integer) valueList.get(position));
                    }
                }
            } else if (supplyModel1 == 1) {
                if (isCheck) {
                    if (!bean.osmSupplyCycle.contains(valueList.get(position)) && !bean.baseSupplyCycle.contains(valueList.get(position))) {
                        bean.osmSupplyCycle.add(valueList.get(position));
                    }
                } else {
                    if (bean.osmSupplyCycle.contains(valueList.get(position))) {
                        bean.osmSupplyCycle.remove( (Integer)valueList.get(position));
                    }
                }
            }
        });

//        StringBuffer sb = new StringBuffer();
        btnSubmit.setOnClickListener(v -> {
//            if (supplyModel == 0) {
//                textView.setText(expertBean.baseSupplyCycle.toString());
//            }else if (supplyModel == 1) {
//                textView.setText(expertBean.osmSupplyCycle.toString());
//            }
            onDialogResultListener.onResult(supplyModel, expertBean);
            dismiss();
        });

        btnClose.setOnClickListener(v -> dismiss());
    }
    public void setOnDialogResultListener(OnDialogResultListener onDialogResultListener) {
        this.onDialogResultListener = onDialogResultListener;
    }
    private OnDialogResultListener onDialogResultListener;
    public interface OnDialogResultListener {
        void onResult(int model, ExpertBean expertBean);
    }

}
