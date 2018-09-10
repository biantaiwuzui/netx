package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.user.dto.photo.ExchangePhotoPositionRequestDto;
import com.netx.common.user.dto.photo.HeadImgDto;
import com.netx.common.user.dto.photo.InsertUserPhotoRequestDto;
import com.netx.ucenter.biz.user.UserPhotoAction;
import com.netx.ucenter.model.user.UserPhoto;
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

@Api(description = "用户照片模块")
@RestController
@RequestMapping("/api/userPhoto/")
public class UserPhotoController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(UserPhotoController.class);

    @Autowired
    private UserPhotoAction userPhotoAction;

    @ResponseBody
    @PutMapping("changeHeadImg")
    @ApiOperation(value = "更改用户头像")
    public JsonResult changeHeadImg(@RequestBody HeadImgDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return super.getResult(userPhotoAction.getUserPhotoService().changeHeadImg(request.getUserId(), request.getUrl()),"操作失败");
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("deletePhotoById")
    @ApiOperation(value = "根据照片id删除照片")
    public JsonResult deletePhotoById(String photoId){
        if(StringUtils.isEmpty(photoId)){
            return JsonResult.fail("照片id不能为空");
        }
        try {
            return super.getResult(userPhotoAction.deletePhotoById(photoId),"删除照片失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除异常");
        }
    }

    @ResponseBody
    @PostMapping("exchangePhotoPosition")
    @ApiOperation(value = "两张照片互换位置（位置值为1的作为头像，可以用此方法设置用户某张照片为头像）")
    public JsonResult exchangePhotoPosition(@Valid @RequestBody ExchangePhotoPositionRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return super.getResult(userPhotoAction.exchangePhotoPosition(request),"照片互换失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("照片互换出现异常");
        }
    }

    @ResponseBody
    @PostMapping("insertUserPhoto")
    @ApiOperation(value = "根据用户id插入照片（可为多张，排列位置与上传次序一致）")
    public JsonResult insertUserPhoto(@Valid @RequestBody InsertUserPhotoRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return JsonResult.success().addResult("list", userPhotoAction.insertUserPhoto(request));
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("插入照片出现异常");
        }
    }

    @ResponseBody
    @PostMapping("selectPhotoListByUserId")
    @ApiOperation(value = "根据用户id查询用户照片集（position为1的是头像）")
    public JsonResult selectPhotoListByUserId(String userId){
        if(!StringUtils.hasText(userId)){
            return JsonResult.fail("用户id不能为空");
        }
        try {
            List<UserPhoto> userPhotoList = userPhotoAction.getUserPhotoService().selectPhotoListByUserId(userId,true);
            return JsonResult.success().addResult("list",userPhotoList);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }
}
