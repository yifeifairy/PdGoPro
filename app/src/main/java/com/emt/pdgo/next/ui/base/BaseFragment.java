package com.emt.pdgo.next.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.interfaces.ThemeChangeObserver;
import com.emt.pdgo.next.rxlibrary.rxjava.RxBaseFragment;


/**
 * Created by Mario on 2017-03-06.
 */

public abstract class BaseFragment extends RxBaseFragment implements ThemeChangeObserver {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) ((Activity) getContext()).getApplication()).registerObserver(this);
    }

    @Override
    public void onDestroy() {
        ((MyApplication) ((Activity) getContext()).getApplication()).unregisterObserver(this);
        super.onDestroy();
    }

    @Override
    public void loadingCurrentTheme() {

    }

    public boolean checkConnectNetwork(Context context) {

        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo net = conn.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }



    /**
     * 设置不可编辑且无点击事件
     *
     * @param mEditText
     */
    public void setCanNotEditNoClick2(EditText mEditText) {
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        // 如果之前没设置过点击事件，该处可省略
        mEditText.setOnClickListener(null);
    }

}
