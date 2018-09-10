package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.DisposeOtherSettingDto;
import com.netx.common.vo.common.OrtherSetRequestDto;
import com.netx.common.vo.common.OtherSettingAddRequestDto;
import com.netx.common.vo.currency.WzCommonOtherSetResponseDto;
import com.netx.ucenter.biz.common.OtherSetAction;
import com.netx.ucenter.model.common.CommonOtherSet;
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
 * Create by wongloong on 17-9-4
 */
@Api(value = "其他设置", description = "其他设置")
@RestController
@RequestMapping("/api/otherSet")
public class OtherSetController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(OtherSetController.class);
    @Autowired
    OtherSetAction otherSetService;

    @Autowired
    CommonServiceProvider commonServiceProvider;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private RedisCache clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    @ApiOperation(value = "添加/更新其他设置,如果存在未审核的就不能添加，普通管理员")
    @PostMapping("/saveOrUpdate")
    public JsonResult saveOrUpdate(@RequestBody OtherSettingAddRequestDto request) {
        try {
            boolean res= otherSetService.save(request);
            return res?JsonResult.success().addResult("result",true):JsonResult.success();
        } catch (Exception e) {
            logger.error("添加其他设置异常信息"+e.getMessage(), e);
            return JsonResult.fail("添加其他设置异常信息");
        }
    }

    @ApiOperation(value = "查询其他设置，审核与未审核")
    @PostMapping("/query")
    public JsonResult query(OrtherSetRequestDto dto) {
        try {
            CommonOtherSet o ;
            if(dto.getType()==1){
                RedisKeyName redisKeyName = getOtherRedisKey();
                o = (CommonOtherSet)clientRedis().get(redisKeyName.getCommonKey());
                if(o==null){
                    o = otherSetService.query(1,false);
                    redisCache.put(redisKeyName.getCommonKey(),o);
                }
            }else {
                o = otherSetService.query(dto.getType(),false);
            }
            return JsonResult.success().addResult("result",o);
        } catch (Exception e) {
            logger.error("查询其他设置出错："+e.getMessage(), e);
            return JsonResult.fail("查询其他设置出错");
        }
    }

    @PostMapping("/dispose")
    @ApiOperation(value = "审核其他设置")
    public JsonResult dispose(@RequestBody @Valid DisposeOtherSettingDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonOtherSet set = commonServiceProvider.getOtherSetService().selectById(request.getId());
            OrtherSetRequestDto dto=new OrtherSetRequestDto();
            set.setUpdateTime(new Date());
            set.setDisposeUserId(request.getDisposeUserId());
            set.setCanUse(request.getCanUse());
            if (null == set || StringUtils.isEmpty(set.getId())) {
                logger.warn("未找到该记录id:" + request.getId());
                return JsonResult.fail("操作失败");
            }
            if(request.getCanUse()==1){
                dto.setType(1);
                CommonOtherSet o=otherSetService.query(dto.getType(),false);
                commonServiceProvider.getOtherSetService().deleteById(o);
                if(!commonServiceProvider.getOtherSetService().updateById(set)){
                    return  JsonResult.fail("更新失败");
                }
                this.setOtherSetRedis(set);
            }else{
                if(!commonServiceProvider.getOtherSetService().deleteById(set)){
                    return JsonResult.fail("更新失败");
                }
            }
            return JsonResult.success();
        } catch (Exception e) {
            logger.error("审核其他信息出错："+e.getMessage(), e);
            return JsonResult.fail("审核其他信息出错");
        }
    }

    /**
     * 设置redis的值
     * @param set
     */
    private void setOtherSetRedis(CommonOtherSet set){
        WzCommonOtherSetResponseDto dto= VoPoConverter.copyProperties(set,WzCommonOtherSetResponseDto.class);
        RedisKeyName redisKeyName = getOtherRedisKey();
        clientRedis().put(redisKeyName.getCommonKey(),dto);
    }

    private RedisKeyName getOtherRedisKey(){
        return new RedisKeyName("otherSetting", RedisTypeEnum.OBJECT_TYPE,null);
    }

    @ApiOperation(value = "其他设置的远程接口,用于筛选活动，需求，网币发起活动时是否符合要求")
    @PostMapping("/queryRemote")
    public JsonResult queryRemote() {
        try {
            WzCommonOtherSetResponseDto responseDto=new WzCommonOtherSetResponseDto();
            CommonOtherSet wzCommonOtherSet = otherSetService.query(1,false);
            if (wzCommonOtherSet == null) {
                return JsonResult.fail("等待管理员设置。。。");
            }
            BeanUtils.copyProperties(wzCommonOtherSet,responseDto);
            return JsonResult.success().addResult("result",responseDto);
        }catch (Exception e) {
            logger.error("审核其他信息出错："+e.getMessage(), e);
            return JsonResult.fail("审核其他信息出错");
        }
    }
}
