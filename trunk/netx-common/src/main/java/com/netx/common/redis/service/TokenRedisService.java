package com.netx.common.redis.service;


import com.netx.common.redis.model.TokenModel;
import com.netx.common.user.model.RedisUser;
import java.util.List;

/**
 * 对 token 进行操作的接口
 * @author 黎子安
 * @date 2017/10/21.
 */
public interface TokenRedisService {

    long TOKEN_EXPIRES_HOUR = 1000L * 60 * 60 * 24;
    /**
     * 创建一个 token 关联上指定用户
     * @param redisUser 用户信息
     * @return 生成的 token
     */
    public String createToken(RedisUser redisUser) throws Exception;

    /**
     * 修改redis里的用户信息
     * @param token
     * @param redisUser
     * @return
     */
    public void updateRedisUser(String token,RedisUser redisUser) throws Exception;

    /**
     * 检查 token 是否有效
     * @param model token
     * @return 是否有效
     */
    public boolean checkToken(TokenModel model) throws Exception;

    /**
     * 根据token获取redisUser
     * @param token 加密后的字符串
     * @return
     */
    public RedisUser getRedisUser(String token) throws Exception;

    /**
     * 根据tokenList获取redisUser列表
     * @param tokens
     * @return
     */
    public List<RedisUser> getRedisUser(List<String> tokens) throws Exception;

    /**
     * 清除 token
     * @param token 登录用户的 token
     */
    public void deleteToken(String token) throws Exception;

}
