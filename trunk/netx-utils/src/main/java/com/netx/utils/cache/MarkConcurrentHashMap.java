package com.netx.utils.cache;

import java.util.Map;


class MarkConcurrentHashMap<V> extends java.util.concurrent.ConcurrentHashMap<String, V> {
    private static final long serialVersionUID = -1918246301191931132L;

    public MarkConcurrentHashMap() {
    }

    public MarkConcurrentHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public MarkConcurrentHashMap(Map<? extends String, ? extends V> m) {
        super(m);
    }

    public MarkConcurrentHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MarkConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(initialCapacity, loadFactor, concurrencyLevel);
    }
}
