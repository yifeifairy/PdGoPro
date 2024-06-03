package com.emt.pdgo.next.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.net.RetrofitUtil;
import com.emt.pdgo.next.net.bean.MyResponse;
import com.emt.pdgo.next.net.bean.PostCommonBean;
import com.emt.pdgo.next.net.bean.TreatAssessRequestBean;
import com.emt.pdgo.next.ui.activity.TreatmentFeedbackActivity;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.google.gson.Gson;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssessFragment extends BaseFragment {

    @BindView(R.id.tv_abdominal_pain_item1)
    TextView tvAbdominalPainItem1;
    @BindView(R.id.tv_abdominal_pain_item2)
    TextView tvAbdominalPainItem2;
    @BindView(R.id.tv_abdominal_pain_item3)
    TextView tvAbdominalPainItem3;
    @BindView(R.id.tv_abdominal_pain_item4)
    TextView tvAbdominalPainItem4;

    @BindView(R.id.tv_drainage_color_item1)
    TextView tvDrainageColorItem1;
    @BindView(R.id.tv_drainage_color_item2)
    TextView tvDrainageColorItem2;
    @BindView(R.id.tv_drainage_color_item3)
    TextView tvDrainageColorItem3;
    @BindView(R.id.tv_drainage_color_item4)
    TextView tvDrainageColorItem4;

    @BindView(R.id.tv_tunnel_opening_pain_item1)
    TextView tvTunnelOpeningPainItem1;
    @BindView(R.id.tv_tunnel_opening_pain_item2)
    TextView tvTunnelOpeningPainItem2;
    @BindView(R.id.tv_tunnel_opening_pain_item3)
    TextView tvTunnelOpeningPainItem3;
    @BindView(R.id.tv_tunnel_opening_pain_item4)
    TextView tvTunnelOpeningPainItem4;

    @BindView(R.id.btnNext)
    Button btnNext;

    private String dailyLifeType ="";
    private String drainageClarity="";
    private String unusualType="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assess, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        btnNext.setOnClickListener(view -> {
            save();
        });
    }

    @OnClick({R.id.tv_abdominal_pain_item1, R.id.tv_abdominal_pain_item2, R.id.tv_abdominal_pain_item3,
            R.id.tv_abdominal_pain_item4,R.id.tv_drainage_color_item1, R.id.tv_drainage_color_item2, R.id.tv_drainage_color_item3, R.id.tv_drainage_color_item4,
            R.id.tv_tunnel_opening_pain_item1, R.id.tv_tunnel_opening_pain_item2, R.id.tv_tunnel_opening_pain_item3, R.id.tv_tunnel_opening_pain_item4,})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.tv_abdominal_pain_item1://透析过程中是否感到腹痛：无
                tvAbdominalPainItem1.setSelected(!tvAbdominalPainItem1.isSelected());
//                tvAbdominalPainItem2.setSelected(false);
//                tvAbdominalPainItem3.setSelected(false);
                break;
            case R.id.tv_abdominal_pain_item2://透析过程中是否感到腹痛：隐隐作痛
//                tvAbdominalPainItem1.setSelected(false);
                tvAbdominalPainItem2.setSelected(!tvAbdominalPainItem2.isSelected());
//                tvAbdominalPainItem3.setSelected(false);
                break;
            case R.id.tv_abdominal_pain_item3://透析过程中是否感到腹痛：持续阵痛
//                tvAbdominalPainItem1.setSelected(false);
//                tvAbdominalPainItem2.setSelected(false);
                tvAbdominalPainItem3.setSelected(!tvAbdominalPainItem3.isSelected());
                break;
            case R.id.tv_abdominal_pain_item4://透析过程中是否感到腹痛：持续阵痛
//                tvAbdominalPainItem1.setSelected(false);
//                tvAbdominalPainItem2.setSelected(false);
                tvAbdominalPainItem4.setSelected(!tvAbdominalPainItem4.isSelected());
                break;
            case R.id.tv_drainage_color_item1://引流液颜色（多选）：清澈
                tvDrainageColorItem1.setSelected(!tvDrainageColorItem1.isSelected());
                break;
            case R.id.tv_drainage_color_item2://引流液颜色（多选）：部分浑浊
                tvDrainageColorItem2.setSelected(!tvDrainageColorItem2.isSelected());
                break;
            case R.id.tv_drainage_color_item3://引流液颜色（多选）：浑浊
                tvDrainageColorItem3.setSelected(!tvDrainageColorItem3.isSelected());
                break;
            case R.id.tv_drainage_color_item4://引流液颜色（多选）：浑浊
                tvDrainageColorItem4.setSelected(!tvDrainageColorItem4.isSelected());
                break;
            case R.id.tv_tunnel_opening_pain_item1://隧道口是否疼痛（多选）：无
                tvTunnelOpeningPainItem1.setSelected(!tvTunnelOpeningPainItem1.isSelected());
                break;
            case R.id.tv_tunnel_opening_pain_item2://隧道口是否疼痛（多选）：轻微疼痛
                tvTunnelOpeningPainItem2.setSelected(!tvTunnelOpeningPainItem2.isSelected());
                break;
            case R.id.tv_tunnel_opening_pain_item3://隧道口是否疼痛（多选）：轻微红肿
                tvTunnelOpeningPainItem3.setSelected(!tvTunnelOpeningPainItem3.isSelected());
                break;
            case R.id.tv_tunnel_opening_pain_item4://隧道口是否疼痛（多选）：轻微红肿
                tvTunnelOpeningPainItem4.setSelected(!tvTunnelOpeningPainItem4.isSelected());
                break;
        }
    }

    private void save() {
        if (tvAbdominalPainItem1.isSelected()) {
            dailyLifeType += ",1";
        }
        if (tvAbdominalPainItem2.isSelected()) {
            dailyLifeType += ",2";
        }
        if (tvAbdominalPainItem3.isSelected()) {
            dailyLifeType += ",3";
        }
        if (tvAbdominalPainItem4.isSelected()) {
            dailyLifeType += ",4";
        }
        if (tvDrainageColorItem1.isSelected()) {
            drainageClarity += ",1";
        }
        if (tvDrainageColorItem2.isSelected()) {
            drainageClarity += ",2";
        }
        if (tvDrainageColorItem3.isSelected()) {
            drainageClarity += ",3";
        }
        if (tvDrainageColorItem4.isSelected()) {
            drainageClarity += ",4";
        }
        if (tvTunnelOpeningPainItem1.isSelected()) {
            unusualType += ",1";
        }
        if (tvTunnelOpeningPainItem2.isSelected()) {
            unusualType += ",2";
        }
        if (tvTunnelOpeningPainItem3.isSelected()) {
            unusualType += ",3";
        }
        if (tvTunnelOpeningPainItem4.isSelected()) {
            unusualType += ",4";
        }
        if (!dailyLifeType.equals("") && !drainageClarity.equals("") && !unusualType.equals("")) {
            postAssess();
            TreatmentFeedbackActivity.viewpager.setCurrentItem(1);
        } else {
            if (dailyLifeType.equals("")) {
                toastMessage("请选择日常生活的状况");
            } else if (drainageClarity.equals("")) {
                toastMessage("请选择引流液颜色");
            } else {
                toastMessage("请选择隧道口是否疼痛");
            }
        }

    }

    private Gson gson = new Gson();
    private void postAssess() {
        TreatAssessRequestBean bean = new TreatAssessRequestBean();
        bean.setMachineSn(PdproHelper.getInstance().getMachineSN());
        bean.setStartTime(MyApplication.pdData.getStartTime());
        bean.setEndTime(MyApplication.pdData.getEndTime());
        bean.setDailyLifeType(dailyLifeType.substring(1));
        bean.setUnusualType(unusualType.substring(1));
        bean.setDrainageClarity(drainageClarity.substring(1));
        String content = gson.toJson(bean);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                content);
        RetrofitUtil.getService().postAssess(body).enqueue(new Callback<MyResponse<PostCommonBean>>() {
            @Override
            public void onResponse(Call<MyResponse<PostCommonBean>> call, Response<MyResponse<PostCommonBean>> response) {
                if (response.body() != null) {
                    if (response.body().data != null) {
                        if (response.body().data.code.equals("10000")) {

                        } else {
                            Log.e("评估", gson.toJson(response.body()));

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<PostCommonBean>> call, Throwable t) {

            }
        });
    }
    private Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void notifyByThemeChanged() {

    }
}