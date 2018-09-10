package com.netx.utils.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Runshine
 * @since 2015-6-23
 * @version 1.0.0
 *
 */
public class SessionHelper {
    public static boolean isLogin(HttpServletRequest request) {
        return getLoginUser(request) != null;
    }

    public static boolean isLogin(HttpSession session) {
        return getLoginUser(session) != null;
    }

    public static LoginUser getLoginUser(HttpServletRequest request) {
        return getLoginUser(request.getSession(false));
    }

    public static LoginUser getLoginUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (LoginUser)session.getAttribute(Constants.Session_User_Login_Key);
    }

    public static void putLoginUser(LoginUser loginUser, HttpServletRequest request) {
        putLoginUser(loginUser, request.getSession(true));
    }

    public static void putLoginUser(LoginUser loginUser, HttpSession session) {
        session.setAttribute(Constants.Session_User_Login_Key, loginUser);
    }

    public static void removeLoginUser(HttpServletRequest request) {
        removeLoginUser(request.getSession(false));
    }

    public static void removeLoginUser(HttpSession session) {
        if (session != null) {
            session.removeAttribute(Constants.Session_User_Login_Key);
        }
    }
}
