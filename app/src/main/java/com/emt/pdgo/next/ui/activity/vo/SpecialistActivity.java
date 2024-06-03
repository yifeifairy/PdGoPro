package com.emt.pdgo.next.ui.activity.vo;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.data.bean.ExpertBean;
import com.emt.pdgo.next.ui.activity.MyDreamActivity;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.activity.local.LocalPrescriptionActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.ClickUtil;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialistActivity extends BaseActivity {

    @BindView(R.id.totalVolTv)
    TextView totalVolTv;
    @BindView(R.id.totalVolLl)
    LinearLayout totalVolLl;
    @BindView(R.id.cycleVolTv)
    TextView cycleVolTv;
    @BindView(R.id.cycleVolLl)
    LinearLayout cycleVolLl;
    @BindView(R.id.cycleNumTv)
    TextView cycleNumTv;
    @BindView(R.id.cycleNumLl)
    LinearLayout cycleNumLl;
    @BindView(R.id.abdTimeTv)
    TextView abdTimeTv;
    @BindView(R.id.abdTimeLl)
    LinearLayout abdTimeLl;
    @BindView(R.id.finalVolTv)
    TextView finalVolTv;
    @BindView(R.id.finalVolLl)
    LinearLayout finalVolLl;
    @BindView(R.id.lastVolTv)
    TextView lastVolTv;
    @BindView(R.id.lastVolLl)
    LinearLayout lastVolLl;
    @BindView(R.id.ultVolTv)
    TextView ultVolTv;
    @BindView(R.id.ultVolLl)
    LinearLayout ultVolLl;
    @BindView(R.id.firstVolTv)
    TextView firstVolTv;
    @BindView(R.id.firstVolLl)
    LinearLayout firstVolLl;

    @BindView(R.id.stage_01_CycleVolLl)
    LinearLayout stage_01_CycleVolLl;
    @BindView(R.id.stage_01_CycleNumLl)
    LinearLayout stage_01_CycleNumLl;
    @BindView(R.id.stage_01_VolTv)
    TextView stage_01_VolTv;
    @BindView(R.id.stage_01_cycleNumTv)
    TextView stage_01_cycleNumTv;
    @BindView(R.id.stage_02_cycleVolTv)
    TextView stage_02_cycleVolTv;
    @BindView(R.id.stage_02_cycleNumTv)
    TextView stage_02_cycleNumTv;
    @BindView(R.id.stage_02_cycleVolLl)
    LinearLayout stage_02_cycleVolLl;
    @BindView(R.id.stage_02_cycleNumLl)
    LinearLayout stage_02_cycleNumLl;

    @BindView(R.id.cycleMyself)
    CheckBox cycleMyself;

    @BindView(R.id.btnRecord)
    Button btnRecord;
    @BindView(R.id.btnRx)
    Button btnRx;
    @BindView(R.id.btnParam)
    Button btnParam;

    @BindView(R.id.isFinalBtn)
    Button isFinalBtn;

    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.modeBtn)
    Button modeBtn;
    @BindView(R.id.reviseBtn)
    Button reviseBtn;

    @BindView(R.id.btnBack)
    Button btnBack;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_specialist);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {
        btnBack.setOnClickListener(view -> onBackPressed());
        btnParam.setOnClickListener(view -> {
            doGoTOActivity(ApdParamSetActivity.class);
        });
        btnRecord.setOnClickListener(view -> {
            doGoTOActivity(MyDreamActivity.class);
        });
        btnRx.setOnClickListener(view -> {
            doGoTOActivity(LocalPrescriptionActivity.class);
        });

//        stage_01_CycleNumLl.setOnClickListener(v -> {
//            SpecialNumberDialog dialog = new SpecialNumberDialog(this, entity, 0, stage_01_cycleNumTv);
//            dialog.show();
//            dialog.setOnDialogResultListener((model, expertBean) -> {
//                stage_01_cycleNumTv.setText(expertBean.baseSupplyCycle.toString());
////                setSupplyTotal(expertBean);
//            });
//        });
//        stage_02_cycleNumLl.setOnClickListener(v -> {
//            SpecialNumberDialog dialog = new SpecialNumberDialog(this, entity, 1, stage_01_cycleNumTv);
//            dialog.show();
//            dialog.setOnDialogResultListener((model, expertBean) -> {
//                stage_02_cycleNumTv.setText(expertBean.osmSupplyCycle.toString());
////                setSupplyTotal(expertBean);
//            });
//        });
        ClickUtil clickUtil = new ClickUtil(reviseBtn, 3000);
        clickUtil.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    doGoTOActivity(SpeVolActivity.class);
                }
            }
        });
        btnNext.setOnClickListener(view -> {
            MyApplication.apdMode = 8;
            doGoTOActivity(PreHeatActivity.class);
        });

    }
    private ExpertBean entity;
    @Override
    public void initViewData() {
//        entity = PdproHelper.getInstance().expertBean();
//        totalVolTv.setText(String.valueOf(entity.total));
//        cycleVolTv.setText(String.valueOf(entity.cycleVol));
//        cycleNumTv.setText(String.valueOf(entity.cycle));
//        firstVolTv.setText(String.valueOf(entity.firstVol));
//        ultVolTv.setText(String.valueOf(entity.ultVol));
//        abdTimeTv.setText(String.valueOf(entity.retainTime));
//        finalVolTv.setText(String.valueOf(entity.finalRetainVol));
//        lastVolTv.setText(String.valueOf(entity.lastRetainVol));
//        isFinalBtn.setText(entity.isFinalSupply? "最末袋(通道1):已开启": "最末袋(通道1):已关闭");
//        stage_01_cycleNumTv.setText(entity.baseSupplyCycle.toString());
//        stage_01_VolTv.setText(String.valueOf(entity.baseSupplyVol));
//        setBg(entity.cycleMyself);
//        cycleMyself.setChecked(entity.cycleMyself);
//        stage_02_cycleNumTv.setText(entity.osmSupplyCycle.toString());
//        stage_02_cycleVolTv.setText(String.valueOf(entity.osmSupplyVol));
        v();

    }

    private void setBg(boolean click) {
        totalVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        cycleNumLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        cycleVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
//        finalVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);
        firstVolLl.setBackgroundResource(click?R.drawable.gray_shape:R.drawable.pa1);

        stage_01_CycleNumLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_01_CycleVolLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);

        stage_02_cycleVolLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);
        stage_02_cycleNumLl.setBackgroundResource(click?R.drawable.pa1:R.drawable.gray_shape);


    }

    @Override
    protected void onResume() {
        super.onResume();
        v();
    }

    private void v() {
        entity = PdproHelper.getInstance().expertBean();
        totalVolTv.setText(String.valueOf(entity.total));
        cycleVolTv.setText(String.valueOf(entity.cycleVol));
        cycleNumTv.setText(String.valueOf(entity.cycle));
        firstVolTv.setText(String.valueOf(entity.firstVol));
        ultVolTv.setText(String.valueOf(entity.ultVol));
        abdTimeTv.setText(String.valueOf(entity.retainTime));
        finalVolTv.setText(String.valueOf(entity.finalRetainVol));
        lastVolTv.setText(String.valueOf(entity.lastRetainVol));
        isFinalBtn.setText(entity.isFinalSupply? "最末袋(通道1):已开启": "最末袋(通道1):已关闭");
        stage_01_cycleNumTv.setText(entity.baseSupplyCycle.toString());
        stage_01_VolTv.setText(String.valueOf(entity.baseSupplyVol));
        setBg(entity.cycleMyself);
        cycleMyself.setChecked(entity.cycleMyself);
        stage_02_cycleNumTv.setText(entity.osmSupplyCycle.toString());
        stage_02_cycleVolTv.setText(String.valueOf(entity.osmSupplyVol));
    }

    @Override
    public void notifyByThemeChanged() {

    }
}