package com.netx.api.controller.worth;

import com.netx.api.controller.BaseController;
import com.netx.common.wz.dto.common.WorthIndexRequestDto;
import com.netx.common.wz.dto.common.WorthSearchRequestDto;
import com.netx.utils.json.ApiCode;
import com.netx.worth.biz.common.IndexAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netx.common.redis.model.UserGeo;
import com.netx.utils.json.JsonResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(description = "首页模块")
@RequestMapping("/wz/wz")
@RestController
public class IndexController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(DemandController.class);

    @Autowired
    private IndexAction indexAction;

    /**
     * @author 黎子安
     * @param requestDto
     * @param request
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "网能首页",notes = "Credit_Type：信用最高<br>"
            +"Hold_Credit_Type：支持网信<br>"
            +"Newly_Publish_Type：最新发布<br>"
            +"Watch_Type：最受关注<br>"
            +"Recommend_Type：精心推荐<br>"
            +"Shape_Happy_Type：齐享欢乐<br>"
            +"Only_You_Type：只需要你<br>"
            +"Supply_Type：供不应求<br>"
            +"Disconnection_Type：齐力断金<br>"
            +"For_You_Type：为你推荐"
            +"Top_Match_Event： 热门赛事")
    @PostMapping(value = "/index")
    public JsonResult index(@RequestBody @Valid WorthIndexRequestDto requestDto, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Double lon = getLon(request);
        Double lat = getLat(request);
        try {
            return JsonResult.success().addResult("list",indexAction.queryIndex(requestDto.getWorthIndexTypeEnum(),requestDto.getCurrent(),requestDto.getSize(),lon,lat));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("查询异常");
        }
    }

    /**
     * @author 黎子安
     * @param requestDto
     * @param request
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "网能搜索")
    @PostMapping(value = "/search")
    public JsonResult search(@RequestBody @Valid WorthSearchRequestDto requestDto, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Double lon = getLon(request);
        Double lat = getLat(request);
        try {
            return JsonResult.success().addResult("list",indexAction.searchWorth(requestDto.getWorthTypeEnum(),requestDto.getTitle(),requestDto.getSort(),requestDto.getCurrent(),requestDto.getSize(),lon,lat));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("搜索异常");
        }

    }

    /**
     * @author 黎子安
     * @param request
     * @return
     */
    @ApiOperation(value = "网能最近参加一次记录")
    @PostMapping(value = "/getNewlyHistory")
    public JsonResult getNewlyHistory(HttpServletRequest request) {
        UserGeo userGeo = null;
        try{
            userGeo = getGeoFromRequest(request);
            if(StringUtils.isEmpty(userGeo.getUserId())){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return JsonResult.success().addResult("history",indexAction.getHistory(userGeo.getUserId(),userGeo.getLon(),userGeo.getLat()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("搜索异常");
        }

    }

}
