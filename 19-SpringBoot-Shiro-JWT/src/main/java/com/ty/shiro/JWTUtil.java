package com.ty.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.annotation.Resource;
import java.util.Date;

public class JWTUtil {

    @Resource
    private static JwtProperties jwtProperties;

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码当作算法密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
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
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,30分钟后过期
     * @param username 用户名
     * @param secret 用户的密码
     * @return token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis()+jwtProperties.getTokenExpireTime());
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)//过期时间
                .sign(algorithm);
    }
}