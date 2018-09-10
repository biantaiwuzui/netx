package com.netx.shopping.mapper.business;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.shopping.model.business.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 商家类目表 Mapper 接口
 * </p>
 *
 * @author 李威
 * @since 2018-04-02
 */
public interface CategoryMapper extends BaseMapper<Category> {

//    List<Category> selectKidTags(@Param("pid") String pid, @Param("currentPage") Integer currentPage, @Param("size")  Integer size);
}