package com.netx.utils.security.rejectflush;

import com.netx.utils.json.JsonResult;
import com.netx.utils.checker.AjaxChecker;
import com.netx.utils.common.SerializeUtil;
import com.netx.utils.json.ApiCode;
import com.netx.utils.security.GeneralSecretUtil;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Runshine
 * @since 2015-7-17
 * @version 1.0.0
 *
 */
public class UserIdentityRationalityValidationNextStep implements NextStep {

    @Override
    public void doNext(HttpServletRequest request, HttpServletResponse response, Serializable blackKey) {
        try {
            if(blackKey != null) {
                byte[] b = SerializeUtil.serialize(blackKey);
                String encode = GeneralSecretUtil.encode(b);
                if(AjaxChecker.isAjax(request)) {
                    JsonResult json = JsonResult.fail("请求过于频繁，请休息片刻再来访问", ApiCode.REDIRECT)
                            .addResult("Location", "/userx/identity/rationality/validation/" + encode);
                    json.toJson(response);
                } else {
                    response.sendRedirect("/userx/identity/rationality/validation/" + encode);
                }
            }
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
