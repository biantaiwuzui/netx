package com.netx.fuse.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netx.common.router.dto.bean.UserBeanResponseDto;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.NearlyGeoRequestDto;
import com.netx.common.router.dto.request.UserInfoRequestDto;
import com.netx.common.router.dto.select.SelectRedisResponseDto;
import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import com.netx.common.user.dto.common.CommonUserBaseInfoDto;
import com.netx.common.user.dto.user.ScreenUserRequestDto;
import com.netx.common.user.model.UserInfo;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.fuse.client.ucenter.UserClientAction;
import com.netx.ucenter.vo.router.SearchUserInfoRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserClientProxy {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    UserClientAction userClient;

    public UserClientAction getUserClient() {
        return userClient;
    }

    public List<String> getAllUserId(){
        try {
            return userClient.selectUserIds();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    public List<CommonUserBaseInfoDto> getUsers(List<String> userIds) {
        return userClient.selectUserBaseInfoByUserId(userIds);
    }

    public JSONObject getUserByMobile(String mobile) {
        UserBeanResponseDto result = userClient.selectUserByMobile(mobile);
        if (result != null) {
            JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(result));
            return jsonObject;
        }
        return null;
    }

    public UserInfoResponseDto selectUserInfo(UserInfoRequestDto userInfoRequestDto) {
        return userClient.selectUserInfo(userInfoRequestDto);
    }


    public List<CommonUserBaseInfoDto> selectUserBaseInfoByUserId(String userId){
        return userClient.selectUserBaseInfoByUserId(Arrays.asList(new String[]{userId}));
    }


    /**
     * 获取在线用户ids
     * @param userIds 在线用户ids，ids不空，进行ids筛选
     * @param time 默认1分钟前，-1为不限
     * @return
     */
    public List<String> screenOnlineUserIds(List<String> userIds,Long time){
        return userClient.screenOnlineUserIds(getScreenUserRequestDto(userIds,time));
    }

    /**
     * 获取在线用户集
     * @param userIds 在线用户ids，ids不空，进行ids筛选
     * @param time 默认1分钟前，-1为不限
     * @return
     */
    public List<UserSynopsisData> screenOnlineUser(List<String> userIds, Long time){
        return userClient.screenOnlineUser(getScreenUserRequestDto(userIds,time));
    }

    private ScreenUserRequestDto getScreenUserRequestDto(List<String> userIds, Long time){
        ScreenUserRequestDto dto = new ScreenUserRequestDto();
        dto.setUserIds(userIds);
        dto.setTime(time);
        return dto;
    }

    private NearlyGeoRequestDto createNearlyGeoRequestDto(Double lon, Double lat, Double redaius){
        NearlyGeoRequestDto dto = new NearlyGeoRequestDto();
        dto.setLat(lat);
        dto.setLon(lon);
        dto.setRedaius(redaius);
        return dto;
    }

    /**
     * 查找附近的登录ids
     * @param lon
     * @param lat
     * @param redaius
     * @return
     */
    public List<String> selectNearlyUserLoginIdsList(Double lon,Double lat,Double redaius){
        NearlyGeoRequestDto dto = createNearlyGeoRequestDto(lon,lat,redaius);
        return userClient.selectNearbyLoginIds(dto);
    }

    /**
     * 查找附近的登录Redis用户
     * @param lon
     * @param lat
     * @param redaius
     * @return
     */
    public List<SelectRedisResponseDto> selectNearbyLoginRedisUser(Double lon, Double lat, Double redaius){
        NearlyGeoRequestDto dto = createNearlyGeoRequestDto(lon,lat,redaius);
        return userClient.selectNearbyLoginRedisUser(dto);
    }

    /**
     * 查找附近的登录Redis用户(Map)
     * @param lon
     * @param lat
     * @param redaius
     * @return
     */
    public Map<String,SelectRedisResponseDto> selectNearbyLoginRedisUserMap(Double lon, Double lat, Double redaius){
        NearlyGeoRequestDto dto = createNearlyGeoRequestDto(lon,lat,redaius);
        return userClient.selectNearbyLoginRedisUserMap(dto);
    }

    /**
     * 按距离、在线时间、性别查找用户列表（list）
     * @param lon
     * @param lat
     * @param redaius
     * @param online 时间戳，-1不限
     * @param sex 性别：男 || 女
     * @return
     */
    public List<UserInfo> searchGetUserInfo(Double lon, Double lat, Double redaius, Long online, String sex){
        return userClient.searchGetUserInfo(createMapToUserInfo(redaius, online, sex),lon, lat);
    }

    private SearchUserInfoRequestDto createMapToUserInfo(Double redaius, Long online, String sex){
        SearchUserInfoRequestDto map = new SearchUserInfoRequestDto();
        map.setOnline(online);
        map.setSex(sex);
        map.setRedaius(redaius);
        return map;
    }

    /**
     * 按距离、在线时间、性别查找用户列表（Map）
     * @param lon
     * @param lat
     * @param redaius
     * @param online 时间戳，-1不限
     * @param sex 性别：男 || 女
     * @return
     */
    public Map<String,UserInfo> searchGetUserInfoMap(Double lon,Double lat,Double redaius,Long online,String sex){
        return userClient.searchGetUserInfoMap(createMapToUserInfo(redaius, online, sex),lon, lat);
    }

    /**
     * 获取用户的乐观锁
     * @param userId
     * @return
     */
    public Integer getUserLockVersion(String userId){
        UserInfoRequestDto userInfoRequestDto = new UserInfoRequestDto();
        userInfoRequestDto.setSelectData(userId);
        userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_ID);
        List<SelectFieldEnum> list = new ArrayList<>();
        list.add(SelectFieldEnum.LOCK_VERSION);
        userInfoRequestDto.setSelectFieldEnumList(list);
        UserInfoResponseDto userInfoResponseDto = this.selectUserInfo(userInfoRequestDto);
        Integer lockVersion = userInfoResponseDto.getLockVersion();
        return lockVersion;
    }

    public CommonUserBaseInfoDto getUser(String userId) {
        List<CommonUserBaseInfoDto> list = userClient.selectUserBaseInfoByUserId(Arrays.asList(new String[]{userId}));
        if (list != null) return list.get(0);
        return null;
    }

    /**
     * 根据用户id集获取用户基本数据
     * @param ids
     * @return
     */
    public Map<String,UserSynopsisData> selectUserMapByIds(List<String> ids){
        return userClient.selectUserMapByIds(ids);
    }
}
