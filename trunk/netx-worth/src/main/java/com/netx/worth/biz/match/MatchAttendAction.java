package com.netx.worth.biz.match;


import com.netx.common.user.model.UserMacthInfo;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.wz.dto.matchEvent.MatchAppearanceDTO;
import com.netx.common.wz.dto.matchEvent.MatchRatersDTO;
import com.netx.common.wz.dto.matchEvent.MatchVoteDTO;
import com.netx.worth.enums.MatchAttendType;
import com.netx.worth.enums.MatchMemberKind;
import com.netx.worth.model.*;
import com.netx.worth.service.*;
import com.netx.worth.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 赛事出席的服务实现类
 * @author 老肥猪
 * @since 2018-08-03
 */
@Service
public class MatchAttendAction {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private MatchServiceProvider matchServiceProvider;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private MatchCreateAction matchCreateAction;

    /**
     * 找到该比赛的所有观众
     * @param venudId
     * @return
     */
    public Map<String, Object> getMatchAudience(String venudId){
        List<UserMacthInfo> list=matchServiceProvider.getMatchAudienceService().listAttendAudience(venudId);
        Map<String, Object> map = new HashMap<>(1);
        map.put("list", list);
        return map;
    }




    /**
     * 获取用户的身份
     * @param matchId
     * @param userId
     * @return
     */
    public Map<String, Object> getMatchMemberKind(String matchId,String userId) {
        String kind = matchServiceProvider.getMatchMemberService().getMemberKind(matchId, userId,1);
        Map<String, Object> map = new HashMap<>();
        if (kind == null) {
            map.put("kind", 6);
        } else {
            map.put("kind", kind);
        }
        Map matchInfo=getMatchInfo(matchId);

        map.put("matchInfo",matchInfo);
        return map;
    }

    /**
     * 获取比赛信息
     * @param matchId
     * @return
     */
    public Map<String,Object> getMatchInfo(String matchId){
        Map<String,Object> map=new HashMap<>();
        MatchEvent matchEvent=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
        map.put("matchEvent",matchEvent);
        List<MatchAward> matchAwardList=matchServiceProvider.getMatchAwardService().listMatchAwardByMatchId(matchId);
        map.put("matchAwardList",matchAwardList);
        List<MatchReviewVo> matchReviewVoList=matchServiceProvider.getMatchReviewService().getMainReviewAcceptBYMatchId(matchId);
        map.put("matchReview",null);
        if(matchReviewVoList.size()>0&&matchReviewVoList!=null){
            map.put("matchReview",matchReviewVoList.get(0));
        }
        return map;
    }

    /**
     * 获取出场次序
     * @param matchId
     * @param zone_id
     * @return
     */
    public Map<String, List> listMatchAppearance(String matchId, String zone_id) {
        List<MatchAppearanceOrderVo> appearances = matchServiceProvider.getMatchAppearanceService().listMatchAppearance(matchId, zone_id);
        if (appearances == null || appearances.size()==0) {
            throw new RuntimeException("还没有设定比赛次序");
        }
        Map<String, List> map = new HashMap<>(1);
        map.put("appearances", appearances);
        return map;
    }

    /**
     * 根据赛组返回参赛选手列表
     * @param progressId
     * @param groupId
     * @return
     */
    public Map<String, List> listMatchAppearanceByGroup(String progressId, String groupId, String zoneId) {
        if(StringUtils.isBlank(progressId) || StringUtils.isBlank(groupId))
            throw new RuntimeException("请选择赛组");
        Map<String, List> map = new HashMap<>(1);
        List<MatchAppearanceOrderVo> matchAppearanceOrderVos = matchServiceProvider.getMatchAppearanceService().listMatchAppearanceByGroup(progressId, zoneId, groupId);
        for (int i = 0; i < matchAppearanceOrderVos.size(); i++) {
            matchAppearanceOrderVos.get(i).setHeadImageUrl(addImgUrlPreUtil.getUserImgPre(matchAppearanceOrderVos.get(i).getHeadImageUrl()));
        }
        map.put("appearances", matchAppearanceOrderVos);
        return map;
    }

    /**
     * 根据赛区查询赛组
     * @param zoneId
     * @return
     */
    public Map<String, Object> selectMatchGroupByZoneId(String zoneId){
        Map<String, Object> map = new HashMap<>();
        List<MatchGroupZoneVo> vos = matchServiceProvider.getMatchGruopService().selectMatchGroupByZoneId(zoneId);
        if (vos != null)
            map.put("data", vos);
        return map;
    }

    /**
     * 根据比赛id查询各赛区赛组的报名情况
     * @param matchId
     * @return
     */
    public Map<String,Object> selectMatchGroupByMatchId(String matchId) {
        Map<String, Object> map = new HashMap<>();
        List<MatchGroupZoneVo> matchGroupZoneVoList = matchServiceProvider.getMatchGruopService().selectMatchGroupByMatchId(matchId);
        int status=0;
        int count=0;
        List<MatchGroupZoneVo> result=new ArrayList<>();
        Date date=new Date();
        for (int i=0;i<matchGroupZoneVoList.size();i++) {
            MatchGroupZoneVo matchGroupZoneVo=matchGroupZoneVoList.get(i);
            if(date.getTime()>matchGroupZoneVo.getEndTime().getTime()) {
                count++;
                result.add(matchGroupZoneVo);
            }
        }

        /**
         * 0表示没有报名结束的
         * 1表示已经有报名结束的
         * 2表示全部结束的
         */
        if(count==0) {
            status=0;
        }else if(count==matchGroupZoneVoList.size()-1){
            status=2;
        }else {
            status=1;
        }
        map.put("status",status);
        map.put("res",result);
        return map;
    }
    /**
     * 返回用户相对于这个比赛的类型
     * @param userId
     * @param matchId
     * @return
     */
    public Map<String, Object> getMemberKind(String userId, String matchId) {
        String memberKind = matchServiceProvider.getMatchMemberService().getMemberKind(matchId, userId);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(memberKind)) {
            map.put("memberKind", "6");
        } else {
            map.put("memberKind", memberKind);
        }
        MatchEvent matchEvent = matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
        Integer status;
        if (matchEvent != null) {
            status = matchEvent.getMatchStatus() != null ? matchEvent.getMatchStatus() : 0;
        } else {
            status = 0;
        }
        map.put("matchStatus", status);
        return map;
    }

    /**
     * 添加一个评分
     * @param dto
     * @return
     */
    public boolean addMatchRater(MatchRatersDTO dto, String userId) {
        String memberKind;
        memberKind = matchServiceProvider.getMatchMemberService().getMemberKind(dto.getMatchId(), userId);
        if (StringUtils.isBlank(memberKind)) {
            throw new RuntimeException("你还不是评分者");
        }
        if (Integer.valueOf(memberKind) != MatchMemberKind.RATER.getStatus()) {
            throw new RuntimeException("你还不是评分人员");
        }
        if(matchServiceProvider.getMatchRatesService().isHadRater(dto, userId)) {
            throw new RuntimeException("您已对该选手评分");
        }
        return matchServiceProvider.getMatchRatesService().addMatchRater(dto, userId);
    }

    /**
     * 返回用户比分
     * @param progressId
     * @param participantId
     * @return
     */
    public Map<String, Object> listMatchRaterScoreDetail(String progressId, String participantId) {
        if(StringUtils.isBlank(progressId)) {
            throw new RuntimeException("请指定赛程");
        }
        if (StringUtils.isBlank(participantId)) {
            throw new RuntimeException("请选择参赛者");
        }
        Map<String, Object> map = new HashMap<>();
        List<MatchRaters> list = matchServiceProvider.getMatchRatesService().listScore(progressId, participantId);
        if (list != null && list.size()>0) {
            List<MatchScoreVo> vos = new ArrayList<>();
            for (MatchRaters raters : list) {
                MatchScoreVo vo = new MatchScoreVo();
                vo.setRaterName(raters.getRatersName());
                vo.setScore(Double.valueOf(new BigDecimal(String.valueOf(raters.getScore())).toString()));
                vos.add(vo);
            }

            // double avgScore = vos.stream().collect(Collectors.averagingDouble(MatchScoreVo::getScore));
            map.put("data", vos);
            // map.put("avgScore", avgScore);
        }
        return map;
    }

    public Map<String, Object> listScoreByGroupId(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            throw new RuntimeException("请填入指定赛组");
        }
        Map<String, Object> map = new HashMap<>(1);
        List<MatchParticipantScoreVo> list = matchServiceProvider.getMatchRatesService().listScoreByGroupId(groupId);
        if (list != null && list.size()>0) {
            map.put("data", list);
        }
        return map;
    }

    /**
     * 返回比赛的嘉宾、工作人员
     * @param matchId
     * @return
     */
    public Map<String, Object> getWorkPeopleAllListByMatchId(String matchId) {
        Map<String, Object> map = new HashMap<>(1);
        List<MatchUserInfoVo> userMacthInfos =  matchServiceProvider.getMatchMemberService().getWorkPeopleAllListByMatchId(matchId);
        if (userMacthInfos.size() == 0 || userMacthInfos == null) {
            return map;
        }
        for (int i = 0; i < userMacthInfos.size(); i++) {
            userMacthInfos.get(i).setUrl(addImgUrlPreUtil.getUserImgPre(userMacthInfos.get(i).getUrl()));
        }
        map.put("workerPeople", userMacthInfos);
        return map;
    }

    /**
     * 返回比赛的接受邀请的工作人员
     * @param matchId
     * @return
     */
    public Map<String, Object> getWorkAcceptPeopleAcceptListByMatchId(String matchId) {
        Map<String, Object> map = new HashMap<>(1);
        List<MatchUserInfoVo> userMacthInfos =  matchServiceProvider.getMatchMemberService().getWorkPeopleIsAcceptListByMatchId(matchId);
        if (userMacthInfos.size() == 0 || userMacthInfos == null) {
            return map;
        }
        for (int i = 0; i < userMacthInfos.size(); i++) {
            userMacthInfos.get(i).setUrl(addImgUrlPreUtil.getUserImgPre(userMacthInfos.get(i).getUrl()));
        }
        map.put("workerPeople", userMacthInfos);
        return map;
    }
    /**
     * 返回比赛的接受邀请的工作人员
     * @param matchId
     * @return
     */
    public Map<String, Object> getWorkAcceptPeopleIsSpotANDIsAccepttListByMatchId(String matchId) {
        Map<String, Object> map = new HashMap<>(1);
        List<MatchUserInfoVo> userMacthInfos =  matchServiceProvider.getMatchMemberService().getWorkPeopleIsSpotANDIsAcceptListByMatchId(matchId);
        if (userMacthInfos.size() == 0 || userMacthInfos == null) {
            return map;
        }
        for (int i = 0; i < userMacthInfos.size(); i++) {
            userMacthInfos.get(i).setUrl(addImgUrlPreUtil.getUserImgPre(userMacthInfos.get(i).getUrl()));
        }
        map.put("workerPeople", userMacthInfos);
        return map;
    }
    /**
     * 添加比赛内容
     * @param matchVoteDTO
     * @return
     */
    public boolean addMatchVote(MatchVoteDTO matchVoteDTO){
        if(matchServiceProvider.getMatchVoteService().addMatchVote(matchVoteDTO)){
            return true;
        }
        return  false;
    }
    public boolean updateMatchVote(MatchVoteDTO matchVoteDTO){
        if(matchServiceProvider.getMatchVoteService().updateMatchVote(matchVoteDTO)){
            return true;
        }
        return  false;
    }
    public Map<String,Object> getMatchVoteByMatchId(String matchId){
        MatchVoteService matchVoteService=matchServiceProvider.getMatchVoteService();
        List<MatchVote> matchVoteList=matchVoteService.listMatchVote(matchId);
        if(matchVoteList.size()==0||matchVoteList==null){
            return null;
        }
        for (int i = 0; i < matchVoteList.size(); i++) {
            matchVoteList.get(i).setProjectImagesUrl(matchCreateAction.pictures(matchVoteList.get(i).getProjectImagesUrl()));
        }
        Map<String,Object> map=new HashMap<>();
        map.put("matchVoteList",matchVoteList);
        return map;
    }
    /**
     * 删除比赛投票内容
     * @param matchVoteId
     * @return
     */
    public boolean deleteMatchVoteById(String matchVoteId){
        if(matchServiceProvider.getMatchVoteService().deleteMatchVoteById(matchVoteId)){
            return true;
        }
        return  false;
    }



//    /**
//     * 获取参赛者的出席码
//     * @param userId
//     * @return
//     */
//    public Map<String, String> getParticipantCode(String userId) {
//        if (StringUtils.isEmpty(userId)) {
//            throw new RuntimeException("请先登录");
//        }
//        MatchParticipant matchParticipant = matchServiceProvider.getMatchParticipantService().getMatchParticipantById(userId);
//        if (matchParticipant == null) {
//            throw new RuntimeException("请先报名比赛");
//        }
//        String code = matchParticipant.getId();
//        Map<String, String> map = new HashMap<>();
//        map.put("code", code);
//        return map;
//    }
    /**
     * 获取门票码
     * @param ticketId
     * @param userId
     * @return
     */
    public Map<String, Object> getAttendCode(String ticketId, String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("请先登录");
        }
        List<MatchAudience> matchAudience = matchServiceProvider.getMatchAudienceService().getMatchAudienceIsPay(ticketId, userId);
        if (matchAudience == null || matchAudience.size() == 0) {
            throw new RuntimeException("请完成购票操作");
        }
        List<String> list = new ArrayList<>();
        for (MatchAudience m : matchAudience) {
            list.add(m.getId()+",aU");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", list);
        return map;
    }


    /**
     * 扫码门票出席
     * @param code 观众出席的ID
     * @return 是否成功
     */
    public MatchAttendVO audienceAttend(String code) {
        if (StringUtils.isEmpty(code)) {
            throw new RuntimeException("点击门票重新试试");
        }
        MatchAudience matchAudience = matchServiceProvider.getMatchAudienceService().selectById(code);
        if (matchAudience == null) {
            throw new RuntimeException("验票失败，请刷新试试");
        }
        if (matchAudience.getAttend()) {
            throw new RuntimeException("门票已进场");
        }
        matchAudience.setAttend(true);
        boolean success = matchServiceProvider.getMatchAudienceService().insertOrUpdate(matchAudience);
        MatchAttendVO vo = new MatchAttendVO();
        UserForMatchVo userForMatchVo = matchServiceProvider.getMatchParticipantService().getUserForMatchVo(matchAudience.getUserId());
        if (success) {
            vo.setHeadImage(addImgUrlPreUtil.getUserImgPre(userForMatchVo.getUrl()));
            vo.setMatchAttendType(MatchAttendType.AUDNENT.description);
            vo.setUserName(userForMatchVo.getNickname());
        }
        return vo;
    }

    /**
     * 参赛者出席
     * @param code
     * @return
     */
    public MatchAttendVO participantAttend(String code) {
        if (StringUtils.isEmpty(code)) {
            throw new RuntimeException("重新点击试试");
        }
        MatchParticipant matchParticipant = matchServiceProvider.getMatchParticipantService().getMatchParticipantById(code);
        if (matchParticipant == null) {
            throw new RuntimeException("您没有参加这个比赛");
        }
        if (matchParticipant.getSpot()) {
            throw new RuntimeException("参赛人员已进场");
        }
        matchParticipant.setSpot(true);
        boolean success = matchServiceProvider.getMatchParticipantService().insertOrUpdate(matchParticipant);
        MatchAttendVO vo = new MatchAttendVO();
        if (success) {
            vo.setHeadImage(addImgUrlPreUtil.getUserImgPre(matchParticipant.getHeadImagesUrl()));
            vo.setMatchAttendType(MatchAttendType.PARTICIPANT.description);
            vo.setUserName(matchParticipant.getUserName());
        }
        return vo;
    }

    /**
     * 修改用户出场次序
     * @param dtos
     * @param userId
     * @param matchId
     * @return
     */
    public boolean updateMatchAppearanceOrder(List<MatchAppearanceDTO> dtos, String userId, String matchId) {
        String kind=matchServiceProvider.getMatchMemberService().getMemberKind(matchId, userId,1);
        if(StringUtils.isBlank(kind)) {
            throw new RuntimeException("你不是比赛的组织成员");
        }
        if (Integer.valueOf(kind) > 3)
            throw new RuntimeException("没有修改权限");
        if (dtos != null || dtos.size() >0) {
            MatchAppearanceService matchAppearanceService = matchServiceProvider.getMatchAppearanceService();
            for (MatchAppearanceDTO dto : dtos) {
                if (!matchAppearanceService.updateAppearanceOrder(dto)) {
                    throw new RuntimeException("更新失败");
                }
            }
        }
        return true;
    }

    /**
     * 所有人员出席
     * @param code
     * @return
     */
    public Map<String, Object> allAttend(String code) {
        if(StringUtils.isBlank(code)) {
            throw new RuntimeException("请确认出席人员");
        }
        MatchAttendVO vo;
        Map<String, Object> map = new HashMap<>(1);
        try {
            String[] codes = code.split(",");
            if ("pA".equals(codes[1])) {
                vo = participantAttend(codes[0]);
            }else {
                vo = audienceAttend(codes[0]);
            }
            map.put("userMessage", vo);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }
}

