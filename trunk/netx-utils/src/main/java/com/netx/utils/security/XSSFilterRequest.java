package com.netx.utils.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author Runshine
 * @since 2014-11-26
 * @version 1.0.0
 *
 */
public class XSSFilterRequest extends HttpServletRequestWrapper {
    public XSSFilterRequest(HttpServletRequest request) {
        super(request);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0;i < count;i++) {
            encodedValues[i] = XSS.filter(values[i]);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return XSS.filter(value);
    }
//
//    public String getHeader(String name) {
//        String value = super.getHeader(name);
//        if (value == null)
//            return null;
//        return XSS.filter(value);
//    }
}
