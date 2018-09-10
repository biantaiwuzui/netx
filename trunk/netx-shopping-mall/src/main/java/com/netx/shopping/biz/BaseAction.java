package com.netx.shopping.biz;

import org.apache.commons.lang.StringUtils;

/**
 * Created by CloudZou on 3/1/2018.
 */
public class BaseAction {

    public String searchProcessing(String name){
        if(StringUtils.isNotBlank(name)) {
            char c[] = name.toCharArray();
            String s = "";
            for(int i=0;i<c.length-1;i++){
                s += c[i]+"%";
            }
            s += c[c.length-1];
            return s;
        }
        return null;
    }

}
