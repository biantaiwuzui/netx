package com.netx.worth.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.MatchVenueGroupMapper;
import com.netx.worth.model.MatchVenueGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 场次和赛组
 * Created by Yawn on 2018/8/26 0026.
 */
@Service
public class MatchVenueGroupService extends ServiceImpl<MatchVenueGroupMapper, MatchVenueGroup> {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

}
