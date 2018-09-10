package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.product.ProductSpecMapper;
import com.netx.shopping.model.product.ProductSpec;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ProductSpecService extends ServiceImpl<ProductSpecMapper, ProductSpec> {

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean delete(String id){
        EntityWrapper<ProductSpec> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0}", id);
        ProductSpec productSpec =new ProductSpec();
        productSpec.setDeleted(1);
        return  this.update(productSpec, wrapper);
    }

    /**
     * 根据userID注销商品商品规格
     * @param userId
     * @return
     */
    public boolean deleteByUserId(String userId){
        //注销商品商品规格
        ProductSpec productSpec = new ProductSpec();
        productSpec.setDeleted(1);
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}", userId);
        return this.update(productSpec, wrapper);
    }

    /**
     * 获取商品分类列表
     * @param userId
     * @return
     */
    public List<ProductSpec> specList(String userId){
        EntityWrapper<ProductSpec> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and deleted = {1}", userId, 0);
        return this.selectList(wrapper);
    }

    /**
     * 根据ids获取列表
     * @param ids
     * @return
     */
    public List<ProductSpec> getSpecListByIds(String ids){
        EntityWrapper<ProductSpec> wrapper = new EntityWrapper<>();
        wrapper.in("id", ids);
        return this.selectList(wrapper);
    }

    public String getNameById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("name").where("id = {0} AND deleted = 0", id);
        return (String)this.selectObj(wrapper);
    }
}
