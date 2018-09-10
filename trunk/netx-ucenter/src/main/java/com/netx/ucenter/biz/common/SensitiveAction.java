package com.netx.ucenter.biz.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.ucenter.model.common.CommonSensitive;
import com.netx.ucenter.service.common.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-9-9
 */
@Service
public class SensitiveAction{

    @Autowired
    private SensitiveService sensitiveService;

    /**
     * 敏感词判断
     * @param mapValue
     * @return
     */
    public Map<String,Boolean> checkValue(Map<String,String> mapValue){
        Map<String,Boolean> map = new HashMap<>();
        if(mapValue!=null && !mapValue.isEmpty()){
            for(String key : mapValue.keySet()){
                map.put(key,sensitiveService.checkValue(mapValue.get(key))>0);
            }
        }
        return map;
    }

    /**
     * 分页查找敏感词
     * @param current
     * @param size
     * @return
     */
    public List<CommonSensitive> querySensitiveList(Integer current, Integer size){
        Page page = new Page();
        page.setSize(size);
        page.setCurrent(current);
        return sensitiveService.selectPage(page).getRecords();
    }
}
