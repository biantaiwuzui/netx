package com.netx.api.controller.shoppingmall.ordercenter;

import com.netx.common.express.AliyunExpressService;
import com.netx.shopping.biz.merchantcenter.MerchantExpressAction;
import com.netx.shopping.biz.merchantcenter.ShippingFeeAction;
import com.netx.shopping.vo.AddShippingFeeRequestDto;
import com.netx.shopping.vo.ExpressResponse;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created By 黎子安
 * Description: 物流快递控制层
 * Date: 2017-09-16
 */
@Api(description = "物流快递相关接口")
@RestController
@RequestMapping("/api/business/express")
public class ExpressController {

    private Logger logger = LoggerFactory.getLogger(ExpressController.class);

    @Autowired
    MerchantExpressAction merchantExpressAction;

    @Autowired
    ShippingFeeAction shippingFeeAction;
    
    @Autowired
    AliyunExpressService aliyunExpressService;

    @ApiOperation(value = "设置商家物流费用", notes = "物流费用")
    @PostMapping("/addShippingFee")
    public JsonResult addShippingFee(@Valid @RequestBody AddShippingFeeRequestDto dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String shippingFeeId = shippingFeeAction.addOrUpdate(dto.getMerchantId(),dto.getFee());
            if(shippingFeeId==null){
                return JsonResult.fail("设置物流费用失败");
            }
            return JsonResult.success().addResult("shippingFeeId",shippingFeeId);
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("设置物流费用异常");
        }
    }

    @ApiOperation(value = "查询快递公司", notes = "list")
    @PostMapping("/list")
    public JsonResult list(){
        try {
            List<ExpressResponse> list = merchantExpressAction.getList();
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("查询快递公司失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询快递公司失败！");
        }
    }

    @ApiOperation(value = "定时任务", notes = "更新快递公司表")
    @PostMapping("/add")
    public JsonResult add(){
        try {
            merchantExpressAction.add();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("更新快递公司表失败！");
        }
    }
    
    @ApiOperation(value = "根据merchantOrderInfo的两个字段查询订单物流详情")
    @PostMapping("/queryExpressDetail")
    public JsonResult queryExpressDetail(String ShippingLogisticsNo,String ShippingCode){
        try{ 
            return JsonResult.success(aliyunExpressService.getExpressData(ShippingLogisticsNo,ShippingCode));
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.fail("获取订单物流信息异常");
        }
    }
    
    
}
