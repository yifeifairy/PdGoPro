package com.emt.pdgo.next.util;

import android.text.TextUtils;


import androidx.annotation.NonNull;

import com.emt.pdgo.next.util.cache.ACache;
import com.emt.pdgo.next.util.encryption.AndroidDes3Util;

import java.io.File;

/**
 * 当SD卡存在或者SD卡不可被移除的时候，存放在可被清理的， 否则就存放在sdk中，存放的数据可按设置的时间存放
 *
 * @author chenjh
 * @date 2018/11/28 10:31
 */
public class CacheUtils {


    private CacheUtils() {
    }

    private static ACache aCache;
    public static File file;

    static {
        file = EmtFileSdkUtil.getBaseDirFile();
        aCache = ACache.get(file);
    }

    private static class SingletonHolder {
        private static final CacheUtils INSTANCE = new CacheUtils();
    }

    public static CacheUtils getInstance() {

        return SingletonHolder.INSTANCE;
    }


    public ACache getACache() {
        return aCache;
    }

    public void putEncrypt(@NonNull String key, @NonNull String value) {

        String date = AndroidDes3Util.encode(value);
        aCache.put(key, date);
    }

    public String getStringNOEncrypt(@NonNull String key) {

        String dat = aCache.getAsString(key);
        if (!TextUtils.isEmpty(dat)) {
            try {
                return AndroidDes3Util.decode(dat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}