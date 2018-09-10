package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.business.AddGoodsPackageRequestDto;
import com.netx.shopping.mapper.product.ProductPackageMapper;
import com.netx.shopping.model.product.ProductPackage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ProductPackageService extends ServiceImpl<ProductPackageMapper, ProductPackage> {

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean delete(String id){
        EntityWrapper<ProductPackage> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0}", id);
        ProductPackage productPackage =new ProductPackage();
        productPackage.setDeleted(1);
        return this.update(productPackage, wrapper);
    }

    /**
     * 根据userId注销商品包装明细
     * @param userId
     * @return
     */
    public boolean deleteByUserId(String userId){
        ProductPackage productPackage = new ProductPackage();
        productPackage.setDeleted(1);
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}", userId);
        return this.update(productPackage, wrapper);
    }

    /**
     * 根据userId获取列表
     * @param userId
     * @return
     */
    public List<ProductPackage> getProductPackageListByUserId(String userId){
        EntityWrapper<ProductPackage> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and deleted = {1}", userId, 0);
        return this.selectList(wrapper);
    }

    /**
     * 根据id集获取列表
     * @param ids
     * @return
     */
    public List<ProductPackage> getProductPackageListByIds(String ids){
        EntityWrapper<ProductPackage> wrapper = new EntityWrapper<>();
        wrapper.in("id", ids);
        return this.selectList(wrapper);
    }

    /**
     * 根据条件获取商品包装
     * @param request
     * @return
     */
    public ProductPackage getProductPackage(AddGoodsPackageRequestDto request){
        EntityWrapper<ProductPackage> wrapper=new EntityWrapper<>();
        wrapper.where("user_id = {0} and name = {1} and quantity = {2} and unit_name = {3} and spec = {4} and deleted = {5}",request.getUserId(),request.getName(),request.getQuantity(),request.getUnitName(),request.getSpec(),0);
        return this.selectOne(wrapper);
    }
}
