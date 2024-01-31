package com.emt.pdgo.next.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    private static SpUtil spUtil;

    public static SpUtil getInstance(Context context) {
        if (spUtil == null) {
            spUtil = new SpUtil();
        }
        sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        return spUtil;
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sp.getString(key,"");
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key,false);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLongValue(String key) {
        return sp.getLong(key,0L);
    }

}
