package com.netx.shopping.biz.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.common.vo.business.AddProductSpeRequertDto;
import com.netx.shopping.model.product.Product;
import com.netx.shopping.model.product.ProductSpe;
import com.netx.shopping.service.product.ProductService;
import com.netx.shopping.service.product.ProductSpeService;
import com.netx.utils.money.Money;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("ProductSpeAction")
@Transactional(rollbackFor = Exception.class)
public class ProductSpeAction {

    @Autowired
    ProductSpeService productSpeService;

    public boolean insertOrUpdateList(List<AddProductSpeRequertDto> requert,String productId,String userId){
        List<ProductSpe> list = new ArrayList<>();
        if (requert != null){
            for (AddProductSpeRequertDto addProductSpeRequertDto : requert){
                ProductSpe productSpe = new ProductSpe();
                BeanUtils.copyProperties(addProductSpeRequertDto,productSpe);
                productSpe.setPrice(new Money(addProductSpeRequertDto.getPrice()).getCent());
                productSpe.setProductId(productId);
                productSpe.setCreateUserId(userId);
                list.add(productSpe);
            }
            return productSpeService.insertOrUpdateList(list);
        }
        return true;
    }

    public boolean insertOrUpdate(boolean flag,String productId,List<AddProductSpeRequertDto> requert,String userId){
        if (flag) {
            List<String> reqIds = new ArrayList<>();
            if (requert != null){
                for (AddProductSpeRequertDto addProductSpeRequertDto : requert){
                    if (!StringUtils.isEmpty(addProductSpeRequertDto.getId())){
                        reqIds.add(addProductSpeRequertDto.getId());
                    }
                }
            }
            List<String> speIds = productSpeService.getProductSpeIdList(productId);
            speIds.removeAll(reqIds);
            if (speIds.size() > 0){
                productSpeService.deleteList(speIds);
            }
        }
        return this.insertOrUpdateList(requert,productId,userId);
    }

    public List<ProductSpe> getProductSpeList(String productId){
        return productSpeService.getProductSpeList(productId);
    }
}
