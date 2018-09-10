package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchReviewDTO;
import com.netx.worth.mapper.MatchReviewMapper;
import com.netx.worth.model.MatchReview;
import com.netx.worth.vo.MatchReviewVo;
import javafx.beans.binding.ObjectExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 比赛审核者管理
 * Created by Yawn on 2018/8/1.
 */
@Service
public class MatchReviewService extends ServiceImpl<MatchReviewMapper, MatchReview>{

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 获取该场赛事的审核人员
     * @param matchId
     * @return
     */
    public List<MatchReview> listMatchReview(String matchId) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper = new EntityWrapper<>();
        matchReviewEntityWrapper.where("match_id = {0}", matchId)
//                .and("is_accept=0")
                .orderBy("organizer_kind",false);
        return selectList(matchReviewEntityWrapper);

    }

    public List<MatchReview> listNoAcceptMatchReview(String matchId) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper = new EntityWrapper<>();
        matchReviewEntityWrapper.where("match_id = {0}", matchId)
                .and("is_accept=0")
                .orderBy("organizer_kind",false);
        return selectList(matchReviewEntityWrapper);
    }

    /**
     *  获取所有被邀请的人
     * @param matchId
     * @return
     */
    public List<MatchReviewVo> getReviewListBYMatchId(String matchId) {
        Map<String,String> map=new HashMap<>();
        map.put("match_id",matchId);
        return super.baseMapper.getReviewListBYMatchId(map);
    }
    /**
     *  获取所有被邀请的人
     * @param matchId
     * @return
     */
    public boolean IsHaveReviewBYMatchId(String matchId) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper=new EntityWrapper<>();
        matchReviewEntityWrapper.where("match_id={0}",matchId);
        if(selectCount(matchReviewEntityWrapper)>0){
            return true;
        }
        return false;
    }
    /**
     *  获取所有接受邀请的人
     * @param matchId
     * @return
     */
    public List<MatchReviewVo> getReviewListAcceptBYMatchId(String matchId) {
        Map<String,Object> map=new HashMap<>();
        map.put("match_id",matchId);
        map.put("is_accept",1);
        return super.baseMapper.getReviewListBYMatchId(map);
    }

    /**
     * 获得主办单位
     * @param matchId
     * @return
     */
    public List<MatchReviewVo> getMainReviewAcceptBYMatchId(String matchId) {
        Map<String,Object> map=new HashMap<>();
        map.put("match_id",matchId);
        map.put("organizer_kind","0");
        return super.baseMapper.getReviewListBYMatchId(map);
    }
    /**
     * 添加审核者
     * @param dto
     * @return
     */
    public boolean addMatchReview(MatchReviewDTO dto) {
        MatchReview matchReview = new MatchReview();
        if (!StringUtils.isEmpty(dto.getId())) {
            matchReview.setId(dto.getId());
        }
        matchReview.setMatchId(dto.getMatchId());
        matchReview.setUserId(dto.getUserId());
        matchReview.setOrganizerName(dto.getOrganizerName());
        matchReview.setOrganizerKind(dto.getOrganizerKind());
        matchReview.setMarchantId(dto.getMerchantId());
        matchReview.setAccept(true);
        return insertOrUpdate(matchReview);
    }

    /**
     * 更新举办方
     * @param dto
     * @param id
     * @return
     */
    public boolean updateMatchReview(MatchReviewDTO dto, String id) {
        MatchReview matchReview = new MatchReview();
        matchReview.setMatchId(dto.getMatchId());
        matchReview.setUserId(dto.getUserId());
        matchReview.setOrganizerName(dto.getOrganizerName());
        matchReview.setOrganizerKind(dto.getOrganizerKind());
        EntityWrapper<MatchReview> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id);
        return update(matchReview, entityWrapper);
    }

    /**
     * 删除审核者
     * @param id
     * @return
     */
    public boolean deleteMatchReview(String id) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper = new EntityWrapper<>();
        matchReviewEntityWrapper.where("id = {0}", id);
        return delete(matchReviewEntityWrapper);
    }
    /**
     * 删除审核者
     * @param matchId
     * @return
     */
    public boolean deleteMatchReviewByMatchId(String matchId) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper = new EntityWrapper<>();
        matchReviewEntityWrapper.where("match_id = {0}", matchId);
        return delete(matchReviewEntityWrapper);
    }

    /**
     * 是否通过审核
     * @param userId
     * @param matchId
     * @param isPass
     * @return
     */
    public boolean passReview(String userId, String matchId, boolean isPass) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper = new EntityWrapper<>();
        matchReviewEntityWrapper.where("match_id = {0}", matchId)
                .and("user_id = {0}", userId);
        MatchReview matchReview = selectOne(matchReviewEntityWrapper);
        if (matchReview == null) {
            throw new RuntimeException("你还不是比赛的受邀举办方");
        }
        matchReview.setApprove(isPass);
        return update(matchReview, matchReviewEntityWrapper);
    }

    /**
     * 不通过审核
     * @param dto
     * @return
     */
    public boolean noPassReview(MatchReviewDTO dto) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper = new EntityWrapper<>();
        MatchReview matchReview = new MatchReview();
        matchReview.setApprove(false);
        matchReviewEntityWrapper.where("match_id = {0}", dto.getMatchId())
                .and("user_id = {0}",dto.getUserId());
        return update(matchReview, matchReviewEntityWrapper);
    }


    /**
     * 接受邀请
     * @param dto
     * @return
     */
    public boolean acceptInvite(MatchReviewDTO dto) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper = new EntityWrapper<>();
        MatchReview matchReview = new MatchReview();
        matchReview.setAccept(true);
        matchReviewEntityWrapper.where("match_id = {0}", dto.getMatchId())
                .and("user_id = {0}",dto.getUserId());
        return update(matchReview, matchReviewEntityWrapper);
    }

    public String getMatchReviewKind(String matchId, String userId) {
        EntityWrapper<MatchReview> matchReviewEntityWrapper = new EntityWrapper<>();
        matchReviewEntityWrapper.where("match_id = {0}", matchId)
                .and("user_id = {0}", userId);
        MatchReview matchReview = selectOne(matchReviewEntityWrapper);
        if (matchReview == null)
            return "";
        return String.valueOf(matchReview.getOrganizerKind());
    }

    /**
     * 获得一级标签列表
     * @param merchantId
     * @return
     */
    public List<String> getOneTags(String merchantId){
        return baseMapper.getOneTags(merchantId);
    }
}
