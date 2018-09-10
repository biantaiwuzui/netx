package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.business.DelectGoodsSpecImplRequestDto;
import com.netx.shopping.mapper.product.ProductSpecItemMapper;
import com.netx.shopping.model.product.ProductSpecItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ProductSpecItemService extends ServiceImpl<ProductSpecItemMapper, ProductSpecItem> {

    /**
     * 根据商品分类id删除
     * @param specId
     * @return
     */
    public boolean deleteBySpecId(String specId){
        EntityWrapper<ProductSpecItem> wrapper1 = new EntityWrapper<>();
        wrapper1.where("spec_id = {0}", specId);
        ProductSpecItem productSpecItem = new ProductSpecItem();
        productSpecItem.setDeleted(1);
        return this.update(productSpecItem, wrapper1);
    }

    /**
     * 根据商品分类id或上商品分类项id获取商品分类项
     * @param itemId
     * @param specId
     * @return
     */
    public List<ProductSpecItem> itemList(String itemId, String specId){
        EntityWrapper<ProductSpecItem> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotEmpty(itemId)){
            wrapper.where("id = {0} and deleted = {1}", itemId, 0);
        }
        if(StringUtils.isNotEmpty(specId)){
            wrapper.where("spec_id = {0} and deleted = {1}", specId, 0);
        }
        return this.selectList(wrapper);
    }

    public boolean delete(DelectGoodsSpecImplRequestDto request){
        EntityWrapper<ProductSpecItem> wrapper1 = new EntityWrapper<>();
        wrapper1.where("id = {0}", request.getId());
        ProductSpecItem productSpecItem = new ProductSpecItem();
        productSpecItem.setDeleted(1);
        return this.update(productSpecItem, wrapper1);
    }

}
