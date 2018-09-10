package com.netx.utils.db.balance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.lang.reflect.Method;
import java.util.Random;
import org.apache.commons.logging.Log;

/**
 * 动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Log log = LogFactory.getLog(DynamicDataSource.class);

    private static final AtomicInteger counter;

    static {
        Random r = new Random();
        counter = new AtomicInteger(r.nextInt());
    }

    /**
     * master库 dataSource
     */
    private DataSource master;

    /**
     * slaves
     */
    private List<DataSource> slaves;

    @Override
    protected Object determineCurrentLookupKey() {
        //do nothing
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        //do nothing
    }

    /**
     * 根据标识 获取数据源
     */
    @Override
    protected DataSource determineTargetDataSource() {
        DataSource returnDataSource = null;
        if (DataSourceHolder.isForceMaster()) {
            returnDataSource = master;
            log("master", returnDataSource);
        } else {
            if (DataSourceHolder.isSlave()) {
                int count = counter.incrementAndGet();
                int n = slaves.size();
                int index = Math.abs(count % n);
                returnDataSource = slaves.get(index);
                log("slave", returnDataSource);
            } else {
                returnDataSource = master;
                log("master", returnDataSource);
            }
        }

        return returnDataSource;
    }

    private void log(String mos, DataSource returnDataSource) {
        if (log.isTraceEnabled()) {
            log.trace(mos);
            Class<? extends DataSource> dsClass = returnDataSource.getClass();
            try {
                Method method = dsClass.getMethod("getUrl");
                log.trace(method.invoke(returnDataSource));
            } catch (Throwable ex) {
                try {
                    Method method = dsClass.getMethod("getJdbcUrl");
                    log.trace(method.invoke(returnDataSource));
                } catch (Throwable ex2) {
                }
            }
        }
    }

    public DataSource getMaster() {
        return master;
    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public List<DataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<DataSource> slaves) {
        this.slaves = slaves;
    }
}
