package com.netx.credit.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.credit.mapper.CreditCashierMapper;
import com.netx.credit.model.CreditCashier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCashierService extends ServiceImpl<CreditCashierMapper,CreditCashier> {

    @Autowired
    CreditCashierMapper creditCashierMapper;
    /**
     * 根据网信收银id获取网信收银人网号
     */
    public String getCashierNetworkNumber(String creditCashierId) {
        EntityWrapper<CreditCashier> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("cashier_network_num");
        wrapper.where("id = {0}", creditCashierId);
        return (String)selectObj(wrapper);
    }
}
