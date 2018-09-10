package com.netx.ucenter.mapper.common;

import com.netx.ucenter.model.common.CommonGroup;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-04-13
 */
public interface CommonGroupMapper extends BaseMapper<CommonGroup> {
    public List<CommonGroup> queryGroupsByUserId(@Param("userId") String userId,@Param("admin") Integer admin);
}