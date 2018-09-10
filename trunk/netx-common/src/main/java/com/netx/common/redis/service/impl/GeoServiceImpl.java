package com.netx.common.redis.service.impl;

import com.netx.common.redis.model.UserGeo;
import com.netx.common.redis.model.UserGeoRadius;
import com.netx.common.redis.service.GeoService;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.geo.*;

import java.math.BigDecimal;
import java.util.*;

@Service
public class GeoServiceImpl implements GeoService {

    @Autowired
    private RedisTemplate objRedisTemplate;

    private Logger logger = LoggerFactory.getLogger(GeoServiceImpl.class);

    @Override
    public boolean addOrUpdate(String userId, double lon, double lat){
        try {
            Point point = new Point(retainTwo(lon,6), retainTwo(lat,6));
            objRedisTemplate.opsForGeo().geoAdd(USER_GEO_KEY, point, userId);
            logger.info("本次位置更新的用户Id为："+userId+"其经纬度为：(lat="+lat+",lon="+lon+")");
            return true;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }

    @Override
    public UserGeo getUserGeo(String userId){
        UserGeo userGeo = new UserGeo();
        try {
            List<Point> points = objRedisTemplate.opsForGeo().geoPos(USER_GEO_KEY, userId);
            if (!(points == null || points.isEmpty() || points.get(0) == null)) {
                points.forEach(point -> {
                    userGeo.setUserId(userId);
                    userGeo.setLon(retainTwo(point.getX(), 6));
                    userGeo.setLat(retainTwo(point.getY(), 6));
                });
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return userGeo;
    }

    @Override
    public List<UserGeo> nearList(String userId, double radius) throws Exception{
        Distance distance = new Distance(radius, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().sortAscending().includeCoordinates();
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = objRedisTemplate.opsForGeo().geoRadiusByMember(USER_GEO_KEY, userId, distance, geoRadiusCommandArgs);
        List<UserGeo> list = getUserGeoList(geoResults);
        return list;
    }

    private double retainTwo(double num,Integer dio) {
        BigDecimal bg = new BigDecimal(num);
        return bg.setScale(dio, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public List<UserGeo> nearList(double lon, double lat, double radius) throws Exception{
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = getResults(lon, lat, radius);
        List<UserGeo> list = getUserGeoList(geoResults);
        return list;
    }

    private List<UserGeo> getUserGeoList(GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults){
        List<UserGeo> list = new ArrayList<>();
        geoResults.forEach(result -> {
            list.add(createUserGeo(result.getContent().getName(),result.getContent().getPoint()));
        });
        return list;
    }

    private UserGeo createUserGeo(String userId,Point point){
        UserGeo userGeo = new UserGeo();
        userGeo.setUserId(userId);
        userGeo.setLon(point.getX());
        userGeo.setLat(point.getY());
        return userGeo;
    }

    private GeoResults<RedisGeoCommands.GeoLocation<String>> getResults(double lon, double lat, double radius) throws Exception{
        BoundGeoOperations boundGeoOps = objRedisTemplate.boundGeoOps(USER_GEO_KEY);
        Circle circle = new Circle(new Point(lon, lat),new Distance(radius, Metrics.KILOMETERS));
        //设置geo查询参数
        RedisGeoCommands.GeoRadiusCommandArgs geoRadiusArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        //查询返回结果包括距离和坐标
        geoRadiusArgs = geoRadiusArgs.includeCoordinates().includeDistance();
        geoRadiusArgs.sortAscending();
        return boundGeoOps.geoRadius(circle,geoRadiusArgs);
    }

    @Override
    public void deleteUserGeo(String userId) throws Exception{
        objRedisTemplate.opsForZSet().remove(USER_GEO_KEY,userId);
    }

    @Override
    public List<UserGeo> nearListExcept(String userId,double lon, double lat, double radius) throws Exception{
        return getUserGeoList(userId,getResults(lon,lat,radius));
    }

    private List<UserGeo> getUserGeoList(String userId,GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults) {
        if(userId==null || userId.trim().equals("")) return getUserGeoList(geoResults);
        List<UserGeo> list = new ArrayList<>();
        geoResults.forEach(result -> {
            UserGeo userGeo = new UserGeo();
            String id = result.getContent().getName();
            if(!id.equals(userId)){
                list.add(createUserGeo(id,result.getContent().getPoint()));
            }
        });
        return list;
    }

    private List<UserGeo> getUserGeoList(List<String> ids,GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults){
        if(ids==null || ids.isEmpty()) return getUserGeoList(geoResults);
        List<UserGeo> list = new ArrayList<>();
        geoResults.forEach(result -> {
            String userId = result.getContent().getName();
            for(String id : ids){
                if(!id.equals(userId)){
                    list.add(createUserGeo(id,result.getContent().getPoint()));
                    ids.remove(id);
                    break;
                }
            }
        });
        return list;
    }

    @Override
    public List<UserGeo> nearListExcept(List<String> ids,double lon, double lat, double radius) throws Exception{
        return getUserGeoList(ids,getResults(lon,lat,radius));
    }

    @Override
    public Map<String,UserGeo> nearListExceptMaps(String userId, double lon, double lat, double radius) throws Exception{
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = getResults(lon,lat,radius);
        if(userId==null || userId.trim().equals("")) return getUserGeoMap(geoResults);
        Map<String,UserGeo> map = new HashMap<>();
        geoResults.forEach(result -> {
            String id = result.getContent().getName();
            if(!id.equals(userId)){
                map.put(id,createUserGeo(id,result.getContent().getPoint()));
            }
        });
        return map;
    }

    private Map<String,UserGeo> getUserGeoMap(GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults){
        Map<String,UserGeo> map = new HashMap<>();
        geoResults.forEach(result -> {
            String id = result.getContent().getName();
            map.put(id,createUserGeo(id,result.getContent().getPoint()));
        });
        return map;
    }

    @Override
    public Map<String,UserGeo> nearListExceptMaps(List<String> ids,double lon, double lat, double radius) throws Exception{
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = getResults(lon,lat,radius);
        Map<String,UserGeo> map = getUserGeoMap(geoResults);
        if(ids!=null && !ids.isEmpty()){
            for(String userId:ids){
                map.remove(userId);
            }
        }
        return map;
    }

    @Override
    public List<String> nearListUserIds(double lon, double lat, double radius) throws Exception{
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = getResults(lon,lat,radius);
        List<String> ids = new ArrayList<>();
        geoResults.forEach(result -> {
            ids.add(result.getContent().getName());
            System.out.println(result.getContent().getPoint());
            Point point = result.getContent().getPoint();
            System.out.println(getDistance(lon,lat,point.getX(),point.getY()));
        });
        return ids;
    }

    private double getDistance(double lon,double lat,double x,double y){
        SpatialContext geo = SpatialContext.GEO;
        double distance = geo.calcDistance(geo.getShapeFactory().pointXY(lon, lat), geo.getShapeFactory().pointXY(x, y)) * DistanceUtils.DEG_TO_KM;
        return distance;
    }

    @Override
    public List<UserGeoRadius> nearListUserIdsRedius(double lon, double lat, double radius) throws Exception{
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = getResults(lon,lat,radius);
        List<UserGeoRadius> list = new ArrayList<>();
        geoResults.forEach(result->{
            if(result.getContent().getPoint()!=null){
                list.add(createUserGeoRadius(result.getContent().getName(),result.getContent().getPoint(),result.getDistance().getValue()));
                logger.info("请求经纬度为：(lat="+lat+",lon="+lon+")，与用户id："+result.getContent().getName()+"的经纬度：lat="+result.getContent().getPoint().getY()+",lon="+result.getContent().getPoint().getX()+")的距离为："+result.getDistance().getValue());
            }
        });
        return list;
    }

    @Override
    public List<UserGeoRadius> nearListExceptRedius(String userId,double lon, double lat, double radius) throws Exception{
        if(userId==null || userId.isEmpty()) return nearListUserIdsRedius(lon, lat, radius);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = getResults(lon,lat,radius);
        List<UserGeoRadius> list = new ArrayList<>();
        geoResults.forEach(result->{
            if(result.getContent().getPoint()!=null){
                list.add(createUserGeoRadius(result.getContent().getName(),result.getContent().getPoint(),result.getDistance().getValue()));
                logger.info("请求经纬度为：(lat="+lat+",lon="+lon+")，与用户id："+result.getContent().getName()+"的经纬度：lat="+result.getContent().getPoint().getY()+",lon="+result.getContent().getPoint().getX()+")的距离为："+result.getDistance().getValue());
            }
        });
        return new ArrayList<>();
    }

    private UserGeoRadius createUserGeoRadius(String userId,Point point,double distance){
        UserGeoRadius geoRadius = new UserGeoRadius();
        geoRadius.setUserId(userId);
        geoRadius.setLon(point.getX());
        geoRadius.setLat(point.getY());
        geoRadius.setRadius(retainTwo(distance,2));
        return geoRadius;
    }

    @Override
    public Map<String,UserGeoRadius> nearListUserIdsRediusMap(double lon, double lat, double radius) throws Exception{
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = getResults(lon,lat,radius);
        Map<String,UserGeoRadius> map = new HashMap<>();
        geoResults.forEach(result->{
            if(result.getContent().getPoint()!=null){
                map.put(result.getContent().getName(),createUserGeoRadius(result.getContent().getName(),result.getContent().getPoint(),result.getDistance().getValue()));
                logger.info("请求经纬度为：(lat="+lat+",lon="+lon+")，与用户id："+result.getContent().getName()+"的经纬度：lat="+result.getContent().getPoint().getY()+",lon="+result.getContent().getPoint().getX()+")的距离为："+result.getDistance().getValue());
            }
        });
        return map;
    }

    @Override
    public Map<String,UserGeoRadius> nearListExceptRediusMap(String userId,double lon, double lat, double radius) throws Exception{
        if(userId==null || userId.isEmpty()) return nearListUserIdsRediusMap(lon, lat, radius);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = getResults(lon,lat,radius);
        Map<String,UserGeoRadius> map = new HashMap<>();
        geoResults.forEach(result->{
            String id = result.getContent().getName();
            if(result.getContent().getPoint()!=null && !userId.equals(id)){
                map.put(id,createUserGeoRadius(id,result.getContent().getPoint(),result.getDistance().getValue()));
                logger.info("用户id："+userId+"的经纬度为：(lat="+lat+",lon="+lon+")，与用户id："+id+"的经纬度：lat="+result.getContent().getPoint().getY()+",lon="+result.getContent().getPoint().getX()+")的距离为："+result.getDistance().getValue());
            }
        });
        return map;
    }
}
