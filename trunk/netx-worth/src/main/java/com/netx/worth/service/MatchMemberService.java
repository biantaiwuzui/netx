package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MemberDTO;
import com.netx.worth.mapper.MatchMemberMapper;
import com.netx.worth.model.MatchMember;
import com.netx.worth.vo.MatchUserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 赛事嘉宾、工作人员管理
 * Created by Yawn on 2018/8/1 0001.
 */
@Service
public class MatchMemberService extends ServiceImpl<MatchMemberMapper, MatchMember> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    /**
     * 邀请会员
     * @param dto
     * @return
     */
    public boolean invitedMember(MemberDTO dto) {
        MatchMember matchMember = new MatchMember();
        matchMember.setNetUser(dto.getInNet());
        matchMember.setKind(String.valueOf(dto.getKind()));
        matchMember.setUserCall(dto.getUserCall());
        matchMember.setMatchId(dto.getMatchId());
        matchMember.setUserId(dto.getUserId());
        matchMember.setNetUser(dto.getInNet());
        if(StringUtils.isNoneBlank(dto.getId())){
            matchMember.setId(dto.getId());
        }
        return insertOrUpdate(matchMember);
    }

    /**
     * 更新管理人员
     * @param dto
     * @return
     */
    public boolean updateMember(MemberDTO dto) {
        MatchMember matchMember = new MatchMember();
        matchMember.setKind(String.valueOf(dto.getKind()));
        matchMember.setUserCall(dto.getUserCall());
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("user_id = {0}", dto.getUserId())
                .and("match_id = {0}", dto.getMatchId());
        return update(matchMember, matchMemberEntityWrapper);
    }

    /**
     * 接受邀请
     * @param userId
     * @param matchId
     * @return
     */
    public boolean acceptMember(String userId,String matchId) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("user_id = {0}",userId)
                .and("match_id = {0}",matchId);
        List<MatchMember> matchMemberList=selectList(matchMemberEntityWrapper);
        for (int i = 0; i < matchMemberList.size(); i++) {
            matchMemberList.get(i).setAccept(true);
        }
        return updateBatchById(matchMemberList);
    }
    /**
     * 删除指定工作人员
     * @param id
     * @return
     */
    public boolean deleteMatchMember(String id) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("id = {0}", id);
        return delete(matchMemberEntityWrapper);
    }
    /**
     * 删除所有工作人员
     * @param matchId
     * @return
     */
    public boolean deleteMatchMemberByMatchId(String matchId) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("match_id = {0}", matchId);
        return delete(matchMemberEntityWrapper);
    }

    /**
     * 返回该比赛工作人员
     * @param matchId
     * @return
     */
    public List<MatchMember> listMatchMember(String matchId) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("match_id = {0}", matchId)
                .orderBy("kind");
        return selectList(matchMemberEntityWrapper);
    }

    /**
     * 查看指定比赛的指定人员
     * @param matchId
     * @param kind
     * @return
     */
    public List<MatchMember> listMatchMemberByKind(String matchId, String kind) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("match_id = {0}", matchId)
                .and("kind = {0}", kind);
        return selectList(matchMemberEntityWrapper);
    }

    /**
     * 查找是否存在此人员
     * @param matchId
     * @param userId
     * @return
     */
    public boolean getMatchMemberIsHave(String matchId,String userId){
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("match_id = {0}", matchId)
                .and("user_id = {0}", userId);
        List<MatchMember> matchMemberList=selectList(matchMemberEntityWrapper);
        if(matchMemberList.size()>0&&matchMemberList!=null)
            return true;
        return false;
    }

    public boolean IsWriteMatchMember(String matchId){
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("match_id = {0}", matchId);
        if(selectCount(matchMemberEntityWrapper)>0){
            return true;
        }
        return false;
    }

    /**
     * 获得未接受邀请的列表
     * @param matchId
     * @param userId
     * @return
     */
    public List<MatchMember> getNoAcceptMember(String matchId,String userId) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("match_id = {0}", matchId)
                .and("user_id = {0}", userId).and("is_accept={0}",false);
        return  selectList(matchMemberEntityWrapper);
    }
    /**
     * 获取工作人员的类型/嘉宾/主持人
     * @param matchId
     * @param userId
     * @return
     */
    public String getMemberKind(String matchId, String userId,Integer isAccept) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.setSqlSelect("kind")
                .where("match_id = {0}", matchId)
                .and("user_id = {0}", userId)
                .and("is_accept = {0}",isAccept);
        MatchMember matchMember=selectOne(matchMemberEntityWrapper);
        if(matchMember==null){
            return null;
        }
        return matchMember.getKind();
    }

    /**
     * 获取工作人员的类型/嘉宾/主持人
     * @param matchId
     * @param userId
     * @return
     */
    public String getMemberKind(String matchId, String userId) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.setSqlSelect("kind")
                .where("match_id = {0}", matchId)
                .and("user_id = {0}", userId).and("is_accept={0}",1);
        MatchMember matchMember=selectOne(matchMemberEntityWrapper);
        if(matchMember==null){
            return "";
        }
        return matchMember.getKind();
    }


    /**
     * 獲取全部工作人員
     * @param matchId
     * @return
     */
    public List<MatchUserInfoVo> getWorkPeopleAllListByMatchId(String matchId){
        Map<String,String> map=new HashMap<>();
        map.put("match_id",matchId);
        return baseMapper.getWorkPeopleList(map);
    }


    /**
     * 獲取接受邀請並在場的工作人員
     * @param matchId
     * @return
     */
    public List<MatchUserInfoVo> getWorkPeopleIsSpotANDIsAcceptListByMatchId(String matchId){
        Map<String,Object> map=new HashMap<>();
        map.put("match_id",matchId);
        map.put("is_spot",1);
        map.put("is_accept",1);
        return baseMapper.getWorkPeopleList(map);
    }

    /**
     * 獲取接受邀請的工作人員
     * @param matchId
     * @return
     */
    public List<MatchUserInfoVo> getWorkPeopleIsAcceptListByMatchId(String matchId){
        Map<String,Object> map=new HashMap<>();
        map.put("match_id",matchId);
        map.put("is_accept",1);
        return baseMapper.getWorkPeopleList(map);
    }


    /**
     * 返回没有接受邀请的工作人员
     * @param matchId
     * @return
     */
    public List<MatchMember> listMatchMemberNotAccept(String matchId) {
        EntityWrapper<MatchMember> matchMemberEntityWrapper = new EntityWrapper<>();
        matchMemberEntityWrapper.where("match_id = {0}", matchId)
                .and("is_accept = 0")
                .orderBy("kind");
        return selectList(matchMemberEntityWrapper);
    }

    /**
     * 添加非网值用户，会增加一个随机码
     * @param dto
     * @param activeCode
     * @return
     */
    public boolean invitedNotNetMember(MemberDTO dto, String activeCode) {
        MatchMember matchMember = new MatchMember();
        matchMember.setNetUser(dto.getInNet());
        matchMember.setKind(String.valueOf(dto.getKind()));
        matchMember.setUserCall(dto.getUserCall());
        matchMember.setMatchId(dto.getMatchId());
        matchMember.setUserId(dto.getUserId());
        matchMember.setNetUser(dto.getInNet());
        matchMember.setActiveCode(activeCode);
        return insertOrUpdate(matchMember);
    }

    /**
     * 返回根据电话号码返回激活码
     * @param telephone
     * @return
     */
    public MatchMember selectActivateCodeByTelephone(String telephone) {
        EntityWrapper<MatchMember> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id = {0}", telephone);
        return selectOne(entityWrapper);
    }

    /**
     * 判断是否邀请重复人员
     * @param matchId 比赛
     * @param userId 用户
     * @return
     */
    public boolean isContainMember(String matchId, String userId) {
        EntityWrapper<MatchMember> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId)
                .and("user_id = {0}", userId);
        MatchMember m = selectOne(entityWrapper);
        if (m != null) {
            return true;
        }
        return false;
    }

    /**
     * 判断改用户是不是该比赛的人员
     * @param matchId
     * @param mobile
     * @return
     */
    public List<MatchMember> getNoAcceptMemberByMobile(String matchId, String mobile) {
        EntityWrapper<MatchMember> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId)
                .and("user_call = {0}", mobile);
        return selectList(entityWrapper);
    }
}
