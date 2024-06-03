package com.emt.pdgo.next.ui.activity.apd.prescrip;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.data.bean.AapdBean;
import com.emt.pdgo.next.ui.activity.MyDreamActivity;
import com.emt.pdgo.next.ui.activity.PreHeatActivity;
import com.emt.pdgo.next.ui.activity.apd.param.ApdParamSetActivity;
import com.emt.pdgo.next.ui.activity.local.LocalPrescriptionActivity;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.NumberDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AapdActivity extends BaseActivity {

    @BindView(R.id.totalVolRl)
    RelativeLayout totalVolRl;
    @BindView(R.id.totalVolTv)
    TextView totalVolTv;
    @BindView(R.id.finalRetainVolRl)
    RelativeLayout finalRetainVolRl;
    @BindView(R.id.finalRetainVolTv)
    TextView finalRetainVolTv;

    @BindView(R.id.group_1_tv)
    TextView group_1_tv;

    @BindView(R.id.per_01_rl)
    RelativeLayout per_01_rl;
    @BindView(R.id.per_01_tv)
    TextView per_01_tv;

    @BindView(R.id.retain_01_rl)
    RelativeLayout retain_01_rl;
    @BindView(R.id.retain_01_tv)
    TextView retain_01_tv;

    @BindView(R.id.cycle_01_rl)
    RelativeLayout cycle_01_rl;
    @BindView(R.id.cycle_01_tv)
    TextView cycle_01_tv;

    @BindView(R.id.group_2_tv)
    TextView group_2_tv;

    @BindView(R.id.per_02_rl)
    RelativeLayout per_02_rl;
    @BindView(R.id.per_02_tv)
    TextView per_02_tv;

    @BindView(R.id.retain_02_rl)
    RelativeLayout retain_02_rl;
    @BindView(R.id.retain_02_tv)
    TextView retain_02_tv;

    @BindView(R.id.cycle_02_rl)
    RelativeLayout cycle_02_rl;
    @BindView(R.id.cycle_02_tv)
    TextView cycle_02_tv;

    @BindView(R.id.group_3_tv)
    TextView group_3_tv;

    @BindView(R.id.per_03_rl)
    RelativeLayout per_03_rl;
    @BindView(R.id.per_03_tv)
    TextView per_03_tv;

    @BindView(R.id.retain_03_rl)
    RelativeLayout retain_03_rl;
    @BindView(R.id.retain_03_tv)
    TextView retain_03_tv;

    @BindView(R.id.cycle_03_rl)
    RelativeLayout cycle_03_rl;
    @BindView(R.id.cycle_03_tv)
    TextView cycle_03_tv;

    @BindView(R.id.group_4_tv)
    TextView group_4_tv;

    @BindView(R.id.per_04_rl)
    RelativeLayout per_04_rl;
    @BindView(R.id.per_04_tv)
    TextView per_04_tv;

    @BindView(R.id.retain_04_rl)
    RelativeLayout retain_04_rl;
    @BindView(R.id.retain_04_tv)
    TextView retain_04_tv;

    @BindView(R.id.cycle_04_rl)
    RelativeLayout cycle_04_rl;
    @BindView(R.id.cycle_04_tv)
    TextView cycle_04_tv;

    @BindView(R.id.group_5_tv)
    TextView group_5_tv;

    @BindView(R.id.per_05_rl)
    RelativeLayout per_05_rl;
    @BindView(R.id.per_05_tv)
    TextView per_05_tv;

    @BindView(R.id.retain_05_rl)
    RelativeLayout retain_05_rl;
    @BindView(R.id.retain_05_tv)
    TextView retain_05_tv;

    @BindView(R.id.cycle_05_rl)
    RelativeLayout cycle_05_rl;
    @BindView(R.id.cycle_05_tv)
    TextView cycle_05_tv;

    @BindView(R.id.group_6_tv)
    TextView group_6_tv;

    @BindView(R.id.per_06_rl)
    RelativeLayout per_06_rl;
    @BindView(R.id.per_06_tv)
    TextView per_06_tv;

    @BindView(R.id.retain_06_rl)
    RelativeLayout retain_06_rl;
    @BindView(R.id.retain_06_tv)
    TextView retain_06_tv;

    @BindView(R.id.cycle_06_rl)
    RelativeLayout cycle_06_rl;
    @BindView(R.id.cycle_06_tv)
    TextView cycle_06_tv;

    @BindView(R.id.rg1)
    RadioGroup rg1;
    @BindView(R.id.rb1C1)
    RadioButton rb1c1;
    @BindView(R.id.rb1C2)
    RadioButton rb1c2;

    @BindView(R.id.rg2)
    RadioGroup rg2;
    @BindView(R.id.rb2C1)
    RadioButton rb2c1;
    @BindView(R.id.rb2C2)
    RadioButton rb2c2;

    @BindView(R.id.rg3)
    RadioGroup rg3;
    @BindView(R.id.rb3C1)
    RadioButton rb3c1;
    @BindView(R.id.rb3C2)
    RadioButton rb3c2;

    @BindView(R.id.rg4)
    RadioGroup rg4;
    @BindView(R.id.rb4C1)
    RadioButton rb4c1;
    @BindView(R.id.rb4C2)
    RadioButton rb4c2;

    @BindView(R.id.rg5)
    RadioGroup rg5;
    @BindView(R.id.rb5C1)
    RadioButton rb5c1;
    @BindView(R.id.rb5C2)
    RadioButton rb5c2;

    @BindView(R.id.rg6)
    RadioGroup rg6;
    @BindView(R.id.rb6C1)
    RadioButton rb6c1;
    @BindView(R.id.rb6C2)
    RadioButton rb6c2;

    @BindView(R.id.finalSupplyCheck)
    CheckBox finalSupplyCheck;

    @BindView(R.id.bag_rl)
    RelativeLayout bag_rl;
    @BindView(R.id.bag_tv)
    TextView bag_tv;

    private AapdBean bean;

    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.btnBack)
    Button btnBack;

    @BindView(R.id.btnRecord)
    Button btnRecord;
    @BindView(R.id.btnRx)
    Button btnRx;
    @BindView(R.id.btnParam)
    Button btnParam;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_aapd);
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
        per_01_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_p1, EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        per_02_rl.setOnClickListener(v -> {
            alertNumberBoardDialog( PdGoConstConfig.aApd_p2,EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        per_03_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_p3,EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });
        per_04_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_p4,EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });

        bag_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_bag,EmtConstant.aapdCycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });

        retain_01_rl.setOnClickListener(v -> {
            alertNumberBoardDialog( PdGoConstConfig.aApd_r1, EmtConstant.aapdAbdTimeMin,EmtConstant.abdTimeMax);
        });
        retain_02_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_r2, EmtConstant.aapdAbdTimeMin,EmtConstant.abdTimeMax);
        });
        retain_03_rl.setOnClickListener(v -> {
            alertNumberBoardDialog( PdGoConstConfig.aApd_r3, EmtConstant.aapdAbdTimeMin,EmtConstant.abdTimeMax);
        });
        retain_04_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_r4, EmtConstant.aapdAbdTimeMin,EmtConstant.abdTimeMax);
        });

        cycle_01_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_c1, EmtConstant.aapdCycleNumMin,EmtConstant.cycleNumMax);
        });
        cycle_02_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_c2,EmtConstant.aapdCycleNumMin,EmtConstant.cycleNumMax);
        });
        cycle_03_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_c3, EmtConstant.aapdCycleNumMin,EmtConstant.cycleNumMax);
        });
        cycle_04_rl.setOnClickListener(v -> {
            alertNumberBoardDialog(PdGoConstConfig.aApd_c4, EmtConstant.aapdCycleNumMin,EmtConstant.cycleNumMax);
        });

        finalRetainVolRl.setOnClickListener(v -> {
            alertNumberBoardDialog( PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY,EmtConstant.cycleVolMin,PdproHelper.getInstance().getPerfusionParameterBean().perfMaxWarningValue);
        });

        btnNext.setOnClickListener(v -> {
            next();
        });
    }

    private void next() {
        if (bean.c2 != 0 && bean.r2 != 0 && bean.p2 != 0) {
            if (bean.c3 != 0 && bean.r3 != 0 && bean.p3 != 0) {
                if (bean.c4 != 0 && bean.r4 != 0 && bean.p4 != 0) {
                    dn();
                } else if (bean.c4 == 0 && bean.r4 == 0 && bean.p4 == 0) {
                    dn();
                } else {
                    if (bean.c4 == 0 ) {
                        toastMessage("第4组周期数不能为0");
                    } else if (bean.r4 == 0) {
                        toastMessage("第4组留腹时间不能为0");
                    } else if (bean.p4 == 0) {
                        toastMessage("第4组灌注量不能为0");
                    }
//                            toastMessage("第四组未设置正确");
                }
            } else {
                if (bean.c3 == 0 ) {
                    toastMessage("第3组周期数不能为0");
                } else if (bean.r3 == 0) {
                    toastMessage("第3组留腹时间不能为0");
                } else if (bean.p3 == 0) {
                    toastMessage("第3组灌注量不能为0");
                }
            }
        } else if (bean.c2 == 0 && bean.r2 == 0 && bean.p2 == 0) {
            if (bean.c3 != 0 && bean.r3 != 0 && bean.p3 != 0) {
                if (bean.c2 == 0 ) {
                    toastMessage("第2组周期数不能为0");
                } else if (bean.r2 == 0) {
                    toastMessage("第2组留腹时间不能为0");
                } else if (bean.p2 == 0) {
                    toastMessage("第2组灌注量不能为0");
                }
//                        toastMessage("第2组未设置正确");
            } else if (bean.c4 != 0 && bean.r4 != 0 && bean.p4 != 0) {
//                        toastMessage("第2组未设置正确");
                if (bean.c2 == 0 ) {
                    toastMessage("第2组周期数不能为0");
                } else if (bean.r2 == 0) {
                    toastMessage("第2组留腹时间不能为0");
                } else if (bean.p2 == 0) {
                    toastMessage("第2组灌注量不能为0");
                }
            } else {
                dn();
            }
        } else {
            if (bean.c2 == 0 ) {
                toastMessage("第2组周期数不能为0");
            } else if (bean.r2 == 0) {
                toastMessage("第2组留腹时间不能为0");
            } else if (bean.p2 == 0) {
                toastMessage("第2组灌注量不能为0");
            }
        }
    }

    private void dn() {
        CacheUtils.getInstance().getACache().put(PdGoConstConfig.aApd_PARAMS, bean);
        MyApplication.apdMode = 4;
        doGoTOActivity(PreHeatActivity.class);
    }

    @Override
    public void initViewData() {
        bean = PdproHelper.getInstance().aapdBean();

        totalVolTv.setText(String.valueOf(bean.total));
        finalRetainVolTv.setText(String.valueOf(bean.finalVol));

        per_01_tv.setText(String.valueOf(bean.p1));
        per_02_tv.setText(String.valueOf(bean.p2));
        per_03_tv.setText(String.valueOf(bean.p3));
        per_04_tv.setText(String.valueOf(bean.p4));
        per_05_tv.setText(String.valueOf(bean.p5));
        per_06_tv.setText(String.valueOf(bean.p6));

        bag_tv.setText(String.valueOf(bean.bagVol));

        retain_01_tv.setText(String.valueOf(bean.r1));
        retain_02_tv.setText(String.valueOf(bean.r2));
        retain_03_tv.setText(String.valueOf(bean.r3));
        retain_04_tv.setText(String.valueOf(bean.r4));
        retain_05_tv.setText(String.valueOf(bean.r5));
        retain_06_tv.setText(String.valueOf(bean.r6));

        cycle_01_tv.setText(String.valueOf(bean.c1));
        cycle_02_tv.setText(String.valueOf(bean.c2));
        cycle_03_tv.setText(String.valueOf(bean.c3));
        cycle_04_tv.setText(String.valueOf(bean.c4));
        cycle_05_tv.setText(String.valueOf(bean.c5));
        cycle_06_tv.setText(String.valueOf(bean.c6));

        finalSupplyCheck.setChecked(bean.isFinalSupply);
        finalSupplyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bean.isFinalSupply = isChecked;
        });

        rg1.check(bean.a1 == 0 ? R.id.rb1C1: R.id.rb1C2);
        rg2.check(bean.a2 == 0 ? R.id.rb2C1: R.id.rb2C2);
        rg3.check(bean.a3 == 0 ? R.id.rb3C1: R.id.rb3C2);
        rg4.check(bean.a4 == 0 ? R.id.rb4C1: R.id.rb4C2);
        rg5.check(bean.a5 == 0 ? R.id.rb5C1: R.id.rb5C2);
        rg6.check(bean.a6 == 0 ? R.id.rb6C1: R.id.rb6C2);

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    bean.a1 = 0;
                } else if (rb.getText().equals("通道2")) {
                    bean.a1 = 1;
                }
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    bean.a2 = 0;
                } else if (rb.getText().equals("通道2")) {
                    bean.a2 = 1;
                }
            }
        });
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    bean.a3 = 0;
                } else if (rb.getText().equals("通道2")) {
                    bean.a3 = 1;
                }
            }
        });
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    bean.a4 = 0;
                } else if (rb.getText().equals("通道2")) {
                    bean.a4 = 1;
                }
            }
        });
        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    bean.a5 = 0;
                } else if (rb.getText().equals("通道2")) {
                    bean.a5 = 1;
                }
            }
        });
        rg6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getText().equals("通道1")) {
                    bean.a6 = 0;
                } else if (rb.getText().equals("通道2")) {
                    bean.a6 = 1;
                }
            }
        });
    }

    private void alertNumberBoardDialog(String type, int min, int max) {
        NumberDialog dialog = new NumberDialog(this,  type, min, max);
        dialog.show();
        dialog.setOnDialogResultListener((mType, result) -> {
            if (!TextUtils.isEmpty(result)) {
                switch (mType) {
                    case PdGoConstConfig.CHECK_TYPE_PRESCRIPTION_ABDOMEN_RETAINING_VOLUME_FINALLY:
                        finalRetainVolTv.setText(result);
                        bean.finalVol = Integer.parseInt(result);
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_bag:
                        bag_tv.setText(result);
                        bean.bagVol = Integer.parseInt(result);
                        setCycleNum();
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_p1:
                        per_01_tv.setText(result);
                        bean.p1 = Integer.parseInt(result);
//                        if (bean.p1 == 0) {
//                            bean.c1 = 0;
//                            bean.r1 = 0;
//                        }
                        setCycleNum();
                        break;
                    case PdGoConstConfig.aApd_p2:
                        per_02_tv.setText(result);
                        bean.p2 = Integer.parseInt(result);
                        if (bean.p2 == 0) {
                            bean.c2 = 0;
                            bean.r2 = 0;
                            cycle_02_tv.setText(String.valueOf(bean.c2));
                            retain_02_tv.setText(String.valueOf(bean.r2));
                        }
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_p3:
                        per_03_tv.setText(result);
                        bean.p3 = Integer.parseInt(result);
                        if (bean.p3 == 0) {
                            bean.c3 = 0;
                            bean.r3 = 0;
                            cycle_03_tv.setText(String.valueOf(bean.c3));
                            retain_03_tv.setText(String.valueOf(bean.r3));
                        }
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_p4:
                        per_04_tv.setText(result);
                        bean.p4 = Integer.parseInt(result);
                        if (bean.p4 == 0) {
                            bean.c4 = 0;
                            bean.r4 = 0;
                            cycle_04_tv.setText(String.valueOf(bean.c4));
                            retain_04_tv.setText(String.valueOf(bean.r4));
                        }
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_p5:
                        per_05_tv.setText(result);
                        bean.p5 = Integer.parseInt(result);
                        if (bean.p5 == 0) {
                            bean.c5 = 0;
                            bean.r5 = 0;
                            cycle_05_tv.setText(String.valueOf(bean.c5));
                            retain_05_tv.setText(String.valueOf(bean.r5));
                        }
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_p6:
                        per_06_tv.setText(result);
                        bean.p6 = Integer.parseInt(result);
                        if (bean.p6 == 0) {
                            bean.c6 = 0;
                            bean.r6 = 0;
                            cycle_06_tv.setText(String.valueOf(bean.c6));
                            retain_06_tv.setText(String.valueOf(bean.r6));
                        }
                        setTotal();
                        break;

                    case PdGoConstConfig.aApd_r1:
                        retain_01_tv.setText(result);
                        bean.r1 = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.aApd_r2:
                        retain_02_tv.setText(result);
                        bean.r2 = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.aApd_r3:
                        retain_03_tv.setText(result);
                        bean.r3 = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.aApd_r4:
                        retain_04_tv.setText(result);
                        bean.r4 = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.aApd_r5:
                        retain_05_tv.setText(result);
                        bean.r5 = Integer.parseInt(result);
                        break;
                    case PdGoConstConfig.aApd_r6:
                        retain_06_tv.setText(result);
                        bean.r6 = Integer.parseInt(result);
                        break;

                    case PdGoConstConfig.aApd_c1:
                        cycle_01_tv.setText(result);
                        bean.c1 = Integer.parseInt(result);
//                        setTotal();
                        setCyclePre();
                        break;
                    case PdGoConstConfig.aApd_c2:
                        cycle_02_tv.setText(result);
                        bean.c2 = Integer.parseInt(result);
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_c3:
                        cycle_03_tv.setText(result);
                        bean.c3 = Integer.parseInt(result);
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_c4:
                        cycle_04_tv.setText(result);
                        bean.c4 = Integer.parseInt(result);
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_c5:
                        cycle_05_tv.setText(result);
                        bean.c5 = Integer.parseInt(result);
                        setTotal();
                        break;
                    case PdGoConstConfig.aApd_c6:
                        cycle_06_tv.setText(result);
                        bean.c6 = Integer.parseInt(result);
                        setTotal();
                        break;
                }
            }
        });
    }

    /**
     * 设置治疗周期数
     */
    private void setCycleNum() {
        //循环治疗周期数 N=int((腹透液重量-首次灌注量-最末留腹量-消耗扣除500)/每周期灌注量)
        int cycle =  bean.bagVol / bean.p1;
        if(cycle <= 0){
            cycle = 1;
        }
        bean.c1 = cycle;
        cycle_01_tv.setText(String.valueOf(bean.c1));
    }

    private void setCyclePre() {
        double total = bean.bagVol / Double.parseDouble(String.valueOf(bean.c1)) / 50.00;

        int perCyclePerfusionVolume = (int) Math.floor(total);
        int cycleVol = perCyclePerfusionVolume * 50;
        per_01_tv.setText(String.valueOf(cycleVol));
        bean.p1 = cycleVol;
    }

    private void setTotal() {
        bean.total = bean.bagVol + bean.p2 * bean.c2 + bean.p3 * bean.c3 + bean.p4 * bean.c4
                + bean.p5 * bean.c5 + bean.p6 * bean.c6 + bean.finalVol + 500;
        totalVolTv.setText(String.valueOf(bean.total));
    }

    @Override
    public void notifyByThemeChanged() {

    }
}