package com.emt.pdgo.next.ui.activity.param;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.bean.PerfusionParameterBean;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 灌注参数设置界面
 *
 * @author chenjh
 * @date 2019/1/24 10:25
 */
public class PerfusionParameterActivity extends BaseActivity {


    @BindView(R.id.et_time_interval)
    EditText etTimeInterval;
    @BindView(R.id.et_threshold_value)
    EditText etThresholdValue;
    @BindView(R.id.et_min_weight)
    EditText etMinWeight;
    @BindView(R.id.et_allow_abdominal_volume)
    EditText etAllowAbdominalVolume;
    @BindView(R.id.et_max_warning_value)
    EditText etMaxWarningValue;

    private PerfusionParameterBean perfusionParameterBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_perfusion_parameter);
        ButterKnife.bind(this);
        initHeadTitleBar("灌注参数设置", "保存");
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @Override
    public void registerEvents() {
        btnBack.setOnClickListener(view -> onBackPressed());
        btnSave.setVisibility(View.VISIBLE);
        btnSave.setText("保存");
        setCanNotEditNoClick2(etTimeInterval);
        setCanNotEditNoClick2(etThresholdValue);
        setCanNotEditNoClick2(etMinWeight);
        setCanNotEditNoClick2(etAllowAbdominalVolume);
        setCanNotEditNoClick2(etMaxWarningValue);
    }

    @Override
    public void initViewData() {

        perfusionParameterBean = PdproHelper.getInstance().getPerfusionParameterBean();

        etTimeInterval.setText(String.valueOf(perfusionParameterBean.perfTimeInterval));
        etThresholdValue.setText(String.valueOf(perfusionParameterBean.perfThresholdValue));
        etMinWeight.setText(String.valueOf(perfusionParameterBean.perfMinWeight));
//        etAllowAbdominalVolume.setText(String.valueOf(mPerfusionParameterBean.allowAbdominalVolume));
        etMaxWarningValue.setText(String.valueOf(perfusionParameterBean.perfMaxWarningValue));
    }

    @OnClick({R.id.btnSave, R.id.et_time_interval, R.id.et_threshold_value, R.id.et_min_weight, R.id.et_allow_abdominal_volume, R.id.et_max_warning_value
            , R.id.layout_time_interval, R.id.layout_threshold_value, R.id.layout_min_weight, R.id.layout_allow_abdominal_volume, R.id.layout_max_warning_value})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                save();
                break;
            case R.id.layout_time_interval:
            case R.id.et_time_interval:
                alertNumberBoardDialog(etTimeInterval.getText().toString(), PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL);
                break;
            case R.id.layout_threshold_value:
            case R.id.et_threshold_value:
                alertNumberBoardDialog(etThresholdValue.getText().toString(), PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE);
                break;
            case R.id.layout_min_weight:
            case R.id.et_min_weight:
                alertNumberBoardDialog(etMinWeight.getText().toString(), PdGoConstConfig.CHECK_TYPE_PERFUSION_MIN_WEIGHT);
                break;
            case R.id.layout_allow_abdominal_volume:
            case R.id.et_allow_abdominal_volume:
                alertNumberBoardDialog(etAllowAbdominalVolume.getText().toString(), PdGoConstConfig.CHECK_TYPE_PERFUSION_ALLOW_ABDOMINAL_VOLUME);
                break;
            case R.id.layout_max_warning_value:
            case R.id.et_max_warning_value:
                alertNumberBoardDialog(etMaxWarningValue.getText().toString(), PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE);
                break;
        }
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(this, value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                switch (mType) {
                    case PdGoConstConfig.CHECK_TYPE_PERFUSION_TIME_INTERVAL: //流量测速 时间间隔  :  60-600
                        etTimeInterval.setText(result);
                        break;
                    case PdGoConstConfig.CHECK_TYPE_PERFUSION_THRESHOLD_VALUE: //流量测速 阈值  :  30-200
                        etThresholdValue.setText(result);
                        break;
                    case PdGoConstConfig.CHECK_TYPE_PERFUSION_MIN_WEIGHT: //加热袋最低重量允许  :  100-1000
                        etMinWeight.setText(result);
                        break;
                    case PdGoConstConfig.CHECK_TYPE_PERFUSION_ALLOW_ABDOMINAL_VOLUME: //是否允许最末灌注减去留腹量  : 1000-3000
                        etAllowAbdominalVolume.setText(result);
                        break;
                    case PdGoConstConfig.CHECK_TYPE_PERFUSION_MAX_WARNING_VALUE: //灌注最大警戒值  : 1000-3000
                        etMaxWarningValue.setText(result);
                        break;
                }
            }
        });
    }

    @BindView(R.id.btnSave)
    Button btnSave;
    private void save() {
        perfusionParameterBean.perfTimeInterval = Integer.parseInt(etTimeInterval.getText().toString());
        perfusionParameterBean.perfThresholdValue = Integer.parseInt(etThresholdValue.getText().toString());
        perfusionParameterBean.perfMinWeight = Integer.parseInt(etMinWeight.getText().toString());
//        mPerfusionParameterBean.allowAbdominalVolume = Integer.valueOf(etAllowAbdominalVolume.getText().toString());
        perfusionParameterBean.perfMaxWarningValue = Integer.parseInt(etMaxWarningValue.getText().toString());

        CacheUtils.getInstance().getACache().put(PdGoConstConfig.PERFUSION_PARAMETER, perfusionParameterBean);

        toastMessage( "灌注参数保存成功");

    }

    @Override
    public void notifyByThemeChanged() {

    }


}
