package com.emt.pdgo.next.ui.local;

import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.RxEntity;
import com.emt.pdgo.next.ui.adapter.local.HisRxLocalAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalRxActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.title)
    TextView title;

    private HisRxLocalAdapter hisRxLocalAdapter;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_local_rx);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {
        btnBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void initViewData() {
        if (hisRxLocalAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            hisRxLocalAdapter = new HisRxLocalAdapter(getLocalRx());
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(hisRxLocalAdapter);
        } else {
            hisRxLocalAdapter.notifyDataSetChanged();
        }
    }



    private List<RxEntity> getLocalRx() {
        return EmtDataBase
                .getInstance(LocalRxActivity.this)
                .getRxDao()
                .getRxList();
    }

    @Override
    public void notifyByThemeChanged() {

    }
}