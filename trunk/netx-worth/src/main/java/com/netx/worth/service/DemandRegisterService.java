package com.netx.worth.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netx.worth.enums.DemandOrderStatus;
import com.netx.worth.model.DemandOrder;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.demand.DemandRegisterDto;
import com.netx.common.wz.util.VerificationCodeUtil;
import com.netx.utils.money.Money;
import com.netx.worth.enums.DemandRegisterStatus;
import com.netx.worth.mapper.DemandRegisterMapper;
import com.netx.worth.model.DemandRegister;
@Service
public class DemandRegisterService extends ServiceImpl<DemandRegisterMapper, DemandRegister> {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private DemandRegisterMapper demandRegisterMapper;

	public DemandRegisterMapper getDemandRegisterMapper() {
		return demandRegisterMapper;
	}

	/**根据demandID和userID查询申请单列表*/
	public List<DemandRegister> getDemandRegByUserIdAndDemandId(String demandId,String userId,Page<DemandRegister> page){
		EntityWrapper<DemandRegister> entityWapper = new EntityWrapper<>();
		entityWapper.where("user_id={0}", userId);
		entityWapper.and("demand_id={0}", demandId);
		Page<DemandRegister> selectPage = selectPage(page, entityWapper);
		return selectPage.getRecords();
	}
	
	/**根据userID查询申请单列表*/
	public List<DemandRegister> getDemandRegByUserId(String userId,Page<DemandRegister> page){
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		Page<DemandRegister> selectPage = selectPage(page, entityWrapper);
		return selectPage.getRecords();
	}
	
	/**根据demandId查询申请单列表*/
	public List<DemandRegister> getDemandRegByDemandId(String demanId,Page<DemandRegister> page){
		EntityWrapper<DemandRegister> entityWapper = new EntityWrapper<>();
		entityWapper.where("demand_id={0}", demanId);
		entityWapper.and("status={0}", DemandRegisterStatus.REGISTERED.status);
		Page<DemandRegister> selectPage = selectPage(page, entityWapper);
		return selectPage.getRecords();
	}

	public List<DemandRegister> getDemandRegisterListByDemandIdAndStatus(String demandId, Integer... status){
		EntityWrapper<DemandRegister> entityWapper = new EntityWrapper<>();
		entityWapper.where("demand_id={0}", demandId);
		if(status.length>0){
			entityWapper.in("status", status);
		}
		return selectList ( entityWapper );
	}

	public List<DemandRegister> getDemandRegisterListByNoStatus(String demandId, Integer status){
		EntityWrapper<DemandRegister> entityWapper = new EntityWrapper<>();
		entityWapper.where("demand_id={0}", demandId);
		entityWapper.and("status!={0}", status);
		entityWapper.orderBy("status");
		return selectList ( entityWapper );
	}

	/**根据demandId查询申请人的总数  在DemandRegister表*/
	public Integer getRegCount(String demandId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		return selectCount(entityWrapper);
	}
	
	/**申请需求*/
	public boolean register(DemandRegister demandRegister) {
		//id为空 自动填充
		demandRegister.setId ( null );
		return insert(demandRegister);
	}
	
	/**报名方修改需求*/
	public boolean registerEdit(DemandRegister demandRegister) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("id={0}", demandRegister.getId());
		return update(demandRegister, entityWrapper);
	}

	/**申请者取消需求*/
	public boolean registerCancel(String id) {
		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setId(id);
		demandRegister.setStatus(DemandRegisterStatus.CANCEL.status);
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("id={0}", id);
		return update(demandRegister, entityWrapper);
	}
	
	/**设置为已入选*/
	public boolean success(String demandOrderId, String registerId) {
		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setStatus(DemandRegisterStatus.SUCCESS.status);
		demandRegister.setDemandOrderId(demandOrderId);
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("id={0}", registerId);
		return update(demandRegister, entityWrapper);
	}

	/**启动需求  修改register状态*/
	public boolean start(String demandRegisterId) {
		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setStatus(DemandRegisterStatus.START.status);
		demandRegister.setId(demandRegisterId);
		return updateById(demandRegister);
	}
	
	/**付款成功后，申请表状态改为已结束*/
	public boolean registerClose(String demandOrderId) {
		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setStatus(DemandRegisterStatus.CLOSE.status);
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<DemandRegister>();
		entityWrapper.where("demand_order_id={0}",demandOrderId);
		return update(demandRegister, entityWrapper);
	}
	
	/**得到成功的申请表列表*/
	public List<DemandRegister> getSuccessRegListByDemandOrderId(String demandOrderId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_order_id={0}", demandOrderId).and("status={0}", DemandRegisterStatus.SUCCESS.status);
		return selectList(entityWrapper);
	}

	public List<String> getSuccessUserIdByDemandOrderId(String demandOrderId) {
		DemandRegister demandRegister = new DemandRegister();
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_order_id={0}", demandOrderId).and("status={0}", DemandRegisterStatus.SUCCESS.status);
		return (List<String>)(List)selectObjs(entityWrapper);
	}

	/**得到申请表者列表*/
	public List<DemandRegister> getListByDemandId(String demandId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		return selectList(entityWrapper);
	}

	/**根据demandOrderId获得申请单*/
	public DemandRegister getDemandRegByOrderId(String demandOrderId) {
		EntityWrapper<DemandRegister> wrapper = new EntityWrapper<>();//获取入选者Id
		wrapper.where("demand_order_id={0}", demandOrderId);
		return selectOne(wrapper);
	}
	
	/**根据demandId列表获得申请表的列表*/
	public List<DemandRegister> getRegList(List<String> demandIds) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.in(demandIds != null && demandIds.size() > 0, "demand_Id", demandIds);
		return selectList(entityWrapper);
	}
	
	/**根据需求单id查询申请人的userId*/
	public String getUserIdByDemandOrderId(String demandOrderId) {
		EntityWrapper<DemandRegister> registerWrapper = new EntityWrapper<DemandRegister>();
		registerWrapper.where("demand_order_id={0}", demandOrderId);
		DemandRegister demandRegister = selectOne(registerWrapper);
		return demandRegister.getUserId();
	}
	
	/**根据userId删除该用户申请的需求*/
	public boolean cleanDemandRegister(String userId){
		EntityWrapper<DemandRegister> demandWrapper = new EntityWrapper<DemandRegister>();
		demandWrapper.where("user_id={0}", userId);
		return delete(demandWrapper);
	}
	
	/**根据demandId获取已入选的申请单列表*/
	public int getRegSuccessCount(String demandId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId);
		Integer[] status = new Integer[]{DemandRegisterStatus.SUCCESS.status,DemandRegisterStatus.START.status,DemandRegisterStatus.CLOSE.status};
		entityWrapper.in("status",status);
		return selectCount(entityWrapper);
	}
	
	/**根据demandId和userId获取申请单*/
	public DemandRegister getRegisterByDemandIdAndUserId(String demandId, String userId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId).and("user_id={0}", userId);
		return selectOne(entityWrapper);
	}

	/**根据Id和userId判断是否存在此数据*/
	public int IsExitsRegisterByIdAndUserId(String Id, String userId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("id={0}", Id).and("user_id={0}", userId);
		return selectCount (entityWrapper);
	}
	/**根据Id和demandId判断是否存在此数据*/
	public int IsExitsRegisterByIdAndDemandId(String Id, String demandId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("id={0}", Id).and("demand_id={0}", demandId);
		return selectCount (entityWrapper);
	}

	/**根据demandId和userId判断是否申请过此需求*/
	public int IsExitsRegisterByDemandIdAndUserId(String demandId, String userId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", demandId).and("user_id={0}", userId);
		return selectCount (entityWrapper);
	}


	/**修改验证码错误次数*/
	public boolean updateVerificationCode(String demandRegisterId,int times) {
		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setTimes(times);
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("id={0}", demandRegisterId);
		return update(demandRegister, entityWrapper);
	}
	/**修改验证状态*/
	public boolean updateValidationStatus(String id, boolean validationStatus) {
		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setValidationStatus(validationStatus);
		demandRegister.setId(id);
		return updateById(demandRegister);
	}
	
	/**入选者者放弃参与*/
	public boolean registerDrop(String userId, String registerId) {
		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setStatus(DemandRegisterStatus.DROP.status);
		demandRegister.setId(registerId);
		return updateById(demandRegister);
	}
	
	/**发布者取消该入选者*/
	public boolean cancelRegister( String orderId) {
		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setStatus(DemandRegisterStatus.PUBLISH_CANCEL.status);
		demandRegister.setDemandOrderId(null);
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_order_id={0}", orderId);
		return update(demandRegister, entityWrapper);
	}
	
	/**根据demandOrderId查询申请者的userId*/
	public String selectRegisterUserId(String demandOrderId) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_order_id={0}", demandOrderId);
		return selectOne(entityWrapper).getUserId();
	}
	
	/**根据userId或demandId查询申请单列表*/
	public List<DemandRegister> getRegList(String demandId, String userId, Page<DemandRegister> page) {
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("status={0}", DemandRegisterStatus.REGISTERED.status);
		if (StringUtils.hasText(userId)) {
			entityWrapper.and("user_id={0}", userId);
		}
		if (StringUtils.hasText(demandId)) {
			entityWrapper.and("demand_id={0}", demandId);
		}
		Page<DemandRegister> selectPage = selectPage(page, entityWrapper);
		return selectPage.getRecords();
	}

	/**查询该用户申请表中状态为“已入选”或者“确认出席，准备校验验证码”*/
	public List<DemandRegister> getUnCompleteList(String userId, List<Integer> registerStatusList) {
		EntityWrapper<DemandRegister> demandRegisterWrapper = new EntityWrapper<DemandRegister>();
		demandRegisterWrapper.where("user_id={0}", userId);
		demandRegisterWrapper.in("status", registerStatusList);
		return selectList(demandRegisterWrapper);
	}
	/**根据申请者id和状态查询**/
	public List<DemandRegister> selectListByUserId(String userId) {
		EntityWrapper<DemandRegister> demandRegisterWrapper = new EntityWrapper<DemandRegister>();
		demandRegisterWrapper.where("user_id={0}", userId);
		return selectList (demandRegisterWrapper);
	}

	public List<DemandRegister> selectPageByUserId(String userId,Page page) {
		EntityWrapper<DemandRegister> demandRegisterWrapper = new EntityWrapper<DemandRegister>();
		demandRegisterWrapper.where("user_id={0}", userId);
		if(page!=null){
			return selectPage(page,demandRegisterWrapper).getRecords();
		}
		return selectList (demandRegisterWrapper);
	}

	/**
	 * create by FRWIN
	 * @param userId
	 * @param page
	 * @return
	 */
	public List<DemandRegister> selectPageByUserIdTwo(String userId,Page page) {
		EntityWrapper<DemandRegister> demandRegisterWrapper = new EntityWrapper<DemandRegister>();
		demandRegisterWrapper.where("user_id={0}", userId);
		demandRegisterWrapper.orderBy ( "create_time",false );//降序
		if(page!=null){
			return selectPage(page,demandRegisterWrapper).getRecords();
		}
		return selectList (demandRegisterWrapper);
	}

	/**根据申请者id和状态查询**/
	public DemandRegister selectByUserIdAndDemand(String userId,String DemandId) {
		EntityWrapper<DemandRegister> demandRegisterWrapper = new EntityWrapper<DemandRegister>();
		demandRegisterWrapper.where("user_id={0}", userId);
		demandRegisterWrapper.and("demand_id={0}", DemandId);
		return selectOne (demandRegisterWrapper);
	}
	/****/
	public boolean changeRegister(String demandReigster){

		DemandRegister demandRegister = new DemandRegister();
		demandRegister.setStatus(DemandRegisterStatus.FAIL.status);
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<DemandRegister>();
		entityWrapper.where("id={0}",demandReigster);
		return update(demandRegister, entityWrapper);
	}

	/**已报名人数*/
	public int getDemandApplyNumByDemandId(String DemandId ){
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("demand_id={0}", DemandId);
		return  selectCount ( entityWrapper );
	}

	/**根据需求id 和 状态获取改状态所有报酬总数*/
	public long getDemandWagesByDemandIdAndStatus(String demandId,Integer... registerStatus){
		EntityWrapper<DemandRegister> entityWrapper = new EntityWrapper<>();
		entityWrapper.setSqlSelect ( "sum(wage)" );
		entityWrapper.where("demand_id={0}", demandId);
		int length = registerStatus.length;
		if(length>0){
			if(length==1){
				entityWrapper.and("status={0}",registerStatus);
			}else{
				entityWrapper.in("status",registerStatus);
			}
		}
		entityWrapper.orderBy ( "create_time",true );
		BigDecimal total = (BigDecimal)selectObj(entityWrapper);
		return total==null?0:total.longValue();
	}

}
