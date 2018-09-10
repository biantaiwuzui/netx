package com.netx.ucenter.mapper.friend;

import com.netx.common.vo.common.StatDataDto;
import com.netx.ucenter.model.friend.CommonFriends;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface CommonFriendsMapper extends BaseMapper<CommonFriends> {
    List<String> getUserIdsByMasterId(String masterId);

    Integer checkFriend(@Param("userId") String userId, @Param("toUserId")  String toUserId);

    List<StatDataDto> selectStatData();
}