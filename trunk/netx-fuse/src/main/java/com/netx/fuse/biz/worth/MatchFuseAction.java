package com.netx.fuse.biz.worth;

import com.netx.common.common.enums.*;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.enums.MobileSmsCode;
import com.netx.common.user.enums.PayTypeEnum;
import com.netx.common.user.enums.RegularExpressionEnum;
import com.netx.common.user.util.MobileMessage;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.common.wz.dto.matchEvent.*;
import com.netx.common.wz.dto.matchEvent.MatchAudienceDTO;
import com.netx.common.wz.dto.matchEvent.MatchTicketDTO;
import com.netx.common.wz.dto.matchEvent.MatchTicketPayDto;
import com.netx.common.wz.dto.matchEvent.MatchTicketPlanPayDto;
import com.netx.common.vo.common.FrozenAddRequestDto;

import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.fuse.client.ucenter.WalletForzenClientAction;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.model.MatchSearchResponse;
import com.netx.searchengine.query.MatchSearchQuery;
import com.netx.searchengine.service.MatchSearchService;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.ucenter.biz.common.WalletFrozenAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.WalletFrozenService;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.format.DateFormatUtils;
import com.netx.utils.randomCode.RandomUtil;
import com.netx.worth.biz.match.MatchApplyAction;
import com.netx.worth.biz.match.MatchCreateAction;
import com.netx.worth.enums.MatchMemberKind;
import com.netx.worth.enums.MatchOrganizerKind;
import com.netx.worth.enums.MatchStatusCode;
import com.netx.worth.model.*;
import com.netx.worth.service.*;
import com.netx.worth.vo.MatchEventSimpleVo;
import com.netx.worth.vo.MatchIndexVo;
import com.netx.worth.vo.MatchReviewVo;
import com.netx.worth.vo.UserForMatchVo;
import org.apache.commons.lang3.StringUtils;

import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 比赛交融模块
 * Created by Yawn on 2018/8/8 0008.
 */
@Service
public class MatchFuseAction {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    // 搜索服务
    @Autowired
    private MatchSearchService matchSearchService;

    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;

    @Autowired
    private MatchServiceProvider matchServiceProvider;

    @Autowired
    private MatchApplyAction matchApplyAction;

    @Autowired
    private JobFuseAction jobFuseAction;

    @Autowired
    private MatchCreateAction matchCreateAction;

    @Autowired
    private WalletForzenClientAction walletForzenClientAction;

    @Autowired
    private UserAction userAction;

    @Autowired
    private MobileMessage mobileMessage;
    /*
    解冻用的
     */
    @Autowired
    private WalletFrozenAction walletFrozenAction;
    /**
     * RedisInfoHolder
     */
    @Autowired
    private RedisInfoHolder redisInfoHolder;
    @Autowired
    private MerchantService merchantService;
    /**
     * 获得redis的方法
     * @return
     */
    private RedisCache redisCache;
    private RedisCache clientRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }


    /**
     * 定时任务：锁票
     * @param userId 用户id
     * @param token token
     * @param matchTicketPlanPayDto
     * @return
     */
    @Transactional
    public Map<String,String> getMacthAudienceId(String userId, String token, MatchTicketPlanPayDto matchTicketPlanPayDto){
        //购买的时间
        Date buyTicketTime=new Date();
        //购买的id
        String payId=matchApplyAction.getTicketCodeByToken(userId,token);
        String audienceId=matchApplyAction.updateTicketCountAndTicketAudience(matchTicketPlanPayDto,userId,false);
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isNoneBlank(payId)){
            //开启定时任务，未支付回滚
            jobFuseAction.addJob(JobEnum.MATCH_BUY_TICKET_ROLLBACK_JOB,audienceId,matchTicketPlanPayDto.getMatchTicketId(),"购票超时回滚票数",new Date(buyTicketTime.getTime()+900000),AuthorEmailEnum.DIAN_QV);
        }
        map.put("audienceId",audienceId);
        map.put("payId",payId);
        return  map;
    }
    /**
     * 报名
     * @param matchParticpantAllDto
     * @return
     */
    @Transactional
    public String addParticipantFreeUserTeam(MatchParticpantAllDto matchParticpantAllDto, String userId) {
        MatchParticipantDTO matchParticipantDTO=matchParticpantAllDto.getMatchParticipantDTO();
        List<MatchChildInfoDTO> matchChildInfoList=matchParticpantAllDto.getMatchChildInfoDTOList();
        if (matchParticipantDTO == null)
            throw new RuntimeException("报名信息为空");
        if (matchServiceProvider.getMatchParticipantService().getMatchParticipantIsHave(matchParticipantDTO.getMatchId(), matchParticipantDTO.getUserId())) {
            throw new RuntimeException("您已报名");
        }
        /*
        第一步，获取用户信息，队长报名
         */
        UserForMatchVo u = matchServiceProvider.getMatchParticipantService().getUserForMatchVo(userId);
        matchParticipantDTO.setSex(u.getSex());
        matchParticipantDTO.setBirthday(u.getBirthday());
        matchParticipantDTO.setUserName(u.getNickname());
        matchParticipantDTO.setHeadImagesUrl(u.getUrl());
        //计算报名人数是否饱和，默认先报名就有优先通过的权力,并插入默认入选者
        matchParticipantDTO=matchApplyAction.addAppearanceService(matchParticipantDTO);
        String id = matchServiceProvider.getMatchParticipantService().addParticipant(matchParticipantDTO,true,2);
        List<MatchRequirementDataDTO> requirementData = matchParticipantDTO.getImage_message();
        matchApplyAction.addImageData(requirementData, id, matchParticipantDTO,false);

        //添加各个照片资料
        /*
        第二步，构建队员的对应信息
         */
        /**
         * 免费的人直接报名直接邀请
         */
        for (MatchChildInfoDTO childInfo : matchChildInfoList) {
            if(org.apache.commons.lang.StringUtils.isNotBlank(childInfo.getUserId())) {
                UserForMatchVo userForMatchVo = matchServiceProvider.getMatchParticipantService().getUserForMatchVo(childInfo.getUserId());
                childInfo.setSex(userForMatchVo.getSex() != null ? userForMatchVo.getSex() : "");
                childInfo.setBirthday(userForMatchVo.getBirthday() != null ? userForMatchVo.getBirthday(): new Date());
                childInfo.setName(userForMatchVo.getNickname() != null ? userForMatchVo.getNickname(): "");
                childInfo.setImagesUrl(userForMatchVo.getUrl() != null ? userForMatchVo.getUrl(): "");
            }
            childInfo.setParticipantId(id);
            String teamMemberId=matchServiceProvider.getMatchChildInfoService().addChildrenInfoForUser(childInfo);
            if(StringUtils.isBlank(teamMemberId)) {
                throw new RuntimeException("填入.." + childInfo.getName() + "信息失败");
            }
            if(StringUtils.isNoneBlank(childInfo.getMobile())) {
                inviteTeamMember(childInfo.getMobile(),matchParticipantDTO.getMatchId(),userId,teamMemberId);
            }
            matchApplyAction.addImageData(childInfo.getMatchRequirementDataDTOList(),teamMemberId,matchParticipantDTO,true);
        }
        return id;
    }

    /**
     * 更新参赛支付的状态
     * @param matchTicketPayDto
     * @return
     */
    @Transactional
    public boolean updateParticipantPay(MatchTicketPayDto matchTicketPayDto,String userId){
        matchServiceProvider.getMatchParticipantService().updateParticpantStatus(2,userId,matchTicketPayDto.getMatchId());
        if(!matchApplyAction.checkApplyCodeTimeOut(userId,matchTicketPayDto.getMatchId())) {
            throw new RuntimeException("支付报名费用超时");
        }
        MatchParticipant matchParticipant=matchServiceProvider.getMatchParticipantService().getMatchParticipantById(matchTicketPayDto.getMatchTicketOrParticipantId());
        //如果是团队就搜索其成员，并发送非平台成员的短息
        if(matchParticipant.getTeam()) {
            List<MatchChildInfo> matchChildInfoList=matchServiceProvider.getMatchChildInfoService().listChildInfoByParticipantId(matchParticipant.getId(),null);
            for (MatchChildInfo matchChildInfo:matchChildInfoList) {
                if(StringUtils.isNoneBlank(matchChildInfo.getMobile())) {
                    //短息模板
                    if(!inviteTeamMember(matchChildInfo.getMobile(),matchTicketPayDto.getMatchId(),userId,matchTicketPayDto.getMatchTicketOrParticipantId())) {
                        throw new RuntimeException("发送短息失败");
                    }
                }
            }
        }
        MatchParticipantDTO matchParticipantDTO=new MatchParticipantDTO();
        matchParticipantDTO.setMatchId(matchTicketPayDto.getMatchId());
        matchParticipantDTO.setId(matchTicketPayDto.getMatchTicketOrParticipantId());
        matchParticipantDTO.setUserId(userId);
        //计算报名人数是否饱和，默认先报名就有优先通过的权力,并插入默认入选者
        matchParticipantDTO=matchApplyAction.addAppearanceService(matchParticipantDTO);
        if(!matchServiceProvider.getMatchParticipantService().updateParticipant(matchParticipantDTO,true)){
            return false;
        }
        String toUserId=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchTicketPayDto.getMatchId()).getInitiatorId();
        String description="编号为"+userId+"的报名了编号为"+matchTicketPayDto.getMatchId()+"的比赛，花费"+matchTicketPayDto.getPayPrices()+"元";
        //将金额写入冻结库
        pay(userId,toUserId,matchTicketPayDto.getPayPrices(),matchTicketPayDto.getMatchTicketOrParticipantId(),description,matchTicketPayDto.getPayType());
        matchApplyAction.deleteApplyCodeByUserId(userId);
        return true;
    }


    /**
     * 更新买票的状态
     * @param matchTicketPayDto
     * @return
     */
    @Transactional
    public boolean updateBuyTicketPayStatus(MatchTicketPayDto matchTicketPayDto,String userId){
        if(!matchApplyAction.CheckTicketCodeout(userId,matchTicketPayDto.getPayId())) {
            throw new RuntimeException("购票超时");
        }
        /**
         * 删除定时任务
         * @param jobEnum 定时任务的执行器
         * @param typeId 事件id
         * @param typeName 事件名
         * @param param 定时任务参数
         * @return
         */
        MatchAudience matchAudience=matchServiceProvider.getMatchAudienceService().selectById(matchTicketPayDto.getMatchTicketOrParticipantId());
        if(matchAudience==null) {
            throw new RuntimeException("您不存在未支付的票");
        }
        if(!jobFuseAction.removeJob(JobEnum.MATCH_BUY_TICKET_ROLLBACK_JOB,matchAudience.getMatchTicketId(),"购票超时回滚票数",matchTicketPayDto.getMatchTicketOrParticipantId())){
            throw new RuntimeException("移除购票回滚失败");
        }
        MatchAudienceDTO matchAudienceDTO=new MatchAudienceDTO();
        matchAudienceDTO.setPay(true);
        matchAudienceDTO.setId(matchTicketPayDto.getMatchTicketOrParticipantId());
        matchAudienceDTO.setUserId(userId);
        if(!matchServiceProvider.getMatchAudienceService().updateAudiencePayStatus(matchAudienceDTO)){
            throw new RuntimeException("修改门票购买状态失败");
        }
        String toUserId=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchTicketPayDto.getMatchId()).getInitiatorId();
        String description="编号为"+userId+"的人购买了门票一张价值"+matchTicketPayDto.getPayPrices()+"的门票";
        //将金额写入冻结库
        pay(userId,toUserId,matchTicketPayDto.getPayPrices(),matchTicketPayDto.getMatchTicketOrParticipantId(),description,matchTicketPayDto.getPayType());
        return true;
    }

    /**
     * 将付款冻结起来
     */
    @Transactional(rollbackFor = Exception.class)
    void pay(String userId, String toUserId, BigDecimal amount, String typeId, String description, PayTypeEnum payTypeEnum) {
        FrozenAddRequestDto requestDto = new FrozenAddRequestDto ();
        requestDto.setUserId ( userId );
        requestDto.setTradeType ( Integer.parseInt(payTypeEnum.getTradeType()) );
        requestDto.setToUserId ( toUserId );
        requestDto.setDescription ( description );
        requestDto.setFrozenType ( FrozenTypeEnum.FTZ_MATCH.getName () );
        requestDto.setAmount ( amount );
        requestDto.setTypeId ( typeId );
        if (!walletForzenClientAction.add ( requestDto )) {
            throw new RuntimeException ( "添加冻结金额异常" );
        }
    }
    /**
     * 定时任务：票数回滚
     */
    public void matchTicketRollBack(String matchTicketId){
        if(StringUtils.isBlank(matchTicketId)){
            throw new RuntimeException("定时任务失败，ticketId为空");
        }
        MatchTicketService matchTicketService=matchServiceProvider.getMatchTicketService();
        MatchTicket matchTicket=matchTicketService.getMatchTicketBYMatchTicketId(matchTicketId);
        //更新票数，及乐观锁
        MatchTicketDTO matchTicketDTO=new MatchTicketDTO();
        matchTicketDTO.setId(matchTicketId);
        matchTicketDTO.setNumber(matchTicket.getNumber()+1);
        Integer newoptimisticLocking=matchTicket.getOptimisticLocking()+1;
        if(!matchTicketService.updateTicketById(matchTicketDTO,newoptimisticLocking,matchTicket.getOptimisticLocking())){
            throw new RuntimeException("票数回滚失败");
        }
    }
    //-------------------------------分割线----Yawn-----------------------------

    /**
     * 发送信息给发布审核之前添加的工作人员
     * @param matchId
     * @param userId
     * @return
     */
    @Transactional
    public boolean inviteMember(String matchId, String userId) {
        if (matchId == null) {
            throw new RuntimeException("请选择需要添加人员的比赛");
        }
        String matchTitle = matchServiceProvider.getMatchEventService().getMatchTitleByMatchId(matchId);
        List<MatchMember> matchMembers = matchServiceProvider.getMatchMemberService().listMatchMemberNotAccept(matchId);
        MatchMemberService matchMemberService = matchServiceProvider.getMatchMemberService();
        for (MatchMember dto : matchMembers) {
            Integer status = Integer.valueOf(dto.getKind());
            String description = "工作人员";
            if (status.equals(MatchMemberKind.AUDIT_PASS.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.GUEST.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.HOST.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.STAFF.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.RATER.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.OTHER.status))
                description = MatchMemberKind.OTHER.description;
            /*
            如果不是网值用户
             */
            if (!dto.getNetUser()) {
                /*
                第一步，生成随机码，添加用户
                 */
                String active_code = ActivateCodePrefixEnum.MATCH_WM.getValue() + RandomUtil.generateString(4);
                dto.setActiveCode(active_code);
                if(!matchMemberService.insertOrUpdate(dto)){
                    throw new RuntimeException("邀请用户失败");
                }
                /*
                第二步，生成短信变量
                 */
                Map<String, Object> map = new HashMap<>();
                map.put("user_call", dto.getUserCall());
                map.put("user_name", userAction.queryUser(userId).getNickname());
                map.put("match_title", matchTitle);
                map.put("active_code", active_code);
                map.put("description", description);
                /*
                第三步，发送信息
                 */
                String mobileNumber = dto.getUserCall();
                if(!mobileNumber.matches(RegularExpressionEnum.MOBILE.getValue())){
                    throw new RuntimeException("手机号" + dto.getUserId() + "格式错误");
                }
                int result = mobileMessage.send(mobileNumber, map, MobileSmsCode.MATCH_INVITATION);
                if (result != 1) {
                    throw new RuntimeException("发送信息失败");
                }
            /*
            否则发送邀请
             */
            } else {
                /*
                直接添加邀请信息
                 */
                boolean sendToUser = wzCommonImHistoryAction.add(userId, dto.getUserId(), userAction.queryUser(userId).getNickname()
                        + "邀请您共同举办" + matchTitle + "比赛", matchId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_MATCH_MEMBERS, null);
                if (!sendToUser)
                    throw new RuntimeException("邀请"+ userAction.queryUser(userId).getNickname() + "失败");
            }
        }
        return true;
    }


    /**
     * 提交审核
     * @param matchId
     * @return
     */
    @Transactional
    public boolean commitMatchEvent(String matchId, String userId){
        MatchEvent matchEvent=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
        if(matchEvent.getMatchStatus()==MatchStatusCode.REVIEW_PENDING.status){
            return false;
        }
        boolean success=false;
        success=matchServiceProvider.getMatchMemberService().IsWriteMatchMember(matchId);
        if(!success) {
            throw new RuntimeException("请填写比赛成员");
        }
        success=matchServiceProvider.getMatchProgressService().IsWriteMatchProgress(matchId);
        if(!success) {
            throw new RuntimeException("请设定比赛赛程");
        }
        success=matchServiceProvider.getMatchGruopService().IsWriteMatchGroup(matchId);
        if(!success) {
            throw new RuntimeException("请设定比赛赛组");
        }
        List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
        if(matchZoneList == null || matchZoneList.size() == 0) {
            throw new RuntimeException("请设定比赛的各赛区");
        }
        String[] zoneIds=new String[matchZoneList.size()];
        for (int i=0;i<matchZoneList.size();i++) {
            zoneIds[i]=matchZoneList.get(i).getId();
        }
        success=matchServiceProvider.getMatchGroupAndZoneService().IsWriteMatchApply(zoneIds);
        if(!success) {
            throw new RuntimeException("请设定赛区统一时间");
        }
//        success=matchServiceProvider.getMatchTicketService().IsWriteMatchTicket(zoneIds);
//        if(!success) {
//            throw new RuntimeException("请设定比赛门票");
//        }
        success=matchServiceProvider.getMatchAwardService().IsWriteMacthTicket(matchId);
        if(!success){
            throw new RuntimeException("请将所有的项目填完才能提交。");
        }
        /*
        要求共同举办方
         */
        String matchTitle=matchEvent.getTitle();
        List<MatchReviewVo> dtos = matchServiceProvider.getMatchReviewService().getReviewListBYMatchId(matchId);
        for (MatchReviewVo dto : dtos) {
            UserInfoAndHeadImg user=userAction.getUserInfoAndHeadImg(userId);
            if (StringUtils.isEmpty(dto.getUserId()))
                continue;
            boolean flag = wzCommonImHistoryAction.add(userId, dto.getUserId(), userAction.queryUser(userId).getNickname()
                    + "邀请您共同举办" + matchTitle + "比赛", matchId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_MATCH, null);
            if (!flag)
                throw new RuntimeException("邀请"+ user.getNickname() + "失败");

            int kind=dto.getOrganizerKind();
            /**
             * ORGANIZER(0, "主办方"),
             *     CO_ORGANIZER(1, "协办方"),
             *     SPONSOR(2, "赞助方"),
             *     UNITED_ORGANIZER(3, "联合举办"),
             *     DIRECTOR(4, "指导单位"),
             *     SUPPORT(5, "支持方");
             */
            String description="";
            if(kind==MatchOrganizerKind.CO_ORGANIZER.getStatus()) {
                description=MatchOrganizerKind.CO_ORGANIZER.getDescription();
            }else if(kind == MatchOrganizerKind.ORGANIZER.getStatus()) {
                description=MatchOrganizerKind.ORGANIZER.getDescription();
            }else if(kind == MatchOrganizerKind.SPONSOR.getStatus()) {
                description=MatchOrganizerKind.SPONSOR.getDescription();
            }else if(kind == MatchOrganizerKind.UNITED_ORGANIZER.getStatus()) {
                description=MatchOrganizerKind.UNITED_ORGANIZER.getDescription();
            }else if(kind == MatchOrganizerKind.SUPPORT.getStatus()) {
                description=MatchOrganizerKind.SUPPORT.getDescription();
            }
            /**
             *生成短信变量
             */
            User toUser=userAction.queryUser(matchEvent.getInitiatorId());
            Map<String, Object> map = new HashMap<>();
            map.put("user_call", user.getNickname());
            map.put("user_name", toUser.getNickname());
            map.put("match_title", matchTitle);
            map.put("description", description);
            /**
             *发送信息
             */
            String mobileNumber = toUser.getMobile();
            if(!mobileNumber.matches(RegularExpressionEnum.MOBILE.getValue())){
                throw new RuntimeException("手机号" + dto.getUserId() + "格式错误");
            }
            int result = mobileMessage.send(mobileNumber, map, MobileSmsCode.MATCH_INVITE_REMIND);
            if (result != 1) {
                throw new RuntimeException("发送信息失败");
            }
        }
        //修改审核状态
        return matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.REVIEW_PENDING);
    }





    /**
     * 赛事发布后添加工作人员
     * @param dtos
     * @param userId
     * @param matchId
     * @return
     */
    @Transactional
    public boolean inviteMemberAfterReview(List<MemberDTO> dtos, String userId, String matchId) {
        if (dtos == null || dtos.size() == 0) {
            throw new RuntimeException("请添加邀请对象");
        }
        if (StringUtils.isEmpty(matchId)) {
            throw new RuntimeException("请确定比赛ID");
        }
        String matchTitle = matchServiceProvider.getMatchEventService().getMatchTitleByMatchId(matchId);
        MatchMemberService matchMemberService = matchServiceProvider.getMatchMemberService();
        if(dtos == null || dtos.size() == 0) {
            throw new RuntimeException("添加的列表不能为空");
        }
        for (MemberDTO dto : dtos) {
            Integer status = Integer.valueOf(dto.getKind());
            String description = "工作人员";
            if (status.equals(MatchMemberKind.AUDIT_PASS.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.GUEST.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.HOST.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.STAFF.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.RATER.status))
                description = MatchMemberKind.AUDIT_PASS.description;
            else if (status.equals(MatchMemberKind.OTHER.status))
                description = MatchMemberKind.OTHER.description;
            /*
            如果不是网值用户
             */
            if (!dto.getInNet()) {
                /*
                第一步，生成随机码，添加用户
                 */
                String active_code = ActivateCodePrefixEnum.MATCH_WM.getValue() + RandomUtil.generateString(4);
                if(!matchMemberService.invitedNotNetMember(dto, active_code)){
                    throw new RuntimeException("邀请用户失败");
                }
                /*
                第二步，生成短信变量
                 */
                Map<String, Object> map = new HashMap<>();
                map.put("user_call", dto.getUserCall());
                map.put("user_name", userAction.queryUser(userId).getNickname());
                map.put("match_title", matchTitle);
                map.put("active_code", active_code);
                map.put("description", description);
                /*
                第三步，发送信息
                 */
                String mobileNumber = dto.getUserId();
                if(!mobileNumber.matches(RegularExpressionEnum.MOBILE.getValue())){
                    throw new RuntimeException("手机号" + dto.getUserId() + "格式错误");
                }
                int result = mobileMessage.send(mobileNumber, map, MobileSmsCode.MATCH_INVITATION);
                if (result != 1) {
                    throw new RuntimeException("发送信息失败");
                }
            /*
            否则正常插入，发送邀请
             */
            } else {
                /*
                第一步，添加用户
                 */
                if(!matchMemberService.invitedMember(dto)){
                    throw new RuntimeException("邀请用户失败");
                }
                /*
                第二步，添加邀请信息
                 */
                boolean sendToUser = wzCommonImHistoryAction.add(userId, dto.getUserId(), userAction.queryUser(userId).getNickname()
                        + "邀请您共同举办" + matchTitle + "比赛", matchId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_MATCH_MEMBERS, null);
                if (!sendToUser)
                    throw new RuntimeException("邀请"+ userAction.queryUser(userId).getNickname() + "失败");
            }
        }
        return true;
    }

    /**
     * 邀请团队成员
     * @param mobile
     * @param matchId
     * @param userId
     * @return
     */
    @Transactional
    public boolean inviteTeamMember(String mobile,String matchId,String userId,String participantId) {
        if(StringUtils.isNoneBlank(mobile)){
            if(!mobile.matches(RegularExpressionEnum.MOBILE.getValue())){
                throw new RuntimeException("手机号" + userId + "格式错误");
            }
            //TM TeamMember的缩写
            String active_code =ActivateCodePrefixEnum.MATCH_TM.getValue() + RandomUtil.generateString(4);
            String title=matchServiceProvider.getMatchEventService().getMatchTitleByMatchId(matchId);
            Map<String, Object> map = new HashMap<>();
            map.put("user_name", userAction.queryUser(userId).getNickname());
            map.put("active_code", active_code);
            map.put("match_title", title);
            //将邀请码当成key,将邀请人的id是值
            clientRedis().put(RedisPrefixEnum.KEY_MATCH_TEAM_MEMBER_INTIVE.getValue()+":"+active_code,participantId);
            int result = mobileMessage.send(mobile, map, MobileSmsCode.MATCH_INVITE_TEAM_MEMBER);
            if (result != 1) {
                throw new RuntimeException("发送信息失败");
            }
            return true;
        }
        return false;
    }

    /**
     * 激活码激活页面
     * @param userId
     * @param activateCode
     * @return
     */
    @Transactional
    public boolean activateMember(String userId, String activateCode) {
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("输入的userId为空");
        }
        if (StringUtils.isEmpty(activateCode)) {
            throw new RuntimeException("邀请码丢失啦");
        }
        User u = userAction.selectMobileByuserId(userId);
        if (u == null) {
            throw new RuntimeException("你还没登记手机号");
        }
        String mobile = u.getMobile();
        //判断邀请码类别
        if(activateCode.startsWith(ActivateCodePrefixEnum.MATCH_TM.getValue())) {
            String participantId=clientRedis().getString(RedisPrefixEnum.KEY_MATCH_TEAM_MEMBER_INTIVE.getValue()+":"+activateCode);
            if(StringUtils.isBlank(participantId)) {
                throw new RuntimeException("邀请码错误");
            }
            List<MatchChildInfo> matchChildInfoList=matchServiceProvider.getMatchChildInfoService().listChildInfoHaveMobileByParticipantId(participantId);
            for (MatchChildInfo matchChildInfo:
            matchChildInfoList) {
                if(matchChildInfo.getMobile().equals(mobile)) {
                    matchChildInfo.setUserId(userId);
                    matchChildInfo.setMobile(null);
                    matchServiceProvider.getMatchChildInfoService().updateById(matchChildInfo);
                    clientRedis().del(RedisPrefixEnum.KEY_MATCH_TEAM_MEMBER_INTIVE.getValue()+":"+activateCode);
                    break;
                }
            }
        }else if(activateCode.startsWith(ActivateCodePrefixEnum.MATCH_WM.getValue())) {
            MatchMember matchMember = matchServiceProvider.getMatchMemberService().selectActivateCodeByTelephone(mobile);
            if (matchMember == null) {
                throw new RuntimeException("还没有人邀请你喔");
            }
            if (activateCode.equals(matchMember.getActiveCode())) {
                matchMember.setNetUser(true);
                matchMember.setUserId(userId);
                if(!matchServiceProvider.getMatchMemberService().insertOrUpdate(matchMember))
                    throw new RuntimeException("接受邀请失败");
            }
        }
        return true;
    }
/*******************************超级分割线**********************************************************/
    /**
     * 比赛最后
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean repealFrozenMatchMoney(String matchId) {
        /*
        第一步，退回未通过审核人员的资金
         */
        List<MatchParticipant> matchParticipants = matchServiceProvider.getMatchParticipantService().listPaidAndNoPassParticipant(matchId);
        for (MatchParticipant p : matchParticipants) {
            FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
            requestDto.setTypeId(p.getId());
            requestDto.setUserId(p.getUserId());
            requestDto.setType(FrozenTypeEnum.FTZ_MATCH);
            if (!walletFrozenAction.repeal(requestDto)) {
                throw new RuntimeException("使用冻结金额异常");
            }
        }
        /*
        第二步，结算通过审核人员资金
         */
        List<MatchParticipant> passParticipants = matchServiceProvider.getMatchParticipantService().listPaidAndPassParticipant(matchId);
        for (MatchParticipant pp : passParticipants) {
            FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto();
            frozenOperationRequestDto.setType(FrozenTypeEnum.FTZ_MATCH);
            frozenOperationRequestDto.setTypeId(pp.getId());
            frozenOperationRequestDto.setUserId(pp.getUserId());
            if (!walletFrozenAction.pay(frozenOperationRequestDto)) {
                throw new RuntimeException("结算失败");
            }
        }
        /*
        第三步，结算门票资金
         */
        List<MatchAudience> paidAudiences = matchServiceProvider.getMatchAudienceService().listpaidAudience(matchId);
        for (MatchAudience audience : paidAudiences) {
            FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto();
            frozenOperationRequestDto.setType(FrozenTypeEnum.FTZ_MATCH);
            frozenOperationRequestDto.setTypeId(audience.getMatchTicketId());
            frozenOperationRequestDto.setUserId(audience.getUserId());
            if (!walletFrozenAction.pay(frozenOperationRequestDto)) {
                throw new RuntimeException("结算失败");
            }
        }
        return true;
    }


    public boolean startClearJob(String matchId) {
        boolean flag = false;
        if(StringUtils.isNoneBlank(matchId)){
            /*
            获取最后比赛时间
             */
            Date lastTime = matchServiceProvider.getMatchVenueService().getLastEndTimeBtMatchId(matchId);
            /*
            添加定时任务,比赛结束之后的第二天
             */
            if (lastTime != null) {
                flag = jobFuseAction.addJob(JobEnum.Match_CLEAR_MONEY_JOB,matchId,matchId,"赛事结束时结算资金",new Date(lastTime.getTime()+172800000),AuthorEmailEnum.DIAN_QV);
            }
        }
        return flag;
    }

    /**
     *  审核操作
     * @param matchPassReviewDto
     * @return
     */
    @Transactional
    public boolean issueMatch(MatchPassReviewDto matchPassReviewDto, String userId){
        String matchId=matchPassReviewDto.getMatchId();
        boolean isPass=matchPassReviewDto.isPass();
        if(!checkIsHaveMatchByMatchId(matchId)) {
            throw new RuntimeException("还没有创建比赛");
        }
        MatchReviewService matchReviewService = matchServiceProvider.getMatchReviewService();
        //审核已经通过了
        if(matchServiceProvider.getMatchEventService().selectById(matchId).getApproved()) {
            throw new RuntimeException("比赛已经通过审核啦");
        }
        // 如果审核者表示通过
        if (isPass) {
            // 检查比赛状态
            if(!matchServiceProvider.getMatchEventService().updateMatchStatus(matchId, MatchStatusCode.AUDIT_PASS)){
                throw new RuntimeException("修改审核状态失败");
            }
            if(matchServiceProvider.getMatchEventService().approveMatch(matchId)) {
                //如果成功,两个表都要更新
                boolean success;

                success= matchReviewService.passReview(userId, matchId, isPass);

                /*
                审核通过，发送通知
                */
                if (success) {
                    success = inviteMember(matchId, matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId).getInitiatorId());
                }
                /*
                发起定时任务
                 */
                if (success) {
                    success = startClearJob(matchId);
                }
                if(StringUtils.isNoneBlank(matchPassReviewDto.getReason())) {
                    /**
                     * 填写拒绝理由
                     */
                    MatchEvent matchEvent=matchServiceProvider.getMatchEventService().selectById(matchId);
                    matchEvent.setReason(matchPassReviewDto.getReason());
                    matchServiceProvider.getMatchEventService().updateById(matchEvent);
                    boolean sendToUser = wzCommonImHistoryAction.add(userId, matchEvent.getInitiatorId(), "您发布的比赛\""+matchEvent.getTitle()+"\"通过了", matchId,
                            MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_MATCH_REASON, null);
                    if (!sendToUser)
                        throw new RuntimeException("邀请"+ userAction.queryUser(userId).getNickname() + "失败");
                }
                return success;
            }
        }
        if(!matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.REVIEW_REJECTED)){
            throw new RuntimeException("修改审核状态失败");
        }

        /*
        不通过审核情况直接返回false
         */
        boolean  flag= matchReviewService.passReview(userId, matchId, isPass);

        if(StringUtils.isNoneBlank(matchPassReviewDto.getReason())) {
            /**
             * 填写拒绝理由
             */
            MatchEvent matchEvent=matchServiceProvider.getMatchEventService().selectById(matchId);
            matchEvent.setReason(matchPassReviewDto.getReason());
            matchServiceProvider.getMatchEventService().updateById(matchEvent);
            boolean sendToUser = wzCommonImHistoryAction.add(userId, matchEvent.getInitiatorId(), "您发布的比赛\""+matchEvent.getTitle()+"\"被拒绝了，理由为"
                    +matchPassReviewDto.getReason(), matchId, MessageTypeEnum.ACTIVITY_TYPE, PushMessageDocTypeEnum.WZ_MATCH_REASON, null);
            if (!sendToUser)
                throw new RuntimeException("邀请"+ userAction.queryUser(userId).getNickname() + "失败");
        }
        return flag;
    }

    /**
     * 检验是否存在这比赛
     * @param matchId
     * @return
     */
    public boolean checkIsHaveMatchByMatchId(String matchId){
        MatchEvent matchEvent=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
        if(matchEvent!=null){
            return true;
        }
        return false;
    }

/*******************************超级分割线**********************************************************/
    /**
     * 增加举办方
     * @param dtos
     * @param matchId
     * @return
     */
    public boolean addOrganizer(List<MatchReviewDTO> dtos, String matchId) {
        MatchReviewService matchReviewService = matchServiceProvider.getMatchReviewService();
        for (MatchReviewDTO dto : dtos) {
            dto.setMatchId(matchId);
            Merchant merchant=merchantService.selectById(dto.getMerchantId());
            if(merchant!=null) {
                dto.setUserId(merchant.getUserId());
            }
            if(!matchReviewService.addMatchReview(dto)) {
                throw new RuntimeException("添加失败");
            }
        }
        return true;
    }

//    public List<MatchEventSimpleVo> list(MatchSearchDTO matchSearchDTO) {
//        //构建商品搜索条件、排序
//        List<MatchSearchResponse> matchSearchResponseList = matchSearchService.queryMatch(getMatchSearchQuery(matchSearchDTO));
//        List<MeetingListDto> meetingListDtoList = new ArrayList<>();
//        for (MeetingSearchResponse meetingSearchResponse : meetingSearchResponseList) {
//            MeetingListDto dto = createMetingListDto(meetingSearchResponse);
//            //List的总是要除以100
//            dto.setAmount(dto.getAmount().divide(new BigDecimal("100")));
//            meetingListDtoList.add(dto);
//        }
//        return meetingListDtoList;
//    }
//
//    private MatchSearchQuery getMatchSearchQuery(MatchSearchDTO matchSearchDTO) {
//
//    }


    //+++++++++++++++++++++++++++++++++++++++搜索+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public List<MatchIndexVo> list(MatchSearchDTO matchSearchDTO) {
        //构建商品搜索条件、排序
        List<MatchSearchResponse> matchSearchResponseList = matchSearchService.queryMatch(getMatchSearchQuery(matchSearchDTO));
        List<MatchIndexVo> matchIndexVoList = new ArrayList<>();
        for (MatchSearchResponse matchSearchResponse : matchSearchResponseList) {
            MatchIndexVo dto = createMatchListVo(matchSearchResponse);
            matchIndexVoList.add(dto);
        }
        return matchIndexVoList;
    }

    private MatchSearchQuery getMatchSearchQuery(MatchSearchDTO matchSearchDTO) {
        MatchSearchQuery matchSearchQuery = new MatchSearchQuery();
        VoPoConverter.copyProperties(matchSearchDTO, matchSearchQuery);
        matchSearchQuery.setCenterGeoPoint(new GeoPoint(matchSearchDTO.getLat(),matchSearchDTO.getLon()));
        matchSearchQuery.setPage(matchSearchDTO.getCurrent(), matchSearchDTO.getSize());

        /** 比赛排序方式
         * 排序方式
         * 0.最热>最近
         * 1.最新>最近
         * 2.最近>信用>在线
         * 3.信用>最近
         * 不传：齐享欢乐（距离、信用、在线状态）
         */
        if (matchSearchDTO.getSort() == 0) {
            matchSearchQuery.addLastAscQuery(new LastAscQuery("regCount", false));
            matchSearchQuery.addLastAscQuery(new LastAscQuery("creditSum", false));
        } else if (matchSearchDTO.getSort() == 1) {
            matchSearchQuery.addFristAscQuery(new LastAscQuery("publishTime", false));
            matchSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        } else if (matchSearchDTO.getSort() == 3) {
            matchSearchQuery.addFristAscQuery(new LastAscQuery("credit", false));
            matchSearchQuery.addLastAscQuery(new LastAscQuery("isLogin", false));
        }
        return matchSearchQuery;
    }

    /**
     * 将搜索出来的东西转成Vo
     * @param matchSearchResponse
     * @return
     */
    private MatchIndexVo createMatchListVo(MatchSearchResponse matchSearchResponse) {
        MatchIndexVo matchIndexVo=new MatchIndexVo();
        VoPoConverter.copyProperties(matchSearchResponse, matchIndexVo);
        if(matchSearchResponse.getMatchImageUrl().size()>0 && matchSearchResponse!=null) {
            matchIndexVo.setMatchImageUrl(matchCreateAction.pictures(matchSearchResponse.getMatchImageUrl().get(0)));
        }
        matchIndexVo.setStatus(matchSearchResponse.getMatchStatus());
        matchIndexVo.setPublishTime(matchSearchResponse.getPublishTime());
        matchIndexVo.setWorthType(WorthTypeEnum.MATCH_TYPE.getName());
        return matchIndexVo;
    }
}
