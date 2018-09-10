package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonExamineFinanceMapper;
import com.netx.ucenter.model.common.CommonExamineFinance;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * Created by 85169 on 2017/12/5.
 */
@Service
public class ExamineFinanceService extends ServiceImpl<CommonExamineFinanceMapper, CommonExamineFinance>{

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonExamineFinance>().where("create_user_id={0}",userId));
    }

    public List<CommonExamineFinance> getWzCommonExamineFinancesByStatus(Integer status){
        EntityWrapper<CommonExamineFinance> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("status={0}", status);
        return this.selectList(entityWrapper);
    }

    public CommonExamineFinance queryExamineFinancesNotAudit(Integer status,Boolean isCreateTimeAsc) {
        EntityWrapper<CommonExamineFinance> entityWrapper = new EntityWrapper<>();
        entityWrapper.setEntity(new CommonExamineFinance());
        entityWrapper.where("status={0}", 0);
        entityWrapper.orderBy("create_time"+getIsAscStr(isCreateTimeAsc));
        return selectOne(entityWrapper);
    }

    private String getIsAscStr(Boolean isAsc){
        return isAsc?" asc":" desc";
    }
}
