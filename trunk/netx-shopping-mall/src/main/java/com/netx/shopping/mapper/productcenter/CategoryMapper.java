package com.netx.shopping.mapper.productcenter;

import com.netx.shopping.model.productcenter.Category;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 商家类目表 Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@Repository("newCategoryMapper")
public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> selectKidTags(@Param("parentId") String parentId, @Param("currentPage") Integer currentPage, @Param("size")  Integer size);

    /**
     * 更新优先级
     * @param priority
     * @param parentId
     * @param num
     * @return
     */
    Boolean updatePriority(@Param("priority") Long priority, @Param("parentId") String parentId, @Param("num") Integer num);

    Boolean deleteByParentId(@Param("parentId") String parentId, @Param("delete")  Integer delete, @Param("deleted")  Integer deleted);

}