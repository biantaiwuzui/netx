package com.netx.utils.security.rejectflush;

import com.netx.utils.cache.Cache;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Runshine
 * @since 2015-7-17
 * @version 1.0.0
 *
 */
public class IPRegulation implements Regulation {
    private static final String keyPref = "RejectFlushInterceptorIP";               //计数器
    private static final String keyBlackPref = "RejectFlushBlackInterceptorIP";     //黑名单

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
        this.seconds = seconds + 5;
    }

    public int getForbiddenSeconds() {
        return forbiddenSeconds;
    }

    public void setForbiddenSeconds(int forbiddenSeconds) {
        this.forbiddenSeconds = forbiddenSeconds;
    }

    @Override
    public RejectProcedureResult isReject(HttpServletRequest request, HttpServletResponse response) {
        String remoteAddr = RequestHelper.getRemoteAddr(request);
        RejectProcedureResult rp = new RejectProcedureResult();
        if(StringUtils.isBlank(remoteAddr)) {
            rp.setReject(true);
            rp.setContinuate(false);
            return rp;
        }

        String blackKey = keyBlackPref + id + remoteAddr;
        Object get = cache.get(blackKey);
        if(get != null) {
            rp.setReject(true);
            rp.setContinuate(false);
            rp.setBlacklistKey(blackKey);
            return rp;
        }

        String key = keyPref + id + remoteAddr;
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
