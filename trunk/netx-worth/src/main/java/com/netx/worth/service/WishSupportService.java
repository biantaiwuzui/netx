package com.netx.worth.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.utils.money.Money;
import com.netx.worth.mapper.WishSupportMapper;
import com.netx.worth.model.Wish;
import com.netx.worth.model.WishSupport;

@Service
public class WishSupportService extends ServiceImpl<WishSupportMapper, WishSupport>{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private WishService wishService;
	
	/**通过userId获取用户支持的心愿列表*/
    public List<WishSupport> getUserSupportList(String userId) {
        EntityWrapper<WishSupport> entityWrapper = new EntityWrapper<>();
        entityWrapper.setSqlSelect("wish_id AS wishId, user_id as userId, sum(amount) as amount ")
                .where("user_id={0}", userId)
                .groupBy("wish_id");
        return selectList(entityWrapper);
    }
    
    /**获取该心愿支持的数量*/
    public int getSupportCountByWish(String wishId) {
        EntityWrapper<WishSupport> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        selectCount(entityWrapper);
        return selectCount(entityWrapper);
    }
    
    /**创建心愿支持*/
    @Transactional
    public boolean create(WishSupport wishSupport) {
        return insert(wishSupport);
    }
    
    /**获取心愿支持通过心愿ID和userId*/
    public List<Map<String,Object>> selectWishIdAndUserId(String wishId) {
        EntityWrapper<WishSupport> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        return selectMaps(entityWrapper);
    }
    
    /**通过心愿Id获取心愿支持列表*/
    public List<WishSupport> getSupportListByWish(String wishId) {
        EntityWrapper<WishSupport> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId).orderBy("amount desc, create_time asc");
        return selectList(entityWrapper);
    }
    
    /**根距userId列表删除心愿支持列表*/
    public boolean deleteWishSupportByUserId(String userId) { 
    	EntityWrapper<WishSupport> wishSupportWrapper = new EntityWrapper<WishSupport>();
		wishSupportWrapper.where("user_id={0}", userId);
    	return delete(wishSupportWrapper);
    }
    
    /**获取已经支付的心愿支持列表*/
    public List<WishSupport> getisPaySupport(String wishId) { 
		EntityWrapper<WishSupport> wishSupportWrapper = new EntityWrapper<>();
		wishSupportWrapper.where("wish_id={0}", wishId);
		wishSupportWrapper.and("is_pay={0}",1);
		return selectList(wishSupportWrapper);
    }
    
    /**上个月获得支持的列表*/
    public List<WishSupport> getSupportMonth(List<String> wishIds,long firstDay,long lastDay) {
    	EntityWrapper<WishSupport> wishSupportWrapper = new EntityWrapper<>();
    	wishSupportWrapper.in("wish_id", wishIds);
    	wishSupportWrapper.and("is_pay={0}", 1);
    	wishSupportWrapper.between("create_time", firstDay, lastDay);
    	return selectList(wishSupportWrapper);
    }

    /**通过wishId获取用户支持的心愿列表*/
    public List<WishSupport> getSupportListByWishId(String id, Page<WishSupport> page) {
        EntityWrapper<WishSupport> entityWrapper = new EntityWrapper<>();
        entityWrapper
                .setSqlSelect("wish_id as wishId, user_id as userId, sum(amount) as amount")
                .where("wish_id={0}", id).groupBy("user_id");
        selectPage(page, entityWrapper);
        return page.getRecords();
    }

    /* 已资助人数*/
    public int getWishSupportNumByWishId(String WishId ){
        EntityWrapper<WishSupport> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", WishId);
        return  selectCount ( entityWrapper );
    }

    public List<Map<String,Object>> queryConsume(Collection<?> userIds){
        EntityWrapper<WishSupport> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("is_pay={0} and deleted=0",true);
        entityWrapper.in("user_id",userIds);
        entityWrapper.setSqlSelect("user_id as userId,sum(amount) as total");
        entityWrapper.groupBy("userId");
        return selectMaps(entityWrapper);
    }
}
