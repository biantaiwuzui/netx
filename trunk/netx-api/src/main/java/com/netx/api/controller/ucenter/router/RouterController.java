package com.netx.api.controller.ucenter.router;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.*;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.fuse.biz.ucenter.RouterFuseAction;
import com.netx.fuse.biz.worth.DemandFuseAction;
import com.netx.fuse.biz.worth.SkillFuseAction;
import com.netx.fuse.biz.worth.WishFuseAction;
import com.netx.ucenter.biz.router.NearlyAction;
import com.netx.ucenter.biz.router.RouterAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserProfileAction;
import com.netx.ucenter.vo.router.SearchUserInfoRequestDto;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/router/")
@Api(description = "跨模块用户通用接口")
public class RouterController extends BaseController{

    @Autowired
    private RouterFuseAction routerFuseAction;

    @Autowired
    private RouterAction routerAction;

    @Autowired
    private UserAction userAction;

    @Autowired
    private NearlyAction nearlyAction;

    @Autowired
    private UserProfileAction userProfileAction;

    @Autowired
    private DemandFuseAction demandFuseAction;

    @Autowired
    private WishFuseAction wishFuseAction;

    @Autowired
    private SkillFuseAction skillFuseAction;

    private Logger logger = LoggerFactory.getLogger(RouterController.class);

    //===================  黎子安 start  =====================

    @ApiOperation(value = "获取所有用户id", notes = "用户id集")
    @PostMapping(value = "selectUserIds")
    public JsonResult selectUserIds() {
        try {
            List<String> ids = userAction.getAllUserId();
            for(String id:ids){
                userProfileAction.updateUserProfileScore(id);
            }
            return JsonResult.success().addResult("list",userAction.getAllUserId());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取用户id异常");
        }
    }

    @ApiOperation(value = "注销验证", notes = "用户注销验证")
    @PostMapping(value = "checkDelectUser")
    public JsonResult checkDelectUser(String userId) {
        try {
            Boolean flag = routerFuseAction.checkDeleteUser(userId);
            return flag?JsonResult.success("可以注销"):JsonResult.fail("你有未完成的交易，不能注销");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注销验证异常");
        }
    }

    @ApiOperation(value = "注销用户", notes = "用户注销")
    @PostMapping(value = "delectUser")
    public JsonResult delectUser(String userId) {
        try {
            Boolean flag = routerFuseAction.deleteUser(userId);
            return flag?JsonResult.success("注销成功"):JsonResult.fail("注销失败");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注销异常");
        }
    }

    @Autowired
    ScoreAction scoreAction;
    
    @ApiOperation(value = "test",notes = "补充用户注册信息")
    @PostMapping(value = "test")
    public JsonResult test(String userId,double score) {
        //userAction.test();
        //userAction.testProfile();
        scoreAction.addScore(userId,score);
        return JsonResult.success();
    }

    @ApiOperation(value = "个人其他信息获取",notes = "图文,音频,礼物,邀请的数量")
    @PostMapping(value = "otherNum")
    public JsonResult otherNum(String userId) {
        if(!StringUtils.hasText(userId)){
            return JsonResult.fail("用户id不能为空");
        }
        try {
            return JsonResult.success().addResult("otherNum",routerFuseAction.otherNumByUserId(userId));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取个人其他信息异常");
        }
    }

    @ApiOperation(value = "个人中心-用户信息",notes = "查询自己传token，查询其他用户传userId")
    @GetMapping(value = "selectUserAllData")
    public JsonResult selectUserAllData(String userId,HttpServletRequest request) {
        String fromUserId = null;
        //判断是否查看自己
        if(StringUtils.isEmpty(userId)){
            try {
                userId = getUserId(request);
                if(userId == null){
                    return JsonResult.fail("授权过期，请重新登录");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }else{
            try {
                fromUserId = getUserId(request);
            }catch (Exception e){
                logger.warn(e.getMessage());
                fromUserId = null;
            }
        }
        try{
            return JsonResult.success().addResult("userBeanData",routerFuseAction.getUserBeanDataMap(userId,fromUserId));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户信息获取失败");
        }
    }

    @ApiOperation(value = "个人中心-商品",notes = "查询自己传token，查询其他用户传userId")
    @GetMapping(value = "queryUserAllDataByProduct")
    public JsonResult queryUserAllDataByProduct(String userId,HttpServletRequest request) {
        try {
            userId = getUserId(userId,request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            return JsonResult.success().addResult("userProduct",routerFuseAction.getUserProductOne(userId, new BigDecimal(getLat(request)), new BigDecimal(getLon(request))));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户商品获取失败");
        }
    }

    @ApiOperation(value = "个人中心-活动",notes = "查询自己传token，查询其他用户传userId")
    @GetMapping(value = "selectUserAllDataByMeeting")
    public JsonResult selectUserAllDataByMeeting(String userId,HttpServletRequest request) {
        try {
            userId = getUserId(userId,request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            Double lon = getLon(request);
            Double lat = getLat(request);
            return JsonResult.success().addResult("userMeeting",routerFuseAction.getMeetingDto(userId,lon,lat));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户活动获取失败");
        }
    }

    @ApiOperation(value = "个人中心-需求",notes = "查询自己传token，查询其他用户传userId")
    @GetMapping(value = "selectUserAllDataByDemand")
    public JsonResult selectUserAllDataByDemand(String userId,HttpServletRequest request) {
        try {
            userId = getUserId(userId,request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            double lon =  getLon ( request );
            double lat =  getLat ( request );
            return JsonResult.success().addResult("userDemand",routerFuseAction.getDemandOne(userId,lat,lon));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户需求获取失败");
        }
    }

    @ApiOperation(value = "个人中心-网信",notes = "查询自己传token，查询其他用户传userId")
    @GetMapping(value = "selectUserAllDataByCredit")
    public JsonResult selectUserAllDataByCredit(String userId,HttpServletRequest request) {
        try {
            userId = getUserId(userId,request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            CommonPageDto commonPageDto = initCommonPageDto(1,99999);
            commonPageDto.setUserId(userId);
            return JsonResult.success().addResult("userCredit",routerFuseAction.getCurrencyDetailData(userId, true));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户网信获取失败");
        }
    }

    @ApiOperation(value = "个人中心-心愿",notes = "查询自己传token，查询其他用户传userId")
    @GetMapping(value = "selectUserAllDataByWish")
    public JsonResult selectUserAllDataByWish(String userId,HttpServletRequest request) {
        boolean isPublish = true;
        if(StringUtils.isEmpty(userId)){
            try {
                userId = getUserId(request);
                if(userId == null){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            }catch (Exception e){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }else{
            try {
                String fromUserId = getUserId(request);
                if(!StringUtils.isEmpty(fromUserId) && !fromUserId.equals(userId)){
                    isPublish = false;
                }
            }catch (Exception e){
                logger.warn(e.getMessage());
                isPublish=false;
            }
        }
        try {
            String fromUserId = getUserId(userId,request);
            if(StringUtils.isEmpty(fromUserId)){
                return JsonResult.fail("用户id不能为空");
            }
            return JsonResult.success().addResult("userWish",routerFuseAction.getWishOne(userId,isPublish));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户心愿获取失败");
        }
    }

    @ApiOperation(value = "个人中心-技能",notes = "查询自己传token，查询其他用户传userId")
    @GetMapping(value = "selectUserAllDataBySkill")
    public JsonResult selectUserAllDataBySkill(String userId, HttpServletRequest request) {
        try {
            userId = getUserId(userId,request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            BigDecimal lat =new BigDecimal(getLat(request));
            BigDecimal lon =new BigDecimal(getLon(request));
            return JsonResult.success().addResult("userSkill",routerFuseAction.getSkillOne(userId,lat,lon));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户技能获取失败");
        }
    }

    //分割线

    @ApiOperation(value = "个人中心-商家",notes = "查询自己传token，查询其他用户传userId")
    @GetMapping(value = "selectUserAllDataBySeller")
    public JsonResult selectUserAllDataBySeller(String userId,HttpServletRequest request) {
        try {
            userId = getUserId(userId,request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            return JsonResult.success().addResult("userSeller", routerFuseAction.getSellerDetailByUserIdVo(userId, getLat(request), getLon(request)));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("用户商家获取失败");
        }
    }

    private CommonPageDto initCommonPageDto(int page,int size){
        CommonPageDto commonPageDto = new CommonPageDto();
        commonPageDto.setSize(size);
        commonPageDto.setCurrentPage(page);
        return commonPageDto;
    }

    @ApiOperation(value = "用户图文与音频数",notes = "个人信息统计")
    @PostMapping(value = "selectUcenterArticleNumByUser")
    public JsonResult selectUcenterArticleNumByUser(String userId) {
        if(!StringUtils.hasText(userId)) {
            return JsonResult.fail("用户id不能为空");
        }
        try {
            return JsonResult.success().addResult("num",routerAction.articleNumByUserId(userId));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "网币信息统计(网币发行者的图文、音频、活动、技能、商家、商品数)" ,notes = "前端传参例子:<br/>"+
            "["+"{\"sellerIds\":\"string\",\"userId\":\"string\"},"+
            "{\"sellerIds\":\"string\",\"userId\": \"string\"}"+"]")
    @PostMapping(value = "/selectCurrencyUserDataList")
    public JsonResult selectCurrencyUserDataList(@RequestBody List<UserAndSellersRequestDto> userAndSellersRequestDtos) {
        if(userAndSellersRequestDtos.isEmpty()){
            return JsonResult.fail("查询信息不能为空");
        }
        try {
            return JsonResult.success().addResult("creditNums",routerFuseAction.selectCurrencyUserDataDtos(userAndSellersRequestDtos));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取网币信息统计异常");
        }
    }

    @ApiOperation(value = "网币信息统计(网币发行者的图文、音频、活动、技能、商家、商品数)(Map)" ,notes = "前端传参例子:<br/>"+
            "["+"{\"sellerIds\":\"string\",\"userId\":\"string\"},"+
            "{\"sellerIds\":\"string\",\"userId\": \"string\"}"+"]")
    @PostMapping(value = "selectCurrencyUserDataMapList")
    public JsonResult selectCurrencyUserDataMapList(@RequestBody List<UserAndSellersRequestDto> userAndSellersRequestDtos) {
        if(userAndSellersRequestDtos.isEmpty()){
            return JsonResult.fail("信息查询不能为空");
        }try{
            return JsonResult.success().addResult("creditNums",routerFuseAction.selectCurrencyUserDataMap(userAndSellersRequestDtos));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取网币信息统计异常");
        }
    }

    @ApiOperation(value = "查找附近的登录用户",notes = "按经纬度查找登录用户列表")
    @PostMapping(value = "selectNearbyLogin")
    public JsonResult selectNearbyLogin(@Valid @RequestBody GeomRequestDto geomRequestDto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        try{
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            List<UserSynopsisData> responses = nearlyAction.selectNearlyLogin(userGeo.getUserId(),userGeo.getLon(),userGeo.getLat(),geomRequestDto.getCurrentPage(),geomRequestDto.getSize());
            return JsonResult.success().addResult("list",responses);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查找附近登录用户异常");
        }
    }

    @ApiOperation(value = "查找附近的用户id",notes = "按经纬度查找用户列表")
    @PostMapping(value = "selectNearbyGeom")
    public JsonResult selectNearbyByGeom(HttpServletRequest request){
        UserGeo userGeo = new UserGeo();
        try{
            userGeo = getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        if(userGeo.getLon()==0 || userGeo.getLat()==0){
            return JsonResult.fail("经纬度不能为空");
        }
        try{
            return JsonResult.success().addResult("list",nearlyAction.querUserIds(userGeo.getUserId(),userGeo.getLon(),userGeo.getLat()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查找附近的用户id异常");
        }
    }

    @ApiOperation(value = "查找附近的用户基本信息(list)",notes = "按经纬度查找用户列表")
    @PostMapping(value = "selectNearUserListByGeom")
    public JsonResult selectNearUserListByGeom(@Valid @RequestBody GeomRequestDto geomRequestDto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        userGeo.setLon(getLon(request));
        userGeo.setLat(getLat(request));
        try {
            userGeo.setUserId(getUserId(request));
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        if(userGeo.getLat()==null || userGeo.getLon()==null){
            return JsonResult.fail("请开启定位再操作");
        }
        try{
            List<UserSynopsisData> list = nearlyAction.selectNearUserList(userGeo.getUserId(),userGeo.getLon(),userGeo.getLat(),geomRequestDto.getCurrentPage(),geomRequestDto.getSize());
            return JsonResult.success().addResult("list",list);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取附近用户的基本信息异常");
        }
    }

    @ApiOperation(value = "查找的用户基本信息(Map)",notes = "按距离、在线时间、性别查找用户列表")
    @PostMapping(value = "searchGetUserInfoMap")
    public JsonResult searchGetUserInfoMap(@RequestBody SearchUserInfoRequestDto dto,HttpServletRequest request){
        Double lon = null;
        Double lat = null;
        if(dto.getRedaius()!=null){
            lon = getLon(request);
            lat = getLat(request);
            if (lon > 180 || lon < -180) return JsonResult.fail("输入的经度不合法");
            if (lat > 85 || lat < -85) return JsonResult.fail("输入的纬度不合法");
        }try{
            return successResult(routerAction.searchGetUserInfoMap(lon,lat,dto.getRedaius(),dto.getOnline(),dto.getSex()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取用户基本信息异常");
        }
    }
    @ApiOperation(value = "查找的用户基本信息(list)",notes = "按距离、在线时间、性别查找用户列表")
    @PostMapping(value = "searchGetUserInfo")
    public JsonResult searchGetUserInfo(@RequestBody SearchUserInfoRequestDto dto,HttpServletRequest request){
        Double lon = null;
        Double lat = null;
        if(dto.getRedaius()!=null){
            lon = getLon(request);
            lat = getLat(request);
            if (lon > 180 || lon < -180) return JsonResult.fail("输入的经度不合法");
            if (lat > 85 || lat < -85) return JsonResult.fail("输入的纬度不合法");
        }try{
            return JsonResult.success().addResult("list",routerAction.searchGetUserInfo(lon,lat,dto.getRedaius(),dto.getOnline(),dto.getSex()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取用户基本信息异常");
        }
    }
/*
    @ApiOperation(value = "查找的用户基本信息(list)",notes = "按距离、在线时间、性别查找用户列表")
    @PostMapping(value = "searchGetUserInfo")
    public JsonResult searchGetUserInfo(@RequestBody SearchUserInfoRequestDto dto,HttpServletRequest request){
        Double lon = null;
        Double lat = null;
        if(dto.getRedaius()!=null){
            lon = getLon(request);
            lat = getLat(request);
            if (lon > 180 || lon < -180) return JsonResult.fail("输入的经度不合法");
            if (lat > 85 || lat < -85) return JsonResult.fail("输入的纬度不合法");
        }try{
            return JsonResult.success().addResult("list",routerAction.searchGetUserInfo(lon,lat,dto.getRedaius(),dto.getOnline(),dto.getSex()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取用户基本信息异常");
        }
    }*/
    //===================  黎子安 end  =====================

    //=================== 李卓 start =======================

    @ApiOperation(value = "选择查询单个用户信息")
    @PostMapping(value = "selectUserInfo")
    public JsonResult selectUserInfo(@Valid @RequestBody UserInfoRequestDto userInfoRequestDto) {
        try{
            UserInfoResponseDto userInfoResponseDto = routerAction.selectUserInfo(userInfoRequestDto.getSelectData(), userInfoRequestDto.getSelectConditionEnum(), userInfoRequestDto.getSelectFieldEnumList());
            return JsonResult.success().addResult("userInfo",userInfoResponseDto);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常,请检查入参的数据是否存在或是否合法");
        }
    }

    @ApiOperation(value = "选择查询多个用户信息")
    @PostMapping(value = "selectUserInfoList")
    public JsonResult selectUserInfoList(@Valid @RequestBody UserInfoListRequestDto userInfoListRequestDto) {
        try{
            List<UserInfoResponseDto> list = routerAction.selectUserInfoList(userInfoListRequestDto.getSelectDataList(), userInfoListRequestDto.getSelectConditionEnum(), userInfoListRequestDto.getSelectFieldEnumList());
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常,请检查入参的数据是否存在或是否合法，可能数据列表含有不存在的数据");
        }
    }
}
