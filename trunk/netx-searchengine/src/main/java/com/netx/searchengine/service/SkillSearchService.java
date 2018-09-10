package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.NetxWorthSearchService;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.SkillSearchResponse;
import com.netx.searchengine.query.SkillSearchQuery;
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

@Service
public class SkillSearchService extends NetxWorthSearchService {
    private Logger logger = LoggerFactory.getLogger(SkillSearchService.class);

    public List<SkillSearchResponse> querySkills(SkillSearchQuery skillSearchQuery) {
        if(getClient()==null){
            return new ArrayList<>();
        }
        level = 0;
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_SKILL_NAME).setTypes(SearchField.TYPE_SKILL_NAME);
        searchBuilder.setFrom(skillSearchQuery.getFrom())
                .setSize(skillSearchQuery.getPageSize())
                .setExplain(false);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if(skillSearchQuery.getLogin()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isLogin",skillSearchQuery.getLogin()?1:0));
        }

        if (StringUtils.isNotBlank(skillSearchQuery.getDescription())) {
            queryBuilder.must(QueryBuilders.wildcardQuery("description","*"+skillSearchQuery.getDescription()+"*"));
        }
        if (StringUtils.isNotBlank(skillSearchQuery.getUserId())) {
            queryBuilder.must(QueryBuilders.matchQuery("userId", skillSearchQuery.getUserId()));
        }

        if(skillSearchQuery.getSkillLabels()!=null && skillSearchQuery.getSkillLabels().size()>0){
            addTermShoule(queryBuilder,"skillLabels",skillSearchQuery.getSkillLabels());
        }

        for(String levels: skillSearchQuery.getLevels()) {
            queryBuilder.must(QueryBuilders.termQuery("levels", levels));
        }

        if(skillSearchQuery.getStatus()!=null){
            queryBuilder.must(QueryBuilders.termQuery("status", skillSearchQuery.getStatus()));
        }else {
            int[] a={1,3};
            queryBuilder.must(QueryBuilders.termsQuery("status",a));
        }

        if(skillSearchQuery.getCreditSum()!=null){
            queryBuilder.must(QueryBuilders.termQuery("creditSum", skillSearchQuery.getCreditSum()));
        }

        //添加距离之前排序条件
        if(skillSearchQuery.getFristAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:skillSearchQuery.getFristAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    level++;
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        if (skillSearchQuery.getDistanceUnit() != null && skillSearchQuery.getCenterGeoPoint() != null) {
            Double lat = skillSearchQuery.getCenterGeoPoint().getLat();
            Double lon = skillSearchQuery.getCenterGeoPoint().getLon();
            Double distance = skillSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_SELLER_LATLON_NAME)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);

            searchBuilder.setPostFilter(distanceQueryBuilder);
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_SELLER_LATLON_NAME, lat, lon);
            sort.unit(skillSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat,lon);
            searchBuilder.addSort(sort);
        }

        //添加距离之后排序条件
        if(skillSearchQuery.getLastAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:skillSearchQuery.getLastAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        searchBuilder.setQuery(queryBuilder);
        SearchResponse response = searchBuilder.execute().actionGet();
        logger.info(searchBuilder.toString());
        return processSearchResponse(response);
    }

    private List<SkillSearchResponse> processSearchResponse(SearchResponse response){
        List<SkillSearchResponse> skillSearchResponses = new ArrayList<>();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> resultMap = null;
            try {
                resultMap = searchHit.getSourceAsMap();
                SkillSearchResponse  skillSearchResponse = new SkillSearchResponse();
                if (searchHit.getSortValues().length > 0) {
                    BigDecimal geoDis = new BigDecimal((Double) searchHit.getSortValues()[level]);
                    BigDecimal distance = geoDis.setScale(3, BigDecimal.ROUND_HALF_UP);
                    skillSearchResponse.setDistance(distance.doubleValue());
                }
                if(resultMap.get("id") != null)
                    skillSearchResponse.setId(resultMap.get("id").toString());

                if(resultMap.get("userId") != null)
                    skillSearchResponse.setUserId(resultMap.get("userId").toString());

                if(resultMap.get("skillLabels") != null)
                    skillSearchResponse.setSkillLabels((List<String>)(List)resultMap.get("skillLabels"));

                if(resultMap.get("levels") != null)
                    skillSearchResponse.setLevels((List<String>)(List)resultMap.get("levels"));

                if(resultMap.get("description") != null)
                    skillSearchResponse.setDescription(resultMap.get("description").toString());

                if(resultMap.get("skillImagesUrl") != null)
                    skillSearchResponse.setSkillImagesUrl(resultMap.get("skillImagesUrl").toString());

                if(resultMap.get("skillDetailImagesUrl") != null)
                    skillSearchResponse.setSkillDetailImagesUrl(resultMap.get("skillDetailImagesUrl").toString());

                if(resultMap.get("unit") != null)
                    skillSearchResponse.setUnit(resultMap.get("unit").toString());

                if(resultMap.get("amount") != null)
                    skillSearchResponse.setAmount(changeBigDecimal(resultMap.get("amount").toString()));

                if(resultMap.get("intr") != null)
                    skillSearchResponse.setIntr(resultMap.get("intr").toString());

                if(resultMap.get("obj") != null)
                    skillSearchResponse.setObj(Integer.parseInt(resultMap.get("obj").toString()));

                if(resultMap.get("status") != null)
                    skillSearchResponse.setStatus(Integer.parseInt(resultMap.get("status").toString()));

                if(resultMap.get("nickname") != null){
                    skillSearchResponse.setNickname(resultMap.get("nickname").toString());
                }

                if(resultMap.get("birthday") != null){
                    skillSearchResponse.setBirthday(StringToDate(resultMap.get("birthday").toString(),false));
                }

                if(resultMap.get("sex") != null){
                    skillSearchResponse.setSex(resultMap.get("sex").toString());
                }

                if(resultMap.get("mobile") != null){
                    skillSearchResponse.setMobile(resultMap.get("mobile").toString());
                }

                if(resultMap.get("score") != null){
                    skillSearchResponse.setScore(new BigDecimal(resultMap.get("score").toString()));
                }

                if(resultMap.get("credit") != null){
                    skillSearchResponse.setCredit(Integer.parseInt(resultMap.get("credit").toString()));
                }

                if(resultMap.get("createTime") != null){
                    skillSearchResponse.setCreateTime(StringToDate(resultMap.get("createTime").toString(), true));
                }

                if(resultMap.get("lv") != null){
                    skillSearchResponse.setLv(Integer.parseInt(resultMap.get("lv").toString()));
                }

                if(resultMap.get("creditSum") != null){
                    skillSearchResponse.setCreditSum(Integer.parseInt(resultMap.get("creditSum").toString()));
                }

                if(resultMap.get("registerCount") != null){
                    skillSearchResponse.setCount(Integer.parseInt(resultMap.get("registerCount").toString()));
                }

                skillSearchResponses.add(skillSearchResponse);

            } catch (Exception e) {
                logger.error("error at processNewsList, message:" + resultMap, e);
            }
        }
        return skillSearchResponses;
    }

    private BigDecimal changeBigDecimal(String bigDecimal){
        return new BigDecimal(bigDecimal);
    }
}
