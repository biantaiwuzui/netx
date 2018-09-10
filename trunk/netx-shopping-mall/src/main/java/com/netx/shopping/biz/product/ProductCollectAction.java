package com.netx.shopping.biz.product;

import com.netx.common.vo.business.AddCollectGoodsRequestDto;
import com.netx.shopping.model.product.ProductFavorite;
import com.netx.shopping.service.product.ProductCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 网商-商品关注表 服务实现类
 * </p>
 *
 * @author 李威
 * @since 2018-01-26
 */
@Service
public class ProductCollectAction{

    @Autowired
    ProductCollectService productCollectService;

    public boolean collectSeller(AddCollectGoodsRequestDto requestDto){
        ProductFavorite collect = new ProductFavorite();
        collect.setUserId(requestDto.getUserId());
        collect.setProductId(requestDto.getGoodsId());
        ProductFavorite isCollect = this.isHaveCollect(collect);
        if( null == isCollect){  //没有关注过就添加关注
            return productCollectService.insert(collect);
        }else{       //取消取消
            return productCollectService.deleteById(isCollect.getId());
        }
    }

    
    public ProductFavorite isHaveCollect(ProductFavorite collect){
        return productCollectService.isHaveCollect(collect);
    }

    
    public List<String> getGoodsCollectListByUserId(String userId){
        return productCollectService.getProductIdsByUserId(userId);
    }

}
