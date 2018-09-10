package com.netx.api.controller.shoppingmall.business;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddPacketSetRequestDto;
import com.netx.common.vo.business.SelectPacketSetRequestDto;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketPoolFuseAction;
import com.netx.shopping.biz.merchantcenter.MerchantPacketSetAction;
import com.netx.shopping.model.merchantcenter.MerchantPacketSet;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(description = "商家红包设置相关接口")
@RestController
@RequestMapping("/api/business/packetset")
public class PacketSetManageController extends BaseController {

    private Logger logger= LoggerFactory.getLogger(PacketSetManageController.class);

    @Autowired
    MerchantPacketSetAction packetSetAction;

    @Autowired
    RedpacketPoolFuseAction redpacketPoolFuseAction;

    @ApiOperation(value = "增加，修改红包设置",notes = "注册商家是第一次点击的话就是新增,提交之后后面点击的话就是修改操作")
    @PostMapping("/saveOrUpdate")
    public JsonResult saveOrUpdate(@Valid @RequestBody AddPacketSetRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(org.springframework.util.StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(org.springframework.util.StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            String packetSetId= packetSetAction.saveOrUpdate(request);
            if(packetSetId!=null && StringUtils.isEmpty(packetSetId)){
                return JsonResult.fail("增加或修改红包设置失败！");
            }
            return JsonResult.success().addResult("packetSetId",packetSetId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("增加或修改红包设置异常！");
        }
    }

    @ApiOperation(value = "获取红包设置信息详情",notes = "根据传递Id来获取红包设置ID")
    @PostMapping("/get")
    public JsonResult get(@Valid @RequestBody SelectPacketSetRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            MerchantPacketSet packetSet= packetSetAction.get(request.getId());
            if(packetSet != null){
                return JsonResult.success().addResult("packetSet",packetSet);
            }
            return JsonResult.fail("获取红包设置失败");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取红包设置异常");
        }
    }

    @ApiOperation(value = "根据红包ID删除红包设置信息记录")
    @DeleteMapping("/delete")
    public JsonResult deletePacketSet(@Valid @RequestBody SelectPacketSetRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            boolean bRet= packetSetAction.deletePacketSetById(request.getId());
            if(bRet==false){
                return JsonResult.fail("删除"+request.getId()+"的红包设置失败");
            }
            return JsonResult.success("删除"+request.getId()+"成功");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除"+request.getId()+"的红包设置异常");
        }
    }

    @ApiOperation(value = "将开启红包启动金额的商家拿钱加入红包池")
    @PostMapping("/getSellerAmountToPool")
    public JsonResult getSellerAmountToPool(){
        try{
            redpacketPoolFuseAction.getSellerAmountToPool();
            return JsonResult.success("开启红包启动金额的商家拿钱加入红包池成功！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("开启红包启动金额的商家拿钱加入红包池异常！");
        }
    }
}