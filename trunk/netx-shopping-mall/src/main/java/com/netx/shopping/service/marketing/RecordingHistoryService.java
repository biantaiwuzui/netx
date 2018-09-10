package com.netx.shopping.service.marketing;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.shopping.mapper.marketing.SellerRecordingHistoryMapper;
import com.netx.shopping.model.marketing.SellerRecordingHistory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class RecordingHistoryService extends ServiceImpl<SellerRecordingHistoryMapper, SellerRecordingHistory> {

    public BigDecimal getDecimal(String sellerId,Integer type){
        Wrapper<SellerRecordingHistory> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("sum(money)");
        if(type==0){
            wrapper.where("seller_id = {0} and type = 0",sellerId);
        }else{
            wrapper.where("to_seller_id = {0} and type = 1",sellerId);
            if(type==2){
                Long time = System.currentTimeMillis();
                wrapper.between("create_time", DateTimestampUtil.getStartOrEndOfTimestamp(time,1),time);
            }
        }
        return (BigDecimal) this.selectObj(wrapper);
    }
}
