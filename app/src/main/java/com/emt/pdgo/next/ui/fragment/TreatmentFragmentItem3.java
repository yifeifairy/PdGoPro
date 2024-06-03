package com.emt.pdgo.next.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.data.bean.DetailedBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.ui.activity.TreatmentFragmentActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.fragment
 * @ClassName: TreatmentFragmentItem3
 * @Description: java类作用描述
 * @Author: chenjh
 * @CreateDate: 2019/12/18 4:23 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/18 4:23 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class TreatmentFragmentItem3 extends BaseFragment {

    Unbinder mUnbinder;

    @BindView(R.id.tv_cycle)
    TextView tvCycle;
    @BindView(R.id.tv_curr_cycle)
    TextView tvCurrCycle;
    @BindView(R.id.tv_curr_cycle_perfusion_volume)
    TextView tvCurrCyclePerfusionVolume;
    @BindView(R.id.tv_up_weight_initial_value)
    TextView tvUpWeightInitialValue;
    @BindView(R.id.tv_supply_target_value)
    TextView tvSupplyTargetValue;
    @BindView(R.id.tv_curr_rinse_perfusion_volume)
    TextView tvCurrRinsePerfusionVolume;
    @BindView(R.id.tv_curr_rinse_num)
    TextView tvCurrRinseNum;
    @BindView(R.id.tv_up_weight_real_time_value)
    TextView tvUpWeightRealTimeValue;
    @BindView(R.id.tv_low_weight_real_time_value)
    TextView tvLowWeightRealTimeValue;
    @BindView(R.id.tv_curr_cycle_drain_volume)
    TextView tvCurrCycleDrainVolume;
    @BindView(R.id.tv_perfusion_volume)
    TextView tvPerfusionVolume;
    @BindView(R.id.tv_abdomen_retaining_time)
    TextView tvAbdomenRetainingTime;
    @BindView(R.id.tv_abdomen_retaining_volume)
    TextView tvAbdomenRetainingVolume;
    @BindView(R.id.tv_supply_target_protection_value)
    TextView tvSupplyTargetProtectionValue;
    @BindView(R.id.tv_predicted_residual_liquid_volume)
    TextView tvPredictedResidualLiquidVolume;
    @BindView(R.id.tv_max_warning_value)
    TextView tvMaxWarningValue;
    @BindView(R.id.tv_drain_target_volume)
    TextView tvDrainTargetVolume;
    @BindView(R.id.tv_drain_pass_volume)
    TextView tvDrainPassVolume;
    @BindView(R.id.tv_perfusion_target_volume)
    TextView tvPerfusionTargetVolume;
    @BindView(R.id.tv_valve_negpre_drain)
    TextView tvValveNegpreDrain;
    @BindView(R.id.tv_valve_supply)
    TextView tvValveSupply;
    @BindView(R.id.tv_valve_perfusion)
    TextView tvValvePerfusion;
    @BindView(R.id.tv_valve_supply2)
    TextView tvValveSupply2;
    @BindView(R.id.tv_valve_safe)
    TextView tvValveSafe;
    @BindView(R.id.tv_valve_drain)
    TextView tvValveDrain;
    @BindView(R.id.tv_treatment_step)
    TextView tvTreatmentStep;

    @BindView(R.id.tvT1Temp)
    TextView tvT0Temp;
    @BindView(R.id.tvT2Temp)
    TextView tvT1Temp;
    @BindView(R.id.tvT3Temp)
    TextView tvT2Temp;

    @BindView(R.id.rinseVol)
    TextView rinseVol;
    @BindView(R.id.rinseSupplyVol)
    TextView rinseSupplyVol;

    public int currCycle = -1;

    public int currentStage = -1;

    private boolean firstStart = true;

    public TreatmentFragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (TreatmentFragmentActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatment_item3, container, false);

        mUnbinder = ButterKnife.bind(this, view);
        init();
        initViewData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //RxBus.get().unRegister(this);
        mUnbinder.unbind();  //释放所有绑定的view
    }

    public void initViewData() {
        try {
//        tvValveNegpreDrain.setVisibility(mActivity.mTreatmentParameterEniity.isNegpreDrain ? View.VISIBLE : View.GONE);
            tvUpWeightInitialValue.setText(String.valueOf(mActivity.dialysateInitialValue));//上位秤腹膜透析液的初始值
//        tvCycle.setText(String.valueOf(mActivity.treatmentParameterEniity.cycle));//循环治疗周期数
            tvCycle.setText(String.valueOf(mActivity.maxCycle));
            tvPerfusionVolume.setText(String.valueOf(mActivity.lowFirstValue));
            tvMaxWarningValue.setText(String.valueOf(mActivity.perfusionParameterBean.perfMaxWarningValue));//灌注警戒值
            setStageAndCycle();
            rinseSupplyVol.setText("预冲补液量:" + MyApplication.rinseSupplyVol);
            rinseVol.setText("预冲量:" + MyApplication.rinseVol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新设备信息
     *
     * @param mDeviceStatusInfo
     */
    public void setDeviceStatusInfo(ReceiveDeviceBean mDeviceStatusInfo) {
        try {
            switch (mDeviceStatusInfo.vaccum) {
                case "00":
                case "80":
//            tvValveNegpreDrain.setTextColor(Color.YELLOW);
                    tvValveNegpreDrain.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveNegpreDrain.setTextColor(Color.GREEN);
                    tvValveNegpreDrain.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveNegpreDrain.setTextColor(Color.GREEN);
                    tvValveNegpreDrain.setBackgroundResource(R.color.darkblue);
                    break;
                case "D0":
                case "E0":
                case "F0":
                    tvValveNegpreDrain.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("vaccum",false));
                    break;
                case "D1":
                case "E1":
                case "F1":
                    tvValveNegpreDrain.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("vaccum",true));
                    break;
                default:
//            tvValveNegpreDrain.setTextColor(Color.RED);
                    tvValveNegpreDrain.setBackgroundResource(R.color.red);
                    break;
            }
//        Log.e("详细状态","vaccum--"+mDeviceStatusInfo.vaccum+
//                "supply"+mDeviceStatusInfo.supply);
            switch (mDeviceStatusInfo.supply1) {
                case "00":
                case "80":
//            tvValveSupply.setTextColor(Color.YELLOW);
//            tvValveSupply.setTextColor(Color.YELLOW);
                    tvValveSupply.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSupply.setTextColor(Color.GREEN);
                    tvValveSupply.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSupply.setTextColor(Color.GREEN);
                    tvValveSupply.setBackgroundResource(R.color.darkblue);
                    break;
                case "D0":
                case "E0":
                case "F0":
                    tvValveSupply.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("supply",false));
                    break;
                case "D1":
                case "E1":
                case "F1":
                    tvValveSupply.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("supply",true));
                    break;
                default:
//            tvValveSupply.setTextColor(Color.RED);
                    tvValveSupply.setBackgroundResource(R.color.red);
                    break;
            }
            switch (mDeviceStatusInfo.perfuse) {
                case "00":
                case "80":
//            tvValvePerfusion.setTextColor(Color.YELLOW);
                    tvValvePerfusion.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValvePerfusion.setTextColor(Color.GREEN);
                    tvValvePerfusion.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValvePerfusion.setTextColor(Color.GREEN);
                    tvValvePerfusion.setBackgroundResource(R.color.darkblue);
                    break;
                case "D0":
                case "E0":
                case "F0":
                    tvValvePerfusion.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("perfuse",false));
                    break;
                case "D1":
                case "E1":
                case "F1":
                    tvValvePerfusion.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("perfuse",true));
                    break;
                default:
//            tvValvePerfusion.setTextColor(Color.RED);
                    tvValvePerfusion.setBackgroundResource(R.color.red);
                    break;
            }
            switch (mDeviceStatusInfo.drain) {
                case "00":
                case "80":
//            tvValveDrain.setTextColor(Color.YELLOW);
                    tvValveDrain.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveDrain.setTextColor(Color.GREEN);
                    tvValveDrain.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveDrain.setTextColor(Color.GREEN);
                    tvValveDrain.setBackgroundResource(R.color.darkblue);
                    break;
                case "D0":
                case "E0":
                case "F0":
                    tvValveDrain.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("drain",false));
                    break;
                case "D1":
                case "E1":
                case "F1":
                    tvValveDrain.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("drain",true));
                    break;
                default:
//            tvValveDrain.setTextColor(Color.RED);
                    tvValveDrain.setBackgroundResource(R.color.red);
                    break;
            }
            switch (mDeviceStatusInfo.safe) {
                case "00":
                case "80":
//            tvValveSafe.setTextColor(Color.YELLOW);
                    tvValveSafe.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSafe.setTextColor(Color.GREEN);
                    tvValveSafe.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSafe.setTextColor(Color.GREEN);
                    tvValveSafe.setBackgroundResource(R.color.darkblue);
                    break;
                case "D0":
                case "E0":
                case "F0":
                    tvValveSafe.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("safe",false));
                    break;
                case "D1":
                case "E1":
                case "F1":
                    tvValveSafe.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("safe",true));
                    break;
                default:
                    tvValveSafe.setBackgroundResource(R.color.red);
//            tvValveSafe.setTextColor(Color.RED);
                    break;
            }
            switch (mDeviceStatusInfo.supply2) {
                case "00":
                case "80":
//            tvValveSupply2.setTextColor(Color.YELLOW);
                    tvValveSupply2.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSupply2.setTextColor(Color.GREEN);
                    tvValveSupply2.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSupply2.setTextColor(Color.GREEN);
                    tvValveSupply2.setBackgroundResource(R.color.darkblue);
                    break;
                case "D0":
                case "E0":
                case "F0":
                    tvValveSupply2.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("supply2",false));
                    break;
                case "D1":
                case "E1":
                case "F1":
                    tvValveSupply2.setBackgroundResource(R.color.red);
                    mActivity.sendToMainBoard(CommandDataHelper.getInstance().valueOpen("supply2",true));
                    break;
                default:
//            tvValveSupply2.setTextColor(Color.RED);
                    tvValveSupply2.setBackgroundResource(R.color.red);
                    break;
            }

            tvT0Temp.setText(getTemp(mDeviceStatusInfo.temp) + "℃");
            tvT1Temp.setText(getTemp(mDeviceStatusInfo.isT1Err) + "℃");
            tvT2Temp.setText(getTemp(mDeviceStatusInfo.isT2Err) + "℃");
//        tvValveNegpreDrain.setTextColor(mDeviceStatusInfo.vaccum.equals("00") ? 0xfff44336 : 0xff00008B);//末次灌注阀  打开亮红色，关闭亮蓝色
//        tvValveSupply.setTextColor(mDeviceStatusInfo.supply.equals("00") ? 0xffff0000 : 0xff00008B);//补液阀  打开亮红色，关闭亮蓝色
//        tvValvePerfusion.setTextColor(mDeviceStatusInfo.perfuse.equals("00") ? 0xffff0000 : 0xff00008B);//灌注阀 打开亮红色，关闭亮蓝色
//        tvValveDrain.setTextColor(mDeviceStatusInfo.drain.equals("00") ? 0xffff0000 : 0xff00008B);//引流阀 打开亮红色，关闭亮蓝色
            tvUpWeightRealTimeValue.setText(String.valueOf(mDeviceStatusInfo.upper - PdproHelper.getInstance().getOtherParamBean().upper));//上位秤实时值
            tvLowWeightRealTimeValue.setText(String.valueOf(mDeviceStatusInfo.lower - PdproHelper.getInstance().getOtherParamBean().lower));//下位秤实时值
            setStageAndCycle();
        } catch (Exception e) {
            Log.e("设备信息", e.getLocalizedMessage());
        }
    }

    /**
     * 实时灌注量
     *
     * @param mVolume
     */
    public void setCurrPerfusionVolume(int mVolume) {
        try {
            tvCurrCyclePerfusionVolume.setText(String.valueOf(Math.max(mVolume, 0)));//当前周期灌注量
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMaxCycle(int maxCycle) {
        try {
            tvCycle.setText(String.valueOf(maxCycle));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BigDecimal mTen = new BigDecimal(10);
    private float getTemp(int temp) {
        BigDecimal mTemp = new BigDecimal(temp);
        BigDecimal newTemp = mTemp.divide(mTen, 1, RoundingMode.HALF_UP);
        return newTemp.floatValue();
    }

    /**
     * 实时引流量
     *
     * @param mVolume
     */
    public void setCurrDrainVolume(int mVolume) {
        try {
            tvCurrCycleDrainVolume.setText(String.valueOf(Math.max(mVolume, 0)));//当前周期引流量
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实时留腹量（预计腹腔剩余液体量）
     *
     * @param mVolume
     */
    public void setWaittingVolume(int mVolume) {
        try {
            tvPredictedResidualLiquidVolume.setText(String.valueOf(Math.max(mVolume, 0)));//预计腹腔剩余液体量
        }catch (Exception e) {
            e.printStackTrace();
        }
//        tvPredictedResidualLiquidVolume.setText(mVolume);//预计腹腔剩余液体量
    }

    /**
     * 留腹倒计时
     *
     * @param currWaitingTime
     */
    public void setWaittingTime(String currWaitingTime) {
        try {
            tvAbdomenRetainingTime.setText(currWaitingTime);//
        }catch (Exception e) {
            e.printStackTrace();
        }
//        tvAbdomenRetainingTime.setText(currWaitingTime);//
    }

    /**
     * 当前冲洗灌注 次数
     */
    public void setRinseData(int mVolume) {
        try {
            tvCurrRinsePerfusionVolume.setText(String.valueOf(Math.max(mVolume, 0)));//当前冲洗灌注量
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRinseNum(int mNum) {
        try {
            tvCurrRinseNum.setText(String.valueOf(mNum));//当前冲洗灌注 次数
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 灌注目标值
     */
    public void setPerfusionTargetVolume(int mCurrCycle, int mTargetVolume) {
        try {
            currCycle = mCurrCycle;
            tvCurrCycle.setText(String.valueOf(mCurrCycle));//当前治疗周期数
            tvPerfusionTargetVolume.setText(String.valueOf(Math.max(mTargetVolume, 0)));//灌注目标值
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 引流目标值
     *
     * @param mCurrCycle
     * @param mTargetVolume
     */
    public void setDrainTargetVolume(int mCurrCycle, int mTargetVolume, int passVol) {
        try {
            currCycle = mCurrCycle;
            tvCurrCycle.setText(String.valueOf(mCurrCycle));//当前治疗周期数
//            int temp_drainValue = Integer.parseInt(mTargetVolume);
//
//            int mDrainPassVolume = temp_drainValue * (currCycle == 0 ?
//                    mActivity.drainParameterBean.drainZeroCyclePercentage : mActivity.drainParameterBean.drainOtherCyclePercentage);

            tvCurrCycle.setText(String.valueOf(mCurrCycle));//当前治疗周期数
            tvDrainTargetVolume.setText(String.valueOf(Math.max(mTargetVolume, 0)));//灌注目标值
            tvDrainPassVolume.setText(String.valueOf(Math.max(passVol, 0)));//引流及格值
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSupply(int supply) {
        try {
            tvAbdomenRetainingVolume.setText(String.valueOf(Math.max(supply, 0)));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 补液目标值和补液保护值
     */
    public void setSupplyTargetValue(int mSupplyTargetValue, int mSupplyTargetProtectionValue, int supply) {
        try {

            tvSupplyTargetValue.setText(String.valueOf(Math.max(mSupplyTargetValue, 0)));//补液保护值

            tvAbdomenRetainingVolume.setText(String.valueOf(Math.max(supply, 0)));
            tvSupplyTargetProtectionValue.setText(String.valueOf(Math.max(mSupplyTargetProtectionValue, 0)));//补液目标值 = 上位秤腹膜透析液的初始值 + 补液保护值
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前周期状态和周期数
     */
    public void setStageAndCycle() {
        try {
            if (currCycle != mActivity.currCycle) {
                currCycle = mActivity.currCycle;
                tvCurrCycle.setText(String.valueOf(mActivity.currCycle));
            }
            if (currentStage != mActivity.currentStage) {
                currentStage = mActivity.currentStage;
                if (1 == mActivity.currentStage) {
                    tvTreatmentStep.setText("灌注");
                } else if (2 == mActivity.currentStage) {
                    tvTreatmentStep.setText("留腹");
                } else if (3 == mActivity.currentStage) {
                    tvTreatmentStep.setText("引流");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFirst(int upper, int lower) {
        tvUpWeightInitialValue.setText(String.valueOf(upper));//上位秤腹膜透析液的初始值
        tvPerfusionVolume.setText(String.valueOf(lower));
    }
    private DetailedBean detailedBean;
    private void init() {
        if (detailedBean == null) {
            detailedBean = mActivity.detailedBean;
        }
        try {
            tvUpWeightInitialValue.setText(String.valueOf(mActivity.dialysateInitialValue));//上位秤腹膜透析液的初始值
//        tvCycle.setText(String.valueOf(mActivity.treatmentParameterEniity.cycle));//循环治疗周期数
            tvCycle.setText(String.valueOf(detailedBean.getMaxCycle()));
            tvPerfusionVolume.setText(String.valueOf(mActivity.lowFirstValue));
            tvMaxWarningValue.setText(String.valueOf(PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue));//灌注警戒值
            setStageAndCycle();
            rinseSupplyVol.setText("预冲补液量:" + MyApplication.rinseSupplyVol);
            rinseVol.setText("预冲量:" + MyApplication.rinseVol);
            switch (detailedBean.getVaccum()) {
                case "00":
                case "80":
//            tvValveNegpreDrain.setTextColor(Color.YELLOW);
                    tvValveNegpreDrain.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveNegpreDrain.setTextColor(Color.GREEN);
                    tvValveNegpreDrain.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveNegpreDrain.setTextColor(Color.GREEN);
                    tvValveNegpreDrain.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValveNegpreDrain.setTextColor(Color.RED);
                    tvValveNegpreDrain.setBackgroundResource(R.color.red);
                    break;
            }
//        Log.e("详细状态","vaccum--"+mDeviceStatusInfo.vaccum+
//                "supply"+mDeviceStatusInfo.supply);
            switch (detailedBean.getSupply1()) {
                case "00":
                case "80":
//            tvValveSupply.setTextColor(Color.YELLOW);
//            tvValveSupply.setTextColor(Color.YELLOW);
                    tvValveSupply.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSupply.setTextColor(Color.GREEN);
                    tvValveSupply.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSupply.setTextColor(Color.GREEN);
                    tvValveSupply.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValveSupply.setTextColor(Color.RED);
                    tvValveSupply.setBackgroundResource(R.color.red);
                    break;
            }
            switch (detailedBean.getPerfuse()) {
                case "00":
                case "80":
//            tvValvePerfusion.setTextColor(Color.YELLOW);
                    tvValvePerfusion.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValvePerfusion.setTextColor(Color.GREEN);
                    tvValvePerfusion.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValvePerfusion.setTextColor(Color.GREEN);
                    tvValvePerfusion.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValvePerfusion.setTextColor(Color.RED);
                    tvValvePerfusion.setBackgroundResource(R.color.red);
                    break;
            }
            switch (detailedBean.getDrain()) {
                case "00":
                case "80":
//            tvValveDrain.setTextColor(Color.YELLOW);
                    tvValveDrain.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveDrain.setTextColor(Color.GREEN);
                    tvValveDrain.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveDrain.setTextColor(Color.GREEN);
                    tvValveDrain.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValveDrain.setTextColor(Color.RED);
                    tvValveDrain.setBackgroundResource(R.color.red);
                    break;
            }
            switch (detailedBean.getSafe()) {
                case "00":
                case "80":
//            tvValveSafe.setTextColor(Color.YELLOW);
                    tvValveSafe.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSafe.setTextColor(Color.GREEN);
                    tvValveSafe.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSafe.setTextColor(Color.GREEN);
                    tvValveSafe.setBackgroundResource(R.color.darkblue);
                    break;
                default:
                    tvValveSafe.setBackgroundResource(R.color.red);
//            tvValveSafe.setTextColor(Color.RED);
                    break;
            }
            switch (detailedBean.getSupply2()) {
                case "00":
                case "80":
//            tvValveSupply2.setTextColor(Color.YELLOW);
                    tvValveSupply2.setBackgroundResource(R.color.orange);
                    break;
                case "01":
                case "81":
//            tvValveSupply2.setTextColor(Color.GREEN);
                    tvValveSupply2.setBackgroundResource(R.color.blue);
                    break;
                case "02":
//            tvValveSupply2.setTextColor(Color.GREEN);
                    tvValveSupply2.setBackgroundResource(R.color.darkblue);
                    break;
                default:
//            tvValveSupply2.setTextColor(Color.RED);
                    tvValveSupply2.setBackgroundResource(R.color.red);
                    break;
            }
            tvT0Temp.setText(getTemp(detailedBean.getTemp()) + "℃");
            tvT1Temp.setText(getTemp(detailedBean.getIsT1Err()) + "℃");
            tvT2Temp.setText(getTemp(detailedBean.getIsT2Err()) + "℃");
            tvCurrRinsePerfusionVolume.setText(String.valueOf(detailedBean.getCurrRinsePerfusionVolume()));//当前冲洗灌注量
            tvCurrRinseNum.setText(String.valueOf(detailedBean.getCurrRinseNum()));//当前冲洗灌注 次数
            tvAbdomenRetainingTime.setText(detailedBean.getRetainTime());//
            tvPredictedResidualLiquidVolume.setText(String.valueOf(detailedBean.getRetain()));//预计腹腔剩余液体量
            tvCurrCycleDrainVolume.setText(String.valueOf(detailedBean.getCurrDrainVol()));//当前周期引流量
            tvUpWeightRealTimeValue.setText(String.valueOf(detailedBean.getUpper()));//上位秤实时值
            tvLowWeightRealTimeValue.setText(String.valueOf(detailedBean.getLower()));//下位秤实时值
            currCycle = detailedBean.getCurrCycle();
            tvCurrCycle.setText(String.valueOf(detailedBean.getCurrCycle()));//当前治疗周期数
            tvPerfusionTargetVolume.setText(String.valueOf(detailedBean.getPerTargetVolume()));//灌注目标值
            tvCurrCyclePerfusionVolume.setText(String.valueOf(detailedBean.getCurrCyclePerfusionVolume()));//当前周期灌注量

            int mDrainPassVolume = detailedBean.getDrainTarget() * (currCycle == 0 ?
                    PdproHelper.getInstance().getDrainParameterBean().drainZeroCyclePercentage : PdproHelper.getInstance().getDrainParameterBean().drainOtherCyclePercentage);
            tvDrainTargetVolume.setText(String.valueOf(detailedBean.getDrainTarget()));
            tvDrainPassVolume.setText(String.valueOf(mDrainPassVolume / 100));//引流及格值

            if (detailedBean.getmSupplyTargetValue() >0) {
                tvSupplyTargetValue.setText(String.valueOf(detailedBean.getmSupplyTargetValue()));//补液保护值
            } else {
                tvSupplyTargetValue.setText(String.valueOf(0));//补液保护值
            }
            tvAbdomenRetainingVolume.setText(String.valueOf(detailedBean.getSupplyVol()));
            tvSupplyTargetProtectionValue.setText(String.valueOf(detailedBean.getmSupplyTargetProtectionValue()));//补液目标值 = 上位秤腹膜透析液的初始值 + 补液保护值
        } catch (Exception e) {
            Log.e("设备信息", e.getLocalizedMessage());
        }
    }

    @Override
    public void notifyByThemeChanged() {

    }
}
