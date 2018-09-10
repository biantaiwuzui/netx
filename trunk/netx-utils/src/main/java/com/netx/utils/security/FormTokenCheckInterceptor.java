package com.netx.utils.security;

import com.netx.utils.checker.AjaxChecker;
import com.netx.utils.json.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Runshine
 * @since 2014-11-20
 * @version 1.0.0
 *
 */
public class FormTokenCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String formToken = request.getParameter("__form_token__");
        HttpSession session = request.getSession();
        String sessionFormToken = (String)session.getAttribute("formToken");
        if (StringUtils.isNotBlank(formToken) && formToken.equals(sessionFormToken)) {
            return true;
        }

        if (AjaxChecker.isAjax(request)) {
            response.setContentType("application/json;charset=UTF-8");
            new JsonResult(false, "请不要重复提交哦～").addResult("__form_token__", sessionFormToken).toJson(response);
        } else {
            response.sendRedirect("/error/formResubmit");
        }
        return false;
    }
}
