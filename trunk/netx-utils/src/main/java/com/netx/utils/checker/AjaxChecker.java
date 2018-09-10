package com.netx.utils.checker;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Runshine
 * @since 2014-11-20
 * @version 1.0.0
 *
 */
public class AjaxChecker {
    public static boolean isAjax(HttpServletRequest request) {
        String isAjax = request.getParameter("ajax");
        String accept = request.getHeader("accept");
        String requestedWith = request.getHeader("X-Requested-With");

        //虽然可能有遗漏，但姑且这么区分ajax请求先...
        if (StringUtils.isNotBlank(isAjax) && "true".equals(isAjax)
                || StringUtils.isNotBlank(accept) && accept.contains("application/json")
                || StringUtils.isNotBlank(requestedWith) && requestedWith.contains("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
