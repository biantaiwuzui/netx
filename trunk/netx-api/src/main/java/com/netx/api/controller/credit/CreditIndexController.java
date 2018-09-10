package com.netx.api.controller.credit;

import com.netx.api.controller.BaseController;
import com.netx.api.controller.worth.MeetingController;
import com.netx.credit.service.CreditSubscriptionService;
import com.netx.credit.vo.CreditAcountDto;
import com.netx.credit.vo.CreditAcountVo;
import com.netx.credit.vo.CreditPreSaleVo;
import com.netx.fuse.biz.credit.CreditIndexFuseAction;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 梓
 * @date 2018-08-03 08:41
 */

@Api(description = "网信首页")
@RequestMapping("/credit/index")
@RestController
public class CreditIndexController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(MeetingController.class);



    @Autowired
    CreditSubscriptionService creditSubscriptionService;

    @Autowired
    private CreditIndexFuseAction creditIndexFuseAction;

    @ApiOperation(value = "我的网信" ,notes = "根据credit表id查询网信详情")
    @GetMapping("getHold")
    public JsonResult getHold(String userId){
        if (org.springframework.util.StringUtils.isEmpty(userId)) {
            return JsonResult.fail("Id不能为空");
        }
        try {
            return JsonResult.success().addResult("creditIndexFuseAction",creditIndexFuseAction.getCreditHold(userId));
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage()+userId,e);
            return JsonResult.fail("获取网信详情异常");
        }
    }

    @ApiOperation(value = "信用流水记录")
    @GetMapping("/getCreditAccount")
    public JsonResult getCreditAccount(HttpServletRequest request){
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null){
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try{
            List<CreditAcountVo> list = creditIndexFuseAction.selectCreditRecordList(userId);
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "网信预售")
    @GetMapping("/getCreditPreSale")
    public JsonResult getCreditPreSale(HttpServletRequest request){
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null){
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try{
            List<CreditPreSaleVo> list = creditIndexFuseAction.getCreditPreSale(userId);
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }



}
