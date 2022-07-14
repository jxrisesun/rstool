package com.jxrisesun.rstool.shiro.redis;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

public class RedisSessionFactory implements SessionFactory {

	@Override
    public Session createSession(SessionContext initData) {
        if (initData != null) {
            String host = initData.getHost();
            if (host != null) {
                return new RedisSession(host);
            }
        }
        return new RedisSession();
    }

}
