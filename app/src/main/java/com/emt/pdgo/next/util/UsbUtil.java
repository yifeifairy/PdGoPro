package com.emt.pdgo.next.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.storage.StorageManager;

import java.lang.reflect.Method;
import java.util.HashMap;

public class UsbUtil {

    public static boolean isUDiskConnected(Context context) {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        // 获取连接usb设备列表
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        for (String key : deviceList.keySet()) {
            UsbDevice usbDevice = deviceList.get(key);
            if (usbDevice != null) {
                for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
                    UsbInterface usbInterface = usbDevice.getInterface(i);
                    // 获取usb设备类型，判断当前连接的usb设备是否为存储设备（u盘或读卡器）
                    int interfaceClass = usbInterface.getInterfaceClass();
                    if (interfaceClass == UsbConstants.USB_CLASS_MASS_STORAGE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkUdiskExist(Context context) {
        try {
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method volumeState = (StorageManager.class).getMethod("getVolumeState", String.class);
            //U盘路径
            String path = get("vold.udisk_mount_path", "");
            //U盘状态
            String udiskState = (String) volumeState.invoke(sm, path);
            //是否有读写权限
            if (android.os.Environment.MEDIA_MOUNTED.equals(udiskState)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String get(String key, String def) {
        try {
            @SuppressLint("PrivateApi") Class<?> cls = Class.forName("android.os.SystemProperties");
            Method m = cls.getDeclaredMethod("get", String.class, String.class);
            m.setAccessible(true);
            return (String) (m.invoke(null, key, def));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
