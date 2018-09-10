package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.model.UserMacthInfo;
import com.netx.common.wz.dto.matchEvent.MatchParticipantDTO;
import com.netx.worth.mapper.MatchParticipantMapper;
import com.netx.worth.model.MatchParticipant;
import com.netx.worth.vo.MatchParticipantAllVo;
import com.netx.worth.vo.MatchParticipantVo;
import com.netx.worth.vo.UserForMatchVo;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleUnresolved;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参赛者报名
 * Created by Yawn on 2018/8/1
 */
@Service
public class MatchParticipantService extends ServiceImpl<MatchParticipantMapper, MatchParticipant> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 插入参赛者
     * @param dto
     * @return
     */
    public String addParticipant(MatchParticipantDTO dto,Boolean isPay,Integer status) {
        MatchParticipant matchParticipant = new MatchParticipant();
        if (StringUtils.isNotEmpty(dto.getId()))
            matchParticipant.setId(dto.getId());
        matchParticipant.setMatchId(dto.getMatchId());
        matchParticipant.setUserId(dto.getUserId());
        matchParticipant.setZoneId(dto.getZoneId());
        matchParticipant.setGroupId(dto.getGroupId());
        matchParticipant.setBirthday(dto.getBirthday());
        matchParticipant.setSex(dto.getSex());
        matchParticipant.setTeam(dto.getTeam());
        matchParticipant.setUserName(dto.getUserName());
        matchParticipant.setProjectName(dto.getProjectName());
        matchParticipant.setGuardian(dto.getGuardian());
        matchParticipant.setHeadImagesUrl(dto.getHeadImagesUrl());
        matchParticipant.setSpot(false);
        if(dto.getPass()==null) {
            matchParticipant.setPass(false);
        }else {
            matchParticipant.setPass(dto.getPass());
        }
        matchParticipant.setPay(isPay);
        matchParticipant.setStatus(status);
        return insert(matchParticipant)?matchParticipant.getId():"";
    }


    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public UserMacthInfo getUserInfo(String userId) {
        return baseMapper.getUserInfo(userId);
    }

    /**
     * 更新参赛者支付状态
     * @param dto
     * @return
     */
    public boolean updateParticipant(MatchParticipantDTO dto,boolean isPay) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        MatchParticipant matchParticipant = new MatchParticipant();
        matchParticipant.setPay(isPay);
        matchParticipantEntityWrapper.where("match_id = {0}", dto.getMatchId())
                .and("user_id = {0}", dto.getUserId()).and("id={0}",dto.getId());
        return update(matchParticipant, matchParticipantEntityWrapper);
    }

    /**
     * 更新比赛状态
     * @param status
     * @param userId
     * @param matchId
     * @return
     */
    public boolean updateParticpantStatus(Integer status,String userId,String matchId) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        MatchParticipant matchParticipant = new MatchParticipant();
        matchParticipant.setStatus(status);
        matchParticipantEntityWrapper.where("match_id = {0}", matchId)
                .and("user_id = {0}",userId);
        return update(matchParticipant, matchParticipantEntityWrapper);
    }
    /**
     * 更新入选信息
     * @param matchParticipantList
     * @return
     */
    public boolean updateParticipantByIds(List<MatchParticipant> matchParticipantList) {
        return updateBatchById(matchParticipantList);
    }
    /**
     * 根据id删除
     * @param id
     * @return
     */
    public boolean deleteParticipantById(String id) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        matchParticipantEntityWrapper.where("id = {0}", id);
        return delete(matchParticipantEntityWrapper);
    }

    /**
     * 获取比赛所有参赛者
     * @param matchId
     * @return
     */
    public List<MatchParticipant> listMatchParticipant(String matchId, Page<MatchParticipant> page) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        matchParticipantEntityWrapper.where("match_id = {0}", matchId);
        if (page == null)
            return selectList(matchParticipantEntityWrapper);
        return selectPage(page, matchParticipantEntityWrapper).getRecords();
    }

    /**
     * 获取赛区所有参赛者
     * @param matchId
     * @return
     */
    public List<MatchParticipant> listMatchParticipantByZone(String matchId, String zoneId, Page<MatchParticipant> page) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        matchParticipantEntityWrapper.where("match_id = {0}", matchId)
                .and("zone_id = {0}", zoneId);
        if (page == null)
            return selectList(matchParticipantEntityWrapper);
        return selectPage(page, matchParticipantEntityWrapper).getRecords();
    }

    /**
     * 计算当前报名数量
     * @param zoneId
     * @param groupId
     * @return
     */
    public Integer getCountBYGroupAndZoneId(String zoneId, String groupId) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        matchParticipantEntityWrapper.where("zone_id = {0}", zoneId)
                .and("group_id = {0}", groupId);
        return selectCount(matchParticipantEntityWrapper);
    }
    /**
     * 计算当前到场和未到场的数量
     * @param zoneId
     * @param groupId
     * @return
     */
    public Integer getCountSpotBYGroupAndZoneId(String zoneId, String groupId,boolean isSpot) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        matchParticipantEntityWrapper.where("zone_id = {0}", zoneId)
                .and("group_id = {0}", groupId).and("is_spot={0}",isSpot);
        return selectCount(matchParticipantEntityWrapper);
    }
    /**
     * 获取赛组的所以参赛者信息
     * @param zoneId
     * @param groupId
     * @return
     */
    public List<MatchParticipant> listMatchParticipantByGroup(String zoneId, String groupId, Page<MatchParticipant> page) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        matchParticipantEntityWrapper.where("zone_id = {0}", zoneId)
                .and("group_id = {0}", groupId);
        if (page == null)
            return selectList(matchParticipantEntityWrapper);
        return selectPage(page, matchParticipantEntityWrapper).getRecords();
    }

    /**
     * 获取比赛已经支付了的参赛者
     * @param matchId
     * @return
     */
    public List<MatchParticipant> listPaidMatchParticipant(String matchId) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        matchParticipantEntityWrapper.where("match_id = {0}", matchId)
                .and("is_pay=1");
        return selectList(matchParticipantEntityWrapper);
    }

    /**
     * 列出团队的成员
     * @param matchId
     * @param teamId
     * @return
     */
    public List<MatchParticipant> listTeamParticipant(String matchId, String teamId) {
        EntityWrapper<MatchParticipant> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId)
                .and("team_id = {0}", teamId);
        return selectList(entityWrapper);
    }

    /**
     * 列出自己正在进行的订单
     * @param matchId
     * @param userId
     * @return
     */
    public MatchParticipantVo listUserParticipant(String matchId, String userId) {
        if (StringUtils.isBlank(matchId) || StringUtils.isBlank(userId))
            throw new RuntimeException("请确定比赛");
        Map<String,Object> map=new HashMap<>();
        map.put("match_id",matchId);
        map.put("user_id",userId);
        map.put("status",1);
        return baseMapper.selectMatchParticipantVo(map);
    }

    /**
     * 通过比赛审核
     * @return
     */
    public boolean passReview(String id) {
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        MatchParticipant matchParticipant = selectById(id);
        if (matchParticipant == null)
            throw new RuntimeException("请选择选手");
        if (matchParticipant.getPass())
            throw new RuntimeException("选手已通过审核");
        matchParticipant.setPass(true);
        matchParticipantEntityWrapper.where("id = {0}", id);
        return update(matchParticipant, matchParticipantEntityWrapper);
    }

    /**
     * 获取是否为参赛人员
     * @param matchId
     * @param userId
     * @return
     */
    public boolean getMatchParticipantIsHave(String matchId,String userId){
        EntityWrapper<MatchParticipant> matchParticipantEntityWrapper = new EntityWrapper<>();
        matchParticipantEntityWrapper.where("match_id = {0}", matchId)
                .and("user_id = {0}", userId)
                .and("status>={0}",1);
        MatchParticipant m = selectOne(matchParticipantEntityWrapper);
        if (m != null) {
            return true;
        }
        return false;
    }

    /**
     * 獲取全部參加比賽的人員
     * @return
     */
    public List<MatchParticipantAllVo> getJionMatchAllListByMatchId(String zoneId,String groupId){
        Map<String,Object> map=new HashMap<>();
        map.put("zone_id",zoneId);
        map.put("group_id",groupId);
        map.put("is_pay",1);
        return super.baseMapper.getJionMatchListByMatchId(map);
    }
    /**
     * 獲取到场的比賽的人員
     * @return
     */
    public List<MatchParticipantAllVo> getJionMatchIsSpotListByMatchId(String zoneId,String groupId){
        Map<String,Object> map=new HashMap<>();
        map.put("zone_id",zoneId);
        map.put("group_id",groupId);
        map.put("is_pay",1);
        map.put("is_spot",1);
        return super.baseMapper.getJionMatchListByMatchId(map);
    }
    /**
     * 獲取到场的比賽的人員
     * @return
     */
    public List<MatchParticipantAllVo> getJionMatchNotSpotListByMatchId(String zoneId,String groupId){
        Map<String,Object> map=new HashMap<>();
        map.put("zone_id",zoneId);
        map.put("group_id",groupId);
        map.put("is_pay",1);
        map.put("is_spot","0");
        return super.baseMapper.getJionMatchListByMatchId(map);
    }
    /**
     * 獲取全部參加比賽
     * @return
     */
    public List<MatchParticipantAllVo> getJionMatchIsPassListByMatchId(String zoneId, String groupId, Integer pageCount, Integer pageSize){
        Map<String,Object> map=new HashMap<>();
        map.put("zone_id",zoneId);
        map.put("group_id",groupId);
        map.put("is_pay",1);
        map.put("pageCount",pageCount);
        map.put("pageSize",pageSize);
        return super.baseMapper.getJionMatchListByMatchId(map);
    }

    /**
     * 獲取全部參加比賽通過userId
     * @return
     */
    public List<MatchParticipantAllVo> getJionMatchIsPassListByUserId(String userId){
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",userId);
        map.put("is_pay",1);
        return super.baseMapper.getJionMatchListByMatchId(map);
    }

    /**
     * 根据用户ID返回出席码
     * @param id
     * @return
     */
    public MatchParticipant getMatchParticipantById(String id) {
        EntityWrapper<MatchParticipant> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return selectOne(entityWrapper);
    }


    /**
     * 获取用户跟比赛相关的信息
     * @param userId
     * @return
     */
    public UserForMatchVo getUserForMatchVo(String userId) {
        return baseMapper.selectUserInfoForMatch(userId);
    }

    /**
     * 查看已经已支付但没过审核的参赛者
     * @param matchId
     * @return
     */
    public List<MatchParticipant> listPaidAndNoPassParticipant(String matchId) {
        EntityWrapper<MatchParticipant> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0} ", matchId)
                .and("is_pay = 1 and is_pass = 0");
        return selectList(entityWrapper);
    }

    public List<MatchParticipant> listPaidAndPassParticipant(String matchId) {
        EntityWrapper<MatchParticipant> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0} ", matchId)
                .and("is_pay = 1 and is_pass = 1");
        return selectList(entityWrapper);
    }
}
