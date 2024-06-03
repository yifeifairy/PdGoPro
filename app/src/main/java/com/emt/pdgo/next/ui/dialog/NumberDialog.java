package com.emt.pdgo.next.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.ui.view.Breathe;
import com.pdp.rmmit.pdp.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NumberDialog extends Dialog {

    @BindView(R.id.tv_item_label)
    TextView tvItemLabel;
    @BindView(R.id.tv_board_value)
    TextView tvBoardValue;

    @BindView(R.id.breathe)
    Breathe breathe;

    @BindView(R.id.tipLayout)
    LinearLayout tipLayout;

    @BindView(R.id.key_board_rv)
    RecyclerView keyBoardRv;

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
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_item11)
    Button btnItem11;
    @BindView(R.id.btn_item12)
    Button btnItem12;

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.btn_sumbit)
    Button btnSumbit;

    @BindView(R.id.key_board_del)
    RelativeLayout keyBoardDel;

    private Context context;


    private NumberDialog.OnDialogResultListener onDialogResultListener;
    private String oldValue = "";

    /*** 是否负数 */
    private boolean isMinus = false;
    /*** 是否整数 */
    private boolean isInteger = false;

    private int mValue = 0;

    private String mType = "";

    private String hint;
    private String title;

    public NumberDialog(Context context, @NonNull String value, String type) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.oldValue = value;
        this.mType = type;
        this.isMinus = false;
        this.isInteger = true;

        View view = View.inflate(context, R.layout.number_board_layout, null);
        Window window = this.getWindow();
        window.setContentView(view);
        ButterKnife.bind(this, view);
        initView();
        initViewData();
    }

    public NumberDialog(Context context, @NonNull String title, int min, int max) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.oldValue = title;
        this.mType = title;
        this.title = title;
        this.isMinus = false;
        this.isInteger = true;
        this.min = min;
        this.max = max;
        View view = View.inflate(context, R.layout.number_board_layout, null);
        Window window = this.getWindow();
        window.setContentView(view);
        ButterKnife.bind(this, view);

        initView();
        setTitleAndHint(title,min,max);
//        setBtnSure();
        setBtnSure(min,max);
    }

    private void setBtnSure(int min, int max) {
        String tempValue = tvBoardValue.getText().toString().trim();
        if (!TextUtils.isEmpty(tempValue)) {
            mValue = Integer.parseInt(tempValue);
            if (mValue > max) {
                tipLayout.setVisibility(View.VISIBLE);
                breathe.show();
                tvBoardValue.setText("");
            } else {
                tipLayout.setVisibility(View.GONE);
                breathe.hide();
            }
            isShowSure(mValue>= min && mValue <= max);
        } else {
            isShowSure(false);
        }
    }

    private int min;
    private int max;
    private void setTitleAndHint(String title, int min, int max) {
        tvItemLabel.setText(title);
//        tvBoardValue.setHint(mHint);
        int hintSize = this.context.getResources().getDimensionPixelOffset(R.dimen.number_board_input_hint_text_size);
        String hint = min + "  -  "+max;
        this.max = max;
        this.min = min;
//        Log.e("输入提示","hintSize:"+hintSize);
        SpannableString sHint = new SpannableString(hint);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintSize, true);//设置字体大小 true表示单位是sp
        sHint.setSpan(ass, 0, sHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBoardValue.setHint(new SpannedString(sHint));
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setOnDialogResultListener(NumberDialog.OnDialogResultListener onDialogResultListener) {
        this.onDialogResultListener = onDialogResultListener;
    }

    private void initView() {
        this.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        this.setCancelable(false);//按返回键不能退出
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialog_animation_style_left_and_right);
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.gravity = Gravity.CENTER;//设置dialog 布局中的位置
        window.setAttributes(lp);
        initValueList();
        btnItem10.setVisibility(isInteger ? View.INVISIBLE : View.VISIBLE);
        btnItem12.setVisibility(isMinus ? View.VISIBLE : View.GONE);
    }

    private void initViewData() {
        setBoardHint();
        setBtnSure(min, max);
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @OnClick({R.id.btn_item1, R.id.btn_item2, R.id.btn_item3, R.id.btn_item4, R.id.btn_item5, R.id.btn_item6, R.id.btn_item7, R.id.btn_item8,
            R.id.btn_item9, R.id.btn_item10, R.id.btn_item11, R.id.btn_item12, R.id.btn_close, R.id.btn_sumbit, R.id.key_board_del})
    public void onViewClicked(View view) {
        String mValue = tvBoardValue.getText().toString();
        switch (view.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_sumbit:
                String newContent = tvBoardValue.getText().toString();

                if (newContent.contains(".")) {//数值为小数时候
                    if (newContent.endsWith(".")) {
                        newContent = newContent + "0";
                    }
                }
                dismiss();
                onDialogResultListener.onResult(mType, newContent);
                break;
            case R.id.key_board_del:
                if (!"".equals(mValue)) {
                    tvBoardValue.setText(mValue.substring(0, mValue.length() - 1));
                    setBtnSure(min, max);
                }
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
                if (TextUtils.isEmpty(mValue) || "0".equals(mValue)) {
                    tvBoardValue.setText("0.");
                } else {
                    if (!mValue.contains(".")) {//数值不为小数时候，点击小数点
                        tvBoardValue.setText(mValue + ".");
                    }
                }
                setBtnSure(min, max);
                break;
            case R.id.btn_item11:// 数字 0

                if (TextUtils.isEmpty(mValue) || "0".equals(mValue)) {
                    tvBoardValue.setText("0");
                } else {
                    tvBoardValue.setText(mValue + "0");
                }
                setBtnSure(min, max);
                break;
            case R.id.btn_item12:// 负号
                if (!"".equals(mValue)) {
                    if (mValue.contains("-")) {
                        tvBoardValue.setText(mValue.replace("-", ""));
                    } else {
                        tvBoardValue.setText("-" + mValue);
                    }
                } else {
                    tvBoardValue.setText("-");
                }
                setBtnSure(min, max);
                break;
        }
    }


    @SuppressLint("SetTextI18n")
    private void setNumber(String num) {

        String tempValue = tvBoardValue.getText().toString().trim();
        if (!TextUtils.isEmpty(tempValue)) {
            if ("0".equals(tempValue)) {//第一个数字为0时候，
                tvBoardValue.setText(num);
            } else {//第一个数字不为0
                if (tempValue.contains(".")) {//数值为小数时候

                    String[] tempValues = tempValue.split("\\.");
                    if (tempValues.length >= 2 && tempValues[1].length() == 4) {
                        Log.e("输入控件","这个是小数点后4位");
                    } else {
                        tvBoardValue.setText(tempValue + num);
                    }
                } else {
                    tvBoardValue.setText(tempValue + num);
//                    Log.e("输入控件","value:  tempValue--"+tempValue +"---num---" + num);
                }
            }
        } else {//还没输入
            tvBoardValue.setText(num);
        }
        setBtnSure(min, max);

    }

    private void setBoardHint() {
        String mHint = min +"-"+max;
        tvItemLabel.setText(title);
//        tvBoardValue.setHint(mHint);
        int hintSize = this.context.getResources().getDimensionPixelOffset(R.dimen.number_board_input_hint_text_size);
//        Log.e("输入提示","hintSize:"+hintSize);
        SpannableString sHint = new SpannableString(mHint);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintSize, true);//设置字体大小 true表示单位是sp
        sHint.setSpan(ass, 0, sHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBoardValue.setHint(new SpannedString(sHint));

    }

    private void isShowSure(boolean isShow) {
//        ivRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnSumbit.setEnabled(isShow);
        btnSumbit.setBackgroundResource(isShow ? R.drawable.key_board_selector_blue : R.drawable.key_board_selector_item_pressed_day);
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
            } else {
                map.put("name", "-");
            }

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
