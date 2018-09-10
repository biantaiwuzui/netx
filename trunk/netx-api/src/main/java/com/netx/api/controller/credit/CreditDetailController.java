package com.netx.api.controller.credit;



import com.netx.api.controller.BaseController;
import com.netx.credit.biz.UserCreditLikesAction;
import com.netx.credit.vo.CreditDetailDto;
import com.netx.fuse.biz.credit.CreditDetailFuseAction;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

@RestController
@RequestMapping("/api/creditDetail")
@Api(description = "网信详情模块")
public class CreditDetailController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(CreditController.class);

    @Autowired
    private CreditDetailFuseAction creditDetailFuseAction;

    @Autowired
    private UserCreditLikesAction userCreditLikesAction;

    @ApiOperation(value = "网信详情" ,notes = "根据credit表id查询网信详情")
    @GetMapping("/getDetail")
    public JsonResult getDetail(String creditId, HttpServletRequest request){

        if (StringUtils.isEmpty(creditId)) {
            return JsonResult.fail("网信Id不能为空");
        }
        try {
            String userId = getUserId(request);
            if (StringUtils.isEmpty(userId)) {
                return JsonResult.fail("授权过期，请重新登录");
            }
            CreditDetailDto creditDetailDto = creditDetailFuseAction.getCreditDetail(creditId, userId);
            if (creditDetailDto != null) {
                return JsonResult.success().addResult("creditDetail", creditDetailDto);
            } else {
                return JsonResult.fail("获取的网信不存在");
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage() + creditId,e);
            return JsonResult.fail("获取网信详情异常");
        }
    }

    @ResponseBody
    @PostMapping("likeCredit")
    @ApiOperation(value = "网信评论点赞", notes = "返回1代表点赞成功，返回2代表取消点赞")
    public JsonResult likeCredit(String userId, String creditId){
        if(!StringUtils.hasText(userId)){
            return JsonResult.fail("用户id不能为空");
        }
        if(!StringUtils.hasText(creditId)){
            return JsonResult.fail("网信id不能为空");
        }
        try{
            return getResult(userCreditLikesAction.likeCredit(userId,creditId),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("点赞咨询异常");
        }
    }

}
