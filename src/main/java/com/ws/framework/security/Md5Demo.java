package com.ws.framework.security;

import org.junit.Test;

import javax.crypto.Cipher;
import java.security.*;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Date: 2019/8/21 0021 12:42
 * 常见加密算法
 */
public class Md5Demo {

    public static void main(String[] args) throws Exception {

        byte[] hellWorlds = encodeMD5("HELL WORLD");
        String byte2Hex = byte2Hex(hellWorlds);

        byte[] bytes = hex2Bytes(byte2Hex);
        System.out.println(bytes.length);


    }

    /**
     * MD5
     */
    public static byte[] encodeMD5(String content) throws Exception {
        //获取加密对象
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //加密
        byte[] bytes = md5.digest(content.getBytes("UTF-8"));
        return bytes;
    }


    /**
     * SHA-1
     */
    public static byte[] encodeSHA(String content) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        byte[] bytes = digest.digest(content.getBytes("utf8"));

        return bytes;
    }


    /**
     * 二进制转16进制
     */
    public static String byte2Hex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        IntStream.range(0, bytes.length).forEach(p -> {

            byte b = bytes[p];
            //是否负数
            boolean negative = false;
            if (b < 0) negative = true;

            int abs = Math.abs(b);
            if (negative) abs = abs | 0x80;

            //负数会转成整数
            String temp = Integer.toHexString(abs & 0xFF);

            if (temp.length() == 1) {
                sb.append("0");
            }
            sb.append(temp.toLowerCase());
        });
        return sb.toString();
    }

    public static byte[] hex2Bytes(String hex) {
        byte[] bytes = new byte[hex.length() >> 1];

        for (int i = 0; i < hex.length(); i += 2) {
            String subStr = hex.substring(i, i + 2);

            boolean negative = false;

            int anInt = Integer.parseInt(subStr, 16);

            if (anInt > 127) negative = true;

            if (anInt == 128) {
                anInt = -128;
            } else if (negative) {
                anInt = 0 - (anInt & 0x7F);
            }

            bytes[i >> 1] = (byte) anInt;
        }


        return bytes;
    }

    /**
     * RSA 生成 获取公匙 和私匙
     */
    @Test
    public void rsa() throws Exception {
        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(512);
        KeyPair keyPair = rsa.generateKeyPair();
        PublicKey aPublic = keyPair.getPublic();
        PrivateKey aPrivate = keyPair.getPrivate();
        System.out.println(byte2Hex(aPublic.getEncoded()));
        System.out.println(byte2Hex(aPrivate.getEncoded()));

    }


}
