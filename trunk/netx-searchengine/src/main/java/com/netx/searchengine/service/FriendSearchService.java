package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.enums.FriendTypeEnum;
import com.netx.searchengine.model.UserSearchResponse;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.Query;
import com.netx.searchengine.query.UserFriendSearchQuery;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class FriendSearchService extends BaseSearchService{
    private Logger logger = LoggerFactory.getLogger(FriendSearchService.class);

    /**
     * 返回资源
     * @param userFriendSearchQuery
     * @return
     */
    public List<Map<String,Object>> queryFriendMap(UserFriendSearchQuery userFriendSearchQuery) {
        SearchResponse searchResponse = searchResponse(userFriendSearchQuery);
        List<Map<String, Object>> list = new ArrayList<>();
        if(searchResponse!=null){
            for (SearchHit searchHit : searchResponse.getHits()) {
                Map<String, Object> map = searchHit.getSourceAsMap();
                Double distance = null;
                if(map.get("location")!=null){
                    if(StringUtils.isNotBlank(map.get("location").toString())){
                        String[] geo = map.get("location").toString().split(",");
                        if(geo.length==2) {
                            BigDecimal lon = new BigDecimal(geo[1]);
                            BigDecimal lat = new BigDecimal(geo[0]);
                            if (checkLocationStr(lon) && checkLocationStr(lat)) {
                                if (searchHit.getSortValues().length > 0) {
                                    distance = getDistance(searchHit);
                                }
                                map.put("lon", lon);
                                map.put("lat", lat);
                            }
                        }
                    }
                }
                map.put("distance",distance);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 搜索
     * @param userFriendSearchQuery
     * @return
     */
    private SearchResponse searchResponse(UserFriendSearchQuery userFriendSearchQuery){
        level = 0;
        if(getClient()==null){
            return null;
        }
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_USER_NAME).setTypes(SearchField.TYPE_USER_NAME);
        BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery();

        //热词搜索
        if(userFriendSearchQuery.getQueryMap()!=null){
            for(FriendTypeEnum friendTypeEnum : userFriendSearchQuery.getQueryMap().keySet()){
                setQuery(searchBuilder,queryBuilders,friendTypeEnum,userFriendSearchQuery.getQueryMap().get(friendTypeEnum));
            }
        }

        Long currentTime = System.currentTimeMillis();

        //体重
        if(userFriendSearchQuery.getMinWeight()==0){
            if(userFriendSearchQuery.getMaxWeight()!=0){
                queryBuilders.must(QueryBuilders.rangeQuery("weight").lte(userFriendSearchQuery.getMaxWeight()));
            }
        }else{
            if(userFriendSearchQuery.getMaxWeight()!=0){
                queryBuilders.must(QueryBuilders.rangeQuery("weight").gte(userFriendSearchQuery.getMinWeight()).lte(userFriendSearchQuery.getMaxWeight()));
            }else{
                queryBuilders.must(QueryBuilders.rangeQuery("weight").gte(userFriendSearchQuery.getMinWeight()));
            }
        }

        //身高
        if(userFriendSearchQuery.getMinHeight()==0){
            if(userFriendSearchQuery.getMaxHeight()!=0){
                queryBuilders.must(QueryBuilders.rangeQuery("height").lte(userFriendSearchQuery.getMaxHeight()));
            }
        }else{
            if(userFriendSearchQuery.getMaxHeight()!=0){
                queryBuilders.must(QueryBuilders.rangeQuery("height").gte(userFriendSearchQuery.getMinHeight()).lte(userFriendSearchQuery.getMaxHeight()));
            }else{
                queryBuilders.must(QueryBuilders.rangeQuery("height").gte(userFriendSearchQuery.getMinHeight()));
            }
        }

        //收入
        setIncome(queryBuilders,userFriendSearchQuery.getMinIncome(),userFriendSearchQuery.getMaxIncome());

        //用户id模糊查询
        if(StringUtils.isNotBlank(userFriendSearchQuery.getUserId())){
            queryBuilders.must(QueryBuilders.wildcardQuery("id","*"+userFriendSearchQuery.getUserId()+"*"));
        }
        if(StringUtils.isNotBlank(userFriendSearchQuery.getExcludeUserId())){
            queryBuilders.mustNot(QueryBuilders.wildcardQuery("id","*"+userFriendSearchQuery.getExcludeUserId()+"*"));
        }
        //网号
        if(StringUtils.isNotBlank(userFriendSearchQuery.getUserNumber())){
            queryBuilders.must(QueryBuilders.wildcardQuery("userNumber","*"+userFriendSearchQuery.getUserNumber()+"*"));
        }
        //昵称
        if(StringUtils.isNotBlank(userFriendSearchQuery.getNickname())){
            queryBuilders.must(QueryBuilders.wildcardQuery("nickName","*"+userFriendSearchQuery.getNickname()+"*"));
        }
        //性别
        if(StringUtils.isNotBlank(userFriendSearchQuery.getSex())){
            queryBuilders.must(QueryBuilders.matchQuery("sex",userFriendSearchQuery.getSex()));
        }

        //民族
        if (StringUtils.isNotBlank(userFriendSearchQuery.getNation())) {
            queryBuilders.must(QueryBuilders.matchQuery("nation", userFriendSearchQuery.getNation()));
        }

        //属相
        if (StringUtils.isNotBlank(userFriendSearchQuery.getAnimalSigns())) {
            queryBuilders.must(QueryBuilders.matchQuery("animalSigns", userFriendSearchQuery.getAnimalSigns()));
        }

        //星座
        if (StringUtils.isNotBlank(userFriendSearchQuery.getStarSign())) {
            queryBuilders.must(QueryBuilders.matchQuery("starSign", userFriendSearchQuery.getStarSign()));
        }

        //情感
        if (StringUtils.isNotBlank(userFriendSearchQuery.getEmotion())) {
            queryBuilders.must(QueryBuilders.wildcardQuery("emotion", "*"+userFriendSearchQuery.getEmotion()+"*"));
        }

        //是否搜索在线
        if(userFriendSearchQuery.getLogin()!=null){
            queryBuilders.must(QueryBuilders.matchQuery("isLogin",userFriendSearchQuery.getLogin()?1:0));
        }

        //是否搜索附近
        if(userFriendSearchQuery.getNearly()!=null){
            queryBuilders.must(QueryBuilders.matchQuery("nearlySetting",userFriendSearchQuery.getNearly()?0:1));
        }

        //学历
        if(StringUtils.isNotBlank(userFriendSearchQuery.getDegree())){
            queryBuilders.must(QueryBuilders.termsQuery("degreeItem",userFriendSearchQuery.getDegree()));
        }

        //学校
        for(String school: userFriendSearchQuery.getSchool()) {
            queryBuilders.must(QueryBuilders.termQuery("school", school));
        }

        //公司
        for(String company: userFriendSearchQuery.getCompany()) {
            queryBuilders.must(QueryBuilders.termQuery("company", company));
        }

        //兴趣爱好
        for(String interest: userFriendSearchQuery.getInterest()) {
            queryBuilders.must(QueryBuilders.termQuery("interestDetails", interest));
        }

        //年龄搜索
        setAge(queryBuilders,userFriendSearchQuery.getMinAge(),userFriendSearchQuery.getMaxAge(),currentTime);


        //在线时间
        if(userFriendSearchQuery.getOnlineTime()!=null){
            queryBuilders.must(QueryBuilders.rangeQuery("loginAt")
                    .from(currentTime - userFriendSearchQuery.getOnlineTime()*60*1000).to(new Date().getTime()));
        }

        //距离搜索
        if (userFriendSearchQuery.getDistanceUnit() != null && userFriendSearchQuery.getCenterGeoPoint() != null) {
            Double lat = userFriendSearchQuery.getCenterGeoPoint().getLat();
            Double lon = userFriendSearchQuery.getCenterGeoPoint().getLon();
            Double distance = userFriendSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_SELLER_LATLON_NAME)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);

            searchBuilder.setPostFilter(distanceQueryBuilder);
            /*GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_SELLER_LATLON_NAME, lat, lon);
            sort.unit(userFriendSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat,lon);
            searchBuilder.addSort(sort);*/
            addScriptGeo(searchBuilder,lon,lat,SearchField.QUERY_FIELD_SELLER_LATLON_NAME,userFriendSearchQuery.getDistanceUnit());
        }

        //添加距离之后排序条件
        if(userFriendSearchQuery.getLastAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:userFriendSearchQuery.getLastAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        //super.printfSearch(queryBuilders);

        //查询建立
        SearchResponse myresponse=searchBuilder.setQuery(queryBuilders)
                .setFrom(userFriendSearchQuery.getFrom()).setSize(userFriendSearchQuery.getPageSize()) //分页
                .setExplain(true)
                .execute()
                .actionGet();
        logger.info(searchBuilder.toString());
        return myresponse;
    }

    /**
     * 自定义搜索
     * @param searchBuilder
     * @param queryBuilders
     * @param friendTypeEnum
     * @param query
     */
    private void setQuery(SearchRequestBuilder searchBuilder,BoolQueryBuilder queryBuilders, FriendTypeEnum friendTypeEnum,Query query){
        if(query.getDescSort()!=null){
            level++;
            searchBuilder.addSort(friendTypeEnum.getKey(),query.getDescSort()?SortOrder.DESC:SortOrder.ASC);
        }
        if(query.getValues()!=null && query.getValues().size()>0){
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            for(Object value : query.getValues()){
                queryBuilder.should(QueryBuilders.commonTermsQuery(friendTypeEnum.getKey(),value));
            }
            queryBuilders.must(queryBuilder);
        }
        if(query.getFilter()!=null){
            Map<String,Object> map = query.getFilter();
            if(map.get("like")!=null){
                queryBuilders.must(QueryBuilders.fuzzyQuery(friendTypeEnum.getKey(),map.get("like")));
            }
            setBetween(queryBuilders,friendTypeEnum.getKey(),map.get("min"),map.get("max"));
        }
        if(query.getOtherCondition()!=null && !query.getOtherCondition().isEmpty()){
            for(String key:query.getOtherCondition().keySet()){
                queryBuilders.must(QueryBuilders.commonTermsQuery(key,query.getOtherCondition().get(key)));
            }
        }
    }

    private void setBetween(BoolQueryBuilder queryBuilders,String name,Object min,Object max){
        if(min!=null){
            if(max!=null){
                queryBuilders.must(QueryBuilders.rangeQuery(name)
                        .from(min).to(max));
            }else{
                queryBuilders.must(QueryBuilders.rangeQuery(name).from(min));
            }
        }else{
            if(max!=null){
                queryBuilders.must(QueryBuilders.rangeQuery(name).to(max));
            }
        }
    }

    /**
     * 返回对应list
     * @param userFriendSearchQuery
     * @return
     */
    public List<UserSearchResponse> queryFriends(UserFriendSearchQuery userFriendSearchQuery) {
        return processSearchResponse(searchResponse(userFriendSearchQuery));
    }

    /**
     * 设置收入条件
     * @param queryBuilders
     * @param min
     * @param max
     */
    private void setIncome(BoolQueryBuilder queryBuilders,Integer min,Integer max){
        if(min!=0){
            if(max==0){
                queryBuilders.must(QueryBuilders.rangeQuery("income").from(min));
            }else {
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                boolQueryBuilder.should(QueryBuilders.rangeQuery("income").from(min).to(max));
                boolQueryBuilder.should(QueryBuilders.rangeQuery("maxIncome").from(min).to(max));
                queryBuilders.filter(boolQueryBuilder);
            }
        }else {
            if(max!=0){
                queryBuilders.must(QueryBuilders.rangeQuery("maxIncome").to(max));
            }
        }
    }

    /**
     * 设置年龄条件
     * @param queryBuilder
     * @param minAge
     * @param maxAge
     * @param currentTime
     */
    private void setAge(BoolQueryBuilder queryBuilder,Integer minAge,Integer maxAge,Long currentTime){
        if(minAge==0){
            if(maxAge!=0){
                queryBuilder.must(QueryBuilders.rangeQuery("birthday")
                        .from(setYear(maxAge).getTime()).to(currentTime));
            }
        }else{
            if(maxAge==0){
                queryBuilder.must(QueryBuilders.rangeQuery("birthday")
                        .to(setYear(minAge).getTime()));
            }else {
                queryBuilder.must(QueryBuilders.rangeQuery("birthday")
                        .from(setYear(maxAge).getTime()).to(setYear(minAge).getTime()));
            }
        }
    }

    /**
     * 年计算
     * @param age
     * @return
     */
    private Date setYear(Integer age){
        Date date = new Date();
        date.setYear(date.getYear()-age);
        return date;
    }

    /**
     * 资源转
     * @param response
     * @return
     */
    private List<UserSearchResponse> processSearchResponse(SearchResponse response){
        List<UserSearchResponse> userSearchResponses = new ArrayList<>();
        if(response!=null){
            for (SearchHit searchHit : response.getHits()) {
                Map<String, Object> resultMap = null;
                try {
                    resultMap = searchHit.getSourceAsMap();
                    UserSearchResponse  userInfo = new UserSearchResponse();
                    if(resultMap.get("location")!=null){
                        if(StringUtils.isNotBlank(resultMap.get("location").toString())){
                            String[] geo = resultMap.get("location").toString().split(",");
                            if(geo.length==2){
                                BigDecimal lon = new BigDecimal(geo[1]);
                                BigDecimal lat = new BigDecimal(geo[0]);
                                if(checkLocationStr(lon) && checkLocationStr(lat)){
                                    userInfo.setLon(lon);
                                    userInfo.setLat(lat);
                                    if (searchHit.getSortValues().length > 0) {
                                        userInfo.setDistance(getDistance(searchHit));
                                    }
                                }
                            }
                        }
                    }

                    if(resultMap.get("id") != null)
                        userInfo.setId(resultMap.get("id").toString());

                    if(resultMap.get("userNumber") != null)
                        userInfo.setUserNumber(resultMap.get("userNumber").toString());

                    if(resultMap.get("nickName") != null)
                        userInfo.setNickname(resultMap.get("nickName").toString());

                    if(resultMap.get("sex") != null)
                        userInfo.setSex(resultMap.get("sex").toString());

                    if(resultMap.get("birthday") != null) {
                        userInfo.setBirthday(StringToDate(resultMap.get("birthday").toString(), false));
                    }

                    if(resultMap.get("lv") != null)
                        userInfo.setLv(Integer.parseInt(resultMap.get("lv").toString()));

                    if(resultMap.get("headImg") != null)
                        userInfo.setHeadImgUrl(resultMap.get("headImg").toString());

                    if(resultMap.get("credit") != null)
                        userInfo.setCredit(Integer.parseInt(resultMap.get("credit").toString()));

                    if(resultMap.get("height") != null)
                        userInfo.setHeight(Integer.parseInt(resultMap.get("height").toString()));

                    if(resultMap.get("loginAt") != null)
                        userInfo.setLastLoginAt(StringToDate(resultMap.get("loginAt").toString(),true));

                    if(resultMap.get("mobile") != null)
                        userInfo.setMobile(resultMap.get("mobile").toString());

                    if(resultMap.get("video") != null)
                        userInfo.setVideo(resultMap.get("video").toString());

                    if(resultMap.get("car") != null)
                        userInfo.setCar(resultMap.get("car").toString());

                    if(resultMap.get("house") != null)
                        userInfo.setHouse(resultMap.get("house").toString());

                    if(resultMap.get("degree") != null)
                        userInfo.setDegree(resultMap.get("degree").toString());

                    if(resultMap.get("education")!=null){
                        userInfo.setEducation(resultMap.get("education").toString());
                    }

                    if(resultMap.get("idNumber") != null) {
                        userInfo.setIdNumber(resultMap.get("idNumber").toString());
                    }

                    if(resultMap.get("address") != null) {
                        userInfo.setAddress(resultMap.get("address").toString());
                    }

                    if(resultMap.get("disposition") != null) {
                        userInfo.setDisposition(resultMap.get("disposition").toString());
                    }

                    if(resultMap.get("nearlySetting") != null) {
                        userInfo.setNearlySetting(Integer.parseInt(resultMap.get("nearlySetting").toString()));
                    }

                    if(resultMap.get("activeAt")!=null) {
                        userInfo.setLastActiveAt(StringToDate(resultMap.get("activeAt").toString(), true));
                    }

                    if(resultMap.get("degreeItem") != null) {
                        userInfo.setDegreeItem((List<String>) (List) resultMap.get("degreeItem"));
                    }

                    if(resultMap.get("credit")!=null){
                        userInfo.setCredit(Integer.parseInt(resultMap.get("credit").toString()));
                    }

                    if(resultMap.get("realName")!=null){
                        userInfo.setRealName(resultMap.get("realName").toString());
                    }

                    if(resultMap.get("oftenIn")!=null){
                        userInfo.setOftenIn(resultMap.get("oftenIn").toString());
                    }

                    if(resultMap.get("homeTown")!=null){
                        userInfo.setHomeTown(resultMap.get("homeTown").toString());
                    }

                    if(resultMap.get("isLogin")!=null){
                        userInfo.setLogin(strToBoolean(resultMap.get("isLogin").toString()));
                    }

                    userSearchResponses.add(userInfo);

                } catch (Exception e) {
                    logger.error("error at processNewsList, message:" + resultMap, e);
                }
            }
        }
        return userSearchResponses;
    }

}
