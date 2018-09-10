package com.netx.worth.biz.settlement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netx.worth.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.netx.worth.model.Invitation;
import com.netx.worth.model.Meeting;
import com.netx.worth.model.Settlement;
import com.netx.worth.model.Wish;
import com.netx.worth.model.WishSupport;
import com.netx.worth.util.ArithUtil;

/**
 * <p>
 * 网值结算表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-10-05
 */
@Service
public class SettlementAction{
    /**
     * 收入
     */
    public static final int IN = 1;
    /**
     * 支出
     */
    public static final int OUT = 0;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private SettlementAmountAction settlementAmountAction;
    @Autowired
    private SettlementCreditAction settlementCreditAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    

    @Transactional(rollbackFor = Exception.class)
    public Settlement create(String description, String relatableType, String relatableId, Boolean isCan, Long expiredAt) {
        Settlement initSettlement = getInit(relatableType, relatableId);
        if (initSettlement != null) {
            return initSettlement;
        }
        Settlement settlement = new Settlement();
        settlement.setDescription(description);
        settlement.setRelatableType(relatableType);
        settlement.setRelatableId(relatableId);
        settlement.setCan(isCan);
        settlement.setExpiredAt(new Date(expiredAt));
        boolean success = worthServiceprovider.getSettlementService().insert(settlement);
        if(!success) {
        	logger.error("创建接受记录失败");
        	throw new RuntimeException();
        }
        return settlement;
    }

    
    @Transactional(rollbackFor = Exception.class)
    public boolean settlementAmountRightNow(String description, String relatableType, String relatableId, String userId, BigDecimal amount) {
        Settlement settlement = create(description, relatableType, relatableId, true, new Date().getTime());
        boolean success = settlement != null && StringUtils.hasText(settlement.getId());
        if (!success) return false;
        success = settlementAmountAction.create(settlement.getId(), amount, userId);
        if (!success) {
        	logger.error("创建结算金额记录失败");
        	throw new RuntimeException();
        }
        return success;
    }

    //信用分扣减
    @Transactional(rollbackFor = Exception.class)
    public boolean settlementCredit(String relatableType, String relatableId, String userId, Integer credit) {
        Settlement settlement = selectNotCanByTypeAndId(relatableType, relatableId);
        if (settlement == null) return false;
        return settlementCreditAction.create(settlement.getId(), credit, userId);
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean settlementCreditRightNow(String description, String relatableType, String relatableId, String userId, Integer credit){
        Settlement settlement = create(description, relatableType, relatableId, true, new Date().getTime());
        boolean success = settlement != null && StringUtils.hasText(settlement.getId());
        if (!success) return false;
        success = settlementCreditAction.create(settlement.getId(), credit, userId);
        // 远程调用用户中心接口，将用户的信用扣掉
        this.settlementCredit(relatableType, relatableId, userId, credit);
        if (!success) throw new RuntimeException();
        return success;
    }

    /**
     * 查询是否存在isCan=false的，并且relatableType和relatableId等于该值的，如果不存在就不插入
     *
     * @param relatableType
     * @param relatableId
     * @return
     */
    private Settlement selectNotCanByTypeAndId(String relatableType, String relatableId) {
        return worthServiceprovider.getSettlementService().selectNotCanByTypeAndId(relatableType,relatableId);
    }

    public void makeCanSettlement(Settlement settlement) {
        settlement.setCan(true);
    }

    public boolean finish(Settlement settlement) {
        settlement.setFinish(true);
        settlement.setFinishAt(new Date());
        return worthServiceprovider.getSettlementService().updateById(settlement);
    }

    public List<Settlement> getAvailableSettlement() {
        return worthServiceprovider.getSettlementService().getAvailableSettlement();
    }

    
    public boolean transfer(String fromUserId, String toUserId, BigDecimal amount, String description, String relatableType, String relatableId) throws Exception {
        settlementAmountRightNow(description, relatableType, relatableId, fromUserId, ArithUtil.unAbs(amount));
        settlementAmountRightNow(description, relatableType, relatableId, toUserId, ArithUtil.abs(amount));
        return true;
    }

    
    public Settlement getInit(String relatableType, String relatableId) {
        return worthServiceprovider.getSettlementService().getInit(relatableType,relatableId);
    }

    
    public Map<String,Object> getTradingVolume(String userId) {
        //获取上个月的第一天和最后一天
        Calendar c = Calendar.getInstance();
        GregorianCalendar calendar = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        calendar.set(calendar.DAY_OF_MONTH, 1);
        long firstDay = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, -1);// 获取上个月月份
        long lastDay = calendar.getTimeInMillis();
        Map tradingVolumeMap = new HashMap();
        //上个月活动赚取的报酬
        List<Meeting> meetingList = worthServiceprovider.getMeetingService().getSuccessMeeting(userId,firstDay,lastDay);
        Long meetingRemuneration = new Long(0);
        for (Meeting meeting : meetingList) {
        	if(meeting.getOrderPrice().doubleValue() > 0) {//有消费
        		meetingRemuneration += (meeting.getAllRegisterAmount() - (meeting.getOrderPrice()));
        	}
        }
        
        tradingVolumeMap.put("meetingRemuneration", meetingRemuneration);

        //上个月接收邀请获得的报酬
        List<Invitation> invitationList = worthServiceprovider.getInvitationService().getInvitationList(firstDay, lastDay, userId);
        Long invitationRemuneration = new Long(0);
        for (Invitation invitation : invitationList) {
            invitationRemuneration += invitation.getAmout();
        }
        tradingVolumeMap.put("invitationRemuneration", invitationRemuneration);

        //上个月用户打赏获取的收入
        List<Wish> wishList = worthServiceprovider.getWishService().getComplete(userId);
        Long rewardIncome = new Long(0);
        List<String> wishIds = new ArrayList<>();
        for (Wish wish : wishList) {
            wishIds.add(wish.getId());
        }
        List<WishSupport> WishSupports = worthServiceprovider.getWishSupportService().getSupportMonth(wishIds, firstDay, lastDay);
        for (WishSupport wishSupport : WishSupports) {
            rewardIncome += wishSupport.getAmount();
        }
        tradingVolumeMap.put("rewardIncome", rewardIncome);
        return tradingVolumeMap;
    }
    
    
	@Transactional
	public boolean clean(List<String> settlementIds) throws Exception {
		boolean cleanSettlementAmount = worthServiceprovider.getSettlementAmountService().cleanSettlementAmount(settlementIds);
		
		boolean cleanSettlementCredit = worthServiceprovider.getSettlementCreditService().cleanSettlementCredit(settlementIds);;
		
		boolean cleanSettlementLog = worthServiceprovider.getSettlementLogService().cleanSettlementLog(settlementIds);
		boolean success = cleanSettlementLog && cleanSettlementCredit && cleanSettlementAmount;
		if(success) {
			return success;
		}else {
			logger.error("删除结算子表失败");
			throw new RuntimeException();
		}
	}
}
