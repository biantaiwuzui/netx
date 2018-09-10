package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.NetxWorthSearchService;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.DemandSearchResponse;
import com.netx.searchengine.query.DemandSearchQuery;
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
public class DemandSearchService extends NetxWorthSearchService{
    private Logger logger = LoggerFactory.getLogger(DemandSearchService.class);

    public List<DemandSearchResponse> queryDemands(DemandSearchQuery demandSearchQuery) {
        level = 0;
        if(getClient()==null){
            return new ArrayList<>();
        }
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_DEMAND_NAME).setTypes(SearchField.TYPE_DENABD_NAME);
        searchBuilder.setFrom(demandSearchQuery.getFrom())
                .setSize(demandSearchQuery.getPageSize())
                .setExplain(false);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        int [] queryTerm={1,3,4,5};
        if(demandSearchQuery.getLogin()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isLogin",demandSearchQuery.getLogin()?1:0));
        }

        if (StringUtils.isNotBlank(demandSearchQuery.getTitle())) {
            queryBuilder.must(QueryBuilders.wildcardQuery("title","*"+demandSearchQuery.getTitle()+"*"));
        }
        if (StringUtils.isNotBlank(demandSearchQuery.getUserId())) {
            queryBuilder.must(QueryBuilders.matchQuery("userId", demandSearchQuery.getUserId()));
        }

        if(demandSearchQuery.getDemandLabels()!=null && demandSearchQuery.getDemandLabels().size()>0){
            addTermShoule(queryBuilder,"demandLabels",demandSearchQuery.getDemandLabels());
        }

        if(demandSearchQuery.getStatus()!=null){
            queryBuilder.must(QueryBuilders.termQuery("status", demandSearchQuery.getStatus()));
        } else {
                queryBuilder.must(QueryBuilders.termsQuery("status",queryTerm));
            }


        if(demandSearchQuery.getDemandType() != null){
            queryBuilder.must(QueryBuilders.termQuery("demandType", demandSearchQuery.getDemandType()));
        }

        if(demandSearchQuery.getCreditSum()!=null){
            queryBuilder.must(QueryBuilders.termQuery("creditSum", demandSearchQuery.getCreditSum()));
        }

        //添加距离之前排序条件
        if(demandSearchQuery.getFristAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:demandSearchQuery.getFristAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    level++;
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        if (demandSearchQuery.getDistanceUnit() != null && demandSearchQuery.getCenterGeoPoint() != null) {
            Double lat = demandSearchQuery.getCenterGeoPoint().getLat();
            Double lon = demandSearchQuery.getCenterGeoPoint().getLon();
            Double distance = demandSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_SELLER_LATLON_NAME)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);

            searchBuilder.setPostFilter(distanceQueryBuilder);
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_SELLER_LATLON_NAME, lat, lon);
            sort.unit(demandSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat,lon);
            searchBuilder.addSort(sort);
        }

        //添加距离之后排序条件
        if(demandSearchQuery.getLastAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:demandSearchQuery.getLastAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        super.printfSearch(queryBuilder);

        searchBuilder.setQuery(queryBuilder);
        SearchResponse response = searchBuilder.execute().actionGet();
        return processSearchResponse(response);
    }

    private List<DemandSearchResponse> processSearchResponse(SearchResponse response){
        List<DemandSearchResponse> demandSearchResponseList = new ArrayList<>();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> resultMap = null;
            try {
                resultMap = searchHit.getSourceAsMap();
                DemandSearchResponse  demandSearchResponse = new DemandSearchResponse();
                if (searchHit.getSortValues().length > 0) {
                    BigDecimal geoDis = new BigDecimal((Double) searchHit.getSortValues()[level]);
                    BigDecimal distance = geoDis.setScale(3, BigDecimal.ROUND_HALF_UP);
                    demandSearchResponse.setDistance(distance.doubleValue());
                }
                if(resultMap.get("id") != null)
                    demandSearchResponse.setId(resultMap.get("id").toString());

                if(resultMap.get("userId") != null)
                    demandSearchResponse.setUserId(resultMap.get("userId").toString());

                if(resultMap.get("title") != null)
                    demandSearchResponse.setTitle(resultMap.get("title").toString());

                if(resultMap.get("demandType") != null)
                    demandSearchResponse.setDemandType(Integer.parseInt(resultMap.get("demandType").toString()));

                if(resultMap.get("demandLabels") != null)
                    demandSearchResponse.setDemandLabels((List<String>)(List)resultMap.get("demandLabels"));

                if(resultMap.get("isOpenEnded") != null)
                    demandSearchResponse.setOpenEnded(changeBoolean(resultMap.get("isOpenEnded").toString()));

                if(resultMap.get("startAt") != null)
                    demandSearchResponse.setStartAt(StringToDate(resultMap.get("startAt").toString(),true));

                if(resultMap.get("endAt") != null)
                    demandSearchResponse.setEndAt(StringToDate(resultMap.get("endAt").toString(), true));

                if(resultMap.get("about") != null)
                    demandSearchResponse.setAbout(resultMap.get("about").toString());

                if(resultMap.get("amount") != null)
                    demandSearchResponse.setAmount(Integer.parseInt(resultMap.get("amount").toString()));

                if(resultMap.get("unit") != null)
                    demandSearchResponse.setUnit(resultMap.get("unit").toString());

                if(resultMap.get("obj") != null)
                    demandSearchResponse.setObj(Integer.parseInt(resultMap.get("obj").toString()));

                if(resultMap.get("objList") != null)
                    demandSearchResponse.setObjList((List<String>)(List)resultMap.get("objList"));

                if(resultMap.get("address") != null)
                    demandSearchResponse.setAddress(resultMap.get("address").toString());

                if(resultMap.get("description") != null)
                    demandSearchResponse.setDescription(resultMap.get("description").toString());

                if(resultMap.get("imagesUrl") != null)
                    demandSearchResponse.setImagesUrl(resultMap.get("imagesUrl").toString());

                if(resultMap.get("detailsImagesUrl") != null)
                    demandSearchResponse.setDetailsImagesUrl(resultMap.get("detailsImagesUrl").toString());

                if(resultMap.get("orderIds") != null)
                    demandSearchResponse.setOrderIds(resultMap.get("orderIds").toString());

                if(resultMap.get("orderPrice") != null)
                    demandSearchResponse.setOrderPrice(changeBigDecimal(resultMap.get("orderPrice").toString()));

                if(resultMap.get("isPickUp") != null)
                    demandSearchResponse.setPickUp(changeBoolean(resultMap.get("isPickUp").toString()));

                if(resultMap.get("wage") != null)
                    demandSearchResponse.setWage(changeBigDecimal(resultMap.get("wage").toString()));

                if(resultMap.get("isEachWage") != null)
                    demandSearchResponse.setEachWage(changeBoolean(resultMap.get("isEachWage").toString()));

                if(resultMap.get("bail") != null)
                    demandSearchResponse.setBail(changeBigDecimal(resultMap.get("bail").toString()));

                if(resultMap.get("status") != null)
                    demandSearchResponse.setStatus(Integer.parseInt(resultMap.get("status").toString()));

                if(resultMap.get("isPay") != null)
                    demandSearchResponse.setPay(changeBoolean(resultMap.get("isPay").toString()));

                if(resultMap.get("nickname") != null){
                    demandSearchResponse.setNickname(resultMap.get("nickname").toString());
                }

                if(resultMap.get("nickname") != null){
                    demandSearchResponse.setNickname(resultMap.get("nickname").toString());
                }

                if(resultMap.get("birthday") != null){
                    demandSearchResponse.setBirthday(StringToDate(resultMap.get("birthday").toString(),false));
                }

                if(resultMap.get("sex") != null){
                    demandSearchResponse.setSex(resultMap.get("sex").toString());
                }

                if(resultMap.get("mobile") != null){
                    demandSearchResponse.setMobile(resultMap.get("mobile").toString());
                }

                if(resultMap.get("score") != null){
                    demandSearchResponse.setScore(new BigDecimal(resultMap.get("score").toString()));
                }

                if(resultMap.get("credit") != null){
                    demandSearchResponse.setCredit(Integer.parseInt(resultMap.get("credit").toString()));
                }

                if(resultMap.get("createTime") != null){
                    demandSearchResponse.setCreateTime(StringToDate(resultMap.get("createTime").toString(), true));
                }

                if(resultMap.get("lv") != null){
                    demandSearchResponse.setLv(Integer.parseInt(resultMap.get("lv").toString()));
                }

                if(resultMap.get("creditSum") != null){
                    demandSearchResponse.setCreditSum(Integer.parseInt(resultMap.get("creditSum").toString()));
                }

                if(resultMap.get("count") != null){
                    demandSearchResponse.setCount(Integer.parseInt(resultMap.get("count").toString()));
                }

                demandSearchResponseList.add(demandSearchResponse);

            } catch (Exception e) {
                logger.error("error at processNewsList, message:" + resultMap, e);
            }
        }
        return demandSearchResponseList;
    }

    private Boolean changeBoolean(String b){
        return b.equals("1");
    }

    private BigDecimal changeBigDecimal(String bigDecimal){
        return new BigDecimal(bigDecimal);
    }
}
