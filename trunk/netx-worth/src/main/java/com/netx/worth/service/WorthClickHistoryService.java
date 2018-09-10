package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.enums.WorthTypeEnum;
import com.netx.worth.mapper.WorthClickHistoryMapper;
import com.netx.worth.model.WorthClickHistory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class WorthClickHistoryService extends ServiceImpl<WorthClickHistoryMapper, WorthClickHistory> {

    /**
     * 获取网能点击量
     * @param worthTypeEnum 网能类型
     * @param typeId 网能id
     * @param userId 点击者id，获取网能对应的点击量时，userId传null
     * @return
     */
    public Integer getClickCount(WorthTypeEnum worthTypeEnum,String typeId,String userId){
        Wrapper<WorthClickHistory> wrapper = new EntityWrapper<>();
        wrapper.where("type_name={0} and type_id={1}",worthTypeEnum.getName(),typeId);
        if(StringUtils.isNotBlank(userId)){
            wrapper.and("user_id={0}",userId);
        }
        return selectCount(wrapper);
    }

    public Boolean delectClickHistoryByUserId(String userId){
        Wrapper<WorthClickHistory> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}",userId);
        return delete(wrapper);
    }

    public Boolean delectClickHistory(WorthTypeEnum worthTypeEnum,String typeId){
        Wrapper<WorthClickHistory> wrapper = new EntityWrapper<>();
        wrapper.where("type_name={0} and type_id={1}",worthTypeEnum.getName(),typeId);
        return delete(wrapper);
    }
}
