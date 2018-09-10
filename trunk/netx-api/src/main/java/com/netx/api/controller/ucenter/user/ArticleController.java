package com.netx.api.controller.ucenter.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.router.dto.request.GeomRequestDto;
import com.netx.common.user.dto.article.*;
import com.netx.common.user.dto.article.ArticlePayForCompanyDto;
import com.netx.common.user.enums.WhoCanEnum;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.fuse.biz.ucenter.ArticleFuseAction;
import com.netx.ucenter.biz.user.ArticleCollectAction;
import com.netx.ucenter.biz.user.ArticleLikesAction;
import com.netx.ucenter.biz.user.ArticleAction;
import com.netx.ucenter.model.user.Article;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/article/")
@Api(description = "咨讯模块（图文、音视）")
public class ArticleController extends BaseController{
    @Autowired
    private ArticleAction articleAction;

    @Autowired
    private ArticleLikesAction articleLikesAction;

    @Autowired
    private ArticleFuseAction articleFuseAction;

    @Autowired
    private ArticleCollectAction articleCollectAction;

    private Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @ResponseBody
    @PostMapping("getIndex")
    @ApiOperation(value ="获取附近资讯")
    public JsonResult getIndex(@Valid @RequestBody GeomRequestDto dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo;
        try {
            userGeo = getGeoFromRequest(request);
            if(StringUtils.isEmpty(userGeo.getUserId())){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage(), ApiCode.NO_AUTHORIZATION);
        }
        try{
            return JsonResult.successJsonResult(articleAction.quertNearlArticle(dto,userGeo.getUserId(),userGeo.getLon(),userGeo.getLat()));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            logger.error("dto"+dto.toString()+"GEO"+userGeo.toString());
            return JsonResult.fail("获取附近咨询异常");
        }
    }

    @ResponseBody
    @PostMapping("publishArticle")
    @ApiOperation(value ="发布图文")
    public JsonResult publishArticle(@Valid @RequestBody PublishArticleRequestDto dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(dto.getTitle().matches("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]")){
            return JsonResult.fail("图文标题不能输入表情");
        }
        UserGeo userGeo = new UserGeo();
        try {
            userGeo = getGeoFromRequest(request);
            if(StringUtils.isEmpty(userGeo.getUserId())){
                return JsonResult.fail("授权过期，请重新登录");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(userGeo.getLon()==0 || userGeo.getLat()==0){
            return JsonResult.fail("经纬度不能为空");
        }
        if (articleFuseAction.checkArticle(userGeo.getUserId(),dto.getTitle()).equals("不能发布自己已发布过的标题！")){
            return JsonResult.fail("不能发布自己已发布过的标题！");
        }else if (articleFuseAction.checkArticle(userGeo.getUserId(),dto.getTitle()).equals("五分钟内不可以发布两次哦！")){
            return JsonResult.fail("五分钟内不可以发布两次哦！");
        }
        //当为对指定好友公开的类型，且指定好友为空时，拒绝服务
        if(dto.getWho() == WhoCanEnum.DESIGNED_FRIENDS && StringUtils.isEmpty(dto.getReceiver())){
            return JsonResult.fail("指定好友不能为空");
        }
        try{
            String articleId = articleFuseAction.publishArticle(dto,userGeo.getUserId(),userGeo.getLon(),userGeo.getLat());
            if(articleId != null){
                return JsonResult.success().addResult("id",articleId);
            }else{
                return JsonResult.fail("操作失败");
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("发布图文异常");
        }
    }

    @PostMapping("editArticle")
    @ApiOperation(value ="编辑图文")
    public JsonResult editArticle(@Valid @RequestBody EditArticleRequestDto dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try{
            String result = articleFuseAction.editArticle(dto,userId);
            return getResult(result==null,result);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑图文异常");
        }
    }

    @ResponseBody
    @PostMapping("selectArticleDetail")
    @ApiOperation(value ="查询图文详情", notes="传递的商品id，空为登录者id")
    public JsonResult selectArticleDetail(String articleId,HttpServletRequest request) {
        String userId;
        try {
            userId = getUserId(request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if(StringUtils.isEmpty(articleId)){
            return JsonResult.fail("图文id不能为空");
        }
        Double lon =  getLon ( request );
        Double lat =  getLat ( request );
        try{
            return JsonResult.successJsonResult(articleFuseAction.selectArticleDetailMap(userId, articleId,lon,lat));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询图文详情异常");
        }
    }

    @ResponseBody
    @GetMapping("selectArticleOtherInfo")
    @ApiOperation(value ="查询图文的其他信息")
    public JsonResult selectArticleOtherInfo(String articleId) {
        if(!StringUtils.hasText(articleId)){
            return JsonResult.fail("图文id不能为空");
        }
        try{
            SelectArticleOtherInfoResponseDto response = articleFuseAction.selectArticleOtherInfo(articleId);
            return JsonResult.success().addResult("otherInfo",response);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询图文其他信息异常");
        }
    }

    @ResponseBody
    @PostMapping("selectArticleListInPublication")
    @ApiOperation(value ="根据用户id分页查询用户已发布的的图文列表",
            notes="图文id，注意：列表包含已经被封禁的咨讯")
    public JsonResult selectArticleListInPublication(@Valid @RequestBody SelectArticleListRequestDto dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("授权过期，请重新登录");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try{
            List<SelectArticleResponseDto> list = articleFuseAction.selectArticleListInPublication(dto,userId);
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询已发布图文列表异常");
        }
    }

    @PostMapping("selectArticleListInLikes")
    @ApiOperation(value = "分页查询用户点赞过的图文列表", notes = "无法查出被封禁的图文")
    public JsonResult selectArticleListInLikes(@Valid @RequestBody SelectArticleListRequestDto dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try{
            userId = getUserId(request);
            if(userId==null){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try{
            return JsonResult.success().addResult("list",articleFuseAction.selectArticleListInLikes(dto,userId));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询点赞图文异常");
        }
    }

    @PostMapping("selectArticleListInCollect")
    @ApiOperation(value = "分页查询用户收藏过的图文列表", notes = "无法查出被封禁的图文")
    public JsonResult selectArticleListInCollect(@Valid @RequestBody SelectArticleListRequestDto dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try{
            userId = getUserId(request);
            if(userId==null){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try{
            return JsonResult.success().addResult("list",articleFuseAction.selectCollectArticle(dto,userId));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询收藏图文列表异常");
        }
    }

    @ResponseBody
    @PostMapping("selectArticleListInTop")
    @ApiOperation(value = "分页查询置顶咨讯列表", notes = "显示后台所设置的置顶的咨讯，不包含置顶过期、被封禁的咨讯")
    public JsonResult selectArticleListInTop(@Valid @RequestBody SelectArticleListInTopRequestDto dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId ;
        try {
            userId = (getUserId(request));
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("授权过期，请重新登录");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try{
            return successResult(articleFuseAction.selectArticleListInTop(dto,userId));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询咨询异常");
        }
    }

    @ResponseBody
    @PostMapping("likeArticle")
    @ApiOperation(value = "点赞咨讯", notes = "返回1代表点赞成功，返回2代表取消点赞")
    public JsonResult likeArticle(String userId, String articleId){
        if(!StringUtils.hasText(userId)){
            return JsonResult.fail("用户id不能为空");
        }
        if(!StringUtils.hasText(articleId)){
            return JsonResult.fail("咨讯id不能为空");
        }
        try{
            return getResult(articleLikesAction.likeArticle(userId, articleId),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("点赞咨询异常");
        }
    }


    @ApiOperation(value = "收藏咨讯", notes = "返回1代表收藏成功，返回2代表取消收藏")
    @PostMapping("collectArticle")
    public JsonResult collectArticle(String articleId,HttpServletRequest request){
        String userId = null;
        try{
            userId = getUserId(request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if(StringUtils.isEmpty(articleId)){
            return JsonResult.fail("图文id不能为空");
        }
        try{
            String result = articleCollectAction.addOnCencelCollect(articleId,userId);
            return getResult(result==null,result);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("收藏咨询异常："+e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("selectArticleCountByUserId")
    @ApiOperation(value = "根据用户id查找已发布的咨讯数量（图文和音视分开来）", notes = "被封禁的咨讯不算入")
    public JsonResult selectArticleCountByUserId(@Valid @RequestBody SelectArticleCountRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            Integer count = articleAction.selectArticleCountByUserId(request.getUserId(),request.getIsArticleType());
            return JsonResult.success().addResult("num",count);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询已发布咨询数量异常");
        }
    }

    @ResponseBody
    @PostMapping("payAmount")
    @ApiOperation(value = "支付押金（或续费）")
    public JsonResult payAmount(@Valid @RequestBody ArticlePayForCompanyDto companyDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("授权过期，请重新登录");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try{
            String result = articleFuseAction.payAmount(companyDto,userId);
            return super.getResult( result==null ,result);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("支付押金或续费异常");
        }
    }

    @ResponseBody
    @PostMapping("deleteArticleByUser")
    @ApiOperation(value = "用户自主删除咨讯", notes = "会把押金退回")
    public JsonResult deleteArticleByUser(@RequestParam("articleId") String articleId,HttpServletRequest request){
        String userId = null;
        try {
            userId = getUserId(request);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("授权过期，请重新登录");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(StringUtils.isEmpty(articleId)){
            return JsonResult.fail("咨讯id不能为空");
        }
        try{
            return super.getResult(articleAction.deleteArticleByUser(userId, articleId),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除咨询异常");
        }
    }

    @ResponseBody
    @PostMapping("deleteUnpaidArticle")
    @ApiOperation(value = "删除自发布后24小时内没交押金的咨讯（定时任务）")
    public JsonResult deleteUnpaidArticle(String articleId){
        if(!StringUtils.hasText(articleId)){
            return JsonResult.fail("咨讯id不能为空");
        }
        try{
            return super.getResult(articleAction.deleteUnpaidArticle(articleId),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除咨询异常");
        }
    }

    @ResponseBody
    @ApiOperation(value = "查看比赛相关的图文")
    @PostMapping(value = "/selectArticleByMatchId")
    public JsonResult selectArticleByMatchId(@Validated @RequestBody CommonPageDto dto, HttpServletRequest httpServletRequest) {
        String userId = null;
        try {
            userId = getUserId(httpServletRequest);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("授权过期，请重新登录");
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try{
            Page<Article> page = new Page<>(dto.getCurrentPage(), dto.getSize());
            return JsonResult.success().addResult("list", articleFuseAction.getArticlesByMatchId(userId,dto.getId(), page));
        } catch (RuntimeException e) {
            return JsonResult.fail(e.getMessage());
        } catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询错误");
        }
    }

}
