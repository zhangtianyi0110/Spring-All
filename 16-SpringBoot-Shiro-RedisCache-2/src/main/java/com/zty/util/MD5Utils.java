package com.zty.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import java.lang.String;


public class MD5Utils {

    private static final String ALGORITH_NAME = "md5";//算法

    private static final int HASH_ITERATIONS = 1;//加盐迭代次数

    public static String encrypt(String password,String salt) {
        String newPassword = new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(salt), HASH_ITERATIONS).toHex();
        return newPassword;
    }

    public static String encrypt(String username, String password, String salt) {
        String newPassword = new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(username + salt),
                HASH_ITERATIONS).toHex();
        return newPassword;
    }
    public static void main(String[] args) {

        System.out.println(MD5Utils.encrypt("123456","zty"));
    }

}
