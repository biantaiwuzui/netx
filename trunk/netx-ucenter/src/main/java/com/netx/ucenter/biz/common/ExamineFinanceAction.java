package com.netx.ucenter.biz.common;

import com.netx.common.vo.common.AuditExamineFinanceDto;
import com.netx.common.vo.common.ExamineFinanceDto;
import com.netx.ucenter.model.common.CommonExamineFinance;
import com.netx.ucenter.service.common.ExamineFinanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Created by 85169 on 2017/12/5.
 */
@Service
public class ExamineFinanceAction {

    @Autowired
    private ExamineFinanceService examineFinanceService;

    public Boolean submitExamineFinance(ExamineFinanceDto request) {
        List<CommonExamineFinance> resultList =  examineFinanceService.getWzCommonExamineFinancesByStatus(0);
        if(resultList != null && resultList.size()>0) {
            return false;
        }else{
            CommonExamineFinance examineFinance = new CommonExamineFinance();
            BeanUtils.copyProperties(request,examineFinance);
            examineFinance.setStatus(0);
            examineFinance.setCreateTime(new Date());
            examineFinanceService.insert(examineFinance);
            return true;
        }
    }

    public CommonExamineFinance queryExamineFinancesNotAudit() {
        return examineFinanceService.queryExamineFinancesNotAudit(0,true);
    }

    public Boolean auditExamineFinances(AuditExamineFinanceDto dto) {
        CommonExamineFinance examineFinance = examineFinanceService.selectById(dto.getId());
        if(examineFinance == null){
            return false;
        }
        examineFinance.setStatus(dto.getStatus());
        examineFinance.setUpdateTime(new Date());
        examineFinance.setUpdateUserId(dto.getUpdateUser());
        examineFinance.setReason(dto.getReason());
        examineFinanceService.insertOrUpdate(examineFinance);
        return true;
    }


}
