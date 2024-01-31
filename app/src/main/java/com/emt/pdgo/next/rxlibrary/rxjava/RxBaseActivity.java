package com.emt.pdgo.next.rxlibrary.rxjava;


import com.emt.pdgo.next.rxlibrary.utils.RxLoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by bhm on 2018/5/11.
 */

public class RxBaseActivity extends RxAppCompatActivity {

    protected RxManager rxManager = new RxManager();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxManager.unSubscribe();
        RxLoadingDialog.getDefaultDialog().cancelLoading(this);
    }
}
