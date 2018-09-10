package com.netx.worth.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.netx.utils.cache.CacheKey;
import com.netx.utils.cache.NetxRedisCache;
import com.netx.utils.cache.ServiceCacheImpl;
import com.netx.worth.model.DemandRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.wz.dto.demand.DemandOrderConfirmDto;
import com.netx.utils.money.Money;
import com.netx.worth.enums.DemandOrderStatus;
import com.netx.worth.enums.DemandStatus;
import com.netx.worth.mapper.DemandOrderMapper;
import com.netx.worth.model.Demand;
import com.netx.worth.model.DemandOrder;

@Service
public class DemandOrderService extends ServiceCacheImpl<DemandOrderMapper, DemandOrder> {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private DemandRegisterService demandRegisterService;

	/** 确认入选人 */
	@Transactional
	public List<DemandOrder> publishAccept(String demandId, String userId, List<String> registerIds) throws Exception {
		boolean success = false;
		List<DemandOrder> list = new ArrayList<>();
		if (registerIds != null && registerIds.size() > 0) {
			for (String registerId : registerIds) {
				DemandOrder demandOrder = new DemandOrder();
				demandOrder.setDemandId(demandId);
				demandOrder.setUserId(userId);
				demandOrder.setStatus(DemandOrderStatus.ACCEPT.status);
				//插入数据到数据库demandOrder表
				success = demandOrder.insert();
				if (!success) {
					return null;
				}
				//更改register表的状态
				success = demandRegisterService.success(demandOrder.getId(), registerId);
				if (!success) {
					return null;
				} else {
					list.add(demandOrder);
				}
			}
		}
		return list;
	}

	/** 支付给商家后回调 */
	@Transactional
	public boolean registerPay(String demandOrderId, String userId, BigDecimal orderPrice) {
		DemandOrder demandOrder = selectById(demandOrderId);
		if(demandOrder == null){
			return false;
		}else{
			demandOrder.setConfirmPay(true);
			demandOrder.setOrderPrice(new Money(orderPrice).getCent());
			return demandOrder.updateById();
		}
	}

	/** 修改需求单状态为已关闭 */
	public boolean setDemandStop(String demandOrderId) {
		DemandOrder demandOrder = selectById(demandOrderId);
		Demand demand = new Demand();
		demand.setStatus(DemandStatus.STOP.status);
		demand.setId(demandOrder.getDemandId());
		boolean success = demand.insertOrUpdate();
		return success;
	}

	@NetxRedisCache
	public List<Map<String, Object>> getDemandOrderListBydemandId(@CacheKey(key="demandId") String demandId, Page<DemandOrder> page) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		Page<Map<String, Object>> selectPage = selectMapsPage(page, entityWrapper);
		List<Map<String, Object>> orderMapList = selectPage.getRecords();
		return orderMapList;
	}

	@NetxRedisCache
	/** 根据userId和demandId查询orderId */
	public DemandOrder getOrderByUserIdAndDemandId(@CacheKey(key="userId") String userId, @CacheKey(key="demandId") String demandId) {
		EntityWrapper<DemandOrder> demandOrderWrapper = new EntityWrapper<DemandOrder>();
		demandOrderWrapper.where("user_id={0}", userId);
		demandOrderWrapper.and ("demand_id={0}",demandId);
		return selectOne(demandOrderWrapper);
	}

	@NetxRedisCache
	/** 根据userId查询需求单列表 */
	public List<DemandOrder> getOrderListByUserId(@CacheKey(key="userId") String userId) {
		EntityWrapper<DemandOrder> demandOrderWrapper = new EntityWrapper<DemandOrder>();
		demandOrderWrapper.where("user_id={0}", userId);
		return selectList(demandOrderWrapper);
	}

	/** 根据userId删除该用户需求单 */
	public boolean cleanDemandOrder(String userId) {
		EntityWrapper<DemandOrder> demandWrapper = new EntityWrapper<DemandOrder>();
		demandWrapper.where("user_id={0}", userId);
		return delete(demandWrapper);
	}

	/** 确认细节 */
	public boolean publishConfirmOrderDetail(List<String> demandOrderIds, DemandOrderConfirmDto demandOrderConfirmDto) {
		DemandOrder demandOrder = new DemandOrder();
		demandOrder.setStartAt(new Date(demandOrderConfirmDto.getStartAt()));
		demandOrder.setEndAt(new Date(demandOrderConfirmDto.getEndAt()));
		demandOrder.setUnit(demandOrderConfirmDto.getUnit());
		demandOrder.setAddress(demandOrderConfirmDto.getAddress());
		demandOrder.setLon(demandOrderConfirmDto.getLon());
		demandOrder.setLat(demandOrderConfirmDto.getLat());
		demandOrder.setOrderIds(demandOrderConfirmDto.getOrderIds());
		demandOrder.setOrderPrice(new Money(demandOrderConfirmDto.getOrderPrice()).getCent());
		demandOrder.setWage(new Money(demandOrderConfirmDto.getWage()).getCent());
		demandOrder.setEachWage(demandOrderConfirmDto.getEachWage());
		demandOrder.setBail(new Money(demandOrderConfirmDto.getBail()).getCent());
		demandOrder.setPay(demandOrderConfirmDto.getPay());
		demandOrder.setStatus(DemandOrderStatus.CONFIRM.status);
		EntityWrapper<DemandOrder> demandOrderWrapper = new EntityWrapper<>();
		demandOrderWrapper.in("id", demandOrderIds);
		return update(demandOrder, demandOrderWrapper);
	}

	@NetxRedisCache
	/** 根据demandId获得没确认细节的Order列表 */
	public List<DemandOrder> noConfirmOrderList(@CacheKey(key="demandId") String demandId) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId).and("status={0}", DemandOrderStatus.ACCEPT.status);
		return selectList(entityWrapper);
	}

	@NetxRedisCache
	/** 根据demandId获取Order单列表 */
	public List<DemandOrder> orderList(@CacheKey(key="demandId") String demandId) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<DemandOrder>();
		entityWrapper.where("demand_id={0}", new Object[] { demandId }).orderBy("create_time desc");
		return selectList(entityWrapper);
	}

	/** 根据demandId获取需求成功Order单列表 */
	@NetxRedisCache
	public List<DemandOrder> getSuccessListByDemandId(@CacheKey(key="demandId") String demandId, @CacheKey(key="demandOrderStatus") DemandOrderStatus status) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		if(status!=null){
			entityWrapper.and("status={0}",status.status);
		}
		return selectList(entityWrapper);
	}

	/** 根据demandId获取需求成功Order单列表 */
	@NetxRedisCache
	public List<DemandOrder> getWaitListByDemandId(@CacheKey(key="demandId") String demandId) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		String[] arr = {DemandOrderStatus.ACCEPT.status+"",DemandOrderStatus.CONFIRM.status+"",DemandOrderStatus.SUCCESS.status+"",DemandOrderStatus.START.status+""};
		return selectList(entityWrapper);
	}

	/** 根据demandId和userId判断是否是用户发布的 */
	@NetxRedisCache
	public int getDemandOrderCountByIdAndUserId(@CacheKey(key="demandId") String Id, @CacheKey(key="userId") String UserId) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("id={0}", Id).and("user_id={0}", UserId);
		return selectCount (entityWrapper);
	}
	/** 根据demandId和userId判断是否重复确认 */
	@NetxRedisCache
	public int getDemandOrderCountByDemandIdAndUserId(@CacheKey(key="demandId") String demandId,@CacheKey(key="userId") String UserId) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId).and("user_id={0}", UserId);
		return selectCount (entityWrapper);
	}

	/** 根据demandId判断已经入选了几个人 */
	@NetxRedisCache
	public int getDemandOrderCountByDemandId(@CacheKey(key="demandId") String demandId) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		return selectCount (entityWrapper);
	}

	public int getDemandOrderCountByDemandId(String demandId,Integer... orderStatus) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		int length = orderStatus.length;
		if(length>0){
			if(length==1){
				entityWrapper.and("status={0}",orderStatus);
			}else{
				entityWrapper.in("status",orderStatus);
			}
		}
		return selectCount (entityWrapper);
	}

	public boolean updateDemandOrderByDemandId(DemandOrder demandOrder){
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandOrder.getDemandId());
		return update(demandOrder,entityWrapper);
	}

	/**需求启动 更改order表状态*/
	public boolean start(DemandOrder order) {

		order.setStatus( DemandOrderStatus.START.status);

		return updateById ( order );
	}

	/**根据userId获取需求成功的总数*/
	@NetxRedisCache
	public int getDemandOrderCountByUserId(@CacheKey(key="userId") String userId){
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		entityWrapper.and ("status={0}", DemandOrderStatus.AllEND);
		return selectCount ( entityWrapper );
	}
	/**根据demandId获取入选者需求状态为5的总数*/
	@NetxRedisCache
	public int getDemandOrderStartCountByDemandId(@CacheKey(key="demandId") String demandId){
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		entityWrapper.and ("status=5");
		return selectCount ( entityWrapper );
	}
    /**根据demandId获取入选者需求启动完成的总数*/
    @NetxRedisCache
    public int getDemandOrderEndCountByDemandId(@CacheKey(key="demandId") String demandId){
        EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("demand_id={0}", demandId);
        entityWrapper.and ("status={0}", DemandOrderStatus.AllEND.status);
        return selectCount ( entityWrapper );
    }

	/**根据状态获取入选者人数*/
	public int getDemandOrderCountByDemandIdAndStatus(String demandId,Integer... orderStatus) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		int length = orderStatus.length;
		if(length>0){
			if(length==1){
				entityWrapper.and("status={0}",orderStatus);
			}else{
				entityWrapper.in("status",orderStatus);
			}
		}
		entityWrapper.orderBy ( "create_time",true );
		return selectCount(entityWrapper);
	}

    /**根据状态获取入选者列表*/
	public List<DemandOrder> getDemandOrderListByDemandIdAndStatus(String demandId,Integer... orderStatus) {
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		int length = orderStatus.length;
		if(length>0){
			if(length==1){
				entityWrapper.and("status={0}",orderStatus);
			}else{
				entityWrapper.in("status",orderStatus);
			}
		}
		entityWrapper.orderBy ( "create_time",true );
		return selectList(entityWrapper);
	}

	/**根据需求id 和 状态获取改状态所有报酬总数*/
	public long getDemandWagesByDemandIdAndStatus(String demandId,Integer... orderStatus){
		EntityWrapper<DemandOrder> entityWrapper = new EntityWrapper<>();
		entityWrapper.setSqlSelect ( "sum(wage)" );
		entityWrapper.where("demand_id={0}", demandId);
		int length = orderStatus.length;
		if(length>0){
			if(length==1){
				entityWrapper.and("status={0}",orderStatus);
			}else{
				entityWrapper.in("status",orderStatus);
			}
		}
		entityWrapper.orderBy ( "create_time",true );
		BigDecimal total = (BigDecimal)selectObj(entityWrapper);
		return total==null?0:total.longValue();
	}
}
