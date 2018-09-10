package com.netx.boss.web.controller.ucenter.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.boss.web.controller.BaseController;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.vo.business.AddCategoryRequestDto;
import com.netx.common.vo.business.DeleteCategoryRequestDto;
import com.netx.common.vo.common.*;
import com.netx.shopping.biz.productcenter.CategoryAction;
import com.netx.shopping.model.productcenter.Category;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping(value = "common/tags/")
public class TagsControllerr extends BaseController {
    private Logger logger = LoggerFactory.getLogger(TagsControllerr.class);

    @Autowired
    private TagsAction tagsAction;
    @Autowired
    private CategoryAction categoryAction;
    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @ApiOperation(value = "模糊分页查询标签")
    @PostMapping("/selectTags")
    public JsonResult selectTags(@Valid @RequestBody GetInnerTagsRequestDto request, BindingResult bindingResult) {
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map map = new HashMap<String,Object>();
            Page<CommonTags> selectPage = commonServiceProvider.getTagsService().selectTags(request);
            List<CommonTags> records = selectPage.getRecords();
            map.put("list",records);
            map.put("total",selectPage.getTotal());
            return JsonResult.successJsonResult(map);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询标签异常");
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

    @ApiOperation(value = "添加标签", notes = "value:标签值,type,标签类别,0:内置标签,1-技能标签,2-兴趣标签,3-商品标签" +
            ",createUser必填,如果是公共标签,createUser为0,typeCate为内置标签专用,填写具体汉字,如:学校,院系等")
    @PostMapping("/saveOrUpdate")
    public JsonResult addOrUpdate(@Valid @RequestBody TagsAddRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(dto.getCreateUser())){
            try {
                dto.setCreateUser("0");
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            Boolean flag = tagsAction.saveOrUpdate(dto);
            if(flag==null){
                return JsonResult.fail("当前公告标签已存在");
            }
            return super.getResult(flag,"操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("添加标签异常");
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

    @ApiOperation(value = "修改标签")
    @PostMapping("/updateTags")
    public JsonResult updateTags(@Valid @RequestBody TagsUpdateRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(tagsAction.updateTags(request),"操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改标签异常");
        }
    }

    @ApiOperation(value = "分页查询商品一级标签")
    @PostMapping("/selectProductTags")
    public JsonResult selectProductTags(@Valid @RequestBody CommonListDto request, BindingResult bindingResult) {
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map map = new HashMap<String,Object>();
            Page<Category> selectPage = categoryAction.getCategoryService().selectParentListOrderByPriority(request);
            List<Category> records = selectPage.getRecords();
            map.put("list",records);
            map.put("total",selectPage.getTotal());
            return JsonResult.success().addResult("map",map);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询标签异常");
        }
    }


    @ApiOperation(value = "根据商品标签父ID分页查询商品子标签")
    @PostMapping("/selectKidTags")
    public JsonResult selectKidTags(@Valid @RequestBody GetKidTagsRequestDto request, BindingResult bindingResult) {
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map map = new HashMap<String,Object>();
            List<Category> records = categoryAction.getCategoryService().selectKidTags(request);
            Integer total = categoryAction.getCategoryService().selectKidTagsCount(request);
            map.put("list",records);
            map.put("total",total);
            return JsonResult.success().addResult("map",map);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("查询标签异常");
        }
    }

//    @ApiOperation(value = "添加或修改商家类目", notes = "添加或修改商家类目")
//    @PostMapping("/addOrUpdate")
//    public JsonResult addOrUpdate(@Valid @RequestBody AddCategoryRequestDto request, BindingResult bindingResult){
//        if( bindingResult.hasErrors()){
//            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
//        try {
//            Category category = categoryAction.addOrUpdate(request);
//            if(category != null){
//                return getResult(true,"增加类目成功！");
//            }else{
//                return getResult(false,"增加类目失败！");
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            return JsonResult.fail("增加类目异常！");
//        }
//    }

    @ApiOperation(value = "删除商家类目", notes = "删除商家类目")
    @PostMapping("/deleteCategoryById")
    public JsonResult deleteCategoryById(@Valid @RequestBody DeleteCategoryRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            final boolean res = categoryAction.getCategoryService().deleteById(request.getId());
            return getResult(res,"删除类目成功！","删除类目失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("增加类目异常！");
        }
    }
}
