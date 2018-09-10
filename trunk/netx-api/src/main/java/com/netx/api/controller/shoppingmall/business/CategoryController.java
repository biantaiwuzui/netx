package com.netx.api.controller.shoppingmall.business;

import com.netx.api.controller.BaseController;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.vo.business.AddCategoryRequestDto;
import com.netx.common.vo.business.DeleteCategoryRequestDto;
import com.netx.common.vo.common.GetKidTagsRequestDto;
import com.netx.shopping.biz.productcenter.CategoryAction;
import com.netx.shopping.model.productcenter.Category;
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
import java.util.List;

/**
 * Created By liwei
 * Description: 网商类目控制层
 * Date: 2017-09-07
 */
@Api(description = "网商类目相关接口")
@RestController
@RequestMapping("/api/business/category")
public class CategoryController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryAction categoryAction;

//    @ApiOperation(value = "添加或修改商家类目", notes = "添加或修改商家类目")
//    @PostMapping("/addOrUpdate")
//    public JsonResult addOrUpdate(@Valid @RequestBody AddCategoryRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
//        if( bindingResult.hasErrors()){
//            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
//        if(StringUtils.isEmpty(request.getUserId())){
//            try {
//                request.setUserId(getUserId(request.getUserId(),requestDto));
//                if(StringUtils.isEmpty(request.getUserId())){
//                    return JsonResult.fail("用户id不能为空");
//                }
//            }catch (Exception e){
//                return JsonResult.fail(e.getMessage());
//            }
//        }
//        try {
//            Category res = categoryAction.addOrUpdate(request);
//            if(res != null){
//                return JsonResult.success().addResult("res",res);
//            }
//            return JsonResult.fail("添加或修改商家类目失败！");
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            return JsonResult.fail("添加或修改商家类目异常！");
//        }
//    }

    @ApiOperation(value = "获取商家一级类目", notes = "获取商家一级类目")
    @PostMapping("/parentList")
    public JsonResult parentList(@Valid @RequestBody CommonListDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }try {
            List<Category> res = categoryAction.selectParentList(request);
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取商家一级类目失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家一级类目异常！");
        }
    }

    @ApiOperation(value = "根据拼音排序获取商家一级类目", notes = "根据拼音获排序取商家一级类目")
    @PostMapping("/parentListByPy")
    public JsonResult parentListByPy() {
        try {
            List<Category> res = categoryAction.selectParentListByPy();
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取商家一级类目失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家一级类目异常！");
        }
    }

    @ApiOperation(value = "获取商家二级类目", notes = "获取商家二级类目")
    @PostMapping("/kidList")
    public JsonResult kidList(@Valid @RequestBody GetKidTagsRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }try {
            List<Category> res = categoryAction.selectKidList(request);
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取商家二级类目失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家二级类目异常！");
        }
    }

    @ApiOperation(value = "根据拼音排序获取商家二级类目", notes = "根据拼音排序获取商家二级类目")
    @PostMapping("/kidListByPy")
    public JsonResult kidListByPy(@Valid @RequestBody GetKidTagsRequestDto request, BindingResult bindingResult) {
     try {
            List<Category> res = categoryAction.selectKidListByPy(request);
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取商家二级类目失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家二级类目异常！");
        }
    }

    @ApiOperation(value = "模糊查询商家类目", notes = "获取商家类目")
    @PostMapping("/selectCategoryByName")
    public JsonResult selectCategoryByName(@Valid @RequestBody String name){
        if (StringUtils.isEmpty(name)){
            return JsonResult.fail("名字不能为空！");
        }try {
            List<Category> res = categoryAction.selectCategoryByName(name);
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("模糊查询商家类目失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("模糊查询商家类目异常！");
        }
    }

    @ApiOperation(value = "删除商家类目", notes = "删除商家类目")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteCategoryRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            boolean res = categoryAction.deleteById(request.getId());
            return getResult(res, "删除商家类别失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除商家类别异常！");
        }
    }

    @ApiOperation(value = "设置py", notes = "设置py")
    @PostMapping("/setPy")
    public JsonResult setPy(String id){
        try {
            boolean res = categoryAction.setPy(id);
            return getResult(res, "设置py失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return JsonResult.fail("设置py异常！");
        }
    }
}
