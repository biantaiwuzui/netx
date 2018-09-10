package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.enums.ProductStatusEnum;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.model.productcenter.ProductManagement;
import com.netx.shopping.mapper.productcenter.ProductManagementMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.vo.ProductForceUpRequestDto;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-24
 */
@Service
public class ProductManagementService extends ServiceImpl<ProductManagementMapper, ProductManagement> {

    /**
     * 下架表:插入被强制下架商品信息,状态为3.
     * @param productForceUpRequestDto
     * @return
     */
    public boolean insertforceDownProduct(ProductForceUpRequestDto productForceUpRequestDto)  {
        ProductManagement productManagement=new ProductManagement();
        productManagement.setProductId(productForceUpRequestDto.getProductId());
        productManagement.setUserId(productForceUpRequestDto.getUserId());
        productManagement.setReason(productForceUpRequestDto.getReason());
        productManagement.setOnlineStatus(productForceUpRequestDto.getOnlineStatus());
        productManagement.setDeleted(0);
        return this.insert(productManagement);
    }

    /**
     * 下架表:更新商品状态为1,和描述
     * @param productId
     * @param onlineStatus
     * @return
     */
    public boolean changeProductManagerOnlineStatusByProductId(String productId, Integer onlineStatus, String reason){
        EntityWrapper<ProductManagement> wrapper = new EntityWrapper<>();
        wrapper.where("product_id = {0}", productId);
        ProductManagement productManagement = new ProductManagement();
        productManagement.setOnlineStatus(onlineStatus);
        productManagement.setReason(reason);
        return this.update(productManagement, wrapper);
    }

}
