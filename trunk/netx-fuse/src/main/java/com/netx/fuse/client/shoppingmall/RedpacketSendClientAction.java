package com.netx.fuse.client.shoppingmall;

import com.netx.common.user.util.MapOrObjectUtil;
import com.netx.common.vo.business.CheckIsSendFinishRequestDto;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketSendFuseAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketRecordAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketSendAction;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
@Service
public class RedpacketSendClientAction {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedpacketSendAction redpacketSendAction;

    @Autowired
    RedpacketRecordAction redpacketRecordAction;

    @Autowired
    RedpacketSendFuseAction redpacketSendFuseAction;

    /**
     * 定时器，检查红包是否发放完毕
     */
    public boolean checkIsSendFinish(Map map){
        if (StringUtils.isNotEmpty(map.get("redpacketSendId").toString())){
            try {
                CheckIsSendFinishRequestDto dto = MapOrObjectUtil.mapToObject(map,CheckIsSendFinishRequestDto.class);
                boolean res= redpacketSendFuseAction.checkIsSendFinish(dto.getRedpacketSendId());
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        return false;
    }


    /**
     *钱包页面获取用户红包累计领取金额
     */
    public BigDecimal seeWalletRedpacket(String userId){
        if (StringUtils.isNotEmpty(userId)){
            try {
                BigDecimal amount= redpacketRecordAction.seeWalletRedpacket(userId);
                return amount;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }

        return new BigDecimal(0);
    }


}
