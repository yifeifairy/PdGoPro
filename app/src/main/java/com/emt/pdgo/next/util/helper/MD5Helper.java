package com.emt.pdgo.next.util.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5
 * @author chenjh
 * @date 2018/11/28 10:17
 *
 */
public class MD5Helper {
    // MD5変換
    public static String Md5(String str) {

        if (str != null && !str.equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//                byte[] md5Byte = md5.digest();
                byte[] md5Byte = md5.digest(str.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : md5Byte) {
                    sb.append(HEX[(int) (b & 0xff) / 16]);
                    sb.append(HEX[(int) (b & 0xff) % 16]);
                }
                str = sb.toString();
            } catch (NoSuchAlgorithmException e) {
            } catch (Exception e) {
            }
        }
        return str;
    }

    /**
     * 通过MD5算法加密；
     * @param pwd
     */
    public static String encoder(String pwd) {
        try {

            //1指定算法类型；
            MessageDigest digest =MessageDigest.getInstance("MD5");
            //2将需要加密的字符串转换成byte数组；
            //注意我们后台编码时Unicode，此处编码根据后台编码走
            byte[] bs = digest.digest(pwd.getBytes("UTF-16LE"));
            //3通过遍历bs 生成32位的字符串；

            //最后字符串有个拼接的过程；
            StringBuffer sb =new StringBuffer();
            for (byte b : bs) {
                int i=b&0xff; //int 类型的i 是4个字节占32位；
                //int 类型的i转换成16进制字符；
                String hexString = Integer.toHexString(i);
//                  if (hexString.length()<2) {//补零的过程，因为生成的时候有的是一位有的是两位所以需要有个补零的过程；
//                      hexString="0"+hexString;
//                  }
                sb.append(hexString);
            }

            return sb.toString() ;

        } catch ( Exception e) {//找不到指定算法的错误；
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }


    }

}