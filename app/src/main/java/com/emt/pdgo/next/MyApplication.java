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
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;

import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;

import com.emt.pdgo.next.common.PdproHelper;
import com.emt.pdgo.next.common.config.PdGoConstConfig;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.bean.PdData;
import com.emt.pdgo.next.interfaces.ThemeChangeObserver;
import com.emt.pdgo.next.receiver.NetBroadcastReceiver;
import com.emt.pdgo.next.rxlibrary.rxbus.RxBus;
import com.emt.pdgo.next.rxlibrary.rxjava.RxConfig;
import com.emt.pdgo.next.service.SerialPortService;
import com.emt.pdgo.next.ui.dialog.MyLoadingDialog;
import com.emt.pdgo.next.ui.presentation.MyPresentation;
import com.emt.pdgo.next.util.CacheUtils;
import com.emt.pdgo.next.util.EmtAndroidInfoUil;
import com.emt.pdgo.next.util.FileUtils;
import com.emt.pdgo.next.util.TtsHelper;
import com.emt.pdgo.next.util.UsbUtil;
import com.emt.pdgo.next.util.logger.AndroidLogTool;
import com.emt.pdgo.next.util.logger.LogLevel;
import com.emt.pdgo.next.util.logger.Logger;
import com.emt.pdgo.next.util.task.ConsumptionTask;
import com.emt.pdgo.next.util.task.LineUpTaskHelp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class MyApplication extends Application {

    private List<ThemeChangeObserver> mThemeChangeObserverStack; //  主题切换监听栈

    public static boolean isRemoteShow = false;

    private MyPresentation mPresentation;
    private Display[] displays; //定义一个屏幕数组

    public static int fuNum = 1;

    public static int rinseSupplyVol;
    public static int rinseVol;

    public static boolean fistHeat; // 是否第一次加热

    public final String DB_NAME = "pdgo_next";

    public static boolean isDebugFinish = false; // 是否故障下机
    public static boolean isBuzzerOff = false; // 是否消音中
    public static boolean isFailure = false;


//    public static DetailedBean detailedBean;
    public static PdData pdData;
    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }

    public static Context mContext;
//    public static LsBleManager mLsBleManager;
    /*** 握手成功 */
    public static boolean hasHello = false;

    public static boolean isOpenMainSerial = true;

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

    public static int apdMode = 1; // apd治疗模式
    public static boolean apdTreat = false;

    public static boolean usb = false;

    public static boolean dprTreatRunning = false; // dpr治疗中

    public static int versionMode = 0; // 版本类型：0：只有APD模式；1：只有DPR；2：APD,DPR共存

    public static final boolean DEBUG = false;

    public static int TPD_FIRST_VOL = 0;
    public static int KID_FIRST_VOL = 0;
    public static int FIRST_VOL = 0;

    public static String currCmd = "";
    public static int firstVol = 0; // DPR首次灌注量
    public static int cfpdFirstVol =0; // DPR首次灌注量

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

    public static boolean isReset; // 参数恢复出厂设置

    public static boolean isDpr = false;

    // 治疗模式
    public static int mode = 2;
    public static boolean isDebug = false; // 工作模式

    // 特殊处方
    public static int supply1 = 1;
    public static int supply2 = 2;

    public static boolean inTreatment = false;

    public static int state = 1;

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
        initListener();
//        ActivityLifeCycle lifecycleCallbacks = new ActivityLifeCycle();
//        registerActivityLifecycleCallbacks(lifecycleCallbacks);
//        startMainBoardService();
//        startLedBoardService();
        myApplication = this;
//        detailedBean = new DetailedBean();
        pdData = new PdData();
        init();
        registerReceiverWifi();

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

    public static boolean isSupplyRinse = true;

    public static LineUpTaskHelp lineUpTaskHelp;
    /**
     *  注册任务监听
     */
    private void initListener(){
        if (lineUpTaskHelp == null) {
            lineUpTaskHelp = LineUpTaskHelp.getInstance();
        }
        lineUpTaskHelp.setOnTaskListener(new LineUpTaskHelp.OnTaskListener() {
            @Override
            public void exNextTask(ConsumptionTask task) {
                // 所有任务，会列队调用exNextTask。在这里编写你的任务执行过程
                exTask(task);
            }

            @Override
            public void noTask() {
//                Log.e("task","所有任务执行完成");
            }
        });
    }

    public static int index;
    private Handler handler;
    /**
     *  模拟执行任务呢
     */
    public void exTask(final ConsumptionTask task){
//        //                if(isOdd(System.currentTimeMillis())){
//        //                    // 模拟任务执行的结果====失败，如果一个任务失败了会导致整个计划失败，请调用此方法。
//        //                    Log.e("Post","任务失败了，结束掉相关联正在排队的任务组");
//        //                    lineUpTaskHelp.deletePlanNoAll(task.planNo);
//        //                }
//        // 检查列队
//        // 可更新UI或做其他事情
//        // 注意这里还在当前线程，没有开启新的线程
//        // new Runnable(){}，只是把Runnable对象以Message形式post到UI线程里的Looper中执行，并没有新开线程。
//        Disposable taskDisposable = Observable.timer(500, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(a -> {
//
//                    RxBus.get().send(RxBusCodeConfig.EVENT_SEND_COMMAND, task.planNo);
//                    // 检查列队
//                    lineUpTaskHelp.exOk(task);
//                    // 可更新UI或做其他事情
//                    // 注意这里还在当前线程，没有开启新的线程
//                    // new Runnable(){}，只是把Runnable对象以Message形式post到UI线程里的Looper中执行，并没有新开线程。
//                }); // 延时执行run内代码
//        if (taskCompositeDisposable == null) {
//            taskCompositeDisposable = new CompositeDisposable();
//        }
//        taskCompositeDisposable.add(taskDisposable);

//        Handler handler = new Handler(); // 如果这个handler是在UI线程中创建的
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(new Runnable() {  // 开启的runnable也会在这个handler所依附线程中运行，即主线程
            @Override
            public void run() {
                RxBus.get().send(RxBusCodeConfig.EVENT_SEND_COMMAND, task.planNo);
//                    // 检查列队
                lineUpTaskHelp.exOk(task);
                // 可更新UI或做其他事情
                // 注意这里还在当前线程，没有开启新的线程
                // new Runnable(){}，只是把Runnable对象以Message形式post到UI线程里的Looper中执行，并没有新开线程。
            }
        }, 1000); // 延时1s执行run内代码

    }

    public int useDeviceTime = 0; // 设备使用时间
    private CompositeDisposable dtCompositeDisposable;
    private void init() {
        DisposableObserver<Long> disposableObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {

                useDeviceTime ++;
                if (useDeviceTime >= 60 * 60 && (useDeviceTime % (60 * 60) == 0)) {
//                    if (useDeviceTime > 0 && useDeviceTime % (10) == 0) {
//                        if (PdproHelper.getInstance().useDeviceTime() == 0) {
//                            CacheUtils.getInstance().getACache().put(PdGoConstConfig.useDeviceTime, 1);
//                        } else {
                    Log.e("MyApplication","useDeviceTime:"+useDeviceTime);
                    int time = PdproHelper.getInstance().useDeviceTime();
                    CacheUtils.getInstance().getACache().put(PdGoConstConfig.useDeviceTime, String.valueOf(time + 1));
//                        }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        if (dtCompositeDisposable == null) {
            dtCompositeDisposable = new CompositeDisposable();
        }
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS).subscribe(disposableObserver);
        dtCompositeDisposable.add(disposableObserver);
    }
}

