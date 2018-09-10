package com.netx.shopping.biz.order;

import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 李威
 * @since 2018-02-11
 */
@Service
public class ProductOrderPutOffAction{

    /**
     * 时间戳转换为日期
     */
    public String getCurrentTimeStr(Long time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(time);
    }
}
