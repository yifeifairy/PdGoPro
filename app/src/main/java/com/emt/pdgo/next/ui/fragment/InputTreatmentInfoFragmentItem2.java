package com.emt.pdgo.next.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.emt.pdgo.next.ui.base.BaseFragment;
import com.pdp.rmmit.pdp.R;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.ui.activity.InputTreatmentInfoActivity;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
public class InputTreatmentInfoFragmentItem2 extends BaseFragment {

    @BindView(R.id.et_prescription_item1)
    EditText etPrescriptionItem1;
    @BindView(R.id.layout_prescription_item_1)
    RelativeLayout layoutPrescriptionItem1;
    @BindView(R.id.et_prescription_item2)
    EditText etPrescriptionItem2;
    @BindView(R.id.layout_prescription_item_2)
    RelativeLayout layoutPrescriptionItem2;
    @BindView(R.id.et_prescription_item3)
    EditText etPrescriptionItem3;
    @BindView(R.id.layout_prescription_item_3)
    RelativeLayout layoutPrescriptionItem3;
    @BindView(R.id.et_prescription_item4)
    EditText etPrescriptionItem4;
    @BindView(R.id.layout_prescription_item_4)
    RelativeLayout layoutPrescriptionItem4;
    @BindView(R.id.et_prescription_item5)
    EditText etPrescriptionItem5;
    @BindView(R.id.layout_prescription_item_5)
    RelativeLayout layoutPrescriptionItem5;
    @BindView(R.id.et_prescription_item6)
    EditText etPrescriptionItem6;
    @BindView(R.id.layout_prescription_item_6)
    RelativeLayout layoutPrescriptionItem6;
    @BindView(R.id.et_prescription_item7)
    EditText etPrescriptionItem7;
    @BindView(R.id.layout_prescription_item_7)
    RelativeLayout layoutPrescriptionItem7;
    @BindView(R.id.et_prescription_item8)
    EditText etPrescriptionItem8;
    @BindView(R.id.layout_prescription_item_8)
    RelativeLayout layoutPrescriptionItem8;


    public InputTreatmentInfoActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (InputTreatmentInfoActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_treatment_info_item2, container, false);

        ButterKnife.bind(this, view);
        initViewData();
        return view;
    }

    private void initViewData() {
        setCanNotEditNoClick2(etPrescriptionItem1);
        setCanNotEditNoClick2(etPrescriptionItem2);
        setCanNotEditNoClick2(etPrescriptionItem3);
        setCanNotEditNoClick2(etPrescriptionItem4);
        setCanNotEditNoClick2(etPrescriptionItem5);
        setCanNotEditNoClick2(etPrescriptionItem6);
        setCanNotEditNoClick2(etPrescriptionItem7);
        setCanNotEditNoClick2(etPrescriptionItem8);

        etPrescriptionItem1.setText(String.valueOf(mActivity.getmParameterEniity().peritonealDialysisFluidTotal));//腹透液总量
        etPrescriptionItem2.setText(String.valueOf(mActivity.getmParameterEniity().perCyclePerfusionVolume));//每周期灌注量
        etPrescriptionItem3.setText(String.valueOf(mActivity.getmParameterEniity().cycle));//循环治疗周期数
        etPrescriptionItem4.setText(String.valueOf(mActivity.getmParameterEniity().abdomenRetainingTime));//留腹时间
        etPrescriptionItem5.setText(String.valueOf(mActivity.getmParameterEniity().abdomenRetainingVolumeFinally));//末次留腹量
        etPrescriptionItem6.setText(String.valueOf(mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime));//上次留腹量
        etPrescriptionItem7.setText(String.valueOf(mActivity.getmParameterEniity().firstPerfusionVolume));//首次灌注量
        etPrescriptionItem8.setText(String.valueOf(mActivity.getmParameterEniity().ultrafiltrationVolume));//预估超滤量

        setPeriodicities();
    }



    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    if (mType.equals(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL)) {//腹透液总量
                        etPrescriptionItem1.setText(result);
                        mActivity.getmParameterEniity().peritonealDialysisFluidTotal = Integer.valueOf(result);
                        setPeriodicities();
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME)) {//每周期灌注量
                        etPrescriptionItem2.setText(result);
                        mActivity.getmParameterEniity().perCyclePerfusionVolume = Integer.valueOf(result);
                        setPeriodicities();
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES)) {//循环治疗周期数
                        etPrescriptionItem3.setText(result);
                        mActivity.getmParameterEniity().cycle = Integer.valueOf(result);
                        setPeriodicities();
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME)) {//留腹时间
                        etPrescriptionItem4.setText(result);
                        mActivity.getmParameterEniity().abdomenRetainingTime = Integer.valueOf(result);
                        setPeriodicities();
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY)) {//末次留腹量
                        etPrescriptionItem5.setText(result);
                        mActivity.getmParameterEniity().abdomenRetainingVolumeFinally = Integer.valueOf(result);
                        setPeriodicities();
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME)) {//上次留腹量
                        etPrescriptionItem6.setText(result);
                        mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime = Integer.valueOf(result);
                        setPeriodicities();
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME)) {//首次灌注量
                        etPrescriptionItem7.setText(result);
                        mActivity.getmParameterEniity().firstPerfusionVolume = Integer.valueOf(result);
                        setPeriodicities();
                    } else if (mType.equals(PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME)) {//预估超滤量
                        etPrescriptionItem8.setText(result);
                        mActivity.getmParameterEniity().ultrafiltrationVolume = Integer.valueOf(result);
                        setPeriodicities();
                    }
                }
            }
        });
    }

    /**
     * 设置治疗周期数
     */
    private void setPeriodicities() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  (mActivity.getmParameterEniity().peritonealDialysisFluidTotal
                - mActivity.getmParameterEniity().firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime
                - mActivity.getmParameterEniity().abdomenRetainingVolumeFinally - 500 ) / mActivity.getmParameterEniity().perCyclePerfusionVolume;
        if(cycle <= 0){
            cycle = 1;
        }
        mActivity.getmParameterEniity().cycle = cycle;

        if(mActivity.getmParameterEniity().abdomenRetainingVolumeFinally>0){//0周期
            mActivity.getmParameterEniity().cycle =  mActivity.getmParameterEniity().cycle + 1;
        }


        etPrescriptionItem3.setText(String.valueOf(mActivity.getmParameterEniity().cycle));

    }

    @OnClick({R.id.et_prescription_item1, R.id.et_prescription_item2, R.id.et_prescription_item3, R.id.et_prescription_item4,
            R.id.et_prescription_item5, R.id.et_prescription_item6, R.id.et_prescription_item7, R.id.et_prescription_item8,
            R.id.layout_prescription_item_1, R.id.layout_prescription_item_2, R.id.layout_prescription_item_3, R.id.layout_prescription_item_4,
            R.id.layout_prescription_item_5, R.id.layout_prescription_item_6, R.id.layout_prescription_item_7, R.id.layout_prescription_item_8,
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_prescription_item1://腹透液总量
            case R.id.layout_prescription_item_1://
                alertNumberBoardDialog(etPrescriptionItem1.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL);
                break;
            case R.id.et_prescription_item2://每周期灌注量：
            case R.id.layout_prescription_item_2://
                alertNumberBoardDialog(etPrescriptionItem2.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME);
                break;
            case R.id.et_prescription_item3://循环治疗周期数：
            case R.id.layout_prescription_item_3://
//                alertNumberBoardDialog(etPrescriptionItem3.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES);
                break;
            case R.id.et_prescription_item4://留腹时间：
            case R.id.layout_prescription_item_4://
                alertNumberBoardDialog(etPrescriptionItem4.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME);
                break;
            case R.id.et_prescription_item5://末次留腹量：
            case R.id.layout_prescription_item_5://
                alertNumberBoardDialog(etPrescriptionItem5.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY);
                break;
            case R.id.et_prescription_item6://上次留腹量：
            case R.id.layout_prescription_item_6://
                alertNumberBoardDialog(etPrescriptionItem6.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME);
                break;
            case R.id.et_prescription_item7://首次灌注量：
            case R.id.layout_prescription_item_7://
                alertNumberBoardDialog(etPrescriptionItem7.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME);
                break;
            case R.id.et_prescription_item8://预估超滤量：
            case R.id.layout_prescription_item_8://
                alertNumberBoardDialog(etPrescriptionItem8.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME);
                break;
        }
    }


    @Override
    public void notifyByThemeChanged() {

    }
}
