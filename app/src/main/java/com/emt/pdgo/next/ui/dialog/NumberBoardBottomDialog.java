package com.emt.pdgo.next.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.pdp.rmmit.pdp.R;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.util.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.dialog
 * @ClassName: NumberBoardDialog
 * @Description: 自定义数字键盘
 * @Author: chenjh
 * @CreateDate: 2019/12/16 8:59 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/16 8:59 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */

public class NumberBoardBottomDialog extends Dialog {


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


    private Context context;

    private ArrayList<Map<String, String>> valueList = new ArrayList<>();

    private OnDialogResultListener onDialogResultListener;
    private String oldValue = "";

    /*** 是否负数 */
    private boolean isMinus = false;
    /*** 是否整数 */
    private boolean isInteger = false;

    private float mValue = 0;

    private String mType = "";

    public NumberBoardBottomDialog(Context context, @NonNull String value, String type) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.oldValue = value;
        this.mType = type;
        this.isMinus = false;
        this.isInteger = true;

        View view = View.inflate(context, R.layout.number_board_layout_popupwindow, null);
        Window window = this.getWindow();
        window.setContentView(view);
        mUnbinder = ButterKnife.bind(this, view);

        findView();
        initView();
        registerEvents();
        initViewData();

    }

    public NumberBoardBottomDialog(Context context, @NonNull String value, String type, boolean isMinus) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.oldValue = value;
        this.mType = type;
        this.isMinus = isMinus;

        View view = View.inflate(context, R.layout.number_board_layout_popupwindow, null);
        Window window = this.getWindow();
        window.setContentView(view);
        mUnbinder = ButterKnife.bind(this, view);

        findView();
        initView();
        registerEvents();
        initViewData();
    }

    public NumberBoardBottomDialog(Context context, @NonNull String value, String type, boolean isMinus, boolean isInteger) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.oldValue = value;
        this.mType = type;
        this.isMinus = isMinus;
        this.isInteger = isInteger;

        View view = View.inflate(context, R.layout.number_board_layout_popupwindow, null);
        Window window = this.getWindow();
        window.setContentView(view);
        mUnbinder = ButterKnife.bind(this, view);

        findView();
        initView();
        registerEvents();
        initViewData();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUnbinder.unbind();
    }

    public void setOnDialogResultListener(OnDialogResultListener onDialogResultListener) {
        this.onDialogResultListener = onDialogResultListener;
    }

    private void findView() {


    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        this.setCancelable(false);//按返回键不能退出
        Window window = this.getWindow();
        //背景改透明
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        layoutParams.width = ScreenUtil.dip2px(context, 800);
//        layoutParams.height = ScreenUtil.dip2px(context, 229);
        layoutParams.width = this.context.getResources().getDimensionPixelOffset(R.dimen.number_board_popup_layout_width);
        layoutParams.height = this.context.getResources().getDimensionPixelOffset(R.dimen.number_board_popup_layout_height);
        layoutParams.gravity = Gravity.BOTTOM;//设置dialog 布局中的位置
        layoutParams.dimAmount = 0.0f;
        window.setAttributes(layoutParams);

        initValueList();

        btnItem12.setVisibility(isMinus ? View.VISIBLE : View.INVISIBLE);

    }

    private void registerEvents() {


    }


    private void initViewData() {

        setBoardHint();

        setBtnSure();

    }

    @OnClick({R.id.btn_item1, R.id.btn_item2, R.id.btn_item3, R.id.btn_item4, R.id.btn_item5, R.id.btn_item6, R.id.btn_item7, R.id.btn_item8, R.id.btn_item9, R.id.btn_item10, R.id.btn_item11,
            R.id.key_board_hide, R.id.btn_sumbit, R.id.key_board_del})
    public void onViewClicked(View view) {
//        String mValue = tvBoardValue.getText().toString();
        switch (view.getId()) {
            case R.id.key_board_hide:
                dismiss();
                break;
            case R.id.btn_sumbit:
//                String newContent = tvBoardValue.getText().toString();
//
//                if (newContent.indexOf(".") != -1) {//数值为小数时候
//                    if (newContent.endsWith(".")) {
//                        newContent = newContent + "0";
//                    }
//                }
//
//                dismiss();
//                onDialogResultListener.onResult(mType, newContent);
                break;
            case R.id.key_board_del:

//                if (!"".equals(mValue)) {
//                    tvBoardValue.setText(mValue.substring(0, mValue.length() - 1));
//                    setBtnSure();
//                }
                break;
            case R.id.btn_item1:// 数字 1
                setNumber("1");
                break;
            case R.id.btn_item2:// 数字 2
                setNumber("2");
                break;
            case R.id.btn_item3:// 数字 3
                setNumber("3");
                break;
            case R.id.btn_item4:// 数字 4
                setNumber("4");
                break;
            case R.id.btn_item5:// 数字 5
                setNumber("5");
                break;
            case R.id.btn_item6:// 数字 6
                setNumber("6");
                break;
            case R.id.btn_item7:// 数字 7
                setNumber("7");
                break;
            case R.id.btn_item8:// 数字 8
                setNumber("8");
                break;
            case R.id.btn_item9:// 数字 9
                setNumber("9");
                break;
            case R.id.btn_item10:// 小数点 .

//                if (TextUtils.isEmpty(mValue) || "0".equals(mValue)) {
//                    tvBoardValue.setText("0.");
//                } else {
//                    if (mValue.indexOf(".") == -1) {//数值不为小数时候，点击小数点
//                        tvBoardValue.setText(mValue + ".");
//                    }
//                }
//                break;
//            case R.id.btn_item11:// 数字 0
//
//                if (TextUtils.isEmpty(mValue) || "0".equals(mValue)) {
//                    tvBoardValue.setText("0");
//                } else {
//                    tvBoardValue.setText(mValue + "0");
//                }
                break;
//            case R.id.btn_item12:// 负号
//
//                if (!"".equals(mValue)) {
//                    if (mValue.contains("-")) {
//                        tvBoardValue.setText(mValue.replace("-", ""));
//                    } else {
//                        tvBoardValue.setText("-" + mValue);
//                    }
//                } else {
//                    if (mValue.equals("-")) {
//                        tvBoardValue.setText(mValue.replace("-", ""));
//                    } else {
//                        tvBoardValue.setText("-");
//                    }
//                }
//                break;
        }
    }


    private void setNumber(String num) {

//        String tempValue = tvBoardValue.getText().toString().trim();
//        if (!TextUtils.isEmpty(tempValue)) {
//            if ("0".equals(tempValue)) {//第一个数字为0时候，
//                tvBoardValue.setText(num);
//            } else {//第一个数字不为0
//                if (tempValue.indexOf(".") != -1) {//数值为小数时候
//
//                    String tempValues[] = tempValue.split("\\.");
//                    if (tempValues.length >= 2 && tempValues[1].length() == 1) {
////                                    Log.e("输入控件","这个是小数点后1位");
//                    } else {
//                        tvBoardValue.setText(tempValue + num);
//                    }
//                } else {
//                    tvBoardValue.setText(tempValue + num);
//                }
//            }
//        } else {//还没输入
//            tvBoardValue.setText(num);
//        }
        setBtnSure();
    }

    private void setBoardHint() {

        String title = "";

        //治疗之前身体数据
        if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_RECOMMENDED_WEIGHT)) {//医生建议体重
            title = "建议体重  :  1  -  500  ";
        } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_WEIGHT)) {//治疗前体重
            title = "治疗前体重  :  1  -  500  ";
        } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_DIASTOLIC_BLOOD_PRESSURE)) {//治疗前舒张压
            title = "治疗前舒张压  :  30  -  150  ";
        } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_SYSTOLIC_BLOOD_PRESSURE)) {//治疗前收缩压
            title = "治疗前收缩压  :  50  -  220  (应大于舒张压)   ";
        }


    }


    private void setBtnSure() {
        String tempValue = "";
//        String tempValue = tvBoardValue.getText().toString().trim();
        if (!TextUtils.isEmpty(tempValue)) {
            if (isMinus && "-".equals(tempValue)) {//当只有负号时候
                isShowSure(false);
                return;
            }
            mValue = Float.valueOf(tempValue);


            if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_WEIGHT)) {//治疗前体重
                if (mValue >= 1f & mValue <= 5f) {
                    isShowSure(true);
                } else {
                    isShowSure(false);
                }
            } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_SYSTOLIC_BLOOD_PRESSURE)) {//治疗前收缩压
                if (mValue >= 30f & mValue <= 150f) {
                    isShowSure(true);
                } else {
                    isShowSure(false);
                }
            } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_DIASTOLIC_BLOOD_PRESSURE)) {//治疗前舒张压
                if (mValue >= 50f & mValue <= 220f) {
                    isShowSure(true);
                } else {
                    isShowSure(false);
                }
            } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_HEART_RATE)) {//治疗前心率
                if (mValue >= 1f & mValue <= 5f) {
                    isShowSure(true);
                } else {
                    isShowSure(false);
                }
            }


        } else {
            isShowSure(false);
        }


    }


    private void isShowSure(boolean isShow) {
//        ivRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnSumbit.setEnabled(isShow);
        btnSumbit.setBackgroundResource(isShow ? R.drawable.bg_round_blue : R.drawable.key_board_selector_item_pressed_day);
        btnSumbit.setClickable(isShow);
        btnSumbit.setFocusable(isShow);
    }

    private void initValueList() {
        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                if (isInteger) {
                    map.put("name", "");
                } else {
                    map.put("name", ".");
                }
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", "-");
            }
            valueList.add(map);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    public interface OnDialogResultListener {
        void onResult(String type, String result);
    }


}

