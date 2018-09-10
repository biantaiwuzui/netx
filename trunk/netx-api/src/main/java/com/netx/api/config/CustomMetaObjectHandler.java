package com.netx.api.config;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomMetaObjectHandler extends MetaObjectHandler {
    private Logger logger = LoggerFactory.getLogger(CustomMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Object createTime = getFieldValByName("createTime", metaObject);
            Object createUser = getFieldValByName("createUserId", metaObject);
            Object updateTime = getFieldValByName("updateTime", metaObject);
            Object updateUser = getFieldValByName("updateUserId", metaObject);
            Object delTag = getFieldValByName("deleted", metaObject);

            Date timeStr = new Date();
            if (createTime == null) {
                setFieldValByName("createTime", timeStr, metaObject);
            }
            if (updateTime == null) {
                setFieldValByName("updateTime", timeStr, metaObject);
            }
            if (delTag == null) {
                setFieldValByName("deleted", 0, metaObject);
            }
//           如果创建人不为空，就将updateUser也一并更新了
            if (createUser != null && updateUser == null) {
                setFieldValByName("updateUserId", createUser, metaObject);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            Object updateTime = getFieldValByName("updateTime", metaObject);
            if (updateTime == null) {
                setFieldValByName("updateTime", new Date(), metaObject);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
