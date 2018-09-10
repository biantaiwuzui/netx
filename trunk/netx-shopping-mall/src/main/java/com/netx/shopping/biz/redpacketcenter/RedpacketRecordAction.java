package com.netx.shopping.biz.redpacketcenter;


import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.vo.business.RedpacketRecordDto;
import com.netx.shopping.service.redpacketcenter.RedpacketRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 网商-红包记录表 前端控制器
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@Service("newRedpacketRecordAction")
public class RedpacketRecordAction {

    @Autowired
    RedpacketRecordService redpacketRecordService;

    public RedpacketRecordDto setUserInfo(RedpacketRecordDto redpacketRecordDto, UserSynopsisData userSynopsisData){
        redpacketRecordDto.setAge(userSynopsisData.getAge());
        redpacketRecordDto.setHeadImgUrl(userSynopsisData.getHeadImgUrl());
        redpacketRecordDto.setLv(userSynopsisData.getAge());
        redpacketRecordDto.setSex(userSynopsisData.getSex());
        redpacketRecordDto.setNickname(userSynopsisData.getNickName());
        return redpacketRecordDto;
    }


    public BigDecimal seeWalletRedpacket(String userId){
        BigDecimal amount = redpacketRecordService.getAmount(userId);
        return amount==null?BigDecimal.ZERO:amount;
    }

}
