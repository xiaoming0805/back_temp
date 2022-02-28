package com.cennavi.system.common;

import com.cennavi.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class TokenCache {

    private final static String TOKEN_PREFIX = "token:%s";

    /**
     * 7天有效期
     */
    private final static Integer EXPIRED_TIME = 7;

    private final static Integer APP_EXPIRED_TIME = 30;


    @Autowired
    private RedisUtil redisUtil;

    public String genKey(String tokenKey) {
        return String.format(TOKEN_PREFIX, tokenKey);
    }

    public void setToken(String tokenKey, String user, Integer terminalType) {
        if (terminalType == 2 || terminalType == 3) {
            redisUtil.setEx(genKey(tokenKey), user,
                    APP_EXPIRED_TIME, TimeUnit.DAYS);
        } else {
            redisUtil.setEx(genKey(tokenKey), user,
                    EXPIRED_TIME, TimeUnit.DAYS);
        }
    }

    public String getToken(String token) {
        String tokenKey = genKey(token);
        String result = redisUtil.get(tokenKey);
        Long expireDay = redisUtil.getExpire(tokenKey, TimeUnit.DAYS);
        if (expireDay < 3) {
            redisUtil.expire(tokenKey, EXPIRED_TIME, TimeUnit.DAYS);
        }
        return result;
    }

    public void rename(String oldToken, String newToken) {
        redisUtil.rename(genKey(oldToken), genKey(newToken));
    }

    public void delToken(String token) {
        redisUtil.delete(genKey(token));
    }
}
