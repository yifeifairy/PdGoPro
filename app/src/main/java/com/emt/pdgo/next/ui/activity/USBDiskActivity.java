package com.emt.pdgo.next.ui.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.pdp.rmmit.pdp.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jahnen.libaums.core.fs.fat32.FAT;

public class USBDiskActivity extends BaseActivity {

    @BindView(R.id.btn_left)
    Button btnFirst;
    @BindView(R.id.btn_right)
    Button btnTwo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private String apkPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_usbdisk);
        ButterKnife.bind(this);

        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        win.setAttributes(lp);

        Intent intent = getIntent();
        apkPath = intent.getStringExtra("filePath");
        tvMsg.setText("是否安装");
        tvTitle.setText("软件升级");
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void initViewData() {

    }

    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_left://第一个按钮
//                installApk(apkPath);
                Log.e("apkPath", apkPath+"");
                MyApplication.getInstance().usbActivityIsRunning = false;
                MyApplication.getInstance().installApk(apkPath);
                finish();
                break;
            case R.id.btn_right://第二个按钮
                MyApplication.getInstance().usbActivityIsRunning = false;
                finish();
                break;
        }

    }

    @Override
    public void notifyByThemeChanged() {

    }

}