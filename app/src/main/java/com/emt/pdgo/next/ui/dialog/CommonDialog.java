package com.emt.pdgo.next.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pdp.rmmit.pdp.R;
import com.emt.pdgo.next.util.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.dialog
 * @ClassName: CommonDialog
 * @Description: 自定义dialog
 * @Author: chenjh
 * @CreateDate: 2019/12/5 6:53 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/5 6:53 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class CommonDialog extends Dialog {

    @BindView(R.id.btn_left)
    Button btnFirst;
    @BindView(R.id.btn_right)
    Button btnTwo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private Context mContext;


    public CommonDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
    }

    private String message;
    private String title;
    private String content;

    private String firstTxt, twoTxt;

    private int firstDrawableId = -1;
    private int twoDrawableId = -1;

    private int firstTxtColorId = -1;
    private int twoTxtColorId = -1;


    private OnCommonClickListener mFirstClickListener;
    private OnCommonClickListener mTwoClickListener;

    private OnCommonLongClickListener mFirstLongClickListener;
    private OnCommonLongClickListener mTwoLongClickListener;


    public interface OnCommonClickListener {
        void onClick(CommonDialog mCommonDialog);
    }

    public interface OnCommonLongClickListener {
        void onLongClick(CommonDialog mCommonDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        this.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        this.setCancelable(false);//按返回键不能退出
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialog_animation_style_left_and_right);
        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = ScreenUtil.dip2px(mContext, 384);
//        lp.height = ScreenUtil.dip2px(mContext, 238);
        lp.gravity = Gravity.CENTER;//设置dialog 布局中的位置
        window.setAttributes(lp);

    }


    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(message)) {
            tvMsg.setText(message);
            tvMsg.setVisibility(View.VISIBLE);
        } else {
            tvMsg.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }

        //设置第一个按钮的文字
        if (!TextUtils.isEmpty(firstTxt)) {
            btnFirst.setText(firstTxt);
            updateTextSize(btnFirst, firstTxt);
            btnFirst.setVisibility(View.VISIBLE);
        } else {
            btnFirst.setVisibility(View.GONE);
        }
        //设置第二个按钮的文字
        if (!TextUtils.isEmpty(twoTxt)) {
            btnTwo.setText(twoTxt);
            updateTextSize(btnTwo, twoTxt);
            btnTwo.setVisibility(View.VISIBLE);
        } else {
            btnTwo.setVisibility(View.GONE);
        }
        //设置第三个按钮的文字


        if (firstDrawableId != -1) {
            btnFirst.setBackgroundResource(firstDrawableId);
        }
        if (twoDrawableId != -1) {
            btnTwo.setBackgroundResource(twoDrawableId);
        }


        if (firstTxtColorId != -1) {
            btnFirst.setTextColor(firstTxtColorId);
        }
        if (twoTxtColorId != -1) {
            btnTwo.setTextColor(twoTxtColorId);
        }

    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }


    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_left://第一个按钮
                if (mFirstClickListener != null) {
                    mFirstClickListener.onClick(CommonDialog.this);
                }
                break;
            case R.id.btn_right://第二个按钮
                if (mTwoClickListener != null) {
                    mTwoClickListener.onClick(CommonDialog.this);
                }
                break;
        }

    }

    @OnLongClick({R.id.btn_left, R.id.btn_right})
    public boolean onViewLongClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left://第一个按钮
                if (mFirstLongClickListener != null) {
                    mFirstLongClickListener.onLongClick(CommonDialog.this);
                }
                break;
            case R.id.btn_right://第二个按钮
                if (mTwoLongClickListener != null) {
                    mTwoLongClickListener.onLongClick(CommonDialog.this);
                }
                break;
        }
        return false;
    }


    public String getMessage() {
        return message;
    }

    public CommonDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommonDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public CommonDialog setMessage(String message, int colorId) {
        this.message = message;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }


    public CommonDialog setBtnFirst(String firstTxt) {
        this.firstTxt = firstTxt;
        return this;
    }


    public CommonDialog setBtnTwo(String twoTxt) {
        this.twoTxt = twoTxt;
        return this;
    }

    public CommonDialog setBtnFirst(String firstTxt, int firstDrawableId) {
        this.firstTxt = firstTxt;
        this.firstDrawableId = firstDrawableId;
        return this;
    }


    public CommonDialog setBtnTwo(String twoTxt, int twoDrawableId) {
        this.twoTxt = twoTxt;
        this.twoDrawableId = twoDrawableId;
        return this;
    }


    public CommonDialog setBtnFirst(String firstTxt, int firstTxtColorId, int firstDrawableId) {
        this.firstTxt = firstTxt;
        this.firstTxtColorId = firstTxtColorId;
        this.firstDrawableId = firstDrawableId;
        return this;
    }


    public CommonDialog setBtnTwo(String twoTxt, int twoTxtColorId, int twoDrawableId) {
        this.twoTxt = twoTxt;
        this.twoTxtColorId = twoTxtColorId;
        this.twoDrawableId = twoDrawableId;
        return this;
    }


    public void updateTextSize(TextView mTextView, String content) {
        int startLen = content.indexOf("(");

        if (startLen != -1) {
            SpannableString msp = new SpannableString(content);
//        msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_right)), 18, 18 + content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new AbsoluteSizeSpan(18, true), startLen, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTextView.setText(msp);
        }


    }


    /**
     * 设置第一个按钮点监听 单点
     *
     * @param listener
     * @return
     */
    public CommonDialog setFirstClickListener(OnCommonClickListener listener) {
        mFirstClickListener = listener;
        return this;
    }

    /**
     * 设置第二个按钮点监听 单点
     *
     * @param listener
     * @return
     */
    public CommonDialog setTwoClickListener(OnCommonClickListener listener) {
        mTwoClickListener = listener;
        return this;
    }


    /**
     * 设置第一个按钮点监听 长按
     *
     * @param listener
     * @return
     */
    public CommonDialog setFirstLongClickListener(OnCommonLongClickListener listener) {
        mFirstLongClickListener = listener;
        return this;
    }

    /**
     * 设置第二个按钮点监听 长按
     *
     * @param listener
     * @return
     */
    public CommonDialog setTwoLongClickListener(OnCommonLongClickListener listener) {
        mTwoLongClickListener = listener;
        return this;
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}