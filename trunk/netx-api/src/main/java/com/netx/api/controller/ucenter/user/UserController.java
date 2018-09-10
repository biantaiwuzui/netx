package com.netx.api.controller.ucenter.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.base.Strings;
import com.netx.api.component.UserTokenState;
import com.netx.api.controller.BaseController;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.router.dto.bean.UserBeanResponseDto;
import com.netx.common.router.dto.bean.UserPhotosResponseDto;
import com.netx.common.user.enums.RegularExpressionEnum;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.biz.common.AliyunPictureAction;
import com.netx.ucenter.biz.user.*;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserOauth;
import com.netx.ucenter.vo.request.*;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Api(description = "用户模块")
@RestController
@RequestMapping("/api/user/")
public class  UserController extends BaseController{
    //===================  公用属性 Start  =====================
    @Autowired
    private UserAction userAction;//用户信息服务
    @Autowired
    private UserOauthAction userOauthAction;//第三方服务
    @Autowired
    private UserPhotoAction userPhotoAction;//用户照片服务

    @Autowired
    private UserProfileAction userProfileAction;

    private Integer getAge(Date birthday){
        return ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    Logger logger = LoggerFactory.getLogger(UserController.class);
    //===================  公用属性  end   =====================

    //======================== 黎子安 start ========================

    @ApiOperation(value = "注册", notes = "添加用户信息")
    @PostMapping(value = "add")
    public JsonResult add(@Valid @RequestBody AddWzUserLoginRequest userVo, BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }else{
            try {
                User user= userAction.register(userVo);
                if(user!=null){
                    if(user.getId()==null){
                        return JsonResult.fail("注册异常");
                    }
                    if(user.getMobile()==null){
                        return JsonResult.fail("注册失败,手机号码已存在");
                    }
                    if(user.getNickname()==null){
                        return JsonResult.fail("注册失败,用户昵称已存在");
                    }
                    String result = userPhotoAction.addHeadImg(user.getId(),userVo.getHeadImg());
                    userProfileAction.updateUserProfileScore(user.getId());
                    if(!result.equals("")) result=",但"+result;
                    return JsonResult.success("注册成功"+result);
                }
                return JsonResult.fail("注册失败");
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                return JsonResult.fail("注册异常");
            }
        }
    }

    @ApiOperation(value = "授权", notes = "授权验证")
    @PostMapping(value = "queryOauth")
    public JsonResult queryOauth(@Valid @RequestBody OauthRequestDto dto,BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            UserOauth userOauth = userOauthAction.queryOauth(dto.getOpenId(),dto.getType());
            if(userOauth==null){
                return JsonResult.fail("授权失败");
            }
            return JsonResult.success().addResult("oauth",userOauth);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("授权异常");
        }
    }

    @ApiOperation(value = "第一次授权登录", notes = "第三方登录")
    @PostMapping(value = "fristOauthLogin")
    public JsonResult fristOauthLogin(@Valid @RequestBody OauthLoginRequetDto dto,BindingResult bindingResult,Device device) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            User user = userAction.fristOauthUser(dto);
            return oauthLogin(user,dto.getLon(),dto.getLat(),device);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("授权登录异常");
        }
    }

    @ApiOperation(value = "授权登录", notes = "第三方登录")
    @PostMapping(value = "oauthLogin")
    public JsonResult oauthLogin(@Valid @RequestBody OauthLoginBean dto,BindingResult bindingResult,Device device) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (StringUtils.isBlank(dto.getUserId())){
            return JsonResult.fail("用户id不能为空");
        }
        try {
            if(userOauthAction.getUserOauthService().countOauth(dto.getOauthId(),dto.getUserId(),1)<1){
                return JsonResult.fail("授权登录失败");
            }
            User user = userAction.getUserService().selectById(dto.getUserId());
            return oauthLogin(user,dto.getLon(),dto.getLat(),device);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("授权登录异常");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    JsonResult oauthLogin(User user, BigDecimal lon,BigDecimal lat,Device device){
        if(user==null){
            return JsonResult.fail("授权登录失败");
        }
        if(user.getId()==null){
            return JsonResult.fail("授权登录异常");
        }
        user= userAction.successLogin(user,new Date(),lon,lat);
        return user==null?JsonResult.fail("登录失败！"):loginRule(user,device);
    }

    @ApiOperation(value = "普通登录", notes = "登录验证")
    @PostMapping(value = "login")
    public JsonResult login(@Valid @RequestBody WzUserLoginRequest dto, BindingResult bindingResult,Device device,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {

            User user= userAction.loginUser(dto);
            if (user == null) {
                return JsonResult.fail("登录失败！账号或密码错误");
            } else if (Strings.isNullOrEmpty(user.getPassword())) {
                return JsonResult.fail("您还没设置密码，请使用手机或第三方登录之后，设置密码即可。");
            } else {
                return loginRule(user,device);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("登录异常");
        }
    }

    private JsonResult loginRule(User user,Device device){
        if(StringUtils.isBlank(user.getId())){
            return JsonResult.fail("系统异常");
        }else if(user.getLock()!=null && user.getLock()){
            return JsonResult.fail("登录失败！账号已经被封");
        }else{
            //生成jwt
            String jws = tokenHelper.generateToken(user.getId(),device);
            //过期时间多大
            int expiresIn = tokenHelper.getExpiredIn(device);
            //设置缓存
            userAction.setToken(user.getId(),jws);
            //返回用户信息,返回token,返回token过期时间
            return JsonResult.success().addResult("userInfo",userToUserBeanResponseDto(user)).addResult("token", new UserTokenState(jws, expiresIn));
        }
    }

    private UserBeanResponseDto userToUserBeanResponseDto(User user){
        UserBeanResponseDto userBeanResponseDto = VoPoConverter.copyProperties(user, UserBeanResponseDto.class);
        userBeanResponseDto.setIsAdminUser(user.getAdminUser());
        userBeanResponseDto.setAge(getAge(user.getBirthday()));
        userBeanResponseDto.setRegJMessage(user.getRegJMessage());
        userBeanResponseDto.setJmessagePassword(user.getJmessagePassword());
        try {
            UserPhotosResponseDto photosResponseDto = userPhotoAction.selectUserPhotos(user.getId());
            if(photosResponseDto!=null){
                userBeanResponseDto.setHeadImgUrl(photosResponseDto.getHeadImgUrl());
                userBeanResponseDto.setImgUrls(photosResponseDto.getImgUrls());
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        userBeanResponseDto.setIsPWD(StringUtils.isNotBlank(user.getPassword()));
        userBeanResponseDto.setIsPayPWD(StringUtils.isNotBlank(user.getPayPassword()));
        userBeanResponseDto.setIsAdminPWD(StringUtils.isNotBlank(user.getAdminPassword()));
        return userBeanResponseDto;
    }
/*
    @ApiOperation(value = "第三方登录", notes = "授权登录")
    @PostMapping(value = "otherLogin")
    public JsonResult otherLogin(@Valid @RequestBody OtherLoginRequest otherLoginRequest, BindingResult bindingResult,Device device,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Date time = new Date();
        int type = otherLoginRequest.getType();
        try {
            User user= userAction.queryOauthUser(otherLoginRequest.getNickName(),toSex(type,otherLoginRequest.getSex()),otherLoginRequest.getLon(),otherLoginRequest.getLat(),otherLoginRequest.getOpenId(),otherLoginRequest.getType(),otherLoginRequest.getHeadImg());
            if(user==null){
                return JsonResult.fail("授权失败");
            }
            user= userAction.successLogin(user,time,otherLoginRequest.getLon(),otherLoginRequest.getLat());
            return user==null?JsonResult.fail("登录失败！"):loginRule(user,device);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("登录异常");
        }
    }*/

    /**
     * "登录类型：\n" +
     "1.微信\n" +
     "2.支付宝\n" +
     "3.微博\n" +
     "4.QQ")
     * @param type
     * @param sex
     * @return
     */
    private String toSex(int type,String sex){
        if(type==1 || type==3){
            return sex.equals("2")?"女":"男";
        }
        if(type==2){
            return sex.equals("F")?"女":"男";
        }
        return sex;
    }




    @ApiOperation(value = "验证是否登录")
    @PostMapping(value = "checkLogin")
    public JsonResult checkLogin(String userId,HttpServletRequest request) {
        try {
            userId = getUserId(userId,request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            return JsonResult.success().addResult("state",userAction.getUserService().checkUser(userId));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("验证异常");
        }
    }

    @ApiOperation(value = "后台登录", notes = "后台密码验证")
    @PostMapping(value = "adminLogin")
    public JsonResult adminLogin(@Valid @RequestBody WzUserLoginRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }else{
            try {
                String result = userAction.loginAdmin(request);
                return super.getResult(request==null,result);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                return JsonResult.fail("后台密码验证异常");
            }
        }
    }

    @ApiOperation(value = "用户退出")
    @PostMapping(value = "exit")
    public JsonResult exit(Boolean type ,HttpServletRequest request) {
        if(type==null){
            return JsonResult.fail("退出方式不能为空");
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null){
                return JsonResult.fail("授权过期，请重新登录", ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage(), ApiCode.NO_AUTHORIZATION);
        }
        try {
            String result = "";
            User user = userAction.getUserService().selectById(userId);
            if(type){
                user.setLoginBackend(false);
                result="后台";
            }else {
                user.setLogin(false);
            }
            return super.getResult(userAction.updateUserById(user),result+"退出成功！",result+"退出失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("退出登录异常");
        }
    }

    @ApiOperation(value = "密码验证", notes = "密码验证")
    @PostMapping(value = "checkPwd")
    public JsonResult checkPayPwd(@Valid @RequestBody WzPassWordBaseRequest dto, BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(dto.getUserId())){
            try {
                dto.setUserId(getUserId(request));
                if(StringUtils.isEmpty(dto.getUserId())){
                    return JsonResult.fail("授权过期，请重新登录");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            //request.setPassword(encoderPassword(request.getPassword()));
            return super.getResult(userAction.checkPwd(dto),"验证成功","密码错误");
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.fail("密码验证异常");
        }

    }

    @ApiOperation(value = "密码修改", notes = "密码类型：登录、钱包、后台")
    @PostMapping(value = "updatePassWord")
    public JsonResult updatePassWord(@Valid @RequestBody WzPassWordRequest dto, BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(dto.getUserId())){
            try {
                dto.setUserId(getUserId(request));
                if(StringUtils.isEmpty(dto.getUserId())){
                    return JsonResult.fail("授权过期，请重新登录");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            //request.setOldPassword(encoderPassword(request.getOldPassword()));
            //request.setPassword(encoderPassword(request.getPassword()));
            String result = userAction.updatePWD(dto);
            return result==null?JsonResult.success("密码修改成功"):JsonResult.fail(result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("密码修改异常");
        }

    }

    @ApiOperation(value = "用户信息", notes = "根据手机号码查询")
    @GetMapping(value = "selectUser/{mobile}")
    public JsonResult selectUser(@PathVariable String mobile) {
        if (!StringUtils.isEmpty(mobile) && !mobile.matches(RegularExpressionEnum.MOBILE.getValue())) {
            return JsonResult.success("非法手机号码");
        }
        try {
            User user = userAction.selectUserByMobile(mobile,null);
            if(user==null)return JsonResult.fail("手机号码未注册");
            return JsonResult.success().addResult("user",userToUserBeanResponseDto(user));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("按手机号查询异常");
        }
    }

    @ApiOperation(value = "根据手机号码模糊查询", notes = "用户信息集")
    @PostMapping(value = "selectUsers")
    public JsonResult selectUsers(@Valid @RequestBody SelectUserByMobileRequestDto requestDto,BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        try{
            userGeo = getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            return JsonResult.success().addResult("list", userAction.selectUserByMobiles(requestDto.getValue(),2,createPage(requestDto.getCurrent(),requestDto.getSize()),userGeo.getLon(),userGeo.getLat(),userGeo.getUserId()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }


    private Page<User> createPage(Integer current,Integer size){
        if(current == null || size == null){
            return null;
        }
        Page<User> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        return page;
    }


    @ApiOperation(value = "根据网号模糊查询", notes = "用户信息集")
    @PostMapping(value = "selectUsersByUserNumber")
    public JsonResult selectUsersByUserNumber(@Valid @RequestBody SelectUserByMobileRequestDto requestDto,BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        try{
            userGeo = getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            return JsonResult.success().addResult("list", userAction.selectUserByMobiles(requestDto.getValue(),1,createPage(requestDto.getCurrent(),requestDto.getSize()),userGeo.getLon(),userGeo.getLat(),userGeo.getUserId()));
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "根据手机号码及网号模糊查询", notes = "用户信息集")
    @PostMapping(value = "selectUsersByNumberOrMobile")
    public JsonResult selectUsersByNumberOrMobile(@Valid @RequestBody SelectUserByMobileRequestDto requestDto,BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        try{
            userGeo = getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            return JsonResult.success().addResult("list", userAction.selectUserByMobiles(requestDto.getValue(),0,createPage(requestDto.getCurrent(),requestDto.getSize()),userGeo.getLon(),userGeo.getLat(),userGeo.getUserId()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "根据用户id集查询用户信息")
    @PostMapping(value = "/selectUserByIds")
    public JsonResult selectUserByIds(@RequestBody List<String> userId,HttpServletRequest request) {
        if(userId.size()==0){
            return JsonResult.fail("用户id集不能为空");
        }
        UserGeo userGeo = new UserGeo();
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            return JsonResult.success().addResult("list", userAction.getUserSynopsisDataList(userId,null,userGeo.getUserId(),userGeo.getLon(),userGeo.getLat()));
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.fail("查询异常");
        }

    }

    @ApiOperation(value = "根据用户id集查询用户信息(Map)")
    @PostMapping(value = "selectUserMapByIds")
    public JsonResult selectUserMapByIds(@RequestBody List<String> userId,HttpServletRequest request) {
        if(userId.size()==0){
            return JsonResult.fail("用户id集不能为空");
        }
        UserGeo userGeo = new UserGeo();
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            return JsonResult.success().addResult("map", userAction.getUserSynopsisDataMap(userId,userGeo.getLon(),userGeo.getLat(),userGeo.getUserId()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    //======================== 黎子安 end ========================

    @ApiOperation(value = "根据好友网号查询好友信息",notes = "需要登录")
    @GetMapping(value = "queryUserFriendsByUserNumber")
    public JsonResult queryUserFriendsByUserNumber(String userNumber,HttpServletRequest request) {
        String userId = null;
        try {
            userId = getUserId(request);
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(StringUtils.isBlank(userId)){
            return JsonResult.fail("请重新登录后再继续操作");
        }
        if(StringUtils.isBlank(userNumber)){
            return JsonResult.fail("好友网号不能为空");
        }
        try {
            return JsonResult.success().addResult("result", userAction.queryUserFriendsByUserNumber(userId, userNumber));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("网号查询用户失败");
        }
    }

//   @ApiOperation("获取用户token")
//    @GetMapping("/getToken")
//    public JsonResult getToken(String user_Id,Device device){
//        //生成jwt
//        String jws = tokenHelper.generateToken(user_Id,device);
//        //过期时间多大
//        int expiresIn = tokenHelper.getExpiredIn(device);
//        //设置缓存
//        userAction.setToken(user_Id,jws);
//        return JsonResult.success(new UserTokenState(jws, expiresIn).getAccess_token());
//    }

}
