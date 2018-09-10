package com.netx.api.security;

import com.alibaba.fastjson.JSONObject;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContentFilter  extends OncePerRequestFilter{

    private Logger logger = LoggerFactory.getLogger("ContentFilter");

    private CommonServiceProvider commonServiceProvider;

    public ContentFilter(CommonServiceProvider commonServiceProvider){
        this.commonServiceProvider = commonServiceProvider;
    }

    private final String[] url = new String[]{
            "/wz/meeting/send",
            "/wz/skill/publish",
            "/wz/wish/publish",
            "/wz/demand/publish",
            "/api/business/merchant/register",
            "/api/business/product/releaseGoods"
    };

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        //因request的body只能使用一次，因此继承重写这个类
        MyHttpServletRequestWrapper wrapper = new MyHttpServletRequestWrapper(request);
        String uri = request.getRequestURI();
        if(StringUtils.isNotBlank(uri) && checkUrl(uri)){
            logger.info("过滤敏感词");
            if(checkBody(wrapper.getRequestBody())){
                JsonResult jsonResult = JsonResult.fail(ApiCode.EXIST_SENSITIVE);
                response.setContentType("application/json;charset=UTF-8");
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(jsonResult.toJson().getBytes());
                return;
            }
        }else{
            logger.info("\n"+wrapper.getRequestBodyString());
        }
        chain.doFilter(wrapper, response);
    }

    private boolean checkUrl(String uri){
        for(String url:this.url){
            if(uri.indexOf(url)!=-1){
                return true;
            }
        }
        return false;
    }

    private boolean checkBody(JSONObject body){
        if(body!=null){
            logger.info("body内容："+body);
            return commonServiceProvider.getSensitiveService().checkValue(body.values());
        }
        return false;
    }
}
