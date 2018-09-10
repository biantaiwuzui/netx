package com.netx.ucenter.biz.common;

import com.netx.common.vo.common.ExamineRedpacketResponseDto;
import com.netx.common.vo.common.LuckMoneyQueryDto;
import com.netx.common.vo.common.LuckyMoneySaveOrUpdateDto;
import com.netx.common.vo.common.WzCommonLuckyMoneyAddDto;
import com.netx.ucenter.model.common.CommonLuckyMoney;
import com.netx.ucenter.service.common.LuckyMoneyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by wongloong on 17-9-8
 */
@Service
public class LuckyMoneyAction{

    @Autowired
    private LuckyMoneyService luckyMoneyService;

    public List<CommonLuckyMoney> query(LuckMoneyQueryDto request) throws Exception {
        return luckyMoneyService.query(request.getStatus(), request.getTime());
    }

    public boolean examine(ExamineRedpacketResponseDto request) throws Exception {
        if (request.getStatus()==1){
            List<String> list=luckyMoneyService.getIdsByStatus(3);
            if (list.size()>0){
                luckyMoneyService.deleteBatchIds(list);
            }
            for (int i=0;i<request.getIds().size();i++){
                CommonLuckyMoney wzCommonLuckyMoney=luckyMoneyService.selectById(request.getIds().get(i));
                wzCommonLuckyMoney.setStatus(3);
                wzCommonLuckyMoney.setExamineUserId(request.getExamineUserId());
                luckyMoneyService.updateById(wzCommonLuckyMoney);
            }
        }else{
            for (int i=0;i<request.getIds().size();i++){
                luckyMoneyService.deleteById(request.getIds().get(i));
            }
        }
        return true;
    }

    public boolean add(LuckyMoneySaveOrUpdateDto requestDto) throws Exception {
        Integer status;
        if (requestDto.getType()==1){
            //超级管理员
            status=3;
        }else {
            //普通管理员
            LuckMoneyQueryDto dto = new LuckMoneyQueryDto();
            dto.setStatus(2);
            List<CommonLuckyMoney> check = this.query(dto);
            if (check.size() != 0) {
                throw new Exception("存在未审核设置！");
            }
            status=2;
        }
        List<CommonLuckyMoney> wzCommonLuckyMonies = new ArrayList<>();
        BigDecimal count = BigDecimal.ZERO;
        for (WzCommonLuckyMoneyAddDto lm : requestDto.getList()) {
            count = count.add(lm.getSendCount());
            CommonLuckyMoney wzCommonLuckyMoney = new CommonLuckyMoney();
            BeanUtils.copyProperties(lm, wzCommonLuckyMoney);
            wzCommonLuckyMoney.setCreateTime(new Date());
            wzCommonLuckyMoney.setStatus(status);
            wzCommonLuckyMonies.add(wzCommonLuckyMoney);
        }
        if (count.compareTo(new BigDecimal(100)) != 0) {
            throw new Exception("发放合计必须为100%");
        }
        boolean res = luckyMoneyService.insertBatch(wzCommonLuckyMonies);
        return res;
    }

    public boolean updateLuckMoneySet() throws Exception {
        List<CommonLuckyMoney> list=luckyMoneyService.getWzCommonLuckyMoneysByStatus(3);
        if (list.size()>0){
            luckyMoneyService.deleteByStatus(1);
            for (CommonLuckyMoney wzCommonLuckyMoney:list){
                wzCommonLuckyMoney.setStatus(1);
                luckyMoneyService.updateById(wzCommonLuckyMoney);
            }
        }
        return true;
    }
}

