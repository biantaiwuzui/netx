package com.netx.api.controller.stat;

import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "获奖名单")
@RestController
@RequestMapping("/api/userStat/")
public class UserStatController {

    private Logger logger = LoggerFactory.getLogger(UserStatController.class);

    @ApiOperation(value = "最具人缘奖")
    @PostMapping("queryFriendStat")
    public JsonResult queryFriendStat(){
        try {
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最具人缘奖获得者异常");
        }
    }

    @ApiOperation(value = "最具人气奖")
    @PostMapping("queryUserStat")
    public JsonResult queryUserStat(){
        try {
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最具人气奖异常");
        }
    }

    @ApiOperation(value = "最具图文奖")
    @PostMapping("queryAritcleStat")
    public JsonResult queryAritcleStat(){
        try {
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最具图文奖获得者异常");
        }
    }

    @ApiOperation(value = "网能红人奖")
    @PostMapping("queryWorthStat")
    public JsonResult queryWorthStat(){
        try {
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询网能红人奖获得者异常");
        }
    }

    @ApiOperation(value = "最具情商奖")
    @PostMapping("queryEQStat")
    public JsonResult queryMeetingStat(){
        try {
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最具情商奖获得者异常");
        }
    }

    @ApiOperation(value = "网商精英奖")
    @PostMapping("queryProduceStat")
    public JsonResult queryProduceStat(){
        try {
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询网商精英奖获得者异常");
        }
    }

    @ApiOperation(value = "最佳买手奖")
    @PostMapping("queryShoppingStat")
    public JsonResult queryShoppingStat(){
        try {
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最佳买手奖获得者异常");
        }
    }

    @ApiOperation(value = "最佳建议奖")
    @PostMapping("querySuggestStat")
    public JsonResult querySuggestStat(){
        try {
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最佳建议奖获得者异常");
        }
    }

}
