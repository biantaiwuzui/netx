package com.netx.session;

import com.netx.session.config.SessionConfig;
import com.netx.session.config.SessionConfigFactory;
import com.netx.session.store.CacheStore;
import com.netx.session.store.CookieStore;
import com.netx.session.util.SessionConstants;
import com.netx.session.util.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


public class NetxSessionFilter implements Filter {

    private static Log logger = LogFactory.getLog(NetxSessionFilter.class);

    private SessionStoreFactory sessionStoreFactory;

    /** session框架配置信息 */
    private SessionConfig sessionConfig;

    private FilterConfig filterConfig;

    private static final String DEFAULT_CONFIG_FILE_NAME = "session-config.xml";

    /** 用于配置是否需要buffer响应,设置为buffer以后等最后刷新到输出流，如果不设置 则利用容器或者MVC框架的机制去刷新输出流 */
    private boolean needResponseBuffered;

    /** 配置不需要进行session过滤的URL */
    private String[] forbiddenUrlSuffixes;

    /** 是否需要登录检测 */
    private boolean needLoginCheck;

    /** 默认的session超期时间 */
    private int sessionExpireTime = 1800;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init");
        this.filterConfig = filterConfig;
        initSessionStoreFactory(filterConfig);
        initSessionConfig(filterConfig);
    }

    private void initSessionConfig(FilterConfig filterConfig) {
        String configFileName = filterConfig.getInitParameter("configFileName");
        if (StringUtils.isEmpty(configFileName)) {
            configFileName = DEFAULT_CONFIG_FILE_NAME;
        }
        sessionConfig = SessionConfigFactory.readSessionConfig(configFileName);
        sessionConfig.init(filterConfig);
        needResponseBuffered = "true".equalsIgnoreCase(filterConfig.getInitParameter("needResponseBuffered"));
        if (StringUtils.isNotBlank(filterConfig.getInitParameter("forbiddenUrlSuffixes"))) {
            forbiddenUrlSuffixes = filterConfig.getInitParameter("forbiddenUrlSuffixes").split(",");
        }
        needLoginCheck = "true".equalsIgnoreCase(filterConfig.getInitParameter("needLoginCheck"));
        final String sessionExpireTime = filterConfig.getInitParameter("sessionExpireTime");
        if (StringUtils.isNotBlank(sessionExpireTime)) {
            try {
                this.sessionExpireTime = Integer.parseInt(sessionExpireTime);
            }
            catch (NumberFormatException e) {
                logger.error("session框架超期时间设置有误，请确认是否是数字类型", e);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        NetxSessionServletRequest ecommerceRequest = new NetxSessionServletRequest((HttpServletRequest) request);
        NetxSessionServletResponse ecommerceResponse = new NetxSessionServletResponse((HttpServletResponse) response);
        if (letitgo(request, response, chain, ecommerceRequest))
            return;
        if (needResponseBuffered) {
            if (logger.isDebugEnabled()) {
                logger.debug("com.netx.session framework responseBuffered is on");
            }
            ecommerceResponse.setWriterBuffered(true);
        }
        NetxSession netxSession = createKariquSession(ecommerceRequest, ecommerceResponse);
        if (needLoginCheck) {
            doLoginCheck(netxSession);
        }
        try {
            chain.doFilter(ecommerceRequest, ecommerceResponse);
            if (null != netxSession) {
                if (logger.isDebugEnabled()) {
                    logger.debug("com.netx.session framework start to commit com.netx.session--" + "netxSession.commit");
                }
                netxSession.commit();
            }
        }
        catch (Exception ex) {
            logger.error("com.netx.session framework occur exception", ex);
            throw new RuntimeException(ex);
        }
        finally {
            if (logger.isDebugEnabled()) {
                logger.debug("com.netx.session framework start to commit buffer--" + "ecommerceResponse.commitBuffer");
            }
            ecommerceResponse.commitBuffer();
        }
    }

    /**
     * 进行登录校验
     *
     * @param netxSession
     */
    private void doLoginCheck(NetxSession netxSession) {
        if (SessionUtils.isLogin(netxSession)) {
            String lastTime = (String) netxSession.getAttribute(SessionConstants.LAST_VISIT_TIME);
            int lastVisitTime = 0;
            if (lastTime != null) {
                try {
                    lastVisitTime = Integer.parseInt(lastTime);
                }
                catch (NumberFormatException e1) {
                    lastVisitTime = 0;
                }
            }
            if ((System.currentTimeMillis() / 1000 - lastVisitTime) >= sessionExpireTime) {
                netxSession.invalidate();
            }
        }
        // 更新上次访问时间
        netxSession.setAttribute(SessionConstants.LAST_VISIT_TIME, Long.toString(System.currentTimeMillis() / 1000));
    }

    /**
     * 判断是否需要经过sessionFilter处理
     *
     * @param request
     * @param response
     * @param chain
     * @param ecommerceRequest
     *
     * @return
     *
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    private boolean letitgo(ServletRequest request, ServletResponse response, FilterChain chain, NetxSessionServletRequest ecommerceRequest) throws IOException, ServletException {
        if (null == forbiddenUrlSuffixes) {
            return false;
        }
        String requestURI = ecommerceRequest.getRequestURI();
        for (String forbiddenSuffix : forbiddenUrlSuffixes) {
            if (StringUtils.isNotBlank(requestURI)) {
                if (requestURI.endsWith(forbiddenSuffix)) {
                    chain.doFilter(request, response);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        logger.info("destroy");
    }

    private NetxSession createKariquSession(NetxSessionServletRequest ecommerceRequest, NetxSessionServletResponse ecommerceResponse) {
        NetxSession netxSession = new NetxSession(sessionConfig, filterConfig.getServletContext());
        netxSession.setSessionStores(sessionStoreFactory.createSessionStores(ecommerceRequest, ecommerceResponse));
        netxSession.setMaxInactiveInterval(sessionExpireTime);
        ecommerceRequest.setSession(netxSession);
        ecommerceResponse.setSession(netxSession);
        netxSession.init();
        return netxSession;
    }

    /**
     * init sessionStoreFactory
     *
     * @param filterConfig
     */
    private void initSessionStoreFactory(FilterConfig filterConfig) {
        HashMap<StoreType, Class<? extends SessionStore>> map = new HashMap<StoreType, Class<? extends SessionStore>>();
        map.put(StoreType.cookie, CookieStore.class);
        map.put(StoreType.cache, CacheStore.class);
        sessionStoreFactory = new SessionStoreFactory(map);
    }
}
