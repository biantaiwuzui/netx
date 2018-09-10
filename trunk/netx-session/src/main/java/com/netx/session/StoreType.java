package com.netx.session;


import com.netx.session.store.CacheStore;
import com.netx.session.store.CookieStore;

/**
 * 会话数据的保存类型 一种是保存在cookie中，一种是保存在缓存中
 *
 */
public enum StoreType {

    cookie {
        @Override
        SessionStore newSessionStore(NetxSessionServletRequest ecommerceRequest, NetxSessionServletResponse ecommerceResponse) {
            return new CookieStore(ecommerceRequest, ecommerceResponse);
        }
    },

    /** cache store */
    cache {
        @Override
        SessionStore newSessionStore(NetxSessionServletRequest ecommerceRequest, NetxSessionServletResponse ecommerceResponse) {
            return new CacheStore(ecommerceRequest, ecommerceResponse);
        }
    };

    abstract SessionStore newSessionStore(NetxSessionServletRequest ecommerceRequest, NetxSessionServletResponse ecommerceResponse);
}
