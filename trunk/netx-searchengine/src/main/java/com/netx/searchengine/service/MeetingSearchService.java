package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.NetxWorthSearchService;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.MeetingSearchResponse;
import com.netx.searchengine.query.MeetingSearchQuery;
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
public class MeetingSearchService extends NetxWorthSearchService {
    private Logger logger = LoggerFactory.getLogger(MeetingSearchService.class);

    public List<MeetingSearchResponse> queryMeetings(MeetingSearchQuery meetingSearchQuery) {
        if(getClient()==null){
            return new ArrayList<>();
        }
        level = 0;
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_MEETING_NAME).setTypes(SearchField.TYPE_MEETING_NAME);
        searchBuilder.setFrom(meetingSearchQuery.getFrom())
                .setSize(meetingSearchQuery.getPageSize())
                .setExplain(false);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
          int [] queryTerm={0,1,4,5};
        if(meetingSearchQuery.getLogin()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isLogin",meetingSearchQuery.getLogin()?1:0));
        }

        if (StringUtils.isNotBlank(meetingSearchQuery.getTitle())) {
            queryBuilder.must(QueryBuilders.wildcardQuery("title","*"+meetingSearchQuery.getTitle()+"*"));
        }

        if (StringUtils.isNotBlank(meetingSearchQuery.getUserId())) {
            queryBuilder.must(QueryBuilders.matchQuery("userId", meetingSearchQuery.getUserId()));
        }

        if(meetingSearchQuery.getMeetingLabels()!=null && meetingSearchQuery.getMeetingLabels().size()>0){
            addTermShoule(queryBuilder,"meetingLabels",meetingSearchQuery.getMeetingLabels());
        }

        if(meetingSearchQuery.getStatus()!=null){
            queryBuilder.must(QueryBuilders.termQuery("status", meetingSearchQuery.getStatus()));
        }else {
            queryBuilder.must(QueryBuilders.termsQuery("status",queryTerm));
        }

        if(meetingSearchQuery.getMeetingType() != null){
            queryBuilder.must(QueryBuilders.termQuery("meetingType", meetingSearchQuery.getMeetingType()));
        }

        if(meetingSearchQuery.getCreditSum()!=null){
            queryBuilder.must(QueryBuilders.termQuery("creditSum", meetingSearchQuery.getCreditSum()));
        }

        //添加距离之前排序条件
        if(meetingSearchQuery.getFristAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:meetingSearchQuery.getFristAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    level++;
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        if (meetingSearchQuery.getDistanceUnit() != null && meetingSearchQuery.getCenterGeoPoint() != null) {
            Double lat = meetingSearchQuery.getCenterGeoPoint().getLat();
            Double lon = meetingSearchQuery.getCenterGeoPoint().getLon();
            Double distance = meetingSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_SELLER_LATLON_NAME)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);

            searchBuilder.setPostFilter(distanceQueryBuilder);
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_SELLER_LATLON_NAME, lat, lon);
            sort.unit(meetingSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat,lon);
            searchBuilder.addSort(sort);
        }

        //添加距离之后排序条件
        if(meetingSearchQuery.getLastAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:meetingSearchQuery.getLastAscQueries()){
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

    private List<MeetingSearchResponse> processSearchResponse(SearchResponse response){
        List<MeetingSearchResponse> meetingSearchResponses = new ArrayList<>();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> resultMap = null;
            try {
                resultMap = searchHit.getSourceAsMap();
                MeetingSearchResponse  meetingSearchResponse = new MeetingSearchResponse();
                if(resultMap.get("location")!=null){
                    if(StringUtils.isNotBlank(resultMap.get("location").toString())){
                        String[] geo = resultMap.get("location").toString().split(",");
                        if(geo.length==2){
                            BigDecimal lon = new BigDecimal(geo[1]);
                            BigDecimal lat = new BigDecimal(geo[0]);
                            if(checkLocationStr(lon) && checkLocationStr(lat)){
                                if (searchHit.getSortValues().length > 0) {
                                    meetingSearchResponse.setDistance(getDistance(searchHit));
                                }
                            }
                        }
                    }
                }
                if(resultMap.get("id") != null)
                    meetingSearchResponse.setId(resultMap.get("id").toString());

                if(resultMap.get("userId") != null)
                    meetingSearchResponse.setUserId(resultMap.get("userId").toString());

                if(resultMap.get("title") != null)
                    meetingSearchResponse.setTitle(resultMap.get("title").toString());

                if(resultMap.get("meetingType") != null)
                    meetingSearchResponse.setMeetingType(Integer.parseInt(resultMap.get("meetingType").toString()));

                if(resultMap.get("meetingLabels") != null)
                    meetingSearchResponse.setMeetingLabels((List<String>)(List)resultMap.get("meetingLabels"));

                if(resultMap.get("startedAt") != null)
                    meetingSearchResponse.setStartedAt(StringToDate(resultMap.get("startedAt").toString(),true));

                if(resultMap.get("endAt") != null)
                    meetingSearchResponse.setEndAt(StringToDate(resultMap.get("endAt").toString(),true));

                if(resultMap.get("regStopAt") != null)
                    meetingSearchResponse.setRegStopAt(StringToDate(resultMap.get("regStopAt").toString(),true));

                if(resultMap.get("obj") != null)
                    meetingSearchResponse.setObj(Integer.parseInt(resultMap.get("obj").toString()));

                if(resultMap.get("objList") != null)
                    meetingSearchResponse.setObjList((List<String>)(List)(resultMap.get("objList")));

                if(resultMap.get("amount") != null)
                    meetingSearchResponse.setAmount(changeBigDecimal(resultMap.get("amount").toString()));

                if(resultMap.get("address") != null)
                    meetingSearchResponse.setAddress(resultMap.get("address").toString());

                if(resultMap.get("orderIds") != null)
                    meetingSearchResponse.setOrderIds(resultMap.get("orderIds").toString());

                if(resultMap.get("orderPrice") != null)
                    meetingSearchResponse.setOrderPrice(changeBigDecimal(resultMap.get("orderPrice").toString()));

                if(resultMap.get("description") != null)
                    meetingSearchResponse.setDescription(resultMap.get("description").toString());

                if(resultMap.get("posterImagesUrl") != null)
                    meetingSearchResponse.setPosterImagesUrl(resultMap.get("posterImagesUrl").toString());

                if(resultMap.get("meetingImagesUrl") != null)
                    meetingSearchResponse.setMeetingImagesUrl(resultMap.get("meetingImagesUrl").toString());

                if(resultMap.get("ceil") != null)
                    meetingSearchResponse.setCeil(Integer.parseInt(resultMap.get("ceil").toString()));

                if(resultMap.get("floor") != null)
                    meetingSearchResponse.setFloor(Integer.parseInt(resultMap.get("floor").toString()));

                if(resultMap.get("status") != null)
                    meetingSearchResponse.setStatus(Integer.parseInt(resultMap.get("status").toString()));

                if(resultMap.get("feeNotEnough") != null)
                    meetingSearchResponse.setFeeNotEnough(Integer.parseInt(resultMap.get("feeNotEnough").toString()));

                if(resultMap.get("isConfirm") != null)
                    meetingSearchResponse.setConfirm(changeBoolean(resultMap.get("isConfirm").toString()));

                if(resultMap.get("lockVersion") != null)
                    meetingSearchResponse.setLockVersion(Integer.parseInt(resultMap.get("lockVersion").toString()));

                if(resultMap.get("allRegisterAmount")!=null)
                    meetingSearchResponse.setAllRegisterAmount(changeBigDecimal(resultMap.get("allRegisterAmount").toString()));

                if(resultMap.get("balance")!=null)
                    meetingSearchResponse.setBalance(changeBigDecimal(resultMap.get("balance").toString()));

                if(resultMap.get("isBalancePay")!=null)
                    meetingSearchResponse.setBalancePay(changeBoolean(resultMap.get("isBalancePay").toString()));

                if(resultMap.get("payFrom")!=null)
                    meetingSearchResponse.setPayFrom(resultMap.get("payFrom").toString());

                if(resultMap.get("nickname") != null){
                    meetingSearchResponse.setNickname(resultMap.get("nickname").toString());
                }

                if(resultMap.get("birthday") != null){
                    meetingSearchResponse.setBirthday(StringToDate(resultMap.get("birthday").toString(),false));
                }

                if(resultMap.get("sex") != null){
                    meetingSearchResponse.setSex(resultMap.get("sex").toString());
                }

                if(resultMap.get("mobile") != null){
                    meetingSearchResponse.setMobile(resultMap.get("mobile").toString());
                }

                if(resultMap.get("score") != null){
                    meetingSearchResponse.setScore(new BigDecimal(resultMap.get("score").toString()));
                }

                if(resultMap.get("credit") != null){
                    meetingSearchResponse.setCredit(Integer.parseInt(resultMap.get("credit").toString()));
                }

                if(resultMap.get("createTime") != null){
                    meetingSearchResponse.setCreateTime(StringToDate(resultMap.get("createTime").toString(), true));
                }

                if(resultMap.get("lv") != null){
                    meetingSearchResponse.setLv(Integer.parseInt(resultMap.get("lv").toString()));
                }

                if(resultMap.get("creditSum") != null){
                    meetingSearchResponse.setCreditSum(Integer.parseInt(resultMap.get("creditSum").toString()));
                }
                if(resultMap.get("regCount") != null){
                    meetingSearchResponse.setCount(Integer.parseInt(resultMap.get("regCount").toString()));
                }

                meetingSearchResponses.add(meetingSearchResponse);

            } catch (Exception e) {
                logger.error("error at processNewsList, message:" + resultMap, e);
            }
        }
        return meetingSearchResponses;
    }

    private BigDecimal changeBigDecimal(String bigDecimal){
        return new BigDecimal(bigDecimal);
    }

    private Boolean changeBoolean(String b){
        return b.equals("1");
    }
}
