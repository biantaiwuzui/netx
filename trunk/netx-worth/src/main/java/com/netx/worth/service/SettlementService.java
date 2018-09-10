package com.netx.worth.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.SettlementMapper;
import com.netx.worth.model.Settlement;

@Service
public class SettlementService extends ServiceImpl<SettlementMapper, Settlement> {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
    public Settlement selectNotCanByTypeAndId(String relatableType, String relatableId) {
        EntityWrapper<Settlement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("relatable_type={0}", relatableType).and("relatable_id={0}", relatableId).and("is_can={0}", false);
        return selectOne(entityWrapper);
    }
    
    public List<Settlement> getAvailableSettlement() {
        Page<Settlement> page = new Page<>(1, 10);
        EntityWrapper<Settlement> entityWrapper = new EntityWrapper<>();
        //entityWrapper.where("isCan={0}", true).and("isFinish={0}", false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        entityWrapper.where("is_finish={0}", false).and("expired_at<={0}", sdf.format(new Date()));
        selectPage(page, entityWrapper);
        return page.getRecords();
    }
    
    public Settlement getInit(String relatableType, String relatableId) {
        EntityWrapper<Settlement> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("relatable_type={0}", relatableType).and("relatable_id={0}", relatableId).and("is_can={0}", false);
        return selectOne(entityWrapper);
    }
    
    public List<Settlement> getSettlementListByTypeAndId(String relatableType,List<String> ids) {
    	EntityWrapper<Settlement> settlementWrapper = new EntityWrapper<Settlement>();
    	settlementWrapper.where("relatable_type={0}", relatableType);
    	settlementWrapper.in("relatable_id",ids);
    	return selectList(settlementWrapper);
    }
    
    public boolean deleteSettlementListByTypeAndId(String relatableType,List<String> ids) {
    	EntityWrapper<Settlement> settlementWrapper = new EntityWrapper<Settlement>();
    	settlementWrapper.where("relatable_type={0}", relatableType);
    	settlementWrapper.in("relatable_id",ids);
    	return delete(settlementWrapper);
    }
}
