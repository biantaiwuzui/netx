package com.netx.worth.biz.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netx.worth.service.WorthServiceprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.worth.model.ActiveLog;
import com.netx.worth.service.ActiveLogService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-11-28
 */
@Service
public class ActiveLogAction{
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    WorthServiceprovider worthServiceprovider;

    public boolean create(String userId, String relatableType, String relatableId, String description, BigDecimal lon, BigDecimal lat) {
        ActiveLog activeLog = new ActiveLog();
        activeLog.setUserId(userId);
        activeLog.setRelatableType(relatableType);
        activeLog.setRelatableId(relatableId);
        activeLog.setDescription(description);
        activeLog.setLon(lon);
        activeLog.setLat(lat);
        activeLog.setCreateUserId(userId);
        activeLog.setCreateUserId(userId);
        return worthServiceprovider.getActiveLogService().insert(activeLog);
    }
    
    public Map<String, Object> nearList(String userId, BigDecimal lon, BigDecimal lat, Double length, Page<ActiveLog> page) {
        Map<String, Object> map = new HashMap<>();
        List<ActiveLog> list = worthServiceprovider.getActiveLogService().getNearList(userId, lon, lat, length, page);
        //TODO
//        map.put("list", DistrictUtil.getDistrictVoList(lat, lon, list));
        return map;
    }
}
