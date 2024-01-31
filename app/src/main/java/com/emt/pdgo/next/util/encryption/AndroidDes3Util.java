package com.emt.pdgo.next.util.encryption;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * des工具类
 *
 * @author chenjh
 * @date 2018/11/28 10:16
 */
public class AndroidDes3Util {

    // 密钥 长度不得小于24
    private final static String secretKey = "123456789012345678901234" ;
    // 向量 可有可无 终端后台也要约定
    private final static String iv = "01234567" ;
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8" ;


    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     */
    public static String encode(String plainText) {
        try {
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
            return Base64.encodeToString(encryptData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }


    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        byte[] decryptData = null;
        Key deskey = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

            decryptData = cipher.doFinal(Base64.decode(encryptText, Base64.DEFAULT));

            return new String(decryptData, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decryptData, encoding);
    }


//    /**
//     * 3DES加密
//     *
//     * @param plainText 普通文本
//     * @return
//     * @throws Exception
//     */
//    public static String encode(String plainText) {
//        try {
//
//            Key deskey = null;
//            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
//            SecretKeyFactory keyfactory = SecretKeyFactory
//                    .getInstance("desede");
//            deskey = keyfactory.generateSecret(spec);
//
//            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
//            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
//            return Base64Util.encode(encryptData);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return plainText;
//
//    }
//
//    /**
//     * 3DES解密
//     *
//     * @param encryptText 加密文本
//     * @return
//     * @throws Exception
//     */
//    public static String decode(String encryptText) throws Exception {
//
//        byte[] decryptData = null;
//        Key deskey = null;
//
//        try {
//            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
//            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
//            deskey = keyfactory.generateSecret(spec);
//            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
//            decryptData = cipher.doFinal(Base64Util.decode(encryptText));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return new String(decryptData, encoding);
//    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    public static byte[] md5BackTyte(String basestring) {

        byte[] bytes = null;

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }
}
