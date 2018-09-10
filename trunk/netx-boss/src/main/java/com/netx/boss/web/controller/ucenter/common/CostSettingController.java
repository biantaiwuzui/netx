package com.netx.boss.web.controller.ucenter.common;

import com.netx.boss.web.controller.BaseController;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.CostSettingResponseDto;
import com.netx.common.vo.common.DisposeCostSettingRequestDto;
import com.netx.common.vo.common.WzCommonCostSettingAddDto;
import com.netx.ucenter.biz.common.CostAction;
import com.netx.ucenter.model.common.CommonCostSetting;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * Create by wongloong on 17-9-3
 */
@Api(value = "费用设置", description = "费用设置")
@RestController
@RequestMapping("/common/costSetting")
public class CostSettingController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(CostSettingController.class);
    @Autowired
    private CostAction costService;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    private RedisCache clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    @ApiOperation(value = "保存,普通管理员可以操作，存在未审核的不能添加")
    @PostMapping("/save")
    public JsonResult save(@RequestBody WzCommonCostSettingAddDto commonCostSetting) {
        try {
            return this.getResult(costService.saveCostSetting(commonCostSetting),"保存失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("保存异常");
        }
    }

    public JsonResult getResult(boolean success,String msg) {
        if(success) {
            return JsonResult.success();
        }else {
            return JsonResult.fail(msg);
        }
    }

    @ApiOperation(value = "保存，超级管理员可以操作")
    @PostMapping("/saveSuper")
    public JsonResult saveSuper(@RequestBody WzCommonCostSettingAddDto commonCostSetting) {
        try {
            CommonCostSetting data = new CommonCostSetting();
            BeanUtils.copyProperties(commonCostSetting, data);
            data.setState(1);
            data.setDisposeTime(new Date());
            if (commonServiceProvider.getCostService().insertOrUpdate(data)) {
                updateWzCommonCostSettingRedis(data);
                return JsonResult.success();
            } else {
                return JsonResult.fail("保存失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("保存异常");
        }
    }

    private void updateWzCommonCostSettingRedis(CommonCostSetting setting){
        CostSettingResponseDto dto = VoPoConverter.copyProperties(setting,CostSettingResponseDto.class);
        RedisKeyName redisKeyName = new RedisKeyName("costSetting", RedisTypeEnum.OBJECT_TYPE,null);
        clientRedis().put(redisKeyName.getCommonKey(),dto);
    }

    @ApiOperation(value = "审核", notes = "dispose(必填):0审核未通过,1审核通过,disposeUser(必填),审核人id,id(必填)")
    @PostMapping("/dispose")
    public JsonResult dispose(@Valid @RequestBody DisposeCostSettingRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonCostSetting setting = commonServiceProvider.getCostService().selectById(request.getId());
            if (null == setting || StringUtils.isEmpty(setting.getId())) {
                logger.warn("未找到改记录id:" + request.getId());
                return JsonResult.fail("操作失败");
            }
            setting.setDisposeTime(new Date());
            setting.setDisposeUser(request.getDisposeUser());
            setting.setState(request.getDispose());
            if (commonServiceProvider.getCostService().updateById(setting)) {
                if(setting.getState()==1){
                    updateWzCommonCostSettingRedis(setting);
                }
                return JsonResult.success();
            } else {
                return JsonResult.fail("操作失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("审核异常");
        }
    }

    @ApiOperation("查询费用设定")
    @PostMapping("/query")
    public JsonResult query() {
        try {
            return JsonResult.success().addResult("result",costService.query());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "查询费用设定",notes = "返回值key:result 类型:CommonCostSetting")
    @PostMapping("/queryList")
    public JsonResult queryUnaudited() {
        try {
            return JsonResult.success().addResult("result",costService.queryListforBoss());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }
}
