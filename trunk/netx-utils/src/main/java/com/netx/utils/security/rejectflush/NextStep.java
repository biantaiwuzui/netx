package com.netx.utils.security.rejectflush;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Runshine
 * @since 2015-7-17
 * @version 1.0.0
 *
 */
public interface NextStep {
    void doNext(HttpServletRequest request, HttpServletResponse response, Serializable blackKey);
}
