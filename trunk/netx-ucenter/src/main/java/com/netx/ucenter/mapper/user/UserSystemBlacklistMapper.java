package com.netx.ucenter.mapper.user;

import com.netx.ucenter.model.user.UserSystemBlacklist;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 系统黑名单，拉黑后即锁定用户，类似封禁的功能 Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface UserSystemBlacklistMapper extends BaseMapper<UserSystemBlacklist> {

}