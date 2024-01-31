package com.emt.pdgo.next.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.util
 * @ClassName: FileUtils
 * @Description: 文件相关处理类
 * @Author: chenjh
 * @CreateDate: 2019/12/3 3:06 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/3 3:06 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class FileUtils {

    private static FileUtils instance;
    private static String APP_SDCARD_PATH;
    private static String APP_BASE_PATH = "";
    private static String APP_LOG_PATH = "logCrash/";
    private static String IMAGE_CACHE_PATH = "imageCache/";
    private static String HTTP_CACHE_PATH = "httpCache/";

    public static Context mContext;

    private FileUtils(Context context) {
        init(context);
    }

    public String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取指定文件大小
     * @param file
     * @return
     * @throws Exception 　　
     */
    public long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    //初始化文件夹
    public static void init(Context context) {
        mContext = context;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                APP_SDCARD_PATH = context.getExternalFilesDir("") +  File.separator;//获取跟目录
                APP_BASE_PATH = context.getExternalFilesDir("") +  File.separator;//获取跟目录
            }else{
                APP_SDCARD_PATH = Environment.getExternalStorageDirectory() +  File.separator;//获取跟目录
                APP_BASE_PATH = EmtFileSdkUtil.getBaseDir(context) + File.separator;
            }

        } else {
            APP_SDCARD_PATH = Environment.getRootDirectory() + "/";
            APP_BASE_PATH = EmtFileSdkUtil.getBaseDir(context) + File.separator;
        }

        File file = new File(APP_SDCARD_PATH + APP_BASE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(APP_SDCARD_PATH + APP_BASE_PATH + IMAGE_CACHE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(APP_SDCARD_PATH + APP_BASE_PATH + APP_LOG_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(APP_SDCARD_PATH + APP_BASE_PATH + HTTP_CACHE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static FileUtils getInstance(Context context) {
        if (instance == null) {
            instance = new FileUtils(context);
        }
        return instance;
    }

    public String getAppBasePath() {
        return APP_SDCARD_PATH + APP_BASE_PATH + APP_BASE_PATH;
    }

    public String getSdPath() {
        return APP_SDCARD_PATH;
    }

    public String getAppLogPath() {
        return APP_SDCARD_PATH + APP_BASE_PATH + APP_LOG_PATH;
    }

    public String getImageCachePath() {
        return APP_SDCARD_PATH + APP_BASE_PATH + IMAGE_CACHE_PATH;
    }

    public String getCrashLogPath() {
        return APP_SDCARD_PATH + APP_BASE_PATH + APP_LOG_PATH;
    }

    public String getHttpCachePath() {
        return APP_SDCARD_PATH + APP_BASE_PATH + HTTP_CACHE_PATH;
    }

//    getExternalCacheDir();
//    路径为：/storage/emulated/0/Android/data/<应用包名>/cache
//
//    getExternalFilesDir(null);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files
//
//    getExternalFilesDir("");
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files
//
//    getExternalFilesDir(“logs”);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/logs
//
//    getExternalCacheDir(“logs/zip”);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/logs/zip
//
//    getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/Music
//
//    getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/Podcasts
//
//    getExternalFilesDir(Environment.DIRECTORY_RINGTONES);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/Ringtones
//
//    getExternalFilesDir(Environment.DIRECTORY_ALARMS);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/Alarms
//
//    getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/Notifications
//
//    getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/Pictures
//
//    getExternalFilesDir(Environment.DIRECTORY_MOVIES);
//    路径为：/storage/emulated/0/Android/data/<应用包名>/files/Movies
//
//            getDataDir;
//    路径为：/data/user/0/<应用包名>
//
//    getFilesDir;
//    路径为：/data/user/0/<应用包名>/files
//
//            getCacheDir;
//    路径为：/data/user/0/<应用包名>/cache


//    //打开文件
//    public static void viewFile(final Context context, final String filePath) {
//        final Uri mUri;
//        String type = getMimeType(filePath);
//        //解决安卓7.0的问题
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            mUri = FileProvider.getUriForFile(context,
//                    BuildConfig.APPLICATION_ID + ".provider",
//                    new File(filePath));
//        } else {
//            mUri = Uri.fromFile(new File(filePath));
//        }
//        if (!TextUtils.isEmpty(type) && !TextUtils.equals(type, "*/*")) {
//            /* 设置intent的file与MimeType */
//            Intent intent = new Intent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(mUri, type);
//            context.startActivity(intent);
//        } else {
//            // unknown MimeType ，可在数组内增加需要的类型，与dialog内的内容一致,dialog根据情况设置风格。
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//            dialogBuilder.setTitle("选择文本类型");
//
//            CharSequence[] menuItemArray = new CharSequence[]{
//                    "文本",
//                    "图像"};
//
//
//            //CharSequence[] menuItemArray = new CharSequence[] {"文本", "图像"};
//            dialogBuilder.setItems(menuItemArray, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    String selectType = "*/*";
//                    switch (which) {
//                        case 0:
//                            selectType = "text/plain";
//                            break;
//
//                        case 1:
//                            selectType = "image/*";
//                            break;
//                    }
//                    Intent intent = new Intent();
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setDataAndType(mUri, selectType);
//                    context.startActivity(intent);
//                }
//            });
//            dialogBuilder.show();
//        }
//    }
//    //获取MimeType
//    private static String getMimeType(String filePath) {
//        int dotPosition = filePath.lastIndexOf('.');
//        if (dotPosition == -1)
//            return "*/*";
//
//        String ext = filePath.substring(dotPosition + 1, filePath.length()).toLowerCase();
//        String mimeType = MimeUtils.guessMimeTypeFromExtension(ext);
//
//        return mimeType != null ? mimeType : "*/*";
//    }

    // 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
    public static String createTmpDir(Context context) {
        String sampleDir = "baiduTTS";
        String tmpDir = APP_SDCARD_PATH + sampleDir;
        Log.e("fileUtils","APP_SDCARD_PATH:"+APP_SDCARD_PATH);
        Log.e("fileUtils","APP_BASE_PATH:"+APP_BASE_PATH);
        if (!makeDir(tmpDir)) {
            tmpDir = context.getExternalFilesDir(sampleDir).getAbsolutePath();
            Log.e("tmpDir","tmpDir:"+tmpDir);
            if (!makeDir(sampleDir)) {
                throw new RuntimeException("create model resources dir failed :" + tmpDir);
            }
        }
        return tmpDir;
    }


    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    public static void copyFromAssets(AssetManager assets, String source, String dest, boolean isCover)
            throws IOException {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }
        }
    }

    /**
     * 将文件转换成uri(支持7.0)
     *
     * @param mContext
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context mContext, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static void copy(File fromFile, File toFile) {
        if (!fromFile.exists()) {
            Log.e("复制文件", "文件不存在");
            return;
        }
        if (!fromFile.isFile()) {
            Log.e("复制文件", "不是个文件");
            return;
        }
        if (!fromFile.canRead()) {
            Log.e("复制文件", "文件不可读");
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists()) {
            toFile.delete();
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(fromFile);
            FileOutputStream fileOutputStream = new FileOutputStream(toFile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = fileInputStream.read(bytes)) > 0) {
                fileOutputStream.write(bytes,0,c);
            }
            fileInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getBytesByFile(File file) {
//        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
            byte[] b = new byte[2048];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
