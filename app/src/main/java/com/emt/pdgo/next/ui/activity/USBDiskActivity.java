package com.emt.pdgo.next.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.pdp.rmmit.pdp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.usbLayout)
    RelativeLayout usbLayout;

    private String apkPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_usbdisk);
        ButterKnife.bind(this);
//        usbLayout.setVisibility(View.GONE);
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
//        NumberBoardDialog dialog = new NumberBoardDialog(USBDiskActivity.this, "", PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD, false);
//        dialog.show();
//        dialog.setOnDialogResultListener((mType, result) -> {
//            if (!TextUtils.isEmpty(result)) {
////                    Logger.d(result);
//                Calendar calendar = Calendar.getInstance();//取得当前时间的年月日 时分秒
//
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH) + 1;
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//                int second = calendar.get(Calendar.SECOND);
//                String mMonth = "";
//
//                if (month >= 10) {
//                    mMonth = "" + month;
//                } else {
//                    mMonth = "0" + month;
//                }
//
//                //123加上月份
//                String tempPwd = "123" + mMonth;
//                Log.e("长按", "tempPwd：" + tempPwd);
//                if (mType.equals(PdGoConstConfig.CHECK_TYPE_ENGINEER_PWD)) {//工程师模式的密码
//                    if (tempPwd.equals(result)) {
//                        usbLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        finish();
//                    }
//                }
//            }
//        });
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

//    public boolean installApk(String filePath) {
////        File apkFile = new File(filePath);
////        Intent intent = new Intent(Intent.ACTION_VIEW);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////            Log.e("installApk", "installApk--------");
////            Uri contentUri = FileProvider.getUriForFile(
////                    this
////                    , "com.pdp.rmmit.pdp.fileprovider"
////                    , apkFile);
////            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
////        } else {
////            Log.e("installApk", "installApk----");
////            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
////        }
////        startActivity(intent);
//        //7.0String [ ] args = { "pm" , "install" , "-i" , "com.example", apkPath} ;
//        //7.0用这个，参考的博客说要加 --user，但是我发现使用了反而不成功。
//        showLoading("升级中");
//        String[] args = {"pm", "install", "--norcofz", filePath};
//        ProcessBuilder processBuilder = new ProcessBuilder(args);
//        Process process = null;
//        BufferedReader successResult = null;
//        BufferedReader errorResult = null;
//        StringBuilder successMsg = new StringBuilder();
//        StringBuilder errorMsg = new StringBuilder();
//        try {
//            process = processBuilder.start();
//            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            String s;
//            while ((s = successResult.readLine()) != null) {
//                successMsg.append(s);
//            }
//            while ((s = errorResult.readLine()) != null) {
//                errorMsg.append(s);
//            }
//            dismissLoading();
//            return process.waitFor() == 0 || successMsg.toString().contains("Success");
//        } catch (IOException | InterruptedException e) {
//            dismissLoading();
//            e.printStackTrace();
//        } finally {
//            try {
//                if (successResult != null) {
//                    successResult.close();
//                }
//                if (errorResult != null) {
//                    errorResult.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (process != null) {
//                process.destroy();
//            }
//            dismissLoading();
//        }
//        return false;
////————————————————
////
////        版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
////
////        原文链接：https://blog.csdn.net/qq_20451879/article/details/117563326
//    }

    @Override
    public void notifyByThemeChanged() {

    }

}