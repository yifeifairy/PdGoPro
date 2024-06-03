package com.emt.pdgo.next.ui.activity.dl;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.data.bean.DetailedBean;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class TrActivity extends BaseActivity {

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
    @BindView(R.id.powerIv)
    ImageView powerIv;
    @BindView(R.id.currentPower)
    TextView currentPower;
    private DetailedBean detailedBean;

    @BindView(R.id.tipView)
    TextView tipView;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_tr);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {
        initHeadTitleBar("当前APD治疗运行数据");
        if (MyApplication.treatmentRunning) {
            mainLayout.setVisibility(View.VISIBLE);
            tipView.setVisibility(View.GONE);
            init();
        } else {
            mainLayout.setVisibility(View.GONE);
            tipView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initViewData() {
        detailedBean = new DetailedBean();
//        tvValveNegpreDrain.setVisibility(mActivity.mTreatmentParameterEniity.isNegpreDrain ? View.VISIBLE : View.GONE);
    }

    private final BigDecimal mTen = new BigDecimal(10);
    private float getTemp(int temp) {
        BigDecimal mTemp = new BigDecimal(temp);
        BigDecimal newTemp = mTemp.divide(mTen, 1, RoundingMode.HALF_UP);
        return newTemp.floatValue();
    }

    private void init() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    if (detailedBean == null) {
                        detailedBean = new DetailedBean();
                    }
                    try {
                        tvUpWeightInitialValue.setText(String.valueOf(detailedBean.getDialysateInitialValue()));//上位秤腹膜透析液的初始值
//        tvCycle.setText(String.valueOf(mActivity.treatmentParameterEniity.cycle));//循环治疗周期数
                        tvCycle.setText(String.valueOf(detailedBean.getMaxCycle()));
                        tvPerfusionVolume.setText(String.valueOf(detailedBean.getLowFirstValue()));
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
                        tvAbdomenRetainingVolume.setText(String.valueOf(detailedBean.getRetain()));
                        tvSupplyTargetProtectionValue.setText(String.valueOf(detailedBean.getmSupplyTargetProtectionValue()));//补液目标值 = 上位秤腹膜透析液的初始值 + 补液保护值
                        if (detailedBean.getIsAcPowerIn() == 1) {
                            powerIv.setImageResource(R.drawable.charging);
                        } else {
                            if (detailedBean.getBatteryLevel() < 30) {
                                powerIv.setImageResource(R.drawable.poor_power);
                            } else if (30 < detailedBean.getBatteryLevel() &&detailedBean.getBatteryLevel() <= 60 ) {
                                powerIv.setImageResource(R.drawable.low_power);
                            } else if (60 < detailedBean.getBatteryLevel() &&detailedBean.getBatteryLevel() <= 80 ) {
                                powerIv.setImageResource(R.drawable.mid_power);
                            } else {
                                powerIv.setImageResource(R.drawable.high_power);
                            }
                        }
                        currentPower.setText(String.valueOf(detailedBean.getBatteryLevel()));
                    } catch (Exception e) {
                        Log.e("设备信息", e.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposableObserver);
    }
    private CompositeDisposable mCompositeDisposable;
    public int currCycle = -1;

    public int currentStage = -1;
    /**
     * 当前周期状态和周期数
     */
    public void setStageAndCycle() {
        try {
            if (currCycle != detailedBean.getCurrCycle()) {
                currCycle = detailedBean.getCurrCycle();
                tvCurrCycle.setText(String.valueOf(detailedBean.getCurrCycle()));
            }
            if (currentStage != detailedBean.getStage()) {
                currentStage = detailedBean.getStage();
                if (1 == detailedBean.getStage()) {
                    tvTreatmentStep.setText("灌注");
                } else if (2 == detailedBean.getStage()) {
                    tvTreatmentStep.setText("留腹");
                } else if (3 == detailedBean.getStage()) {
                    tvTreatmentStep.setText("引流");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void notifyByThemeChanged() {

    }
}