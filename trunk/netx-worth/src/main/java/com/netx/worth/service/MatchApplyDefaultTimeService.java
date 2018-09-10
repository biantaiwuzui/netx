package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchApplyDefaultTimeDto;
import com.netx.worth.mapper.MatchApplyDefaultTimeMapper;
import com.netx.worth.model.MatchApplyDefaultTime;
import org.springframework.stereotype.Service;

@Service
public class MatchApplyDefaultTimeService extends ServiceImpl<MatchApplyDefaultTimeMapper,MatchApplyDefaultTime> {
    /**
     * 添加默认时间
     * @param matchApplyDefaultTimeDto
     * @return
     */
    public String insertMatchApplyDefaultTime(MatchApplyDefaultTimeDto matchApplyDefaultTimeDto){
        MatchApplyDefaultTime matchApplyDefaultTime=new MatchApplyDefaultTime();
        matchApplyDefaultTime.setEndTime(matchApplyDefaultTimeDto.getEndTime());
        matchApplyDefaultTime.setId(matchApplyDefaultTimeDto.getId());
        matchApplyDefaultTime.setMatchId(matchApplyDefaultTimeDto.getMatchId());
        matchApplyDefaultTime.setStartTime(matchApplyDefaultTimeDto.getStartTime());
        matchApplyDefaultTime.setType(matchApplyDefaultTimeDto.getType());
        return insert(matchApplyDefaultTime)?matchApplyDefaultTime.getId():null;
    }
    /**
     * 更新默认时间
     * @param matchApplyDefaultTimeDto
     * @return
     */
    public String updateMatchApplyDefaultTime(MatchApplyDefaultTimeDto matchApplyDefaultTimeDto){
        MatchApplyDefaultTime matchApplyDefaultTime=new MatchApplyDefaultTime();
        matchApplyDefaultTime.setEndTime(matchApplyDefaultTimeDto.getEndTime());
        matchApplyDefaultTime.setId(matchApplyDefaultTimeDto.getId());
        matchApplyDefaultTime.setMatchId(matchApplyDefaultTimeDto.getMatchId());
        matchApplyDefaultTime.setStartTime(matchApplyDefaultTimeDto.getStartTime());
        matchApplyDefaultTime.setType(matchApplyDefaultTimeDto.getType());
        return updateById(matchApplyDefaultTime)?matchApplyDefaultTime.getId():null;
    }
    /**
     * 通过matchId获取默认报名时间
     * @param matchId
     * @return
     */
    public MatchApplyDefaultTime getMatchApplyDefaultTimeByMacthId(String matchId,Integer type){
        EntityWrapper<MatchApplyDefaultTime> matchApplyDefaultTimeEntityWrapper=new EntityWrapper<>();
        matchApplyDefaultTimeEntityWrapper.where("match_id={0} and type={1}",matchId,type);
        return selectOne(matchApplyDefaultTimeEntityWrapper);
    }


    /**
     * 删除默认时间通过比赛id
     * @param matchId
     * @return
     */
    public boolean deleteMatchApplyDefaultTimeByMacthId(String matchId){
        EntityWrapper<MatchApplyDefaultTime> matchApplyDefaultTimeEntityWrapper=new EntityWrapper<>();
        matchApplyDefaultTimeEntityWrapper.where("match_id={0}",matchId);
        return delete(matchApplyDefaultTimeEntityWrapper);
    }

    /**
     * 是否填写了默认的时间
     * @param matchId
     * @return
     */
    public Boolean IsWriteMatchApply(String matchId) {
        EntityWrapper<MatchApplyDefaultTime> matchApplyDefaultTimeEntityWrapper=new EntityWrapper<>();
        matchApplyDefaultTimeEntityWrapper.where("match_id={0} and type=1",matchId);
        if(selectCount(matchApplyDefaultTimeEntityWrapper)>0){
            return true;
        }
        return false;
    }
}
