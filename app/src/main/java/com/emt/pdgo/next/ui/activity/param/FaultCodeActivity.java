package com.emt.pdgo.next.ui.activity.param;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.database.entity.FaultCodeEntity;
import com.emt.pdgo.next.ui.adapter.local.FaultCodeAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.pdp.rmmit.pdp.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class FaultCodeActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.btnBack)
    Button btnBack;

    private FaultCodeAdapter adapter;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_fault_code);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {
        btnBack.setOnClickListener(view -> onBackPressed());
        btnSave.setVisibility(View.VISIBLE);
        btnSave.setText("清空");
        btnSave.setOnClickListener(v -> EmtDataBase
                .getInstance(FaultCodeActivity.this)
                .getFaultCodeDao()
                .delete());
    }

    @Override
    public void initViewData() {
        disposable = new CompositeDisposable();
        init();
    }

    private List<FaultCodeEntity> getLocalFaultCode() {
        return EmtDataBase
                .getInstance(FaultCodeActivity.this)
                .getFaultCodeDao()
                .getCodeList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView() {
        if (adapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new FaultCodeAdapter(getLocalFaultCode());
            recyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setNewData(getLocalFaultCode());
            adapter.notifyDataSetChanged();
        }
    }
    private CompositeDisposable disposable;
    private void init() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                runOnUiThread(() -> {
                    initRecyclerView();
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
        disposable.add(disposableObserver);
    }

    @Override
    public void notifyByThemeChanged() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}