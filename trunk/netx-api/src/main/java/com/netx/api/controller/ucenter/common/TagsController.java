package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.*;
import com.netx.ucenter.biz.common.TagsAction;
import com.netx.ucenter.model.common.CommonTags;
import com.netx.ucenter.service.common.CommonServiceProvider;
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
import java.util.List;

/**
 * Create by wongloong on 17-9-1
 */
@Api(description = "标签接口")
@RestController
@RequestMapping("/api/tags")
public class TagsController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(TagsController.class);

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @Autowired
    private TagsAction tagsAction;

    @ApiOperation(value = "添加标签", notes = "value:标签值,type,标签类别,0:内置标签,1-技能标签,2-兴趣标签,3-商品标签" +
            ",createUser必填,如果是公共标签,createUser为0,typeCate为内置标签专用,填写具体汉字,如:学校,院系等")
    @PostMapping("/saveOrUpdate")
    public JsonResult addOrUpdate(@Valid @RequestBody TagsAddRequestDto dto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(dto.getCreateUser())){
            try {
                dto.setCreateUser(getUserId(request));
                if(StringUtils.isEmpty(dto.getCreateUser())){
                    return JsonResult.fail("创建者不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            Boolean flag = tagsAction.saveOrUpdate(dto);
            if(flag==null){
                return JsonResult.fail("当前标签已存在");
            }
            return super.getResult(flag,"操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加标签异常");
        }
    }

    @ApiOperation(value = "查询标签", notes = "createUser(必填),'0'为公共标签'-1'为不限,type(必填):0:内置标签,1-技能标签,2-兴趣标签,3-商品标签," +
            "typeCate(如果为空则查询type下所有的标签)为内置/技能标签专用,填写具体汉字,如:学校,院系等")
    @PostMapping("/query")
    public JsonResult queryList(@Valid @RequestBody TagsQueryRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<CommonTags> wzCommonTagsList = tagsAction.queryList(request);
            return JsonResult.success().addResult("list",wzCommonTagsList);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询标签异常");
        }
    }

    @ApiOperation(value = "查询标签项", notes = "createUser(必填),'0'为公共标签'-1'为不限,type(必填):0:内置标签,1-技能标签,2-兴趣标签,3-商品标签,typeCate不用传")
    @PostMapping("/queryTypeCate")
    public JsonResult queryListTypeCate(@Valid @RequestBody TagsQueryRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return JsonResult.success().addResult("list", commonServiceProvider.getTagsService().queryTypeList(request));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询标签异常");
        }
    }

    @ApiOperation(value = "查询所有人的自选标签", notes = "createUser请填-1,type(必填):0:内置标签,1-技能标签,2-兴趣标签,3-商品标签," +
            "typeCate")
    @PostMapping("/queryPrivate")
    public JsonResult queryPrivate(@Valid @RequestBody TagsQueryRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<CommonTags> wzCommonTagsList = commonServiceProvider.getTagsService().selectPrivate(request.getType(),request.getTypeCate());
            return JsonResult.success().addResult("list",wzCommonTagsList);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询标签异常");
        }
    }

    @ApiOperation(value = "升级或降级标签", notes = "参数 ids(必填):所操作的标签[数组]" +
            "toPublic(必填):0降级,1升级")
    @PostMapping("/changeScope")
    public JsonResult changeScope(@Valid @RequestBody TagsScopeChangeRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(tagsAction.changeTagsScope(request),"操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("升降级异常");
        }
    }

    @ApiOperation(value = "删除标签值", notes = "根据标签id删除标签值")
    @PostMapping("/delete")
    public JsonResult delete(@RequestBody BaseDto baseDto) {
        if (StringUtils.isEmpty(baseDto.getId())) {
            return JsonResult.fail("标签id不能为空");
        }
        try {
            return super.getResult(tagsAction.deleteById(baseDto.getId()),"操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除标签异常");
        }
    }

    @ApiOperation(value = "标签项的升降级")
    @PostMapping("/changeCateScope")
    public JsonResult changeTypeCateScope(@Valid @RequestBody TagsTypeCateScopeChangeRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(commonServiceProvider.getTagsService().changeTypeScope(requestDto.getTypeCate(),requestDto.getToPublic()),"操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail("标签升降级异常");
        }

    }

    @ApiOperation(value = "根据id查询标签值", notes = "多个id用,分隔")
    @GetMapping("/get")
    public JsonResult getTags(@RequestBody String ids) {
        if (StringUtils.isEmpty(ids)) {
            return JsonResult.success();
        }
        try {
            return JsonResult.success().addResult("list", tagsAction.selectByIds(ids));
        } catch (Exception e) {
            logger.error("根据id获取标签值异常："+e.getMessage(), e);
            return JsonResult.fail("根据id获取标签值异常");
        }
    }
}
