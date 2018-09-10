package com.netx.worth.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.netx.utils.DistrictUtil;
import com.netx.worth.model.Skill;
import com.netx.worth.model.SkillOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.skill.SkillRegisterDto;
import com.netx.utils.money.Money;
import com.netx.worth.enums.SkillRegisterStatus;
import com.netx.worth.mapper.SkillRegisterMapper;
import com.netx.worth.model.SkillRegister;

@Service
public class SkillRegisterService extends ServiceImpl<SkillRegisterMapper, SkillRegister> {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	/**通过userId查询该用户申请的技能状态为已发布的列表*/
	public List<SkillRegister> checkHasUnComplete(String userId) {
		EntityWrapper<SkillRegister> skillOrderWrapper = new EntityWrapper<SkillRegister>();
        skillOrderWrapper.where("user_id={0}", userId);
        skillOrderWrapper.and("status={0}", SkillRegisterStatus.SUCCESS.status);
        return selectList(skillOrderWrapper);
	}
	
	/**根据userId清除register单的数据*/
	public boolean cleanRegister(String userId) {
		EntityWrapper<SkillRegister> skillRegisterWrapper = new EntityWrapper<SkillRegister>();
		skillRegisterWrapper.where("user_id={0}", userId);
		return delete(skillRegisterWrapper);
	}

	/**查询用户的申请单列表*/
    public List<SkillRegister> getUserRegisterList(String userId, Page<SkillRegister> page) {
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        Page<SkillRegister> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }


    /**设置申请保证金*/
    public boolean registerDeposit(String id, String userId, BigDecimal bail, Integer payWay) {
        SkillRegister skillRegister = new SkillRegister();
        skillRegister.setUpdateUserId(userId);
        skillRegister.setBail(new Money(bail).getCent());
        skillRegister.setPayWay(payWay);
        skillRegister.setPay(true);
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId).and("id={0}", id);
        return update(skillRegister, entityWrapper);
    }
    
    /**确定入选*/
    public boolean success(String skillRegisterId) {
        SkillRegister skillRegister = new SkillRegister();
        skillRegister.setStatus(SkillRegisterStatus.SUCCESS.status);
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", skillRegisterId);
        return update(skillRegister, entityWrapper);
    }
    
    /**更新验证码验证次数*/
    public boolean updateVerificationCode(String userId, String skillRegisterId, int times) {
        SkillRegister skillRegister = new SkillRegister();
        skillRegister.setTimes(times);
        skillRegister.setUpdateUserId(userId);
        skillRegister.setValidationStatus(true);
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", skillRegisterId).and("user_id={0}", userId);
        return update(skillRegister, entityWrapper);
    }
    
    /**更新验证码状态*/
    public boolean codeValidated(String userId, String skillRegisterId) {
        SkillRegister skillRegister = new SkillRegister();
        skillRegister.setValidation(true);
        skillRegister.setValidationStatus(true);
        skillRegister.setUpdateUserId(userId);
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", skillRegisterId).and("user_id={0}", userId);
        return update(skillRegister, entityWrapper);
    }
    
    /**申请人支付后回调*/
    public boolean registerPay(String registerId, String userId) {
        SkillRegister skillRegister = new SkillRegister();
        skillRegister.setPay(true);
        skillRegister.setUpdateUserId(userId);
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", registerId).and("user_id={0}", userId);
        return update(skillRegister, entityWrapper);
    }
    
    /**更新申请状态为已过期*/
    public boolean expire(String skillRegisterId) {
        SkillRegister skillRegister = new SkillRegister();
        skillRegister.setStatus(SkillRegisterStatus.FAIL.status);
        skillRegister.setId(skillRegisterId);
        return updateById(skillRegister);
    }

    /**
     * 更新状态为已拒绝 2
     * @param skillRegisterId
     * @return
     */
    public boolean refust(String skillRegisterId){
        SkillRegister skillRegister = new SkillRegister();
        skillRegister.setStatus(SkillRegisterStatus.CANCEL.status);
        skillRegister.setId(skillRegisterId);
        return  updateById(skillRegister);
    }
    
    /**更新验证状态*/
    public boolean updateValidationStatus(String id, boolean validationStatus) {
        SkillRegister skillRegister = new SkillRegister();
        skillRegister.setValidationStatus(validationStatus);
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0}", id);
        return update(skillRegister, entityWrapper);
    }
    
    /**根据skillId查询申请列表*/
    public List<SkillRegister> getRegListBySkillId(String skillId, Page<SkillRegister> page) {
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("skill_id={0}", skillId).orderBy("create_time desc");
        Page<SkillRegister> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }
    
    /**根据skillId列表查询申请列表*/
    public List<SkillRegister> getRegList(List<String> skillIds) {
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("skill_id", skillIds);
        return selectList(entityWrapper);
    }
    
    /**根据userId和预约id判断是否存在此条数据**/
    public Integer getCountByUserIdAndSkillId(String id,String userId) {
        EntityWrapper<SkillRegister> skillRegisterWrapper = new EntityWrapper<>();
        skillRegisterWrapper.where("user_id={0}", userId);
        skillRegisterWrapper.and("id={0}", id);
        return selectCount(skillRegisterWrapper);
    }
    
    /**根据userId查r单id数据*/
    public List<SkillRegister> RegisterList( String userId) {
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        entityWrapper.orderBy("create_time desc");
        return selectList(entityWrapper);
    }

    /**
     * @param userId
     * @param skillRegisterStatus 不包含的状态
     * @return
     */
    public List<String> getSkillIds( String userId,SkillRegisterStatus skillRegisterStatus) {
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        if(skillRegisterStatus!=null){
            entityWrapper.and("status!={0}",skillRegisterStatus.status);
        }
        entityWrapper.orderBy("create_time desc");
        entityWrapper.setSqlSelect("skill_id");
        return (List<String>)(List)selectObjs(entityWrapper);
    }

    /**根据skillId查询申请列表*/
    public List<SkillRegister> getRegListBySkillId1(String skillId) {
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("skill_id={0}", skillId);
        return selectList(entityWrapper);
    }

    /** 根据UserId查询预约列表*/
    public SkillRegister getRegListList(String id) {
            EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("user_id={0}", id);
            return selectOne(entityWrapper);
    }


    
    /**根据userId和技能id返回预约单**/
    public SkillRegister getReByUserIdAndSkillId(String reId,String skillId) {
        EntityWrapper<SkillRegister> skillRegisterWrapper = new EntityWrapper<>();
        skillRegisterWrapper.where("id={0}", reId);
        skillRegisterWrapper.and("skill_id={0}", skillId);
        return selectOne(skillRegisterWrapper);
    }
    
    /**根据ID返回预约单**/
    public SkillRegister getReById(String skillRegisterUserId) {
        EntityWrapper<SkillRegister> skillRegisterWrapper = new EntityWrapper<>();
        skillRegisterWrapper.where("id={0}", skillRegisterUserId);
        return selectOne(skillRegisterWrapper);
    }

    /**根据skillId查询申请列表*/
    public SkillRegister getRegBySkillId(String skillId) {
        EntityWrapper<SkillRegister> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("skill_id={0}", skillId);
        return selectOne(entityWrapper);
    }

    /**根据userId和技能id返回预约单**/
    public SkillRegister getReByUserIdAndSkillIdList(String userId,String skillId) {
        EntityWrapper<SkillRegister> skillRegisterWrapper = new EntityWrapper<>();
        skillRegisterWrapper.where("user_id={0}", userId);
        skillRegisterWrapper.and("skill_id={0}", skillId);
        skillRegisterWrapper.orderBy("create_time", false);
        return selectOne(skillRegisterWrapper);
    }

    /**判断是否重复预约**/
    public int getByUserIdAndSkillId(String skill_id,String userId) {
        EntityWrapper<SkillRegister> skillRegisterWrapper = new EntityWrapper<>();
        skillRegisterWrapper.where("user_id={0}", userId);
        skillRegisterWrapper.and("skill_id={0}", skill_id);
        return selectCount(skillRegisterWrapper);
    }

    public SkillRegister queryByUserIdAndId(String id,String userId) {
        EntityWrapper<SkillRegister> skillRegisterWrapper = new EntityWrapper<>();
        skillRegisterWrapper.where("id={0} and user_id={1}", id,userId);
        return selectOne(skillRegisterWrapper);
    }

    /**判断被预约**/
    public int getBySkillId(String skill,SkillRegisterStatus skillRegisterStatus,SkillRegisterStatus noSkillRegisterStatus) {
        EntityWrapper<SkillRegister> wrapper = new EntityWrapper<>();
        wrapper.where("skill_id={0}", skill);
        if(skillRegisterStatus!=null){
            wrapper.and("status={0}",skillRegisterStatus.status);
        }
        if(noSkillRegisterStatus!=null){
            wrapper.and("status!={0}",noSkillRegisterStatus.status);
        }
        return selectCount(wrapper);
    }
    
    /* 获取已成交的预约列表（获取入选者）*/
    public List<SkillRegister> getStatusSkillRegister(String skillId,Integer status){
        EntityWrapper<SkillRegister> wrapper = new EntityWrapper<>();
        wrapper.where("skill_id={0} and status={1}",skillId,status);
        return selectList(wrapper);
    }
    
    /* 获取预约列表 */
    public List<SkillRegister> getSkillRegisterList(String skillId){
        EntityWrapper<SkillRegister> wrapper = new EntityWrapper<>();
        wrapper.where("skill_id={0}",skillId);
        return selectList(wrapper);
    }

    public List<String> querySkillRegister(String[] skillId){
        EntityWrapper<SkillRegister> wrapper = new EntityWrapper<>();
        wrapper.where("deleted=0");
        wrapper.in("skill_id",skillId);
        wrapper.setSqlSelect("id");
        return (List<String>)(List)selectObjs(wrapper);
    }

    public List<Map<String,Object>> querySkillRegister( Collection<?> userIds){
        EntityWrapper<SkillRegister> wrapper = new EntityWrapper<>();
        wrapper.where("deleted=0 and is_pay={0}",true);
        wrapper.in("user_id",userIds);
        wrapper.setSqlSelect("user_id as userId,sum(fee) as total");
        wrapper.groupBy("user_id");
        return selectMaps(wrapper);
    }
}
