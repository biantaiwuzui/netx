package com.netx.worth.biz.demand;

import static com.netx.common.user.util.DateTimestampUtil.TIME_DIFFERENCE;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.netx.common.common.enums.AuthorEmailEnum;
import com.netx.common.common.enums.JobEnum;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.wz.dto.demand.*;
import com.netx.common.wz.util.VerificationCodeUtil;
import com.netx.worth.biz.common.RefundAction;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.wz.dto.common.CommonCheckDto;
import com.netx.common.wz.dto.common.CreateRefundDto;
import com.netx.utils.DistrictUtil;
import com.netx.utils.money.Money;
import com.netx.worth.enums.DemandOrderStatus;
import com.netx.worth.enums.DemandRegisterStatus;
import com.netx.worth.enums.DemandStatus;
import com.netx.worth.model.Demand;
import com.netx.worth.model.DemandOrder;
import com.netx.worth.model.DemandRegister;
import com.netx.worth.model.Refund;
import com.netx.worth.model.Settlement;

/**
 * <p>
 * 需求表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class DemandAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private DemandOrderAction demandOrderAction;
	@Autowired
	private RefundAction refundAction;
	@Autowired
	private SettlementAction settlementAction;
	@Autowired
	private AddImgUrlPreUtil addImgUrlPreUtil;
	@Autowired
	private WorthServiceprovider worthServiceprovider;
	@Autowired
	private DemandRegisterAction demandRegisterAction;

	public boolean check(CommonCheckDto commonCheckDto,Double lon,Double lat) {
		boolean success = false;
		Demand demand = null;
		DemandOrder demandOrder = null;
		DemandRegister demandRegister = null;
		if (commonCheckDto.getFromOrTo().equals("0")) {// 主方
			demandOrder = worthServiceprovider.getDemandOrderService().selectById(commonCheckDto.getId());
			demand = selectById(demandOrder.getDemandId());
		} else {// 客方
			demandRegister = worthServiceprovider.getDemandRegisterService().selectById(commonCheckDto.getId());
			demand = selectById(demandRegister.getDemandId());
		}
		Double distance = DistrictUtil.calcDistance(demand.getLat(), demand.getLon(), new BigDecimal(lat), new BigDecimal(lon));
		boolean validationStatus = (distance * 1000) <= 300;
		if (commonCheckDto.getFromOrTo().equals("0")) {// 主方
			success = demandOrderAction.updateValidationStatus(demandOrder.getId(), validationStatus);
		} else {// 客方
			success = worthServiceprovider.getDemandRegisterService().updateValidationStatus(demandRegister.getId(), validationStatus);
		}
		return validationStatus && success;
	}

	public Map<String, Object>  getUserRegDemand(String demandId, String userId, Page<DemandRegister> page) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.hasText(demandId) && StringUtils.hasText(userId)) {
			// userId和demandId都有
			List<DemandRegister> registerList = worthServiceprovider.getDemandRegisterService().getDemandRegByUserIdAndDemandId(demandId, userId,
					page);
			map.put("demandRegister", registerList);
			getRegisterListHash(map, registerList);
		}
		if (StringUtils.hasText(userId) && StringUtils.isEmpty(demandId)) {
		// 只有userId
			List<DemandRegister> registerList = worthServiceprovider.getDemandRegisterService().getDemandRegByUserId(userId, page);
			map.put("demandRegisterListByUserId", registerList);
			getRegisterListHash(map, registerList);
		}
		else if (StringUtils.hasText(demandId) && StringUtils.isEmpty(userId)) {
			// 只有demandId
			List<DemandRegister> registerList = worthServiceprovider.getDemandRegisterService().getDemandRegByDemandId(demandId, page);
			map.put("demandRegisterListByDemandId", registerList);
			getRegisterListHash(map, registerList);
		}
		return map;
	}

	public Map<String, Object> getUserPublishDemand(String userId, Page<Demand> page) {
		Map<String, Object> map = new HashMap<>();
		List<Demand> list = worthServiceprovider.getDemandService().getSendList(userId, page);
		map.put("list", list);
		if (list != null && list.size() > 0) {
			for (Demand demand : list) {
				demand.setImagesUrl(
						addImgUrlPreUtil.addImgUrlPres(demand.getImagesUrl(), AliyunBucketType.ActivityBucket));
				demand.setDetailsImagesUrl(addImgUrlPreUtil.addImgUrlPres(demand.getDetailsImagesUrl(),
						AliyunBucketType.ActivityBucket));
			}
			getPublishListHash ( map, list );
		}
		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean publishEdit(Demand demand,DemandPublishDto demandPublishDto) {
		worthServiceprovider.getDemandService().editById(DemandPublishDtoToDemand (demand,demandPublishDto));
		return true;
	}

	public boolean publishCancel(String demandId, String userId) {
		Integer count = worthServiceprovider.getDemandRegisterService().getRegSuccessCount (demandId);
		//已经有人入选不可取消需求
		if (count > 0) {
			throw new RuntimeException("已经有人入选不可取消该需求");
		}
		boolean success = worthServiceprovider.getDemandService().cancel(demandId);
		return success;
	}

	@Transactional(rollbackFor = Exception.class)
	public String register(DemandRegisterDto demandRegisterDto) {
		//申请的需求是否存在
		Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegisterDto.getDemandId () );
		long now = System.currentTimeMillis();
		if(demand== null){
			return "申请的需求不存在";
		}
		if(demand.getStartAt()!=null && demand.getStartAt().getTime()<now){
			return "此需求已开始，请等下次再申请";
		}
		//判断用户和发布者是否是同一个人
		if(demand.getUserId ().equals ( demandRegisterDto.getUserId () )){
			return "不能申请自己发布的需求";
		}
		if(demand.getStatus().equals(DemandStatus.START.status)){
			return "此需求已结束报名，请等下次再申请";
		}
		//判断用户是否申请过此需求
		int checkDemandCount = worthServiceprovider.getDemandRegisterService().IsExitsRegisterByDemandIdAndUserId
				(demandRegisterDto.getDemandId (),demandRegisterDto.getUserId ());
		if(checkDemandCount != 0) {
			return "重复申请需求";
		}
		return worthServiceprovider.getDemandRegisterService().register(demandRegisterAction.DemandRegisterDtoToDemandRegister(demandRegisterDto))?null:"申请需求失败";
	}

	/* 编辑申请单 */
	public boolean registerEdit(DemandRegisterDto demandRegisterDto) {
		//判断订单号和用户是否正确
		int checkDemandCount = worthServiceprovider.getDemandRegisterService().IsExitsRegisterByIdAndUserId (demandRegisterDto.getId (),
				demandRegisterDto.getUserId () );
		if(checkDemandCount != 1) {
			throw new RuntimeException ( "用户不存在此需求" );
		}
		if(demandRegisterDto.getWage()!=null){
			demandRegisterDto.setWageLong(new Money(demandRegisterDto.getWage()).getCent());
		}
		return worthServiceprovider.getDemandRegisterService().getDemandRegisterMapper().updateDate(demandRegisterDto);
	}

	/* 取消申请 */
	@Transactional(rollbackFor = Exception.class)
	public boolean registerCancel(String id, String userId) {
		//判断订单号和用户是否正确
		int checkDemandCount = worthServiceprovider.getDemandRegisterService ().IsExitsRegisterByIdAndUserId (id,userId);
		if(checkDemandCount != 1) {
			throw new RuntimeException ( "用户不存在此需求" );
		}

		return worthServiceprovider.getDemandRegisterService().registerCancel(id);
	}

/*	@Transactional(rollbackFor = Exception.class)
	public String publishStop(String demandId, String userId) {
		Demand demand = worthServiceprovider.getDemandService().selectById(demandId);
		if(demand==null){
			return "该需求可能已经删除";
		}
		if(demand.getStatus().equals(DemandStatus.START.status) || !demand.getStatus().equals(DemandStatus.PUBLISHED.status)){
			return "该需求已结束报名";
		}
		if(!demand.getUserId().equals(userId)){
			return "你非发布者，不能结束报名";
		}
		demand.setStatus(DemandStatus.START.status);
		//届时所有未入选者状态变为3  未入选
		List<DemandRegister> demandRegisterList = worthServiceprovider.getDemandRegisterService ().getDemandRegisterListByDemandIdAndStatus ( demandId,0 );
		for (int i=0;i<demandRegisterList.size ();i++){
			demandRegisterList.get ( i ).setStatus ( 3 );
			worthServiceprovider.getDemandRegisterService ().updateById ( demandRegisterList.get ( i ) );
		}
		return worthServiceprovider.getDemandService().updateById(demand)?null:"结束报名失败";
	}*/

	/* 立即启动需求(申请方生成验证码) */
	@Transactional(rollbackFor = Exception.class)
	public int start(String userId, String registerId,double lon,double lat){
		DemandRegister register = worthServiceprovider.getDemandRegisterService ().selectById ( registerId );
		if (register == null){
		    throw new RuntimeException("该需求申请单不存在！");
        }
		DemandOrder order = worthServiceprovider.getDemandOrderService ().selectById ( register.getDemandOrderId () );
		if(order == null){
		    throw new RuntimeException("该需求申请未被确认");
        }
		if(register.getAddress() != null && !this.checkDistance(lon,lat,order.getLon(),order.getLat())){
			throw new RuntimeException ( "用户不在约定地点 获得验证码失败" );
		}
		if(!order.getUserId ().equals ( userId )){
			throw new RuntimeException ( "该需求申请非当前用户 非法进入" );
		}
		int code = VerificationCodeUtil.generator();
		register.setCode(code);
		if(!worthServiceprovider.getDemandRegisterService().updateById(register)){
			throw new RuntimeException ( "保存生成的验证码失败" );
		}
		//设置register表的状态为已启动
		boolean successReg = worthServiceprovider.getDemandRegisterService().start(registerId);
		boolean successOrd = worthServiceprovider.getDemandOrderService ().start(order);
		if (!successReg||!successOrd) {
			logger.error("启动需求时失败 registerId为"+registerId);
			throw new RuntimeException("启动需求失败");
		}
		return code;
	}

	public boolean registerPay(String demandOrderId, String userId, BigDecimal orderPrice) {
		boolean success = worthServiceprovider.getDemandOrderService().registerPay(demandOrderId, userId, orderPrice);
/*		if (!success) {
			*//*logger.error("支付后修改需求单数据失败");
			throw new RuntimeException("支付后修改需求单数据失败");*//*
			return false;
		}*/
		return success;
	}

	public boolean setDemandStop(String demandOrderId) {
		DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(demandOrderId);
		Demand demand = new Demand();
		demand.setStatus(DemandStatus.STOP.status);
		demand.setId(demandOrder.getDemandId());
		return demand.insertOrUpdate();
	}

	public boolean registerClose(String demandOrderId) {
		// 付款成功后，申请表状态改为已结束
		return worthServiceprovider.getDemandRegisterService().registerClose(demandOrderId);
	}

	public BigDecimal getTotalWageByDemandId(String demandOrderId) {
		DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(demandOrderId);
		BigDecimal wage = BigDecimal.valueOf(demandOrder.getWage() / 100);// 报酬
		Boolean eachWate = demandOrder.getEachWage();// wage是否为单位报酬
		List<DemandRegister> list = worthServiceprovider.getDemandRegisterService().getSuccessRegListByDemandOrderId(demandOrder.getId());
		return eachWate ? wage.multiply(new BigDecimal(list.size())) : wage;
	}

	public BigDecimal getRegisterWage(String registerId) throws Exception {
		DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService().selectById(registerId);
		DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(demandRegister.getDemandOrderId());
		if (demandOrder == null) {
			logger.error("取得需求报名者的报酬失败");
			throw new RuntimeException();
		}
		BigDecimal wage = BigDecimal.valueOf(demandOrder.getWage() / 100);// 报酬
		Boolean eachWage = demandOrder.getEachWage();// wage是否为单位报酬
		if (!eachWage) {
			List<DemandRegister> list = worthServiceprovider.getDemandRegisterService().getSuccessRegListByDemandOrderId(demandOrder.getId());
			BigDecimal size = new BigDecimal(list.size());
			wage = wage.divide(size, 2, BigDecimal.ROUND_DOWN);
		}
		return wage;
	}

/*
	@Transactional(rollbackFor = Exception.class)
	public boolean publishRefund(CreateRefundDto createRefundDto){
		DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(createRefundDto.getId());
		demandOrder.setStatus(DemandOrderStatus.REFUNDMENT.status);
		//更改状态并创建退款表
		return worthServiceprovider.getDemandOrderService().updateById(demandOrder) && refundAction.create(createRefundDto.getDescription(),
				createRefundDto.getBail(), createRefundDto.getPayWay(), createRefundDto.getId(), "DemandOrder",
				createRefundDto.getAmount(), createRefundDto.getUserId())!=null;
	}
*/

	public void getPublishListHash(Map<String, Object> map, List<Demand> list) {
		if (list == null || list.size() <= 0)
			return;
		List<String> demandIds = list.stream().map(Demand::getId).collect(Collectors.toList());
		List<DemandRegister> demandRegisters = worthServiceprovider.getDemandRegisterService().getRegList(demandIds);
		Map<String, Long> registerCountHash = demandRegisters.stream()
				.collect(groupingBy(DemandRegister::getDemandId, Collectors.counting()));
		Map<String, Long> registerSuccessCountHash = demandRegisters.stream()
				.filter(e -> e.getStatus().equals(DemandRegisterStatus.SUCCESS.status))
				.collect(groupingBy(DemandRegister::getDemandId, Collectors.counting()));
		map.put("registerCountHash", registerCountHash);
		map.put("registerSuccessCountHash", registerSuccessCountHash);
	}

	public boolean registerRefuse(String demandRegisterId) {
		return   worthServiceprovider.getDemandRegisterService().changeRegister (demandRegisterId);
	}

	public void getRegisterListHash(Map<String, Object> map, List<DemandRegister> demandRegisters) {
		if (demandRegisters == null || demandRegisters.size() <= 0)
			return;
		Map<String, Long> registerCountHash = demandRegisters.stream()
				.collect(groupingBy(DemandRegister::getDemandId, Collectors.counting()));
		Map<String, Long> registerSuccessCountHash = demandRegisters.stream()
				.filter(e -> e.getStatus().equals(DemandRegisterStatus.SUCCESS.status))
				.collect(groupingBy(DemandRegister::getDemandId, Collectors.counting()));
		List<Demand> demands = worthServiceprovider.getDemandService()
				.selectBatchIds(demandRegisters.stream().map(DemandRegister::getDemandId).collect(toList()));
		if (demands != null && demands.size() > 0) {
			for (Demand demand : demands) {
				demand.setImagesUrl(
						addImgUrlPreUtil.addImgUrlPres(demand.getImagesUrl(), AliyunBucketType.ActivityBucket));
				demand.setDetailsImagesUrl(addImgUrlPreUtil.addImgUrlPres(demand.getDetailsImagesUrl(),
						AliyunBucketType.ActivityBucket));
			}
		}
		Map<String, Demand> demandHash = demands.stream().collect(Collectors.toMap(Demand::getId, e -> e));
		map.put("registerCountHash", registerCountHash);
		map.put("registerSuccessCountHash", registerSuccessCountHash);
		map.put("demandHash", demandHash);
	}

/*	public boolean registerDrop(String userId, String registerId) {
		//检验用户是否是申请人
		int count = worthServiceprovider.getDemandRegisterService().IsExitsRegisterByIdAndUserId (registerId,userId);
		if(count!=1){
			throw  new RuntimeException ( "用户不存在此需求" );
		}
		//删除数据库demand_order表数据
		DemandRegister demandRegister = worthServiceprovider.getDemandRegisterService ().selectById ( registerId );
		DemandOrder demandOrder = worthServiceprovider.getDemandOrderService ().getOrderByUserIdAndDemandId ( userId, demandRegister.getDemandId () );
		worthServiceprovider.getDemandOrderService ().deleteById ( demandOrder.getId () );
		//更改demand_register表数据
		return worthServiceprovider.getDemandRegisterService().registerDrop(userId, registerId);
	}*/

//	@Transactional
//	public void checkStart(String demandOrderId) throws Exception {
//		DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(demandOrderId);
//		List<DemandRegister> registers = worthServiceprovider.getDemandRegisterService().getSuccessRegListByDemandOrderId(demandOrderId);
//		boolean hasStart = false;
//		for (DemandRegister demandRegister : registers) {
//			boolean isStart = demandRegister.getStatus().equals(DemandRegisterStatus.START.status);
//			boolean isSuccess = demandRegister.getStatus().equals(DemandRegisterStatus.SUCCESS.status);
//			boolean isDrop = demandRegister.getStatus().equals(DemandRegisterStatus.DROP.status);
//			//有入选的申请者
//			if (isSuccess) {
//				//修改申请状态为超时未启动
//				demandRegister.setStatus(DemandRegisterStatus.TIMEOUT.status);
//				worthServiceprovider.getDemandRegisterService().updateById(demandRegister);
//				//扣除两分
//				settlementAction.settlementCredit("DemandOrder", demandOrderId, demandRegister.getUserId(), -2);
//				// 修改demandOrder的status为超时未启动
//				demandOrder.setStatus(DemandOrderStatus.TIMEOUT.status);
//				worthServiceprovider.getDemandOrderService().updateById(demandOrder);
//			}
//			//已入选放弃入选
//			if (isDrop) {
//				settlementAction.settlementCredit("DemandOrder", demandOrderId, demandRegister.getUserId(), -2);
//			}
//			//已经启动需求 已经有人启动需求不进行退款
//			if (isStart) {
//				hasStart = true;
//			}
//		}
//		if (!hasStart) {// 入选者全部取消，退还费用
//			settlementAction.settlementAmountRightNow("入选者全部取消，退还费用", "DemandOrder", demandOrderId,
//					demandOrder.getUserId(), Money.CentToYuan(demandOrder.getBail()).getAmount());
//		} else {
//			demandOrder.setStatus(DemandOrderStatus.START.status);
//			worthServiceprovider.getDemandOrderService().updateById(demandOrder);
//		}
//	}

	@Transactional
	public void checkStart(String demandOrderId) throws Exception {
		DemandOrder demandOrder = worthServiceprovider.getDemandOrderService().selectById(demandOrderId);
		List<DemandRegister> registers = worthServiceprovider.getDemandRegisterService ().getListByDemandId(demandOrder.getDemandId ());
		boolean hasStart = false;
		for (DemandRegister demandRegister : registers) {
			boolean isStart = demandRegister.getStatus().equals(DemandRegisterStatus.START.status);
			boolean isSuccess = demandRegister.getStatus().equals(DemandRegisterStatus.SUCCESS.status);
			boolean isDrop = demandRegister.getStatus().equals(DemandRegisterStatus.DROP.status);
			//有入选的申请者
			if (isSuccess) {
				//修改申请状态为超时未启动
				demandRegister.setStatus(DemandRegisterStatus.TIMEOUT.status);
				worthServiceprovider.getDemandRegisterService().updateById(demandRegister);
				//扣除两分
				settlementAction.settlementCredit("DemandOrder", demandOrderId, demandRegister.getUserId(), -2);
				// 修改demandOrder的status为超时未启动
				demandOrder = worthServiceprovider.getDemandOrderService().selectById(demandRegister.getDemandOrderId ());
				demandOrder.setStatus(DemandOrderStatus.TIMEOUT.status);
				worthServiceprovider.getDemandOrderService().updateById(demandOrder);
			}
			//已入选放弃入选
			if (isDrop) {
				settlementAction.settlementCredit("DemandOrder", demandOrderId, demandRegister.getUserId(), -2);
			}
			//已经启动需求 已经有人启动需求不进行退款
			if (isStart) {
				hasStart = true;
			}
		}
		if (!hasStart) {// 入选者全部取消，退还费用
			settlementAction.settlementAmountRightNow("入选者全部取消，退还费用", "DemandOrder", demandOrderId,
					demandOrder.getUserId(), Money.CentToYuan(demandOrder.getBail()).getAmount());
		}
	}


	public void checkSuccess(String demandOrderId) throws Exception {
		demandOrderAction.checkSuccess(demandOrderId);
	}

	public List<Map<String, Object>> orderList(String demandId, String userId, Page<DemandOrder> page) {
		List<Map<String, Object>> orderMapList = worthServiceprovider.getDemandOrderService().getDemandOrderListBydemandId(demandId, page);
		if (orderMapList != null) {
			for (Map<String, Object> map : orderMapList) {
				String demandOrderId = (String) map.get("id");// 需求单id
				// 根据需求单id查询申请人的userId
				String regUserId = worthServiceprovider.getDemandRegisterService().getUserIdByDemandOrderId(demandOrderId);
				map.put("registerUserId", regUserId);
			}
		}
		// 如果传入有userId,只返回该userId对应的需求详情
		List<Map<String, Object>> orderMapListByUserId = new ArrayList<>();
		if (orderMapList != null) {
			for (Map<String, Object> map : orderMapList) {
				if (userId.equals(map.get("registerUserId"))) {
					orderMapListByUserId.add(map);
					return orderMapListByUserId;
				}
			}
		}

		return orderMapList;
	}

	public boolean checkHasUnComplete(String userId) {
		int count = worthServiceprovider.getDemandService().getPublishCount(userId);
		if (count > 0) {
			return true;
		}

		// 查询该用户申请表中状态为“已入选”或者“确认出席，准备校验验证码”
		List<Integer> registerStatusList = new ArrayList<Integer>();
		registerStatusList.add(DemandRegisterStatus.SUCCESS.status);
		registerStatusList.add(DemandRegisterStatus.START.status);
		List<DemandRegister> list = worthServiceprovider.getDemandRegisterService().getUnCompleteList(userId,registerStatusList);
		List<String> demandIds = list.stream().map(DemandRegister::getDemandId).collect(toList());
		if (demandIds != null && demandIds.size() > 0) {
			List<Demand> demands = worthServiceprovider.getDemandService().selectBatchIds(demandIds);
			if (demands != null && demands.size() > 0) {
				for (Demand demand : demands) {
					// 时间在结束时间之前并且状态是“已发布”
					if (demand.getEndAt().getTime() >= new Date().getTime()
							&& demand.getStatus().equals(DemandStatus.PUBLISHED.status)) {
						return true;
					}
				}
			}
		}
		return false;

	}

	@Transactional
	public boolean clean(String userId) throws Exception {
		// 删除发布的需求
		boolean cleanDemand = worthServiceprovider.getDemandService().cleanDemand(userId);

		// 先删除结算表中的数据
		List<DemandOrder> list = worthServiceprovider.getDemandOrderService().getOrderListByUserId(userId);
		List<String> demandOrderIds = list.stream().map(DemandOrder::getId).collect(toList());
		boolean cleanDemandOrder = true;
		boolean cleanSettlment = true;
		// 没有数据的话直接返回true
		if (demandOrderIds != null && demandOrderIds.size() > 0) {
			// 删除结算表和子表
			List<Settlement> settlementList = worthServiceprovider.getSettlementService().getSettlementListByTypeAndId("DemandOrder",
					demandOrderIds);
			List<String> settlementIds = settlementList.stream().map(Settlement::getId).collect(toList());
			cleanSettlment = worthServiceprovider.getSettlementService().deleteSettlementListByTypeAndId("DemandOrder", demandOrderIds)
					&& settlementAction.clean(settlementIds);
			// 再删除需求单表
			cleanDemandOrder = worthServiceprovider.getDemandOrderService().cleanDemandOrder(userId);
		}

		// 删除申请表
		boolean cleanDemandRegister = worthServiceprovider.getDemandRegisterService().cleanDemandRegister(userId);
		boolean success = cleanDemand && cleanDemandRegister && cleanDemandOrder && cleanSettlment;
		if (success) {
			return success;
		} else {
			logger.error("清除该用户需求的数据失败");
			throw new RuntimeException();
		}
	}

	public Refund refundMessage(String demandOrderId) {
		return worthServiceprovider.getRefundService().getRefundByRelIdAndRelType(demandOrderId, "DemandOrder");
	}

	public boolean arbitrationStatus(String demandOrderId) {
		DemandOrder demandOrder = new DemandOrder();
		demandOrder.setId(demandOrderId);
		demandOrder.setStatus(DemandOrderStatus.ARBITRATION.status);
		return demandOrder.insertOrUpdate();
	}

	public Demand selectById(String id) {
		return worthServiceprovider.getDemandService().selectById(id);
	}

	/* 吧DemandPublishDto赋给Demand对象**/
	public Demand DemandPublishDtoToDemand(Demand demand,DemandPublishDto demandPublishDto){
		demand.setId ( demandPublishDto.getId () );
		demand.setUserId(demandPublishDto.getUserId());
		demand.setTitle(demandPublishDto.getTitle());
		demand.setDemandType(demandPublishDto.getDemandType());
		demand.setDemandLabel(demandPublishDto.getDemandLabel());
		demand.setOpenEnded(demandPublishDto.getOpenEnded());
		if(demandPublishDto.getStartAt()!=null){
			demand.setStartAt(new Date(demandPublishDto.getStartAt()));
		}
		if(demandPublishDto.getEndAt()!=null){
			demand.setEndAt(new Date(demandPublishDto.getEndAt()));
		}
		demand.setUnit(demandPublishDto.getUnit());
		demand.setAbout(demandPublishDto.getAbout());
		demand.setAmount(demandPublishDto.getAmount());
		demand.setObj(demandPublishDto.getObj());
		demand.setObjList(demandPublishDto.getObjList());
		demand.setAddress(demandPublishDto.getAddress());
		demand.setLon(demandPublishDto.getLon());
		demand.setLat(demandPublishDto.getLat());
		demand.setDescription(demandPublishDto.getDescription());
		demand.setImagesUrl(demandPublishDto.getPic());
		demand.setDetailsImagesUrl(demandPublishDto.getMerchantId());
		demand.setOrderIds(demandPublishDto.getOrderIds());
		demand.setOrderPrice(new Money (demandPublishDto.getOrderPrice()).getCent());
		demand.setPickUp(demandPublishDto.getPickUp());
		if(demandPublishDto.getWage ()!=null) {
			demand.setWage ( new Money ( demandPublishDto.getWage () ).getCent () );
		}
		demand.setEachWage(demandPublishDto.getEachWage());
		demand.setBail(new Money(demandPublishDto.getBail()).getCent());
		demand.setPay(demandPublishDto.getPay());
		demand.setStatus(DemandStatus.PUBLISHED.status);
		return  demand;
	}

	/* 验证距离*/
	public boolean checkDistance(double lon,double lat,BigDecimal userlon,BigDecimal userlat) {
		Double distance = DistrictUtil.calcDistance(new BigDecimal ( lat),new BigDecimal (lon),userlat,userlon);
		return (distance * 1000) <= 300 ? true : false;
	}


	/* 发布者输入验证码 */
	public int checkSendInputCode(String registerId, Integer code, UserGeo userGeo){
	    Boolean success;
        DemandRegister demandRegister  = worthServiceprovider.getDemandRegisterService().selectById ( registerId );
        //验证当前用户是否是发布者
        Demand demand = worthServiceprovider.getDemandService ().selectById ( demandRegister.getDemandId () );
        if (demand!=null) {
			if (!demand.getUserId().equals(userGeo.getUserId())) {
				return 0;
			}
		}
        DemandOrder order = worthServiceprovider.getDemandOrderService ().selectById ( demandRegister.getDemandOrderId () );

        if (order!=null) {
			Double distance = DistrictUtil.calcDistance(userGeo.getLat(), userGeo.getLon(), order.getLat().doubleValue(), order.getLon().doubleValue());
			if (order.getAddress() != null && !((distance * 1000) <= 300)) {
				return 1;
			}
		}
        if (demandRegister.getTimes() >= 3) {
        	return 2;
        }
        if (!demandRegister.getCode().equals(code)) {
            success = worthServiceprovider.getDemandRegisterService().updateVerificationCode(registerId, demandRegister.getTimes() + 1);
            if(success)
            	return 3;
            else
            	return 4;
        }else {
			DemandOrder demandOrder = new DemandOrder ();
			demandOrder.setId ( demandRegister.getDemandOrderId () );
			demandOrder.setStatus ( DemandOrderStatus.SUCCESS.status );
			demandOrder.setValidationStatus ( true );
			success = demandOrderAction.updateById ( demandOrder );
			if(success)
				return 5;
			else
				return 6;
		}
    }

}
