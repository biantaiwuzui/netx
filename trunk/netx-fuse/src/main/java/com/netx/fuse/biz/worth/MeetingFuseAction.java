package com.netx.fuse.biz.worth;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.netx.common.common.enums.*;
import com.netx.common.common.vo.message.GroupDto;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.wz.dto.meeting.*;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.shopping.biz.ordercenter.MerchantOrderInfoAction;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.ordercenter.constants.OrderTypeEnum;
import com.netx.shopping.model.ordercenter.constants.PayStatus;
import com.netx.shopping.service.merchantcenter.MerchantManagerService;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.service.ordercenter.MerchantOrderInfoService;
import com.netx.ucenter.biz.common.EvaluateAction;
import com.netx.ucenter.biz.common.GroupAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.friend.FriendsAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.common.CommonEvaluate;
import com.netx.ucenter.model.user.User;
import com.netx.utils.DistrictUtil;
import com.netx.utils.cache.RedisCache;
import com.netx.worth.biz.common.WorthClickHistoryAction;
import com.netx.worth.enums.*;
import com.netx.worth.service.WorthServiceprovider;

import com.netx.worth.vo.MeetingDetailResponseDto;
import com.netx.worth.vo.MeetingDetailSendDto;
import io.swagger.annotations.ApiOperation;
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
import com.netx.common.vo.common.FrozenQueryRequestDto;
import com.netx.common.vo.common.WzCommonWalletFrozenResponseDto;
import com.netx.common.wz.dto.common.CommonPayCallbackDto;
import com.netx.fuse.client.shoppingmall.OrderClientAction;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import com.netx.fuse.client.ucenter.WalletForzenClientAction;
import com.netx.fuse.client.ucenter.WangMingClientAction;
import com.netx.fuse.proxy.ClientProxy;
import com.netx.fuse.client.ucenter.CostSettingClientAction;
import com.netx.fuse.proxy.EvaluateProxy;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.fuse.proxy.WalletFrozenProxy;
import com.netx.searchengine.model.MeetingSearchResponse;
import com.netx.searchengine.query.MeetingSearchQuery;
import com.netx.searchengine.service.MeetingSearchService;
import com.netx.utils.money.Money;
import com.netx.worth.biz.meeting.MeetingAction;
import com.netx.worth.biz.meeting.MeetingRegisterAction;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.model.Meeting;
import com.netx.worth.model.MeetingRegister;
import com.netx.worth.model.MeetingSend;
import com.netx.worth.service.MeetingRegisterService;
import com.netx.worth.service.MeetingSendService;

@Service
public class MeetingFuseAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private MeetingRegisterAction meetingRegisterAction;
    /*	@Autowired
        private ActiveLogAction activeLogAction;*/
    @Autowired
    private MeetingAction meetingAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private WalletForzenClientAction walletForzenClientAction;
    @Autowired
    private ClientProxy clientProxy;
    @Autowired
    private CostSettingClientAction costSettingClientAction;
    @Autowired
    private WalletFrozenProxy walletFrozenProxy;
    @Autowired
    private WalletBillClientAction walletBillClientAction;
    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;
    @Autowired
    private MeetingSearchService meetingSearchService;
    @Autowired
    private UserClientProxy userClientProxy;
    @Autowired
    private OrderClientAction orderClientAction;
    @Autowired
    private WangMingClientAction wangMingClientAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private UserAction userAction;
    @Autowired
    private FriendsAction friendsAction;
    @Autowired
    private MerchantOrderInfoService merchantOrderInfoService;
    @Autowired
    private MerchantOrderInfoAction merchantOrderInfoAction;
    @Autowired
    private MerchantManagerService merchantManagerService;
    @Autowired
    private MeetingSendService meetingSendService;
    @Autowired
    private MeetingRegisterService meetingRegisterService;
    @Autowired
    private NetEnergyFuseAction netEnergyFuseAction;
    @Autowired
    private WorthClickHistoryAction worthClickHistoryAction;
    @Autowired
    RedisInfoHolder redisInfoHolder;
    RedisCache redisCache;
    @Autowired
    JobFuseAction jobFuseAction;
    @Autowired
    private EvaluateAction evaluateAction;
    @Autowired
    private GroupAction groupAction;


    private void createRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
    }

    public MeetingSendService getMeetingSendService() {
        return meetingSendService;
    }

    public MeetingRegisterService getMeetingRegisterService() {
        return meetingRegisterService;
    }

    //我附近的聚会
    public Map<String, List> nearHasManyList(String userId, BigDecimal lon, BigDecimal lat, Double length, Page<Meeting> page) {
        Map<String, List> map = new HashMap<>();
        MeetingSearchQuery meetingSearchQuery = new MeetingSearchQuery();
        meetingSearchQuery.setPageSize(page.getSize());
        meetingSearchQuery.setFrom(page.getCurrent());
        meetingSearchQuery.setMaxDistance(length);
        meetingSearchQuery.setUserId(userId);
        GeoPoint geoPoint = new GeoPoint(lat.doubleValue(), lon.doubleValue());
        meetingSearchQuery.setCenterGeoPoint(geoPoint);
        List<MeetingSearchResponse> targetList = meetingSearchService.queryMeetings(meetingSearchQuery);
        List<Meeting> list = new ArrayList<>();
        Meeting source = null;
        if (targetList != null && targetList.size() > 0) {
            for (MeetingSearchResponse meetingSearchResponse : targetList) {
                source = new Meeting();
                BeanUtils.copyProperties(meetingSearchResponse, source);
                list.add(source);
            }
        }
        if (list != null && list.size() > 0) {
            for (Meeting meeting : list) {
                meeting.setMeetingImagesUrl((addImgUrlPreUtil.addImgUrlPres(meeting.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket)));
                meeting.setPosterImagesUrl(meeting.getPosterImagesUrl());
            }
        }
        //TODO
//		map.put("list", DistrictUtil.getDistrictVoList(lat, lon, list));
        return map;
    }

    // 发布活动
    @Transactional(rollbackFor = Exception.class)
    public PublishStatus send(SendMeetingDto sendMeetingDto) throws Exception {
        boolean success = false;

        /* 判断是否需要更改定时任务 **/
        if (StringUtils.isNotBlank(sendMeetingDto.getId())) {
            Meeting meeting = worthServiceprovider.getMeetingService().selectById(sendMeetingDto.getId());
            if (meeting == null) {
                throw new RuntimeException("未找到该活动信息");
            }
            if (meeting.getRegStopAt().getTime() != sendMeetingDto.getRegStopAt() || meeting.getStartedAt().getTime() != sendMeetingDto.getStartedAt() || meeting.getEndAt().getTime() != sendMeetingDto.getEndAt()) {
                success = jobFuseAction.removeJob(JobEnum.MEETING_CHECK_SELECTED_CONDITIONS_JOB, meeting.getId(), null, meeting.getId());
                if (!success) {
                    logger.error("清除定时任务失败");
                } else {
                    /* 定时任务：检查活动报名人数是否足够 */
                    success = jobFuseAction.addJob(JobEnum.MEETING_CHECK_SELECTED_CONDITIONS_JOB, meeting.getId(), meeting.getId(), meeting.getTitle() + ":检查确定入选人条件", new Date(meeting.getRegStopAt().getTime() - 300000), AuthorEmailEnum.ZHENG_JUE);
                    if (!success) {
                        logger.error("创建检查活动报名人数的定时任务失败错误参数1");
                        throw new RuntimeException("你的报名截止时间必须提前于当前时间30分钟的间距");
                    }
                }
            }
        }

        /* 检测是否有相同活动 **/
        Date startedAt = (new Date(sendMeetingDto.getStartedAt()));
        Date endAt = (new Date(sendMeetingDto.getEndAt()));
        Integer count = worthServiceprovider.getMeetingService().getSameMeeting(sendMeetingDto.getUserId(), startedAt, endAt, sendMeetingDto.getId());
        if (count != null && count > 0) {
            return PublishStatus.SAME;
        }

        /* 如果为编辑活动 则检查是否需要更改订单Id*/
        if (StringUtils.isNotBlank(sendMeetingDto.getOrderIds()) && StringUtils.isNotBlank(sendMeetingDto.getId())) {
            Meeting meeting = worthServiceprovider.getMeetingService().selectById(sendMeetingDto.getId());
            if (!sendMeetingDto.getOrderIds().equals(meeting.getOrderIds())) {/* 如果是新订单则更换*/
                success = merchantOrderInfoAction.updateTypeId(sendMeetingDto.getOrderIds().split(","), meeting.getId(), OrderTypeEnum.ACTIVITY_ORDER);
                if (!success) {
                    throw new RuntimeException("订单绑定活动出错！");
                }
            }
        } else { /* 如果编辑后无订单 则判断是否需要删除以前的订单 **/
            if (StringUtils.isNotBlank(sendMeetingDto.getId())) {
                Meeting meeting = worthServiceprovider.getMeetingService().selectById(sendMeetingDto.getId());
                if (StringUtils.isNotBlank(meeting.getOrderIds())) {/* 如果之前有订单 **/
                    merchantOrderInfoAction.delete(meeting.getId(), OrderTypeEnum.ACTIVITY_ORDER);
                    meeting.setOrderIds(sendMeetingDto.getOrderIds());
                    success = worthServiceprovider.getMeetingService().updateMeeting(meeting);
                    if (!success) {
                        throw new RuntimeException("删除活动消费订单异常");
                    }
                }
            }
        }

        Meeting meeting = new Meeting();
        /* 将活动信息存入数据库 **/
        success = worthServiceprovider.getMeetingService().create(sendMeetingDto, meeting);
        if (!success) {
            if (StringUtils.isNotBlank(sendMeetingDto.getId())) {
                throw new RuntimeException("更新活动失败");
            }
            throw new RuntimeException("创建活动信息失败");
        }

        /* 如果是发布活动 则绑定*/
        if (StringUtils.isBlank(sendMeetingDto.getId())) {
            success = merchantOrderInfoAction.updateTypeId(sendMeetingDto.getOrderIds().split(","), meeting.getId(), OrderTypeEnum.ACTIVITY_ORDER);
            if (!success) {
                throw new RuntimeException("订单绑定活动出错！");
            }
        }

        /* 如果不是更新活动 则创建发起人 **/
        if (!StringUtils.isNotBlank(sendMeetingDto.getId())) {
            success = meetingSendService.createSender(sendMeetingDto.getUserId(), meeting.getId());
            if (!success) {
                logger.error("创建发起人失败");
                throw new RuntimeException("创建发起人失败");
            }
        }

        /* 添加联合发起人 **/
        if (StringUtils.isNotBlank(sendMeetingDto.getSendIds())) {
            if (StringUtils.isNotBlank(sendMeetingDto.getId())) { /* 判断是否为编辑活动 **/
                String[] sendIds = sendMeetingDto.getSendIds().split(",");
                List<String> old = worthServiceprovider.getMeetingSendService().getMeetingSendId(meeting.getId()); /* 获取原来的联合发起人 **/
                if (old.size() > 0) { /* 判断以前是否存在联合发起人 **/
                    for (String checkId : sendIds) { /* 循环获取编辑时添加的联合发起人 **/
                        if (StringUtils.isNotBlank(checkId)) {
                            for (String checkOld : old) { /* 循环匹配旧数据 **/
                                if (checkId.equals(checkOld)) { /* 判断是否为发起人 **/
                                    if (old.size() != 1) {/* 判断是否未最后一个 */
                                        old.remove(checkOld); /* 存在则删除避免再次判断 **/
                                        break;
                                    }
                                } else {
                                    if (checkOld.equals(old.get(old.size() - 1))) { /* 判断是否为最后一个元素 如果是，则添加 **/
                                        success = meetingSendService.createUnionSender(checkId, meeting.getId());
                                        if (success) {
                                            wzCommonImHistoryAction.add(sendMeetingDto.getUserId(), checkId, userAction.queryUser(meeting.getUserId()).getNickname()
                                                    + "邀请你联合发起" + meeting.getTitle() + "活动，请你确认", meeting.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_INVOCATION, null);
                                        } else {
                                            throw new RuntimeException("添加" + userAction.queryUser(checkId).getNickname() + "为联合发起人时失败");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else { /* 如果是第一次添加 **/
                    addSendIds(sendMeetingDto.getSendIds(), meeting.getId(), meeting.getUserId());
                }
            } else {
                addSendIds(sendMeetingDto.getSendIds(), meeting.getId(), meeting.getUserId());
            }
        }

        /* 是否需要通知被邀请者 **/
        if (StringUtils.isNotBlank(sendMeetingDto.getObjList())) {
            if (StringUtils.isNotBlank(sendMeetingDto.getId()) && StringUtils.isNotBlank(meeting.getObjList())) { /* 如果为编辑活动且二次添加邀请 **/
                if (!sendMeetingDto.getObjList().equals(meeting.getObjList())) { /* 如果前后邀请对象有更变则进入推送 **/
                    String[] newRegister = sendMeetingDto.getObjList().split(",");
                    String[] oldRegister = meeting.getObjList().split(",");
                    for (String fresh : newRegister) { /* 遍历获取最新的被邀请人员*/
                        if (StringUtils.isNotBlank(fresh)) {
                            for (String old : oldRegister) {
                                if (StringUtils.isNotBlank(old)) {
                                    if (fresh.equals(old)) {
                                        break;
                                    } else {
                                        if (old.equals(oldRegister[oldRegister.length - 1])) {
                                            wzCommonImHistoryAction.add(sendMeetingDto.getUserId(), fresh, userAction.queryUser(meeting.getUserId()).getNickname()
                                                    + "邀请你参加" + meeting.getTitle() + "活动，请你确认", meeting.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_INVOCATION, null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else { /* 如果为发布活动或者第一次邀请，则立即通知被邀请者 **/
                String[] invited = sendMeetingDto.getObjList().split(",");
                for (String Invited : invited) {
                    if (StringUtils.isNotBlank(Invited)) {
                        wzCommonImHistoryAction.add(sendMeetingDto.getUserId(), Invited, userAction.queryUser(meeting.getUserId()).getNickname()
                                + "邀请你参加" + meeting.getTitle() + "活动，请你确认", meeting.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_INVOCATION, null);
                    }
                }
            }
        }

        /* 发布活动时生成定时任务 **/
        if (!StringUtils.isNotBlank(sendMeetingDto.getId())) {
            /* 定时任务：检查活动报名人数是否足够 */
            success = jobFuseAction.addJob(JobEnum.MEETING_CHECK_SELECTED_CONDITIONS_JOB, meeting.getId(), meeting.getId(),
                    meeting.getTitle() + ":检查是否满足确定入选人条件", new Date(meeting.getRegStopAt().getTime() - 300000), AuthorEmailEnum.ZHENG_JUE);
            if (!success) {
                logger.error("创建检查确定入选人条件定时任务失败错误参数1");
                throw new RuntimeException("唉哟，失败了(；′⌒`)");
            }
        }

        /* 返回活动Id **/
        if (success) {
            sendMeetingDto.setId(meeting.getId());
            return PublishStatus.SUCCESS;
        } else {
            return PublishStatus.FAIL;
        }
    }

    /* 活动主要发布者取消活动 **/
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelSend(String id, String userId) {
        boolean success = false;
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(id);
        if (meeting == null) {
            logger.error(id + "此活动已不存在");
            throw new RuntimeException("获取活动数据异常");
        }
        if (userId.equals(meeting.getUserId())) {
            success = true;
            /* 如果发起者同意开始活动之后取消活动则进入扣分程序 **/
            if (meeting.getStatus().equals(MeetingStatus.CODE_GENERATOR.status)) {
                meeting.setStatusDescription("发起者活动开始后取消活动");
                success = meetingReduceCredit(id, meeting.getTitle(), userId, "开始活动后取消活动", true, 5);
            }
            if (success && meeting.getAmount() > 0) {
                /* 如果超出开始活动时间则仅退款给已出席的入选者 **/
                if (meeting.getStartedAt().getTime() < new Date().getTime()) {
                    List<String> attend = worthServiceprovider.getMeetingRegisterService().getStartListById(id).stream().map(MeetingRegister::getUserId).collect(Collectors.toList());
                    success = meetingRefundMoney(id, attend, "活动发起者取消活动，退还已出席者报名费");
                    meeting.setStatusDescription("发起者活动开始后取消活动");
                } else {
                    List<String> register = worthServiceprovider.getMeetingRegisterService().registerStatusList(id, 1, 2, 3, 5).stream().map(MeetingRegister::getUserId).collect(Collectors.toList());
                    success = meetingRefundMoney(id, register, "发起人取消活动退还所有报名费");
                    meeting.setStatusDescription("发起者活动开始前取消活动");
                }
            }
            if (success) {
                meeting.setStatus(MeetingStatus.CANCEL.status);
                success = worthServiceprovider.getMeetingService().updateById(meeting);
                if (success) {
                    if (!jobFuseAction.BatchRemoveJob(meeting.getId())) {
                        logger.error(meeting.getId() + ":活动的定时任务删除失败");
                    }
                } else {
                    logger.error(id + "活动数据更新失败");
                }
            }
            return success;
        } else {
            throw new RuntimeException("你不是活动主要发起人，不能取消活动");
        }
    }

    /* 报名者退出活动 **/
    @Transactional(rollbackFor = Exception.class)
    public boolean exitMeeting(String meetingId, String userId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting == null) {
            logger.error(meetingId + "该活动不存在");
            throw new RuntimeException("该活动不存在");
        }
        boolean success = meetingAction.cancelReg(meetingId, userId);
        if (success) {
            MeetingRegister meetingRegister = worthServiceprovider.getMeetingRegisterService().selectByMeetingIdAndUserId(meetingId, userId);
            if (meetingRegister.getStatus().equals(MeetingRegisterStatus.SUCCESS.status)) {
                this.meetingReduceCredit(meetingId, meeting.getTitle(), meetingRegister.getUserId(), "选入之后退出活动，扣减2分信用值", false, 2);
            }
            if (meeting.getAmount() > 0 && meeting.getStatus().equals(MeetingStatus.WAITING.status)) {
                try {
                    if (meetingRegister.getFee() != null && meetingRegister.getFee() > 0) {
                        FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto();
                        frozenOperationRequestDto.setTypeId(meetingId);
                        frozenOperationRequestDto.setUserId(meetingRegister.getUserId());
                        frozenOperationRequestDto.setType(FrozenTypeEnum.FTZ_MEETING);
                        success = walletForzenClientAction.repealFrozen(frozenOperationRequestDto);
                        if (!success) {
                            logger.error(meetingId + "退还报名费失败");
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage() + meetingId + userId + "退出活动退款失败", e);
                }
            }
            success = worthServiceprovider.getMeetingRegisterService().cancelReg(meetingId, userId);
        }
        return success;
    }

    /* 添加联合发起人 **/
    private void addSendIds(String sendIds, String meetingId, String createUserId) throws Exception {
        boolean success = meetingSendService.createUnionSender(sendIds, meetingId);
        if (success) {
            User user = userAction.queryUser(createUserId);
            /* 邀请联合发起人时，需向被邀请者发送信息 **/
            String[] userIds = sendIds.split(",");
            if (userIds.length > 0) {
                for (String userId : userIds) {
                    if (StringUtils.isNotBlank(userId)) {
                        wzCommonImHistoryAction.add(userId, userId, user.getNickname()
                                + "邀请你联合发起" + "活动，请你确认", meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_INVOCATION, null);
                    }
                }
            }
        } else {
            throw new RuntimeException("呜呜呜~创建联合发起人失败了！再来一次");
        }
    }

    // 联合发起
    public Map<String, Object> unionSendList(String userId, Double lat, Double lon, Page<MeetingSend> page) {
        return getUserSendMeeting(userId, lat, lon, page);
    }

    // 编辑活动
    public PublishStatus edit(SendMeetingDto sendMeetingDto, UserGeo userGeo) throws Exception {
        if (!StringUtils.isNotBlank(sendMeetingDto.getId())) {
            throw new RuntimeException("Id为空，请选择要编辑的活动");
        }
        int registerCount = worthServiceprovider.getMeetingRegisterService().getNotSendCount(sendMeetingDto.getId(), null);
        if (registerCount > 0) {
            throw new RuntimeException("已有人报名参与活动，不可编辑");
        }
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(sendMeetingDto.getId());
        if (meeting == null) {
            throw new RuntimeException("未找到该活动");
        }
        sendMeetingDto.setId(meeting.getId());
        sendMeetingDto.setUserId(userGeo.getUserId());
        return send(sendMeetingDto);
    }

    // 活动报名
    @Transactional(rollbackFor = Exception.class)
    public boolean register(RegMeetingDto regMeetingDto, String id) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(regMeetingDto.getMeetingId());
        /** 判断用户报名的活动状态是为可报名状态 **/
        if (!meeting.getStatus().equals(MeetingStatus.WAITING.status)) {
            throw new RuntimeException("当前活动状态下不能报名！");
        }
        if (System.currentTimeMillis() > meeting.getRegStopAt().getTime()) {
            throw new RuntimeException("已错过活动报名时间");
        }
//		if (meeting.getAmount() <= 0 && meeting.getRegCount() >= meeting.getCeil()) {// 如果是免费活动并且报名人数达到入选人数上限了，就返回
//			throw new RuntimeException("报名人数已达上限");
//		}
        if (meetingSendService.getSendByUserIdAndMeetingId(meeting.getId(), id) == null) {//如果不是发起人，则限制报名对象！
            if (meeting.getObj() != 1) {
                UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg(id);
                switch (meeting.getObj()) {
                    case 2:
                        if (!userInfoAndHeadImg.getSex().equals("女")) {
                            throw new RuntimeException("该活动仅限于女性报名");
                        }
                        break;
                    case 3:
                        if (!userInfoAndHeadImg.getSex().equals("男")) {
                            throw new RuntimeException("该活动仅限于男性报名");
                        }
                        break;
                    case 4:
                        if (!friendsAction.isFriend(meeting.getUserId(), id)) {
                            throw new RuntimeException("你不是该活动发布者的好友，不能报名");
                        }
                        break;
                    case 5:
                        int exist = worthServiceprovider.getMeetingService().getExist(meeting.getId(), id);
                        if (exist <= 0) {
                            throw new RuntimeException("您不是该活动指定的报名对象");
                        }
                        break;
                }
            }
        }
        Long fee = new Money(regMeetingDto.getFee()).getCent();
        if (!(meeting.getAmount() <= 0) && meeting.getAmount() * regMeetingDto.getAmount() - fee != 0) {
            throw new RuntimeException("费用支付不正确");
        }
        meeting.setAllRegisterAmount(meeting.getAllRegisterAmount() + fee);
        String friends = regMeetingDto.getFriends();
        //查询是否重复报名
        MeetingRegister meetingRegister = worthServiceprovider.getMeetingRegisterService().getRegisterStatus(id, meeting.getId());
        if (meetingRegister != null && meetingRegister.getStatus() != 4) {
            meetingRegister.setAmount(meetingRegister.getAmount() + regMeetingDto.getAmount());
            Money money1 = new Money(regMeetingDto.getFee());
            meetingRegister.setFee(money1.getCent() + meetingRegister.getFee());
            meetingRegister.setUpdateTime(new Date());
            getMeetingRegisterService().updateByUserIDAndMeetingId(meetingRegister.getMeetingId(), meetingRegister.getUserId(), meetingRegister);
        } else {
            // 添加活动报名者
            getMeetingRegisterService().register(regMeetingDto, id);
        }
        User user = userAction.getUserService().selectById(id);
        meeting.setRegCount(meeting.getRegCount() + regMeetingDto.getAmount());
        if (StringUtils.isNotBlank(friends)) {
            String[] friend = friends.split(",");
            for (String userId : friend) {
                wzCommonImHistoryAction.add(id, userId, "你的好友" + user.getNickname() + "邀请你一起报名参加" + meeting.getTitle() + "活动,活动费用只需要" + Money.CentToYuan(meeting.getAmount()).getAmount() + "哦！快来一起报名参加吧",
                        meeting.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
            }
        }
        return worthServiceprovider.getMeetingService().updateById(meeting);
    }

    // 获取活动详情信息
    public Map<String, Object> selectById(String meetingId, Double lon, Double lat, String userId) throws Exception {
        Map map = new HashMap();
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        MeetingDetailResponseDto meetingDetailResponseDto = new MeetingDetailResponseDto();
        if (meeting != null) {
            /* 获取活动点击记录 */
            if (!StringUtils.isEmpty(userId) && !userId.equals(meeting.getUserId())) {
                worthClickHistoryAction.create(WorthTypeEnum.MEETING_TYPE, userId, meeting.getId());
            }
            int clickCount = worthClickHistoryAction.getClickCount(WorthTypeEnum.MEETING_TYPE, meeting.getId());
            VoPoConverter.copyProperties(meeting, meetingDetailResponseDto);
            /* 计算与活动地址距离 **/
            meetingDetailResponseDto.setAmount(meeting.getAmount());
            meetingDetailResponseDto.setDistance(DistrictUtil.calcDistance(meeting.getLat().doubleValue(), meeting.getLon().doubleValue(), lat, lon));
            meetingDetailResponseDto.setClickQuantity(clickCount);
            meetingDetailResponseDto.setOrderPrice(meeting.getOrderPrice());
            meetingDetailResponseDto.setMerchantId(meeting.getPosterImagesUrl());
            meetingDetailResponseDto.setAllRegisterAmount(Money.CentToYuan(meeting.getAllRegisterAmount()).getAmount());
        } else {
            throw new RuntimeException("未搜索到此活动信息");
        }

        /* 获取图片信息 **/
        if (StringUtils.isNotBlank(meetingDetailResponseDto.getMeetingImagesUrl())) {/* 拼凑活动图片 **/
            meetingDetailResponseDto.setMeetingImagesUrl(addImgUrlPreUtil.addImgUrlPres(meetingDetailResponseDto.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket));
        }

        /* 查询报名信息 **/
        MeetingRegister Registers = worthServiceprovider.getMeetingRegisterService().getRegisterStatus(userId, meetingId);
        if (Registers == null || Registers.getStatus() == 4) {//判断是否为活动参与者
            meetingDetailResponseDto.setRegisterStatus(0);          //如果不是参与者则设置他的状态为0
            meetingDetailResponseDto.setDefault(false);
        } else {  //否者 则获取对应的报名信息
            MeetingRegister RegisterStatus = worthServiceprovider.getMeetingRegisterService().getRegisterStatus(userId, meeting.getId());
            meetingDetailResponseDto.setAnonymity(RegisterStatus.getAnonymity());/* 是否匿名 **/
            meetingDetailResponseDto.setRegisterStatus(RegisterStatus.getStatus());/* 报名状态 **/
            meetingDetailResponseDto.setIsValidation(RegisterStatus.getValidationStatus());/* 报名者验证状态 **/
            meetingDetailResponseDto.setDefault(false);
        }

        /* 查询活动发布者的信息 **/
        MeetingSend meetingSend = worthServiceprovider.getMeetingSendService().getSendByUserIdAndMeetingId(meeting.getId(), userId);
        if (meetingSend != null) {
            UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg(meeting.getUserId());
            meetingDetailResponseDto.setSendValidation(meetingSend.getValidationStatus());
            meetingDetailResponseDto.setUserNumber(userInfoAndHeadImg.getUserNumber());
            meetingDetailResponseDto.setDefault(meetingSend.getDefault());
        }
        boolean payFlag = false;
        /* 判断是否存在消费订单 **/
        if (StringUtils.isNotBlank(meeting.getOrderIds())) {
            String[] orderIds = meeting.getOrderIds().split(",");
            for (String orderId : orderIds) {
                if (StringUtils.isNotBlank(orderId)) {
                    MerchantOrderInfo merchantOrderInfo = merchantOrderInfoAction.getMerchantOrderInfoService().selectById(orderId);
                    if (merchantOrderInfo != null && merchantOrderInfo.getPayStatus().equals(PayStatus.PS_PAYED.name())) {
                        payFlag = true;
                        break;
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(meeting.getMeetingFeePayFrom())) {
            payFlag = true;
        } else {
            payFlag = false;
        }
        meetingDetailResponseDto.setIsPay(payFlag);

        /* 获取的指定的报名对象信息 **/
        List<MeetingRegisterListDto> allowList = new ArrayList<>();
        if (StringUtils.isNotBlank(meeting.getObjList())) {
            String[] ObjId = meeting.getObjList().split(",");
            for (String Id : ObjId) {
                UserSynopsisData userSynopsisData = userAction.getUserSynopsisData(Id, meeting.getUserId());
                MeetingRegisterListDto meetingRegisterListDto = new MeetingRegisterListDto();
                if (userSynopsisData != null) {
                    VoPoConverter.copyProperties(userSynopsisData, meetingRegisterListDto);
                }
                allowList.add(meetingRegisterListDto);
            }
        }
        map.put("allowList", allowList);

        /* 获取报名成功的对象信息 */
        List<MeetingRegister> meetingRegisters = worthServiceprovider.getMeetingRegisterService().getRegisterSuccessUserId(meetingId);
        if (meetingRegisters != null && meetingRegisters.size() > 0) {
            List<String> userIdList = meetingRegisters.stream().map(MeetingRegister::getUserId).collect(Collectors.toList());
            List<Object> list = new ArrayList<>();
            if (userIdList != null && userIdList.size() > 0) {
                Map<String, Object> userMap = userAction.getUsersAndHeadImg(userIdList);
                meetingRegisters.forEach(meetingRegister -> {
                    if (meetingRegister.getStatus() != 4) {
                        list.add(createRegMeetingResponseDto(userMap.get(meetingRegister.getUserId()), meetingRegister));
                    }
                });
            }
            map.put("reg_success_user", list);
        }

        /* 获取制定的联合发起人 **/
        List<String> sendId = worthServiceprovider.getMeetingSendService().getMeetingSendId(meeting.getId());
        if (sendId.size() > 0) {
            meetingDetailResponseDto.setSendList(sendId);
        }
        /* 获取活动评论 **/
        CommonEvaluate commonEvaluate = new CommonEvaluate();
        commonEvaluate.setTypeId(meetingId);
        commonEvaluate.setFromUserId(userId);
        commonEvaluate.setEvaluateType(EvaluateTypeEnum.MEETING_EVALUATE.getValue());
        meetingDetailResponseDto.setComments(evaluateAction.getCount(commonEvaluate) != 0);
        map.put("detail", meetingDetailResponseDto);
        return map;
    }

    // 向活动详情列表插入数据
    private RegMeetingResponseDto createRegMeetingResponseDto(Object o, MeetingRegister meetingRegister) {
        RegMeetingResponseDto regMeetingResponseDto = new RegMeetingResponseDto();
        if (o != null) {
            VoPoConverter.copyProperties(o, regMeetingResponseDto);
        }
        if (meetingRegister != null) {
            regMeetingResponseDto.setCreateTime(meetingRegister.getCreateTime());
            regMeetingResponseDto.setStatus(meetingRegister.getStatus());
            regMeetingResponseDto.setAnonymity(meetingRegister.getAnonymity());
            regMeetingResponseDto.setFee(Money.CentToYuan(meetingRegister.getFee()).getAmount());
        }
        return regMeetingResponseDto;
    }

    /* 我发起的活动 **/
    public Map<String, Object> getUserSendMeeting(String userId, Double lat, Double lon, Page page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        List<MeetingDetailSendDto> result = new ArrayList<>();
        List<MeetingDetailSendDto> resultOne = new ArrayList<>();
        List<MeetingDetailSendDto> resultTwo = new ArrayList<>();
        List<MeetingDetailSendDto> stutas_2_3_6 = new ArrayList<>();
        List<String> meetingSendList = worthServiceprovider.getMeetingSendService().selectByStatusAndUserId(userId);
        if (meetingSendList.size() > 0) {
            List<Meeting> meetingLists = worthServiceprovider.getMeetingService().getMeetingPageByIds(meetingSendList, page);
            if (meetingLists.size() > 0) {
                List<String> userIds = new ArrayList<>();
                meetingLists.forEach(meeting -> {
                    if (!StringUtils.isEmpty(meeting.getUserId())) {
                        userIds.add(meeting.getUserId());
                    }
                });
                if (userIds.size() > 0) {
                    userMap = userAction.getUsersAndHeadImg(userIds); /* 存放多条用户信息 **/
                }
                for (Meeting meeting : meetingLists) {
                    resultOne.add(createMeetingDetailSendDto(meeting, userMap, lon, lat));
                }
            }
        }
        List<Meeting> meetingList = worthServiceprovider.getMeetingService().getSelfSendMeetingCreateTimeDesc(userId, page);
        if (meetingList.size() > 0) {
            Map<String, Meeting> meetingMap = new HashMap<>();
            List<String> userIds = new ArrayList<>();

            meetingList.forEach(meeting -> {
                if (!StringUtils.isEmpty(meeting.getUserId())) {
                    userIds.add(meeting.getUserId());
                }
                meetingMap.put(meeting.getId(), meeting); /* 存放多条用户活动信息 **/
            });
            if (userIds.size() > 0) {
                userMap = userAction.getUsersAndHeadImg(userIds); /* 存放多条用户信息 **/
            }
            for (Meeting meeting : meetingList) {
                resultTwo.add(createMeetingDetailSendDto(meeting, userMap, lon, lat));
            }
        }
        if (resultTwo.size() > resultOne.size()) {
            resultTwo.removeAll(resultOne);
            resultTwo.addAll(resultOne);
            result.addAll(resultTwo);
        } else {
            resultOne.remove(result);
            resultOne.addAll(result);
            result.addAll(resultOne);
        }
        //按照status，跟createTime排序
        Collections.sort(result, new Comparator<MeetingDetailSendDto>() {
            @Override
            public int compare(MeetingDetailSendDto o1, MeetingDetailSendDto o2) {
                return o2.getCreateTime().compareTo(o1.getCreateTime());
            }
        });
        map.put("list", result);
        return map;
    }

    // 向发布的活动列表填充字段
    private MeetingDetailSendDto createMeetingDetailSendDto(Meeting meeting, Map<String, Object> userMap, Double lon, Double lat) {
        MeetingDetailSendDto meetingDetailSendDto = new MeetingDetailSendDto();
        if (meeting != null) {
            Object user = userMap.get(meeting.getUserId());
            if (user != null) {
                VoPoConverter.copyProperties(user, meetingDetailSendDto);
                VoPoConverter.copyProperties(meeting, meetingDetailSendDto);
            }
            meetingDetailSendDto.setAmount(meeting.getAmount());
            meetingDetailSendDto.setMeetingId(meeting.getId());
            meetingDetailSendDto.setDistance(DistrictUtil.calcDistance(meeting.getLat().doubleValue(), meeting.getLon().doubleValue(), lat, lon));
            meetingDetailSendDto.setMeetingImagesUrl((addImgUrlPreUtil.addImgUrlPres(meeting.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket)));
            meetingDetailSendDto.setMerchantId(meeting.getPosterImagesUrl());
        }
        return meetingDetailSendDto;
    }


    // 获取用户报名的活动信息
    public Map<String, Object> getUserRegMeetings(String userId, Double lat, Double lon, Page<MeetingRegister> page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        List<RegMeetingListDto> result = new ArrayList<>();
        List<RegMeetingListDto> stutas_2_3_6 = new ArrayList<>();
        //根据userId查询 MeetingRegister表，目的：获取 meeting_id,create_time,status 三个字段
        List<MeetingRegister> regmeetingList = worthServiceprovider.getMeetingRegisterService().getRegListTwo(userId, page);
        if (regmeetingList != null && regmeetingList.size() > 0) {
            //根据查询到的meeting_Id 查询 Meeting表 目的：获取 title，user_Id，status 三个字段
            List<Meeting> meetingList = worthServiceprovider.getMeetingService().selectBatchIds(regmeetingList.stream().map(MeetingRegister::getMeetingId).collect(Collectors.toList()));
            Map<String, Meeting> meetingMap = new HashMap<>();
            List<String> userIds = new ArrayList<>();
            meetingList.forEach(meeting -> {
                if (!StringUtils.isEmpty(meeting.getUserId())) {
                    userIds.add(meeting.getUserId());
                }
                meetingMap.put(meeting.getId(), meeting); /** 存放多条用户活动信息 **/
            });
            if (userIds.size() > 0) {
                userMap = userAction.getUsersAndHeadImg(userIds); /** 存放多条用户信息 **/
            }
            for (MeetingRegister register : regmeetingList) {
                result.add(createRegMeetingList(register, meetingMap.get(register.getMeetingId()), userMap, lat, lon));
            }
            //按照status，跟createTime排序
            Collections.sort(result, new Comparator<RegMeetingListDto>() {
                @Override
                public int compare(RegMeetingListDto o1, RegMeetingListDto o2) {
                    return o2.getCreateTime().compareTo(o1.getCreateTime());
                }
            });
            map.put("list", result);
        }
        return map;

    }


    // 拼凑已报名的活动列表
    private RegMeetingListDto createRegMeetingList(MeetingRegister register, Meeting meeting, Map<String, Object> userMap, Double lat, Double lon) {
        RegMeetingListDto regMeetingListDto = new RegMeetingListDto();
        if (meeting != null) {
            Object user = userMap.get(meeting.getUserId());
            if (user != null) {
                VoPoConverter.copyProperties(user, regMeetingListDto);
            }
            regMeetingListDto.setStatusDescription(meeting.getStatusDescription());
            regMeetingListDto.setMeetingImagesUrl((addImgUrlPreUtil.addImgUrlPres(meeting.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket)));
            regMeetingListDto.setTitle(meeting.getTitle());
            regMeetingListDto.setMeetingType(meeting.getMeetingType());
            if (meeting.getAmount() != null) {
                regMeetingListDto.setFee(Money.CentToYuan(meeting.getAmount()).getAmount());
            }
            regMeetingListDto.setRegCount(meeting.getRegCount());
            regMeetingListDto.setDistance(DistrictUtil.calcDistance(meeting.getLat().doubleValue(), meeting.getLon().doubleValue(), lat, lon));
        }
        regMeetingListDto.setStatus(meeting.getStatus());
        regMeetingListDto.setMeetingId(register.getMeetingId());
        regMeetingListDto.setCreateTime(register.getCreateTime());
        return regMeetingListDto;
    }

    // 查看活动人员
    public Map<String, Object> meetingPerson(String meetingId, Page page, Double lon, Double lat, String userId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting == null) {
            throw new RuntimeException("不存在此活动");
        }
        Map<String, Object> map = new HashMap<>();
        /* 获取活动所有报名人员 */
        List<MeetingRegister> meetingRegisterList = worthServiceprovider.getMeetingRegisterService().getRegisterSuccessUserId(meetingId, page);
        /* 获取所有活动发起人的信息（包括联合发起人） */
        List<MeetingSend> meetingSendsList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
        /* 存储获取的用户信息 **/
        Map<String, MeetingRegister> registerMap = new HashMap<>();/* 按用户id存入活动报名信息 **/
        List<String> meetingRegisterUserId = new ArrayList<>(); /* 存储参与活动者Id **/
        List<String> sendUserId = new ArrayList<>(); /* 存储活动发起者Id **/
        List<String> allUserIdList = new ArrayList<>();
        List<MeetingRegisterListDto> passList = new ArrayList<>();
        List<MeetingRegisterListDto> regList = new ArrayList<>();
        List<MeetingRegisterListDto> sendList = new ArrayList<>();
        if (meetingRegisterList.size() > 0) {
            meetingRegisterList.forEach(registerUser -> {
                        if (registerUser != null) {
                            meetingRegisterUserId.add(registerUser.getUserId());  /* 提取活动申请者Id **/
                            allUserIdList.add(registerUser.getUserId());
                        }
                        registerMap.put(registerUser.getUserId(), registerUser); /* 按用户Id存入参与活动的申请信息 **/
                    }
            );
        }
        meetingSendsList.forEach(meetingSend -> {
//            if(meetingSend.getStatus()==1){
            sendUserId.add(meetingSend.getUserId()); /* 提取活动发起者Id **/
            allUserIdList.add(meetingSend.getUserId());
//            }
        });
        Map<String, UserSynopsisData> userSynopsisDataMap = userAction.getUserSynopsisDataMap(sendUserId, lon, lat, userId);
        Map<String, UserSynopsisData> registerUser = new HashMap<>();
        if (meetingRegisterUserId.size() > 0) {
            registerUser = userAction.getUserSynopsisDataMap(meetingRegisterUserId, lon, lat, userId);
        }
        if (meetingRegisterList.size() > 0) {
            for (MeetingRegister meetingRegisterId : meetingRegisterList) {
                if (meetingRegisterId.getStatus() == 2 || meetingRegisterId.getStatus() == 5) {
                    passList.add(createMeetingRegisterListDto(meetingRegisterId, registerUser.get(meetingRegisterId.getUserId())));
                } else {
                    regList.add(createMeetingRegisterListDto(meetingRegisterId, registerUser.get(meetingRegisterId.getUserId())));
                }
            }
        }
        for (MeetingSend sendUser : meetingSendsList) {
//            if(sendUser.getStatus()==1)
            sendList.add(createMeetingSend(userSynopsisDataMap.get(sendUser.getUserId()), sendUser));
        }
        map.put("passList", passList);
        map.put("regList", regList);
        //联合发起人+状态和发起人
        map.put("sendList", sendList);
        return map;
    }

    // 向参加活动人员列表插入字段
    private MeetingRegisterListDto createMeetingRegisterListDto(MeetingRegister meetingRegister, UserSynopsisData synopsisData) {
        MeetingRegisterListDto meetingRegisterListDto = new MeetingRegisterListDto();
        if (synopsisData != null) {
            VoPoConverter.copyProperties(synopsisData, meetingRegisterListDto);
        }
        if (meetingRegister != null) {
            meetingRegisterListDto.setRegisterStatus(meetingRegister.getStatus());
            meetingRegisterListDto.setFee(Money.CentToYuan(meetingRegister.getFee()).getAmount());
            meetingRegisterListDto.setAnonymity(meetingRegister.getAnonymity());
        }
        return meetingRegisterListDto;
    }

    // 向活动发布人员列表插入字段
    private MeetingRegisterListDto createMeetingSend(UserSynopsisData userSynopsisData, MeetingSend meetingSend) {
        MeetingRegisterListDto meetingRegisterListDto = new MeetingRegisterListDto();
        if (userSynopsisData != null) {
            VoPoConverter.copyProperties(userSynopsisData, meetingRegisterListDto);
        }
        if (meetingSend != null) {
            meetingRegisterListDto.setSendStatus(meetingSend.getStatus());
            meetingRegisterListDto.setDefault(meetingSend.getDefault());
        }
        return meetingRegisterListDto;
    }

    // 按活动id查询活动发起人员的列表
    public Map<String, Object> showSendUserList(String meetingId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
        List<String> userId = sendList.stream().map(MeetingSend::getUserId).collect(Collectors.toList());
        List<MeetingSendUserInforDto> userInforDtos = new ArrayList<>();
        Map<String, UserSynopsisData> userSynopsisDataMap = userAction.getUserSynopsisDataMap(userId, null, null, null);
        sendList.forEach(sendId -> {
            if (sendId.getDefault())
                userInforDtos.add(sendUserInforDto(sendId.getUserId(), userSynopsisDataMap));
        });
        map.put("userInformation", userInforDtos);
        return map;
    }

    // 向校验人员列表植入数据
    private MeetingSendUserInforDto sendUserInforDto(String userId, Map<String, UserSynopsisData> map) {
        MeetingSendUserInforDto meetingSendUserInforDto = new MeetingSendUserInforDto();
        if (map != null) {
            Object userSend = map.get(userId);
            if (userSend != null) {
                VoPoConverter.copyProperties(userSend, meetingSendUserInforDto);
            }
        }
        return meetingSendUserInforDto;
    }

    //补充活动信息（地址、消费）
    public boolean modify(ModifyMeetingDto modifyMeetingDto) throws Exception {
        boolean success = worthServiceprovider.getMeetingService().modify(modifyMeetingDto); //更新活动细节
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(modifyMeetingDto.getId());
        // 有消费
        if (modifyMeetingDto.getOrderPrice() != null && modifyMeetingDto.getOrderPrice().compareTo(new BigDecimal(0)) == 1) {
            String manageUserId = orderClientAction.getManageUserIdByOrderId(modifyMeetingDto.getOrderIds());
            wzCommonImHistoryAction.add(modifyMeetingDto.getUserId(), manageUserId, meeting.getTitle() + "活动开始时间" + DateTimestampUtil.getDateStrByDate(meeting.getStartedAt()) + "，消费" + meeting.getOrderPrice() + "，人数"
                    + meeting.getRegSuccessCount() + "人，请准备服务", meeting.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
        } else {
            if (!success) {
                logger.error("创建无消费时自动结算的定时任务失败");
                throw new RuntimeException();
            }
        }
        List<MeetingRegister> list = meetingRegisterService.getStartListById(modifyMeetingDto.getId());
        List<String> userIds = list.stream().map(MeetingRegister::getUserId).collect(toList());
        userIds.add(modifyMeetingDto.getUserId());
        if (userIds != null && userIds.size() > 0) {
            for (String userId : userIds) {
                wzCommonImHistoryAction.add(modifyMeetingDto.getUserId(), userId, "活动开始时间" + DateTimestampUtil.getDateStrByDate(meeting.getStartedAt()) + "，地点" + meeting.getAddress() + "，人数"
                        + meeting.getRegSuccessCount() + "人，请准备出席", meeting.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
            }
        }
        return success;
    }

    // 检查验证码
    public boolean checkCode(String meetingId, String userId, String sendUserId, Integer code, Double lat, Double lon) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (code == null) {
            throw new RuntimeException("验证码不能为空");
        }
        if (sendUserId == null) {
            throw new RuntimeException("发起人Id不能为空");
        }
        boolean validationStatus = true;
        if (meeting.getMeetingType() == 3) { // 如果是纯上线活动，则无需判校验直接通过！
            return meetingRegisterAction.codeValidated(meetingId, 1);
        } else {
            Double distance = (DistrictUtil.calcDistance(meeting.getLat().doubleValue(), meeting.getLon().doubleValue(), lat, lon));
            validationStatus = (distance * 1000) <= 300;
        }
        if (validationStatus) {
            MeetingSend meetingSend = worthServiceprovider.getMeetingSendService().selectByMeetingIdAndUserId(meetingId, sendUserId);
            if (meetingSend == null) {
                throw new Exception("没有发起者信息");
            }
            if (!userId.equals(sendUserId)) {
                MeetingRegister meetingRegister = worthServiceprovider.getMeetingRegisterService().selectByMeetingIdAndUserId(meetingId, userId);
                if (meetingRegister != null) {
                    if (meetingRegister.getTimes() >= 3) {
                        throw new Exception("重试次数过多");
                    }
                    boolean success = worthServiceprovider.getMeetingRegisterService().updateVerificationCode(userId, meetingId, meetingRegister.getTimes() + 1);
                    if (!success) {
                        throw new Exception("更新验证码次数失败");
                    } else {
                        if (code - meetingSend.getCode() == 0) {
                            Integer status = 1;
                            return meetingRegisterAction.codeValidated(userId, meetingId, sendUserId, status);
                        } else {
                            throw new Exception("验证失败");
                        }
                    }
                } else {
                    throw new Exception("没有该活动发起信息");
                }
            } else {
                throw new Exception("不能自己验证自己");
            }
        } else {
            throw new Exception("您与活动地点的距离超出300米，未进入校验范围、不能进行校验");
        }
    }

    // 向商家发送提醒
    public boolean getRemindMerchants(String meetingId, String userId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (StringUtils.isNotBlank(meeting.getOrderIds())) {
            String[] orderIds = meeting.getOrderIds().split(",");
            for (String orderId : orderIds) {
                if (StringUtils.isNotBlank(orderId)) {
                    MerchantOrderInfo merchantOrderInfo = merchantOrderInfoAction.getMerchantOrderInfoService().selectById(orderId);
                    if (merchantOrderInfo == null) {
                        throw new Exception("商品订单不存在");
                    }
                    List<MerchantManager> merchantManagers = merchantManagerService.getMerchantType(merchantOrderInfo.getMerchantId());
                    if (merchantManagers != null && merchantManagers.size() > 0) {
                        for (MerchantManager merchantManager : merchantManagers) {
                            if (merchantManager.getMerchantUserType().equals("业务主管") || merchantManager.getMerchantUserType().equals("客服人员")) {
                                wzCommonImHistoryAction.add(userId, merchantManager.getUserId(), meeting.getTitle() + "活动开始时间" + DateTimestampUtil.getDateStrByDate(meeting.getStartedAt()) + "，消费" + meeting.getOrderPrice() + "，人数"
                                        + meeting.getRegSuccessCount() + "人，请准备服务", meeting.getOrderIds(), MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // 向好友发送邀请
    public boolean getInviteFriends(String meetingId, String userId, String friends) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        User user = userAction.getUserService().selectById(meeting.getUserId());
        if (StringUtils.isNotBlank(friends)) {
            String[] friend = friends.split(",");
            for (String friendId : friend) {
                wzCommonImHistoryAction.add(userId, friendId, "你的好友" + user.getNickname() + "邀请你一起报名参加" + meeting.getTitle() + "活动,活动费用只需要" + Money.CentToYuan(meeting.getAmount()).getAmount() + "哦！快来一起报名参加吧",
                        meeting.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
            }
        }
        return true;
    }

    //未确认活动细节则取消活动，退还用户所有报名费
    public void checkNoConfirmDetail(String meetingId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting != null) {
            if (meeting.getStatus() != 2 || meeting.getStatus() != 3) {
                /* 如果未确认活动细节*/
                if (!meeting.getConfirm()) {
                    //推送
                    sendMessageAfterFailActivity(meetingId);
                    /* 获取已入选名单 **/
                    List<MeetingRegister> meetingRegisterList = meetingRegisterService.getSuccessRegListById(meetingId);
                    /* 如果活动存在报名费用 **/
                    if (meeting.getAmount() > 0) {
                        /* 如果存在有效入选人则进入退款程序**/
                        if (meetingRegisterList != null && meetingRegisterList.size() > 0) {
                            this.meetingRefundMoney(meetingId, meetingRegisterList.stream().map(MeetingRegister::getUserId).collect(Collectors.toList()), "发起者未确认活动细节");
                        }

                    }
                    meeting.setStatusDescription("发起者超时未确认活动细节");
                    meeting.setStatus(MeetingStatus.FAIL.status);//设置活动状态
                    meeting.insertOrUpdate();//更新活动状态信息
                    meetingReduceCredit(meetingId, meeting.getTitle(), meeting.getUserId(), "超时未确认活动细节", true, 5);
                }
            } else {
                logger.error("活动被提前取消或失败");
            }
        } else {
            logger.error(meetingId + "此活动不存在");
        }
    }

    // 活动取消，退回活动费用
    private void backRegisterFee(String meetingId) {
        meetingRegisterService.getRegSuccessListByMeetingId(meetingId).forEach(meetingRegister -> {
            try {
                Long fee = meetingRegister.getFee();
                if (fee != null && fee > 0) {
                    FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto();
                    frozenOperationRequestDto.setTypeId(meetingId);
                    frozenOperationRequestDto.setUserId(meetingRegister.getUserId());
                    frozenOperationRequestDto.setType(FrozenTypeEnum.FTZ_MEETING);
                    walletForzenClientAction.repealFrozen(frozenOperationRequestDto);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    // 支付回掉函数
    @Transactional(rollbackFor = Exception.class)
    public boolean payCallback(CommonPayCallbackDto commonPayCallbackDto, String userId) {
        String meetingId = commonPayCallbackDto.getId();
        String payType = commonPayCallbackDto.getPayType(); /** 支付方式，暂且没有用到**/
        BigDecimal amount = commonPayCallbackDto.getAmount();

        // 获取冻结金额（报名总费用）
        BigDecimal frozenAmount = BigDecimal.ZERO; // 记录总冻结金额
        FrozenQueryRequestDto frozenQueryRequestDto = new FrozenQueryRequestDto();
        frozenQueryRequestDto.setTypeId(meetingId);
        frozenQueryRequestDto.setCurrent(1);
        frozenQueryRequestDto.setSize(999999999);
        List<WzCommonWalletFrozenResponseDto> walletFrozenList = walletFrozenProxy.queryList(frozenQueryRequestDto);
        if (walletFrozenList == null) {
            logger.error("获取冻结金额（报名费）记录失败");
            throw new RuntimeException("获取冻结金额（报名费）记录失败");
        }
        if (walletFrozenList.isEmpty()) {
            logger.error("获取冻结金额（报名费）记录失败");
            throw new RuntimeException("获取冻结金额（报名费）记录失败");
        }
        int walletFrozenListSize = walletFrozenList.size();
        for (int i = 0; i < walletFrozenListSize; i++) {
            frozenAmount = frozenAmount.add(walletFrozenList.get(i).getAmount());
        }

        // 先支付给平台
        FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto();
        frozenOperationRequestDto.setTypeId(meetingId);
        frozenOperationRequestDto.setType(FrozenTypeEnum.FTZ_MEETING);
        walletForzenClientAction.pay(frozenOperationRequestDto);
        //再支付给发起者
        paySend(amount, userId);

        // 获取平台的收取的手续费比例
        BigDecimal sharedFee = getSharedFee();
        // 扣取平台的手续费
        BigDecimal fee = frozenAmount.multiply(sharedFee); // 记录手续费
        BigDecimal totalAmount = frozenAmount.subtract(fee); // 记录交了手续费后的冻结费用

        // 计算费用，并做业务处理
        BigDecimal finalAmount = totalAmount.subtract(amount); // 交了手续费后的费用 - 给商家的费用，得到剩余的金额

        // 若剩余的金额多出，就平均分配给发起人
        if (finalAmount.compareTo(BigDecimal.ZERO) == 1) {
            List<MeetingSend> sendList = meetingSendService.getAcceptListByMeetingId(meetingId);
            sendList = sendList.stream().filter(e -> !e.getStatus().equals(MeetingSendStatus.ATTEND.status))
                    .collect(Collectors.toList());// 过滤掉未出席
            int sendListSize = sendList.size(); // 发起者人数
            BigDecimal money = finalAmount.divide(BigDecimal.valueOf(sendListSize)); // 每人可以得到的平均钱数
            BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
            billAddRequestDto.setAmount(money);
            billAddRequestDto.setDescription("活动报名费剩余的金额平均分给每个活动的发起人");
            billAddRequestDto.setPayChannel(3);
            billAddRequestDto.setType(1);
            for (MeetingSend meetingSend : sendList) {
                billAddRequestDto.setToUserId(meetingSend.getUserId());
                Boolean addBill = walletBillClientAction.addBill("999", billAddRequestDto);
                if (!addBill) {
                    throw new RuntimeException("平台支付给发起人剩余金额出现异常");
                }
            }
        }
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setMeetingFeePayType(payType);
        meeting.setMeetingFeePayFrom(userId);
        boolean success = worthServiceprovider.getMeetingService().updateMeeting(meeting);
        if (!success) {
            throw new RuntimeException("录入支付信息异常");
        }
        return true;
    }

    //获取平台收费比例
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal getSharedFee() {
        // 获取平台的收取的手续费比例
        CostSettingResponseDto costSettingResponseDto = costSettingClientAction.feignQuery();
        if (costSettingResponseDto == null) {
            logger.error("费用设置获取失败");
            throw new RuntimeException("费用设置获取失败");
        }
        BigDecimal sharedFee = costSettingResponseDto.getSharedFee(); // 记录手续费比例
        // 手续费比例大于或等于 1 或者 手续费比例小于 0，说明后台设置出现问题
        boolean flag_sharedFee = sharedFee.compareTo(BigDecimal.ONE) == 1 || sharedFee.compareTo(BigDecimal.ONE) == 0
                || sharedFee.compareTo(BigDecimal.ZERO) == -1;
        if (flag_sharedFee) {
            logger.error("后台设置网值的手续费比例不合法：" + sharedFee);
            throw new RuntimeException("后台设置网值的手续费比例不合法：" + sharedFee);
        }
        return sharedFee;
    }

    // 活动结束自动结算
    public boolean autoMeetingSettle(String meetingId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting.getAmount() != 0) {//如果不是免费活动则结算费用
            List<MeetingSend> sendList = getMeetingSendService().getSendListByMeetingId(meetingId);
            // 获取冻结金额（报名总费用）
            BigDecimal frozenAmount = BigDecimal.ZERO; // 记录总冻结金额
            FrozenQueryRequestDto frozenQueryRequestDto = new FrozenQueryRequestDto();
            frozenQueryRequestDto.setTypeId(meetingId);
            List<WzCommonWalletFrozenResponseDto> walletFrozenList = walletFrozenProxy.queryList(frozenQueryRequestDto);
            if (walletFrozenList == null || walletFrozenList.isEmpty()) {
                logger.error("获取冻结金额(报名费)记录失败");
                throw new RuntimeException("结算异常");
            }
            int walletFrozenListSize = walletFrozenList.size();
            for (int i = 0; i < walletFrozenListSize; i++) {
                frozenAmount = frozenAmount.add(walletFrozenList.get(i).getAmount());
            }
            // 先支付给平台
            FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto();
            frozenOperationRequestDto.setTypeId(meetingId);
            frozenOperationRequestDto.setType(FrozenTypeEnum.FTZ_MEETING);
            walletForzenClientAction.pay(frozenOperationRequestDto);
            // 获取手续费比例
            BigDecimal sharedFee = getSharedFee();
            // 扣取平台的手续费
            BigDecimal fee = frozenAmount.multiply(sharedFee); // 记录手续费
            BigDecimal totalAmount = frozenAmount.subtract(fee); // 记录交了手续费后的冻结费用
            // 剩余的就平均分配给发起人
            if (totalAmount.compareTo(BigDecimal.ZERO) == 1) {
                sendList = sendList.stream().filter(e -> !e.getValidationStatus().equals(0)).collect(Collectors.toList());// 过滤掉未出席
                int sendListSize = sendList.size(); // 发起者人数
                BigDecimal money = totalAmount.divide(BigDecimal.valueOf(sendListSize)); // 每人可以得到的平均钱数
                BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
                billAddRequestDto.setAmount(money);
                billAddRequestDto.setDescription("活动报名费剩余的金额平均分给每个活动的发起人");
                billAddRequestDto.setPayChannel(3);
                billAddRequestDto.setType(1);
                for (MeetingSend meetingSend : sendList) {
                    billAddRequestDto.setToUserId(meetingSend.getUserId());
                    Boolean addBill = walletBillClientAction.addBill("999", billAddRequestDto);
                    if (!addBill) {
                        logger.error("平台支付给发起人剩余金额出现异常");
                        throw new RuntimeException("平台支付给发起人剩余金额出现异常");
                    }
                }
            }
        }
        meeting.setStatus(MeetingStatus.SUCCESS.status);
        boolean success = meeting.updateById();
        if (!success) {
            logger.error("活动结束时更新状态出错");
            return false;
        }
        return true;
    }

    //向活动发布者支付报酬
    @Transactional(rollbackFor = Exception.class)
    public boolean paySend(BigDecimal amount, String userId) {
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        billAddRequestDto.setAmount(amount);
        billAddRequestDto.setDescription("活动报名费剩余的金额平均分给每个活动的发起人");
        billAddRequestDto.setPayChannel(3);
        billAddRequestDto.setType(1);
        billAddRequestDto.setToUserId(userId);
        Boolean addBill = walletBillClientAction.addBill("999", billAddRequestDto);
        if (!addBill) {
            throw new RuntimeException("平台支付给发起人支付的金额出现异常");
        }
        return true;
    }

    //获取用户活动总数
    public Integer getSendMeetingCount(String userId) {
        Integer sendCoun = worthServiceprovider.getMeetingService().getSendCount(userId);
        if (sendCoun == null) {
            sendCoun = 0;
        }
        return sendCoun;
    }

    //获取用户发布的成功活动总数
    public Integer getSuccessMeetingCount(String userId) {
        Integer SuccessCount = worthServiceprovider.getMeetingService().getSuccessCount(userId, 4);
        if (SuccessCount == null) {
            SuccessCount = 0;
        }
        return SuccessCount;
    }

    //获取用户一条最新的活动
    public MeetingDetailSendDto getLatestNewsMeeting(String userId, Double lat, Double lon) {
        MeetingDetailSendDto meetingDetailSendDto = new MeetingDetailSendDto();
        Meeting latestMeeting = worthServiceprovider.getMeetingService().getLatestMeeting(userId);
        if (latestMeeting == null) {
            return null;
        }
        VoPoConverter.copyProperties(latestMeeting, meetingDetailSendDto);
        meetingDetailSendDto.setMeetingImagesUrl(addImgUrlPreUtil.addImgUrlPres(latestMeeting.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket));
        meetingDetailSendDto.setMerchantId(latestMeeting.getPosterImagesUrl());
        meetingDetailSendDto.setMeetingId(latestMeeting.getId());
        meetingDetailSendDto.setAmount(latestMeeting.getAmount());
        UserInfoAndHeadImg sendInformation = userAction.getUserInfoAndHeadImg(userId);
        if (sendInformation == null) {
            return null;
        }
        VoPoConverter.copyProperties(sendInformation, meetingDetailSendDto);
        meetingDetailSendDto.setDistance(DistrictUtil.calcDistance(latestMeeting.getLat().doubleValue(), latestMeeting.getLon().doubleValue(), lat, lon));
        return meetingDetailSendDto;
    }

    /**
     * 查询列表
     *
     * @param meetingSearchDto
     * @return List<MeetingListDto>
     * @since ChenQian
     */
    public List<MeetingListDto> list(MeetingSearchDto meetingSearchDto) {
        //构建商品搜索条件、排序
        List<MeetingSearchResponse> meetingSearchResponseList = meetingSearchService.queryMeetings(getMeetingSearchQuery(meetingSearchDto));
        List<MeetingListDto> meetingListDtoList = new ArrayList<>();
        for (MeetingSearchResponse meetingSearchResponse : meetingSearchResponseList) {
            MeetingListDto dto = createMetingListDto(meetingSearchResponse);
            //List的总是要除以100
            dto.setAmount(dto.getAmount().divide(new BigDecimal("100")));
            meetingListDtoList.add(dto);
        }
        return meetingListDtoList;
    }

    /**
     * 活动搜索内容处理
     *
     * @param meetingSearchResponse
     * @return MeetingListDto
     * @since ChenQian
     */
    public MeetingListDto createMetingListDto(MeetingSearchResponse meetingSearchResponse) {
        MeetingListDto meetingListDto = new MeetingListDto();
        VoPoConverter.copyProperties(meetingSearchResponse, meetingListDto);
        if (StringUtils.isNotBlank(meetingSearchResponse.getMeetingImagesUrl())) {
            meetingListDto.setImages(netEnergyFuseAction.updateImagesUrl(meetingSearchResponse.getMeetingImagesUrl()));
        }
        if (meetingSearchResponse.getCreditSum() > 0) {
            meetingListDto.setHoldCredit(true);
        } else {
            meetingListDto.setHoldCredit(false);
        }
        if (meetingSearchResponse.getCreateTime() != null) {
            meetingListDto.setPublishTime(meetingSearchResponse.getCreateTime());
        }
        meetingListDto.setAge(ComputeAgeUtils.getAgeByBirthday(meetingSearchResponse.getBirthday()));
        return meetingListDto;
    }

    /**
     * 构建商品搜索条件、排序
     *
     * @param meetingSearchDto
     * @return MeetingSearchQuery
     * @since ChenQian
     */
    public MeetingSearchQuery getMeetingSearchQuery(MeetingSearchDto meetingSearchDto) {
        MeetingSearchQuery meetingSearchQuery = new MeetingSearchQuery();
        VoPoConverter.copyProperties(meetingSearchDto, meetingSearchQuery);
        meetingSearchQuery.setCenterGeoPoint(new GeoPoint(meetingSearchDto.getLat(), meetingSearchDto.getLon()));
        meetingSearchQuery.setPage(meetingSearchDto.getCurrent(), meetingSearchDto.getSize());

        /**
         * 排序方式
         * 0.最热>最近
         * 1.最新>最近
         * 2.最近>信用>在线
         * 3.支持网信
         * 4.信用>最近
         * 5.价格最低>最近
         * 不传：齐享欢乐（距离、信用、在线状态）
         */
        if (meetingSearchDto.getSort() == 0) {
            meetingSearchQuery.addFristAscQueries(new LastAscQuery("regCount", false));
            meetingSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        } else if (meetingSearchDto.getSort() == 1) {
            meetingSearchQuery.addFristAscQueries(new LastAscQuery("createTime", false));
            meetingSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        } else if (meetingSearchDto.getSort() == 3) {
            meetingSearchQuery.addFristAscQueries(new LastAscQuery("creditSum", false));
            meetingSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        } else if (meetingSearchDto.getSort() == 4) {
            meetingSearchQuery.addFristAscQueries(new LastAscQuery("credit", false));
            meetingSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        } else if (meetingSearchDto.getSort() == 5) {
            meetingSearchQuery.addFristAscQueries(new LastAscQuery("amount", true));
            meetingSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        } else {
            meetingSearchQuery.addLastAscQuery(new LastAscQuery("credit", false));
            meetingSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        }
        return meetingSearchQuery;
    }

    // 定时任务：检查金额是否足够
    public void checkAmountEnough(String meetingId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting.getRegCount() == 0 || meeting.getRegCount() < meeting.getFloor()) {// 报名人数等于0或者报名人数小于入选人数下限
            wzCommonImHistoryAction.add("999", meeting.getUserId(), "活动即将开始，但报名费用不足以支付你指定的消费，建议你修改开始时间，延长报名期限，如逾时未处理，活动将自动关闭。",
                    meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
        }
    }

    /* 检查确定入选人条件 **/
    @Transactional
    public boolean checkSureSelected(String meetingId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        boolean success = false;
        if (meeting == null) {
            logger.error(meetingId + "未找到该活动信息");
            throw new RuntimeException("未找到该活动信息");
        } else {
            /*  获取联合发起人 */
            List<String> sendUserId = worthServiceprovider.getMeetingSendService().getMeetingSendId(meetingId);
            /* 获取有效报名人数 */
            Integer registerCount = worthServiceprovider.getMeetingRegisterService().getAllRegisterCount(meetingId, sendUserId);
            /* 判断是否满足启动入选人条件 */
            if (registerCount >= meeting.getFloor() && meeting.getStatus() == 0) {
                if (meeting.getAmount() <= 0 || meeting.getAllRegisterAmount() >= meeting.getOrderPrice() || meeting.getFeeNotEnough() == 1) {
                    logger.info(meetingId + "满足确定入选人条件，即将启动选定入选人程序");
                    success = jobFuseAction.addJob(JobEnum.MEETING_SURE_SELECTED_JOB, meeting.getId(), meeting.getId(), meeting.getTitle() + ":确定活动入选人", meeting.getRegStopAt(), AuthorEmailEnum.ZHENG_JUE);
                    if (success) {
                        success = jobFuseAction.removeJob(JobEnum.MEETING_CHECK_SELECTED_CONDITIONS_JOB, meetingId, null, meetingId);
                        if (!success) {
                            logger.error("删除检查入选条件的定时任务失败" + meetingId);
                            success = true;
                        }
                    } else {
                        logger.error("生成确定入选的定时任务失败" + meetingId);
                        logger.error("启动默认的选定入选人程序");
                        SureSelected(meetingId);
                        success = true;
                    }
                }
            }
            if (!success) {
                if (meeting.getAllRegisterAmount() < meeting.getOrderPrice() && meeting.getAmount() > 0) {
                    meeting.setStatusDescription("活动费用低于" + Money.CentToYuan(meeting.getOrderPrice()) + "元");
                    //活动失败，推送发起人、入选者
                    sendMessageAfterFailActivity(meetingId);
                    logger.error(meetingId + "活动费用不满足启动入选人条件，活动失败");
                }
                if (registerCount < meeting.getFloor()) {
                    if (registerCount == 0) {
                        meeting.setStatusDescription("无可入选人员报名");
                        sendMessageAfterFailActivity(meetingId);
                    } else {
                        meeting.setStatusDescription("活动报名人数低于" + meeting.getFloor() + "人");
                        sendMessageAfterFailActivity(meetingId);
                    }
                    logger.error(meetingId + "报名人数上不满足启动入选人条件，活动失败");
                }
                if (meeting.getStatus() != 0) {
                    logger.error(meetingId + "活动状态不符合启动入选人条件，活动失败" + meeting.getStatus());
                }
                meeting.setStatus(MeetingStatus.FAIL.status);
                success = worthServiceprovider.getMeetingService().updateMeeting(meeting);
                if (success) {
                    if (meeting.getAmount() > 0) {
                        logger.info(meetingId + "设置活动失败成功，即将进入退款程序");
                        this.backRegisterFee(meetingId);
                        logger.info(meetingId + "活动退款成功");
                    }
                } else {
                    logger.error(meetingId + "未满足启动入选人条件，更新活动失败异常");
                }
            }
        }
        return success;
    }

    /* 定时任务：确定入选人 **/
    @Transactional
    public void SureSelected(String meetingId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting == null) {
            logger.error("没有该活动信息" + meetingId);
            throw new RuntimeException("没有该活动信息");
        }
        if (meeting.getStatus().equals(MeetingStatus.CANCEL.status) || meeting.getStatus().equals(MeetingStatus.FAIL.status)) {
            logger.error(meetingId + "该活动已失效,活动状态:" + meeting.getStatus());
            throw new RuntimeException("该活动已失效");
        }
        /* 获取活动发起者Id,用于筛选入选者 **/
        List<String> sendUserId = worthServiceprovider.getMeetingSendService().getMeetingSendId(meetingId);
        /* 二次检查活动状态，是否支持确定入选人 */
        if (meeting.getStatus() == 0) {
            /* 获取排除发起人之外的有效报名人数 **/
            int regCount = worthServiceprovider.getMeetingRegisterService().getAllRegisterCount(meetingId, sendUserId);
            /* 当有效报名人数满足活动人数下限时截止报名，并选定入选人 **/
            if (regCount >= meeting.getFloor()) {
                /* 截止报名 **/
                meetingAction.stopRegister(meeting, regCount);
                /* 标记入选人 */
                meetingRegisterAction.regSuccess(meetingId);
                logger.info(meetingId + "报名截止，已确定入选人");
                //推送消息
                sendMessageTo(meetingId);
                /* 如果是纯线上活动，则创建群聊 */
                if (meeting.getMeetingType().equals(MeetingType.ONLINE.getType())) {
                    Long groupId = null;
                    GroupDto groupDto = new GroupDto();
                    groupDto.setGroupName(meeting.getTitle());
                    groupDto.setType("userCreate");
                    groupDto.setUserId(meeting.getUserId());
                    groupId = groupAction.createGroup(groupDto);
                    if (groupId != null) {
                        meeting.setGroupId(groupId);
                        boolean success = worthServiceprovider.getMeetingService().updateMeeting(meeting);
                        if (!success) {
                            logger.error("更新群聊组Id失败！");
                            throw new RuntimeException();
                        }
                        List<MeetingRegister> meetingRegister = worthServiceprovider.getMeetingRegisterService().selectSuccessRegListByMeetingId(meetingId);
                        for (MeetingRegister aMeetingRegister : meetingRegister) {
                            groupAction.addMember(groupId, aMeetingRegister.getUserId());
                        }
                    } else {
                        logger.error("创建群聊组失败");
                    }
                }
                /* 定时任务：超时未确认细节活动关闭，活动开始前15分钟启动 */
                boolean success = jobFuseAction.addJob(JobEnum.MEETING_CHECK_CONFIRM_JOB, meeting.getId(), meeting.getId(), meeting.getTitle() + ":超时未确认活动细节取消活动", new Date(meeting.getStartedAt().getTime() - 900000), AuthorEmailEnum.ZHENG_JUE);
                if (!success) {
                    sendMessageAfterFailActivity(meetingId);
                    logger.error("创建检查“超时未确认活动细节则取消活动，退还所有报名者的费用并推送提醒所有报名用户”的定时任务失败错误参数：4");
                } else {
                    success = jobFuseAction.addJob(JobEnum.MEETING_REMIND_ATTEND_jOB, meeting.getId(), meeting.getId(), meeting.getTitle() + ":活动开始前30分钟提醒参与者出席", new Date(meeting.getStartedAt().getTime() - 1800000), AuthorEmailEnum.ZHENG_JUE);
                    if (!success) {
                        logger.error(meetingId + ":活动创建\"活动开始前三十分钟提醒参与者出席\"的定时任务失败");
                    }
                    if (!jobFuseAction.removeJob(JobEnum.MEETING_SURE_SELECTED_JOB, meetingId, null, meetingId)) {
                        logger.error(meetingId + ":确认活动入选人:的定时任务删除失败");
                    }
                }
            } else {
                meetingAction.meetingFail(meeting, 0);
                sendMessageAfterFailActivity(meetingId);
//                backRegisterFee(meetingId);
                logger.info(meetingId + "活动报名人数不足，活动失败");
            }
        }

    }

    /* 定时任务：检查确认活动细节*/
    @Transactional
    public void checkConfirmDetail(String meetingId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting == null) {
            throw new RuntimeException(meetingId + "此活动不存在");
        }
        if (meeting.getStatus().equals(MeetingStatus.CANCEL.status) || meeting.getStatus().equals(MeetingStatus.FAIL.status)) {
            logger.error(meetingId + "该活动已失效,活动状态:" + meeting.getStatus());
            throw new RuntimeException("该活动已失效");
        }
        /* 如果为线上活动 则创建是否同意开始活动程序 **/
        if (meeting.getMeetingType().equals(MeetingType.ONLINE.getType())) {
            /* 如果活动状态为同意开始活动，准备校验验证码 **/
            if (meeting.getStatus().equals(MeetingStatus.CODE_GENERATOR.status) && meeting.getConfirm()) {
                /* 创建检查是否同意开始活动的定时任务 **/
                boolean success = jobFuseAction.addJob(JobEnum.MEETING_CHECK_PUBLISH_START_JOB, meeting.getId(), meeting.getId(), meeting.getTitle()
                        + ":检查是否开始同意活动", meeting.getStartedAt(), AuthorEmailEnum.ZHENG_JUE);
                if (success) {
                    success = jobFuseAction.removeJob(JobEnum.MEETING_CHECK_CONFIRM_JOB, meetingId, null, meetingId);
                    if (!success) {
                        logger.error(meetingId + "删除检查确认活动细节的定时任务失败");
                    }
                } else {
                    logger.error(meetingId + "创建检查是否同意开始活动的定时任务失败");
                }
            } else {
                /*不满足条件，进入活动失败，退款程序 **/
                checkNoConfirmDetail(meetingId);
                //活动失败，推送
                sendMessageAfterFailActivity(meetingId);
            }
        } else {
            if (meeting.getMeetingType().equals(MeetingType.OFFLINE_NO_FEE.getType())) { //如果为线下活动
                //推送消息
                confirmDetailsMessageToSenderUnderline(meetingId);
            }
            if (meeting.getStatus() == 1 || meeting.getStatus().equals(MeetingStatus.CODE_GENERATOR.status)) {
                if (!meeting.getConfirm()) {// 没有确认
                    /*不满足条件，进入活动失败，退款程序 **/
                    checkNoConfirmDetail(meetingId);
                    //推送
                    sendMessageAfterFailActivity(meetingId);
                } else {
                    // 定时任务：检查活动发布者是否同意开始活动
                    boolean success = jobFuseAction.addJob(JobEnum.MEETING_CHECK_PUBLISH_START_JOB, meeting.getId(), meeting.getId(), meeting.getTitle() + ":检查是否开始同意活动", meeting.getStartedAt(), AuthorEmailEnum.ZHENG_JUE);
                    if (!success) {
                        logger.error("创建检查活动发布者是否同意开始活动的定时任务失败");
                    }
                    success = jobFuseAction.removeJob(JobEnum.MEETING_CHECK_CONFIRM_JOB, meeting.getId(), null, meeting.getId());
                    if (!success) {
                        logger.error("删除检查是否确认活动细节的定时任务失败");
                    }
                }
            }
        }
    }

    // 定时任务：检查活动发布者是否同意开始活动
    public void checkPublishStart(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId); //查询活动信息
        if (meeting != null) {
            if (meeting.getStatus().equals(MeetingStatus.CANCEL.status) || meeting.getStatus().equals(MeetingStatus.FAIL.status)) {
                logger.error(meetingId + "该活动已失效,活动状态:" + meeting.getStatus());
                throw new RuntimeException("该活动已失效");
            }
            boolean start = meeting.getStatus().equals(MeetingStatus.CODE_GENERATOR.status);// 判断活动状态是否为同意开始、准备分发验证码
            if (start) {
                //如果为线上，推送给发起人
                if (meeting.getMeetingType().equals(MeetingType.ONLINE.getType())) {
                    sendMessageToSenderOnline(meetingId);
                }
                //活动开始前分发验证码（线下），推送发起人，入选者
                if (meeting.getMeetingType().equals(MeetingType.OFFLINE_NO_FEE.getType())) {
                    sendMessageBeforeActivityUnderline(meetingId);
                }
            }else if(!start){
                //推送活动失败
                sendMessageAfterFailActivity(meetingId);
            }
            List<MeetingSend> list = meetingSendService.getAcceptListByMeetingId(meetingId); // 查询同意发起的活动状态列表
            if (meeting.getAmount() <= 0 || meeting.getBalancePay() || meeting.getFeeNotEnough() == 1 || meeting.getAllRegisterAmount() >= meeting.getMeetingFeePayAmount().doubleValue() && start) {
                //活动开始（线上），推送发起人、入选者
                if (meeting.getMeetingType().equals(MeetingType.ONLINE.getType())) {
                    sendMessageBeforeActivityOnline(meetingId);
                }
                // 定时任务：活动结束后检查成功通过验证出席者扣减信用
                boolean success = jobFuseAction.addJob(JobEnum.MEETING_CHECK_SUCCESS_JOB, meeting.getId(), meeting.getId(), meeting.getTitle() + "检查是否有活动人员出席", new Date(meeting.getStartedAt().getTime() + 1800000), AuthorEmailEnum.ZHENG_JUE);
                if (!success) {
                    logger.error("创建检查是否有活动参与者出席");
                }
                // 定时任务：活动结束，自动结束
                success = jobFuseAction.addJob(JobEnum.MEETING_AUTO_SETTLE_JOB, meeting.getId(), meeting.getId(), meeting.getTitle() + ":活动结束自动结算", meeting.getEndAt(), AuthorEmailEnum.ZHENG_JUE);
                if (!success) {
                    logger.error("创建活动结束自动结算的定时任务失败");
                }
                if (success) {
                    //活动结束，推送发起人、入选者
                    sendMessageAfterActivity(meetingId);
                }
                // 定时任务：检查活动结束后24内是否评价
                success = jobFuseAction.addJob(JobEnum.MEETING_CHECK_EVALUATE_JOB, meeting.getId(), meeting.getId(), meeting.getTitle() + ":检查活动结束24小时内是否评论", new Date(meeting.getEndAt().getTime() + 43200000), AuthorEmailEnum.ZHENG_JUE);
                if (!success) {
                    logger.error("创建检查活动结束24小时内是否评论的定时任务失败");
                }
            } else {
                /*  主要发起者未补足活动差额 **/
                if (meeting.getFeeNotEnough() == 1 && !meeting.getBalancePay()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("relatableId", meeting.getId());
                    map.put("relatableType", MeetingSend.class.getSimpleName());
                    map.put("credit", -5); // 扣减5分信用
                    map.put("description", meeting.getTitle() + "违约，未补足活动差额");
                    Integer lockVersion = userClientProxy.getUserLockVersion(meeting.getUserId());
                    map.put("lockVersion", lockVersion);
                    map.put("userId", meeting.getUserId());
                    wangMingClientAction.addCreditRecord(map);//减信用
                    meeting.setStatusDescription("发起者未及时补足活动费用差额");
                } else {
                    if (!start) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("relatableId", meeting.getId());
                        map.put("relatableType", MeetingSend.class.getSimpleName());
                        map.put("credit", -5); // 扣减5分信用
                        map.put("description", meeting.getTitle() + "超时未同意开始活动");
                        for (MeetingSend meetingSend : list) {
                            Integer lockVersion = userClientProxy.getUserLockVersion(meetingSend.getUserId());
                            map.put("lockVersion", lockVersion);
                            map.put("userId", meetingSend.getUserId());
                            wangMingClientAction.addCreditRecord(map);//减信用
                            meeting.setStatusDescription("发起者未及时同意开始活动");
                        }
                    }
                }
                meeting.setStatus(MeetingStatus.FAIL.status);
                boolean success = worthServiceprovider.getMeetingService().updateMeeting(meeting);
                if (success) {
                    if (!start) {
                        //推送
                        sendMessageAfterFailActivity(meetingId);
                        logger.error("超时未同意开始活动，自动结束活动失败");
                    } else {
                        //推送
                        sendMessageAfterFailActivity(meetingId);
                        logger.error("主要发起者违约，未补足活动费用，活动自动失败");
                    }
                    if (meeting.getAmount() > 0) {
                        backRegisterFee(meetingId);
                        logger.error(meetingId + "活动退款成功");
                    }
                }

                /* 入选者放弃参与或在活动开始时仍未点击确认开始按钮 **/
//                meetingRegisterService.getRegListByMeetingId(meetingId).forEach(meetingRegister -> {
//                    if (meetingRegister.getStatus().equals(MeetingRegisterStatus.SUCCESS.status) || meetingRegister.getStatus().equals(MeetingRegisterStatus.CANCEL.status)) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("relatableId", meeting.getId());
//                        map.put("relatableType", MeetingRegister.class.getSimpleName());
//                        map.put("credit", -5);
//                        map.put("description", meeting.getTitle() + "超时未点击确认参加活动");
//                        Integer lockVersion = userClientProxy.getUserLockVersion(meetingRegister.getUserId());
//                        map.put("lockVersion", lockVersion);
//                        map.put("userId", meetingRegister.getUserId());
//                        wangMingClientAction.addCreditRecord(map);//减信用
//                    }
//                });
            }
        }
    }

    // 定时任务：检查是否有活动参与者出席
    public void checkRegisterIfAttend(String meetingId) throws Exception {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting == null) {
            logger.error(meetingId + "该活动不存在");
            throw new RuntimeException(meetingId + "该活动不存在");
        }
        if (meeting.getStatus().equals(MeetingStatus.CANCEL.status) || meeting.getStatus().equals(MeetingStatus.FAIL.status)) {
            logger.error(meetingId + "该活动已失效,活动状态:" + meeting.getStatus());
            throw new RuntimeException("该活动已失效");
        }
        /* 获取活动发起者出席人数 **/
        int sendAttend = worthServiceprovider.getMeetingSendService().getAttendSendCount(meetingId);
        /* 获取入选者出席人数 **/
        List<String> attend = worthServiceprovider.getMeetingRegisterService().getAttend(meetingId).stream().map(MeetingRegister::getUserId).collect(Collectors.toList());
//	    if(sendAttend>=0 && attend.size() <=0){//只有发起者，发起者获得利息
//            try{
//                meetingAction.checkSuccess(meetingId);
//            }catch (Exception e){
//                logger.error(meetingId+"没有入选者出席，活动失败结算错误");
//            }
//        }
//        if(attend.size()>0 || sendAttend<=0 && meeting.getAmount()>0){//只有入选者出席，退还报名费
//            try{
//                meetingRefundMoney(meetingId,attend,"没有发起者出席，退还入选者报名费");
//            }catch (Exception e){
//                logger.error(meetingId+"没有发起者通过校验，退回活动报名费失败");
//            }
//        }
        if (sendAttend <= 0 || attend.size() <= 0) {//既没有入选者，也没有发起者
            meeting.setStatusDescription("无人员出席");
            meeting.setStatus(MeetingStatus.FAIL.status);
            //推送活动失败
            sendMessageAfterFailActivity(meetingId);
            boolean success = worthServiceprovider.getMeetingService().updateMeeting(meeting);
            if (!success) {
                logger.error("没有入选者出席，更新活动失败错误");
            }
        }
    }

    // 定时任务：检查评价
    public void checkEvaluate(String meetingId) {
        List<MeetingRegister> meetingRegisters = meetingRegisterService.getRegSuccessListByMeetingId(meetingId);
        List<MeetingSend> meetingSends = meetingSendService.getAcceptListByMeetingId(meetingId);
        List<String> registerUserIds = meetingRegisters.stream().map(MeetingRegister::getUserId).collect(Collectors.toList());
        List<String> sendUserIds = meetingSends.stream().map(MeetingSend::getUserId).collect(Collectors.toList());
        sendUserIds.addAll(registerUserIds);
        List<String> list = new EvaluateProxy().notEvaluateUsers(sendUserIds, meetingId);
        list.forEach(userId -> {
            settlementAction.settlementCredit("Meeting", meetingId, userId, -2);
        });
    }

    // 定时任务：活动开始前提醒活动相关人员
    public void remindAttendMeeting(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting != null) {
            /* 当活动未有效状态时推送 **/
            if (!meeting.getStatus().equals(MeetingStatus.CANCEL.status) && !meeting.getStatus().equals(MeetingStatus.FAIL.status)) {
                /* 获取活动有效发起者 **/
                List<MeetingSend> meetingSendList = worthServiceprovider.getMeetingSendService().getAcceptListByMeetingId(meetingId);
                /* 获取活动入选者 **/
                List<MeetingRegister> meetingRegisterList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
                if (meetingSendList != null && meetingSendList.size() > 0) {
                    for (MeetingSend meetingSend : meetingSendList) {
                        wzCommonImHistoryAction.add("999", meetingSend.getUserId(), "\"" + meeting.getTitle() + "\"还有30分钟开始，请做好出席准备", meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                    }
                }
                if (meetingRegisterList != null && meetingRegisterList.size() > 0) {
                    for (MeetingRegister meetingRegister : meetingRegisterList) {
                        wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), "\"" + meeting.getTitle() + "\"还有30分钟开始，请做好出席准备", meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                    }
                }
            }
        }
    }

    //todo
    /* 定时任务：活动结束24小时之后，未付款给商家则自动获取冻结金额付款给商家 **/
//    public void checkAutomaticPayment(String meetingId){
//        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
//        /* 当存在订单消费时，检查订单消费状态 **/
//        if(StringUtils.isNotBlank(meeting.getOrderIds()) && meeting.getOrderPrice()>0){
//            /* 活动ID和订单ID绑定，可直接根据订单ID获取订单详情 */
//            MerchantOrderInfo merchantOrderInfo = merchantOrderInfoService.selectById(meeting.getOrderIds());
//            /* 如果超出活动结束时间24后仍为未付款状态则自动付款 **/
//            if(merchantOrderInfo.getPayStatus().equals("PS_UNPAID")){
//
//            }
//        }
//
//    }

    /* 活动退款流程 **/
    private boolean meetingRefundMoney(String meetingId, List<String> animal, String exitType) {
        boolean success = false;
        if (StringUtils.isNotBlank(meetingId) && animal != null && animal.size() > 0) {
            for (String userId : animal) {
                MeetingRegister meetingRegister = worthServiceprovider.getMeetingRegisterService().selectByMeetingIdAndUserId(meetingId, userId);
                if (meetingRegister.getFee() > 0 && meetingRegister.getFee() != null) {
                    FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto();
                    frozenOperationRequestDto.setTypeId(meetingId);
                    frozenOperationRequestDto.setUserId(userId);
                    frozenOperationRequestDto.setType(FrozenTypeEnum.FTZ_MEETING);
                    success = walletForzenClientAction.repealFrozen(frozenOperationRequestDto);
                    if (success) {
                        logger.info(userId + "用户参加的" + meetingId + "活动因" + exitType + "退款成功");
                    } else {
                        logger.error(userId + "用户参加的" + meetingId + "活动因" + exitType + "退款失败");
                    }
                }
            }
        } else {
            success = true;
        }
        return success;
    }

    /* 扣减信用分数工具 **/
    private boolean meetingReduceCredit(String meetingId, String title, String userId, String exitType, boolean roleType, Integer scores) {
        boolean success = false;
        if (StringUtils.isNotBlank(meetingId) && StringUtils.isNotBlank(userId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("relatableId", meetingId);
            if (roleType) {
                map.put("relatableType", MeetingSend.class.getSimpleName());
            } else {
                map.put("relatableType", MeetingRegister.class.getSimpleName());
            }
            map.put("credit", -scores);
            map.put("description", "《" + title + "》扣分原因：" + exitType);
            Integer lockVersion = userClientProxy.getUserLockVersion(userId);
            map.put("lockVersion", lockVersion);
            map.put("userId", userId);
            success = wangMingClientAction.addCreditRecord(map);
            if (success) {
                logger.info(userId + "发起的" + title + "活动因" + exitType + "扣除" + scores + "成功Id:" + meetingId);
            } else {
                logger.error(userId + "发起的" + title + "活动因" + exitType + "扣除" + scores + "失败Id:" + meetingId);
            }
        }
        return success;
    }

//    /* 批量删除测试 **/
//    public boolean batchDeleteJob(String meetingId){
//        if(jobFuseAction.BatchRemoveJob(meetingId)){
//            return true;
//        } else {
//            return false;
//        }
//    }

    /*活动确认入选人，推送给发起人、入选者、未入选者*/
    public void sendMessageTo(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting != null && meeting.getStatus().equals(1)) {
            //获取入选者
            List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
            //获取未入选者
            List<MeetingRegister> failRegList = worthServiceprovider.getMeetingRegisterService().getFailRegListById(meetingId);
            //所有报名者
            List<MeetingRegister> regList = meetingRegisterService.getRegSuccessListByMeetingId(meetingId);
//            List<MeetingRegister> regList2 = worthServiceprovider.getMeetingRegisterService().getRegSuccessListByMeetingId(meetingId);
            //获取发起人
            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
            if (successRegList != null && successRegList.size() > 0 && sendList != null && sendList.size() > 0) {
                //推送给发起人
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), "\"" + meeting.getTitle() + "\"活动已经开始，请做好出席准备",
                            meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
                //推送给入选者
                for (MeetingRegister meetingRegister : successRegList) {
                    wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), "恭喜入选\"" + meeting.getTitle() + "\"活动",
                            meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
                //推送除去发起者的未入选者
//                for (MeetingRegister meetingRegister : failRegList) {
//                    for (MeetingSend meetingSend : sendList) {
//                        if (!meetingRegister.getUserId().equals(meetingSend.getUserId())) {
//                            wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), "\"" + meeting.getTitle() + "\"活动报名已截止，抱歉，您未能入选",
//                                    meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
//                        }
//                    }
//                }
                //推送给未入选者
                for (MeetingRegister meetingRegister : failRegList) {
                    wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), "\"" + meeting.getTitle() + "\"活动报名已截止，抱歉，您未能入选",
                            meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }

        }
    }

    //确认活动细节推送发起人、入选者（线下）
    public void confirmDetailsMessageToSenderUnderline(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting != null && meeting.getMeetingType().equals(MeetingType.OFFLINE_NO_FEE.getType())) {
            //获取发起人
            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
            //获取入选者
            List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
            if (sendList != null && sendList.size() > 0) {
                //推送给发起人
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), "\"" + meeting.getTitle() + "\"活动已确认入选人，请在8：45前确认活动细节",
                            meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
            if (successRegList != null && successRegList.size() > 0) {
                //推送给入选者
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), "您入选活动\"" + meeting.getTitle()
                                    + "\"活动开始时间为" + meeting.getStartedAt() + "地点为" + meeting.getAddress() + "，消费为" + meeting.getOrderPrice() + "请准时出席", meetingId,
                            MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
        }
    }

    //推送：发起人可以开始活动（线上）
    public void sendMessageToSenderOnline(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting != null && meeting.getMeetingType().equals(MeetingType.ONLINE.getType())) {
            //获取发起人
            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
            if (sendList != null && sendList.size() > 0) {
                //推送给发起人
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), "请在8：45前开始\"" + meeting.getTitle() + "\"活动",
                            meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
        }
    }


    //活动开始前分发验证码后推送（线下）
    public void sendMessageBeforeActivityUnderline(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting != null && meeting.getStatus().equals(5) && meeting.getMeetingType().equals(MeetingType.OFFLINE_NO_FEE.getType())) {
            //获取入选人
            List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
            //获取发起人
            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
            if (successRegList != null && successRegList.size() > 0 && sendList != null && sendList.size() > 0) {
                //线下推送给发起人
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), "（线下）请在\"" + meeting.getTitle()
                            + "\"活动开始前将验证码提供给入选者，未通过验证则视为未正常参与活动", meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
                //推送给入选人
                for (MeetingRegister meetingRegister : successRegList) {
                    wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), "发起人已接收验证码，请在\"" + meeting.getTitle() + "\"活动内验证到场",
                            meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
        }
    }

    //活动开始后 推送给商家（线下） 未完成
//    public void sendToBusinessAfterActivityUnderline(String meetingId) {
//        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
//        if (meeting != null) {
//            //获取商家
//
//            //确认入选人
//            List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
//            //获取发起人
//            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
//            if (successRegList != null && successRegList.size() > 0 && sendList != null && sendList.size() > 0) {
//                //线下推送给发起人
//                for (MeetingSend meetingSend : sendList) {
//                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), "活动已开始", meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
//                }
//                //推送给入选者
//                for (MeetingRegister meetingRegister : successRegList) {
//                    wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), "活动已开始", meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
//                }
//            }
//        }
//    }

    //活动开始后 推送（线上）
    public void sendMessageBeforeActivityOnline(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        if (meeting != null && meeting.getMeetingType().equals(MeetingType.ONLINE.getType())) {
            //确认入选人
            List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
            //获取发起人
            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
            if (successRegList != null && successRegList.size() > 0 && sendList != null && sendList.size() > 0) {
                //线下推送给发起人
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), "活动已开始", meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
                //推送给入选者
                for (MeetingRegister meetingRegister : successRegList) {
                    wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), "活动已开始", meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
        }
    }

    //活动结束推送发起人和入选者
    public void sendMessageAfterActivity(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        String message = "\"" + meeting.getTitle() + "\"活动已结束，请在结束后24小时内评论活动";
        if (meeting != null && meeting.getStatus().equals(4)) {
            //确认入选人
            List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
            //获取发起人
            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
            if (successRegList != null && successRegList.size() > 0 && sendList != null && sendList.size() > 0) {
                //推送给发起人
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), message, meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
                //推送给入选者
                for (MeetingRegister meetingRegister : successRegList) {
                    wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), message, meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
        }
    }

    //活动失败，推送发起人和入选者
    public void sendMessageAfterFailActivity(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        String message = "\"" + meeting.getTitle() + "\"活动失败，失败原因：" + meeting.getStatusDescription();
        if ((meeting.getStatus().equals(3) || meeting.getStatus().equals(6))) { //meeting != null&&
            //确认入选人
            List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
            //获取发起人
            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
            if (successRegList != null && successRegList.size() > 0 && sendList != null && sendList.size() > 0) {
                //推送给发起人
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), message, meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
                //推送给入选者
                for (MeetingRegister meetingRegister : successRegList) {
                    wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), message, meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
        }
    }

    //活动取消，推送发起人和入选者
    public void sendMessageCancelActivity(String meetingId) {
        Meeting meeting = worthServiceprovider.getMeetingService().selectById(meetingId);
        String message = "发起人已取消\"" + meeting.getTitle() + "\"活动";
        if (meeting.getStatus().equals(2)) {
            //确认入选人
            List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
            //获取发起人
            List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
            //推送给发起人
            if (sendList != null && sendList.size() > 0) {
                for (MeetingSend meetingSend : sendList) {
                    wzCommonImHistoryAction.add("999", meetingSend.getUserId(), message, meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
            if (successRegList != null && successRegList.size() > 0) {
                //推送给入选者
                for (MeetingRegister meetingRegister : successRegList) {
                    wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), message, meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
                }
            }
        }
    }

    //推送提取的代码
//    public void SendMessage(String meetingId,String message){
//        //确认入选人
//        List<MeetingRegister> successRegList = worthServiceprovider.getMeetingRegisterService().getSuccessRegListById(meetingId);
//        //获取发起人
//        List<MeetingSend> sendList = worthServiceprovider.getMeetingSendService().getSendListByMeetingId(meetingId);
//        //推送给发起人
//        if (sendList != null && sendList.size() > 0) {
//            for (MeetingSend meetingSend : sendList) {
//                wzCommonImHistoryAction.add("999", meetingSend.getUserId(), message, meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
//            }
//        }
//        if (successRegList != null && successRegList.size() > 0) {
//            //推送给入选者
//            for (MeetingRegister meetingRegister : successRegList) {
//                wzCommonImHistoryAction.add("999", meetingRegister.getUserId(), message, meetingId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.MEETINGDETAIL, null);
//            }
//        }
//    }

    //获取用户一条最新的活动
    public MeetingDetailSendDto getMeetingById(String Id,Double lat,Double lon) {
        MeetingDetailSendDto meetingDetailSendDto = new MeetingDetailSendDto();
        Meeting latestMeeting = worthServiceprovider.getMeetingService().selectById(Id);
        if (latestMeeting == null) {
            return null;
        }
        VoPoConverter.copyProperties(latestMeeting, meetingDetailSendDto);
        meetingDetailSendDto.setMeetingImagesUrl(addImgUrlPreUtil.addImgUrlPres(latestMeeting.getMeetingImagesUrl(), AliyunBucketType.ActivityBucket));
        meetingDetailSendDto.setMerchantId(latestMeeting.getPosterImagesUrl());
        meetingDetailSendDto.setMeetingId(latestMeeting.getId());
        meetingDetailSendDto.setAmount(latestMeeting.getAmount());
        UserInfoAndHeadImg sendInformation=null;
        if(StringUtils.isNotBlank(latestMeeting.getUserId())){
            sendInformation = userAction.getUserInfoAndHeadImg(latestMeeting.getUserId());
        }
        if (sendInformation == null) {
            return null;
        }
        VoPoConverter.copyProperties(sendInformation, meetingDetailSendDto);
        meetingDetailSendDto.setDistance(DistrictUtil.calcDistance(latestMeeting.getLat().doubleValue(), latestMeeting.getLon().doubleValue(), lat, lon));
        return meetingDetailSendDto;
    }
}
