package com.emt.pdgo.next.util.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHelper {


    /**
     * Json字符串转Class
     * @param strJsonData
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T jsonToClass(String strJsonData, Class<T> cls)
    {
        Gson gson = new Gson();
        // 空字符串转换
        return gson.fromJson(strJsonData, cls);
    }

    /**
     * json转list
     * @param strJsonData
     * @param cls
     * @return
     */
    public static List<?> jsonToList(String strJsonData, Class<?> cls)
    {
        Gson gson = new Gson();
        List<?> list = gson.fromJson(strJsonData, new TypeToken<List<?>>(){}.getType());

        if (null == list)
            return null;

        return list;
    }

    public static int getCharacterPosition(String compileString,String string,int index){
        //这里是获取"{"或者"}"符号的位置
        Matcher slashMatcher = Pattern.compile(compileString).matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"{"或者"}"符号出现的位置
            if(mIdx == index){
                break;
            }
        }
        return slashMatcher.start();
    }


}
