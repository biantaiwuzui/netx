package com.netx.ucenter.biz.common;

import com.netx.common.common.enums.CostSettingEnum;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.CostSettingResponseDto;
import com.netx.common.vo.common.WzCommonCostSettingAddDto;
import com.netx.ucenter.model.common.CommonCostSetting;
import com.netx.ucenter.service.common.CostService;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-9-3
 */
@Service
public class CostAction{

    @Autowired
    private CostService costService;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private Logger logger = LoggerFactory.getLogger(CostAction.class);

    private RedisCache clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    public CostSettingResponseDto query() {
        clientRedis();
        RedisKeyName redisKeyName = new RedisKeyName("costSetting", RedisTypeEnum.OBJECT_TYPE,null);
        logger.info("通过redis获取费用设置");
        CostSettingResponseDto dto = (CostSettingResponseDto)redisCache.get(redisKeyName.getCommonKey());
        if(dto==null){
            logger.info("通过数据库获取费用设置");
            CommonCostSetting commonCostSetting = costService.selectNewlyByState(1);
            if(commonCostSetting==null){
                commonCostSetting = initWzCommonCostSetting();
                costService.insert(commonCostSetting);
            }
            dto = VoPoConverter.copyProperties(commonCostSetting,CostSettingResponseDto.class);
            redisCache.put(redisKeyName.getCommonKey(),dto);
        }
        return dto;
    }

    public Map<String,Object> query(List<CostSettingEnum> costSettingEnums) {
        if(costSettingEnums==null || costSettingEnums.size()<1){
            return new HashMap<>();
        }
        String sqlField = costSettingEnums.get(0).getValue();
        Integer len = costSettingEnums.size();
        for(int i=1;i<len;i++){
            sqlField+=","+costSettingEnums.get(i).getValue();
        }
        Map<String,Object> map = costService.selectNewlyByState(sqlField,1);
        if(map==null || map.size()<1){
            CommonCostSetting wzCommonCostSetting = initWzCommonCostSetting();
            costService.insert(wzCommonCostSetting);
            map = new HashMap<>();
        }
        Map<String,Object> result = new HashMap<>();
        for(CostSettingEnum costSettingEnum : costSettingEnums){
            if(map.get(costSettingEnum.getValue())==null){
                result.put(costSettingEnum.name(),costSettingEnum.getCost());
            }else{
                result.put(costSettingEnum.name(),map.get(costSettingEnum.getValue()));
            }
        }
        return result;
    }

    public CommonCostSetting queryUnaudited() {
        List<CommonCostSetting> list = costService.selectByState(0);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**lcx*/
    public List<CommonCostSetting> queryListforBoss() {
        List<CommonCostSetting> list = costService.queryListforBoss();
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    public CommonCostSetting initWzCommonCostSetting() {
        CommonCostSetting wzCommonCostSetting = new CommonCostSetting();
        wzCommonCostSetting.setSharedFee(BigDecimal.ZERO);
        wzCommonCostSetting.setWithdrawFee(BigDecimal.ZERO);
        wzCommonCostSetting.setShopManagerFee(BigDecimal.ZERO);
        wzCommonCostSetting.setCreditIssueFee(BigDecimal.ZERO);
        wzCommonCostSetting.setCreditFundsInterest(BigDecimal.ZERO);
        wzCommonCostSetting.setClickFee(BigDecimal.ZERO);
        wzCommonCostSetting.setWishCapitalManageFee(BigDecimal.ZERO);
        wzCommonCostSetting.setSalerSharedFee(BigDecimal.ZERO);
        wzCommonCostSetting.setShopManagerFeeLimitDate(0);
        wzCommonCostSetting.setCreditInst(BigDecimal.ZERO);
        wzCommonCostSetting.setCreditSubscribeFee(BigDecimal.ZERO);
        wzCommonCostSetting.setViolationClickFee(BigDecimal.ZERO);
        wzCommonCostSetting.setPicAndVoicePublishDeposit(BigDecimal.ZERO);
        wzCommonCostSetting.setState(0);
        wzCommonCostSetting.setCreateTime(new Date());
        wzCommonCostSetting.setCreateUserId("0");
        return wzCommonCostSetting;
    }

    public Boolean saveCostSetting(WzCommonCostSettingAddDto commonCostSetting) {
        CommonCostSetting data = new CommonCostSetting();
        BeanUtils.copyProperties(commonCostSetting, data);
        data.setState(0);
        List<CommonCostSetting> list = costService.selectByState(0);
        if(list!=null && list.size()>0){
            return  false;
        }else{
            if(costService.insertOrUpdate(data)){
                return  true;
            }else{
                return false;
            }
        }

    }

}
