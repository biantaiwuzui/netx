package com.netx.api.controller.shoppingmall.business;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.common.vo.business.UpadteRedpacketPoolRequestDto;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketPoolFuseAction;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketSendFuseAction;
import com.netx.fuse.client.ucenter.LuckyMoneyClientAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketPoolAction;
import com.netx.shopping.model.redpacketcenter.RedpacketPool;
import com.netx.shopping.service.redpacketcenter.RedpacketPoolService;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(description = "红包池相关接口")
@RestController
@RequestMapping("/api/business/RedpacketPool")
public class RedpacketPoolController {
    private Logger logger= LoggerFactory.getLogger(RedpacketPoolController.class);

    @Autowired
    RedpacketPoolAction redpacketPoolAction;

    @Autowired
    RedpacketPoolService redpacketPoolService;

    @Autowired
    LuckyMoneyClientAction luckyMoneyClient;

    @Autowired
    RedpacketPoolFuseAction redpacketPoolFuseAction;

    @Autowired
    RedpacketSendFuseAction redpacketSendFuseAction;

    @ApiOperation(value = "更改红包池",notes = "红包池加入金额或者取出金额")
    @PostMapping("/upadteRedpacketPool")
    public JsonResult upadteRedpacketPool(@Valid @RequestBody UpadteRedpacketPoolRequestDto requestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            boolean res= redpacketPoolAction.upadteRedpacketPool(requestDto);
            if (!res){
                return JsonResult.fail("更改红包池失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("更改红包池失败！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "更新今天红包总额")
    @PostMapping("/updateReapacketAmount")
    public JsonResult updateReapacketAmount(){
        try {
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
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("更新今天红包总额失败!");
        }
        return JsonResult.success();
    }
}