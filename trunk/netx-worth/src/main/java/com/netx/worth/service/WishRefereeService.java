package com.netx.worth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netx.common.wz.dto.wish.WishWithdrawalDto;
import com.netx.worth.enums.WishHistoryStatus;
import com.netx.worth.model.WishHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.wish.WishRefereeDto;
import com.netx.worth.enums.WishRefereeStatus;
import com.netx.worth.mapper.WishRefereeMapper;
import com.netx.worth.model.WishReferee;
import com.netx.worth.model.WishSupport;

@Service
public class WishRefereeService extends ServiceImpl<WishRefereeMapper, WishReferee>{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	/**创建心愿推荐*/
    public boolean createReferee( String wishId,String... userIds) {
        ArrayList<WishReferee> list = new ArrayList<>();
        Arrays.asList(userIds).forEach(userId -> {
            WishReferee wishReferee = new WishReferee();
            wishReferee.setUserId(userId);
            wishReferee.setWishId(wishId);
            list.add(wishReferee);

        });
        return insertBatch(list);
    }

    public boolean createReferee(String wishId,String userId){
        WishReferee wishReferee = new WishReferee();
        wishReferee.setUserId(userId);
        wishReferee.setWishId(wishId);
        wishReferee.setStatus(WishRefereeStatus.WAITING.status);
        return insert(wishReferee);
    }
    
    /**接受推荐*/
    public boolean refereeAccept(WishRefereeDto wishRefereeDto) {
        WishReferee wishReferee = new WishReferee();
        wishReferee.setStatus(WishRefereeStatus.ACCEPT.status);
        wishReferee.setUpdateUserId(wishRefereeDto.getUserId());
        wishReferee.setDescription(wishRefereeDto.getDescription());
        EntityWrapper<WishReferee> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", wishRefereeDto.getId());
        return update(wishReferee, entityWrapper);

    }
    
    /**拒绝推荐*/
    public boolean refereeRefuse(WishRefereeDto wishRefereeDto) {
        WishReferee wishReferee = new WishReferee();
        wishReferee.setStatus(WishRefereeStatus.REFUSE.status);
        wishReferee.setUpdateUserId(wishRefereeDto.getUserId());
        wishReferee.setDescription(wishRefereeDto.getDescription());
        EntityWrapper<WishReferee> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", wishRefereeDto.getId());
        return update(wishReferee, entityWrapper);
    }


    
    /**获得心愿推荐的列表通过心愿Id或者userId*/
    public List<WishReferee> getRefereeListByWish(String wishId, String userId, Page<WishReferee> page) {
        EntityWrapper<WishReferee> entityWrapper = new EntityWrapper<>();
        if (StringUtils.hasText(wishId)) {
            entityWrapper.where("wish_id={0}", wishId);
        } else {
            entityWrapper.where("user_id={0}", userId).orderBy("wish_id");
        }
        selectPage(page, entityWrapper);
        return page.getRecords();
    }
    
    /**获取心愿推荐通过心愿Id和userId*/
    public List<Map<String,Object>> selectByWishIdAndUserId(String wishId) {
        EntityWrapper<WishReferee> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        return selectMaps(entityWrapper);

    }
    
    /**通过心愿ID获取心愿推荐列表*/
    public List<WishReferee> selectByWishId(String wishId) {
        EntityWrapper<WishReferee> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0} and status in (1,2,3,0)", wishId);
        entityWrapper.orderBy("FIELD(status,1,0,3,2),create_time DESC");
        return selectList(entityWrapper);
    }

    /**通过心愿ID获取心愿推荐列表*/
    public List<WishReferee> selectStasusByWishId(String wishId) {
        EntityWrapper<WishReferee> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        return selectList(entityWrapper);
    }
    
    /**根距userId列表删除心愿推荐列表*/
    public boolean deleteWishSupportByUserId(String userId) { 
    	EntityWrapper<WishReferee> wishRefereeWrapper = new EntityWrapper<WishReferee>();
		wishRefereeWrapper.where("user_id={0}", userId);
    	return delete(wishRefereeWrapper);
    }

    public Map<String,Object> getRefereeDetail(String wishId){
        EntityWrapper<WishReferee> wrapper = new EntityWrapper<>();
        wrapper.where("wish_id={0}",wishId);
        return selectMap(wrapper);
    }

    public int countRefereeByStatus(String wishId,WishRefereeStatus wishRefereeStatus){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("wish_id={0}",wishId);
        if(wishRefereeStatus!=null){
            wrapper.and("status={0}",wishRefereeStatus.status);
        }
        return selectCount(wrapper);
    }

    public WishReferee queryReferee(String wishId,String userId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("wish_id={0} and user_id={1} and deleted=0",wishId,userId);
        return selectOne(wrapper);
    }
}
