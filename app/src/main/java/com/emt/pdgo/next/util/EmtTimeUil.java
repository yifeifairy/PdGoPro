package com.emt.pdgo.next.util;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.util
 * @ClassName: EmtTimeUil
 * @Description: 时间工具类
 * @Author: chenjh
 * @CreateDate: 2020/1/13 11:15 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/13 11:15 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class EmtTimeUil {
    /**
     * 日期类型 *
     */
    public static final String yyyyMMDD = "yyyy-MM-dd";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String HHmmss = "HH:mm:ss";
    public static final String hhmmss = "HH:mm:ss";
    public static final String LOCALE_DATE_FORMAT = "yyyy年M月d日 HH:mm:ss";
    public static final String DB_DATA_FORMAT = "yyyy-MM-DD HH:mm:ss";
    public static final String NEWS_ITEM_DATE_FORMAT = "hh:mm M月d日 yyyy";
    public static final String WeiBo_ITEM_DATE_FORMAT = "EEE MMM d HH:mm:ss Z yyyy";
    public static final String MMddHHmmss = "MM-dd HH:mm";
    public static final String HHmm = "HH:mm";
    public static final String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    public static final String yyyyMMDD_ZN = "yyyy年MM月dd日";


    private static final int seconds_of_1minute = 1 * 60;
    public static final int seconds_of_3minute = 3 * 60;
    private static final int seconds_of_30minutes = 30 * 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_1day = 24 * 60 * 60;
    private static final int seconds_of_2day = 2 * 24 * 60 * 60;
    private static final int seconds_of_7day = 7 * 24 * 60 * 60;
    private static final int seconds_of_2days = seconds_of_1day * 3;
    private static final int seconds_of_30days = seconds_of_1day * 30;
    private static final int seconds_of_6months = seconds_of_30days * 6;
    private static final int seconds_of_1year = seconds_of_30days * 12;

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append(date.getTime());
        return sb.toString();
    }

    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String getTimeWithYmd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return dateFormat.format(date);
    }

    public static String getTimeWithYmd1() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 获取当前的时间戳
     * @return
     */
    public static String getCurrentTime2() {
        //获取当前的毫秒值
        long time = System.currentTimeMillis();
        //将毫秒值转换为String类型数据
        //返回出去
        return String.valueOf(time);
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static long getCurrentFlontTime() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentTime(String pattern) {
        try {
            Date date = new Date();
            return dateToString(date, pattern);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateToString(Date date, String pattern)
            throws Exception {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date stringToDate(String dateStr, String pattern)
            throws Exception {
        return new SimpleDateFormat(pattern).parse(dateStr);
    }

    /**
     * 将Date类型转换为日期字符串
     *
     * @param date Date对象
     * @param type 需要的日期格式
     * @return 按照需求格式的日期字符串
     */
    public static String formatDate(Date date, String type) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(type);
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将日期字符串转换为Date类型
     *
     * @param dateStr 日期字符串
     * @param type    日期字符串格式
     * @return Date对象
     */
    public static Date parseDate(String dateStr, String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = null; // date类型转成String
        try {
            strTime = dateToString(date, formatType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strTime;
    }


    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = null; // 把date类型的时间转换为string
        try {
            sDateTime = dateToString(dateOld, formatType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = null; // 把String类型转换为Date类型
        try {
            date = stringToDate(sDateTime, formatType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static long parseLong(String strTime, String formatType)
            throws ParseException {
        Date date = parseDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            return dateToLong(date);
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 得到年
     *
     * @param date Date对象
     * @return 年
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 得到月
     *
     * @param date Date对象
     * @return 月
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;

    }

    /**
     * 得到日
     *
     * @param date Date对象
     * @return 日
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 转换日期 将日期转为今天, 昨天, 前天, XXXX-XX-XX, ...
     *
     * @param time 时间
     * @return 当前日期转换为更容易理解的方式
     */
    public static String translateDate(Long time) {
        long oneDay = 24 * 60 * 60 * 1000;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        long todayStartTime = today.getTimeInMillis();

        if (time >= todayStartTime && time < todayStartTime + oneDay) { // today
            return "今天";
        } else if (time >= todayStartTime - oneDay && time < todayStartTime) { // yesterday
            return "昨天";
        } else if (time >= todayStartTime - oneDay * 2 && time < todayStartTime - oneDay) { // the day before yesterday
            return "前天";
        } else if (time > todayStartTime + oneDay) { // future
            return "将来某一天";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(time);
            return dateFormat.format(date);
        }
    }

    /**
     * 转换日期 转换为更为人性化的时间
     *
     * @param time 时间
     * @return
     */
    public static String translateDate(long time, long curTime) {
        long oneDay = 24 * 60 * 60;
        Calendar today = Calendar.getInstance();    //今天
        today.setTimeInMillis(curTime * 1000);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStartTime = today.getTimeInMillis() / 1000;
        if (time >= todayStartTime) {
            long d = curTime - time;
            if (d <= 60) {
                return "1分钟前";
            } else if (d <= 60 * 60) {
                long m = d / 60;
                if (m <= 0) {
                    m = 1;
                }
                return m + "分钟前";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("今天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            }
        } else {
            if (time < todayStartTime && time > todayStartTime - oneDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("昨天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {

                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            } else if (time < todayStartTime - oneDay && time > todayStartTime - 2 * oneDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("前天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            }
        }
    }


    //根据日期取得星期几
    public static String getWeek(Long dateTime) {
        Date date = new Date(dateTime);
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }


    /**
     * @return timtPoint距离现在经过的时间，分为
     * 刚刚，1-29分钟前，半小时前，1-23小时前，1-14天前，半个月前，1-5个月前，半年前，1-xxx年前
     */
    public static String getTimeElapse(long createTime) {
        long nowTime = new Date().getTime() / 1000;
        //createTime是发表文章的时间
        long oldTime = createTime / 1000;
        //elapsedTime是发表和现在的间隔时间
        long elapsedTime = nowTime - oldTime;
        if (elapsedTime < seconds_of_1minute) {
            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {
            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {
            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {
            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime <= seconds_of_7day) {
            return elapsedTime / seconds_of_1day + "天前";
        } else {

            try {
                return EmtTimeUil.longToString(createTime, EmtTimeUil.MMddHHmmss);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    /**
     * 时间显示
     *
     * @param time
     * @return
     */
    public static String formatDateTime(String time) {

        try {
            long timLong = Long.parseLong(time);
            time = longToString(timLong, yyyyMMddHHmm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time == null || "".equals(time)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance(); // 今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        // Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance(); // 昨天
        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        Calendar yesterday2 = Calendar.getInstance(); // 昨天
        yesterday2.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday2.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday2.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 2);
        yesterday2.set(Calendar.HOUR_OF_DAY, 0);
        yesterday2.set(Calendar.MINUTE, 0);
        yesterday2.set(Calendar.SECOND, 0);

        Calendar yesterday3 = Calendar.getInstance(); // 昨天
        yesterday3.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday3.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday3.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 3);
        yesterday3.set(Calendar.HOUR_OF_DAY, 0);
        yesterday3.set(Calendar.MINUTE, 0);
        yesterday3.set(Calendar.SECOND, 0);


        Calendar yesterday4 = Calendar.getInstance(); // 昨天
        yesterday4.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday4.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday4.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 4);
        yesterday4.set(Calendar.HOUR_OF_DAY, 0);
        yesterday4.set(Calendar.MINUTE, 0);
        yesterday4.set(Calendar.SECOND, 0);

        Calendar yesterday5 = Calendar.getInstance(); // 昨天
        yesterday5.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday5.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday5.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 5);
        yesterday5.set(Calendar.HOUR_OF_DAY, 0);
        yesterday5.set(Calendar.MINUTE, 0);
        yesterday5.set(Calendar.SECOND, 0);

        Calendar yesterday6 = Calendar.getInstance(); // 昨天
        yesterday6.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday6.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday6.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 6);
        yesterday6.set(Calendar.HOUR_OF_DAY, 0);
        yesterday6.set(Calendar.MINUTE, 0);
        yesterday6.set(Calendar.SECOND, 0);

        Calendar yesterday7 = Calendar.getInstance(); // 昨天
        yesterday7.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday7.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday7.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 7);
        yesterday7.set(Calendar.HOUR_OF_DAY, 0);
        yesterday7.set(Calendar.MINUTE, 0);
        yesterday7.set(Calendar.SECOND, 0);

        if (date == null) {
            return "";
        }
        current.setTime(date);
        if (current.after(today)) {
            return time.split(" ")[1];
        } else if (current.before(today) && current.after(yesterday)) {
            return "昨天 ";
        } else if (current.before(today) && current.after(yesterday2)) {
            return getWeekOfDate(date);
        } else if (current.before(today) && current.after(yesterday3)) {
            return getWeekOfDate(date);
        } else if (current.before(today) && current.after(yesterday4)) {
            return getWeekOfDate(date);
        } else if (current.before(today) && current.after(yesterday5)) {
            return getWeekOfDate(date);
        } else if (current.before(today) && current.after(yesterday6)) {
            return getWeekOfDate(date);
        } else if (current.before(today) && current.after(yesterday7)) {
            return getWeekOfDate(date);
        } else {
            int index = time.indexOf("-") + 1;
            return time.substring(index, time.length());
        }
    }

    /**
     * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }


    // 将字符串转为时间戳
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    // 将字符串转为时间戳
    public static String getTime(Date mDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss);
        return sdf.format(mDate);
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));


        return re_StrTime;
    }

    // 将时间戳转为字符串 HH:mm
    public static String getStrTime2(String cc_time) {
        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));


        return re_StrTime;
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time, String sdfString) {
        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat(sdfString);
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));


        return re_StrTime;
    }


    public static String strToYMDHM(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter1.format(strtodate);
        return dateString;
    }

    // 将时间戳转为字符串
    public static String getYMDHMTime(String cc_time) {
        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time));


        return re_StrTime;
    }

    //根据秒数转化为时分秒   00:00:00
    public static String getTime2(int second) {
        if (second < 10) {
            return "00分0" + second+"秒";
        }
        if (second < 60) {
            return "00分" + second+"秒";
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0时" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }
            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour  + ":" + minute + ":0" + second;
            }
            return "0" + hour  + ":" + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour  + ":" + minute + ":0" + second;
        }
        return hour  + ":" + minute + ":" + second;
    }

    //根据秒数转化为时分秒   00:00:00
    public static String getTime(int second) {
        if (second < 10) {
            return "00:0" + second;
        }
        if (second < 60) {
            return "00:" + second;
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }
            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour  + ":" + minute + ":0" + second;
            }
            return "0" + hour  + ":" + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour  + ":" + minute + ":0" + second;
        }
        return hour  + ":" + minute + ":" + second;
    }


}
