package com.emt.pdgo.next.ui.mode.activity.cfpd;

import com.emt.pdgo.next.ui.base.BaseActivity;
import com.pdp.rmmit.pdp.R;

import butterknife.ButterKnife;

public class CfpdActivity extends BaseActivity {


    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_cfpd);
        ButterKnife.bind(this);
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void initViewData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void notifyByThemeChanged() {

    }
}