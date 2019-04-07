package com.eap.framework.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-3-4
 */
public class AESUtil {


    public final static String key = "CZqBPj4iu29DE&oWFbjdG^s1IRv%A0ivHJLpB4'ehcvMgXV'5Nr8-enO#QxtSyThfKU@Y_";

    public final static int[] position = {3, 12, 57, 65, 19, 43, 46, 40, 52,
            9, 26, 21, 44, 16, 18, 19};

    public static String encode(String in) {
        String hex = "";
        try {
            byte[] bytIn = in.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(
                    getPassword().getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] bytOut = cipher.doFinal(bytIn);
            hex = byte2hexString(bytOut);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hex;
    }

    public static String decode(String hex) {
        String rr = "";

        byte[] bytIn = hex2Bin(hex);
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(getPassword().getBytes(),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] bytOut = cipher.doFinal(bytIn);

            rr = new String(bytOut, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rr;
    }

    private static byte[] hex2Bin(String src) {
        if (src.length() < 1)
            return null;
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }

    private static String byte2hexString(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            strbuf.append(Integer.toString((buf[i] >> 4) & 0xf, 16)
                    + Integer.toString(buf[i] & 0xf, 16));
        }

        return strbuf.toString();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public static String getPassword() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < position.length; i++) {
            builder.append(key.charAt(position[i]));
        }
        return builder.toString();
    }

    public static void main(String[] args) throws IllegalBlockSizeException,
            NoSuchPaddingException,
            BadPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException {

        String hello="321456789564512422";
        String en_hello=AESUtil.encode(hello);
        System.out.println(AESUtil.encode(en_hello));
        System.out.println(AESUtil.decode(en_hello));
        System.out.println(AESUtil.encrypt(hello));
        String hello1=AESUtil.encrypt(hello);
        System.out.println(AESUtil.decrypt(hello1));
    }


    // ----------add by billjiang 用于person_offline的加解密-----
    public final static String AppKey="www.admineap.com";

    /**
     * 加密源数据
     *
     * @param input
     * @return
     */
    public static String encrypt(String input) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(AppKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes("UTF-8"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(Base64.encodeBase64(crypted));
    }

    /**
     * 解密源数据
     *
     * @param input
     * @return
     */
    public static String decrypt(String input) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(AppKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decodeBase64(input));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(output);
    }
}
