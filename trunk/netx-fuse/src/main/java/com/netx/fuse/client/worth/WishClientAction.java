package com.netx.fuse.client.worth;

import java.util.Map;

import com.netx.fuse.biz.worth.WishFuseAction;
import com.netx.worth.biz.wish.WishAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class WishClientAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private WishAction wishAction;
	@Autowired
	private WishFuseAction wishFuseAction;
	
    public void checkRefereeSuccess(Map map) {
    	try {
    		wishFuseAction.checkRefereeSuccess((String)map.get("wishId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    public void checkSuccess(Map map) {
    	try {
    		wishFuseAction.checkSuccess((String)map.get("wishId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    public void checkEvaluate(Map map) {
    	try {
    		wishFuseAction.checkEvaluate((String)map.get("wishId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    public void autoAcceptApply(Map map) {
    	try {
    		wishFuseAction.acceptApply((String)map.get("applyId"), (String)map.get("userId"), (String)map.get("description"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    public void checkHasUnComplete(String userId) {
    	try {
    		wishAction.checkHasUnComplete(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    /***
     * 投诉裁决通过，退款给支持者
     * @param wishId
     * @return
     */
    public void wishComplaintSuccess(@RequestParam("wishId") String wishId) {
    	try {
    		wishFuseAction.pushMessageAndRefund(wishId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    /***
     * 被投诉，推送告诉支持者心愿违规。
     * @param wishId
     * @return
     */
    public void wishComplaint(String wishId) {
    	try {
    		wishFuseAction.pushMessage(wishId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }

    public void check60DayHasWishApply(Map map) {
    	try {
    		wishFuseAction.check60DayHasWishApply((String) map.get("wishId"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }
}