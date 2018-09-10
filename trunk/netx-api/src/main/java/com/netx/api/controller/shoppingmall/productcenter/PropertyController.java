package com.netx.api.controller.shoppingmall.productcenter;


import com.netx.shopping.biz.productcenter.PropertyAction;
import com.netx.shopping.vo.RegisterMerchantResponseVo;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created By CHENQIAN
 * Description: 属性控制层
 * Date: 2018-05-09
 */
@Api(description = "属性相关接口")
@RestController
@RequestMapping("/api/business/property")
public class PropertyController {

    private Logger logger = LoggerFactory.getLogger(PropertyController.class);

    @Autowired
    private PropertyAction propertyAction;

    @ApiOperation(value = "根据商家id查询属性id", notes = "根据商家id查询属性id")
    @PostMapping("/getPropertyId")
    public JsonResult getPropertyId(@RequestParam String merchantId){
        try {
            RegisterMerchantResponseVo res = propertyAction.getPropertIdByMerchantId(merchantId);
            if(res == null){
                return JsonResult.fail("根据商家id查询属性id失败！");
            }
            return JsonResult.success().addResult("res", res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("根据商家id查询属性id异常！");
        }
    }
}
