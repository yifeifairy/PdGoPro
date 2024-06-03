package com.emt.pdgo.next.ui.activity.param;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.http.RetrofitUtil;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通信参数设置界面
 *
 * @author chenjh
 * @date 2018/11/26 15:53
 */
public class UrlSetActivity extends BaseActivity {


    public static String HOST = "http://apc.ckdcloud.com";//http://192.168.31.38:8765/api/auth/jwt/token   "http://apc.ckdcloud.com"

    @BindView(R.id.et_server_address)
    EditText etServerAddress;

    @BindView(R.id.custom_id_app_background)
    LinearLayout rootView;

    @BindView(R.id.tv_label)
    TextView labelTv;
    @BindView(R.id.layout_url)
    RelativeLayout layoutUrl;

    private String appServerUrl = "";

    private String tips;
    private MarioResourceHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_url_set);
        ButterKnife.bind(this);
        initHeadTitleBar("通信地址设置", "保存");
        helper = MarioResourceHelper.getInstance(getContext());
    }

    @Override
    public void initViewData() {
//        txtUrl.setText(HOST.toLowerCase().replace("http://", ""));
        appServerUrl = PdproHelper.getInstance().getAppServerUrl();
        if (TextUtils.isEmpty(appServerUrl)) {
            appServerUrl = RetrofitUtil.BASE_URL;
        }
        etServerAddress.setText(appServerUrl.replace("http://", "").replace("https://",""));
        etServerAddress.clearFocus();
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @Override
    public void registerEvents() {
        btnBack.setOnClickListener(view -> onBackPressed());
        etServerAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
                helper.setBackgroundResourceByAttr(layoutUrl, R.attr.custom_attr_input_box_normal_bg);
            } else {
                // 此处为失去焦点时的处理内容
                if (TextUtils.isEmpty(etServerAddress.getText().toString().toString())) {
                    helper.setBackgroundResourceByAttr(layoutUrl, R.attr.custom_attr_input_box_focused_bg);
                } else {
                    //layoutSN.setBackgroundResource(R.drawable.bg_round_white);
                    helper.setBackgroundResourceByAttr(layoutUrl, R.attr.custom_attr_input_box_bg);
                }
            }
        });

        rootView.setOnTouchListener((arg0, arg1) -> {
            // rel.setFocusable(true);
            // 如果xml文件里面没设置，就需要在这里设置
            // rel.setFocusableInTouchMode(true);
            rootView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etServerAddress.getWindowToken(), 0);
            return false;
        });
    }


    @OnClick({R.id.btnSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                String mUrl = etServerAddress.getText().toString();
                if (TextUtils.isEmpty(mUrl)) {
                    tips = "请输入通信地址，通信地址不能为空";

                } else {
                    mUrl = mUrl.replace("http://", "");
                    if (Patterns.WEB_URL.matcher(mUrl).matches() || URLUtil.isValidUrl(mUrl)) {
                        Log.e("mUrl","mUrl--"+mUrl);
                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.PDP_APP_SERVER_URL.toLowerCase(), "https://" + mUrl);
//                        APIServiceManage.getInstance().cleanAPIServices();
                        tips = "保存成功";
//                        hideLoadingDialog();
                    } else {
                        tips = "请输入正确的通信地址";
                    }
                }
                toastMessage(tips);
                break;
        }
    }


    @Override
    public void notifyByThemeChanged() {
        helper.setBackgroundResourceByAttr(rootView, R.attr.custom_attr_app_bg);

        helper.setBackgroundResourceByAttr(layoutUrl, R.attr.custom_attr_input_box_bg);
        helper.setTextColorByAttr(etServerAddress, R.attr.custom_attr_common_text_color);
        helper.setTextColorByAttr(labelTv, R.attr.custom_attr_common_text_color);
    }

}
