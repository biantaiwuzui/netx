package com.netx.fuse.biz.worth;

import static com.netx.common.common.enums.FrozenTypeEnum.FTZ_DEMAND;
import static com.netx.common.common.enums.PushMessageDocTypeEnum.WZ_DEMANDDETAIL;
import static com.netx.common.user.util.DateTimestampUtil.TIME_DIFFERENCE;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.netx.common.common.enums.*;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.dto.wangMing.AddCreditRecordRequestDto;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.FrozenAddRequestDto;
import com.netx.common.vo.currency.WzCommonOtherSetResponseDto;
import com.netx.common.wz.dto.common.CommonCheckDto;
import com.netx.common.wz.dto.common.CreateRefundDto;
import com.netx.common.wz.dto.demand.*;
import com.netx.fuse.biz.FuseBaseAction;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.fuse.client.ucenter.*;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.biz.ordercenter.MerchantOrderInfoAction;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.ordercenter.constants.OrderTypeEnum;
import com.netx.ucenter.biz.common.CostAction;
import com.netx.ucenter.biz.common.WalletFrozenAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserCreditAction;
import com.netx.ucenter.model.user.User;
import com.netx.utils.DistrictUtil;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.utils.datastructures.Tuple;
import com.netx.utils.money.Money;
import com.netx.worth.biz.common.RefundAction;
import com.netx.worth.biz.demand.DemandAction;
import com.netx.worth.biz.demand.DemandOrderAction;
import com.netx.worth.biz.demand.DemandRegisterAction;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.enums.DemandStatus;
import com.netx.worth.service.*;
import com.netx.worth.util.PageWrapper;
import com.netx.worth.vo.DemandListVo;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.vo.common.CostSettingResponseDto;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.common.wz.dto.common.CommonPayCallbackDto;
import com.netx.fuse.client.shoppingmall.OrderClientAction;
import com.netx.fuse.proxy.EvaluateProxy;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.searchengine.model.DemandSearchResponse;
import com.netx.searchengine.query.DemandSearchQuery;
import com.netx.searchengine.service.DemandSearchService;
import com.netx.worth.enums.DemandOrderStatus;
import com.netx.worth.enums.DemandRegisterStatus;
import com.netx.worth.enums.RefundStatus;
import com.netx.worth.model.Demand;
import com.netx.worth.model.DemandOrder;
import com.netx.worth.model.DemandRegister;
import com.netx.worth.model.Refund;
import com.netx.worth.model.Settlement;

@Service
public class DemandFuseAction extends FuseBaseAction {

    private Logger logger = LoggerFactory.getLogger ( this.getClass ().getSimpleName () );

    @Autowired
    private DemandAction demandAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private DemandOrderAction demandOrderAction;
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private DemandSearchService demandSearchService;
    @Autowired
    private MessagePushProxy messagePushProxy;
    @Autowired
    private OrderClientAction orderClientAction;
    @Autowired
    private WalletFrozenAction walletFrozenAction;
    @Autowired
    private WalletForzenClientAction walletForzenClientAction;
    @Autowired
    private CostAction costAction;
    @Autowired
    private WalletBillClientAction walletBillClientAction;
    @Autowired
    private UserCreditAction userCreditAction;
    @Autowired
    private OtherSetClientAction otherSetClientAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private NetEnergyFuseAction netEnergyFuseAction;
    @Autowired
    private JobFuseAction jobFuseAction;
    @Autowired
    private MerchantOrderInfoAction merchantOrderInfoAction;
    @Autowired
    private EvaluateProxy evaluateProxy;
    @Autowired
    private ScoreAction scoreAction;
    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;
    @Autowired
    private DemandRegisterAction demandRegisterAction;
    @Autowired
    private MerchantAction merchantAction;
    @Autowired
    private RefundAction refundAction;

    /* 发布需求*/
    @Transactional(rollbackFor = Exception.class)
    public String publish(DemandPublishDto demandPublishDto) {

        //判断是否可以发布需求
        WzCommonOtherSetResponseDto otherSetResult = otherSetClientAction.queryRemote ();
        if (otherSetResult == null) {
            throw new RuntimeException ( "远程其他设置查询失败" );
        }
		/*String buffer = this.booleanIsCanRelease ( demandPublishDto.getUserId (), otherSetResult );
		if (!buffer.toString ().equals ( "" )) {
			throw new RuntimeException ( buffer.toString () );
		}*/
        Tuple<Boolean, String> tuple = this.booleanIsCanReleaseTuple ( demandPublishDto.getUserId (), otherSetResult );
        if (!tuple.left ()) {
            throw new RuntimeException ( tuple.right () );
        }
        Integer count = worthServiceprovider.getDemandService ().getSameDemand ( demandPublishDto );
        // 已经存在 不能发布相同的需求
        if (count != null && count > 0) {
            return null;
        }
        //	发布需求是不能有id的
        demandPublishDto.setId ( null );
        Demand demand = worthServiceprovider.getDemandService ().publish ( demandAction.DemandPublishDtoToDemand ( new Demand (), demandPublishDto ) );
        if (demand == null) {
            logger.error ( "发布需求失败" );
            throw new RuntimeException ( "数据库操作失败" );
        }
        if (StringUtils.isNotBlank ( demandPublishDto.getOrderIds () )) {
            boolean success = merchantOrderInfoAction.updateTypeId ( demandPublishDto.getOrderIds ().split ( "," ), demand.getId (), OrderTypeEnum.DEMAND_ORDER );
            if (!success) {
                throw new RuntimeException ( "绑定需求id失败" );
            }
        }
        if (demand.getBail () > 0) {
            String description = "你的\"" + demand.getTitle () + "\"需求已托管了" + demandPublishDto.getBail () + "元。";
            pay ( demandPublishDto.getUserId (), "999", demandPublishDto.getBail (), demand.getId (), description );
        }
        //创建日志
        /*boolean success = activeLogAction.create(demandPublishDto.getUserId(), "Demand", demand.getId(), "有新需求",
                demandPublishDto.getLon(), demandPublishDto.getLat());*/
        /*if (!success) {
            logger.error("创建发布记录失败");
            throw new RuntimeException();
        }*/
        if (demand.getStartAt () != null) {
            /**产生定时任务*/
            boolean success = jobFuseAction.addJob ( JobEnum.DEMAND_CHECK_AUTO_CANCEL_JOB, demand.getId (), demand.getId (), demand.getTitle () + "检查需求发起后是否自动取消", new Date ( demandPublishDto.getStartAt () + 10000 ), AuthorEmailEnum.FRWIN );
            if (!success) {
                logger.error ( "create timed task -> CheckAutoCancel error" );
                throw new RuntimeException ( "请检查需求开始时间是否有误!!是否在当前时间之前!" );
            }
        }
        return demand.getId ();
    }

    /**
     * 需求因无入选人而自动取消
     */
    public Integer checkDemandAutoCancel(String demandId) {
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandId );
        if (demand == null) {
            logger.error ( "找不到需求id" );
            return 1;
        } else {
            //得到入选者人数
            int count = worthServiceprovider.getDemandRegisterService ().getRegSuccessCount ( demand.getId () );
            /*BigDecimal demandBail;
            //处理金额
            demandBail = (Money.CentToYuan ( demand.getBail ().longValue () ).getAmount ());*/
            long now = System.currentTimeMillis ();
            //如果时间到了还没有入选者  自动取消该需求  钱退回发布者
            long startTime = demand.getStartAt ().getTime ();
            if (demand.getStartAt () != null && startTime <= now && count <= 0) {
                demand.setStatus ( DemandStatus.CANCEL.getStatus () );
                worthServiceprovider.getDemandService ().updateById ( demand );
                /*CommonPayCallbackDto commonPayCallbackDto = new CommonPayCallbackDto ();
                commonPayCallbackDto.setId ( demand.getId () );
                commonPayCallbackDto.setUserId ( demand.getUserId () );
                commonPayCallbackDto.setAmount ( demandBail );
                commonPayCallbackDto.setPayType ( "1" );*/
                FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto ();
                frozenOperationRequestDto.setType ( FTZ_DEMAND );
                frozenOperationRequestDto.setTypeId ( demandId );
                frozenOperationRequestDto.setUserId ( demand.getUserId () );
                try {
                    walletFrozenAction.repeal ( frozenOperationRequestDto );
                } catch (Exception e) {
                    throw new RuntimeException ( "资金回退异常" );
                }
                //推送
                wzCommonImHistoryAction.add ( "999", demand.getUserId (),"\""+demand.getTitle ()+"\"需求因为您超时未确认入选者，需求已自动取消！",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
            }
            return 0;
        }
    }

    /* 编辑需求 **/
    @Transactional(rollbackFor = Exception.class)
    public String publishEdit(DemandPublishDto demandPublishDto) {
        Demand demand = worthServiceprovider.getDemandService ().queryByIdAndUserId ( demandPublishDto.getUserId (), demandPublishDto.getId () );
        if (demand == null) {
            return "此需求可能已取消";
        }
        int count = worthServiceprovider.getDemandRegisterService ().getRegCount ( demandPublishDto.getId () );
        //已经有人申请的需求不可以改
        if (count > 0) {
            return "已有人申请此需求，不可以再修改";
        }
        //如果需求还未开始,则可产生定时任务 否则定时任务不产生
        if (demand.getStartAt ().getTime () != demandPublishDto.getStartAt ()) {
            /**  先删除原本的定时任务再产生新的定时任务,检测需求是否因无入选人而自动取消*/
            jobFuseAction.removeJob ( JobEnum.DEMAND_CHECK_AUTO_CANCEL_JOB, demand.getId (), demand.getTitle () + "检查需求发起后是否自动取消", demand.getId () );
            boolean success = jobFuseAction.addJob ( JobEnum.DEMAND_CHECK_AUTO_CANCEL_JOB, demand.getId (), demand.getId (), demand.getTitle () + "检查需求发起后是否自动取消", new Date ( demandPublishDto.getStartAt () + 2000 ), AuthorEmailEnum.FRWIN );
            if (!success) {
                logger.error ( "create timed task -> CheckAutoCancel error" );
                throw new RuntimeException ( "创建“检测需求是否因无入选人而自动取消”定时任务失败" );
            }
        }
        long yu = new Money ( demandPublishDto.getBail () ).getCent () - demand.getBail ();
        if (yu > 0) {
            Money money = Money.CentToYuan ( yu );
            String description = "你为\"" + demand.getTitle () + "\"需求补充托管了" + money.getAmount () + "元。";
            pay ( demandPublishDto.getUserId (), "999", money.getAmount (), demand.getId (), description );
        }
        String resultMsg = updateOrderId ( demand, demandPublishDto.getOrderIds () );
        demandAction.publishEdit ( demand, demandPublishDto );
        return resultMsg;
    }


    /**
     * 冻结
     */
    @Transactional(rollbackFor = Exception.class)
    void pay(String userId, String toUserId, BigDecimal amount, String demandId, String description) {
        FrozenAddRequestDto requestDto = new FrozenAddRequestDto ();
        requestDto.setUserId ( userId );
        requestDto.setTradeType ( 0 );
        requestDto.setToUserId ( toUserId );
        requestDto.setDescription ( description );
        requestDto.setFrozenType ( FTZ_DEMAND.getName () );
        requestDto.setAmount ( amount );
        requestDto.setTypeId ( demandId );
        if (!walletForzenClientAction.add ( requestDto )) {
            throw new RuntimeException ( "添加冻结金额异常" );
        }
    }

    @Transactional
    public boolean registerRejectRefund(String demandOrderId, String userId) throws Exception {
        boolean success = worthServiceprovider.getRefundService ().reject ( demandOrderId, userId );
        if (!success) {
            logger.error ( "更新refund单状态失败" );
            throw new RuntimeException ();
        }
        publishPay ( demandOrderId, null );
        return success;
    }

//	/*@Transactional(rollbackFor = Exception.class)
//	public DemandOrder publishPay(String demandOrderId, String userId, String payFrom) throws Exception {
//		//得到确认单
//		DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( demandOrderId );
//		if (demandOrder == null) throw new RuntimeException ( "需求确认单不存在" );
//
//		BigDecimal sharedFee = getSharedFee ();//获取后台设置网值的手续费比例
//		BillAddRequestDto requestDto = new BillAddRequestDto ();
//
//		requestDto.setDescription ( "用户报酬收入" );
//		requestDto.setToUserId ( demandOrder.getUserId () );
//		requestDto.setPayChannel ( 3 );
//		requestDto.setType ( 1 );
//		//平台付钱给申请者 扣除手续费
//		if (demandOrder.getEachWage ()) {//是单位报酬
//			requestDto.setAmount ( BigDecimal.valueOf ( demandOrder.getWage () / 100 ).multiply ( BigDecimal.ONE.subtract ( sharedFee ) ) );
//			walletBillClientAction.addBill ( "999", requestDto );
//		} else {//不是单位报酬
//			Integer count = worthServiceprovider.getDemandRegisterService ().getRegSuccessCount ( demandOrder.getDemandId () );
//			BigDecimal eachWage = BigDecimal.valueOf ( demandOrder.getWage () / 100 ).divide ( new BigDecimal ( count ) );
//			requestDto.setAmount ( eachWage.multiply ( BigDecimal.ONE.subtract ( sharedFee ) ) );
//			walletBillClientAction.addBill ( "999", requestDto );
//		}
//
//		//预约单状态为关闭
//		if(!demandAction.registerClose ( demandOrder.getId () )){
//			throw new RuntimeException ( "预约单关闭失败" );
//		}
//		return demandOrder ;
//
//	}*/

    @Transactional(rollbackFor = Exception.class)
    public boolean publishPay(String demandOrderId, String userId) {
        //得到确认单
        boolean success = false;
        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( demandOrderId );
        if (demandOrder == null) {
            throw new RuntimeException ( "需求确认单不存在" );
        }
        if (!demandOrder.getStatus ().equals ( DemandOrderStatus.AllEND.status )) {
            Demand demand = worthServiceprovider.getDemandService ().selectById ( demandOrder.getDemandId () );
            //根据order表得到入选者的人数
            long orderCount = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandId ( demandOrder.getDemandId () );
            if (demand.getBail () > 0) {
                scoreAction.addScore ( userId, StatScoreEnum.SS_FINISH_PAY_WORTH );
            } else {
                scoreAction.addScore ( userId, StatScoreEnum.SS_FINISH_WORTH );
            }
            if (demand == null) {
                throw new RuntimeException ( "该需求已不存在" );
            }
            if (userId != null && !demand.getUserId ().equals ( userId )) {
                throw new RuntimeException ( "你非发布者，不能确认需求完成" );
            }

            //结算报酬
            presentPay(demandOrder,orderCount, demand);

            if (!demand.getOpenEnded ()) {
                //获取该需求的出席者完成需求人数
                //int endCount = worthServiceprovider.getDemandOrderService ().getDemandOrderEndCountByDemandId ( demand.getId () );

                    //根据order表获取是否还存在未完成状态的需求订单人数
                    int isEndCount = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandId ( demandOrder.getDemandId (), 5, 6, 8 );
                    boolean isEnd = true;//用于判断是否关闭需求
                    //若存在,则设置isEnd为false 即不关闭需求
                    if (isEndCount > 0) {
                    isEnd = false;
                }
                if (isEnd) {
                    //不是长期有效并且入选者状态都需求成功,关闭需求
                    demand.setStatus ( DemandStatus.STOP.status );
                    /**需求结束,剩余金额退回 isFormal为0需求正常结束,否则需求失败*/
                    demandEndMoneyCallBack ( demand,0 );
                }
                return worthServiceprovider.getDemandService ().updateById ( demand );
            }
        }
        return success;

    }

    /* 入选者同意退款申请 **/
    public boolean registerAcceptRefund(String demandOrderId, String userId) throws Exception {
        Refund refund = worthServiceprovider.getRefundService ().getRefundByRelIdAndRelType ( demandOrderId, "DemandOrder" );
        if (refund != null && refund.getStatus ().equals ( RefundStatus.REQUEST.status )) {
            // 发布者托管到平台的资金，退款给发布者。
            BillAddRequestDto billAddRequestDto = new BillAddRequestDto ();
            billAddRequestDto.setAmount ( Money.CentToYuan ( refund.getAmount () ).getAmount () );
            billAddRequestDto.setDescription ( "退回给发布者的费用" );
            billAddRequestDto.setPayChannel ( 3 );
            billAddRequestDto.setType ( 1 );
            billAddRequestDto.setToUserId ( userId );//发起人Id
            Boolean addBill = walletBillClientAction.addBill ( "999", billAddRequestDto );
            if (!addBill) {
                throw new RuntimeException ( "退e回给发布者的费用出现异常" );
            }
            // 支付给入选者
            DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( demandOrderId );
            // 获取平台的收取的手续费比例
            BigDecimal sharedFee = getSharedFee ();
            DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().getDemandRegByOrderId ( demandOrderId );
            billAddRequestDto.setDescription ( "支付给入选者的费用" );

            billAddRequestDto.setAmount ( Money.CentToYuan ( refund.getBail () ).getAmount ().multiply ( BigDecimal.ONE.subtract ( sharedFee ) ) );
            billAddRequestDto.setPayChannel ( 3 );
            billAddRequestDto.setType ( 1 );
            billAddRequestDto.setToUserId ( demandRegister.getUserId () );//入选者Id
            addBill = walletBillClientAction.addBill ( "999", billAddRequestDto );
            if (!addBill) {
                logger.error ( "支付给入选者的费用出现异常" );
                throw new RuntimeException ( "支付给入选者的费用出现异常" );
            }
            demandOrder.setStatus ( DemandOrderStatus.AllEND.status );
            boolean success = worthServiceprovider.getDemandOrderService ().updateById ( demandOrder );
            if (!success) {
                logger.error ( "更新order单状态失败" );
                throw new RuntimeException ( "更新order单状态失败" );
            }
            refund.setStatus ( RefundStatus.ACCEPT.status );
            success = worthServiceprovider.getRefundService ().updateById ( refund );
            if (!success) {
                logger.error ( "更新refund单状态失败" );
                throw new RuntimeException ( "更新refund单状态失败" );
            }
            success = demandAction.registerClose ( demandOrderId );
            if (!success) {
                logger.error ( "更新register单状态失败" );
                throw new RuntimeException ( "更新register单状态失败" );
            }
            if(success) {
                //推送
                User user = userAction.getUserService ().getUserById ( demandRegister.getUserId () );
                wzCommonImHistoryAction.add ( demandRegister.getUserId (), userId, "\"" + user.getNickname () + "\"同意了您的退款要求！" , demandOrder.getDemandId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
            }
        }
        boolean success = demandAction.setDemandStop ( demandOrderId );
        if (!success) {
            logger.error ( "更新demand单状态失败" );
            throw new RuntimeException ( "更新demand单状态失败" );
        }
         return success;
    }


    public Map<String, Object> nearList(String userId, BigDecimal lon, BigDecimal lat, Double length,
                                        Page<Demand> page) {
        Map<String, Object> map = new HashMap<> ();
        DemandSearchQuery demandSearchQuery = new DemandSearchQuery ();
        demandSearchQuery.setPageSize ( page.getSize () );
        demandSearchQuery.setFrom ( page.getCurrent () );
        demandSearchQuery.setMaxDistance ( length );
        demandSearchQuery.setUserId ( userId );
        GeoPoint geoPoint = new GeoPoint ( lat.doubleValue (), lon.doubleValue () );
        demandSearchQuery.setCenterGeoPoint ( geoPoint );
        List<DemandSearchResponse> targetList = demandSearchService.queryDemands ( demandSearchQuery );
        List<Demand> list = new ArrayList<> ();
        Demand source = null;
        if (targetList != null && targetList.size () > 0) {
            for (DemandSearchResponse demandSearchResponse : targetList) {
                source = new Demand ();
                BeanUtils.copyProperties ( demandSearchResponse, source );
                list.add ( source );
            }
        }

        if (list != null && list.size () > 0) {
            for (Demand demand : list) {
                demand.setImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demand.getImagesUrl (), AliyunBucketType.ActivityBucket ) );
                demand.setDetailsImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demand.getDetailsImagesUrl (), AliyunBucketType.ActivityBucket ) );
            }
        }
        //TODO
//		map.put("list", DistrictUtil.getDistrictVoList(lat, lon, list));
        if (list != null && list.size () > 0) {
            getPublishListHash ( map, list );
        }
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean publishConfirmOrderDetail(DemandOrderConfirmDto demandOrderConfirmDto) {
        List<DemandOrder> demandOrderList = worthServiceprovider.getDemandOrderService ().getSuccessListByDemandId ( demandOrderConfirmDto.getId (), DemandOrderStatus.ACCEPT );
        List<String> demandOrderIds = demandOrderList.stream ().map ( DemandOrder::getId ).collect ( toList () );
        List<String> demandOrderuserIds = demandOrderList.stream ().map ( DemandOrder::getUserId ).collect ( toList () );
        boolean publishConfirmOrderDetail = false;
        if (demandOrderList != null && demandOrderList.size () > 0) {
            for (DemandOrder demandOrder : demandOrderList) {
                if (demandOrder.getStartAt () != null)
                    return false;// 不能二次确认
                if (demandOrder.getStartAt () == null && demandOrder.getCreateTime ().getTime () + 24l * 3600 * 1000 < new Date ().getTime ())
                    return false;// 超时确认也作为失败处理
                publishConfirmOrderDetail = demandOrderAction.publishConfirmOrderDetail ( demandOrderIds,
                        demandOrderConfirmDto );
                Settlement settlement = settlementAction.create ( "初始化", "DemandOrder", demandOrder.getId (), false,
                        demandOrderConfirmDto.getEndAt () + 25l * 3600 * 1000 );
                if (settlement == null) {
                    logger.error ( "初始化结算表失败" );
                    throw new RuntimeException ();
                }
                //TODO
                boolean success = false;
//				boolean success = quartzService.checkDemandStart(demandOrder.getId(),
//						demandOrderConfirmDto.getStartAt());
                if (!success) {
                    logger.error ( "创建查询需求单启动情况，是否全部启动需求 的定时任务失败" );
                    throw new RuntimeException ();
                }
                if (!success) {
                    logger.error ( "创建检查需求是否成功的定时任务失败" );
                    throw new RuntimeException ();
                }
                if (!success) {
                    logger.error ( "创建检查评价的定时任务失败" );
                    throw new RuntimeException ();
                }
            }
        }
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandOrderConfirmDto.getId () );
        demand.setOrderIds ( demandOrderConfirmDto.getOrderIds () );
        worthServiceprovider.getDemandService ().updateById ( demand );

        demandOrderuserIds.add ( demand.getUserId () );// 发布者
        String manageUserId = orderClientAction.getManageUserIdByOrderId ( demandOrderConfirmDto.getOrderIds () );
        demandOrderuserIds.add ( manageUserId );// 商家
        if (demandOrderuserIds != null && demandOrderuserIds.size () > 0) {
            for (String userId : demandOrderuserIds) {
                messagePushProxy.messagePush ( MessageTypeEnum.ACTIVITY_TYPE,
                        demand.getTitle () + "需求发布成功，时间" + demandOrderConfirmDto.getStartAt () + "，地点"
                                + demandOrderConfirmDto.getAddress () + "，人数" + 1 + "人、消费" + 2 + "，请准备",
                        "需求提醒", userId, WZ_DEMANDDETAIL.getValue (),
                        demandOrderConfirmDto.getId () );
            }
        }
        return publishConfirmOrderDetail;
    }

    /* 未出席扣5分*/
    @Transactional(rollbackFor = Exception.class)
    public void checkConfirm(String demandOrderId) {
        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( demandOrderId );
        if (demandOrder != null) {
            Demand demand = worthServiceprovider.getDemandService ().selectById ( demandOrder.getDemandId () );
            if (demand != null) {
                if (!demandOrder.getStatus ().equals ( DemandOrderStatus.ACCEPT.status )) {
                    String description = "你的\"" + demand.getTitle () + "\"需求在" + DateTimestampUtil.getDateStrByDate ( new Date () ) + "未确定细节";
                    addCreditRecord ( demandOrder.getUserId (), -5, demandOrderId, description );
                    demandOrder.setStatus ( DemandOrderStatus.CANCEL.status );
                    worthServiceprovider.getDemandOrderService ().updateById ( demandOrder );
                    //推送
                    wzCommonImHistoryAction.add ( "999", demand.getUserId (), "您的“" + demand.getTitle () + "”需求逾期未确认细节，改需求已自动取消！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
                    //获取入选者发推送
                    List<DemandRegister> demandRegisterList = worthServiceprovider.getDemandRegisterService ().getListByDemandId ( demand.getId () );
                    for (int i = 0; i < demandRegisterList.size (); i++) {
                        wzCommonImHistoryAction.add ( "999", demandRegisterList.get ( i ).getUserId (), "由于“" + demand.getTitle () + "”需求发布者逾期未确认需求细节，该需求已自动取消！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    boolean addCreditRecord(String userId, int credit, String typeId, String description) {
        AddCreditRecordRequestDto addCreditRecordRequestDto = new AddCreditRecordRequestDto ();
        addCreditRecordRequestDto.setRelatableType ( DemandOrder.class.getSimpleName () );
        addCreditRecordRequestDto.setRelatableId ( typeId );
        addCreditRecordRequestDto.setUserId ( userId );
        addCreditRecordRequestDto.setCredit ( credit );
        addCreditRecordRequestDto.setDescription ( description );
        return userCreditAction.addCreditRecord ( addCreditRecordRequestDto );
    }

    @Transactional
    public void checkStart(String demandOrderId) throws Exception {
        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( demandOrderId );
        List<DemandRegister> registers = worthServiceprovider.getDemandRegisterService ().getListByDemandId ( demandOrder.getDemandId () );
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandOrder.getDemandId () );
        //已入选未启动
        List<DemandRegister> startNumber = worthServiceprovider.getDemandRegisterService ().getDemandRegisterListByDemandIdAndStatus ( demand.getId (), 1 );
        boolean hasStart = false;
        int i = 0,j = 0;
        String [] notStartDemandName = new String [startNumber.size ()];
        StringBuffer linkStartNames = new StringBuffer (  );
        for (DemandRegister demandRegister : registers) {
            boolean isStart = demandRegister.getStatus ().equals ( DemandRegisterStatus.START.status );
            boolean isSuccess = demandRegister.getStatus ().equals ( DemandRegisterStatus.SUCCESS.status );
            boolean isDrop = demandRegister.getStatus ().equals ( DemandRegisterStatus.DROP.status );
            //有入选的申请者
            if (isSuccess) {
                //修改申请状态为超时未启动
                demandRegister.setStatus ( DemandRegisterStatus.TIMEOUT.status );
                worthServiceprovider.getDemandRegisterService ().updateById ( demandRegister );
                //扣除两分
                settlementAction.settlementCredit ( "DemandOrder", demandOrderId, demandRegister.getUserId (), -2 );
                // 修改demandOrder的status为超时未启动
                demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( demandRegister.getDemandOrderId () );
                demandOrder.setStatus ( DemandOrderStatus.TIMEOUT.status );
                worthServiceprovider.getDemandOrderService ().updateById ( demandOrder );
                //推送给发布者
                wzCommonImHistoryAction.add ( "999", registers.get ( i ).getUserId (), "“" + demand.getTitle () + "”需求已开始，您未启动需求，系统已判定您为缺席！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
                User user = userAction.getUserService ().getUserById ( registers.get ( i ).getUserId () );
                notStartDemandName[j] = user.getNickname () + " ";
                if (j < startNumber.size ())
                    linkStartNames.append ( notStartDemandName[j] );
                i++;
                j++;
            }
            //已入选放弃入选
            if (isDrop) {
                settlementAction.settlementCredit ( "DemandOrder", demandOrderId, demandRegister.getUserId (), -2 );
            }
            //已经启动需求 已经有人启动需求不进行退款
            if (isStart) {
                hasStart = true;
            }
        }
        String names = linkStartNames.toString ();
        //推送给发布者
        wzCommonImHistoryAction.add ( "999", demand.getUserId (), "入选者 " + names + " 未启动需求", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
        if (!hasStart) {// 入选者全部取消，退还费用
            //推送给发布者
            wzCommonImHistoryAction.add ( "999", demand.getUserId (), "“" + demand.getTitle () + "”需求已开始，还未有入选者启动需，需求失败。您的托管金额已退还，请到钱包收支明细查看！！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_WALLET, null );
            demand.setStatus ( 2 );
            worthServiceprovider.getDemandService ().updateById ( demand );
            settlementAction.settlementAmountRightNow ( "入选者全部取消，退还费用", "DemandOrder", demandOrderId,
                    demandOrder.getUserId (), Money.CentToYuan ( demandOrder.getBail () ).getAmount () );
        }
    }

    /* 评论定时任务*/
    public void checkEvaluate(String demandOrderId) {
        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( demandOrderId );
        if (demandOrder != null) {
            List<String> demandRegisterUserIds = worthServiceprovider.getDemandRegisterService ().getSuccessUserIdByDemandOrderId ( demandOrderId );
            String publishUserId = demandOrder.getUserId ();
            if (demandRegisterUserIds == null) {
                demandRegisterUserIds = new ArrayList<> ();
            }
            if (StringUtils.isNotBlank ( publishUserId )) {
                demandRegisterUserIds.add ( publishUserId );
            }
            if (demandRegisterUserIds.size () > 0) {
                List<String> list = evaluateProxy.notEvaluateUsers ( demandRegisterUserIds, demandOrderId );
                if (list != null) {
                    list.forEach ( userId -> {
                        settlementAction.settlementCredit ( "DemandOrder", demandOrderId, userId, -2 );
                    } );
                }
            }
        }
    }

    /* 支付回退*/
    public boolean payCallback(CommonPayCallbackDto commonPayCallbackDto) throws Exception {
        String demandId = commonPayCallbackDto.getId ();
        String userId = commonPayCallbackDto.getUserId ();
        String payType = commonPayCallbackDto.getPayType (); // 支付方式，暂且没有用到
        BigDecimal amount = commonPayCallbackDto.getAmount ();

        // 解除冻结
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto ();
        requestDto.setTypeId ( demandId );
        requestDto.setUserId ( userId );
        requestDto.setType ( FTZ_DEMAND );
        walletForzenClientAction.pay ( requestDto );

        // 发布者托管到平台的资金，还给入选者支付给商家的费用。
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto ();
        billAddRequestDto.setAmount ( amount );
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandId );
        /*if (demand != null) {
            if (userId.equals ( demand.getUserId () )) {
                billAddRequestDto.setDescription ( demand.getTitle () + "需求自动取消.金额退回发布者." );
            } else {*/
        billAddRequestDto.setDescription ( "退回入选者支付给商家的费用" );
         /*   }
        }*/
        billAddRequestDto.setPayChannel ( 3 );
        billAddRequestDto.setType ( 1 );
        billAddRequestDto.setToUserId ( userId );
        Boolean success = walletBillClientAction.addBill ( "999", billAddRequestDto );
        if (!success) {
            logger.error ( "退回费用出现异常" );
            throw new RuntimeException ();
        }
        return true;
    }


    public BigDecimal getSharedFee() {
        CostSettingResponseDto costSettingResponseDto = costAction.query ();
        if (costSettingResponseDto == null) {
            logger.error ( "费用设置获取失败" );
            throw new RuntimeException ();
        }
        BigDecimal sharedFee = costSettingResponseDto.getSharedFee (); // 记录手续费比例
        // 手续费比例大于或等于 1 或者 手续费比例小于 0，说明后台设置出现问题
        boolean flag_sharedFee = sharedFee.compareTo ( BigDecimal.ONE ) == 1 || sharedFee.compareTo ( BigDecimal.ONE ) == 0
                || sharedFee.compareTo ( BigDecimal.ZERO ) == -1;
        if (flag_sharedFee) {
            logger.error ( "后台设置网值的手续费比例不合法：" + sharedFee );
            throw new RuntimeException ();
        }
        return sharedFee;
    }

    @Transactional(rollbackFor = Exception.class)
    public String publishConfirm(DemandOrderConfirmRequestDto dto, String userId) {
        Demand demand = worthServiceprovider.getDemandService ().selectById ( dto.getDemandId () );
        if (demand == null) {
            return "该需求可能已取消";
        }
        if (!userId.equals ( demand.getUserId () )) {
            return "你非发布者，不能确认细节";
        }
        if (demand.getStatus ().equals ( DemandStatus.CONFIRM.status )) {
            return "此需求已确认细节，无需再确认";
        }
        int orderNum = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandId ( dto.getDemandId (), DemandOrderStatus.ACCEPT.status );
        if (orderNum < 1) {
            return "暂无入选人，不能确认细节";
        }
        long bail = new Money ( dto.getBailBig () ).getCent ();
        if (worthServiceprovider.getDemandOrderService ().updateDemandOrderByDemandId ( orderConfirmToDemandOrder ( dto, bail ) )) {
            demand.setStatus ( DemandStatus.CONFIRM.status );
            Money yu = Money.CentToYuan ( bail - demand.getBail () );
            demand.setBail ( bail );
            demand.setWage ( Money.YuanToCent(dto.getWage().longValue()+""));
            demand.setEachWage ( dto.getEachWage () );
            if (yu.getCent () > 0) {
                String description = "你为\"" + demand.getTitle () + "\"需求补充托管了" + yu.getAmount () + "元。";
                pay ( userId, "999", yu.getAmount (), demand.getId (), description );
            }
            //Page<DemandRegister> page = new Page<>(dto.getCurrentPage(), dto.getSize());
/*            List<DemandRegister> registerList = worthServiceprovider.getDemandRegisterService ().getListByDemandId  ( demand.getId () );
            //设置未选入人员的状态为3
            for(int i=0;i<registerList.size ();i++){
                if(registerList.get ( i ).getStatus ().equals ( DemandRegisterStatus.REGISTERED.status )){
                    registerList.get ( i ).setStatus ( DemandRegisterStatus.FAIL.status );
                }
            }
            //更新入选者状态
            worthServiceprovider.getDemandRegisterService ().insertOrUpdateBatch ( registerList );*/
            String result = updateOrderId ( demand, dto.getOrderIds () );
            worthServiceprovider.getDemandService ().updateById ( demand );
            //时间转换
            String timeChange = timeTransform ( dto.getStartAt () );
            /**产生定时任务:检测需求开始后30分钟是否有入选者验证码通过*/
            boolean checkCode = jobFuseAction.addJob ( JobEnum.DEMAND_CHECK_PASS_CODE_JOB, demand.getId (), demand.getId (), demand.getTitle () + "检测需求开始后30分钟是否有入选者验证码通过", new Date ( dto.getStartAt ().getTime () + (1000 * 60 * 30) ), AuthorEmailEnum.FRWIN );
            if (!checkCode) {
                logger.error ( "create timed task -> checkCodePass error" );
                throw new RuntimeException ( "创建“检测需求开始后30分钟是否有入选者验证码通过”定时任务失败" );
            }

            List<DemandRegister> demandRegisterList = worthServiceprovider.getDemandRegisterService ().getDemandRegisterListByDemandIdAndStatus ( demand.getId (), 1 );
            String address = demand.getAddress ();
            if(demand.getAddress ()==null){
                address = "抱歉，细节中并无地址呢！";
            }
            if(dto.getOrderIds () != null ){
                //商家地址不为空,给商家推送
                String [] merchantIds = demand.getOrderIds ().split ( "," );
                String [] merchantNames={};
                for(int i = 0; i < merchantIds.length; i++) {
                    //得到第i个商家id
                    MerchantOrderInfo merchantOrderInfo = merchantOrderInfoAction.getMerchantOrderInfoService ().selectById ( merchantIds[i] );
                    //用商家id得到商家资料
                    Merchant merchant = merchantAction.getMerchantService ().selectById ( merchantOrderInfo.getMerchantId () );
                    //推送
                    wzCommonImHistoryAction.add ( "999", merchant.getUserId (), "“" + demand.getTitle () + "”需求将在您的“"+merchant.getName ()+"”店铺消费!,时间为：" + timeChange + "，人数为:" + demandRegisterList.size () + "请提前做好热情招待的准备！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
                    //去重复推送
                    int count = 0;
                    merchantNames[i] = merchant.getName ();
                    for(int j = 0; j < i; j++ ){
                        if(!merchantNames[j].equals ( merchantNames[i] ))
                            count++;
                    }
                    if (count > 0)
                        wzCommonImHistoryAction.add ( "999", merchant.getUserId (), "“" + demand.getTitle () + "”需求将在您的“" + merchant.getName () + "”店铺消费！时间为:" + timeChange + "，人数为：" + demandRegisterList.size () + "请提前做好热情招待的准备！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
                }
            }
            //报酬
            //获取入选人的数量
            int count = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandId ( demand.getId () );
            BigDecimal orderCount = new BigDecimal ( count );
            BigDecimal wage = dto.getWage ();
            if (!dto.getEachWage ()) {
                wage = (wage.divide (orderCount));
            }
            //平台给入选者的推送
            for (int i = 0; i < demandRegisterList.size (); i++) {
                wzCommonImHistoryAction.add ( "999", demandRegisterList.get ( i ).getUserId (), "“" + demand.getTitle () + "”需求已确认细节。时间为：" + timeChange + "，地址为:“" + address + "”，报酬为" + wage + "元，记得按照细节准时到场参与需求！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
            }
            return result;
        }
        return "确认细节失败";
    }

    private String updateOrderId(Demand demand, String orderIds) {
        String result = null;
        if (StringUtils.isNotBlank ( orderIds )) {
            if (StringUtils.isNotBlank ( demand.getOrderIds () )) {
                if (demand.getOrderIds ().equals ( orderIds )) {
                    return null;
                }
            }
            boolean success = merchantOrderInfoAction.updateTypeId ( orderIds.split ( "," ), demand.getId (), OrderTypeEnum.DEMAND_ORDER );
            if (!success) {
                result = "绑定预付消费失败";
            }
        } else {
            if (StringUtils.isBlank ( demand.getOrderIds () )) {
                return null;
            }
            merchantOrderInfoAction.delete ( demand.getId (), OrderTypeEnum.DEMAND_ORDER );
        }
        worthServiceprovider.getDemandService ().getDemandMapper ().updateOrderId ( demand.getId (), orderIds );
        return result;
    }

    private DemandOrder orderConfirmToDemandOrder(DemandOrderConfirmRequestDto dto, long bail) {
        DemandOrder demandOrder = VoPoConverter.copyProperties ( dto, DemandOrder.class );
        if (dto.getOrderPriceBig () != null) {
            demandOrder.setOrderPrice ( new Money ( dto.getOrderPriceBig () ).getCent () );
        }
        if (dto.getWage () != null) {
            demandOrder.setWage ( new Money ( dto.getWage () ).getCent () );
        }
        demandOrder.setBail ( bail );
        demandOrder.setStatus ( DemandOrderStatus.CONFIRM.status );
        return demandOrder;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean publishAccept(String demandRegisterId, DemandOrderConfirmDto demandOrderConfirmDto) throws Exception {
        DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectById ( demandRegisterId );
        if (demandRegister == null) {
            throw new RuntimeException ( "需求申请单不存在" );
        }
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegister.getDemandId () );
        if (!demand.getUserId ().equals ( demandOrderConfirmDto.getUserId () )) {
            throw new RuntimeException ( "用户不是需求发布人 非法进入" );
        }
        //不能出现同一用户申请确认两次
        int count = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandIdAndUserId ( demandRegister.getDemandId (), demandRegister.getUserId () );
        if (count != 0) {
            throw new RuntimeException ( "已存在此订单，不能重复" );
        }
        ;
        //验证需求人数
        count = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandId ( demandRegister.getDemandId () );
        if (count >= demand.getAmount ()) {
            throw new RuntimeException ( "需求人数满足，不能再接受申请" );
        }
        DemandOrder demandOrder = new DemandOrder ();
        //复制用户信息
        VoPoConverter.copyProperties ( demandOrderConfirmDto, demandOrder );
        //设置申请人的id和需求id
        demandOrder.setDemandId ( demandRegister.getDemandId () );
        demandOrder.setUserId ( demandRegister.getUserId () );
        return publishOrder ( demandOrder, demandRegister );
    }

    public void getPublishListHash(Map<String, Object> map, List<Demand> list) {
        if (list == null || list.size () <= 0)
            return;
        List<String> demandIds = list.stream ().map ( Demand::getId ).collect ( Collectors.toList () );
        List<DemandRegister> demandRegisters = worthServiceprovider.getDemandRegisterService ().getRegList ( demandIds );
        Map<String, Long> registerCountHash = demandRegisters.stream ()
                .collect ( groupingBy ( DemandRegister::getDemandId, Collectors.counting () ) );
        Map<String, Long> registerSuccessCountHash = demandRegisters.stream ()
                .filter ( e -> e.getStatus ().equals ( DemandRegisterStatus.SUCCESS.status ) )
                .collect ( groupingBy ( DemandRegister::getDemandId, Collectors.counting () ) );
        map.put ( "registerCountHash", registerCountHash );
        map.put ( "registerSuccessCountHash", registerSuccessCountHash );
    }

    public List<DemandListDto> list(DemandSearchDto demandSearchDto) {
        //构建需求搜索条件、排序
        List<DemandSearchResponse> demandSearchResponseList = demandSearchService.queryDemands ( getDemandSearchQuery ( demandSearchDto ) );
        List<DemandListDto> demandListDtoList = new ArrayList<> ();
        for (DemandSearchResponse demandSearchResponse : demandSearchResponseList) {
            demandListDtoList.add ( createDemandListDto ( demandSearchResponse ) );
        }
        return demandListDtoList;
    }

    /**
     * 需求搜索内容处理
     *
     * @param demandSearchResponse
     * @return DemandListDto
     * @since ChenQian
     */
    public DemandListDto createDemandListDto(DemandSearchResponse demandSearchResponse) {
        DemandListDto demandListDto = new DemandListDto ();
        VoPoConverter.copyProperties ( demandSearchResponse, demandListDto );
        demandListDto.setBail ( Money.CentToYuan ( demandListDto.getBail ().longValue () ).getAmount () );
        if (StringUtils.isNotBlank ( demandSearchResponse.getImagesUrl () )) {
            demandListDto.setImages ( netEnergyFuseAction.updateImagesUrl ( demandSearchResponse.getImagesUrl () ) );
        }
        if (demandSearchResponse.getCreditSum () > 0) {
            demandListDto.setHoldCredit ( true );
        } else {
            demandListDto.setHoldCredit ( false );
        }
        if (demandSearchResponse.getCreateTime () != null) {
            demandListDto.setPublishTime ( demandSearchResponse.getCreateTime () );
        }
        demandListDto.setAge ( ComputeAgeUtils.getAgeByBirthday ( demandSearchResponse.getBirthday () ) );
        return demandListDto;
    }

    /**
     * 构建需求搜索条件、排序
     *
     * @param demandSearchDto
     * @return DemandSearchQuery
     * @since ChenQian
     */
    public DemandSearchQuery getDemandSearchQuery(DemandSearchDto demandSearchDto) {
        DemandSearchQuery demandSearchQuery = new DemandSearchQuery ();
        VoPoConverter.copyProperties ( demandSearchDto, demandSearchQuery );
        demandSearchQuery.setCenterGeoPoint ( new GeoPoint ( demandSearchDto.getLat (), demandSearchDto.getLon () ) );
        demandSearchQuery.setPage ( demandSearchDto.getCurrent (), demandSearchDto.getSize () );

        /**
         * 排序方式
         * 0.最多人>最近>在线状态
         * 1.最新>最近>在线状态
         * 2.最近>信用>在线状态
         * 3.支持网信>最近>在线状态
         * 4.信用>最近>在线状态
         * 5.价格最高>最近>在线状态
         * 不传：只需要你（距离、信用、在线状态）
         */
        if (demandSearchDto.getSort () == 0) {
            demandSearchQuery.addFristAscQueries ( new LastAscQuery ( "amount", false ) );
            demandSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else if (demandSearchDto.getSort () == 1) {
            demandSearchQuery.addFristAscQueries ( new LastAscQuery ( "createTime", false ) );
            demandSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else if (demandSearchDto.getSort () == 3) {
            demandSearchQuery.addFristAscQueries ( new LastAscQuery ( "creditSum", false ) );
            demandSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else if (demandSearchDto.getSort () == 4) {
            demandSearchQuery.addFristAscQueries ( new LastAscQuery ( "credit", false ) );
            demandSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else if (demandSearchDto.getSort () == 5) {
            demandSearchQuery.addFristAscQueries ( new LastAscQuery ( "wage", false ) );
            demandSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        } else {
            demandSearchQuery.addLastAscQuery ( new LastAscQuery ( "credit", false ) );
            demandSearchQuery.addLastAscQuery ( new LastAscQuery ( "isLogin", false ) );
        }
        return demandSearchQuery;
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

    /* 通过registerList 查询多个用户的头像 年龄等 返回key为userId的Map*/
    public Map getUserDateByRegisterList(List<DemandRegister> list) {
        if (list.size () == 0) {
            return null;
        }
        List userIdlist = new ArrayList ();
        for (int i = 0; i < list.size (); i++) {
            userIdlist.add ( list.get ( i ).getUserId () );
        }
        return getUserDateByUserID ( userIdlist );
    }

    /* 我发布的需求*/
    public Map<String, Object> getUserPublishDemandList(String userId, double lon, double lat, Page page) {
        Map<String, Object> map = new HashMap<> ();
        List<Demand> demandList = worthServiceprovider.getDemandService ().selectListByUserIdTwo ( userId, page );
        //调用User模块的方法获得当前用户信息
        UserInfoAndHeadImg UserData = getUserDateByUserID ( userId );
        map.put ( "userData", UserData );
        List<DemandDataDto> demandListDtoList = new ArrayList<> ();
        if (demandList != null) {
            for (Demand demand : demandList) {
                DemandDataDto demandDataDto = new DemandDataDto ();
                //需求信息
                VoPoConverter.copyProperties ( demand, demandDataDto );
                //距离
                demandDataDto.setDistance ( DistrictUtil.calcDistance ( demand.getLat ().doubleValue (), demand.getLon ().doubleValue (), lat, lon ) );
                //报名人数 根据demand_register表的demand_id  是demand表的id
                demandDataDto.setRegistersCount ( worthServiceprovider.getDemandRegisterService ().getRegCount ( demand.getId () ) );
                //处理图片
                demandDataDto.setImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demandDataDto.getImagesUrl (), AliyunBucketType.ActivityBucket ) );
                demandDataDto.setDetailsImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demandDataDto.getDetailsImagesUrl (), AliyunBucketType.ActivityBucket ) );
                //传申请需求的id demandRegisterid;
                //worthServiceprovider.getDemandRegisterService ().selectByUserIdAndDemand ( userId,demand.getId () );
                //处理金额
                if (demand.getWage () != null) {
                    demandDataDto.setWage ( Money.CentToYuan ( demand.getWage () ).getAmount () );
                }
                demandDataDto.setBail ( Money.CentToYuan ( demand.getBail () ).getAmount () );

                demandListDtoList.add ( demandDataDto );
            }
            map.put ( "list", demandListDtoList );
        }
        return map;
    }

    /* 我申请的需求*/
    public Map getRegisterDetailBydamandId(String userId, int current, int size, double lon, double lat) {
        Map map = new HashMap ();
        DemandDataDto demandDataDto;
        List<DemandDataDto> element = new ArrayList ();
        Page<DemandRegister> page = new Page ( current, size );
        List<DemandRegister> demandRegisterList = worthServiceprovider.getDemandRegisterService ().selectPageByUserIdTwo ( userId, page );
        if (demandRegisterList != null) {
            for (DemandRegister demandRegister : demandRegisterList) {
                demandDataDto = new DemandDataDto ();
                Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegister.getDemandId () );
                UserInfoAndHeadImg userInfoAndHeadImg = getUserDateByUserID ( demand.getUserId () );
                VoPoConverter.copyProperties ( userInfoAndHeadImg, demandDataDto );
                VoPoConverter.copyProperties ( demand, demandDataDto );
                //距离
                demandDataDto.setDistance ( DistrictUtil.calcDistance ( demandRegister.getLat ().doubleValue (), demandRegister.getLon ().doubleValue (), lat, lon ) );
                //报名人数 根据demand_register表的demand_id
                demandDataDto.setRegistersCount ( worthServiceprovider.getDemandRegisterService ().getRegCount ( demandRegister.getDemandId () ) );
                //处理图片
                demandDataDto.setImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demandDataDto.getImagesUrl (), AliyunBucketType.ActivityBucket ) );
                demandDataDto.setDetailsImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demandDataDto.getDetailsImagesUrl (), AliyunBucketType.ActivityBucket ) );
                //处理状态
                demandDataDto.setStatus ( demandRegister.getStatus () );
                //处理金额
                if (demand.getWage () != null) {
                    demandDataDto.setWage ( Money.CentToYuan ( demand.getWage () ).getAmount () );
                }
                demandDataDto.setBail ( Money.CentToYuan ( demand.getBail () ).getAmount () );
                element.add ( demandDataDto );
            }
        }
        map.put ( "list", element );
        return PageWrapper.wrapper ( page.getTotal (), map );
    }

    /* 申请人选信息  传入userID，报酬 状态 申请时间 */
    public DemandDataRegisterDto getRegisterData(String userId, Long wage, Date createtime, Integer status) {
        DemandDataRegisterDto demandDataRegisterDto = new DemandDataRegisterDto ();
        VoPoConverter.copyProperties ( getUserDateByUserID ( userId ), demandDataRegisterDto );
        demandDataRegisterDto.setCreatetime ( createtime );
        demandDataRegisterDto.setStatus ( status );
        if (wage != null) {
            demandDataRegisterDto.setWage ( Money.CentToYuan ( wage ).getAmount () );
        }
        return demandDataRegisterDto;
    }

    /* StringToList 处理标签等list信息*/
    public List<String> StringToList(String string) {
        List<String> list = new ArrayList<> ();
        if (string == null) {
            return null;
        }
        String[] stringsArr = string.split ( "," );
        for (int i = 0; i < stringsArr.length; i++) {
            list.add ( stringsArr[i] );
        }
        return list;
    }

    /* 需求详情 包含申请人员列表或者我申请的信息 发布者和申请者看到的东西不一样*/
    public Map<String, Object> getDemandDetail(String userId, double lon, double lat, String demandId) {
        Map<String, Object> map = new HashMap<> ();
        List wait = new ArrayList ();
        List pass = new ArrayList ();
        DemandDetailData demandDetailData = new DemandDetailData ();
        Demand demand;
        try {
            //需求demand表信息
            demand = worthServiceprovider.getDemandService ().selectById ( demandId );
        } catch (RuntimeException e) {
            logger.info ( e.getMessage (), e );
            throw new RuntimeException ( "此需求不存在" );
        }

        if (demand != null) {
            //处理图片
            demand.setImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demand.getImagesUrl (), AliyunBucketType.ActivityBucket ) );
            //将数据写入Dto
            VoPoConverter.copyProperties ( demand, demandDetailData );
            demandDetailData.setMerchantId ( demand.getDetailsImagesUrl () );
            //long now = System.currentTimeMillis ();
            //如果时间到了还没有入选者  自动取消该需求  钱退回发布者
            //System.out.println ( demand.getStartAt ().getTime ()+","+now );
            //标签list需要单独写
            demandDetailData.setDemandLabels ( StringToList ( demand.getDemandLabel () ) );
            demandDetailData.setObjectList ( StringToList ( demand.getObjList () ) );
            //邀请人数
//			map.put("registerAllCount", worthServiceprovider.getDemandRegisterService().getRegCount(demandId));
            //入选人数
            demandDetailData.setRegisterSuccessCount ( worthServiceprovider.getDemandRegisterService ().getRegSuccessCount ( demandId ) );
            //如果入选人数达到需求人数  将其他人的状态设置为未入选
            if (demandDetailData.getRegisterSuccessCount () == demand.getAmount ()) {
                List<DemandRegister> demandRegisterList = worthServiceprovider.getDemandRegisterService ().getDemandRegisterListByDemandIdAndStatus ( demandId, 0 );
                for (int i = 0; i < demandRegisterList.size (); i++) {
                    demandRegisterList.get ( i ).setStatus ( 3 );
                    worthServiceprovider.getDemandRegisterService ().updateById ( demandRegisterList.get ( i ) );
                }
            }
            //处理金额
            if (demandDetailData.getWage () != null) {
                demandDetailData.setWage ( Money.CentToYuan ( demandDetailData.getWage ().longValue () ).getAmount () );
            }
            demandDetailData.setBail ( Money.CentToYuan ( demandDetailData.getBail ().longValue () ).getAmount () );
            demandDetailData.setOrderPrice ( Money.CentToYuan ( demandDetailData.getOrderPrice ().longValue () ).getAmount () );
            demandDetailData.setDistance ( DistrictUtil.calcDistance ( demand.getLat ().doubleValue (), demand.getLon ().doubleValue (), lat, lon ) );

            //判断当前用户是不是需求发布人 是发布者 返回申请人员数据
            if (userId.equals ( demand.getUserId () )) {
                //申请人员  未接受和未入选和申请时取消的存在wait 其他都在pass
                List<DemandRegister> registeredDomainList = worthServiceprovider.getDemandRegisterService ().getListByDemandId ( demandId );
                if (registeredDomainList != null && registeredDomainList.size () > 0) {
                    registeredDomainList.forEach ( register -> {
                        DemandDataRegisterDto demandDataRegisterDto = this.getRegisterData ( register.getUserId (), register.getWage (), register.getCreateTime (), register.getStatus () );
                        demandDataRegisterDto.setDemandOrderId ( register.getDemandOrderId () );
                        demandDataRegisterDto.setDemandRegisterId ( register.getId () );
                        if (register.getStatus ().equals ( DemandRegisterStatus.REGISTERED.status ) || register.getStatus ().equals ( DemandRegisterStatus.FAIL.status ) || register.getStatus ().equals ( DemandRegisterStatus.CANCEL.status )) {
                            wait.add ( demandDataRegisterDto );
                        } else {
                            //pass的才有demandOrder
                            DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().getOrderByUserIdAndDemandId ( register.getUserId (), register.getDemandId () );
                            demandDataRegisterDto.setDemandOrderStatus ( demandOrder.getStatus () );
                            demandDataRegisterDto.setOrderId ( demandOrder.getOrderIds () );
                            pass.add ( demandDataRegisterDto );
                        }
                    } );
                }
                map.put ( "waitList", wait );
                map.put ( "passList", pass );
            } else {//是申请者 返回申请信息
                DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectByUserIdAndDemand ( userId, demandId );
                if (demandRegister != null) {
                    DemandRegisterReturnDt demandRegisterReturnDt = new DemandRegisterReturnDt ();
                    VoPoConverter.copyProperties ( demandRegister, demandRegisterReturnDt );
                    //设置距离
                    demandRegisterReturnDt.setDistance (
                            DistrictUtil.calcDistance ( demandRegister.getLat ().doubleValue (), demandRegister.getLon ().doubleValue (), lat, lon )
                    );
                    //获得商家消费
                    //处理金额
                    if (demandRegisterReturnDt.getWage () != null) {
                        demandRegisterReturnDt.setWage ( Money.CentToYuan ( demandRegisterReturnDt.getWage ().longValue () ).getAmount () );
                    }
                    demandRegisterReturnDt.setOrder_price ( Money.CentToYuan (
                            worthServiceprovider.getDemandService ().selectById ( demandId ).getOrderPrice ().longValue ()
                    ).getAmount () );

                    map.put ( "registerData", demandRegisterReturnDt );
                }
            }
        } else {
            throw new RuntimeException ( "该需求可能已取消" );
        }
        demandDetailData.setRegistersCount ( pass.size () + wait.size () );
        map.put ( "demandData", demandDetailData );
        return map;
    }

    /* 需求详情--发布者查看申请的人员列表*/
    public Map getDemandDetailPeoples(String userId, double lon, double lat, String demandId) {
        Map map = new HashMap ();
        List wait = new ArrayList ();
        List pass = new ArrayList ();
        List start = new ArrayList ();
        List end = new ArrayList ();
        DemandDataRegisterDto demandDataRegisterDto;
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandId );
//		//验证是否是用户是不是发布者
//		if(demand.getUserId ().equals ( userId )){
//			throw new RuntimeException ( "用户不是需求发布者无权限查看申请人员信息" );
//		}
        if (demand != null) {
            //申请人员 通过demandId和status查找表DemandRegister
            List<DemandRegister> registeredDomainList = worthServiceprovider.getDemandRegisterService ().getDemandRegisterListByDemandIdAndStatus ( demandId, 0, 1, 2, 3, 4, 7, 8 );
            if (registeredDomainList != null && registeredDomainList.size () > 0) {
                for (DemandRegister register : registeredDomainList) {
                    //获得用户信息 同时将其他参数封装到Dto返回
                    demandDataRegisterDto = this.getRegisterData ( register.getUserId (), register.getWage (), register.getCreateTime (), register.getStatus () );
                    demandDataRegisterDto.setDemandRegisterId ( register.getId () );
                    demandDataRegisterDto.setDemandOrderId ( register.getDemandOrderId () );
                    if (register.getStatus () != 0 && register.getStatus () != 3 && register.getStatus () != 2) {
                        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().getOrderByUserIdAndDemandId ( register.getUserId (), register.getDemandId () );
                        demandDataRegisterDto.setDemandOrderStatus ( demandOrder.getStatus () );
                        demandDataRegisterDto.setOrderId ( demandOrder.getOrderIds () );
                    }
                    switch (register.getStatus ()) {
                        case 0:
                        case 2:
                        case 3:
                        case 7:
                            wait.add ( demandDataRegisterDto );
                            break;
                        case 1:
                            //通过者才有demandOrder
                            pass.add ( demandDataRegisterDto );
                            break;
                        case 4:
                            start.add ( demandDataRegisterDto );
                            break;
                        case 8:
                            end.add ( demandDataRegisterDto );
                            break;
                    }
                }
            }
            map.put ( "waitList", wait );
            map.put ( "passList", pass );
            map.put ( "startList", start );
            map.put ( "endList", end );

        } else {
            throw new RuntimeException ( "此需求不存在" );
        }
        return map;
    }

    /* 发布者查看需求预约者的详情  未接受预约的 和已经接受的一个接口 先判断好 */
    public Map<String, Object> getRegisterDetail(String userId, double lon, double lat, String demandRegisterId) {
        Map<String, Object> map = new HashMap<> ();
        DemandDataRegisterDetailDto demandDataRegisterDetailDto = new DemandDataRegisterDetailDto ();
        DemandOrder demandOrder = null;
        //demandRegister表信息
        DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectById ( demandRegisterId );
        if (demandRegister == null) {
            throw new RuntimeException ( "此需求申请不存在" );
        }
        //不为空即已经生成order数据  返回order数据
        if (demandRegister.getDemandOrderId () != null) {
            demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( demandRegister.getDemandOrderId () );
        }
        Demand demand = demandAction.selectById ( demandRegister.getDemandId () );
        //判断是否是发布者------呃，是判断该需求是否存在吧
        if (demand == null) {
//            throw new RuntimeException ( "预约需求的发布者不存在" );
            throw new RuntimeException ( "该需求不存在" );
        }
        UserSynopsisData userSynopsisData;
        try {
            if (demand.getUserId ().equals ( userId )) {
                map.put ( "isPublish", true );
                map.put ( "bail", Money.CentToYuan ( demand.getBail () ).getAmount () );
                userSynopsisData = userAction.getUserSynopsisData ( demandRegister.getUserId (), userId );
            } else {
                map.put ( "isPublish", false );
                userSynopsisData = userAction.getUserSynopsisData ( demand.getUserId (), userId );
            }
        } catch (Exception e) {
            logger.error ( e.getMessage () );
            throw new RuntimeException ( "查询用户信息失败" );
        }
        //复制用户信息
        VoPoConverter.copyProperties ( userSynopsisData, demandDataRegisterDetailDto );
        //设置商家消费id  list类型
        demandDataRegisterDetailDto.setOrderId ( this.StringToList ( demand.getOrderIds () ) );
        //处理tag标签
        demandDataRegisterDetailDto.setTags ( StringToList ( userSynopsisData.getTag () ) );
        //发布者预先设定好的报酬

        //需求的人数
        demandDataRegisterDetailDto.setAmount ( demand.getAmount () );
        demandDataRegisterDetailDto.setAbout ( demand.getAbout () );
        if (demandOrder == null) {//未被选择的预约申请  复制register表信息
            //预约者需要处理iseachwage
            demandDataRegisterDetailDto.setEachWage ( demand.getEachWage () );
            VoPoConverter.copyProperties ( demandRegister, demandDataRegisterDetailDto );
            //预约地点和预约者申请时候的距离
            if (demandRegister.getWage () != null) {
                demandDataRegisterDetailDto.setWage ( Money.CentToYuan ( demandRegister.getWage () ).getAmount () );
            }
            demandDataRegisterDetailDto.setPredistance ( DistrictUtil.calcDistance ( lat, lon, demandRegister.getLat ().doubleValue (), demandRegister.getLon ().doubleValue () ) );
            //处理金额
            demandDataRegisterDetailDto.setOrder_price ( Money.CentToYuan ( demand.getOrderPrice () ).getAmount () );
        } else {//已经被选择的预约申请 复制order表信息
            demandDataRegisterDetailDto.setCode ( demandRegister.getCode () );
            //是否是总报酬不确定
            VoPoConverter.copyProperties ( demandOrder, demandDataRegisterDetailDto );
            //设置id
            demandDataRegisterDetailDto.setDemandorderId ( demandOrder.getId () );
            demandDataRegisterDetailDto.setId ( demandRegister.getId () );
            //设置状态
            demandDataRegisterDetailDto.setOrder_status ( demandOrder.getStatus () );
            demandDataRegisterDetailDto.setStatus ( demandRegister.getStatus () );
            //预约地点和预约者申请时候的距离
            demandDataRegisterDetailDto.setPredistance ( DistrictUtil.calcDistance ( lat, lon, demandOrder.getLat ().doubleValue (), demandOrder.getLon ().doubleValue () ) );
            //处理金额
            demandDataRegisterDetailDto.setWage ( Money.CentToYuan ( demandOrder.getWage () ).getAmount () );
            //处理金额
            demandDataRegisterDetailDto.setOrder_price ( Money.CentToYuan ( demandOrder.getOrderPrice () ).getAmount () );
            //当订单状态为6时返回退款信息
            if(demandOrder.getStatus().equals(6)){
                Integer isRefund = worthServiceprovider.getRefundService().getRefund(demandOrder.getId(), demand.getUserId());
                if(isRefund != 0 ){
                    Refund refund = worthServiceprovider.getRefundService().getRefundId(demandOrder.getId(), demand.getUserId());
                    if(refund != null)
                        demandDataRegisterDetailDto.setRefund(refund);
                }
            }
        }
        if (demand.getWage () != null) {
            demandDataRegisterDetailDto.setPublisterWage ( Money.CentToYuan ( demand.getWage () ).getAmount () );
        }
        map.put ( "data", demandDataRegisterDetailDto );
        return map;
    }

    /* 提醒商家提供服务推送*/
/*    public void provideService(String demandRegisterId, String userId) {
        DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectById ( demandRegisterId );

        DemandOrder order = worthServiceprovider.getDemandOrderService ().selectById ( demandRegister.getDemandOrderId () );
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegister.getDemandId () );


        WzCommonImHistoryAction wzCommonImHistoryAction = new WzCommonImHistoryAction ();
        String message = "\""+demand.getTitle () + "\"需求发布成功，时间" + DateTimestampUtil.getDateStrByDate ( order.getStartAt () ) + "，地点" + order.getAddress () + "，人数" + 1 + "人、消费" + 2 + "，请准备";
        try {
            wzCommonImHistoryAction.add ( userId, order.getId (), message, demand.getId ().toString (),
                    MessageTypeEnum.ACTIVITY_TYPE, WZ_DEMANDDETAIL, null );
        } catch (Exception e) {
            throw new RuntimeException ( e );
        }
    }*/

    /* 发布者接受申请*/
    @Transactional(rollbackFor = Exception.class)
    public boolean publishOrder(DemandOrder demandOrder, DemandRegister register) {
        demandOrder.setId ( null );
        demandOrder.setPay ( false );
        demandOrder.setStatus ( DemandOrderStatus.CONFIRM.status );
        //托管金额和报酬换为分存入数据库
        demandOrder.setWage ( new Money ( demandOrder.getWage () ).getCent () );
        demandOrder.setBail ( new Money ( demandOrder.getBail () ).getCent () );

        boolean success = worthServiceprovider.getDemandOrderService ().insert ( demandOrder );
        //设置rigister表的状态为 已入选
        register.setStatus ( DemandRegisterStatus.SUCCESS.status );
        register.setDemandOrderId ( worthServiceprovider.getDemandOrderService ().getOrderByUserIdAndDemandId ( demandOrder.getUserId (), demandOrder.getDemandId () ).getId () );
        worthServiceprovider.getDemandRegisterService ().updateById ( register );
        try {
            boolean orderJob = jobFuseAction.addJob ( JobEnum.DEMAND_CHECK_ORDER_JOB, demandOrder.getId (), demandOrder.getId (), "检测是否评论", new Date ( demandOrder.getEndAt ().getTime () + 24l * 3600 * 1000 ), null );
            if (!orderJob) {
                throw new RuntimeException ( "创建需求开始,检查启动情况定时任务失败" );
            }
        } catch (Exception e) {

        }

        //如果是第一个确定的 启动定时任务DemandCheckStartJobHandler
        if (worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandId ( register.getDemandId () ) == 1) {
            boolean orderJob = jobFuseAction.addJob ( JobEnum.DEMAND_CHECK_START_JOB, demandOrder.getId (),
                    demandOrder.getId (), "检测是否需求启动情况并扣除违约人信用",
                    new Date ( demandOrder.getStartAt ().getTime () + 8 * 1000 ),
                    AuthorEmailEnum.FRWIN );
            if (!orderJob) {
                logger.error ( "检测需求启动审核定时任务失败" );
            }
            boolean isSuccess = jobFuseAction.addJob ( JobEnum.DEMAND_CHECK_REGISTER_START, demandOrder.getId (),
                    demandOrder.getId (), "检测需求开始前60分钟入选者是否已启动需求",
                    new Date ( demandOrder.getStartAt ().getTime () + 60*60 * 1000 ),
                    AuthorEmailEnum.FRWIN );
            if (!isSuccess) {
                logger.error ( "检测需求开始前60分钟入选者是否已启动需求定时任务失败" );
            }

        }

        return success;

    }

    /**
     * 需求正常结束或需求失败,剩余金额退回
     */
    public String demandEndMoneyCallBack(Demand demand,int moneyGet) {
        //获取已完成需求的所有使用报酬金额
        long wage = worthServiceprovider.getDemandOrderService ().getDemandWagesByDemandIdAndStatus( demand.getId (), 9 );
        //托管金 - 已使用报酬金额 = 所有剩余金额
        long surplusWage = demand.getBail () - wage;
        if (surplusWage <= 0) {
            return "无剩余金额";
        } else {
            //平台拿钱(获取冻结金额的钱),不用把剩余金额付给发布者(因为报酬是平台给的,所以获取冻结金额相当于把余款退回给平台)
            FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto ();
            requestDto.setTypeId ( demand.getId () );
            requestDto.setUserId ( demand.getUserId () );
            requestDto.setType ( FrozenTypeEnum.FTZ_DEMAND );
            Boolean successBill = walletFrozenAction.pay ( requestDto );
            if (!successBill) {
                logger.error ( "退回费用出现异常" );
                throw new RuntimeException ( "退回费用出现异常!" );
            }
            //结算出席的入选者报酬
            if( moneyGet == 1){
                //根据order表得到入选者的人数
                long orderCount = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandId ( demand.getId () );
                //结算报酬
                presentPay( null,orderCount, demand);
            }else if ( moneyGet == 0 ) {
            //需求正常结束或需求失败时,资金回退发布者(moneyGet为0时),否则余款归平台所有
            //发布者拿钱,平台付款
                BillAddRequestDto billAddRequestDto = new BillAddRequestDto ();
                billAddRequestDto.setToUserId ( demand.getUserId () );
                billAddRequestDto.setPayChannel ( 3 );
                billAddRequestDto.setType ( 1 );
                billAddRequestDto.setAmount ( Money.CentToYuan ( surplusWage ).getAmount () );
                billAddRequestDto.setDescription ( "\""+demand.getTitle () + "\"的余款回退发布者" + Money.CentToYuan ( surplusWage ).getAmount () + "元" );
                boolean success = walletBillClientAction.addBill ( "999", billAddRequestDto );
                if (!success) {
                    throw new RuntimeException ( "余款回退发布者时出现异常!" );
                }
            }
        }
        return "返回成功!";
    }

    public int checkIsPresent(String userId, String registerId, double lon, double lat) {
        //获取验证码
        int code = demandAction.start ( userId, registerId, lon, lat );
        DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectById ( registerId );
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegister.getDemandId () );
        if (demand.getEndAt () != null) {
            /**产生定时任务:检测需求完成24小时后是否自动结算报酬*/
            boolean paySuccess = jobFuseAction.addJob ( JobEnum.DEMAND_CHECK_AUTO_PAY_JOB, demand.getId (), userId, "\""+demand.getTitle () + "\"检测需求完成24小时后是否自动结算报酬", new Date ( demand.getEndAt ().getTime () + TIME_DIFFERENCE ), AuthorEmailEnum.FRWIN );
            if (!paySuccess) {
                logger.error ( "create timed task -> checkPublisherIsPay error" );
                throw new RuntimeException ( "创建“检测需求完成24小时后是否自动结算报酬”定时任务失败" );
            }
        }
        return code;
    }

    //定时任务调用方法  检测需求完成24小时后是否自动结算报酬
    public int checkPublisherIsPay(String demandId) {
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandId );
        List<DemandOrder> demandOrderList = worthServiceprovider.getDemandOrderService ().getDemandOrderListByDemandIdAndStatus ( demandId, 3, 5 );
        long nowTime = System.currentTimeMillis ();
        //需求结束时间不为空或者需求结束时间小于定时任务执行时间,执行下一步
        if (demand.getEndAt () != null && demand.getEndAt ().getTime () <= nowTime) {
            //先判断入选人数是否为0,若不为0进入for循环
            if (demandOrderList.size () != 0) {
                //for循环,先判断入选者状态是否还是3或者5  若是,进入自动结算
                for (int i = 0; i < demandOrderList.size (); i++) {
                    if (3 == demandOrderList.get ( i ).getStatus () || 5 == demandOrderList.get ( i ).getStatus ()) {
                        boolean success = publishPay ( demandOrderList.get ( i ).getId (), demand.getUserId () );
                        if (!success) {
                            return 0;
                        }
                    }
                }
            }
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 定时任务:检测需求开始后30分钟是否有入选者验证码通过
     */
    public int checkCodePass(String demandId) {
        //获取入选者验证码通过人数  即状态为5,6,8,9
        int count = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandIdAndStatus ( demandId, 5, 6, 8, 9 );
        if (count > 0) {
            return 1;
        } else {
            Demand demand = worthServiceprovider.getDemandService ().selectById ( demandId );
            demand.setStatus ( DemandStatus.CANCEL.getStatus () );
            worthServiceprovider.getDemandService ().updateById ( demand );
            //需求失败,获取入选者启动需求人数
            int countStart = worthServiceprovider.getDemandOrderService ().getDemandOrderCountByDemandIdAndStatus ( demandId, 3 );
            int moneyGet;
            //如果存在3即证明有入选者出席
            if (countStart > 0) {
                //需求失败,入选者出席,发布者未出席,结算入选者金额,余款归平台
                moneyGet = 1;
                wzCommonImHistoryAction.add ( "999", demand.getUserId (),"\""+demand.getTitle ()+"\"需求已开始30分钟，您还未验证到场，系统判定您为缺席。",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
            } else {
                //没有入选者出席,金额解冻 全部退回发布者
                moneyGet = 0;
            }
            demandEndMoneyCallBack ( demand, moneyGet );
            return 0;
        }
    }

    //申请需求
    public String register(DemandRegisterDto demandRegisterDto) {

        String result = demandAction.register(demandRegisterDto);
        //推送申请动态
        if(result == null){
            Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegisterDto.getDemandId () );
            User registerUser = userAction.getUserService ().getUserById ( demandRegisterDto.getUserId () );
            wzCommonImHistoryAction.add ( demandRegisterDto.getUserId (), demand.getUserId (),registerUser.getNickname ()+"申请了您的\""+demand.getTitle ()+"\"需求！",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
        }
        return result;
    }

    //入选者接受申请人申请需求
    public boolean acceptRegister(String id, String userId) {
        String result = demandRegisterAction.acceptRegister ( id, userId );
        String[] resultArray = result.split ( "," );
        DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectById ( id );
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegister.getDemandId () );
        if (Integer.parseInt ( resultArray[0] ) == demand.getAmount ()) {
            //入选者人数已足够  即确认所有人入选者了  发布推送
            wzCommonImHistoryAction.add ( "999", demand.getUserId (), "您已确认\"" + demand.getTitle () + "\"需求的所有入选者，请在24小时内确认需求细节，逾期需求将自动取消，所托管费用将会归平台所有！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
        }
        wzCommonImHistoryAction.add ( demand.getUserId (), demandRegister.getUserId (), "恭喜您已入选\"" + demand.getTitle () + "\"需求！！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
        if (resultArray[1].equals ( "success" )) {
            return true;
        } else {
            return false;
        }
    }

    //发布者取消需求推送
    public boolean publishCancel(String demandId, String userId) {
        boolean success = demandAction.publishCancel ( demandId, userId );
        if(success){
            Demand demand = worthServiceprovider.getDemandService ().selectById ( demandId );
            List<DemandRegister> demandRegisterList = worthServiceprovider.getDemandRegisterService ().getListByDemandId ( demandId );
            for(int i = 0; i < demandRegisterList.size (); i++){
                wzCommonImHistoryAction.add (  userId,demandRegisterList.get ( i ).getUserId (),"\""+demand.getTitle ()+"\"需求已取消！",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
            }
        }
        return success;
    }

    //发布者结束报名以及推送
    @Transactional(rollbackFor = Exception.class)
    public String publishStop(String demandId, String userId){
        Demand demand = worthServiceprovider.getDemandService().selectById(demandId);
        if(demand==null){
            return "该需求可能已经删除";
        }
        if(demand.getStatus().equals(DemandStatus.START.status) || !demand.getStatus().equals(DemandStatus.PUBLISHED.status)){
            return "该需求已结束报名";
        }
        if(!demand.getUserId().equals(userId)){
            return "你非发布者，不能结束报名";
        }
        demand.setStatus(DemandStatus.START.status);
        //届时所有未入选者状态变为3  未入选
        List<DemandRegister> demandRegisterList = worthServiceprovider.getDemandRegisterService ().getDemandRegisterListByDemandIdAndStatus ( demandId,0 );
        for (int i=0;i<demandRegisterList.size ();i++){
            demandRegisterList.get ( i ).setStatus ( 3 );
            worthServiceprovider.getDemandRegisterService ().updateById ( demandRegisterList.get ( i ) );
            //推送
            wzCommonImHistoryAction.add ( demand.getUserId (),demandRegisterList.get ( i ).getUserId (),"抱歉，您的\""+demand.getTitle ()+"\"需求申请未能入选",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
        }
        //手动提前结束报名  即确认所有人入选者了  发布推送
        wzCommonImHistoryAction.add ( "999", demand.getUserId (), "您已确认\"" + demand.getTitle () + "\"需求的所有入选者，请在24小时内确认需求细节，逾期需求将自动取消，所托管费用将会归平台所有 ！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null );
        return worthServiceprovider.getDemandService().updateById(demand)?null:"结束报名失败";
    }

    //发布者取消入选者以及推送
    public boolean cancelRegister(String userId, String orderId) throws Exception {
        //检验当前用户是否是发布者
        int count = worthServiceprovider.getDemandOrderService().getDemandOrderCountByIdAndUserId(orderId,userId);
        if(count!=1){
            throw  new RuntimeException ( "用户不存在此需求" );
        }
        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().selectById ( orderId );
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandOrder.getDemandId () );
        boolean deleteOrder = worthServiceprovider.getDemandOrderService().deleteById(orderId);
        boolean updateRegister = worthServiceprovider.getDemandRegisterService().cancelRegister(orderId);
        //推送
        wzCommonImHistoryAction.add ( userId, demandOrder.getUserId (),"很抱歉，\""+demand.getTitle ()+"\"需求发起人取消了您的入选！",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
        return deleteOrder && updateRegister;
    }

    //入选者放弃参与需求
    public boolean registerDrop(String userId, String registerId) {
        //检验用户是否是申请人
        int count = worthServiceprovider.getDemandRegisterService().IsExitsRegisterByIdAndUserId (registerId,userId);
        if(count!=1){
            throw  new RuntimeException ( "用户不存在此需求" );
        }
        //删除数据库demand_order表数据

        DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectById ( registerId );
        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().getOrderByUserIdAndDemandId ( userId, demandRegister.getDemandId () );
        worthServiceprovider.getDemandOrderService ().deleteById ( demandOrder.getId () );
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandOrder.getDemandId () );
        User user = userAction.getUserService ().getUserById ( demandOrder.getUserId () );
        //推送
        wzCommonImHistoryAction.add ( userId, demand.getUserId (),"入选者，\""+user.getNickname ()+"已放弃参与"+demand.getTitle ()+"\"需求！",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
        //更改demand_register表数据
        return worthServiceprovider.getDemandRegisterService().registerDrop(userId, registerId);
    }

    //发起人申请退款
    @Transactional(rollbackFor = Exception.class)
    public boolean publishRefund(CreateRefundDto createRefundDto){
        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(createRefundDto.getId());
        demandOrder.setStatus(DemandOrderStatus.REFUNDMENT.status);
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandOrder.getDemandId () );
        //推送
        long wage = Money.CentToYuan ( demandOrder.getWage () ).getAmount ().longValue ();
        wzCommonImHistoryAction.add ( demand.getUserId (), demandOrder.getUserId (),"\""+ demand.getTitle () + "\"需求发起人要求退款"+ wage +"元",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
        //更改状态并创建退款表
        return worthServiceprovider.getDemandOrderService().updateById(demandOrder) && refundAction.create(createRefundDto.getDescription(),
                createRefundDto.getBail(), createRefundDto.getPayWay(), createRefundDto.getId(), "DemandOrder",
                createRefundDto.getAmount(), createRefundDto.getUserId())!=null;
    }

    //入选者拒绝申请人申请退款
    public boolean arbitrationStatus(String demandOrderId) {
        boolean success = demandAction.arbitrationStatus(demandOrderId);
        if(success){
            //推送
            DemandOrder demandOrder  = worthServiceprovider.getDemandOrderService ().selectById ( demandOrderId );
            Demand demand = worthServiceprovider.getDemandService ().selectById ( demandOrder.getDemandId () );
            User user = userAction.getUserService ().getUserById ( demandOrder.getUserId () );
            wzCommonImHistoryAction.add ( "999", demand.getUserId (),"\""+ user.getNickname () + "\"拒绝了您的退款要求，现已进入仲裁流程!请耐心等待仲裁结果！！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
        }
        return success;
    }
    //发布者拒绝入选者申请
    public boolean registerRefuse(String demandRegisterId) {
        boolean success = demandAction.registerRefuse ( demandRegisterId );
        if(success){
            //推送
            DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectById ( demandRegisterId );
            Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegister.getDemandId () );
            wzCommonImHistoryAction.add ( demand.getUserId (),demandRegister.getUserId (),"抱歉，您的\""+demand.getTitle ()+"\"需求申请未能入选",demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
        }
        return success;
    }

    /**定时任务:检测需求开始前60分钟入选者是否已启动需求*/
    public int checkSixtyMinuteStart(String demandId){
        List<DemandOrder> demandOrderList = worthServiceprovider.getDemandOrderService ().getDemandOrderListByDemandIdAndStatus ( demandId, 2 );
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandId );
        for(int i = 0; i < demandOrderList.size (); i++){
            //推送
            wzCommonImHistoryAction.add ( "999", demandOrderList.get ( i ).getUserId ()," \""+demand.getTitle ()+"\"需求还有一小时就要开始了，请在需求开始前到达现场并启动需求，逾期系统已判定您为缺席！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_DEMANDDETAIL, null);
        }
        return 0;
    }

    //结算实际出席入选者的报酬
    public int presentPay(DemandOrder demandOrder, long orderCount, Demand demand) {
        List<DemandOrder> demandOrderList = worthServiceprovider.getDemandOrderService ().getDemandOrderListByDemandIdAndStatus ( demand.getId (), 3, 5 );
        int size = demandOrderList.size ();
        for (int i = 0; i < size; i++) {
            //若demandOrd不为空 则为正常完成需求  单个结算报酬
            if (demandOrder != null) {
                size = 1;
            }else {
                //推送--->实际出席入选者
                wzCommonImHistoryAction.add ( "999", demandOrderList.get ( i ).getUserId (),"\""+demand.getTitle ()+"\"需求发起人未到场，需求失败。您的报酬已发放!请到收支明细查看！", demand.getId (), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_WALLET, null);
                demandOrder = demandOrderList.get ( i );
            }
            if (demand.getWage () != 0 && demand.getWage () != null) { //如有报酬则进入流水结账
                BigDecimal sharedFee = getSharedFee ();//获取后台设置网值的手续费比例
                BillAddRequestDto requestDto = new BillAddRequestDto ();
                requestDto.setToUserId ( demandOrder.getUserId () );
                requestDto.setPayChannel ( 3 );
                requestDto.setType ( 1 );
                //平台付钱给申请者 扣除手续费
                //每一份都要收取手续费
                long wage = demand.getWage ();
                if (!demand.getEachWage ()) {//不是单位报酬
                    wage /= orderCount;
                }
                requestDto.setAmount ( Money.CentToYuan ( wage ).getAmount ().multiply ( BigDecimal.ONE.subtract ( sharedFee ) ) );
                requestDto.setDescription ( demand.getTitle () + "的报酬收入（已收取手续费" + Money.CentToYuan ( wage - new Money ( requestDto.getAmount () ).getCent () ).getAmount () + "元）" );
                boolean success = walletBillClientAction.addBill ( "999", requestDto );
                if (!success) {
                    throw new RuntimeException ( "支付交易异常" );
                }
                //预约单状态为关闭
                demandOrder.setStatus ( DemandOrderStatus.AllEND.status );
                demandOrder.setWage ( wage );
                if (!worthServiceprovider.getDemandRegisterService ().registerClose ( demandOrder.getId () )) {
                    throw new RuntimeException ( "预约单关闭失败" );
                }
                success = worthServiceprovider.getDemandOrderService ().updateById ( demandOrder );
                if (!success) {
                    throw new RuntimeException ( "更新异常，请稍候重试" );
                }
            }
        }
        return 0;
    }
    //时间转换
    public String timeTransform(Date time){
        //可以方便地修改日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String changeDate = dateFormat.format ( time );
        return  changeDate;
    }

    /**
     * 需求失败,进行退款操作(已合并至demandEndMoneyCallBack.暂时不删除此方法)
     */

   /* public String demandFailMoneyCallBack(Demand demand, boolean moneyGet) {
        //获取已完成需求的所有使用报酬金额
        long wage = worthServiceprovider.getDemandRegisterService ().getDemandWagesByDemandIdAndStatus ( demand.getId (), 4 );
        //托管金 - 已使用报酬金额 = 所有剩余金额
        long surplusWage = demand.getBail () - wage;
        if (surplusWage <= 0) {
            return "无剩余金额";
        } else {
            //平台拿钱,不用把剩余金额付给发布者(因为报酬是平台给的,所以余款退回给平台时不用做任何其他操作)
            FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
            requestDto.setTypeId(demand.getId ());
            requestDto.setUserId(demand.getUserId ());
            requestDto.setType(FrozenTypeEnum.FTZ_DEMAND);
            Boolean successBill = walletFrozenAction.pay(requestDto);
            if (!successBill) {
                logger.error ( "退回费用出现异常" );
                throw new RuntimeException ("退回费用出现异常!");
            }
            //发布者拿钱,平台付款
            if ( !moneyGet ) {
                BillAddRequestDto billAddRequestDto = new BillAddRequestDto ();
                billAddRequestDto.setToUserId ( demand.getUserId () );
                billAddRequestDto.setPayChannel ( 3 );
                billAddRequestDto.setType ( 1 );
                billAddRequestDto.setAmount ( Money.CentToYuan ( surplusWage ).getAmount () );
                billAddRequestDto.setDescription ( demand.getTitle () + "的余款" + Money.CentToYuan (  new Money ( surplusWage ).getCent () ).getAmount () + "元）" );
                boolean success = walletBillClientAction.addBill ( "999", billAddRequestDto );
                if (!success) {
                    throw new RuntimeException ( "余款回退发布者时出现异常!" );
                }
            }
        }
        return "返回成功!";
    }
*/
   /*
   查看使用冻结金额的流向
    public void checkMoneyHowToUser(){

        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId("1a0d7c5bae4c467884cbfc00322bba04");
        requestDto.setUserId("a15239dce8f44088828bcb50f19a9eb4");
        requestDto.setType(FrozenTypeEnum.FTZ_DEMAND);
        walletFrozenAction.pay(requestDto);
    }*/

    /**
     * 需求信息
     * @param id
     * @return
     */
    public Map<String,Object> getDemandById(String id,Double lon,Double lat){
        Map<String,Object> map = new HashMap<>();
        //获得最新发布的需求
        Demand demand = worthServiceprovider.getDemandService().selectById (id);
        DemandListVo demandListVo = null;
        //获得申请人数
        if(demand!=null){
            demandListVo = new DemandListVo();
            BeanUtils.copyProperties(demand,demandListVo);
            //处理图片
            demandListVo.setImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demand.getImagesUrl (), AliyunBucketType.ActivityBucket ) );
            demandListVo.setDetailsImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demand.getDetailsImagesUrl (), AliyunBucketType.ActivityBucket ) );
            //处理金额
            if(demand.getWage ()!=null) {
                demandListVo.setWageBig(Money.CentToYuan(demand.getWage()).getAmount());
            }
            if(demand.getBail()!=null){
                demandListVo.setBailBig(Money.CentToYuan(demand.getBail()).getAmount());
            }
            if(demand.getOrderPrice()!=null){
                demandListVo.setOrderPriceBig(Money.CentToYuan(demand.getOrderPrice()).getAmount());
            }
        }
        //获取用户的信息
        UserInfoAndHeadImg UserData = userAction.getUserInfoAndHeadImg ( demand.getUserId() );
        map.put("list",demandListVo);//最新一条需求
        map.put ("userData", UserData );
        map.put("distance",DistrictUtil.calcDistance ( demand.getLat ().doubleValue (), demand.getLon ().doubleValue (), lat, lon ));
        return map;
    }
}

