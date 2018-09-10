package com.netx.shopping.biz.order;


import com.netx.shopping.model.order.ProductOrderItem;
import com.netx.shopping.service.order.ProductOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网商-商品订单详情表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("productOrderItemAction")
public class ProductOrderItemAction{

    @Autowired
    ProductOrderItemService productOrderItemService;

    public List<ProductOrderItem> getListByOrderId(String orderId) {
        return productOrderItemService.getProductOrderItemListByOrderId(orderId);
    }
}
