package com.emt.pdgo.next.ui.local;

import android.annotation.SuppressLint;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.PdEntity;
import com.emt.pdgo.next.ui.adapter.LocalPdAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.pdp.rmmit.pdp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalPdActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.btnBack)
    Button btnBack;

    private int page = EmtDataBase.getInstance(LocalPdActivity.this).getPdDao().getPdList().size();

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_local_pd);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {
        btnBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void initViewData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        localPdAdapter = new LocalPdAdapter(getPdInfo(page),false);
        recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(localPdAdapter);
        localPdAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.ivNextPage) {
                if (page > 1 && page <= EmtDataBase.getInstance(LocalPdActivity.this).getPdDao().getPdList().size()) {
                    page--;
                    init(page);
                }
            } else if (view.getId() == R.id.ivPrePage) {
                if (page < EmtDataBase.getInstance(LocalPdActivity.this).getPdDao().getPdList().size()) {
                    page++;
                    init(page);
                }
            } else if (view.getId() == R.id.btnUpload) {
                if (checkConnectNetwork(LocalPdActivity.this)) {
                    getToken();
                } else {
                    toastMessage("请连接网络");
                }
            }
        });
    }

    private LocalPdAdapter localPdAdapter;
    private List<PdEntity> getPdInfo(int page) {
        return EmtDataBase.getInstance(LocalPdActivity.this).getPdDao()
                .getPdListById(page);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void init(int page) {
//        pdListBeans.addAll(rowsDTOS);
        localPdAdapter.setNewData(getPdInfo(page));
        localPdAdapter.notifyDataSetChanged();
    }


    @Override
    public void notifyByThemeChanged() {

    }
}