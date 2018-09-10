package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.common.enums.LimitEnum;
import com.netx.common.vo.common.ArticleCommonResponseDto;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.user.enums.ArticleStatusCodeEnum;
import com.netx.common.user.enums.SystemBlackStatusEnum;
import com.netx.common.vo.common.*;
import com.netx.ucenter.biz.common.ArticleLimitedAction;
import com.netx.ucenter.biz.user.ArticleAction;
import com.netx.ucenter.biz.user.SystemBlacklistAction;
import com.netx.ucenter.model.common.CommonArticleLimit;
import com.netx.ucenter.model.user.Article;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.ucenter.service.user.UserServiceProvider;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author haojun
 * @Date create by 2017/9/26
 */

@Api(value = "资讯管理控制器",description = "用于操作资讯表")
@RestController
@RequestMapping("/api/common/article")
public class ArticleManagementController extends BaseController{

  /*  private Logger logger = LoggerFactory.getLogger(ArticleManagementController.class);
    @Autowired
    private ArticleAction articleAction;

    @Autowired
    private UserServiceProvider userServiceProvider;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @Autowired
    private SystemBlacklistAction systemBlacklistAction;

    @ApiOperation(value = "修改资讯详情信息")
    @PostMapping("/update")
    public JsonResult updateArticleDetail(@Valid @RequestBody EditArticleDetailRequestDto request, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return super.getResult(articleAction.editArticleDetail(request),"修改失败");
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("修改异常");
        }
    }
    @ApiOperation(value = "根据资讯类型，标题或者网号赖查询返回资讯集合",notes = "返回值key:list 类型: List<ArticleCommonResponseDto>")
    @PostMapping("/query")
    public JsonResult listArticleByTypeOrTitleOrUserid(@RequestBody SelectAriticleListRequestDto request, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<ArticleCommonResponseDto> articles = null;
            articles = articleAction.selectByArticleTypeOrTitleOrUserid(request);
            if (articles == null) {
                return JsonResult.fail("获取资讯集合出现错误");
            }
            return JsonResult.success().addResult("list",articles);
        }catch (Exception e) {
            logger.error("查询资讯异常："+e.getMessage(),e);
            return JsonResult.fail("查询资讯异常");
        }
    }


    @ApiOperation(value = "删除资讯并且为该用户减少5点信用点")
    @PostMapping("/delete")
    public JsonResult deleteArticleAndCredit(@Valid @RequestBody ArticleDeleteAndReleaseScoreRequestDto request,BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String result = articleAction.deleteArticleByID(request.getArticleIds());
            return super.getResult(result==null,"删除成功","删除失败");
        }catch (Exception e) {
            logger.error("删除异常并且减少5信用点异常："+e.getMessage(),e);
            return JsonResult.fail("删除异常");
        }
    }

    @ApiOperation(value="列入资讯异常的接口")
    @PostMapping("/pushException")
    public JsonResult pushArticleException(@Valid @RequestBody ArticleExceptionPushOrPopRequestDto request, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            //Wrapper<Article> demo = new EntityWrapper<Article>();
            //demo.where("id={0}",request.getArticleId()).andNew("isLock=true");
            //Article article = articleAction.selectOne(demo);
            Article article = userServiceProvider.getArticleService().selectById(request.getArticleId());
            if(article == null ){
                return JsonResult.fail("此资讯不存在");
            }
            if(article.getIsLock()==1){
                return JsonResult.fail("资讯已经被锁住，不能列入异常资讯");
            }
            return super.getResult(articleAction.pushOrPopArticleException(article,1, request),"列入资讯异常成功","列入失败");
        }catch (Exception e){
            logger.error("列入资讯异常："+e.getMessage(),e);
            return JsonResult.fail("列入资讯异常");
        }
    }

    @ApiOperation(value="解除资讯异常的接口")
    @PostMapping("/popException")
    public JsonResult popArticleException(@Valid @RequestBody ArticleExceptionPushOrPopRequestDto request, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(articleAction.pushOrPopArticleException(2, request),"解除资讯异常成功","解除失败");
        }catch (Exception e) {
            logger.error("解除资讯异常："+e.getMessage(),e);
            return JsonResult.fail("解除资讯异常");
        }
    }

    @ApiOperation(value = "受限名单查找作者",notes = "返回值key:result 类型:UserInfoResponseDto")
    @PostMapping("/queryLimited")
    public JsonResult queryLimitedListByUserNetworkNum(@Valid @RequestBody QueryArticleLimitedListRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            UserInfoResponseDto responseVo = articleAction.queryByUserNetworkNumId(request.getUserNetworkNum());
            if (responseVo == null) {
                return JsonResult.fail("没有任何该网号的用户信息");
            }
            return JsonResult.success().addResult("result",responseVo);
        }catch (Exception e){
            logger.error("查询资讯受限名单异常："+e.getMessage(),e);
            return JsonResult.fail("查询资讯受限名单异常");
        }
    }

    @ApiOperation(value = "处分作者的立即处分或者调整处分",notes = "只要传入的articleLimitedId有值就是调整处分，不然处理处分不用传")
    @PostMapping("/punishAuthor")
    public JsonResult punishAuthor(@Valid @RequestBody PunishAuthorRequestDto request, BindingResult bindingResult) throws Exception{
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        PunishAuthorTransferRequestDto transfer=new PunishAuthorTransferRequestDto();
        CommonArticleLimit wzCommonArticleLimited=null;
        if(StringUtils.isNotBlank(request.getArticleLimitedId())){
            wzCommonArticleLimited=commonServiceProvider.getArticleLimitedService().selectById(request.getArticleLimitedId());
            if(wzCommonArticleLimited==null){
                return JsonResult.fail("该"+request.getArticleLimitedId()+"的资讯不存在");
            }
            if(!wzCommonArticleLimited.getUserId().equals(request.getUserId())){
                return JsonResult.fail("没有"+request.getArticleLimitedId()+"的资讯受限列");
            }
        }
        LimitEnum limitEnum=request.getLimitEnum();
        // Map<String,Object> map=new HashMap<String,Object>();
        try {
            switch (limitEnum)
            {
                //減少信用值
                case LIMIT_RELEASE_CREDIT:
                    //扣除信用值
                    articleAction.punishAuthorByCredit(request,wzCommonArticleLimited.getId());
                    request.setLimitValue(request.getParam1());
                    break;
                //限制每月發資訊的數量
                case LIMIT_MONTHLY_PUBLISH:
                    request.setLimitValue(request.getParam2());
                    break;
                //限制每天發資訊的數量
                case LIMIT_DAYLY_PUBLISH:
                    request.setLimitValue(request.getParam3());
                    break;
                //禁止发布持续天数
                case LIMIT_FORBID_DAYLY_PUBLISH:
                    request.setLimitValue(request.getParam4());
                    break;
                //发一条资讯先付费
                case LIMIT_EVERY_PAY_PUBLISH:
                    request.setLimitValue(request.getParam5());
                    break;
                //禁止发资讯
                case LIMIT_FORBID_PUBLISH:
                    break;
                //拉入黑名单
                case LIMIT_BLACKLIST_PUSH:
                    // 操作黑名单接口
                    systemBlacklistAction.operateSystemBlacklist(request.getUserId(),request.getOperatorUserId(),"被管理员拉入黑名单", SystemBlackStatusEnum.DEFRIEND.getValue());
                    break;
            }
            transfer.setLimitMeasure(limitEnum.getCode());
            BeanUtils.copyProperties(request,transfer);
            return super.getResult(articleAction.punishAuthor(transfer),"处分作者失败");
        }catch (Exception e) {
            logger.error("处分作者异常："+e.getMessage(),e);
            return JsonResult.fail("处分作者异常："+e.getMessage());
        }
    }

    @ApiOperation(value="解除处分")
    @PostMapping("/sanction")
    public JsonResult sanction(@Valid @RequestBody ArticleSanctionRequestDto request,BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(articleAction.sanctionAritcle(request),"解除处分失败");
        }catch (Exception e) {
            logger.error("解除处分异常："+e.getMessage(),e);
            return JsonResult.fail("解除处分异常");
        }
    }

    @PostMapping("/introduceToTop")
    @ApiOperation(value = "资讯置顶")
    public JsonResult toTop(@Valid @RequestBody ArticleIntroduceToTopRequestDto requestDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(articleAction.introduceToTop(requestDto),"置顶成功","置顶失败");
        }catch (Exception e){
            logger.error("执行异常发生："+e.getMessage(),e);
            return JsonResult.fail("置顶异常");
        }

    }

    @ApiOperation(value = "异常咨询要求修改发行消息")
    @PostMapping("/commandUpdate")
    public JsonResult commandUpdate(@Valid @RequestBody ArticleCommandUpdateRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            if(request.getArticleIds().isEmpty()){
                return JsonResult.fail("没有选择任何异常资讯");
            }
            articleAction.commandUpdate(request.getArticleIds());
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改异常");
        }
    }

    @ApiOperation(value = "所有异常咨询列表",notes = "返回值key:list 类型:List<UserArticle>")
    @PostMapping("/getExceptionArticleList")
    public JsonResult getExceptionArticleList(@Valid @RequestBody PageRequestDto requestDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            List<Article> articleList=articleAction.selectPageByStatusCode(requestDto.getCurrent(),requestDto.getSize(),ArticleStatusCodeEnum.EXCEPTION.getValue());
            return JsonResult.success().addResult("list",articleList);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "资讯受限名单列表",notes = "返回值key:list 类型:List<CommonArticleLimit>")
    @PostMapping("/getArticleLimited")
    public JsonResult getArticleLimited(@Valid @RequestBody PageRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return JsonResult.success().addResult("list",commonServiceProvider.getArticleLimitedService().selectArticleLimitedList(request.getCurrent(),request.getSize()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }


    @ApiOperation(value = "后台管理待审核图文,音视列表",notes = "返回值key:list 类型:List<ArticleLockListResponseDto>")
    @PostMapping(value = "/selectArticleLockList")
    public JsonResult selectArticleLockList(@Valid @RequestBody PageRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            List<ArticleLockListResponseDto> list=articleAction.selectArticleLockList(request.getCurrent(),request.getSize());
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "广告软文和音视广告宣传的接口")
    @PostMapping("/advertorialBoolean")
    public JsonResult advertorialBoolean(@Valid @RequestBody ArticleDeleteAndReleaseScoreRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            String result=articleAction.advertorialBoolean(request);
            return super.getResult(result==null,"广告修改成功！",result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改异常");
        }
    }

    @ApiOperation(value ="后台管理软文和音视共用的免费发布")
    @PostMapping("/managementPublishArticle")
    public JsonResult publishArticle(@Valid @RequestBody ArticleDeleteAndReleaseScoreRequestDto requset,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            String result=articleAction.managementArticlePublish(requset);
            return super.getResult(result==null,"免费发布资讯成功！",result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("发布异常");
        }
    }*/
}

