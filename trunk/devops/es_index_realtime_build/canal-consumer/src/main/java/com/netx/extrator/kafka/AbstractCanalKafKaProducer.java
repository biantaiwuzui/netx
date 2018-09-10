package com.netx.extrator.kafka;

import com.alibaba.otter.canal.protocol.Message;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Yawn on 2018/7/18
 */
public abstract class AbstractCanalKafKaProducer {
    protected static final String[] WORTH_TABLES = {"demand", "skill", "meeting", "wish"};
    protected static final String[] COLUM_LIST = {"id", "update_time", "create_time"};
    //    相关表格正则
    protected final static String RELATED_TABLES_PATTERN = "skill_.*||demand_.*||wish_.*||meeting_.*";
    protected static final String SKILL_RELATED_TABLES_PATTERN = "skill_.*";
    protected static final String DEMAND_RELATED_TABLES_PATTERN = "demand_.*";
    protected static final String WISH_RELATED_TABLES_PATTERN = "wish_.*";
    protected static final String MEETING_RELATED_TABLES_PATTERN = "meeting_.*";
    //    需要的ID
    protected static final String TABLE_ID = "demand_id||skill_id||wish_id||meeting_id";

//    会话名称
    protected static final String TOPIC = "test";

    public abstract void stop();

    public abstract void extratorAndSend(Message message);


}
