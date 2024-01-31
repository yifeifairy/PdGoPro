package com.emt.pdgo.next.util;



import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.TextUtils;

import com.emt.pdgo.next.util.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


/**
 *  
 * @ProjectName:
 * @Package:        com.emt.pdgo.next.util
 * @ClassName:      FileDatabaseContext
 * @Description:    支持数据库任意地址
 * @Author:         chenjh
 * @CreateDate:     2019/12/3 4:42 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/12/3 4:42 PM
 * @UpdateRemark:   更新内容
 * @Version:        1.0
 */

public class FileDatabaseContext extends ContextWrapper {
    private File file;
    private boolean isUseDefaultDB;
    private Context context;

    /**
     * @param base           上下文
     * @param file           文件存储目录
     * @param isUseDefaultDB 是否使用raw/initdb.db文件
     */
    public FileDatabaseContext(Context base, File file, boolean isUseDefaultDB) {
        super(base);
        this.file = file;
        this.context = base;
        this.isUseDefaultDB = isUseDefaultDB;
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    @Override
    public File getDatabasePath(String name) {
        //携带数据库
        if (isUseDefaultDB) {
            File tempFile = file;

            if (tempFile == null)
                tempFile = context.getDatabasePath(name).getParentFile();
            createInitDatabase(tempFile, name, context);
        }

        if (file == null)
            return context.getDatabasePath(name);

        // 判断目录是否存在，不存在则创建该目录
        if (!file.exists())
            file.mkdirs();

        File addressFile = new File(file, name);
        if (addressFile == null)
            addressFile = context.getDatabasePath(name);
        return addressFile;
    }

    public boolean createInitDatabase(File toFile, String name, Context context) {
        String path = toFile.getAbsolutePath();
        if (TextUtils.isEmpty(path))
            return false;

        // 判断数据文件是否存在，不存在的话使用打包的数据文件
        String toFileName = path + File.separator + name;

        File f = new File(toFileName);
        if (f == null || f.exists())
            return false;

        File dir = new File(path);
        if (dir == null) return false;
        if (!dir.exists())
            dir.mkdirs();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getResources().openRawResource(context.getResources().getIdentifier("initdb", "raw", context.getPackageName()));
            // 创建输出流
            fos = new FileOutputStream(toFileName);
            // 将数据输出
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            buffer = null;
        } catch (Exception ee) {
            Logger.e("createInitDatabase error",ee.toString());
        } finally {
            try {
                fos.close();
            } catch (Exception ee) {
            }
            try {
                is.close();
            } catch (Exception ee) {
            }
        }
        return true;
    }

    //重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    //Android 4.0会调用此方法获取数据库。
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}