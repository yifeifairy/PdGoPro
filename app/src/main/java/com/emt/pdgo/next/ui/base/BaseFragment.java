package com.emt.pdgo.next.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.interfaces.ThemeChangeObserver;
import com.emt.pdgo.next.rxlibrary.rxjava.RxBaseFragment;
import com.emt.pdgo.next.ui.dialog.CommonDialog;
import com.emt.pdgo.next.util.GlobalDialogManager;
import com.emt.pdgo.next.util.TtsHelper;
import com.emt.pdgo.next.util.task.ConsumptionTask;
import com.pdp.rmmit.pdp.R;


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

    public void speak(String content) {
//        if (PdproHelper.getInstance().getTtsSoundOpen()) {
////            BaiduTtsHelper.getInstance(BaseActivity.this).speak(content);
//            TtsHelper.getInstance(activity).speak(content);
//        }
        TtsHelper.getInstance(activity).speak(content);
    }

    public void toastMessage(String messages) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
        View view = inflater.inflate(R.layout.toast_item, null); //加載layout下的布局
//        ImageView iv = view.findViewById(R.id.tvImageToast);
//        iv.setImageResource(R.mipmap.atm);//显示的图片
//        TextView title = view.findViewById(R.id.tvTitleToast);
//        title.setText(titles); //toast的标题
        TextView text = view.findViewById(R.id.tvTextToast);
        text.setText(messages); //toast内容
        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        toast.setDuration(Toast.LENGTH_SHORT);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }

    @Override
    public void loadingCurrentTheme() {

    }

    public void sendToMainBoard(String mCommand) {
//        Log.e("sendToMainBoard", "isOpenMainSerial:" + MyApplication.isOpenMainSerial);
        if (!MyApplication.isOpenMainSerial) {
            Log.d("发送数据到主板", "MyApplication.isOpenMainSerial:" + false);
//            RxBus.get().send(RxBusCodeConfig.EVENT_ERROR_MAIN_BOARD, MainBoardService.OpenSerialError);
        } else {
            if (mCommand == null || "".equals(mCommand)) {
                return;
            }
            ConsumptionTask task = new ConsumptionTask();
            task.taskNo = "Task" + (MyApplication.index++); // 确保唯一性
            task.planNo = mCommand; // 将数据分组， 如果没有该需求的同学，可以不进行设置
            MyApplication.lineUpTaskHelp.addTask(task); // 添加到排队列表中去， 如果还有任务没完成，
//            if (!mCommand.contains("report")) {
//                FaultCodeEntity entity = new FaultCodeEntity();
//                entity.time = EmtTimeUil.getTime();
//                entity.code = "发送:"+mCommand;
//                EmtDataBase.getInstance(MyApplication.getInstance()).getFaultCodeDao()
//                        .insertFaultCode(entity);
//            }
//            RxBus.get().send(RxBusCodeConfig.EVENT_SEND_COMMAND, mCommand);
        }
    }

    private CommonDialog commonDialog;
    public void showTipsCommonDialog(String tips) {


        if (commonDialog == null) {
            commonDialog = new CommonDialog(getActivity());
        }
        commonDialog.setContent(tips)
                .setBtnFirst("确定")
                .setFirstClickListener(Dialog::dismiss)
                .setTwoClickListener(Dialog::dismiss)
                .show();
    }

    public void showLoading(String hintMsg) {
        if (!MyApplication.DEBUG) {
            GlobalDialogManager.getInstance().show(activity.getFragmentManager(), hintMsg);
        }
    }

    public void dismissLoading() {
        if (!MyApplication.DEBUG) {
            GlobalDialogManager.getInstance().dismiss();
        }

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
