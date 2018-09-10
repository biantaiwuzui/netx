package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.ucenter.biz.user.UserSuggestAction;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(description = "用户建议模块")
@RestController
@RequestMapping("/api/userSuggest/")
public class UserSuggestController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserSuggestController.class);

    @Autowired
    private UserSuggestAction userSuggestAction;

    @ApiOperation(value = "添加建议")
    @PostMapping("add")
    public JsonResult add(String content, HttpServletRequest request){
        if(StringUtils.isBlank(content)){
            return JsonResult.fail("建议内容不能为空");
        }
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return getResult(userSuggestAction.add(userId,content),"反馈建议失败");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("反馈建议异常");
        }
    }
}
