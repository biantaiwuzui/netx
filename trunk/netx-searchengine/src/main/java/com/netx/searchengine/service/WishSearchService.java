package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.NetxWorthSearchService;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.WishSearchResponse;
import com.netx.searchengine.query.WishSearchQuery;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WishSearchService extends NetxWorthSearchService {
    private Logger logger = LoggerFactory.getLogger(WishSearchService.class);

    public List<WishSearchResponse> queryWishs(WishSearchQuery wishSearchQuery) {
        if(getClient()==null){
            return new ArrayList<>();
        }
        level = 0;
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_WISH_NAME).setTypes(SearchField.TYPE_WISH_NAME);
        searchBuilder.setFrom(wishSearchQuery.getFrom())
                .setSize(wishSearchQuery.getPageSize())
                .setExplain(false);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if(wishSearchQuery.getLogin()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isLogin",wishSearchQuery.getLogin()?1:0));
        }

        if (StringUtils.isNotBlank(wishSearchQuery.getTitle())) {
            queryBuilder.must(QueryBuilders.wildcardQuery("title","*"+wishSearchQuery.getTitle()+"*"));
        }

       if (StringUtils.isNotBlank(wishSearchQuery.getUserId())) {
            queryBuilder.must(QueryBuilders.matchQuery("userId", wishSearchQuery.getUserId()));
        }

        if(wishSearchQuery.getWishLabels()!=null && wishSearchQuery.getWishLabels().size()>0){
            addTermShoule(queryBuilder,"wishLabels",wishSearchQuery.getWishLabels());
        }

        if(wishSearchQuery.getStatus()!=null) {
            queryBuilder.must(QueryBuilders.termQuery("status", wishSearchQuery.getStatus()));
        }else{
            int[] queryWish={4,6,7};
            queryBuilder.must(QueryBuilders.termsQuery("status", queryWish));
        }

        if(wishSearchQuery.getCreditSum()!=null){
            queryBuilder.must(QueryBuilders.termQuery("creditSum", wishSearchQuery.getCreditSum()));
        }

        if(wishSearchQuery.getLock()!=null){
            queryBuilder.must(QueryBuilders.termQuery("isLock", wishSearchQuery.getLock()?1:0));
        }

        //添加距离之前排序条件
        if(wishSearchQuery.getFristAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:wishSearchQuery.getFristAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    level++;
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        if (wishSearchQuery.getDistanceUnit() != null && wishSearchQuery.getCenterGeoPoint() != null) {
            Double lat = wishSearchQuery.getCenterGeoPoint().getLat();
            Double lon = wishSearchQuery.getCenterGeoPoint().getLon();
            Double distance = wishSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_SELLER_LATLON_NAME)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);

            searchBuilder.setPostFilter(distanceQueryBuilder);
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_SELLER_LATLON_NAME, lat, lon);
            sort.unit(wishSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat,lon);
            searchBuilder.addSort(sort);
        }

        //添加距离之后排序条件
        if(wishSearchQuery.getLastAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:wishSearchQuery.getLastAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }
        //查询
        searchBuilder.setQuery(queryBuilder);
        SearchResponse response = searchBuilder
                .execute()
                .actionGet();
        logger.info(searchBuilder.toString());
        return processSearchResponse(response);
    }

    private List<WishSearchResponse> processSearchResponse(SearchResponse response){
        List<WishSearchResponse> wishSearchResponses = new ArrayList<>();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> resultMap = null;
            try {
                resultMap = searchHit.getSourceAsMap();
                WishSearchResponse  wishSearchResponse = new WishSearchResponse();
                if (searchHit.getSortValues().length > 0) {
                    BigDecimal geoDis = new BigDecimal((Double) searchHit.getSortValues()[level]);
                    BigDecimal distance = geoDis.setScale(3, BigDecimal.ROUND_HALF_UP);
                    wishSearchResponse.setDistance(distance.doubleValue());
                }
                if(resultMap.get("id") != null)
                    wishSearchResponse.setId(resultMap.get("id").toString());

                if(resultMap.get("userId") != null)
                    wishSearchResponse.setUserId(resultMap.get("userId").toString());

                if(resultMap.get("title") != null)
                    wishSearchResponse.setTitle(resultMap.get("title").toString());

                if(resultMap.get("wishLabels") != null)
                    wishSearchResponse.setWishLabels((List<String>)(List)resultMap.get("wishLabels"));

                if(resultMap.get("amount") != null)
                    wishSearchResponse.setAmount(changeBigDecimal(resultMap.get("amount").toString()));

                if(resultMap.get("currentAmount") != null)
                    wishSearchResponse.setCurrentAmount(changeBigDecimal(resultMap.get("currentAmount").toString()));

                if(resultMap.get("currentApplyAmount") != null)
                    wishSearchResponse.setCurrentAmount(changeBigDecimal(resultMap.get("currentApplyAmount").toString()));

                if(resultMap.get("expiredAt") != null)
                    wishSearchResponse.setExpiredAt(StringToDate(resultMap.get("expiredAt").toString(), true));

                if(resultMap.get("refereeIds") != null)
                    wishSearchResponse.setRefereeIds((List<String>) (List)resultMap.get("refereeIds"));

                if(resultMap.get("description") != null)
                    wishSearchResponse.setDescription(resultMap.get("description").toString());

                if(resultMap.get("imagesUrl") != null)
                    wishSearchResponse.setImagesUrl(resultMap.get("imagesUrl").toString());

                if(resultMap.get("imagesTwoUrl") != null)
                    wishSearchResponse.setImagesTwoUrl(resultMap.get("imagesTwoUrl").toString());

                if(resultMap.get("status") != null)
                    wishSearchResponse.setStatus(Integer.parseInt(resultMap.get("status").toString()));

                if(resultMap.get("nickname") != null){
                    wishSearchResponse.setNickname(resultMap.get("nickname").toString());
                }

                if(resultMap.get("birthday") != null){
                    wishSearchResponse.setBirthday(StringToDate(resultMap.get("birthday").toString(),false));
                }

                if(resultMap.get("sex") != null){
                    wishSearchResponse.setSex(resultMap.get("sex").toString());
                }

                if(resultMap.get("mobile") != null){
                    wishSearchResponse.setMobile(resultMap.get("mobile").toString());
                }

                if(resultMap.get("score") != null){
                    wishSearchResponse.setScore(new BigDecimal(resultMap.get("score").toString()));
                }

                if(resultMap.get("credit") != null){
                    wishSearchResponse.setCredit(Integer.parseInt(resultMap.get("credit").toString()));
                }

                if(resultMap.get("createTime") != null){
                    wishSearchResponse.setCreateTime(StringToDate(resultMap.get("createTime").toString(), true));
                }

                if(resultMap.get("lv") != null){
                    wishSearchResponse.setLv(Integer.parseInt(resultMap.get("lv").toString()));
                }

                if(resultMap.get("creditSum") != null){
                    wishSearchResponse.setCreditSum(Integer.parseInt(resultMap.get("creditSum").toString()));
                }

                if(resultMap.get("count") != null){
                    wishSearchResponse.setCount(Integer.parseInt(resultMap.get("count").toString()));
                }

                if(resultMap.get("isLock") != null){
                    wishSearchResponse.setLock(strToBoolean(resultMap.get("isLock").toString()));
                }

                wishSearchResponses.add(wishSearchResponse);

            } catch (Exception e) {
                logger.error("error at processNewsList, message:" + resultMap, e);
            }
        }
        return wishSearchResponses;
    }

    private BigDecimal changeBigDecimal(String bigDecimal){
        return new BigDecimal(bigDecimal);
    }
}
