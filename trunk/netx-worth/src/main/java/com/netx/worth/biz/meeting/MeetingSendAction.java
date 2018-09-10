package com.netx.worth.biz.meeting;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.MeetingSendMapper;
import com.netx.worth.model.MeetingSend;

/**
 * <p>
 * 活动聚会表发起表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-09-06
 */
@Service
public class MeetingSendAction extends ServiceImpl<MeetingSendMapper, MeetingSend>{
    private Logger logger = LoggerFactory.getLogger(MeetingSendAction.class);
}
