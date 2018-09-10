package com.netx.shopping.mapper.product;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.common.common.exception.ClientException;
import com.netx.shopping.model.product.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 网商-商品表 Mapper 接口
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 获取商品成交额
     * @param goodsId
     * @return
     * @throws ClientException
     */
    Long getGoodsDealAmount(@Param("goodsId") String goodsId);

    /**
     * 获取订单商品id列表
     * @param orderId
     * @return
     * @throws ClientException
     */
    @Select("select goodsId from wz_business_goods_order_item where orderId = #{orderId}")
    List<String> getGoodsIdsByOrderId(String orderId) throws ClientException;

    List<Product> getGoodsBySellerIdRank(@Param("sellerIds") String sellerIds, Integer currentPage, Integer size);

    List<Map<String,String>> selectGoodsList();
}