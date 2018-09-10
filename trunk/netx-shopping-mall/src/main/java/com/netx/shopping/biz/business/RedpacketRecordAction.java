package com.netx.shopping.biz.business;

import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.vo.business.RedpacketRecordDto;
import com.netx.shopping.service.business.RedpacketRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 *
 */

@Transactional
@Service("oldRedpacketRecordAction")
public class RedpacketRecordAction{

    @Autowired
    RedpacketRecordService redpacketRecordService;

    public RedpacketRecordDto setUserInfo(RedpacketRecordDto redpacketRecordDto,UserSynopsisData userSynopsisData){
        redpacketRecordDto.setAge(userSynopsisData.getAge());
        redpacketRecordDto.setHeadImgUrl(userSynopsisData.getHeadImgUrl());
        redpacketRecordDto.setLv(userSynopsisData.getAge());
        redpacketRecordDto.setSex(userSynopsisData.getSex());
        redpacketRecordDto.setNickname(userSynopsisData.getNickName());
        return redpacketRecordDto;
    }

    
    public BigDecimal seeWalletRedpacket(String userId){
        return redpacketRecordService.getAmount(userId);
    }
}