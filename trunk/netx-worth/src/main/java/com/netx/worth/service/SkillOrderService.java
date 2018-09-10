package com.netx.worth.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netx.worth.enums.SkillOrderStatus;
import com.netx.worth.enums.SkillRegisterStatus;
import com.netx.worth.model.Skill;
import com.netx.worth.model.SkillRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.SkillOrderMapper;
import com.netx.worth.model.SkillOrder;


@Service
public class SkillOrderService extends ServiceImpl<SkillOrderMapper, SkillOrder> {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public long getSkillIncomeTotal(List<String> registerId){
        EntityWrapper<SkillOrder> skillOrderWrapper = new EntityWrapper<SkillOrder>();
        skillOrderWrapper.in("skill_register_id", registerId);
        return getSkillIncomeTotal(skillOrderWrapper);
    }

    private long getSkillIncomeTotal(Wrapper<SkillOrder> wrapper){
        wrapper.where("status={0} and deleted=0",SkillOrderStatus.SUCCESS.status);
        wrapper.setSqlSelect("sum(fee)");
        BigDecimal total = (BigDecimal)selectObj(wrapper);
        return total==null?0:total.longValue();
    }

    public long getSkillIncomeTotal(String[] registerId){
        EntityWrapper<SkillOrder> skillOrderWrapper = new EntityWrapper<SkillOrder>();
        skillOrderWrapper.in("skill_register_id", registerId);
        return getSkillIncomeTotal(skillOrderWrapper);
    }

	public List<SkillOrder> getOrderListByUserId(String reUserId) {
		EntityWrapper<SkillOrder> skillOrderWrapper = new EntityWrapper<SkillOrder>();
		skillOrderWrapper.where("skill_register_id={0}", reUserId);
		return selectList(skillOrderWrapper);
	}

    public SkillOrder getOrderByUserId(String reUserId) {
        EntityWrapper<SkillOrder> skillOrderWrapper = new EntityWrapper<SkillOrder>();
        skillOrderWrapper.where("skill_register_id={0}", reUserId);
        return selectOne(skillOrderWrapper);
    }

	/**根据userId清除Order单的数据*/
	public boolean cleanOrder(String userId) {
		EntityWrapper<SkillOrder> skillOrderWrapper = new EntityWrapper<SkillOrder>();
		skillOrderWrapper.where("user_id={0}", userId);
		return delete(skillOrderWrapper);
	}
	
	/**插入Order单数据*/
    public SkillOrder create(SkillOrder skillOrder) {
        boolean success = insert(skillOrder);
        if(!success) {
        	return null;
        }
        return skillOrder;
    }
    
    /**生成验证码并修改状态*/
    public SkillOrder publishGeneratCode(SkillOrder skillOrder ,String id, String reId) {
        EntityWrapper<SkillOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", id);
        boolean success = update(skillOrder, entityWrapper);
        if(!success) {
        	return null;
        }
        return skillOrder;
    }
    
    /**根据registerId查询Order单*/
    public SkillOrder selectByRegisterId(String skillRegisterId) {
        EntityWrapper<SkillOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("skill_register_id={0}", skillRegisterId);
        return selectOne(entityWrapper);
    }

    /**根据skill_register_id返回此条数据*/
    public SkillOrder getOrderListByregisterId(String registerId) {
        EntityWrapper<SkillOrder> skillOrderWrapper = new EntityWrapper<SkillOrder>();
        skillOrderWrapper.where("skill_register_id={0}",registerId);
        return selectOne(skillOrderWrapper);
    }

    /**根据Id修改验证状态*/
    public boolean updateValidationStatus(String id,SkillOrder skillOrder) {
        EntityWrapper<SkillOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", id);
        return update(skillOrder, entityWrapper);
    }

    /**根据ID查r单数据*/
    public List<SkillOrder> orderList( String id) {
        EntityWrapper<SkillOrder> skillOrderWrapper = new EntityWrapper<>();
        skillOrderWrapper.where("create_user_id = {0} status = 3", id);
        return selectList(skillOrderWrapper);
    }

    /**根据userId和技能单id判断是否存在此条数据**/
    public int getCountByUserIdAndSkillId(String userId,String id) {
        EntityWrapper<SkillOrder> skillOrderWrapper = new EntityWrapper<>();
        //skillOrderWrapper.where("user_id={0}", userId);//!!!!!model根本没有这个字段，目测改为create_user_id
        skillOrderWrapper.where("create_user_id={0}", userId);
        skillOrderWrapper.and("id={0}", id);
        return selectCount(skillOrderWrapper);
    }

    /**根据registerId查询Order单*/
    public int selectCoCount(String skillRegisterId) {
        EntityWrapper<SkillOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("skill_register_id={0}", skillRegisterId);
        entityWrapper.and("status={0}",3);
        return selectCount(entityWrapper);
    }

    /**根据userId获取技能成功的总数*/
    public int getSkillOrderCountByUserId(String userId){
        EntityWrapper<SkillOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("skill_register_id={0}", userId);
        entityWrapper.and ("status={0}",SkillOrderStatus.SUCCESS.status);
        return selectCount ( entityWrapper );
    }

    /**
     * 更改订单状态
     * @return
     */
    public boolean updateOrderStatus(SkillOrder skillOrder,int status){
        EntityWrapper<SkillOrder> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}",skillOrder.getId());
        skillOrder.setStatus(status);
        return update(skillOrder,entityWrapper);
    }

}
