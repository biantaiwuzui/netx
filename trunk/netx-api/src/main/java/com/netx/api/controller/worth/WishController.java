package com.netx.api.controller.worth;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


import com.netx.common.common.enums.WorthTypeEnum;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.fuse.biz.worth.WishFuseAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.utils.json.ApiCode;

import com.netx.common.wz.dto.wish.*;
import com.netx.worth.biz.common.IndexAction;
import com.netx.worth.biz.wish.WishAction;
import com.netx.worth.model.*;
import com.netx.worth.service.WishBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.api.controller.BaseController;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.utils.json.JsonResult;
import com.netx.worth.util.PageWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(description = "心愿模块")
@RequestMapping("/wz/wish")
@RestController
public class WishController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(WishController.class);
    @Autowired
//    心愿模块
    private WishAction wishAction;
    @Autowired
    private WishBankService wishBankService;
//    交互模块
    @Autowired
    private WishFuseAction wishFuseAction;
//    图片模块
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
//    添加最新支持心愿的记录
    @Autowired
    private IndexAction indexAction;
    @Autowired
    private ScoreAction scoreAction;


    @ApiOperation(value = "推荐的心愿列表")
    @PostMapping(value = "/refereeList", consumes = {"application/json"})
    public JsonResult refereeList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Page<WishReferee> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        List<WishReferee> list = null;
        try {
            list = wishAction.getRefereeListByWish(commonPageDto.getId(), commonPageDto.getUserId(), page);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看推荐的心愿列表失败");
        }
        return JsonResult.success().addResult("map",PageWrapper.wrapper(page.getTotal(), list));
    }


    @ApiOperation(value = "我使用的心愿")
    @PostMapping(value = "/applyList", consumes = {"application/json"})
    public JsonResult applyList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Page<WishApply> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        Map<String, Object> map = null;
        try {
            map = wishAction.getApplyListByWish(commonPageDto.getId(), page);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我使用的心愿失败");
        }
        return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
    }

    
    @ApiOperation(value = "我监管的心愿")
    @PostMapping(value = "/managerList", consumes = {"application/json"})
    public JsonResult managerList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Page<WishManager> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        List<WishManager> list = null;
        try {
            list = wishAction.getManagerListByUserId(commonPageDto.getUserId(), page);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我监管的心愿失败");
        }
        return JsonResult.success().successJsonResult(PageWrapper.wrapper(page.getTotal(), list));
    }

    
    @ApiOperation(value = "我支持的心愿")
    @PostMapping(value = "/supportList", consumes = {"application/json"})
    public JsonResult supportList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Page page = new Page(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        Map<String, Object> map = null;
        UserGeo userGeo = null;
            try {
                userGeo = getGeoFromRequest(request);
            }catch (Exception e){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        try {
            List<WishSendListDto> list = wishFuseAction.supportListById(commonPageDto.getUserId(),page,userGeo.getLon(),userGeo.getLat());
            if (list != null) {
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("查看我支持的心愿失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }

    
    @ApiOperation(value = "我发布的心愿")
    @PostMapping(value = "/publishList", consumes = {"application/json"})
    public JsonResult sendList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Page<Wish> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        Map<String, Object> map = null;
        UserGeo userGeo = null;
        try {
            userGeo = getGeoFromRequest(request);
            if(StringUtils.isEmpty(userGeo.getUserId())){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            List<WishSendListDto> list = wishFuseAction.publishListById(userGeo.getUserId(),commonPageDto.getUserId(),page,userGeo.getLon(),userGeo.getLat());
            if (list != null) {
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("查看我发布的心愿失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看我发布的心愿失败");
        }
    }


    @ApiOperation(value = "审查用款")
    @PostMapping(value = "/examine")
    public JsonResult examine(String wishId,HttpServletRequest request) {
        if(!StringUtils.hasText(wishId)) {
            return JsonResult.fail("wishId");
        }
        UserGeo userGeo = null;
        try {
            userGeo = getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            WishExamineDto wishExamineDto = wishFuseAction.examine(wishId,userGeo.getUserId());
            return JsonResult.success().addResult("WishExamineDto",wishExamineDto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }

    }
    
    
    @ApiOperation(value = "心愿详情，wish对象是心愿情况，wishUserRole是心愿角色，数据结构为：角色 + 对象，共有4个角色：wish：心愿发布者，默认拥有使用心愿的权限，referee：心愿推荐者，support：心愿支持者，manager：心愿管理者。")
    @PostMapping(value = "/detail")
    public JsonResult detail(String wishId,HttpServletRequest request) {
        String userId = super.getUserIdInWorth(request);
        if (!StringUtils.hasText(wishId)) {
            return JsonResult.fail("心愿ID不能为空");
        }
        UserGeo userGeo = null;
        try {
            userGeo = getGeoFromRequest(request);
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            Map<String,Object> map = wishFuseAction.selectRoleByUserId(wishId,userGeo.getUserId(), userGeo.getLat(),userGeo.getLon());
            return JsonResult.successJsonResult(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }

    }

    
    @ApiOperation(value = "发布心愿")
    @PostMapping(value = "/publish", consumes = {"application/json"})
    public JsonResult publish(@Validated @RequestBody WishPublishDto wishPublishDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Wish wish = wishFuseAction.publish(wishPublishDto);
            if(wish != null) {
                scoreAction.addScore(wish.getUserId(), StatScoreEnum.SS_PUBLISH_WORTH);
                wish.setWishImagesUrl((addImgUrlPreUtil.addImgUrlPres(wish.getWishImagesUrl(),AliyunBucketType.ActivityBucket)));
                wish.setWishImagesTwoUrl((addImgUrlPreUtil.addImgUrlPres(wish.getWishImagesTwoUrl(),AliyunBucketType.ActivityBucket)));
                return JsonResult.success().addResult("wish",wish);
            }else {
                return JsonResult.fail("不能发布相同的心愿");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发布心愿失败");
        }
    }

    
    @ApiOperation(value = "编辑心愿")
    @PostMapping(value = "/edit", consumes = {"application/json"})
    public JsonResult edit(@Validated @RequestBody WishPublishDto wishPublishDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = super.getUserIdInWorth(request);
        try {
            if(StringUtils.isEmpty(wishPublishDto.getId())){
                return JsonResult.fail("心愿id不能为空");
            }
            return getResult(wishAction.edit(wishPublishDto),"修改成功","修改失败");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑心愿失败");
        }
    }


    @ApiOperation(value = "检查是否能够支持")
    @PostMapping(value = "/checkSupport", consumes = {"application/json"})
    public JsonResult checkSupport(HttpServletRequest request) {
        String userId = super.getUserIdInWorth(request);
        if (!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户ID不能为空");
        }
        try {
            boolean flag = wishFuseAction.checkSupport(userId);
            return JsonResult.success().addResult("检查是否能够支持", flag);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("检查是否能够支持失败");
        }
    }

    
    @ApiOperation(value = "取消心愿")
    @PostMapping(value = "/cancel")
    public JsonResult cancel(@RequestParam("id") String id,HttpServletRequest httpServletRequest) {
        String userId = null;
        try {
            userId = getUserId(httpServletRequest);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if (!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户ID不能为空");
        }
        if (!StringUtils.hasText(id)) {
            return JsonResult.fail("心愿ID不能为空");
        }
        try {
            boolean success = wishAction.cancel(id, userId);
            //取消扣10分
            if (success){
                scoreAction.addScore(userId, -StatScoreEnum.SS_PUBLISH_WORTH.score());
            }
            return getResult(success, "只有心愿发起者才可以取消");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("只有心愿发起者才可以取消");
        }
    }

    
    @ApiOperation(value = "推荐心愿")
    @PostMapping(value = "/refereeAccept", consumes = {"application/json"})
    public JsonResult refereeAccept(@Validated @RequestBody WishRefereeDto wishRefereeDto, BindingResult bindingResult,HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            wishRefereeDto.setUserId (super.getUserId(httpServletRequest));
        }catch (Exception e){
            return JsonResult.fail ( ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = wishAction.refereeAccept(wishRefereeDto);
            return getResult(success, "推荐心愿失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("推荐心愿失败"+e.getMessage());
        }
    }

    
    @ApiOperation(value = "拒绝推荐（暂不考虑）")
    @PostMapping(value = "/refereeRefuse", consumes = {"application/json"})
    public JsonResult refereeRefuse(@Validated @RequestBody WishRefereeDto wishRefereeDto, BindingResult bindingResult,HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            wishRefereeDto.setUserId (super.getUserId(httpServletRequest));
        }catch (Exception e){
            return JsonResult.fail ( ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean success = wishAction.refereeRefuse(wishRefereeDto);
            return getResult(success, "拒绝推荐失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("拒绝推荐失败"+e.getMessage());
        }
    }

    
    @ApiOperation(value = "支持心愿。该方法必须在支付成功后调用。")
    @PostMapping(value = "/support")
    public JsonResult support(String id, BigDecimal amount,HttpServletRequest request) {
        String userId = super.getUserIdInWorth(request);

        if (!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户ID不能为空");
        }

        if (!StringUtils.hasText(id)) {
            return JsonResult.fail("心愿ID不能为空");
        }
        if (amount == null || amount.doubleValue() <= 0) {
            return JsonResult.fail("金额不正确");
        }
        try {
            Boolean success = wishAction.support(id, userId, amount);
            if(success==null) {
                return getResult(success, "发布者不能支持");
            }
            if(success){
                //添加最新支持心愿的记录
                indexAction.addHistory(WorthTypeEnum.WISH_TYPE,userId,id);
                return JsonResult.success();
            }
            return JsonResult.fail("只有心愿推荐成功才能支持心愿");
        }
        catch (RuntimeException e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发布者不能支持！");
        }
    }


    @ApiOperation(value = "申请用款，如果通过后，选‘提现’和‘付款给未在平台注册的用户’选项之后，心愿的钱会到使用者账户中，由他自行分配")
    @PostMapping(value = "/apply", consumes = {"application/json"})
    public JsonResult apply(@Validated @RequestBody WishApplyDto wishApplyDto, WishBankDto wishBankDto,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            wishFuseAction.apply(wishApplyDto,wishBankDto);
            return JsonResult.success();
        }catch (RuntimeException ex) {
            ex.printStackTrace();
            return JsonResult.fail(ex.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }

    
    @ApiOperation(value = "批准用款")
    @PostMapping(value = "/acceptApply")
    public JsonResult authApply(String applyId, String description,HttpServletRequest request) {
        String userId = super.getUserIdInWorth(request);
        if (!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户ID不能为空");
        }
        if (!StringUtils.hasText(applyId)) {
            return JsonResult.fail("心愿使用表ID不能为空");
        }
        if (!StringUtils.hasText(description)) {
            return JsonResult.fail("描述不能为空");
        }
        try {
            boolean success = wishFuseAction.acceptApply(applyId, userId, description);
            return getResult(success, "批准用款失败!!");
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage ());
        }
//        return JsonResult.fail("批准用款失败!!");
    }

    
    @ApiOperation(value = "拒绝用款")
    @PostMapping(value = "/refuseApply")
    public JsonResult refuseApply(String applyId,String description,HttpServletRequest request) {
        String userId = super.getUserIdInWorth(request);
        if (!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户ID不能为空");
        }
        if (!StringUtils.hasText(applyId)) {
            return JsonResult.fail("心愿使用表ID不能为空");
        }
        if (!StringUtils.hasText(description)) {
            return JsonResult.fail("描述不能为空");
        }
        try {
            boolean success = wishAction.refuseApply(applyId, userId, description);
            return getResult(success, "拒绝用款失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("拒绝用款失败");
        }
    }

    
    @ApiOperation(value = "心愿参与人")
    @PostMapping(value = "/wishUsers")
    public JsonResult wishUsers(String wishId) {
        if (!StringUtils.hasText(wishId)) {
            return JsonResult.fail("心愿ID不能为空");
        }
        try {
            return JsonResult.success().addResult("map",wishFuseAction.getWishUsers(wishId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看心愿参与人失败");
        }
    }
    
    
    @ApiOperation(value = "检查这个用户是否有未完成的心愿")
    @PostMapping(value = "/checkHasUnComplete", consumes = {"application/json"})
    public JsonResult checkHasUnComplete(HttpServletRequest request) {
        String userId = super.getUserIdInWorth(request);
    	if (!StringUtils.hasText(userId)) {
    		return JsonResult.fail("用户ID不能为空");
    	}
    	try {
    		boolean hasUnComplete = wishAction.checkHasUnComplete(userId);
    		return JsonResult.success().addResult("hasUnComplete", hasUnComplete);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		return JsonResult.fail("检查这个用户是否有未完成的心愿失败");
    	}
    }
    
    
    @ApiOperation(value = "心愿投诉后推送接口")
    @PostMapping(value = "/wishComplaint", consumes = {"application/json"})
    public JsonResult wishComplaint(String wishId) {
    	if (!StringUtils.hasText(wishId)) {
            return JsonResult.fail("心愿ID不能为空");
        }
    	try {
    		wishFuseAction.pushMessage(wishId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
            return JsonResult.fail("心愿投诉后推送接口请求失败");
		}
    	return JsonResult.success();
    }
    
    
    @ApiOperation(value = "心愿裁决通过后的推送接口")
    @PostMapping(value = "/wishComplaintSuccess", consumes = {"application/json"})
    public JsonResult wishComplaintSuccess(String wishId) {
    	if (!StringUtils.hasText(wishId)) {
    		return JsonResult.fail("心愿ID不能为空");
    	}
    	try {
    		wishFuseAction.pushMessageAndRefund(wishId);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		return JsonResult.fail("心愿裁决通过后的推送接口请求失败");
    	}
    	return JsonResult.success();
    }

    
    @ApiOperation(value = "定时任务：心愿发起24小时后，检查心愿是否推荐成功，并拒绝掉未完成推荐的人")
    @PostMapping(value = "/checkRefereeSuccess")
    public JsonResult checkRefereeSuccess(String wishId) {
        if (!StringUtils.hasText(wishId)) {
            return JsonResult.fail("心愿ID不能为空");
        }
        try {
            wishFuseAction.checkRefereeSuccess(wishId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("启动定时任务：心愿发起24小时后，检查心愿是否成功，并拒绝掉未完成推荐的人失败");
        }
        return JsonResult.success();
    }

    
    @ApiOperation(value = "定时任务：检查是否发起成功")
    @PostMapping(value = "/checkSuccess")
    public JsonResult checkSuccess(String wishId) {
        if (!StringUtils.hasText(wishId)) {
            return JsonResult.fail("心愿ID不能为空");
        }
        try {
            wishFuseAction.checkSuccess(wishId);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("启动定时任务：检查是否发起成功失败");
        }
        return JsonResult.success();
    }

    
    @ApiOperation(value = "定时任务：是否评价")
    @PostMapping(value = "/checkEvaluate")
    public JsonResult checkEvaluate(String wishId) {
        if (!StringUtils.hasText(wishId)) {
            return JsonResult.fail("心愿ID不能为空");
        }
        try {
        	wishFuseAction.checkEvaluate(wishId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("启动定时任务：是否评价失败");
        }
        return JsonResult.success();
    }

    
    @ApiOperation(value = "心愿列表", notes = "通用的心愿列表接口，根据不同的条件返回")
    @PostMapping(value = "/list")
    public JsonResult list(@Valid @RequestBody WishSearchDto wishSearchDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            wishSearchDto.setLat(getLat(request));
            wishSearchDto.setLon(getLon(request));
            List<WishListDto> list = wishFuseAction.list(wishSearchDto);
            if(list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取心愿列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取心愿列表异常！");
        }
    }

}
