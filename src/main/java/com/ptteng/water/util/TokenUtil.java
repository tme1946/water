package com.ptteng.water.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO token生成和解密的工具类
 * by  2018/9/29 18:38
 */

public class TokenUtil {
    // token密钥 自定义
    private  static final String SECRET = "hellojns";

    // token 过期时间
    private static final int calendarField = Calendar.DATE;
    private static final int calendarInterval = 10;


    /**
     * TODO jwt生成token
     * @param uid 用户id
     * @return string类型的token
     * by   2018/9/29 15:12
     **/
    public  static String createToken(Long uid){
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();
        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create()
                .withHeader(map) // header
                .withClaim("uid", null == uid ? null : uid.toString())
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature
        return token;
    }


    /**
     * TODO 解密token
     * @param token
     * @return
     * by   2018/9/29 15:17
     **/

    static  Map<String,Claim> verifyToken(String token){
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        }catch (Exception e){
            e.printStackTrace();
            // token 校验失败, 抛出Token验证非法异常
        }
        return jwt.getClaims();
    }

    /**
     * TODO 根据token获取uid
     * @param
     * @return
     * by   2018/9/29 15:21
     **/
    public static Long getUID(String token){
        Map<String,Claim> claimMap = verifyToken(token);
        Claim uid = claimMap.get("uid");
//       if (null == logntime_claim || StringUtils.isEmpty(logntime_claim.asString())) {
//            // token 校验失败, 抛出Token验证非法异常
//        }
        return Long.valueOf(uid.asString());
    }


}
