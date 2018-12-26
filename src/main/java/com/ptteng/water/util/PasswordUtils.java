package com.ptteng.water.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * TODO
 * by   2018/9/30 14:31
 */
@Slf4j

public class PasswordUtils {
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public PasswordUtils() {
    }

    public static String encode(String inputString) {
        return encodeByMD5(inputString);
    }

    public static boolean authenticatePassword(String password, String inputString) {
        return password.equals(encodeByMD5(inputString));
    }

    private static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] results = md.digest(originString.getBytes());
                String resultString = byteArrayToHexString(results);
                return resultString.toLowerCase();
            } catch (Exception var4) {
                log.info("{}",var4);
            }
        }

        return null;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = 256 + b;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
