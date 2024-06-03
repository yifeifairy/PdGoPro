package com.emt.pdgo.next.ui.activity.param;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.data.bean.OtherParamBean;
import com.emt.pdgo.next.interfaces.OnToggledListener;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberDialog;
import com.emt.pdgo.next.ui.view.ToggleableView;
import com.emt.pdgo.next.ui.widget.LabeledSwitch;
import com.emt.pdgo.next.util.CacheUtils;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherParamActivity extends BaseActivity {

    @BindView(R.id.perHeartWeightTv)
    TextView tvPerHeartWeight;
    @BindView(R.id.perHeartWeightRl)
    RelativeLayout perHeartWeightRl;

    @BindView(R.id.upperTv)
    TextView upperTv;
    @BindView(R.id.upperRl)
    RelativeLayout upperRl;

    @BindView(R.id.lowerTv)
    TextView lowerTv;
    @BindView(R.id.lowerRl)
    RelativeLayout lowerRl;

    @BindView(R.id.sleepTv)
    TextView sleepTv;
    @BindView(R.id.sleepRl)
    RelativeLayout sleepRl;


    @BindView(R.id.debugSwitch)
    LabeledSwitch debugSwitch;

    @BindView(R.id.isHospitalSwitch)
    LabeledSwitch isHospitalSwitch;

    @BindView(R.id.usbSwitch)
    LabeledSwitch usbSwitch;

    private OtherParamBean bean;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_other_param);
        ButterKnife.bind(this);
        initHeadTitleBar("其他参数设置","保存");
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;
    @Override
    public void registerEvents() {
        bean = PdproHelper.getInstance().getOtherParamBean();
        usbSwitch.setOn(MyApplication.usb);
        tvPerHeartWeight.setText(String.valueOf(bean.perHeartWeight));
        perHeartWeightRl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.CHECK_TYPE_PREHEAT,0,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        btnSave.setOnClickListener(v -> {
            CacheUtils.getInstance().getACache().put(PdGoConstConfig.OTHER_PARAMETER, bean);
            toastMessage("保存成功");
        });
        debugSwitch.setOn(bean.isDebug);
        debugSwitch.setOnToggledListener((toggleableView, isOn) -> {
            bean.isDebug = isOn;
        });
        usbSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                MyApplication.usb = isOn;
                Intent intent = new Intent();
                intent.setAction(isOn?"android.intent.action.norco_disk_enable":"android.intent.action.norco_disk_disable");
                sendBroadcast(intent);
            }
        });
        btnSave.setVisibility(View.VISIBLE);
        btnSave.setText("保存");
        btnBack.setOnClickListener(view -> onBackPressed());
        isHospitalSwitch.setOn(bean.isHospital);
        isHospitalSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                bean.isHospital = isOn;
            }
        });
        upperTv.setText(String.valueOf(bean.upper));
        lowerTv.setText(String.valueOf(bean.lower));
        sleepTv.setText(String.valueOf(bean.sleep));
        upperRl.setOnClickListener(view -> alertNumberBoardDialog("upper",0,5000));
        lowerTv.setOnClickListener(view -> alertNumberBoardDialog("lower",0,5000));
        sleepRl.setOnClickListener(view -> alertNumberBoardDialog("sleep",1,1440));
    }

    @Override
    public void initViewData() {

    }

    private void alertNumberBoardDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(this, type, min,max);
        dialog.show();
        dialog.setOnDialogResultListener(new NumberDialog.OnDialogResultListener() {
            @Override
            public void onResult(String mType, String result) {
                if (!TextUtils.isEmpty(result)) {
//                    Logger.d(result);
                    if (mType.equals(PdGoConstConfig.CHECK_TYPE_PREHEAT)) {//上位秤 称量系数
                        tvPerHeartWeight.setText(result);
                        bean.perHeartWeight = Integer.parseInt(result);
                    } else if (mType.equals("upper")) {
                        bean.upper = Integer.parseInt(result);
                        upperTv.setText(String.valueOf(bean.upper));
                    } else if (mType.equals("lower")) {
                        bean.lower = Integer.parseInt(result);
                        lowerTv.setText(String.valueOf(bean.lower));
                    } else if (mType.equals("sleep")) {
                        bean.sleep = Integer.parseInt(result);
                        sleepTv.setText(String.valueOf(bean.sleep));
                    }
                }
            }
        });
    }

    @Override
    public void notifyByThemeChanged() {

    }
}