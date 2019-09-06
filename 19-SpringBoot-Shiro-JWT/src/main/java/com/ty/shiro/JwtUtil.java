package com.ty.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ty.config.JwtProperties;
import com.ty.constant.SecurityConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    private static JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        jwtUtil = this;
        jwtUtil.jwtProperties = this.jwtProperties;
    }

    /**
     * 校验token是否正确
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtUtil.jwtProperties.getSecretKey());
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
        Date date = new Date(System.currentTimeMillis() + jwtUtil.jwtProperties.getTokenExpireTime()*60*1000);
        Algorithm algorithm = Algorithm.HMAC256(jwtUtil.jwtProperties.getSecretKey());
        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        return JWT.create()
                .withClaim(SecurityConsts.USERNAME, username)
                .withClaim(SecurityConsts.CURRENT_TIME_MILLIS, currentTimeMillis)
                .withExpiresAt(date)//过期时间
                .sign(algorithm);
    }
}