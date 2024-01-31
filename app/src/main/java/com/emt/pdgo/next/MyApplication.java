package com.emt.pdgo.next;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;

import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.database.EmtDataBase;
import com.emt.pdgo.next.interfaces.ThemeChangeObserver;
import com.emt.pdgo.next.receiver.NetBroadcastReceiver;
import com.emt.pdgo.next.rxlibrary.rxjava.RxConfig;
import com.emt.pdgo.next.service.SerialPortService;
import com.emt.pdgo.next.ui.dialog.MyLoadingDialog;
import com.emt.pdgo.next.ui.presentation.MyPresentation;
import com.emt.pdgo.next.util.ActivityLifeCycle;
import com.emt.pdgo.next.util.EmtAndroidInfoUil;
import com.emt.pdgo.next.util.EmtFileSdkUtil;
import com.emt.pdgo.next.util.FileDatabaseContext;
import com.emt.pdgo.next.util.FileUtils;
import com.emt.pdgo.next.util.TtsHelper;
import com.emt.pdgo.next.util.UsbUtil;
import com.emt.pdgo.next.util.logger.AndroidLogTool;
import com.emt.pdgo.next.util.logger.LogLevel;
import com.emt.pdgo.next.util.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private List<ThemeChangeObserver> mThemeChangeObserverStack; //  主题切换监听栈

    public static boolean isRemoteShow = false;

    private MyPresentation mPresentation;
    private Display[] displays; //定义一个屏幕数组

    public static int fuNum = 1;

    public static int rinseSupplyVol;
    public static int rinseVol;

    public final String DB_NAME = "pdgo_next";

    public static boolean isDebugFinish = false; // 是否故障下机
    public static boolean isBuzzerOff = false; // 是否消音中
    public static boolean isFailure = false;

    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }

    public static Context mContext;
//    public static LsBleManager mLsBleManager;
    /*** 握手成功 */
    public static boolean hasHello = false;

    public static boolean isOpenMainSerial = false;

    public static boolean isOpenLedSerial = false;

    public static boolean sendSelfCheck = false;

    public static boolean isStartTreatment = false;

    public static boolean isLight = true;


    public static boolean isPipecartInstall = false;

    public static boolean isHideLed = false;//测试隐藏LED功能

    public static int dialysateInitialValue;//透析液初始值

    public static float mTargetTemper = 0f;//预热设置的加热目标温度

    public static boolean treatmentRunning = false;//是否在治疗中

    public static boolean acPowerOff = false;//是否220v市电供电中断

    public static boolean isKid = false; // 是否儿童模式

    public static int apdMode = 0; // apd治疗模式
    public static boolean apdTreat = false;

    public static boolean dprTreatRunning = false; // dpr治疗中

    public static int versionMode = 0; // 版本类型：0：只有APD模式；1：只有DPR；2：APD,DPR共存

    public static final boolean DEBUG = false;

    public static int TPD_FIRST_VOL = PdproHelper.getInstance().tpdBean().firstPerfusionVolume;
    public static int KID_FIRST_VOL = PdproHelper.getInstance().kidBean().firstPerfusionVolume;
    public static int FIRST_VOL = PdproHelper.getInstance().getTreatmentParameter().firstPerfusionVolume;

    public static String currCmd = "";
    public static int firstVol = PdproHelper.getInstance().getPrescription().firstpersuse; // DPR首次灌注量
    public static int cfpdFirstVol = PdproHelper.getInstance().getCfpdBean().firstpersuse; // DPR首次灌注量

    private void registerReceiverWifi() {
        NetBroadcastReceiver wifiReceiver = new NetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态
//        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        registerReceiver(wifiReceiver, filter);
    }

    private USBReceiver mUsbReceiver;

    /*** 开始治疗时间 yyyy-mm-dd hh:mm:ss **/
    public static String mStartTime;
    /*** 治疗结束时间 yyyy-mm-dd hh:mm:ss **/
    public static String mEndTime;

    public static int brightness = 166;

    public static String phone = "";

    public static int ApdTotalDrainVol; // 总引流量
    public static int ApdTotalPerVol; // 总灌注量
    public static int ApdTotalUltVol; // 总超滤量
    public static String ApdTreatTime;

    public static boolean isReset; // 参数恢复出厂设置

    public static boolean isDpr = false;

    // 治疗模式
    public static int mode = 2;
    public static boolean isDebug = false; // 工作模式

    // 特殊处方
    public static int supply1 = 1;
    public static int supply2 = 2;

    public static boolean inTreatment = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initConfig();
        //初始化DBFLOW
        initDB();
        initDao();
        ActivityLifeCycle lifecycleCallbacks = new ActivityLifeCycle();
        registerActivityLifecycleCallbacks(lifecycleCallbacks);
//        startMainBoardService();
//        startLedBoardService();
        myApplication = this;
        registerReceiverWifi();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    EmtDataBase
                            .getInstance(myApplication)
                            .getFaultCodeDao()
                            .delete();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        DisplayManager mDisplayManager;// 屏幕管理类
//        mDisplayManager = (DisplayManager) this
//                .getSystemService(Context.DISPLAY_SERVICE);
//        displays = mDisplayManager.getDisplays();

//        registerUSBReceiver();
//        CrashHandler.getInstance().init(getApplicationContext());
        TtsHelper.getInstance(this).initTts();
//        BaiduTtsHelper.getInstance(this).initialTts();
    }

    /**
     * app配置内容
     */
    private void initConfig() {
        // 初始化日志
        inintLog();
        FileUtils.init(mContext);
        initRxConfig();
    }

    private void initRxConfig() {
        /*配置默认的Rx配置项*/
        RxConfig.newBuilder()
                .setRxLoadingDialog(new MyLoadingDialog())
                .setDialogAttribute(true, false, false)
                .isDefaultToast(true)
                .isLogOutPut(true)
                .setReadTimeOut(30000)
                .setConnectTimeOut(30000)
                .setOkHttpClient(null)
                .build();
    }

    /**
     * 初始化日志显示信息
     */
    private void inintLog() {

        Logger.init(EmtAndroidInfoUil.getPackageName(mContext))
                .methodCount(2)
                .hideThreadInfo()
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)//LogLevel.FULL，显示日志。NONE不显示日志
                .methodOffset(0)
                .logTool(new AndroidLogTool());

    }

    //初始化数据库
    public void initDB() {
        FileDatabaseContext mSdDatabaseContext = new FileDatabaseContext(this, new File(EmtFileSdkUtil.getBaseDirFile() + File.separator + DB_NAME), true);
        FlowManager.init(mSdDatabaseContext);
    }

    /**
     * 初始化数据库
     */
    private void initDao() {

//        treatmentInfoTable = new TreatmentInfoTable();
//        PdGoDbManager.getInstance().initMainBoardTable();
//        PdGoDbManager.getInstance().initTemperBoardTable();
//        PdGoDbManager.getInstance().initTreatmentTable();

    }

    public void registerUSBReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction("android.hardware.usb.action.USB_STATE");
        mUsbReceiver = new USBReceiver();
        registerReceiver(mUsbReceiver, filter);
        Log.e("MyApplication", "registerUSBReceiver");
    }

//    /**
//     * 获取设备参数
//     *
//     * @return
//     */
//    public static DeviceStatusInfo getDeviceStatusInfo() {
//
//        return mDeviceStatusInfo;
//    }
//
//    /**
//     * 启动主板服务
//     */
//    public static void startMainBoardService() {
//        if (!isServiceRunning(context, MainBoardService.class.getName())) {
//            Intent intent = new Intent();
//            intent.setClass(context, MainBoardService.class);
//            context.startService(intent);
//        } else {
//            Log.e("MyApplication", "startMainBoardService has start");
//        }
//    }

    /**
     * 关闭串口数据处理服务
     */
    public static void stopSerialPortService() {
        Intent intent = new Intent();
        intent.setClass(mContext, SerialPortService.class);
        mContext.stopService(intent);
    }

    /**
     * 启动串口数据处理服务
     */
    public static void startSerialPortService() {
        if (!isServiceRunning(mContext, SerialPortService.class.getName())) {
            Intent intent = new Intent();
            intent.setClass(mContext, SerialPortService.class);
            mContext.startService(intent);
        } else {
            Log.e("MyApplication", "startSerialPortService has start");
        }
    }

//    /**
//     * 关闭主板服务
//     */
//    public static void stopMainBoardService() {
//        Intent intent = new Intent();
//        intent.setClass(context, MainBoardService.class);
//        context.stopService(intent);
//    }

    /**
     * 启动LED主板服务
     */
    public static void startLedBoardService() {
//        if(!isHideLed){
//            if (!isServiceRunning(context, LedBoardService.class.getName())) {
//                Intent intent = new Intent();
//                intent.setClass(context, LedBoardService.class);
//                context.startService(intent);
//            } else {
//                Log.e("MyApplication", "startLedBoardService has start");
//            }
//        }
    }

    /**
     * 关闭LED服务
     */
    public static void stopLedBoardService() {
//        Intent intent = new Intent();
//        intent.setClass(context, LedBoardService.class);
//        context.stopService(intent);
    }

    public boolean usbActivityIsRunning = false;

    /**
     * 方法描述：判断某一Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service的全路径： 包名 + service的类名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    /**
     * 获得observer堆栈
     */
    private List<ThemeChangeObserver> obtainThemeChangeObserverStack() {
        if (mThemeChangeObserverStack == null) mThemeChangeObserverStack = new ArrayList<>();
        return mThemeChangeObserverStack;
    }

    /**
     * 向堆栈中添加observer
     */
    public void registerObserver(ThemeChangeObserver observer) {
        if (observer == null || obtainThemeChangeObserverStack().contains(observer)) return;
        obtainThemeChangeObserverStack().add(observer);
    }



    /**
     * 从堆栈中移除observer
     */
    public void unregisterObserver(ThemeChangeObserver observer) {
        if (observer == null || !(obtainThemeChangeObserverStack().contains(observer))) return;
        obtainThemeChangeObserverStack().remove(observer);
    }

    /**
     * 向堆栈中所有对象发送更新UI的指令
     */
    public void notifyByThemeChanged() {
        List<ThemeChangeObserver> observers = obtainThemeChangeObserverStack();
        for (ThemeChangeObserver observer : observers) {
            observer.loadingCurrentTheme(); //
            observer.notifyByThemeChanged(); //
        }
    }

    public void unregisterReceiver() {
        try {
            if (mContext != null) {
                mContext.unregisterReceiver(mUsbReceiver);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        unregisterReceiver();
    }

    private static final String TAG = "MyApplication";

    public void receiveUSBDisk() {
        boolean isUDiskConnected = UsbUtil.isUDiskConnected(mContext);
        Log.e("activity", "插入USB设备:" + isUDiskConnected);
        if (isUDiskConnected) {
//            try {
//                Thread.sleep(10 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.e("activity", "isUDiskConnected=true");
//            for (int i = 0; i < getAllExternalSdcardPath().size(); i++) {
//                Log.e("SD路径", getAllExternalSdcardPath().get(i));
//            }
//            RxBus.get().send(RxBusCodeConfig.EVENT_USB_DEVICE_ATTACHED, "");
//            UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(this /* Context or Activity */);
//            for(UsbMassStorageDevice device: devices) {
//
//                // before interacting with a device you need to call init()!
////                device.init();
//                try {
//                    device.init();
//                    // Only uses the first partition on the device
//                    FileSystem currentFs = device.getPartitions().get(0).getFileSystem();
////                    Log.e(TAG, "Capacity: " + currentFs.getCapacity());
////                    Log.e(TAG, "Occupied Space: " + currentFs.getOccupiedSpace());
////                    Log.e(TAG, "Free Space: " + currentFs.getFreeSpace());
////                    Log.e(TAG, "Chunk size: " + currentFs.getChunkSize());
//                    UsbFile root = currentFs.getRootDirectory();
//                    UsbFile[] files = root.listFiles();
//                    for(UsbFile file: files) {
////                        Log.e(TAG, file.getName());
////                        if(!file.isDirectory()) {
////                            Log.e(TAG, String.valueOf(file.getLength()));
////                        }
//                        String rooPath = "/mnt/media_rw/4CF1-7B62/";
////                        if (getAllExternalSdcardPath().size() > 1) {
//                            if (file.getName().startsWith("PDNEXT") && file.getName().endsWith(".apk")) {
//                                Intent intent = new Intent(mContext, USBDiskActivity.class);
//                                intent.putExtra("filePath",
//                                        rooPath+file.getName()
//                                );

//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                MyApplication.getInstance().usbActivityIsRunning = true;
//                                mContext.startActivity(intent);
//                            }
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

//            Log.e("myApplication", "打开USBDiskActivity");
//            installApk("/mnt/media_rw/4CF1-7B62/"+"PDNEXT_v1.2.1_release.apk");
        }
    }
    public static int chargeFlag = 1;
    public static int batteryLevel = 50;
    public void receiveUSBRemove() {

    }

    private class USBReceiver extends BroadcastReceiver {

        private boolean hasFirst = true;
        private final String ACTION_MEDIA_MOUNTED = Intent.ACTION_MEDIA_MOUNTED;//媒体安装 - 获取USB路径需在媒体安装完成后获取
        @Override
        public void onReceive(Context context, Intent intent) {
            // 这里可以拿到插入的USB设备对象
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            switch (intent.getAction()) {
                case Intent.ACTION_MEDIA_MOUNTED://扩展介质被插入，而且已经被挂载。
                    if (intent.getData() != null) {
                        String path = intent.getData().getPath();
                        Log.e("ACTION_MEDIA_MOUNTED","path--" + path);
                    }
                    Log.e("ACTION_MEDIA_MOUNTED","扩展介质被插入，而且已经被挂载");
                    break;
                case UsbManager.ACTION_USB_DEVICE_ATTACHED: // 插入USB设备
                    Log.e("USBReceiver", "插入USB设备");
//                ToastUtils.showToast(context,"发现USB设备");
                    receiveUSBDisk();
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED: // 拔出USB设备
                    Log.e("USBReceiver", "拔出USB设备");
                    receiveUSBRemove();
                    break;
                default:
                    break;
            }
        }
    }

    public void installApk(String filePath) {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Log.e("installApk", "installApk--------");
            Uri contentUri = FileProvider.getUriForFile(
                    this
                    , "com.pdp.rmmit.pdp.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.e("installApk", "installApk----");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }
}

