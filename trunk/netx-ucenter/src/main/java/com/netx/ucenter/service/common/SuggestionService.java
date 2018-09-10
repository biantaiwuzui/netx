package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonSuggestionMapper;
import com.netx.ucenter.model.common.CommonSuggestion;
import org.springframework.stereotype.Service;

/**
 * Create by wongloong on 17-9-9
 */
@Service
public class SuggestionService extends ServiceImpl<CommonSuggestionMapper, CommonSuggestion>{

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonSuggestion>().where("user_id={0} or replacer_id={0}",userId));
    }

    public Page<CommonSuggestion> getListByPage(Page page) throws Exception{
        EntityWrapper entityWrapper = new EntityWrapper(new CommonSuggestion());
        entityWrapper.orderBy("create_time desc");
        return selectPage(page,entityWrapper);
    }
}
