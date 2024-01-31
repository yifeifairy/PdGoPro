package com.emt.pdgo.next.ui.fragment.dpr.treat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emt.pdgo.next.data.serial.dpr.receive.BaseSupplyBean;
import com.emt.pdgo.next.data.serial.dpr.receive.BaseSupplyRunningBean;
import com.emt.pdgo.next.data.serial.dpr.receive.DprDrainVacuumRunningBean;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean1;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.pdp.rmmit.pdp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewDataFragment extends BaseFragment {

    @BindView(R.id.upWeightValueTv)
    TextView upWeightValueTv;
    @BindView(R.id.underWeightValueTv)
    TextView underWeightValueTv;
    @BindView(R.id.retainVolTv)
    TextView retainVolTv;
    @BindView(R.id.drainVolTv)
    TextView drainVolTv;
    @BindView(R.id.drainTargetVolTv)
    TextView drainTargetVolTv;
    @BindView(R.id.drainPassVolTv)
    TextView drainPassVolTv;
    @BindView(R.id.perVolTv)
    TextView perVolTv;
    @BindView(R.id.vacDrainCountTv)
    TextView vacDrainCountTv;
    @BindView(R.id.vacDrainTimeTv)
    TextView vacDrainTimeTv;
    @BindView(R.id.perTargetVolTv)
    TextView perTargetVolTv;
    @BindView(R.id.drainRoteTv)
    TextView drainRoteTv;
    @BindView(R.id.perRoteTv)
    TextView perRoteTv;
    @BindView(R.id.baseSupplyTargetTv)
    TextView baseSupplyTargetTv;
    @BindView(R.id.baseSupplyVolTv)
    TextView baseSupplyVolTv;
    @BindView(R.id.baseSupplyUpWeightTv)
    TextView baseSupplyUpWeightTv;
    @BindView(R.id.osmSupplyTargetTv)
    TextView osmSupplyTargetTv;
    @BindView(R.id.osmSupplyVolTv)
    TextView osmSupplyVolTv;
    @BindView(R.id.osmSupplyUpWeightTv)
    TextView osmSupplyUpWeightTv;

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

    @BindView(R.id.tvT1Temp)
    TextView tvT0Temp;
    @BindView(R.id.tvT2Temp)
    TextView tvT1Temp;
    @BindView(R.id.tvT3Temp)
    TextView tvT2Temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_data, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 基础液
     * @param baseSupplyMsg
     */
    public void setBaseSupplyMsg(BaseSupplyRunningBean baseSupplyMsg) {
        baseSupplyVolTv.setText(String.valueOf(baseSupplyMsg.Supply1Volume));
    }

    public void setBaseSupplyTargetMsg(BaseSupplyBean baseSupplyMsg) {
        baseSupplyTargetTv.setText(String.valueOf(baseSupplyMsg.supplyTarget));
        baseSupplyUpWeightTv.setText(String.valueOf(baseSupplyMsg.upperWeight));
    }

    public void setDrainPassVolTv(int vol) {
        drainPassVolTv.setText(String.valueOf(vol));
    }

    public void setDrainTargetVVolTv(int vol) {
        drainTargetVolTv.setText(String.valueOf(vol));
    }

    public void setRealtimeDrain(int vol) {
        drainVolTv.setText(String.valueOf(vol));
    }

    public void setRealtimePer(int vol) {
        perVolTv.setText(String.valueOf(vol));
    }

    public void setVacDrain(DprDrainVacuumRunningBean bean) {
        vacDrainCountTv.setText(String.valueOf(bean.vaccumCount));
        vacDrainTimeTv.setText(String.valueOf(bean.remainVaccumTimes));
    }

    public void setPerTargetVol(int vol) {
//        perTargetVolTv.setText(String.valueOf(vol));
        perTargetVolTv.setText(String.valueOf(vol));
    }

    public void setRetain(int vol) {
        retainVolTv.setText(String.valueOf(vol));
    }

    /**
     * 渗透液
     * @param baseSupplyMsg
     */
    public void setOsmSupplyMsg(BaseSupplyBean baseSupplyMsg) {
//        osmSupplyTargetTv.setText(String.valueOf(baseSupplyMsg.supply1Target));
        baseSupplyUpWeightTv.setText(String.valueOf(baseSupplyMsg.upperWeight));
//        baseSupplyVolTv.setText(String.valueOf(baseSupplyMsg.supply1Volume));
    }

    public void text(int color) {
        tvValveSupply.setBackgroundResource(color);
    }

    /**
     * 刷新设备信息
     *
     * @param mDeviceStatusInfo 设备信息
     */
    public void setDeviceStatusInfo(ReceiveDeviceBean1 mDeviceStatusInfo) {
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
                default:
//            tvValveNegpreDrain.setTextColor(Color.RED);
                    tvValveNegpreDrain.setBackgroundResource(R.color.red);
                    break;
            }
//        Log.e("详细状态","vaccum--"+mDeviceStatusInfo.vaccum+
//                "supply"+mDeviceStatusInfo.supply);
            switch (mDeviceStatusInfo.supply) {
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
                default:
//            tvValveSupply2.setTextColor(Color.RED);
                    tvValveSupply2.setBackgroundResource(R.color.red);
                    break;
            }

            tvT0Temp.setText(String.valueOf(getTemp(mDeviceStatusInfo.temp)));
            tvT1Temp.setText(String.valueOf(getTemp(mDeviceStatusInfo.isT1Err)));
            tvT2Temp.setText(String.valueOf(getTemp(mDeviceStatusInfo.isT2Err)));
//        tvValveNegpreDrain.setTextColor(mDeviceStatusInfo.vaccum.equals("00") ? 0xfff44336 : 0xff00008B);//末次灌注阀  打开亮红色，关闭亮蓝色
//        tvValveSupply.setTextColor(mDeviceStatusInfo.supply.equals("00") ? 0xffff0000 : 0xff00008B);//补液阀  打开亮红色，关闭亮蓝色
//        tvValvePerfusion.setTextColor(mDeviceStatusInfo.perfuse.equals("00") ? 0xffff0000 : 0xff00008B);//灌注阀 打开亮红色，关闭亮蓝色
//        tvValveDrain.setTextColor(mDeviceStatusInfo.drain.equals("00") ? 0xffff0000 : 0xff00008B);//引流阀 打开亮红色，关闭亮蓝色
            upWeightValueTv.setText(String.valueOf(mDeviceStatusInfo.upper));//上位秤实时值
            underWeightValueTv.setText(String.valueOf(mDeviceStatusInfo.lower));//下位秤实时值
        }catch (Exception e) {
            Log.e("设备信息", e.getLocalizedMessage());
        }
    }

    private final BigDecimal mTen = new BigDecimal(10);
    private float getTemp(int temp) {
        BigDecimal mTemp = new BigDecimal(temp);
        BigDecimal newTemp = mTemp.divide(mTen, 1, RoundingMode.HALF_UP);
        return newTemp.floatValue();
    }

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void notifyByThemeChanged() {

    }
}