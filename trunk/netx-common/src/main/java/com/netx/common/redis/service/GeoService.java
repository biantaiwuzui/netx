package com.netx.common.redis.service;

import com.netx.common.redis.model.UserGeo;
import com.netx.common.redis.model.UserGeoRadius;

import java.util.List;
import java.util.Map;

public interface GeoService {
    public final String USER_GEO_KEY = "UserGeo";
    
    /**
     * 添加/更新 坐标
     *
     * @param userId
     * @param lon
     * @param lat
     * @return
     */
    public boolean addOrUpdate(String userId, double lon, double lat);

    /**
     * 取用户实时经纬度
     *
     * @param userId
     * @return
     */
    public UserGeo getUserGeo(String userId);

    /**
     * 找我附近的人
     *
     * @param userId
     * @param radius 距离范围，单位为KM
     * @return List数据
     */
    public List<UserGeo> nearList(String userId, double radius) throws Exception;

    /**
     * 找坐标附近的人
     *
     * @param lon
     * @param lat
     * @param radius 距离范围，单位为KM
     * @return List数据
     */
    public List<UserGeo> nearList(double lon, double lat, double radius) throws Exception;

    /**
     * 删除这个用户的位置
     * @param userId
     */
    public void deleteUserGeo(String userId) throws Exception;

    /**
     * 找坐标附近的人，除去userId
     * @param userId
     * @param lon
     * @param lat
     * @param radius 距离范围，单位为KM
     * @return List数据
     */
    public List<UserGeo> nearListExcept(String userId,double lon, double lat, double radius) throws Exception;

    /**
     * 找坐标附近的人，除去用户id集里的用户id
     * @param ids
     * @param lon
     * @param lat
     * @param radius 距离范围，单位为KM
     * @return List数据
     */
    public List<UserGeo> nearListExcept(List<String> ids,double lon, double lat, double radius) throws Exception;

    /**
     * 找坐标附近的人，除去userId
     * @param userId
     * @param lon
     * @param lat
     * @param radius 距离范围，单位为KM
     * @return map数据
     */
    public Map<String,UserGeo> nearListExceptMaps(String userId, double lon, double lat, double radius) throws Exception;

    /**
     * 找坐标附近的人，除去用户id集里的用户id
     * @param ids
     * @param lon
     * @param lat
     * @param radius 距离范围，单位为KM
     * @return map数据
     */
    public Map<String,UserGeo> nearListExceptMaps(List<String> ids,double lon, double lat, double radius) throws Exception;

    /**
     * 找坐标附近的用户id
     * @param lon
     * @param lat
     * @param radius
     * @return
     */
    public List<String> nearListUserIds(double lon, double lat, double radius) throws Exception;

    /**
     * 查找附近用户（含距离）
     * @param lon
     * @param lat
     * @param radius
     * @return
     */
    List<UserGeoRadius> nearListUserIdsRedius(double lon, double lat, double radius) throws Exception;

    /**
     * 查找附近用户map（含距离）
     * @param lon
     * @param lat
     * @param radius
     * @return
     */
    Map<String,UserGeoRadius> nearListUserIdsRediusMap(double lon, double lat, double radius) throws Exception;

    /**
     * 取出传人的userId,查找附近用户（含距离）
     * @param userId
     * @param lon
     * @param lat
     * @param radius
     * @return
     */
    List<UserGeoRadius> nearListExceptRedius(String userId,double lon, double lat, double radius) throws Exception;

    /**
     * 取出传人的userId,查找附近用户（含距离）
     * @param userId
     * @param lon
     * @param lat
     * @param radius
     * @return
     */
    Map<String,UserGeoRadius> nearListExceptRediusMap(String userId,double lon, double lat, double radius) throws Exception;
}
