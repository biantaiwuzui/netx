package com.netx.worth.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netx.worth.model.DemandOrder;
import com.netx.worth.model.SkillOrder;
import com.netx.worth.model.SkillRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.wz.dto.skill.SkillPublishDto;
import com.netx.utils.money.Money;
import com.netx.worth.enums.SkillStatus;
import com.netx.worth.mapper.SkillMapper;
import com.netx.worth.model.Skill;

@Service
public class SkillService extends ServiceImpl<SkillMapper, Skill> {

	/**
	 * 发布技能
	 */
	public String insertOrUpdateSkill(Skill skill) throws Exception {
		boolean success = insertOrUpdate(skill);
		if (!success) {
			return null;
		}
		return skill.getId();
	}

	public List<Skill> queryByIds(List<String> skillIds,Page page){
		Wrapper wrapper = new EntityWrapper();
		wrapper.in("id",skillIds);
		if(page==null){
			return selectList(wrapper);
		}
		return selectPage(page,wrapper).getRecords();
	}

	/**
	 * 查询是否有相同的技能已经发布了
	 */
	public List<Skill> samePublish(SkillPublishDto skillPublishDto) {
		EntityWrapper<Skill> skillWrapper = new EntityWrapper<Skill>();
		skillWrapper.where("user_id={0}", skillPublishDto.getUserId());
		skillWrapper.and("description={0}", skillPublishDto.getDescription());
		skillWrapper.and("skill={0}", skillPublishDto.getSkill());
		skillWrapper.and("level={0}", skillPublishDto.getLevel());
		return selectList(skillWrapper);
	}

	/**
	 * 根据userId和Id修改该技能
	 */
	public boolean editByIdAndUserId(String id, String userId, Skill skill) {
		EntityWrapper<Skill> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("id={0}", id).and("user_id={0}", userId);
		return update(skill, entityWrapper);
	}

	/**
	 * 通过userId查询该用户未完成的技能列表
	 */
	public List<Skill> checkHasUnComplete(String userId) {
		EntityWrapper<Skill> skillWrapper = new EntityWrapper<Skill>();
		skillWrapper.where("user_id={0}", userId);
		skillWrapper.and("status={0}", SkillStatus.PUBLISHED.status);
		return selectList(skillWrapper);
	}

	/**
	 * 根据userId分页查询该用户的技能列表
	 */
	public List<Skill> getUserSkillList(String userId, Page<Skill> page) {
		EntityWrapper<Skill> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		Page<Skill> selectPage = selectPage(page, entityWrapper);
		return selectPage.getRecords();
	}

	/**
	 * 根据userId分页查询该用户的技能列表
	 * create by FRWIN
	 */
	public List<Skill> getUserSkillListTwo(String userId, Page<Skill> page) {
		EntityWrapper<Skill> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		entityWrapper.orderBy ( "create_time",false );//降序
		Page<Skill> selectPage = selectPage(page, entityWrapper);
		return selectPage.getRecords();
	}

	/**
	 * 清除该用户的技能数据
	 */
	public boolean clean(String userId) {
		EntityWrapper<Skill> skillWrapper = new EntityWrapper<Skill>();
		skillWrapper.where("user_id={0}", userId);
		return delete(skillWrapper);
	}

	/**
	 * 根据userId和技能id判断是否存在此条数据
	 **/
	public int getCountByUserIdAndSkillId(String id, String userId) {
		EntityWrapper<Skill> skillWrapper = new EntityWrapper<Skill>();
		skillWrapper.where("user_id={0}", userId);
		skillWrapper.and("id={0}", id);
		return selectCount(skillWrapper);
	}

	/**根据技能id和用户（技能发布者id获取技能对象）*/
	public Skill queryByUserIdAndSkillId(String id, String userId) {
		EntityWrapper<Skill> skillWrapper = new EntityWrapper<Skill>();
		skillWrapper.where("user_id={0}", userId);
		skillWrapper.and("id={0}", id);
		return selectOne(skillWrapper);
	}


	/**根据该技能ID是否属于当前用户**/
	public int userSkill (String id,String userId ) {
		EntityWrapper<Skill> skillWrapper = new EntityWrapper<>();
		skillWrapper.where("user_id={0}", userId);
		skillWrapper.and("id={0}", id);
		return selectCount(skillWrapper);
	}
	/**
	 * 根据userId查询该用户的技能列表
	 */
	public List<Skill> getUserSkList(String userId) {
		EntityWrapper<Skill> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		return selectList(entityWrapper);
	}

	/**
	 * 根据userId和技能id判断是否存在此条数据
	 **/
	public int getByUserIdAndSkillId(String userId, String skillId) {
		EntityWrapper<Skill> skillWrapper = new EntityWrapper<Skill>();
		skillWrapper.where("user_id={0}", userId);
		skillWrapper.and("id={0}", skillId);
		return selectCount(skillWrapper);
	}

	/**
	 * 根据userId此条数据
	 **/
	public Skill getSkill(String userId) {   //此方法有错，因为一个用户可以有多个技能，根据userId返回是集合
		EntityWrapper<Skill> skillWrapper = new EntityWrapper<Skill>();
		skillWrapper.and("id={0}", userId);
		return selectOne(skillWrapper);
	}

	/**根据userId获取技能的总数*/
	public int getSkillCountByUserId(String userId){
		EntityWrapper<Skill> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		return selectCount ( entityWrapper );
	}

	/**根据userId获取最新发布的技能*/
	public Skill getNewSkill(String userId){
		EntityWrapper<Skill> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("user_id={0}", userId);
		entityWrapper.orderBy ( "create_time",false );
		return selectOne( entityWrapper );
	}

	public List<Map<String,Object>> querySkill(Collection<?> userIds){
		EntityWrapper<Skill> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("status!={0} and deleted=0",SkillStatus.CANCEL.status);
		entityWrapper.in("user_id",userIds);
		entityWrapper.setSqlSelect("user_id as userId,GROUP_CONCAT(id) as ids");
		entityWrapper.groupBy("userId");
		return selectMaps(entityWrapper);
	}
}
