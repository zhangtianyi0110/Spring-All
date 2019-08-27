package com.ty.handler;

import com.ty.bean.ResponseBean;
import com.ty.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MyException.class)
    public ResponseBean MyExcepitonHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        //系统异常
        log.error(e.getMessage());
        e.printStackTrace();
        return new ResponseBean(500,e.getMessage(),null);
    }
}
