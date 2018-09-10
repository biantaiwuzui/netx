package com.netx.utils.security.rejectflush;

import com.netx.utils.cache.Cache;
import com.netx.utils.datastructures.Tuple;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Runshine
 * @since 2015-7-17
 * @version 1.0.0
 *
 */
public class SessionRegulation implements Regulation {
    private static final String keyPref = "RejectFlushInterceptorSS";               //计数器
    private static final String keyBlackPref = "RejectFlushBlackInterceptorSS";     //黑名单

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
        Tuple<Boolean, String> checkSession = checkSession(request, response);
        RejectProcedureResult rp = new RejectProcedureResult();
        if(!checkSession.left()) {
            rp.setReject(false);
            rp.setContinuate(true);
            return rp;
        }

        String sessionId = checkSession.right();

        String blackKey = keyBlackPref + id + sessionId;
        Object get = cache.get(blackKey);
        if(get != null) {
            rp.setReject(true);
            rp.setContinuate(false);
            rp.setBlacklistKey(blackKey);
            return rp;
        }

        String key = keyPref + id + sessionId;
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

    private Tuple<Boolean, String> checkSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);  //对KariquSession无效，KariquSession无论true、false都是建立session
        if(session == null) {
            session = request.getSession(true);
            return new Tuple<>(Boolean.FALSE, session.getId());
        } else {
            return new Tuple<>(Boolean.TRUE, session.getId());
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
