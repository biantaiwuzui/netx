package com.netx.api.controller.ucenter.router;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.searchengine.enums.FriendTypeEnum;
import com.netx.ucenter.biz.router.NearlyAction;
import com.netx.ucenter.vo.request.FriendTypePageRequestDto;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/nearly/")
@Api(description = "附近用户通用接口")
public class NearlyController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(NearlyController.class);

    @Autowired
    private NearlyAction nearlyAction;

    @ApiOperation(value = "网友搜索",notes = "按类型搜索网友<br>" +
            "校 友：SCHOOL_TYPE<br>" +
            "同 事：COMPANY_TYPE<br>" +
            "美 丽：BEAUTIFUL_TYPE<br>" +
            "帅 气：HANDSOME_TYPE<br>" +
            "高 管：EXECUTIVE_TYPE<br>" +
            "多 金：RICH_TYPE<br>" +
            "未 婚：UNMARRIED_TYPE<br>" +
            "博 学：ERUDITE_TYPE<br>" +
            "同龄之缘：SAME_AGE_TYPE<br>" +
            "共同爱好：COMMON_HOBBY_TYPE<br>" +
            "同乡近邻：SAME_HOMETOWN_TYPE")
    @PostMapping("getUserByType")
    public JsonResult getUserByType(@Valid @RequestBody FriendTypePageRequestDto dto, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        FriendTypeEnum friendTypeEnum = dto.getFriendTypeEnum();
        try {
            userGeo = getGeoFromRequest(request);
            if(friendTypeEnum.getLogin() && StringUtils.isBlank(userGeo.getUserId())){
                return JsonResult.fail("授权过期，请重新登录后再操作", ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            if(friendTypeEnum.getLogin()){
                return JsonResult.fail(e.getMessage(),ApiCode.NO_AUTHORIZATION);
            }
            logger.warn(e.getMessage());
        }
        try {
            return JsonResult.success().addResult("list", nearlyAction.queryFriendsByType(friendTypeEnum, userGeo.getUserId(), userGeo.getLon(), userGeo.getLat(), dto.getCurrent(), dto.getSize()));
        }catch (Exception e){
            logger.error("查询异常："+e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }
}
