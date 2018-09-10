package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.product.ProductSpeMapper;
import com.netx.shopping.model.product.ProductSpe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpeService extends ServiceImpl<ProductSpeMapper, ProductSpe> {

    public boolean insertOrUpdateList(List<ProductSpe> list){
        return this.insertOrUpdateBatch(list);
    }

    public boolean deleteByProductId(String productId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("product_id = {0}",productId);
        return this.delete(wrapper);
    }

    public List<ProductSpe> getProductSpeList(String productId){
        EntityWrapper<ProductSpe> wrapper = new EntityWrapper<>();
        wrapper.where("product_id = {0}",productId);
        return this.selectList(wrapper);
    }

    public List<String> getProductSpeIdList(String productId){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id").where("product_id = {0}",productId);
        return this.selectObjs(wrapper);
    }

    public boolean deleteList(List<String> ids){
        EntityWrapper<ProductSpe> wrapper = new EntityWrapper<>();
        wrapper.in("id",ids);
        return this.delete(wrapper);
    }

    public String getNameById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("name").where("id = {0} AND deleted = 0",id);
        return this.selectObj(wrapper).toString();
    }

    public long getMinPrice(String productId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("Min(price)").where("product_id = {0}",productId);
        return (long)this.selectObj(wrapper);
    }

    public List<ProductSpe> selectByProductId(String productId){
        EntityWrapper<ProductSpe> wrapper = new EntityWrapper<>();
        wrapper.where("product_id = {0} AND deleted = 0", productId);
        return this.selectList(wrapper);
    }
}
