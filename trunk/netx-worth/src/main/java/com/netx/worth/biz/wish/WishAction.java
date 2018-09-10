package com.netx.worth.biz.wish;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.netx.common.router.dto.select.SelectNumResponseDto;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.wz.dto.wish.*;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.model.*;
import com.netx.worth.service.*;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.utils.money.Money;
import com.netx.worth.enums.WishRefereeStatus;
import com.netx.worth.enums.WishStatus;

/**
 * <p>
 * 心愿表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class WishAction{
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private WishService wishService;


    /*支持*/
    public Boolean support(String id,String userId,BigDecimal amount) {
        boolean success = false;
        Money money = new Money(amount);
        Wish wish = wishService.selectById(id);
        if(wish!=null){
            if(userId.equals(wish.getUserId())){
                return null;
            }
            if (wish.getStatus()==4) {
                WishSupport wishSupport = createWishSupport(id, userId, money);
                success = worthServiceprovider.getWishSupportService().create(wishSupport);
                if (success) {
                    wish.setCurrentAmount(wish.getCurrentAmount() + money.getCent());
                    wishService.updateById(wish);
                }else {
                    throw new RuntimeException("只有处于推荐成功状态才可以支持该心愿");
                }
            }
        }
        return success;
    }

    private WishSupport createWishSupport(String wishId,String userId,Money amount){
        WishSupport wishSupport = new WishSupport();
        wishSupport.setWishId(wishId);
        wishSupport.setUserId(userId);
        wishSupport.setAmount(amount.getCent());
        wishSupport.setCreateUserId(userId);
        wishSupport.setPay(true);
        return wishSupport;

    }

    /**取消心愿!*/
    public boolean cancel(String id, String userId) {
        return worthServiceprovider.getWishService().cancel(id, userId);
    }
    
    public Map<String, Object> getApplyListByWish(String wishId, Page<WishApply> page) {
        Map<String, Object> map = new HashMap<>();
        List<WishApply> list = worthServiceprovider.getWishApplyService().getApplyListByWish(wishId, page);
        if(list != null && list.size() > 0) {
        	for (WishApply wishApply : list) {
        		wishApply.setPic(addImgUrlPreUtil.addImgUrlPre(wishApply.getPic(), AliyunBucketType.ActivityBucket));
			}
        }
        Wish wish = selectById(wishId);
        if(wish != null) {
        	wish.setWishImagesUrl(addImgUrlPreUtil.addImgUrlPre(wish.getWishImagesUrl(), AliyunBucketType.ActivityBucket));
        	wish.setWishImagesTwoUrl(addImgUrlPreUtil.addImgUrlPre(wish.getWishImagesTwoUrl(), AliyunBucketType.ActivityBucket));
        }
        List<WishAuthorize> wishAuthorizes = worthServiceprovider.getWishAuthorizeService().getListByWishApplyIds(list.stream().map(WishApply::getId).collect(Collectors.toList()));
        map.put("list", list);
        map.put("wish", wish);
        map.put("wishAuthorizeHash", wishAuthorizes.stream().collect(Collectors.groupingBy(WishAuthorize::getWishApplyId)));
        return map;
    }
    /**编辑心愿*/
    public boolean edit(WishPublishDto wishPublishDto) {
        String[] refereeUserIds =wishPublishDto.getRefereeIds().split(",");
        WishReferee wishReferee;
        for (int i = 0;i<refereeUserIds.length;i++){
            wishReferee = worthServiceprovider.getWishRefereeService().queryReferee(wishPublishDto.getId(),refereeUserIds[i]);
            if(wishReferee==null){
                worthServiceprovider.getWishRefereeService().createReferee(wishPublishDto.getId(),refereeUserIds[i]);
            }
        }
        Wish wish = worthServiceprovider.getWishService().selectById(wishPublishDto.getId());
        Date now = new Date();
        VoPoConverter.copyProperties(wishPublishDto, wish);
        wish.setExpiredAt(new Date(now.getTime() + 3600l * 24 * 30 * 1000));
        wish.setUpdateTime(now);
        wish.setStatus(WishStatus.PUBLISHED.status);
        wish.setAmount(new Money(wishPublishDto.getAmount()).getCent());
        wish.setWishImagesUrl(wishPublishDto.getPic());

        if(wish==null){
            throw new RuntimeException("修改的心愿不存在");
        }
        if(wish.getStatus()!=null && wish.getStatus()!=WishStatus.PUBLISHED.status) {
            //只有状态为1的能修改
            throw new RuntimeException("当前心愿状态不能修改");
        }
        return worthServiceprovider.getWishService().wish(wish);
    }
    /**推荐心愿*/
    public boolean refereeAccept(WishRefereeDto wishRefereeDto) {
        return updateRefereeRefuse(wishRefereeDto,true);
    }

    private boolean updateRefereeRefuse(WishRefereeDto wishRefereeDto,boolean isReferee){
        WishReferee wishReferee = worthServiceprovider.getWishRefereeService().selectById(wishRefereeDto.getId());
        if(wishReferee==null){
            throw new RuntimeException("此心愿推荐不存在");
        }
        WishRefereeStatus wishRefereeStatus;
        Wish wish = worthServiceprovider.getWishService().selectById(wishReferee.getWishId());
        if(isReferee){
            wishRefereeStatus = WishRefereeStatus.ACCEPT;
            wishReferee.setUpdateUserId(wishRefereeDto.getUserId());
            wishReferee.setDescription(wishRefereeDto.getDescription());
            wish.setStatus(WishStatus.UNDERWAY.status);

        }else {
            wishRefereeStatus = WishRefereeStatus.REFUSE;
            wishReferee.setDescription(wishRefereeDto.getDescription());
            wishReferee.setUpdateUserId(wishRefereeDto.getUserId());

        }
        if(wishReferee.getStatus().equals(wishRefereeStatus.status)){
            throw new RuntimeException("你已处理过此心愿推荐请求!");
        }
        wishReferee.setStatus(wishRefereeStatus.status);
        boolean flag = worthServiceprovider.getWishRefereeService().updateById(wishReferee);

        if(wish!=null){
            if(isReferee){
                wish.setRefereeAcceptCount(worthServiceprovider.getWishRefereeService().countRefereeByStatus(wish.getId(),wishRefereeStatus));
                if(wish.getRefereeAcceptCount()==3){
                    wish.setLock(false);
                }
            }else{
                wish.setRefereeRefuseCount(worthServiceprovider.getWishRefereeService().countRefereeByStatus(wish.getId(),wishRefereeStatus));
            }
            worthServiceprovider.getWishService().updateById(wish);

        }
//
        List<WishReferee> list = worthServiceprovider.getWishRefereeService().selectStasusByWishId(wishReferee.getWishId());
        double acceptCount = 0; // 记录同意人数
        double refuseCount = 0;//记录拒绝人数

        //如被邀请者中达到或超过50%的人拒绝推荐，则心愿发起失败，发起人的信用值扣减5分
        for (WishReferee wishReferee2 : list) {
            //如果状态为同意  计数+1
            if (wishReferee2.getStatus().equals(WishRefereeStatus.ACCEPT.status)) {
                acceptCount += 1;
            }
            //如果状态为弃权或者状态为拒绝  计数+1
            if (wishReferee2.getStatus().equals(WishRefereeStatus.REFUSE.status)) {
                refuseCount += 1;
            }
        }
       // double result = refuseCount/ list.size();
/*        // 若拒绝人数大于 50% 或者(当只有3个推荐人拒绝人数不为0时)，则心愿变为推荐失败状态，否则，心愿变为成功状态
        if ( refuseCount/ list.size() > 0.5 || (list.size ()==3&&refuseCount!=0)||(list.size ()==4&&refuseCount>1)) {
            //if(waitCount +== 0)//如果不存在待确认人数  更改心愿状态  否则不更改状态
            wish.setStatus(WishStatus.CLOSE.status);

        } else {
            wish.setStatus(WishStatus.REFEREE_SUCCESS.status);
            settlementAction.settlementCreditRightNow("推荐人小于50%，心愿发起失败", "Wish", wishReferee.getWishId(), wish.getUserId(), -5);
        }*/
        if(list.size ()<6){
            if(acceptCount >= 3 ){
                wish.setStatus(WishStatus.REFEREE_SUCCESS.status);
                worthServiceprovider.getWishService().updateById(wish);
            }
            if(list.size ()== 3 && refuseCount >= 1){
                wish.setStatus(WishStatus.CLOSE.status);
                worthServiceprovider.getWishService().updateById(wish);
            }
            if(list.size () == 4 && refuseCount >= 2){
                wish.setStatus(WishStatus.CLOSE.status);
                worthServiceprovider.getWishService().updateById(wish);
            }
            if(list.size ()==5 && refuseCount >= 3){
                wish.setStatus(WishStatus.CLOSE.status);
                worthServiceprovider.getWishService().updateById(wish);
            }
        }else {
            if(acceptCount / list.size () >= 0.5 ){
                wish.setStatus(WishStatus.REFEREE_SUCCESS.status);
                worthServiceprovider.getWishService().updateById(wish);
            }else if(refuseCount / list.size () >= 0.5 ){
                wish.setStatus(WishStatus.CLOSE.status);
                worthServiceprovider.getWishService().updateById(wish);
            }
        }
        worthServiceprovider.getWishService().updateById(wish);
//

        return flag;
    }

    /**拒绝推荐*/
    public boolean refereeRefuse(WishRefereeDto wishRefereeDto) {
        return updateRefereeRefuse(wishRefereeDto,false);
    }

    /**提现成功*/
    public boolean withdrawalSuccess(WishWithdrawalDto withdrawalDto) {
        return worthServiceprovider.getWishHistoryService().withdrawalSuccess(withdrawalDto);
    }
    /**提现失败*/
    public boolean withdrawalFailure(WishWithdrawalDto withdrawalDto) {
        return worthServiceprovider.getWishHistoryService().withdrawalFailure(withdrawalDto);
    }
    
    public Map<String, Object> getWishByUserIds(List<String> userIds, Page<Wish> page) {
        Map<String, Object> map = new HashMap<>();
        List<Wish> list = worthServiceprovider.getWishService().getWishByUserIds(userIds,page);
        if (list != null) {
            map.put("list", list);
            map.put("wishSupportCount", worthServiceprovider.getWishSupportService().getSupportCountByWish(list.get(0).getId()));
        }
        return map;
    }


    /**拒绝用款*/
    public boolean refuseApply(String applyId, String userId, String description) {
        return worthServiceprovider.getWishAuthorizeService().refuseApply(applyId, userId, description);
    }
    /**推荐的心愿列表*/
    public List<WishReferee> getRefereeListByWish(String wishId, String userId, Page<WishReferee> page) {
        return worthServiceprovider.getWishRefereeService().getRefereeListByWish(wishId, userId, page);
    }

    /**监管的心愿列表*/
    public List<WishManager> getManagerListByUserId(String userId, Page<WishManager> page) {
        return worthServiceprovider.getWishManagerService().getManagerListByUserId(userId, page);
    }

    /**我的银行信息*/
    public List<WishBank> bankListById(String userId, Page<WishBank> page) {
        return worthServiceprovider.getWishBankService().getBankList(userId, page);
    }

    /**检查用户是否有未完成心愿*/
    public boolean checkHasUnComplete(String userId) {
    	//查询该用户发布的心愿中状态为“已发布”、“推荐成功”、“筹集目标达成，即心愿发起成功”心愿。
		List<Wish> wishList = worthServiceprovider.getWishService().getUnComplete(userId);
		if(wishList != null && wishList.size() > 0) {
			return true;
		}
    	return false;
    }

    
    
    @Transactional()
	public boolean clean(String userId) throws Exception {
    	List<Wish> list = worthServiceprovider.getWishService().getWishByUserId(userId);
    	List<String> wishIds = list.stream().map(Wish::getId).collect(toList());
    	boolean cleanSettlment = true;
    	boolean cleanWish = true;
    	//没有数据直接返回false
    	if(wishIds != null && wishIds.size() > 0 ) {
    		//删除结算表和子表
			List<Settlement> settlementList = worthServiceprovider.getSettlementService().getSettlementListByTypeAndId("Wish", wishIds);
			List<String> settlementIds = settlementList.stream().map(Settlement::getId).collect(toList());
			cleanSettlment = worthServiceprovider.getSettlementService().deleteSettlementListByTypeAndId("Wish", wishIds) && settlementAction.clean(settlementIds);
    		cleanWish = worthServiceprovider.getWishService().deleteWishByUserId(userId);
    	}
		

		boolean cleanWishApply = worthServiceprovider.getWishApplyService().deleteWishApplyByUserId(userId);
		
		boolean cleanWishSupport = worthServiceprovider.getWishSupportService().deleteWishSupportByUserId(userId);
		
		boolean cleanWishReferee = worthServiceprovider.getWishRefereeService().deleteWishSupportByUserId(userId);
		
		boolean cleanWishAuthorize = worthServiceprovider.getWishAuthorizeService().deleteWishAuthorizeByUserId(userId);
		
		boolean cleanWishManager = worthServiceprovider.getWishManagerService().deleteWishManagerByUserId(userId);
		
		boolean success = cleanWishReferee && cleanWishApply && cleanWish && cleanWishReferee && cleanWishAuthorize && cleanWishManager && cleanSettlment && cleanWishSupport;
		if(success) {
			return success;
		}else {
			logger.error("清除该用户心愿的数据失败");
			throw new RuntimeException();
		}
	}
    /**心愿详情*/
    public Wish selectById(String id) {
        return worthServiceprovider.getWishService().selectById(id);
    }
    /**发布的心愿*/
    public Wish publishListById(String id) {
        return worthServiceprovider.getWishService().selectById(id);
    }



/**心愿授权表*/
    public boolean createList(List<WishManager> managers, String wishApplyId, String userId) {
        List<WishAuthorize> list = new ArrayList<>();
        managers.forEach(wishManager -> {
            WishAuthorize wishAuthorize = new WishAuthorize();
            wishAuthorize.setCreateUserId(userId);
            wishAuthorize.setUserId(wishManager.getUserId());
            wishAuthorize.setWishApplyId(wishApplyId);
            list.add(wishAuthorize);
        });
        return worthServiceprovider.getWishAuthorizeService().insertBatch(list);
    }

    /**
     * 获取心愿通过id
     * @param id
     * @return
     */
    public Wish getWishById(String id){

        Wish wish = wishService.selectById(id);
        if (wish!=null){
            //处理图片
            wish.setWishImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesUrl (), AliyunBucketType.ActivityBucket ) );
            wish.setWishImagesTwoUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesTwoUrl (), AliyunBucketType.ActivityBucket ) );
            //处理金额
            if(wish.getAmount ()!=null) {
                wish.setAmount ( Money.CentToYuan ( wish.getAmount () ).getAmount ().longValue ());
            }
            if(wish.getCurrentAmount ()!=null) {
                wish.setCurrentAmount ( Money.CentToYuan ( wish.getCurrentAmount () ).getAmount ().longValue ());
            }
            if(wish.getCurrentApplyAmount ()!=null) {
                wish.setCurrentApplyAmount ( Money.CentToYuan ( wish.getCurrentApplyAmount () ).getAmount ().longValue ());
            }
        }
        return wish;
    }
}

