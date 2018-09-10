package com.netx.ucenter.mapper.user;

import com.netx.common.user.model.StatData;
import com.netx.ucenter.model.user.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.netx.ucenter.vo.response.UserStatData;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * <p>
  * 用户表 Mapper 接口
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
public interface UserMapper extends BaseMapper<User> {

    public User queryUserFriendsByUserNumber(@Param("userId") String userId, @Param("userNumber") String userNumber);

    public List<User> queryUserFriendsByUserId(@Param("userId") String userId,@Param("current") Integer current,@Param("size") Integer size);

    /*网信 - 信用排行**/
    public int getCreditRankByUserId(String userId);

    public int selectNums();

    /**
     * 清理日常字段
     * @return
     * @throws Exception
     */
    boolean updateDayNum();

    /**
     *查询用户部分信息+url
     * 大赛排行专用
     * @return
     */
    public List<UserStatData> getUserStatData();

    /**
     *查询有效建议建议分数
     * 1.2.
     * 大赛排行专用
     * @return
     */
    public List<StatData> getSuggestStat();

}