package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.user.enums.MobileSmsCode;
import com.netx.common.user.enums.RegularExpressionEnum;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.ucenter.biz.user.UserVerificationCodeAction;
import com.netx.ucenter.model.user.UserVerificationCode;
import com.netx.common.user.util.MobileMessage;
import com.netx.ucenter.vo.request.WzMobileCodeRequest;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(description = "手机验证码模块")
@RestController
@RequestMapping("/api/MobileCode/")
public class UserMobileController extends BaseController{
    //===================  公用属性 start  =====================
    @Autowired
    UserVerificationCodeAction userVerificationCodeService;

    @Autowired
    MobileMessage mobileMessage;


    private Logger logger = LoggerFactory.getLogger(UserMobileController.class);
    //===================  公用属性  end   =====================

    //======================== 黎子安 start ========================

    @ApiOperation(value = "获取验证码", notes = "手机号码验证")
    @PostMapping(value = "sendCode")
    public JsonResult getMobileCode(@RequestParam("mobile") String mobile, HttpServletRequest request) {
        if(mobile==null){
            return JsonResult.fail("手机号码不能为空");
        }
        if(!mobile.matches(RegularExpressionEnum.MOBILE.getValue())){
            return JsonResult.fail("手机号码格式不正确");
        }
        try {
            String userId = null;
            try {
                userId = getUserId(request);
            }catch (Exception e){
                logger.warn(e.getMessage());
            }
            UserVerificationCode userVerificationCode= userVerificationCodeService.getMobileCode(userId,mobile);
            if(userVerificationCode==null){
                return JsonResult.fail("获取验证码失败");
            }
           /* Map<String,Object> map1=new HashMap<>();
            map1.put("date", DateTimestampUtil.getDateStrByDate(new Date()));
            map1.put("type","身份认证");
            mobileMessage.send(userVerificationCode.getMobile(),map1, MobileSmsCode.MS_VERIFY_SUCESS);
            map1.put("reason","照片不符合");
            mobileMessage.send(userVerificationCode.getMobile(),map1, MobileSmsCode.MS_VERIFY_FAIL);*/
            return super.getResult(mobileMessage.send(userVerificationCode.getMobile(),userVerificationCode.getCode())==1,"获取验证码失败!");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "验证验证码", notes = "手机号码验证")
    @PostMapping(value = "checkMobileCode")
    public JsonResult checkMobileCode(@Valid @RequestBody WzMobileCodeRequest response, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try{
            switch (userVerificationCodeService.checkMobileCode(response,userId)){
                case 0:
                    return JsonResult.fail("手机验证失败");
                case 1:
                    return JsonResult.success("验证成功");
                case 2:
                    return JsonResult.fail("验证码过期");
                case 3:
                    return JsonResult.fail("验证码已经被验证过，请重新获取验证码！");
            }
            return JsonResult.fail("你暂无验证码");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        }
    }
    //======================== 黎子安 end ========================
}