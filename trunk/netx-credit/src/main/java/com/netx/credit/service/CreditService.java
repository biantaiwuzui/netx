package com.netx.credit.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.credit.mapper.CreditMapper;
import com.netx.credit.model.Credit;
import com.netx.credit.vo.PublishCreditRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CreditService
 *
 * @author FRWIN
 * @since 2018-07-06
 */
@Service
public class CreditService extends ServiceImpl<CreditMapper,Credit>{
    @Autowired
    CreditMapper creditMapper;

    public String getId(String id){
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.where("id={0}",id);
        entityWrapper.setSqlSelect("id");
        return selectById(id).getId();
    }

    // 检验发售信息
    public Credit isCanPublishCredit(PublishCreditRequestDto requestDto, String userId) {
        EntityWrapper<Credit> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}", userId)
                .andNew("name = {0}",requestDto.getName())
                .andNew("merchant_id = {0}", requestDto.getMerchantId())
                .andNew("issue_number = {0}", requestDto.getIssueNumber())
                .andNew("deleted = {0}", 0);
        return this.selectOne(wrapper);
    }

    // 网信认购流水 - 获取收银人id
    public String getCashierId(String creditId) {
        EntityWrapper<Credit> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("cashier_id");
        wrapper.where("id = {0}", creditId);
        return (String)selectObj(wrapper);
    }

    // 发布网信前 - 获取预售上限
    public Integer getUpperLimit(String userId, String merchantId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("presale_upper_limit");
        wrapper.where("user_id = {0} and merchantId = {1}", userId, merchantId);
        return (Integer)selectObj(wrapper);
    }

    //获取credit_Id
    public List<Credit> getCreditId (){
        EntityWrapper<Credit> wrapper = new EntityWrapper<>();
        return selectList(wrapper);
    }

}
