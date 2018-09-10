package com.netx.api.controller.worth;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.api.controller.BaseController;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.common.wz.dto.gift.GiftLogisticsDto;
import com.netx.common.wz.dto.gift.GiftSendDto;
import com.netx.common.wz.vo.gift.GiftListVo;
import com.netx.fuse.biz.worth.GiftFuseAction;
import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.common.GiftAction;
import com.netx.worth.model.Gift;
import com.netx.worth.util.PageWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

@Api(description = "礼物模块")
@RequestMapping("/wz/gift")
@RestController
public class GiftController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(GiftController.class);
    @Autowired
    private GiftAction giftAction;
    @Autowired
    private GiftFuseAction giftFuseAction;

    //    @ApiOperation(value = "查询礼物列表", response = Gift.class)
//    @PostMapping(value = "/list", consumes = {"application/json"})//   GetMapping不支持consumes
//    public JsonResult list(@Validated @RequestBody GiftListDto gift, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());;
//        }
//        Page<Gift> page = new Page<Gift>(gift.getCurrentPage(), gift.getSize());
//        List<GiftListVo> list = null;
//        try{
//            list = giftAction.getUserReceiveGifts(gift, page);
//        }catch (Exception e){
//            logger.error(e.getMessage(), e);
//            return JsonResult.fail();
//        }
//        return ResultGenerator.genSuccessResult(PageWrapper.wrapper(page.getTotal(), list));
//    }
    @ApiOperation(value = "我收到的礼物", response = GiftListVo.class)
    @PostMapping(value = "/receiveList", consumes = {"application/json"})
    public JsonResult receiveList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Map map = null;
        Page<Gift> page = new Page<Gift>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        try {
            map = giftFuseAction.getUserReceiveGifts(commonPageDto.getUserId(), page);
            return JsonResult.success().addResult("map", PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询我收到的礼物失败");
        }
    }

    @ApiOperation(value = "我送出的礼物", response = GiftListVo.class)
    @PostMapping(value = "/sendList", consumes = {"application/json"})
    public JsonResult sendList(@Validated @RequestBody CommonPageDto commonPageDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Map map = null;
        Page<Gift> page = new Page<Gift>(commonPageDto.getCurrentPage(), commonPageDto.getSize());
        try {
            map = giftFuseAction.getUserSendGifts(commonPageDto.getUserId(), page);
            return JsonResult.success().addResult("map", PageWrapper.wrapper(page.getTotal(), map));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询我送出的礼物失败");
        }
    }

    @ApiOperation(value = "我 送出/收到 的礼物数量", notes = "status：送出(send) 2.收到(receive)")
    @PostMapping(value = "/getCountByUserId")
    public JsonResult getCountByUserId(String status, HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        if (!StringUtils.hasText(status)) {
            return JsonResult.fail("状态不能为空");
        }
        Integer count = 0;
        try {
            count = giftAction.getCountByUserId(status, userId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询我送出的礼物数量失败");
        }
        return JsonResult.success().addResult("count", count);
    }

    @ApiOperation(value = "接受礼物，请先调用其它模块接口，返回成功才可以调用该接口")
    @PostMapping("/accept")
    public JsonResult accept(String giftId, HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        if (!StringUtils.hasText(giftId)) {
            return JsonResult.fail("礼物ID不能为空");
        }
        try {
            boolean flag = giftFuseAction.accept(userId, giftId);
            return getResult(flag, "接受礼物失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("接受礼物失败");
        }
    }

    @ApiOperation(value = "拒绝礼物，请先调用其它模块接口，返回成功才可以调用该接口")
    @PostMapping("/refuse")
    public JsonResult refuse(String giftId, HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        if (!StringUtils.hasText(giftId)) {
            return JsonResult.fail("礼物ID不能为空");
        }
        try {
            boolean flag = giftFuseAction.refuse(userId, giftId);
            return getResult(flag, "拒绝礼物失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("拒绝礼物失败");
        }
    }

    @ApiOperation(value = "赠送礼物，请先调用其它模块接口，返回成功才可以调用该接口")
    @PostMapping(value = "/send", consumes = {"application/json"})
    public JsonResult add(@Validated @RequestBody GiftSendDto gift) {
        try {
            boolean flag = giftFuseAction.add(gift);
            return getResult(flag, "赠送礼物失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("赠送礼物失败");
        }
    }

    @ApiOperation(value = "查看礼物详情")
    @PostMapping("/detail")
    public JsonResult detail(String giftId, HttpServletRequest requestDto) {
        String userId = null;
        try {
            userId = getUserId(requestDto);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        try {
            Map<String, Object> map = giftFuseAction.detail(giftId, userId);
            if (map != null) {
                return JsonResult.success().addResult("map", map);
            } else {
                return JsonResult.fail("查看礼物详情失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查看礼物详情失败");
        }
    }

    @ApiOperation(value = "设置物流信息")
    @PostMapping(value = "/setLogistics", consumes = {"application/json"})
    public JsonResult setLogistics(@Validated @RequestBody GiftLogisticsDto giftLogisticsDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean success = giftAction.setLogistics(giftLogisticsDto);
            return getResult(success, "设置物流信息失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("设置物流信息失败");
        }
    }

//    @ApiOperation(value = "我收到的礼物总数")
//    @PostMapping(value = "/receiveCount")
//    public JsonResult receiveCount(HttpServletRequest requestDto) {
//        String userId = null;
//        try {
//            userId = getUserId(requestDto);
//        }catch (Exception e){
//            logger.warn(e.getMessage());
//        }
//        int count = 0;
//        try {
//            count = giftAction.getReceiveCount(userId);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return JsonResult.fail("查询我收到的礼物总数失败");
//        }
//        return JsonResult.success().addResult("count",count);
//    }

}
