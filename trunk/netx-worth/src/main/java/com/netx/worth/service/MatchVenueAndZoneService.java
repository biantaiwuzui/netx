package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.wz.dto.matchEvent.MatchVenueAndZoneDto;
import com.netx.utils.cache.RedisCache;
import com.netx.worth.mapper.MatchVenueAndZoneMapper;
import com.netx.worth.model.MatchVenueAndZone;
import com.netx.worth.vo.TitleAndVenueIdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 赛区和场次的中间表
 * @author 老肥猪
 * @since  2018-8-15
 */
@Service
public class MatchVenueAndZoneService extends ServiceImpl<MatchVenueAndZoneMapper,MatchVenueAndZone> {
    @Autowired
    private RedisInfoHolder redisInfoHolder;
    private RedisCache redisCache;
    private final String MATCH_FLOW_INCR="MATCH_FLOW_INCR";

    /**
     * 获得redis的方法
     * @return
     */
    private RedisCache clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }
    /**
     * 添加
     * @param matchVenueAndZoneDto
     * @return
     */
    public boolean addMatchVenueAndZone(MatchVenueAndZoneDto matchVenueAndZoneDto){
        MatchVenueAndZone matchVenueAndZone=new MatchVenueAndZone();
        matchVenueAndZone.setMatchVenueId(matchVenueAndZoneDto.getMatchVenueId());
        matchVenueAndZone.setMatchZoneId(matchVenueAndZoneDto.getMatchZoneId());
        Long sort=clientRedis().incrBy(MATCH_FLOW_INCR+":"+matchVenueAndZoneDto.getMatchVenueId(),1);
        matchVenueAndZone.setSort(sort.intValue());
        return insert(matchVenueAndZone);
    }

    /**
     * 删除赛区和场次的中间表通过赛区id
     * @param zoneId
     * @return
     */
    public boolean deleteMatchVenueAndZoneByZoneId(String zoneId){
        EntityWrapper<MatchVenueAndZone> matchVenueAndZoneEntityWrapper=new EntityWrapper<>();
        matchVenueAndZoneEntityWrapper.where("match_zone_id={0}",zoneId);
        return delete(matchVenueAndZoneEntityWrapper);
    }

    /**
     * 通过赛区id获取场次的
     * @param matchZoneId
     * @return
     */
    public List<TitleAndVenueIdVo> getMatchVunueBYMatchZoneId(String matchZoneId){
        return super.baseMapper.getMatchVunueBYMatchZoneId(matchZoneId);
    }
}
