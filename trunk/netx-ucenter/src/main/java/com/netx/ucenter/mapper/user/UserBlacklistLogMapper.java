package com.netx.ucenter.mapper.user;

import com.netx.ucenter.model.user.UserBlacklistLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 黑名单操作日志流水表(关联主表，取最新的一条，即为拉黑或释放理由) Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface UserBlacklistLogMapper extends BaseMapper<UserBlacklistLog> {

}