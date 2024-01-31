package com.emt.pdgo.next.ui.activity.param;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bw.yml.YModem;
import com.bw.yml.YModemListener;
import com.emt.pdgo.next.MyApplication;
import com.emt.pdgo.next.common.config.CommandDataHelper;
import com.emt.pdgo.next.common.config.RxBusCodeConfig;
import com.emt.pdgo.next.data.serial.receive.ReceiveDeviceBean;
import com.emt.pdgo.next.rxlibrary.rxbus.Subscribe;
import com.emt.pdgo.next.ui.base.BaseActivity;
import com.emt.pdgo.next.ui.view.StateButton;
import com.emt.pdgo.next.util.FileCutUtils;
import com.emt.pdgo.next.util.FileUtils;
import com.emt.pdgo.next.util.helper.JsonHelper;
import com.pdp.rmmit.pdp.R;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.utils.ByteUtil;

public class FirmwareUpgradeActivity extends BaseActivity {

    private SerialHelper serialHelper;

    @BindView(R.id.selectFileBtn)
    Button selectFileBtn;

    @BindView(R.id.fileName)
    TextView fileName;

    @BindView(R.id.receiveMsg)
    TextView receiveMsg;

    @BindView(R.id.btn_submit)
    StateButton btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String hexStr;
    @Override
    public void initAllViews() {
        setContentView(R.layout.activity_firmware_upgrade);
        ButterKnife.bind(this);
        initHeadTitleBar("固件升级","删除");
        compositeDisposable = new CompositeDisposable();
        serialHelper = new SerialHelper("/dev/ttyS0", 115200) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onDataReceived(ComBean comBean) {
                String hex = ByteUtil.ByteArrToHex(comBean.bRec).toUpperCase(Locale.ROOT);
                Log.e("数据接收","hex--"+hex);
                runOnUiThread(()->{
                    hexStr =  hexStr + "\n" + hex;
                    receiveMsg.setText(hexStr);
                });
                if (hex.startsWith("F181")) { // 请求更新固件
                    if (hardNum != 0) {
                        String s = "F102" + hardSize +
                                "00" + Integer.toHexString(hardNum);
                        Disposable mainDisposable = Observable.timer(3, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    sendHex(s + getCrc32(s)); // 请求擦除FLASH
                                });
                        compositeDisposable.add(mainDisposable);
                    }
                } else if (hex.startsWith("F182")) { // 请求擦除FLASH
                    // 上位机发送升级固件包
                    String s = "F1030000"+ByteUtil.ByteArrToHex(Objects.requireNonNull(FileUtils.getBytesByFile(cutUtils.getLittlefilelist().get(0))));
                    sendHex(s+getCrc32(s));
                } else if (hex.startsWith("F183")) { // 发送升级固件包
                    if (num <= cutUtils.getLittlefilelist().size()) {
                        delay(num);
                    } else {
                        cutUtils.deleteLittlelist();
                    }
                }
            }
        };
        try {
            serialHelper.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int num = 1;
    private CompositeDisposable compositeDisposable;
    private void delay(int i) {
        String size = "";
        if (Integer.toHexString(i).length() == 1) {
            size = "000"+Integer.toHexString(i);
        } else if (Integer.toHexString(i).length() == 2) {
            size = "00"+Integer.toHexString(i);
        } else if (Integer.toHexString(i).length() == 3) {
            size = "0"+Integer.toHexString(i);
        } else if (Integer.toHexString(i).length() == 4) {
            size = ""+Integer.toHexString(i);
        }
//        cutUtils.deleteSingleFile(cutUtils.getLittlefilelist().get(i).getPath());
        String s = "F103"+size+ByteUtil.ByteArrToHex(Objects.requireNonNull(FileUtils.getBytesByFile(cutUtils.getLittlefilelist().get(i))));
        Disposable mainDisposable = Observable.timer(150, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    serialHelper.sendHex(s+getCrc32(s));
//                    startYModem(cutUtils.getLittlefilelist().get(i).getPath(),cutUtils.getLittlefilelist().get(i).getName());
                    num ++;
                });
        compositeDisposable.add(mainDisposable);
    }

    private void requestFirmwareUpdate() {
        cutUtils.deleteLittlelist();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"选择bin文件"),1);
    }

    private FileCutUtils cutUtils;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            if (data.getData() != null) {
                Log.e("选择文件夹","路径--"+ data.getData().toString());
                String s = data.getData().toString()
                        .replace("content://com.android.externalstorage.documents/document", "")
                        .replace("%3A","/");
                Log.e("选择文件夹","真实路径--"+ s);
                if (!s.endsWith(".bin")) {
                    toastMessage("请选择正确的升级文件");
                } else {
                    cut(new File("/storage" + s),s.replace("/2800-6224/",""));
                }
            }
        }
    }
    private int hard; // 固件大小
    private String hardSize; // 固件长度
    private int hardNum; // 分包数
    private void requestUpdate(String size, int num) {
        String h = "F101"+size+"00"+Integer.toHexString(num);
        String s = h+getCrc32(h);
        Log.e("升级",h+"--数据--"+getCrc32(h));
        Log.e("升级","发送数据--"+s);
        serialHelper.sendHex(s);
    }

    private void cut(File file, String filename) {
//        hardNum = cutUtils.getSplitFile(new File("/storage" + s), 2048);
//                    hardNum = cutUtils.getSplitFile("/storage" + s, 2048);
        hardNum = cutUtils.getSplitFile(file, 2048);
        fileName.setText(filename);
        try {
            hard = (int) file.length();
            Log.e("选择文件夹", "文件length--" + file.length());

            Log.e("选择文件夹", "文件大小--" + file.length() +
                    "分包数" + hardNum);
            cutUtils.getLittlefilelist().size();
            if (Integer.toHexString(hard).length() == 1) {
                hardSize = "0000000" + Integer.toHexString(hard);
            } else if (Integer.toHexString(hard).length() == 2) {
                hardSize = "000000" + Integer.toHexString(hard);
            } else if (Integer.toHexString(hard).length() == 3) {
                hardSize = "00000" + Integer.toHexString(hard);
            } else if (Integer.toHexString(hard).length() == 4) {
                hardSize = "0000" + Integer.toHexString(hard);
            } else if (Integer.toHexString(hard).length() == 5) {
                hardSize = "000" + Integer.toHexString(hard);
            } else if (Integer.toHexString(hard).length() == 6) {
                hardSize = "00" + Integer.toHexString(hard);
            } else if (Integer.toHexString(hard).length() == 7) {
                hardSize = "0" + Integer.toHexString(hard);
            } else if (Integer.toHexString(hard).length() == 8) {
                hardSize = Integer.toHexString(hard);
            }
            Log.e("选择文件夹","cutUtils--"+ cutUtils.getLittlefilelist().get(1).getName()+"--AbsolutePath--"
                    +cutUtils.getLittlefilelist().get(1).getAbsolutePath()+"--Parent--"
                    +cutUtils.getLittlefilelist().get(1).getParent()+"--Path--"
                    +cutUtils.getLittlefilelist().get(1).getPath());
            requestUpdate(hardSize, hardNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCrc32(String hex) {
        try {
            if (hex == null) {
                return "";
            }
            CRC32 crc32 = new CRC32();
            crc32.update(ByteUtil.HexToByteArr(hex));
            return String.format(Locale.US,"%08X", crc32.getValue());
        } catch (Exception e) {
            Log.e("getCrc32", e.getLocalizedMessage());
        }
        return "";
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
        selectFileBtn.setOnClickListener(v ->{
            requestFirmwareUpdate();
        });
        btn_submit.setOnClickListener(
                v -> {cutUtils.deleteLittlelist();}
        );
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
        cutUtils = new FileCutUtils();
    }

    public File getFileFromAssetsFile(String fileName){
        String path = "file:///android_asset/" + fileName;
        return new File(path);
    }

    private YModem yModem;
    private void startYModem(String filepath, String filename){
        Log.e("yModem","startYModem"+ filepath+","+filename);
        yModem = new YModem.Builder()
                .with(this)
                .filePath(filepath) //存放到手机的文件路径 stroge/0/.../xx.bin 这种路径
                .fileName(filename)
                .checkMd5("") //Md5可以写可以不写 看自己的通讯协议
                .sendSize(2048) //可以修改成你需要的大小
                .callback(new YModemListener() {
                    @Override
                    public void onDataReady(byte[] data) {
                        Log.e("yModem","onDataReady"+ ByteUtil.ByteArrToHex(data));
                    }

                    @Override
                    public void onProgress(int currentSent, int total) {
                        //进度条处理
                        Log.e("yModem","onProgress--"+currentSent+"--total--"+total);
                    }

                    @Override
                    public void onSuccess() {
                        //成功的显示
                    }

                    @Override
                    public void onFailed(String reason) {
                        Log.e("yModem","onFailed--"+reason);
                        yModem.stop();
                    }
                }).build();
        yModem.start();
//        yModem.onReceiveData();
    }



    @Override
    public void notifyByThemeChanged() {

    }

    @Override
    protected void onDestroy() {
        serialHelper.close();
        super.onDestroy();
    }
}