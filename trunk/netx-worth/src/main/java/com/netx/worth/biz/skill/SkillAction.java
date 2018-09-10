package com.netx.worth.biz.skill;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import com.netx.utils.money.Money;
import com.netx.worth.biz.common.RefundAction;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.model.*;
import com.netx.worth.service.*;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.wz.dto.common.CommonCheckDto;
import com.netx.common.wz.dto.skill.SkillPublishDto;
import com.netx.common.wz.dto.skill.SkillRegisterDto;
import com.netx.searchengine.model.SkillSearchResponse;
import com.netx.searchengine.query.SkillSearchQuery;
import com.netx.searchengine.service.SkillSearchService;
import com.netx.utils.DistrictUtil;
import com.netx.worth.enums.SkillOrderStatus;
import com.netx.worth.enums.SkillRegisterStatus;
import com.netx.worth.enums.SkillStatus;

/**
 * <p>
 * 技能表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class SkillAction {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private SkillRegisterAction skillRegisterAction;
	@Autowired
	private SkillOrderAction skillOrderAction;
	@Autowired
	private RefundAction refundAction;
	@Autowired
	private SettlementAction settlementAction;
	@Autowired
	private AddImgUrlPreUtil addImgUrlPreUtil;
	@Autowired
	private SkillSearchService skillSearchService;
	@Autowired
	private WorthServiceprovider worthServiceprovider;



	public boolean check(CommonCheckDto commonCheckDto,String userId,Double lon,Double lat) {
		boolean success;
		Skill skill = null;
		SkillOrder skillOrder;
		SkillRegister skillRegister = null;
		//xx xx xx
		if (commonCheckDto.getFromOrTo() == 0) {// 主方
			skillOrder = worthServiceprovider.getSkillOrderService().selectById(commonCheckDto.getId());
			//skill = selectById(skillRegister.getSkillId());
		} else {// 客方
			SkillRegister skillRe = worthServiceprovider.getSkillRegisterService().getRegListList(userId);
			skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRe.getId());
			//skill = selectById(skillRegister.getSkillId());
			skillOrder = worthServiceprovider.getSkillOrderService().selectByRegisterId(skillRegister.getId());
		}
		Double distance = DistrictUtil.calcDistance(skillOrder.getLat(), skillOrder.getLon(), new BigDecimal(lat), new BigDecimal(lon));
		boolean validationStatus = (distance * 1000) <= 300; //订单经纬度

		if (commonCheckDto.getFromOrTo()== 0) {// 主方
			success = worthServiceprovider.getSkillOrderService().updateValidationStatus(skillOrder.getId(), skillOrderAction.skillOrderActionStatus(validationStatus));
		} else {// 客方
			success = worthServiceprovider.getSkillRegisterService().updateValidationStatus(skillRegister.getId(), validationStatus);
		}
		return validationStatus && success;
	}


	
	//我发布的技能
	public Map<String, Object> publishList(String userId, Page<Skill> page) {
		Map<String, Object> map = new HashMap<>();
		List<Skill> list = worthServiceprovider.getSkillService().getUserSkillListTwo(userId, page);
		if (list != null && list.size() > 0) {
			for (Skill skill : list) {
				skill.setSkillImagesUrl((addImgUrlPreUtil.addImgUrlPres(skill.getSkillImagesUrl(), AliyunBucketType.ActivityBucket)));
				skill.setSkillDetailImagesUrl((addImgUrlPreUtil.addImgUrlPres(skill.getSkillDetailImagesUrl(), AliyunBucketType.ActivityBucket)));
			}
		}
		map.put("list", list);
		if (list != null && list.size() > 0)
			getPublishListHash(map, list);
		return map;
	}

	public boolean edit(Skill skill) throws Exception {
		String skillId = worthServiceprovider.getSkillService().insertOrUpdateSkill(skill);
		if (skillId == null) {
			return false;
		}
		return true;
	}

	//发布者取消技能，该方法会直接取消技能，但是技能单不影响
	public boolean publishCancel(String id, String userId) {
		int count = worthServiceprovider.getSkillService().getCountByUserIdAndSkillId(id, userId);
		if(count != 1) {
			throw new RuntimeException("该用户不能取消此技能");
		}
		Skill skill = worthServiceprovider.getSkillService().selectById(id);
		if(skill.getStatus().equals(SkillStatus.CANCEL.status)) {  //如果状态本身为取消 返回
			return false;
		}
		skill.setUpdateUserId(userId);
		skill.setStatus(SkillStatus.CANCEL.status);
		return worthServiceprovider.getSkillService().editByIdAndUserId(id, userId, skill);
	}

	//发布者结束技能
	public boolean publishStop(String id, String userId) {
		//判断数据库是否存在技能id 且用户id为userId
		int count = worthServiceprovider.getSkillService().getCountByUserIdAndSkillId(id, userId);
		if (count != 1) {
			throw new RuntimeException("该用户没有此技能");
		}
		Skill skill = worthServiceprovider.getSkillService().selectById(id);
		if (skill.getStatus().equals(SkillStatus.STOP.status)){
			throw new RuntimeException("该技能已经结束");
		}
		skill.setUpdateUserId(userId);
		skill.setStatus(SkillStatus.STOP.status);
		return worthServiceprovider.getSkillService().editByIdAndUserId(id, userId, skill);
	}

	/* 报名者修改预约 */
	public boolean registerEdit(SkillRegisterDto skillRegisterDto) throws Exception {
		boolean success =false;
		Map<String,Object> map = new HashMap<>();
		int count = worthServiceprovider.getSkillRegisterService().getCountByUserIdAndSkillId(skillRegisterDto.getUserId(), skillRegisterDto.getId());
		if (count != 1) {
			throw new  RuntimeException("该用户不能修改此预约");
		}
		Skill skill = worthServiceprovider.getSkillService().selectById(skillRegisterDto.getSkillId());
		if(skill==null){
			throw new RuntimeException("技能可能已取消");
		}
		map = skillRegisterAction.SkillRegisterDtoToSkill(skill.getAmount(),skillRegisterDto);
		if (map!=null)
			success = true;
		return success;
	}


	public Map<String, Object> nearList(String userId, BigDecimal lon, BigDecimal lat, Double length, Page<Skill> page) {
		Map<String, Object> map = new HashMap<>();
		SkillSearchQuery skillSearchQuery = new SkillSearchQuery();
		skillSearchQuery.setPageSize(page.getSize());
		skillSearchQuery.setFrom(page.getCurrent());
		skillSearchQuery.setMaxDistance(length);
		skillSearchQuery.setUserId(userId);
		GeoPoint geoPoint = new GeoPoint(lat.doubleValue(), lon.doubleValue());
		skillSearchQuery.setCenterGeoPoint(geoPoint);
		List<SkillSearchResponse> targetList = skillSearchService.querySkills(skillSearchQuery);
		List<Skill> list = new ArrayList<>();
		Skill source = null;
		if (targetList != null && targetList.size() > 0) {
			for (SkillSearchResponse SkillSearchResponse : targetList) {
				source = new Skill();
				BeanUtils.copyProperties(SkillSearchResponse, source);
				list.add(source);
			}
		}
		if (list != null && list.size() > 0) {
			for (Skill skill : list) {
				skill.setSkillImagesUrl((addImgUrlPreUtil.addImgUrlPres(skill.getSkillImagesUrl(), AliyunBucketType.ActivityBucket)));
				skill.setSkillDetailImagesUrl((addImgUrlPreUtil.addImgUrlPres(skill.getSkillDetailImagesUrl(), AliyunBucketType.ActivityBucket)));
			}
		}
		//TODO
		//map.put("list", DistrictUtil.getDistrictVoList(lat, lon, list));
		if (list != null && list.size() > 0) {
			getPublishListHash(map, list);
		}
		return map;
	}

	public void getPublishListHash(Map<String, Object> map, List<Skill> list) {
		if (list == null || list.size() <= 0)
			return;
		List<String> skillIds = list.stream().map(Skill::getId).collect(Collectors.toList());
		List<SkillRegister> skillRegisters = worthServiceprovider.getSkillRegisterService().getRegList(skillIds);
		Map<String, Long> registerCountHash = skillRegisters.stream()
				.collect(groupingBy(SkillRegister::getSkillId, Collectors.counting()));
		Map<String, Long> registerSuccessCountHash = skillRegisters.stream()
				.filter(e -> e.getStatus().equals(SkillRegisterStatus.SUCCESS.status))
				.collect(groupingBy(SkillRegister::getSkillId, Collectors.counting()));
		map.put("registerCountHash", registerCountHash);
		map.put("registerSuccessCountHash", registerSuccessCountHash);
	}
	
	//取消技能单
	public boolean publishCancelOrder(String id, String userId) {
		//判斷数据库是否存在技能id 且用户id为userId
		int count = worthServiceprovider.getSkillOrderService().getCountByUserIdAndSkillId(userId, id);
		if (count != 1) {
			return false;
		}
		SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(id);  //判断技能状态
		if (skillOrder.getStatus().equals(SkillOrderStatus.CANCEL.status))
			return false;
		return skillOrderAction.publishCancelOrder(id);
	}

/*
	public Map<String, Object> orderList(String userId) {
		Map skills = worthServiceprovider.getSkillService().getCompletedSkills(userId); //判断技能状态
		return skills;
	}
*/

//		if (list != null && list.size() > 0) {
//			List<SkillRegister> skillRegisters = worthServiceprovider.getSkillRegisterService()
//					.selectBatchIds(list.stream().map(SkillOrder::getSkillRegisterId).collect(Collectors.toList()));
//			map.put("registerUserIds",
//					skillRegisters.stream().map(SkillRegister::getUserId).collect(Collectors.toList()));
//			map.put("skillRegisterHash",
//					skillRegisters.stream().collect(Collectors.toMap(SkillRegister::getId, e -> e)));
//		}
//		List<SkillOrder> list = worthServiceprovider.getSkillOrderService().orderList(userId);
//		map.put("list", list);
//		Skill skill = worthServiceprovider.getSkillService().selectById(userId);
//		/** skill 空指针异常 **/
//		if (skill != null) {
//			skill.setSkillImagesUrl((addImgUrlPreUtil.addImgUrlPres(skill.getSkillImagesUrl(), AliyunBucketType.ActivityBucket)));
//			skill.setSkillDetailImagesUrl((addImgUrlPreUtil.addImgUrlPres(skill.getSkillDetailImagesUrl(), AliyunBucketType.ActivityBucket)));
//		}
//		map.put("skillOrder", skill);
//		if (list != null && list.size() > 0) {
//			List<SkillRegister> skillRegisters = worthServiceprovider.getSkillRegisterService()
//					.selectBatchIds(list.stream().map(SkillOrder::getSkillRegisterId).collect(Collectors.toList()));
//			map.put("registerUserIds",
//					skillRegisters.stream().map(SkillRegister::getUserId).collect(Collectors.toList()));
//			map.put("skillRegisterHash",
//					skillRegisters.stream().collect(Collectors.toMap(SkillRegister::getId, e -> e)));


	public boolean checkHasUnComplete(String userId) {
		List<Skill> skillList = worthServiceprovider.getSkillService().checkHasUnComplete(userId);
		if (skillList != null && skillList.size() > 0) {
			return true;
		}

		List<SkillRegister> skillOrderList = worthServiceprovider.getSkillRegisterService().checkHasUnComplete(userId);
		List<String> skillIds = skillOrderList.stream().map(SkillRegister::getSkillId).collect(toList());
		if (skillIds != null && skillIds.size() > 0) {
			List<Skill> skills = worthServiceprovider.getSkillService().selectBatchIds(skillIds);
			if (skills != null && skills.size() > 0) {
				for (Skill skill : skills) {
					if (skill.getStatus().equals(SkillStatus.PUBLISHED.status)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Transactional
	public boolean clean(String userId) throws Exception {
		boolean cleanSkill = worthServiceprovider.getSkillService().clean(userId);

		List<SkillOrder> list = worthServiceprovider.getSkillOrderService().getOrderListByUserId(userId);
		List<String> skillIds = list.stream().map(SkillOrder::getId).collect(toList());
		boolean cleanSettlment = true;
		boolean cleanSkillOrder = true;
		// 没有数据直接返回true
		if (skillIds != null && skillIds.size() > 0) {
			// 删除结算表和子表
			List<Settlement> settlementList = worthServiceprovider.getSettlementService().getSettlementListByTypeAndId("SkillOrder", skillIds);
			List<String> settlementIds = settlementList.stream().map(Settlement::getId).collect(toList());
			cleanSettlment = worthServiceprovider.getSettlementService().deleteSettlementListByTypeAndId("SkillOrder", skillIds) && settlementAction.clean(settlementIds);
			cleanSkillOrder = worthServiceprovider.getSkillOrderService().cleanOrder(userId);
		}

		boolean cleanSkillRegister = worthServiceprovider.getSkillRegisterService().cleanRegister(userId);

		boolean success = cleanSkillRegister && cleanSkillOrder && cleanSkill && cleanSettlment;
		if (success) {
			return success;
		} else {
			throw new RuntimeException("清除该用户技能的数据失败");
		}
	}

	public Skill selectById(String id) {
		return worthServiceprovider.getSkillService().selectById(id);
	}

	//发布、编辑技能
	public Skill SkillPublishDtoToSkill(SkillPublishDto skillPublishDto,BigDecimal lon,BigDecimal lat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(sdf.format(new Date()));
		boolean publish = !StringUtils.hasText(skillPublishDto.getId());  //是否该技能已近发布	如果字符串里面的值为null， ""， "   "，那么返回值为false；否则为true
		Skill skill = new Skill();
		if (publish) {
			skill.setCreateUserId(skillPublishDto.getUserId());
			skill.setUserId(skillPublishDto.getUserId());
			skill.setStatus(SkillStatus.PUBLISHED.status);
		} else {
			int count = worthServiceprovider.getSkillService().getCountByUserIdAndSkillId(skillPublishDto.getId(), skillPublishDto.getUserId());
			if (count != 1) {
				throw  new  RuntimeException("该用户不能编辑此技能");
			}
			skill.setUpdateUserId(skillPublishDto.getUserId());
			skill.setId(skillPublishDto.getId());        //编辑技能
		}
		skill.setSkill(skillPublishDto.getSkill());
		skill.setLevel(skillPublishDto.getLevel());
		skill.setDescription(skillPublishDto.getDescription());
		skill.setSkillImagesUrl((skillPublishDto.getPic()));
		skill.setSkillDetailImagesUrl((skillPublishDto.getPic2()));
		skill.setUnit(skillPublishDto.getUnit());
		skill.setAmount(new Money(skillPublishDto.getAmount()).getCent());
		skill.setIntr(skillPublishDto.getIntr());
		skill.setObj(skillPublishDto.getObj());
		skill.setLon(lon);
		skill.setLat(lat);
		skill.setUpdateTime(date);
		return skill;
	}

}
