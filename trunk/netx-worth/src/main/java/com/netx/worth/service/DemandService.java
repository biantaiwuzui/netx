package com.netx.worth.service;

import java.util.Date;
import java.util.List;

import com.netx.utils.cache.CacheKey;
import com.netx.utils.cache.NetxRedisCache;
import com.netx.utils.cache.ServiceCacheImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.wz.dto.demand.DemandPublishDto;
import com.netx.worth.enums.DemandStatus;
import com.netx.worth.mapper.DemandMapper;
import com.netx.worth.model.Demand;

@Service
public class DemandService extends ServiceCacheImpl<DemandMapper, Demand> {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private DemandMapper demandMapper;

	public DemandMapper getDemandMapper() {
		return demandMapper;
	}

	/** 根据userId查询用户发布的需求 */
	@NetxRedisCache
	public List<Demand> getSendList(@CacheKey(key="userId") String userId, @CacheKey(key="page") Page<Demand> page) {
		EntityWrapper<Demand> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		Page<Demand> selectPage = selectPage(page, entityWrapper);
		return selectPage.getRecords();
	}
	/**根据userId查询用户发布的需求**/
//	public List<Demand> (String userId, Page<Demand> page) {
//		EntityWrapper<Demand> entityWrapper = new EntityWrapper<>();
//		entityWrapper.where("user_id={0}", userId);
//		Page<Demand> selectPage = selectPage(page, entityWrapper);
//		return selectPage.getRecords();
//	}
	/**查询相同的需求的条数*/
	public Integer getSameDemand(DemandPublishDto demandPublishDto){
		EntityWrapper<Demand> demandWrapper = new EntityWrapper<Demand>();
		demandWrapper.where("title={0}", demandPublishDto.getTitle());
		demandWrapper.and("user_id={0}", demandPublishDto.getUserId());
		demandWrapper.and("description={0}", demandPublishDto.getDescription());
		demandWrapper.and("address={0}", demandPublishDto.getAddress());
		demandWrapper.and("amount={0}", demandPublishDto.getAmount());
		return selectCount(demandWrapper);
	}
	
	/**发布新的需求*/
	public Demand publish(Demand demand) {
		if(! demand.insert()) {
			return null;
		}
		return demand;
	}
	
	/**根据Id修改需求*/
	public boolean editById(Demand demand) {
		return demand.insertOrUpdate ();
	}
	
	/**发布者取消需求*/
	public boolean cancel(String demandId) {
		Demand demand = new Demand();
		demand.setId(demandId);
		demand.setStatus(DemandStatus.CANCEL.status);
		return updateById(demand);
	}
//	/**发布者取消需求*/
//	public boolean cancel(String demandId) {
//		Demand demand = new Demand();
//		demand.setId(demandId);
//		demand.setUserId(userId);
//		demand.setStatus(DemandStatus.CANCEL.status);
//		return deleteById (demandId);
//	}
	
	/**发布方结束报名*/
	public boolean publishStop(String demandId, String userId) {
		Demand demand = new Demand();
		demand.setStatus(DemandStatus.START.status);
		demand.setId(demandId);
		return updateById(demand);
	}
	
	/**根据userId删除该用户发布的需求*/
	public boolean cleanDemand(String userId){
		EntityWrapper<Demand> demandWrapper = new EntityWrapper<Demand>();
		demandWrapper.where("user_id={0}", userId);
		return delete(demandWrapper);
	}
	
	/**查询已发布的次数*/
	@NetxRedisCache
	public int getPublishCount(@CacheKey(key="userId") String userId) {
		EntityWrapper<Demand> demandWrapper = new EntityWrapper<Demand>();
		demandWrapper.where("status={0}", DemandStatus.PUBLISHED.status);
		demandWrapper.gt("end_at", new Date().getTime());
		demandWrapper.and("user_id={0}", userId);
		return selectCount(demandWrapper);
	}
	/**通过userId和Id查询是否存在**/
	@NetxRedisCache
	public int selectByIdAndUserId(@CacheKey(key="userId") String userId,@CacheKey(key="demandId") String Id) {
		EntityWrapper<Demand> demandWrapper = new EntityWrapper<Demand>();
		demandWrapper.where("user_id={0}",userId );
		demandWrapper.and("id={0}", Id);
		return selectCount(demandWrapper);
	}

	@NetxRedisCache
	public Demand queryByIdAndUserId(@CacheKey(key="userId") String userId,@CacheKey(key="demandId") String Id) {
		EntityWrapper<Demand> demandWrapper = new EntityWrapper<Demand>();
		demandWrapper.where("user_id={0}",userId );
		demandWrapper.and("id={0}", Id);
		return selectOne(demandWrapper);
	}

	/**通过userId查询单个用户信息**/
	@NetxRedisCache
	public List<Demand> selectListByUserId(@CacheKey(key="userId") String userId, @CacheKey(key="page") Page page) {

		EntityWrapper<Demand> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		return selectPage (page, entityWrapper ).getRecords ();
	}

	/**通过userId查询单个用户信息
	 * create by FRWIN**/
	@NetxRedisCache
	public List<Demand> selectListByUserIdTwo(@CacheKey(key="userId") String userId,@CacheKey(key="page") Page page) {

		EntityWrapper<Demand> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		entityWrapper.orderBy ( "create_time",false );//降序
		return selectPage (page, entityWrapper ).getRecords ();
	}

	/**根据userId获取需求的总数*/
	@NetxRedisCache
	public int getDemandCountByUserId(@CacheKey(key="userId") String userId) {
		EntityWrapper<Demand> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		return selectCount(entityWrapper);
	}

	/**根据userId获取最新发布的需求*/
	@NetxRedisCache
	public Demand getNewDemand(@CacheKey(key="userId") String userId){
		EntityWrapper<Demand> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		entityWrapper.orderBy ( "create_time",false );
		return selectOne( entityWrapper );
	}

	
}
