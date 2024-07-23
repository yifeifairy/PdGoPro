package com.emt.pdgo.next.ui.activity.param;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.CommandSendConfig;
import com.emt.pdgo.next.data.entity.CommandItem;
import com.emt.pdgo.next.ui.adapter.CommandAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.MarioResourceHelper;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoiceDebugActivity extends BaseActivity {

    @BindView(R.id.custom_id_app_background)
    public LinearLayout mAppBackground;

    @BindView(R.id.rv_set)
    RecyclerView rvSet;

    private List<CommandItem> mList;
    private CommandAdapter mAdapter;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_voice_debug);
        ButterKnife.bind(this);
    }
    @BindView(R.id.btnBack)
    Button btnBack;
    @Override
    public void registerEvents() {
       btnBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void initViewData() {
        mList = new ArrayList<>();

        mList.add(new CommandItem("打开语音", "tsOpen"));
        mList.add(new CommandItem("关闭语音", "tsClose"));
//        mList.add(new CommandItem(msg,"ts"));
        mList.add(new CommandItem("语音测试", "tsTest"));
        mList.add(new CommandItem("打开蜂鸣器", "buzzerOpen"));
        mList.add(new CommandItem("关闭蜂鸣器", "buzzerClose"));


        mList.add(new CommandItem("高优先级", "highAlarm"));
        mList.add(new CommandItem("中优先级", "midAlarm"));
        mList.add(new CommandItem("低优先级", "lowerAlarm"));

        mAdapter = new CommandAdapter(this, R.layout.item_setting, mList);
        rvSet.setLayoutManager(new GridLayoutManager(this, 3));
        rvSet.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if ("tsOpen".equals(mList.get(position).mCommand)) {
                PdproHelper.getInstance().updateTtsSoundOpen(true);
                Log.e("tts","状态："+PdproHelper.getInstance().getTtsSoundOpen());
            } else if ("tsClose".equals(mList.get(position).mCommand)) {
                PdproHelper.getInstance().updateTtsSoundOpen(false);
                Log.e("tts","状态："+PdproHelper.getInstance().getTtsSoundOpen());
            } else if ("buzzerOpen".equals(mList.get(position).mCommand)) {
                sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_ON));
            } else if ("buzzerClose".equals(mList.get(position).mCommand)) {
                sendToMainBoard(CommandDataHelper.getInstance().customCmd(CommandSendConfig.METHOD_BEEP_OFF));
            } else if ("tsTest".equals(mList.get(position).mCommand)) {
                speak("语音测试");
            } else if ("ts".equals(mList.get(position).mCommand)) {
                PdproHelper.getInstance().updateTtsSoundOpen(!PdproHelper.getInstance().getTtsSoundOpen());
            } else if ("highAlarm".equals(mList.get(position).mCommand)) {
                try {
//                    if (player == null) {
//                        player = MediaPlayer.create(this,R.raw.high_alarm);
////                    }
//                    player.start();
                    initBeepSoundSus(R.raw.high_alarm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("midAlarm".equals(mList.get(position).mCommand)) {
                try {
//                    if (player == null) {
//                        player = MediaPlayer.create(this,R.raw.medium_alarm);
////                    }
//                    player.start();
                    initBeepSoundSus(R.raw.medium_alarm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("lowerAlarm".equals(mList.get(position).mCommand)) {
                try {
//                    if (player == null) {
//                        player = MediaPlayer.create(this,R.raw.low_alarm);
////                    }
//                    player.setLooping(true);
//                    player.start();
                    initBeepSoundSus(R.raw.low_alarm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private MediaPlayer player;
    @Override
    public void notifyByThemeChanged() {
        MarioResourceHelper helper = MarioResourceHelper.getInstance(getContext());
        helper.setBackgroundResourceByAttr(mAppBackground, R.attr.custom_attr_app_bg);
        if (mAdapter != null) mAdapter.notifyDataSetChanged(); //

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}