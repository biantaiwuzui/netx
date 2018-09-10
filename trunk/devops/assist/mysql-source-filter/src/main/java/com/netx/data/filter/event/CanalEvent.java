package com.netx.data.filter.event;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;

/**
 * Created by czou on 5/7/2018.
 */
public class CanalEvent {
    private Entry source;
    private EventType eventType;

    public CanalEvent(Entry source, EventType eventType) {
        this.source = source;
        this.eventType = eventType;
    }

    public Entry getEntry() {
        return source;
    }

    public EventType getEventType() {
        return eventType;
    }
}
