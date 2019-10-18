package com.zty.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: ShiroSessionListener
 * @Description: 配置session监听器
 * @author zhangtainyi
 * @date 2019/9/5 14:10
 *
 */
public class ShiroSessionListener implements SessionListener {

    /**
     * 统计在线人数
     * juc包下线程安全自增
     */
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 获取在线人数
     * @return
     */
    public AtomicInteger getSessionCount(){
        return sessionCount;
    }

    /**
     * 会话创建时候触发
     * @param session
     */
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();//会话创建，在线人数自增
    }

    /**
     * 会话停止时候触发
     * @param session
     */
    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();//会话退出，在线人数自减
    }

    /**
     * 会话过期触发
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();//会话过期，在线人数自减
    }
}
