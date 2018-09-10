package com.netx.utils.cache;


/**
 * 
 * @author Runshine
 * @since 2015-12-11
 * @version 1.0.0
 *
 */
public class SimpleCache implements Cache {
    public SimpleCache() {
    }

    private ConcurrentDoubleStringKeyHashMap cdskhm = new ConcurrentDoubleStringKeyHashMap(5120, 0.85f, Runtime.getRuntime().availableProcessors());

    public static SimpleCache getInstance() {
        return InstanceHolder.sc;
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private long expireTime(long seconds) {
        if(seconds <= 0) throw new RuntimeException("过期时间必须大于0");
        return now() + seconds * 1000;
    }

    @Override
    public void put(String key, Object value, int seconds) {
        BaseValue bv = new BaseValue(value, expireTime(seconds));
        cdskhm.put(key, bv);
    }

    @Override
    public void put(String key, Object value) {
        BaseValue bv = new BaseValue(value);
        cdskhm.put(key, bv);
    }

    @Override
    public Object get(String key) {
        BaseValue bv = cdskhm.get(key);
        if(bv != null) {
            return bv.getValue();
        }
        return null;
    }

    @Override
    public void del(String key) {
        cdskhm.delete(key);
    }

    @Override
    public void hset(String key, String field, Object value, int seconds) {
        BaseValue bv = new BaseValue(value, expireTime(seconds));
        cdskhm.put(key, field, bv);
    }

    @Override
    public void hset(String key, String field, Object value) {
        BaseValue bv = new BaseValue(value);
        cdskhm.put(key, field, bv);
    }

    @Override
    public Object get(String key, String field) {
        BaseValue bv = cdskhm.get(key, field);
        if(bv != null) {
            return bv.getValue();
        }
        return null;
    }

    @Override
    public void del(String key, String field) {
        cdskhm.delete(key, field);
    }

    @Override
    public boolean exists(String key) {
        return cdskhm.exists(key);
    }

    @Override
    public boolean exists(String key, String field) {
        return cdskhm.exists(key, field);
    }

    @Override
    public Long incrBy(String key) {
        BaseValue<Long> bv = new BaseValue<>(1L);
        BaseValue<Long> incrBy = cdskhm.incrBy(key, bv);
        return incrBy.getValue();
    }

    @Override
    public Long incrBy(String key, long increment) {
        BaseValue<Long> bv = new BaseValue<>(increment);
        BaseValue<Long> incrBy = cdskhm.incrBy(key, bv);
        return incrBy.getValue();
    }

    @Override
    public Long incrBy(String key, int seconds) {
        BaseValue<Long> bv = new BaseValue<>(1L, expireTime(seconds));
        BaseValue<Long> incrBy = cdskhm.incrBy(key, bv);
        return incrBy.getValue();
    }

    @Override
    public Long incrBy(String key, long increment, int seconds) {
        BaseValue<Long> bv = new BaseValue<>(increment, expireTime(seconds));
        BaseValue<Long> incrBy = cdskhm.incrBy(key, bv);
        return incrBy.getValue();
    }

    @Override
    public Long decrBy(String key) {
        BaseValue<Long> bv = new BaseValue<>(-1L);
        BaseValue<Long> incrBy = cdskhm.incrBy(key, bv);
        return incrBy.getValue();
    }

    @Override
    public Long decrBy(String key, long increment) {
        BaseValue<Long> bv = new BaseValue<>(-increment);
        BaseValue<Long> incrBy = cdskhm.incrBy(key, bv);
        return incrBy.getValue();
    }

    @Override
    public Long decrBy(String key, int seconds) {
        BaseValue<Long> bv = new BaseValue<>(-1L, expireTime(seconds));
        BaseValue<Long> incrBy = cdskhm.incrBy(key, bv);
        return incrBy.getValue();
    }

    @Override
    public Long decrBy(String key, long increment, int seconds) {
        BaseValue<Long> bv = new BaseValue<>(-increment, expireTime(seconds));
        BaseValue<Long> incrBy = cdskhm.incrBy(key, bv);
        return incrBy.getValue();
    }

    @Override
    public Long incrBy(String key, String field) {
        BaseValue<Long> bv = new BaseValue<>(1L);
        BaseValue<Long> incrBy = cdskhm.incrBy(key, field, bv);
        return incrBy.getValue();
    }

    @Override
    public Long incrBy(String key, String field, long increment) {
        BaseValue<Long> bv = new BaseValue<>(increment);
        BaseValue<Long> incrBy = cdskhm.incrBy(key, field, bv);
        return incrBy.getValue();
    }

    @Override
    public Long incrBy(String key, String field, int seconds) {
        BaseValue<Long> bv = new BaseValue<>(1L, expireTime(seconds));
        BaseValue<Long> incrBy = cdskhm.incrBy(key, field, bv);
        return incrBy.getValue();
    }

    @Override
    public Long incrBy(String key, String field, long increment, int seconds) {
        BaseValue<Long> bv = new BaseValue<>(increment, expireTime(seconds));
        BaseValue<Long> incrBy = cdskhm.incrBy(key, field, bv);
        return incrBy.getValue();
    }

    @Override
    public Long decrBy(String key, String field) {
        BaseValue<Long> bv = new BaseValue<>(-1L);
        BaseValue<Long> incrBy = cdskhm.incrBy(key, field, bv);
        return incrBy.getValue();
    }

    @Override
    public Long decrBy(String key, String field, long increment) {
        BaseValue<Long> bv = new BaseValue<>(-increment);
        BaseValue<Long> incrBy = cdskhm.incrBy(key, field, bv);
        return incrBy.getValue();
    }

    @Override
    public Long decrBy(String key, String field, int seconds) {
        BaseValue<Long> bv = new BaseValue<>(-1L, expireTime(seconds));
        BaseValue<Long> incrBy = cdskhm.incrBy(key, field, bv);
        return incrBy.getValue();
    }

    @Override
    public Long decrBy(String key, String field, long increment, int seconds) {
        BaseValue<Long> bv = new BaseValue<>(-increment, expireTime(seconds));
        BaseValue<Long> incrBy = cdskhm.incrBy(key, field, bv);
        return incrBy.getValue();
    }

    private void expiredResourceCollect() {
        ExpiredResourceCollector rc = new ExpiredResourceCollector();
        rc.setDaemon(true);
        rc.setName("ExpiredResourceCollector");
        rc.start();
    }

    private class ExpiredResourceCollector extends Thread {
        @Override
        public void run() {
            for(;;) {
                try {
                    Thread.sleep(60_000);
                    cdskhm.expiredResourceCollect();
                } catch(InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private final static class InstanceHolder {
        private static final SimpleCache sc = new SimpleCache();

        static {
            sc.expiredResourceCollect();
        }
    }

    public String toString() {
        return cdskhm.toString();
    }
}
