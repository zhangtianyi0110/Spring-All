package com.ty.handler;

import com.ty.pojo.ResponseData;
import com.ty.util.ResponseUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ShiroExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ShiroExceptionHandler.class);

    /**
     * 对shiro认证抛出的异常统一处理
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseData handleAuthenticationException(HttpServletRequest request,
             HttpServletResponse response, Exception e) throws IOException {
        //系统异常打印
        logger.error(e.getMessage());
        if(e instanceof UnknownAccountException){
            return ResponseUtil.failure(401,"用户名不存在");
        }else if(e instanceof IncorrectCredentialsException){
            return ResponseUtil.failure(401,"用户名或密码错误");
        }
        return ResponseUtil.failure(401,"认证失败！");//异常回传信息
    }
}
