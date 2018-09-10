package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.common.enums.EvaluateTypeEnum;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.vo.common.*;
import com.netx.common.wz.dto.common.TypeIdPageDto;
import com.netx.fuse.biz.ucenter.EvaluateFuseAction;
import com.netx.ucenter.biz.common.EvaluateAction;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.common.CommonEvaluate;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Create by wongloong on 17-9-9
 */
@Api(description = "评价与评论")
@RestController
@RequestMapping("/api/evaluate")
public class EvaluateController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(EvaluateController.class);
    @Autowired
    private EvaluateAction evaluateAction;
    @Autowired
    private EvaluateFuseAction evaluateFuseAction;
    @Autowired
    private CommonServiceProvider commonServiceProvider;
    @Autowired
    private MessagePushAction messagePushAction;
    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;

    @ApiOperation(value = "评论动态")
    @PostMapping("/getMyDynamic")
    public JsonResult getMyDynamic(@Valid @RequestBody CommonListDto dto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try{
            userId = getUserId(request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("请重新登录后再操作");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try {
            return JsonResult.successJsonResult(evaluateAction.getMyDynamic(userId,dto.getCurrentPage(),dto.getSize()));
        } catch (Exception e) {
            logger.error("查询动态异常："+e.getMessage(), e);
            return JsonResult.fail("查询动态异常");
        }
    }


    @ApiOperation(value = "添加评价",notes = "返回值key:result 类型:CommonEvaluate")
    @PostMapping("/add")
    public JsonResult add(@Valid @RequestBody EvaluateAddRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String fromUserId = null;
        try{
            fromUserId = getUserId(request);
            if(StringUtils.isEmpty(fromUserId)){
                return JsonResult.fail("请重新登录后再操作");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try {
            CommonEvaluate commonEvaluate = evaluateAction.addEvaluate(dto,fromUserId);
            if (commonEvaluate!=null) {
                EvaluateTypeEnum evaluateTypeEnum = dto.getEvaluateType();
                String content = StringUtils.isEmpty(commonEvaluate.getpId())?"评论了我"+evaluateTypeEnum.getName():"回复了我";
                wzCommonImHistoryAction.add(fromUserId,dto.getToUserId(),content,dto.getTypeId(),evaluateTypeEnum.getMessageTypeEnum(),evaluateTypeEnum.getPushMessageDocTypeEnum(),null);
                return JsonResult.success().addResult("result",commonEvaluate);
            } else {
                return JsonResult.fail("添加评论失败");
            }
        } catch (Exception e) {
            logger.error("添加评价评论异常："+e.getMessage(), e);
            return JsonResult.fail("添加评价评论异常");
        }
    }

    @ApiOperation(value = "批量添加评价",notes = "返回值key:result 类型:List<CommonEvaluate>")
    @PostMapping("/batchAdd")
    public JsonResult add(@Valid @RequestBody List<EvaluateAddRequestDto> dto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String fromUserId = null;
        try{
            fromUserId = getUserId(request);
            if(StringUtils.isEmpty(fromUserId)){
                return JsonResult.fail("请重新登录后再操作");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try {
            List<CommonEvaluate> list=new ArrayList<>();
            for (EvaluateAddRequestDto evaluate : dto) {
                CommonEvaluate commonEvaluate = evaluateAction.addEvaluate(evaluate,fromUserId);
                if(commonEvaluate!=null){
                    list.add(commonEvaluate);
                }
            }
            if(list.size()==0){
                return JsonResult.fail("操作失败");
            }
            JsonResult jsonResult;
            if(list.size()!=dto.size()){
                jsonResult = JsonResult.success("有部分操作失败");
            }else{
                jsonResult = JsonResult.success();
            }
            return jsonResult.addResult("result",list);
        } catch (Exception e) {
            logger.error("添加评价评论异常："+e.getMessage(), e);
            return JsonResult.fail("添加评价评论异常");
        }
    }

    @ApiOperation(value = "查询评价",notes = "返回值key:result")
    @PostMapping("/list")
    public JsonResult queryList(@RequestBody @Valid EvaluateQueryRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return successResult(evaluateAction.querysMap(request));
        } catch (Exception e) {
            logger.error("查询评价出错："+e.getMessage(),e);
            return JsonResult.fail("查询评价出错");
        }
    }

    @ApiOperation(value = "查询评价的回复列表")
    @PostMapping("/replyList")
    public JsonResult replyList(@RequestParam("id") String id) {
        if(StringUtils.isEmpty(id)){
            return JsonResult.fail("评论id不能为空");
        }
        try {
            return successResult(evaluateAction.getCommentOne(id));
        } catch (Exception e) {
            logger.error("查询回复出错："+e.getMessage(),e);
            return JsonResult.fail("查询回复出错");
        }
    }

    @ApiOperation(value = "查询我的评价",notes = "返回值key:result 类型: Page<CommonEvaluate>")
    @PostMapping("/myEvaluateList")
    public JsonResult queryList(@Valid @RequestBody MyEvaluateQueryRequestDto dto, BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(dto.getToUserId())){
            try {
                dto.setToUserId(getUserId(request));
                if(StringUtils.isEmpty(dto.getToUserId())){
                    return JsonResult.fail("授权过期，请重新登录");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            return successResult(evaluateAction.queryMyEvaluate(dto));
        }catch (Exception e){
            logger.error("查询我的评价出错："+e.getMessage(),e);
            return JsonResult.fail("查询我的评价出错");
        }
    }

    @ApiOperation(value = "统计评价数量",notes = "返回值key:result 类型:Integer")
    @PostMapping("/countEvaluate")
    public JsonResult countEvaluate(@Valid @RequestBody CountEvaluatRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonEvaluate wzCommonEvaluate=new CommonEvaluate();
            wzCommonEvaluate.setTypeId(request.getTypeId());
            return JsonResult.success().addResult("result",commonServiceProvider.getEvaluateService().getCount(null));
        }catch (Exception e){
            logger.error("统计评价数量出错："+e.getMessage(),e);
            return JsonResult.fail("统计评价数量出错");
        }
    }


    @ApiOperation(value = "根据商家查询商品评价",notes = "返回值key:result 类型:Page<CommonEvaluate>")
    @PostMapping("/businessQueryList")
    public JsonResult businessQueryList(@RequestBody BusinessEvaluateQueryRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return JsonResult.successJsonResult(evaluateFuseAction.businessQueryPage(request));
        } catch (Exception e) {
            logger.error("查询评价出错："+e.getMessage(),e);
            return JsonResult.fail("查询评价出错");
        }
    }

    @ApiOperation(value = "判断订单是否已经被指定用户评论",notes = "返回值key:result 类型:WetherExistEvaluateReponseDto")
    @PostMapping("/wetherExistEvaluate")
    public JsonResult wetherExistEvaluate(@RequestBody WetherExistEvaluateRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonEvaluate wzCommonEvaluate=new CommonEvaluate();
            wzCommonEvaluate.setOrderId(request.getOrderId());
            wzCommonEvaluate.setFromUserId(request.getUserId());
            int count = evaluateAction.getCount(wzCommonEvaluate);
            WetherExistEvaluateReponseDto wd=new WetherExistEvaluateReponseDto();
            if(count>0){
                wd.setWetherExist(true);
            }else{
                wd.setWetherExist(false);
            }
            return JsonResult.success().addResult("result",wd);

        } catch (Exception e) {
            logger.error("查询评价出错："+e.getMessage(),e);
            return JsonResult.fail("查询评价出错");
        }
    }


}
