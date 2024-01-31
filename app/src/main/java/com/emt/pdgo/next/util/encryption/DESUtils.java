package com.emt.pdgo.next.util.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;


public class DESUtils {

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String KEY_PARAMETER = "Oyea";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "UTF-16LE";

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param data     待加密字符串
     * @return 加密后内容
     */
    public static String encrypt( String data) {
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(KEY_PARAMETER);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(KEY_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            return new String(Base64Util.encode(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES解密字符串
     *

     * @param data     待解密字符串
     * @return 解密后内容
     */
    public static String decrypt(String data) {

        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(KEY_PARAMETER);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(KEY_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64Util.decode(data)), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public static void main(String[] args) {
        //201810215→ B9E8DC06F3700D80803089A6775E9105 ckd-wxae981c69f64904f7
        System.err.println("decrypt = " + DESUtils.decrypt("uQU4DiJqQakkU9rUQKukkZBl/9ugVkSSppwEcwwWRPZLPRqyNj6FWkSOKgkfzkD1"));
        System.err.println("encrypt = " + DESUtils.encrypt("201810215"));
    }

}