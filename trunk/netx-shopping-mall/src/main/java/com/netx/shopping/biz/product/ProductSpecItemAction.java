package com.netx.shopping.biz.product;

import com.netx.common.vo.business.DelectGoodsSpecImplRequestDto;
import com.netx.shopping.service.product.ProductSpecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 网商-商品规格分类项目 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("productSpecItemAction")
@Transactional(rollbackFor = Exception.class)
public class ProductSpecItemAction {

    @Autowired
    ProductSpecItemService productSpecItemService;

    public boolean delete(DelectGoodsSpecImplRequestDto request) {
        return productSpecItemService.delete(request);
    }


}
