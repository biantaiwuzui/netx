package com.netx.ucenter.mapper.user;

import com.netx.ucenter.model.user.UserVerifyCredit;
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
public interface UserVerifyCreditMapper extends BaseMapper<UserVerifyCredit> {
    Boolean delectUserId(@Param("userId") String userId, @Param("credit") Integer credit) throws Exception;
}