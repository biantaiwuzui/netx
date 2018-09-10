package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.matchEvent.MatchTicketDTO;
import com.netx.worth.mapper.MatchTicketMapper;
import com.netx.worth.model.MatchTicket;
import com.netx.worth.vo.MatchBuyTicketVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门票管理
 * Created by Yawn on 2018/8/1 0001.
 */
@Service
public class MatchTicketService extends ServiceImpl<MatchTicketMapper, MatchTicket> {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 发布门票
     * @param dto
     * @return
     */
    public boolean publishTicket(MatchTicketDTO dto) {
        MatchTicket matchTicket = new MatchTicket();
        matchTicket.setId(dto.getId());
        matchTicket.setZoneId(dto.getMatchZoneOrMatchId());
        matchTicket.setDescription(dto.getDescription());
        matchTicket.setFree(dto.getFree());
        matchTicket.setNumber(dto.getNumber());
        matchTicket.setBeginTime(dto.getBeginTime());
        matchTicket.setEndTime(dto.getEndTime());
        matchTicket.setOptimisticLocking(0);
        matchTicket.setTicketName(dto.getTicketName());
        matchTicket.setSort(dto.getSort());
        matchTicket.setVenueIds(dto.getVenueIds());
        matchTicket.setDefalut(dto.isDefault());
        matchTicket.setUseDefalut(dto.isUseDefalut());
        return insertOrUpdate(matchTicket);
    }

    /**
     * 删除指定的门票
     * @param id
     * @return
     */
    public boolean deleteTicket(String id) {
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.where("id = {0}", id);
        return delete(ticketEntityWrapper);
    }

    /**
     * 通过比赛区id删除门票信息
     * @param zoneId
     * @return
     */
    public boolean deleteTicketByZoneId(String zoneId) {
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.where("zone_id = {0} and is_defalut=1", zoneId);
        return delete(ticketEntityWrapper);
    }
    /**
     * 通过比赛区id删除门票信息
     * @param zoneId
     * @return
     */
    public boolean deleteAllTicketByZoneId(String[] zoneId) {
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.in("zone_id", zoneId);
        return delete(ticketEntityWrapper);
    }
    /**
     * 删除门票
     * @param id
     * @return
     */
    public boolean deleteTicketById(String id) {
        if (StringUtils.isEmpty(id))
            throw new RuntimeException("门票ID为空");
        return deleteById(id);
    }
    /**
     * 更新门票
     * @param dto
     * @return
     */
    public boolean updateTicket(MatchTicketDTO dto) {
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.where("zone_id = {0}", dto.getMatchZoneOrMatchId())
                .and("ticket_name={0}", dto.getTicketName());
        MatchTicket matchTicket = new MatchTicket();
        matchTicket.setDescription(dto.getDescription());
        matchTicket.setFree(dto.getFree());
        matchTicket.setNumber(dto.getNumber());
        return update(matchTicket, ticketEntityWrapper);
    }
    /**
     * 通过门票id更新乐观锁
     * @param dto
     * @return
     */
    public boolean updateTicketById(MatchTicketDTO dto,Integer newoptimisticLocking,Integer oldoptimisticLocking) {
        MatchTicket matchTicket = new MatchTicket();
        matchTicket.setNumber(dto.getNumber());
        matchTicket.setOptimisticLocking(newoptimisticLocking);
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.where("id = {0}", dto.getId())
                .and("optimistic_locking={0}", oldoptimisticLocking);
        return update(matchTicket,ticketEntityWrapper);
    }

    /**
     * 获得默认的门票档次信息
     * @param matchId
     * @return
     */
    public List<MatchTicket> getDefaultTicketByMatchId(String matchId){
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.where("zone_id = {0} and is_defalut=1", matchId).orderBy("sort",true);
        return selectList(ticketEntityWrapper);
    }
    /**
     * 获取指定赛区的门票
     * @param zoneId
     * @return
     */
     public List<MatchTicket> listMatchTicket(String zoneId) {
         EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
         ticketEntityWrapper.where("zone_id = {0} and is_defalut=0", zoneId).orderBy("sort",true);
         return selectList(ticketEntityWrapper);
     }
    /**
     * 获取指定赛区的门票(最后的结果)
     * @param zoneId
     * @return
     */
    public List<MatchTicket> getMatchTicketByZoneId(String zoneId) {
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.where("zone_id = {0} and is_defalut=0 and use_defalut=0", zoneId).orderBy("sort",true);
        return selectList(ticketEntityWrapper);
    }
    /**
     * 获取指定赛区的门票(最后的结果)
     * @param zoneIds
     * @return
     */
    public List<MatchTicket> getAllMatchTicketByZoneIds(String[] zoneIds) {
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.in("zone_id", zoneIds).where(" is_defalut=0 and use_defalut=0");
        return selectList(ticketEntityWrapper);
    }

    /**
     * 获取门票的结束时间
     * @param matchId
     * @return
     */
    public Date getEndTicketTimeByMatchId(String matchId) {
         return baseMapper.getEndTicketTimeByMatchId(matchId);
    }
    /**
     * 判断该赛区是否不使用默认
     */
    public boolean isUseDefalutGrade(String zoneId) {
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.where("zone_id = {0} and is_defalut=0 and use_defalut=1", zoneId)
                .orderBy("sort",true);
        if(selectCount(ticketEntityWrapper)>0) {
            return true;
        }
        return false;
    }
    /**
     * 通过一些信息获取相关门票的所有信息
     * @param matchTicketDTO
     * @return
     */
    public MatchTicket getMatchTicket(MatchTicketDTO matchTicketDTO){
        EntityWrapper<MatchTicket> ticketEntityWrapper = new EntityWrapper<>();
        ticketEntityWrapper.where("zone_id = {0}", matchTicketDTO.getMatchZoneOrMatchId()).and("ticket_name={0}", matchTicketDTO.getTicketName());
        return selectOne(ticketEntityWrapper);
    }

    /**
     * 通过门票id查询门票
     * @param matchTicketId
     * @return
     */
    public MatchTicket getMatchTicketBYMatchTicketId(String matchTicketId){
        return selectById(matchTicketId);
    }

    /**
     * 是否已设置了门票
     * @param zoneIds
     * @return
     */
    public Boolean IsWriteMatchTicket(String[] zoneIds) {
        EntityWrapper<MatchTicket> matchTicketEntityWrapper=new EntityWrapper<>();
        matchTicketEntityWrapper.in("zone_id",zoneIds);
        if(selectCount(matchTicketEntityWrapper)>0){
            return true;
        }
        return false;
    }

    /**
     * 根据赛制查询相应的门票
     * @param progressId
     * @return
     */
    public List<MatchTicket> getMatchTicketByProgress(String progressId){
        return baseMapper.getMatchTicketByProgress(progressId);
    }

    /**
     * 通过userId获取已经购买的票
     * @param userId
     * @return
     */
    public List<MatchBuyTicketVo> getBuyTicketByUserIdANDMacthId(String userId,String matchId){
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",userId);
        map.put("match_id",matchId);
        return baseMapper.getBuyTicketByUserId(map);
    }
}
