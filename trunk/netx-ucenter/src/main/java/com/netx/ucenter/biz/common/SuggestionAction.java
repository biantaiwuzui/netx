package com.netx.ucenter.biz.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.vo.common.SuggestionAddRequestDto;
import com.netx.ucenter.model.common.CommonSuggestion;
import com.netx.ucenter.service.common.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SuggestionAction {
    @Autowired
    private SuggestionService suggestionService;

    public Page<CommonSuggestion> getListByPage(Integer current, Integer size) throws Exception{
        Page<CommonSuggestion> page = new Page(current,size);
        return suggestionService.getListByPage(page);
    }

    public Boolean insert(SuggestionAddRequestDto request) throws Exception{
        CommonSuggestion suggestion = new CommonSuggestion();
        suggestion.setCreateUserId(request.getUserId());
        suggestion.setNickname(request.getUserName());
        suggestion.setSuggestContent(request.getContent());
        suggestion.setCreateTime(new Date());
        suggestion.setUserId(request.getUserId());
        return suggestionService.insert(suggestion);
    }
}
