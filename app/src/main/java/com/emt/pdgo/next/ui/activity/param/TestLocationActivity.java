package com.emt.pdgo.next.ui.activity.param;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellIdentityCdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.emt.pdgo.next.util.location.WifiInfoManager;
import com.pdp.rmmit.pdp.R;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestLocationActivity extends BaseActivity {

    public static final int LOCATION_CODE = 301;
    private LocationManager locationManager;
    private String locationProvider = null;

    private static final int MSG_SUCCESS = 0;// 获取成功的标识
    private static final int MSG_FAILURE = 1;// 获取失败的标识
//    private Thread mThread;
//    SCell cell = null;

    private Context mContext;

    private WifiInfoManager wifiInfoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_test_location);
        ButterKnife.bind(this);
        initHeadTitleBar("测试定位");
        mContext = this;
    }
    @BindView(R.id.powerIv)
    ImageView powerIv;
    @BindView(R.id.currentPower)
    TextView currentPower;
    @Subscribe(code = RxBusCodeConfig.RESULT_REPORT)
    public void receiveCmdDeviceInfo(String bean) {
        ReceiveDeviceBean mReceiveDeviceBean = JsonHelper.jsonToClass(bean, ReceiveDeviceBean.class);
        runOnUiThread(() -> {
            if (mReceiveDeviceBean.isAcPowerIn == 1) {
                powerIv.setImageResource(R.drawable.charging);
            } else {
                if (mReceiveDeviceBean.batteryLevel < 30) {
                    powerIv.setImageResource(R.drawable.poor_power);
                } else if (30 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 60 ) {
                    powerIv.setImageResource(R.drawable.low_power);
                } else if (60 < mReceiveDeviceBean.batteryLevel &&mReceiveDeviceBean.batteryLevel <= 80 ) {
                    powerIv.setImageResource(R.drawable.mid_power);
                } else {
                    powerIv.setImageResource(R.drawable.high_power);
                }
            }
            currentPower.setText(mReceiveDeviceBean.batteryLevel+"");
        });
    }
    @Override
    public void registerEvents() {
        if (MyApplication.chargeFlag == 1) {
            powerIv.setImageResource(R.drawable.charging);
        } else {
            if (MyApplication.batteryLevel < 30) {
                powerIv.setImageResource(R.drawable.poor_power);
            } else if (30 < MyApplication.batteryLevel &&MyApplication.batteryLevel < 60 ) {
                powerIv.setImageResource(R.drawable.low_power);
            } else if (60 < MyApplication.batteryLevel &&MyApplication.batteryLevel <= 80 ) {
                powerIv.setImageResource(R.drawable.mid_power);
            } else {
                powerIv.setImageResource(R.drawable.high_power);
            }
        }
        currentPower.setText(MyApplication.batteryLevel+"");
        sendToMainBoard(CommandDataHelper.getInstance().setStatusOn());
    }

    @Override
    public void initViewData() {

        getCellLac();

        String BSSID = getWifiBSSID(this);
        Log.e("BSSID", "BSSID=" + BSSID);
    }

    public static String getWifiBSSID(Context context) {

        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);

        if (wm != null) {

            WifiInfo winfo = wm.getConnectionInfo();

            return winfo.getBSSID();

        }

        return null;

    }

    //定义请求
    private static final int PERMISSIONS_FINE_LOCATION = 1;

    int cell;
    int lac;

    @SuppressLint("MissingPermission")
    private void getCellLac() {
//检查定位权限是否已经允许
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请定位权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_FINE_LOCATION);
        } else {
            //定位权限已经被允许
        }

        StationInfo stationInfo = new StationInfo();

        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

//        CellLocation cel = tel.getCellLocation();

        String operator = tel.getNetworkOperator();
        Log.e("operator", "operator=" + operator);
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        int cid = 0;
        int lac = 0;
        Log.e("mcc", "mcc -----------------》:" + mcc);
        Log.e("mnc", "mnc -----------------》:" + mnc);

//        List<CellInfo> infoLists = tel.getAllCellInfo();
//        for (CellInfo info : infoLists) {
//            CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
//            CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
//            CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
//            int strength = cellSignalStrengthCdma.getCdmaDbm();
//            int cid2 = cellIdentityCdma.getBasestationId();
//            // 处理 strength和id数据
//        }
        //需要判断网络类型，因为获取数据的方法不一样
        if (mnc == 11 || mnc == 03 || mnc == 05) {  //03 05 11 为电信CDMA
//            CdmaCellLocation location = (CdmaCellLocation) telephonyManager.getCellLocation();
            try {
                CellLocation cel = tel.getCellLocation();
                CdmaCellLocation location = (CdmaCellLocation) cel;
                //这里的值可根据接口需要的参数获取
                cid = location.getBaseStationId();
                lac = location.getNetworkId();
                mnc = location.getSystemId();
            } catch (Exception e) {
                GsmCellLocation location = (GsmCellLocation) tel.getCellLocation();
                cid = location.getCid();
                lac = location.getLac();
            }


        } else {
            GsmCellLocation location = (GsmCellLocation) tel.getCellLocation();
            cid = location.getCid();
            lac = location.getLac();
        }

        /** 将获得的数据放到结构体中 */
        stationInfo.setMCC(mcc);
        stationInfo.setMNC(mnc);
        stationInfo.setLAC(lac);
        stationInfo.setCID(cid);

        Log.i("定位的数据", "mcc:" + mcc + ",mnc:" + mnc + ",lac:" + lac + ",cid:" + cid);

        Log.e("定位的数据", "http://api.cellocation.com:83/cell/?mcc=" + mcc + "&mnc=" + mnc + "&lac=" + lac + "&ci=" + cid + "&output=json");


        //        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        CellLocation cel = tel.getCellLocation();
//        int nPhoneType = tel.getPhoneType();
////移动联通 GsmCellLocation
//        if (nPhoneType == 2 && cel instanceof GsmCellLocation) {
//            GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
//            int nGSMCID = gsmCellLocation.getCid();
//            if (nGSMCID > 0) {
//                if (nGSMCID != 65535) {
//                    this.cell = nGSMCID;
//                    this.lac = gsmCellLocation.getLac();
//                }
//            }
//        }
    }

    /**
     * 基站信息
     * MCC: 国家代码：中国代码 460
     * MNC，移动设备网络代码（Mobile Network Code，MNC），中国移动 = 00，中国联通 = 01, 中国电信 = 03 05 11
     * LAC，Location Area Code，位置区域码；
     * CID，Cell Identity，基站编号，是个16位的数据（范围是0到65535）。
     */
    public static class StationInfo {
        private int MCC;
        private int MNC;
        private int LAC;
        private int CID;

        public int getMCC() {
            return MCC;
        }

        public void setMCC(int MCC) {
            this.MCC = MCC;
        }

        public int getMNC() {
            return MNC;
        }

        public void setMNC(int MNC) {
            this.MNC = MNC;
        }

        public int getLAC() {
            return LAC;
        }

        public void setLAC(int LAC) {
            this.LAC = LAC;
        }

        public int getCID() {
            return CID;
        }

        public void setCID(int CID) {
            this.CID = CID;
        }

    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        Log.d("try", result ? "有SIM卡" : "无SIM卡");
        return result;
    }

    public boolean hasSIMCard(Context context) {

        TelephonyManager manager = (TelephonyManager) context

                .getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务

        if (Build.VERSION.SDK_INT > 28) {
            String simSer = manager.getSimSerialNumber();

            if (simSer == null || simSer.equals("")) {
                return false;
            } else {
                return true;
            }
        } else {
            String imsi = manager.getSubscriberId(); // 取出IMSI

            System.out.println("取出IMSI" + imsi);

            if (imsi == null || imsi.length() <= 0) {

                System.out.println("请确认sim卡是否插入或者sim卡暂时不可用！");

//APIFailSimBuyJNI();
                return false;
            } else {

                System.out.println("有SIM卡");
                return true;
            }
        }


    }

    /**
     * 通过此方法请求定位信息
     */
    public void request() {
        String url = "http://api.cellocation.com:83/cell/?mcc=%1$s&mnc=%2$s&lac=%3$s&ci=%4$s&output=json";

        StationInfo info = getCellInfo();

//        if (info == null) {
//            listener.onFailed();
//            return;
//        }
        Log.i("q", info.toString());
        url = String.format(url, info.getMCC(), info.getMNC(), info.getLAC(), info.getCID());
        //通过网络请求获取经纬度和详细位置


//        getLocation(url);
    }

    /**
     * 获取基站信息
     */
    @SuppressLint("MissingPermission")
    private StationInfo getCellInfo() {
        StationInfo stationInfo = new StationInfo();

        /** 调用API获取基站信息 */
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        if (!hasSimCard(mContext)) { //判断有没有sim卡，如果没有安装sim卡下面则会异常
            Toast.makeText(mContext, "请安装sim卡", Toast.LENGTH_LONG).show();
            return null;
        }
        String operator = telephonyManager.getNetworkOperator();
        Log.e("operator", "operator=" + operator);
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));

        //参考http://www.cellocation.com/interfac/
        int cid = 0;
        int lac = 0;

        List<CellInfo> infoLists = telephonyManager.getAllCellInfo();
        for (CellInfo info : infoLists) {
            CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
            CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
            CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
            int strength = cellSignalStrengthCdma.getCdmaDbm();
            int cid2 = cellIdentityCdma.getBasestationId();
            // 处理 strength和id数据
        }
        if (mnc == 11 || mnc == 03 || mnc == 05) {  //03 05 11 为电信CDMA
//            CdmaCellLocation location = (CdmaCellLocation) telephonyManager.getCellLocation();
            try {
                CellLocation cel = telephonyManager.getCellLocation();
                CdmaCellLocation location = (CdmaCellLocation) cel;
                //这里的值可根据接口需要的参数获取
                cid = location.getBaseStationId();
                lac = location.getNetworkId();
                mnc = location.getSystemId();
            } catch (Exception e) {
                GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
                cid = location.getCid();
                lac = location.getLac();
            }


        } else {
            GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
            cid = location.getCid();
            lac = location.getLac();
        }

        /** 将获得的数据放到结构体中 */
        stationInfo.setMCC(mcc);
        stationInfo.setMNC(mnc);
        stationInfo.setLAC(lac);
        stationInfo.setCID(cid);

        return stationInfo;
    }
//
//    int sid;
//    int nid;
//    int bid;
//
//    private void chinaTelecom() {
//        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.e("电信", "-----------------》checkSelfPermission");
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        CellLocation cel = tel.getCellLocation();
//        int nPhoneType = tel.getPhoneType();
//        Log.e("电信", "-----------------》nPhoneType:" + nPhoneType);
////电信   CdmaCellLocation
//        if (nPhoneType == 2 && cel instanceof CdmaCellLocation) {
//            Log.e("电信", "-----------------》电信");
//            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
//            sid = cdmaCellLocation.getSystemId();
//            nid = cdmaCellLocation.getNetworkId();
//            bid = cdmaCellLocation.getBaseStationId();
//            Log.e("电信", "sid -----------------》" + sid);
//            Log.e("电信", "nid -----------------》" + nid);
//            Log.e("电信", "bid -----------------》" + bid);
//        }
//    }

//
//    /**
//     * 基站信息结构体
//     */
//    public class SCell {
//        public int MCC;
//        public int MNC;
//        public int LAC;
//        public int CID;
//    }
//
//    /**
//     * 经纬度信息结构体
//     */
//    public class SItude {
//        public String latitude = "none";
//        public String longitude = "none";
//        public String address = "none";
//        public String radis = "none";
//    }
//
//
//    /**
//     * 获取基站信息
//     *
//     * @throws Exception
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public SCell getCellInfo() {
//        SCell cell = new SCell();
//
//        /** 调用API获取基站信息 */
//        TelephonyManager mTelNet = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            Log.e("Error", "遇到权限问题");
//        }
//        GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
////        CellInfo location = (CellInfo) mTelNet.getAllCellInfo();
//        if (location == null){
//            Log.e("Error", "获取基站信息失败");
////            Toast.makeText(TestLocationActivity.this, "获取基站信息失败", Toast.LENGTH_SHORT).show();
//        }
//
//
//        String operator = mTelNet.getNetworkOperator();
//        int mcc = Integer.parseInt(operator.substring(0, 3));
//        int mnc = Integer.parseInt(operator.substring(3));
//        int cid = location.getCid();
//        int lac = location.getLac();
//
//        /** 将获得的数据放到结构体中 */
//        cell.MCC = mcc;
//        cell.MNC = mnc;
//        cell.LAC = lac;
//        cell.CID = cid;
//
//        return cell;
//    }

//    /**
//     * 获取基站地理位置
//     *
//     * @throws Exception
//     */
//    public String getWebData(String domain) {
//
//        String resultString = "";
//
//        HttpClient client = new DefaultHttpClient();
//        /** 采用GET方法 */
//        HttpGet get = new HttpGet(domain);
//
//        try {
//            /** 发起GET请求并获得返回数据 */
//            HttpResponse response = client.execute(get);
//            HttpEntity entity = response.getEntity();
//            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
//            StringBuffer strBuff = new StringBuffer();
//            String result = null;
//            while ((result = buffReader.readLine()) != null) {
//                strBuff.append(result);
//            }
//            resultString = strBuff.toString();
//        } catch (Exception e) {
//            //throw new Exception("获取物理位置出现错误:" + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            get.abort();
//            client = null;
//        }
//
//        return resultString;
//
//    }

//    public SItude getLocation(String url) {
//        String resultString = "";
//        SItude situde = new SItude();
//        resultString = getWebData(url);
//
//        /** 解析基站位置 */
//        String errcode = "";
//        String[] arr = resultString.split(",");
//        errcode = arr[0];
//
//        if (errcode.equals("0")) {
//            situde.latitude = arr[1];
//            situde.longitude = arr[2];
//            situde.radis = arr[3];
//            situde.address = arr[4].replace("", "");
//
//        }
//        return situde;
//    }
//
//    private Handler mHandler = new Handler() {
//        @Override
//        // 重写handleMessage()方法，此方法在UI线程运行
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            try {
//                switch (msg.what) {
//                    case MSG_SUCCESS:
//                        String cellInfo = "基站信息" + "\nMCC：" + cell.MCC + "\nMNC：" + cell.MNC + "\nLAC：" + cell.LAC + "\nCID：" + cell.CID;
//                        cellText.setText(cellInfo);
//
//                        SItude s = (SItude) msg.obj;
//                        String loc = "基站坐标" + "\nlon：" + s.longitude + "\nlat：" + s.latitude + "\n地址：" + s.address;
//                        locationText.setText(loc);
//
//                        Toast.makeText(TestLocationActivity.this, "解析基站位置成功", Toast.LENGTH_SHORT).show();
//                        break;
//                    case MSG_FAILURE:
//                        Toast.makeText(TestLocationActivity.this, "网络解析基站位置失败", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//
//    private Runnable runnable = new Runnable() {
//        // 重写run()方法，此方法在新的线程中运行
//
//        @RequiresApi(api = Build.VERSION_CODES.M)
//        @Override
//        public void run() {
//            /** 获取基站 */
//            cell = getCellInfo();
//
//            /** 查询基站地理位置 */
//            String url = "http://api.cellocation.com:81/cell/?mcc=" + cell.MCC + "&mnc=" + cell.MNC + "&lac=" + cell.LAC + "&ci=" + cell.CID + "&output=csv";
//            Log.v("url", url);
//
//            SItude situde = new SItude();
////            try {
////                situde = getLocation(url);
////            } catch (Exception e) {
////                mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
////                e.printStackTrace();
////            } finally {
////                mHandler.obtainMessage(MSG_SUCCESS, situde).sendToTarget();
////            }
//        }
//    };


    @Override
    public void notifyByThemeChanged() {

    }

    private void getLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.v("TAG", "定位方式GPS");
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Log.v("TAG", "定位方式Network");
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("TAG", "1");
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "1----1");
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
            } else {
                Log.e("TAG", "1----2");
                //3.获取上次的位置，一般第一次运行，此值为null
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location != null) {
                    Log.e("TAG", "1----2:1");
                    Toast.makeText(this, location.getLongitude() + " " +
                            location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                    Log.v("TAG", "获取上次的位置-经纬度：" + location.getLongitude() + "   " + location.getLatitude());
                    getAddress(location);

                } else {
                    Log.e("TAG", "1----2:2");
                    //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                    locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
                }
            }
        } else {
            Log.e("TAG", "2");
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                Toast.makeText(this, location.getLongitude() + " " +
                        location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                Log.v("TAG", "获取上次的位置-经纬度：" + location.getLongitude() + "   " + location.getLatitude());
                getAddress(location);

            } else {
                //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            }
        }
    }

    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
            Log.e("TAG", "onProviderEnabled：" + provider);
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            Log.e("TAG", "onProviderDisabled：" + provider);
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //如果位置发生变化，重新显示地理位置经纬度
                Toast.makeText(TestLocationActivity.this, location.getLongitude() + " " +
                        location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "监视地理位置变化-经纬度：" + location.getLongitude() + "   " + location.getLatitude());
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TestLocationActivity.this, "申请权限", Toast.LENGTH_LONG).show();
                    try {
                        List<String> providers = locationManager.getProviders(true);
                        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                            //如果是Network
                            locationProvider = LocationManager.NETWORK_PROVIDER;

                        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                            //如果是GPS
                            locationProvider = LocationManager.GPS_PROVIDER;
                        }
                        Location location = locationManager.getLastKnownLocation(locationProvider);
                        if (location != null) {
                            Toast.makeText(TestLocationActivity.this, location.getLongitude() + " " +
                                    location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                            Log.v("TAG", "获取上次的位置-经纬度：" + location.getLongitude() + "   " + location.getLatitude());
                        } else {
                            // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                            locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
                        }

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(TestLocationActivity.this, "缺少权限", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    //获取地址信息:城市、街道等信息
    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(TestLocationActivity.this, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                Toast.makeText(TestLocationActivity.this, "获取地址信息：" + result.toString(), Toast.LENGTH_LONG).show();
                Log.v("TAG", "获取地址信息：" + result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

}

