package com.netx.utils.cache;

/**
 * @author Closure.Yang
 * @since 2015/12/19
 */
public class RedisUtil {

    public static int[] getRange(int pageNo, int pageSize) {
        int _s = getStartIndex(pageNo, pageSize);
        int _e = getEndIndex(pageNo, pageSize);
        return new int[] {_s, _e};
    }


    public static int getStartIndex(int pageNo, int pageSize) {
        int _p = pageNo < 1 ? 1 : pageNo;
        return (pageNo - 1) * pageSize;
    }

    public static int getEndIndex(int pageNo, int pageSize) {
        if(pageSize < 0) return -1;
        int _p = pageNo < 1 ? 1 : pageNo;
        int startIndex = (pageNo - 1) * pageSize;
        return startIndex + (pageSize - 1);
    }
}
