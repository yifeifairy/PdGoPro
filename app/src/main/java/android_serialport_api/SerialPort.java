//package android_serialport_api;
//
//
//
//import com.emt.pdgo.next.util.logger.Logger;
//
//import java.io.File;
//import java.io.FileDescriptor;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
///**
// * Google官方代码
// * 获取串口的类
// * 此类的作用为，JNI的调用，用来加载.so文件的
// * 获取串口输入输出流
// *
// * @author chenjh
// * @date 2018/11/16 10:01
// */
//public class SerialPort {
//
//    private static final String TAG = "SerialPort";
//
//    /*
//     * Do not remove or rename the field mFd: it is used by native method close();
//     */
//    private FileDescriptor mFd;
//    private FileInputStream mFileInputStream;
//    private FileOutputStream mFileOutputStream;
//
//    /**
//     * 构造函数
//     *
//     * @param device   要操作的文件对象
//     * @param baudrate 波特率
//     * @param flags    文件操作的标志
//     * @throws SecurityException
//     * @throws IOException
//     */
//    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
//
//        /* Check access permission */ //检查权限
//        if (!device.canRead() || !device.canWrite()) {
//            try {
//                /* Missing read/write permission, trying to chmod the file */  //如果丢失权限，就再获取权限
//                Process su;
//                su = Runtime.getRuntime().exec("/system/bin/su");
//                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
//                        + "exit\n";
//                Logger.e(TAG, "cmd:" + cmd);
//                //写命令
//                su.getOutputStream().write(cmd.getBytes());
//                if ((su.waitFor() != 0) || !device.canRead()
//                        || !device.canWrite()) {
//                    Logger.e(TAG, "SerialPort() SecurityException:");
//                    throw new SecurityException();
//                }
//            } catch (Exception e) {
////                e.printStackTrace();
////                Logger.e(TAG,"SerialPort() Exception:"+e.getMessage());
//                throw new SecurityException();
//            }
//        }
//
//        //开启串口，调用jni 的open方法,传入物理地址、波特率、flags值
//        mFd = open(device.getAbsolutePath(), baudrate, flags);
//        if (mFd == null) {
//            Logger.e(TAG, "native open returns null");
//            throw new IOException();
//        }
//
//        // 输入流，也就是获取从单片机或者传感器，通过串口传入到Android主板的IO数据（使用的时候，执行Read方法）
//        mFileInputStream = new FileInputStream(mFd);
//        //输出流，Android将需要传输的数据发送到单片机或者传感器（使用的时候，执行Write方法）
//        mFileOutputStream = new FileOutputStream(mFd);
//    }
//
//    //获取串口的输入流
//    public InputStream getInputStream() {
//        return mFileInputStream;
//    }
//
//    //获取串口的输出流
//    public OutputStream getOutputStream() {
//        return mFileOutputStream;
//    }
//
//
//    /**
//     * JNI调用，开启串口
//     *
//     * @param path     设备的绝对路径,为串口的物理地址，一般硬件工程师都会告诉你的例如ttyS0、ttyS1等，或者通过SerialPortFinder类去寻找得到可用的串口地址。
//     * @param baudrate 波特率，与外接设备一致
//     * @param flags    标志
//     * @return
//     */
//    private native static FileDescriptor open(String path, int baudrate, int flags);
//
//    // 关闭串口
//    public native void close();
//
//    static {
//        //加载库文件.so文件
//        System.loadLibrary("serial_port");
//    }
//}
