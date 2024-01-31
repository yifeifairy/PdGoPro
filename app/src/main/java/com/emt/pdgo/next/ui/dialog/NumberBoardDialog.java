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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.data.bean.TpdBean;
import com.pdp.rmmit.pdp.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

public class NumberBoardDialog extends Dialog {


    @BindView(R.id.tv_item_label)
    TextView tvItemLabel;
    @BindView(R.id.tv_board_value)
    TextView tvBoardValue;

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


    private OnDialogResultListener onDialogResultListener;
    private String oldValue = "";

    /*** 是否负数 */
    private boolean isMinus = false;
    /*** 是否整数 */
    private boolean isInteger = false;

    private double mValue = 0;

    private String mType = "";

    private String hint;
    private String title;

    public NumberBoardDialog(Context context, @NonNull String value, String type) {
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

        findView();
        initView();
        registerEvents();
        initViewData();

    }

    public NumberBoardDialog(Context context, @NonNull String title, int min, int max) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.oldValue = title;
        this.mType = title;
        this.isMinus = false;
        this.isInteger = true;
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
            mValue = Float.parseFloat(tempValue);
            isShowSure(mValue>= min && mValue <= max);
        } else {
            isShowSure(false);
        }
    }

    private void setTitleAndHint(String title, int min, int max) {
        tvItemLabel.setText(title);
//        tvBoardValue.setHint(mHint);
        int hintSize = this.context.getResources().getDimensionPixelOffset(R.dimen.number_board_input_hint_text_size);
        String hint = min + "  -  "+max;
//        Log.e("输入提示","hintSize:"+hintSize);
        SpannableString sHint = new SpannableString(hint);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintSize, true);//设置字体大小 true表示单位是sp
        sHint.setSpan(ass, 0, sHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBoardValue.setHint(new SpannedString(sHint));
    }


    public NumberBoardDialog(Context context, @NonNull String value, String type, boolean isMinus) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.oldValue = value;
        this.mType = type;
        this.isMinus = isMinus;

        View view = View.inflate(context, R.layout.number_board_layout, null);
        Window window = this.getWindow();
        window.setContentView(view);
        ButterKnife.bind(this, view);

        findView();
        initView();
        registerEvents();
        initViewData();
    }

    public NumberBoardDialog(Context context, @NonNull String value, String type, boolean isMinus, boolean isInteger) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.oldValue = value;
        this.mType = type;
        this.isMinus = isMinus;
        this.isInteger = isInteger;

        View view = View.inflate(context, R.layout.number_board_layout, null);
        Window window = this.getWindow();
        window.setContentView(view);
        ButterKnife.bind(this, view);

        findView();
        initView();
        registerEvents();
        initViewData();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
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
//        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation_style_left_and_right);
        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = ScreenUtil.dip2px(context, 602);
//        lp.height = ScreenUtil.dip2px(context, 368);

//        lp.width = ScreenUtil.getScreenHeight(context);
//        lp.height = ScreenUtil.getScreenWidth(context);
//        lp.height = ScreenUtil.getScreenWidth(context) * 8 / 10;
        lp.gravity = Gravity.CENTER;//设置dialog 布局中的位置
        window.setAttributes(lp);

        initValueList();

        btnItem10.setVisibility(isInteger ? View.INVISIBLE : View.VISIBLE);

        btnItem12.setVisibility(isMinus ? View.VISIBLE : View.GONE);

    }

    private void registerEvents() {


    }


    private void initViewData() {

//        tvBoardValue.setText(oldValue);

        setBoardHint();

        setBtnSure();

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
                    setBtnSure();
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
                setBtnSure();
                break;
            case R.id.btn_item11:// 数字 0

                if (TextUtils.isEmpty(mValue) || "0".equals(mValue)) {
                    tvBoardValue.setText("0");
                } else {
                    tvBoardValue.setText(mValue + "0");
                }
                setBtnSure();
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
                setBtnSure();
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
                }
            }
        } else {//还没输入
            tvBoardValue.setText(num);
        }
        setBtnSure();

    }

    private TpdBean mParameterEniity;
    private PerfusionParameterBean perfusionParameterBean;

//    private TreatmentParameterEniity getmParameterEniity() {
//        if (mParameterEniity == null) {
//            mParameterEniity = PdproHelper.getInstance().getTreatmentParameter();
//        }
//        return mParameterEniity;
//    }

    private void setBoardHint() {
        mParameterEniity = PdproHelper.getInstance().tpdBean();
//        drainParameterBean = PdproHelper.getInstance().getDrainParameterBean();
        perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();

        String title = "";
        String mHint = "";

        //工程师模式
        if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式
            title = "工程师模式";
            mHint = "";
        }

        if (mType.equals(PdGoConstConfig.RESET)) {//工程师模式
            title = "恢复出厂设置";
            mHint = "";
        }

        if (mType.equals(PdGoConstConfig.zeroClear)) {//工程师模式
            title = "清零";
            mHint = "";
        }

        //温控板参数设置
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_MAX_DIFFERENCE: //最大温差
                title = "最大温差";
                mHint = "0.5  -  2";
                break;
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_UP_LOW_DIFFERENCE: //上下回差
                title = "上下回差";
                mHint = "0  -  1";
                break;
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_MAX_HEAT_PLATE: //加热板温度上限
                title = "加热板温度上限";
                mHint = "40  -  60";
                break;
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_LOW_HEAT_PLATE: //加热板温度调低值
                title = "加热板温度调低值";
                mHint = "0  -  10";
                break;
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGBTN: //目标温度值
                title = "目标温度值";
                mHint = "34  -  40";
                break;
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGBTN_DIFFERENCE: //目标温度差
                title = "目标温度差";
                mHint = "0  -  2";
                break;
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TA_ADJUST: //Ta温度校正值
                title = "Ta温度校正值";
                mHint = "-2  -  2";
                break;
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TB_ADJUST: //Tb温度校正值
                title = "Tb温度校正值";
                mHint = "-2  -  2";
                break;
            case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TC_ADJUST: //Tc温度校正值
                title = "Tc温度校正值";
                mHint = "-2  -  2";
                break;
        }

        if (mType.equals(PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE)) {//预热界面 加热目标温度值
            title = "加热目标温度值";
            mHint = "34.0  -  40.0";
        }

        //传感器参数设置
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_WEIGH_PEELED_UPPER: //上位秤 去皮值
                title = "上位秤 去皮值";
                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.CHECK_TYPE_WEIGH_PEELED_LOWER: //下位秤 去皮值
                title = "下位秤 去皮值  : ";
                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_UPPER: //上位秤 称量系数
                title = "上位秤校准值";
                mHint = "0  -  999999";
                break;
            case PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_LOWER: //下位秤 称量系数
                title = "下位秤校准值";
                mHint = "0  -  999999";
                break;

        }

        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_PREHEAT: //下位秤 称量系数
                title = "预热重量";
                mHint = "0  -  999999";
                break;
        }

        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_EMPTYING_TIME: //排空时间 s :  8-15
                title = "第一阶段引流目标量";
                mHint = "20  -  150";
                break;
            case PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT: //预设增量1[上位秤增加重量X1克] g 范围 50-100
                title = "第二阶段引流目标量";
                mHint = "20  -  150";
                break;
            case PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT_LOSS: //预设减量 [上位秤减少重量X1克] g 范围 50-100
                title = "预冲完补液测速周期";
                mHint = "10  -  120";
                break;
            case PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT3: //预设减量 [上位秤减少重量X1克] g 范围 50-100
                title = "补液合格速度";
                mHint = "10  -  120";
                break;
        }

        //引流参数
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_DRAIN_TIME_INTERVAL: //流量测速 时间间隔
                title = "流量测速 时间间隔";
                mHint = "20  -  600";
                break;
            case PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE: //流量测速 阈值
                title = "流量测速 阈值";
                mHint = "5  -  200";
                break;
            case PdGoConstConfig.CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE: //0周期引流比例
                title = "0周期引流比例";
                mHint = "50  -  120";
                break;
            case PdGoConstConfig.CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE: //其他周期引流比例
                title = "其他周期引流比例";
                mHint = "50  -  120";
                break;
            case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM: //引流/灌注超时报警
                title = "引流/灌注超时报警";
                mHint = "0  -  600";
                break;
            case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME: ///引流辅助冲洗 量
                title = "引流辅助冲洗量";
                mHint = "30  -  200";
                break;
            case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER: //引流辅助冲洗 次数 ： 1-3改成0-3
                title = "引流辅助冲洗次数";
                mHint = "0  -  3";
                break;
            case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_LATENCY_TIME: //最末引流等待
                title = "最末引流等待";
                mHint = "0  -  60";
                break;
            case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL: //最末引流提醒间隔
                title = "最末引流提醒间隔";
                mHint = " 0  -  60 ";
                break;

            case PdGoConstConfig.drainFlowRate: //流量测速 时间间隔
                title = "引流速率";
                mHint = "5  -  600";
                break;
            case PdGoConstConfig.drainFlowPeriod: //流量测速 阈值
                title = "测速周期";
                mHint = "5  -  200";
                break;
            case PdGoConstConfig.drainPassRate: //0周期引流比例
                title = "通过率";
                mHint = "50  -  120";
                break;
            case PdGoConstConfig.finalDrainEmptyWaitTime: //其他周期引流比例
                title = "末次排空等待时间";
                mHint = "1  -  60";
                break;
            case PdGoConstConfig.VaccumDraintimes: //引流/灌注超时报警
                title = "负压引流次数";
                mHint = "1  -  3";
                break;
        }

        //灌注参数
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL: //流量测速 时间间隔
                title = "流量测速 时间间隔";
                mHint = "20  -  600";
                break;
            case PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE: //流量测速 阈值
                title = "流量测速 阈值";
                mHint = "5  -  200";
                break;
            case PdGoConstConfig.CHECK_TYPE_PERFUSION_MIN_WEIGHT: //加热袋最低重量允许
                title = "加热袋最低重量允许";
                mHint = "100  -  1000";
                break;
            case PdGoConstConfig.CHECK_TYPE_PERFUSION_ALLOW_ABDOMINAL_VOLUME: //是否允许最末灌注减去留腹量
                title = "是否允许最末灌注\n减去留腹量";
                mHint = "1000  -  3000";
                break;
            case PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE: //灌注最大警戒值
                title = "灌注最大警戒值";
                mHint = "50  -  5000";
                break;

            case PdGoConstConfig.perfuseFlowRate: //流量测速 时间间隔
                title = "灌注速率";
                mHint = "5  -  600";
                break;
            case PdGoConstConfig.perfuseFlowPeriod: //流量测速 阈值
                title = "测速周期";
                mHint = "50  -  100";
                break;
            case PdGoConstConfig.perfuseMaxVolume: //加热袋最低重量允许
                title = "最大灌注量";
                mHint = "1000  -  5000";
                break;
        }

        //补液参数
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL: //流量测速 时间间隔
                title = "流量测速 时间间隔";
                mHint = "20  -  600";
                break;
            case PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE: //流量测速 阈值
                title = "流量测速 阈值";
                mHint = "5  -  200";
                break;
            case PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE: //补液目标保护值
                title = "补液目标保护值";
                mHint = "100  -  500";
                break;
            case PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT: //启动补液的加热袋最低值
                title = "启动补液的\n加热袋最低值";
                mHint = "100  -  5000";
                break;

            case PdGoConstConfig.supplyFlowRate: //流量测速 时间间隔
                title = "补液速率";
                mHint = "5  -  600";
                break;
            case PdGoConstConfig.supplyFlowPeriod: //流量测速 阈值
                title = "测速周期";
                mHint = "5  -  200";
                break;
            case PdGoConstConfig.supplyProtectVolume: //补液目标保护值
                title = "补液目标保护量";
                mHint = "0  -  500";
                break;
            case PdGoConstConfig.supplyMinVolume: //启动补液的加热袋最低值
                title = "开始补液最低液量";
                mHint = "500  -  10000";
                break;
        }

        //其他参数
        switch (mType) {
            case PdGoConstConfig.TotalRemainder: //流量测速 时间间隔
                title = "液体总余量";
                mHint = "0  -  600";
                break;
            case PdGoConstConfig.PerfuseDecDrain: //流量测速 阈值
                title = "灌注减引流累积量";
                mHint = "0  -  600";
                break;
            case PdGoConstConfig.DprSuppThreshold: //补液目标保护值
                title = "补液阀值";
                mHint = "0  -  600";
                break;
            case PdGoConstConfig.DprFlowPeriod: //启动补液的加热袋最低值
                title = "测速周期";
                mHint = "0  -  600";
                break;

            case PdGoConstConfig.FaultTime: //流量测速 时间间隔
                title = "故障判断周期";
                mHint = "0  -  600";
                break;

            case PdGoConstConfig.VALUE_TEST:
                title = "测速周期";
                mHint = "10  -  600";
                break;
        }


        //治疗之前身体数据
        switch (mType) {

            case PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_RECOMMENDED_WEIGHT: //医生建议体重
                title = "建议体重";
                mHint = " 1  -  500 ";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_WEIGHT: //治疗前体重
                title = "治疗前体重";
                mHint = "1  -  500";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_DIASTOLIC_BLOOD_PRESSURE: //治疗前舒张压
                title = "治疗前舒张压";
                mHint = "30  -  150";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_SYSTOLIC_BLOOD_PRESSURE: //治疗前收缩压
                title = "治疗前收缩压";
                mHint = "50  -  220";// (应大于舒张压)

                break;
        }

        //输入处方数据
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL: //腹透液总量 1000  -  30000  (只能是500的整倍数)
                title = "腹透液总量";
//                mHint = "1000  -  "+(13 * getmParameterEniity().perCyclePerfusionVolume + 500);
                mHint = "1000  -  500000";
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME: //每周期灌注量
                title = "每周期灌注量";
//                if (getmParameterEniity().firstPerfusionVolume == 0) {
//                    mHint = (int)((getmParameterEniity().peritonealDialysisFluidTotal-500) / 13)+"  -  "+getmParameterEniity().peritonealDialysisFluidTotal;
//                } else {
//                    mHint = (int)((getmParameterEniity().peritonealDialysisFluidTotal-500) / 13)+"  -  " +getmParameterEniity().firstPerfusionVolume;
//                }
//                if (mParameterEniity.firstPerfusionVolume == 0) {
//                    mHint = "0  -  5000";
//                } else {
//                    mHint = "0  -  " +mParameterEniity.firstPerfusionVolume;
//                }
//                mHint = "0  -  5000";
                int v = MyApplication.FIRST_VOL == 0
                        ?perfusionParameterBean.perfMaxWarningValue:MyApplication.FIRST_VOL;
                mHint = "0  -  " + v;
//                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.TPD_CYCLE_VOL: //每周期灌注量
                title = "每周期灌注量";
//                if (getmParameterEniity().firstPerfusionVolume == 0) {
//                    mHint = (int)((getmParameterEniity().peritonealDialysisFluidTotal-500) / 13)+"  -  "+getmParameterEniity().peritonealDialysisFluidTotal;
//                } else {
//                    mHint = (int)((getmParameterEniity().peritonealDialysisFluidTotal-500) / 13)+"  -  " +getmParameterEniity().firstPerfusionVolume;
//                }
//                if (MyApplication.TPD_FIRST_VOL == 0) {
//                    mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
//                } else {
//                    mHint = "0  -  " + MyApplication.TPD_FIRST_VOL == 0
//                ?perfusionParameterBean.perfMaxWarningValue:PdproHelper.getInstance().tpdBean().firstPerfusionVolume;
//                }
                int vol = MyApplication.TPD_FIRST_VOL == 0
                        ?perfusionParameterBean.perfMaxWarningValue:MyApplication.TPD_FIRST_VOL;
                mHint = "0  -  " + vol;
//                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES: //循环治疗周期数
                title = "循环治疗周期数";//N=int((腹透液重量-首次灌注量-消耗扣除-最末留腹量)/每周期灌注量)
                mHint = "1  -  100";
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //TPD首次灌注量
                title = "首次灌注量";
                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
//            case PdGoConstConfig.TPD_FIRST_VOL: //TPD首次灌注量
//                title = "TPD首次灌注量";
//                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
//                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME: //留腹时间
                title = "留腹时间";
                mHint = "1  -  600";
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY: //最末留腹量
                title = "末次留腹量";
                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME: //上次留腹量
                title = "上次留腹量";
                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME: //预估超滤量
                title = "预估超滤量";
                mHint = "0  -  3000";
                break;

            case PdGoConstConfig.FINAL_SUPPLY: //预估超滤量
                title = "末袋补液量";
                mHint = "0  -  3000";
                break;

            case PdGoConstConfig.DPR_TOTAL_AMOUNT:
                title = "治疗总量";
                mHint = EmtConstant.DPR_TOTAL_AMOUNT_MIN+ "  -  "+EmtConstant.DPR_TOTAL_AMOUNT_MAX;
                break;
            case PdGoConstConfig.DPR_TOTAL_TIME:
                title = "治疗总时间";
                mHint = EmtConstant.DPR_TOTAL_TIME_MIN+"  -  "+EmtConstant.DPR_TOTAL_TIME_MAX;
                break;
            case PdGoConstConfig.DPR_TREAT_DURATION:
                title = "定时补液时间";
                mHint = EmtConstant.DPR_TREAT_DURATION_MIN+"  -  "+EmtConstant.DPR_TREAT_DURATION_MAX;
                break;
            case PdGoConstConfig.DPR_PER_RATE:
                title = "持续灌注速率";
//                if (prescription.drain_rate == 0) {
//                    mHint = EmtConstant.DPR_PER_RATE_MIN + "  -  " + EmtConstant.DPR_PER_RATE_MAX;
//                } else {
//                    mHint = EmtConstant.DPR_PER_RATE_MIN + "  -  " + prescription.drain_rate;
//                }
                mHint = EmtConstant.DPR_PER_RATE_MIN + "  -  " + EmtConstant.DPR_PER_RATE_MAX;
                break;
            case PdGoConstConfig.DPR_LEAVE_BELLY:
                title = "持续留腹量";
                mHint = EmtConstant.DPR_LEAVE_BELLY_MIN+"  -  "+MyApplication.firstVol;
                break;
            case PdGoConstConfig.DPR_DRAIN_RATE:
                title = "持续引流速率";
//                if (prescription.perfuse_rate == 0) {
//                    mHint = EmtConstant.DPR_DRAIN_RATE_MIN + "  -  " + EmtConstant.DPR_DRAIN_RATE_MAX;
//                } else {
//                    mHint = prescription.perfuse_rate + "  -  " + EmtConstant.DPR_DRAIN_RATE_MAX;
//                }
                mHint = EmtConstant.DPR_DRAIN_RATE_MIN + "  -  " + EmtConstant.DPR_DRAIN_RATE_MAX;
                break;
            case PdGoConstConfig.DPR_BELLY_EMPTY:
                title = "腹腔排空时间";
                mHint = EmtConstant.DPR_BELLY_EMPTY_MIN+"  -  "+EmtConstant.DPR_BELLY_EMPTY_MAX;;
                break;
            case PdGoConstConfig.DPR_FIRST_PER:
                title = "首次灌注量";
                mHint = EmtConstant.DPR_FIRST_PER_MIN+"  -  "+EmtConstant.DPR_FIRST_PER_MAX;;
                break;
            case PdGoConstConfig.DPR_TARGET_TEMP:
                title = "目标温度";
                mHint = EmtConstant.DPR_TARGET_TEMP_MIN+"  -  "+EmtConstant.DPR_TARGET_TEMP_MAX;;
                break;
            case PdGoConstConfig.DPR_LIMIT_TEMP:
                title = "下限温度";
                mHint = EmtConstant.DPR_LIMIT_TEMP_MIN+"  -  "+EmtConstant.DPR_LIMIT_TEMP_MAX;;
                break;
            case PdGoConstConfig.DPR_FIX_TIME:
                title = "混合等待时间";
                mHint = EmtConstant.DPR_FIX_TIME_MIN+"  -  "+EmtConstant.DPR_FIX_TIME_MAX;
                break;
            case PdGoConstConfig.DPR_NOR_VOL:
                title = "普通液治疗量";
                mHint = EmtConstant.DPR_NOR_VOL_MIN+"  -  "+EmtConstant.DPR_NOR_VOL_MAX;;
                break;
            case PdGoConstConfig.DPR_PER_VOL:
                title = "渗透液治疗量";
                mHint = EmtConstant.DPR_PER_VOL_MIN+"  -  "+EmtConstant.DPR_PER_VOL_MAX;;
                break;
            case PdGoConstConfig.DPR_OSM:
                title = "渗透压";
                mHint = EmtConstant.DPR_OSM_MIN+"  -  "+EmtConstant.DPR_OSM_MAX;;
                break;
            case PdGoConstConfig.DPR_INTER_TIME:
                title = "间隔时间";
                mHint = EmtConstant.DPR_INTER_TIME_MIN+"  -  "+EmtConstant.DPR_INTER_TIME_MAX;;
                break;

            case PdGoConstConfig.DPR_NUM_TIME:
                title = "次数";
                mHint = EmtConstant.DPR_NUM_TIME_MIN+"  -  "+EmtConstant.DPR_NUM_TIME_MAX;;
                break;

                // 治疗中
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL_TIMING: //腹透液总量 1000  -  30000  (只能是500的整倍数)
                title = "腹透液总量";
//                mHint = "1000  -  "+(13 * getmParameterEniity().perCyclePerfusionVolume + 500);
                mHint = "1000  -  "+mParameterEniity.peritonealDialysisFluidTotal;
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME_TIMING: //每周期灌注量
                title = "每周期灌注量";
//
//                if (mParameterEniity.firstPerfusionVolume == 0) {
//                    mHint = "0  -  5000";
//                } else {
//                    mHint = "0  -  " +mParameterEniity.firstPerfusionVolume;
//                }
//                mHint = "0  -  5000";
                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;

            case PdGoConstConfig.CFPD_LEAVE_BELLY:
                title = "持续留腹量";
                mHint = EmtConstant.DPR_LEAVE_BELLY_MIN+"  -  "+MyApplication.cfpdFirstVol;
                break;
            case PdGoConstConfig.CFPD_FIRST_PER:
                title = "首次灌注量";
                mHint = EmtConstant.DPR_FIRST_PER_MIN+"  -  "+EmtConstant.DPR_FIRST_PER_MAX;;
                break;

            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES_TIMING: //循环治疗周期数
                title = "循环治疗周期数";//N=int((腹透液重量-首次灌注量-消耗扣除-最末留腹量)/每周期灌注量)

                mHint = "1  -  100";
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME_TIMING: //TPD首次灌注量
                title = "首次灌注量";
                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME_TIMING: //留腹时间
                title = "留腹时间";
                mHint = "1  -  300";
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY_TIMING: //最末留腹量
                title = "末次留腹量";
                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME_TIMING: //上次留腹量
                title = "上次留腹量";
                mHint = "0  -  3000";
                break;
            case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME_TIMING: //预估超滤量
                title = "预估超滤量";
                mHint = "0  -  3000";
                break;
            case PdGoConstConfig.INCREASING_VOL:
                title = "递增灌注量";
                mHint = "1  -  3000";
                break;
            case PdGoConstConfig.INCREASING_TIME:
                title = "递增留腹时间";
                mHint = "1  -  240";
                break;

            case PdGoConstConfig.SPECIAL_SUPPLY_CYCLE:
                title = "补液2-循环周期";
                mHint = "1  -  100";
                break;
            case PdGoConstConfig.SPECIAL_SUPPLY_VOL:
                title = "补液2-周期灌注量";
                mHint = "1  -  30000";
                break;
            case PdGoConstConfig.BASE_SUPPLY_VOL:
                title = "补液1-周期灌注量";
                mHint = "1  -  30000";
                break;
            case PdGoConstConfig.BASE_SUPPLY_CYCLE:
                title = "补液1-循环周期数";
                mHint = "1  -  100";
                break;
        }

        switch (mType) {
            case "const_p1":
                title = "上位阀门PID比例常数";
                mHint = "0  -  100";
                break;
            case "const_i1":
                title = "上位阀门PID微分常数";
                mHint = "0  -  100";
                break;
            case "const_d1":
                title = "灌注控制积分常数";
                mHint = "0  -  100";
                break;
            case "const_p2":
                title = "下位阀门PID比例常数";
                mHint = "0  -  100";
                break;
            case "const_i2":
                title = "下位阀门PID微分常数";
                mHint = "0  -  100";
                break;
            case "const_d2":
                title = "引流控制积分常数";
                mHint = "0  -  100";
                break;
            case "saturate_bw":
                title = "上位阀速度误差阈值";
                mHint = "0  -  100";
                break;
            case "saturate_bw2":
                title = "下位阀速度误差阈值";
                mHint = "0  -  100";
                break;
        }

        // 儿童模式
        switch (mType) {
            case PdGoConstConfig.KID_TOTAL:
                title = "腹透液总量";
                mHint = "10  -  20000";
                break;
            case PdGoConstConfig.KID_CYCLE_VOL:
                title = "每周期灌注量";
                int vol = MyApplication.KID_FIRST_VOL == 0?
                        1000:MyApplication.KID_FIRST_VOL;
                mHint = "10  -  "+vol;
//                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.KID_CYCLE:
                title = "循环治疗周期数";
                mHint = "1  -  50";
                break;
            case PdGoConstConfig.KID_EST_ULT_VOL:
                title = "预估超滤量";
                mHint = "0  -  3000";
                break;
            case PdGoConstConfig.KID_FINAL_RETAIN_VOL:
                title = "末次留腹量";
                mHint = "0  -  3000";
                break;
            case PdGoConstConfig.KID_BSA:
                title = "体表面积";
                mHint = "0  -  8";
                break;
            case PdGoConstConfig.KID_FIRST_VOL:
                title = "首次灌注量";
                int firstVol = Math.min(perfusionParameterBean.perfMaxWarningValue, 1000);
                mHint = "20  -  "+firstVol;
                break;
            case PdGoConstConfig.KID_FINAL_SUPPLY_VOL:
                title = "末袋补液量";
                mHint = "0  -  3000";
                break;
            case PdGoConstConfig.KID_LAST_RETAIN_VOL:
                title = "上次留腹量";
                mHint = "0  -  3000";
                break;
            case PdGoConstConfig.KID_RETAIN_TIME:
                title = "留腹时间";
                mHint = "1  -  1200";
                break;
            case PdGoConstConfig.HEIGHT:
                title = "身高";
                mHint = "1  -  210";
                break;
            case PdGoConstConfig.WEIGHT:
                title = "体重";
                mHint = "1  -  100";
                break;
        }

        // 专家模式
        switch (mType) {
            case PdGoConstConfig.EXPERT_TOTAL:
                title = "腹透液总量";
                mHint = "10  -  500000";
                break;
            case PdGoConstConfig.EXPERT_CYCLE_VOL:
                title = "每周期灌注量";
//                mHint = "10  -  5000";
                mHint = "10  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.EXPERT_CYCLE:
                title = "循环治疗周期数";
                mHint = "1  -  50";
                break;
            case PdGoConstConfig.EXPERT_ULT_VOL:
                title = "预估超滤量";
                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.EXPERT_FINAL_RETAIN_VOL:
                title = "末次留腹量";
                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.EXPERT_RETAIN_TIME:
                title = "留腹时间";
                mHint = "1  -  1200";
                break;
//            case PdGoConstConfig.KID_FIRST_VOL:
//                title = "TPD首次灌注量";
//                mHint = "20  -  1000";
//                break;
            case PdGoConstConfig.EXPERT_FINAL_SUPPLY:
                title = "末袋补液量";
                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.EXPERT_LAST_RETAIN_VOL:
                title = "上次留腹量";
                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.EXPERT_BASE_SUPPLY_VOL:
                title = "通道1·周期灌注量";
                mHint = "10  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.EXPERT_BASE_SUPPLY_CYCLE:
                title = "通道1·灌注周期";
                mHint = "0  -  100";
                break;
            case PdGoConstConfig.EXPERT_OSM_SUPPLY_VOL:
                title = "通道2·周期灌注量";
                mHint = "10  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.EXPERT_OSM_SUPPLY_CYCLE:
                title = "通道2·灌注周期";
                mHint = "0  -  100";
                break;
        }

        //治疗参数
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_DRAIN_THRESHOLD_ZERO_CYCLE: //零周期引流阈值
                title = "零周期引流阈值";
                mHint = "50  -  120";
                break;
            case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_DRAIN_THRESHOLD_OTHER_CYCLE: //其他周期引流阈值
                title = "其他周期引流阈值";
                mHint = "50  -  120";
                break;
            case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_DRAIN_LAST_DELAY: //末次排空延迟
                title = "末次排空延迟";
                mHint = "0  -  60";
                break;
            case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_WAITING_INTERVA: //等待提醒间隔时间
                title = "等待提醒间隔时间";
                mHint = "0  -  60";
                break;
            case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_PERFUSION_WARNING_VALUE: //灌注警戒值
                title = "灌注警戒值";
                mHint = "50  -  5000";
                break;
        }


        //治疗中断反馈
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_FEEDBACK_DRAIN_VOLUME: //引流量
                title = "引流量";
                mHint = "0  -  3000";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_FEEDBACK_PERFUSION_VOLUME: //超滤量
                title = "灌注量";
                mHint = "0  -  3000";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_FEEDBACK_UFV: //超滤量
                title = "超滤量";
                mHint = "0  -  3000";
                break;
        }

        //治疗完成
        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_URINE_OUTPUT: //尿量 ml
                title = "尿量";
                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_WATER_INTAKE: //饮水量 ml
                title = "饮水量";
                mHint = "0  -  5000";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_FASTING_BLOOD_GLUCOSE: //空腹血糖
                title = "血糖";
                mHint = "0  -  1000";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_WEIGHT: //治疗后体重
                title = "治疗后体重";
                mHint = "1  -  500";
                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_SYSTOLIC_BLOOD_PRESSURE: //治疗后收缩压
                title = "治疗后收缩压";
                mHint = "50  -  220";//  (应大于舒张压)

                break;
            case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_DIASTOLIC_BLOOD_PRESSURE: //治疗后舒张压
                title = "治疗后舒张压";
                mHint = "30  -  150";
                break;
        }


        switch (mType) {
            case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_1: //体重差值1 kg
                title = "体重差值1";
                mHint = "1  -  5";
                break;
            case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_2: //体重差值2 kg
                title = "体重差值2";
                mHint = "1  -  5";
                break;
            case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_3: //体重差值3 kg
                title = "体重差值3";
                mHint = "1  -  5";
                break;
            case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_4:
                title = "倒计时2";
                mHint = "1  -  120";
                break;
            case PdGoConstConfig.AUTO_SLEEP:
                title = "息屏倒计时";
                mHint = "1  -  240(分)";
                break;
            case PdGoConstConfig.aApd_bag:
                title = "预热液量:";
                mHint = "100  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;

            case PdGoConstConfig.aApd_p1:
                title = "灌注量";
                mHint = "10  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;
            case PdGoConstConfig.aApd_p2:
            case PdGoConstConfig.aApd_p3:
            case PdGoConstConfig.aApd_p4:
            case PdGoConstConfig.aApd_p5:
            case PdGoConstConfig.aApd_p6:
                title = "灌注量";
                mHint = "0  -  "+perfusionParameterBean.perfMaxWarningValue;
                break;

            case PdGoConstConfig.aApd_r1:
            case PdGoConstConfig.aApd_r2:
            case PdGoConstConfig.aApd_r3:
            case PdGoConstConfig.aApd_r4:
            case PdGoConstConfig.aApd_r5:
            case PdGoConstConfig.aApd_r6:
                title = "留腹时间";
                mHint = "0  -  1200";
                break;

            case PdGoConstConfig.aApd_c1:
            case PdGoConstConfig.aApd_c2:
            case PdGoConstConfig.aApd_c3:
            case PdGoConstConfig.aApd_c4:
            case PdGoConstConfig.aApd_c5:
            case PdGoConstConfig.aApd_c6:
                title = "重复次数";
                mHint = "0  -  50";
                break;

        }


        tvItemLabel.setText(title);
//        tvBoardValue.setHint(mHint);
        int hintSize = this.context.getResources().getDimensionPixelOffset(R.dimen.number_board_input_hint_text_size);
//        Log.e("输入提示","hintSize:"+hintSize);
        SpannableString sHint = new SpannableString(mHint);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintSize, true);//设置字体大小 true表示单位是sp
        sHint.setSpan(ass, 0, sHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBoardValue.setHint(new SpannedString(sHint));

    }


    private void setBtnSure() {
        String tempValue = tvBoardValue.getText().toString().trim();
        if (!TextUtils.isEmpty(tempValue)) {
            if (isMinus && "-".equals(tempValue)) {//当只有负号时候
                isShowSure(false);
                return;
            }
//            Logger.e("输入： "+tempValue);
            mValue = Double.parseDouble(tempValue);

            if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式
                isShowSure(true);
            }

            if (mType.equals(PdGoConstConfig.RESET)) {//工程师模式
                isShowSure(true);
            }

            if (mType.equals(PdGoConstConfig.zeroClear)) {//工程师模式
                isShowSure(true);
            }

            // aApd
            switch (mType) {
                case PdGoConstConfig.aApd_bag:
                    isShowSure(mValue >= 100 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.aApd_p1:
                    isShowSure(mValue >= 10 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.aApd_p2:
                case PdGoConstConfig.aApd_p3:
                case PdGoConstConfig.aApd_p4:
                case PdGoConstConfig.aApd_p5:
                case PdGoConstConfig.aApd_p6:
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.aApd_r1:
                case PdGoConstConfig.aApd_r2:
                case PdGoConstConfig.aApd_r3:
                case PdGoConstConfig.aApd_r4:
                case PdGoConstConfig.aApd_r5:
                case PdGoConstConfig.aApd_r6:
                    isShowSure(mValue >= 0 & mValue <= 1200);
                    break;

                case PdGoConstConfig.aApd_c1:
                case PdGoConstConfig.aApd_c2:
                case PdGoConstConfig.aApd_c3:
                case PdGoConstConfig.aApd_c4:
                case PdGoConstConfig.aApd_c5:
                case PdGoConstConfig.aApd_c6:
                    isShowSure(mValue >= 0 & mValue <= 50);
                    break;
            }

            switch (mType) {
                case PdGoConstConfig.CHECK_TYPE_PREHEAT: //下位秤 称量系数
                    isShowSure(mValue >= 0 & mValue <= 999999);
                    break;
            }

            switch (mType) {
                case "const_p1":
                case "const_i1":
                case "const_d1":
                case "const_p2":
                case "const_i2":
                case "const_d2":
                case "saturate_bw":
                case "saturate_bw2":
                    isShowSure(mValue >= 0 & mValue <= 100);
                    break;
            }

            // 儿童模式
            switch (mType) {
                case PdGoConstConfig.KID_TOTAL:
                    isShowSure(mValue >= 10 & mValue <= 20000);
                    break;
                case PdGoConstConfig.KID_CYCLE_VOL:
                    int vol = MyApplication.KID_FIRST_VOL == 0?
                            1000:MyApplication.KID_FIRST_VOL;
                    isShowSure(mValue >= 10 & mValue <= vol);
                    break;
                case PdGoConstConfig.KID_CYCLE:
                    isShowSure(mValue >= 1 & mValue <= 50);
                    break;
                case PdGoConstConfig.KID_RETAIN_TIME:
                    isShowSure(mValue >= 1 & mValue <= 1200);
                    break;
                case PdGoConstConfig.KID_LAST_RETAIN_VOL:
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;
                case PdGoConstConfig.KID_FINAL_RETAIN_VOL:
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.KID_FINAL_SUPPLY_VOL:
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;
                case PdGoConstConfig.KID_BSA:
                    isShowSure(mValue >= 0 & mValue <= 8);
                    break;
                case PdGoConstConfig.KID_FIRST_VOL:
                    int firstVol = Math.min(perfusionParameterBean.perfMaxWarningValue, 1000);
                    isShowSure(mValue >= 20 & mValue <= firstVol);
                    break;
                case PdGoConstConfig.KID_EST_ULT_VOL:
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;
                case PdGoConstConfig.HEIGHT:
                    isShowSure(mValue >= 1 & mValue <= 210);
                    break;
                case PdGoConstConfig.WEIGHT:
                    isShowSure(mValue >= 1 & mValue <= 100);
                    break;
            }

            // 专家模式
            switch (mType) {
                case PdGoConstConfig.EXPERT_TOTAL:
                    isShowSure(mValue >= 10 & mValue <= 500000);
                    break;
                case PdGoConstConfig.EXPERT_CYCLE_VOL:
                    isShowSure(mValue >= 10 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.EXPERT_CYCLE:
                    isShowSure(mValue >= 1 & mValue <= 50);
                    break;
                case PdGoConstConfig.EXPERT_ULT_VOL:
                    isShowSure(mValue >= 0 & mValue <= 5000);
                    break;
                case PdGoConstConfig.EXPERT_FINAL_RETAIN_VOL:
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.EXPERT_RETAIN_TIME:
                    isShowSure(mValue >= 1 & mValue <= 1200);
                    break;
//                case PdGoConstConfig.KID_FIRST_VOL:
//                    isShowSure(mValue >= 0 & mValue <= 100);
//                    break;
                case PdGoConstConfig.EXPERT_FINAL_SUPPLY:
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.EXPERT_LAST_RETAIN_VOL:
                    isShowSure(mValue >= 0 & mValue <= 5000);
                    break;
                case PdGoConstConfig.EXPERT_BASE_SUPPLY_VOL:
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.EXPERT_BASE_SUPPLY_CYCLE:
                    isShowSure(mValue >= 0 & mValue <= 100);
                    break;
                case PdGoConstConfig.EXPERT_OSM_SUPPLY_VOL:
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.EXPERT_OSM_SUPPLY_CYCLE:
                    isShowSure(mValue >= 0 & mValue <= 100);
                    break;
            }

            //温控板参数设置
            switch (mType) {
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_MAX_DIFFERENCE: //最大温差  :  0.5-2
                    isShowSure(mValue >= 0.5f & mValue <= 2f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_UP_LOW_DIFFERENCE: //上下回差  :  0-1
                    isShowSure(mValue >= 0 & mValue <= 1f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_MAX_HEAT_PLATE: //加热板温度上限  :  40-60
                    isShowSure(mValue >= 40f & mValue <= 60f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_LOW_HEAT_PLATE: //加热板温度调低值  : 1-10
                    isShowSure(mValue >= 1f & mValue <= 10f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGBTN: //目标温度值  :  35-40
                    isShowSure(mValue >= 34f & mValue <= 40f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGBTN_DIFFERENCE: //目标温度差  :  0-2
                    isShowSure(mValue >= 0 & mValue <= 2f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TA_ADJUST: //Ta温度校正值  :  -1 -1
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TB_ADJUST: //Tb温度校正值  :  -1 -1
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TC_ADJUST: //Tc温度校正值  :  -1 -1
                    isShowSure(mValue >= -2f & mValue <= 2f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TEMPERATURE_TARGET_TEMPERATURE: //预热界面 加热目标温度值
                    isShowSure(mValue >= 34.0f & mValue <= 40.0f);
                    break;

                //传感器参数设置
                case PdGoConstConfig.CHECK_TYPE_WEIGH_PEELED_UPPER: //上位秤 去皮值
                case PdGoConstConfig.CHECK_TYPE_WEIGH_PEELED_LOWER: //下位秤 去皮值
                    isShowSure(mValue >= 1 & mValue <= 5000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_UPPER: //上位秤 称量系数
                case PdGoConstConfig.CHECK_TYPE_WEIGH_COEFF_LOWER: //下位秤 称量系数
                    isShowSure(mValue >= 0 & mValue <= 999999);
                    break;
                case PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_EMPTYING_TIME: //排空时间 s :  5-15
                    isShowSure(mValue >= 20 & mValue <= 150);
                    break;
                case PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT:
                    isShowSure(mValue >= 20 & mValue <= 150);
                    break;
                case PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT3: //预设增量1[上位秤增加重量X1克] g 范围 50-100
                case PdGoConstConfig.CHECK_TYPE_FIRST_RINSE_PRESET_WEIGHT_LOSS: //预设减量 [上位秤减少重量X1克] g 范围 50-100
                    isShowSure(mValue >= 10 & mValue <= 120);
                    break;
                case PdGoConstConfig.CHECK_TYPE_DRAIN_ZERO_CYCLE_PERCENTAGE: //0周期引流比例
                case PdGoConstConfig.CHECK_TYPE_DRAIN_OTHER_CYCLE_PERCENTAGE: //其他周期引流比例
                    isShowSure(mValue >= 50 & mValue <= 100);
                    break;
                //引流参数
                case PdGoConstConfig.CHECK_TYPE_DRAIN_TIME_INTERVAL: //流量测速 时间间隔
                    isShowSure(mValue >= 20 & mValue <= 600);
                    break;
                case PdGoConstConfig.CHECK_TYPE_DRAIN_THRESHOLD_VALUE: //流量测速 阈值
                    isShowSure(mValue >= 5 & mValue <= 200);
                    break;
                case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_TIMEOUT_ALARM: //引流/灌注超时报警
                    isShowSure(mValue >= 0 & mValue <= 600);
                    break;
                case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_VOLUME: ///引流辅助冲洗 量
                    isShowSure(mValue >= 30 & mValue <= 200);
                    break;
                case PdGoConstConfig.CHECK_TYPE_DRAIN_RINSE_NUMBER: //引流辅助冲洗 次数 1-3改成0-3
                    isShowSure(mValue >= 0 & mValue <= 3);
                    break;
                case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_LATENCY_TIME: //最末引流等待
                    isShowSure(mValue >= 0 & mValue <= 60);
                    break;
                case PdGoConstConfig.CHECK_TYPE_DRAIN_UNIT_WARN_TIME_INTERVAL: //最末引流提醒间隔
                    isShowSure(mValue >= 0 & mValue <= 60);
                    break;

                case PdGoConstConfig.drainFlowRate: //引流/灌注超时报警
                    isShowSure(mValue >= 5 & mValue <= 600);
                    break;
                case PdGoConstConfig.drainFlowPeriod: ///引流辅助冲洗 量
                    isShowSure(mValue >= 5 & mValue <= 200);
                    break;
                case PdGoConstConfig.drainPassRate: //引流辅助冲洗 次数 1-3改成0-3
                    isShowSure(mValue >= 50 & mValue <= 120);
                    break;
                case PdGoConstConfig.finalDrainEmptyWaitTime: //最末引流等待
                    isShowSure(mValue >= 1 & mValue <= 60);
                    break;
                case PdGoConstConfig.VaccumDraintimes: //最末引流提醒间隔
                    isShowSure(mValue >= 1 & mValue <= 3);
                    break;

                //灌注参数
                case PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL: //流量测速 时间间隔
                    isShowSure(mValue >= 20 & mValue <= 600);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE: //流量测速 阈值
                    isShowSure(mValue >= 5 & mValue <= 200);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PERFUSION_MIN_WEIGHT: //加热袋最低重量允许
                    isShowSure(mValue >= 100 & mValue <= 1000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PERFUSION_ALLOW_ABDOMINAL_VOLUME: //是否允许最末灌注减去留腹量
                    isShowSure(mValue >= 60 & mValue <= 600);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE: //灌注最大警戒值
                    isShowSure(mValue >= 50 & mValue <= 5000);
                    break;

                case PdGoConstConfig.perfuseFlowRate: //加热袋最低重量允许
                    isShowSure(mValue >= 5 & mValue <= 600);
                    break;
                case PdGoConstConfig.perfuseFlowPeriod: //是否允许最末灌注减去留腹量
                    isShowSure(mValue >= 50 & mValue <= 100);
                    break;
                case PdGoConstConfig.perfuseMaxVolume: //灌注最大警戒值
                    isShowSure(mValue >= 1000 & mValue <= 5000);
                    break;
                //补液参数
                case PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL: //流量测速 时间间隔
                    isShowSure(mValue >= 20 & mValue <= 600);
                    break;
                case PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE: //流量测速 阈值
                    isShowSure(mValue >= 5 & mValue <= 200);
                    break;
                case PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE: //补液目标保护值
                    isShowSure(mValue >= 100 & mValue <= 500);
                    break;
                case PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT: //启动补液的加热袋最低值
                    isShowSure(mValue >= 100 & mValue <= 5000);
                    break;

                case PdGoConstConfig.supplyFlowRate: //流量测速 时间间隔
                    isShowSure(mValue >= 5 & mValue <= 600);
                    break;
                case PdGoConstConfig.supplyFlowPeriod: //流量测速 阈值
                    isShowSure(mValue >= 5 & mValue <= 200);
                    break;
                case PdGoConstConfig.supplyProtectVolume: //补液目标保护值
                    isShowSure(mValue >= 0 & mValue <= 500);
                    break;
                case PdGoConstConfig.supplyMinVolume: //启动补液的加热袋最低值
                    isShowSure(mValue >= 500 & mValue <= 10000);
                    break;

                //输入处方数据
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL: //腹透液总量
//                    isShowSure(mValue >= 1000 & mValue <= (13 * getmParameterEniity().perCyclePerfusionVolume + 500));
                    isShowSure(mValue >= 1000 & mValue <= 500000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME: //每周期灌注量
                    //mValue >= 0 & mValue <= 3000
//                    int max;
//                    if (getmParameterEniity().firstPerfusionVolume == 0) {
//                        max = getmParameterEniity().peritonealDialysisFluidTotal;
//                    } else {
//                        max = getmParameterEniity().firstPerfusionVolume;
//                    }
//                    Log.e("處方界面","首次灌注量--"+getmParameterEniity().firstPerfusionVolume
//                    +"--max--"+max);
//                    isShowSure(mValue >= (int)((getmParameterEniity().peritonealDialysisFluidTotal-500) / 13) & mValue <= max);
//                    if (mParameterEniity.firstPerfusionVolume != 0) {
//                        isShowSure(mValue >= 0 & mValue <= mParameterEniity.firstPerfusionVolume);
//                    } else {
//                        isShowSure(mValue >= 0 & mValue <= 5000);
//                    }
                    int v = MyApplication.FIRST_VOL == 0
                            ?perfusionParameterBean.perfMaxWarningValue:MyApplication.FIRST_VOL;
                    isShowSure(mValue >= 0 & mValue <= v);
                    break;
                case PdGoConstConfig.TPD_CYCLE_VOL: //每周期灌注量
//                    mValue >= 0 & mValue <= 3000
//                    int max;
//                    if (getmParameterEniity().firstPerfusionVolume == 0) {
//                        max = getmParameterEniity().peritonealDialysisFluidTotal;
//                    } else {
//                        max = getmParameterEniity().firstPerfusionVolume;
//                    }
//                    Log.e("處方界面","首次灌注量--"+getmParameterEniity().firstPerfusionVolume
//                    +"--max--"+max);
//                    isShowSure(mValue >= (int)((getmParameterEniity().peritonealDialysisFluidTotal-500) / 13) & mValue <= max);
                    int vol = MyApplication.TPD_FIRST_VOL == 0
                            ?perfusionParameterBean.perfMaxWarningValue:MyApplication.TPD_FIRST_VOL;
//                    if (mParameterEniity.firstPerfusionVolume != 0) {
//                        isShowSure(mValue >= 0 & mValue <= mParameterEniity.firstPerfusionVolume);
//                    } else {
//                        isShowSure(mValue >= 0 & mValue <= 5000);
//                    }
                    isShowSure(mValue >= 0 & mValue <= vol);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES: //循环治疗周期数
                    isShowSure(mValue >= 1 & mValue <= 100);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //TPD首次灌注量
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME: //留腹时间
                    isShowSure(mValue >= 1 & mValue <= 600);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY: //最末留腹量
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME: //上次留腹量
                    isShowSure(mValue >= 0 & mValue <= 5000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME: //预估超滤量
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;
                case PdGoConstConfig.FINAL_SUPPLY: //预估超滤量
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;
                case PdGoConstConfig.CFPD_FIRST_PER:
                    isShowSure(mValue >= EmtConstant.DPR_FIRST_PER_MIN & mValue <= EmtConstant.DPR_FIRST_PER_MAX);
                    break;
                case PdGoConstConfig.CFPD_LEAVE_BELLY:
                    isShowSure(mValue >= EmtConstant.DPR_LEAVE_BELLY_MIN & mValue <= MyApplication.cfpdFirstVol);
                    break;
                case PdGoConstConfig.DPR_TOTAL_AMOUNT:
                    isShowSure(mValue >= EmtConstant.DPR_TOTAL_AMOUNT_MIN & mValue <= EmtConstant.DPR_TOTAL_AMOUNT_MAX);
                    break;
                case PdGoConstConfig.DPR_TOTAL_TIME:
                    isShowSure(mValue >= EmtConstant.DPR_TOTAL_TIME_MIN & mValue <= EmtConstant.DPR_TOTAL_TIME_MAX);
                    break;
                case PdGoConstConfig.DPR_TREAT_DURATION:
                    isShowSure(mValue >= EmtConstant.DPR_TREAT_DURATION_MIN & mValue <= EmtConstant.DPR_TREAT_DURATION_MAX);
                    break;
                case PdGoConstConfig.DPR_PER_RATE:
//                    if (prescription.drain_rate == 0) {
//                        isShowSure(mValue >= EmtConstant.DPR_PER_RATE_MIN & mValue <= EmtConstant.DPR_PER_RATE_MAX);
//                    } else {
//                        isShowSure(mValue >= EmtConstant.DPR_PER_RATE_MIN & mValue <= prescription.drain_rate);
//                    }
                    isShowSure(mValue >= EmtConstant.DPR_PER_RATE_MIN & mValue <= EmtConstant.DPR_PER_RATE_MAX);
                    break;
                case PdGoConstConfig.DPR_LEAVE_BELLY:
                    isShowSure(mValue >= EmtConstant.DPR_LEAVE_BELLY_MIN & mValue <= MyApplication.firstVol);
                    break;
                case PdGoConstConfig.DPR_DRAIN_RATE:
//                    if (prescription.perfuse_rate == 0) {
//                        isShowSure(mValue >= EmtConstant.DPR_PER_RATE_MIN & mValue <= EmtConstant.DPR_PER_RATE_MAX);
//                    } else {
//                        isShowSure(mValue >= prescription.perfuse_rate & mValue <= prescription.drain_rate);
//                    }
                    isShowSure(mValue >= EmtConstant.DPR_DRAIN_RATE_MIN & mValue <= EmtConstant.DPR_DRAIN_RATE_MAX);
                    break;
                case PdGoConstConfig.DPR_BELLY_EMPTY:
                    isShowSure(mValue >= EmtConstant.DPR_BELLY_EMPTY_MIN & mValue <= EmtConstant.DPR_BELLY_EMPTY_MAX);
                    break;

                case PdGoConstConfig.DPR_FIRST_PER:
                    isShowSure(mValue >= EmtConstant.DPR_FIRST_PER_MIN & mValue <= EmtConstant.DPR_FIRST_PER_MAX);
                    break;
                case PdGoConstConfig.DPR_TARGET_TEMP:
                    isShowSure(mValue >= EmtConstant.DPR_TARGET_TEMP_MIN & mValue <= EmtConstant.DPR_TARGET_TEMP_MAX);
                    break;

                case PdGoConstConfig.DPR_NOR_VOL:
                    isShowSure(mValue >= EmtConstant.DPR_NOR_VOL_MIN & mValue <= EmtConstant.DPR_NOR_VOL_MAX);
                    break;

                case PdGoConstConfig.DPR_PER_VOL:
                    isShowSure(mValue >= EmtConstant.DPR_PER_VOL_MIN & mValue <= EmtConstant.DPR_PER_VOL_MAX);
                    break;
                case PdGoConstConfig.DPR_OSM:
                    isShowSure(mValue >= EmtConstant.DPR_OSM_MIN & mValue <= EmtConstant.DPR_OSM_MAX);
                    break;
                case PdGoConstConfig.DPR_INTER_TIME:
                    isShowSure(mValue >= EmtConstant.DPR_INTER_TIME_MIN & mValue <= EmtConstant.DPR_INTER_TIME_MAX);
                    break;
                case PdGoConstConfig.DPR_NUM_TIME:
                    isShowSure(mValue >= EmtConstant.DPR_NUM_TIME_MIN & mValue <= EmtConstant.DPR_NUM_TIME_MAX);
                    break;
                case PdGoConstConfig.DPR_LIMIT_TEMP:
                    isShowSure(mValue >= EmtConstant.DPR_LIMIT_TEMP_MIN & mValue <= EmtConstant.DPR_LIMIT_TEMP_MAX);
                    break;
                case PdGoConstConfig.DPR_FIX_TIME:
                    isShowSure(mValue >= EmtConstant.DPR_FIX_TIME_MIN & mValue <= EmtConstant.DPR_FIX_TIME_MAX);
                    break;
                    // 治疗中输入处方
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL_TIMING: //腹透液总量
//                    isShowSure(mValue >= 1000 & mValue <= (13 * getmParameterEniity().perCyclePerfusionVolume + 500));
                    isShowSure(mValue >= 1000 & mValue <= mParameterEniity.peritonealDialysisFluidTotal);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME_TIMING: //每周期灌注量
                    //mValue >= 0 & mValue <= 3000
//                    int max;
//                    if (getmParameterEniity().firstPerfusionVolume == 0) {
//                        max = getmParameterEniity().peritonealDialysisFluidTotal;
//                    } else {
//                        max = getmParameterEniity().firstPerfusionVolume;
//                    }
//                    Log.e("處方界面","首次灌注量--"+getmParameterEniity().firstPerfusionVolume
//                    +"--max--"+max);
//                    isShowSure(mValue >= (int)((getmParameterEniity().peritonealDialysisFluidTotal-500) / 13) & mValue <= max);
                    if (mParameterEniity.firstPerfusionVolume != 0) {
                        isShowSure(mValue >= 0 & mValue <= mParameterEniity.firstPerfusionVolume);
                    } else {
                        isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    }
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES_TIMING: //循环治疗周期数
                    isShowSure(mValue >= 1 & mValue <= 100);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME_TIMING: //TPD首次灌注量
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME_TIMING: //留腹时间
                    isShowSure(mValue >= 1 & mValue <= 300);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY_TIMING: //最末留腹量
                    isShowSure(mValue >= 0 & mValue <= perfusionParameterBean.perfMaxWarningValue);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME_TIMING: //上次留腹量
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME_TIMING: //预估超滤量
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;

                //其他参数
//                switch (mType) {
                    case PdGoConstConfig.TotalRemainder: //流量测速 时间间隔
                        isShowSure(mValue >= 0 & mValue <= 600);
                        break;
                    case PdGoConstConfig.PerfuseDecDrain: //流量测速 阈值
                        isShowSure(mValue >= 0 & mValue <= 600);
                        break;
                    case PdGoConstConfig.DprSuppThreshold: //补液目标保护值
                        isShowSure(mValue >= 0 & mValue <= 600);
                        break;
                    case PdGoConstConfig.DprFlowPeriod: //启动补液的加热袋最低值
                        isShowSure(mValue >= 0 & mValue <= 600);
                        break;

                    case PdGoConstConfig.FaultTime: //流量测速 时间间隔
                        isShowSure(mValue >= 0 & mValue <= 600);
                        break;

                case PdGoConstConfig.INCREASING_TIME:
                    isShowSure(mValue >= 1 & mValue <= 240);
                    break;

                case PdGoConstConfig.INCREASING_VOL: //流量测速 时间间隔
                    isShowSure(mValue >= 1 & mValue <= 3000);
                    break;

                case PdGoConstConfig.BASE_SUPPLY_CYCLE:
                    isShowSure(mValue >= 1 & mValue <= 100);
                    break;

                case PdGoConstConfig.SPECIAL_SUPPLY_VOL: //流量测速 时间间隔
                    isShowSure(mValue >= 1 & mValue <= 30000);
                    break;
                case PdGoConstConfig.SPECIAL_SUPPLY_CYCLE:
                    isShowSure(mValue >= 1 & mValue <= 100);
                    break;

                case PdGoConstConfig.BASE_SUPPLY_VOL: //流量测速 时间间隔
                    isShowSure(mValue >= 1 & mValue <= 30000);
                    break;
//                }
                case PdGoConstConfig.VALUE_TEST:
                    isShowSure(mValue >= 10 & mValue <= 600);
                    break;
                //治疗之前身体数据
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_RECOMMENDED_WEIGHT: //医生建议体重
                    isShowSure(mValue >= 1f & mValue <= 500f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_WEIGHT: //治疗前体重
                    isShowSure(mValue >= 1f & mValue <= 500f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_SYSTOLIC_BLOOD_PRESSURE: //治疗前收缩压
                    isShowSure(mValue >= 30 & mValue <= 150);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_BEFORE_DIASTOLIC_BLOOD_PRESSURE: //治疗前舒张压
                    isShowSure(mValue >= 50 & mValue <= 220);
                    break;
                //治疗参数
                case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_DRAIN_THRESHOLD_ZERO_CYCLE: //零周期引流阈值
                    isShowSure(mValue >= 50 & mValue <= 120);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_DRAIN_THRESHOLD_OTHER_CYCLE: //其他周期引流阈值
                    isShowSure(mValue >= 50 & mValue <= 120);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_DRAIN_LAST_DELAY: //末次排空延迟
                    isShowSure(mValue >= 0 & mValue <= 60);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_WAITING_INTERVA: //等待提醒间隔时间
                    isShowSure(mValue >= 0 & mValue <= 60);
                    break;
                case PdGoConstConfig.CHECK_TYPE_PARAMETER_SETTING_PERFUSION_WARNING_VALUE: //灌注警戒值
                    isShowSure(mValue >= 50 & mValue <= 5000);
                    break;
                //治疗反馈
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_FEEDBACK_DRAIN_VOLUME: //引流量
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_FEEDBACK_PERFUSION_VOLUME: //灌注量
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_FEEDBACK_UFV: //超滤量
                    isShowSure(mValue >= 0 & mValue <= 3000);
                    break;

                //治疗完成
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_URINE_OUTPUT: //尿量 ml
                    isShowSure(mValue >= 0 & mValue <= 5000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_WATER_INTAKE: //饮水量 ml
                    isShowSure(mValue >= 0 & mValue <= 5000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_WEIGHT: //治疗后体重
                    isShowSure(mValue >= 1f & mValue <= 500f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_FASTING_BLOOD_GLUCOSE: //空腹血糖
                    isShowSure(mValue >= 0 & mValue <= 1000);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_SYSTOLIC_BLOOD_PRESSURE: //治疗后收缩压
                    isShowSure(mValue >= 50 & mValue <= 220);
                    break;
                case PdGoConstConfig.CHECK_TYPE_TREATMENT_COMPLETION_DIASTOLIC_BLOOD_PRESSURE: //治疗后舒张压
                    isShowSure(mValue >= 30 & mValue <= 150);
                    break;
                case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_1: //体重差值1 kg
                    isShowSure(mValue >= 1f & mValue <= 5f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_2: //体重差值2 kg
                    isShowSure(mValue >= 1f & mValue <= 5f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_3: //体重差值3 kg
                    isShowSure(mValue >= 1f & mValue <= 5f);
                    break;
                case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_4:
                    isShowSure(mValue >= 0 & mValue <= 120);
                    break;
                case PdGoConstConfig.AUTO_SLEEP:
                    isShowSure(mValue >= 1 & mValue <= 240);
                    break;
                case PdGoConstConfig.CHECK_TYPE_USER_PARAMETER_UNDER_WEIGHT_VALUE_5:
                    isShowSure(mValue >= 0 & mValue <= 120);
                    break;
            }


        } else {
            isShowSure(false);
        }


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

