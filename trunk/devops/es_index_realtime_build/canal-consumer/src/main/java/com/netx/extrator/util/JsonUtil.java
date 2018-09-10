package com.netx.extrator.util;



import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by Yawn on 2018/7/18
 */
public class JsonUtil {
    public static String map2Josn(Map<?,?> map) {
        if (CollectionUtil.isNotEmpty(map))
            return JSON.toJSONString(map);
        else
            return "";
    }
}
