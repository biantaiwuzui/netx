package com.netx.utils.security.rejectflush;

import java.io.Serializable;

/**
 *
 * @author Runshine
 * @since 2015-7-13
 * @version 1.0.0
 *
 */
public class RejectModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ip;
    private long firstTime;
    private int count;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
