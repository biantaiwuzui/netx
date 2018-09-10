package com.netx.api.controller.shoppingmall.order;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddDeliverymanRequestDto;
import com.netx.common.vo.business.DelectDeliverymanRequestDto;
import com.netx.common.vo.business.GetDeliverymanRequestDto;
import com.netx.common.vo.business.UpdateDeliverymanRequestDto;
import com.netx.shopping.biz.order.DeliverymanAction;
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
import java.util.List;

/**
 * Created By liwei
 * Description: 商家配送人员控制层
 * Date: 2017-09-16
 */
@Api(description = "商家配送人员相关接口")
@RestController
@RequestMapping("/api/business/DeliverymanController")
public class DeliverymanController extends BaseController {
   /* private Logger logger = LoggerFactory.getLogger(UserOrderAddressController.class);

    @Autowired
    DeliverymanAction deliverymanAction;

    @ApiOperation(value = "增加商家配送人员", notes = "增加商家配送人员")
    @PostMapping("/add")
    public JsonResult add(@Valid @RequestBody AddDeliverymanRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<String> address= deliverymanAction.add(request);
            if (address != null){
                return JsonResult.success().addResult("address",address);
            }
            return JsonResult.fail("增加商家配送人员失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("增加商家配送人员失败！");
        }
    }

    @ApiOperation(value = "删除商家配送人员", notes = "删除商家配送人员")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DelectDeliverymanRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = deliverymanAction.delete(request);
            return getResult(res,"删除商家配送人员失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除商家配送人员失败!");
        }
    }

    @ApiOperation(value = "获取商家配送人员列表", notes = "获取商家配送人员列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody GetDeliverymanRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<Object> res = deliverymanAction.getDeliverymanList(request);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取商家配送人员列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家配送人员列表失败！");
        }
    }

    @ApiOperation(value = "修改商家配送人员", notes = "修改商家配送人员")
    @PostMapping("/update")
    public JsonResult update(@Valid @RequestBody UpdateDeliverymanRequestDto request , BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = deliverymanAction.update(request);
            return getResult(res,"修改商家配送人员失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改商家配送人员失败!");
        }
    }*/
}
