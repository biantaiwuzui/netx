package com.netx.worth.biz.match;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.wz.dto.matchEvent.*;
import com.netx.worth.enums.MatchMemberKind;
import com.netx.worth.enums.MatchStatusCode;
import com.netx.worth.enums.MatchTimeTypeEnum;
import com.netx.worth.model.*;
import com.netx.worth.service.*;
import com.netx.worth.vo.*;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 赛事创建的服务实现类
 * @author 老肥猪
 * @since 2018-08-03
 */
@Service
public class MatchCreateAction {
    @Autowired
    private MatchServiceProvider matchServiceProvider;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

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

    /**
     * 网值的图片加上前缀再组合
     * @param matchImageUrls
     * @return
     */
    public String pictures(String matchImageUrls){
        if(!StringUtils.isEmpty(matchImageUrls)){
            String[] matchImageUrl=matchImageUrls.split(",");
            matchImageUrls="";
            for (int i = 0; i < matchImageUrl.length; i++) {
                matchImageUrl[i]=addImgUrlPreUtil.getActivityImgPre(matchImageUrl[i]);
                if(i!=matchImageUrl.length-1){
                    matchImageUrls+=matchImageUrl[i]+",";
                }else {
                    matchImageUrls += matchImageUrl[i];
                }
            }
        }
        return matchImageUrls;
    }

    /**
     * 网商的加上前缀
     * @param url
     * @return
     */
    public String updateImages(String url) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(url)) {
            //判断是否要加前缀
            if (!url.contains("http")) {
                url = addImgUrlPreUtil.getProductImgPre(url);
            }
        }
        return url;
    }
    /**
     * 將查出來票的了做封裝
     * @param matchTicket
     * @return
     */
    public MatchTicketVo letMatchTicketToMatchTicketVo(MatchTicket matchTicket){
        MatchTicketVo matchTicketVo=new MatchTicketVo();
        matchTicketVo.setBeginTime(matchTicket.getBeginTime());
        matchTicketVo.setDescription(matchTicket.getDescription());
        matchTicketVo.setEndTime(matchTicket.getEndTime());
        matchTicketVo.setFree(matchTicket.getFree());
        matchTicketVo.setId(matchTicket.getId());
        matchTicketVo.setMatchZoneId(matchTicket.getZoneId());
        matchTicketVo.setNumber(matchTicket.getNumber());
        matchTicketVo.setTicketName(matchTicket.getTicketName());
        matchTicketVo.setSort(matchTicket.getSort());
        matchTicketVo.setUsedefault(matchTicket.getUseDefalut());
        String[] ids=matchTicket.getVenueIds().split(",");
        if(ids.length!=0&&ids!=null){
            List<MatchVenue> matchVenueList=matchServiceProvider.getMatchVenueService().getMatchVenueListByIds(Arrays.asList(ids));
            List<KindAndVenceVo> kindAndVenceVoList=new ArrayList<>();
            for (MatchVenue matchVenue:
                    matchVenueList) {
                KindAndVenceVo matchKindAndVenceVo=new KindAndVenceVo();
                matchKindAndVenceVo.setKind(matchVenue.getKind());
                MatchProgress matchProgress=matchServiceProvider.getMatchProgressService().getMatchProgressVoById(matchVenue.getKind());
                if(matchProgress!=null) {
                    matchKindAndVenceVo.setKindName(matchProgress.getMatchName());
                }
                matchKindAndVenceVo.setVenueId(matchVenue.getId());
                matchKindAndVenceVo.setVenueName(matchVenue.getTitle());
                matchKindAndVenceVo.setAddress(matchVenue.getAddress());
                matchKindAndVenceVo.setEndTime(matchVenue.getEndTime());
                matchKindAndVenceVo.setStartTime(matchVenue.getBeginTime());
                kindAndVenceVoList.add(matchKindAndVenceVo);
            }
            matchTicketVo.setKindAndVenceVoList(kindAndVenceVoList);
        }
        return  matchTicketVo;
    }

    /**
     * 检测是否有权力删除
     * @param matchDeleteDto
     * @return
     */
    public boolean checkIsOkDelete(MatchDeleteDto matchDeleteDto){
        String kind = matchServiceProvider.getMatchMemberService().getMemberKind(matchDeleteDto.getMatchId(), matchDeleteDto.getUserId(),0);
        if(StringUtils.isBlank(kind)){
            return false;
        }
        if(kind.equals(MatchMemberKind.SPONSOR.getStatus().toString())){
            return true;
        }
        return false;
//        throw new RuntimeException("你没有权利做删除操作");
    }

    /**
     * 删除报名时间表
     * @param matchId
     * @return
     */
    public boolean deleteMatchGroupAndZoneByMatchId(String matchId){
        List<MatchZone> macthZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
        if(macthZoneList.size()==0||macthZoneList==null){
            return  false;
        }
        String matchZones="";
        for (int i = 0; i < macthZoneList.size(); i++) {
            if(i!=macthZoneList.size()-1){
                matchZones+=macthZoneList.get(i)+",";
            }else {
                matchZones+=macthZoneList.get(i);
            }
        }
        return matchServiceProvider.getMatchGroupAndZoneService().deleteMatchGroupAndZoneByZoneId(matchZones);
    }

    /**
     * 更新比赛状态
     * @param matchId
     * @return
     */
    private boolean updateMatchSatus(String matchId) {
        Date nowDate=new Date();
        Date venueEndDate=matchServiceProvider.getMatchVenueService().getLastEndTimeBtMatchId(matchId);
        /**
         * 所有场次都成了
         */
        if(nowDate.getTime()>venueEndDate.getTime()){
            if(matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.END)){
                return true;
            }
        }
        /**
         * 报名购票结束，全面进行中
         */
        Date ticketEndDate=matchServiceProvider.getMatchTicketService().getEndTicketTimeByMatchId(matchId);
        Date applyEndDate=matchServiceProvider.getMatchGroupAndZoneService().getEndApplyTimeByMatchId(matchId);
        if(ticketEndDate!=null) {
            if(nowDate.getTime()>ticketEndDate.getTime()&&nowDate.getTime()>applyEndDate.getTime()){
                if(matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.TICKET_APPLY_END)){
                    return true;
                }
            }
            /**
             * 报名结束，全面进入购票
             */
            if(nowDate.getTime()>applyEndDate.getTime()){
                if(matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.APPLY_END)){
                    return true;
                }
            }
            /**
             * 门票购票结束
             */
            if(nowDate.getTime()>ticketEndDate.getTime()){
                if(matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.TICKET_END)){
                    return true;
                }
            }
        }else {
            /**
             * 报名结束，全面进入购票
             */
            if(nowDate.getTime()>applyEndDate.getTime()){
                if(matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.APPLY_END)){
                    return true;
                }
            }
            if(matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.TICKET_END)){
                return true;
            }
        }
        /**
         * 报名购票都在进行中
         */
        if(matchServiceProvider.getMatchEventService().updateMatchStatus(matchId,MatchStatusCode.ALL_START)){
            return true;
        }
        return false;
    }
//===========================================================================================>Yawn
    /**
     * 创建或者更新比赛
     * @param sendMatchEventDTO 获取赛事详情
     *
     * @return
     */
    @Transactional
    public String addOrUpdateMatch(SendMatchEventDTO sendMatchEventDTO) {
        if (sendMatchEventDTO == null)
            throw new RuntimeException("传入比赛值为空");
        String matchId = matchServiceProvider.getMatchEventService().insertOrUpdateMatch(sendMatchEventDTO);
        if(StringUtils.isBlank(matchId)){
            return null;
        }
        if(matchServiceProvider.getMatchMemberService().getMatchMemberIsHave(sendMatchEventDTO.getInitiatorId(),matchId)){
            MemberDTO memberDTO=new MemberDTO();
            memberDTO.setInNet(true);
            memberDTO.setKind(MatchMemberKind.SPONSOR.getStatus());
            memberDTO.setUserId(sendMatchEventDTO.getInitiatorId());
            memberDTO.setMatchId(matchId);
            if(!matchServiceProvider.getMatchMemberService().invitedMember(memberDTO)){
                throw new RuntimeException("创建赛事发起人失败");
            }
        }
        return matchId;
    }


    /**
     * 获取赛事的基本详情
     * @param matchId
     * @return
     */
    public Map getBaseMatchEvent(String matchId) {
        Map<String,Object> map=new HashMap<>();
        MatchEvent matchEvent=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
        if(matchEvent==null){
            return null;
        }
        if(matchEvent.getMatchStatus()>=MatchStatusCode.AUDIT_PASS.status){
            if(!updateMatchSatus(matchEvent.getId())){
                throw new RuntimeException("更改比赛状态失败");
            }
        }
        matchEvent=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
        if(matchEvent==null){
            return null;
        }
        matchEvent.setMatchImageUrl(pictures(matchEvent.getMatchImageUrl()));
        map.put("matchevent",matchEvent);
        List<MatchReviewVo> matchReviewVoList=matchServiceProvider.getMatchReviewService().getMainReviewAcceptBYMatchId(matchId);
        if(matchReviewVoList!=null&&matchReviewVoList.size()>0) {
            matchReviewVoList.get(0).setLogo(updateImages(matchReviewVoList.get(0).getLogo()));
            List<String> tags=null;
            if(StringUtils.isNoneBlank(matchReviewVoList.get(0).getMerchantId())) {
                tags=matchServiceProvider.getMatchReviewService().getOneTags(matchReviewVoList.get(0).getMerchantId());
            }
            matchReviewVoList.get(0).setTags(tags);
            map.put("mainOrganizer",matchReviewVoList.get(0));
        }
        return map;
    }


    /**
     * 获取发布赛事的填写信息
     * @param matchCheckDTO 哎哎哎
     * @return 获取赛事详情
     */
    public Map<String,Object> getMatchEvent(MatchCheckDTO matchCheckDTO){
        Map<String,Object> map=new HashMap<>();
        String matchId=matchCheckDTO.getMatchId();
        switch (matchCheckDTO.getPage()){
            case 1: MatchEvent matchEvent=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
                    if(matchEvent==null){
                        return null;
                    }
//                    if(matchEvent.getMatchStatus()>=MatchStatusCode.AUDIT_PASS.status){
//                        if(updateMatchSatus(matchEvent.getId())){
//                            throw new RuntimeException("更改购票时间失败");
//                        }
//                    }
                    matchEvent.setMatchImageUrl(pictures(matchEvent.getMatchImageUrl()));
                    map.put("matchevent",matchEvent);
                    map.put("isHaveOrganizer",matchServiceProvider.getMatchReviewService().IsHaveReviewBYMatchId(matchId));
                    break;
            case 2: map.put("matchMembers",matchServiceProvider.getMatchMemberService().IsWriteMatchMember(matchId));
                    map.put("matchPprogress",matchServiceProvider.getMatchProgressService().IsWriteMatchProgress(matchId));
                    map.put("matchGroup",matchServiceProvider.getMatchGruopService().IsWriteMatchGroup(matchId));
                    List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
                    map.put("matchZone",false);
                    if(matchZoneList.size()>0&&matchZoneList!=null){
                        map.put("matchZone",true);
                    }
                    map.put("matchFlow",false);
                    String[] ids=new String[matchZoneList.size()];
                    for (int i=0;i<matchZoneList.size();i++) {
                        ids[i]=matchZoneList.get(i).getId();
                    }
                    map.put("macthApply",false);
                    map.put("ticket",false);
                    if(ids.length>0&&ids!=null){
                        map.put("macthApply",matchServiceProvider.getMatchGroupAndZoneService().IsWriteMatchApply(ids));
                        map.put("ticket",matchServiceProvider.getMatchTicketService().IsWriteMatchTicket(ids));
                    }
                    map.put("matchAward",matchServiceProvider.getMatchAwardService().IsWriteMacthTicket(matchId));
                    break;
            default:return null;
        }
        return map;
    }





    /**
     * 获取所有发布的比赛
     * @return
     */
    public Map<String, Object> listMatchEvent() {
        Map<String, Object> map = new HashMap<>();
        List<MatchEvent> matchEvents = matchServiceProvider.getMatchEventService().matchEventList();
        if (matchEvents!= null &&matchEvents.size()>0){
            for (int i = 0; i < matchEvents.size(); i++) {
                matchEvents.get(i).setMatchImageUrl(pictures(matchEvents.get(i).getMatchImageUrl()));
            }
            map.put("matchEvents",matchEvents);
        }
        return map;
    }

    /**
     * 返回通过审核的比赛
     * @param page
     * @return
     */
    public List<MatchEvent> listMatchEvent(Page<MatchEvent> page) {
        Page<MatchEvent> matchEvents = matchServiceProvider.getMatchEventService().matchEventList(page);
        for (MatchEvent event : matchEvents.getRecords()) {
            event.setMatchImageUrl(pictures(event.getMatchImageUrl()));
        }
        return matchEvents.getRecords();
    }

    /**
     * 返回两个比赛
     * @return
     */
    public Map<String, Object> listTwoMatchEvent() {
        Map<String, Object> map = new HashMap<>();
        Page<MatchEvent> matchEvents = matchServiceProvider.getMatchEventService().matchTwoEventList();
            map.put("matchEvents",matchEvents);
        return map;
    }



    /**
     * 获得所有刚刚添加的人员
     * @param matchId
     * @return
     */
    public Map<String,List> getWorkPeopleAllListByMatchId(String matchId){
        Map<String,List> map=new HashMap<>();
        List<MatchUserInfoVo> userMacthInfoList= matchServiceProvider.getMatchMemberService().getWorkPeopleAllListByMatchId(matchId);
        for (int i = 0; i < userMacthInfoList.size(); i++) {
            userMacthInfoList.get(i).setUrl(addImgUrlPreUtil.getUserImgPre(userMacthInfoList.get(i).getUrl()));
        }
        if(userMacthInfoList.size()>0&&userMacthInfoList!=null){
            map.put("userMacthInfoList",userMacthInfoList);
            return map;
        }
        return null;
    }


    /**
     * 删除单位
     * @param matchDeleteDto
     * @return
     */
    public boolean deleteOrganizer(MatchDeleteDto matchDeleteDto) {
        checkIsOkDelete(matchDeleteDto);
        MatchReviewService matchReviewService = matchServiceProvider.getMatchReviewService();
        if(!matchReviewService.deleteMatchReview(matchDeleteDto.getDeleteId())) {
            throw new RuntimeException("删除举办方" + matchDeleteDto.getDeleteId() + "失败");
        }
        return true;
    }

    /**
     * 获取比赛组织方
     * @param matchId
     * @return
     */
    public Map<String, Object> listOrganizer(String matchId) {
        Map<String, Object> map = new HashMap<>(1);
        List<MatchReviewVo> list = matchServiceProvider.getMatchReviewService().getReviewListBYMatchId(matchId);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setLogo(updateImages(list.get(i).getLogo()));
        }
        map.put("Organizers", list);
        return map;
    }

    /**
     * 添加比赛要求
     * @param requirementDTOList
     * @return
     */
    public boolean addRequirement(List<MatchRequirementDTO> requirementDTOList){
        MatchRequirementService matchRequirementService = matchServiceProvider.getMatchRequirementService();
        for (MatchRequirementDTO dto : requirementDTOList) {
            if(!matchRequirementService.addRequirement(dto)){
                StringBuilder exceptionBuilder = new StringBuilder();
                exceptionBuilder.append(dto.getRequirementName())
                        .append("更新或插入失败");
                throw new RuntimeException(exceptionBuilder.toString());
            }
        }
        return true;
    }

    /**
     * 删除比赛要求
     * @param matchDeleteDto
     * @return
     */
    public boolean deleteRequirement(MatchDeleteDto matchDeleteDto){
        checkIsOkDelete(matchDeleteDto);
        MatchRequirementService matchRequirementService = matchServiceProvider.getMatchRequirementService();
        if (!matchRequirementService.deleteRequirement(matchDeleteDto.getDeleteId())) {
            throw new RuntimeException("delete" + matchDeleteDto.getDeleteId() + "faild");
        }
        return true;
    }

    /**
     * 查询比赛要求列表
     * @param groupId
     * @return
     */
    public Map<String, Object> listMatchRequirement(String groupId) {
        MatchRequirementService matchRequirementService = matchServiceProvider.getMatchRequirementService();
        List<MatchRequirement> matchRequirements = matchRequirementService.listMatchRequirement(groupId);
        Map<String, Object> map = new HashMap<>(1);
        if (matchRequirements != null && matchRequirements.size() > 0) {
            map.put("matchRequirements", matchRequirements);
        }
        return map;
    }

    /**
     * 添加工作人员，嘉宾等
     * @param dtoList
     * @return
     */
    @Transactional
    public boolean addOrUpdateMember(List<MemberDTO> dtoList, String matchId) {
        MatchMemberService matchMemberService = matchServiceProvider.getMatchMemberService();
        if(dtoList.size()==0||dtoList==null) {
            throw new RuntimeException("添加的列表不能为空");
        }
        for (MemberDTO member : dtoList) {
            if(matchMemberService.isContainMember(matchId, member.getUserId())) {
                //跳过这个用户
                continue;
            }
            member.setMatchId(matchId);
            if(!matchMemberService.invitedMember(member)){
                return false;
            }
        }
        return true;
    }
    /**
     * 添加比赛要求
     * @param matchId
     * @param dtoList
     * @return
     */
    public boolean addRequirement(String matchId, List<MatchRequirementDTO>  dtoList) {
        MatchEvent matchEvent = matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
        if (matchEvent == null) {
            throw new RuntimeException("获取赛事失败呐");
        }
        MatchRequirementService matchRequirementService=matchServiceProvider.getMatchRequirementService();
        for (MatchRequirementDTO dto:dtoList){
            if(!matchRequirementService.addRequirement(dto)){
                StringBuilder exceptionBuilder = new StringBuilder();
                exceptionBuilder.append(dto.getRequirementName())
                        .append("更新或插入失败");
                throw new RuntimeException(exceptionBuilder.toString());
            }
        }
        return true;
    }



    /**
     * 赛事删除添加的人员
     * @param matchDeleteDto
     * @return
     */
    public boolean deleteMatchMember(MatchDeleteDto matchDeleteDto) {
        checkIsOkDelete(matchDeleteDto);
        MatchMemberService matchMemberService = matchServiceProvider.getMatchMemberService();
        if (!matchMemberService.deleteMatchMember(matchDeleteDto.getDeleteId())) {
            throw new RuntimeException("删除人员"+ matchDeleteDto.getDeleteId() + "失败");
        }
        return true;
    }

    /**
     * 获取团队成员的List
     * @param macthId
     * @return
     */
    public Map<String, List> getMatchMemberListByMatchId(String macthId){
        Map<String, List> map = new HashMap<>();
        List<MatchMember> list = matchServiceProvider.getMatchMemberService().listMatchMember(macthId);
        map.put("memberList", list);
        return map;
    }

    /**
     * 插入场次
     * @param matchVenueDTOList
     * @return
     */
    @Transactional
    public boolean addOrUpdateMatchVenue(List<MatchVenueDTO> matchVenueDTOList){
        if(matchVenueDTOList.size()==0||matchVenueDTOList==null){
            throw new RuntimeException("不能提交空选项");
        }
        MatchVenueService matchVenueService=matchServiceProvider.getMatchVenueService();
        MatchVenueGroupService matchVenueGroupService = matchServiceProvider.getMatchVenueGroupService();
        for (MatchVenueDTO matchVenueDTO:matchVenueDTOList) {
            MatchVenueAndZoneDto matchVenueAndZoneDto=new MatchVenueAndZoneDto();
            matchServiceProvider.getMatchVenueAndZoneService().addMatchVenueAndZone(matchVenueAndZoneDto);
            String id = matchVenueService.addVenue(matchVenueDTO);
            String progressId = matchVenueDTO.getKind();
            if(StringUtils.isBlank(id)){
                throw new RuntimeException("插入场次"+matchVenueDTO.getTitle()+"失败");
            }
            String[] groupIds = matchVenueDTO.getGroupIds().split(",");
            if (groupIds != null && groupIds .length > 0) {
                for (int i = 0; i < groupIds.length; i++) {
                    MatchVenueGroup m = new MatchVenueGroup();
                    m.setGroupId(groupIds[i]);
                    m.setVenueId(id);
                    m.setProgressId(progressId);
                    if (!matchVenueGroupService.insert(m)) {
                        throw new RuntimeException("插入场次"+matchVenueDTO.getTitle()+"失败");
                    }
                }
            }
        }
        return true;
    }

    /**
     * 获得比赛的所有场次信息通过比赛id
     */
    public Map<String, List>  getMatchAllVenueListByMatchId(String matchId){
        List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getZoneIdListByMatchId(matchId);
        String[] ids=new String[matchZoneList.size()];
        for (int i=0;i<matchZoneList.size();i++) {
            ids[i]=matchZoneList.get(i).getId();
        }
        return getMatchVenueListByMatchZoneId(ids);
    }

    /**
     * 获得比赛的场次信息通过赛组id
     * @param matchZoneIds
     * @return
     */
    public Map<String, List> getMatchVenueListByMatchZoneId(String[] matchZoneIds){
        Map<String, List> map = new HashMap<>();
        List<MatchVenue> list=matchServiceProvider.getMatchVenueService().getMatchVenueListByMatchZoneId(matchZoneIds);
        List<MatchVenueVo> matchVenueVoList=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MatchVenueVo matchVenueVo=new MatchVenueVo();
            MatchVenue matchVenue=list.get(i);
            matchVenueVo.setId(matchVenue.getId());
            matchVenueVo.setZoneId(matchVenue.getZoneId());
            matchVenueVo.setTitle(matchVenue.getTitle());
            matchVenueVo.setBeginTime(matchVenue.getBeginTime());
            matchVenueVo.setEndTime(matchVenue.getEndTime());
            matchVenueVo.setAddress(matchVenue.getAddress());
            matchVenueVo.setSite(matchVenue.getSite());
            matchVenueVo.setSort(matchVenue.getSort());
            matchVenueVo.setSiteImageUrl(pictures(matchVenue.getSiteImageUrl()));
            if(StringUtils.isNoneBlank(matchVenueVo.getZoneId())) {
                MatchZone matchZone=matchServiceProvider.getMatchZoneService().selectById(matchVenueVo.getZoneId());
                matchVenueVo.setZoneName( matchZone.getZoneName());
            }
            String[] ids=matchVenue.getGroupIds().split(",");
            if(ids.length!=0&&ids!=null){
                List<MatchGroup> matchGroupList=matchServiceProvider.getMatchGruopService().getMatchGroupListByIds(Arrays.asList(ids));
                List<GroupAndVenueVo> groupAndVenueVoList=new ArrayList<>();
                for (MatchGroup matchGroup:
                        matchGroupList) {
                    GroupAndVenueVo groupAndVenueVo=new GroupAndVenueVo();
                    groupAndVenueVo.setGroupName(matchGroup.getMatchGroupName());
                    groupAndVenueVo.setMatchGroupId(matchGroup.getId());
                    groupAndVenueVoList.add(groupAndVenueVo);
                }
                matchVenueVo.setGroupAndVenueVoList(groupAndVenueVoList);
            }
            MatchProgress matchProgress=matchServiceProvider.getMatchProgressService().getMatchProgressVoById(matchVenue.getKind());
            MatchProgressVo matchProgressVo=new MatchProgressVo();
            matchProgressVo.setId(matchProgress.getId());
            matchProgressVo.setMatchProgressName(matchProgress.getMatchName());
            matchVenueVo.setMatchProgressVo(matchProgressVo);
            matchVenueVoList.add(matchVenueVo);
        }
        map.put("matchVenueVoList", matchVenueVoList);
        return map;
    }

    /**
     * 删除会场
     * @param matchDeleteDto
     * @return
     */
    @Transactional
    public boolean deleteVenue(MatchDeleteDto matchDeleteDto) {
        checkIsOkDelete(matchDeleteDto);
        if(matchServiceProvider.getMatchApplyDefaultTimeService().deleteMatchApplyDefaultTimeByMacthId(matchDeleteDto.getMatchId())) {
            List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchDeleteDto.getMatchId());
            String[] ids=new String[matchZoneList.size()];
            for (int i = 0; i < matchZoneList.size(); i++) {
                ids[i]=matchZoneList.get(i).getId();
            }
            matchServiceProvider.getMatchTicketService().deleteAllTicketByZoneId(ids);
        }
        if (!matchServiceProvider.getMatchVenueService().deleteVenue(matchDeleteDto.getDeleteId()))
            return false;
        return true;
    }

    /**
     * 添加或更新门票信息
     * @param matchTicketDTOList
     * @return
     */
    @Transactional
    public boolean addOrUpdateMatchTicket(List<MatchTicketDTO> matchTicketDTOList,Date startTime,Date endTime){
        if(matchTicketDTOList.size()==0||matchTicketDTOList==null) {
            throw new RuntimeException("不能提交空选项");
        }
        MatchTicketService matchTicketService=matchServiceProvider.getMatchTicketService();
        for (MatchTicketDTO matchTicketDTO:matchTicketDTOList) {
            if(!matchTicketService.publishTicket(matchTicketDTO)){
                StringBuilder exceptionBuilder = new StringBuilder();
                exceptionBuilder.append(matchTicketDTO.getTicketName())
                        .append("更新或插入失败");
                throw new RuntimeException(exceptionBuilder.toString());
            }
        }
        if(startTime!=null&&endTime!=null) {
            //添加默认门票
            List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchTicketDTOList.get(0).getMatchZoneOrMatchId());
            for (MatchZone matchZone:
                    matchZoneList) {
                List<MatchVenue> matchVenueList=matchServiceProvider.getMatchVenueService().getMatchAllVenueListByMatchZoneIds(matchZone.getId());
                String ids="";
                for (int i=0;i<matchVenueList.size();i++) {
                    if(matchVenueList.size()-1!=i){
                        ids+=matchVenueList.get(i).getId()+",";
                    }else {
                        ids+=matchVenueList.get(i).getId();
                    }
                }
                for (MatchTicketDTO matchTicketDTO:
                        matchTicketDTOList) {
                    if(StringUtils.isBlank(matchTicketDTO.getId())) {
                        matchTicketDTO.setEndTime(endTime);
                        matchTicketDTO.setBeginTime(startTime);
                        matchTicketDTO.setVenueIds(ids);
                        matchTicketDTO.setUseDefalut(true);
                        matchTicketDTO.setDefault(false);
                        matchTicketDTO.setMatchZoneOrMatchId(matchZone.getId());
                        matchServiceProvider.getMatchTicketService().publishTicket(matchTicketDTO);
                    }
                }
            }
        }
        return true;
    }


    /**
     * 获得默认门票档次设置
     * @param matchId
     * @return
     */
    public Map<String,Object> getDefaultTicketByMatchId(String matchId){
        Map<String,Object> map=new HashMap<>();
        List<MatchTicket> matchTicketList=matchServiceProvider.getMatchTicketService().getDefaultTicketByMatchId(matchId);
        if(matchTicketList.size()>0&&matchTicketList!=null){
            map.put("defaultTicket",matchTicketList);
        }
        MatchApplyDefaultTime matchApplyDefaultTime=matchServiceProvider.getMatchApplyDefaultTimeService().getMatchApplyDefaultTimeByMacthId(matchId,1);
        if(matchApplyDefaultTime!=null) {
            map.put("matchApplyDefaultTime",matchApplyDefaultTime);
        }
        return map;
    }
    /**
     * 获取门票的List
     * @param zoneId
     * @return
     */
    public Map<String, Object> getMatchTicketListByMatchId(String zoneId){
        if (StringUtils.isEmpty(zoneId))
            throw new RuntimeException("赛区ID为空");
        Map<String, Object> map = new HashMap<>();
        List<MatchTicket> list=matchServiceProvider.getMatchTicketService().listMatchTicket(zoneId);
        List<MatchTicketVo> matchTicketVoList=new ArrayList<>();
        for (MatchTicket matchTicket:list) {
            MatchTicketVo matchTicketVo=letMatchTicketToMatchTicketVo(matchTicket);
            matchTicketVoList.add(matchTicketVo);
        }
        MatchZone matchZone=matchServiceProvider.getMatchZoneService().selectById(zoneId);
        if(matchZone==null){
            return map;
        }
        map.put("matchDefaultTime", matchServiceProvider.getMatchApplyDefaultTimeService().getMatchApplyDefaultTimeByMacthId(matchZone.getMatchId(),1));
        map.put("matchList", matchTicketVoList);
        return map;
    }

    /**
     * 删除门票通过门票id
     * @param MatchDeleteDto
     * @return
     */
    public boolean deleteTicketId(MatchDeleteDto MatchDeleteDto) {
        checkIsOkDelete(MatchDeleteDto);
        MatchTicketService m = matchServiceProvider.getMatchTicketService();
        if (!m.deleteTicketById(MatchDeleteDto.getDeleteId()))
            return false;
        return true;
    }
    /**
     * 添加或更新奖项(没有对应Service)
     * @param matchAwardDTOList
     * @return
     */
    public boolean addOrUpdateMatchAward(List<MatchAwardDTO> matchAwardDTOList){
        if(matchAwardDTOList.size() == 0 || matchAwardDTOList == null) {
            throw new RuntimeException("请填入相关数据");
        }
        for (MatchAwardDTO matchAwardDTO :matchAwardDTOList) {
            if(!matchServiceProvider.getMatchAwardService().addMatchAward(matchAwardDTO)){
                StringBuilder exceptionBuilder = new StringBuilder();
                exceptionBuilder.append(matchAwardDTO.getAwardName())
                        .append("更新或插入失败");
                throw new RuntimeException(exceptionBuilder.toString());
            }
        }
        return true;
    }

    /**
     * 删除比赛的奖项通过Id
     * @param matchDeleteDto
     * @return
     */
    public boolean deleteMatchAwardByMatchId(MatchDeleteDto matchDeleteDto) {
        checkIsOkDelete(matchDeleteDto);
        MatchAwardService matchAwardService = matchServiceProvider.getMatchAwardService();
        if(!matchAwardService.deleteMatchAwardByMatchId(matchDeleteDto.getDeleteId()))
            return false;
        return true;
    }

    /**
     * 获取比赛奖项列表
     * @param matchId
     * @return
     */
    public Map<String, Object> listMatchAward(String matchId) {
        MatchAwardService matchAwardService = matchServiceProvider.getMatchAwardService();
        List<MatchAward> matchAwards = matchAwardService.listMatchAwardByMatchId(matchId);
        Map<String, Object> map = new HashMap<>(1);
        if (matchAwards != null && matchAwards.size() != 0)
            map.put("list", matchAwards);
        return map;
    }

    /**
     * 获取所有已设置的奖项信息
     * @param matchId
     * @return
     */
    public Map<String, Object> getMatchAwardListByMatchId(String matchId){
        List<MatchAward> list=matchServiceProvider.getMatchAwardService().listMatchAwardByMatchId(matchId);
        Map<String, Object> map = new HashMap<>();
        if(list.size()>0&&list!=null){
            map.put("awardList", list);
            return map;
        }
        return map;
    }


    /**
     * 取消保存的时候删除所有已经写入比赛的信息
     * @param matchDeleteDto
     * @return
     */
    @Transactional
    public boolean deleteAllMatch(MatchDeleteDto matchDeleteDto){
        checkIsOkDelete(matchDeleteDto);
        String matchId=matchDeleteDto.getMatchId();
        matchServiceProvider.getMatchEventService().deleteMatchById(matchId);
        matchServiceProvider.getMatchAwardService().deleteMatchAwardByMatchId(matchId);
        matchServiceProvider.getMatchMemberService().deleteMatchMemberByMatchId(matchId);
        matchServiceProvider.getMatchReviewService().deleteMatchReviewByMatchId(matchId);
        List<MatchGroup> matchGroupList=matchServiceProvider.getMatchGruopService().listMatchGroupByMatch(matchId);
        for (MatchGroup matchGroup:
        matchGroupList) {
            matchServiceProvider.getMatchRequirementService().deleteRequirementByGroupId(matchGroup.getId());
        }
        matchServiceProvider.getMatchGruopService().deleteMatchGroupByMatchId(matchId);
        matchServiceProvider.getMatchProgressService().deleteMatchProgressByMatchId(matchId);
        List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
        for (MatchZone matchZone:
        matchZoneList) {
            matchServiceProvider.getMatchTicketService().deleteTicketByZoneId(matchZone.getId());
            matchServiceProvider.getMatchVenueService().deleteVenueByMatchZoneId(matchZone.getId());
        }
        matchServiceProvider.getMatchZoneService().deleteMatchZoneByMatchId(matchId);
        return true;
    }
    /**
     * 返回所有信息
     * @param matchCheckDTO
     * @return
     */
    @Transactional
    public Map<String,Object> getAllMatch(MatchCheckDTO matchCheckDTO){
        String matchId=matchCheckDTO.getMatchId();
        Map<String,Object> map=new HashMap<String,Object>();
        switch (matchCheckDTO.getPage()){
            case 1:
                    MatchEvent matchEvent=matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
                    matchEvent.setMatchImageUrl(pictures(matchEvent.getMatchImageUrl()));
                    map.put("matchEvent",matchEvent);
                    List<MatchZone> matchZoneLists=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
                    MatchApplyDefaultTime matchApplyDefaultTime=matchServiceProvider.getMatchApplyDefaultTimeService().getMatchApplyDefaultTimeByMacthId(matchId,0);
                    List<MatchApplyInfoVo> MatchApplyInfoVoList=new ArrayList<>();
                    for (int i = 0; i < matchZoneLists.size();i++) {
                        MatchGroupAndZone matchGroupAndZone=matchServiceProvider.getMatchGroupAndZoneService().getZoneDefaultApplyTime(matchZoneLists.get(i).getId());
                        List<MatchApplyInfoVo> matchApplyInfoVos=matchServiceProvider.getMatchGroupAndZoneService().getApplyInfoByZoneId(matchZoneLists.get(i).getId());
                        if(matchGroupAndZone!=null){
                            for (int j = 0; j < matchApplyInfoVos.size(); j++) {
                                matchApplyInfoVos.get(j).setEndTime(matchGroupAndZone.getEndTime());
                                matchApplyInfoVos.get(j).setStartTime(matchGroupAndZone.getStartTime());
                                matchApplyInfoVos.get(j).setZoneName(matchZoneLists.get(i).getZoneName());
                            }
                        }else {
                            for (int j = 0; j < matchApplyInfoVos.size(); j++) {
                                if(matchApplyInfoVos.get(j).isDefulat()){
                                    matchApplyInfoVos.get(j).setEndTime(matchApplyDefaultTime.getEndTime());
                                    matchApplyInfoVos.get(j).setStartTime(matchApplyDefaultTime.getStartTime());
                                }
                                matchApplyInfoVos.get(j).setZoneName(matchZoneLists.get(i).getZoneName());
                            }
                        }
                        MatchApplyInfoVoList.addAll(matchApplyInfoVos);
                    }
                    map.put("matchApplyInfo",MatchApplyInfoVoList);
                    List<MatchAward> matchAwardList=matchServiceProvider.getMatchAwardService().listMatchAwardByMatchId(matchId);
                    map.put("matchAwardList",matchAwardList);
                    List<MatchZone> matchZones= matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
                    List<MatchTicketVo> matchTicketLists=new ArrayList<>();
                    for (int i=0;i<matchZones.size();i++){
                        List<MatchTicket> matchTicketList=matchServiceProvider.getMatchTicketService().getMatchTicketByZoneId(matchZones.get(i).getId());
                        for (MatchTicket matchTicket:
                                matchTicketList) {
                            //将matchTicket做封装
                            MatchTicketVo matchTicketVo=letMatchTicketToMatchTicketVo(matchTicket);
                            matchTicketVo.setMatchZoneId(matchZones.get(i).getZoneName());
                            matchTicketLists.add(matchTicketVo);
                        }
                    }
                    map.put("matchTicketLists",matchTicketLists);
                    return map;
            case 2:
                    List<MatchProgress> matchProgressList=matchServiceProvider.getMatchProgressService().listMatchProgress(matchId);
                    map.put("matchProgressList",matchProgressList);
                    List<Map<String,Object>> maps=new ArrayList<>();
                    List<MatchGroup> matchGroupList=matchServiceProvider.getMatchGruopService().listMatchGroupByMatch(matchId);
                    for (int i=0;i<matchGroupList.size();i++) {
                        Map<String,Object> groupMap=new HashMap<>();
                        groupMap.put("Group",matchGroupList.get(i));
                        List<MatchRequirement> matchRequirementList=matchServiceProvider.getMatchRequirementService().listMatchRequirement(matchGroupList.get(i).getId());
                        groupMap.put("matchRequirementList",matchRequirementList);
                        maps.add(groupMap);
                    }
                    map.put("matchGroupList",maps);
                    List<Map<String,Object>> maps2=new ArrayList<>();
                    List<MatchZone> matchZoneList= matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
                    for (int i=0;i<matchZoneList.size();i++){
                        Map<String,Object> zoneMap=new HashMap<>();
                        zoneMap.put("Zone",matchZoneList.get(i));
                        List<MatchVenueVo> matchVenueVoList=getMatchVenueListByMatchZoneId(new String[]{matchZoneList.get(i).getId()}).get("matchVenueVoList");
                        for (int j = 0; j < matchVenueVoList.size(); j++) {
                            matchVenueVoList.get(j).setSiteImageUrl(pictures(matchVenueVoList.get(j).getSiteImageUrl()));
                        }
                        zoneMap.put("matchVenueVoList",matchVenueVoList);
                        maps2.add(zoneMap);
                    }
                    map.put("matchZoneList",maps2);
                    return map;
            case 3:
                    List<MatchReviewVo> matchReviewList=matchServiceProvider.getMatchReviewService().getReviewListBYMatchId(matchId);
                    for (int i = 0; i < matchReviewList.size(); i++) {
                        matchReviewList.get(i).setLogo(updateImages(matchReviewList.get(i).getLogo()));
                    }
                    map.put("matchReviewList",matchReviewList);
                    return map;

            case 4:
                    List<MatchUserInfoVo> matchMemberList=getWorkPeopleAllListByMatchId(matchId).get("userMacthInfoList");
                    map.put("matchMemberList",matchMemberList);
                    return map;
            default: return null;
        }
    }

    /*******************新增的表格**************************/

    /**
     * 添加默认时间
     * @param matchApplyDefaultTimeDto
     * @return
     */
    @Transactional
    public String addDefaultTime(MatchApplyDefaultTimeDto matchApplyDefaultTimeDto,List<MatchTicketDTO> matchTicketDTOList){
        String timeId=null;
        if(StringUtils.isBlank(matchApplyDefaultTimeDto.getId())){
            timeId=matchServiceProvider.getMatchApplyDefaultTimeService().insertMatchApplyDefaultTime(matchApplyDefaultTimeDto);
            if(matchApplyDefaultTimeDto.getType()==MatchTimeTypeEnum.MATCHTICKET.getType()){
                if(matchTicketDTOList!=null&&matchTicketDTOList.size()>0) {
                    if(!addOrUpdateMatchTicket(matchTicketDTOList,matchApplyDefaultTimeDto.getStartTime(),matchApplyDefaultTimeDto.getEndTime())){
                        throw new RuntimeException("添加门票列表失败，请稍后再试");
                    }
                }
                return timeId;
            }
            List<MatchGroupAndZoneVo> matchGroupAndZoneVoList=matchServiceProvider.getMatchGroupAndZoneService().getAllMacthGroupAndZoneByMatchId(matchApplyDefaultTimeDto.getMatchId());
            for (int i = 0; i < matchGroupAndZoneVoList.size(); i++) {
                MatchGroupAndZoneDto matchGroupAndZoneDto=new MatchGroupAndZoneDto();
                matchGroupAndZoneDto.setStartTime(matchApplyDefaultTimeDto.getStartTime());
                matchGroupAndZoneDto.setEndTime(matchApplyDefaultTimeDto.getEndTime());
                matchGroupAndZoneDto.setMatchGroupId(matchGroupAndZoneVoList.get(i).getMatchGroupId());
                matchGroupAndZoneDto.setMatchZoneId(matchGroupAndZoneVoList.get(i).getMatchZoneId());
                if(!matchServiceProvider.getMatchGroupAndZoneService().addOrUpdateMatchGroupAndZone(matchGroupAndZoneDto)){
                    throw new RuntimeException("插入赛事各个赛区的赛组报名时间失败");
                }
            }
        }else {
            timeId=matchServiceProvider.getMatchApplyDefaultTimeService().updateMatchApplyDefaultTime(matchApplyDefaultTimeDto);
            if(matchApplyDefaultTimeDto.getType()==MatchTimeTypeEnum.MATCHTICKET.getType()){
                if(matchTicketDTOList!=null&&matchTicketDTOList.size()>0) {
                    if(!addOrUpdateMatchTicket(matchTicketDTOList,matchApplyDefaultTimeDto.getStartTime(),matchApplyDefaultTimeDto.getEndTime())){
                        throw new RuntimeException("添加门票列表失败，请稍后再试");
                    }
                }
            }
        }
        return timeId;
    }

    /**
     * 更新赛区的时间
     * @param matchGroupAndZoneDtos
     * @return
     */
    public boolean updateMatchGroupAndZone(List<MatchGroupAndZoneDto> matchGroupAndZoneDtos) {
        if(matchGroupAndZoneDtos.size()==0&&matchGroupAndZoneDtos==null) {
            return false;
        }
        MatchZone matchZone=matchServiceProvider.getMatchZoneService().selectById(matchGroupAndZoneDtos.get(0).getMatchZoneId());
        MatchApplyDefaultTime matchApplyDefaultTime=matchServiceProvider.getMatchApplyDefaultTimeService().getMatchApplyDefaultTimeByMacthId(matchZone.getMatchId(),MatchTimeTypeEnum.APPLYMATCH.getType());
        boolean isUseZoneTime=false;
        MatchGroupAndZoneDto zoneDefaultTime=null;
        for (int i=0;i<matchGroupAndZoneDtos.size();i++) {
            if(matchGroupAndZoneDtos.get(i).isZoneTime()&&matchGroupAndZoneDtos.get(i).isDefault()){
                isUseZoneTime=true;
                zoneDefaultTime=matchGroupAndZoneDtos.get(i);
                break;
            }
        }
        for (int i = 0; i <matchGroupAndZoneDtos.size() ; i++) {
            if(matchGroupAndZoneDtos.get(i).isDefault()&&!matchGroupAndZoneDtos.get(i).isZoneTime()) {
                matchGroupAndZoneDtos.get(i).setEndTime(matchApplyDefaultTime.getEndTime());
                matchGroupAndZoneDtos.get(i).setStartTime(matchApplyDefaultTime.getStartTime());
            }
            if(isUseZoneTime) {
                matchGroupAndZoneDtos.get(i).setEndTime(zoneDefaultTime.getEndTime());
                matchGroupAndZoneDtos.get(i).setStartTime(zoneDefaultTime.getStartTime());
            }
            if(!matchServiceProvider.getMatchGroupAndZoneService().addOrUpdateMatchGroupAndZone(matchGroupAndZoneDtos.get(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 更新默认时间
     * @param matchApplyDefaultTimeDto
     * @return
     */
    public String updateDefaultTime(MatchApplyDefaultTimeDto matchApplyDefaultTimeDto){
        return matchServiceProvider.getMatchApplyDefaultTimeService().updateMatchApplyDefaultTime(matchApplyDefaultTimeDto);
    }

    /**
     * 获取报名的默认时间
     * @param matchId
     * @return
     */
    public Map<String,MatchApplyDefaultTime> getMatchDefaultApplyTime(String matchId){
        if (StringUtils.isBlank(matchId)) {
            throw new RuntimeException("请选定比赛");
        }
        MatchApplyDefaultTime matchApplyDefaultTime=matchServiceProvider.getMatchApplyDefaultTimeService().getMatchApplyDefaultTimeByMacthId(matchId,MatchTimeTypeEnum.APPLYMATCH.getType());
        Map<String,MatchApplyDefaultTime> map=new HashMap<>();
        if(matchApplyDefaultTime!=null){
            map.put("matchApplyDefaultTime",matchApplyDefaultTime);
        }
        return map;
    }

    /**
     * 获取报门票的默认时间
     * @param matchId
     * @return
     */
    public Map<String,MatchApplyDefaultTime> getMatchDefaultTicketTime(String matchId){
        MatchApplyDefaultTime matchApplyDefaultTime=matchServiceProvider.getMatchApplyDefaultTimeService().getMatchApplyDefaultTimeByMacthId(matchId,MatchTimeTypeEnum.MATCHTICKET.getType());
        Map<String,MatchApplyDefaultTime> map=new HashMap<>();
        if(matchApplyDefaultTime!=null){
            map.put("matchTicketDefaultTime",matchApplyDefaultTime);
        }
        return map;
    }


    /**
     * 添加赛组
     * @param dto
     * @return
     */
    public String addMatchGroup(MatchGroupDTO dto) {
        if (dto == null) {
            throw new RuntimeException("请正确填写赛组资料");
        }
        MatchGroupService matchGroupService = matchServiceProvider.getMatchGruopService();
        String groupId=matchGroupService.addMatchGroup(dto);
        return groupId;
    }

    /**
     * 更新赛组信息
     * @param matchGroupDTOList
     * @return
     */
    public boolean updateMatchGroup(List<MatchGroupDTO> matchGroupDTOList) {
        return matchServiceProvider.getMatchGruopService().updateBatchMatchGroup(matchGroupDTOList);
    }

    /**
     * 删除赛组
     * @param matchDeleteDto
     * @return
     */
    @Transactional
    public boolean deleteMatchGroup(MatchDeleteDto matchDeleteDto) {
        checkIsOkDelete(matchDeleteDto);
        matchServiceProvider.getMatchApplyDefaultTimeService().deleteMatchApplyDefaultTimeByMacthId(matchDeleteDto.getMatchId());
        List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchDeleteDto.getMatchId());
        String[] ids=new String[matchZoneList.size()];
        for (int i = 0; i < matchZoneList.size(); i++) {
            ids[i]=matchZoneList.get(i).getId();
        }
        matchServiceProvider.getMatchGroupAndZoneService().deleteMatchGroupAndZoneByZoneId(ids);
        matchServiceProvider.getMatchVenueService().deleteVenueByMatchZoneId(ids);
        MatchGroupService matchGroupService = matchServiceProvider.getMatchGruopService();
        if (!matchGroupService.deleteMatchGroup(matchDeleteDto.getDeleteId()))
            throw new RuntimeException("删除失败");
        return true;
    }

    /**
     * 查看赛组
     * @param matchId
     * @return
     */
    public Map<String, Object> listMatchGroupByMatch(String matchId) {
        Map<String, Object> map = new HashMap<>();
        List<MatchGroup> matchGroups = matchServiceProvider.getMatchGruopService().listMatchGroupByMatch(matchId);
        List<MatchGroupVo> matchGroupVoList=new ArrayList<>();
        for (MatchGroup matchGroup:
        matchGroups) {
            MatchGroupVo matchGroupVo=new MatchGroupVo();
            matchGroupVo.setId(matchGroup.getId());
            matchGroupVo.setMatchGroupName(matchGroup.getMatchGroupName());
            matchGroupVo.setMatchId(matchGroup.getMatchId());
            matchGroupVo.setSort(matchGroup.getSort());
            matchGroupVo.setWirte(matchServiceProvider.getMatchRequirementService().getIsHaveMatchRequirementByGroupId(matchGroup.getId()));
            matchGroupVo.setFree(matchGroup.getFree());
            matchGroupVo.setAutoSelect(matchGroup.getAutoSelect());
            matchGroupVo.setQuota(matchGroup.getQuota());
            matchGroupVoList.add(matchGroupVo);
        }
        if (matchGroupVoList!= null&&matchGroupVoList.size()>0) {
            map.put("matchGroupVoList", matchGroupVoList);
        }
        return map;
    }

    /**
     * 获得赛区的赛组
     * @param zoneId
     * @return
     */
    public Map<String,List> getMatchGroupByZoneId(String zoneId) {
        if(StringUtils.isBlank(zoneId)) {
            throw new RuntimeException("请选定赛区");
        }
        List <MatchGroupAndTimeVo> matchGroupVoList=matchServiceProvider.getMatchGroupAndZoneService().getMatchGroupByZoneId(zoneId);
        Map<String,List> map=new HashMap<>();
        if(matchGroupVoList!=null) {
            map.put("matchGroupVoList",matchGroupVoList);
        }
        return map;
    }
    /**
     * 添加赛区
     * @param matchZoneDto
     * @return
     */
    public String addMatchZone(MatchZoneDto matchZoneDto){
        if(matchZoneDto==null){
            throw new RuntimeException("添加的赛区为空");
        }
        return matchServiceProvider.getMatchZoneService().addMatchZone(matchZoneDto);
    }

    /**
     * 更新赛区
     * @param matchZoneDtoList
     * @return
     */
    public boolean updateMatchZoneList(List<MatchZoneDto> matchZoneDtoList){
        if (matchZoneDtoList == null || matchZoneDtoList.size() == 0) {
            throw new RuntimeException("更新的赛区为空");
        }
        return matchServiceProvider.getMatchZoneService().updateMatchZone(matchZoneDtoList);
    }

    /**
     * 通过id删除赛区
     * @param matchDeleteDto
     * @return
     */
    public boolean deleteMatchZone(MatchDeleteDto matchDeleteDto){
        checkIsOkDelete(matchDeleteDto);
        matchServiceProvider.getMatchApplyDefaultTimeService().deleteMatchApplyDefaultTimeByMacthId(matchDeleteDto.getMatchId());
        List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchDeleteDto.getMatchId());
        String[] ids=new String[matchZoneList.size()];
        for (int i = 0; i < matchZoneList.size(); i++) {
            ids[i]=matchZoneList.get(i).getId();
        }
        matchServiceProvider.getMatchTicketService().deleteAllTicketByZoneId(ids);
        matchServiceProvider.getMatchGroupAndZoneService().deleteMatchGroupAndZoneByZoneId(ids);
        matchServiceProvider.getMatchVenueService().deleteVenueByMatchZoneId(matchDeleteDto.getDeleteId());
        boolean success=matchServiceProvider.getMatchZoneService().deleteMatchZone(matchDeleteDto.getDeleteId());
        return success;
    }

    /**
     * 通过ids删除赛区
     * @param ids
     * @return
     */
    public boolean deleteBatchMatchZone(List<String> ids){
        if(ids.size()==0||ids==null){
            throw new RuntimeException("赛区id为空");
        }
        return matchServiceProvider.getMatchZoneService().deleteBatchMatchZone(ids);
    }

    /**
     * 通过比赛id获取赛区信息
     * @param matchId
     * @return
     */
    public Map<String,List> getMatchZoneListByMatchId(String matchId){
        Map<String,List> map=new HashMap<>();
        List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
        List<MatchZoneVo> matchZoneVoList=new ArrayList<>();
        if(matchZoneList.size()>0&&matchZoneList!=null){
            for (int i = 0; i < matchZoneList.size(); i++) {
                MatchZoneVo matchZoneVo=new MatchZoneVo();
                MatchZone matchZone=matchZoneList.get(i);
                matchZoneVo.setId(matchZone.getId());
                matchZoneVo.setMatchId(matchZone.getMatchId());
                matchZoneVo.setSort(matchZone.getSort());
                matchZoneVo.setZoneAdress(matchZone.getZoneAdress());
                matchZoneVo.setZoneName(matchZone.getZoneName());
                matchZoneVo.setZoneSite(matchZone.getZoneSite());
                matchZoneVo.setIswrite(matchServiceProvider.getMatchVenueService().isHaveMatchVenueByMatchZoneId(matchZone.getId()));
                matchZoneVoList.add(matchZoneVo);
            }
            map.put("matchZoneVoList",matchZoneVoList);
        }
        return map;
    }

    /**
     * 添加或更新赛程
     * @param dtos
     * @return
     */
    public boolean addMatchProgress(List<MatchProgressDTO> dtos) {
        if (dtos == null || dtos.size() == 0) {
            throw new RuntimeException("添加的赛程为空");
        }
        MatchProgressService matchProgressService = matchServiceProvider.getMatchProgressService();
        for (MatchProgressDTO dto : dtos) {
            if (!matchProgressService.addMatchProgress(dto))
                return false;
        }
        return true;
    }

    /**
     * 删除赛程
     * @param matchDeleteDto
     * @return
     */
    public boolean deleteMatchProgress(MatchDeleteDto matchDeleteDto) {
        checkIsOkDelete(matchDeleteDto);
        matchServiceProvider.getMatchApplyDefaultTimeService().deleteMatchApplyDefaultTimeByMacthId(matchDeleteDto.getMatchId());
        List<MatchZone> matchZoneList=matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchDeleteDto.getMatchId());
        String[] ids=new String[matchZoneList.size()];
        for (int i = 0; i < matchZoneList.size(); i++) {
            ids[i]=matchZoneList.get(i).getId();
        }
        matchServiceProvider.getMatchTicketService().deleteAllTicketByZoneId(ids);
        matchServiceProvider.getMatchGroupAndZoneService().deleteMatchGroupAndZoneByZoneId(ids);
        matchServiceProvider.getMatchVenueService().deleteVenueByMatchZoneId(ids);
        MatchProgressService matchProgressService = matchServiceProvider.getMatchProgressService();
        if (!matchProgressService.deleteMatchProgress(matchDeleteDto.getDeleteId()))
            return false;
        return true;
    }
    /**
     * 查看赛程
     * @param matchId matchId
     * @return
     */
    public Map<String, Object> listMatchProgress(String matchId) {
        if (StringUtils.isBlank(matchId)) {
            throw new RuntimeException("请指定查看赛程的比赛");
        }
        Map<String, Object> map = new HashMap<>(1);
        List<MatchProgress> data = matchServiceProvider.getMatchProgressService().listMatchProgress(matchId);
        if (data != null && data.size() > 0) {
            map.put("data", data);
        }
        return map;
    }
    /**
     * 更新流程
     * @param matchFlowDTOList
     * @return
     */
    public boolean updateFlow(List<MatchFlowDTO> matchFlowDTOList) {
        return  matchServiceProvider.getMatchVenueService().updateVenueByIds(matchFlowDTOList);
    }


}
