package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerProductMapper;
import com.netx.shopping.model.business.SellerProduct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class SellerProductRelService  extends ServiceImpl<SellerProductMapper, SellerProduct> {

    public List<String> getProductIdBySellerId(List<String> sellerId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("product_id AS productId").in("seller_id", sellerId).where("deleted = 0").isNotNull("product_id");
        return this.selectObjs(wrapper);
    }
}
