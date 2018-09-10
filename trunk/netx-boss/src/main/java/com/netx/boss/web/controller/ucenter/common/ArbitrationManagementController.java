package com.netx.boss.web.controller.ucenter.common;

import com.netx.common.vo.common.ArbitrationAcceptHandleRequestDto;
import com.netx.common.vo.common.ArbitrationRefuseAcceptHandleRequestDto;
import com.netx.common.vo.common.ArbitrationSelectByTypeVo;
import com.netx.fuse.biz.ucenter.ArbitrationFuseAction;
import com.netx.ucenter.biz.common.ArbitrationAction;
import com.netx.ucenter.model.common.CommonManageArbitration;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "仲裁")
@RestController
@RequestMapping(value = "/common/arbitration/")
public class ArbitrationManagementController {

    private Logger logger = LoggerFactory.getLogger(ArbitrationManagementController.class);

    @Autowired
    ArbitrationAction arbitrationAction;

    @Autowired
    ArbitrationFuseAction arbitrationFuseAction;

    @ApiOperation(value = "查询仲裁列表",notes = "根据投诉的时间升序进行返回所有的仲裁信息")
    @PostMapping("getArbitrationListByPage")
    public JsonResult getArbitrationListByPage(@Valid @RequestBody ArbitrationSelectByTypeVo requestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<CommonManageArbitration> list = arbitrationAction.getWaitingSettleArbitration(requestDto);
            Map map = new HashMap();
            map.put("list",list);
            map.put("count",list!=null ? list.size():0);
            /*return JsonResult.success().addResult("list",list);*/
            return JsonResult.successJsonResult(map);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("查询仲裁列表异常");
        }

    }

    /**
     * 拒绝受理
     * @return
     */
    @ApiOperation(value = "拒绝受理")
    @PostMapping("refuseAcceptHandle")
    public JsonResult refuseAcceptHandle(@Valid @RequestBody ArbitrationRefuseAcceptHandleRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            String ret = arbitrationAction.isCanRefuseAcceptHandle(requestDto);
            if(ret!=null){
                return JsonResult.fail(ret);
            }
            if(!arbitrationFuseAction.refuseAcceptHandle(requestDto)){
                return JsonResult.fail("拒绝受理插入到数据库失败");
            }
            return JsonResult.success();
        }catch (Exception e) {
            logger.error("拒绝受理出现异常："+e.getMessage(),e);
            return JsonResult.fail("拒绝受理出现异常");
        }

    }

    @PostMapping("acceptHandle")
    @ApiOperation(value = "仲裁审核受理")
    public JsonResult acceptHandle(@Valid @RequestBody ArbitrationAcceptHandleRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String result = arbitrationFuseAction.acceptCheckAndHandle(requestDto);
            return this.getResult(result==null,result);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("裁决请求插入数据库失败："+e.getMessage(),e);
            return JsonResult.fail("裁决请求异常");
        }
    }

    public JsonResult getResult(boolean success,String msg) {
        if(success) {
            return JsonResult.success();
        }else {
            return JsonResult.fail(msg);
        }
    }

}
