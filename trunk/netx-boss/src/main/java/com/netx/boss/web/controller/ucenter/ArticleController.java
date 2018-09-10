package com.netx.boss.web.controller.ucenter;

import com.netx.boss.web.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.dto.article.DeleteArticleRequestDto;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.vo.common.ArticleExceptionPushOrPopRequestDto;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.biz.user.ArticleAction;
import com.netx.ucenter.model.user.Article;
import com.netx.ucenter.service.user.UserServiceProvider;
import com.netx.ucenter.vo.request.QueryArticleListRequestDto;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@Api(description = "咨讯模块（图文、音视）")
public class ArticleController extends BaseController {
    @Autowired
    private ArticleAction articleAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private UserServiceProvider userServiceProvider;
    @Autowired
    private ScoreAction scoreAction;

    private Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @ApiOperation("根据userId查询用户发表的文章列表")
    @PostMapping("getUserArticleList")
    public JsonResult getUserArticleList(@Valid @RequestBody QueryArticleListRequestDto requestDto) {
        try {
            Map<String, Object> map = articleAction.queryArticleList(requestDto);
            if (map.get("userId") == null) {
                return JsonResult.fail("网号错误！！");
            }else {
                if (map.get("records") == null) {
                    return JsonResult.fail("暂时没有该发布图文！！");
                } else {
                    return JsonResult.success().addResult("map", map);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("根据userId查询文章列表异常");

        }
    }

    @PostMapping("deleteArticleByBoss")
    @ApiOperation(value = "后台删除咨讯", notes = "会把押金退回")
    public JsonResult deleteArticleByBoss(@Valid @RequestBody DeleteArticleRequestDto request) {
        if (StringUtils.isEmpty(request.getId())) {
            return JsonResult.fail("咨讯id不能为空");
        }
        try {
            return super.getResult(articleAction.deleteArticleByBoss(request.getId()), "操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除异常");
        }
    }

    @PostMapping("/returnAmount")
    @ApiOperation(value = "审核", notes = "审核通过，押金退回")
    public JsonResult returnAmount(@Valid @RequestBody DeleteArticleRequestDto request) {
        if (StringUtils.isEmpty(request.getId())) {
            return JsonResult.fail("咨讯id不能为空");
        }
        try {
            return super.getResult(articleAction.returnAmount(request.getId()), "操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("押金退回异常");
        }
    }

    @ApiOperation(value = "列入资讯异常的接口")
    @PostMapping("/pushException")
    public JsonResult pushArticleException(@Valid @RequestBody ArticleExceptionPushOrPopRequestDto request, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            //Wrapper<Article> demo = new EntityWrapper<Article>();
            //demo.where("id={0}",request.getArticleId()).andNew("isLock=true");
            //Article article = articleAction.selectOne(demo);
            Article article = userServiceProvider.getArticleService().selectById(request.getArticleId());
            if (article == null) {
                return JsonResult.fail("此资讯不存在");
            }
            if (article.getIsLock() == 1) {
                return JsonResult.fail("资讯已经被锁住，不能列入异常资讯");
            }
            return super.getResult(articleAction.pushOrPopArticleException(article, 1, request), "列入资讯异常成功", "列入失败");
        } catch (Exception e) {
            logger.error("列入资讯异常：" + e.getMessage(), e);
            return JsonResult.fail("列入资讯异常");
        }
    }

    @ApiOperation(value = "解除资讯异常的接口")
    @PostMapping("/popException")
    public JsonResult popArticleException(@Valid @RequestBody ArticleExceptionPushOrPopRequestDto request, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(articleAction.pushOrPopArticleException(2, request), "解除资讯异常成功", "解除失败");
        } catch (Exception e) {
            logger.error("解除资讯异常：" + e.getMessage(), e);
            return JsonResult.fail("解除资讯异常");
        }
    }

    @ApiOperation(value = "修改咨询状态的接口")
    @PostMapping("/modifyArticleStatus")
    public JsonResult modifyArticleStatus(@Valid @RequestBody ArticleExceptionPushOrPopRequestDto articleExceptionPushOrPopRequestDto) {
        try {
            boolean success = articleAction.updateStatusbyUserIdAndStatus(articleExceptionPushOrPopRequestDto.getArticleId(), articleExceptionPushOrPopRequestDto.getStatus());
            if (success) {
                if (articleExceptionPushOrPopRequestDto.getStatus() == 0 && articleExceptionPushOrPopRequestDto.getLength() > 140 && articleExceptionPushOrPopRequestDto.getPic() != null) {
                    scoreAction.addScore(articleExceptionPushOrPopRequestDto.articleId, +15);
                } else if (articleExceptionPushOrPopRequestDto.getStatus() == 5) {
                    scoreAction.addScore(articleExceptionPushOrPopRequestDto.articleId, -15);
                }
                articleAction.updateUpdateUserId(articleExceptionPushOrPopRequestDto.getArticleId());
                return JsonResult.success();
            } else {
                return JsonResult.fail("状态一样！");
            }
        } catch (Exception e) {
            logger.error("修改资讯状态异常：" + e.getMessage(), e);
            return JsonResult.fail("修改资讯状态异常");
        }
    }
}
