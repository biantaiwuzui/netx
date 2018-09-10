package com.netx.shopping.biz.order;

import com.netx.common.vo.business.*;
import com.netx.shopping.model.order.ProductReturn;
import com.netx.shopping.service.order.ProductReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 网商-商品退货表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-10-24
 */
@Service("productReturnAction")
@Transactional(rollbackFor = Exception.class)
public class ProductReturnAction {

    @Autowired
    ProductReturnService productReturnService;
    
    public ProductReturn get(GetGoodsReturnRequestDto request){
        return productReturnService.selectById(request.getId());
    }

    
    public ProductReturn getGoodsReturnByOrderId(String orderId){
        return productReturnService.getProductReturnByOrderId(orderId);
    }
}
