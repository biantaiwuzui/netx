package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.CreditSearchResponse;
import com.netx.searchengine.query.CreditSearchQuery;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CreditSearchService extends BaseSearchService{
    private Logger logger= LoggerFactory.getLogger(CreditSearchService.class);

    public List<CreditSearchResponse> queryCredits(CreditSearchQuery creditSearchQuery){
        if(getClient()==null){
            return new ArrayList<>();
        }
        SearchRequestBuilder searchRequestBuilder=client.prepareSearch( SearchField.INDEX_CREDIT_NAME).setTypes(SearchField.TYPE_CREDIT_NAME);
        searchRequestBuilder.setFrom(creditSearchQuery.getFrom()).
                setSize(creditSearchQuery.getPageSize()).setExplain(false);

        BoolQueryBuilder queryBuilder= QueryBuilders.boolQuery();
        if(StringUtils.isNotEmpty(creditSearchQuery.getName())){
            queryBuilder.must(QueryBuilders.matchQuery( SearchField.QUERY_FIELD_CREDIT_NAME,creditSearchQuery.getName()));
        }
        if(StringUtils.isNotEmpty(creditSearchQuery.getTag())){
            queryBuilder.must(QueryBuilders.matchQuery( SearchField.QUERY_FIELD_CREDIT_TAG ,creditSearchQuery.getTag()));
        }
        if(creditSearchQuery.getFaceValue()!=null){
            queryBuilder.must(QueryBuilders.matchQuery( SearchField.QUERY_FIELD_CREDIT_FACEVALUE ,creditSearchQuery.getFaceValue()));
        }
        if(creditSearchQuery.getBuyFactor()!=null){
            queryBuilder.must(QueryBuilders.matchQuery( SearchField.QUERY_FIELD_CREDIT_BUYFACTOR,creditSearchQuery.getBuyFactor()));
        }
        if(creditSearchQuery.getRoyaltyRatio()!=null){
            queryBuilder.must(QueryBuilders.matchQuery( SearchField.QUERY_FIELD_CREDIT_ROYALTYRATIO,creditSearchQuery.getRoyaltyRatio()));
        }
        if(creditSearchQuery.getStatus()!=null){
            if(creditSearchQuery.getStatus()==1){//status = 1,2,4,5,6,8
                queryBuilder.must(QueryBuilders.termsQuery( SearchField.QUERY_FIELD_CREDIT_STATUS,new Integer[]{1,2,4,5,6,8}));
            }else{
                queryBuilder.must(QueryBuilders.matchQuery( SearchField.QUERY_FIELD_CREDIT_STATUS,7));
            }
        }

        if (creditSearchQuery.getDistanceUnit() != null && creditSearchQuery.getCenterGeoPoint() != null) {
            double lat = creditSearchQuery.getCenterGeoPoint().getLat();
            double lon = creditSearchQuery.getCenterGeoPoint().getLon();
            double distance = creditSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery( SearchField.QUERY_FIELD_CREIDT_LOCATION)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);


            searchRequestBuilder.setPostFilter(distanceQueryBuilder);
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort( SearchField.QUERY_FIELD_CREIDT_LOCATION, lat, lon);
            sort.unit(creditSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat,lon);
            searchRequestBuilder.addSort(sort);
        }

        searchRequestBuilder.setQuery(queryBuilder);
        SearchResponse searchResponse=searchRequestBuilder.execute().actionGet();
        return processResponse(searchResponse);

    }

    public List<CreditSearchResponse> processResponse(SearchResponse searchResponse){
        List<CreditSearchResponse> responses=new ArrayList<>();
        CreditSearchResponse creditSearchResponse=null;
        for(SearchHit searchHit:searchResponse.getHits()){
            Map<String,Object> resultMap=null;
            resultMap=searchHit.getSourceAsMap();
            double distance=0;
            if(searchHit.getSortValues().length>0){
                BigDecimal geoDis = new BigDecimal((Double) searchHit.getSortValues()[0]);
                distance = geoDis.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            creditSearchResponse=new CreditSearchResponse();
            creditSearchResponse.setDistance(distance);
            if(resultMap.get("id")!=null){
                creditSearchResponse.setId(resultMap.get("id").toString());
            }
            if(resultMap.get("userId")!=null){
                creditSearchResponse.setUserId(resultMap.get("userId").toString());
            }
            if(resultMap.get("name")!=null){
                creditSearchResponse.setName(resultMap.get("name").toString());
            }
            if(resultMap.get("tagIds")!=null){
                creditSearchResponse.setTagIds(resultMap.get("tagIds").toString());
            }
            if(resultMap.get("frontStyle")!=null){
                creditSearchResponse.setFrontStyle(resultMap.get("frontStyle").toString());
            }
            if(resultMap.get("backStyle")!=null){
                creditSearchResponse.setBackStyle(resultMap.get("backStyle").toString());
            }
            if(resultMap.get("isFitScopeOne")!=null){
                creditSearchResponse.setFitScopeOne((Boolean) resultMap.get("isFitScopeOne"));
            }
            if(resultMap.get("isFitScopeTwo")!=null){
                creditSearchResponse.setFitScopeTwo((Boolean)resultMap.get("isFitScopeTwo"));
            }
            if(resultMap.get("sellerIds")!=null){
                creditSearchResponse.setSellerIds(resultMap.get("sellerIds").toString());
            }

            if(resultMap.get("isFitScopeThree")!=null){
                creditSearchResponse.setFitScopeThree((Boolean)resultMap.get("isFitScopeThree"));
            }
            if(resultMap.get("importNetNum")!=null){
                creditSearchResponse.setImportNetNum(resultMap.get("importNetNum").toString());
            }
            if(resultMap.get("importName")!=null){
                creditSearchResponse.setImportName(resultMap.get("importName").toString());
            }
            if(resultMap.get("importPhone")!=null){
                creditSearchResponse.setImportPhone(resultMap.get("importPhone").toString());
            }
            if(resultMap.get("importIdcard")!=null){
                creditSearchResponse.setImportIdcard(resultMap.get("importIdcard").toString());
            }
            if(resultMap.get("releaseNum")!=null){
                creditSearchResponse.setReleaseNum(Integer.parseInt(resultMap.get("releaseNum").toString()));
            }
            if(resultMap.get("releaseTime")!=null){
                creditSearchResponse.setReleaseTime((Date) resultMap.get("releaseTime"));
            }
            if(resultMap.get("successTime")!=null){
                creditSearchResponse.setSuccessTime((Date)resultMap.get("successTime"));
            }
            if(resultMap.get("faceValue")!=null){
                creditSearchResponse.setFaceValue(Integer.parseInt(resultMap.get("faceValue").toString()));
            }
            if(resultMap.get("applyPrice")!=null){
                creditSearchResponse.setApplyPrice(Long.parseLong(resultMap.get("applyPrice").toString()));
            }
            if(resultMap.get("buyFactor")!=null){
                creditSearchResponse.setBuyFactor(new BigDecimal(resultMap.get("buyFactor").toString()));
            }
            if(resultMap.get("growthRate")!=null){
                creditSearchResponse.setGrowthRate(Integer.parseInt(resultMap.get("growthRate").toString()));
            }
            if(resultMap.get("royaltyRatio")!=null){
                creditSearchResponse.setRoyaltyRatio(Integer.parseInt(resultMap.get("royaltyRatio").toString()));
            }
            if(resultMap.get("remark")!=null){
                creditSearchResponse.setRemark(resultMap.get("remark").toString());
            }
            if(resultMap.get("buyAmount")!=null){
                creditSearchResponse.setBuyAmount(Long.parseLong(resultMap.get("buyAmount").toString()));
            }
            if(resultMap.get("payAmount")!=null){
                creditSearchResponse.setPayAmount(Long.parseLong(resultMap.get("payAmount").toString()));
            }
            if(resultMap.get("status")!=null){
                creditSearchResponse.setStatus(Integer.parseInt(resultMap.get("status").toString()));
            }
            if(resultMap.get("createTime")!=null){
                creditSearchResponse.setCreateTime(super.StringToDate(resultMap.get("createTime").toString(),true));
            }
            responses.add(creditSearchResponse);
        }
        return responses;
    }

//    public void queryCredits() {
//
//    }
}
