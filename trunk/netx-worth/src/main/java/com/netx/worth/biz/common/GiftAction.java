package com.netx.worth.biz.common;

import com.netx.worth.service.WorthServiceprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.common.wz.dto.gift.GiftLogisticsDto;

/**
 * <p>
 * 礼物表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class GiftAction{
    @Autowired
    private WorthServiceprovider worthServiceprovider;

    private Logger logger = LoggerFactory.getLogger(GiftAction.class);

    public boolean setLogistics(GiftLogisticsDto giftLogisticsDto) {
        return worthServiceprovider.getGiftService().setLogistics(giftLogisticsDto);
    }

    public int getReceiveCount(String userId) {
        return worthServiceprovider.getGiftService().getReceiveCount(userId);
    }

    public int getSendCount(String userId, String articleId) {
        return worthServiceprovider.getGiftService().getSendCount(userId, articleId);
    }

    public Integer getCountByUserId(String status, String userId) {
        if("send".equals(status)){
            return worthServiceprovider.getGiftService().getSendCountByUserId(userId);
        }else{
            return worthServiceprovider.getGiftService().getReceiveCount(userId);
        }
    }
    
    
    public boolean clean(String userId) throws Exception {
    	boolean success = worthServiceprovider.getGiftService().clean(userId);
    	if(success) {
    		return success;
    	}else {
    		logger.error("清除该用户礼物的数据失败");
    		throw new RuntimeException();
    	}
    }

}
