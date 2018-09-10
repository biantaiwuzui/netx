package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.product.ProductFavoriteMapper;
import com.netx.shopping.model.product.ProductFavorite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductFavoriteService extends ServiceImpl<ProductFavoriteMapper, ProductFavorite> {

    /**
     * 根据userId获取productId
     * @param userId
     * @return
     */
    public List<String> getProductIdByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("product_id AS productId").where("user_id = {0} AND deleted = 0", userId).isNotNull("product_id");
        return this.selectObjs(wrapper);
    }
}
