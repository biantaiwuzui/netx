package com.netx.worth.biz.demand;

import java.math.BigDecimal;
import java.util.List;

import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.enums.DemandOrderStatus;
import com.netx.worth.enums.DemandRegisterStatus;
import com.netx.worth.service.WorthServiceprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netx.common.wz.dto.demand.DemandOrderConfirmDto;
import com.netx.utils.money.Money;
import com.netx.worth.model.DemandOrder;
import com.netx.worth.model.DemandRegister;

/**
 * <p>
 * 需求订单表，每次入选就生成一张，以应对长期的需求，长期需求只能有1人入选。 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-09-11
 */
@Service
public class DemandOrderAction{
	
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	

    @Autowired
    private SettlementAction settlementAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;

    @Transactional(rollbackFor = Exception.class)
    public boolean addDemandOrder(DemandOrder demandOrder,String demandId,String userId,String createUserId){
        demandOrder.setDemandId(demandId);
        demandOrder.setUserId(userId);
        demandOrder.setStatus(DemandOrderStatus.ACCEPT.status);
        demandOrder.setCreateUserId(createUserId);
        demandOrder.setUpdateUserId(createUserId);
        return worthServiceprovider.getDemandOrderService().insert(demandOrder);
    }

    public boolean updateValidationStatus(String id, boolean validationStatus) {
        DemandOrder demandOrder = new DemandOrder();
        demandOrder.setId(id);
        demandOrder.setValidationStatus(validationStatus);
        return worthServiceprovider.getDemandOrderService().updateById(demandOrder);
    }
    
    public boolean publishConfirmOrderDetail(List<String> demandOrderIds, DemandOrderConfirmDto demandOrderConfirmDto) {
    	return worthServiceprovider.getDemandOrderService().publishConfirmOrderDetail(demandOrderIds, demandOrderConfirmDto);
    }

    /**定时任务 验证到场*/
    @Transactional
    public void checkSuccess(String demandOrderId) throws Exception {
        DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(demandOrderId);
        List<DemandRegister> demandRegisters = worthServiceprovider.getDemandRegisterService().getSuccessRegListByDemandOrderId(demandOrderId);
        boolean each = demandOrder.getEachWage();
        Long wage = demandOrder.getWage();
        Long bail = demandOrder.getBail();
        Long eachWage = null;
        //是否是单位报酬
        if (each) {
            eachWage = wage;
        } else {
            eachWage = new Money(new BigDecimal(wage)).divide(new BigDecimal(demandRegisters.size())).getCent();

        }

        for (DemandRegister demandRegister : demandRegisters) {
            boolean orderSuccess = demandRegister.getValidation() && demandRegister.getValidationStatus() && demandOrder.getValidationStatus();
            if (!orderSuccess) {
                if (demandOrder.getValidationStatus() && !demandRegister.getValidationStatus()) {//只有出席者可以获得报酬，缺席者的应得报酬退回给发起人
                    settlementAction.settlementAmountRightNow("发布者到场，预约者缺席，将预约者的报酬给发布人", "DemandOrder", demandOrderId, demandOrder.getUserId(),Money.CentToYuan(eachWage).getAmount());
                    bail = bail - eachWage;
                }
                if (!demandOrder.getValidationStatus() && demandRegister.getValidationStatus()) {
                    settlementAction.settlementAmountRightNow("预约者到场，发布者缺席，结算预约者的报酬", "DemandOrder", demandOrderId, demandRegister.getUserId(), Money.CentToYuan(eachWage).getAmount());
                }
                if (!demandOrder.getValidationStatus()) {
                    settlementAction.settlementCredit("DemandOrder", demandOrderId, demandOrder.getUserId(), -5);
                }
                if (!demandRegister.getValidationStatus()) {
                    settlementAction.settlementCredit("DemandOrder", demandOrderId, demandRegister.getUserId(), -2);
                }
                if (!demandRegister.getValidation()) {
                    settlementAction.settlementCredit("DemandOrder", demandOrderId, demandRegister.getUserId(), -2);
                }
            }
        }
        worthServiceprovider.getDemandOrderService().updateById(demandOrder);

    }

	public Integer getOrderSuccessCount(String demandId) {
		List<DemandOrder> list = worthServiceprovider.getDemandOrderService().getSuccessListByDemandId(demandId,DemandOrderStatus.SUCCESS);
		if(list != null && list.size() >= 0) {
			return list.size();
		}else{
			logger.error("查询需求成功的需求单列表失败");
			throw new RuntimeException();
		}
	}
    /**判断order 状态为 1 2 3 5 的数量  为0则关闭需求**/
    public Integer getOrderWaitCount(String demandId) {
        List<DemandOrder> list = worthServiceprovider.getDemandOrderService().getWaitListByDemandId(demandId);
        if(list != null && list.size() >= 0) {
            return list.size();
        }else{
            logger.error("查询需求成功的需求单列表失败");
            throw new RuntimeException();
        }
    }

	public boolean updateById(DemandOrder demandOrder) {
		boolean success = worthServiceprovider.getDemandOrderService().updateById(demandOrder);
		if(!success) {
			logger.error("更新失败");
			throw new RuntimeException();
		}
		return success;
	}
	@Transactional
    public boolean publishOrder(DemandOrder order,DemandRegister register) {
        order.setId(null);
        order.setPay ( true );
        order.setStatus ( DemandOrderStatus.ACCEPT.status );
        boolean success = worthServiceprovider.getDemandOrderService().insert (order);
        //设置rigister表的状态为 已入选
        register.setStatus ( DemandRegisterStatus.SUCCESS.status);
        register.setDemandOrderId ( worthServiceprovider.getDemandOrderService ().getOrderByUserIdAndDemandId (order.getUserId (),order.getDemandId ()  ).getId ());
        worthServiceprovider.getDemandRegisterService ().updateById ( register );
        return success;
    }

}
