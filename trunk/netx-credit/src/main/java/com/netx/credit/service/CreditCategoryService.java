package com.netx.credit.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.credit.mapper.CreditCategoryMapper;
import com.netx.credit.model.CreditCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lanyingchu
 * @date 2018/7/17 19:25
 */
@Service
public class CreditCategoryService extends ServiceImpl<CreditCategoryMapper, CreditCategory> {
    @Autowired
    private CreditCategoryMapper creditCategoryMapper;

    /**
     * 根据creditId获取categoryIds
     */
    public List<String> getCategoryIdsByCreditId(String creditId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("category_id");
        wrapper.in("credit_id", creditId);
        wrapper.where("deleted = 0");
        return this.selectObjs(wrapper);
    }
}
