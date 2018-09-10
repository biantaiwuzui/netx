package com.netx.common.user.constant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 积分常量类
 * 记录积分项目描述、积分值、积分日上限、特殊项目的前置条件（例如：“每获得5个赞”，需要5次点赞的前置条件才能触发）
 * @author 李卓
 */
public class ScoreConstant {

    /**
     * 记录积分项目
     * key 与 score 表的 code 对应
     */
    public final static Map<Integer, String> CODE_MAP_DESCRIPTION = new HashMap<Integer, String>(){
        {
            put(1, "资料完善度每提高10%");
            put(2, "连续登录1-4天");
            put(3, "连续登录5-10天");
            put(4, "连续登录11-29天");
            put(5, "连续登录30天及以上");
            put(6, "每获得5个赞");
            put(7, "每发布1条信息");
            put(8, "每完成1个交易");
            put(9, "每被人关注5次");
            put(10, "每关注他人5次");
            put(11, "每个正分评价");
            put(12, "每个负分评价");
            put(13, "信用每降低1分");
            put(14, "超过10天没登录");
            put(15, "邀请好友完成注册");
            put(16, "成功分享链接");
        }
    };

    /**
     * 记录积分
     * 扣除积分的用负值表示
     * key 与 score 表的 code 对应
     */
    public final static Map<Integer, BigDecimal> CODE_MAP_SCORE = new HashMap<Integer, BigDecimal>(){
        {
            put(1, BigDecimal.valueOf(10));//资料完善度每提高10%
            put(2, BigDecimal.valueOf(1));//连续登录1-4天，1分/天
            put(3, BigDecimal.valueOf(2));//连续登录5-10天
            put(4, BigDecimal.valueOf(2));//连续登录11-29天
            put(5, BigDecimal.valueOf(5));//连续登录30天及以上
            put(6, BigDecimal.valueOf(1));//每获得5个赞
            put(7, BigDecimal.valueOf(2));//每发布1条信息
            put(8, BigDecimal.valueOf(1));//每完成1个交易
            put(9, BigDecimal.valueOf(1));//每被人关注5次
            put(10, BigDecimal.valueOf(1));//每关注他人5次
            put(11, BigDecimal.valueOf(1));//每个正分评价
            put(12, BigDecimal.valueOf(-2));//每个负分评价
            put(13, BigDecimal.valueOf(-2));//信用每降低1分
            put(14, BigDecimal.valueOf(-1));//超过10天没都登录，-1分/天
            put(15, BigDecimal.valueOf(10));//邀请好友完成注册
            put(16, BigDecimal.valueOf(1));//成功分享链接
        }
    };

    /**
     * 记录积分日上限
     * key 与 score 表的 code 对应
     * 1、日上限的 -999 值表示无上限，在添加积分流水接口里，遇到无上限是直接忽略判断日上限逻辑。所以无上限的项目，性能更高。
     * 2、为了提高性能，以下部分日上限与原型日上限不相同，但实现结果与原型的一致
     * 3、下面一一列举与原型不同日上限的积分项目
     *  积分项目        原型日上限
     *  连续登录1-4天：       1
     *  连续登录5-10天：      2
     *  连续登录11-29天：     3
     *  连续登录30天及以上：  5
     *  超过10天没登录：      -1
     */
    public final static Map<Integer, Integer> CODE_MAP_TOPLIMIT = new HashMap<Integer, Integer>(){
        {
            put(1, -999);//资料完善度每提高10%
            put(2, -999);//连续登录1-4天
            put(3, -999);//连续登录5-10天
            put(4, -999);//连续登录11-29天
            put(5, -999);//连续登录30天及以上
            put(6, 1);//每获得5个赞
            put(7, 10);//每发布1条信息
            put(8, 10);//每完成1个交易
            put(9, 10);//每被人关注5次
            put(10, 10);//每关注他人5次
            put(11, 10);//每个正分评价
            put(12, -999);//每个负分评价
            put(13, -999);//信用每降低1分
            put(14, -999);//超过10天都没登录
            put(15, 100);//邀请好友完成注册
            put(16, 10);//成功分享链接
        }
    };

}
