package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.product.ProductFavoriteMapper;
import com.netx.shopping.model.product.ProductFavorite;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ProductCollectService extends ServiceImpl<ProductFavoriteMapper, ProductFavorite> {
    /**
     * 根据userId获取收藏商品数量
     * @param userId
     * @return
     */
    public int getProductCollectCountByUserId(String userId){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("user_id={0}",userId);
        return this.selectCount(wrapper);
    }

    /**
     * 是否已收藏
     * @param collect
     * @return
     */
    public ProductFavorite isHaveCollect(ProductFavorite collect){
        EntityWrapper<ProductFavorite> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}", collect.getUserId())
                .andNew("product_id = {0}", collect.getProductId());
        return this.selectOne(wrapper);
    }

    public List<String> getProductIdsByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("product_id").where("user_id = {0}", userId);
        wrapper.orderBy("create_time", false);
        return this.selectObjs(wrapper);
    }
}
