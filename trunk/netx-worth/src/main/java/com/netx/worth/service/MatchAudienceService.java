package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.model.UserMacthInfo;
import com.netx.common.wz.dto.matchEvent.MatchAudienceDTO;
import com.netx.worth.mapper.MatchAudienceMapper;
import com.netx.worth.model.MatchAudience;
import com.netx.worth.model.MatchMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参赛观众管理
 * Created by Yawn on 2018/8/1 0001.
 */
@Service
public class MatchAudienceService extends ServiceImpl<MatchAudienceMapper, MatchAudience>{

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 添加或修改观众
     * @param dto
     * @return
     */
    public boolean addOrUpdateAudience(MatchAudienceDTO dto) {
        MatchAudience audience = new MatchAudience();
        audience.setMatchId(dto.getMatchId());
        audience.setMatchTicketId(dto.getMatchTicketId());
        audience.setPay(dto.getPay());
        audience.setUserId(dto.getUserId());
        if (!StringUtils.isEmpty(dto.getId())) {
            audience.setId(dto.getId());
        }
        return insertOrUpdate(audience);
    }

    /**
     * 添加观众
     * @param dto
     * @return
     */
    public String addAudience(MatchAudienceDTO dto) {
        MatchAudience audience = new MatchAudience();
        audience.setMatchId(dto.getMatchId());
        audience.setMatchTicketId(dto.getMatchTicketId());
        audience.setPay(dto.getPay());
        audience.setUserId(dto.getUserId());
        audience.setAttend(dto.getAttend());
        audience.setQuit(false);
        audience.setCreateTime(new Date());
        audience.setUpdateTime(new Date());
        return insert(audience)?audience.getId():null;
    }
    /**
     * 修改支付状态
     * @param dto
     * @return
     */
    public boolean updateAudiencePayStatus(MatchAudienceDTO dto) {
        MatchAudience audience = new MatchAudience();
        audience.setPay(dto.getPay());
        EntityWrapper<MatchAudience> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id = {0}", dto.getUserId())
                .and("id = {0}", dto.getId());
        return update(audience,entityWrapper);
    }
    /**
     * 删除某个观众
     * @param userId
     * @param matchTrickId
     * @return
     */
    public boolean deleteAudience(String userId, String matchTrickId) {
        EntityWrapper<MatchAudience> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id = {0}", userId)
                .and("match_ticket_id = {0}", matchTrickId);
        return delete(entityWrapper);
    }

    /**
     * 支付门票
     * @param dto
     * @return
     */
    public boolean payTicket(MatchAudience dto) {
        EntityWrapper<MatchAudience> entityWrapper = new EntityWrapper<>();
        MatchAudience audience = new MatchAudience();
        audience.setPay(true);
        entityWrapper.where("user_id = {0}", dto.getUserId())
                .and("match_ticket_id = {0}", dto.getMatchTicketId());
        return update(audience, entityWrapper);
    }

    /**
     * 观众出席
     * @param dto
     * @return
     */
    public boolean attendMatch(MatchAudience dto) {
        EntityWrapper<MatchAudience> entityWrapper = new EntityWrapper<>();
        MatchAudience audience = new MatchAudience();
        audience.setAttend(true);
        entityWrapper.where("user_id = {0}", dto.getUserId())
                .and("match_ticket_id = {0}", dto.getMatchTicketId());
        return update(audience, entityWrapper);
    }

    /**
     * 查看比赛出席观众
     * @return
     */
    public List<UserMacthInfo> listAttendAudience(String venudId) {
        Map<String, String> map = new HashMap<>(1);
        map.put("match_id", venudId);
        return baseMapper.listAudienceByMatchId(map);
    }
    /**
     * 查看已支付观众
     * @return
     */
    public List<MatchAudience> listpaidAudience(String matchId) {
        EntityWrapper<MatchAudience> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_id = {0}", matchId)
                .and("is_pay = 1");
        return selectList(entityWrapper);
    }

    /**
     * 获取是否有该观众人员
     * @param matchTicketId
     * @param userId
     * @return
     */
    public int getMatchAudienceIsHave(String matchTicketId,String userId){
        EntityWrapper<MatchAudience> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_ticket_id = {0}", matchTicketId)
                    .and("user_id={0}",userId).and("is_pay",1);
        return selectCount(entityWrapper);
    }


    public List<MatchAudience> getMatchAudienceIsPay(String matchTicketId,String userId){
        EntityWrapper<MatchAudience> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("match_ticket_id = {0}", matchTicketId)
                .and("is_pay = 1").and("user_id = {0}",userId);
        return selectList(entityWrapper);
    }

    /**
     * 获取自己购买的门票
     * @param id
     * @param userId
     * @return
     */
    public MatchAudience getAudienceIsPayById(String id,String userId){
        EntityWrapper<MatchAudience> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id = {0}", id)
                .and("is_pay = 1").and("user_id = {0}",userId);
        return selectOne(entityWrapper);
    }
}
