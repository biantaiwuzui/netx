package com.netx.utils;


import ch.hsr.geohash.GeoHash;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.io.GeohashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * author: 逍遥游
 */

public class DistrictUtil {
    private static Logger logger = LoggerFactory.getLogger(DistrictUtil.class);

    /**
     * @param lat
     * @param lon
     * @param length 比如length为5，代表搜索附近5公里
     *               1	5,009.4km	4,992.6km
     *               2	1,252.3km	624.1km
     *               3	156.5km	156km
     *               4	39.1km	19.5km
     *               5	4.9km	4.9km
     *               6	1.2km	609.4m
     *               7	152.9m	152.4m
     *               8	38.2m	19m
     *               9	4.8m	4.8m
     *               10	1.2m	59.5cm
     *               11	14.9cm	14.9cm
     *               12	3.7cm	1.9cm
     * @return
     */
    public static List<String> getHashAdjcent(BigDecimal lat, BigDecimal lon, int length) {
        List<String> list = new ArrayList<>();
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat.doubleValue(), lon.doubleValue(), length);//取九宫格
        list.add(geoHash.toBase32());//当前的
        GeoHash[] adjacent = geoHash.getAdjacent();//剩下的8个
        for (GeoHash hash : adjacent) {
            list.add(hash.toBase32());
        }
        return list;
    }


    /**
     * 计算2个点之间的距离
     *
     * @param centerLat 中心点纬度
     * @param centerLon 中心点经度
     * @param lat       目标纬度
     * @param lon       目标经度
     * @return
     */
    public static double calcDistance(BigDecimal centerLat, BigDecimal centerLon, BigDecimal lat, BigDecimal lon) {
        return calcDistance(centerLat.doubleValue(),centerLon.doubleValue(), lat.doubleValue(),lon.doubleValue());
    }

    /**
     * 计算2个点之间的距离
     * @param centerLat
     * @param centerLon
     * @param lat
     * @param lon
     * @return
     */
    public static Double calcDistance(Double centerLat, Double centerLon, Double lat, Double lon) {
        if(centerLat==null || centerLon==null || lat==null || lon==null){
            return null;
        }
        SpatialContext geo = SpatialContext.GEO;
        return retainTwo(geo.calcDistance(geo.getShapeFactory().pointXY(centerLon, centerLat), geo.getShapeFactory().pointXY(lon, lat)) * DistanceUtils.DEG_TO_KM);
    }

    private static double retainTwo(double num) {
        BigDecimal bg = new BigDecimal(num);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将经纬度编码为geohash存储的mysql中
     *
     * @param lat
     * @param lon
     * @return
     */
    public static String encodeLatLon(BigDecimal lat, BigDecimal lon) {
        return GeohashUtils.encodeLatLon(lat.doubleValue(), lon.doubleValue());
    }

    public static String encodeLatLon(Double lat, Double lon) {
        return GeohashUtils.encodeLatLon(lat, lon);
    }
}
