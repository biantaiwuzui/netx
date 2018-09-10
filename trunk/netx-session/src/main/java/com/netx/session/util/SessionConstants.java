package com.netx.session.util;



public interface SessionConstants {

    /**
     * 跟踪session对象的Id
     */
    String SESSION_ID = "sessionId";

    /**
     * 跟踪ID生命周期设置足够长，只要用户访问网站即可生成次cookie
     */
    String TRACK_ID = "trackId";

    /**
     * 是否登录cookie的名称
     */
    String LOGIN = "login";


    /**
     * 用户IDcookie名称
     */
    String USERID = "userId";


    /**
     * 防表单重提交的token
     */
    String TOKEN = "token";
    /**
     * 上次访问时间cookie名称
     */
    String LAST_VISIT_TIME = "lastVisitTime";

    String LOGIN_USER = "loginUser";
}
