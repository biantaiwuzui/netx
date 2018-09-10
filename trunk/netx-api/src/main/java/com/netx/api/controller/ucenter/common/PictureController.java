package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.utils.time.DateTimeFormatterProvider;
import com.netx.utils.time.DateUtils;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.ucenter.biz.common.AliyunPictureAction;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by CloudZou on 1/10/2018.
 */
@Api(description = "图片上传和下载API")
@RestController
@RequestMapping("/api/picture")
public class PictureController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(PictureController.class);

    @Autowired
    private AliyunPictureAction aliyunPictureService;

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    @RequestMapping(value = "/upload")
    @ResponseBody
    public JsonResult upload(@RequestParam("file") MultipartFile file, @RequestParam("bucketType") String bucketType) {
        try {
            AliyunBucketType type;
            try {
                type = AliyunBucketType.valueOf(bucketType);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                return JsonResult.fail("图片归属类型不合法");
            }
            String contentType = file.getContentType();
            if(!contentType.startsWith("image")) {
                return JsonResult.fail("无效的图片");
            }

            String filename = file.getOriginalFilename();

            // 获取图片后缀名
            final String imageType = filename.substring(filename.lastIndexOf("."));

            String realPictureName = UUID.randomUUID().toString().replace("-", "").toUpperCase() + imageType;
            String folder = DateUtils.formatNow(DateTimeFormatterProvider.yyyyMM);
            String relativePath = "/" + folder + "/" + realPictureName;

            //上传到阿里云服务器
            try {
                if(aliyunPictureService.uploadPicture(file, type.getName(), folder, realPictureName)!=null){
                    //return Result.newSuccess(relativePath);
                    /*String fullPath = uploadImgDomainPre + type.getName() + uploadImgDomainSuffix + relativePath;
                    return Result.newSuccess(fullPath);*/
                    //return Result.newSuccess(addImgUrlPreUtil.addImgUrlPre(relativePath,type));
                    return successResult(getImgUrl(relativePath,type));
                }
                return JsonResult.fail("上传失败");
            } catch(Exception e) {
                logger.error(String.format("图件:%s上传阿里云失败", realPictureName), e);
                return JsonResult.fail("上传文件错误");
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("上传文件错误");
        }
    }

    private Map<String,Object> getImgUrl(String url,AliyunBucketType aliyunBucketType){
        Map<String,Object> map = new HashMap<>();
        map.put("fullPath",addImgUrlPreUtil.addImgUrlPre(url,aliyunBucketType));
        map.put("relativePath",url);
        return map;
    }

    @RequestMapping(value = "/uploadNet")
    @ResponseBody
    public JsonResult uploadNet(@RequestParam("url") String url,@RequestParam("bucketType") String bucketType) {
        AliyunBucketType type;
        try {
            type = AliyunBucketType.valueOf(bucketType);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("图片归属类型不合法");
        }
        InputStream inStream = aliyunPictureService.downloadNetPicture(url);
        if(inStream==null){
            return JsonResult.fail("网络图片下载失败");
        }
        try {
            String newUrl = aliyunPictureService.uploadNetPicture(inStream,type);
            return newUrl==null?JsonResult.fail("上传失败"):successResult(getImgUrl(newUrl,type));
        } catch(Exception e) {
            logger.error(String.format("网络图片上传阿里云失败"), e);
            return JsonResult.fail("上传文件错误");
        }
    }
}
