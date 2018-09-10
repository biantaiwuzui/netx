package com.netx.ucenter.util;

import org.apache.commons.lang3.StringUtils;

public class SearchProcessing {

    public String SearchProcessing(String name){
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
