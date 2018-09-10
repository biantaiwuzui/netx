package com.netx.common.wz.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public class CommonUtil {
    /**
     * dbLabels是否包含queryLabels中的标签
     *
     * @param queryLabels 待查询的labels，逗号分隔
     * @param dbLabels    数据库存的lables，逗号分隔
     * @return
     */
    public static boolean labelsAllMatch(String queryLabels, final String dbLabels) {
        if (!StringUtils.hasText(queryLabels)) return true;
        if (!StringUtils.hasText(dbLabels)) return false;
        String[] queryLabelsArray = queryLabels.split(",");
        final String[] dbLabelsArray = dbLabels.split(",");
        boolean flag = Arrays.stream(queryLabelsArray).allMatch(e -> Arrays.asList(dbLabelsArray).contains(e));
        return flag;
    }
}
