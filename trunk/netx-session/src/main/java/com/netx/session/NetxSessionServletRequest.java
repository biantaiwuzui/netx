package com.netx.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class NetxSessionServletRequest extends HttpServletRequestWrapper {

    private NetxSession netxSession;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public NetxSessionServletRequest(HttpServletRequest request) {
        super(request);
    }

    public void setSession(NetxSession netxSession) {
        this.netxSession = netxSession;
    }

    @Override
    public NetxSession getSession() {
        return netxSession;
    }

    @Override
    public NetxSession getSession(boolean create) {
        return getSession();
    }
}
