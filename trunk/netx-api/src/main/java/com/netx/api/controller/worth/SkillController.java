package com.netx.api.controller.worth;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.wz.dto.common.CommonCheckDto;
import com.netx.fuse.biz.worth.SkillFuseAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.utils.DistrictUtil;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.json.ApiCode;
import com.netx.common.wz.dto.skill.*;
import com.netx.utils.money.Money;
import com.netx.worth.biz.settlement.SettlementAction;
import com.netx.worth.enums.SkillOrderStatus;
import com.netx.worth.model.Refund;
import com.netx.worth.service.WorthServiceprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.api.controller.BaseController;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.common.CommonSearchDto;
import com.netx.common.wz.dto.common.CreateRefundDto;
import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.skill.SkillAction;
import com.netx.worth.model.Skill;
import com.netx.worth.model.SkillOrder;
import com.netx.worth.model.SkillRegister;
import com.netx.worth.util.PageWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Api(description = "技能模块")
@RequestMapping("/wz/skill")
@RestController
public class SkillController extends BaseController {
        private Logger logger = LoggerFactory.getLogger(SkillController.class);
        @Autowired
        private SkillAction skillAction;
        @Autowired
        private SkillFuseAction skillFuseAction;
        @Autowired
        private WorthServiceprovider worthServiceprovider;
        @Autowired
        private ScoreAction scoreAction;
        @Autowired
        private SettlementAction settlementAction;
        @Autowired
        private UserAction userAction;

        @Autowired
        private RedisInfoHolder redisInfoHolder;

        private RedisCache redisCache;

        private RedisCache clientRedis() {
            redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
            return redisCache;
        }
        
        @ApiOperation(value = "技能详情")
        @PostMapping(value = "/detail")
        public JsonResult detail(String id,HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            if(StringUtils.isEmpty(id)){
                return JsonResult.fail("技能Id不能为空");
            }
            BigDecimal lat = new BigDecimal(getLat(request));
            BigDecimal lon = new BigDecimal(getLon(request));
            try {
                Map<String,Object> map  = skillFuseAction.getDtail(userId,id,lat,lon);
                if(map!=null) {
                    return JsonResult.successJsonResult(map);
                }
                return JsonResult.fail("技能列表失败");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("查看技能详情失败");
            }
        }

        
        @ApiOperation(value = "预约单详情")
        @PostMapping(value = "/registerDetail")
        public JsonResult registerDetail(HttpServletRequest request,String skillId,String reId) {
            BigDecimal lat;
            BigDecimal lon;
            Map<String,Object> map = new HashMap();
            try {
                lat=new BigDecimal(getLat(request));
                lon=new BigDecimal(getLon(request));
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            try {
                map  = skillFuseAction.getReList(reId,lat,lon,skillId);
             if(map!=null) {
                    return JsonResult.successJsonResult(map);
                }
                return JsonResult.fail("查看预约单详情失败");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                return JsonResult.fail("查看预约单详情失败");
            }
        }

        
        @ApiOperation(value = "我附近的技能")
        @PostMapping(value = "/nearList", consumes = {"application/json"})
        public JsonResult nearList(@Validated @RequestBody CommonSearchDto commonSearchDto, BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            if (commonSearchDto.getLength() == null) return JsonResult.fail("length距离不能为空");
            Page<Skill> page = new Page<>(commonSearchDto.getCurrentPage(), commonSearchDto.getSize());
            Map<String, Object> map = null;
            try {
                map = skillAction.nearList(commonSearchDto.getUserId(), commonSearchDto.getLon(), commonSearchDto.getLat(), commonSearchDto.getLength(), page);
                return JsonResult.successJsonResult(PageWrapper.wrapper(page.getTotal(), map));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("查看我附近的技能失败");
            }
        }
        
        
        @ApiOperation(value = "发布技能")
        @PostMapping(value = "/publish", consumes = {"application/json"})
        public JsonResult publish(@Validated @RequestBody SkillPublishDto skillPublishDto, BindingResult bindingResult, HttpServletRequest request) {
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            try {
                skillPublishDto.setUserId(getUserId(skillPublishDto.getUserId(),request));
                if(StringUtils.isEmpty(skillPublishDto.getUserId())){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            BigDecimal lon = new BigDecimal(getLon(request));
            BigDecimal lat = new BigDecimal(getLat(request));
            try {
                boolean success = skillFuseAction.publish(skillPublishDto,lon,lat);
                if(success){
                    scoreAction.addScore(skillPublishDto.getUserId(), StatScoreEnum.SS_PUBLISH_WORTH);
                    return JsonResult.success(skillPublishDto.getId());
                }
                return JsonResult.fail( "不能发布相同的技能");
            } catch (RuntimeException ex) {
                return JsonResult.fail(ex.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("发布技能失败");
            }
        }
        
        
        @ApiOperation(value = "编辑技能")
        @PostMapping(value = "/edit", consumes = {"application/json"})
        public JsonResult edit(@Validated @RequestBody SkillPublishDto skillPublishDto, BindingResult bindingResult, HttpServletRequest request) {
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            try {
                skillPublishDto.setUserId(getUserId(skillPublishDto.getUserId(),request));
                if(StringUtils.isEmpty(skillPublishDto.getUserId())){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            BigDecimal lon = new BigDecimal(getLon(request));
            BigDecimal lat = new BigDecimal(getLat(request));
            try {
                boolean success = skillAction.edit(skillAction.SkillPublishDtoToSkill(skillPublishDto,lon,lat));
                return getResult(success, "编辑技能失败");
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("编辑技能失败");
            }
        }

        
        @ApiOperation(value = "发布者取消技能，该方法会直接取消技能，但是技能单不影响")
        @PostMapping(value = "/publishCancel")
        public JsonResult publishCancel(String id, HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            if (!StringUtils.hasText(id)) {
                return JsonResult.fail("ID不能为空");
            }
            try {
                boolean success = skillAction.publishCancel(id, userId);
                if (success){
                    scoreAction.addScore(userId, -StatScoreEnum.SS_PUBLISH_WORTH.score());
                }
                return getResult(success, "发布者取消技能失败");
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("发布者取消技能失败");
            }
        }

        
        @ApiOperation(value = "发布者结束技能")
        @PostMapping(value = "/publishStop")
        public JsonResult publishStop(String id, HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            if (!StringUtils.hasText(id)) {
                return JsonResult.fail("技能ID不能为空");
            }
            try {
                boolean success = skillAction.publishStop(id, userId);
                return getResult(success, "发布者结束技能失败");
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("发布者结束技能失败");
            }
        }

        
        @ApiOperation(value = "发布者取消技能单")
        @PostMapping(value = "/publishCancelOrder")
        public JsonResult publishCancelOrder(HttpServletRequest request, String id) {
            String userId = super.getUserIdInWorth(request);
            if (!StringUtils.hasText(id)) {
                return JsonResult.fail("ID不能为空");
            }
            try {
                boolean success = skillAction.publishCancelOrder(id, userId);
                //TODO 扣除发布者的信用值，如果需要托管费的退还给入选者
                SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectById(id);
                SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillOrder.getSkillRegisterId());
                //扣除信用
                boolean b = skillFuseAction.checkOrderStart(skillOrder.getId());
                logger.info("扣除发布者信用值成功");
                boolean b1 = false ;
                if(skillRegister.getPay()){
                    SkillDingDto skillDingDto = new SkillDingDto();
                    Long bail = skillRegister.getBail();
                    skillDingDto.setBail(BigDecimal.valueOf(bail));
                    skillDingDto.setSkillRegisterId(skillRegister.getId());
                    skillDingDto.setUserId(skillRegister.getUserId());
                    b1 = skillFuseAction.publishCancleRO(skillDingDto);
                    logger.info("返还入选者托管费成功");
                }
                // 通知入选者
                skillFuseAction.sendMessageCancelOrder(userId,skillRegister.getId());
                return getResult(success, "发布者取消技能单失败");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("发布者取消技能单失败");
            }
        }

        
        @ApiOperation(value = "预约技能")
        @PostMapping(value = "/register", consumes = {"application/json"})
        public JsonResult register(@Validated @RequestBody SkillRegisterDto skillRegisterDto, BindingResult bindingResult, HttpServletRequest request) {
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            try {
                skillRegisterDto.setUserId(getUserId(request));
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            try {

                Map<String,Object> map = skillFuseAction.register(skillRegisterDto);
                if(map.isEmpty()){
                    return JsonResult.fail("发出预约失败");
                }
                return JsonResult.successJsonResult(map);
            }catch (RuntimeException ex) {
                return JsonResult.fail(ex.getMessage());
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("发出预约失败");
            }
        }

        
        @ApiOperation(value = "报名者修改预约")
        @PostMapping(value = "/registerEdit", consumes = {"application/json"})
        public JsonResult registerEdit(@Validated @RequestBody SkillRegisterDto skillRegisterDto, BindingResult bindingResult, HttpServletRequest request) {
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            try {
                skillRegisterDto.setUserId(getUserId(request));
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            try {
                boolean success = skillAction.registerEdit(skillRegisterDto);
                return getResult(success, "报名者修改预约失败");
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("报名者修改预约失败");
            }
        }

        
        @ApiOperation(value = "标记报名者已托管费用，必须托管付款成功后才能调用该方法，另外支付对象不要填，意思是支付给平台")
        @PostMapping(value = "/registerDeposit", consumes = {"application/json"})
        public JsonResult registerDeposit(@Validated @RequestBody SkillRegisterDepositDto skillRegisterDepositDto, BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            try {
                boolean success = skillFuseAction.registerDeposit(skillRegisterDepositDto.getSkillRegisterId(), skillRegisterDepositDto.getUserId(), skillRegisterDepositDto.getBail(), skillRegisterDepositDto.getPayWay());
                return getResult(success, "标记报名者已托管费用失败");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
                return JsonResult.fail("标记报名者已托管费用失败");
            }
        }

        
        @ApiOperation(value = "发布者接受预约")
        @PostMapping(value = "/publishAccept")
        public JsonResult publishAccept(String id, String skillRegisterId, HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            if (!StringUtils.hasText(skillRegisterId)) {
                return JsonResult.fail("预约表ID不能为空");
            }
            if (!StringUtils.hasText(id)) {
                return JsonResult.fail("技能表ID不能为空");
            }
            try {
                //定时器
                Map<String,Object> map = skillFuseAction.publishAccept(id, userId, skillRegisterId);
                if(map!=null && !map.isEmpty()) {
                    return JsonResult.successJsonResult(map);
                }
                return JsonResult.fail("发布者接受预约失败");
            } catch (RuntimeException ex) {
                return JsonResult.fail(ex.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);  //写入日志
                return JsonResult.fail("发布者接受预约失败");
            }
        }

        
        @ApiOperation(value = "发布者启动预约前生成验证码")
        @PostMapping(value = "/publishGeneratCode")
        public JsonResult publishGeneratCode(String reId,String orderId, HttpServletRequest request) {
            UserGeo userGeo = null;
            try {
                userGeo=getGeoFromRequest(request);
            }catch (Exception e){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            try {
                SkillOrder skillOrder = skillFuseAction.publishGeneratCode(reId,orderId,userGeo.getLat(),userGeo.getLon());


                return JsonResult.success().addResult("code", skillOrder.getCode());
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("发布者启动预约前生成验证码失败");
            }
        }

        
        @ApiOperation(value = "预约者取消预约")
        @PostMapping(value = "/registerCancel")
        public JsonResult registerCancel(String id, HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            try {
                boolean success = skillFuseAction.registerCancel(id, userId);
                return getResult(success, "预约者取消预约失败");
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("预约者取消预约失败");
            }
        }

        
        @ApiOperation(value = "预约者输入验证码")
        @PostMapping(value = "/verificationCode")
        public JsonResult verificationCode(String skillRegisterId, Integer code, HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            if (!StringUtils.hasText(skillRegisterId)) {
                return JsonResult.fail("技能预约单ID不能为空");
            }
            if (code == null) {
                return JsonResult.fail("验证码不能为空");
            }
            UserGeo userGeo = null;
            try {
                userGeo=getGeoFromRequest(request);
            }catch (Exception e){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            try {
                SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
                if (skillRegister.getTimes() >= 3) {
                    return JsonResult.fail("重试次数过多");
                }
                Double distance = (DistrictUtil.calcDistance(skillRegister.getLat().doubleValue(), skillRegister.getLon().doubleValue(), userGeo.getLat(), userGeo.getLon()));
                boolean validationStatus = (distance * 1000) <= 100;
                Skill skill = worthServiceprovider.getSkillService().selectById(skillRegister.getSkillId());
                if(!validationStatus && !skill.getObj().equals(2)){
                    return JsonResult.fail("你与预约距离超出100米，请前往预约地点");
                }
                SkillOrder skillOrder = worthServiceprovider.getSkillOrderService().selectByRegisterId(skillRegisterId);
                // 验证码是否超过预约30分钟，超过则入选者扣除信用分且托管费用归发布者所有\
                    long diffTime = (new Date().getTime() / (1000 * 60)) - (skillRegister.getStartAt().getTime() / (1000 * 60));
                    boolean diffStatus = diffTime > 30;
                    if (diffStatus) {
                        if(skillOrder.getStatus()<=1) {
                            //修改预约单和订单状态
                            worthServiceprovider.getSkillRegisterService().expire(skillRegisterId);
                            logger.info("超出30分钟，预约单状态更改为已过期");
                            worthServiceprovider.getSkillOrderService().updateOrderStatus(skillOrder, SkillOrderStatus.FAIL.status);
                            logger.info("超出30分钟，技能订单更改为已失败");
                            //托管资金转给发布者，预约者扣除信用值
                            settlementAction.settlementAmountRightNow("发布者到场，预约者缺席，立即结算报酬", "SkillOrder", skillOrder.getId(), skill.getUserId(), Money.CentToYuan(skillOrder.getAmount()).getAmount());
                            settlementAction.settlementCredit("SkillOrder", skillOrder.getId(), skillRegister.getUserId(), -2);//信用分-2
                            User user = userAction.getUserService().selectById(userId);
                            user.setCredit(user.getCredit() - 2);
                            user.updateById();
                            logger.info("扣除入选者信用值2分，托管金归还给发布者");
                            return JsonResult.fail("超过30分钟未验证验证码，扣除预约者信用值2分，托管资金归发布者");
                        }
                    }
                if (!skillOrder.getCode().equals(code)) {
                    worthServiceprovider.getSkillRegisterService().updateVerificationCode(userId, skillRegisterId, skillRegister.getTimes() + 1);
                    return JsonResult.fail("验证码错误");
                } else {
                    boolean success = worthServiceprovider.getSkillRegisterService().codeValidated(userId, skillRegisterId);
                    return getResult(success, "预约者输入验证码失败");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("预约者输入验证码失败");
            }
        }

        
        @ApiOperation(value = "预约者同意付款给发布者，支付完成后才可调用")
        @PostMapping(value = "/registerPay")
        public JsonResult registerPay(String registerId, HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            if (!StringUtils.hasText(registerId)) {
                return JsonResult.fail("技能预约单ID不能为空");
            }
            try {
              Map<String ,Object> map = skillFuseAction.registerPay(registerId, userId);
                if(map!=null) {
                    return JsonResult.successJsonResult(map);
                }
                return JsonResult.fail("预约者同意付款给发布者失败");
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("预约者同意付款给发布者失败");
            }
        }

        
        @ApiOperation(value = "预约者申请退款") 
        @PostMapping(value = "/registerRefund")
        public JsonResult registerRefund(@Validated @RequestBody CreateRefundDto createRefundDto, BindingResult bindingResult, HttpServletRequest request) {
            Map<String,Object> map = new HashMap<>();
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            try {
                createRefundDto.setUserId(getUserId(request));
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            try {
                Refund refund =skillFuseAction.registerRefund(createRefundDto);
                if(refund!=null) {
                    return JsonResult.success().addResult("refundId", refund.getId());
                }
                return JsonResult.fail("预约者申请退款失败");
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("预约者申请退款失败");
            }
        }

        
        @ApiOperation(value = "发布者拒绝预约者的退款申请")
        @PostMapping(value = "/publishRejectRefund")
        public JsonResult registerRejectRefund(String refundId, HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            if (!StringUtils.hasText(refundId)) {
                return JsonResult.fail("退款表ID不能为空");
            }
            try {
//                boolean success = skillAction.publishRejectRefund(refundId, userId);
                boolean success = skillFuseAction.publishRejectRefund(refundId, userId);
                return getResult(success, "发布者拒绝预约者的退款申请失败");
            } catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("发布者拒绝预约者的退款申请失败");
            }
        }

        
        @ApiOperation(value = "发布者同意预约者的退款申请")
        @PostMapping(value = "/publishAcceptRefund")
        public JsonResult registerAcceptRefund(String refundId, HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            if (!StringUtils.hasText(refundId)) {
                return JsonResult.fail("退款表ID不能为空");
            }
            try {
                boolean success = skillFuseAction.publishAcceptRefund(refundId, userId);
                return getResult(success, "发布者同意预约者的退款申请失败");
            } catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("发布者同意预约者的退款申请失败");
            }
        }

        
        @ApiOperation(value = "验证距离(确认出席)")
        @PostMapping(value = "/check", consumes = {"application/json"})
        public JsonResult check(@Validated @RequestBody CommonCheckDto commonCheckDto,HttpServletRequest request) {
            UserGeo userGeo = null;
            try {
                userGeo = getGeoFromRequest(request);
                if(StringUtils.isEmpty(userGeo.getUserId())){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            try {
                boolean success = skillAction.check(commonCheckDto,userGeo.getUserId(),userGeo.getLon(),userGeo.getLat());
                return getResult(success, "验证距离(确认出席)失败");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("验证距离(确认出席)失败");
            }
        }

        
        @ApiOperation(value = "定时任务：未接受或预约者设定的时间开始后，发布者均未响应，则预约失败")
        @PostMapping(value = "/checkSkillRegisterAccept")
        public JsonResult checkSkillRegisterAccept(String skillRegisterId, HttpServletRequest request) {
            SkillRegister skillRegister = worthServiceprovider.getSkillRegisterService().selectById(skillRegisterId);
            SkillDingDto skillDingDto = new SkillDingDto();
            skillDingDto.setSkillRegisterId(skillRegisterId);
            skillDingDto.setUserId(getUserIdInWorth(request));
            skillDingDto.setBail(new BigDecimal(skillRegister.getBail()));
            if (!StringUtils.hasText(skillDingDto.getSkillRegisterId())) {
                return JsonResult.fail("预约表ID不能为空");
            }
            if (skillDingDto.getBail() == null) {
                return JsonResult.fail("退款金额不能为空");
            }
            try {
                boolean success = skillFuseAction.checkSkillRegisterAccept(skillDingDto.getSkillRegisterId());
                return getResult(success, "启动定时任务：未接受或预约者设定的时间开始后，发布者均未响应，则预约失败失败");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("定时任务：未接受或预约者设定的时间开始后，发布者均未响应，则预约失败失败");
            }

        }

        
        @ApiOperation(value = "定时任务：发布者取消预约或在预约开始时仍未点击同意启动按钮，信用值扣5分")
        @PostMapping(value = "/checkOrderStart")
        public JsonResult checkOrderStart(String skillOrderId) {
            if (!StringUtils.hasText(skillOrderId)) {
                   return JsonResult.fail("预约单ID不能为空");
               }
               try {
                   boolean success = skillFuseAction.checkOrderStart(skillOrderId);
                   return getResult(success,"启动定时任务：发布者取消预约或在预约开始时仍未点击同意启动按钮，信用值扣5分失败");
               } catch (Exception e) {
                   logger.error(e.getMessage(), e);
                   return JsonResult.fail("启动定时任务：发布者取消预约或在预约开始时仍未点击同意启动按钮，信用值扣5分失败"); 
            } 
        }


        @ApiOperation(value = "定时任务：检测发布者是否启动预约")
        @PostMapping(value = "/checkRegister")
        public JsonResult checkRegister(String skillOrderId){
            if(!StringUtils.hasText(skillOrderId)){
                return JsonResult.fail("预约单ID不能为空");
            }
            try {
                skillFuseAction.checkRegister(skillOrderId);
                return JsonResult.success();
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                return JsonResult.fail(e.getMessage());
            }

        }
        
        @ApiOperation(value = "定时任务：验证码、距离是否通过")
        @PostMapping(value = "/checkSuccess")
        public JsonResult checkSuccess(String skillOrderId) {
            if (!StringUtils.hasText(skillOrderId)) {
                return JsonResult.fail("预约单ID不能为空");
               }
               try {
                   skillFuseAction.checkSuccess(skillOrderId);
                   return JsonResult.success();
               } catch (Exception e) {
                   logger.error(e.getMessage(), e);
                   return JsonResult.fail(e.getMessage()); 
            } 
        }

        
        @ApiOperation(value = "定时任务：是否评价") 
        @PostMapping(value = "/checkEvaluate")
        public JsonResult checkEvaluate(String skillOrderId) {
            if (!StringUtils.hasText(skillOrderId)) {
                return JsonResult.fail("预约单ID不能为空"); 
            }
            try{
                skillFuseAction.checkEvaluate(skillOrderId);
                return JsonResult.success();
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                return JsonResult.fail(e.getMessage());
            }
           }

           
        @ApiOperation(value = "技能列表", notes = "通用的技能列表接口，根据不同的条件返回")
        @PostMapping(value = "/list")
        public JsonResult list(@Validated @RequestBody SkillSearchDto skillSearchDto, BindingResult bindingResult, HttpServletRequest request) {
            if (bindingResult.hasErrors()) {

                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            try {
                skillSearchDto.setLat(getLat(request));
                skillSearchDto.setLon(getLon(request));
                List<SkillListDto> list = skillFuseAction.list(skillSearchDto);
                if (list != null) {
                    return JsonResult.success().addResult("list", list);
                }
                return JsonResult.fail("获取技能列表失败！");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("获取技能列表异常！");
            }
        }

        
        @ApiOperation(value = "预约（已接受/未接受/已失败）列表")
        @PostMapping(value = "/subscribeList")
        public JsonResult getSubscribeList(String skillId,HttpServletRequest request) {
            if(!StringUtils.hasText(skillId)){
                return JsonResult.fail("技能Id不能为空");
            }
            UserGeo userGeo = null;
            try {
                userGeo=getGeoFromRequest(request);
            }catch (Exception e){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            try {
                   Map<String,Object> map  = skillFuseAction.getUserPublishList(skillId,userGeo.getUserId(),userGeo.getLon(),userGeo.getLat());
                   if(map!=null) {
                       return JsonResult.successJsonResult(map);
                   }
                    return getResult(false, "查询预约（已接受/未接受）列表失败");
            } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    return JsonResult.fail("查询预约（已接受/未接受）列表失败");
                }
            }

            
        @ApiOperation(value = "已成交列表")
        @GetMapping(value = "/completedList")
        public JsonResult getCompletedList (String skillId,HttpServletRequest request) {
            String userId = null;
            try {
                userId = (super.getUserId(request));
                if(StringUtils.isEmpty(userId)){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            BigDecimal lon = new BigDecimal(getLon(request));
            BigDecimal lat = new BigDecimal(getLat(request));
            try {

                Map<String,Object> map  = skillFuseAction.getUserComPublishList(skillId,userId,lon,lat);
                if(map!=null) {
                    return JsonResult.successJsonResult(map);
                }
                return JsonResult.fail("已完成列表列表失败");
            } catch (RuntimeException e){
                return JsonResult.fail(e.getMessage());
            }
            catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                return JsonResult.fail("查询预约已完成列表列表失败");
            }
        }


        @ApiOperation(value = "我发布的技能")
        @PostMapping(value = "/publishList", consumes = {"application/json"})
        public JsonResult publishList(@Validated @RequestBody CommonPageDto commonPageDto,BindingResult bindingResult, HttpServletRequest request) {
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            String userId=null;
            try {
                userId=super.getUserId(commonPageDto.getUserId(),request);
                if(StringUtils.isEmpty(userId)){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            BigDecimal lon=(new BigDecimal(getLon(request)));
            BigDecimal lat=(new BigDecimal(getLat(request)));
            Page<Skill> page = new Page<>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
            try {
                Map<String,Object> map  = skillFuseAction.getPublishList( lat, lon,userId,page);
                if(map!=null) {
                    return JsonResult.successJsonResult(map);
                }
                return JsonResult.fail("查询我发布的技能表失败");
            }catch (RuntimeException ex){
                return JsonResult.fail(ex.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("查看我发布的技能失败");
            }
        }
        
        
        @ApiOperation(value = "我预约的技能")
        @PostMapping(value = "/registerList", consumes = {"application/json"})
        public JsonResult registerList(@RequestBody @Valid CommonPageDto commonPageDto, BindingResult bindingResult, HttpServletRequest request) {
            if (bindingResult.hasErrors()) {
                return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            try {
                if(StringUtils.isEmpty(commonPageDto.getUserId())){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            } catch (Exception e) {
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
            BigDecimal lat = new BigDecimal(getLat(request));
            BigDecimal lon = new BigDecimal(getLon(request));
            try {
                Map<String,Object> map  = skillFuseAction. getRegisterList(commonPageDto,lat,lon);
                if(map!=null) {
                    return JsonResult.successJsonResult(map);
                }
                return JsonResult.fail("查询我预约的技能表失败");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("查看我预约的技能表失败");
            }
        }

        
        @ApiOperation(value = "通过技能查预约单")
        @PostMapping(value = "/findRegisterList", consumes = {"application/json"})
        public JsonResult findRegisterList(String skillId,HttpServletRequest request) {
            String userId = super.getUserIdInWorth(request);
            try {
                SkillRegister list =worthServiceprovider.getSkillRegisterService().getReByUserIdAndSkillId(userId,skillId);
                if (list != null) {
                    return JsonResult.success().addResult("list", list);
                }
                return JsonResult.fail("通过技能查预约单失败！");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonResult.fail("通过技能查预约单失败");
            }
        }

        
        @ApiOperation(value = "发布者拒绝预约")
        @PostMapping(value = "/publishRefuse")
        public JsonResult publishRefuse(String id, String skillRegisterId, HttpServletRequest request) {
        String userId = super.getUserIdInWorth(request);
        if (!StringUtils.hasText(skillRegisterId)) {
            return JsonResult.fail("预约表ID不能为空");
        }
        if (!StringUtils.hasText(id)) {
            return JsonResult.fail("技能表ID不能为空");
        }
        try {
            Boolean success = skillFuseAction.publishRefuse(id, userId, skillRegisterId);
            return getResult(success,"拒绝预约失败");
        } catch (RuntimeException ex) {
            return JsonResult.fail(ex.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);  //写入日志
            return JsonResult.fail("发布者拒绝预约失败");
        }
    }


  /*     @ApiOperation(value = "退款详情")
    @PostMapping(value = "/refundList", consumes = {"application/json"})
    public JsonResult refundList(HttpServletRequest request) {
        String userId = null;
        try {
            userId=super.getUserId(request);
        } catch (Exception e) {
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            Map<String,Object> map  = skillFuseAction.getRefund(userId);
            if(map!=null) {
                return JsonResult.success().addResult("map", map);
            }
            return getResult(false, "查询退款详情失败");
        } catch (RuntimeException e){
            return JsonResult.fail("查询退款详情失败");
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return JsonResult.fail("查询退款详情失败");
        }
    }
*/
    /**
     * 预约成功，向发布者、入选者、商家业务主管推送
     *
     * @param id     技能id
     * @param userId 技能发布者id
     * @author hgb
     */
  @ApiOperation(value="预约成功，向发布者、入选者、商家业务主管推送")
  @PostMapping(value = "/sendMessageAfterSuccessOder")
  public JsonResult sendMessageAfterSuccessOder(String id, String userId){
      if(!StringUtils.hasText(id)||!StringUtils.hasText(userId)){
          return JsonResult.fail("ID不能为空");
      }
      try{
          skillFuseAction.sendMessageAfterSuccessOder(id, userId);
      }catch(Exception e){
          logger.error(e.getMessage(),e);
          return JsonResult.fail("推送失败");
      }
      return JsonResult.success();
  }


    /**
     * 双方互相评价完毕或预约结束时间届满后24小时，预约结束
     * @author hgb
     */
    @ApiOperation(value="双方互相评价完毕或预约结束时间届满后24小时，预约结束")
    @PostMapping(value = "/orderOver")
    public JsonResult orderOver(String skillOrderId){
        if(!StringUtils.hasText(skillOrderId)){
            return JsonResult.fail("ID不能为空");
        }
        try{
            skillFuseAction.orderOver(skillOrderId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("预约结束方法执行失败");
        }
        return JsonResult.success();
    }


    /**
     * ps:测试用
     * 推送超时未接受预约，超时未托管费用，超时未启动预约，超时未输入验证码的api,请勿调用
     * @param type
     * @param paramId
     * @return
     */
    @PostMapping(value = "/pushTimeout/{type}/{paramId}")
    public JsonResult pushTimeoutRefusal(@PathVariable int type,@PathVariable String paramId){
        try {
//            boolean b = skillFuseAction.pushTimeoutRefuse(skillRegisterId);
            boolean b = skillFuseAction.pushTimeoutMessage(type, paramId);
            if(!b){
                throw  new RuntimeException("推送失败");
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            JsonResult.fail("推送失败");
        }
        return JsonResult.success();
    }

    @ApiOperation(value="定时任务：检测预约者申请退款36小时后发布者是否处理退款申请")
    @PostMapping("/publishTimeoutNotHandleRefund")
    public JsonResult publishTimeoutNotHandleRefund(String json){
        try {
            skillFuseAction.publishTimeoutNotHandleRefund(json);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            JsonResult.fail("调用失败");
        }
        return JsonResult.success();
    }


}