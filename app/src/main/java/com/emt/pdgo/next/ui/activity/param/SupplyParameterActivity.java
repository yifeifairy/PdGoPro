package com.emt.pdgo.next.ui.activity.param;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.bean.SupplyParameterBean;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.ToastUtils;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 补液参数设置界面
 *
 * @author chenjh
 * @date 2019/1/24 10:25
 */
public class SupplyParameterActivity extends BaseActivity {

    Unbinder mUnbinder;

    @BindView(R.id.et_time_interval)
    EditText etTimeInterval;
    @BindView(R.id.et_threshold_value)
    EditText etThresholdValue;
    @BindView(R.id.et_target_protection_value)
    EditText etTargetProtectionValue;
    @BindView(R.id.et_min_weight)
    EditText etMinWeight;

    private SupplyParameterBean supplyParameterBean;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_supply_parameter);
        ButterKnife.bind(this);
        initHeadTitleBar("补液参数设置", "保存");
    }

    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;
    @Override
    public void registerEvents() {
        btnSave.setVisibility(View.VISIBLE);
        btnSave.setText("保存");
        btnBack.setOnClickListener(view -> onBackPressed());
        setCanNotEditNoClick2(etTimeInterval);
        setCanNotEditNoClick2(etThresholdValue);
        setCanNotEditNoClick2(etTargetProtectionValue);
        setCanNotEditNoClick2(etMinWeight);
    }

    @Override
    public void initViewData() {
        supplyParameterBean = PdproHelper.getInstance().getSupplyParameterBean();

        etTimeInterval.setText(String.valueOf(supplyParameterBean.supplyTimeInterval));
        etThresholdValue.setText(String.valueOf(supplyParameterBean.supplyThresholdValue));
        etTargetProtectionValue.setText(String.valueOf(supplyParameterBean.supplyTargetProtectionValue));
        etMinWeight.setText(String.valueOf(supplyParameterBean.supplyMinWeight));

    }

    @OnClick({R.id.btnSave, R.id.et_time_interval, R.id.et_threshold_value, R.id.et_target_protection_value, R.id.et_min_weight,
            R.id.layout_time_interval, R.id.layout_threshold_value, R.id.layout_target_protection_value, R.id.layout_min_weight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                save();
                break;
            case R.id.et_time_interval:
            case R.id.layout_time_interval:
                alertNumberBoardDialog(etTimeInterval.getText().toString(), PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL);
                break;
            case R.id.et_threshold_value:
            case R.id.layout_threshold_value:
                alertNumberBoardDialog(etThresholdValue.getText().toString(), PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE);
                break;
            case R.id.et_target_protection_value:
            case R.id.layout_target_protection_value:
                alertNumberBoardDialog(etTargetProtectionValue.getText().toString(), PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE);
                break;
            case R.id.et_min_weight:
            case R.id.layout_min_weight:
                alertNumberBoardDialog(etMinWeight.getText().toString(), PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT);
                break;
        }
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    if (mType.equals(PdGoConstConfig.CHECK_TYPE_SUPPLY_TIME_INTERVAL)) {//流量测速 时间间隔  :  60-600
                        etTimeInterval.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_SUPPLY_THRESHOLD_VALUE)) {//流量测速 阈值  :  30-200
                        etThresholdValue.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_SUPPLY_TARGET_PROTECTION_VALUE)) {//补液目标保护值  :  0-500
                        etTargetProtectionValue.setText(result);
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_SUPPLY_MIN_WEIGHT)) {//启动补液的加热袋最低值  : 500-10000
                        etMinWeight.setText(result);
                    }
                }
            }
        });
    }

    private void save() {


        supplyParameterBean.supplyTimeInterval = Integer.valueOf(etTimeInterval.getText().toString());
        supplyParameterBean.supplyThresholdValue = Integer.valueOf(etThresholdValue.getText().toString());
        supplyParameterBean.supplyTargetProtectionValue = Integer.valueOf(etTargetProtectionValue.getText().toString());
        supplyParameterBean.supplyMinWeight = Integer.valueOf(etMinWeight.getText().toString());

        CacheUtils.getInstance().getACache().put(PdGoConstConfig.SUPPLY_PARAMETER, supplyParameterBean);

        ToastUtils.showToast(SupplyParameterActivity.this, "补液参数保存成功");

    }

    @Override
    public void notifyByThemeChanged() {

    }


}
