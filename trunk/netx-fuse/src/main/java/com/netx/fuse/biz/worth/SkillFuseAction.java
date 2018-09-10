package com.netx.fuse.biz.worth;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.*;
import com.netx.common.common.utils.HttpUtils;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.vo.common.CostSettingResponseDto;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.common.vo.currency.WzCommonOtherSetResponseDto;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.skill.SkillDataListDto;
import com.netx.common.wz.dto.skill.SkillDetailDto;
import com.netx.common.wz.dto.common.CreateRefundDto;
import com.netx.common.wz.dto.skill.*;
import com.netx.fuse.biz.FuseBaseAction;
import com.netx.fuse.client.ucenter.*;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.model.SkillSearchResponse;
import com.netx.searchengine.query.SkillSearchQuery;
import com.netx.searchengine.service.SkillSearchService;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import com.netx.shopping.service.merchantcenter.MerchantManagerService;
import com.netx.shopping.service.ordercenter.MerchantOrderInfoService;
import com.netx.ucenter.biz.common.EvaluateAction;
import com.netx.ucenter.biz.common.WalletFrozenAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.friend.FriendsAction;
import com.netx.ucenter.model.common.CommonEvaluate;
import com.netx.ucenter.model.user.User;
import com.netx.utils.DistrictUtil;
import com.netx.utils.datastructures.Tuple;
import com.netx.utils.money.Money;
import com.netx.worth.biz.common.RefundAction;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.biz.skill.SkillAction;
import com.netx.worth.biz.skill.SkillOrderAction;
import com.netx.worth.biz.skill.SkillRegisterAction;
import com.netx.worth.enums.SkillOrderStatus;
import com.netx.worth.enums.SkillRegisterStatus;
import com.netx.worth.model.*;
import com.netx.worth.service.WorthServiceprovider;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.elasticsearch.common.geo.GeoPoint;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SkillFuseAction extends FuseBaseAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private WalletBillClientAction walletBillClientAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private RefundAction refundAction;
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private SkillRegisterAction skillRegisterAction;
    @Autowired
    private SkillOrderAction skillOrderAction;
    @Autowired
    private SkillAction skillAction;
    @Autowired
    private SkillOrderFuseAction skillOrderFuseAction;
    @Autowired
    private OtherSetClientAction otherSetClientAction;
    @Autowired
    private SkillSearchService skillSearchService;
    @Autowired
    private NetEnergyFuseAction netEnergyFuseAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private JobFuseAction jobFuseAction;
    @Autowired
    private CostSettingClientAction costSettingClientAction;
    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;
    @Autowired
    private FriendsAction friendsAction;
    @Autowired
    private EvaluateAction evaluateAction;
    @Autowired
    private WalletFrozenAction walletFrozenAction;
    @Value("${localHost.host}")
    private String host;

    /* 发布技能 **/
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(SkillPublishDto skillPublishDto, BigDecimal lon, BigDecimal lat) throws Exception {
        //判断是否可以发布技能
        WzCommonOtherSetResponseDto otherSetResult = otherSetClientAction.queryRemote();
        if (otherSetResult == null) {
            throw new RuntimeException("远程其他设置查询失败");
        }
        //验证用户信息
		/*StringBuffer buffer = this.booleanIsCanRelease(skillPublishDto.getUserId(), otherSetResult);
		if (!buffer.toString().equals("")) {
			throw new Exception(buffer.toString());
		}*/
        Tuple<Boolean, String> tuple = this.booleanIsCanReleaseTuple(skillPublishDto.getUserId(), otherSetResult);
        if (!tuple.left()) {
            throw new RuntimeException(tuple.right());
        }
        //判断相同的需求
        List<Skill> list = worthServiceprovider.getSkillService().samePublish(skillPublishDto);
        if (list != null && list.size() > 0) {
            return false;
        }
        String skillId = worthServiceprovider.getSkillService().insertOrUpdateSkill(skillAction.SkillPublishDtoToSkill(skillPublishDto, lon, lat));
        if (skillId == null) {
            throw new RuntimeException("发布技能失败");
        }
        skillPublishDto.setId(skillId);
        return true;
    }

    /* 发出预约,返回预约者s */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> register(SkillRegisterDto skillRegisterDto) throws Exception { //SkillRegisterDto：作为一个公共类，作用类似于SkillRegister类，技能预约表
        //判断预约者不能预约自己的技能
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegisterDto.getSkillId());
        if (skill == null) {
            throw new RuntimeException("此技能可能已取消");
        }
        if (skill.getUserId().equals(skillRegisterDto.getUserId())) {
            throw new RuntimeException("不能预约自己发布的技能");
        }
        //不能预约结束技能
        if (skill.getStatus() == 3) {
            throw new RuntimeException("不能预约已结束技能");
        }
        //不能预约结束技能
        if (skill.getStatus() == 2) {
            throw new RuntimeException("不能预约已取消技能");
        }
        //判断同一个不能多次预约一个技能
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().getReByUserIdAndSkillIdList(skillRegisterDto.getUserId(), skill.getId());
        if (skillRegister != null) {
            if (skillRegister.getStatus() < 2) {
                throw new RuntimeException("上一次预约未完成，暂不能预约此技能");
            }
//			skillRegisterDto.setId(skillRegister.getId());
        }
        if (skill.getObj() != 1) {
            UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg(skillRegisterDto.getUserId());
            switch (skill.getObj()) {
                case 2:
                    break;
                case 3:
                    Double Distance = DistrictUtil.calcDistance(skill.getLat().doubleValue(), skill.getLon().doubleValue(), skillRegisterDto.getLat().doubleValue(), skillRegisterDto.getLon().doubleValue());
                    if (Distance > 50) {
                        throw new RuntimeException("您的距离超出预约范围");
                    }
                    break;
                case 4:
                    if (!userInfoAndHeadImg.getSex().equals("女")) {
                        throw new RuntimeException("该技能仅限女性预约！");
                    }
                    break;
                case 5:
                    if (!userInfoAndHeadImg.getSex().equals("男")) {
                        throw new RuntimeException("该技能仅限男性预约！");
                    }
                    break;
                case 6:
                    if (!friendsAction.isFriend(skill.getUserId(), skillRegisterDto.getUserId())) {
                        throw new RuntimeException("该技能仅限发布者好友预约！");
                    }
                    break;
            }
        }
        //获取预约者
        Map<String, Object> map = skillRegisterAction.SkillRegisterDtoToSkill(skill.getAmount(), skillRegisterDto);
        //确认细节
//        String skillRegisterId = (String) map.get("skillRegister");
//        skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
        //托管费用
        Boolean flag = true;
//        if(skillRegister!=null) {
//             flag = registerDeposit(skillRegister.getId(), skillRegister.getUserId(), new BigDecimal(skillRegister.getBail()), skillRegister.getPayWay());
//        }
        if (flag == true) {
            wzCommonImHistoryAction.add(skillRegisterDto.getUserId(), skill.getUserId(),
                    "您的技能\""+skill.getSkill()+"\"正在被" + getNickNameByuserId(skillRegisterDto.getUserId()) + "预约，是否接受？", skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
        }
        if (map.size() > 0) {
            //修改预约总数
            skill.setRegisterCount(skill.getRegisterCount() + 1);
            worthServiceprovider.getSkillService().updateById(skill);
            //开启定时器，检测发布者接受预约是否超时
            String skillRegisterId = map.get("skillRegister").toString();
            Date startAt = (Date) map.get("startAt");
            jobFuseAction.addJob(JobEnum.SKILL_CHECK_REGISTER_JOB, skillRegisterId, skillRegisterId, "技能发布者接受预约超时", startAt, AuthorEmailEnum.SHI_JIE);
            map.remove("startAt");

        }
        return map;
    }

    //确认技能详情等细节
    public String getSkillOrder(SkillOrder skillOrder) {
        return skillOrder.toString();
    }

    /* 获取预约者名字 **/
    public String getNickNameByuserId(String userId) {
        User user = userAction.getUserService().selectById(userId);
        return user.getNickname();
    }

    /**
     * 获取用户性别
     * @param userId
     * @return
     */
    public String getSexStringByUserId(String userId){
        User user = userAction.getUserService().selectById(userId);
        String sex = user.getSex();
        String returnSexStr = "ta";
        if(sex.trim().equals("1") || sex.trim().equals("男")){
            returnSexStr = "他";
        }else if(sex.trim().equals("0") || sex.trim().equals("女")){
            returnSexStr = "她";
        }
        return returnSexStr;
    }

    /* 报名者已托管费用 **/
    @Transactional(rollbackFor = Exception.class)
    public boolean registerDeposit(String skillRegisterId, String userId, BigDecimal bail, Integer payWay) throws Exception {
        boolean success = false;
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        success = worthServiceprovider.getSkillRegisterService().registerDeposit(skillRegisterId, userId, bail, payWay);//申请保障金
        if (!success)
            return false;
        //通知发布者
        wzCommonImHistoryAction.add("999",skill.getUserId(),"\""+getNickNameByuserId(skillRegister.getUserId())+"\"已托管\""+skill.getSkill()+"\"技能的费用，请在技能预约开始前启动预约！",skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
//        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
        //TODO
        //	success = quartzService.checkSkillRegisterAccept(skillRegister.getStartAt().getTime(), skillRegisterId, bail,
        //	skillRegister.getUserId());
//        if (!success) {
//            logger.error("创建“未接受或预约者设定的时间开始后，发布者均未响应，则预约失败，托管费用解冻并退回给预约者”定时任务失败");
//            throw new RuntimeException();
//        }

        return success;
    }

    /* 发布者接受预约 skill userId */
    @Transactional(rollbackFor = Exception.class)    //id:技能Id，userId：技能发布者id，skillRegisterId：预约表id
    public Map<String, Object> publishAccept(String id, String userId, String skillRegisterId) throws Exception {
        Skill skill = worthServiceprovider.getSkillService().queryByUserIdAndSkillId(id, userId);
        //判断该用户是否有这个技能
        if (skill == null) {
            throw new RuntimeException("此技能可能已取消");
        }
        Map<String, Object> map = new HashMap<>();
        boolean success = false;
        //检查是否接受预约

        //获取预约者
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
        boolean registerStatus = skillRegister.getStatus().equals(SkillRegisterStatus.REGISTERED.status);
        if (!registerStatus) {
            throw new RuntimeException("只能接受待入选状态的预约单");
        }
        if (skillRegister.getStartAt() == null || skillRegister.getEndAt() == null) {
            throw new RuntimeException("预约单的开始时间、结束时间不能为空");
        }

        SkillOrder skillOrder = skillOrderAction.create(id, userId, skillRegisterId);
        String skillOrderId = skillOrder.getId();
        if (StringUtils.isBlank(skillOrderId)) {
            throw new RuntimeException("发布者接受预约失败");
        }
        //预约者是否成为入选者
        success = worthServiceprovider.getSkillRegisterService().success(skillRegisterId);
        if (!success) {
            throw new RuntimeException("标记为入选失败");
        } else {
            skill.setSuccessCount(skill.getSuccessCount() + 1);
            worthServiceprovider.getSkillService().updateById(skill);
        }
        //获取入选者

        Settlement settlement = settlementAction.create("初始化", "SkillOrder", skillOrderId, false,
                skillOrder.getEndAt().getTime() + 25l * 3600 * 1000);// 延后1小时以免评论check还没执行
        success = settlement != null && StringUtils.isNotBlank(settlement.getId());
        if (!success) {
            throw new RuntimeException("初始化结算表失败");
        }
        //接受预约后启用定时器，在开始预约时间开始后查双方出席情况
        jobFuseAction.addJob(JobEnum.SKILL_CHECK_START_JOB, skillOrderId, skillOrderId, "检测发布者是否已经启动预约", skillRegister.getStartAt(), AuthorEmailEnum.SHI_JIE);
        //推送给入选者
        wzCommonImHistoryAction.add(skill.getUserId(),skillRegister.getUserId(),"您的\""+skill.getSkill()+"\"技能预约已被接受，请前往托管费用！",skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
        //TODO
        //	success = quartzService.checkOrderStart(skillOrderId, skillOrder.getStartAt().getTime());
//		if (!success) {
//			throw new RuntimeException("创建“检查预约单的开始时间、结束时间不能为空”的定时任务失败");
//		}
        //TODO
        //	success = quartzService.checkSkillSuccess(skillOrderId, skillOrder.getStartAt().getTime() + 1800 * 1000l);
//		if (!success) {
//			throw new RuntimeException("创建“检查预约单的开始时间、结束时间不能为空”的定时任务失败");
//		}
        //TODO
        //	success = quartzService.checkSkillEvaluate(skillOrderId, skillOrder.getEndAt().getTime() + 24 * 3600 * 1000l);
//		if (!success) {
//			throw new RuntimeException("创建“检查是否双方已评价”的定时任务失败");
//		}
//		Skill skill = null;
//		try {
//			skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
//		} catch (Exception e) {
//			e.getMessage();
//		}

        // 同意用户预约成功，向发布者、入选者、商家的业务主管发送信息
//		try {
//			messagePushProxy.messagePush(MessageTypeEnum.ACTIVITY_TYPE,
//					skill.getSkill() + "技能预约成功，时间" + skillOrder.getStartAt() + "，地点" + skillOrder.getAddress() + "，人数2人",
//					"技能提醒", userId, PushMessageDocTypeEnum.WZ_SKILLDETAIL.getValue(), skillRegister.getSkillId());// 入选者
//			messagePushProxy.messagePush(MessageTypeEnum.ACTIVITY_TYPE,
//					skill.getSkill() + "技能预约成功，时间" + skillOrder.getStartAt() + "，地点" + skillOrder.getAddress() + "，人数2人",
//					"技能提醒", skill.getUserId(), PushMessageDocTypeEnum.WZ_SKILLDETAIL.getValue(), skillRegister.getSkillId());// 发布者
//		} catch (Exception e) {
//		}
        map.put("orderId", skillOrder.getId());
        return map;
    }


    /**
     * 预约成功，向发布者、入选者、商家业务主管推送
     *
     * @param id     技能id
     * @param userId 技能发布者id
     * @author hgb
     */
    public void sendMessageAfterSuccessOder(String id, String userId) {  //372行代码有问题，商家无法推送
        //获取技能
        Skill skill = worthServiceprovider.getSkillService().queryByUserIdAndSkillId(id, userId);
        String message;
//        一条预约记录对应一条订单
//        每条订单记录对应不同商家？
        //入选者
        List<SkillRegister> skillRegisters = worthServiceprovider.getSkillRegisterService().getStatusSkillRegister(id, 1);
        MerchantOrderInfoService merchantOrderInfoService = new MerchantOrderInfoService();
        MerchantManagerService merchantManagerService = new MerchantManagerService();
        if (skillRegisters != null) {
            for (SkillRegister skillRegister : skillRegisters) {
                //根据预约成功者的id  获取订单
                SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectByRegisterId(skillRegister.getId());
                message = skill.getSkill() + "技能预约成功，时间" + skillOrder.getStartAt() + "到" + skillOrder.getEndAt() + "，地点" + skillOrder.getAddress() + "，人数" + skillOrder.getAmount() + "人";
                //向发布者推送消息，每一条订单推送一条消息
                if (userId != null) {
                    wzCommonImHistoryAction.add(userId, userId, message, skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
                }
                //向入选者推送
                wzCommonImHistoryAction.add(userId, skillRegister.getUserId(), message, skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
//                MerchantOrderInfo merchantOrderInfo = merchantOrderInfoService.selectById(skillOrder.getCreateUserId()); //    !!!!
                List<String> orderIds = merchantOrderInfoService.getOrderIds(skillOrder.getCreateUserId(), OrderStatusEnum.OS_FINISH);
                for (String orderId : orderIds) {
                    MerchantOrderInfo merchantOrderInfo = merchantOrderInfoService.selectById(orderId);
                    List<MerchantManager> merchantManagers = merchantManagerService.getMerchantType(merchantOrderInfo.getMerchantId());
                    //商家业务主管推送（此处应做判断）
                    for (MerchantManager merchantManager : merchantManagers) {
                        if (merchantManager.getMerchantUserType().equals("业务主管")) {
                            wzCommonImHistoryAction.add(userId, merchantManager.getUserId(), message, skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
                        }
                    }
                }
            }
        }
    }

    /**
     * 发布者取消技能单时通知入选者
     *
     * @param userId 技能发布者id
     * @param reId   技能单id
     */
    public void sendMessageCancelOrder(String userId, String reId) {
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(reId);
        Skill skill = worthServiceprovider.getSkillService().queryByUserIdAndSkillId(skillRegister.getSkillId(), userId);
        User user = userAction.getUserService().selectById(userId);
        String message = "您预约的" + skill.getSkill() + "技能已经被发布者：" + user.getNickname() + "取消。";
        wzCommonImHistoryAction.add(userId, skillRegister.getUserId(), message, skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
    }

    /* 生成验证码 **/
    public SkillOrder publishGeneratCode(String reId, String orderId, Double lat, Double lon) {
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(reId);
        if (skillRegister.getStatus() >= 2) {
            throw new RuntimeException("订单已经失效");
        }
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        Double distance = (DistrictUtil.calcDistance(skillRegister.getLat().doubleValue(), skillRegister.getLon().doubleValue(), lat, lon));
//        boolean validationStatus = (distance * 1000) <= 300;
        boolean validationStatus = (distance * 1000) <= 100;

        logger.info("userGet.lat:" + lat + "userGet.lon:" + lon);
        logger.info("skillRegister.lat:" + skillRegister.getLat().doubleValue() + "skillRegister.lon:" + skillRegister.getLon().doubleValue());
        logger.info("distance:" + distance);

        if(!skillRegister.getPay()){
            throw new RuntimeException("待预约者支付完成后才可启动预约");
        }

        if(!validationStatus && !skill.getObj().equals(2)){
            throw new RuntimeException("你与预约距离超出100米，不能启动预约");
        }
//            if (!skillRegister.getPay()) {
//                throw new RuntimeException("待预约者支付完成后才可启动预约");
//            } else {
//                jobFuseAction.addJob(JobEnum.SKILL_CHECK_SUCCESS_JOB, orderId, orderId, "定时验证码、距离是否通过", new Date(new Date().getTime()+30*6000), AuthorEmailEnum.DAI_HO);
        return skillOrderAction.publishGeneratCode(reId, orderId);
//            }

    }

    /* 预约者取消预约 **/
    public boolean registerCancel(String id, String userId) {
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().queryByUserIdAndId(id, userId);
        if (skillRegister == null) {
            throw new RuntimeException("该用户没有预约此技能");
        }
        if (skillRegister.getStatus().equals(SkillRegisterStatus.CANCEL.status)) return false;
        skillRegister.setStatus(SkillRegisterStatus.CANCEL.status);
        skillRegister.setUpdateUserId(userId);
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        skill.setRegisterCount(skill.getRegisterCount() - 1);//预约人数-1
        boolean success =  worthServiceprovider.getSkillRegisterService().updateById(skillRegister) && worthServiceprovider.getSkillService().updateById(skill);
        if(success)
            wzCommonImHistoryAction.add("999",skill.getUserId(),"预约者\""+getNickNameByuserId(skillRegister.getUserId())+"\"已取消\""+skill.getSkill()+"\"技能的预约！",
                    skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
        return success;
    }

    /* 预约者付款 **/
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> registerPay(String registerId, String userId) {
        Integer count = worthServiceprovider.getSkillRegisterService().getCountByUserIdAndSkillId(registerId, userId);
        if (count != 1) {
            throw new RuntimeException("该用户没有预约此技能");
        }
        /* 根据预约Id获取预约的信息 **/
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(registerId);
        /* 根据技能Id获取预约的技能信息 **/
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        /* 根据预约Id和用户Id 获取支付回掉信息 **/
        skillRegister.setPay(true);
        skillRegister.setUpdateUserId(userId);
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(registerId);
        requestDto.setUserId(userId);
        requestDto.setType(FrozenTypeEnum.FTZ_SKILL);
        if (!walletFrozenAction.pay(requestDto)) {
            throw new RuntimeException("使用冻结金额异常");
        }
        skillRegister.setStatus(3);
        if (worthServiceprovider.getSkillRegisterService().updateById(skillRegister)) {
            Map<String, Object> map = new HashMap<>();
            /* 获取技能预约订单信息 */
            SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectByRegisterId(skillRegister.getId());
            UserInfoAndHeadImg registerInformation = userAction.getUserInfoAndHeadImg(userId);
            if (registerInformation != null) {
                map.put("registerInformation", registerInformation);
            }
            success(skillOrder);
            map.put("registerOrderDetail", skillOrder);
            // 付款成功后开启定时器，检测24小时后双方是否已经评价
            jobFuseAction.addJob(JobEnum.SKILL_CHECK_EVALUATE_JOB, skillOrder.getId(), skillOrder.getId(), "检测24小时之后是否评价", new Date(new Date().getTime() + 24l * 60 * 60 * 1000), AuthorEmailEnum.SHI_JIE);
            //系统推送给发布者和入选者
            wzCommonImHistoryAction.add("999",skill.getUserId(),"预约者\""+getNickNameByuserId(skillRegister.getUserId())+"\"的预约已完成，请在24小时内完成评论，未完成评论者将扣减 2 信用值！",
                    skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
            wzCommonImHistoryAction.add("999",skillRegister.getUserId(),"您的\""+skill.getSkill()+"\"技能预约已完成，请在24小时内完成评论，未完成评论者将扣减 2 信用值!",
                    skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
            return map;
        } else {
            throw new RuntimeException("更新技能预约状态失败");
        }
    }

    /* 预约者申请退款 **/
    @Transactional
    public Refund registerRefund(CreateRefundDto createRefundDto) throws Exception {   //预约单ID 和 预约单用户
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().getReById(createRefundDto.getId());
        Integer coun = worthServiceprovider.getRefundService().getRefund(createRefundDto.getId(), createRefundDto.getUserId());
        if (coun == 1)
            throw new RuntimeException("用户已经申请过退款");
        Integer count = worthServiceprovider.getSkillRegisterService().getCountByUserIdAndSkillId(skillRegister.getId(), skillRegister.getUserId());
        if (count != 1) {
            throw new RuntimeException("该用户不具有对此技能操作的权利");
        }
        if (skillRegister.getPay() == false) {
            throw new RuntimeException("该用户还未支付余额");
        }
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        Refund refund = refundAction.cre(createRefundDto.getDescription(), createRefundDto.getBail() == null ? new BigDecimal(0): createRefundDto.getBail(), createRefundDto.getPayWay()==null?1:createRefundDto.getPayWay(),
                createRefundDto.getId(), "SkillRegister", createRefundDto.getAmount(), createRefundDto.getUserId());
        // 退款请求发出后，同时发送信息给退款方
        if (refund != null) {
            wzCommonImHistoryAction.add(createRefundDto.getUserId(), skill.getUserId(), "技能预约者\""+getNickNameByuserId(createRefundDto.getUserId()) + "\"对\"" + skill.getSkill()
                    + "\"技能发起退款请求，请您在36小时给予处理，逾期将视作您同意退款，系统将自动完成退款流程", skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("refundId",refund.getId());
            paramMap.put("userId",skill.getUserId());//发布者id
            long nowTime = new Date().getTime();
            //定时器走起-----检测发布者申请退款36个小时后发布者是否处理
            jobFuseAction.addJob(JobEnum.SKILL_CHECK_REFUND_JOB,paramMap,refund.getId(),"检测发布者申请退款36个小时后发布者是否处理",new Date(nowTime+(36l*60*60*1000)),AuthorEmailEnum.SHI_JIE);
        }
        skillRegister.setStatus(4);
        worthServiceprovider.getSkillRegisterService().updateById(skillRegister);
        return refund;
    }


    /* 发布者拒绝预约者的退款申请 */
    public boolean publishRejectRefund(String refundId, String userId) {
        Refund fundId= worthServiceprovider.getRefundService().getByUserId(refundId);
//		SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().getRegListList(fundId.getRelatableId());//！！！！？？有毒-.-，传registerid,当用户id来查
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(fundId.getRelatableId());
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        int count = worthServiceprovider.getSkillService().getCountByUserIdAndSkillId(skillRegister.getSkillId(), userId);
        if (count != 1) {
            throw new RuntimeException("该用户不能进行退款申请操作");
        }
        boolean success = refundAction.reject(refundId, userId);
        if(success){
            //推送给预约者
            wzCommonImHistoryAction.add("999",skillRegister.getUserId(),"\""+skill.getSkill()+"\"的技能发起者拒绝了您的退款申请，现已进入仲裁流程，请耐心等待仲裁结果。",
                    skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
        }
        return success;
    }
    /* 发布者同意预约者的退款申请 **/
    @Transactional
    public boolean publishAcceptRefund(String refundId, String userId) throws Exception {
        Refund fundId = worthServiceprovider.getRefundService().getByUserId(refundId);
        if(fundId == null){
            throw new RuntimeException("该退款记录不存在");
        }
//        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().getRegListList(fundId.getRelatableId());//！！！！？？有毒-.-，传registerid,当用户id来查
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(fundId.getRelatableId());
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectByRegisterId(skillRegister.getId());
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        int count = worthServiceprovider.getSkillService().getCountByUserIdAndSkillId(skillRegister.getSkillId(), userId);
        if (count != 1) {
            throw new RuntimeException("该用户不能进行退款申请操作");
        }
        if(fundId.getStatus()> 0){
            throw new RuntimeException("退款失败，该订单已经处被理");
        }

        boolean success = refundAction.accept(refundId, userId);
        success(skillOrder);
        Refund refund = worthServiceprovider.getRefundService().selectById(refundId);
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        // 获取平台的收取的手续费比例
        BigDecimal sharedFee = getSharedFee();
//        billAddRequestDto.setAmount(BigDecimal.valueOf((refund.getAmount() / 100)).multiply(BigDecimal.ONE.subtract(sharedFee)));
        billAddRequestDto.setAmount(Money.CentToYuan(refund.getAmount()).getAmount().multiply(BigDecimal.ONE.subtract(sharedFee)));

        billAddRequestDto.setDescription("退回给预约者的费用");
        billAddRequestDto.setPayChannel(3);
        billAddRequestDto.setType(1);
        billAddRequestDto.setToUserId(skillRegister.getUserId());//预约者id
        Boolean addBill = walletBillClientAction.addBill("999", billAddRequestDto);
        if (!addBill) {
            throw new RuntimeException("退回给预约者的费用出现异常");
        }
        //判断是否全额退款，为0时全额退款，不需要插入数据库
        if(fundId.getBail()!= 0) {
            billAddRequestDto.setDescription("支付给发布者的费用");
//            billAddRequestDto.setAmount(BigDecimal.valueOf(refund.getBail() / 100));
            billAddRequestDto.setAmount(Money.CentToYuan(refund.getBail()).getAmount());
            billAddRequestDto.setPayChannel(3);
            billAddRequestDto.setType(1);
            billAddRequestDto.setToUserId(userId);//发布者id
            addBill = walletBillClientAction.addBill("999", billAddRequestDto);
            if (!addBill) {
                throw new RuntimeException("支付给发布者的费用出现异常");
            }
        }
        skillRegister.setStatus(SkillRegisterStatus.FAIL.status);
        boolean b = worthServiceprovider.getSkillRegisterService().updateById(skillRegister);
        if(!b){
            throw  new RuntimeException("修改订单状态失败");
        }
        //推送给预约者
        wzCommonImHistoryAction.add("999",skillRegister.getUserId(),"\""+skill.getSkill()+"\"的技能发起者已同意您的退款申请。",
                skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
        return success;
    }

    /* 是否评价 **/
    public void checkEvaluate(String skillOrderId) {
//        skillOrderFuseAction.checkEvaluate(skillOrderId);
        skillOrderFuseAction.checkComment2Skill(skillOrderId);
    }

    /* 预约（已接受/未接受/已失败）列表 **/
    public Map<String, Object> getUserPublishList(String skillId, String userId, Double lon, Double lat) throws Exception { //用户为发布者Id
        Map<String, Object> map = new HashMap<>();
        List<SkillDataDto> completed = new ArrayList<>();
        List<SkillDataDto> NoCompleted = new ArrayList<>();
        List<SkillDataDto> failed = new ArrayList<>();
        /* 获取技能详情 */
        Skill skill = worthServiceprovider.getSkillService().selectById(skillId);
        /* 获取技能已成交预约列表 */
        List<SkillRegister> skillRegisters = worthServiceprovider.getSkillRegisterService().getSkillRegisterList(skillId);
        /* 获取所有预约的用户信息*/
        for (SkillRegister skillRegister : skillRegisters) {
            UserSynopsisData userSynopsisData = userAction.getUserSynopsisData(skillRegister.getUserId(), lon, lat, userId);
            SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().getOrderListByregisterId(skillRegister.getId());
            if (skillRegister.getStatus() == 0) {
                NoCompleted.add(getMergeCompleted(skill, skillRegister, userSynopsisData, skillOrder, lon, lat));
            }
            if (skillRegister.getStatus() == 1 || skillRegister.getStatus() == 4) {
                completed.add(getMergeCompleted(skill, skillRegister, userSynopsisData, skillOrder, lon, lat));
            }
            //20180824 从register==2,改为register==2,oreder=2,order==4
            if (skillRegister.getStatus() == 2) {
                failed.add(getMergeCompleted(skill, skillRegister, userSynopsisData, skillOrder, lon, lat));
            }
            if (skillOrder != null) {
                if (skillOrder.getStatus() == 2 || skillOrder.getStatus() == 4) {
                    if (!failed.contains(getMergeCompleted(skill, skillRegister, userSynopsisData, skillOrder, lon, lat))) {
                        failed.add(getMergeCompleted(skill, skillRegister, userSynopsisData, skillOrder, lon, lat));
                    }
                }
            }
        }
        map.put("completed", completed);
        map.put("NoCompleted", NoCompleted);
        map.put("failed", failed);//失败的列表
        return map;
    }

    /* 已成交列表 **/
    public Map<String, Object> getUserComPublishList(String skillId, String userId, BigDecimal lon, BigDecimal lat) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<SkillDataDto> com = new ArrayList<>();
        /* 获取技能详情 */
        Skill skill = worthServiceprovider.getSkillService().selectById(skillId);
        /* 获取技能已成交预约列表 */
        List<SkillRegister> skillRegisters = worthServiceprovider.getSkillRegisterService().getStatusSkillRegister(skillId, 3);
        /* 获取所有已成交的用户信息*/
        for (SkillRegister skillRegister : skillRegisters) {
            SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().getOrderListByregisterId(skillRegister.getId());
            //筛选skillOrder的status 为3的数据
            if (skillOrder.getStatus() == SkillOrderStatus.SUCCESS.status) {
                UserSynopsisData userSynopsisData = userAction.getUserSynopsisData(skillRegister.getUserId(), lon.doubleValue(), lat.doubleValue(), userId);
                com.add(getMergeCompleted(skill, skillRegister, userSynopsisData, skillOrder, lon.doubleValue(), lat.doubleValue()));
            }
        }
        map.put("com", com);
        return map;
    }

    /* 合并已成交的数据 */
    private SkillDataDto getMergeCompleted(Skill skill, SkillRegister skillRegister, UserSynopsisData userSynopsisData, SkillOrder skillOrder, Double lon, Double lat) throws Exception {
        SkillDataDto skillDataDto = new SkillDataDto();
        if (skill == null || skillRegister == null || userSynopsisData == null) {
            throw new Exception("查找已交易数据失败");
        }
        /* 获取退款信息表*/
        Integer ifReFund = worthServiceprovider.getRefundService().getRefund(skillRegister.getId(), skillRegister.getUserId());
        Refund refund = new Refund();
        if (ifReFund != 0) {
            refund = worthServiceprovider.getRefundService().getRefundId(skillRegister.getId(), skillRegister.getUserId());
        }
        CommonEvaluate commonEvaluate = new CommonEvaluate();
        commonEvaluate.setTypeId(skillRegister.getId());
        commonEvaluate.setFromUserId(skillRegister.getUserId());
        commonEvaluate.setEvaluateType(EvaluateTypeEnum.SKILL_EVALUATE.getValue());
        skillDataDto.setComments(evaluateAction.getCount(commonEvaluate) != 0);
        VoPoConverter.copyProperties(skill, skillDataDto);
        VoPoConverter.copyProperties(userSynopsisData, skillDataDto);
        VoPoConverter.copyProperties(skillRegister, skillDataDto);
        skillDataDto.setReCreateTime(skillRegister.getStartAt());
        skillDataDto.setSkilCreateTime(skillRegister.getCreateTime());
        skillDataDto.setReId(skillRegister.getUserId());
        skillDataDto.setDescription(skillRegister.getDescription());
        Money money = Money.CentToYuan(skillRegister.getAmount());
        skillDataDto.setAmount(money.getAmount());
        if (skillOrder != null) {
            skillDataDto.setOrderStu(skillOrder.getStatus());
            skillDataDto.setOrderId(skillOrder.getId());
            skillDataDto.setCode(skillOrder.getCode());
        }
        skillDataDto.setRefundId(refund.getId());
        skillDataDto.setRefund(refund);
        skillDataDto.setReDistance(DistrictUtil.calcDistance(skillRegister.getLat(), skillRegister.getLon(), userSynopsisData.getLat(), userSynopsisData.getLon()));
        skillDataDto.setSkDistance(DistrictUtil.calcDistance(skillRegister.getLat().doubleValue(), skillRegister.getLon().doubleValue(), lat, lon));
        return skillDataDto;
    }

    /* 我预约的技能 **/
    public Map<String, Object> getRegisterList(CommonPageDto commonPageDto, BigDecimal lat, BigDecimal lon) {
        Map<String, Object> map = new HashMap<>();   //返回
        List<String> skillIds = worthServiceprovider.getSkillRegisterService().getSkillIds(commonPageDto.getUserId(), SkillRegisterStatus.CANCEL);
        List<SkillDataListDto> list = new ArrayList();
        if (skillIds != null && skillIds.size() > 0) {
            Page page = new Page(commonPageDto.getCurrentPage(), commonPageDto.getSize());
            List<Skill> skillList = worthServiceprovider.getSkillService().queryByIds(skillIds, page);
            list = skillListToSkillDataListDto(skillList, lat, lon);


        }
        map.put("list", list);
        return map;
    }

    /* 技能列表转为技能dto列表 **/
    private List<SkillDataListDto> skillListToSkillDataListDto(List<Skill> skillList, BigDecimal lat, BigDecimal lon) {
        List<SkillDataListDto> list = new ArrayList();
        if (skillList != null) {
            skillList.forEach(skill -> {
                list.add(skillToSkillDataListDto(skill, lat, lon));
            });
            //排序,时间从新到旧
            Collections.sort(list, new Comparator<SkillDataListDto>() {
                @Override
                public int compare(SkillDataListDto o1, SkillDataListDto o2) {
                    long date1 = o1.getCreateTime().getTime();
                    long date2 = o2.getCreateTime().getTime();
                    if (date1 < date2)
                        return 1;
                    else if (date1 > date2)
                        return -1;
                    else
                        return 0;
                }
            });
        }

        return list;
    }


    /* 补全我预约的技能数据 **/
    private SkillDataListDto skillToSkillDataListDto(Skill skill, BigDecimal lat, BigDecimal lon) {
        SkillDataListDto skillDataListDto = new SkillDataListDto();
        UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg(skill.getUserId());
        if (userInfoAndHeadImg == null) {
            throw new RuntimeException("此用户不存在");
        }
        VoPoConverter.copyProperties(userInfoAndHeadImg, skillDataListDto);
        VoPoConverter.copyProperties(skill, skillDataListDto);
        //我到-技能距离
        Double wodistance = DistrictUtil.calcDistance(skill.getLat(), skill.getLon(), lat, lon);
        skillDataListDto.setWdistance(wodistance);
        Money money = new Money();
        //处理图片
        skillDataListDto.setSkillImagesUrl(addImgUrlPreUtil.addImgUrlPres(skill.getSkillImagesUrl(), AliyunBucketType.ActivityBucket));
        skillDataListDto.setSkillDetailImagesUrl(addImgUrlPreUtil.addImgUrlPres(skill.getSkillDetailImagesUrl(), AliyunBucketType.ActivityBucket));
        skillDataListDto.setAmount(money.CentToYuan(skill.getAmount()).getAmount());
        return skillDataListDto;
    }

    /* 我发布的技能 **/
    public Map<String, Object> getPublishList(BigDecimal lat, BigDecimal lon, String userId, Page<Skill> page) {
        Map<String, Object> map = new HashMap<>();
        List<Skill> skillList = worthServiceprovider.getSkillService().getUserSkillListTwo(userId, page);     //获取用户所有发布技能
        if (skillList == null)
            throw new RuntimeException("用户没有发布技能");
        map.put("list", skillListToSkillDataListDto(skillList, lat, lon));
        return map;
    }

    /* 预约单详情 **/
    public Map<String, Object> getReList(String reId, BigDecimal lat, BigDecimal lon, String skillId) {
        Map<String, Object> map = new HashMap<>();
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().getReByUserIdAndSkillId(reId, skillId);
        Skill skill = worthServiceprovider.getSkillService().selectById(skillId);
        List<SkillRegisterListDto> com = new ArrayList<>();
        SkillRegisterListDto skillRegisterListDto = new SkillRegisterListDto();
        VoPoConverter.copyProperties(skillRegister, skillRegisterListDto);
        //预约-预约者距离
        Double reDistance = DistrictUtil.calcDistance(skillRegister.getLat(), skillRegister.getLon(), lat, lon);
        skillRegisterListDto.setReDistance(reDistance);
        //发布-预约距离
        Double skDistance = DistrictUtil.calcDistance(skill.getLat(), skill.getLon(), lat, lon);
        skillRegisterListDto.setSkDistance(skDistance);
        skillRegisterListDto.setSkilCreateTime(skillRegister.getStartAt());  //技能时间
        skillRegisterListDto.setAmount(Money.CentToYuan(skill.getAmount()).getAmount());
        skillRegisterListDto.setBail(Money.CentToYuan(skillRegister.getBail()).getAmount());
        skillRegisterListDto.setFee(Money.CentToYuan(skillRegister.getFee()).getAmount());
        skillRegisterListDto.setFee(Money.CentToYuan(skillRegister.getFee()).getAmount());
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().getOrderByUserId(skillRegister.getId());
        if (skillOrder != null) {
            skillRegisterListDto.setOrStatus(skillOrder.getStatus());
            skillRegisterListDto.setCode(skillOrder.getCode());
        }
        //判断是否在退款中
        if(skillRegister.getStatus().equals(4)){
            //是否存在记录
            Integer refundCount = worthServiceprovider.getRefundService().getRefund(skillRegister.getId(), skillRegister.getUserId());
            Refund refund = new Refund();
            if(refundCount != 0){
                refund = worthServiceprovider.getRefundService().getRefundId(skillRegister.getId(), skillRegister.getUserId());
            }
            skillRegisterListDto.setRefund(refund);
        }
        com.add(skillRegisterListDto);
        map.put("com", com);
        return map;
    }

    /* 技能详情 **/
    public Map<String, Object> getDtail(String userId, String id, BigDecimal lat, BigDecimal lon) throws Exception {
        SkillDetailDto skillDetailDto = new SkillDetailDto();
        Map<String, Object> map = new HashMap<>();
        Skill skill = skillAction.selectById(id);
        if (skill == null) {
            throw new RuntimeException("技能可能已经注销");
        }
        UserSynopsisData userInfoAndHeadImg = userAction.getUserSynopsisData(skill.getUserId(), lon.doubleValue(), lat.doubleValue(), userId);
        skillDetailDto.setUserNumber(userInfoAndHeadImg.getUserNumber());
        skillDetailDto.setNickname(userInfoAndHeadImg.getNickName());
        skillDetailDto.setBothDistance(userInfoAndHeadImg.getDistance());
        skillDetailDto.setSkillImagesUrl(skill.getSkillImagesUrl());
        VoPoConverter.copyProperties(skill, skillDetailDto);
        //当前-技能距离
        Double skDistance = DistrictUtil.calcDistance(skill.getLat(), skill.getLon(), lat, lon);
        skillDetailDto.setSkDistance(skDistance);
        skillDetailDto.setSkillId(skill.getId());
        skillDetailDto.setDescription(skill.getDescription());
        int count = worthServiceprovider.getSkillRegisterService().getBySkillId(skill.getId(), null, SkillRegisterStatus.CANCEL);
        skillDetailDto.setReCount(count);
        skillDetailDto.setCoCount(skill.getSuccessCount());
        Money money = Money.CentToYuan(skill.getAmount());
        skillDetailDto.setAmount(money.getAmount());
        skillDetailDto.setSkillImagesUrl(addImgUrlPreUtil.addImgUrlPres(skill.getSkillImagesUrl(), AliyunBucketType.ActivityBucket));
        skillDetailDto.setSkillDetailImagesUrl(addImgUrlPreUtil.addImgUrlPres(skill.getSkillDetailImagesUrl(), AliyunBucketType.ActivityBucket));
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().getReByUserIdAndSkillIdList(userId, id);
        if (skillRegister != null) {
            //当前-预约者距离
            if (skillRegister.getStatus() >= 2) {
                skillDetailDto.setIsRegister(true);
            } else {
                skillDetailDto.setIsRegister(false);
            }
            Double reDistance = DistrictUtil.calcDistance(skillRegister.getLat(), skillRegister.getLon(), lat, lon);
            skillDetailDto.setReId(skillRegister.getId());
            skillDetailDto.setReDistance(reDistance);
            skillDetailDto.setAnonymity(skillRegister.getAnonymity());
        } else {
            skillDetailDto.setIsRegister(null);
        }
        map.put("skillDetailDto", skillDetailDto);
        return map;
    }

    /* 备注（定时取消预约）**/
    @Transactional(rollbackFor = Exception.class)
    public boolean checkOrderStart(String skillOrderId) {
        boolean success = true;
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        Integer status = skillOrder.getStatus();
        if (status.equals(SkillOrderStatus.INIT.status) || status.equals(SkillOrderStatus.CANCEL.status)) {
            //TODO 扣除信用值
            Skill skill = skillAction.selectById(skillRegister.getSkillId());
            User user = userAction.getUserService().selectById(skill.getUserId());
            user.setCredit(user.getCredit() - 5);
            boolean b = user.updateById();
            if (b)
                settlementAction.settlementCredit("Skill0Order", skillOrderId, skill.getUserId(), -5);//只增加记录，不扣除积分
            Boolean removeJob = jobFuseAction.removeJob(JobEnum.SKILL_CHECK_ORDER_JOB, skillOrderId, skillOrderId, "定时取消预约");
            if (removeJob == null)
                removeJob = true;
            if (removeJob) {
                Boolean flag = jobFuseAction.addJob(JobEnum.SKILL_CHECK_ORDER_JOB, skillOrderId, skillOrderId, "定时取消预约", new Date(skillRegister.getStartAt().getTime() + 24l * 3600 * 1000), AuthorEmailEnum.DAI_HO);
                if (flag == false) {
                    success = false;
                }
            }
        }
        return success;
    }

    //定时任务：未接受或预约者设定的时间开始后，发布者均未响应，则预约失败
    @Transactional(rollbackFor = Exception.class)
    public boolean checkSkillRegisterAccept(String skillRegisterId) throws Exception {
        boolean success = false;
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());

        //public Boolean removeJob(JobEnum jobEnum,String typeId,String typeName,String param){
        //remove有时候返回false不就没添加定时器上去了么，所以注释掉removejob部分
//        Boolean removeJob = jobFuseAction.removeJob(JobEnum.SKILL_CHECK_REGISTER_JOB, skillDingDto.getUserId(), skillDingDto.getUserId(), "技能发布者接受预约超时");
//        if (removeJob == null)
//            removeJob = true;
//        if (removeJob) {
//            Boolean flag = jobFuseAction.addJob(JobEnum.SKILL_CHECK_REGISTER_JOB, skillDingDto, skillDingDto.getUserId(),
//                    "技能发布者接受预约超时", new Date(skillRegister.getStartAt().getTime() + 24l * 3600 * 1000), AuthorEmailEnum.DAI_HO);
//            if (flag == true) {
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectByRegisterId(skillRegister.getId());
        if (skillOrder == null) {
            //修改预约表的状态为已拒绝2，之前为已过期3
            success = worthServiceprovider.getSkillRegisterService().refust(skillRegister.getId());
            Boolean add = wzCommonImHistoryAction.add(skill.getUserId(), skillRegister.getUserId(), "您的\"" + skill.getSkill() + "\"技能预约已被拒绝！", skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);

            //通知入选者
            if (!success) {
                throw new RuntimeException("设置已拒绝失败");
            }

            //因为要等发布者接受才托管预约费用，所以不需要释放预约费用
//                    success = settlementAction.settlementAmountRightNow("技能发布者接受预约超时，释放预约费用", "SkillRegister", skillDingDto.getSkillRegisterId(),
//                            skillRegister.getUserId(), skillDingDto.getBail());
//                    if (!success) {
//                        throw new RuntimeException("立即结算失败");
//                    }
        }
//            }
//        }
        return success;
    }

    /**
     * 技能发布者取消技能单，释放费用
     *
     * @param skillDingDto
     * @return
     */
    public boolean publishCancleRO(SkillDingDto skillDingDto) throws Exception {
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillDingDto.getSkillRegisterId());
        if(skillRegister == null){
            throw new RuntimeException("该SkillRegisterId不存在");
        }
//        boolean success = settlementAction.settlementAmountRightNow("技能发布者取消技能单，释放预约费用", "SkillRegister", skillDingDto.getSkillRegisterId(), skillRegister.getUserId(), skillDingDto.getBail());
//        boolean success = returnOrGiveCost(skillRegister, "repay");
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(skillRegister.getId());
        requestDto.setUserId(skillRegister.getUserId());
        requestDto.setType(FrozenTypeEnum.FTZ_SKILL);
        if(!walletFrozenAction.repeal(requestDto)){
            throw new RuntimeException("回退失败");
        }

        settlementAction.settlementAmountRightNow("技能发布者取消技能单，释放预约费用", "SkillRegister", skillDingDto.getSkillRegisterId(), skillRegister.getUserId(), skillDingDto.getBail());

        return true;
    }

    /* 发布者拒绝预约**/
    @Transactional(rollbackFor = Exception.class)
    public boolean publishRefuse(String id, String userId, String skillRegisterId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        //判断该用户是否有这个技能
        int count = worthServiceprovider.getSkillService().getCountByUserIdAndSkillId(id, userId);
        if (count != 1) {
            throw new RuntimeException("该用户没有此技能");
        }
        boolean success = false;
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
        boolean registerStatus = skillRegister.getStatus().equals(SkillRegisterStatus.REGISTERED.status);
        if (!registerStatus) {
            throw new RuntimeException("只能拒绝待入选状态的预约单");
        }
        try {
            Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
//            SkillRegister skillRe = worthServiceprovider.getSkillRegisterService().getReByUserIdAndSkillId(skillRegisterId, skill.getId());
            success = true;
            skillRegister.setStatus(2);
//            skillRegister.setDeleted(1);
//            skillRe.setStatus(2);
            worthServiceprovider.getSkillRegisterService().updateById(skillRegister);
            //通知入选者
            wzCommonImHistoryAction.add(skill.getUserId(),skillRegister.getUserId(),"您的\""+skill.getSkill()+"\"技能预约已被拒绝！",skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
        } catch (Exception e) {
            e.getMessage();
        }
        return success;
    }



    /* 获取平台收取的手续费比例 **/
    public BigDecimal getSharedFee() throws Exception {
        CostSettingResponseDto costSettingResponseDto = costSettingClientAction.feignQuery();
        if (costSettingResponseDto == null) {
            logger.error("费用设置获取失败");
            throw new RuntimeException();
        }
        BigDecimal sharedFee = costSettingResponseDto.getSharedFee(); // 记录手续费比例
        // 手续费比例大于或等于 1 或者 手续费比例小于 0，说明后台设置出现问题
        boolean flag_sharedFee = sharedFee.compareTo(BigDecimal.ONE) == 1 || sharedFee.compareTo(BigDecimal.ONE) == 0
                || sharedFee.compareTo(BigDecimal.ZERO) == -1;
        if (flag_sharedFee) {
            logger.error("后台设置网值的手续费比例不合法：" + sharedFee);
            throw new RuntimeException();
        }
        return sharedFee;
    }

    /* 定时任务：验证码、距离是否通过 **/
    @Transactional
    public void checkSuccess(String skillOrderId) throws Exception {
        checkSuccessJu(skillOrderId);
    }

    /* 拼接--定时任务：验证码、距离是否通过 **/
    @Transactional
    public void checkSuccessJu(String skillOrderId) throws Exception {
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        if(skillRegister.getStatus()> 1 || skillOrder.getStatus() > 1){
            logger.error("该订单已经结束或者过期");
            return;
        }
        boolean orderSuccess = skillRegister.getValidation() && skillRegister.getValidationStatus() && skillOrder.getValidationStatus();
        //remove??
//        Boolean removeJob = jobFuseAction.removeJob(JobEnum.SKILL_CHECK_SUCCESS_JOB, skillOrderId, skillOrderId, "定时验证码、距离是否通过");
//        if (removeJob == null)
//            removeJob = true;
//        if (removeJob) {
        //add??
//            Boolean flag = jobFuseAction.addJob(JobEnum.SKILL_CHECK_SUCCESS_JOB, skillOrderId, skillOrderId, "定时验证码、距离是否通过", new Date(skillRegister.getStartAt().getTime() + 24l * 3600 * 1000), AuthorEmailEnum.DAI_HO);
        if (orderSuccess) {
            success(skillOrder);
        } else {
//                if (flag == true) {
            fail(skillOrder);   //预约失败
            failSkillRegisterStatus(skillRegister);//预约单过期
            Skill skill = skillAction.selectById(skillRegister.getSkillId());
            if (skillOrder.getValidationStatus() && !skillRegister.getValidationStatus()) {
//                Boolean succcess = returnOrGiveCost(skillRegister, "pay");
                FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
                requestDto.setTypeId(skillRegister.getId());
                requestDto.setUserId(skillRegister.getUserId());
                requestDto.setType(FrozenTypeEnum.FTZ_SKILL);
                if(!walletFrozenAction.pay(requestDto)){
                    throw new RuntimeException("回退失败");
                }
                settlementAction.settlementAmountRightNow("发布者到场，入选者缺席，立即结算报酬给发布者","SkillRegister",
                        skillRegister.getId(),skill.getUserId(),new BigDecimal(skillRegister.getBail()));
                //系统to发布者
                Boolean add = wzCommonImHistoryAction.add("999", skill.getUserId(), "\"" + skill.getSkill() + "\"技能预约者\"" + getNickNameByuserId(skillRegister.getUserId()) + "\"未输入您的验证码，系统判断" + getSexStringByUserId(skillRegister.getUserId()) + "未出席，该预约已失效，您的报酬已结算！",
                        skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
                //系统to入选者
                Boolean add1 = wzCommonImHistoryAction.add("999", skillRegister.getUserId(), "您超时未输入验证码，系统判断您未出席，\"" + skill.getSkill() + "\"技能预约已失效！",
                        skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);

                logger.info("结算给发布者");
            }
            if (!skillOrder.getValidationStatus() && skillRegister.getValidationStatus()) {
                if (skillRegister.getBail().doubleValue() > 0) {
//                    Boolean success = returnOrGiveCost(skillRegister, "repay");
                    FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
                    requestDto.setTypeId(skillRegister.getId());
                    requestDto.setUserId(skillRegister.getUserId());
                    requestDto.setType(FrozenTypeEnum.FTZ_SKILL);
                    if(!walletFrozenAction.repeal(requestDto)){
                        throw new RuntimeException("回退失败");
                    }
                    settlementAction.settlementAmountRightNow("发布者缺席，入选者到场，释放托管费用","SkillRegister",
                            skillRegister.getId(),skillRegister.getUserId(),new BigDecimal(skillRegister.getBail()));
                    logger.info("返还给入选者");
                }
            }
            if (!skillOrder.getValidationStatus()) {
                updateUserCredit(skill.getUserId(), -5);//扣除发布者信用分
                settlementAction.settlementCredit("SkillOrder", skillOrderId, skill.getUserId(), -5);//信用分-5
            }
            if (!skillRegister.getValidationStatus()) {
                updateUserCredit(skillRegister.getUserId(), -2);//扣除入选者信用分
                settlementAction.settlementCredit("SkillOrder", skillOrderId, skillRegister.getUserId(), -2);//信用分-2
            }
//                }
        }
//        }
    }

    /**
     * 定时任务，预约时间到了，预约者是否托管预约费用、发布者是否启动预约
     *
     * @param skillOrderId 订单id
     * @throws Exception
     */
    @Transactional
    public void checkRegister(String skillOrderId) throws Exception {
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        if(skillOrder.getStatus()>1){
            logger.error("该订单已经结束或者过期");
            return;
        }
        //判断是否已经支付托管费用，已经支付判断是否启动预约，否则订单也失败
        if(skillRegister.getPay()) {
            //如果已经启动预约了，启动30分钟验证状态的定时器
            if (skillOrder.getValidationStatus()) {
                jobFuseAction.addJob(JobEnum.SKILL_CHECK_SUCCESS_JOB, skillOrderId, skillOrderId, "定时验证码、距离是否通过", new Date(skillRegister.getStartAt().getTime() + 1800 * 1000), AuthorEmailEnum.SHI_JIE);

            } else {
                //否则就订单自动失败
                logger.error("发布者未启动预约");
                publisherNotStartOrder(skillOrder);
            }
        }else{
            //未托管费用，订单失败
            logger.info("预约者未支付托管费用");
            registerNotDeposit(skillOrderId);
        }
    }


    /**
     * 预约时间到了发布者尚未启动预约，扣除信用值和释放预约费用
     *
     * @param skillOrder 技能订单id
     */
    @Transactional
    public void publisherNotStartOrder(SkillOrder skillOrder) throws Exception {
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        try {

            logger.info("未启动预约");
            fail(skillOrder);
            failSkillRegisterStatus(skillRegister);
//            Boolean success = returnOrGiveCost(skillRegister, "repay");
            FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
            requestDto.setTypeId(skillRegister.getId());
            requestDto.setUserId(skillRegister.getUserId());
            requestDto.setType(FrozenTypeEnum.FTZ_SKILL);
            if(!walletFrozenAction.repeal(requestDto)){
                logger.error("回退失败");
                throw new RuntimeException("回退失败");
            }
            settlementAction.settlementAmountRightNow("技能发布者在预约时间开始后尚未启动预约","SkillRegister",
                    skillRegister.getId(),skillRegister.getUserId(),new BigDecimal(skillRegister.getBail()));
            logger.info("技能发布者在预约时间开始后尚未启动预约");
            updateUserCredit(skill.getUserId(), -5);//扣除发布者信用分
            settlementAction.settlementCredit("SkillOrder", skillOrder.getId(), skill.getUserId(), -5);//信用分-5
//            settlementFuseAction.start();
            //系统推送信息给发布者和入选者
            wzCommonImHistoryAction.add("999",skill.getUserId(),"您超时尚未启动\""+skill.getSkill()+"\"技能预约，该预约已失效！",
                    skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
            wzCommonImHistoryAction.add("999",skillRegister.getUserId(),"由于\""+getNickNameByuserId(skill.getUserId())+"\"技能发起者，超时未启动您的预约，您的预约已失效，所托管费用已退回！",
                    skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);

            logger.info("扣除发布者信用分成功");
        } catch (Exception e) {
            logger.info("扣除发布者信用分失败");
            throw e;
        }
    }

    /**
     * 超时未托管费用，取消订单
     * @param skillOrderId
     */
    @Transactional
    public void registerNotDeposit(String skillOrderId){
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        try {
            logger.info("未托管费用");
            fail(skillOrder);
            failSkillRegisterStatus(skillRegister);
            //系统推送给发布者
            wzCommonImHistoryAction.add("999",skill.getUserId(),"\""+skill.getSkill()+"\"技能的的预约者\""+getNickNameByuserId(skillRegister.getUserId())+"\"超时未托管费用，系统已自动取消该预约！",
                    skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
            //系统推送给入选者
            wzCommonImHistoryAction.add("999",skillRegister.getUserId(),"由于您超时未托管\""+skill.getSkill()+"\"技能费用，系统已自动取消该预约！",
                    skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);


        }catch (Exception e){
            logger.error("取消订单出现异常",e.getMessage());
        }

    }


    /**
     * 有问题，不要调用
     * @param skillRegister
     * @param returnType
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean returnOrGiveCost(SkillRegister skillRegister, String returnType) throws Exception {
        boolean flag = true;
        if(StringUtils.isBlank(returnType)){
            throw new RuntimeException("出现未知错误");
        }
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(skillRegister.getId());
        requestDto.setUserId(skillRegister.getUserId());
        requestDto.setType(FrozenTypeEnum.FTZ_SKILL);
        if(returnType.equals("pay")) {
            if (!walletFrozenAction.pay(requestDto)) {
                flag = false;
                throw new RuntimeException("结算给发布者出现异常");
            }

        }else if(returnType.equals("repay")){
            if(!walletFrozenAction.repeal(requestDto)){
                flag = false;
                throw new RuntimeException("技能预约托管费用返还给入选者出现异常");
            }
        }

        return flag;
    }



    /* 更新技能预约订单状态 */
    public Boolean success(SkillOrder skillOrder) {
        skillOrder.setStatus(SkillOrderStatus.SUCCESS.status);
        return worthServiceprovider.getSkillOrderService().updateById(skillOrder);
    }


    public void fail(SkillOrder skillOrder) {
        skillOrder.setStatus(SkillOrderStatus.FAIL.status);
        worthServiceprovider.getSkillOrderService().updateById(skillOrder);
    }

    /*更新预约单状态为已过期*/
    public void failSkillRegisterStatus(SkillRegister skillRegister) {
        skillRegister.setStatus(SkillRegisterStatus.FAIL.status);
        worthServiceprovider.getSkillRegisterService().updateById(skillRegister);
    }

    /**
     * 修改用户信用值
     *
     * @param userId 用户id
     * @param credit 信用值，正数为增加，负数为扣除
     * @return
     */
    public boolean updateUserCredit(String userId, Integer credit) {
        User user = userAction.getUserService().selectById(userId);
        user.setCredit(user.getCredit() + credit);
        boolean b = userAction.getUserService().updateById(user);
        return b;
    }

    /**
     * 查询技能列表
     *
     * @param
     * @return
     * @since ChenQian
     */
    public List<SkillListDto> list(SkillSearchDto skillSearchDto) {
        //构建技能搜索条件、排序
        List<SkillSearchResponse> skillSearchResponseList = skillSearchService.querySkills(getSkillSearchQuery(skillSearchDto));
        List<SkillListDto> skillListDtoList = new ArrayList<>();
        for (SkillSearchResponse skillSearchResponse : skillSearchResponseList) {
            skillListDtoList.add(createSkillListDto(skillSearchResponse));
        }
        return skillListDtoList;
    }

    /**
     * 技能搜索内容处理
     *
     * @param skillSearchResponse
     * @return SkillListDto
     * @since ChenQian
     */
    public SkillListDto createSkillListDto(SkillSearchResponse skillSearchResponse) {
        SkillListDto skillListDto = new SkillListDto();
        VoPoConverter.copyProperties(skillSearchResponse, skillListDto);
        List<String> skills = skillSearchResponse.getSkillLabels();
        if (skills != null && skills.size() > 0) {
            String title = skills.remove(0);
            for (String skill : skills) {
                title += "、" + skill;
            }
            skillListDto.setTitle(title);
        }

        if (StringUtils.isNotBlank(skillSearchResponse.getSkillImagesUrl())) {
            skillListDto.setImages(netEnergyFuseAction.updateImagesUrl(skillSearchResponse.getSkillImagesUrl()));
        }
        if (skillSearchResponse.getCreditSum() > 0) {
            skillListDto.setHoldCredit(true);
        } else {
            skillListDto.setHoldCredit(false);
        }
        if (skillSearchResponse.getCreateTime() != null) {
            skillListDto.setPublishTime(skillSearchResponse.getCreateTime());
        }
        if (skillSearchResponse.getCount() > 0) {
            Money money = new Money(skillSearchResponse.getAmount());
            skillListDto.setAmount(money.getAmount());
        }
        skillListDto.setAge(ComputeAgeUtils.getAgeByBirthday(skillSearchResponse.getBirthday()));
        return skillListDto;
    }

    /**
     * 构建技能搜索条件、排序
     *
     * @param skillSearchDto
     * @return SkillSearchQuery
     * @since ChenQian
     */
    public SkillSearchQuery getSkillSearchQuery(SkillSearchDto skillSearchDto) {
        SkillSearchQuery skillSearchQuery = new SkillSearchQuery();
        VoPoConverter.copyProperties(skillSearchDto, skillSearchQuery);
        System.out.println(skillSearchQuery.toString());
        skillSearchQuery.setCenterGeoPoint(new GeoPoint(skillSearchDto.getLat(), skillSearchDto.getLon()));
        skillSearchQuery.setPage(skillSearchDto.getCurrent(), skillSearchDto.getSize());

        /**
         * 排序方式
         * 0.最热（供不应求）：距离>信用>在线状态
         * 1.最新：最新>距离>在线状态
         * 2.最近：距离>信用>在线
         * 3.支持网信：是否发行网信>距离>在线状态
         * 4.信用：信用>距离>在线状态
         * 5.价格最低：价格最低>距离>在线状态
         * 6.报名人数多：报名人数最多>距离>在线状态
         * 7.成交量最多：成交量最多>距离>在线状态
         */
        switch (skillSearchDto.getSort()) {
            case 1:
                skillSearchQuery.addFristAscQueries(new LastAscQuery("createTime", false));
                skillSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
                break;
            case 3:
                skillSearchQuery.addFristAscQueries(new LastAscQuery("creditSum", false));
                skillSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
                break;
            case 4:
                skillSearchQuery.addFristAscQueries(new LastAscQuery("credit", false));
                skillSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
                break;
            case 5:
                skillSearchQuery.addFristAscQueries(new LastAscQuery("amount", true));
                skillSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
                break;
            case 6:
                skillSearchQuery.addFristAscQueries(new LastAscQuery("registerCount", false));
                skillSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
                break;
            case 7:
                skillSearchQuery.addFristAscQueries(new LastAscQuery("successCount", false));
                skillSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
            default:
                skillSearchQuery.addLastAscQuery(new LastAscQuery("credit", false));
                skillSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
                break;
        }
        return skillSearchQuery;
    }


    /**
     * 双方互相评价完毕或预约结束时间届满后24小时，预约结束
     */
    public boolean orderOver(String skillOrderId) {
        boolean flag = false;
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        long time = skillOrder.getEndAt().getTime() + 24 * 60 * 60 * 1000;
        if (skillOrderFuseAction.checkEvaluate(skillOrderId) || System.currentTimeMillis() > time) {
            skillOrder.setStatus(3);
            flag = true;
        }
        return flag;
    }

    /**
     * 使用post请求接口推送信息
     * @param type
     * @param paramId
     * @return
     * @throws Exception
     */
    public JSONObject callPushUrl(int type,String paramId) throws Exception{
        String path = "/wz/skill/pushTimeout/"+type+"/"+paramId;
        host += ":18088";
        String protocol = "http://";
        URL url = new URL(protocol+host + path);
        logger.info(host+path);
        Map<String,String> map = null;
        Map<String,String> header = new HashMap<>();
        HttpResponse httpResponse = HttpUtils.doPost(url.getProtocol()+"//"+url.getHost()+":"+url.getPort(), url.getPath(), header, null, map);
        HttpEntity entity = httpResponse.getEntity();
        InputStream content = entity.getContent();
        BufferedReader br=new BufferedReader(new InputStreamReader(content,"utf-8"));
        String str = br.readLine();
        JSONObject jsonObject = JSON.parseObject(str);
        logger.info(str);
        return jsonObject;
    }

    /**
     * 推送信息
     * @param type
     * @param paramId 0 发布者超时未接受预约 1 预约者超时未托管费用 2 发布者超时未启动预约 3 预约者超时未输入验证码
     * @return
     */
    public boolean pushTimeoutMessage(int type,String paramId){
        boolean success = false;
        switch (type){
            case 0:
                success = pushTimeoutRefuse(paramId);
                break;
            case 1:
                success = pushTimeoutTrusteeship(paramId);
                break;
            case 2:
                success = pushTimeouStartRegister(paramId);
                break;
            case 3:
                success = pushTimeoutInputCode(paramId);
        }
        return success;
    }

    /**
     * 推送发布者超时未接受预约的信息
     * @param skillRegisterId
     * @return
     */
    public boolean pushTimeoutRefuse(String skillRegisterId){
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        boolean success = wzCommonImHistoryAction.add(skill.getUserId(), skillRegister.getUserId(), "您的\"" + skill.getSkill() + "\"技能预约已被拒绝!", skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
        return success;
    }

    /**
     * 推送预约者未托管的信息
     * @param skillOrderId
     * @return
     */
    public boolean pushTimeoutTrusteeship(String skillOrderId){
        boolean success = false;
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        //系统to发布者
        Boolean add = wzCommonImHistoryAction.add("999", skill.getUserId(), "\"" + skill.getSkill() + "\"的技能预约者超时未托管费用，系统已自动取消预约!",
                skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
        //系统to预约者
        Boolean add1 = wzCommonImHistoryAction.add("999", skillRegister.getUserId(), "由于您超时未托管\"" + skill.getSkill() + "\"的技能费用，系统已自动取消预约!",
                skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
        return success = add && add1;

    }

    /**
     * 推送发布者未启动预约的信息
     * @param skillOrderId
     * @return
     */
    public boolean pushTimeouStartRegister(String skillOrderId){
        boolean success = false;
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        //系统to发起者
        Boolean add = wzCommonImHistoryAction.add("999", skill.getUserId(), "您超时未启动\"" + skill.getSkill() + "\"技能预约，该预约已失效!",
                skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
        //系统to入选者
        Boolean add1 = wzCommonImHistoryAction.add("999", skillRegister.getUserId(), "由于\"" + skill.getSkill() + "\"技能发起者超时未启动您的预约，您的预约已失效，所托管费用已退回!",
                skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
        return success = add && add1;
    }


    /**
     * 推送预约者未输入验证码信息
     * @param skillOrderId
     * @return
     */
    public boolean  pushTimeoutInputCode(String skillOrderId){
        boolean success = false;
        SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(skillOrderId);
        SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
        Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
        //系统to发布者
        Boolean add = wzCommonImHistoryAction.add("999", skill.getUserId(), "\"" + skill.getSkill() + "\"技能预约者\"" + getNickNameByuserId(skillRegister.getUserId()) + "\"未输入您的验证码，系统判断" + getSexStringByUserId(skillRegister.getUserId()) + "未出席，该预约已失效，您的报酬已结算!",
                skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
        //系统to入选者
        Boolean add1 = wzCommonImHistoryAction.add("999", skillRegister.getUserId(), "您超时未输入验证码，系统判断您未出席，\"" + skill.getSkill() + "\"技能预约已失效!",
                skill.getId(), MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_SKILLDETAIL, null);
        return success = add && add1;
    }
    //定时任务： 预约者发起退款申请后，36小时后发布者为处理，自动处理
    @Transactional
    public Boolean publishTimeoutNotHandleRefund(String json){
        boolean succ = true;
        JSONObject jsonObject = JSON.parseObject(json);
        String refundId = jsonObject.getString("refundId");
        String userId = jsonObject.getString("userId");
        Refund refund = worthServiceprovider.getRefundService().getByUserId(refundId);
        if(refund.getStatus() == 0){
            try {
                SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(refund.getRelatableId());
                SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectByRegisterId(skillRegister.getId());
                Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
                int count = worthServiceprovider.getSkillService().getCountByUserIdAndSkillId(skillRegister.getSkillId(), userId);
                if (count != 1) {
                    throw new RuntimeException("该用户不能进行退款申请操作");
                }

                boolean success = refundAction.timeoutAutoAccept(refundId, userId);
                if(!success){
                    throw new RuntimeException("修改退款记录状态失败");
                }
                success(skillOrder);
//                Refund refund = worthServiceprovider.getRefundService().selectById(refundId);
                BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
                // 获取平台的收取的手续费比例
                BigDecimal sharedFee = getSharedFee();
                billAddRequestDto.setAmount(Money.CentToYuan(refund.getAmount()).getAmount().multiply(BigDecimal.ONE.subtract(sharedFee)));

                billAddRequestDto.setDescription("退回给预约者的费用");
                billAddRequestDto.setPayChannel(3);
                billAddRequestDto.setType(1);
                billAddRequestDto.setToUserId(skillRegister.getUserId());//预约者id
                Boolean addBill = walletBillClientAction.addBill("999", billAddRequestDto);
                if (!addBill) {
                    throw new RuntimeException("退回给预约者的费用出现异常");
                }
                //判断是否全额退款，为0时全额退款，不需要插入数据库
                if(refund.getBail()!= 0) {
                    billAddRequestDto.setDescription("支付给发布者的费用");
                    billAddRequestDto.setAmount(Money.CentToYuan(refund.getBail()).getAmount());
                    billAddRequestDto.setPayChannel(3);
                    billAddRequestDto.setType(1);
                    billAddRequestDto.setToUserId(userId);//发布者id
                    addBill = walletBillClientAction.addBill("999", billAddRequestDto);
                    if (!addBill) {
                        throw new RuntimeException("支付给发布者的费用出现异常");
                    }
                }
                skillRegister.setStatus(SkillRegisterStatus.FAIL.status);
                boolean b = worthServiceprovider.getSkillRegisterService().updateById(skillRegister);
                if(!b){
                    throw  new RuntimeException("修改订单状态失败");
                }
                //推送给预约者
                wzCommonImHistoryAction.add("999",skillRegister.getUserId(),"\""+skill.getSkill()+"\"的技能发起者已同意您的退款申请。",
                        skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
                //告知发布者系统已自动处理退款
                wzCommonImHistoryAction.add("999",skillRegister.getUserId(),"由于您超过36个小时未处理由预约者\""+getNickNameByuserId(skillRegister.getUserId())+"\"发起的退款申请，系统已经自动处理为同意退款。",
                        skill.getId(),MessageTypeEnum.ACTIVITY_TYPE,PushMessageDocTypeEnum.WZ_SKILLDETAIL,null);
                succ = true;
            } catch (Exception e) {
                succ = false;
                throw new RuntimeException("定时任务超过36小时发布者未处理自动同意退款出现异常");
            }
        }

        return succ;
    }


    /**
     * 获得技能相关信息通过id
     * @param id
     * @return
     */
    public Map<String, Object> getSkillById(String id,Double lat,Double lon) {
        Map<String, Object> map = new HashMap<>();
        Skill skill = worthServiceprovider.getSkillService().selectById(id);
        //处理图片
        //获取用户的信息
        UserInfoAndHeadImg userData=null;
        if(StringUtils.isNotBlank(skill.getUserId())) {
            userData = userAction.getUserInfoAndHeadImg(skill.getUserId());
        }
        if (skill != null) {
            //处理图片
            skill.setSkillImagesUrl(addImgUrlPreUtil.addImgUrlPres(skill.getSkillImagesUrl(), AliyunBucketType.ActivityBucket));
            skill.setSkillDetailImagesUrl(addImgUrlPreUtil.addImgUrlPres(skill.getSkillDetailImagesUrl(), AliyunBucketType.ActivityBucket));
            //处理金额
            if (skill.getAmount() != null) {
                skill.setAmount(Money.CentToYuan(skill.getAmount()).getAmount().longValue());
            }
            //获得距离
        }
        map.put("list", skill);
        map.put("userData", userData);
        map.put("distance",DistrictUtil.calcDistance ( skill.getLat ().doubleValue (), skill.getLon ().doubleValue (), lat, lon ));
        return map;
    }

}
