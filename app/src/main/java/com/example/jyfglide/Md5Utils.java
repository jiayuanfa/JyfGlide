package com.example.jyfglide;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5加密类
 */
public class Md5Utils {
    private static MessageDigest digest;
    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.d("jyf", "md5算法不支持");
        }
    }

    /**
     * MD5 加密方法
     * @param key
     * @return
     */
    public static String toMD5(String key) {
        if (digest == null) return String.valueOf(key.hashCode());

        // 更新字符
        digest.update(key.getBytes());
        // 获取最终的摘要
        return conver2HexString(digest.digest());
    }

    /**
     * 转为16进制字符串
     * @param bytes
     * @return
     */
    private static String conver2HexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
