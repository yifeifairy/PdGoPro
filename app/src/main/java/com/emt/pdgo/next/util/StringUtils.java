package com.emt.pdgo.next.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @ProjectName:
 * @Package:        com.emt.pdgo.next.util
 * @ClassName:      StringUtils
 * @Description:    java类作用描述
 * @Author:         chenjh
 * @CreateDate:     2019/12/3 3:00 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/12/3 3:00 PM
 * @UpdateRemark:   更新内容
 * @Version:        1.0
 */
public class StringUtils {


    /**
     * 判断str是否为空
     *
     * @param str String
     * @return boolean true:空;false:非空
     */
    public static boolean isStrEmpty(String str) {
        return ((str == null) || (str.trim().equals("")));
    }
    /**
     * 判断list是否为空
     * @param str String
     * @return boolean true:空;false:非空
     */
    public static boolean isListEmpty(List str) {
        return ((str == null) || (str.size()<=0));
    }

    //获取String
    public static String getString(String str) {
        if (str == null || "null".equalsIgnoreCase(str)) return "";
        return str;
    }
    //获取String
    public static String getString(Object str) {
        if (str == null || "null".equalsIgnoreCase(str.toString()))  return "";
        return str.toString();
    }

    /**
     * 十六进制转换字符串
     * @param hexStr str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr)
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * byte数组转为对应的16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            if(i != src.length-1){
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * byte数组转为对应的16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString2(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
//            if(i != src.length-1){
//                stringBuilder.append("");
//            }
        }
        return stringBuilder.toString();
    }

    /**
     * byte数组转为对应的16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src, int length) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            if(i != length-1){
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制编码字符串转为对应的二进制数组
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToBytes(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {

                baKeyword[i] = (byte) (Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return baKeyword;
    }

    /**
     * 十六进制转ascii
     *
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        // 49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    /**
     * 10进制字符串 转为16进制字符串
     * @param dec
     * @return
     */
    public static String convertDecToHexString(String dec) {
        Log.i("", dec);
        String str = Integer.toHexString(Integer.parseInt(dec));
        if(str.length()%2==1){
            str = "0"+str;
        }
        return str;
    }

    /**
     * 通过做异或运算,求出校验码
     * @param cmd
     * @return
     */
    public static String xor(String cmd)
    {
        if(cmd.length()%2!=0){
            cmd = "0"+cmd;
        }
        int result = 0;
        for (int i = 0; i < cmd.length()-1; i=i+2) {
            //System.out.println(cmd.substring(i,i+2));
            result ^= Integer.valueOf(cmd.substring(i, i + 2), 16);
            System.out.println("16-->"+ Integer.valueOf(cmd.substring(i, i + 2), 16));
            System.out.println("result:"+result);
        }
        return Integer.toHexString(result);
    }

    /**
     * 以"-"拆分字符串
     * @param str
     * @return
     */
    public static String[] splitString(String str){
        return str.split("-");
    }

    public static String takeCity(String str){
        String nstr = null;
        if(str!=null){
            nstr=str.substring(0, str.length()-1);
        }
        return nstr;
    }

    /**
     * 时间戳转为日期
     * @param datestr
     * @return
     */
    public static String getSimpDate(String datestr){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String date = sdf.format(new Date(Long.parseLong(datestr) ));
        return date;
    }
    /**
     * 时间戳转为日期
     * @param smdateint
     * @return
     */
    public static String getSimpDate(long smdateint){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String date = sdf.format(new Date(smdateint));
        System.out.println(date);
        return date;
    }


    /**
     * String 转 ASCII
     * @param value
     * @return
     */
    public static String stringToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                sbu.append((int)chars[i]).append(",");
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }


    /**
     * ASCII  转 String
     * @param value
     * @return
     */
    public static String asciiToString(String value)
    {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    /**
     * 16进制转换成为string类型字符串
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 隐藏手机号中间4位
     * @param phone
     * @return
     */
    public static String hidenPhone(String phone)
    {
        String hidenPhone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        return hidenPhone;
    }

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 方法一：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun1(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    /**
     * 方法二：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun2(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    /**
     * 方法三：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    /***
     * Has been tested
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(InputStream in) throws IOException {
        byte[] temp = new byte[in.available()];
        byte[] result = new byte[0];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            byte[] readBytes = new byte[size];
            System.arraycopy(temp, 0, readBytes, 0, size);
            result = mergeArray(result,readBytes);
        }
        return result;
    }

    public static byte[] readBytes3(InputStream in) throws IOException {
        BufferedInputStream bufin = new BufferedInputStream(in);
        int buffSize = 1024;
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);

        // System.out.println("Available bytes:" + in.available());

        byte[] temp = new byte[buffSize];
        int size = 0;
        while ((size = bufin.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        bufin.close();

        byte[] content = out.toByteArray();
        return content;
    }

    /***
     * 合并字节数组
     * @param a
     * @return
     */
    public static byte[] mergeArray(byte[]... a) {
        // 合并完之后数组的总长度
        int index = 0;
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + a[i].length;
        }
        byte[] result = new byte[sum];
        for (int i = 0; i < a.length; i++) {
            int lengthOne = a[i].length;
            if(lengthOne==0){
                continue;
            }
            // 拷贝数组
            System.arraycopy(a[i], 0, result, index, lengthOne);
            index = index + lengthOne;
        }
        return result;
    }

}
