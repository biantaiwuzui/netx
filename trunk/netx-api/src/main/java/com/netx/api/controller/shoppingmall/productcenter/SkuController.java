package com.netx.api.controller.shoppingmall.productcenter;


import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By CHENQIAN
 * Description: SKU-控制层
 * Date: 2018-05-09
 */
@Api(description = "SKU-相关接口")
@RestController
@RequestMapping("/api/business/sku")
public class SkuController {

    private Logger logger = LoggerFactory.getLogger(SkuController.class);
}
