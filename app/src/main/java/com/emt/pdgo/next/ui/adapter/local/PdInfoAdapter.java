package com.emt.pdgo.next.ui.adapter.local;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.database.entity.PdInfoEntity;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class PdInfoAdapter extends RecyclerView.Adapter<PdInfoAdapter.ViewHolder> {

    private List<PdInfoEntity> entities;
    private Context context;

    public PdInfoAdapter(Context context, List<PdInfoEntity> entities) {
        this.context = context;
        this.entities = entities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.his_pd_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvCycle.setText(String.valueOf(entities.get(position).cycle));
        holder.tvPre.setText(String.valueOf(entities.get(position).preVol));
        holder.preTime.setText(String.valueOf(entities.get(position).preTime));
        holder.auFv.setText(String.valueOf(entities.get(position).auFvVol));
        holder.abdAfter.setText(String.valueOf(entities.get(position).abdAfterVol));
        holder.abdTime.setText(String.valueOf(entities.get(position).abdTime));
        holder.drainTv.setText(String.valueOf(entities.get(position).drainTvVol));
        holder.drainage.setText(String.valueOf(entities.get(position).drainage));
        holder.drainTime.setText(String.valueOf(entities.get(position).drainTime));
        holder.remain.setText(String.valueOf(entities.get(position).remain));
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvCycle, tvPre, preTime, auFv, abdAfter,abdTime,drainTv,drainage,drainTime,remain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCycle = itemView.findViewById(R.id.tvCycle);
            tvPre = itemView.findViewById(R.id.tvPre);
            preTime = itemView.findViewById(R.id.preTime);
            auFv = itemView.findViewById(R.id.auFv);
            abdAfter = itemView.findViewById(R.id.abdAfter);
            abdTime = itemView.findViewById(R.id.abdTime);
            drainTv = itemView.findViewById(R.id.drainTv);
            drainage = itemView.findViewById(R.id.drainage);
            drainTime = itemView.findViewById(R.id.drainTime);
            remain = itemView.findViewById(R.id.remain);

        }
    }

    public void insert(List<PdInfoEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        notifyItemInserted(getItemCount());
    }

}
