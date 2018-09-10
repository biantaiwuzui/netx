package com.netx.shopping.mapper.order;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.shopping.model.order.ProductOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
  * 网商-商品订单表 Mapper 接口
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */


public interface ProductOrderMapper extends BaseMapper<ProductOrder> {

    Long getSumOrderAmountBySellerId(@Param("sellerId") String sellerId);

    List<String> getSellerByByDealAmount(@Param("currentPage") Integer currentPage, @Param("size")  Integer size);

    List<ProductOrder> getAllGoodsOderByUserId(@Param("sellerIds") String[] sellerId, @Param("statuslist")String[] statuslist, @Param("currentPage") Integer currentPage, @Param("size")  Integer size);

    List<ProductOrder> getMyGoodsOrderByUserId(@Param("userId") String userId, @Param("statuslist")String[] statuslist, @Param("currentPage") Integer currentPage, @Param("size")  Integer size);

    List<ProductOrder> getProductOrderListByIds(@Param("ids") String[] Ids);
}