package com.netx.worth.service;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.wish.WishPublishDto;
import com.netx.worth.enums.WishStatus;
import com.netx.worth.mapper.WishMapper;
import com.netx.worth.model.Wish;

@Service
public class WishService extends ServiceImpl<WishMapper, Wish>{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public List<Wish> getSupportList(List<String> ids,String userId, Page page) {
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("deleted=0 and is_lock={0}",false);
        entityWrapper.in("id",ids);
        entityWrapper.orNew("user_id={0}",userId);
        entityWrapper.orderBy("create_time desc");
        return selectPage(page,entityWrapper).getRecords();
    }

	/**获取用户发布的心愿*/
    public List<Wish> getPublishList(String userId, Page<Wish> page,boolean isPublish) {
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId).orderBy("create_time desc");
        if(!isPublish){
            entityWrapper.and("is_lock={0}",false);
        }
        return selectPage(page, entityWrapper).getRecords();
    }

    
    /**根距userId列表获取心愿分页列表*/
    public List<Wish> getWishByUserIds(List<String> userIds,Page<Wish> page) { 
    	EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("user_id", userIds);
        Page<Wish> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }

    /**根距userId列表获取心愿列表*/
    public List<Wish> getWishByUserId(String userId) { 
    	EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
    	entityWrapper.where("user_id={0} AND deleted = {1}", userId,0);
    	return selectList(entityWrapper);
    }
    
    /**根距userId列表删除心愿列表*/
    public boolean deleteWishByUserId(String userId) { 
    	EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
    	entityWrapper.where("user_id", userId);
    	return delete(entityWrapper);
    }
    
    /**得到重复的心愿列表*/
    public Integer getSameWishList(WishPublishDto wishPublishDto){
        EntityWrapper<Wish> wishWrapper = new EntityWrapper<Wish>();
        wishWrapper.where("user_id={0}", wishPublishDto.getUserId());
        wishWrapper.and("description={0}", wishPublishDto.getDescription());
        wishWrapper.and("title={0}", wishPublishDto.getTitle());
        wishWrapper.and("wish_label={0}", wishPublishDto.getWishLabel());
        return selectCount(wishWrapper);
    }

      /**发布心愿*/
    public Boolean wish(Wish wish) {
        return insertOrUpdate(wish);
    }

    /**取消心愿*/
    public boolean cancel(String id, String userId) {
        Wish wish = new Wish();
        wish.setStatus(WishStatus.CANCEL.status);
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", id).and("user_id={0}", userId);
        return update(wish, entityWrapper);
    }

    /**通过心愿ID和用户ID查询心愿*/
    public Wish getWishUserIdAndWishId(String wishId, String userId) {
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", wishId).and("user_id={0}", userId);
        return selectOne(entityWrapper);
    }

    /**通过心愿ID查询心愿*/
    public Wish getWishbyWishId(String wishId) {
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", wishId);
        return selectOne(entityWrapper);
    }
    
    /**查询未完成的心愿*/
    public List<Wish> getUnComplete(String userId) {
    	//查询该用户发布的心愿中状态为“已发布”、“推荐成功”、“筹集目标达成，即心愿发起成功”心愿。
		List<Integer> wishStatusList = new ArrayList<Integer>();
		wishStatusList.add(WishStatus.PUBLISHED.status);
		wishStatusList.add(WishStatus.REFEREE_SUCCESS.status);
		wishStatusList.add(WishStatus.SUCCESS.status);
		EntityWrapper<Wish> wishWrapper = new EntityWrapper<Wish>();
		wishWrapper.where("user_id={0}", userId);
		wishWrapper.in("status={0}", wishStatusList);
		return selectList(wishWrapper);
    }
    
    /**根据心愿信息过滤*/
    public List<Wish> nearList(String[] labels,Page<Wish> page,Set<String> userInfoSet,String title) {
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("user_id={0}", userInfoSet);
        entityWrapper.like("title={0}", title);
//        if (StringUtils.hasText(userId)) {
//            entityWrapper.ne("user_id={0}", userId);
//        }
        int length = labels.length;
        for (int i=0; i<length; i++) {
            entityWrapper.like("wish_label", labels[i]);
        }
        Page<Wish> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }
    
    /**心愿已完成的列表*/
    public List<Wish> getComplete(String userId) {
    	EntityWrapper<Wish> wishWrapper = new EntityWrapper<>();
    	wishWrapper.where("user_id={0}", userId);
    	wishWrapper.and("status.={0}", WishStatus.COMPLETE.status);
    	return selectList(wishWrapper);
    }

    /* 根据userId获取心愿的总数*/
    public int getWishCountByUserId(String userId){
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        return selectCount ( entityWrapper );
    }

    /* 根据userId获取心愿成功的总数*/
    public int getWishOrderCountByUserId(String userId){
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        entityWrapper.and ("status={0}",WishStatus.COMPLETE.status);
        return selectCount ( entityWrapper );
    }


    /* 根据userId获取最新发布的心愿*/
    public Wish getNewWish(String userId,boolean isPublishUser){
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        if(!isPublishUser){
            entityWrapper.and("is_lock={0}",false);
        }
        entityWrapper.orderBy ( "create_time",false );
        return selectOne( entityWrapper );
    }

    public List<Map<String,Object>> queryWishIncome(Collection<?> userIds){
        EntityWrapper<Wish> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("status={0} and deleted=0", WishStatus.SUCCESS.status);
        entityWrapper.in("user_id",userIds);
        entityWrapper.setSqlSelect("user_id as userId,sum(current_amount) as total");
        entityWrapper.groupBy("userId");
        return selectMaps(entityWrapper);
    }
}
