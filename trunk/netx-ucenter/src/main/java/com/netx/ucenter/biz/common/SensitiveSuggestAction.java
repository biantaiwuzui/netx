package com.netx.ucenter.biz.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.SensitiveAddRequestDto;
import com.netx.common.vo.common.SensitiveAuditListRequestDto;
import com.netx.common.vo.common.SensitiveSuggestAuditRequestDto;
import com.netx.common.vo.common.QuerySensitiveSuggestListResponseDto;
import com.netx.ucenter.model.common.CommonSensitive;
import com.netx.ucenter.model.common.CommonSensitiveSuggest;
import com.netx.ucenter.service.common.SensitiveService;
import com.netx.ucenter.service.common.SensitiveSuggestService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by CHEN-QIAN 2018-5-29 重写
 */
@Service
public class SensitiveSuggestAction{
    private Logger logger = LoggerFactory.getLogger(SensitiveSuggestAction.class);

    @Autowired
    private SensitiveSuggestService sensitiveSuggestService;
    @Autowired
    private SensitiveService sensitiveService;

    /**
     * CHEN-QIAN
     * 添加过滤词建议
     * @param requestDto
     * @return
     */
    public CommonSensitiveSuggest addSensitiveSuggest(SensitiveAddRequestDto requestDto){
        CommonSensitiveSuggest commonSensitiveSuggest = new CommonSensitiveSuggest();
        VoPoConverter.copyProperties(requestDto, commonSensitiveSuggest);
        if (requestDto.getDelOrSave() == 1 && StringUtils.isNotBlank(requestDto.getDelReason())) {
            commonSensitiveSuggest.setReason(requestDto.getDelReason());
        }
        if(sensitiveSuggestService.insert(commonSensitiveSuggest)){
            return commonSensitiveSuggest;
        }
        return null;
    }

    /**
     * CHEN-QIAN
     * 判断是否已有类似-过滤词
     * @param requestDto
     * @return
     */
    public String repeatSensitiveSuggest(SensitiveAddRequestDto requestDto){
        if(sensitiveSuggestService.getCountByValueAndDelOrSave(requestDto.getValue(), requestDto.getDelOrSave()) > 0){
            return "已有类似过滤词提交！";
        }
        return null;
    }

    /**
     * CHEN-QIAN
     * 分页查询 已通过/未通过/全部 过滤词审核列表
     * @param requestDto
     * @return
     */
    public List<QuerySensitiveSuggestListResponseDto> querySensitiveSuggestList(SensitiveAuditListRequestDto requestDto){
        Page page = new Page();
        page.setSize(requestDto.getSize());
        page.setCurrent(requestDto.getCurrent());
        return createCommonSensitiveSuggest(sensitiveSuggestService.querySensitiveSuggestList(page, requestDto.getStatus(), requestDto.getDelOrSave()));
    }

    private List<QuerySensitiveSuggestListResponseDto> createCommonSensitiveSuggest(List<CommonSensitiveSuggest> sensitiveSuggests){
        List<QuerySensitiveSuggestListResponseDto> responseDtos = new ArrayList<>();
        for(CommonSensitiveSuggest sensitiveSuggest:sensitiveSuggests){
            QuerySensitiveSuggestListResponseDto responseDto = new QuerySensitiveSuggestListResponseDto();
            VoPoConverter.copyProperties(sensitiveSuggest, responseDto);
            String [] values = sensitiveSuggest.getValue().split(",");
            List<String> list = new ArrayList<>();
            for(String value:values){
                list.add(value);
            }
            responseDto.setValues(list);
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    /**
     * CHEN-QIAN
     * 批量审核过滤词
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean audit(SensitiveSuggestAuditRequestDto request) {
        List<CommonSensitiveSuggest> suggests = sensitiveSuggestService.selectBatchIds(request.getIds());
        for(CommonSensitiveSuggest suggest: suggests) {
            suggest.setAuditUserId(request.getAuditUserId());
            if (request.getPassOrRefuse().intValue() == 0) {
                String[] values = suggest.getValue().split(",");
                for (String value:values) {
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    if (suggest.getDelOrSave().intValue() == 0) {
                        Integer count = sensitiveService.getCountByValue(value);
                        if (count < 1) {
                            CommonSensitive sensitive = new CommonSensitive();
                            sensitive.setValue(value);
                            sensitive.setSuggestUserId(suggest.getSuggestUserId());
                            sensitive.setSuggestUserName(suggest.getSuggestUserName());
                            sensitiveService.insert(sensitive);
                        }
                    } else {
                        List<CommonSensitive> sensitives = sensitiveService.getCommonSensitiveByValue(value);
                        for(CommonSensitive sensitive:sensitives){
                            sensitive.setDeleted(1);
                            sensitive.setDelReason(suggest.getReason());
                            sensitiveService.updateById(sensitive);
                        }
                    }
                }
            }else{
                suggest.setDeleted(1);
            }
            sensitiveSuggestService.updateById(suggest);
        }
        return true;
    }
}
