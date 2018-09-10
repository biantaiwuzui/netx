package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.common.enums.ErrorCode;
import com.netx.common.vo.common.AreaAddRequestDto;
import com.netx.common.vo.common.AreaDeteleRequestDto;
import com.netx.common.vo.common.AreaQueryRequestDto;
import com.netx.ucenter.biz.common.AreaAction;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by wongloong on 17-8-29
 */
@Api(description = "区域接口")
@RestController
@RequestMapping("/api/area")
public class AreaController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(AreaController.class);
    @Autowired
    AreaAction areaAction;

    @ApiOperation("添加或删除区域,flag表示层级,0为第一层,同时pid表示上级目录,如果没有请填写0,如果新增请保证id为null或没有这个属性")
    @PostMapping("/saveOrUpdate")
    public JsonResult add(@RequestBody AreaAddRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(areaAction.saveOrUpdate(request),ErrorCode.ERROR_OPERATION_FAILD.getMsg());
        } catch (Exception e) {
            logger.error("添加或删除区域异常:"+e.getMessage(),e);
            return JsonResult.fail("操作区域异常");
        }
    }

    @ApiOperation("获取区域数据,根据pid和flag确定,如果都为-1则返回全部数据")
    @PostMapping("/query")
    public JsonResult getList(@RequestBody AreaQueryRequestDto request) {
        try {
            return JsonResult.success().addResult("area",areaAction.selectByPidAndFlag(request));
        } catch (Exception e) {
            logger.error("查询区域数据异常:"+e.getMessage(),e);
            return JsonResult.fail("查询区域数据异常");
        }
    }

    @ApiOperation("根据id删除数据")
    @PostMapping("/delete")
    public JsonResult delData(@RequestBody AreaDeteleRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(areaAction.deleteByIdAndValidationChildren(request.getId()),"当前区域有子区域,删除失败");
        } catch (Exception e) {
            logger.error("删除区域数据异常:"+e.getMessage(),e);
            return JsonResult.fail("删除区域数据异常");
        }
    }

    @ApiOperation("获取所有区域数据")
    @PostMapping("/querys")
    public JsonResult getAllList() {
        try {
            return JsonResult.success().addResult("list",areaAction.selectAllAreas());
        } catch (Exception e) {
            logger.error("查询区域数据异常："+e.getMessage(),e);
            return JsonResult.fail("查询区域数据异常");
        }
    }

}
