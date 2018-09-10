package com.netx.worth.biz.match;

import com.baomidou.mybatisplus.plugins.Page;

import com.netx.common.redis.RedisInfoHolder;


import com.netx.common.user.util.AddImgUrlPreUtil;

import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.matchEvent.*;
import com.netx.utils.cache.RedisCache;

import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.utils.format.DateFormatUtils;
import com.netx.utils.sign.Base64;
import com.netx.worth.model.*;
import com.netx.worth.service.MatchAudienceService;
import com.netx.worth.service.MatchParticipantService;
import com.netx.worth.service.MatchServiceProvider;
import com.netx.worth.service.MatchTicketService;
import com.netx.worth.util.NetWorthMatchResult;
import com.netx.worth.vo.*;
import org.apache.commons.lang.StringUtils;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.transport.ObjectTable;

import java.util.*;
import java.util.concurrent.DelayQueue;

/**
 * 赛事报名服务实现类
 *
 * @author 老肥猪
 * @since 2018-08-03
 */
@Service
public class MatchApplyAction {

    @Autowired
    private MatchServiceProvider matchServiceProvider;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    @Autowired
    private MatchCreateAction matchCreateAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;


    private RedisCache redisCache;
    private final String TICKETCODE = "TICKETCODE";

    /**
     * 获得redis的方法
     *
     * @return
     */
    private RedisCache clientRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }


    /**
     * 团体报名
     *
     * @param matchParticpantAllDto
     * @return
     */
    @Transactional
    public String addParticipantFree(MatchParticpantAllDto matchParticpantAllDto, String userId) {
        MatchParticipantDTO matchParticipantDTO = matchParticpantAllDto.getMatchParticipantDTO();
        List<MatchChildInfoDTO> matchChildInfoList = matchParticpantAllDto.getMatchChildInfoDTOList();
        if (matchParticipantDTO == null)
            throw new RuntimeException("报名信息为空");
        if (matchServiceProvider.getMatchParticipantService().getMatchParticipantIsHave(matchParticipantDTO.getMatchId(), matchParticipantDTO.getUserId())) {
            throw new RuntimeException("您已报名");
        }
        UserForMatchVo u = matchServiceProvider.getMatchParticipantService().getUserForMatchVo(userId);
        matchParticipantDTO.setSex(u.getSex());
        matchParticipantDTO.setBirthday(u.getBirthday());
        matchParticipantDTO.setUserName(u.getNickname());
        matchParticipantDTO.setHeadImagesUrl(u.getUrl());
        //计算报名人数是否饱和，默认先报名就有优先通过的权力,并插入默认入选者
        matchParticipantDTO=addAppearanceService(matchParticipantDTO);
        String id = matchServiceProvider.getMatchParticipantService().addParticipant(matchParticipantDTO, true, 2);
        List<MatchRequirementDataDTO> requirementData = matchParticipantDTO.getImage_message();
        addImageData(requirementData, id, matchParticipantDTO, false);
        for (MatchChildInfoDTO childInfo : matchChildInfoList) {
            childInfo.setParticipantId(id);
            String childId = matchServiceProvider.getMatchChildInfoService().addChildrenInfoForUser(childInfo);
            if (StringUtils.isBlank(childId)) {
                throw new RuntimeException("填入.." + childInfo.getName() + "信息失败");
            }
            addImageData(childInfo.getMatchRequirementDataDTOList(), childId, matchParticipantDTO, true);
        }
        return id;
    }


    /**
     * 个人报名
     *
     * @param matchParticipantDTO
     * @return
     */
    @Transactional
    public String addParticipantFreeAlone(MatchParticipantDTO matchParticipantDTO) {
        if (matchParticipantDTO == null)
            throw new RuntimeException("报名信息为空");
        if (matchServiceProvider.getMatchParticipantService().getMatchParticipantIsHave(matchParticipantDTO.getMatchId(), matchParticipantDTO.getUserId())) {
            throw new RuntimeException("您已报名");
        }
        String id = matchServiceProvider.getMatchParticipantService().addParticipant(matchParticipantDTO, true, 2);
        List<MatchRequirementDataDTO> requirementData = matchParticipantDTO.getImage_message();
        //添加各个照片资料
        addImageData(requirementData, id, matchParticipantDTO, false);
        return id;
    }

    /**
     * 用户报名
     *
     * @param matchParticipantDTO
     * @return
     */
    @Transactional
    public String addParticipantFreeUser(MatchParticipantDTO matchParticipantDTO, String userId) {
        if (matchParticipantDTO == null)
            throw new RuntimeException("报名信息为空");
        if (matchServiceProvider.getMatchParticipantService().getMatchParticipantIsHave(matchParticipantDTO.getMatchId(), matchParticipantDTO.getUserId())) {
            throw new RuntimeException("您已报名");
        }
        UserForMatchVo u = matchServiceProvider.getMatchParticipantService().getUserForMatchVo(userId);
        matchParticipantDTO.setSex(u.getSex());
        matchParticipantDTO.setBirthday(u.getBirthday());
        matchParticipantDTO.setUserName(u.getNickname());
        matchParticipantDTO.setHeadImagesUrl(u.getUrl());
        //计算报名人数是否饱和，默认先报名就有优先通过的权力,并插入默认入选者
        matchParticipantDTO=addAppearanceService(matchParticipantDTO);
        String id = matchServiceProvider.getMatchParticipantService().addParticipant(matchParticipantDTO, true, 2);
        List<MatchRequirementDataDTO> requirementData = matchParticipantDTO.getImage_message();
        addImageData(requirementData, id, matchParticipantDTO, false);
        //添加各个照片资料


        return id;
    }
    //插入默认的入选者
    public MatchParticipantDTO addAppearanceService(MatchParticipantDTO matchParticipantDTO) {
        MatchGroup matchGroup=matchServiceProvider.getMatchGruopService().selectById(matchParticipantDTO.getGroupId());
        if(matchGroup!=null) {
            Integer count=matchServiceProvider.getMatchParticipantService().getCountBYGroupAndZoneId(matchParticipantDTO.getZoneId(),matchParticipantDTO.getGroupId());
            if(matchGroup.getQuota()!=null) {
                if(matchGroup.getQuota()>=count) {
                    matchParticipantDTO.setPass(true);
                    MatchProgress matchProgress=matchServiceProvider.getMatchProgressService().getMinMatchProgressByMatchId(matchParticipantDTO.getMatchId());
                    if(matchProgress!=null) {
                        if (!matchServiceProvider.getMatchAppearanceService().addAppearance(matchParticipantDTO, matchProgress.getId())) {
                            throw new RuntimeException("挑选入选者失败，请稍后重试");
                        }
                    }
                }
            }else {
                matchParticipantDTO.setPass(true);
                MatchProgress matchProgress=matchServiceProvider.getMatchProgressService().getMinMatchProgressByMatchId(matchParticipantDTO.getMatchId());
                if(matchProgress!=null) {
                    if (!matchServiceProvider.getMatchAppearanceService().addAppearance(matchParticipantDTO, matchProgress.getId())) {
                        throw new RuntimeException("挑选入选者失败，请稍后重试");
                    }
                }
            }

        }
        return matchParticipantDTO;
    }

    /**
     * 个人付费的添加
     *
     * @param matchParticipantDTO
     * @param token
     * @return
     */
    @Transactional
    public Map<String, String> addParticipantPayUser(MatchParticipantDTO matchParticipantDTO, String token, String userId) {
        MatchParticipantService matchParticipantService = matchServiceProvider.getMatchParticipantService();
        if (matchServiceProvider.getMatchParticipantService().getMatchParticipantIsHave(matchParticipantDTO.getMatchId(), matchParticipantDTO.getUserId())) {
            throw new RuntimeException("您已报名该比赛");
        }
        UserForMatchVo u = matchServiceProvider.getMatchParticipantService().getUserForMatchVo(userId);
        matchParticipantDTO.setSex(u.getSex());
        matchParticipantDTO.setBirthday(u.getBirthday());
        matchParticipantDTO.setUserName(u.getNickname());
        matchParticipantDTO.setHeadImagesUrl(u.getUrl());
        String matchParticipantId = matchParticipantService.addParticipant(matchParticipantDTO, false, 1);
        List<MatchRequirementDataDTO> requirementData = matchParticipantDTO.getImage_message();
        //添加各个照片资料
        addImageData(requirementData, matchParticipantId, matchParticipantDTO, false);

        Map<String, String> map = new HashMap<>();
        map.put("matchParticipantId", matchParticipantId);
        String payId = getTicketCodeByToken(matchParticipantDTO.getUserId(), token);
        map.put("payId", payId);
        return map;
    }

    public void addImageData(List<MatchRequirementDataDTO> requirementData, String id, MatchParticipantDTO matchParticipantDTO, boolean isChildOrTeam) {
        if (requirementData != null && requirementData.size() > 0) {
            for (MatchRequirementDataDTO dataDTO : requirementData) {
                MatchRequirementData imageData = new MatchRequirementData();
                imageData.setUserId(id);
                imageData.setRequirementId(dataDTO.getRequirementId());
                imageData.setImagesUrl(dataDTO.getImagesUrl());
                imageData.setIntroduction(dataDTO.getIntroduction());
                imageData.setRequirementTitle(dataDTO.getRequirementTitle());
                //公用的
                imageData.setMatchId(matchParticipantDTO.getMatchId());
                imageData.setZoneId(matchParticipantDTO.getZoneId());
                imageData.setGroupId(matchParticipantDTO.getGroupId());
                imageData.setChildOrTeam(isChildOrTeam);
                if (!matchServiceProvider.getMatchRequirementDataService().insert(imageData)) {
                    throw new RuntimeException("上传报名资料失败");
                }
            }
        }
    }

    public Map<String, Object> getUserForMatchVo(String userId) {
        Map<String, Object> map = new HashMap<>();
        UserForMatchVo u = matchServiceProvider.getMatchParticipantService().getUserForMatchVo(userId);
        if (u != null) {
            map.put("userInfo", u);
        }
        return map;
    }

    /**
     * 删除参赛的成员
     *
     * @param matchDeleteDto
     * @return
     */
    @Transactional
    public boolean deleteParticipantById(MatchDeleteDto matchDeleteDto) {
        matchCreateAction.checkIsOkDelete(matchDeleteDto);
        // 删除参赛者信息
        if (!matchServiceProvider.getMatchParticipantService().deleteParticipantById(matchDeleteDto.getDeleteId())) {
            throw new RuntimeException("删除" + matchDeleteDto.getDeleteId() + "失败");
        }
        // 删除上传图片信息
        if (!matchServiceProvider.getMatchRequirementDataService().deleteImageDataByUserId(matchDeleteDto.getDeleteId())) {
            throw new RuntimeException("删除" + matchDeleteDto.getDeleteId() + "失败");
        }
        return true;
    }

    /**
     * 获取参赛的成员
     *
     * @param matchId
     * @return
     */
    public Map<String, Object> listMatchParticipant(String matchId, Page<MatchParticipant> page) {
        if (matchId == null)
            throw new RuntimeException("传入的matchId 为空");
        Map<String, Object> map = new HashMap<>();
        List<MatchParticipant> matchParticipants = matchServiceProvider.getMatchParticipantService().listMatchParticipant(matchId, page);
        if (matchParticipants != null && matchParticipants.size() > 0) {
            map.put("matchParticipants", matchParticipants);
        } else {
            map.put("matchParticipants", "没有数据");
        }
        return map;
    }

    /**
     * 获取已经支付的参赛者
     *
     * @param matchId
     * @return
     */
    public Map<String, Object> listPaidMatchParticipant(String matchId) {
        if (matchId == null)
            throw new RuntimeException("传入的matchId 为空");
        Map<String, Object> map = new HashMap<>();
        List<MatchParticipant> matchParticipants = matchServiceProvider.getMatchParticipantService().listPaidMatchParticipant(matchId);
        if (matchParticipants != null && matchParticipants.size() > 0) {
            map.put("matchParticipants", matchParticipants);
        }
        return map;
    }

    /**
     * 通过审核
     * 同时将参赛者放入表演列表
     *
     * @param ids
     * @return
     */
    @Transactional
    public boolean passReview(List<String> ids, String progressId) {
        if (ids == null || ids.size() == 0)
            throw new RuntimeException("请选择参赛者");
        MatchParticipantService matchParticipantService = matchServiceProvider.getMatchParticipantService();
        for (String participantId : ids) {
            if (!matchParticipantService.passReview(participantId)) {
                throw new RuntimeException("通过审核失败");
            }
            MatchParticipant matchParticipant = matchParticipantService.getMatchParticipantById(participantId);
            if (matchServiceProvider.getMatchAppearanceService().hadAppearance(progressId, matchParticipant.getId(), matchParticipant.getGroupId())) {
                //如果已经有这个参赛者
                continue;
            }
            if (!matchServiceProvider.getMatchAppearanceService().addAppearance(matchParticipant, progressId)) {
                throw new RuntimeException("通过审核失败啦");
            }
        }
        return true;
    }

    /**
     * 通过入选者
     * @param matchUpDateDtoList
     * @return
     */
    @Transactional
    public boolean affirmFinalists(List<MatchUpDateDto> matchUpDateDtoList, String matchId) {
        MatchParticipantService matchParticipantService = matchServiceProvider.getMatchParticipantService();
        List<MatchParticipant> matchParticipantList = new ArrayList<>();
        MatchProgress matchProgress = matchServiceProvider.getMatchProgressService().getMinMatchProgressByMatchId(matchId);
        if (matchUpDateDtoList.size() > 0 && matchUpDateDtoList != null) {
            String[] ids = new String[matchUpDateDtoList.size()];
            System.out.print(ids.length);
            for (int i = 0; i < matchUpDateDtoList.size(); i++) {
                ids[i] = matchUpDateDtoList.get(i).getId();
            }
            matchServiceProvider.getMatchAppearanceService().deleteAppearanceByPerformerIds(ids);
        }
        for (MatchUpDateDto matchUpDateDto :
                matchUpDateDtoList) {
            MatchParticipant matchParticipant = matchParticipantService.getMatchParticipantById(matchUpDateDto.getId());
            matchParticipant.setPass(matchUpDateDto.getSelected());
            matchParticipantList.add(matchParticipant);
            if (matchUpDateDto.getSelected()) {
                if (!matchServiceProvider.getMatchAppearanceService().addAppearance(matchParticipant, matchProgress.getId())) {
                    throw new RuntimeException("挑选入选者失败，请稍后重试");
                }
            }
        }
        if (!matchParticipantService.updateParticipantByIds(matchParticipantList)) {
            throw new RuntimeException("挑选入选者失败，请稍后重试");
        }
        return true;
    }

    /**
     * 参赛人员晋级下一轮比赛
     *
     * @param ids
     * @param progressId
     * @return
     */
    @Transactional
    public boolean passMatch(List<String> ids, String progressId) {
        if (ids == null || ids.size() == 0) {
            throw new RuntimeException("请选择参赛者");
        }
        if (StringUtils.isBlank(progressId)) {
            throw new RuntimeException("请选择赛程");
        }
        MatchParticipantService matchParticipantService = matchServiceProvider.getMatchParticipantService();
        for (String participantId : ids) {
            MatchParticipant matchParticipant = matchParticipantService.getMatchParticipantById(participantId);
            if (matchServiceProvider.getMatchAppearanceService().hadAppearance(progressId, matchParticipant.getId(), matchParticipant.getGroupId())) {
                //如果已经有这个参赛者
                continue;
            }
            if (!matchServiceProvider.getMatchProgressParticipantService().addMatchParticipantProgress(progressId, participantId)) {
                throw new RuntimeException("晋级错误");
            }
            if (!matchServiceProvider.getMatchAppearanceService().addAppearance(matchParticipant, progressId)) {
                throw new RuntimeException("晋级错误");
            }
        }
        return true;
    }

    /**
     * 通过赛区ID查询所有参赛者
     *
     * @param matchId
     * @param zoneId
     * @return
     */
    public Map<String, Object> listMatchParticipantByZone(String matchId, String zoneId, Page<MatchParticipant> page) {
        if (StringUtils.isBlank(matchId)) {
            throw new RuntimeException("请确认比赛");
        }
        if (StringUtils.isBlank(zoneId)) {
            throw new RuntimeException("请选择赛区");
        }
        List<MatchParticipant> participants = matchServiceProvider.getMatchParticipantService().listMatchParticipantByZone(matchId, zoneId, page);
        Map<String, Object> map;
        map = addParticipantVoToMap(participants);
        return map;
    }

    /**
     * 构造 map 用的
     */
    private Map<String, Object> addParticipantVoToMap(List<MatchParticipant> participants) {
        Map<String, Object> map = new HashMap<>();
        if (participants != null && participants.size() > 0) {
            List<MatchParticipantReviewVo> reviewVos = new ArrayList<>();
            for (MatchParticipant participant : participants) {
                //      String imageData = matchServiceProvider.getMatchRequirementDataService().getImageUrlByUserId(participant.getId());
                //      reviewVo.setHeadImg(StringUtils.isBlank(imageData) ? "defaultImage" : imageData);
                MatchParticipantReviewVo reviewVo = new MatchParticipantReviewVo();
                reviewVo.setGuardian(participant.getGuardian());
                reviewVo.setTeam(participant.getTeam());
                reviewVo.setAge(participant.getBirthday() == null ?
                        0 : DateFormatUtils.getAgeByBirth(participant.getBirthday()));
                reviewVo.setHeadImg(participant.getHeadImagesUrl());
                reviewVo.setId(participant.getId());
                reviewVo.setPass(participant.getPass() == null ? false : participant.getPass());
                reviewVo.setPay(participant.getPay() == null ? false : participant.getPay());
                reviewVo.setSpot(participant.getSpot() == null ? false : participant.getSpot());
                reviewVo.setRequirement_text(participant.getRequirementText() == null ? "default" : participant.getRequirementText());
                reviewVo.setName(participant.getUserName() == null ? "" : participant.getUserName());
                reviewVo.setProjectName(participant.getProjectName() == null ? "" : participant.getProjectName());
                reviewVos.add(reviewVo);
            }
            map.put("participants", reviewVos);
        }
        return map;
    }

    /**
     * 根据赛组选择参赛用户
     *
     * @param zoneId
     * @param groupId
     * @return
     */
    public Map<String, Object> listMatchParticipantByGroupe(String zoneId, String groupId, Integer pageCount, Integer pageSize) {
        if (StringUtils.isBlank(zoneId)) {
            throw new RuntimeException("请确认比赛");
        }
        if (StringUtils.isBlank(groupId)) {
            throw new RuntimeException("请选择赛组");
        }
        Map<String, Object> map = new HashMap<>();
        List<MatchParticipantAllVo> matchParticipantAllVos = matchServiceProvider.getMatchParticipantService().getJionMatchIsPassListByMatchId(zoneId, groupId, pageCount, pageSize);
        for (int i = 0; i < matchParticipantAllVos.size(); i++) {
            matchParticipantAllVos.get(i).setUrl(addImgUrlPreUtil.getUserImgPre(matchParticipantAllVos.get(i).getUrl()));
        }
        map.put("list", matchParticipantAllVos);
        return map;
    }
    /**
     * 根据赛组选择参赛用户
     *
     * @param zoneId
     * @param groupId
     * @return
     */
    public Map<String, Object> listMatchParticipantByGroupAndStatus(String zoneId, String groupId,Integer status) {
        if (StringUtils.isBlank(zoneId)) {
            throw new RuntimeException("请确认比赛");
        }
        if (StringUtils.isBlank(groupId)) {
            throw new RuntimeException("请选择赛组");
        }
        Map<String, Object> map = new HashMap<>();
        Integer spot=matchServiceProvider.getMatchParticipantService().getCountSpotBYGroupAndZoneId(zoneId,groupId,true);
        Integer nospot=matchServiceProvider.getMatchParticipantService().getCountSpotBYGroupAndZoneId(zoneId,groupId,false);
        if(status==2) {
            List<MatchParticipantAllVo> matchParticipantAllVos = matchServiceProvider.getMatchParticipantService().getJionMatchAllListByMatchId(zoneId, groupId);
            for (int i = 0; i < matchParticipantAllVos.size(); i++) {
                matchParticipantAllVos.get(i).setUrl(addImgUrlPreUtil.getUserImgPre(matchParticipantAllVos.get(i).getUrl()));
            }
            map.put("list", matchParticipantAllVos);
            map.put("spot",spot);
            map.put("nospot",nospot);
            return map;
        }else if(status==0) {
            List<MatchParticipantAllVo> matchParticipantAllVos = matchServiceProvider.getMatchParticipantService().getJionMatchIsSpotListByMatchId(zoneId, groupId);
            for (int i = 0; i < matchParticipantAllVos.size(); i++) {
                matchParticipantAllVos.get(i).setUrl(addImgUrlPreUtil.getUserImgPre(matchParticipantAllVos.get(i).getUrl()));
            }
            map.put("list", matchParticipantAllVos);
            map.put("spot",spot);
            map.put("nospot",nospot);
            return map;
        }else  if(status==1) {
            List<MatchParticipantAllVo> matchParticipantAllVos = matchServiceProvider.getMatchParticipantService().getJionMatchNotSpotListByMatchId(zoneId, groupId);
            for (int i = 0; i < matchParticipantAllVos.size(); i++) {
                matchParticipantAllVos.get(i).setUrl(addImgUrlPreUtil.getUserImgPre(matchParticipantAllVos.get(i).getUrl()));
            }
            map.put("list", matchParticipantAllVos);
            map.put("spot",spot);
            map.put("nospot",nospot);
            return map;
        }
        return map;
    }

    /**
     * 通过参赛者id获取资料信息
     *
     * @param participantId
     * @return
     */
    public Map<String, Object> getRequirementData(String participantId) {
        List<MatchRequirementData> matchRequirementDataList = matchServiceProvider.getMatchRequirementDataService().listUserRequirementData(participantId);
        if (matchRequirementDataList.size() == 0 && matchRequirementDataList == null) {
            return null;
        }
        for (int i = 0; i < matchRequirementDataList.size(); i++) {
            matchRequirementDataList.get(i).setImagesUrl(matchCreateAction.pictures(matchRequirementDataList.get(i).getImagesUrl()));
        }
        Map<String, Object> map = new HashMap<>();
        if (matchRequirementDataList != null && matchRequirementDataList.size() > 0) {
            map.put("matchRequirementDataList", matchRequirementDataList);
        }
        return map;
    }


    /**
     * 返回报名用户的状态
     *
     * @param matchId
     * @param userId
     * @return
     */
    @Transactional
    public Map<String, Object> listParticipantByUser(String matchId, String userId) {
        Map<String, Object> map = new HashMap<>(1);
        MatchParticipantVo participantVo = matchServiceProvider.getMatchParticipantService().listUserParticipant(matchId, userId);
        /*
        更好用户头像，不用了
         */
//        if (participantVos != null && participantVos.size() > 0) {
//            for (MatchParticipantVo vo : participantVos) {
//                String imageData = matchServiceProvider.getMatchRequirementDataService().getImageUrlByUserId(vo.getAttendCode());
//                vo.setImageUrl(imageData);
//            }
//        }
        if (participantVo != null) {
            if (participantVo.getStatus() == 1) {
                if (!checkApplyCodeTimeOut(userId, matchId)) {
                    map.put("status", 0);
                    return map;
                }
                Long time = getApplyCodePastDue(userId);
                map.put("time", time);
                map.put("status", 1);
            } else if (participantVo.getStatus() == 2) {
                map.put("status", 2);
            }
            participantVo.setAttendCode(participantVo.getAttendCode() + ",pA");
            participantVo.setImageUrl(addImgUrlPreUtil.getUserImgPre(participantVo.getImageUrl()));
            map.put("data", participantVo);
            if (StringUtils.isNotBlank(participantVo.getGroupId())) {
                Float free = matchServiceProvider.getMatchGruopService().selectById(participantVo.getGroupId()).getFree();
                map.put("money", free);
            }
        } else {
            map.put("status", 0);
        }
        return map;
    }

    /**
     * 创建团队
     *
     * @param matchTeamDTO
     * @return
     */
    public boolean createMatchTeam(MatchTeamDTO matchTeamDTO) {
        return matchServiceProvider.getMatchTeamService().addTeam(matchTeamDTO);
    }

    /**
     * 删除孩子
     *
     * @param matchDeleteDto
     * @return
     */
    public boolean deleteChild(MatchDeleteDto matchDeleteDto) {
        matchCreateAction.checkIsOkDelete(matchDeleteDto);
        if (!matchServiceProvider.getMatchChildInfoService().deleteChildrenInfoByChildId(matchDeleteDto.getDeleteId()))
            throw new RuntimeException("删除" + matchDeleteDto.getDeleteId() + "失败");
        return true;
    }

//    /**
//     * 添加未成年人
//     *
//     * @param dtos
//     * @return
//     */
//    public boolean addChild(List<MatchChildInfoDTO> dtos) {
//        for (MatchChildInfoDTO dto : dtos) {
//            if (!matchServiceProvider.getMatchChildInfoService().addChildrenInfo(dto))
//                throw new RuntimeException("填入.." + dto.getName() + "信息失败");
//        }
//        return true;
//    }

    /**
     * 获取指定监护人的未成年人List
     */
    public Map<String, Object> getChildList(String participantId) {
        if (StringUtils.isEmpty(participantId))
            throw new RuntimeException("请选定需要查看的队伍");
        List<MatchChildInfo> list = matchServiceProvider.getMatchChildInfoService().listChildInfoByParticipantId(participantId,null);
        Map<String, Object> map = new HashMap<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (StringUtils.isNotBlank(list.get(i).getImagesUrl())) {
                    list.get(i).setImagesUrl(addImgUrlPreUtil.getUserImgPre(list.get(i).getImagesUrl()));
                }
                if (StringUtils.isNotBlank(list.get(i).getHeadImage())) {
                    list.get(i).setHeadImage(matchCreateAction.pictures(list.get(i).getHeadImage()));
                }
            }
            if (list.size() > 0 && list != null) {
                if (list.size() == 1 && StringUtils.isBlank(list.get(0).getUserId()) && StringUtils.isBlank(list.get(0).getMobile())) {
                    List<MatchRequirementData> matchRequirementDataList = matchServiceProvider.getMatchRequirementDataService().listChildRequirementData(list.get(0).getId());
                    for (int i = 0; i < matchRequirementDataList.size(); i++) {
                        if (StringUtils.isNotBlank(matchRequirementDataList.get(i).getImagesUrl())) {
                            matchRequirementDataList.get(i).setImagesUrl(matchCreateAction.pictures(matchRequirementDataList.get(i).getImagesUrl()));
                        }
                    }
                    if (matchRequirementDataList.size() > 0 && matchRequirementDataList != null) {
                        map.put("data", matchRequirementDataList);
                    }
                }
            }

            map.put("list", list);
            return map;
        }
        return map;
    }

    /**
     * 获取团队或者监护人团队的详情信息
     */
    public Map<String, Object> getChildDescList(String participantId, Page<MatchChildInfo> page) {
        if (StringUtils.isEmpty(participantId))
            throw new RuntimeException("请选定需要查看的队伍");
        Map<String, Object> map = new HashMap<>();
        List<MatchChildInfo> matchChildInfoList = matchServiceProvider.getMatchChildInfoService().listChildInfoByParticipantId(participantId,page);
        MatchParticipant matchParticipant=matchServiceProvider.getMatchParticipantService().getMatchParticipantById(participantId);
        if(matchParticipant!=null) {
            //判断是否队长
            if(matchParticipant.getTeam()&&!matchParticipant.getGuardian()) {
                List<MatchParticipantAllVo> matchParticipantAllVos=matchServiceProvider.getMatchParticipantService().getJionMatchIsPassListByUserId(matchParticipant.getUserId());
                MatchParticipantDescVo matchParticipantDescVo=new MatchParticipantDescVo();
                if(matchParticipantAllVos.size()>0&&matchParticipantAllVos!=null) {
                    matchParticipantDescVo.setMatchParticipantAllVo(matchParticipantAllVos.get(0));
                    List<MatchRequirementData> matchRequirementDataList=matchServiceProvider.getMatchRequirementDataService().listUserRequirementData(participantId);
                    for (int j = 0; j < matchRequirementDataList.size(); j++) {
                        if(StringUtils.isNotBlank(matchRequirementDataList.get(j).getImagesUrl())) {
                            matchRequirementDataList.get(j).setImagesUrl(matchCreateAction.pictures(matchRequirementDataList.get(j).getImagesUrl()));
                            map.put("matchParticipantDescVo",matchParticipantDescVo);
                        }
                    }
                    matchParticipantDescVo.setMatchRequirementDataList(matchRequirementDataList);
                }
            }
        }

        List<MatchChildDescVo> matchChildDescVoList=new ArrayList<>();
        if (matchChildInfoList != null) {
            //组装孩子信息
            for (int i = 0; i < matchChildInfoList.size(); i++) {
                if (StringUtils.isNotBlank(matchChildInfoList.get(i).getImagesUrl())) {
                    matchChildInfoList.get(i).setImagesUrl(addImgUrlPreUtil.getUserImgPre(matchChildInfoList.get(i).getImagesUrl()));
                } else if (StringUtils.isNotBlank(matchChildInfoList.get(i).getHeadImage())) {
                    matchChildInfoList.get(i).setHeadImage(matchCreateAction.pictures(matchChildInfoList.get(i).getHeadImage()));
                }
                MatchChildDescVo matchChildDescVo=new MatchChildDescVo();
                matchChildDescVo.setMatchChildInfo(matchChildInfoList.get(i));
                List<MatchRequirementData> matchRequirementDataList=matchServiceProvider.getMatchRequirementDataService().listChildRequirementData(matchChildInfoList.get(i).getId());
                for (int j = 0; j < matchRequirementDataList.size(); j++) {
                    if(StringUtils.isNotBlank(matchRequirementDataList.get(j).getImagesUrl())) {
                        matchRequirementDataList.get(j).setImagesUrl(matchCreateAction.pictures(matchRequirementDataList.get(j).getImagesUrl()));
                    }
                }
                matchChildDescVo.setMatchRequirementDataList(matchRequirementDataList);
                matchChildDescVoList.add(matchChildDescVo);
            }
            map.put("list", matchChildDescVoList);
            return map;
        }
        return map;
    }


    /**
     * 通过比赛id查询相关票的信息
     *
     * @param matchId
     * @return
     */
    public Map<String, Object> getMatchTicketInfo(String matchId) {
        MatchEvent matchEvent = matchServiceProvider.getMatchEventService().getMatchEventByMatchId(matchId);
        Map<String, Object> map = new HashMap<>();
        //比赛的主要信息
        if (matchEvent == null) {
            throw new RuntimeException("找不到比赛的主要信息");
        }
        map.put("matchEvent", matchEvent);
        List<MatchTicket> list = matchServiceProvider.getMatchTicketService().listMatchTicket(matchId);
        List<MatchTicketVo> matchTicketVoList = new ArrayList<>();
        for (MatchTicket matchTicket : list) {
            MatchTicketVo matchTicketVo = matchCreateAction.letMatchTicketToMatchTicketVo(matchTicket);
            matchTicketVoList.add(matchTicketVo);
        }
        //比赛的售票信息
        if (matchTicketVoList.size() == 0 || matchTicketVoList == null) {
            throw new RuntimeException("找不到该比赛没有票信息");
        }
        map.put("matchTicketList", matchTicketVoList);
        List<MatchVenue> matchVenueList = matchServiceProvider.getMatchVenueService().getMatchVenueListByMatchZoneId(new String[]{matchId});
        //比赛的会场信息
        if (matchVenueList.size() == 0 || matchVenueList == null) {
            throw new RuntimeException("找不到该比赛的会场信息");
        }
        return map;
    }

    /**
     * 购票写入
     *
     * @param matchAudienceDTO
     * @return
     */
    public boolean addMatchAudience(MatchAudienceDTO matchAudienceDTO) {
        MatchAudienceService matchAudienceService = matchServiceProvider.getMatchAudienceService();
        return matchAudienceService.addOrUpdateAudience(matchAudienceDTO);
    }

    /**
     * 退票写入(晚点写----------------------------------------------------》——）
     *
     * @param matchTicketId
     * @return
     */
    public NetWorthMatchResult exitMatchAudienceInfo(String matchTicketId, String refundId, String userId) {
        MatchTicket matchTicket = matchServiceProvider.getMatchTicketService().getMatchTicketBYMatchTicketId(matchTicketId);
        if (matchTicket == null) {
            return NetWorthMatchResult.build(400, "没有该票");
        }
        Date nowTime = new Date();
        Date endTime = matchTicket.getEndTime();
        Long interval = nowTime.getTime() - endTime.getTime() / 1000 * 60 * 60;
        if (interval > 24) {
            return NetWorthMatchResult.build(400, "已经超时了，不能进行退款申请。");
        } else if (interval > 12 && interval <= 24) {
            return NetWorthMatchResult.build(200, "申请成功但会收取10%的手续费");
        } else {
            return NetWorthMatchResult.build(200, "免费申请成功");
        }
    }




    /*+++++++++++++++++++++++++++++++++++++++购票分割线++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /**
     * 根据token生成支付的码
     *
     * @param userId
     * @param token
     * @return
     */
    public String getTicketCodeByToken(String userId, String token) {
        //生成hash值
        long Ihash = token.hashCode() + (long) (Math.random() * System.currentTimeMillis());
        String ticketCode = Base64.encode((Ihash + "").getBytes()).replace("[B@", "").replace("=", "");
        RedisKeyName redisKeyName = new RedisKeyName(TICKETCODE, RedisTypeEnum.STRING_TYPE, userId);
        clientRedis().put(redisKeyName.getOrderKey(), ticketCode, 60 * 15);
        return ticketCode;
    }

    /**
     * 判断支付码是否超时
     *
     * @param userId
     * @param ticketCode
     * @return
     */
    public boolean CheckTicketCodeout(String userId, String ticketCode) {
        RedisKeyName redisKeyName = new RedisKeyName(TICKETCODE, RedisTypeEnum.STRING_TYPE, userId);
        String redisHash = clientRedis().getRaw(redisKeyName.getOrderKey());
        if (StringUtils.isNotBlank(redisHash) && redisHash.equals(ticketCode)) {
            clientRedis().remove(redisKeyName.getOrderKey());
            return true;
        }
        return false;
    }

    /**
     * 根据userId生成支付时效
     *
     * @param userId
     * @return
     */
    public void createApplyCodeByUserId(String userId) {
        //生成hash值
        clientRedis().put("MATCH_APPLY:" + userId, "正在支付", 60 * 15);
    }

    /**
     * @param userId
     */
    public void deleteApplyCodeByUserId(String userId) {
        clientRedis().del("MATCH_APPLY:" + userId);
    }

    /**
     * 获取过期时间
     *
     * @param userId
     * @return
     */
    public Long getApplyCodePastDue(String userId) {
        return clientRedis().ttl("MATCH_APPLY:" + userId);
    }

    /**
     * 判断是否超时
     *
     * @param userId
     * @return
     */
    public boolean checkApplyCodeTimeOut(String userId, String matchId) {
        String json = clientRedis().getString("MATCH_APPLY:" + userId);
        if (StringUtils.isNotBlank(json)) {
            return true;
        }
        matchServiceProvider.getMatchParticipantService().updateParticpantStatus(0, userId, matchId);
        return false;
    }

    /**
     * 更新门票的数量和获取购票的Id
     *
     * @param matchTicketPlanPayDto
     * @param userId
     * @param isPay
     * @return
     */
    @Transactional
    public String updateTicketCountAndTicketAudience(MatchTicketPlanPayDto matchTicketPlanPayDto, String userId, boolean isPay) {
        MatchAudienceService matchAudienceService = matchServiceProvider.getMatchAudienceService();
        int matchAudience = matchAudienceService.getMatchAudienceIsHave(matchTicketPlanPayDto.getMatchTicketId(), userId);
        /**
         * 判断是否已经买过票
         */
        if (matchAudience > 1) {
            throw new RuntimeException("每人最多购票1张门票");
        }
        MatchTicketService matchTicketService = matchServiceProvider.getMatchTicketService();

        /**
         * 判断是否还有库存
         */
        MatchTicket matchTicket = matchTicketService.getMatchTicketBYMatchTicketId(matchTicketPlanPayDto.getMatchTicketId());
        if (matchTicket == null) {
            throw new RuntimeException("发生错误，不存在此类型票。");
        }
        if (matchTicket.getNumber() == 0) {
            throw new RuntimeException("不好意思您来晚了，票已经售光了。");
        }
        //更新票数，及乐观锁
        MatchTicketDTO matchTicketDTO = new MatchTicketDTO();
        matchTicketDTO.setId(matchTicketPlanPayDto.getMatchTicketId());
        matchTicketDTO.setNumber(matchTicket.getNumber() - 1);
        Integer newoptimisticLocking = matchTicket.getOptimisticLocking() + 1;
        if (!matchTicketService.updateTicketById(matchTicketDTO, newoptimisticLocking, matchTicket.getOptimisticLocking())) {
            throw new RuntimeException("发生错误，锁票失败");
        }
        //写入购票表（处于未支付状态）
        MatchAudienceDTO matchAudienceDTO = new MatchAudienceDTO();
        matchAudienceDTO.setPay(isPay);
        matchAudienceDTO.setAttend(false);
        matchAudienceDTO.setUserId(userId);
        matchAudienceDTO.setMatchTicketId(matchTicketPlanPayDto.getMatchTicketId());
        matchAudienceDTO.setMatchId(matchTicketPlanPayDto.getMatchId());
        String audienceId = matchAudienceService.addAudience(matchAudienceDTO);
        return audienceId;
    }


    /**
     * 付费的添加
     *
     * @param matchParticpantAllDto
     * @return
     */
    @Transactional
    public Map<String, String> addParticipantPay(MatchParticpantAllDto matchParticpantAllDto, String userId) {
        MatchParticipantDTO matchParticipantDTO = matchParticpantAllDto.getMatchParticipantDTO();
        MatchParticipantService matchParticipantService = matchServiceProvider.getMatchParticipantService();
        if (matchServiceProvider.getMatchParticipantService().getMatchParticipantIsHave(matchParticipantDTO.getMatchId(), matchParticipantDTO.getUserId())) {
            throw new RuntimeException("您已报名");
        }
        UserForMatchVo u = matchServiceProvider.getMatchParticipantService().getUserForMatchVo(userId);
        matchParticipantDTO.setSex(u.getSex());
        matchParticipantDTO.setBirthday(u.getBirthday());
        matchParticipantDTO.setUserName(u.getNickname());
        matchParticipantDTO.setHeadImagesUrl(u.getUrl());

        String matchParticipantId = matchParticipantService.addParticipant(matchParticipantDTO, false, 1);
        List<MatchRequirementDataDTO> requirementData = matchParticipantDTO.getImage_message();
        //添加各个照片资料
        addImageData(requirementData, matchParticipantId, matchParticipantDTO, false);

        Map<String, String> map = new HashMap<>();
        map.put("matchParticipantId", matchParticipantId);
        List<MatchChildInfoDTO> matchChildInfoDTOList = matchParticpantAllDto.getMatchChildInfoDTOList();
        for (MatchChildInfoDTO childInfo : matchChildInfoDTOList) {
            childInfo.setParticipantId(matchParticipantId);
            String childId = matchServiceProvider.getMatchChildInfoService().addChildrenInfoForUser(childInfo);
            if (StringUtils.isBlank(childId)) {
                throw new RuntimeException("填入.." + childInfo.getName() + "信息失败");
            }
            addImageData(childInfo.getMatchRequirementDataDTOList(), childId, matchParticipantDTO, true);
        }
        createApplyCodeByUserId(matchParticipantDTO.getUserId());
//        addChild(matchChildInfoDTOList);
        return map;
    }


    /**
     * 团队付费的添加
     *
     * @param matchParticpantAllDto
     * @return
     */
    @Transactional
    public Map<String, String> addParticipantPayUserTeam(MatchParticpantAllDto matchParticpantAllDto, String userId) {
        MatchParticipantDTO matchParticipantDTO = matchParticpantAllDto.getMatchParticipantDTO();
        MatchParticipantService matchParticipantService = matchServiceProvider.getMatchParticipantService();
        if (matchParticipantService.getMatchParticipantIsHave(matchParticipantDTO.getMatchId(), matchParticipantDTO.getUserId())) {
            throw new RuntimeException("您已报名");
        }
        /*
        第一步，获取用户信息，队长报名
         */
        UserForMatchVo u = matchParticipantService.getUserForMatchVo(userId);
        matchParticipantDTO.setSex(u.getSex());
        matchParticipantDTO.setBirthday(u.getBirthday());
        matchParticipantDTO.setUserName(u.getNickname());
        matchParticipantDTO.setHeadImagesUrl(u.getUrl());
        String id = matchParticipantService.addParticipant(matchParticipantDTO, false, 1);
        List<MatchRequirementDataDTO> requirementData = matchParticipantDTO.getImage_message();
        addImageData(requirementData, id, matchParticipantDTO, false);
        //添加各个照片资料
        List<MatchChildInfoDTO> matchChildInfoList = matchParticpantAllDto.getMatchChildInfoDTOList();
        /*
        第二步，构建队员的对应信息
         */
        for (MatchChildInfoDTO childInfo : matchChildInfoList) {
            if (StringUtils.isNotBlank(childInfo.getUserId())) {
                UserForMatchVo userForMatchVo = matchParticipantService.getUserForMatchVo(childInfo.getUserId());
                childInfo.setSex(userForMatchVo.getSex() != null ? userForMatchVo.getSex() : "");
                childInfo.setBirthday(userForMatchVo.getBirthday() != null ? userForMatchVo.getBirthday() : new Date());
                childInfo.setName(userForMatchVo.getNickname() != null ? userForMatchVo.getNickname() : "");
                childInfo.setImagesUrl(userForMatchVo.getUrl() != null ? userForMatchVo.getUrl() : "");
            }
            childInfo.setParticipantId(id);
            String teamMemberId = matchServiceProvider.getMatchChildInfoService().addChildrenInfoForUser(childInfo);
            if (StringUtils.isBlank(teamMemberId)) {
                throw new RuntimeException("填入.." + childInfo.getName() + "信息失败");
            }
            addImageData(childInfo.getMatchRequirementDataDTOList(), teamMemberId, matchParticipantDTO, true);
        }
        //

        Map<String, String> map = new HashMap<>();
        map.put("matchParticipantId", id);
        createApplyCodeByUserId(matchParticipantDTO.getUserId());
        return map;
    }

    /**
     * 个人付费的添加
     *
     * @param matchParticipantDTO
     * @param token
     * @return
     */
    @Transactional
    public Map<String, String> addParticipantPayAlone(MatchParticipantDTO matchParticipantDTO, String token) {
        MatchParticipantService matchParticipantService = matchServiceProvider.getMatchParticipantService();
        if (matchParticipantService.getMatchParticipantIsHave(matchParticipantDTO.getMatchId(), matchParticipantDTO.getUserId())) {
            throw new RuntimeException("您已报名该比赛");
        }
        String matchParticipantId = matchParticipantService.addParticipant(matchParticipantDTO, false, 1);
        List<MatchRequirementDataDTO> requirementData = matchParticipantDTO.getImage_message();
        //添加各个照片资料
        addImageData(requirementData, matchParticipantId, matchParticipantDTO, false);
        Map<String, String> map = new HashMap<>();
        map.put("matchParticipantId", matchParticipantId);
        String payId = getTicketCodeByToken(matchParticipantDTO.getUserId(), token);
        map.put("payId", payId);
        return map;
    }

    /**
     * 获得所有赛区的报名信息
     *
     * @param matchId
     * @return
     */
    public Map<String, List> getMatchAllZoneApplyInfo(String matchId) {
        Map<String, List> map = new HashMap<>();
        List<MatchAllApplyInfoVO> matchAllApplyInfoVOS = matchServiceProvider.getMatchEventService().getMatchAllZoneApplyInfo(matchId);
        map.put("matchAllApplyInfoVOS", matchAllApplyInfoVOS);
        return map;
    }

    /**
     * 获得所有赛区的报名信息
     *
     * @param matchId
     * @return
     */
    public Map<String, List> getMatchFirstZoneApplyInfo(String matchId) {
        Map<String, List> map = new HashMap<>();
        List<MatchAllApplyInfoVO> matchAllApplyInfoVOS = matchServiceProvider.getMatchEventService().getMatchFirstZoneApplyInfo(matchId);
        map.put("matchAllApplyInfoVOS", matchAllApplyInfoVOS);
        return map;
    }

    /**
     * 获取所有接收邀请的人
     *
     * @param matchId
     * @return
     */
    public Map<String, List> getReviewListAcceptBYMatchId(String matchId) {
        Map<String, List> map = new HashMap<>();
        List<MatchReviewVo> matchReviewVoList = matchServiceProvider.getMatchReviewService().getReviewListAcceptBYMatchId(matchId);
        for (int i = 0; i < matchReviewVoList.size(); i++) {
            matchReviewVoList.get(i).setLogo(matchCreateAction.updateImages(matchReviewVoList.get(i).getLogo()));
        }
        map.put("acceptReview", matchReviewVoList);
        return map;
    }

    /**
     * 获取所有赛区的门票信息
     */
    public Map<String, Object> getAllMatchTicketByMatchId(String matchId) {
        List<MatchZone> matchZoneList = matchServiceProvider.getMatchZoneService().getMatchZoneListByMatchId(matchId);
        String[] ids = new String[matchZoneList.size()];
        for (int i = 0; i < matchZoneList.size(); i++) {
            ids[i] = matchZoneList.get(i).getId();
        }
        List<MatchTicketVo> matchTicketVoList = new ArrayList<>();
        List<MatchTicket> matchTicketList = matchServiceProvider.getMatchTicketService().getAllMatchTicketByZoneIds(ids);
        for (int i = 0; i < matchTicketList.size(); i++) {
            for (int j = 0; j < matchZoneList.size(); j++) {
                if (matchZoneList.get(j).getId().equals(matchTicketList.get(i).getZoneId())) {
                    matchTicketList.get(i).setZoneId(matchZoneList.get(j).getZoneName());
                    break;
                }
            }
            matchTicketVoList.add(matchCreateAction.letMatchTicketToMatchTicketVo(matchTicketList.get(i)));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("matchTicketVoList", matchTicketVoList);
        return map;
    }

    /**
     * 根据赛区id筛选门票
     *
     * @param zoneId
     * @return
     */
    public Map<String, Object> getMatchTicketByZoneId(String zoneId) {
        List<MatchTicketVo> matchTicketVoList = new ArrayList<>();
        MatchZone matchZone = matchServiceProvider.getMatchZoneService().selectById(zoneId);
        List<MatchTicket> matchTicketList = matchServiceProvider.getMatchTicketService().getMatchTicketByZoneId(zoneId);
        for (int i = 0; i < matchTicketList.size(); i++) {
            matchTicketList.get(i).setZoneId(matchZone.getZoneName());
            matchTicketVoList.add(matchCreateAction.letMatchTicketToMatchTicketVo(matchTicketList.get(i)));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("matchTicketVoList", matchTicketVoList);
        return map;
    }

    /**
     * 根据赛区id和场次筛选门票
     *
     * @param zoneId
     * @param venueId
     * @return
     */
    public Map<String, Object> getMatchTicketByZoneIdAndVenueId(String zoneId, String venueId) {
        List<MatchTicketVo> matchTicketVoList = new ArrayList<>();
        MatchZone matchZone = matchServiceProvider.getMatchZoneService().selectById(zoneId);
        List<MatchTicket> matchTicketList = matchServiceProvider.getMatchTicketService().getMatchTicketByZoneId(zoneId);
        for (int i = 0; i < matchTicketList.size(); i++) {
            matchTicketList.get(i).setZoneId(matchZone.getZoneName());
            matchTicketVoList.add(matchCreateAction.letMatchTicketToMatchTicketVo(matchTicketList.get(i)));
        }
        List<MatchTicketVo> res = new ArrayList<>();
        for (int i = 0; i < matchTicketVoList.size(); i++) {
            List<KindAndVenceVo> kindAndVenceVoList = matchTicketVoList.get(i).getKindAndVenceVoList();
            for (KindAndVenceVo kindAndVenceVo : kindAndVenceVoList) {
                if (kindAndVenceVo.getVenueId().equals(venueId)) {
                    res.add(matchTicketVoList.get(i));
                    continue;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", res);
        return map;
    }

    /**
     * 通过赛程获取相关门票
     *
     * @param progressId
     * @return
     */
    public Map<String, Object> getMatchTicketByProgress(String progressId) {
        Map<String, Object> map = new HashMap<>();
        List<MatchTicketVo> matchTicketVoList = new ArrayList<>();
        List<MatchTicket> matchTicketList = matchServiceProvider.getMatchTicketService().getMatchTicketByProgress(progressId);
        matchServiceProvider.getMatchTicketService().getMatchTicketByProgress(progressId);
        for (int i = 0; i < matchTicketList.size(); i++) {
            matchTicketVoList.add(matchCreateAction.letMatchTicketToMatchTicketVo(matchTicketList.get(i)));
        }
        map.put("matchTicketVoList", matchTicketVoList);
        return map;
    }

    /**
     * 获取已经购买的门票
     */
    public Map<String, List> getBuyTicketByUserIdANDMacthId(String userId, String matrchId) {
        List<MatchBuyTicketVo> matchTicketList = matchServiceProvider.getMatchTicketService().getBuyTicketByUserIdANDMacthId(userId, matrchId);
        for (int i = 0; i < matchTicketList.size(); i++) {
            MatchBuyTicketVo matchBuyTicketVo=matchTicketList.get(i);
            String[] venueIds=matchBuyTicketVo.getVenueIds().split(",");
            if(venueIds.length>0&&venueIds!=null) {
                MatchVenue matchVenue=matchServiceProvider.getMatchVenueService().selectById(venueIds[0]);
                matchBuyTicketVo.setAddress(matchVenue.getAddress());
                matchBuyTicketVo.setEndTime(matchVenue.getEndTime());
                matchBuyTicketVo.setStartTime(matchVenue.getBeginTime());
                if(matchBuyTicketVo.getAttend()) {
                    matchBuyTicketVo.setStatus(2);
                }else {
                    if(matchVenue.getEndTime().getTime()<new Date().getTime()) {
                        matchBuyTicketVo.setStatus(0);
                    }else {
                        matchBuyTicketVo.setStatus(1);
                    }
                }

            }

            matchBuyTicketVo.setAudienceId(matchBuyTicketVo.getAudienceId()+",aU");
            matchBuyTicketVo.setUrl(matchCreateAction.pictures(matchBuyTicketVo.getUrl()));
        }
        Map<String, List> map = new HashMap<>();
        map.put("matchTicketList", matchTicketList);
        return map;
    }

    /**
     * 查看举办了的比赛
     * @param userId
     * @return
     */
    public Map<String, Object>  selectSimpleMatchEventList(String userId,Integer pageCount,Integer pageSize) {
        List<MatchEventSimpleVo> matchEventSimpleVos = matchServiceProvider.getMatchEventService().selectSimpleMatchEventList(userId,pageCount,pageSize);
        Map<String, Object> map = new HashMap<>();
        /*
        加上前缀
         */
        for (MatchEventSimpleVo vo : matchEventSimpleVos) {
            vo.setMatchImageUrl(matchCreateAction.pictures(vo.getMatchImageUrl()));
        }
        map.put("data", matchEventSimpleVos);
        return map;
    }

    /**
     * 查看用户举办的全部比赛
     * @param matchId
     * @return
     */
    public List<MatchEventSimpleVo> selectActiveMatchEventById(String matchId) {
        List<MatchEventSimpleVo> matchEventSimpleVos = matchServiceProvider.getMatchEventService().selectActiveMatchEventById(matchId);
        return  matchEventSimpleVos;
    }
    /**
     * 查看用户举办的全部比赛
     * @param userId
     * @return
     */
    public Map<String, Object> selectAllMyMatchEvent(String userId,Integer pageCount,Integer pageSize) {
        List<MatchEventSimpleVo> matchEventSimpleVos = matchServiceProvider.getMatchEventService().selectAllMyMatchEvent(userId,pageCount,pageSize);
        Map<String, Object> map = new HashMap<>();
        /*
        加上前缀
         */
        for (MatchEventSimpleVo vo : matchEventSimpleVos) {
            vo.setMatchImageUrl(matchCreateAction.pictures(vo.getMatchImageUrl()));
        }
        map.put("data", matchEventSimpleVos);
        return map;
    }
    /**
     * 查看参加了的比赛
     */
    public Map<String, Object>  selectParticipantMatch(String userId) {
        List<MatchEventSimpleVo> matchEventSimpleVos  =  matchServiceProvider.getMatchEventService().selectParticipantMatch(userId);
        /*
        加上前缀
         */
        for (MatchEventSimpleVo vo : matchEventSimpleVos) {

            vo.setMatchImageUrl(matchCreateAction.pictures(vo.getMatchImageUrl()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", matchEventSimpleVos);
        return map;
    }

    /**
     * 查看审核通过的比赛
     * @param dto
     * @return
     */
    public List<MatchEventSimpleVo> selectApprovedMatchEvent(CommonPageDto dto) {
        List<MatchEventSimpleVo> list =  matchServiceProvider.getMatchEventService().selectApprovedMatchEvent(dto);
        for (MatchEventSimpleVo vo : list) {
            vo.setMatchImageUrl(matchCreateAction.pictures(vo.getMatchImageUrl()));
        }
        return list;
    }

    /**
     * 獲取一個相關賽事
     * @param userId
     * @param myUserId
     * @return
     */
    public MatchEventSimpleVo selectOneMatchEvent(String userId,String myUserId) {
        MatchEventSimpleVo matchEventSimpleVo;
        if(myUserId.equals(userId)) {
            matchEventSimpleVo=matchServiceProvider.getMatchEventService().selectOneMatchEventIsApproved(userId,false);
        }else {
            matchEventSimpleVo=matchServiceProvider.getMatchEventService().selectOneMatchEventIsApproved(userId,true);
        }
        if(matchEventSimpleVo==null) {
            return null;
        }
        List<MatchEvent> matchEvents=matchServiceProvider.getMatchEventService().getMatchByUserId(userId);
        int matching=0;
        for (MatchEvent matchEvent:
                matchEvents) {
            if(StringUtils.isNotBlank(matchEvent.getId())) {
                Date date=matchServiceProvider.getMatchVenueService().getLastEndTimeBtMatchId(matchEvent.getId());
                Date now=new Date();
                if(date!=null) {
                    if(date.getTime()>now.getTime()) {
                        matching++;
                    }
                }
            }
        }
        matchEventSimpleVo.setMatchImageUrl(matchCreateAction.pictures(matchEventSimpleVo.getMatchImageUrl()));
        matchEventSimpleVo.setMatchingCount(matching);
        matchEventSimpleVo.setMatchCount(matchEvents.size());
        return matchEventSimpleVo;
    }

    /**
     * 获得最新的比赛通过userId
     * @param userId
     * @return
     */
    public Map<String,Object> getNewestMatchByUserId(String userId) {
        Map<String,Object> map=new HashMap<>();
        MatchEventNewest matchEventNewest=matchServiceProvider.getMatchEventService().getNewestMatchByUserId(userId);
        if(matchEventNewest==null) {
            return null;
        }
        map.put("matchEventNewest",matchEventNewest);
        List<MatchEvent> matchEvents=matchServiceProvider.getMatchEventService().getMatchByUserId(userId);
        int matching=0;
        for (MatchEvent matchEvent:
        matchEvents) {
            if(StringUtils.isNotBlank(matchEvent.getId())) {
                Date date=matchServiceProvider.getMatchVenueService().getLastEndTimeBtMatchId(matchEvent.getId());
                Date now=new Date();
                if(date.getTime()>now.getTime()) {
                    matching++;
                }
            }
        }
        map.put("matchCount",matchEvents.size());
        map.put("matching",matching);
        return map;
    }

    /***********第三次需求****/
    public String insertOrUpdateMatchNotice(MatchNoticeDTO dto) {
        String matchReviewKind = matchServiceProvider.getMatchReviewService().getMatchReviewKind(dto.getMatchId(), dto.getUserId());
        if (StringUtils.isBlank(matchReviewKind)) {
            throw new RuntimeException("只允许比赛相关人员发布公告");
        }
        dto.setMerchantType(matchReviewKind);
        String matchNoticeId = matchServiceProvider.getMatchNoticeService().insertOrUpdate(dto);
        if (StringUtils.isBlank(matchNoticeId)) {
            throw new RuntimeException("添加报告失败");
        }
        return matchNoticeId;
    }

    public List<MatchNoticeVo> getMatchNoticeVosByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new RuntimeException("请确定要查看的公告");
        }
        List<MatchNotice> matchNotices = matchServiceProvider.getMatchNoticeService().selectMatchNoticeByUserId(userId);
        List<MatchNoticeVo> vos = new ArrayList<>();
        if (matchNotices != null && matchNotices.size() > 0) {
            for (MatchNotice matchNotice : matchNotices) {
                MatchNoticeVo vo = new MatchNoticeVo();
                vo.setTitle(matchNotice.getTitle());
                vo.setAfficheContent(matchNotice.getAfficheContent());
                vo.setCreateTime(matchNotice.getCreateTime());
                vo.setId(matchNotice.getId());
                vo.setMessageType(matchNotice.getMessageType());
                vo.setUserType(matchNotice.getUserType());
                vo.setWatchNum(getWatchNumber(matchNotice.getId()));
                vo.setUserName(matchNotice.getMerchantName());
                vos.add(vo);
            }
        }
        return vos;
    }

    public List<MatchNoticeVo> getMatchNoticeVosByMatchId(CommonPageDto dto) {
        String matchId = dto.getId();
        if (StringUtils.isBlank(matchId)) {
            throw new RuntimeException("请确定要查看的公告");
        }
        Page<MatchNotice> page = new Page<>(dto.getCurrentPage(), dto.getSize());
        List<MatchNotice> matchNotices = matchServiceProvider.getMatchNoticeService().selectMatchNoticeByMatchId(matchId, page).getRecords();
        List<MatchNoticeVo> vos = new ArrayList<>();
        if (matchNotices != null && matchNotices.size() > 0) {
            for (MatchNotice matchNotice : matchNotices) {
                MatchNoticeVo vo = new MatchNoticeVo();
                vo.setTitle(matchNotice.getTitle());
                vo.setAfficheContent(matchNotice.getAfficheContent());
                vo.setCreateTime(matchNotice.getCreateTime());
                vo.setId(matchNotice.getId());
                vo.setUserName(matchNotice.getMerchantName());
                vo.setMessageType(matchNotice.getMessageType());
                vo.setUserType(matchNotice.getUserType());
                vo.setWatchNum(getWatchNumber(matchNotice.getId()));
                vos.add(vo);
            }
        }
        return vos;
    }

    private Integer getWatchNumber(String id) {
        clientRedis();
        String key = "watch" + id;
        if (!redisCache.exists(key)) {
            redisCache.put(key, String.valueOf(0));
        }
        Integer watchNum = (Integer) redisCache.get(key);
        redisCache.incrBy(key, 1);
        return watchNum;
    }

    public boolean deleteMatchNotice(MatchDeleteDto matchDeleteDto, String userId) {
        String matchNoticeId =  matchDeleteDto.getDeleteId();
        if(StringUtils.isBlank(matchNoticeId)) {
            throw new RuntimeException("请选择需要删除的比赛");
        }
        String noticeUserId = matchServiceProvider.getMatchNoticeService().getUserId(matchNoticeId);
        if (!noticeUserId.equals(userId)) {
            throw new RuntimeException("请删除自己更新的帖子");
        }
        return matchServiceProvider.getMatchNoticeService().delete(matchNoticeId);
    }
}
