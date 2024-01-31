package com.emt.pdgo.next.ui.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pdp.rmmit.pdp.R;
import com.emt.pdgo.next.util.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by chenjh on 2020/1/8.
 */

public class KeyBoardView extends PopupWindow implements View.OnLongClickListener {

    Unbinder mUnbinder;

    @BindView(R.id.btn_item1)
    Button btnItem1;
    @BindView(R.id.btn_item2)
    Button btnItem2;
    @BindView(R.id.btn_item3)
    Button btnItem3;

    @BindView(R.id.btn_item4)
    Button btnItem4;
    @BindView(R.id.btn_item5)
    Button btnItem5;
    @BindView(R.id.btn_item6)
    Button btnItem6;

    @BindView(R.id.btn_item7)
    Button btnItem7;
    @BindView(R.id.btn_item8)
    Button btnItem8;
    @BindView(R.id.btn_item9)
    Button btnItem9;

    @BindView(R.id.btn_item10)
    Button btnItem10;
    @BindView(R.id.btn_item11)
    Button btnItem11;
    @BindView(R.id.btn_item12)
    Button btnItem12;


    @BindView(R.id.btn_sumbit)
    Button btnSumbit;

    @BindView(R.id.key_board_hide)
    RelativeLayout keyBoardHide;
    @BindView(R.id.key_board_del)
    RelativeLayout keyBoardDel;

    //点击的view
    private View clickView;

    private Activity activity;
    //记录键盘的值，默认值是“0”
    private String keyValues;
    //这个就是默认值...
//    private static final String DEFAULT = "";
    //这个是可输入的最大长度，根据项目需求修改
    private static final int STRING_LENGTH = 7;//

    //构造器
    public KeyBoardView(Activity activity) {
        this.activity = activity;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.number_board_layout_popupwindow, null);

        this.setContentView(view);
        mUnbinder = ButterKnife.bind(this, view);

        init();
    }

    //初始化keyvalues的值为0
    private void initKeyValues(String value) {
        keyValues = value;
        refreshView();
    }

    //进行视图刷新
    private void refreshView() {
        keyInputListener.onFinish(clickView, keyValues);
//        input_content.setText(keyValues == null || "".equals(keyValues) ? DEFAULT : keyValues);
    }

    //设置值
    //value的值有
    //         1~9 .
    //         CLR    长按清除按钮 ，清空keyvalues
    //         CL     点击清除按钮 ，删除最后一个
    private void setKeyValues(String value) {

        if ("CLR".equals(value)) {//清空
            initKeyValues("");
            return;
        }
        if ("CL".equals(value)) {
            keyValues = keyValues.substring(0, keyValues.length() - 1);//把最后一个字符截取调
            value = "";//把他置为空字符串，这样再继续执行代码时 "CL"就不会影响计算了
        }
        if (".".equals(value) && keyValues.indexOf(".") != -1) {
            //如果keyvalues中已经有了“.”，就不能再加了
            return;
        }
        //最新值 = 上次的值+输入的值
        String tmpValue = keyValues + value;
        if (tmpValue.length() > STRING_LENGTH) {
            //如果超过了最大长度就return
            return;
        }
        if (tmpValue.indexOf(".") != -1) {//那就是小数了
            keyValues = tmpValue;//原来的值不动
        } else {//整数
            int tmpValueInt = Integer.parseInt(tmpValue == null || "".equals(tmpValue) ? "0" : tmpValue);
            keyValues = "" + tmpValueInt;
        }
        refreshView();
    }

    //初始化
    private void init() {

//        this.setWidth(w);
//        this.setWidth(ScreenUtil.dip2px(activity, 800));
//        // 设置SelectPicPopupWindow弹出窗体的高
//        this.setHeight(ScreenUtil.dip2px(activity, 229));

        this.setWidth(this.activity.getResources().getDimensionPixelOffset(R.dimen.number_board_popup_layout_width));
        this.setHeight(this.activity.getResources().getDimensionPixelOffset(R.dimen.number_board_popup_layout_height));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationToast);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                keyInputListener.onDismiss(clickView);
            }
        });
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            clickView = parent;
            initKeyValues("");
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    public void showPopupWindow(View parent, String value) {
        if (!this.isShowing()) {
            clickView = parent;
            initKeyValues(value);
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }


    public void dismissPopWindow() {
        if (isShowing()) {
            dismiss();
        }
    }

    @OnClick({R.id.btn_item1, R.id.btn_item2, R.id.btn_item3, R.id.btn_item4, R.id.btn_item5, R.id.btn_item6, R.id.btn_item7, R.id.btn_item8, R.id.btn_item9, R.id.btn_item10, R.id.btn_item11,
            R.id.key_board_hide, R.id.btn_sumbit, R.id.key_board_del})
    public void onViewClicked(View view) {
//        String mValue = tvBoardValue.getText().toString();
        switch (view.getId()) {
            case R.id.key_board_hide:
                dismissPopWindow();
                break;
            case R.id.btn_sumbit:
                dismissPopWindow();
                if (keyInputListener != null) {
                    //如果用户最后输入.时 比如 12345. ，这时候点击“完成”我们返回的字符串就是"12345."，无论是显示还是计算都是不好看的，我这里的处理是在末尾补0，如果项目需求不是可以将此删除掉
                    if (keyValues.indexOf(".") == keyValues.length() - 1) {
//                        keyValues = keyValues + "0";
                    } else {
                        if (keyValues.indexOf(".") != -1) {
                            //是double
                            double value = Double.parseDouble(keyValues);
                            if (value == 0d) {
                                keyValues = "0.0";
                            }
                        }
                    }
                    keyInputListener.onFinish(clickView, keyValues);
                }
                break;
            case R.id.key_board_del:
                setKeyValues("CL");//删除最后一个元素
                break;
            case R.id.btn_item1:// 数字 1
                setKeyValues("1");
                break;
            case R.id.btn_item2:// 数字 2
                setKeyValues("2");
                break;
            case R.id.btn_item3:// 数字 3
                setKeyValues("3");
                break;
            case R.id.btn_item4:// 数字 4
                setKeyValues("4");
                break;
            case R.id.btn_item5:// 数字 5
                setKeyValues("5");
                break;
            case R.id.btn_item6:// 数字 6
                setKeyValues("6");
                break;
            case R.id.btn_item7:// 数字 7
                setKeyValues("7");
                break;
            case R.id.btn_item8:// 数字 8
                setKeyValues("8");
                break;
            case R.id.btn_item9:// 数字 9
                setKeyValues("9");
                break;
            case R.id.btn_item10:// 小数点 .
                setKeyValues(".");
                break;
            case R.id.btn_item11:// 数字 0
                setKeyValues("0");
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        setKeyValues("CLR");//长按清除
        return true;//只执行长按事件
    }

    //这个是自定义一个监听器，主要是用于当用户点完成将输入的数据返回
    public interface OnKeyInputListener {
        //当用户点击“完成”时会调用此方法
        //参数view:这个是你在activity点击的控件的对象
        //参数value:这个就是keyValues值
        public void onFinish(View view, String value);

        public void onDismiss(View view);
    }

    private OnKeyInputListener keyInputListener;

    //监听器Getter
    public OnKeyInputListener getKeyInputListener() {
        return keyInputListener;
    }

    //监听器Setter
    public void setKeyInputListener(OnKeyInputListener keyInputListener) {
        this.keyInputListener = keyInputListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}
