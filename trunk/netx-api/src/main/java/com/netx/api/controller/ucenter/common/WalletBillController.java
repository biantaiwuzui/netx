package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.*;
import com.netx.fuse.biz.ucenter.WallerFrozenFuseAction;
import com.netx.ucenter.biz.common.BillAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-9-17
 */
@Api(description = "钱包流水操作")
@RestController
@RequestMapping("/api/walletBill")
public class WalletBillController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(WalletBillController.class);
    @Autowired
    private BillAction billAction;

    @Autowired
    UserAction userAction;

    @Autowired
    WallerFrozenFuseAction wallerFrozenFuseAction;

    @ApiOperation(value = "查询交易流水",notes = "返回值key:result 类型:Page<BillListResponseDto>")
    @PostMapping("/list")
    public JsonResult queryList(@RequestBody @Valid BillQueryRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId == null){
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }

        try {
            return JsonResult.success().addResult("result", billAction.getPages(userId,requestDto));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询交易流水异常："+e.getMessage(), e);
            return JsonResult.fail("查询交易流水异常");
        }
    }

    @ApiOperation(value = "添加交易流水", notes = "添加交易流水")
    @PostMapping("/add")
    public JsonResult addBill(@RequestBody @Valid BillAddRequestDto requestDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try{
            userId = getUserId(request);
            if(userId == null){
                return JsonResult.fail("用户id不能为空");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }
        try {
            wallerFrozenFuseAction.add(userId,requestDto);
            return JsonResult.success();
        } catch (Exception e) {
            logger.error("添加交易流水失败："+e.getMessage(), e);
            return JsonResult.fail("添加交易流水失败");
        }
    }

    @ApiOperation(value = "查询保荐人每个用户的交易额", notes = "返回值key:list 类型:List<BigDecimal>")
    @PostMapping("/searchMonthBillCount")
    public JsonResult searchMonthBillCounts(@RequestBody SearchMonthBillCountRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<BigDecimal> recommendCounts = billAction.searchMonthBillCounts(request.getRecommendUserIds());
            if (recommendCounts != null && recommendCounts.size() > 0) {
                return JsonResult.success().addResult("list",recommendCounts);
            }
            return JsonResult.fail("查询保荐人每个用户的交易额返回为空");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "按照时间段统计交易流水",notes = "返回值key:result 类型:BigDecimal")
    @PostMapping("/statisticsBillByTime")
    public JsonResult statisticsBillByTime(@RequestBody @Valid BillStatisticsRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            BigDecimal results = billAction.statisticBill(request);
            return JsonResult.success().addResult("result",results);
        } catch (Exception e) {
            logger.error("统计交易流水异常："+e.getMessage(), e);
            return JsonResult.fail("统计交易流水异常");
        }
    }

    @ApiOperation(value = "统计交易流水",notes = "返回值key:map 类型:Map<String,BigDecimal>")
    @PostMapping("/statisticsBill")
    public JsonResult statisticsBill(@RequestBody @Valid BillsStatisticsRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map<String,BigDecimal> results = billAction.statisticBills(request);
            return JsonResult.success().addResult("map",results);
        } catch (Exception e) {
            logger.error("统计交易流水异常："+e.getMessage(), e);
            return JsonResult.fail("统计交易流水异常");
        }
    }
}
