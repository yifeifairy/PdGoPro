package com.emt.pdgo.next.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.data.bean.ExpertBean;
import com.pdp.rmmit.pdp.R;

import java.util.List;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.adapter
 * @ClassName: KeyBoardAdapter
 * @Description: 键盘的适配器
 * @Author: chenjh
 * @CreateDate: 2019/12/16 9:02 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/16 9:02 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class KeyBoardAdapter extends RecyclerView.Adapter<KeyBoardAdapter.BoardViewHolder> {
    private List<Integer> valueList;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private ExpertBean expertBean;
    private int supplyModel;

    public KeyBoardAdapter(Context mContext, List<Integer> valueList, ExpertBean expertBean, int supplyModel) {
        this.mContext = mContext;
        this.valueList = valueList;
        this.supplyModel = supplyModel;
        this.expertBean = expertBean;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_board_layout, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BoardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.checkBox.setText("第"+valueList.get(position)+"周期");

        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mOnItemClickListener.onItemClick(expertBean, supplyModel,isChecked, position, valueList);
            });
        }

        if (supplyModel == 0) {
            if (expertBean.osmSupplyCycle.contains(valueList.get(position))) {
                holder.checkBox.setBackgroundColor(Color.GRAY);
                holder.checkBox.setEnabled(false);
                holder.checkBox.setChecked(true);
            }
            if (expertBean.baseSupplyCycle.contains(valueList.get(position))) {
                holder.checkBox.setChecked(true);
            }
        } else if (supplyModel == 1) {
            if (expertBean.baseSupplyCycle.contains(valueList.get(position))) {
                holder.checkBox.setBackgroundColor(Color.GRAY);
                holder.checkBox.setEnabled(false);
                holder.checkBox.setChecked(true);
            }
            if (expertBean.osmSupplyCycle.contains(valueList.get(position))) {
                holder.checkBox.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return valueList.size();
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public BoardViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ExpertBean bean,int supplyModel,boolean isCheck, int position, List<Integer> valueList);
    }
}
