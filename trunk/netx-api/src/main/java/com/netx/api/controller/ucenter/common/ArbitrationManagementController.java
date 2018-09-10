package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.common.enums.ArbitrationEnum;
import com.netx.common.vo.business.CheckOrderArbitrationRequestDto;
import com.netx.common.vo.common.*;
import com.netx.fuse.biz.ucenter.ArbitrationFuseAction;
import com.netx.ucenter.biz.common.ArbitrationAction;
import com.netx.ucenter.biz.common.ArbitrationResultAction;
import com.netx.ucenter.model.common.CommonManageArbitration;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author haojun
 * @Date create by 2017/9/26
 *
 */
@Api(value = "仲裁管理控制器",description = "用户投诉方面操作")
@RestController
@RequestMapping("/api/common/arbitration")
public class ArbitrationManagementController extends BaseController{
    private Logger logger= LoggerFactory.getLogger(ArbitrationManagementController.class);

    @Autowired
    private ArbitrationAction arbitrationAction;

    @Autowired
    private ArbitrationResultAction arbitrationResultAction;

    @Autowired
    private ArbitrationFuseAction arbitrationFuseAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @ApiOperation(value = "立即投诉接口")
    @PostMapping("/addComplaint")
    public JsonResult addComplaint(@Valid @RequestBody ArbitrationAddComplaintRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(requestDto.getFromUserId(),request);
            if(userId ==null){
                return JsonResult.fail("用户id不能为空");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        }
        requestDto.setFromUserId(userId);
        try {
            String errorStr = arbitrationAction.checkSubmit(requestDto);
            if(errorStr != null){
                return JsonResult.fail(errorStr);
            }
            errorStr = arbitrationAction.isCanAddComplaint(requestDto);
            if(errorStr != null){
                return JsonResult.fail(errorStr);
            }
            String arbitrationId = arbitrationFuseAction.addComplaint(requestDto);
            if(arbitrationId != null){
                return JsonResult.success().addResult("arbitrationId", arbitrationId);
            }
            return JsonResult.fail("投诉失败");
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("确认,立即投诉请求失败:"+e.getMessage(),e);
            return JsonResult.fail("投诉异常");
        }
    }


    @PostMapping("/appealArbitration")
    @ApiOperation(value = "申诉请求")
    public JsonResult appealArbitration(@Valid @RequestBody ArbitrationAppealRequestDto requestDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String errorStr= arbitrationAction.isCanAppealArbitration(requestDto);
            if(errorStr!=null){
                return JsonResult.fail(errorStr);
            }
            if(arbitrationAction.appealArbitration(requestDto)){
                return JsonResult.success("申诉成功");
            }
            return JsonResult.fail("申诉请求失败");
        }catch(Exception e){
            logger.error("申诉请求失败:"+e.getMessage(),e);
            return JsonResult.fail("申诉请求异常");
        }
    }

    @PostMapping("/acceptHandle")
    @ApiOperation(value = "仲裁审核受理")
    public JsonResult acceptHandle(@Valid @RequestBody ArbitrationAcceptHandleRequestDto requestDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String result = arbitrationFuseAction.acceptCheckAndHandle(requestDto);
            return super.getResult(result==null,result);
        }catch (Exception e){
            logger.error("裁决请求插入数据库失败："+e.getMessage(),e);
            return JsonResult.fail("裁决请求异常");
        }
    }

    /**
     * 决绝处理
     * @return
     */
    @ApiOperation(value = "拒绝受理")
    @PostMapping("/refuseAcceptHandle")
    public JsonResult refuseAcceptHandle(@Valid @RequestBody ArbitrationRefuseAcceptHandleRequestDto requestDto, BindingResult bindingResult)
    {
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

    /**
     * 表单的值
     * @paramater
     *  queryType:决定查找的方式
     *     用户：
     *          0.用户自身userId关联查询
     *     管理员：
     *      仲裁管理表：
     *      1.根据昵称查找nickname
     *      2.根据userId查找
     *      仲裁结果表：
     *          3.根据opUserId（操作者ID）
     *   returnType:
     *      0.全部仲裁,未处理或者已裁决
     *      1.未处理或者对方已申诉-------可以修改功能
     *      2.受理中,已裁决,拒绝受理------查看功能
     *
     * @return
     */

    @PostMapping("/selectArbitration")
    @ApiOperation(value = "根据选择相应的字段查询仲裁")
    public JsonResult selectArbitration(@Valid @RequestBody ArbitrationQueryParamaterRequestDto requestDto,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }try {
            //不符合规定就跳转到报错信息
            if (!checkArbitrationQueryParamater(requestDto)) {
                List<ArbitrationSelectResponseVo> list = arbitrationAction.selectByParam(requestDto);
                if(list==null){
                    return JsonResult.fail("返回对象为空");
                }
                if (list.isEmpty()) {
                    return JsonResult.success("返回的对象集合为空");
                }
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("参数不符合规定:字段为空或者全为空格");
        }catch (Exception e){
            logger.error("查询仲裁处理异常",e);
            return JsonResult.fail("查询仲裁处理异常");
        }
    }

    private Boolean checkArbitrationQueryParamater(ArbitrationQueryParamaterRequestDto requestDto){
        switch (requestDto.getQueryType()) {
            case 0:
                if (StringUtils.isEmpty(requestDto.getUserId())) {
                    return true;
                }
                break;
            case 1:
                if (StringUtils.isEmpty(requestDto.getNickname())) {
                    return true;
                }
                break;
            case 2:
                if (StringUtils.isEmpty(requestDto.getUserNetworkNum())) {
                   return true;
                }
                break;
            case 3:
                if (StringUtils.isEmpty(requestDto.getInputUserId())) {
                   return true;
                }
                break;
            case 4:
                if (StringUtils.isEmpty(requestDto.getOpUserId()))
                   return true;
                break;
        }
        return false;
    }

    /**
     *
     * @param arbitrationId 是上面查询接口返回的信息ID
     * @return 两个obj(wzCommonManageArbitration,wzCommonArbitrationResult),name就是这两个obj
     */
    @ApiOperation(value = "根据ID返回仲裁相关信息,结果返回map中")
    @GetMapping("/selectByArbitrationId")
    public JsonResult selectByArbitrationId(String arbitrationId) {
        if(StringUtils.isBlank(arbitrationId)){
            return JsonResult.fail("仲裁id不能为空");
        }
        Map<String,Object> map=arbitrationAction.selectByArbitrationId(arbitrationId);
        if(map!=null && map.isEmpty()){
            return JsonResult.fail("没有找到相对应的仲裁信息记录");
        }
        return JsonResult.successJsonResult(map);
    }

    @PostMapping("/updateArbitrationById")
    @ApiOperation(value = "修改未处理或者对方已申诉的仲裁管理表")
    public JsonResult updateArbitrationById(@Valid @RequestBody ArbitrationUpdateRequestDto requestDto,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            if (requestDto.getStatusCode() == ArbitrationEnum.ARBITRATION_DOESNT_SETTLE.getCode() ||
                    requestDto.getStatusCode() == ArbitrationEnum.ARBITRATION_OTHER_COMPLAINT.getCode()) {
                CommonManageArbitration wzCommonManageArbitration=commonServiceProvider.getArbitrationService().selectById(requestDto.getArbitrationId());
                //投诉方,就修改投诉方有关信息
                if(wzCommonManageArbitration==null){
                    return JsonResult.fail("获取数据库记录失败");
                }
                if(wzCommonManageArbitration.getFromUserId()==requestDto.getUserId()){
                    //如果有值但是指没有修改过就不更新该字段
                    if(requestDto.getReason().length()>0&&wzCommonManageArbitration.getReason().equals(requestDto.getReason()) &&
                            requestDto.getTheme().length()>0 && requestDto.getTheme().equals(wzCommonManageArbitration.getTheme())){
                        return JsonResult.success("没有任何修改数据变更");
                    }
                    //直接更新
                    if(requestDto.getReason().length()>0 && requestDto.getTheme().length()>0){
                        wzCommonManageArbitration.setReason(requestDto.getReason());
                        wzCommonManageArbitration.setTheme(requestDto.getTheme());
                        if(commonServiceProvider.getArbitrationService().updateById(wzCommonManageArbitration)){
                            return JsonResult.success("作为投诉者修改成功");
                        }
                        return JsonResult.fail("修改数据库数据失败");
                    }
                }
                else if(wzCommonManageArbitration.getToUserId()==requestDto.getUserId()){
                    if(requestDto.getAppealSrcUrl().length()>0&&wzCommonManageArbitration.getAppealSrcUrl().equals(requestDto.getAppealSrcUrl()) &&
                            requestDto.getDescriptions().length()>0 && requestDto.getDescriptions().equals(wzCommonManageArbitration.getDescriptions())){
                        return JsonResult.success("没有任何修改数据变更");
                    }
                    //直接更新
                    if(requestDto.getDescriptions().length()>0 && requestDto.getAppealSrcUrl().length()>0){
                        wzCommonManageArbitration.setAppealSrcUrl(requestDto.getAppealSrcUrl());
                        wzCommonManageArbitration.setDescriptions(requestDto.getDescriptions());
                        wzCommonManageArbitration.setAppealDate(new Date());
                        if(commonServiceProvider.getArbitrationService().updateById(wzCommonManageArbitration)){
                            return JsonResult.success("作为被投诉者修改成功");
                        }
                        return JsonResult.fail("修改数据库数据失败");
                    }
                }
            } else {
                return JsonResult.success("无法修改已经为处理的仲裁信息");
            }
            return JsonResult.fail("操作异常");
        }catch (Exception e){
            logger.error("修改仲裁信息异常："+e.getMessage(),e);
            return JsonResult.fail("修改仲裁信息异常");
        }

    }

    @ApiOperation(value = "查询仲裁列表",notes = "根据投诉的时间升序进行返回所有的仲裁信息")
    @PostMapping("/getArbitrationListByPage")
    public JsonResult getArbitrationListByPage(@Valid @RequestBody ArbitrationSelectByTypeVo requestDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<CommonManageArbitration> list=arbitrationAction.getWaitingSettleArbitration(requestDto);
            if(list.isEmpty()){
                return JsonResult.fail("仲裁列为空");
            }
            return JsonResult.success().addResult("list",list);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询仲裁列表异常");
        }

    }


    @ApiOperation(value = "外部服务，查看是否已经是申诉状态",notes = "远程接口")
    @PostMapping("/checkIsComplainted")
    public JsonResult checkIsComplainted(@Valid @RequestBody CheckOrderArbitrationRequestDto request){
        try {
            Boolean result = false;
            CommonManageArbitration wzCommonManageArbitration=commonServiceProvider.getArbitrationService().selectById(request.getArbitrationId());
            if (wzCommonManageArbitration != null) {
                if (wzCommonManageArbitration.getStatusCode() != ArbitrationEnum.ARBITRATION_DOESNT_SETTLE.getCode()) {
                    result = true;
                }
            }
            return JsonResult.success().addResult("result",result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查看申诉状态异常");
        }
    }

    @ApiOperation(value = "外部服务，获取仲裁结果信息，然后就进行仲裁操作")
    @PostMapping("/getArbitrationResultVo")
    public JsonResult getArbitrationResultVo(@Valid @RequestBody CheckOrderArbitrationRequestDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            ArbitrationResultResponseVo arbitrationResultResponseVo = arbitrationResultAction.getArbitrationResultResponseVo(request.getArbitrationId());
            if(arbitrationResultResponseVo != null){
                return JsonResult.success().addResult("result",arbitrationResultResponseVo);
            }else{
                return JsonResult.success();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取仲裁结果信息异常");
        }
    }
}
