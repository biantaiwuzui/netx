package com.netx.fuse.biz.worth;

import static com.netx.common.common.enums.PushMessageDocTypeEnum.LuckyMoneyDetail;
import static com.netx.common.common.enums.PushMessageDocTypeEnum.WZ_WISHDETAIL;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.netx.common.common.enums.*;
//import com.netx.common.user.dto.divideManagement.SelectUserByUserNumberResponseDto;
import com.netx.common.common.vo.message.GroupDto;
import com.netx.common.router.dto.select.SelectNumResponseDto;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.vo.currency.WzCommonOtherSetResponseDto;
import com.netx.common.wz.dto.wish.*;
import com.netx.fuse.biz.FuseBaseAction;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.fuse.client.ucenter.OtherSetClientAction;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import com.netx.ucenter.biz.common.GroupAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.UserService;
import com.netx.ucenter.service.user.UserServiceProvider;
import com.netx.utils.DistrictUtil;
import com.netx.utils.datastructures.Tuple;
import com.netx.utils.money.Money;
import com.netx.utils.time.DateUtils;
import com.netx.worth.enums.WishApplyType;
import com.netx.worth.enums.WishRefereeStatus;
import com.netx.worth.enums.WishStatus;
import com.netx.worth.enums.WishApplyStatus;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.model.WishSearchResponse;
import com.netx.searchengine.query.WishSearchQuery;
import com.netx.searchengine.service.WishSearchService;
import com.netx.worth.model.*;
import com.netx.worth.service.*;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.fuse.client.ucenter.WalletForzenClientAction;
import com.netx.fuse.proxy.EvaluateProxy;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.biz.wish.WishAction;

@Service
public class WishFuseAction extends FuseBaseAction {

    private Logger logger = LoggerFactory.getLogger ( this.getClass ().getName () );

    @Autowired
    private UserClientProxy userClientProxy;
    @Autowired
    private MessagePushProxy messagePushProxy;
    @Autowired
    private WishAction wishAction;
    @Autowired
    private WalletForzenClientAction walletForzenClientAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private OtherSetClientAction otherSetClientAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private UserServiceProvider userServiceProvider;
    @Autowired
    private WishSearchService wishSearchService;
    @Autowired
    private NetEnergyFuseAction netEnergyFuseAction;
    @Autowired
    private WalletBillClientAction walletBillClientAction;
    @Autowired
    private WishService wishService;
    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;

    @Autowired
    private JobFuseAction jobFuseAction;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupAction groupAction;

    /**
     * 发布心愿-含有定时任务
     */
    @Transactional
    public Wish publish(WishPublishDto wishPublishDto) {
        //判断是否可以发布心愿
        WzCommonOtherSetResponseDto otherSetResult = otherSetClientAction.queryRemote ();
        if (otherSetResult == null) {
            throw new RuntimeException ( "远程其他设置查询失败" );
        }
        Tuple<Boolean, String> tuple = this.booleanIsCanReleaseTuple ( wishPublishDto.getUserId (), otherSetResult );
        if (!tuple.left ()) {
            throw new RuntimeException ( tuple.right () );
        }
        boolean success = false;
        // 判断是否有重复的心愿
        Integer count = worthServiceprovider.getWishService ().getSameWishList ( wishPublishDto );
        if (count != null && count > 0) {
            throw new RuntimeException ( "心愿发布有重复!" );
        }
        //判断是否在邀请自己
        for (String repeat : wishPublishDto.getRefereeIds ().split ( "," ))
            if (repeat.equals ( wishPublishDto.getUserId () )) {
                throw new RuntimeException ( "不可以邀请自己作为推荐人!" );
            }
        wishPublishDto.setId ( null );
        // 发布心愿
        Wish wish = new Wish ();
        Date now = new Date ();
        VoPoConverter.copyProperties ( wishPublishDto, wish );
        wish.setCreateTime ( now );
        wish.setExpiredAt ( new Date ( now.getTime () + 3600l * 24 * 30 * 1000 ) );
        wish.setWishImagesUrl ( (wishPublishDto.getPic ()) );
        wish.setWishImagesTwoUrl ( (wishPublishDto.getPic2 ()) );
        wish.setStatus ( WishStatus.PUBLISHED.status );
        wish.setAmount ( new Money ( wishPublishDto.getAmount () ).getCent () );
        if (!worthServiceprovider.getWishService ().wish ( wish )) {
            return null;
        }
        logger.info ( "wishId:" + wishPublishDto.getId () + "userId:" + wishPublishDto.getUserId () + "，title:"
                + wishPublishDto.getTitle () + "，description:" + wishPublishDto.getDescription () );

        // 创建日志
        /*activeLogAction.create(wishPublishDto.getUserId(), "Wish", wishPublishDto.getId(), "发布了心愿",
                wishPublishDto.getLon(), wishPublishDto.getLat());*/
        // 邀请好友推荐
        if (StringUtils.isNotBlank ( wishPublishDto.getRefereeIds () )) {
            //推送消息
            String[] stringarr = wishPublishDto.getRefereeIds ().split ( "," );
            success = worthServiceprovider.getWishRefereeService ().createReferee ( wish.getId (), stringarr );
            for (int i = 0; i < stringarr.length; i++) {
                wzCommonImHistoryAction.add (
                        wishPublishDto.getUserId (), stringarr[i],
                        "有心愿需要你推荐", wish.getId (),
                        MessageTypeEnum.ACTIVITY_TYPE, WZ_WISHDETAIL, null );
            }
            logger.info ( "createReferee's refereeIds:" + wishPublishDto.getRefereeIds () );

            if (!success) {
                logger.error ( "邀请好友推荐失败" );
                messagePushProxy.messagePushJump ( MessageTypeEnum.ACTIVITY_TYPE, "邀请好友推荐失败", "邀请好友推荐失败", wishPublishDto.getUserId (), WZ_WISHDETAIL, wish.getId () );
                throw new RuntimeException ( "邀请好友推荐失败" );
            }
        }

        //为心愿建一个心愿群
        worthServiceprovider.getWishGroupService ().createGroup ( wish.getId () );

        // 结算
        Settlement settlement = settlementAction.create ( "初始化", "Wish", wish.getId (), false,
                wish.getExpiredAt ().getTime () + 25L * 3600 * 1000 );
        success = settlement != null && StringUtils.isNotBlank ( settlement.getId () );
        if (!success) {
            logger.error ( "初始化结算失败" );
            throw new RuntimeException ( "初始化结算失败" );
        }
//


//        /**检测好友推荐定时任务，心愿发起24小时*/
//        success = jobFuseAction.addJob(JobEnum.WISH_CHECK_REFEREE_JOB,wish.getId(),wish.getId(),wish.getTitle()+"检查心愿发起24小时后是否完成推荐",new Date(wish.getCreateTime().getTime() + 86400000), AuthorEmailEnum.WEN_YI);
//        if (!success) {
//            logger.error("create timed task -> checkRefereeSuccess error");
//           throw new RuntimeException("创建“检测好友推荐”定时任务失败");
//        }
//        /**检测是否发起成功定时任务-30天*/
//        success = jobFuseAction.addJob(JobEnum.WISH_CHECK_SUCCESS_JOB,wish.getId(),wish.getId(),wish.getTitle()+"检测是否发起成功",wish.getExpiredAt(), AuthorEmailEnum.WEN_YI);
//        if (!success) {
//            logger.error("create timed task -> checkSuccess error");
//            throw new RuntimeException("创建“检测发起成功”定时任务失败");
//
//        }
//        /**检测是否双方已评价定时任务*/
//        success = jobFuseAction.addJob(JobEnum.WISH_CHECK_EVALUATE_JOB,wish.getId(),wish.getId(),wish.getTitle()+"检测是否双方已评价",wish.getExpiredAt(), AuthorEmailEnum.WEN_YI);
//        if (!success) {
//            logger.error("create timed task -> checkWishEvaluate error");
//            throw new RuntimeException("创建“检测是否双方已评价”定时任务失败");
//
//        }

        return wish;
    }


    /**
     * 申请用款-含有定时任务
     */
    @Transactional
    public WishApplyReceiveDto apply(WishApplyDto wishApplyDto, WishBankDto wishBankDto) {
        WishApplyReceiveDto wishApplyReceiveDto = new WishApplyReceiveDto ();
        VoPoConverter.copyProperties ( wishApplyDto, wishApplyReceiveDto );
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishApplyDto.getWishId () );
        /*if (wish!=null) {
            wish.setCurrentApplyAmount(wish.getCurrentApplyAmount() + wishApplyDto.getAmount().longValue()*100);
            worthServiceprovider.getWishService().updateById(wish);
        }*/
        /** 判断是否存在监管者 **/
        List<WishManager> managers = worthServiceprovider.getWishManagerService ().getManagerByWish ( wishApplyReceiveDto.getWishId () );
        if (managers.size () <= 0) {
            throw new RuntimeException ( "没有监管者" );
        }
        /** 检查是否有未完成的心愿 **/
        boolean allPass = worthServiceprovider.getWishApplyService ().isPass ( wishApplyDto.getWishId () );
        if (allPass) {
            throw new RuntimeException ( "有待通过审核的用款申请,请审核结束后再申请用款" );
        }
        if (wishApplyDto.getAmount () == null || wishApplyDto.getAmount ().doubleValue () <= 0) {
            throw new RuntimeException ( "申请金额必须大于0" );
        }
        if (wishApplyDto.getAmount ().doubleValue () > wish.getCurrentAmount ()) {
            throw new RuntimeException ( "申请金额不能大于筹集数" );
        }
//        UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg(wishApplyDto.getUserId());
////        if(!userInfoAndHeadImg.getId().equals(wishApplyDto.getUserId())){
////            String a = userInfoAndHeadImg.getId();
////            String b = wishApplyDto.getUserId();
////            throw new Exception("收款者信息有误，请检查收款者网号和昵称是正确！");
////        }

        if (wishApplyDto.getApplyType () == 2) {
            try {
                {
                    //判断是否可以发布银行信息
                    WzCommonOtherSetResponseDto otherSetResult = otherSetClientAction.queryRemote ();
                    Tuple<Boolean, String> tuple = this.booleanIsCanReleaseTuple ( wishBankDto.getUserId (), otherSetResult );
                    if (!tuple.left ()) {
                        throw new RuntimeException ( tuple.right () );
                    }
                    //	发布是不能有id的
                    wishBankDto.setId ( null );
                    //    填信息
                    WishBank wishBank = new WishBank ();
                    Date now = new Date ();
                    VoPoConverter.copyProperties ( wishBankDto, wishBank );
                    wishBank.setCreateTime ( now );
                    if (!worthServiceprovider.getWishBankService ().WishBank ( wishBank )) {
                    }
                    logger.info ( "wishId:" + wishBankDto.getId () + "userId:" + wishBankDto.getUserId () + "，depositBank:"
                            + wishBankDto.getDepositBank () + "，accountName:" + wishBankDto.getAccountName () + "，mobile:" + wishBankDto.getMobile () + "，account:" + wishBankDto.getAccount () );

                    //创建心愿历史信息
                    worthServiceprovider.getWishHistoryService ().createHistory ( wishApplyReceiveDto );
                }
                messagePushProxy.messagePush ( MessageTypeEnum.ACTIVITY_TYPE, "收款方的信息未经网值验证，请注意资金使用风险", "温馨提示", wishApplyDto.getUserId (),
                        WZ_WISHDETAIL.getValue (), wishApplyDto.getWishId () );
            } catch (Exception e) {
                logger.error ( "推送提醒失败", e );
            }
        }

        /** 创建心愿使用 **/
        WishApply wishApply = new WishApply ();
        VoPoConverter.copyProperties ( wishApplyDto, wishApply );
        wishApply.setAmount ( wishApplyDto.getAmount ().multiply ( new BigDecimal ( 100 ) ).longValue () );
        wishApply.setBalance ( wish.getCurrentAmount () - wish.getCurrentApplyAmount () );
        List<WishManager> managerList = worthServiceprovider.getWishManagerService ().getManagerByWish ( wishApplyDto.getWishId () );
        wishApply.setManagerCount ( managerList.size () );
        if (wishApply.getBalance () <= 0) {
            throw new RuntimeException ( "心愿余额不足，申请心愿失败" );
        }

        worthServiceprovider.getWishApplyService ().create ( wishApply );

        if (wishApply.getIsPass () != null) {
            if (wishApply.getIsPass ().equals ( 2 )) {
                throw new RuntimeException ( "当前有未经过审核的用款申请，请审核结束后再申请用款" );
            }
        }

        boolean success = false;
        if (wishApply == null) {
            throw new RuntimeException ( "创建心愿失败" );
        }
        if (!wishAction.createList ( managers, wishApply.getId (), wishApply.getUserId () )) {
            throw new RuntimeException ( "创建监管者失败" );
        }

        Calendar calendar = Calendar.getInstance ();
        calendar.add ( Calendar.HOUR_OF_DAY, 12 );
        if (wish.getCurrentAmount () - (wish.getCurrentApplyAmount ()) > 0) {// 还有余额
//            /**创建检查60天内有无新的用款申请的定时任务*/
//          success = jobFuseAction.addJob(JobEnum.WISH_CHECK_sixtyDay_JOB, wish.getId(), wish.getId(), wish.getTitle()+"检查60天内有无新的用款申请", new Date(wish.getExpiredAt().getTime()+5184*1000000), AuthorEmailEnum.WEN_YI);
//            if (!success) {
//                logger.error("创建检查60天内有无新的用款申请的定时任务失败");
//                throw new RuntimeException("创建检查60天内有无新的用款申请的定时任务失败");
//            }
//              /**12小时后自动批准用款--没做逻辑*/
//            success = jobFuseAction.addJob(JobEnum.WISH_TWELVEHOURS_AUTO_JOB, wish.getId(), wish.getId(), wish.getTitle(), new Date(wish.getExpiredAt().getTime()+43200000), AuthorEmailEnum.WEN_YI);
//            if (!success) {
//                logger.error("创建“12小时后自动批准用款”定时任务失败");
//                throw new RuntimeException();
//            }
        }
        return wishApplyReceiveDto;
    }

    /**
     * 审批用款
     */
    public WishExamineDto examine(String wishId, String userId) throws Exception {
        /* 第一步：找钱 */
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        Integer wishManager = worthServiceprovider.getWishManagerService ().getManagerCount ( wishId, userId );
        if (wishManager == null || wishManager == 0) {
            logger.error ( userId + "不是心愿管理者,不能审批" );
            throw new RuntimeException ( "不是心愿管理者,不能审批" );
        }
        WishApply wishApply = worthServiceprovider.getWishApplyService ().getWishApplybyWishId ( wishId, wish.getUserId () );
        if (wishApply == null) {
            logger.error ( wishId + "该用款不存在" );
            throw new RuntimeException ( "该用款不存在" );
        }
        WishExamineDto wishExamineDto = new WishExamineDto ();
        wishApply.setPic ( addImgUrlPreUtil.addImgUrlPres ( wishApply.getPic (), AliyunBucketType.ActivityBucket ) );
        Money money = Money.CentToYuan ( wishApply.getAmount () );
        //wishApply.setAmount ( money.getAmount ().doubleValue () );
        if (wishApply == null) {
            logger.error ( wishId + "未找到申请用款信息" );
            throw new RuntimeException ( "未找到申请用款信息" );
        }
//        if(moneyDetail.getApplyType()==2){/* 个人银行信息 */
//            WishBank BankDetail = worthServiceprovider.getWishBankService().getWishBankUserIdAndWishId(wishBankId,userId);
//            if(BankDetail == null){
//                throw new RuntimeException("未找到银行信息");
//            }
//            VoPoConverter.copyProperties(BankDetail,wishExamineDto);
//        }else{
        String ApplyInfoId = userService.getUserIdByUserNumber ( wishApply.getApplyInfo () );
        UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg ( ApplyInfoId );
        if (userInfoAndHeadImg == null) {
            logger.error ( wishId + "未找到申请用款信息" );
            throw new RuntimeException ( "未找到申请者信息" );
        }
        VoPoConverter.copyProperties ( userInfoAndHeadImg, wishExamineDto );
//        }
        VoPoConverter.copyProperties ( wishApply, wishExamineDto );
        wishExamineDto.setAmount ( money.getAmount ());
        wishExamineDto.setApplyId ( wishApply.getId () );

        return wishExamineDto;
    }

    /**
     * 定时任务：是否评价
     */
    public void checkEvaluate(String wishId) throws Exception {
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        if (wish != null) {
            List<WishSupport> wishSupports = worthServiceprovider.getWishSupportService ().getSupportListByWish ( wishId );
            List<String> users = new ArrayList<> ();
            wishSupports.forEach ( wishSupport -> {
                users.add ( wishSupport.getUserId () );
            } );
            users.add ( wish.getUserId () );
            List<String> list = new EvaluateProxy ().notEvaluateUsers ( users, wishId );
            list.forEach ( userId -> {
                settlementAction.settlementCredit ( "Wish", wishId, userId, -2 );
            } );
        } else {
            logger.error ( "检查是否评价失败,心愿ID不存在" );
            throw new RuntimeException ();
        }
    }

    /**批准用款 定时任务*/
  /*  @Transactional
    public boolean checkAcceptApply(String applyId){
        WishApply wishApply = worthServiceprovider.getWishApplyService().selectById(applyId);
        boolean success = worthServiceprovider.getWishAuthorizeService().acceptApply(applyId, wishApply.getUserId(), "定时24小时自动批准");
        double p = worthServiceprovider.getWishAuthorizeService().getAcceptPerecent(wishApply.getId());
        if (p > 2d / 3) {
            Wish wish = worthServiceprovider.getWishService().selectById(wishApply.getWishId());
            int applyType = wishApply.getApplyType();
            if (applyType != WishApplyType.OTHER.type) {//第三方审批通过需要平台自行安排转账事宜
                settlementAction.settlementAmountRightNow("心愿使用审批通过", "WishApply", wishApply.getId(), wishApply.getUserId(), Money.CentToYuan(wishApply.getAmount()).getAmount());
            }
            wish.setCurrentApplyAmount(wish.getCurrentAmount() + (wishApply.getAmount()));
            if (wish.getCurrentApplyAmount().doubleValue() >= wish.getCurrentAmount().doubleValue()) {
                wish.setStatus(WishStatus.COMPLETE.status);
            }
            worthServiceprovider.getWishService().updateById(wish);
            //wishApply.setPass(1);
            *//**long型的用款金额转BigDecimal型*//*
            BigDecimal totalFee = new BigDecimal(wishApply.getAmount());
            BigDecimal d100 = new BigDecimal(100);
            BigDecimal wishApplyAmount= totalFee.divide(d100,2,2);//小数点保留2位

            // 心愿集资托管到平台的资金，申请者要使用。
            BillAddRequestDto billAddRequestDto = new BillAddRequestDto ();
            billAddRequestDto.setPayChannel(3);
            billAddRequestDto.setType(1);
            billAddRequestDto.setAmount(wishApplyAmount);
            if (wishApply.getApplyType()==0) {
                billAddRequestDto.setDescription ( "申请者自提使用心愿资金" );
                billAddRequestDto.setToUserId ( wishApply.getUserId () );
            }
            else if (wishApply.getApplyType()==1) {
                billAddRequestDto.setDescription ( "申请者转账使用心愿资金" );
                billAddRequestDto.setToUserId ( userService.getUserIdByUserNumber ( wishApply.getApplyInfo () ) );
            }
            else if (wishApply.getApplyType()==2) {
                billAddRequestDto.setDescription ( "申请者通过第三方使用心愿资金" );

            }
            billAddRequestDto.setToUserId ( wish.getUserId () );
            Boolean successCommon = walletBillClientAction.addBill ( "999", billAddRequestDto );
            if (!successCommon) {
                logger.error ( "申请人使用心愿资金出现异常" );
                throw new RuntimeException ();
            }
            worthServiceprovider.getWishApplyService().updateById(wishApply);
        }
        return success;
    }*/


    /**批准用款*/
    /**
     * @param applyId
     * @param userId
     * @param description
     * @return
     * @throws Exception amount 希望筹集的金额
     *                   current_amount 当前筹集数
     *                   current_apply_amount 当前已使用金额
     *                   <p>
     *                   如果满足推荐条件，那么取心愿Id,获取心愿状态，如果心愿状态不需要第三方审批，那么心愿使用审批通过，然后取当前筹集数和当前已使用金额
     *                   参数，如果当前已使用金额大于等于当前筹集数那么返回已完成状态，然后更新wish表数据，设置pass状态为ture，然后更新wishapply表数据
     */
    @Transactional
    public boolean acceptApply(String applyId, String userId, String description) {
        WishApply wishApply = worthServiceprovider.getWishApplyService ().selectById ( applyId );

        /** 检查是否是待批准状态,如果是,则可批准,否则提示不可操作该用款 **/
        int allPass = worthServiceprovider.getWishAuthorizeService ().countiapplywaitByWish ( applyId );
        if (allPass < 1) {
            throw new RuntimeException ( "该申请用款已处理过!不可重复处理!" );
        } else {
            boolean success = worthServiceprovider.getWishAuthorizeService ().acceptApply ( applyId, userId, description );
            double p = worthServiceprovider.getWishAuthorizeService ().getAcceptPerecent ( wishApply.getId () );
            if (p > 2d / 3) {
                Wish wish = worthServiceprovider.getWishService ().selectById ( wishApply.getWishId () );
                int applyType = wishApply.getApplyType ();
                if (applyType != WishApplyType.OTHER.type) {//第三方审批通过需要平台自行安排转账事宜
                    settlementAction.settlementAmountRightNow ( "心愿使用审批通过", "WishApply", wishApply.getId (), wishApply.getUserId (), Money.CentToYuan ( wishApply.getAmount () ).getAmount () );
                }
                wish.setCurrentApplyAmount ( wish.getCurrentApplyAmount () + (wishApply.getAmount ()) );
                if (wish.getCurrentApplyAmount ().doubleValue () >= wish.getCurrentAmount ().doubleValue ()) {
                    wish.setStatus ( WishStatus.COMPLETE.status );
                }
                worthServiceprovider.getWishService ().updateById ( wish );

                /**long型的用款金额转BigDecimal型*/
                BigDecimal totalFee = new BigDecimal ( wishApply.getAmount () );
                BigDecimal d100 = new BigDecimal ( 100 );
                BigDecimal wishApplyAmount = totalFee.divide ( d100, 2, 2 );//小数点保留2位

                // 心愿集资托管到平台的资金，申请者要使用。
                BillAddRequestDto billAddRequestDto = new BillAddRequestDto ();
                billAddRequestDto.setPayChannel ( 3 );
                billAddRequestDto.setType ( 1 );
                billAddRequestDto.setAmount ( wishApplyAmount );
                if (wishApply.getApplyType () == 0) {
                    billAddRequestDto.setDescription ( "申请者自提使用心愿资金" );
                    //billAddRequestDto.setToUserId ( wishApply.getUserId () );
                } else if (wishApply.getApplyType () == 1) {
                    billAddRequestDto.setDescription ( "申请者转账使用心愿资金." );
                } else if (wishApply.getApplyType () == 2) {
                    billAddRequestDto.setDescription ( "申请者通过第三方使用心愿资金" );
                }
                String walletId = userService.getUserIdByUserNumber ( wishApply.getApplyInfo () );
                if (walletId == null) {
                    logger.error ( "找不到该网号的账户" );
                    throw new RuntimeException ( "找不到该网号的账户" );
                } else {
                    billAddRequestDto.setToUserId ( walletId );
                }
                //billAddRequestDto.setToUserId ( userId );
                Boolean successCommon = walletBillClientAction.addBill ( "999", billAddRequestDto );
                if (!successCommon) {
                    logger.error ( "申请人使用心愿资金出现异常" );
                    throw new RuntimeException ( "申请人使用心愿资金出现异常" );
                }
                wishApply.setBalance ( wish.getCurrentAmount () - wish.getCurrentApplyAmount () );
                wishApply.setIsPass ( 1 );
                worthServiceprovider.getWishApplyService ().updateById ( wishApply );
            }
            return success;
        }

    }

    /**
     * 心愿详情
     */
    public Wish selectById(String id) {
        return worthServiceprovider.getWishService ().selectById ( id );
    }

    /**
     * 检查是否发起成功-定时任务
     * 如果收到的钱比目标金额够或者多，就是成功，如果成功，就改变心愿状态为6，通过心愿id来更新，统计心愿个数，管理员个数加1，否则改变心愿状态为失败，通过心愿id来更新
     */
    @Transactional
    public void checkSuccess(String wishId) throws Exception {
        Wish wish = selectById ( wishId );
        if (wish != null) {
            boolean wishSuccess = wish.getCurrentAmount ().doubleValue () >= wish.getAmount ().doubleValue ();
            if (wishSuccess) {
                wish.setStatus ( WishStatus.SUCCESS.status );
                worthServiceprovider.getWishService ().updateById ( wish );
                //创建监管者
                worthServiceprovider.getWishManagerService ().create ( wishId );

                Long groupId/* = null*/;

                GroupDto groupDto = new GroupDto ();
                groupDto.setGroupName ( wish.getTitle () );
                groupDto.setType ( "userCreate" );
                WishGroup wishGroup = worthServiceprovider.getWishGroupService ().getGroupByWish ( wishId );
                List<WishManager> wishManagerList = worthServiceprovider.getWishManagerService ().getManagerByWish ( wishId );
                List<WishSupport> wishSupportList = worthServiceprovider.getWishSupportService ().getSupportListByWish ( wishId );
                long max = 0;//记录最大值
                String richUserId = "";//记录最有钱的监管者
                for (int i = 0; i < wishManagerList.size (); i++) {
                    for (int j = 0; j < wishSupportList.size (); j++) {
                        if (wishManagerList.get ( i ).getUserId ().equals ( wishSupportList.get ( i ).getUserId () )) {
                            if (wishSupportList.get ( i ).getAmount () > max) {
                                max = wishSupportList.get ( i ).getAmount ();
                                richUserId = wishSupportList.get ( i ).getUserId ();
                            }
                        }
                    }
                }
                groupDto.setUserId ( richUserId );
                groupId = groupAction.createGroup ( groupDto );
                wishGroup.setGroupId ( groupId );
                worthServiceprovider.getWishGroupService ().updateById ( wishGroup );
                boolean success = worthServiceprovider.getWishGroupService ().WishGroup ( wishGroup );
                if (!success) {
                    logger.error ( "更新群聊组Id失败！" );
                    throw new RuntimeException ();
                }
                List<WishReferee> wishReferees = worthServiceprovider.getWishRefereeService ().selectStasusByWishId ( wishId );
                List<WishSupport> wishSupports = worthServiceprovider.getWishSupportService ().getSupportListByWish ( wishId );
                //添加组人员，发布者，推荐者，支持者
                groupAction.addMember ( groupId, wish.getUserId () );
                for (WishReferee wishReferee : wishReferees) {
                    groupAction.addMember ( groupId, wishReferee.getUserId () );
                }
                for (WishSupport wishSupport : wishSupports) {
                    groupAction.addMember ( groupId, wishSupport.getUserId () );
                }
           /*     }else{
                    logger.error("创建群聊组失败");
                }*/

            } else {
                wish.setStatus ( WishStatus.FAIL.status );
                worthServiceprovider.getWishService ().updateById ( wish );

                // 解除冻结
                FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto ();
                requestDto.setTypeId ( wishId );
                requestDto.setUserId ( wish.getUserId () );
                requestDto.setType ( FrozenTypeEnum.FTZ_WISH );
                walletForzenClientAction.pay ( requestDto );
                /**long型的用款金额转BigDecimal型*/
                BigDecimal totalFee = new BigDecimal ( wish.getCurrentApplyAmount () );
                BigDecimal d100 = new BigDecimal ( 100 );
                BigDecimal wishCurrentApplyAmount = totalFee.divide ( d100, 2, 2 );//小数点保留2位
                // 心愿集资托管到平台的资金，申请者要使用。
                BillAddRequestDto billAddRequestDto = new BillAddRequestDto ();
                billAddRequestDto.setPayChannel ( 3 );
                billAddRequestDto.setType ( 1 );
                billAddRequestDto.setAmount ( wishCurrentApplyAmount );
                billAddRequestDto.setDescription ( "心愿取消使用资金返回" );
                billAddRequestDto.setToUserId ( wish.getUserId () );
                Boolean successCommon = walletBillClientAction.addBill ( "999", billAddRequestDto );
                if (!successCommon) {
                    logger.error ( "心愿取消使用资金返回出现异常" );
                    throw new RuntimeException ();
                }
                for (WishSupport wishSupport : worthServiceprovider.getWishSupportService ().getSupportListByWish ( wishId )) {
                    if (wishSupport.getPay ()) {
                        settlementAction.settlementAmountRightNow ( "心愿筹集目标未达成", "Wish", wishId, wishSupport.getUserId (), Money.CentToYuan ( wishSupport.getAmount () ).getAmount () );
                    }
                }
            }
        } else {
            logger.error ( "检查心愿发起成功失败，心愿ID不存在" );
            throw new RuntimeException ();
        }
    }

    /**
     * 心愿投诉后推送接口
     */
    public void pushMessage(String wishId) {
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        List<WishSupport> list = worthServiceprovider.getWishSupportService ().getisPaySupport ( wishId );
        List<String> userIds = list.stream ().map ( WishSupport::getUserId ).collect ( toList () );
        if (userIds != null && userIds.size () > 0) {
            for (String userId : userIds) {
                messagePushProxy.messagePush ( MessageTypeEnum.ACTIVITY_TYPE, "你支持的" + wish.getTitle () + "心愿已被人投诉，平台正在调查之中，如经核实，你的支持款将全额退回给你", "心愿提醒", userId, WZ_WISHDETAIL.getValue (), wishId );
            }
        }
    }

    /**
     * 心愿裁决通过后的推送接口
     */
    public void pushMessageAndRefund(String wishId) {
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        List<WishSupport> list = worthServiceprovider.getWishSupportService ().getisPaySupport ( wishId );
        if (list != null && list.size () > 0) {
            for (WishSupport wishSupport : list) {
                messagePushProxy.messagePush ( MessageTypeEnum.ACTIVITY_TYPE, "你支持的" + wish.getTitle () + "心愿，平台裁决其违规，你的支持款已全额退回给你，谢谢你的支持", "心愿提醒", wishSupport.getUserId (), WZ_WISHDETAIL.getValue (), wishId );
                FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto ();
                frozenOperationRequestDto.setTypeId ( wishSupport.getWishId () );
                frozenOperationRequestDto.setUserId ( wishSupport.getUserId () );
                frozenOperationRequestDto.setType ( FrozenTypeEnum.FTZ_WISH );
                walletForzenClientAction.repealFrozen ( frozenOperationRequestDto );
            }
        }
    }


    /**
     * 检查60天内有无新的用款申请”的定时任务
     */
    public void check60DayHasWishApply(String wishId) {
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        //查出监管人的ID
        List<WishManager> list = worthServiceprovider.getWishManagerService ().getManagerByWish ( wishId );
        List<String> userIds = list.stream ().map ( WishManager::getUserId ).collect ( toList () );
        if (userIds == null) {//非空处理
            userIds = new ArrayList<> ();
        }
        //查出最近一次申请用款记录
        Long createTime = (Long) worthServiceprovider.getWishApplyService ().getLastCreateTime ( wishId );

        userIds.add ( wish.getUserId () );
        if (new Date ().getTime () - createTime >= 1000 * 60 * 60 * 24 * 60) {//60天内没有申请过用款
            for (String userId : userIds) {
                messagePushProxy.messagePush ( MessageTypeEnum.ACTIVITY_TYPE, wish.getTitle () + "心愿筹集的资金共计" + wish.getCurrentAmount () + "元，已使用" + wish.getCurrentApplyAmount () + "元，余额" + (wish.getCurrentAmount () - (wish.getCurrentApplyAmount ())) + "元，"
                                + "自最近一次用款申请已过去60天，是否同意将全部余额转入红包池，让更多的人得到帮助？",
                        "心愿详情", userId, WZ_WISHDETAIL.getValue (), wishId );
            }
        }
    }

    /**
     * 12小时后自动批准用款”的定时任务
     */
    public void acceptApply12Hours(String wishId) {
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        //查出监管人的ID
        List<WishManager> list = worthServiceprovider.getWishManagerService ().getManagerByWish ( wishId );
        List<String> userIds = list.stream ().map ( WishManager::getUserId ).collect ( toList () );
        if (userIds == null) {//非空处理
            userIds = new ArrayList<> ();
        }
        //查出最近一次申请用款记录
        Long createTime = (Long) worthServiceprovider.getWishApplyService ().getLastCreateTime ( wishId );

        userIds.add ( wish.getUserId () );
        if (new Date ().getTime () - createTime >= 1000 * 60 * 60 * 24) {//60天内没有申请过用款
            for (String userId : userIds) {
                messagePushProxy.messagePush ( MessageTypeEnum.ACTIVITY_TYPE, wish.getTitle () + "心愿筹集的资金共计" + wish.getCurrentAmount () + "元，已使用" + wish.getCurrentApplyAmount () + "元，余额" + (wish.getCurrentAmount () - (wish.getCurrentApplyAmount ())) + "元，"
                                + "自最近一次用款申请已过去60天，是否同意将全部余额转入红包池，让更多的人得到帮助？",
                        "心愿详情", userId, WZ_WISHDETAIL.getValue (), wishId );
            }
        }
    }


    /**
     * 查询心愿列表
     *
     * @param
     * @return
     * @since ChenQian
     */
    public List<WishListDto> list(WishSearchDto wishSearchDto) {
        //构建心愿搜索条件、排序
        List<WishSearchResponse> wishSearchResponseList = wishSearchService.queryWishs ( getWishSearchQuery ( wishSearchDto ) );
        List<WishListDto> wishListDtoList = new ArrayList<> ();
        for (WishSearchResponse wishSearchResponse : wishSearchResponseList) {
            wishListDtoList.add ( createWishListDto ( wishSearchResponse ) );
        }
        return wishListDtoList;
    }

    /**
     * 心愿搜索内容处理
     *
     * @param wishSearchResponse
     * @return WishListDto
     * @since ChenQian
     */
    public WishListDto createWishListDto(WishSearchResponse wishSearchResponse) {
        WishListDto wishListDto = new WishListDto ();
        VoPoConverter.copyProperties ( wishSearchResponse, wishListDto );
        if (StringUtils.isNotBlank ( wishSearchResponse.getImagesUrl () )) {
            wishListDto.setImages ( netEnergyFuseAction.updateImagesUrl ( wishSearchResponse.getImagesUrl () ) );
        }
        if (wishSearchResponse.getCreditSum () > 0) {
            wishListDto.setHoldCredit ( true );
        } else {
            wishListDto.setHoldCredit ( false );
        }
        if (wishSearchResponse.getCreateTime () != null) {
            wishListDto.setPublishTime ( wishSearchResponse.getCreateTime () );
        }
        wishListDto.setAge ( ComputeAgeUtils.getAgeByBirthday ( wishSearchResponse.getBirthday () ) );
        return wishListDto;
    }


    /**
     * 构建心愿搜索条件、排序
     *
     * @param wishSearchDto
     * @return WishSearchQuery
     * @since ChenQian
     */
    public WishSearchQuery getWishSearchQuery(WishSearchDto wishSearchDto) {
        WishSearchQuery wishSearchQuery = new WishSearchQuery ();
        VoPoConverter.copyProperties ( wishSearchDto, wishSearchQuery );
        wishSearchQuery.setCenterGeoPoint ( new GeoPoint ( wishSearchDto.getLat (), wishSearchDto.getLon () ) );
        wishSearchQuery.setPage ( wishSearchDto.getCurrent (), wishSearchDto.getSize () );
        wishSearchQuery.setLock ( false );
        /**
         * 排序方式
         * 0.最热>最近
         * 1.最新>最近
         * 2.最近>信用>在线
         * 3.支持网信
         * 4.信用>最近
         * 5.价格最低>最近
         * 不传：供不应求（距离、信用、在线状态）
         */
        if (wishSearchDto.getSort () == 0) {
            wishSearchQuery.addFristAscQueries ( new LastAscQuery ( "refereeCount", false ) );
            wishSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else if (wishSearchDto.getSort () == 1) {
            wishSearchQuery.addFristAscQueries ( new LastAscQuery ( "createTime", false ) );
            wishSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else if (wishSearchDto.getSort () == 3) {
            wishSearchQuery.addFristAscQueries ( new LastAscQuery ( "creditSum", false ) );
            wishSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else if (wishSearchDto.getSort () == 4) {
            wishSearchQuery.addFristAscQueries ( new LastAscQuery ( "credit", false ) );
            wishSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else if (wishSearchDto.getSort () == 5) {
            wishSearchQuery.addFristAscQueries ( new LastAscQuery ( "amount", true ) );
            wishSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else {
            wishSearchQuery.addLastAscQuery ( new LastAscQuery ( "credit", false ) );
            wishSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        }
        return wishSearchQuery;
    }


    /* 查询单个用户的头像 年龄等*/
    public UserInfoAndHeadImg getUserDateByUserID(String userId) {
        try {
            return userAction.getUserInfoAndHeadImg ( userId );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            throw new RuntimeException ( "查询不到该用户" );
        }
    }


    public WishSendListDto getWishDateByUserID(String userId, Wish wish) {
        try {
            UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg ( userId );
            WishSendListDto wishSendListDto = VoPoConverter.copyProperties ( userInfoAndHeadImg, WishSendListDto.class );
            //或者心愿
            wishSendListDto.setUserId ( userInfoAndHeadImg.getId () );
            wishSendListDto.setId ( wish.getId () );
            wishSendListDto.setTitle ( wish.getTitle () );
            wishSendListDto.setWishLabels ( wish.getWishLabel () );
            return wishSendListDto;
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            throw new RuntimeException ( "查询不到该用户" );
        }
    }


    /* 查询多个用户的头像 年龄等 返回key为userId的Map*/
    public Map getUserDateByUserID(List<String> userIdList) {
        UserInfoAndHeadImg userInfoAndHeadImg = new UserInfoAndHeadImg ();
        Map<String, UserInfoAndHeadImg> map = new HashMap<> ();
        try {
            if (userIdList != null) {
                for (int i = 0; i < userIdList.size (); i++) {
                    userInfoAndHeadImg = userAction.getUserInfoAndHeadImg ( userIdList.get ( i ) );
                    map.put ( userIdList.get ( i ), userInfoAndHeadImg );
                }
            }
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            throw new RuntimeException ( "查询不到该用户" );
        }
        return map;
    }

    /**
     * CHEN-QIAN
     * 模糊分页查询申请提现列表
     *
     * @param businessWishDto
     * @return
     */
    public Map<String, Object> queryWishHistoryList(QueryWishWithdrawalsDto businessWishDto) {
        Map<String, Object> map = new HashMap<> ();
        List<WishBossListDto> wishBossListDtos = new ArrayList<> ();
        String userId = null;
        if (StringUtils.isNotBlank ( businessWishDto.getUserNumber () )) {
            userId = userAction.getUserService ().getUserIdByUserNumber ( businessWishDto.getUserNumber () );
        }
        Page page = new Page ( businessWishDto.getCurrentPage (), businessWishDto.getSize () );
        Page<WishHistory> wishHistoryPage = worthServiceprovider.getWishHistoryService ().getWishHistoryList ( userId, businessWishDto.getStatus (), page );
        for (WishHistory wishHistory : wishHistoryPage.getRecords ()) {
            WishApply wishApply = worthServiceprovider.getWishApplyService ().selectById ( wishHistory.getWishApplyId () );
            if (wishApply == null) {
                continue;
            }
            WishBank wishBank = worthServiceprovider.getWishBankService ().selectById ( wishApply.getBankId () );
            if (wishBank == null) {
                continue;
            }
            User user = userService.selectById ( wishHistory.getUserId () );
            if (user == null) {
                continue;
            }
            wishBossListDtos.add ( createWishHistory ( wishHistory, wishApply.getAmount (), wishBank, user ) );
        }
        map.put ( "total", wishHistoryPage.getTotal () );
        map.put ( "list", wishBossListDtos );
        return map;
    }

    public WishBossListDto createWishHistory(WishHistory wishHistory, Long amount, WishBank wishBank, User user) {
        WishBossListDto dto = new WishBossListDto ();
        VoPoConverter.copyProperties ( wishBank, dto );
        dto.setId ( wishHistory.getId () );
        dto.setWishApplyId ( wishHistory.getWishApplyId () );
        dto.setStatus ( wishHistory.getStatus () );
        dto.setAmount ( Money.CentToYuan ( amount ).getAmount () );
        dto.setUserNumber ( user.getUserNumber () );
        dto.setNickname ( user.getNickname () );
        return dto;
    }

    /**
     * 心愿详情
     */
    public WishDetailsDto selectById(Wish wish, Double lon, Double lat) {
        Map map = new HashMap ();
        WishDetailsDto wishDetailsDto = new WishDetailsDto ();
        if (wish != null) {
            //把wish的数据拷贝到wishDateDto
            VoPoConverter.copyProperties ( wish, wishDetailsDto );
            //从用户表中取出用户Id
            User user = userServiceProvider.getUserService ().selectById ( wish.getUserId () );
            /** 分转换为元 **/
            Money moneyAmount = Money.CentToYuan ( wish.getAmount () );
            wishDetailsDto.setAmount ( moneyAmount.getAmount () );
            Money moneyCurrentAmount = Money.CentToYuan ( wish.getCurrentAmount () );
            wishDetailsDto.setCurrentAmount ( moneyCurrentAmount.getAmount () );
            Money moneyCurrentApplyAmount = Money.CentToYuan ( wish.getCurrentApplyAmount () );
            wishDetailsDto.setCurrentApplyAmount ( moneyCurrentApplyAmount.getAmount () );
            if (user != null) {
                //计算心愿(用户)距离
                wishDetailsDto.setDistance ( DistrictUtil.calcDistance ( user.getLat ().doubleValue (), user.getLon ().doubleValue (), lat, lon ) );
            }
        }
        map.put ( "wishDetails", wishDetailsDto );
        return wishDetailsDto;
    }


    /* 我支持的心愿 **/
    public List<WishSendListDto> supportListById(String userId, Page page, Double lon, Double lat) throws Exception {
        List<WishSendListDto> wishSendListDto = new ArrayList<> ();
        /* 第一步：根据userId获取用户所支持的所有心愿列表 **/
        List<WishSupport> supportList = worthServiceprovider.getWishSupportService ().getUserSupportList ( userId );
        if (supportList.size () <= 0) { //如果该用户未报名支持任何的心愿，则返回空值！
            return wishSendListDto;
        }
        /* 第二步：根据第一步获取的WishSupport的信息查询心愿列表 **/
        List<Wish> wishList = worthServiceprovider.getWishService ().getSupportList ( supportList.stream ().map ( WishSupport::getWishId ).collect ( toList () ), userId, page );
        if (wishList.size () <= 0) {
            return null;
        }
        /* 第三步：从第二步获取的wish资料中提取userId **/
        List<String> wishSendId = new ArrayList<> ();
        wishList.forEach ( wishId -> {
            wishSendId.add ( wishId.getUserId () );
        } );
        /* 第四步：根据第三步提取的userId获取用户信息 **/
        if (wishSendId.size () <= 0) {
            return null;
        }
        Map<String, UserSynopsisData> userMap = userAction.getUserSynopsisDataMap ( wishSendId, lon, lat, userId );
        /* 第五步：根据第二步的wishId将Wish数据和user数据到wishSendListDto返回给前端 **/
        for (Wish wish : wishList) {
            wishSendListDto.add ( fusionner ( wish, userMap ) );
        }

        return wishSendListDto;
    }


    /* 合并我支持的心愿的信息 **/
    private WishSendListDto fusionner(Wish wish, Map<String, UserSynopsisData> userMap) {
        WishSendListDto wishSendListDto = new WishSendListDto ();
        //第六步：根据wish.userId获取用户信息
        if (wish != null) {
            VoPoConverter.copyProperties ( wish, wishSendListDto );
            Object user = userMap.get ( wish.getUserId () );
            //第八步：将第七步获取的数据copy到wishSendListDto返回给前端
            if (user != null) {
                VoPoConverter.copyProperties ( user, wishSendListDto );
            }
            /* 设置金额 **/
            Money money = Money.CentToYuan ( wish.getAmount () );
            Money moneyCurrent = Money.CentToYuan ( wish.getCurrentAmount () );
            Money moneyCurrentApply = Money.CentToYuan ( wish.getCurrentApplyAmount () );
            wishSendListDto.setId ( wish.getId () );
            wishSendListDto.setWishLabels ( wish.getWishLabel () );
            wishSendListDto.setAmount ( money.getAmount () );
            wishSendListDto.setCurrentAmount ( moneyCurrent.getAmount () );
            wishSendListDto.setCurrentApplyAmount ( moneyCurrentApply.getAmount () );
            wishSendListDto.setWishImagesTwoUrl ( (addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesTwoUrl (), AliyunBucketType.ActivityBucket )) );
            wishSendListDto.setWishImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesUrl (), AliyunBucketType.ActivityBucket ) );
        }
        return wishSendListDto;
    }


    /**
     * 我发布的心愿
     *
     * @param userId
     * @param page
     * @param lon
     * @param lat
     * @return
     */
    public List<WishSendListDto> publishListById(String fromUserId, String userId, Page<Wish> page, Double lon, Double lat) {

        List<WishSendListDto> wishSendListDtoList = new ArrayList<> ();
        if (userId != null) {
            List<Wish> wishList = wishService.getPublishList ( userId, page, fromUserId.equals ( userId ) );
            if (wishList != null && wishList.size () > 0) {
                /**从用户表中取出用户Id*/
                User user = userServiceProvider.getUserService ().selectById ( userId );
                for (Wish wish : wishList) {
                    WishSendListDto wishSendListDto = new WishSendListDto ();
                    /**用户信息*/
                    UserInfoAndHeadImg userInfoAndHeadImg = getUserDateByUserID ( wish.getUserId () );
                    VoPoConverter.copyProperties ( userInfoAndHeadImg, wishSendListDto );
                    /**距离*/
                    wishSendListDto.setDistance ( DistrictUtil.calcDistance ( user.getLat ().doubleValue (), user.getLon ().doubleValue (), lat, lon ) );
                    /**图片绝对路径*/
                    wish.setWishImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesUrl (), AliyunBucketType.ActivityBucket ) );
                    wish.setWishImagesTwoUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesTwoUrl (), AliyunBucketType.ActivityBucket ) );
                    VoPoConverter.copyProperties ( wish, wishSendListDto );
                    wishSendListDtoList.add ( wishSendListDto );

                    /** 分转换为元 **/
                    Money money = Money.CentToYuan ( wish.getAmount () );
                    wishSendListDto.setAmount ( money.getAmount () );
                    Money moneyCurrent = Money.CentToYuan ( wish.getCurrentAmount () );
                    wishSendListDto.setCurrentAmount ( moneyCurrent.getAmount () );
                    Money moneApplyCurrent = Money.CentToYuan ( wish.getCurrentApplyAmount () );
                    wishSendListDto.setCurrentApplyAmount ( moneApplyCurrent.getAmount () );

                }
            }
        }

        return wishSendListDtoList;
    }


    /**
     * 心愿详情
     */
    public Map<String, Object> selectRoleByUserId(String wishId, String userId, Double lat, Double lon) throws Exception {
        Map<String, Object> map = new HashMap<> ();
        WishDetailsDto wishDetailsDto = new WishDetailsDto ();
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        if (wish == null) {
            logger.error ( wishId + "该心愿不存在" );
            throw new RuntimeException ( "该心愿不存在" );
        }
        UserInfoAndHeadImg sendUserInformation = userAction.getUserInfoAndHeadImg ( wish.getUserId () );
        if (sendUserInformation != null) {
            VoPoConverter.copyProperties ( sendUserInformation, wishDetailsDto );
        }
        if (wish != null) {
            //List<WishdeaDto> wishdeaDto = new ArrayList<>();
            List<WishApply> wishApplies = worthServiceprovider.getWishApplyService ().getApplyLisByWish ( wishId );
            long countApplyMoney = 0;
            for (int i = 0; i < wishApplies.size (); i++) {

                //判断申请表中的申请金额状态是否为1 是则把金额添加到wish表的已申请金额的字段中 否则 不添加
                if (wishApplies.get ( i ).getIsPass () == 1)
                    countApplyMoney += wishApplies.get ( i ).getAmount ();
                wish.setCurrentApplyAmount ( countApplyMoney );
                worthServiceprovider.getWishService ().updateById ( wish );
            }
            VoPoConverter.copyProperties ( wish, wishDetailsDto );
            wishDetailsDto.setWishImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesUrl (), AliyunBucketType.ActivityBucket ) );
            wishDetailsDto.setWishImagesTwoUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesTwoUrl (), AliyunBucketType.ActivityBucket ) );
            /** 分转换为元 **/
            Money moneyAmount = Money.CentToYuan ( wish.getAmount () );
            wishDetailsDto.setAmount ( moneyAmount.getAmount () );
            Money moneyCurrentAmount = Money.CentToYuan ( wish.getCurrentAmount () );
            wishDetailsDto.setCurrentAmount ( moneyCurrentAmount.getAmount () );
            Money moneyCurrentApplyAmount = Money.CentToYuan ( wish.getCurrentApplyAmount () );
            wishDetailsDto.setCurrentApplyAmount ( moneyCurrentApplyAmount.getAmount () );
            User user = userAction.getNearlyUser ( wish.getUserId () );
            wishDetailsDto.setDistance ( DistrictUtil.calcDistance ( user.getLat ().doubleValue (), user.getLon ().doubleValue (), lat, lon ) );
        }
        map.put ( "wishDetail", wishDetailsDto );
        /* 获取推荐者信息 **/
        List<RefereeDetailDto> refereeInformation = new ArrayList<> ();
        List<WishReferee> refereeList = worthServiceprovider.getWishRefereeService ().selectByWishId ( wishId );
        for (WishReferee refereeId : refereeList) {
            refereeInformation.add ( MegerReferee ( userAction.getUserSynopsisData ( refereeId.getUserId (), wish.getUserId () ), refereeId, wish.getId (), wish.getUserId () ) );
        }
        map.put ( "referees", refereeInformation );
        /* 获取管理者信息 **/
        List<WishManagerDto> managerList = new ArrayList<> ();
        List<WishManager> wishManagerList = worthServiceprovider.getWishManagerService ().getManagerByWish ( wishId );
        wishManagerList.forEach ( wishManager -> {
            UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg ( wishManager.getUserId () );
            /*把userInfoAndHeadImg中的所有变量赋值给WishManagerDto*/
            WishManagerDto wishManagerDto = VoPoConverter.copyProperties ( userInfoAndHeadImg, WishManagerDto.class );
            /*把WishManagerDto中的id(即原来从user中的id赋值到userInfoAndHeadImg的id(说白了就是userId)赋值给wishManagerDto的userId*/
            wishManagerDto.setUserId ( wishManagerDto.getId () );
            /*把监管者的id赋值给wishManagerDto */
            wishManagerDto.setId ( wishManager.getId () );
            managerList.add ( wishManagerDto );
        } );
        map.put ( "managers", managerList );
        /* 获取支持者信息 **/
        List<SuppDetailDto> supportList = new ArrayList<> ();
        List<WishSupport> wishSuppors = worthServiceprovider.getWishSupportService ().getSupportListByWish ( wishId );
        for (WishSupport SupportId : wishSuppors) {
            supportList.add ( MegerSupp ( userAction.getUserSynopsisData ( SupportId.getUserId (), wish.getUserId () ), SupportId, wish.getId (), wish.getUserId () ) );
        }
        map.put ( "supportList", supportList );
//        /* 获取申请者信息 **/
//        List<ApplyDetailDto> applyList = new ArrayList<>();
//        List<WishApply> wishApplyList = worthServiceprovider.getWishApplyService().getApplyLisByWish(wishId);
//        for(WishApply ApplId :wishApplyList){
//            applyList.add(MegerAppl(userAction.getUserSynopsisData(ApplId.getUserId(),wish.getUserId()),ApplId,wish.getId(),wish.getUserId()));
//
//        }
//        map.put("applyList",applyList);
        /*使用信息**/
        List<WishdeaDto> wishdeaDto = new ArrayList<> ();
        List<WishApply> wishApplies = worthServiceprovider.getWishApplyService ().getApplyLisByWish ( wishId );
        for (WishApply wishApply : wishApplies) {
            wishdeaDto.add ( wishdeaDto ( wishApply ) );
        }
        map.put ( "wishApply", wishdeaDto );

        /*群号**/
        WishGroup wishGroup = worthServiceprovider.getWishGroupService ().getGroupByWish ( wishId );
        if (wishGroup != null) {
            map.put ( "groupId", wishGroup.getGroupId () );
        }
        /**判断时间是否已达或已超预期时间,若是 判断当前筹集金额是否已满*/
        DateFormat timeFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
        String currentTime = timeFormat.format ( new Date () );
        String dataTime = timeFormat.format ( wishDetailsDto.getExpiredAt () );
        long timeIsZero = timeFormat.parse ( dataTime ).getTime () - timeFormat.parse ( currentTime ).getTime ();
        if (timeIsZero <= 0) {
            if (wish.getCurrentAmount () < wish.getAmount ()) {
                wish.setStatus ( WishStatus.FAIL.status );
                //撤销冻结金额
                wishRefundMoney ( wishId, "未在预期时间内完成筹集金额" );
                worthServiceprovider.getWishService ().updateById ( wish );
            }
        }
        return map;
    }

    private WishdeaDto wishdeaDto(WishApply wishApply) throws Exception {
        WishdeaDto wishdeaDto = new WishdeaDto ();
        if (wishApply != null) {
            /**ispass是通过的意思，z=zero=0=未通过  o=one=1=已通过 w=wait=2=待通过*/
            int ispassz = worthServiceprovider.getWishAuthorizeService ().countiapplyzeroByWish ( wishApply.getId () );
            int ispasso = worthServiceprovider.getWishAuthorizeService ().countiapplyoneByWish ( wishApply.getId () );
            int ispassw = worthServiceprovider.getWishAuthorizeService ().countiapplywaitByWish ( wishApply.getId () );
            wishdeaDto.setIspassz ( ispassz );
            wishdeaDto.setIspasso ( ispasso );
            wishdeaDto.setIspassw ( ispassw );
            List<WishAuthorize> wishAuthorizeList = worthServiceprovider.getWishAuthorizeService ().getListByWishApplyId ( wishApply.getId () );
            List<WishdeaDtotwo> wishdeaDtotwoList = new ArrayList<> ();
            for (int i = 0; i < wishAuthorizeList.size (); i++) {
                WishdeaDtotwo wishdeaDtotwo = new WishdeaDtotwo ();
                wishdeaDtotwo.setUserid ( wishAuthorizeList.get ( i ).getUserId () );
                wishdeaDtotwo.setStaus ( wishAuthorizeList.get ( i ).getStatus () );
                wishdeaDtotwoList.add ( wishdeaDtotwo );
                wishdeaDto.setStausList ( wishdeaDtotwoList );
            }
            int stastus = wishdeaDto.getIspasso () + wishdeaDto.getIspassw () + wishdeaDto.getIspassz ();
           /* double two = 2;
            double three = 3;*/
            double ratio = 2.0 / 3.0;
            if (wishdeaDto.getIspasso () / stastus >= ratio) {
                wishApply.setIsPass ( 1 );
            } else {
                if (wishdeaDto.getIspassz () / stastus >= ratio) {
                    wishApply.setIsPass ( 0 );
                } else {
                    wishApply.setIsPass ( 2 );
                }
            }
            worthServiceprovider.getWishApplyService ().updateAllColumnById ( wishApply );


            User user = userAction.getNearlyUser ( wishApply.getUserId () );

            wishdeaDto.setDescription ( wishApply.getDescription () );
            wishdeaDto.setNickName ( user.getNickname () );

        }
        VoPoConverter.copyProperties ( wishApply, wishdeaDto );

        Money money = Money.CentToYuan ( wishApply.getAmount () );
        wishdeaDto.setAmount ( money.getAmount ().doubleValue () );
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishApply.getWishId () );
        Money money1 = Money.CentToYuan ( wish.getCurrentAmount () );
        wishdeaDto.setAmounts ( money1.getAmount ().longValue () );
        wishdeaDto.setCreateTime ( wishApply.getCreateTime () );
        wishdeaDto.setId ( wishApply.getId () );
        wishdeaDto.setPic ( wishApply.getPic () );
        wishdeaDto.setPic ( addImgUrlPreUtil.addImgUrlPres ( wishApply.getPic (), AliyunBucketType.ActivityBucket ) );

        return wishdeaDto;
    }


    /* 合并推荐者信息 */
    private RefereeDetailDto MegerReferee(UserSynopsisData userSynopsisData, WishReferee wishReferee, String wishId, String userId) throws Exception {
        RefereeDetailDto refereeDetailDto = new RefereeDetailDto ();
        Wish wish = worthServiceprovider.getWishService ().getWishUserIdAndWishId ( wishId, userId );
        User user = userAction.getNearlyUser ( wish.getUserId () );
        if (wishReferee == null) {
            throw new Exception ( "未找到推荐信息" );
        }
        if (userSynopsisData == null) {
            throw new Exception ( "未找到推荐者认证信息" );
        }
        VoPoConverter.copyProperties ( userSynopsisData, refereeDetailDto );
        VoPoConverter.copyProperties ( wishReferee, refereeDetailDto );
        return refereeDetailDto;
    }

    /* 合并支持者信息 */
    private SuppDetailDto MegerSupp(UserSynopsisData userSynopsisData, WishSupport wishSupport, String wishId, String userId) throws Exception {
        SuppDetailDto suppDetailDto = new SuppDetailDto ();
        Wish wish = worthServiceprovider.getWishService ().getWishUserIdAndWishId ( wishId, userId );
        User user = userAction.getNearlyUser ( wish.getUserId () );
        if (wishSupport == null) {
            throw new Exception ( "未找到支持信息" );
        }
        if (userSynopsisData == null) {
            throw new Exception ( "未找到支持者认证信息" );
        }
        VoPoConverter.copyProperties ( userSynopsisData, suppDetailDto );

        VoPoConverter.copyProperties ( wishSupport, suppDetailDto );
        Money money = Money.CentToYuan ( suppDetailDto.getAmount ().longValue () );
        suppDetailDto.setAmount ( money.getAmount () );


        return suppDetailDto;
    }
//    /* 合并申请者信息 */
//    private ApplyDetailDto MegerAppl(UserSynopsisData userSynopsisData, WishApply wishApply,String wishId,String userId) throws Exception {
//        ApplyDetailDto applyDetailDto = new ApplyDetailDto();
//        Wish wish = worthServiceprovider.getWishService().getWishUserIdAndWishId(wishId,userId);
//        User user = userAction.getNearlyUser(wish.getUserId());
//        if(wishApply == null){
//            throw new Exception("未找到申请者信息");
//        }
//        if (userSynopsisData==null){
//            throw new Exception("未找到申请者认证信息");
//        }
//        VoPoConverter.copyProperties(wishApply,applyDetailDto);
//        VoPoConverter.copyProperties(userSynopsisData,applyDetailDto);
//        return applyDetailDto;
//    }


    /* 心愿参与人*/
    public Map<String, Object> getWishUsers(String wishId) {
        Map<String, Object> map = new HashMap<> ();
        // 心愿发起者id
        List<String> publishUserIds = new ArrayList<> ();
        //通过wishId找到wish
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        if (wish != null) {
            UserInfoAndHeadImg userInfoAndHeadImg = getUserDateByUserID ( wish.getUserId () );
            String[] strarr = worthServiceprovider.getWishService ().selectById ( wishId ).getUserId ().split ( "," );
            for (int i = 0; i < strarr.length; i++) {
                if (strarr == null) {
                    continue;
                }
                publishUserIds.add ( strarr[i] );
            }
            map.put ( "publishUserIds", publishUserIds );
            map.put ( "nickname", userInfoAndHeadImg.getNickname () );
            map.put ( "headImgUrl", userInfoAndHeadImg.getHeadImgUrl () );

            // 支持者id
            //保存多个用户名和头像
            List<HashMap> supportList = new ArrayList<> ();
            List<WishSupport> wishSupportList = worthServiceprovider.getWishSupportService ().getSupportListByWish ( wishId );
            if (wishSupportList != null) {
                for (int i = 0; i < wishSupportList.size (); i++) {
                    UserInfoAndHeadImg userInfoAndHeadImgSupport = getUserDateByUserID ( wishSupportList.get ( i ).getUserId () );
                    if (userInfoAndHeadImg != null) {
                        HashMap SupUsermap = new HashMap ();
                        SupUsermap.put ( "headImgUrl", userInfoAndHeadImgSupport.getHeadImgUrl () );
                        SupUsermap.put ( "id", userInfoAndHeadImgSupport.getId () );
                        SupUsermap.put ( "nickname", userInfoAndHeadImgSupport.getNickname () );
                        supportList.add ( SupUsermap );
                    }
                }
                map.put ( "supportList", supportList );

                // 心愿推荐者id
                //保存多个用户名和头像
                List<HashMap> refereeList = new ArrayList<> ();
                List<WishReferee> wishRefereeList = worthServiceprovider.getWishRefereeService ().selectByWishId ( wishId );
                if (wishRefereeList != null) {
                    for (int i = 0; i < wishRefereeList.size (); i++) {
                        UserInfoAndHeadImg userInfoAndHeadImgReferee = getUserDateByUserID ( wishRefereeList.get ( i ).getUserId () );
                        HashMap RefUsermap = new HashMap ();
                        RefUsermap.put ( "headImgUrl", userInfoAndHeadImgReferee.getHeadImgUrl () );
                        RefUsermap.put ( "id", userInfoAndHeadImgReferee.getId () );
                        RefUsermap.put ( "nickname", userInfoAndHeadImgReferee.getNickname () );
//                        RefUsermap.put("stustas",wishRefereeList.get(i).getStatus());
                        refereeList.add ( RefUsermap );
                    }
                    map.put ( "refereeList", refereeList );
                }
            }
        }
        return map;
    }


    /* 定时任务：心愿发起24小时后，检查心愿是否成功，并拒绝掉未完成推荐的人*/
    @Transactional
    public void checkRefereeSuccess(String wishId) {
        Wish wish = worthServiceprovider.getWishService ().selectById ( wishId );
        if (wish != null) {
            if (wish.getStatus ().equals ( WishStatus.REFEREE_SUCCESS.status )) return;
            List<WishReferee> list = worthServiceprovider.getWishRefereeService ().selectByWishId ( wishId );
            double acceptCount = 0; // 记录同意人数
            double refuseCount = 0;//记录拒绝人数

            //如被邀请者中达到或超过50%的人拒绝推荐，则心愿发起失败，发起人的信用值扣减5分
            for (WishReferee wishReferee : list) {
                // 若需求仍为“待确认”状态，则自动拒绝
                if (wishReferee.getStatus ().equals ( WishRefereeStatus.WAITING.status )) {
                    wishReferee.setStatus ( WishRefereeStatus.CANCEL.status );
                    wishReferee.setDescription ( "抱歉，有点忙，来不及推荐" );
                    worthServiceprovider.getWishRefereeService ().updateById ( wishReferee );
                }
                //如果状态为同意  计数+1
                if (wishReferee.getStatus ().equals ( WishRefereeStatus.ACCEPT.status )) {
                    acceptCount += 1;
                }
                //如果状态为非同意  计数+1
                if (!wishReferee.getStatus ().equals ( WishRefereeStatus.ACCEPT )) {
                    refuseCount += 1;
                }
            }

            // 若拒绝人数大于 50% (当只有3个推荐人时,只要有一个人拒绝则失败,四个人时如果拒绝人数大于1则推荐失败)，则心愿推荐变为失败状态
/*            if (refuseCount / list.size() > 0.5 || (3 == list.size () && refuseCount > 0 ) || ( 4 == list.size () && refuseCount > 1) ) {
                wish.setStatus(WishStatus.CLOSE.status);
                settlementAction.settlementCreditRightNow("推荐人小于50%，心愿发起失败", "Wish", wishId, wish.getUserId(), -5);
                worthServiceprovider.getWishService().updateById(wish);
            } */
            /*else if( 3 == list.size ()){
                if(refuseCount == 0 ){
                    wish.setStatus(WishStatus.REFEREE_SUCCESS.status);
                    worthServiceprovider.getWishService().updateById(wish);
                }
            }else if(4 == list.size ()){
                if( refuseCount <= 1){
                    wish.setStatus(WishStatus.REFEREE_SUCCESS.status);
                    worthServiceprovider.getWishService().updateById(wish);
                }
            }else{

            }*/
            if (list.size () < 6) {
                if (acceptCount >= 3) {
                    wish.setStatus ( WishStatus.REFEREE_SUCCESS.status );
                    worthServiceprovider.getWishService ().updateById ( wish );
                }
                if (list.size () == 3 && refuseCount >= 1) {
                    wish.setStatus ( WishStatus.CLOSE.status );
                    worthServiceprovider.getWishService ().updateById ( wish );
                }
                if (list.size () == 4 && refuseCount >= 2) {
                    wish.setStatus ( WishStatus.CLOSE.status );
                    worthServiceprovider.getWishService ().updateById ( wish );
                }
                if (list.size () == 5 && refuseCount >= 3) {
                    wish.setStatus ( WishStatus.CLOSE.status );
                    worthServiceprovider.getWishService ().updateById ( wish );
                }
            } else {
                if (acceptCount / list.size () >= 0.5) {
                    wish.setStatus ( WishStatus.REFEREE_SUCCESS.status );
                    worthServiceprovider.getWishService ().updateById ( wish );
                } else if (refuseCount / list.size () >= 0.5) {
                    wish.setStatus ( WishStatus.CLOSE.status );
                    worthServiceprovider.getWishService ().updateById ( wish );
                } else if (acceptCount == 1) {
                }//作用 好看一点  不会有波浪线
            }
        } else {
            logger.error ( "检查心愿是否成功失败，心愿ID不存在" );
            throw new RuntimeException ();
        }
    }

    /**
     * 检查是否能够支持
     */
    public boolean checkSupport(String userId) {
        //查询用户是否验证身份信息
        try {
            User user = userAction.getNearlyUser ( userId );

            if (user.getIdNumber () != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            throw new RuntimeException ( "查询不到该用户" );
        }
    }

    /* 心愿退款流程 **/
    private boolean wishRefundMoney(String wishId, String exitType) {
        boolean success = false;
        //通过wishId查询该心愿的所有支持者
        List<WishSupport> wishSupportList = worthServiceprovider.getWishSupportService ().getSupportListByWish ( wishId );
        //判断wishId,支持者列表以及列表大小是否为空以及是否大于零
        if (StringUtils.isNotBlank ( wishId ) && wishSupportList != null && wishSupportList.size () > 0) {
            for (int i = 0; i < wishSupportList.size (); i++) {
                //通过userId和wishId分别查询用户昵称和心愿昵称
                String userId = wishSupportList.get ( i ).getUserId ();
                String userName = userService.getNickNameById ( userId );
                String wishName = worthServiceprovider.getWishService ().getWishbyWishId ( wishId ).getTitle ();
                //判断支持者的金额是否为空   进行撤销冻结金额
                if (wishSupportList.get ( i ).getAmount () > 0 && wishSupportList.get ( i ).getAmount () != null) {
                    FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto ();
                    frozenOperationRequestDto.setTypeId ( wishId );
                    frozenOperationRequestDto.setUserId ( userId );
                    frozenOperationRequestDto.setType ( FrozenTypeEnum.FTZ_WISH );
                    success = walletForzenClientAction.repealFrozen ( frozenOperationRequestDto );
                    if (success) {
                        logger.info ( userName + "用户参加的" + wishName + "心愿因" + exitType + "退款成功" );
                    } else {
                        logger.error ( userName + "用户参加的" + wishName + "心愿因" + exitType + "退款失败" );
                    }
                }
            }
        } else {
            success = true;
        }
        return success;
    }


    public Map<String,Object> getWishById(String id){
        Map<String,Object> map = new HashMap<>();
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
        //获取用户的信息
        UserInfoAndHeadImg userData=null;
        if(StringUtils.isNotBlank(wish.getUserId())) {
            userData = userAction.getUserInfoAndHeadImg ( wish.getUserId() );
        }
        map.put("list",wish);
        map.put("userDate",userData);
        return map;
    }
}
