package com.netx.fuse.client.ucenter;

import com.netx.common.redis.service.GeoService;
import com.netx.common.router.dto.bean.UserBeanResponseDto;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.bean.UserPhotosResponseDto;
import com.netx.common.router.dto.request.NearlyGeoRequestDto;
import com.netx.common.router.dto.request.UserInfoListRequestDto;
import com.netx.common.router.dto.request.UserInfoRequestDto;
import com.netx.common.router.dto.select.SelectRedisResponseDto;
import com.netx.common.user.dto.common.CommonUserBaseInfoDto;
import com.netx.common.user.dto.user.ScreenUserRequestDto;
import com.netx.common.user.enums.RegularExpressionEnum;
import com.netx.common.user.model.UserInfo;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.biz.router.NearlyAction;
import com.netx.ucenter.biz.router.RouterAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserPhotoAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.vo.router.SearchUserInfoRequestDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserClientAction {

    private Logger logger = LoggerFactory.getLogger(UserClientAction.class);

    @Autowired
    UserAction userAction;

    @Autowired
    UserPhotoAction userPhotoAction;

    @Autowired
    RouterAction routerAction;

    @Autowired
    NearlyAction nearlyAction;

    @Autowired
    GeoService geoService;

    public List<String> selectUserIds(){
        return userAction.getAllUserId();
    }

    public List<CommonUserBaseInfoDto> selectUserBaseInfoByUserId(List<String> userIds){
        try{
            return userAction.selectUserBaseInfoByUserId(userIds);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }
    
    public UserBeanResponseDto selectUserByMobile(String mobile){
        if (!StringUtils.isEmpty(mobile) && !mobile.matches(RegularExpressionEnum.MOBILE.getValue())) {
            throw new RuntimeException("非法手机号码");
        }
        try {
            User user = userAction.selectUserByMobile(mobile,null);
            if(user!=null) {
                return userToUserBeanResponseDto(user);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    private UserBeanResponseDto userToUserBeanResponseDto(User user){
        UserBeanResponseDto userBeanResponseDto = VoPoConverter.copyProperties(user, UserBeanResponseDto.class);
        userBeanResponseDto.setIsAdminUser(user.getAdminUser());
        try {

            UserPhotosResponseDto photosResponseDto = userPhotoAction.selectUserPhotos(user.getId());
            if(photosResponseDto!=null){
                userBeanResponseDto.setHeadImgUrl(photosResponseDto.getHeadImgUrl());
                userBeanResponseDto.setImgUrls(photosResponseDto.getImgUrls());
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        userBeanResponseDto.setIsPWD(StringUtils.isNotBlank(user.getPassword()));
        userBeanResponseDto.setIsPayPWD(StringUtils.isNotBlank(user.getPayPassword()));
        userBeanResponseDto.setIsAdminPWD(StringUtils.isNotBlank(user.getAdminPassword()));
        return userBeanResponseDto;
    }

    public UserInfoResponseDto selectUserInfo(UserInfoRequestDto userInfoRequestDto){
        try{
            UserInfoResponseDto userInfoResponseDto = routerAction.selectUserInfo(userInfoRequestDto.getSelectData(), userInfoRequestDto.getSelectConditionEnum(), userInfoRequestDto.getSelectFieldEnumList());
            return userInfoResponseDto;
        }catch(Exception e){
            logger.error("出现异常,请检查入参的数据是否存在或是否合法:"+e.getMessage(), e);
        }
        return new UserInfoResponseDto();
    }

    public List<UserInfoResponseDto>  selectUserInfoList(UserInfoListRequestDto userInfoListRequestDto){
        try{
            List<UserInfoResponseDto> list = routerAction.selectUserInfoList(userInfoListRequestDto.getSelectDataList(), userInfoListRequestDto.getSelectConditionEnum(), userInfoListRequestDto.getSelectFieldEnumList());
            return list;
        }catch(Exception e){
            logger.error("出现异常,请检查入参的数据是否存在或是否合法，可能数据列表含有不存在的数据:"+e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据用户id集获取用户基本数据
     * @param ids
     * @return
     */
    public Map<String,UserSynopsisData> selectUserMapByIds(List<String> ids){
        try {
            if(ids.size()>0){
                return userAction.getUserSynopsisDataMap(ids,null,null,null);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new HashMap<>();
    }

    /**
     * 在线用户ids
     * @param dto
     * @return
     */
    public List<String> screenOnlineUserIds(ScreenUserRequestDto dto){
        try {
            return userAction.getOnlineUserIds(dto.getUserIds(), getOnlineTime(dto.getTime()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    private long getOnlineTime(Long time){
        if(time==null){
            time=1l;
        }else if(time==-1){
            return time;
        }
        return System.currentTimeMillis()-time*60*1000l;
    }

    /**
     * 在线用户信息
     * @param dto
     * @return
     */
    public List<UserSynopsisData> screenOnlineUser(ScreenUserRequestDto dto){
        try {
            return userAction.getOnlineUser(dto.getUserIds(), getOnlineTime(dto.getTime()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    /**
     * 查找附近的登录Redis用户(Map)
     * @param dto
     * @return
     */
    public Map<String,SelectRedisResponseDto> selectNearbyLoginRedisUserMap(NearlyGeoRequestDto dto){
        try {
            return nearlyAction.selectNearbyLoginRedisUserMap(dto.getLon(),dto.getLat(),dto.getRedaius());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new HashMap<>();
    }

    /**
     * 查找附近的登录Redis用户
     * @param dto
     * @return
     */
    public List<SelectRedisResponseDto> selectNearbyLoginRedisUser(NearlyGeoRequestDto dto){
        try {
            return nearlyAction.selectNearbyLoginRedisUser(dto.getLon(),dto.getLat(),dto.getRedaius());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    /**
     * 查找附近的登录ids
     * @param dto
     * @return
     */
    public List<String> selectNearbyLoginIds(NearlyGeoRequestDto dto){
        try {
            List<String> ids = geoService.nearListUserIds(dto.getLon(),dto.getLat(),dto.getRedaius());
            if(ids.size()>0){
                return nearlyAction.selectNearlyUserLoginIdsList(ids);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    public List<UserInfo> searchGetUserInfo(SearchUserInfoRequestDto dto,Double lon,Double lat){
        try{
            checkGeo(dto.getRedaius(),lon,lat);
            return routerAction.searchGetUserInfo(lon,lat,dto.getRedaius(),dto.getOnline(),dto.getSex());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new ArrayList<>();
    }

    private void checkGeo(Double redaius,Double lon,Double lat) throws Exception{
        if(redaius!=null){
            if (lon > 180 || lon < -180) throw new RuntimeException("输入的经度不合法");
            if (lat > 85 || lat < -85) throw new RuntimeException("输入的纬度不合法");
        }
    }

    public Map<String,UserInfo> searchGetUserInfoMap(SearchUserInfoRequestDto dto,Double lon,Double lat){
        try{
            checkGeo(dto.getRedaius(),lon,lat);
            return routerAction.searchGetUserInfoMap(lon,lat,dto.getRedaius(),dto.getOnline(),dto.getSex());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new HashMap<>();
    }

}
