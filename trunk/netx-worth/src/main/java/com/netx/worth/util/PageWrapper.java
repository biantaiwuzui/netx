package com.netx.worth.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageWrapper {
    public static Map wrapper(Integer total, List list) {
        Map map = new HashMap();
        map.put("total", total);
        map.put("list", list);
        return map;
    }
    public static Map wrapper(Integer total, Map map) {
        map.put("total", total);

        return map;
    }
}
