package com.netx.session.store;

import com.netx.session.SessionStore;
import com.netx.session.config.SessionConfigEntry;
import com.netx.session.NetxSessionServletRequest;
import com.netx.session.NetxSessionServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class CacheStore implements SessionStore {

    private static Log logger = LogFactory.getLog(CacheStore.class);

    private final NetxSessionServletRequest ecommerceRequest;

    private Map<String, Object> attributes;

    /** 标记缓存是否发生变化 */
    @SuppressWarnings("unused")
    private boolean cacheChanged;

    private boolean isAleadayReaded = false;

    public CacheStore(NetxSessionServletRequest ecommerceRequest, NetxSessionServletResponse ecommerceResponse) {
        this.ecommerceRequest = ecommerceRequest;
    }

    @Override
    public Object getAttribute(SessionConfigEntry sessionConfigEntry) {
        if (StringUtils.isNotBlank(ecommerceRequest.getSession().getId()) && !isAleadayReaded) {
            isAleadayReaded = readFromCache();
        }
        return attributes.get(sessionConfigEntry.getName());

    }

    @Override
    public void setAttribute(SessionConfigEntry sessionConfigEntry, Object value) {
        if (StringUtils.isNotBlank(ecommerceRequest.getSession().getId()) && !isAleadayReaded) {
            isAleadayReaded = readFromCache();
            if (!isAleadayReaded)
                return;
        }
        if (null == value) {
            attributes.remove(sessionConfigEntry.getName());
        }
        else {
            attributes.put(sessionConfigEntry.getName(), value);
        }
        cacheChanged = true;
    }

    @Override
    public void init() {
        this.attributes = new HashMap<String, Object>();
    }

    /**
     * 当session提交的时候，需要更新session有效期，如果发现还没有从缓存服务器读取session数据
     * 则先读取然后再写入，如果读取的时候不成功，那么就有可能会覆盖掉用户的session数据，因此 session 缓存服务器的可用性非常重要
     */
    @Override
    public void commit() {
        if (!isAleadayReaded) {
            final boolean isSuccess = readFromCache();
            if (!isSuccess) {
                logger.error("Session缓存服务器已经挂了，请速度检查！");
            }
        }
        writeToCache();

    }

    private void writeToCache() {
        final CacheService cacheService = CacheServiceFactory.getCacheService();
        cacheService.put(ecommerceRequest.getSession().getId(), attributes, ecommerceRequest.getSession().getMaxInactiveInterval());
    }

    private boolean readFromCache() {
        final CacheService cacheService = CacheServiceFactory.getCacheService();
        if (null == cacheService) {
            throw new IllegalStateException("cacheService没有获取到，请检查配置！");
        }
        try {
            @SuppressWarnings("unchecked")
            final Map<String, Object> cachedAttributes = (Map<String, Object>) cacheService.get(ecommerceRequest.getSession().getId());
            if (null != cachedAttributes && !cachedAttributes.isEmpty()) {
                attributes.putAll(cachedAttributes);
            }
            return true;
        }
        catch (Exception e) {
            logger.error("从缓存中读取session数据错误", e);
            return false;
        }
    }
}
