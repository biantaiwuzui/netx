package com.netx.ucenter.mapper.user;

import com.netx.ucenter.model.user.UserLoginHistory;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * <p>
  * 登录记录表 Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface UserLoginHistoryMapper extends BaseMapper<UserLoginHistory> {
	public List<UserLoginHistory> selectUserNewLoginList(Collection<?> ids);

    public UserLoginHistory selectUserNewLogin(@Param("userId") String userId);
}