package com.netx.fuse.client.job;

import com.netx.common.common.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobClient {
    @Value("${xxl.addresses}")
    private String host;

    private Map<String,String> header;

    @Autowired
    public JobClient(){
        header = new HashMap<>();
        header.put("netx-Token","XXL_JOB_LOGIN_IDENTITY");
    }

    public HttpResponse add(Map<String,String> map) throws Exception{
        return HttpUtils.doPost(host,"/jobinfo/add",header,null,map);
    }

    public HttpResponse remove(Map<String,String> map) throws Exception{
        return HttpUtils.doPost(host,"/jobinfo/remove",header,null,map);
    }

    public HttpResponse pause(Map<String,String> map) throws Exception{
        return HttpUtils.doPost(host,"/jobinfo/pause",header,null,map);
    }

    public HttpResponse resume(Map<String,String> map) throws Exception{
        return HttpUtils.doPost(host,"/jobinfo/resume",header,null,map);
    }

}
