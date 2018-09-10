package com.netx.searchengine.service;


import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.ArticleSearchResponse;
import com.netx.searchengine.query.ArticleSearchQuery;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleSearchService extends BaseSearchService {

    private Logger logger = LoggerFactory.getLogger(ArticleSearchService.class);

    public List<ArticleSearchResponse> queryArticle(ArticleSearchQuery articleSearchQuery) {
        //连接集群 client对象已获取
        if (getClient() == null) {
            return new ArrayList<>();
        }
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_ARTICLE_NAME).setTypes(SearchField.TYPE_ARTICLE_NAME);
        level = 0;
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(articleSearchQuery.getTagName())) {
            queryBuilder.must(QueryBuilders.matchQuery(SearchField.QUERY_FIELD_ARTICLE_TAGNAME, articleSearchQuery.getTagName()).operator(Operator.AND));
        }
        if (StringUtils.isNotEmpty(articleSearchQuery.getUserId())) {
            queryBuilder.must(QueryBuilders.matchQuery(SearchField.QUERY_FIELD_ARTICLE_USERID, articleSearchQuery.getUserId()));
        }
        if (articleSearchQuery.getAdvertorialType() != null) {
            queryBuilder.must(QueryBuilders.matchQuery(SearchField.QUERY_FIELD_ARTICLE_ADVERTORIALTYPE, articleSearchQuery.getAdvertorialType()));
        }
        if (articleSearchQuery.getArticleType() != null) {
            queryBuilder.must(QueryBuilders.matchQuery(SearchField.QUERY_FIELD_ARTICLE_ARTICLETYPE, articleSearchQuery.getArticleType()));
        }
        if (articleSearchQuery.getStatusCode() != null) {
            queryBuilder.must(QueryBuilders.matchQuery(SearchField.QUERY_FIELD_ARTICLE_STATUSCODE, articleSearchQuery.getStatusCode()));
        }else{
            int[] status={0,2,3,4,5};
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_ARTICLE_STATUSCODE,status));
        }
        if (articleSearchQuery.getIsLock() != null) {
            queryBuilder.must(QueryBuilders.matchQuery(SearchField.QUERY_FIELD_ARTICLE_ISLOCK, articleSearchQuery.getIsLock()));
        }

        if (articleSearchQuery.getDistanceUnit() != null && articleSearchQuery.getCenterGeoPoint() != null) {
            double lat = articleSearchQuery.getCenterGeoPoint().getLat();
            double lon = articleSearchQuery.getCenterGeoPoint().getLon();
            double distance = articleSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_ARTICLE_LOCATION)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);


            searchBuilder.setPostFilter(distanceQueryBuilder);
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_ARTICLE_LOCATION, lat, lon);
            sort.unit(articleSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat, lon);
            searchBuilder.addSort(sort);
        }

        //查询建立
        SearchResponse myresponse = searchBuilder.setQuery(queryBuilder)
                .setFrom(articleSearchQuery.getFrom()).setSize(articleSearchQuery.getPageSize()) //分页
                .setExplain(true)
                .execute()
                .actionGet();

        logger.info(searchBuilder.toString());
        return processResponse(myresponse);

    }

    private List<ArticleSearchResponse> processResponse(SearchResponse searchResponse) {
        List<ArticleSearchResponse> responses = new ArrayList<>();
        ArticleSearchResponse obj = null;
        Map<String, Object> resultMap = null;
        for (SearchHit searchHit : searchResponse.getHits()) {
            resultMap = searchHit.getSourceAsMap();
            obj = new ArticleSearchResponse();
            if (resultMap.get("location") != null) {
                if (StringUtils.isNotBlank(resultMap.get("location").toString())) {
                    String[] geo = resultMap.get("location").toString().split(",");
                    if (geo.length == 2) {
                        BigDecimal lon = new BigDecimal(geo[1]);
                        BigDecimal lat = new BigDecimal(geo[0]);
                        if (checkLocationStr(lon) && checkLocationStr(lat)) {
                            obj.setLon(lon);
                            obj.setLat(lat);
                            if (searchHit.getSortValues().length > 0) {
                                obj.setDistance(getDistance(searchHit));
                            }
                        }
                    }
                }
            }
            if (resultMap.get("id") != null) {
                obj.setId(resultMap.get("id").toString());
            }
            if (resultMap.get("userId") != null) {
                obj.setUserId(resultMap.get("userId").toString());
            }
            if (resultMap.get("title") != null) {
                obj.setTitle(resultMap.get("title").toString());
            }
            if (resultMap.get("tagNames") != null) {
                obj.setTagNames((List<String>) (List) (resultMap.get("tagNames")));
            }
            if (resultMap.get("pic") != null) {
                obj.setPic(resultMap.get("pic").toString());
            }
            if (resultMap.get("atta") != null) {
                obj.setAtta(resultMap.get("atta").toString());
            }
            if (resultMap.get("content") != null) {
                obj.setContent(resultMap.get("content").toString());
            }
            if (resultMap.get("who") != null) {
                obj.setWho(Integer.parseInt(resultMap.get("who").toString()));
            }
            if (resultMap.get("isShowLocation") != null) {
                if (Integer.parseInt(resultMap.get("isShowLocation").toString()) == 1) {
                    if (resultMap.get("reLocation") != null) {
                        obj.setLocation(resultMap.get("reLocation").toString());
                    }
                }
            }

            if (resultMap.get("advertorialType") != null) {
                obj.setAdvertorialType(Integer.parseInt(resultMap.get("advertorialType").toString()));
            }

            if (resultMap.get("isIllegal") != null) {
                obj.setIllegal(resultMap.get("isIllegal").toString().equals("1"));
            }

            if (resultMap.get("statusCode") != null) {
                obj.setStatusCode(Integer.parseInt(resultMap.get("statusCode").toString()));
            }

            if (resultMap.get("reason") != null) {
                obj.setReason(resultMap.get("reason").toString());
            }

            if (resultMap.get("hits") != null) {
                obj.setHits(Long.parseLong(resultMap.get("hits").toString()));
            }

            if (resultMap.get("createTime") != null) {
                obj.setCreateTime(super.StringToDate(resultMap.get("createTime").toString(), true));
            }

            if (resultMap.get("commentNum") != null) {
                obj.setCommentNum(Integer.parseInt(resultMap.get("commentNum").toString()));
            }

            if (resultMap.get("giftNum") != null) {
                obj.setGiftNum(Integer.parseInt(resultMap.get("giftNum").toString()));
            }

            if (resultMap.get("invitationNum") != null) {
                obj.setInvitationNum(Integer.parseInt(resultMap.get("invitationNum").toString()));
            }

            if (resultMap.get("likeNum") != null) {
                obj.setLikeNum(Integer.parseInt(resultMap.get("likeNum").toString()));
            }

            if (resultMap.get("isAnonymity") != null) {
                obj.setAnonymity(strToBoolean(resultMap.get("isAnonymity").toString()));
            }

            responses.add(obj);
        }
        return responses;
    }

}
