package com.netx.worth.biz.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.WorthTypeEnum;
import com.netx.worth.model.ActiveLog;
import com.netx.worth.model.WorthClickHistory;
import com.netx.worth.service.WorthServiceprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-4-24
 */
@Service
public class WorthClickHistoryAction {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private WorthServiceprovider worthServiceprovider;

    /**
     * 添加网能点击记录
     * @param worthTypeEnum 网能类型
     * @param userId 点击者id
     * @param typeId 网能id
     */
    public void create(WorthTypeEnum worthTypeEnum,String userId,String typeId){
        Integer count = worthServiceprovider.getWorthClickHistoryService().getClickCount(worthTypeEnum, typeId, userId);
        if(count==null || count==0){
            WorthClickHistory worthClickHistory = new WorthClickHistory();
            worthClickHistory.setCreateUserId(userId);
            worthClickHistory.setTypeId(typeId);
            worthClickHistory.setTypeName(worthTypeEnum.getName());
            worthClickHistory.setUserId(userId);
            worthServiceprovider.getWorthClickHistoryService().insert(worthClickHistory);
        }
    }

    /**
     * 获取网能点击量
     * @param worthTypeEnum 网能类型
     * @param typeId 网能id
     * @return
     */
    public Integer getClickCount(WorthTypeEnum worthTypeEnum,String typeId){
        Integer count = worthServiceprovider.getWorthClickHistoryService().getClickCount(worthTypeEnum,typeId,null);
        return count==null?0:count;
    }
}
