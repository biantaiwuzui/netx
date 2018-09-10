package com.netx.utils.db.balance;

public class DataSourceHolder {
    private static class FlagThreadLocal extends ThreadLocal<Boolean> {

        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    }

    /**
     * dataSource master or slave
     */
    private static final FlagThreadLocal dataSources = new FlagThreadLocal();

    private static final FlagThreadLocal forceMaster = new FlagThreadLocal();

    /**
     * 标志为slave
     */
    public static void setSlave() {
        dataSources.set(Boolean.TRUE);
    }

    /**
     * 标志为master
     */
    public static void setMaster() {
        dataSources.set(Boolean.FALSE);
    }

    public static boolean isMaster() {
        return !isSlave();
    }

    public static boolean isSlave() {
        return dataSources.get();
    }

    /**
     * 清除thread local中的数据源
     */
    public static void clearDataSource() {
        dataSources.remove();
    }

    public static void forceMaster() {
        forceMaster.set(Boolean.TRUE);
    }

    public static boolean isForceMaster() {
        return forceMaster.get();
    }

    public static void destroyForceMaster() {
        forceMaster.remove();
    }
}
