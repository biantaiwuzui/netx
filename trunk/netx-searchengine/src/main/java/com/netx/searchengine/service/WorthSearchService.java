package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.NetxWorthSearchService;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.WorthSearchResponse;
import com.netx.searchengine.query.WorthSearchQuery;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorthSearchService extends NetxWorthSearchService {
    private Logger logger = LoggerFactory.getLogger(WorthSearchService.class);


    public List<WorthSearchResponse> queryWorth(WorthSearchQuery worthSearchQuery,String key){
        if(getClient()==null) {
            return new ArrayList<>();
        }
        level =0;
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_WORTH_NAME).setTypes(SearchField.TYPE_WORTH_NAME);
        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
        //嵌套查询
        //精确查询存在某一用户信息的文档
        if(StringUtils.isNotBlank(worthSearchQuery.getUserId())){
            queryBuilder.must(QueryBuilders.matchQuery(SearchField.QUERY_FIELD_ARTICLE_USERID,worthSearchQuery.getUserId()));
        }
        //根据标题名模糊查询信息
        if(StringUtils.isNotBlank(worthSearchQuery.getTitle())){
            queryBuilder.must(QueryBuilders.wildcardQuery("title", "*"+worthSearchQuery.getTitle()+"*"));
        }
        //根据网能类型精确查询
        Map queryMap=new HashMap<String ,List<Integer>>();
        queryMap.put("Meeting",new ArrayList<Integer>(){{
            add(0);
            add(5);
            add(1);
            add(4);
        }});
        queryMap.put("Skill",new ArrayList<Integer>(){{
            add(1);
            add(3);
        }});
        queryMap.put("Demand",new ArrayList<Integer>(){{
            add(1);
            add(3);
        }});
        queryMap.put("Wish",new ArrayList<Integer>(){{
            add(4);
            add(6);
            add(7);
        }});
        queryMap.put("Match",new ArrayList<Integer>(){{
            add(3);
            add(4);
            add(5);
            add(6);
            add(7);
            add(8);
        }});
        //当不符合以下条件时，按标题搜索
        //标题为空，类型不为空
        if (StringUtils.isNotBlank(worthSearchQuery.getType())&&!StringUtils.isNotBlank(worthSearchQuery.getTitle())){
            queryBuilder.must(QueryBuilders.matchQuery("worthType",worthSearchQuery.getType()));
            queryBuilder.must(QueryBuilders.termsQuery("status",(ArrayList)queryMap.get(worthSearchQuery.getType())));

        //标题不为空，类型不为空
        }else if(StringUtils.isNotBlank(worthSearchQuery.getType())&&StringUtils.isNotBlank(worthSearchQuery.getTitle())){
            queryBuilder.must(QueryBuilders.matchQuery("worthType",worthSearchQuery.getType()));
        //标题为空，类型为空----首页
        }else if (!StringUtils.isNotBlank(worthSearchQuery.getType())&&!StringUtils.isNotBlank(worthSearchQuery.getTitle())){
            Map queryIndexMap=new HashMap<String ,List<Integer>>();
            queryIndexMap.put("Index",new ArrayList<Integer>(){{
                add(0);
                add(5);
                add(1);
                add(4);
                add(6);
                add(7);
            }});
            queryBuilder.must(QueryBuilders.termsQuery("status",(ArrayList)queryIndexMap.get("Index")));
        }
        //添加排序规则
        //添加距离之前排序条件
        if(worthSearchQuery.getFristAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:worthSearchQuery.getFristAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    level++;
                    //把之前设置的需要排序的字段名全部添加，并按指定规则排序
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }
        if (!key.equals("publishTime")) {
            //距离筛选查询
            if (worthSearchQuery.getDistanceUnit() != null && worthSearchQuery.getCenterGeoPoint() != null) {
                double lat = worthSearchQuery.getCenterGeoPoint().getLat();
                double lon = worthSearchQuery.getCenterGeoPoint().getLon();
                double distance = worthSearchQuery.getMaxDistance();
                //用户当前的位置，设置筛选的距离
                GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_ARTICLE_LOCATION)
                        .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);


                searchBuilder.setPostFilter(distanceQueryBuilder);
                // 获取距离多少公里 这个才是获取点与点之间的距离
                //设置当前用户的数值
                GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_ARTICLE_LOCATION, lat, lon);
                //设置差值
                sort.unit(worthSearchQuery.getDistanceUnit());
                //设置排序
                sort.order(SortOrder.ASC);
                //创建当前用户的数值
                sort.point(lat, lon);
                //添加筛选条件
                searchBuilder.addSort(sort);
            }

            //添加距离之后排序条件
            if (worthSearchQuery.getLastAscQueries().size() > 0) {
                for (LastAscQuery lastAscQuery : worthSearchQuery.getLastAscQueries()) {
                    if (lastAscQuery.getAsc() != null) {
                        searchBuilder.addSort(lastAscQuery.getKey(), lastAscQuery.getAsc() ? SortOrder.ASC : SortOrder.DESC);
                    }
                }
            }
        }
        //查询建立
        SearchResponse myresponse = searchBuilder.setQuery(queryBuilder)
                .setFrom(worthSearchQuery.getFrom())//分页
                .setSize(worthSearchQuery.getPageSize()) //分页
                .setExplain(true)//显示计算规则
                .execute()
                .actionGet();
        logger.info(searchBuilder.toString());


        if (worthSearchQuery.getCenterGeoPoint() != null) {
            return processResponse(myresponse, worthSearchQuery.getCenterGeoPoint());
        }
        return processResponse(myresponse);
    }

    private List<WorthSearchResponse> processResponse(SearchResponse myresponse, GeoPoint centerGeoPoint) {
            List<WorthSearchResponse> responses=new ArrayList<>();
            WorthSearchResponse obj=null;
            Map<String,Object> resultMap=null;
            for(SearchHit searchHit:myresponse.getHits()){
                resultMap=searchHit.getSourceAsMap();
                obj=new WorthSearchResponse();
                if(resultMap.get("location")!=null){
                    if(StringUtils.isNotBlank(resultMap.get("location").toString())){
                        String[] geo = resultMap.get("location").toString().split(",");
                        if(geo.length==2){
                            BigDecimal lon = new BigDecimal(geo[1]);
                            BigDecimal lat = new BigDecimal(geo[0]);
                            if(checkLocationStr(lon) && checkLocationStr(lat)){
                                obj.setLon(lon);
                                obj.setLat(lat);
                                obj.setDistance(getDistance(lat.doubleValue(),lon.doubleValue(),centerGeoPoint.getLat(), centerGeoPoint.getLon()));
                            }
                        }
                    }
                }
                responses.add(addWorthSearchResponse(obj,resultMap));
            }
            return responses;
    }

    private double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        try {
            double radLat1 = getRadian(lat1);
            double radLat2 = getRadian(lat2);
            double a = radLat1 - radLat2;// 两点纬度差
            double b = getRadian(lng1) - getRadian(lng2);// 两点的经度差
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                    * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
            double EARTH_RADIUS = 6378.137;// 单位千米
            s = s * EARTH_RADIUS;
            BigDecimal bigDecimal = new BigDecimal(s);
            double s1 = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            return s1;
        } catch (Exception e) {
            return 0.00;
        }

    }

    public WorthSearchResponse queryWorthById(String id,String type){
        WorthSearchResponse worthSearchResponse = null;
        if(getClient()!=null){
            SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_WORTH_NAME).setTypes(SearchField.TYPE_WORTH_NAME);
            SearchResponse searchResponse=searchBuilder.setQuery(QueryBuilders.idsQuery().addIds(type+"-"+id)).setExplain(true)
                    .execute()
                    .actionGet();
            logger.info(searchBuilder.toString());
            Map<String,Object> resultMap=null;
            for(SearchHit searchHit:searchResponse.getHits()){
                resultMap = searchHit.getSourceAsMap();
                worthSearchResponse = addWorthSearchResponse(new WorthSearchResponse(),resultMap);
                if(resultMap.get("location")!=null){
                    if(StringUtils.isNotBlank(resultMap.get("location").toString())){
                        String[] geo = resultMap.get("location").toString().split(",");
                        if(geo.length==2){
                            BigDecimal lon = new BigDecimal(geo[1]);
                            BigDecimal lat = new BigDecimal(geo[0]);
                            if(checkLocationStr(lon) && checkLocationStr(lat)){
                                worthSearchResponse.setLon(lon);
                                worthSearchResponse.setLat(lat);
                            }
                        }
                    }
                }
                break;
            }
        }

        return worthSearchResponse;
    }

    private List<WorthSearchResponse> processResponse(SearchResponse searchResponse){
        List<WorthSearchResponse> responses=new ArrayList<>();
        WorthSearchResponse obj=null;
        Map<String,Object> resultMap=null;
        for(SearchHit searchHit:searchResponse.getHits()){
            resultMap=searchHit.getSourceAsMap();
            obj=new WorthSearchResponse();
            if(resultMap.get("location")!=null){
                if(StringUtils.isNotBlank(resultMap.get("location").toString())){
                    String[] geo = resultMap.get("location").toString().split(",");
                    if(geo.length==2){
                        BigDecimal lon = new BigDecimal(geo[1]);
                        BigDecimal lat = new BigDecimal(geo[0]);
                        if(checkLocationStr(lon) && checkLocationStr(lat)){
                            obj.setLon(lon);
                            obj.setLat(lat);
                            if (searchHit.getSortValues().length > 0) {
                                obj.setDistance(getDistance(searchHit));
                            }
                        }
                    }
                }
            }
            responses.add(addWorthSearchResponse(obj,resultMap));
        }
        return responses;
    }

    private WorthSearchResponse addWorthSearchResponse(WorthSearchResponse obj,Map<String,Object> resultMap){
        if(resultMap.get("id")!=null){
            obj.setId(resultMap.get("id").toString());
        }
        if(resultMap.get("status")!=null){
            obj.setStatus((Integer)(resultMap.get("status")));
        }
        if(resultMap.get("meetingType")!=null){
            obj.setMeetingType((Integer)(resultMap.get("meetingType")));
        }
        if(resultMap.get("worthType")!=null){
            obj.setWorthType(resultMap.get("worthType").toString());
        }
        if(resultMap.get("title")!=null){
            obj.setTitle(resultMap.get("title").toString());
        }
        if(resultMap.get("detail")!=null){
            obj.setDetail(resultMap.get("detail").toString());
        }
        if(resultMap.get("hit")!=null){
            obj.setHit(Integer.parseInt(resultMap.get("hit").toString()));
        }
        if(resultMap.get("publishTime")!=null){
            obj.setPublishTime(StringToDate(resultMap.get("publishTime").toString(),true));
        }
        if(resultMap.get("endTime")!=null){
            obj.setEndTime(StringToDate(resultMap.get("endTime").toString(),true));
        }
        if(resultMap.get("amount")!=null){
            obj.setAmount(new BigDecimal(resultMap.get("amount").toString()));
        }
        if(resultMap.get("num")!=null){
            obj.setNum((Integer) (resultMap.get("num")));
        }
        if(resultMap.get("dealsCount")!=null){
            obj.setCount(Integer.parseInt(resultMap.get("dealsCount").toString()));
        }
        if(resultMap.get("dealsTotal")!=null){
            obj.setTotal(new BigDecimal(resultMap.get("dealsTotal").toString()));
        }
        if(resultMap.get("imgesUrl")!=null){
            obj.setImages(resultMap.get("imgesUrl").toString());
        }
        if(resultMap.get("unit")!=null){
            obj.setUnit(resultMap.get("unit").toString());
        }
        if(resultMap.get("isHoldCredit")!=null){
            obj.setHoldCredit(strToBoolean(resultMap.get("isHoldCredit").toString()));
        }
        if(resultMap.get("userId")!=null){
            obj.setUserId(resultMap.get("userId").toString());
        }
        if(resultMap.get("headImg")!=null){
            obj.setHeadImg(resultMap.get("headImg").toString());
        }
        if(resultMap.get("birthday")!=null){
            obj.setBirthday(StringToDate(resultMap.get("birthday").toString(),false));
        }
        if(resultMap.get("nickname")!=null){
            obj.setNickname(resultMap.get("nickname").toString());
        }

        if(resultMap.get("lv")!=null){
            obj.setLv(Integer.parseInt(resultMap.get("lv").toString()));
        }

        if(resultMap.get("isLogin")!=null){
            obj.setLogin(strToBoolean(resultMap.get("isLogin").toString()));
        }

        if(resultMap.get("credit")!=null){
            obj.setCredit(Integer.parseInt(resultMap.get("credit").toString()));
        }

        if(resultMap.get("loginAt")!=null){
            obj.setLastLoginAt(StringToDate(resultMap.get("loginAt").toString(),true));
        }

        if(resultMap.get("activeAt")!=null){
            obj.setLastActiveAt(StringToDate(resultMap.get("activeAt").toString(),true));
        }

        if(resultMap.get("sex")!=null){
            obj.setSex(resultMap.get("sex").toString());
        }

        if(resultMap.get("matchImageUrl")!=null){
            obj.setMatchImageUrl((List<String>)resultMap.get("matchImageUrl"));
        }

        if(resultMap.get("subTitle")!=null){
            obj.setSubTitle(resultMap.get("subTitle").toString());
        }

        if(resultMap.get("matchKind")!=null){
            obj.setMatchKind(new Integer(resultMap.get("matchKind").toString()));
        }

        if(resultMap.get("matchRule")!=null){
            obj.setMatchRule((resultMap.get("matchRule").toString()));
        }

        if(resultMap.get("grading")!=null){
            obj.setGrading((resultMap.get("grading").toString()));
        }

        if(resultMap.get("matchIntroduction")!=null){
            obj.setMatchIntroduction((resultMap.get("matchIntroduction").toString()));
        }

        if(resultMap.get("organizerName")!=null){
            obj.setOrganizerName((resultMap.get("organizerName").toString()));
        }

        if(resultMap.get("initimtorId")!=null){
            obj.setInitimtorId((resultMap.get("initimtorId").toString()));
        }

        if(resultMap.get("regCount")!=null){
            obj.setRegCount(new Integer(resultMap.get("regCount").toString()));
        }
        return obj;
    }
}
