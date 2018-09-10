package com.netx.fuse.client.shoppingmall;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.common.vo.business.UpadteRedpacketPoolRequestDto;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketPoolFuseAction;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketSendFuseAction;
import com.netx.shopping.biz.merchantcenter.MerchantPacketSetAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketPoolAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketSendAction;
import com.netx.shopping.model.redpacketcenter.RedpacketPool;
import com.netx.shopping.service.redpacketcenter.RedpacketPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author hj.Mao   红包池远程接口
 * @Date 2017-11-20
 */
@Service
public class RedpacketPoolClientAction {
    Logger logger= LoggerFactory.getLogger(RedpacketSendClientAction.class);

    @Autowired
    RedpacketPoolAction redpacketPoolAction;

    @Autowired
    com.netx.fuse.client.ucenter.LuckyMoneyClientAction luckyMoneyClient;

    @Autowired
    RedpacketPoolService redpacketPoolService;

    @Autowired
    RedpacketSendAction redpacketSendAction;

    @Autowired
    MerchantPacketSetAction packetSetAction;

    @Autowired
    RedpacketPoolFuseAction redpacketPoolFuseAction;

    @Autowired
    RedpacketSendFuseAction redpacketSendFuseAction;

    /**
     * 更改红包池,红包池加入金额或者取出金额
     */
    public boolean upadteRedpacketPool(@RequestBody UpadteRedpacketPoolRequestDto requestDto){
        if (requestDto != null){
            try{
                boolean res= redpacketPoolAction.upadteRedpacketPool(requestDto);
                return res;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }

        return false;
    }

    /**
     * 更新今天红包总额
     */
    public void updateReapacketAmount() throws Exception{
        //将昨天待生效的红包设置改为今日的红包设置
        luckyMoneyClient.updateLuckMoneySet();
        redpacketPoolAction.judgeRedpacketPool();
        //更新今天红包总额
        RedpacketPool redpacketPool = redpacketPoolService.selectOne(new EntityWrapper<>());
        redpacketPool.setRedpacketAmount(redpacketPool.getTotalAmount());
        redpacketPoolService.updateById(redpacketPool);
        //发放红包
        redpacketSendFuseAction.send();
        //将开启红包启动金额的商家拿钱加入红包池
        redpacketPoolFuseAction.getSellerAmountToPool();
    }
}
