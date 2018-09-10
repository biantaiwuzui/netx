package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.common.enums.CostSettingEnum;
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
import java.util.List;

/**
 * Create by wongloong on 17-9-3
 */
@Api(value = "费用设置", description = "费用设置")
@RestController
@RequestMapping("/costSetting")
public class CostSettingController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(CostSettingController.class);
    @Autowired
    private CostAction costAction;

    private RedisCache redisCache;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @ApiOperation(value = "保存,普通管理员可以操作，存在未审核的不能添加")
    @PostMapping("/save")
    public JsonResult save(@RequestBody WzCommonCostSettingAddDto commonCostSetting) {
        try {
            return super.getResult(costAction.saveCostSetting(commonCostSetting),"保存失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("保存异常");
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

    private void clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
    }

    private void updateWzCommonCostSettingRedis(CommonCostSetting setting){
        clientRedis();
        CostSettingResponseDto dto = VoPoConverter.copyProperties(setting,CostSettingResponseDto.class);
        RedisKeyName redisKeyName = new RedisKeyName("costSetting", RedisTypeEnum.OBJECT_TYPE,null);
        redisCache.put(redisKeyName.getCommonKey(),dto);
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
            return JsonResult.success().addResult("data", costAction.query());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "查询特定的费用设定",
            notes = "SHARED_FEE：分成<br>"+
                    "WITHDRAW_FEE：提现手续费<br>"+
                    "SHOP_MANAGER_FEE：注册商家管理费<br>"+
                    "SHOP_MANAGER_FEE_LIMIT_DATE：注册商家管理费有效期<br>"+
                    "SHOP_CATEGORY_FEE：商品一级类目收费<br>"+
                    "SHOP_TAGS_FEE：商家二级类目收费<br>"+
                    "CREDIT_ISSUE_FEE：网信发行费<br>"+
                    "CREDIT_FUNDS_INTEREST：网信竞购系数<br>"+
                    "CREDIT_SUBSCRIBE_FEE：网信报名认购费用<br>"+
                    "CREDIT_INST：网信资金利息<br>"+
                    "PIC_AND_VOICE_PUBLISH_DEPOSIT：图文、音视的发布押金<br>"+
                    "CLICK_FEE：点击费用<br>"+
                    "VIOLATION_CLICK_FEE： 违规图文、音视的点击费用<br>"+
                    "WISH_CAPITAL_MANAGE_FEE：心愿资金管理费<br>"+
                    "SALER_SHARED_FEE：销售收入分成")
    @PostMapping("/queryByType")
    public JsonResult queryByType(@RequestBody List<CostSettingEnum> list) {
        try {
            if (list==null || list.size()<0){
                return JsonResult.fail("查询设定不能为空");
            }
            return JsonResult.successJsonResult(costAction.query(list));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "查询未审核的费用设定",notes = "返回值key:result 类型:CommonCostSetting")
    @PostMapping("/queryUnaudited")
    public JsonResult queryUnaudited() {
        try {
            return JsonResult.success().addResult("data", costAction.queryUnaudited());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    /*@ApiOperation("远程跨模块查询费用设定")
    @PostMapping("/feignQuery")
    public Result<CostSettingResponseDto> feignQuery() {
        try {
            Result<CostSettingResponseDto> result = new Result<>();
            EntityWrapper wrapper=new EntityWrapper<>();
            wrapper.where("state={0}",1).orderBy("disposeTime",false);
            List<WzCommonCostSetting> list = costAction.selectList(wrapper);
            if (list.isEmpty()&&list.size()==0) {
                WzCommonCostSetting wzCommonCostSetting = costAction.initWzCommonCostSetting();
                costAction.insert(wzCommonCostSetting);
                list.add(wzCommonCostSetting);
            }
            //result.setObject(list.get(0));
            CostSettingResponseDto responseDto= VoPoConverter.copyProperties(list.get(0),CostSettingResponseDto.class);
            result.setObject(responseDto);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.newException(e);
        }
    }*/
}
