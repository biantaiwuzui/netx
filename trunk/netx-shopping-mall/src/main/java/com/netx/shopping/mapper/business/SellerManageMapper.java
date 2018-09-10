package com.netx.shopping.mapper.business;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.shopping.model.business.SellerManage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 网商-业务主管表 Mapper 接口
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */


public interface SellerManageMapper extends BaseMapper<SellerManage> {

    List<SellerManage> selectListByUserId(@Param("userId")String userId);
}