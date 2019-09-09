package com.ty.shiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ty.constant.SecurityConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    private static JwtUtil jwtUtil;

    private static final String UNKNOWN = "unknow";

    @PostConstruct
    public void init() {
        jwtUtil = this;
        jwtUtil.jwtConfig = this.jwtConfig;
    }

    /**
     * 校验token是否正确
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtUtil.jwtConfig.getSecretKey());
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的私有信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(SecurityConsts.USERNAME).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取指定claim
     * @param token
     * @param claimName
     * @return
     */
    public static String getClaim(String token, String claimName){
        return JWT.decode(token).getClaim(claimName).asString();
    }

    /**
     * 生成签名,指定时间后过期
     * @param username 用户名
     * @param currentTimeMillis 时间戳
     * @return token
     */
    public static String sign(String username,  String currentTimeMillis) {
        //过期时间毫秒
        Date date = new Date(System.currentTimeMillis() + jwtUtil.jwtConfig.getTokenExpireTime()*60*1000);
        Algorithm algorithm = Algorithm.HMAC256(jwtUtil.jwtConfig.getSecretKey());
        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        return JWT.create()
                .withClaim(SecurityConsts.USERNAME, username)
                .withClaim(SecurityConsts.CURRENT_TIME_MILLIS, currentTimeMillis)
                .withExpiresAt(date)//过期时间
                .sign(algorithm);
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
        return ip;
    }

}