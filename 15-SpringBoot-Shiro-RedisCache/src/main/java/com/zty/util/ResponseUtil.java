package com.zty.util;

import com.zty.pojo.ResponseData;

public class ResponseUtil {



    //成功
    public static ResponseData success(){

        return new ResponseData(200,"响应成功",null);
    }
    public static ResponseData success(String msg){
        return new ResponseData(200,msg,null);
    }
    public static ResponseData success(Object data){
        return new ResponseData(200,"响应成功",data);
    }
    public static ResponseData success(String msg,Object data){
        return new ResponseData(200,msg,data);
    }
    public static ResponseData success(ResponseData responseData){
        return responseData;
    }
    //失败
    public static ResponseData failure(int code,String msg){
        return new ResponseData(code,msg,null);
    }

}
