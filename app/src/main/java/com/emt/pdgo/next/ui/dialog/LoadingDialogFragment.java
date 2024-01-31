package com.emt.pdgo.next.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.emt.pdgo.next.ui.view.CircularProgressView;
import com.pdp.rmmit.pdp.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class LoadingDialogFragment extends DialogFragment {

    @BindView(R.id.circularProgressView)
    CircularProgressView circularProgressView;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mCompositeDisposable = new CompositeDisposable();
        tvTime.setText(String.valueOf(maxCountdown));
        startLoopSubscription();
    }

    private CompositeDisposable mCompositeDisposable;
    private Disposable disposable;
    private final int maxCountdown = 5;//倒计时
    private int currCountdown = 0;//倒计时

    /**
     * 开始轮询计时
     */
    private void startLoopSubscription() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(maxCountdown - currCountdown)
                .subscribe(aLong -> {
                    currCountdown++;
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circularProgressView.setProgress(currCountdown * 100 / maxCountdown);
                            tvTime.setText(String.valueOf(maxCountdown - currCountdown));
                        }
                    });
                }, throwable -> {

                }, () -> {

                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        currCountdown = 0;
        mCompositeDisposable.clear();
    }

    /**
     * 停止轮询获取计时
     */
    private void stopLoopSubscription() {

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
            mCompositeDisposable.clear();
        }

    }

    Unbinder unbinder;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.clear();
        unbinder.unbind();
    }

}