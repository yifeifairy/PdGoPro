package com.emt.pdgo.next.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.activity.InputTreatmentInfoActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.fragment
 * @ClassName: InputTreatmentInfoFragmentItem1
 * @Description: java类作用描述
 * @Author: chenjh
 * @CreateDate: 2019/12/12 10:33 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/12 10:33 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class InputTreatmentInfoFragmentItem1 extends BaseFragment {

    @BindView(R.id.tv_weight_value)
    TextView tvWeightValue;
    @BindView(R.id.tv_blood_pressure_value)
    TextView tvBloodPressureValue;
    @BindView(R.id.tv_heart_rate_value)
    TextView tvHeartRateValue;
    @BindView(R.id.tv_manual_input)
    TextView tvManualInput;

    private CompositeDisposable mCompositeDisposable;

    public InputTreatmentInfoActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (InputTreatmentInfoActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_treatment_info_item1, container, false);
        ButterKnife.bind(this, view);
        RxBus.get().register(this);
        initViewData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.get().unRegister(this);
        mCompositeDisposable.clear();
    }

    public void initViewData() {
        mCompositeDisposable = new CompositeDisposable();
    }


    @Subscribe(code = RxBusCodeConfig.EVENT_INPUT_BODY_DATA_BLOOD)
    public void receiveBlood(String[] mData) {
        mActivity.getmParameterEniity().sbp = Integer.parseInt(mData[0]);
        mActivity.getmParameterEniity().dbp = Integer.parseInt(mData[1]);
        mActivity.getmParameterEniity().heartRate = Integer.parseInt(mData[2]);
        tvBloodPressureValue.setText(mData[0] + "/" + mData[1]);
        tvHeartRateValue.setText(TextUtils.isEmpty(mData[2]) ? "0" : mData[2]);
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_INPUT_BODY_DATA_WEIGHT)
    public void receiveWeight(String value) {
        tvWeightValue.setText(TextUtils.isEmpty(value) ? "0" : value);
        mActivity.getmParameterEniity().weight = Float.valueOf(TextUtils.isEmpty(value) ? "0" : value);
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_INPUT_BODY_DATA_HEART_RATE)
    public void receiveHeartRate(String value) {
        mActivity.getmParameterEniity().heartRate = Integer.valueOf(TextUtils.isEmpty(value) ? "0" : value);
        tvHeartRateValue.setText(TextUtils.isEmpty(value) ? "0" : value);
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_INPUT_BODY_DATA_SBP)
    public void receiveSBP(String value) {
        mActivity.getmParameterEniity().sbp = Integer.valueOf(TextUtils.isEmpty(value) ? "0" : value);
        tvBloodPressureValue.setText(mActivity.getmParameterEniity().sbp + "/" + mActivity.getmParameterEniity().dbp);
    }

    @Subscribe(code = RxBusCodeConfig.EVENT_INPUT_BODY_DATA_DBP)
    public void receiveDBP(String value) {
        mActivity.getmParameterEniity().dbp = Integer.valueOf(TextUtils.isEmpty(value) ? "0" : value);
        tvBloodPressureValue.setText(mActivity.getmParameterEniity().sbp + "/" + mActivity.getmParameterEniity().dbp);
    }

//    @OnClick({R.id.tv_manual_input, R.id.layout_weight, R.id.layout_blood_pressure, R.id.layout_heart_rate})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.layout_weight:
//                mActivity.showBluetoothDialog();
//                break;
//            case R.id.layout_blood_pressure:
//                mActivity.showBluetoothDialog();
////                sanLsBleDialog();
//                break;
//            case R.id.layout_heart_rate:
//                mActivity.showBluetoothDialog();
//                break;
//            case R.id.tv_manual_input:
//                mActivity.doGoTOActivity(InputBodyDataActivity.class);
//                break;
//        }
//    }


    @Override
    public void notifyByThemeChanged() {

    }
}
