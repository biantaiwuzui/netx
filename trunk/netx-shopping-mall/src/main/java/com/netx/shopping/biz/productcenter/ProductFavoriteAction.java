package com.netx.shopping.biz.productcenter;


import com.baomidou.mybatisplus.plugins.Page;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.model.productcenter.ProductFavorite;
import com.netx.shopping.service.productcenter.ProductFavoriteService;
import com.netx.shopping.service.productcenter.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网商-商品关注表
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service("newProductFavoriteAction")
public class ProductFavoriteAction {

    @Autowired
    private ProductFavoriteService productFavoriteService;

    @Autowired
    private ProductService productService;

    /**
     * 关注或取消关注商品
     * @param productId
     * @param userId
     * @return
     */
    public String addOrCancel(String productId,String userId){
        Product product = productService.selectById(productId);
        if(product==null){
            return "你关注的商品不存在";
        }
        ProductFavorite productFavorite = productFavoriteService.queryByUserIdAndProductId(userId,productId);
        if(productFavorite!=null){
            return productFavoriteService.deleteById(productFavorite.getId())?null:"取消关注失败";
        }else{
            productFavorite = new ProductFavorite();
            productFavorite.setProductId(productId);
            productFavorite.setUserId(userId);
            return productFavoriteService.insert(productFavorite)?null:"关注失败";
        }
    }

    /**
     * 根据用户id和商品id查询商品
     * @param userId
     * @param productId
     * @return
     */
    public ProductFavorite getProductFavoriteByUserIdAndProductId(String userId, String productId){
        return productFavoriteService.getProductFavoriteByUserIdAndProductId(userId, productId);
    }

    /**
     * 根据用户id查询已收藏的商品id
     * @param userId
     * @return
     */
    public List<String> getProductIdByUserId(String userId){
        return productFavoriteService.queryProductIdByUserId(userId);
    }

    /**
     * 根据用户id查询收藏的商品数
     * @param userId
     * @return
     */
    public int countProductByUserId(String userId){
        return productFavoriteService.countProductByUserId(userId);
    }
    
    
    /*
     * 根据用户id获取已收藏的商品
     */
    public List<ProductFavorite> getProductIdByUserId(String userId, Page page){
        return productFavoriteService.queryProductIdByUserId(userId,page);
    }
    
}
