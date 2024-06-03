package com.emt.pdgo.next.ui.activity.local;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.DelRxEntity;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.model.mode.IpdBean;
import com.emt.pdgo.next.ui.activity.PrescriptionActivity;
import com.emt.pdgo.next.ui.adapter.local.HisRxLocalAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalPrescriptionActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private HisRxLocalAdapter hisRxLocalAdapter;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_local_prescription);
        ButterKnife.bind(this);
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.title)
    TextView title;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void registerEvents() {
        btnSave.setText("清空");
        btnSave.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(view -> onBackPressed());
        btnSave.setOnClickListener(v -> {
            final CommonDialog dialog = new CommonDialog(this);
            dialog.setContent("确认清空？")
                    .setBtnFirst("确定")
                    .setBtnTwo("取消")
                    .setFirstClickListener(mCommonDialog -> {
                        DelRxEntity delRxEntity = new DelRxEntity();
                        delRxEntity.id = 1;
                        delRxEntity.num = getLocalRx().size();
                        Log.e("处方","处方删除--"+getLocalRx().size());
                        if (EmtDataBase.getInstance(LocalPrescriptionActivity.this).delRxDao().getDelRxById(1) == null) {
                            EmtDataBase
                                    .getInstance(LocalPrescriptionActivity.this)
                                    .delRxDao()
                                    .insertRx(delRxEntity);
                        } else {
                            EmtDataBase
                                    .getInstance(LocalPrescriptionActivity.this)
                                    .delRxDao()
                                    .update(delRxEntity);
                        }
                        // 假清空
                        hisRxLocalAdapter.setNewData(getLocalRxById(EmtDataBase.getInstance(LocalPrescriptionActivity.this).delRxDao().getDelRxById(1).num));
                        hisRxLocalAdapter.notifyDataSetChanged();
                        mCommonDialog.dismiss();
                    })
                    .setTwoClickListener(Dialog::dismiss)
                    .show();
        });
    }

    @Override
    public void initViewData() {
        initRecyclerLocal();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerLocal() {
        if (hisRxLocalAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            Log.e("删除前处方","size:"+EmtDataBase.getInstance(LocalPrescriptionActivity.this).delRxDao().delRxList().size());
            if (EmtDataBase.getInstance(LocalPrescriptionActivity.this).delRxDao().delRxList().size() == 0) {
                hisRxLocalAdapter = new HisRxLocalAdapter(getLocalRx());
            } else {
                hisRxLocalAdapter = new HisRxLocalAdapter(getLocalRxById(EmtDataBase.getInstance(LocalPrescriptionActivity.this).delRxDao().getDelRxById(1).num));
            }
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(hisRxLocalAdapter);
            hisRxLocalAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.applyBtn) {
                    final CommonDialog dialog = new CommonDialog(this);
                    dialog.setContent("是否应用")
                            .setBtnFirst("确定")
                            .setBtnTwo("取消")
                            .setFirstClickListener(mCommonDialog -> {
                                IpdBean ipdBean = PdproHelper.getInstance().ipdBean();
                                // 处方
                                ipdBean.peritonealDialysisFluidTotal = getLocalRx().get(position).perVol;
                                ipdBean.perCyclePerfusionVolume = getLocalRx().get(position).perCycleVol;
                                ipdBean.cycle = getLocalRx().get(position).treatCycle;
                                ipdBean.firstPerfusionVolume = getLocalRx().get(position).firstPerVol;
                                ipdBean.abdomenRetainingTime = getLocalRx().get(position).abdTime;
                                ipdBean.abdomenRetainingVolumeFinally = getLocalRx().get(position).endAbdVol;
                                ipdBean.abdomenRetainingVolumeLastTime = getLocalRx().get(position).lastTimeAbdVol;
                                ipdBean.ultrafiltrationVolume = getLocalRx().get(position).ult;
                                ipdBean.isFinalSupply = false;
                                CacheUtils.getInstance().getACache().put(PdGoConstConfig.IPD_BEAN, ipdBean);
                                doGoTOActivity(PrescriptionActivity.class);
                                mCommonDialog.dismiss();
                            })
                            .setTwoClickListener(Dialog::dismiss)
                            .show();
                }
            });
        } else {
            hisRxLocalAdapter.notifyDataSetChanged();
        }
    }

    private List<RxEntity> getLocalRxById(int id) {
        return EmtDataBase
                .getInstance(LocalPrescriptionActivity.this)
                .getRxDao()
                .getRxListById(id);
    }

    private List<RxEntity> getLocalRx() {
        return EmtDataBase
                .getInstance(LocalPrescriptionActivity.this)
                .getRxDao()
                .getRxList();
    }

    @Override
    public void notifyByThemeChanged() {

    }
}