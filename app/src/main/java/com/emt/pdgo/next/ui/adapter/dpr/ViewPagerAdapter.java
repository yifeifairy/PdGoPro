package com.emt.pdgo.next.ui.adapter.dpr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList;
    private final List<String> titles;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragmentList,
                            List<String> titles) {
        super(fm, behavior);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
