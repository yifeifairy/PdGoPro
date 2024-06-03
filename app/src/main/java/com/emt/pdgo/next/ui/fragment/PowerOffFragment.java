package com.emt.pdgo.next.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PowerOffFragment extends BaseFragment {


    @BindView(R.id.imageView)
    ImageView imageView;

    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_power_off, container, false);
        unbinder = ButterKnife.bind(this,view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Button power = view.findViewById(R.id.power);
        speak("右手食指放在卡匣弹片位置——上抬 取出卡匣 取下所有液袋和卡匣管组，随家庭垃圾丢弃 将引流袋内的引流液倒入马桶");
        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group1"));
        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true,"group2"));
        sendToMainBoard(CommandDataHelper.getInstance().isValveOpen(true, "group3"));

        power.setOnClickListener(view1 -> {
            speak("正在关机中,请拔下电源插头");
            showLoading("正在关机中,请拔下电源插头...");
            countDownTimer.start();
            sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BATTERY_OFF));
        });
        Glide.with(this).asGif().load(R.raw.remove_case).into(imageView);
    }

    private final CountDownTimer countDownTimer = new CountDownTimer(5 * 1000,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            dismissLoading();
            showTipsCommonDialog("内部电池已关闭,请拔下电源插头");
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void notifyByThemeChanged() {

    }

}