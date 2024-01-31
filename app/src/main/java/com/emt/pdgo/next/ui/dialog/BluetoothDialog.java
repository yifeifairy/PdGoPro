//package com.emt.pdgo.next.ui.dialog;
//
//import android.app.Dialog;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothManager;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bhm.sdk.rxlibrary.rxbus.RxBus;
//import com.emt.pdgo.next.MyApplication;
//import com.emt.pdgo.next.common.config.RxBusCodeConfig;
//import com.emt.pdgo.next.ui.activity.InputBodyDataActivity;
//import com.emt.pdgo.next.ui.adapter.BluetoothDeviceAdapter;
//import com.emt.pdgo.next.util.decoration.SpaceItemDecoration;
//import com.lifesense.ble.DeviceConnectState;
//import com.lifesense.ble.LsBleManager;
//import com.lifesense.ble.OnConnectListener;
//import com.lifesense.ble.PairCallback;
//import com.lifesense.ble.ReceiveDataCallback;
//import com.lifesense.ble.SearchCallback;
//import com.lifesense.ble.bean.BloodPressureData;
//import com.lifesense.ble.bean.LsDeviceInfo;
//import com.lifesense.ble.bean.WeightData_A2;
//import com.lifesense.ble.bean.WeightData_A3;
//import com.lifesense.ble.commom.BroadcastType;
//import com.lifesense.ble.commom.DeviceType;
//import com.pdp.rmmit.pdp.R;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
//
///**
// * Created by Administrator on 2016/12/22.
// */
//
//public class BluetoothDialog extends DialogFragment {
//
//
//    Unbinder mUnbinder;
//
//
//    @BindView(R.id.layout_scan)
//    RelativeLayout layoutScan;
//    @BindView(R.id.tv_connect)
//    TextView tvConnect;
//    @BindView(R.id.layout_connected)
//    RelativeLayout layoutConnected;
//
//    @BindView(R.id.device_list)
//    RecyclerView mRecyclerView;
//
//    @BindView(R.id.tv_weight_value)
//    TextView tvWeightValue;
//    @BindView(R.id.layout_weight)
//    RelativeLayout layoutWeight;
//    @BindView(R.id.tv_blood_pressure)
//    TextView tvBloodPressure;
//    @BindView(R.id.tv_blood_pressure_value)
//    TextView tvBloodPressureValue;
//    @BindView(R.id.tv_heart_rate)
//    TextView tvHeartRate;
//    @BindView(R.id.tv_heart_rate_value)
//    TextView tvHeartRateValue;
//
//    @BindView(R.id.layout_blood)
//    LinearLayout layoutBlood;
//
//    //    @BindView(R.id.layout_connecting)
////    RelativeLayout layoutConnecting;
//    @BindView(R.id.layout_data)
//    FrameLayout layoutData;
//    @BindView(R.id.btn_sure)
//    Button btnSure;
//
//
//    private String TAG = "BluetoothDialog";
//
//    private View view;
//
//
//    private LsBleManager mLsBleManager;
//    private Context context;
//    private Set<String> macSet = new HashSet<>();
//    private List<LsDeviceInfo> deviceInfos = new ArrayList<>();
//    //默认是扫描体重秤
//    private DeviceType deviceType = DeviceType.WEIGHT_SCALE;
//
//    private enum ViewStatus {
//        SCAN, CONNECTING, CONNECTED;
//    }
//
//
//    private Toast toast;
//    private BluetoothAdapter bluetoothAdapter;
//
//    private BluetoothDeviceAdapter mAdapter;
//
//
//    /****更新listview 扫描到的蓝牙设备******/
//    private static final int UPDATA_UI = 0X1000;
//    /******更新接收到的 体重数据*******/
//    private static final int UPDATA_WEIGHT_DATA = 0X1001;
//
//    /****跟新接收到的血压数据********/
//    private static final int UPDATA_BLOOD_DATA = 0X1002;
//    /****更新title**********/
//    private static final int UPDATA_TITLE = 0X1003;
//
//    /****收缩压******/
//    private float systolic = 0f;
//    /*****舒张压*****/
//    private float diastolic = 0f;
//    /******体重****/
//    private double weight = 0f;
//    /*****心率****/
//    private float pulseRate = 0f;
//
//    private String deviceName = "";
//    private String deviceMac = "";
//
//    /**
//     * 更新UI
//     */
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            switch (msg.what) {
//                case UPDATA_UI:
//                    Log.e(TAG, "handler  更新UI....1");
//                    if (mAdapter == null) {
//                        if (mAdapter == null) {
//                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            mAdapter = new BluetoothDeviceAdapter(R.layout.item_bluetooth_device_data, deviceInfos, mListener);
//                            mRecyclerView.addItemDecoration(new SpaceItemDecoration(1, 0, 0));
//                            //添加Android自带的分割线
//                            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
//                            mRecyclerView.setAdapter(mAdapter);
//                        } else {
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case UPDATA_WEIGHT_DATA:
//                    Log.e(TAG, "handler  更新体重数据");
//                    tvWeightValue.setText(new DecimalFormat("0.0").format(weight));
//                    btnSure.setVisibility(View.VISIBLE);
//                    break;
//                case UPDATA_BLOOD_DATA:
//                    Log.e(TAG, "handler  更新血压数据");
//                    tvBloodPressureValue.setText(String.valueOf((int) systolic) + "/" + (String.valueOf((int) diastolic)));
//                    tvHeartRateValue.setText((int) pulseRate + "");
//                    btnSure.setVisibility(View.VISIBLE);
//                    break;
//                case UPDATA_TITLE:
//                    tvConnect.setText("已连接: " + deviceName);
//                    break;
//            }
//
//        }
//    };
//
//    public static BluetoothDialog instance() {
//
//        BluetoothDialog dialog = new BluetoothDialog();
//
//        Bundle bundle = new Bundle();
//
//        dialog.setArguments(bundle);
//
//        return dialog;
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), (int) (dm.heightPixels * 0.75));
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().setCanceledOnTouchOutside(false);
//
//        context = getActivity();
//
//        view = inflater.inflate(R.layout.dialog_bluetooth, container, false);
//
//        mUnbinder = ButterKnife.bind(this, view);
//
//        scanStatus();
//
//        initData();
//
//        return view;
//    }
//
//
//    /**
//     * 扫描设备状态 view
//     */
//    private void scanStatus() {
//
//        layoutScan.setVisibility(View.VISIBLE);
//        layoutConnected.setVisibility(View.GONE);
//
//        layoutWeight.setVisibility(View.GONE);
//        layoutBlood.setVisibility(View.GONE);
//        mRecyclerView.setVisibility(View.VISIBLE);
//
//        btnSure.setVisibility(View.GONE);
//
////        layoutConnecting.setVisibility(View.GONE);
//
//    }
//
//    /**
//     * 连接设备成功后view
//     */
//    private void connectedStatus() {
//
//        Log.e(TAG, "-------connectedStatus------");
//
//        layoutScan.setVisibility(View.GONE);
//        layoutConnected.setVisibility(View.VISIBLE);
//
////        layoutConnecting.setVisibility(View.GONE);
//
//        mRecyclerView.setVisibility(View.GONE);
//        if (deviceType == DeviceType.WEIGHT_SCALE) {
//            //如果是体重
//            layoutWeight.setVisibility(View.VISIBLE);
//            layoutBlood.setVisibility(View.GONE);
//
//        } else if (deviceType == DeviceType.SPHYGMOMANOMETER) {
//            //如果是血压
//            layoutBlood.setVisibility(View.VISIBLE);
//            layoutWeight.setVisibility(View.GONE);
//        }
//    }
//
//
//    /**
//     * 连接中的状态
//     */
//    private void connectingStatus() {
//
//        mRecyclerView.setVisibility(View.GONE);
//        layoutWeight.setVisibility(View.GONE);
//        layoutBlood.setVisibility(View.GONE);
////        layoutConnecting.setVisibility(View.VISIBLE);
//
//    }
//
//    private BluetoothDeviceAdapter.OnCommonClickListener mListener = new BluetoothDeviceAdapter.OnCommonClickListener() {
//
//        @Override
//        public void onClick(LsDeviceInfo item) {
//            connect(item);
//        }
//    };
//
//
//    @OnClick({R.id.btn_sure, R.id.btn_cancel})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_sure:
//                sure();
//                break;
//            case R.id.btn_cancel:
//                dismiss();
//                break;
//        }
//    }
//
//
//    /***
//     * 确定测量数据 把测量数据回传到 填写界面
//     */
//    private void sure() {
//
//
//        /******测量的体重*****/
//        if (deviceType == DeviceType.WEIGHT_SCALE) {
//            RxBus.get().send(RxBusCodeConfig.EVENT_INPUT_BODY_DATA_WEIGHT, tvWeightValue.getText().toString());
//            dismiss();
//            return;
//        }
//
//        /******测量的血压*****/
//        if (deviceType == DeviceType.SPHYGMOMANOMETER) {
//            String[] bloodData = new String[3];
//            bloodData[0] = String.valueOf((int) systolic);
//            bloodData[1] = String.valueOf((int) diastolic);
//            bloodData[2] = String.valueOf((int) pulseRate);
//            RxBus.get().send(RxBusCodeConfig.EVENT_INPUT_BODY_DATA_BLOOD, bloodData);
//            dismiss();
//            return;
//        }
//
//
//    }
//
//
//    private void initData() {
//
//        BluetoothManager bluetoothManager =
//                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
//        bluetoothAdapter = bluetoothManager.getAdapter();
//
//        mLsBleManager = MyApplication.mLsBleManager;
//        if (mLsBleManager == null) {
//            mLsBleManager = LsBleManager.newInstance();
//            mLsBleManager.initialize(context.getApplicationContext());
//        }
//
//
//        if (!mLsBleManager.isSupportLowEnergy()) {
//            showToast("该设备不支持蓝牙4.0");
//            Log.e(TAG, "该设备不支持蓝牙4.0");
//            return;
//        }
//
//        if (!mLsBleManager.isOpenBluetooth()) {
//            //打开蓝牙
//            Log.e(TAG, "正在打开蓝牙......");
//            bluetoothAdapter.enable();
//        }
//
//        List<DeviceType> type = new ArrayList<>();
////        type.add(deviceType);
//        type.add(DeviceType.WEIGHT_SCALE);
//        type.add(DeviceType.SPHYGMOMANOMETER);
//        Log.e(TAG, "开始扫描设备......");
//        mLsBleManager.searchLsDevice(callback, type, BroadcastType.ALL);
////        mLsBleManager.searchLsDevice(callbackPair, type, BroadcastType.PAIR);
////        mLsBleManager.searchLsDevice(callback, type, BroadcastType.NORMAL);
//        //BroadcastType是一个枚举类，这个类主要是封装了乐心设备的蓝牙广播类型，可以通过设置 不同的广播类型来搜索处于对应广播类型的乐心设备
//        //BroadcastType.ALL 所有广播信号，包括正常与配对广播;BroadcastType.NORMAL 正常广播信号;BroadcastType.PAIR 配对广播信号
//    }
//
//
//    private SearchCallback callback = new SearchCallback() {
//        @Override
//        public void onSearchResults(LsDeviceInfo lsDeviceInfo) {
//
//            String macAddress = lsDeviceInfo.getMacAddress();
//            if (!macSet.contains(macAddress)) {
//                Log.e(TAG, "扫描到新设备 name:" + lsDeviceInfo.getDeviceName() + "  配对状态:" + lsDeviceInfo.getPairStatus()
//                        + "  macAddress:" + lsDeviceInfo.getMacAddress());
//                macSet.add(macAddress);
//                deviceInfos.add(lsDeviceInfo);
//                upDataUI();
////                if (0 == lsDeviceInfo.getPairStatus()) {
////                    Log.e(TAG, "停止搜索设备 name:" + lsDeviceInfo.getDeviceName() + "  配对状态:" + lsDeviceInfo.getPairStatus());
////                    mLsBleManager.stopSearch();
////                    //添加多个已配对的测量设备对象
//////                    mLsBleManager.setMeasureDevice(deviceInfos);
////                    mLsBleManager.addMeasureDevice(lsDeviceInfo);
////                    mLsBleManager.startDataReceiveService(receiveDataCallback);
////                }
//            }
//        }
//    };
//
//
//    private SearchCallback callbackPair = new SearchCallback() {
//        @Override
//        public void onSearchResults(LsDeviceInfo lsDeviceInfo) {
//
//            String macAddress = lsDeviceInfo.getMacAddress();
//            if (!macSet.contains(macAddress)) {
//                Log.e(TAG, "扫描到派对过的设备 name:" + lsDeviceInfo.getDeviceName() + "  macAddress:" + lsDeviceInfo.getMacAddress());
//                macSet.add(macAddress);
//                deviceInfos.add(lsDeviceInfo);
//                upDataUI();
//
//
//            }
//        }
//    };
//
//
//    /**
//     * 更新UI listView
//     */
//    private void upDataUI() {
//
//        Log.e(TAG, "....更新UI.....");
//
//        //在UI线程更新UI
//        handler.sendEmptyMessage(UPDATA_UI);
//
//    }
//
//    /**
//     * 连接设备
//     *
//     * @param deviceInfo
//     */
//    private void connect(final LsDeviceInfo deviceInfo) {
//        showToast("设备配对中...");
//        Log.e(TAG, "name:" + deviceInfo.getDeviceName() + " address:" + deviceInfo.getMacAddress() + " deviceType: " + deviceInfo.getDeviceType());
//
//        if ("01".equals(deviceInfo.getDeviceType())) {
//            deviceType = DeviceType.WEIGHT_SCALE;
//        } else if ("08".equals(deviceInfo.getDeviceType())) {
//            deviceType = DeviceType.SPHYGMOMANOMETER;
//        }
//        connectingStatus();
//
//        if (deviceInfo == null) {
//            Log.e(TAG, "connect deviceInfo is null");
//            return;
//        }
//
//        //停止搜索
//        Log.e(TAG, "----connect  停止搜索---");
//        mLsBleManager.stopSearch();//配对过程必须停止搜索设备
//
//        Log.e(TAG, "----connect  开始配对---");
//        //配对
//        mLsBleManager.startPairing(deviceInfo, new PairCallback() {
//            @Override
//            public void onDiscoverUserInfo(List list) {// A2 协议的乐心设备在配 对过程中是没有这些用户信息返回。
//                super.onDiscoverUserInfo(list);
//            }
//
//            @Override
//            public void onPairResults(LsDeviceInfo lsDeviceInfo, int status) {//当 status=0 时，表示本次配对成功，返回的 lsDevice 对象不为空；当 status!=0 时，表示本次配对失败，返回的 lsDevice 对象为空，
//                super.onPairResults(lsDeviceInfo, status);
//
//                Log.e(TAG, "----connect  配对中 1  status :" + status);
//
//                if (lsDeviceInfo != null) {
//                    Log.e(TAG, "----connect  配对中 2 status ：" + status + "  name:" + lsDeviceInfo.getDeviceName() + "  address:" + lsDeviceInfo.getMacAddress());
//                }
//
//                if (status == 0) {
//                    //配对成功 连接设备
//                    Log.e(TAG, "----connect  配对成功---");
//                    //添加一个配对成功的设备
//                    mLsBleManager.addMeasureDevice(lsDeviceInfo);
//
//                    mLsBleManager.startDataReceiveService(receiveDataCallback);
//
//                    deviceName = lsDeviceInfo.getDeviceName();
//                    deviceMac = lsDeviceInfo.getMacAddress();
//                    handler.sendEmptyMessage(UPDATA_TITLE);
//
//
//                    if (deviceType == DeviceType.SPHYGMOMANOMETER) {
//                        //如果是连接的血压计
//                        mLsBleManager.connectDevice(deviceInfo, connectListener);
//                        Log.e(TAG, "----connect  连接血压计---");
//                    } else if (deviceType == DeviceType.WEIGHT_SCALE) {
//                        Log.e(TAG, "----connect  连接体重秤---");
//                    }
//
//
//                    layoutScan.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            connectedStatus();
//                        }
//                    }, 100);
//
//                } else {
//                    Log.e(TAG, "----connect  配对失败 ---");
//                    showToast("配对失败");
//
//                    layoutScan.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            scanStatus();
//                        }
//                    }, 200);
//                }
//            }
//        });
//    }
//
//    /**
//     * OnConnectListener是一个接口类，用于实现读取通过命令启动测量的血压计设备的测量数据 回调、以及接收处理与设备和连接状态、下发开始测量的命令等
//     */
//    private OnConnectListener connectListener = new OnConnectListener() {
//        @Override
//        public void onConnectStateChange(DeviceConnectState deviceConnectState) {//设备当前的连接状态
//            Log.e(TAG, "connectListener onConnectStateChange---");
//        }
//
//        @Override
//        public void onReceiveBloodPressureData(BloodPressureData bloodPressureData) {//血压计测量数据的实体类
//            //当是连接血压时
//            Log.e(TAG, "connectListener    onReceiveBloodPressureData---");
//        }
//
//        @Override
//        public void onWaitingForStartMeasuring(String s) {//通过重写这个接口方法来处理是否启动设备开始测量的命令，当与设备建立连接成 功后，会触发这个回调接口通知用户，现在设备已经可以开始测量了，等待用户的开始测量命令。
//
//            Log.e(TAG, "connectListener    onWaitingForStartMeasuring---");
//
//            Log.e(TAG, "OnConnectListener  onWaitingForStartMeasuring() s: " + s);
//
//
//            if (deviceType == DeviceType.SPHYGMOMANOMETER) {
//                //如果是连接血压计 发送开始测量命令
//                mLsBleManager.startMeasuring();
//            }
//
//        }
//    };
//
//
//    private ReceiveDataCallback receiveDataCallback = new ReceiveDataCallback() {
//
//        @Override
//        public void onReceiveDeviceInfo(LsDeviceInfo mLsDeviceInfo) {//接收当前已连接上的设备的详细信息，适用于没有配 对过程中的设备
//            super.onReceiveDeviceInfo(mLsDeviceInfo);
//            Log.e(TAG, "ReceiveDataCallback onReceiveDeviceInfo() :" + mLsDeviceInfo.toString());
//        }
//
////        @Override
////        public void onConnectStateChange(DeviceConnectState connectState) {//设备当前的连接状态
////            super.onConnectStateChange(connectState);
////            Log.e(TAG, "ReceiveDataCallback onConnectStateChange() :" + connectState.toString());
////        }
//
//        @Override
//        public void onReceiveWeightData_A3(WeightData_A3 weightData_a3) {
//            super.onReceiveWeightData_A3(weightData_a3);
//            Log.e(TAG, "ReceiveDataCallback onReceiveWeightData_A3() weight:" + weightData_a3.getWeight());
//            weight = weightData_a3.getWeight();
//            handler.sendEmptyMessage(UPDATA_WEIGHT_DATA);
//        }
//
//        @Override
//        public void onReceiveWeightDta_A2(WeightData_A2 weightData_a2) {
//            super.onReceiveWeightDta_A2(weightData_a2);
//            Log.e(TAG, "ReceiveDataCallback onReceiveWeightData_A2() weight:" + weightData_a2.getWeight());
//            weight = weightData_a2.getWeight();
//            handler.sendEmptyMessage(UPDATA_WEIGHT_DATA);
//        }
//
//        @Override
//        public void onReceiveBloodPressureData(BloodPressureData bloodPressureData) {//接收血压计的测量数据
//            super.onReceiveBloodPressureData(bloodPressureData);
//            Log.e(TAG, "ReceiveDataCallback onReceiveBloodPressureData() systolic:" + bloodPressureData.getSystolic() + " dialysis:" + bloodPressureData.getDiastolic() + "  pulseRate:" + bloodPressureData.getPulseRate());
//            systolic = bloodPressureData.getSystolic();
//            diastolic = bloodPressureData.getDiastolic();
//            pulseRate = bloodPressureData.getPulseRate();
//            handler.sendEmptyMessage(UPDATA_BLOOD_DATA);
//        }
//    };
//
//
//    private void showToast(String message) {
//        if (toast == null) {
//            toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
//        }
//        toast.setText(message);
//        toast.show();
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mUnbinder.unbind();
//        if (mLsBleManager != null) {
//            Log.e(TAG, "-----onDestroy------");
//            mLsBleManager.stopSearch();
//            mLsBleManager.stopDataReceiveService();
//            mLsBleManager.unregisterBleStateChangeReceiver();
//        }
//    }
//
//
//}
