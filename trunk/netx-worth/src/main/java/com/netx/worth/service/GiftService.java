package com.netx.worth.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.gift.GiftLogisticsDto;
import com.netx.common.wz.dto.gift.GiftSendDto;
import com.netx.utils.money.Money;
import com.netx.utils.money.Money;
import com.netx.worth.enums.GiftStatus;
import com.netx.worth.mapper.GiftMapper;
import com.netx.worth.model.Gift;

@Service
public class GiftService extends ServiceImpl<GiftMapper, Gift>{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	/**根据userId查询用户送出或者受到的礼物列表*/
    public List<Gift> getUserGifts(String userId, Page<Gift> page, String type) {
        List<Gift> list = new ArrayList<Gift>();
        EntityWrapper<Gift> entityWrapper = new EntityWrapper<Gift>();
        if (type.equals("send")) {
            entityWrapper.where("from_user_id={0}", userId);
        } else {
            entityWrapper.where("to_user_id={0}", userId);
        }
        entityWrapper.orderBy("create_time", false);//降序
        Page<Gift> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();

    }
    
    /**接收礼物*/
    public boolean accept(String userId, String giftId) {
        Gift gift = selectById(giftId);
        gift.setStatus(GiftStatus.ACCEPT.status);
        EntityWrapper<Gift> entityWrapper = new EntityWrapper<Gift>();
        entityWrapper.where("to_user_id={0}", userId).and("id={0}", giftId);
        return update(gift, entityWrapper);
    }
    
    /**拒绝礼物*/
    public boolean refuse(String userId, String giftId) {
        Gift gift = selectById(giftId);
        gift.setStatus(GiftStatus.REFUSE.status);
        EntityWrapper<Gift> entityWrapper = new EntityWrapper<Gift>();
        entityWrapper.where("to_user_id={0}", userId).and("id={0}", giftId);
        return update(gift, entityWrapper);
    }
    
    /**赠送礼物*/
    public boolean add(GiftSendDto giftDto,Gift gift) {
        gift.setTitle(giftDto.getTitle());
        gift.setFromUserId(giftDto.getFromUserId());
        gift.setToUserId(giftDto.getToUserId());
        gift.setSendAt(new Date());
        gift.setGiftType(giftDto.getGiftType());
        gift.setRelatableId(giftDto.getRelatableId());
        gift.setAmount(new Money(giftDto.getAmount()).getCent());
        gift.setDescription(giftDto.getDescription());
        gift.setAnonymity(giftDto.getAnonymity());
        gift.setDeliveryAt(new Date(giftDto.getDeliveryAt()));
        boolean ret= insert(gift);
        return ret;
    }
    
    /**设置物流信息*/
    public boolean setLogistics(GiftLogisticsDto giftLogisticsDto) {
        Gift gift = new Gift();
        gift.setId(giftLogisticsDto.getGiftId());
        gift.setAddress(giftLogisticsDto.getAddress());
        gift.setDeliveryAt(new Date(giftLogisticsDto.getDeliveryAt()));
        gift.setMessage(giftLogisticsDto.getMessage());
        gift.setSetLogistics(true);
        EntityWrapper<Gift> entityWrapper = new EntityWrapper<Gift> ();
        return update(gift, entityWrapper);
    }
    
    /**得到受到礼物的总数*/
    public int getReceiveCount(String userId) {
        EntityWrapper<Gift>  entityWrapper = new EntityWrapper<Gift> ();
        entityWrapper.where("to_user_id={0}", userId);
        return selectCount(entityWrapper);
    }
    /**得到送出礼物的总数*/
    public int getSendCount(String userId, String articleId) {
        EntityWrapper<Gift>  entityWrapper = new EntityWrapper<Gift>();
        entityWrapper.where(StringUtils.hasText(userId), "from_user_id={0}", userId).and("article_id={0}", articleId);
        return selectCount(entityWrapper);
    }

    public Integer getSendCountByUserId(String userId) {
        EntityWrapper<Gift>  entityWrapper = new EntityWrapper<Gift>();
        entityWrapper.where(StringUtils.hasText(userId), "from_user_id={0} AND deleted = 0", userId);
        return selectCount(entityWrapper);
    }
    
    /**清除该用户的礼物模块的数据*/
    public boolean clean(String userId) throws Exception {
    	EntityWrapper<Gift> giftWrapper = new EntityWrapper<Gift>();
    	giftWrapper.where("from_user_id={0}", userId);
    	giftWrapper.or("to_user_id={0}", userId);
    	return delete(giftWrapper);
    }
}
