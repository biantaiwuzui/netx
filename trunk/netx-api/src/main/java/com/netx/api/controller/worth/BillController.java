package com.netx.api.controller.worth;

import java.util.Map;

import com.netx.worth.service.WorthServiceprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.settlement.SettlementAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "收支模块")
@RequestMapping("/wz/bill")
@RestController
public class BillController {
    private Logger logger = LoggerFactory.getLogger(BillController.class);
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;

    @ApiOperation(value = "获取用户的交易额")
    @PostMapping(value = "/getTradingVolume")
    public JsonResult getTradingVolume(String userId) {
        if (!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户id不能为空");
        }
        try {
            Map<String, Object> map = settlementAction.getTradingVolume(userId);
            if (map != null) {
                return JsonResult.success().addResult("map", map);
            } else {
                return JsonResult.fail("获取用户的交易额失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取用户的交易额失败");
        }
    }
}
