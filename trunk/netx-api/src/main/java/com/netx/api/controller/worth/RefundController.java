package com.netx.api.controller.worth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.common.RefundAction;

import io.swagger.annotations.Api;

@Api(description = "退款模块", hidden = true)
@RequestMapping("/wz/refund")
@RestController
public class RefundController {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private RefundAction refundAction;

    @PostMapping(value = "/start")
    public JsonResult start() {
        try {
            refundAction.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail();
        }
        return JsonResult.success();
    }
}
