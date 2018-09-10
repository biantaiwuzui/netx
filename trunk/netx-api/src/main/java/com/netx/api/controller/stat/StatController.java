package com.netx.api.controller.stat;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.StatPageRequestDto;
import com.netx.fuse.biz.shoppingmall.ordercenter.MerchantOrderFuseAction;
import com.netx.fuse.biz.worth.WorthFuseAction;
import com.netx.ucenter.biz.friend.FriendsAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.biz.user.ArticleAction;
import com.netx.ucenter.biz.user.UserSuggestAction;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(description = "排行榜")
@RestController
@RequestMapping("/api/stat/")
public class StatController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(StatController.class);

    @Autowired
    private FriendsAction friendsAction;

    @Autowired
    private MerchantOrderFuseAction merchantOrderFuseAction;

    @Autowired
    private WorthFuseAction worthFuseAction;

    @Autowired
    private ArticleAction articleAction;

    @Autowired
    private UserSuggestAction userSuggestAction;

    @Autowired
    private ScoreAction scoreAction;

    @ApiOperation(value = "总排行榜")
    @PostMapping("queryScoreStat")
    public JsonResult queryScoreStat(StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent()-1)*dto.getSize()+1;
            int end = dto.getCurrent()*dto.getSize();
            return JsonResult.successJsonResult(scoreAction.queryScoreStat(fromUserId,stat,end));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("查询总排行榜异常");
        }
    }

    @ApiOperation(value = "人缘排行榜")
    @PostMapping("queryFriendStat")
    public JsonResult queryFriendStat(StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent()-1)*dto.getSize()+1;
            int end = dto.getCurrent()*dto.getSize();
            return JsonResult.successJsonResult(friendsAction.queryFriendStatOne(fromUserId,stat,end));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询人缘排行榜异常");
        }
    }

    @ApiOperation(value = "人气排行榜")
    @PostMapping("queryUserStat")
    public JsonResult queryUserStat(StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent() - 1) * dto.getSize() + 1;
            int end = dto.getCurrent() * dto.getSize();
            return JsonResult.successJsonResult(scoreAction.getUserStat(fromUserId,stat,end));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询人气排行榜异常");
        }
    }

    @ApiOperation(value = "图文排行榜")
    @PostMapping("queryAritcleStat")
    public JsonResult queryAritcleStat(StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent() - 1) * dto.getSize() + 1;
            int end = dto.getCurrent() * dto.getSize();
            return JsonResult.successJsonResult(articleAction.queryArticleStat(fromUserId,stat,end));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("查询图文排行榜异常");
        }
    }

    @ApiOperation(value = "网能红人排行榜")
    @PostMapping("queryWorthStat")
    public JsonResult queryWorthStat(StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent() - 1) * dto.getSize() + 1;
            int end = dto.getCurrent() * dto.getSize();
            return JsonResult.successJsonResult(worthFuseAction.queryWorthStat(fromUserId,stat,end));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询网能红人排行榜异常");
        }
    }

    @ApiOperation(value = "情商排行榜")
    @PostMapping("queryEQStat")
    public JsonResult queryMeetingStat(StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent() - 1) * dto.getSize() + 1;
            int end = dto.getCurrent() * dto.getSize();
            return JsonResult.successJsonResult(worthFuseAction.queryEQStat(fromUserId,stat,end));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询情商排行榜异常");
        }
    }

    @ApiOperation(value = "网商精英排行榜")
    @PostMapping("queryProduceStat")
    public JsonResult queryProduceStat(StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent()-1)*dto.getSize()+1;
            int end = dto.getCurrent()*dto.getSize();
            return JsonResult.successJsonResult(merchantOrderFuseAction.queryProduceStat(fromUserId,stat,end));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询网商精英排行榜异常");
        }
    }

    @ApiOperation(value = "买手排行榜")
    @PostMapping("queryShoppingStat")
    public JsonResult queryShoppingStat( StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent()-1)*dto.getSize()+1;
            int end = dto.getCurrent()*dto.getSize();
            return JsonResult.successJsonResult(merchantOrderFuseAction.queryShoppingStat(fromUserId,stat,end));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("查询买手排行榜异常");
        }
    }

    @ApiOperation(value = "建议排行榜")
    @PostMapping("querySuggestStat")
    public JsonResult querySuggestStat( StatPageRequestDto dto, HttpServletRequest request){
        String fromUserId = null;
        try {
            fromUserId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            int stat = (dto.getCurrent()-1)*dto.getSize()+1;
            int end = dto.getCurrent()*dto.getSize();
          //  return JsonResult.successJsonResult(userSuggestAction.querySuggestStat(fromUserId,stat,end));
            return JsonResult.successJsonResult(userSuggestAction.querySuggestStat(fromUserId,stat,end));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询建议排行榜异常");
        }
    }

}
