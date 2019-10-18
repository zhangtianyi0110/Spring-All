package com.zty.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zty.pojo.ResponseData;
import com.zty.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: JWTVerificationExceptionHandler
 * @Description: JWT异常处理
 * @author zhangtainyi
 * @date 2019/9/6 9:57
 *
 */
@ControllerAdvice
public class JWTVerificationExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseData handleAuthenticationException(HttpServletRequest request,
            HttpServletResponse response, Exception e) throws IOException {

        //系统异常打印
        log.error(e.getMessage());
        return ResponseUtil.failure(401,e.getMessage());
    }
}
