package com.emt.pdgo.next.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GuideFragment extends BaseFragment {

    @BindView(R.id.imageview)
    ImageView imageView;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        unbinder = ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        speak("请带上口罩 干洗手:喷 75%酒精或消毒液于掌心，用七步洗手法（内外夹弓 大立腕），反复搓洗数次。 请准备新的肝素帽——检查有效日期、外观没有破损 撕开 肝素帽包装 仍保持肝素帽在包装内 双手拇指及食指紧握腹透管和灌注管接口近端 旋开腹透管接头 检查新的肝素帽有无碘酒海绵 并旋紧于腹透管接头 收回腹透管");
//        Glide.with(this).asGif().load(R.drawable.conn_supply).into(imageView);
        Glide.with(this).asGif().load(R.raw.seperation).into(imageView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void notifyByThemeChanged() {

    }
}