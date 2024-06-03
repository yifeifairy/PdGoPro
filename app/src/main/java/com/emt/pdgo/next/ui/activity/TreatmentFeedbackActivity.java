package com.emt.pdgo.next.ui.activity;


import android.content.Intent;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.emt.pdgo.next.ui.adapter.dpr.ViewPagerAdapter;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.fragment.AssessFragment;
import com.emt.pdgo.next.ui.fragment.GuideFragment;
import com.emt.pdgo.next.ui.fragment.PowerOffFragment;
import com.emt.pdgo.next.ui.view.NoScrollViewPager;
import com.google.android.material.tabs.TabLayout;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreatmentFeedbackActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
//    @BindView(R.id.viewpager)
    public static NoScrollViewPager viewpager;

    @BindView(R.id.btnBack)
    Button btnBack;

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_treatment_feedback);
        ButterKnife.bind(this);

    }

    @Override
    public void registerEvents() {
        btnBack.setOnClickListener(view -> onBackPressed());
    }

    public void initViewData() {
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        initViewPage();
        if (msg != null) {
            viewpager.setCurrentItem(Integer.parseInt(msg),false);
        }
    }

    private void initViewPage() {
        viewpager = findViewById(R.id.viewpager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new AssessFragment());
        fragmentList.add(new GuideFragment());
        fragmentList.add(new PowerOffFragment());
        List<String> titles = new ArrayList<>();
        titles.add("治疗评估");
        titles.add("下机引导");
        titles.add("关机");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragmentList, titles);
        viewpager.setAdapter(adapter);
        viewpager.setNoScroll(false);
        viewpager.setOffscreenPageLimit(fragmentList.size()-1);
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public void notifyByThemeChanged() {

    }

}
