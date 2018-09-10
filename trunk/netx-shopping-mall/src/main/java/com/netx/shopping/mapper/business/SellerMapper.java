package com.netx.shopping.mapper.business;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.common.common.exception.ClientException;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.vo.SellerAgentDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 网商-商家表 Mapper 接口
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
public interface SellerMapper extends BaseMapper<Seller> {

    boolean updateSellerRedpacket();

    boolean emptySellerRedpacket();

    boolean updateDayNum();

    List<Seller> getRelatedSellersNotMoneyUserByUserId(@Param("userId") String userId,@Param("manageId") String[] manageId,
                                                       @Param("current") Integer current, @Param("size") Integer size);

    String getAllGoodsOderByUserId(@Param("userId") String userId, @Param("cashierId")String[] cashierIds, @Param("manageId") String[] manageId);

    List<SellerAgentDto> selectSellersAndRandNo(@Param("userId") String userId);

    Long selectSellerAndRandNo(@Param("id") String id,@Param("provinceCode") String provinceCode,@Param("cityCode") String cityCode);

    List<Seller> getSellerbyIn(@Param("id") String id) throws ClientException;
}