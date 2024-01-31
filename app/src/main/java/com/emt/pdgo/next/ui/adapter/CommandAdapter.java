package com.emt.pdgo.next.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.pdp.rmmit.pdp.R;
import com.emt.pdgo.next.data.entity.CommandItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @ProjectName:
 * @Package:        com.emt.pdgo.next.ui.adapter
 * @ClassName:      CommandAdapter
 * @Description:    上下机指令数据适配器
 * @Author:         chenjh
 * @CreateDate:     2019/12/3 10:38 AM
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/12/3 10:38 AM
 * @UpdateRemark:   更新内容
 * @Version:        1.0
 */
public class CommandAdapter extends BaseQuickAdapter<CommandItem, BaseViewHolder> {

    private Context mContext;

    public CommandAdapter(Context context,int layoutResId, List data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommandItem item) {
        helper.setText(R.id.btn_item, item.mName).addOnClickListener(R.id.btn_item);
        MarioResourceHelper mHelper = MarioResourceHelper.getInstance(mContext);
        mHelper.setBackgroundResourceByAttr(helper.getConvertView().findViewById(R.id.btn_item), R.attr.custom_attr_item_btn_bg);
        mHelper.setTextColorByAttr(helper.getConvertView().findViewById(R.id.btn_item), R.attr.custom_attr_item_btn_text_color);
    }


}