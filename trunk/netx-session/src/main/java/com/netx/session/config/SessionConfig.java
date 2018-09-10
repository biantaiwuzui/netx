package com.netx.session.config;

import javax.servlet.FilterConfig;
import java.util.Collection;

public interface SessionConfig {

    SessionConfigEntry getSessionConfigEntry(String name);

    void init(FilterConfig filterConfig);

    Collection<String> getAttributeNames();

    Collection<SessionConfigEntry> getAllSessionConfigAttributes();

    Collection<SessionConfigEntry> getCombinedConfigEntries(String combineKey);

    /**
     * gain the global config infomation,for example the encrypt key and so on
     *
     * @param key
     * @return
     */
    String getGlobalConfigInfo(String key);
}
