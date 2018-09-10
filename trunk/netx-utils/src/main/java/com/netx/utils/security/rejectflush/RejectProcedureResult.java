package com.netx.utils.security.rejectflush;

import java.io.Serializable;

/**
 *
 * @author Runshine
 * @since 2015-7-18
 * @version 1.0.0
 *
 */
public class RejectProcedureResult {
    /**
     * 当前规则处理后认定是否需要阻止
     */
    private boolean reject;

    /**
     * 当前规则处理后是否流入下一级
     */
    private boolean continuate;

    /**
     * 被阻止后说加入的黑名单key,如果为空则是未加入黑名单
     */
    private Serializable blacklistKey;

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public boolean isContinuate() {
        return continuate;
    }

    public void setContinuate(boolean continuate) {
        this.continuate = continuate;
    }

    public Serializable getBlacklistKey() {
        return blacklistKey;
    }

    public void setBlacklistKey(Serializable blacklistKey) {
        this.blacklistKey = blacklistKey;
    }
}
