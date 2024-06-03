package com.emt.pdgo.next.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

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
public class TreatmentAlarmDialog extends Dialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.tv_countdown)
    TextView tvCountDown;

    @BindView(R.id.btn_first)
    Button btnFirst;
    @BindView(R.id.btn_two)
    Button btnTwo;
    @BindView(R.id.btn_three)
    Button btnThree;

    @BindView(R.id.btn_view_data)
    Button btnViewData;

    @BindView(R.id.btnSilencers)
    Button btnSilencers;

    private Context mContext;

    private String message;
    private String title;

    private String mStatus;
    private String viewDataTxt;

    private String firstTxt, twoTxt, threeTxt;

    private boolean isBtnFirst, isBtnTwo, isBtnThree;

    private int viewDataDrawableId = -1;
    private int firstDrawableId = -1;
    private int twoDrawableId = -1;
    private int threeDrawableId = -1;

    private int viewDataTxtColorId = -1;
    private int msgTxtColorId = -1;
    private int firstTxtColorId = -1;
    private int twoTxtColorId = -1;
    private int threeTxtColorId = -1;

    private int imageResId = -1;

    /**
     * 是否显示倒计时
     */
    private boolean isCountdown = false;
    private static TimeCount timeCount;
    private long millisInFuture;

    private OnCommonClickListener mViewDataClickListener;

    private OnCommonClickListener silencersClickListener;

    private OnCommonClickListener mFirstClickListener;
    private OnCommonClickListener mTwoClickListener;
    private OnCommonClickListener mThreeClickListener;

    private OnCommonLongClickListener mFirstLongClickListener;
    private OnCommonLongClickListener mTwoLongClickListener;
    private OnCommonLongClickListener mThreeLongClickListener;


    public interface OnCommonClickListener {
        void onClick(TreatmentAlarmDialog mCommonDialog);
    }

    public interface OnCommonLongClickListener {
        void onLongClick(TreatmentAlarmDialog mCommonDialog);
    }


    public TreatmentAlarmDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
//        View view = View.inflate(context, R.layout.dialog_treatment_alarm, null);
//        Window window = this.getWindow();
//        window.setContentView(view);
//        ButterKnife.bind(this, view);
//
//        initView();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_treatment_alarm);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        initView();
    }


    /**
     * 初始化界面控件
     */
    private void initView() {
        this.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        this.setCancelable(false);//按返回键不能退出
        Window window = this.getWindow();
//        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation_style_left_and_right);
        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = ScreenUtil.dip2px(mContext, 564);
//        lp.height = ScreenUtil.dip2px(mContext, 301);
        lp.gravity = Gravity.CENTER;//设置dialog 布局中的位置
        window.setAttributes(lp);

    }

    @Override
    public void show() {
        super.show();
        refreshView();
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
        }

        if (!TextUtils.isEmpty(mStatus)) {
            tvStatus.setText(mStatus);
            tvStatus.setVisibility(View.VISIBLE);
        } else {
            tvStatus.setVisibility(View.GONE);
        }

        //设置右上角按钮的文字
//        if (!TextUtils.isEmpty(viewDataTxt)) {
//            btnViewData.setText(viewDataTxt);
////            updateTextSize(btnViewData, viewDataTxt);
//            btnViewData.setVisibility(View.VISIBLE);
//        } else {
//            btnViewData.setVisibility(View.GONE);
//        }

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
        btnFirst.setVisibility(isBtnFirst?View.GONE:View.VISIBLE);
        btnTwo.setVisibility(isBtnTwo?View.GONE:View.VISIBLE);
        btnThree.setVisibility(isBtnThree ?View.GONE:View.VISIBLE);
        //设置第三个按钮的文字
        if (!TextUtils.isEmpty(threeTxt)) {
            btnThree.setText(threeTxt);
            updateTextSize(btnThree, threeTxt);
            btnThree.setVisibility(View.VISIBLE);
        } else {
            btnThree.setVisibility(View.GONE);
        }

        if (imageResId != -1) {
            ivTop.setImageResource(imageResId);
            ivTop.setVisibility(View.VISIBLE);
        } else {
            ivTop.setVisibility(View.GONE);
        }

        if (viewDataDrawableId != -1) {
            btnViewData.setBackgroundResource(viewDataDrawableId);
        }
        if (firstDrawableId != -1) {
            btnFirst.setBackgroundResource(firstDrawableId);
        }
        if (twoDrawableId != -1) {
            btnTwo.setBackgroundResource(twoDrawableId);
        }
        if (threeDrawableId != -1) {
            btnThree.setBackgroundResource(threeDrawableId);
        }


        if (msgTxtColorId != -1) {
            tvMsg.setTextColor(msgTxtColorId);
        }
        if (viewDataTxtColorId != -1) {
            btnViewData.setTextColor(viewDataTxtColorId);
        }
        if (firstTxtColorId != -1) {
            btnFirst.setTextColor(firstTxtColorId);
        }
        if (twoTxtColorId != -1) {
            btnTwo.setTextColor(twoTxtColorId);
        }
        if (threeTxtColorId != -1) {
            btnThree.setTextColor(threeTxtColorId);
        }


        if (isCountdown && timeCount == null) {
            timeCount = new TimeCount(millisInFuture, 1000);
            timeCount.start();
        }
    }

    @OnClick({R.id.btn_view_data, R.id.btn_first, R.id.btn_two, R.id.btn_three,R.id.btnSilencers})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_view_data://右上角按钮
                if (mViewDataClickListener != null) {
                    mViewDataClickListener.onClick(TreatmentAlarmDialog.this);
                }
                break;
            case R.id.btn_first://第一个按钮
                if (mFirstClickListener != null) {
                    mFirstClickListener.onClick(TreatmentAlarmDialog.this);
                }
                break;
            case R.id.btn_two://第二个按钮
                if (mTwoClickListener != null) {
                    mTwoClickListener.onClick(TreatmentAlarmDialog.this);
                }
                break;
            case R.id.btn_three://第三个按钮
                if (mThreeClickListener != null) {
                    mThreeClickListener.onClick(TreatmentAlarmDialog.this);
                }
                break;
            case R.id.btnSilencers:
                if (silencersClickListener != null) {
                    silencersClickListener.onClick(TreatmentAlarmDialog.this);
                }
                break;
        }

    }

    @OnLongClick({R.id.btn_first, R.id.btn_two, R.id.btn_three})
    public boolean onViewLongClick(View view) {
        switch (view.getId()) {
            case R.id.btn_first://第一个按钮
                if (mFirstLongClickListener != null) {
                    mFirstLongClickListener.onLongClick(TreatmentAlarmDialog.this);
                }
                break;
            case R.id.btn_two://第二个按钮
                if (mTwoLongClickListener != null) {
                    mTwoLongClickListener.onLongClick(TreatmentAlarmDialog.this);
                }
                break;
            case R.id.btn_three://第三个按钮
                if (mThreeLongClickListener != null) {
                    mThreeLongClickListener.onLongClick(TreatmentAlarmDialog.this);
                }
                break;
        }
        return false;
    }


    public String getMessage() {
        return message;
    }

    public TreatmentAlarmDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public TreatmentAlarmDialog setMessage(String message, int msgTxtColorId) {
        this.message = message;
        this.msgTxtColorId = msgTxtColorId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TreatmentAlarmDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public TreatmentAlarmDialog setStatus(String status) {
        this.mStatus = status;
        return this;
    }

    public TreatmentAlarmDialog setBtnViewDataTxt(String viewDataTxt) {
        this.viewDataTxt = viewDataTxt;
        return this;
    }

    public TreatmentAlarmDialog setBtnFirstTxt(String firstTxt) {
        this.firstTxt = firstTxt;
        return this;
    }


    public TreatmentAlarmDialog setBtnTwoTxt(String twoTxt) {
        this.twoTxt = twoTxt;
        return this;
    }
    public TreatmentAlarmDialog setBtnTwoGone(boolean visible) {
        this.isBtnTwo = visible;
        return this;
    }
    public TreatmentAlarmDialog setBtnOneGone(boolean visible) {
        this.isBtnFirst = visible;
        return this;
    }
    public TreatmentAlarmDialog setBtnThreeGone(boolean visible) {
        this.isBtnThree = visible;
        return this;
    }

    public TreatmentAlarmDialog setBtnThreeTxt(String threeTxt) {
        this.threeTxt = threeTxt;
        return this;
    }

    public TreatmentAlarmDialog setBtnViewData(String viewDataTxt, int viewDataDrawableId) {
        this.viewDataTxt = viewDataTxt;
        this.viewDataDrawableId = viewDataDrawableId;
        return this;
    }

    public TreatmentAlarmDialog setBtnFirst(String firstTxt, int firstDrawableId) {
        this.firstTxt = firstTxt;
        this.firstDrawableId = firstDrawableId;
        return this;
    }


    public TreatmentAlarmDialog setBtnTwo(String twoTxt, int twoDrawableId) {
        this.twoTxt = twoTxt;
        this.twoDrawableId = twoDrawableId;
        return this;
    }

    public TreatmentAlarmDialog setBtnThree(String threeTxt, int threeDrawableId) {
        this.threeTxt = threeTxt;
        this.threeDrawableId = threeDrawableId;
        return this;
    }


    public TreatmentAlarmDialog setBtnFirst(String firstTxt, int firstTxtColorId, int firstDrawableId) {
        this.firstTxt = firstTxt;
        this.firstTxtColorId = firstTxtColorId;
        this.firstDrawableId = firstDrawableId;
        return this;
    }


    public TreatmentAlarmDialog setBtnTwo(String twoTxt, int twoTxtColorId, int twoDrawableId) {
        this.twoTxt = twoTxt;
        this.twoTxtColorId = twoTxtColorId;
        this.twoDrawableId = twoDrawableId;
        return this;
    }

    public TreatmentAlarmDialog setBtnThree(String threeTxt, int threeTxtColorId, int threeDrawableId) {
        this.threeTxt = threeTxt;
        this.threeTxtColorId = threeTxtColorId;
        this.threeDrawableId = threeDrawableId;
        return this;
    }


    public int getImageResId() {
        return imageResId;
    }


    public TreatmentAlarmDialog setImageResId(int imageResId) {
        this.imageResId = imageResId;
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
     * 设置右上角"查看数据"按钮点监听 单点
     *
     * @param listener
     * @return
     */
    public TreatmentAlarmDialog setViewDataClickListener(OnCommonClickListener listener) {
        mViewDataClickListener = listener;
        return this;
    }

    public TreatmentAlarmDialog silencersClickListener(OnCommonClickListener listener) {
        silencersClickListener = listener;
        return this;
    }

    /**
     * 设置第一个按钮点监听 单点
     *
     * @param listener
     * @return
     */
    public TreatmentAlarmDialog setFirstClickListener(OnCommonClickListener listener) {
        mFirstClickListener = listener;
        return this;
    }

    /**
     * 设置第二个按钮点监听 单点
     *
     * @param listener
     * @return
     */
    public TreatmentAlarmDialog setTwoClickListener(OnCommonClickListener listener) {
        mTwoClickListener = listener;
        return this;
    }

    /**
     * 设置第三个按钮点监听 单点
     *
     * @param listener
     * @return
     */
    public TreatmentAlarmDialog setThreeClickListener(OnCommonClickListener listener) {
        mThreeClickListener = listener;
        return this;
    }

    /**
     * 设置第一个按钮点监听 长按
     *
     * @param listener
     * @return
     */
    public TreatmentAlarmDialog setFirstLongClickListener(OnCommonLongClickListener listener) {
        mFirstLongClickListener = listener;
        return this;
    }

    /**
     * 设置第二个按钮点监听 长按
     *
     * @param listener
     * @return
     */
    public TreatmentAlarmDialog setTwoLongClickListener(OnCommonLongClickListener listener) {
        mTwoLongClickListener = listener;
        return this;
    }

    /**
     * 设置第三个按钮点监听 长按
     *
     * @param listener
     * @return
     */
    public TreatmentAlarmDialog setThreeLongClickListener(OnCommonLongClickListener listener) {
        mThreeLongClickListener = listener;
        return this;
    }

    public TreatmentAlarmDialog setCountdown(boolean countdown, int _second) {
        isCountdown = countdown;
        millisInFuture = (long) _second * 1000;
        return this;
    }

    public class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvCountDown.setVisibility(View.VISIBLE);
            tvCountDown.setEnabled(false);
            tvCountDown.setText(" （" + millisUntilFinished / 1000 + "秒后关闭）");
        }

        @Override
        public void onFinish() {
            isCountdown = false;
            TreatmentAlarmDialog.this.dismiss();
        }

    }

    @Subscribe(code = RxBusCodeConfig.EVENT_TREATMENT_CLOSED_ALARM_DIALOG)
    public void closedAlarmDialog(String entity) {
        TreatmentAlarmDialog.this.dismiss();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RxBus.get().unRegister(this);
        if (timeCount != null) {
            timeCount.cancel();
            timeCount.onFinish();
            timeCount = null;
        }
    }
}