package com.emt.pdgo.next.ui.fragment.apd.prescription;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.emt.pdgo.next.ui.adapter.dpr.ViewPagerAdapter;
import com.emt.pdgo.next.ui.base.BaseFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.AapdFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.ExpertPrescriptionFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.InfantsPrescriptionFragment;
import com.emt.pdgo.next.ui.fragment.apd.prescription.special.TpdPrescriptionFragment;
import com.emt.pdgo.next.ui.view.NoScrollViewPager;
import com.google.android.material.tabs.TabLayout;
import com.pdp.rmmit.pdp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpecialPrescriptionFragment extends BaseFragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_special_prescription, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViewPage();
        return view;
    }

    private void initViewPage() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TpdPrescriptionFragment());
        fragmentList.add(new AapdFragment());
        fragmentList.add(new InfantsPrescriptionFragment());
        fragmentList.add(new ExpertPrescriptionFragment());
        List<String> titles = new ArrayList<>();
        titles.add("TPD模式");
        titles.add("aAPD模式");
        titles.add("儿童模式");
        titles.add("专家模式");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragmentList, titles);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setNoScroll(false);
        tabLayout.setupWithViewPager(viewpager);
    }

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void notifyByThemeChanged() {

    }
}