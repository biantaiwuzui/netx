package com.netx.session;


import com.netx.session.config.SessionConfigEntry;


public interface SessionStore {

    /**
     * 查询属性
     *
     * @param sessionConfigEntry
     *
     * @return
     */
    Object getAttribute(SessionConfigEntry sessionConfigEntry);

    /**
     * 设置属性
     *
     * @param sessionConfigEntry
     * @param value
     */
    void setAttribute(SessionConfigEntry sessionConfigEntry, Object value);

    /** 提交SessionStore中存储的数据 */
    void commit();

    /** 初始化sessionStore */
    void init();
}
