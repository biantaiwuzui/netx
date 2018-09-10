package com.netx.api.controller.shoppingmall.business;

import com.netx.common.vo.business.AddSellerCategoryRequestDto;
import com.netx.common.vo.business.DeleteCategoryRequestDto;
import com.netx.shopping.biz.business.SellerCategoryAction;

import com.netx.shopping.model.business.SellerCategory;
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
 * Created By wj.liu
 * Description: 系统类别控制层
 * Date: 2017-09-04
 */
@Api(description = "系统类别相关接口")
@RestController
@RequestMapping("/api/business/sellerCategory")
public class SellerCategoryController {


    private Logger logger = LoggerFactory.getLogger(SellerCategoryController.class);

}
