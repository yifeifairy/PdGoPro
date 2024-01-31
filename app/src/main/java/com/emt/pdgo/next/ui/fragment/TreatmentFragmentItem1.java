package com.emt.pdgo.next.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.dialog.NumberBoardDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.EmtTimeUil;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.fragment
 * @ClassName: TreatmentFragmentItem1
 * @Description: 治疗主界面
 * @Author: chenjh
 * @CreateDate: 2019/12/9 10:40 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/9 10:40 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class TreatmentFragmentItem1 extends BaseFragment {

    @BindView(R.id.et_prescription_item1)
    TextView etPrescriptionItem1;
    @BindView(R.id.layout_prescription_item_1)
    RelativeLayout layoutPrescriptionItem1;
    @BindView(R.id.et_prescription_item2)
    TextView etPrescriptionItem2;
    @BindView(R.id.layout_prescription_item_2)
    RelativeLayout layoutPrescriptionItem2;
    @BindView(R.id.et_prescription_item3)
    TextView etPrescriptionItem3;
    @BindView(R.id.layout_prescription_item_3)
    RelativeLayout layoutPrescriptionItem3;
    @BindView(R.id.et_prescription_item4)
    TextView etPrescriptionItem4;
    @BindView(R.id.layout_prescription_item_4)
    RelativeLayout layoutPrescriptionItem4;
    @BindView(R.id.et_prescription_item5)
    TextView etPrescriptionItem5;
    @BindView(R.id.layout_prescription_item_5)
    RelativeLayout layoutPrescriptionItem5;
    @BindView(R.id.et_prescription_item6)
    TextView etPrescriptionItem6;
    @BindView(R.id.layout_prescription_item_6)
    RelativeLayout layoutPrescriptionItem6;
    @BindView(R.id.et_prescription_item7)
    TextView etPrescriptionItem7;
    @BindView(R.id.layout_prescription_item_7)
    RelativeLayout layoutPrescriptionItem7;
    @BindView(R.id.et_prescription_item8)
    TextView etPrescriptionItem8;
    @BindView(R.id.layout_prescription_item_8)
    RelativeLayout layoutPrescriptionItem8;

    @BindView(R.id.layout_prescription_item_9)
    RelativeLayout layoutPrescriptionItem9;
    @BindView(R.id.et_prescription_item9)
    TextView et_prescription_item9;

    @BindView(R.id.finalSupplyCheck)
    CheckBox finalSupplyCheck;

    @BindView(R.id.btnNext)
    StateButton btnNext;

    public TreatmentFragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (TreatmentFragmentActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatment_item1, container, false);
        unbinder = ButterKnife.bind(this, view);
        registerEvents();
        initViewData();
        return view;
    }
    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();  //释放所有绑定的view
    }


    public void registerEvents() {
       layoutPrescriptionItem9.setOnClickListener(view -> mActivity.toastMessage("首次灌注量不能修改"));
        btnNext.setOnClickListener(view -> {
            if (mActivity.maxCycle > mActivity.currCycle ) {
                if (mActivity.currCycle < treatCycle) {
                    final CommonDialog dialog = new CommonDialog(mActivity);
                    dialog.setContent("是否修改")
                            .setBtnFirst("确定")
                            .setBtnTwo("取消")
                            .setFirstClickListener(mCommonDialog -> {
                                mActivity.isConfirm = true;
                                mActivity.oldVol = mActivity.getVol();
                                mActivity.revise();
                                // 单模式
                                mActivity.modify();
                                mCommonDialog.dismiss();
                            })
                            .setTwoClickListener(mCommonDialog -> {
                                mActivity.isConfirm = false;
                                mCommonDialog.dismiss();
                            });
                    if (!mActivity.isFinishing()) {
                        dialog.show();
                    }
                } else {
                    mActivity.toastMessage("治疗周期数要大于当前周期");
                }
            } else {
                mActivity.toastMessage("已是最后一周期,不能修改");
            }
        });

    }
    public void initViewData() {
//        if (MyApplication.isKid) {
//            etPrescriptionItem1.setText(String.valueOf(mActivity.kidBean.peritonealDialysisFluidTotal));
//            etPrescriptionItem2.setText(String.valueOf(mActivity.kidBean.perCyclePerfusionVolume));
//            etPrescriptionItem3.setText(String.valueOf(mActivity.kidBean.cycle));
//            etPrescriptionItem4.setText(String.valueOf(mActivity.kidBean.abdomenRetainingTime));
//            etPrescriptionItem5.setText(String.valueOf(mActivity.kidBean.abdomenRetainingVolumeFinally));
//            etPrescriptionItem6.setText(String.valueOf(mActivity.kidBean.abdomenRetainingVolumeLastTime));
//            etPrescriptionItem7.setText(String.valueOf(mActivity.kidBean.finalSupply));
//            etPrescriptionItem8.setText(String.valueOf(mActivity.kidBean.ultrafiltrationVolume));
////            etPrescriptionItem9.setText(String.valueOf(mActivity.kidBean.treatTime));
//        } else {
        etPrescriptionItem1.setText(String.valueOf(mActivity.eniity.peritonealDialysisFluidTotal));
        etPrescriptionItem2.setText(String.valueOf(mActivity.eniity.perCyclePerfusionVolume));
        etPrescriptionItem3.setText(String.valueOf(mActivity.eniity.cycle));
        etPrescriptionItem4.setText(String.valueOf(mActivity.eniity.abdomenRetainingTime));
        etPrescriptionItem5.setText(String.valueOf(mActivity.eniity.abdomenRetainingVolumeFinally));
        etPrescriptionItem6.setText(String.valueOf(mActivity.eniity.abdomenRetainingVolumeLastTime));
//            etPrescriptionItem7.setText(String.valueOf(mActivity.eniity.finalSupply));
        etPrescriptionItem8.setText(String.valueOf(mActivity.eniity.ultrafiltrationVolume));
        et_prescription_item9.setText(String.valueOf(mActivity.eniity.firstPerfusionVolume));
//            etPrescriptionItem9.setText(String.valueOf(mActivity.treatmentParameterEniity.treatTime));
        finalSupplyCheck.setChecked(mActivity.eniity.isFinalSupply);
        finalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            finalAbd = isChecked;
        });

        cancelTotal = mActivity.eniity.peritonealDialysisFluidTotal;
        cancelCycle = mActivity.eniity.cycle;
        cancelPer = mActivity.eniity.perCyclePerfusionVolume;
        cancelAbdTime = mActivity.eniity.abdomenRetainingTime;
        cancelFinalVol = mActivity.eniity.abdomenRetainingVolumeFinally;
        cancelLastVol = mActivity.eniity.abdomenRetainingVolumeLastTime;
        cancelUlt = mActivity.eniity.ultrafiltrationVolume;
        isFinal = mActivity.eniity.isFinalSupply;

        total = cancelTotal;
        abdTime = cancelAbdTime;
        finalVol = cancelFinalVol;
        lastVol = cancelLastVol;
        ultVol = cancelUlt;
        finalAbd = isFinal;
        treatCycle = cancelCycle;
        perVol = cancelPer;
//        }
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
                alertNumberBoardDialog(etPrescriptionItem3.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES);
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
//                mActivity.toastMessage("首次灌注量不可更改");
                alertNumberBoardDialog(etPrescriptionItem7.getText().toString(), PdGoConstConfig.FINAL_SUPPLY);
                break;
            case R.id.et_prescription_item8://预估超滤量：
            case R.id.layout_prescription_item_8://
                alertNumberBoardDialog(etPrescriptionItem8.getText().toString(), PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME);
                break;
        }
    }

    private void setTotal() {
        int total = (mActivity.eniity.cycle) * mActivity.eniity.perCyclePerfusionVolume
                + mActivity.eniity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime
                + mActivity.eniity.abdomenRetainingVolumeFinally + 500 ;
        etPrescriptionItem1.setText(String.valueOf(total));
        mActivity.eniity.peritonealDialysisFluidTotal = total;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
        setTreatTime();
    }

    private void alertNumberBoardDialog(String value, String type) {
        NumberBoardDialog dialog = new NumberBoardDialog(getActivity(), value, type, false, true);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberBoardDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
                    switch (mType) {
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERITONEAL_DIALYSIS_FLUID_TOTAL: //腹透液总量
                            etPrescriptionItem1.setText(result);
//                            if (MyApplication.isKid) {
//                                mActivity.kidBean.peritonealDialysisFluidTotal = Integer.parseInt(result);
//                            } else {
                                total = Integer.parseInt(result);
//                            }
                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PER_CYCLE_PERFUSION_VOLUME: //每周期灌注量
                            etPrescriptionItem2.setText(result);
//                            if (MyApplication.isKid) {
//                                mActivity.kidBean.perCyclePerfusionVolume = Integer.parseInt(result);
//                            } else {
                            perVol = mActivity.eniity.perCyclePerfusionVolume = Integer.parseInt(result);
//                            }
                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERIODICITIES: //循环治疗周期数
                            etPrescriptionItem3.setText(result);
//                            if (MyApplication.isKid) {
//                                mActivity.kidBean.cycle = Integer.parseInt(result);
//                            } else {
                            treatCycle = Integer.parseInt(result);
//                            }
//                            setTotal();
                            setCyclePre();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_TIME: //留腹时间
                            etPrescriptionItem4.setText(result);
//                            if (MyApplication.isKid) {
//                                mActivity.kidBean.abdomenRetainingTime = Integer.parseInt(result);
//                            } else {
                            abdTime = Integer.parseInt(result);
//                            }
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY: //末次留腹量
                            etPrescriptionItem5.setText(result);
//                            if (MyApplication.isKid) {
//                                mActivity.kidBean.abdomenRetainingVolumeFinally = Integer.parseInt(result);
//                            } else {
                            finalVol = Integer.parseInt(result);
//                            }
//                            setTotal();
                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_LAST_TIME: //上次留腹量
                            etPrescriptionItem6.setText(result);
//                            if (MyApplication.isKid) {
//                                mActivity.kidBean.abdomenRetainingVolumeLastTime = Integer.parseInt(result);
//                            } else {
                            lastVol = Integer.parseInt(result);
//                            }
//                            setPeriodicities();
                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_PERFUSION_VOLUME: //首次灌注量
                            etPrescriptionItem7.setText(result);
//                            if (MyApplication.isKid) {
//                                mActivity.kidBean.firstPerfusionVolume = Integer.parseInt(result);
//                            } else {
                            mActivity.eniity.firstPerfusionVolume = Integer.parseInt(result);
//                            }
//                            setTotal();
                            setPeriodicities();
                            break;
//                        case PdGoConstConfig.FINAL_SUPPLY: //末袋补液量
//                            etPrescriptionItem7.setText(result);
////                            if (MyApplication.isKid) {
////                                mActivity.kidBean.finalSupply = Integer.parseInt(result);
////                            } else {
//                                mActivity.eniity.finalSupply = Integer.parseInt(result);
////                            }
////                            setTotal();
//                            setPeriodicities();
//                            break;
                        case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ESTIMATED_ULTRAFILTRATION_VOLUME: //预估超滤量
                            etPrescriptionItem8.setText(result);
//                            if (MyApplication.isKid) {
//                                mActivity.kidBean.ultrafiltrationVolume = Integer.parseInt(result);
//                            } else {
                            ultVol = Integer.parseInt(result);
//                            }
//                            setPeriodicities();
                            break;
                    }
                }
            }
        });
    }
    /**
     * 预估治疗时间= （每周期留腹时间x周期数）+(（灌注量+赢流量+最末留腹量）➗125ml/s)
     */
    private void setTreatTime() {
        int time = (mActivity.eniity.abdomenRetainingTime * 60 * mActivity.eniity.cycle) + ((mActivity.eniity.firstPerfusionVolume + mActivity.eniity.abdomenRetainingVolumeFinally +
                mActivity.eniity.abdomenRetainingVolumeLastTime ) / 125);
        mActivity.eniity.treatTime = EmtTimeUil.getTime(time);
//        mActivity.setModifyParam();
        // mActivity.amend();
    }

    private void setCyclePre() {
//        int perCyclePerfusionVolume =
////                MyApplication.isKid ?
////                (mActivity.kidBean.peritonealDialysisFluidTotal
////                        - mActivity.kidBean.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime
////                        - mActivity.kidBean.abdomenRetainingVolumeFinally - 500 ) /
////                        mActivity.kidBean.cycle :
//                (mActivity.eniity.peritonealDialysisFluidTotal
//                - mActivity.eniity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime
//                - mActivity.eniity.abdomenRetainingVolumeFinally - 500 ) /
//                mActivity.eniity.cycle;
        perVol =  (total - mActivity.getVol() - finalVol) / treatCycle;
        etPrescriptionItem2.setText(String.valueOf(perVol));
        Log.e("getVol","total"+total+"---"
                +"treatCycle"+treatCycle+"---"
                +"finalVol"+finalVol+"---"
                +"perVol"+perVol+"---");
//        mActivity.eniity.perCyclePerfusionVolume = perCyclePerfusionVolume;
//        CacheUtils.getInstance().getACache().put(PdGoConstConfig.TREATMENT_PARAMETER, mParameterEniity);
        setTreatTime();
    }

    public int treatCycle;
    private void setPeriodicities() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
//        int cycle =
////        MyApplication.isKid ?
////                (mActivity.kidBean.peritonealDialysisFluidTotal
////                        - mActivity.kidBean.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime
////                        - mActivity.kidBean.abdomenRetainingVolumeFinally - 500 ) /
////                        mActivity.kidBean.perCyclePerfusionVolume:
//                (mActivity.eniity.peritonealDialysisFluidTotal
//                - mActivity.eniity.firstPerfusionVolume ////不需要扣除上次最末留腹量 - mActivity.getmParameterEniity().abdomenRetainingVolumeLastTime
//                - mActivity.eniity.abdomenRetainingVolumeFinally - 500 ) /
//                        mActivity.eniity.perCyclePerfusionVolume;
        // 6000  500  9  1000
        // curr = 2
        // (2000+1-0-1)
        // (6000-2000) / 500
        treatCycle = (total - mActivity.getVol() - finalVol) / perVol;
//        if(cycle <= 0){
//            cycle = 1;
//        }
        Log.e("cycle---","total"+total+"---"
                +"treatCycle"+treatCycle+"---"
                +"finalVol"+finalVol+"---"
                +"perVol"+perVol+"---");
        if(treatCycle <= 0){
            treatCycle = 1;
        }
//        if (MyApplication.isKid) {
//            mActivity.kidBean.cycle = cycle;
//        } else {
//            mActivity.eniity.cycle = cycle;
//        }
//        if(mActivity.treatmentParameterEniity.abdomenRetainingVolumeFinally>0){//0周期
//            mActivity.treatmentParameterEniity.cycle =  mActivity.treatmentParameterEniity.cycle + 1;
//        }
        etPrescriptionItem3.setText(String.valueOf(treatCycle));
//        setTreatTime();
        // mActivity.amend();
    }
    // 取消修改或者返回参数恢复
    private int cancelTotal, cancelPer, cancelCycle, cancelAbdTime, cancelFinalVol, cancelLastVol, cancelUlt;
    public boolean isFinal;
    public int total, abdTime, finalVol, lastVol, ultVol, perVol;
    private boolean finalAbd;

    public void goBack(boolean isConfirm) {
        if (isConfirm) {
            cancelTotal = total;
            cancelCycle = treatCycle;
            cancelPer = perVol;
            cancelAbdTime = abdTime;
            cancelFinalVol = finalVol;
            cancelLastVol = lastVol;
            cancelUlt = ultVol;
            isFinal = finalAbd;
            Log.e("goBack", "perVol--"+perVol);
        } else {
            total = cancelTotal;
            abdTime = cancelAbdTime;
            finalVol = cancelFinalVol;
            lastVol = cancelLastVol;
            ultVol = cancelUlt;
            finalAbd = isFinal;
            treatCycle = cancelCycle;
            perVol = cancelPer;
        }
        Log.e("goBack", "cancelPer--"+cancelPer+"---"+perVol);
        etPrescriptionItem1.setText(String.valueOf(cancelTotal));
        etPrescriptionItem2.setText(String.valueOf(cancelPer));
        etPrescriptionItem3.setText(String.valueOf(cancelCycle));
        etPrescriptionItem4.setText(String.valueOf(cancelAbdTime));
        etPrescriptionItem5.setText(String.valueOf(cancelFinalVol));
        etPrescriptionItem6.setText(String.valueOf(cancelLastVol));
//            etPrescriptionItem7.setText(String.valueOf(mActivity.eniity.finalSupply));
        etPrescriptionItem8.setText(String.valueOf(cancelUlt));
//            etPrescriptionItem9.setText(String.valueOf(mActivity.treatmentParameterEniity.treatTime));
        finalSupplyCheck.setChecked(isFinal);
    }

    @Override
    public void notifyByThemeChanged() {

    }

}
