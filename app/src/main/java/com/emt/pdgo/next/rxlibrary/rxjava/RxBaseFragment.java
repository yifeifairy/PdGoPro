package com.emt.pdgo.next.rxlibrary.rxjava;

import android.content.Context;
import android.widget.EditText;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * Created by bhm on 2018/5/11.
 */

public class RxBaseFragment extends RxFragment {

    protected RxAppCompatActivity activity;
    protected RxManager rxManager = new RxManager();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (RxAppCompatActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rxManager.unSubscribe();
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
    }

}
