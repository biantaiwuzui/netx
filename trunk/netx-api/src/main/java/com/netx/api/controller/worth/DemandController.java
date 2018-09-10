package com.netx.api.controller.worth;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.netx.common.common.enums.*;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.fuse.biz.worth.DemandFuseAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.utils.json.ApiCode;
import com.netx.common.wz.dto.demand.*;
import com.netx.worth.biz.demand.DemandRegisterAction;
import com.netx.worth.biz.common.IndexAction;
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
import com.netx.common.wz.dto.common.CommonCheckDto;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.common.CommonPayCallbackDto;
import com.netx.common.wz.dto.common.CommonSearchDto;
import com.netx.common.wz.dto.common.CreateRefundDto;
import com.netx.worth.vo.DemandListVo;
import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.demand.DemandAction;
import com.netx.worth.biz.demand.DemandOrderAction;
import com.netx.worth.model.Demand;
import com.netx.worth.model.DemandOrder;
import com.netx.worth.util.PageWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.transform.Result;

@Api(description = "需求模块")
@RequestMapping("/wz/demand")
@RestController
public class DemandController extends BaseController {
    private Logger logger = LoggerFactory.getLogger ( DemandController.class );
    @Autowired
    private DemandAction demandAction;
    @Autowired
    private DemandFuseAction demandFuseAction;
    @Autowired
    private DemandOrderAction demandOrderAction;
    @Autowired
    private WorthServiceprovider worthServiceprovider;
    @Autowired
    private DemandRegisterAction demandRegisterAction;
    @Autowired
    private IndexAction indexAction;
    @Autowired
    private ScoreAction scoreAction;
    @Autowired
    JobFuseAction jobFuseAction;


    @ApiOperation(value = "需求单详情----简介页面 通过demandId查询 发布者和申请者数据不一样")
    @PostMapping(value = "/detail")
    public JsonResult detail(String id, HttpServletRequest request) {
        String userId;
        if (!StringUtils.hasText ( id )) {
            return JsonResult.fail ( "需求ID不能为空" );
        }
        userId = super.getUserIdInWorth ( request );
        double lon = getLon ( request );
        double lat = getLat ( request );
        Map<String, Object> map;

        try {
            map = demandFuseAction.getDemandDetail ( userId, lon, lat, id );
            return JsonResult.successJsonResult ( map );
        } catch (RuntimeException e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "此需求不存在" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查看需求详情失败" );
        }
    }

    @ApiOperation("需求预约者的详情，传递一个demandRegisterId  发布者在人员界面点击查看申请和申请者点击申请界面  返回参数id是需求申请单id orderId需求确认单id")
    @PostMapping({"/registerDetail"})
    public JsonResult registerDetail(String demandRegisterId, HttpServletRequest request) {
        if (StringUtils.isEmpty ( demandRegisterId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        String userId = super.getUserIdInWorth ( request );
        try {
            return JsonResult.successJsonResult ( demandFuseAction.getRegisterDetail ( userId, getLon ( request ), getLat ( request ), demandRegisterId ) );
        } catch (RuntimeException e) {
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            this.logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查询需求单详情失败" );
        }
    }

    @ApiOperation(value = "发布者查看申请人员 ")
    @PostMapping(value = "/detailPeoples")
    public JsonResult detailPeoples(String demandId, HttpServletRequest request) {
        if (!StringUtils.hasText ( demandId )) {
            return JsonResult.fail ( "需求ID不能为空" );
        }
        String userId = super.getUserIdInWorth ( request );

        double lon = getLon ( request );
        double lat = getLat ( request );
        try {
            return JsonResult.successJsonResult ( demandFuseAction.getDemandDetailPeoples ( userId, lon, lat, demandId ) );
        } catch (RuntimeException e) {
            return JsonResult.fail ( "查看需求详情失败：" + e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查看需求详情失败" );
        }
    }

    @ApiOperation(value = "我附近的需求")
    @PostMapping(value = "/nearList", consumes = {"application/json"})
    public JsonResult nearList(@Validated @RequestBody CommonSearchDto commonSearchDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        commonSearchDto.setUserId ( super.getUserIdInWorth ( request ) );
        if (commonSearchDto.getLength () == null) return JsonResult.fail ( "length距离不能为空" );
        Page<Demand> page = new Page<> ( commonSearchDto.getCurrentPage (), commonSearchDto.getSize () );
        Map<String, Object> map = null;
        try {
            map = demandFuseAction.nearList ( commonSearchDto.getUserId (), commonSearchDto.getLon (), commonSearchDto.getLat (), commonSearchDto.getLength (), page );
            return JsonResult.successJsonResult ( PageWrapper.wrapper ( page.getTotal (), map ) );
        } catch (RuntimeException e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "用户名不能为空" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查询我附近的需求失败" );
        }
    }

    @ApiOperation(value = "我申请的需求", response = DemandListVo.class)
    @PostMapping(value = "/regList", consumes = {"application/json"})
    public JsonResult receiveList(@Valid @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        double lon = getLon ( request );
        double lat = getLat ( request );
        try {
            return JsonResult.successJsonResult ( demandFuseAction.getRegisterDetailBydamandId ( commonPageDto.getUserId (), commonPageDto.getCurrentPage (), commonPageDto.getSize (), lon, lat ) );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查询我申请的需求失败" );
        }
    }

    @ApiOperation(value = "我发布的需求", response = DemandListVo.class)
    @PostMapping(value = "/sendList", consumes = {"application/json"})
    public JsonResult sendList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        double lon = getLon ( request );
        double lat = getLat ( request );

        Page<DemandListDto> page = new Page<> ( commonPageDto.getCurrentPage (), commonPageDto.getSize () );
        Map<String, Object> map = null;
        try {
            map = demandFuseAction.getUserPublishDemandList ( commonPageDto.getUserId (), lon, lat, page );
            return JsonResult.successJsonResult ( PageWrapper.wrapper ( page.getTotal (), map ) );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查询发布需求失败" );
        }
    }

    @ApiOperation(value = "入选的需求单")
    @PostMapping(value = "/orderList")
    public JsonResult orderList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        commonPageDto.setUserId ( super.getUserIdInWorth ( request ) );

        Page<DemandOrder> page = new Page<DemandOrder> ( commonPageDto.getCurrentPage ().intValue (), commonPageDto.getSize ().intValue () );
        List<Map<String, Object>> list = null;
        try {
            list = this.demandAction.orderList ( commonPageDto.getId (), commonPageDto.getUserId (), page );
            return JsonResult.successJsonResult ( PageWrapper.wrapper ( page.getTotal (), list ) );
        } catch (Exception e) {
            this.logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查询入选的需求单失败" );
        }
    }

    @ApiOperation(value = "发布需求")
    @PostMapping(value = "/publish", consumes = {"application/json"})
    public JsonResult publish(@Validated @RequestBody DemandPublishDto demandPublishDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        demandPublishDto.setUserId ( super.getUserIdInWorth ( request ) );
        try {
            String demandId = demandFuseAction.publish ( demandPublishDto );
            if (StringUtils.hasText ( demandId )) {
                // 若 demandId 有内容，发布成功 返回demandId
                scoreAction.addScore ( demandPublishDto.getUserId (), StatScoreEnum.SS_PUBLISH_WORTH );
                return JsonResult.success ().addResult ( "demandId", demandId );
            } else {
                return JsonResult.fail ( "不能发布同样的需求" );
            }
        } catch (RuntimeException e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布需求失败：" + e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布需求异常" );
        }
    }

    @ApiOperation(value = "编辑需求")
    @PostMapping(value = "/publishEdit", consumes = {"application/json"})
    public JsonResult edit(@Validated @RequestBody DemandPublishDto demandPublishDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        demandPublishDto.setUserId ( super.getUserIdInWorth ( request ) );
        try {
            //判断该修改的需求id是否是该用户发布的
            String result = demandFuseAction.publishEdit ( demandPublishDto );
            return getResult ( result == null, result );
        } catch (Exception e) {
            logger.error ( e.getMessage () );
            return JsonResult.fail ( "发布者修改需求失败" );
        }
    }

    @ApiOperation(value = "发布者取消需求")
    @PostMapping(value = "/publishCancel")
    public JsonResult publishCancel(String id, HttpServletRequest request) {
        if (!StringUtils.hasText ( id )) {
            return JsonResult.fail ( "ID不能为空" );
        }
        String userId = super.getUserIdInWorth ( request );
        //判断订单号和用户是否正确
        int checkDemandCount = worthServiceprovider.getDemandService ().selectByIdAndUserId ( userId, id );
        if (checkDemandCount != 1) {
            return JsonResult.fail ( "此用户不存在此需求" );
        }
        try {
            boolean success = demandFuseAction.publishCancel ( id, userId );
            if (success) {
                scoreAction.addScore ( userId, -StatScoreEnum.SS_PUBLISH_WORTH.score () );
            }
            return getResult ( success, "发布者取消需求失败" );
        } catch (RuntimeException e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布者取消需求异常" );
        }
    }

    @ApiOperation(value = "申请需求")
    @PostMapping(value = "/register", consumes = {"application/json"})
    public JsonResult register(@Validated @RequestBody DemandRegisterDto demandRegisterDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        demandRegisterDto.setUserId ( super.getUserIdInWorth ( request ) );
        try {
            String result = demandFuseAction.register ( demandRegisterDto );
            if (result == null) {
                //添加最新申请需求的记录
                indexAction.addHistory ( WorthTypeEnum.DEMAND_TYPE, demandRegisterDto.getUserId (), demandRegisterDto.getDemandId () );
                return JsonResult.success ();
            }
            return JsonResult.fail ( result );
        } catch (RuntimeException e) {
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "申请需求异常" );
        }
    }

    @ApiOperation(value = "报名方修改需求")
    @PostMapping(value = "/registerEdit", consumes = {"application/json"})
    public JsonResult registerEdit(@Validated @RequestBody DemandRegisterDto demandRegisterDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        try {
            demandRegisterDto.setUserId ( super.getUserId ( request ) );
        } catch (Exception e) {
            return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
        }
        try {
            boolean success = demandAction.registerEdit ( demandRegisterDto );
            return getResult ( success, "报名方修改需求失败" );
        } catch (RuntimeException e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "报名方修改需求异常" );
        }
    }

    @ApiOperation(value = "报名方取消需求")
    @PostMapping(value = "/registerCancel")
    public JsonResult registerCancel(String id, HttpServletRequest request) {
        String userId = super.getUserIdInWorth ( request );
        if (!StringUtils.hasText ( id )) {
            return JsonResult.fail ( "需求申请ID不能为空" );
        }
        try {
            boolean success = demandAction.registerCancel ( id, userId );
            return getResult ( success, "报名方取消需求失败" );
        } catch (RuntimeException e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "报名方取消需求失败" );
        }
    }

    @ApiOperation(value = "发布方接受报名人预约【预约单id】", notes = "选定入选人")
    @PostMapping(value = "/acceptRegister")
    public JsonResult acceptRegister(@RequestParam("id") String id, HttpServletRequest request) {
        if (StringUtils.isEmpty ( id )) {
            return JsonResult.fail ( "预约单id不能为空" );
        }
        String userId;
        try {
            userId = getUserId ( request );
            if (StringUtils.isEmpty ( userId )) {
                return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
            }
        } catch (Exception e) {
            return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
        }
        try {
            boolean result = demandFuseAction.acceptRegister ( id, userId );
            return getResult ( result, "发布方接受报名人失败" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布方接受报名人异常" );
        }
    }

    @ApiOperation(value = "发布者拒绝入选者申请需求")
    @PostMapping(value = "/refuseRegister")
    public JsonResult registerActRefund(String demandRegisterId, HttpServletRequest request) {
        String userId = super.getUserIdInWorth ( request );
        if (!StringUtils.hasText ( demandRegisterId )) {
            return JsonResult.fail ( "需求单不能为空" );
        }
        try {

            boolean success = demandFuseAction.registerRefuse ( demandRegisterId );
            return getResult ( success, "发布者拒绝入选者申请需求请求成功" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布者拒绝入选者申请需求请求失败" );
        }
    }

    @ApiOperation(value = "发布方确认细节")
    @PostMapping(value = "/publishConfirm")
    public JsonResult publishConfirm(@Validated @RequestBody DemandOrderConfirmRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        String userId;
        try {
            userId = getUserId ( request );
            if (StringUtils.isEmpty ( userId )) {
                return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
            }
        } catch (Exception e) {
            return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
        }
        try {
            String result = demandFuseAction.publishConfirm ( dto, userId );
            return getResult ( result == null, result );
        } catch (RuntimeException e) {
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布方确认细节失败" );
        }
    }

    @ApiOperation(value = "发布方结束报名")
    @PostMapping(value = "/publishStop")
    public JsonResult registerStop(String demandId, HttpServletRequest request) {
        String userId = super.getUserIdInWorth ( request );
        if (!StringUtils.hasText ( demandId )) {
            return JsonResult.fail ( "ID不能为空" );
        }
        try {
            String result = demandFuseAction.publishStop ( demandId, userId );
            return getResult ( result == null, result );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布方结束报名异常：" + e.getMessage () );
        }
    }

    @ApiOperation(value = "立即启动需求(申请方生成验证码)")
    @PostMapping(value = "/start")
    public JsonResult start(HttpServletRequest request, String registerId) {
        String userId = super.getUserIdInWorth ( request );
        if (!StringUtils.hasText ( registerId )) {
            return JsonResult.fail ( "预约单ID不能为空" );
        }
        double lon = getLon ( request );
        double lat = getLat ( request );
        try {
            return JsonResult.success ().addResult ( "code", demandFuseAction.checkIsPresent ( userId, registerId, lon, lat ) );
        } catch (RuntimeException e) {
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "启动需求失败" );
        }
    }

    @ApiOperation("发布者取消入选者的需求")
    @PostMapping({"/cancelRegister"})
    public JsonResult cancelRegister(HttpServletRequest request, String orderId) {
        String userId;
        if (!StringUtils.hasText ( orderId )) {
            return JsonResult.fail ( "预约单ID不能为空" );
        }
        try {
            userId = super.getUserId ( request );
        } catch (Exception e) {
            return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
        }
        try {
            boolean success = this.demandFuseAction.cancelRegister ( userId, orderId );
            return getResult ( success, "发布者取消入选者的需求失败" );
        } catch (RuntimeException e) {
            this.logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "用户不存在此申请需求" );
        } catch (Exception e) {
            this.logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布者取消入选者的需求失败" );
        }

    }

    @ApiOperation(value = "放弃参与,即以入选放弃参与")
    @PostMapping(value = "/registerDrop")
    public JsonResult registerDrop(HttpServletRequest request, String registerId) {
        String userId;
        try {
            userId = super.getUserId ( request );
        } catch (Exception e) {
            return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
        }
        if (!StringUtils.hasText ( registerId )) {
            return JsonResult.fail ( "预约单ID不能为空" );
        }
        try {
            boolean success = demandFuseAction.registerDrop ( userId, registerId );
            return getResult ( success, "放弃参与的请求失败" );
        } catch (RuntimeException e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "用户没有申请该需求" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "放弃参与的请求失败" );
        }
    }

    @ApiOperation(value = "发布者输入验证码")
    @PostMapping(value = "/verificationCode")
    public JsonResult verificationCode(String registerId, Integer code, HttpServletRequest request) {
        UserGeo userGeo = null;
        try {
            userGeo = getGeoFromRequest ( request );
            if (StringUtils.isEmpty ( userGeo.getUserId () )) {
                return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
            }
        } catch (Exception e) {
            return JsonResult.fail ( ApiCode.NO_AUTHORIZATION );
        }
        if (!StringUtils.hasText ( registerId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        if (code == null) {
            return JsonResult.fail ( "验证码不能为空" );
        }
        try {
            int success = demandAction.checkSendInputCode ( registerId, code, userGeo );
            switch (success) {
                case 0:
                    return JsonResult.fail ( "您不是发起者，不能进行此项操作" );
                case 1:
                    return JsonResult.fail ( "您未进入预约地点，不能进行校验" );
                case 2:
                    return JsonResult.fail ( "您已超出三次校验失败，不能进行校验" );
                case 3:
                    return JsonResult.fail ( "验证码错误,错误验证次数+1" );
                case 4:
                    return JsonResult.fail ( "验证码错误.增加错误验证次数失败." );
                case 5:
                    return JsonResult.success ( "验证码与距离验证均成功!!!" );
                case 6:
                    return JsonResult.fail ( "验证码与距离验证均成功!更新入选者信息失败.请联系客服小哥哥." );
            }
            return JsonResult.fail ( "验证出错~~!" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "验证码的校验时出错!!" );
        }
    }
/*
    @ApiOperation(value = "提醒商家提供服务")
    @PostMapping(value = "/procideService")
    public JsonResult provideService(String demandregisterId, HttpServletRequest request) {
        if (!StringUtils.hasText(demandregisterId)) {
            return JsonResult.fail("需求ID不能为空");
        }
        try {
            demandFuseAction.provideService(demandregisterId, super.getUserIdInWorth(request));
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("提醒失败");
        }
    }*/

    @ApiOperation(value = "未确认的需求单列表")
    @PostMapping(value = "/noConfirmOrderList")
    public JsonResult noConfirmOrderList(String demandId) {
        if (!StringUtils.hasText ( demandId )) {
            return JsonResult.fail ( "需求ID不能为空" );
        }
        try {
            List<DemandOrder> list = worthServiceprovider.getDemandOrderService ().noConfirmOrderList ( demandId );
            return JsonResult.success ().addResult ( "list", list );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查询未确认的需求单列表失败" );
        }

    }

    @ApiOperation(value = "入选者同意付款给商家，即调用商家的支付订单接口，必须成功支付订单后再调用此方法")
    @PostMapping(value = "/registerPay")
    public JsonResult registerPay(String demandOrderId, String userId, BigDecimal orderPrice) {
        if (!StringUtils.hasText ( userId )) {
            return JsonResult.fail ( "用户ID不能为空" );
        }
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        if (orderPrice == null) {
            return JsonResult.fail ( "订单金额不能为空" );
        }
        try {
            boolean success = demandAction.registerPay ( demandOrderId, userId, orderPrice );
            return getResult ( success, "成功!", "找不到订单Id,请检查订单Id是否正确!" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "入选者同意付款给商家的请求失败!" );
        }
    }

    @ApiOperation(value = "该需求单所需支付给入选者的报酬")
    @PostMapping(value = "/getTotalWageByDemandId")
    public JsonResult getRegisterWage(String registerId) {
        if (!StringUtils.hasText ( registerId )) {
            return JsonResult.fail ( "需求报名表ID不能为空" );
        }
        try {
            BigDecimal totalWage = demandAction.getRegisterWage ( registerId );
            return JsonResult.success ().addResult ( "totalWage", totalWage );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "获取该需求单所需支付给入选者的报酬失败" );
        }
    }

    @ApiOperation(value = "需求完成     发布者同意付款给入选者，如果选网币支付则必须先支付，待支付完成后再调用此接口。")
    @PostMapping(value = "/publishPay")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "payWay", value = "0：网币支付。1：托管费用支付")})
    public JsonResult publishPay(String demandOrderId, String payWay, HttpServletRequest request) {
        String userId = super.getUserIdInWorth ( request );
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求确认单ID不能为空" );
        }
        try {
            return getResult ( demandFuseAction.publishPay ( demandOrderId, userId ), "更新异常" );
        } catch (RuntimeException e) {
            logger.error ( e.getMessage () );
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布者同意付款给入选者的请求失败" );
        }
    }

    @ApiOperation(value = "发布者申请退款")
    @PostMapping(value = "/publishRefund", consumes = {"application/json"})
    public JsonResult publishRefund(@Validated @RequestBody CreateRefundDto createRefundDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }

        createRefundDto.setUserId ( super.getUserIdInWorth ( request ) );
        //身份校验  是发布者才给通过
        try {
            //更新order表状态并创建退款单数据
            boolean success = demandFuseAction.publishRefund ( createRefundDto );
            return getResult ( success, "发布者申请退款失败" );
        } catch (RuntimeException e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "发布者申请退款失败" );
        }
    }

    @ApiOperation(value = "付款给入选者，仲裁通过后调用")
    @PostMapping(value = "/registerRejectRefund")
    public JsonResult registerRejectRefund(String demandOrderId, HttpServletRequest request) {
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        String userId = super.getUserIdInWorth ( request );
        try {
            boolean success = demandFuseAction.registerRejectRefund ( demandOrderId, userId );
            return getResult ( success, "付款给入选者，冲裁通过后调用的请求失败" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "付款给入选者，冲裁通过后调用的请求失败" );
        }
    }

    @ApiOperation(value = "根据demandOrderId查询发布者的退款申请信息")
    @PostMapping(value = "/refundMessage")
    public JsonResult refundMessage(String demandOrderId) {
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        try {
            return JsonResult.success ().addResult ( "refund", demandAction.refundMessage ( demandOrderId ) );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "查询发布者的退款申请信息失败" );//根据demandOrderId
        }
    }

    @ApiOperation(value = "根据demandOrderId将状态改成仲裁中")
    @PostMapping(value = "/arbitrationStatus")
    public JsonResult arbitrationStatus(String demandOrderId) {
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        try {
            boolean success = demandFuseAction.arbitrationStatus ( demandOrderId );
            return getResult ( success, "根据demandOrderId将状态改成仲裁中失败" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "根据demandOrderId将状态改成仲裁中失败" );
        }
    }

    @ApiOperation(value = "入选者同意发布者的退款申请,userId是发起人Id或者仲裁不通过时调用，退款给发布者")
    @PostMapping(value = "/registerAcceptRefund")
    public JsonResult registerAcceptRefund(String demandOrderId, HttpServletRequest request) {
        String userId = super.getUserIdInWorth ( request );
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单不能为空" );
        }
        try {
            boolean success = demandFuseAction.registerAcceptRefund ( demandOrderId, userId );
            return getResult ( success, "入选者同意发布者的退款申请成功~~" );
        } catch (RuntimeException e) {
            return JsonResult.fail ( e.getMessage () );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "入选者同意发布者的退款申请请求失败" );
        }
    }

    @ApiOperation(value = "验证距离(确认出席)")
    @PostMapping(value = "/check", consumes = {"application/json"})
    public JsonResult check(@Validated @RequestBody CommonCheckDto commonCheckDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        Double lon = getLon ( request );
        Double lat = getLat ( request );
        try {
            boolean success = demandAction.check ( commonCheckDto, lon, lat );

            return getResult ( success, "验证距离失败" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "验证距离失败" );
        }
    }

    @ApiOperation(value = "检查这个用户是否有未完成的需求")
    @PostMapping(value = "/checkHasUnComplete", consumes = {"application/json"})
    public JsonResult checkHasUnComplete(String userId) {
        if (!StringUtils.hasText ( userId )) {
            return JsonResult.fail ( "用户ID不能为空" );
        }
        try {
            boolean hasUnComplete = demandAction.checkHasUnComplete ( userId );
            return JsonResult.success ().addResult ( "hasUnComplete", hasUnComplete );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "检查这个用户是否有未完成的需求失败" );
        }
    }

    @ApiOperation("支付后回调")
    @PostMapping(value = {"/payCallback"}, consumes = {"application/json"})
    public JsonResult payCallback(@Validated @RequestBody CommonPayCallbackDto commonPayCallbackDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        try {
            boolean success = this.demandFuseAction.payCallback ( commonPayCallbackDto );
            return getResult ( success, "支付后回调失败" );
        } catch (Exception e) {
            this.logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "支付后回调失败" );
        }
    }

    @ApiOperation(value = "定时任务：检查是否确定细节")
    @PostMapping(value = "/checkConfirm")
    public JsonResult checkConfirm(String demandOrderId) {
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        try {
            demandFuseAction.checkConfirm ( demandOrderId );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "启动定时任务：检查是否确定细节失败" );
        }
        return JsonResult.success ();
    }

    @ApiOperation(value = "定时任务：需求开始,检查启动情况，报名方是否确认启动")
    @PostMapping(value = "/checkStart")
    public JsonResult checkStart(String demandOrderId) {
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        try {
            demandFuseAction.checkStart ( demandOrderId );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "启动定时任务：需求开始,检查启动情况，报名方是否确认启动失败" );
        }
        return JsonResult.success ();
    }

    @ApiOperation(value = "定时任务：验证码、距离是否通过")
    @PostMapping(value = "/checkSuccess")
    public JsonResult checkSuccess(String demandOrderId) {
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        try {
            demandAction.checkSuccess ( demandOrderId );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            //启动定时任务
//            jobFuseAction.addJob ( JobEnum.ARTICLE_DELETED_JOB,, AuthorEmailEnum)
            return JsonResult.fail ( "启动定时任务：验证码、距离是否通过失败" );
        }
        return JsonResult.success ();
    }

    @ApiOperation(value = "定时任务：是否评价")
    @PostMapping(value = "/checkEvaluate")
    public JsonResult checkEvaluate(String demandOrderId) {
        if (!StringUtils.hasText ( demandOrderId )) {
            return JsonResult.fail ( "需求单ID不能为空" );
        }
        try {
            demandFuseAction.checkEvaluate ( demandOrderId );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "启动定时任务：是否评价失败" );
        }
        return JsonResult.success ();
    }

    @ApiOperation(value = "需求列表", notes = "通用的需求列表接口，根据不同的条件返回")
    @PostMapping(value = "/list")
    public JsonResult list(@Validated @RequestBody DemandSearchDto demandSearchDto, BindingResult bindingResult, HttpServletRequest requestDto) {
        if (bindingResult.hasErrors ()) {
            return JsonResult.fail ( bindingResult.getAllErrors ().get ( 0 ).getDefaultMessage () );
        }
        try {
            demandSearchDto.setLat ( getLat ( requestDto ) );
            demandSearchDto.setLon ( getLon ( requestDto ) );
            List<DemandListDto> list = demandFuseAction.list ( demandSearchDto );
            if (list != null) {
                return JsonResult.success ().addResult ( "list", list );
            }
            return JsonResult.fail ( "获取需求列表失败！" );
        } catch (Exception e) {
            logger.error ( e.getMessage (), e );
            return JsonResult.fail ( "获取需求列表异常！" );
        }
    }

    @ApiOperation(value = "定时任务:需求因无入选人而自动取消!", notes = "需求因无入选人而自动取消，需求因无入选人而自动取消")
    @PostMapping(value = "/checkDemandAutoCancel")
    public JsonResult checkDingShiRenWu(String demandId) {
        try {
            int a = demandFuseAction.checkDemandAutoCancel ( demandId );

            if (0 <= a) {
                switch (a) {
                    case 0:
                        return JsonResult.success ( "资金回退成功!!" );
                    case 1:
                        return JsonResult.fail ( "找不到该需求!" );
                    case 2:
                        return JsonResult.fail ( "资金回退异常!请联系客服!" );
                }
            }
            return JsonResult.fail ( "定时任务出现异常..." );
        } catch (Exception e) {
            logger.error ( "定时任务异常!!" );
            return JsonResult.fail ( "定时任务异常!" );
        }
    }

    @ApiOperation(value = "定时任务:需求完成24小时后自动结算报酬!", notes = "需求因完成24小时后发布者无任何操作(默认同意结算报酬)自动结算报酬!")
    @PostMapping(value = "/checkPublisherIsPay")
    public JsonResult checkPublisherIsPay(String demandId) {
        try {
            int a = demandFuseAction.checkPublisherIsPay ( demandId );
            if (0 <= a) {
                switch (a) {
                    case 0:
                        throw new RuntimeException ( "报酬结算异常!请联系客服!" );
                    case 1:
                        return JsonResult.success ( "报酬自动结算成功!!" );
                    case 2:
                        return JsonResult.success ( "需求已完成,无需自动结算!" );
                }
            }
            throw new RuntimeException ( "定时任务出现异常..." );
        } catch (Exception e) {
            logger.error ( "定时任务异常!!" );
            throw new RuntimeException ( "定时任务异常!" );
        }

    }

    @ApiOperation(value = "定时任务:检测需求开始后30分钟是否有入选者验证码通过!", notes = "需求因需求开始后30分钟是否有入选者验证码通过,需求失败!")
    @PostMapping(value = "/checkCodePass")
    public JsonResult checkCodePass(String demandId) {
        try {
            int a = demandFuseAction.checkCodePass ( demandId );
            if (0 <= a) {
                switch (a) {
                    case 0:
                        return JsonResult.fail ( "无入选者验证码通过,需求失败!资金已退回!" );

                    case 1:
                        return JsonResult.success ( "有入选者验证码通过!需求正常!" );
                }
            }
            throw new RuntimeException ( "定时任务出现异常..." );
        } catch (Exception e) {
            logger.error ( "定时任务异常!!" );
            throw new RuntimeException ( "定时任务异常!" );
        }
    }


/*    查看使用冻结金额的流向
    @ApiOperation(value = "!!!!!")
    @PostMapping(value = "/checkMoneyHowToUser")
    public void checkMoneyHowToUser(){
        demandFuseAction.checkMoneyHowToUser();
    }*/

    @ApiOperation(value = "定时任务:检测需求开始前60分钟入选者是否已启动需求!", notes = "检测需求开始前60分钟入选者是否已启动需求!")
    @PostMapping(value = "/checkRegisterStart")
    public void checkSixtyMinuteStart(String demandId) {
        try {
            demandFuseAction.checkSixtyMinuteStart ( demandId );
        } catch (Exception e) {
            logger.error ( "定时任务异常!!" );
            throw new RuntimeException ( "定时任务异常!" );
        }
    }

}

