package com.netx.utils.security.rejectflush;

import com.netx.utils.cache.Cache;
import com.netx.utils.datastructures.Tuple;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 *
 * @author Runshine
 * @since 2015-7-17
 * @version 1.0.0
 *
 */
public class CookieRegulation implements Regulation {
    private static final String keyPref = "RejectFlushInterceptorCK";               //计数器
    private static final String keyBlackPref = "RejectFlushBlackInterceptorCK";     //黑名单
    private static final String cookieName = "___ck_reg__";

    private String domain;

    private Cache cache;

    private int limit;
    private int seconds;
    private int forbiddenSeconds;

    private String id = ":";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = "@" + id + ":";
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getForbiddenSeconds() {
        return forbiddenSeconds;
    }

    public void setForbiddenSeconds(int forbiddenSeconds) {
        this.forbiddenSeconds = forbiddenSeconds;
    }

    @Override
    public RejectProcedureResult isReject(HttpServletRequest request, HttpServletResponse response) {
        Tuple<Boolean, String> checkCookie = checkCookie(request, response);
        RejectProcedureResult rp = new RejectProcedureResult();
        if(!checkCookie.left()) {
            rp.setReject(false);
            rp.setContinuate(true);
            return rp;
        }

        String cookieValue = checkCookie.right();

        String blackKey = keyBlackPref + id + cookieValue;
        Object get = cache.get(blackKey);
        if(get != null) {
            rp.setReject(true);
            rp.setContinuate(false);
            rp.setBlacklistKey(blackKey);
            return rp;
        }

        String key = keyPref + id + cookieValue;
        String remoteAddr = RequestHelper.getRemoteAddr(request);

        RejectModel rm = getCacheRejectModel(key);
        if(rm == null) {
            initRejectModel(key, remoteAddr, seconds);
            rp.setReject(false);
            rp.setContinuate(true);
            return rp;
        } else {
            int count = rm.getCount();
            if(count >= limit) {
                cache.put(blackKey, 1, forbiddenSeconds);
                delRejectModel(key);
                rp.setReject(true);
                rp.setContinuate(false);
                rp.setBlacklistKey(blackKey);
                return rp;
            } else {
                cache.incrBy(key, "count");
                rp.setReject(false);
                rp.setContinuate(false);
                return rp;
            }
        }
    }

    private Tuple<Boolean, String> checkCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = RequestHelper.getCookie(request, cookieName);
        if(cookie == null) {
            String uuid = UUID.randomUUID().toString();
            Cookie newCookie = new Cookie(cookieName, uuid);
            newCookie.setDomain(domain);
            newCookie.setPath("/");
            newCookie.setHttpOnly(true);
            newCookie.setMaxAge(3600 * 24 * 60);
            response.addCookie(newCookie);

            return new Tuple<>(Boolean.FALSE, uuid);
        } else {
            String value = cookie.getValue();
            return new Tuple<>(Boolean.TRUE, value);
        }
    }

    private void initRejectModel(String key, String ip, int seconds) {
        long firstTime = System.currentTimeMillis();

        cache.hset(key, "ip", ip, seconds);  //只需要第一次设定超时时间
        cache.hset(key, "firstTime", firstTime);
        cache.incrBy(key, "count");
    }

    private void delRejectModel(String key) {
        cache.del(key, "ip");
        cache.del(key, "firstTime");
        cache.del(key, "count");
    }

    private RejectModel getCacheRejectModel(String key) {
        String ip = (String)cache.get(key, "ip");
        Object firstTime = cache.get(key, "firstTime");
        if(ip != null && firstTime != null) {
            Object count = cache.incrBy(key, "count");
            RejectModel rm = new RejectModel();
            rm.setCount((int)(long)count);
            rm.setFirstTime((long)firstTime);
            rm.setIp(ip);
            return rm;
        } else {
            return null;
        }
    }
}
