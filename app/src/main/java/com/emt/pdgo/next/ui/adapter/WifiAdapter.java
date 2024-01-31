package com.emt.pdgo.next.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.data.bean.WifiListBean;
import com.emt.pdgo.next.ui.dialog.SecurityEditText;
import com.pdp.rmmit.pdp.R;

import java.util.List;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyViewHolder> {
    private List<WifiListBean> wifiListBeanList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public WifiAdapter.OnItemClickListener mOnItemClickListerer;

    public void setmOnItemClickListerer(WifiAdapter.OnItemClickListener listerer) {
        this.mOnItemClickListerer = listerer;
    }

    public WifiAdapter(List<WifiListBean> wifiListBeanList) {
        this.wifiListBeanList = wifiListBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_recyclerview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText("wifi名：" + wifiListBeanList.get(position).getName());
        holder.tv_encrypt.setText("加密方式：" + wifiListBeanList.get(position).getEncrypt());
        holder.btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListerer.onItemClick(v, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return wifiListBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_encrypt;
        Button btn_link;
        SecurityEditText editText;

        public MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_encrypt = view.findViewById(R.id.tv_encrypt);
            btn_link = view.findViewById(R.id.btn_link);
//            editText = view.findViewById(R.id.login_input_password);
        }
    }
}
