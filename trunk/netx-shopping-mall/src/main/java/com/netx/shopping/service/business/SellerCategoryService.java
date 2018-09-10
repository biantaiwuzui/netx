package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerCategoryMapper;
import com.netx.shopping.model.business.SellerCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class SellerCategoryService extends ServiceImpl<SellerCategoryMapper, SellerCategory> {

    public boolean deleteBySellerId(String sellerId){
        EntityWrapper<SellerCategory> wrapper = new EntityWrapper<>();
        wrapper.in("seller_id",sellerId);
        return this.delete(wrapper);
    }

    public List<String> selectSellerCategoryList(String sellerId){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("category_id").in("seller_id",sellerId);
        return this.selectObjs(wrapper);
    }

    public List<String> getCategoryIdBySellerId(String sellerId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("category_id");
        wrapper.where("seller_id = {0} AND deleted = 0", sellerId);
        return this.selectObjs(wrapper);
    }
}