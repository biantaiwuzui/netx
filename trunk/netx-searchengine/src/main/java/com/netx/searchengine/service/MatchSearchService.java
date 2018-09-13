package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.NetxWorthSearchService;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.MatchSearchResponse;
import com.netx.searchengine.query.MatchSearchQuery;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 赛事搜索服务
 * Created by Yawn on 2018/9/5 0005.
 */
@Service
public class MatchSearchService extends BaseSearchService {

    private Logger logger = LoggerFactory.getLogger(MatchSearchService.class);

    public List<MatchSearchResponse> queryMatch(MatchSearchQuery matchSearchQuery) {
        if(getClient()==null){
            return new ArrayList<>();
        }
        level = 0;
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_MATCH_NANE).setTypes(SearchField.TYPE_MATCH_NAME);
        searchBuilder.setFrom(matchSearchQuery.getFrom())
                .setSize(matchSearchQuery.getPageSize())
                .setExplain(false);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        int [] queryTerm={3,4,5,6,7,8};
        // 标题
        if (StringUtils.isNotBlank(matchSearchQuery.getTitle())) {
            queryBuilder.must(QueryBuilders.wildcardQuery("title","*"+matchSearchQuery.getTitle()+"*"));
        }
        // 创建者ID
        if (StringUtils.isNotBlank(matchSearchQuery.getUserId())) {
            queryBuilder.must(QueryBuilders.matchQuery("initiatorId", matchSearchQuery.getUserId()));
        }
        // 比赛状态
        if(matchSearchQuery.getStatus()!=null){
            queryBuilder.must(QueryBuilders.termQuery("matchStatus", matchSearchQuery.getStatus()));
        }else {
            queryBuilder.must(QueryBuilders.termsQuery("matchStatus",queryTerm));
        }
        // 比赛类型
        if(matchSearchQuery.getMatchKind() != null){
            queryBuilder.must(QueryBuilders.termQuery("matchKind", matchSearchQuery.getMatchKind()));
        }
        // 点击数
        if(matchSearchQuery.getCreditSum()!=null){
            queryBuilder.must(QueryBuilders.termQuery("creditSum", matchSearchQuery.getCreditSum()));
        }


        /**
         * 没位置信息，报错的
         */
//        添加距离之前排序条件
        if(matchSearchQuery.getFristAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:matchSearchQuery.getFristAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    level++;
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()? SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        if (matchSearchQuery.getDistanceUnit() != null && matchSearchQuery.getCenterGeoPoint() != null) {
            Double lat = matchSearchQuery.getCenterGeoPoint().getLat();
            Double lon = matchSearchQuery.getCenterGeoPoint().getLon();
            Double distance = matchSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_SELLER_LATLON_NAME)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);

            searchBuilder.setPostFilter(distanceQueryBuilder);
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_SELLER_LATLON_NAME, lat, lon);
            sort.unit(matchSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat,lon);
            searchBuilder.addSort(sort);
        }

        //添加距离之后排序条件
        if(matchSearchQuery.getLastAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:matchSearchQuery.getLastAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }
        System.out.println(queryBuilder.toString());
        super.printfSearch(queryBuilder);

        searchBuilder.setQuery(queryBuilder);
        SearchResponse response = searchBuilder.execute().actionGet();
        return processSearchResponse(response);
    }

    private List<MatchSearchResponse> processSearchResponse(SearchResponse response) {
        List<MatchSearchResponse> matchSearchResponses = new ArrayList<>();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> resultMap = null;
            try {
                resultMap = searchHit.getSourceAsMap();
                MatchSearchResponse  matchSearchResponse = new MatchSearchResponse();
                if(resultMap.get("location")!=null){
                    if(StringUtils.isNotBlank(resultMap.get("location").toString())){
                        String[] geo = resultMap.get("location").toString().split(",");
                        if(geo.length==2){
                            BigDecimal lon = new BigDecimal(geo[1]);
                            BigDecimal lat = new BigDecimal(geo[0]);
                            if(checkLocationStr(lon) && checkLocationStr(lat)){
                                if (searchHit.getSortValues().length > 0) {
                                    matchSearchResponse.setDistance(getDistance(searchHit));
                                }
                            }
                        }
                    }
                }
                if(resultMap.get("id") != null)
                    matchSearchResponse.setId(resultMap.get("id").toString());
                if(resultMap.get("initiatorId") != null)
                    matchSearchResponse.setInitimtorId(resultMap.get("initiatorId").toString());

                if(resultMap.get("title") != null)
                    matchSearchResponse.setTitle(resultMap.get("title").toString());

                if(resultMap.get("matchImageUrl") != null)
                    matchSearchResponse.setMatchImageUrl((List<String>)resultMap.get("matchImageUrl"));

                if(resultMap.get("subTitle") != null)
                    matchSearchResponse.setSubTitle(resultMap.get("subTitle").toString());

                if(resultMap.get("matchKind") != null)
                    matchSearchResponse.setMatchKind(new Integer(resultMap.get("matchKind").toString()));

                if(resultMap.get("matchRule") != null)
                    matchSearchResponse.setMatchRule(resultMap.get("matchRule").toString());

                if(resultMap.get("grading") != null)
                    matchSearchResponse.setGrading(resultMap.get("grading").toString());

                if(resultMap.get("matchIntroduction") != null)
                    matchSearchResponse.setMatchIntroduction(resultMap.get("matchIntroduction").toString());

                if(resultMap.get("matchStatus") != null)
                    matchSearchResponse.setMatchStatus(new Integer(resultMap.get("matchStatus").toString()));

                if(resultMap.get("publishTime") != null)
                    matchSearchResponse.setPublishTime(StringToDate(resultMap.get("publishTime")+"",true));

                if(resultMap.get("nickname") != null)
                    matchSearchResponse.setNickname(resultMap.get("nickname").toString());

                if(resultMap.get("sex") != null)
                    matchSearchResponse.setSex(resultMap.get("sex").toString());

                if(resultMap.get("birthday") != null)
                    matchSearchResponse.setBirthday(StringToDate(resultMap.get("birthday").toString(),false));

                if(resultMap.get("mobile") != null)
                    matchSearchResponse.setMobile(resultMap.get("mobile").toString());

                if(resultMap.get("score") != null)
                    matchSearchResponse.setScore(new BigDecimal(resultMap.get("score").toString()));

                if(resultMap.get("credit") != null)
                    matchSearchResponse.setCredit(new Integer(resultMap.get("credit").toString()));

                if(resultMap.get("isLogin") != null)
                    matchSearchResponse.setIsLogin(new Integer(resultMap.get("isLogin").toString()));

                if(resultMap.get("lv") != null)
                    matchSearchResponse.setLv(new Integer(resultMap.get("lv").toString()));

                if(resultMap.get("creditSum") != null)
                    matchSearchResponse.setCreditSum(new Integer(resultMap.get("creditSum").toString()));

                if(resultMap.get("pictureUrl") != null)
                    matchSearchResponse.setPictureUrl(resultMap.get("pictureUrl").toString());

                if(resultMap.get("merchantId") != null)
                    matchSearchResponse.setMerchantId(resultMap.get("merchantId").toString());

                if(resultMap.get("organizerName") != null)
                    matchSearchResponse.setOrganizerName(resultMap.get("organizerName").toString());

                if(resultMap.get("regCount") != null)
                    matchSearchResponse.setRegCount(new Integer(resultMap.get("regCount").toString()));
                matchSearchResponses.add(matchSearchResponse);

            } catch (Exception e) {
                logger.error("error at processNewsList, message:" + resultMap, e);
            }
        }
        return matchSearchResponses;
    }


}
