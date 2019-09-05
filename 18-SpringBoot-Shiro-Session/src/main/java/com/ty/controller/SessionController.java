package com.ty.controller;

import com.ty.pojo.ResponseData;
import com.ty.pojo.UserOnline;
import com.ty.service.SessionService;
import com.ty.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/** 
 * @ClassName: SessionController
 * @Description: TODO(用一句话描述)
 * @author zhangtainyi
 * @date 2019/9/5 15:12
 *
 */
@Controller
@RequestMapping("/online")
public class SessionController {
    @Autowired
    SessionService sessionService;

    @RequestMapping("index")
    public String online() {
        return "online";
    }

    @ResponseBody
    @RequestMapping("list")
    public List<UserOnline> list() {
        return sessionService.list();
    }

    @ResponseBody
    @RequestMapping("forceLogout")
    public ResponseData forceLogout(String id) {
        try {
            sessionService.forceLogout(id);
            return ResponseUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.failure(500,"踢出用户失败");
        }
    }
}