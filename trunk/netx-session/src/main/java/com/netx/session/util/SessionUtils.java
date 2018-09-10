package com.netx.session.util;

import com.netx.session.NetxSessionLoginCallback;

import javax.servlet.http.HttpSession;


public class SessionUtils {

    public static boolean isLogin(HttpSession kariquSession) {
        boolean login = false;
        try {
            login = null != kariquSession && "true".equals(kariquSession.getAttribute(SessionConstants.LOGIN));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return login;
    }

    public static void doLogin(HttpSession session, Object userId) {
        session.setAttribute(SessionConstants.LOGIN, "true");
        session.setAttribute(SessionConstants.USERID, userId);
    }

    public static void doLogin(NetxSessionLoginCallback callback) {
        callback.beforeLogin();
        callback.getHttpSession().setAttribute(SessionConstants.LOGIN, "true");
        callback.getHttpSession().setAttribute(SessionConstants.USERID, callback.getUserId());
        callback.afterLogin();
    }

    public static Object getUserId(HttpSession session) {
        return session.getAttribute(SessionConstants.USERID);
    }

}
