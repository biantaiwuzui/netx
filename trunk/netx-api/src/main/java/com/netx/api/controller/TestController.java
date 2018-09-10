package com.netx.api.controller;

import com.netx.api.component.CustomUserDetailsService;
import com.netx.api.component.UserTokenState;
import com.netx.api.security.TokenHelper;
import com.netx.utils.json.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by CloudZou on 3/7/2018.
 */

@RestController
@RequestMapping( value = "/test", produces = MediaType.APPLICATION_JSON_VALUE )
public class TestController {
    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @RequestMapping(value="/getToken", method = RequestMethod.GET)
    public JsonResult getToken(HttpServletRequest request, Principal principal){
        String authToken = tokenHelper.getToken( request );

        Device device = DeviceUtils.getCurrentDevice(request);

        if (authToken != null && principal != null) {
            return JsonResult.success().addResult("principal", principal);
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return JsonResult.fail();
        }

    }
}
