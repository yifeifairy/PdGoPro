package com.emt.pdgo.next.ui.activity.param;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.emt.pdgo.next.util.ToastUtils;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SNSetActivity extends BaseActivity {


    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.et_machineSN)
    EditText etMachineSN;

    @BindView(R.id.layout_sn)
    RelativeLayout layoutSN;
    @BindView(R.id.tv_label)
    TextView labelTv;

    private String machineSN;
    private String tips;



    private MarioResourceHelper helper;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_sn_set);
        ButterKnife.bind(this);
        initHeadTitleBar("SN参数设置", "保存");
        helper = MarioResourceHelper.getInstance(getContext());
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.title)
    TextView title;
    @Override
    public void registerEvents() {
        btnSave.setText("保存");
        btnSave.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(view -> onBackPressed());
        etMachineSN.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
                helper.setBackgroundResourceByAttr(layoutSN, R.attr.custom_attr_input_box_normal_bg);
            } else {
                // 此处为失去焦点时的处理内容
                if (TextUtils.isEmpty(etMachineSN.getText().toString().toString())) {
                    helper.setBackgroundResourceByAttr(layoutSN, R.attr.custom_attr_input_box_focused_bg);
                } else {
                    //layoutSN.setBackgroundResource(R.drawable.bg_round_white);
                    helper.setBackgroundResourceByAttr(layoutSN, R.attr.custom_attr_input_box_bg);
                }
            }
        });

        mAppBackground.setOnTouchListener((arg0, arg1) -> {
            // rel.setFocusable(true);
            // 如果xml文件里面没设置，就需要在这里设置
            // rel.setFocusableInTouchMode(true);
            mAppBackground.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etMachineSN.getWindowToken(), 0);
            return false;
        });
    }

    @Override
    public void initViewData() {

        machineSN = PdproHelper.getInstance().getMachineSN();

        if (!TextUtils.isEmpty(machineSN)) {
            etMachineSN.setText(machineSN);
        } else {
            etMachineSN.setText("");
        }

        etMachineSN.clearFocus();

    }


    @OnClick({R.id.btnSave})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btnSave:
                String mSn = etMachineSN.getText().toString();
                if (TextUtils.isEmpty(mSn)) {
                    tips = "SN不能为空";
                } else {
//                    showLoadingDialog();
                    tips = "SN保存成功";
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.PDP_MACHINE_SN, mSn);
//                    hideLoadingDialog();
                }
//                showCommonDialog(tips, SNSetActivity.this);
                ToastUtils.showToast(SNSetActivity.this, tips);
                break;
        }
    }

    @Override
    public void notifyByThemeChanged() {

        helper.setBackgroundResourceByAttr(mAppBackground, R.attr.custom_attr_app_bg);

        helper.setBackgroundResourceByAttr(layoutSN, R.attr.custom_attr_input_box_bg);
        helper.setTextColorByAttr(etMachineSN, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(labelTv, R.attr.custom_attr_common_text_color);

    }

}
