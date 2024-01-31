package com.emt.pdgo.next.ui.activity.remote;

import com.emt.pdgo.next.ui.base.BaseActivity;
import com.pdp.rmmit.pdp.R;

import butterknife.ButterKnife;

public class RemoteIpdActivity extends BaseActivity {


    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_remote_ipd);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void initViewData() {

    }

    @Override
    public void notifyByThemeChanged() {

    }
}