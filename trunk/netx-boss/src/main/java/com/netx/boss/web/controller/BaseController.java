package com.netx.boss.web.controller;

import com.netx.boss.web.security.TokenHelper;
import com.netx.utils.json.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    protected TokenHelper tokenHelper;

    public final JsonResult baseResult(boolean success, String res) {
        JsonResult result = new JsonResult(success, res);
        return result;
    }

    public JsonResult getResult(boolean success,String msg) {
        if(success) {
            return JsonResult.success();
        }else {
            return JsonResult.fail(msg);
        }
    }

    public JsonResult getResult(boolean success,String successMsg,String failMsg) {
        if(success) {
            return JsonResult.success(successMsg);
        }else {
            return JsonResult.fail(failMsg);
        }
    }

    public final JsonResult success() {
        return success("");
    }

    public final JsonResult success(String res) {
        return baseResult(true, res);
    }

    public final JsonResult fail(String res) {
        return baseResult(false, res);
    }


}
