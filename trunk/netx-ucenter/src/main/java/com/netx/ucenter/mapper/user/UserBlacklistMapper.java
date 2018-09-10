package com.netx.ucenter.mapper.user;

import com.netx.ucenter.model.user.UserBlacklist;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 用户拉黑表（拉黑后对本人是黑名单，不影响其他人） Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface UserBlacklistMapper extends BaseMapper<UserBlacklist> {

}