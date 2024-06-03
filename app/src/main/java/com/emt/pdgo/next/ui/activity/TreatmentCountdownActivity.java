package com.emt.pdgo.next.ui.activity;

import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.emt.pdgo.next.constant.EmtConstant;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.ActivityManager;
import com.emt.pdgo.next.util.ClickUtil;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.ui.activity
 * @ClassName: TreatmentCountdownActivity
 * @Description: 开始治疗倒数计时
 * @Author: chenjh
 * @CreateDate: 2020/1/3 11:53 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/3 11:53 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class TreatmentCountdownActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnGoToTreatment)
    Button btnGoToTreatment;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_treatment_countdown);
        ButterKnife.bind(this);
    }
    @BindView(R.id.imageview)
    ImageView imageView;
    @Override
    public void registerEvents() {
        RxBus.get().register(this);
        btnBack.setOnClickListener(view -> {
            if (jumpMsg != null) {
                doGoCloseTOActivity(PreRinseActivity.class,jumpMsg);
            } else {
                ActivityManager.getActivityManager().removeActivity(this);
            }
        });
        Glide.with(this).asGif().skipMemoryCache(true)                      //禁止Glide内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE).load(R.raw.in_peo).into(imageView);
    }

//    private LoadingDialogFragment loadingDialogFragment;

    private String jumpMsg;
//    private LoadingPress loadingPress;
//    private volatile boolean isLoading = false;

//    private class LoadingPress implements Runnable {
//
//        @Override
//        public void run() {
//            if (isLoading) {//没有做up事件
//                loadingDialogFragment.dismiss();
//                doGoCloseTOActivity(TreatmentFragmentActivity.class,"");
//            } else {
//                tvTitle.removeCallbacks(loadingPress);
//            }
//            Log.e("LoadingPress",""+isLoading);
//        }
//    }

    @Override
    public void initViewData() {
        speak("请确保管路已连接人体后开始进入治疗");

        jumpMsg = getIntent().getStringExtra(EmtConstant.JUMP_WITH_PARAM);
        ClickUtil clickUtil = new ClickUtil(btnGoToTreatment, 3000);
        clickUtil.setResultListener(new ClickUtil.ResultListener() {
            @Override
            public void onResult(boolean press) {
                if (press) {
                    doGoCloseTOActivity(TreatmentFragmentActivity.class,"");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
    }

    @Override
    public void notifyByThemeChanged() {

    }
}
