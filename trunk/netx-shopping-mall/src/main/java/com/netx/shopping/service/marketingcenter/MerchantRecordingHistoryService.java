package com.netx.shopping.service.marketingcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.shopping.model.marketingcenter.MerchantRecordingHistory;
import com.netx.shopping.mapper.marketingcenter.MerchantRecordingHistoryMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 网商-提成记录表 服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-22
 */
@Service
public class MerchantRecordingHistoryService extends ServiceImpl<MerchantRecordingHistoryMapper, MerchantRecordingHistory> {

    public BigDecimal getDecimal(String merchantId, Integer type){
        Wrapper<MerchantRecordingHistory> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("sum(money)");
        if(type==0){
            wrapper.where("merchant_id = {0} and type = 0", merchantId);
        }else{
            wrapper.where("to_merchant_id = {0} and type = 1", merchantId);
            if(type==2){
                Long time = System.currentTimeMillis();
                wrapper.between("create_time", DateTimestampUtil.getStartOrEndOfTimestamp(time,1),time);
            }
        }
        return (BigDecimal) this.selectObj(wrapper);
    }
	
}
