package com.emt.pdgo.next.ui.adapter;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.database.entity.PdEntity;
import com.emt.pdgo.next.ui.adapter.local.HisPdLocalAdapter;
import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class LocalPdAdapter extends BaseQuickAdapter<PdEntity, BaseViewHolder> {

    private boolean isShow;

    public LocalPdAdapter(@Nullable List<PdEntity> data, boolean isShow) {
        super(R.layout.recyclerview_local_pd, data);
        this.isShow = isShow;
    }

    @Override
    protected void convert(BaseViewHolder helper, PdEntity item) {
        helper.setText(R.id.tvStartTime, item.startTime)
                .setText(R.id.tvEndTime, item.endTime)
                .setText(R.id.tv_total_drain_volume, String.valueOf(item.totalDrainVol))
                .setText(R.id.tv_total_perfusion_volume, String.valueOf(item.totalPerVol))
                .setText(R.id.tv_curr_ultrafiltration_volume, String.valueOf(item.totalUltVol))
                .addOnClickListener(R.id.ivPrePage).addOnClickListener(R.id.ivNextPage)
                .addOnClickListener(R.id.btnUpload);
        helper.getView(R.id.btnUpload).setVisibility(isShow?View.VISIBLE: View.GONE);
        RecyclerView rv = helper.getView(R.id.recyclerview);
        assert rv != null;
//        rv.setLayoutManager(new GridLayoutManager(mContext, 2));
        HisPdLocalAdapter adapter = new HisPdLocalAdapter(item.pdInfoEntities);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
//            localAdapter = new HisPdLocalAdapter(getPdInfo(page));
        rv.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
        //添加Android自带的分割线
        rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        rv.setAdapter(adapter);
    }
}
