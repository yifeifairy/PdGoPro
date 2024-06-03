package com.emt.pdgo.next.util;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ClickUtil  {

    private final View view;
    private final long time;
    private boolean isLongPressed;
    private final PressAndHold pressAndHold;
    private ResultListener resultListener;

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }
    public ClickUtil(View view, long time) {
        this.view = view;
        this.time = time;
        pressAndHold = new PressAndHold();
        init();
    }
    public interface ResultListener {
        void onResult(boolean press);
    }
    private void init() {
        view.setOnTouchListener((view1, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                Log.d("onTouch", "action down");
//                ivLogo.postDelayed(mCheckForLongPress1, 1000);
                    Log.e("onTouch","ACTION_DOWN");
                    if (!isLongPressed) {
                        isLongPressed = true;
                        view.postDelayed(pressAndHold, time);
                    }
                    break;
//            case MotionEvent.ACTION_MOVE:
////                long tTime = System.currentTimeMillis() - touchStartTime;
////                if (tTime == 5000) {
////                    alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
////                }
//                if (isLongPressed) {
//                    if (tTime == 5000) {
//                        alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
//                    }
//                }
//                Log.e("onTouch","ACTION_MOVE");
//                break;
                case MotionEvent.ACTION_UP:
//                long touchTime = System.currentTimeMillis() - touchStartTime;
//                if (touchTime > 5000) {
//                    alertNumberBoardDialog("", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD);
//                }
                    isLongPressed = false;
                    view.removeCallbacks(pressAndHold);
                    Log.e("onTouch", "ACTION_UP");
                    break;
            }
            return true;
        });
    }

    private class PressAndHold implements Runnable {

        @Override
        public void run() {
            //5s之后，查看isLongPressed的变量值：
            if (isLongPressed) {//没有做up事件
                resultListener.onResult(true);
                Log.e("长按", time / 1000+ "s的事件触发");
            } else {
                Log.e("长按", time / 1000+"s的事件未触发");
                resultListener.onResult(false);
                view.removeCallbacks(pressAndHold);
            }
        }
    }

}
