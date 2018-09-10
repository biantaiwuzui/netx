package com.netx.ucenter.biz.router;

import com.netx.common.redis.model.UserGeoRadius;
import com.netx.common.redis.service.GeoService;
import com.netx.common.router.dto.select.SelectRedisResponseDto;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.searchengine.enums.FriendTypeEnum;
import com.netx.searchengine.model.UserSearchResponse;
import com.netx.searchengine.common.FriendType;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.Query;
import com.netx.searchengine.query.UserFriendSearchQuery;
import com.netx.searchengine.service.FriendSearchService;
import com.netx.ucenter.biz.user.*;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.LoginHistoryService;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class NearlyAction{
    @Autowired
    GeoService geoService;
    @Autowired
    UserAction userAction;
    @Autowired
    LoginHistoryService loginHistoryService;
    @Autowired
    FriendSearchService friendSearchService;
    @Autowired
    UserWatchAction userWatchAction;
    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    UserProfileAction userProfileAction;
    @Autowired
    UserEducationAction userEducationAction;
    @Autowired
    UserProfessionAction userProfessionAction;
    @Autowired
    UserInterestAction userInterestAction;

    private Logger logger = LoggerFactory.getLogger(NearlyAction.class);

    private List<String> getMapKeyString(Map map){
        List<String> ids = new ArrayList<>();
        for (Object key:map.keySet()){
            ids.add(key.toString());
        }
        return ids;
    }

    public List<String> querUserIds(String userId,Double lon,Double lat){
        List<String> ids = new ArrayList<>();
        int current = 1;
        int size = 99999;
        UserFriendSearchQuery userFriendSearchQuery = createUserFriendSearchQuery(userId,lon,lat,current,size,true);
        List<Map<String,Object>> friends = new ArrayList<>();
        while (true){
            friends = friendSearchService.queryFriendMap(userFriendSearchQuery);
            friends.forEach(map->{
                if(map.get("id") != null) {
                    ids.add(map.get("id").toString());
                }
            });
            if(friends.size()!=size){
                break;
            }
            userFriendSearchQuery.setPage(++current,size);
        }
        return ids;
    }

    public List<UserSynopsisData> queryFriendsByType(FriendTypeEnum friendTypeEnum,String userId,double lon,double lat,Integer current,Integer size){
        Query query = FriendType.getFriendType().getQueryByTypeEnum(friendTypeEnum);
        if(friendTypeEnum.getLogin()){
            if(updateQuery(query,friendTypeEnum,userId)){
                return new ArrayList<>();
            }
        }
        UserFriendSearchQuery userFriendSearchQuery = createUserFriendSearchQuery(userId, lon, lat, current, size,friendTypeEnum.getLogin());
        userFriendSearchQuery.createQueryMap(friendTypeEnum,query);
        List<UserSynopsisData> list = new ArrayList<>();
        List<UserSearchResponse> friends = friendSearchService.queryFriends(userFriendSearchQuery);
        if (friends.size()>0){
            friends.forEach(userSearchResponse -> {
                        list.add(SearchResponseToSynopsisData(userSearchResponse,userId));
                    }
            );
        }
        return list;
    }

    private Boolean updateQuery(Query query,FriendTypeEnum friendTypeEnum,String userId){
        User user = userAction.getUserService().selectById(userId);
        List<Object> list ;
        if(user!=null){
            switch (friendTypeEnum){
                case SCHOOL_TYPE:
                    list = userEducationAction.getUserEducationService().selectEducationOneDateByUserId(userId,"school");
                    return setValues(query,list);
                case COMPANY_TYPE:
                    list = userProfessionAction.getProfessionService().selectUserProfessionOneByUserId(userId,"company");
                    return setValues(query,list);
                case SAME_AGE_TYPE:
                    if(user.getBirthday()!=null){
                        query.setAge(user.getBirthday());
                        query.setSex(user.getSex());
                        return false;
                    }
                    return true;
                case SAME_HOMETOWN_TYPE:
                    String value = userProfileAction.getUserTag(userId,"home_town");
                    if(!StringUtils.isEmpty(value)){
                        query.addValue(value);
                        return false;
                    }
                    return true;
                case UNMARRIED_TYPE:
                    if(user.getSex()!=null){
                        query.setSex(user.getSex());
                        return false;
                    }
                    return true;
                case COMMON_HOBBY_TYPE:
                    list = userInterestAction.getUserInterestService().selectInterestOneByUserId(userId,"interest_detail");
                    return setValues(query,list);
            }
        }
        return true;
    }

    private Boolean setValues(Query query,List<Object> list){
        if(list!=null && list.size()>0){
            query.setValues(list);
            return false;
        }
        return true;
    }

    private UserSynopsisData SearchResponseToSynopsisData(UserSearchResponse response,String userId){
        UserSynopsisData userSynopsisData = VoPoConverter.copyProperties(response,UserSynopsisData.class);
        userSynopsisData.setWatch(userWatchAction.getIsWatch(userId,response.getId()));
        userSynopsisData.setHeadImgUrl(addImgUrlPreUtil.getUserImgPre(userSynopsisData.getHeadImgUrl()));
        userSynopsisData.setTag(response.getDisposition());
        return userSynopsisData;
    }

    private UserFriendSearchQuery createUserFriendSearchQuery(String userId,double lon,double lat,Integer current,Integer size,Boolean isLogin){
        UserFriendSearchQuery userFriendSearchQuery = new UserFriendSearchQuery();
        userFriendSearchQuery.setCenterGeoPoint(new GeoPoint(lat,lon));
        userFriendSearchQuery.setPage(current,size);
        if(isLogin){
            userFriendSearchQuery.setExcludeUserId(userId);
        }
        return userFriendSearchQuery;
    }

    private List<UserSynopsisData> queryFriends(UserFriendSearchQuery userFriendSearchQuery,String userId){
        List<UserSynopsisData> list = new ArrayList<>();
        List<UserSearchResponse> friends = friendSearchService.queryFriends(userFriendSearchQuery);
        if (friends.size()>0){
            friends.forEach(userSearchResponse -> {
                        list.add(SearchResponseToSynopsisData(userSearchResponse,userId));
                    }
            );
        }
        return list;
    }

    private Map<String,UserSynopsisData> queryFriendMaps(UserFriendSearchQuery userFriendSearchQuery,String userId){
        Map<String,UserSynopsisData> map = new HashMap<>();
        List<UserSearchResponse> friends = friendSearchService.queryFriends(userFriendSearchQuery);
        if (friends.size()>0){
            friends.forEach(userSearchResponse -> {
                        map.put(userSearchResponse.getId(),SearchResponseToSynopsisData(userSearchResponse,userId));
                    }
            );
        }
        return map;
    }

    public List<UserSynopsisData> selectNearlyLogin(String userId,double lon,double lat,Integer current,Integer size) throws Exception{
        UserFriendSearchQuery userFriendSearchQuery = createUserFriendSearchQuery(userId, lon, lat, current, size,true);
        return queryFriends(addNearly(userFriendSearchQuery),userId);
    }

    public List<UserSynopsisData> selectNearUserList(String userId,double lon,double lat,Integer current,Integer size) throws Exception{
        UserFriendSearchQuery userFriendSearchQuery = createUserFriendSearchQuery(userId, lon, lat, current, size,true);
        userFriendSearchQuery = addNearly(userFriendSearchQuery);
        userFriendSearchQuery.addLastAscQuery(new LastAscQuery("credit",false));
        return queryFriends(userFriendSearchQuery,userId);
    }

    public Map<String,UserSynopsisData> queryNearUserMaps(String userId,double lon,double lat,Integer current,Integer size){
        return queryFriendMaps(addNearly(createUserFriendSearchQuery(userId,lon,lat,current,size,true)),userId);
    }

    private UserFriendSearchQuery addNearly(UserFriendSearchQuery searchQuery){
        searchQuery.setNearly(true);
        searchQuery.addLastAscQuery(new LastAscQuery("activeAt",false));
        return searchQuery;
    }

    private UserSynopsisData createUserSynopsisData(User user,UserGeoRadius geo){
        return userAction.userAndRedisTo(user,geo.getRadius(),geo.getUserId());
    }

    public Map<String,UserSynopsisData> selectNearUserMap(String userId, double lon, double lat, double redaius) throws Exception{
        Map<String,UserGeoRadius> map = geoService.nearListExceptRediusMap(userId,lon,lat,redaius);
        Map<String,UserSynopsisData> dataMap = new HashMap<>();
        if(!map.isEmpty()){
            List<User> userList=userAction.getNearlyUserByIds(map.keySet());
            if(userList!=null && !userList.isEmpty()){
                for(User user:userList) {
                    String id = user.getId();
                    dataMap.put(id,createUserSynopsisData(user,map.get(id)));
                }
            }
        }
        return dataMap;
    }

    public List<String> selectNearlyUserLoginIdsList(Collection<?> ids) throws Exception{
        List<String> list = userAction.getNearlyByIds(ids);
        List<String> result = new ArrayList<>();
        if(list!=null && !list.isEmpty()){
            result=loginHistoryService.getUserId(list);
        }
        return result;
    }

    public List<SelectRedisResponseDto> selectNearbyLoginRedisUser(Double lon,Double lat,Double redaius) throws Exception{
        List<SelectRedisResponseDto> dtoList = new ArrayList<>();
        List<UserGeoRadius> list = geoService.nearListUserIdsRedius(lon,lat,redaius);
        list.forEach(userGeoRadius -> {
            if(loginHistoryService.getUserLoginHistoryMapper().selectUserNewLogin(userGeoRadius.getUserId())!=null){
                try {
                    User user = userAction.getNearlyUser(userGeoRadius.getUserId());
                    if(user!=null){
                        SelectRedisResponseDto temp = createSelectRedisResponseDto(user,userGeoRadius);
                        if(temp != null){
                            dtoList.add(temp);
                        }
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            }
        });
        return dtoList;
    }

    public Map<String,SelectRedisResponseDto> selectNearbyLoginRedisUserMap(Double lon,Double lat,Double redaius) throws Exception{
        Map<String,SelectRedisResponseDto> dtoMap = new HashMap<>();
        Map<String,UserGeoRadius> userGeoMap=geoService.nearListUserIdsRediusMap(lon,lat,redaius);
        System.out.println(userGeoMap.toString());
        if(!userGeoMap.isEmpty()){
            List<String> ids = loginHistoryService.getUserId(userGeoMap.keySet());
            if(ids!=null && !ids.isEmpty()){
                List<User> list = userAction.getNearlyUserByIds(ids);
                if(list!=null && !list.isEmpty()){
                    for(User user : list){
                        SelectRedisResponseDto temp = createSelectRedisResponseDto(user,userGeoMap.get(user.getId()));
                        if(temp != null){
                            dtoMap.put(user.getId(),temp);
                        }
                    }
                }
            }
        }
        return dtoMap;
    }

    private SelectRedisResponseDto createSelectRedisResponseDto(User user,UserGeoRadius userGeo){
        try {
            SelectRedisResponseDto dto = VoPoConverter.copyProperties(user,SelectRedisResponseDto.class);
            if(userGeo!=null){
                dto.setLon(userGeo.getLon());
                dto.setLat(userGeo.getLat());
                dto.setDistance(userGeo.getRadius());
            }
            return dto;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return null;
        }
    }
}