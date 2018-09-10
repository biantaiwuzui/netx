package com.netx.fuse.client.worth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.worth.biz.common.GiftAction;
@Service
public class GiftClientAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private GiftAction giftAction;

    /**
     * 获取咨讯的礼物数量
     * @param userId
     * @param articleId
     * @return
     */
    public Integer sendListCount(String userId,String articleId) {
    	try {
            return giftAction.getSendCount(userId, articleId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    	return 0;
    }

}
