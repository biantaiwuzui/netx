package com.netx.shopping.biz.product;

import com.netx.common.vo.business.AddGoodsPackageRequestDto;
import com.netx.shopping.model.product.ProductPackage;
import com.netx.shopping.service.product.ProductPackageService;
import com.netx.shopping.service.product.ProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 网商-商品包装明细表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("productPackageAction")
@Transactional(rollbackFor = Exception.class)
public class ProductPackageAction{

    @Autowired
    ProductService productService;

    @Autowired
    ProductPackageService productPackageService;

    public ProductPackage addOrUpdate(AddGoodsPackageRequestDto request) throws Exception{
        ProductPackage productPackage = new ProductPackage();
        BeanUtils.copyProperties(request, productPackage);
        if(StringUtils.isNotBlank(request.getId())){
            productPackage.setUpdateUserId(request.getUserId());
        }else{
            if (!this.isCanAdd(request)){
                throw new Exception("已存在相同的包装明细");
            }
            productPackage.setDeleted(0);
            productPackage.setCreateUserId(request.getUserId());
        }
        //-----------------------------------------------liwei---------start-----------------
        Boolean res=productPackageService.insertOrUpdate(productPackage);
        if (res==false)
        {
            return null;
        }
        return productPackage;
    }
    //-----------------------------------------------liwei----------end------------------
    
    public boolean delete(String id){
        //判断此包装明细是否有上架商品在用，是：不能删除
        if (isCanDelect(id)){
            return productPackageService.delete(id);
        }
        return false;
    }

    
    public List<ProductPackage> getGoodsPackageListByUserId(String userId){
        return productPackageService.getProductPackageListByUserId(userId);
    }

    
    public List<ProductPackage> getGoodsPackageListByIds(String ids){
        return productPackageService.getProductPackageListByIds(ids);
    }

    
    public boolean isCanDelect(String id){
        if (productService.getProductCountByPackageId(id)>0){
            return false;
        }
        return true;
    }

    
    public boolean isCanAdd(AddGoodsPackageRequestDto request){
        if (productPackageService.getProductPackage(request) !=null){
            return false;
        }
        return true;
    }
}