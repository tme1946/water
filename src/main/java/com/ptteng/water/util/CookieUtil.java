package com.ptteng.water.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO cookie工具类
 * by   2018/9/30 15:45
 */

public class CookieUtil {

    public static final String UID = "UID";
    public Integer cookieMaxAge = 1209600;

    /**
     * TODO 找出存入cookie中的uid
     * by   2018/9/30 16:03
     **/
    public static Long getUid(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(UID)) {
                Long uid = TokenUtil.getUID(cookie.getValue());
                return uid;
            }
        }
        return 0L;
    }

    public static void loginout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(UID)) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }
        }

    }

}



