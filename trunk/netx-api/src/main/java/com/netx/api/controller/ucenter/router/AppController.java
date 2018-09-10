package com.netx.api.controller.ucenter.router;

import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/")
@Api(description = "APP检测接口")
public class AppController {

    @Value("${app.version}")
    private String appVersion;
    @ApiOperation(value = "版本检测")
    @PostMapping("check")
    public JsonResult check(String version){
        if(StringUtils.isBlank(version)){
            return JsonResult.fail("app版本号不能为空");
        }
        String message = "";
        return JsonResult.success()
                .addResult("check",appVersion.equals(version))
                .addResult("message",message);
    }

}
