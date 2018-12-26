package com.ptteng.water.util;

/**
 * @Description TODO
 * @Author ruanshaonan
 * @Date 2018/10/4 2:33
 **/

public class DataUtil {

    public static boolean isNullOrEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else {
            return "".equals(obj);
        }
    }

    public static boolean isNotNullOrEmpty(Object obj) {
        return !isNullOrEmpty(obj);
    }

    public static boolean isZero(Long num) {
        if (null == num) {
            return true;
        } else {
            return num.intValue() == 0;
        }
    }
}
