package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchProgressDTO;
import com.netx.worth.mapper.MatchProgressMapper;
import com.netx.worth.model.MatchProgress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 比赛赛制
 * Created by Yawn on 2018/8/13 0013.
 */
@Service
public class MatchProgressService extends ServiceImpl<MatchProgressMapper, MatchProgress>{

    /**
     * 添加或更新
     * @param dto
     * @return
     */
    public boolean addMatchProgress(MatchProgressDTO dto) {
        MatchProgress matchProgress = new MatchProgress();
        if (StringUtils.isNoneBlank(dto.getId())) {
            matchProgress.setId(dto.getId());
        }
        matchProgress.setBeginTime(dto.getBeginTime());
        matchProgress.setEndTime(dto.getEndTime());
        matchProgress.setMatchId(dto.getMatchId());
        matchProgress.setMatchName(dto.getMatchName());
        matchProgress.setSort(dto.getSort());
        return insertOrUpdate(matchProgress);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean deleteMatchProgress(String id) {
        EntityWrapper<MatchProgress> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return delete(entityWrapper);
    }
    /**
     * 删除通过比赛id
     * @param matchId
     * @return
     */
    public boolean deleteMatchProgressByMatchId(String matchId) {
        EntityWrapper<MatchProgress> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId);
        return delete(entityWrapper);
    }

    /**
     * 根据比赛选择赛程
     * @param matchId
     * @return
     */
    public List<MatchProgress> listMatchProgress(String matchId) {
        EntityWrapper<MatchProgress> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId).orderBy("sort",true);
        return selectList(entityWrapper);
    }

    /**
     * 根据赛程查询By赛程id
     * @param id
     * @return
     */
    public MatchProgress getMatchProgressVoById(String id) {
        return selectById(id);
    }

    /**
     *
     * @param matchId
     * @return
     */
    public MatchProgress getMinMatchProgressByMatchId(String matchId) {
        EntityWrapper<MatchProgress> matchProgressEntityWrapper=new EntityWrapper<>();
        matchProgressEntityWrapper.where("match_id={0}",matchId)
                .orderBy("sort",true);
        return selectOne(matchProgressEntityWrapper);
    }

    /**
     * 是否已经填写
     * @param matchId
     * @return
     */
    public Boolean IsWriteMatchProgress(String matchId) {
        EntityWrapper<MatchProgress> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id={0}",matchId);
        int size=selectCount(entityWrapper);
        if(size>0){
            return true;
        }
        return  false;
    }
}
