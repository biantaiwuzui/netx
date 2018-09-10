package com.netx.fuse.client.ucenter;

import com.netx.ucenter.biz.common.SensitiveAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveClientAction {

    @Autowired
    SensitiveAction sensitiveAction;
    /**
     * 敏感词判断
     * @param map
     * @return
     */
    public Map<String,Boolean> filtering(Map<String,String> map){
        if(map==null || map.isEmpty()){
            return new HashMap<>();
        }
        return sensitiveAction.checkValue(map);
    }
}
