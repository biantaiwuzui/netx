package com.netx.api.controller.worth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netx.fuse.biz.worth.SettlementFuseAction;
import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.settlement.SettlementAction;

import io.swagger.annotations.Api;

@Api(description = "结算模块", hidden = true)
@RequestMapping("/wz/settlement")
@RestController
public class SettlementController {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private SettlementFuseAction settlementFuseAction;

    @PostMapping(value = "/start")
    public JsonResult start() {
        try {
        	settlementFuseAction.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail();
        }
        return JsonResult.success();
    }
}
