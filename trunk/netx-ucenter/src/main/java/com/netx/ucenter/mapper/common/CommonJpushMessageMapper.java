package com.netx.ucenter.mapper.common;

import com.netx.ucenter.model.common.CommonJpushMessage;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface CommonJpushMessageMapper extends BaseMapper<CommonJpushMessage> {

    Boolean updateStateByUserId(@Param("userId") String userId,@Param("state") Integer state) throws Exception;

    void deleteByUserId(@Param("userId") String userId);
}