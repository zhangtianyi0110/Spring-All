package com.ty.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ty.pojo.ResponseData;
import com.ty.util.ResponseUtil;
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
    Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseData handleJWTVerificationException(HttpServletRequest request,
                HttpServletResponse response, Exception e) throws IOException {
        log.error(e.getMessage());
        //系统异常打印
        return ResponseUtil.failure(401,e.getMessage());//异常回传信息
    }
}
