package com.emt.pdgo.next.ui.activity;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.ClickUtil;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.useDeviceTv)
    TextView useDeviceTv;
    @BindView(R.id.useDeviceRl)
    RelativeLayout useDeviceRl;

    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_unit_max_warning_value)
    TextView tvV;

    @BindView(R.id.btnBack)
    Button btnBack;

    @BindView(R.id.title)
    TextView title;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {
        title.setText("关于");
        btnBack.setOnClickListener(view -> onBackPressed());
        ClickUtil clickUtil = new ClickUtil(useDeviceRl,5000);
        clickUtil.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    alertNumberBoardDialog("", PdGoConstConfig.zeroClear);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initViewData() {
        useDeviceTv.setText(PdproHelper.getInstance().useDeviceTime()+"小时");
    }

    @SuppressLint("SetTextI18n")
    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this.getContext(), value, type, false);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
//                    Logger.d(result);
                if (mType.equals(PdGoConstConfig.zeroClear)) {//工程师模式的密码
                    if ("303626".equals(result)) {
                        MyApplication.getInstance().useDeviceTime = 0;
                        CacheUtils.getInstance().getACache().put(PdGoConstConfig.useDeviceTime, 0+"");
                        useDeviceTv.setText(PdproHelper.getInstance().useDeviceTime()+"小时");
                    }
                }
            }
        });
    }

    @Override
    public void notifyByThemeChanged() {
        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());

    }



}
