package com.netx.utils.security.rejectflush;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Runshine
 * @since 2015-7-17
 * @version 1.0.0
 *
 */
public interface Regulation {
    /**
     * 根据规则决定是否拒绝此请求
     *
     * @return left返回是否拒绝；right返回拒绝时被加入的黑名单key，如果为空则是未加入黑名单
     */
    RejectProcedureResult isReject(HttpServletRequest request, HttpServletResponse response);
}
