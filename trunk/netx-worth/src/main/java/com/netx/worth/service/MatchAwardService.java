package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchAwardDTO;
import com.netx.worth.mapper.MatchAwardMapper;
import com.netx.worth.model.MatchAward;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 奖项设置
 * Created by Yawn on 2018/8/4 0004.
 */
@Service
public class MatchAwardService extends ServiceImpl<MatchAwardMapper, MatchAward> {

    /**
     * 添加奖项
     * @param dto
     * @return
     */
    public boolean addMatchAward(MatchAwardDTO dto) {
        MatchAward matchAward = new MatchAward();
        if (!StringUtils.isEmpty(dto.getId())) {
            matchAward.setId(dto.getId());
        }
        matchAward.setMatchId(dto.getMatchId());
        matchAward.setAwardName(dto.getAwardName());
        matchAward.setAwardNumber(dto.getAwardNumber());
        matchAward.setBonus(dto.getBonus());
        matchAward.setPrize(dto.getPrize());
        matchAward.setCertificate(dto.getCertificate());
        matchAward.setSort(dto.getSort());
        return insertOrUpdate(matchAward);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean deleteMatchAwardByMatchId(String id) {
        EntityWrapper<MatchAward> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return delete(entityWrapper);
    }
    /**
     * 更新
     * @param dto
     * @param id
     * @return
     */
    public boolean updateMatchAward(MatchAwardDTO dto, String id) {
        MatchAward matchAward = new MatchAward();
        matchAward.setMatchId(dto.getMatchId());
        matchAward.setAwardName(dto.getAwardName());
        matchAward.setBonus(dto.getBonus());
        matchAward.setPrize(dto.getPrize());
        matchAward.setCertificate(dto.getCertificate());
        matchAward.setMatchId(dto.getMatchId());
        EntityWrapper<MatchAward> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return matchAward.update(entityWrapper);
    }

    /**
     * 根据比赛ID查询
     * @param matchId
     * @return
     */
    public List<MatchAward> listMatchAwardByMatchId(String matchId) {
        EntityWrapper<MatchAward> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId).orderBy("sort",true);
        return selectList(entityWrapper);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public MatchAward getMatchAwardById(String id) {
        EntityWrapper<MatchAward> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return selectOne(entityWrapper);
    }


    /**
     * 判断是否有该奖项员
     * @param matchAwardDTO
     * @return
     */
    public MatchAward getMatchAward(MatchAwardDTO matchAwardDTO){
        EntityWrapper<MatchAward> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchAwardDTO.getMatchId())
                .and("award_name",matchAwardDTO.getAwardName());
        return selectOne(entityWrapper);
    }

    /**
     * 判断是否已经填完了
     * @param matchId
     * @return
     */
    public Boolean IsWriteMacthTicket(String matchId) {
        EntityWrapper<MatchAward> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId);
        if(selectCount(entityWrapper)>0){
            return true;
        }
        return false;
    }
}
