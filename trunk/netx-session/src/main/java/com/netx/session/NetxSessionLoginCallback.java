package com.netx.session;

import javax.servlet.http.HttpSession;
import java.io.Serializable;


public interface NetxSessionLoginCallback {

    HttpSession getHttpSession();

    Serializable getUserId();

    void beforeLogin();

    void afterLogin();


}
