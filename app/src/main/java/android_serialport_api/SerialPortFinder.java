//package android_serialport_api;
//
//
//
//import com.emt.pdgo.next.util.logger.Logger;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.LineNumberReader;
//import java.util.Iterator;
//import java.util.Vector;
//
///**
// * Google官方代码
// * 获取硬件地址的类
// *
// * @author chenjh
// * @date 2018/11/16 10:15
// */
//public class SerialPortFinder {
//
//    public class Driver {
//        public Driver(String name, String root) {
//            mDriverName = name;
//            mDeviceRoot = root;
//        }
//
//        private String mDriverName;
//        private String mDeviceRoot;
//        Vector<File> mDevices = null;
//
//        public Vector<File> getDevices() {
//            if (mDevices == null) {
//                mDevices = new Vector<File>();
//                File dev = new File("/dev");
//                File[] files = dev.listFiles();
//                int i;
//                for (i = 0; i < files.length; i++) {
//                    if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
//                        Logger.d(TAG, "Found new device: " + files[i]);
//                        mDevices.add(files[i]);
//                    }
//                }
//            }
//            return mDevices;
//        }
//
//        public String getName() {
//            return mDriverName;
//        }
//    }
//
//    private static final String TAG = "SerialPort";
//
//    private Vector<Driver> mDrivers = null;
//
//    Vector<Driver> getDrivers() throws IOException {
//        if (mDrivers == null) {
//            mDrivers = new Vector<Driver>();
//            LineNumberReader r = new LineNumberReader(new FileReader("/proc/tty/drivers"));
//            String l;
//            while ((l = r.readLine()) != null) {
//                // Issue 3:
//                // Since driver name may contain spaces, we do not extract driver name with split()
//                String drivername = l.substring(0, 0x15).trim();
//                String[] w = l.split(" +");
//                if ((w.length >= 5) && (w[w.length - 1].equals("serial"))) {
//                    Logger.d(TAG, "Found new driver " + drivername + " on " + w[w.length - 4]);
//                    mDrivers.add(new Driver(drivername, w[w.length - 4]));
//                }
//            }
//            r.close();
//        }
//        return mDrivers;
//    }
//
//    public String[] getAllDevices() {
//        Vector<String> devices = new Vector<String>();
//        // Parse each driver
//        Iterator<Driver> itdriv;
//        try {
//            itdriv = getDrivers().iterator();
//            while (itdriv.hasNext()) {
//                Driver driver = itdriv.next();
//                Iterator<File> itdev = driver.getDevices().iterator();
//                while (itdev.hasNext()) {
//                    String device = itdev.next().getName();
//                    String value = String.format("%s (%s)", device, driver.getName());
//                    devices.add(value);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return devices.toArray(new String[devices.size()]);
//    }
//
//
//    /**
//     * 获取在设备目录下的，所有串口的具体物理地址，并且存入到数组里面。
//     * 能够获取所有的串口的具体地址，然后进行选择你需要的物理地址就行了。一般来说的话，串口地址为: /dev/ttyS2、/dev/ttyS1、/dev/ttyS0
//     *
//     * @author chenjh
//     * @date 2018/11/16 10:17
//     */
//    public String[] getAllDevicesPath() {
//        Vector<String> devices = new Vector<String>();
//        // Parse each driver
//        Iterator<Driver> itdriv;
//        try {
//            itdriv = getDrivers().iterator();
//            while (itdriv.hasNext()) {
//                Driver driver = itdriv.next();
//                Iterator<File> itdev = driver.getDevices().iterator();
//                while (itdev.hasNext()) {
//                    String device = itdev.next().getAbsolutePath();
//                    devices.add(device);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return devices.toArray(new String[devices.size()]);
//    }
//}
