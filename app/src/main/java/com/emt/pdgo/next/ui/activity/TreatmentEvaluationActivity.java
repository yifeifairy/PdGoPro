package com.emt.pdgo.next.ui.activity;

import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.PdEntity;
import com.emt.pdgo.next.ui.adapter.LocalPdAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TreatmentEvaluationActivity extends BaseActivity {

    Unbinder unbinder;
    @BindView(R.id.tv_total_perfusion_volume)
    TextView tvTotalPerfusionVolume;
    @BindView(R.id.tv_total_drain_volume)
    TextView tvTotalDrainVolume;
    @BindView(R.id.tv_curr_ultrafiltration_volume)
    TextView tvCurrUltrafiltrationVolume;

    @BindView(R.id.rv_list)
    RecyclerView recyclerView;

    @BindView(R.id.assess)
    StateButton assess;

    @BindView(R.id.powerOff)
    StateButton powerOff;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_treatment_evaluation);
        ButterKnife.bind(this);
        speak("请关闭人体端导管");

    }

    @Override
    public void registerEvents() {
        assess.setOnClickListener(view -> doGoTOActivity(TreatmentFeedbackActivity.class));
        powerOff.setOnClickListener(view -> {
            openValve();
        });
        MyApplication.treatmentRunning = false;
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setContent("请关闭人体端开关,关上碘伏帽,取出卡匣后关闭电源")
                .setBtnFirst("确定")
                .setFirstClickListener(mCommonDialog -> {
                    String mCommand = CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BATTERY_OFF);
                    sendToMainBoard(mCommand);
                    mCommonDialog.dismiss();
                });
        if (!TreatmentEvaluationActivity.this.isFinishing()) {
            dialog.show();
        }
    }

    private void openValve() {
        showLoading("阀门打开中，请稍等");
//        showTipsCommonDialog("电机打开中，请稍等");
        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group1"));
        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group2"));
//                speak("请取出卡匣,关闭所有管夹");
        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group3"));
    }

    private LocalPdAdapter localPdAdapter;
    private List<PdEntity> getPdInfo(int page) {
        return EmtDataBase.getInstance(TreatmentEvaluationActivity.this).getPdDao()
                .getPdListById(page);
    }
    private int page = EmtDataBase.getInstance(TreatmentEvaluationActivity.this).getPdDao().getPdList().size();

    public void initViewData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        localPdAdapter = new LocalPdAdapter(getPdInfo(page),false);
        recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(localPdAdapter);

    }

//    private int n = 0;
//    @Subscribe(code = RxBusCodeConfig.EVENT_CMD_RESULT_OK)
//    public void receiveCmdResultOk(String topic) {
//        if (topic.contains("valve/open")) {//开始治疗
//            n ++;
//            if (n == 3) {
//                dismissLoading();
//                n = 0;
//            }
//        }
//
//    }

    @Override
    public void notifyByThemeChanged() {

    }

}
