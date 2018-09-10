package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.SellerSearchResponse;
import com.netx.searchengine.query.SellerSearchQuery;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CloudZou on 2/9/18.
 */
@Service
public class SellerSearchService extends BaseSearchService{
    private Logger logger = LoggerFactory.getLogger(SellerSearchService.class);

    public List<SellerSearchResponse> querySellers(SellerSearchQuery sellerSearchQuery) {
        if(getClient()==null){
            return new ArrayList<>();
        }
        level =0;
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_SELLER_NAME).setTypes(SearchField.TYPE_SELLER_NAME);
        searchBuilder.setFrom(sellerSearchQuery.getFrom())
                .setSize(sellerSearchQuery.getPageSize())
                .setExplain(false);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        //商家名称模糊查询
        if (StringUtils.isNotBlank(sellerSearchQuery.getName())) {
            queryBuilder.must(QueryBuilders.wildcardQuery(SearchField.QUERY_FIELD_SELLER_NAME, "*"+sellerSearchQuery.getName()+"*"));
        }

        //是否搜索在线
        if(sellerSearchQuery.getLogin()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isLogin",sellerSearchQuery.getLogin()?1:0));
        }

        //是否搜索支持网信
        if(sellerSearchQuery.getHoldCredit()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isHoldCredit",sellerSearchQuery.getHoldCredit()?1:0));
        }

        if(sellerSearchQuery.getFirstRate() != null){
            queryBuilder.must(QueryBuilders.matchQuery("firstRate", sellerSearchQuery.getFirstRate()));
        }

        if(StringUtils.isNotBlank(sellerSearchQuery.getUserId())){
            queryBuilder.must(QueryBuilders.wildcardQuery(SearchField.QUERY_FIELD_SELLER_USERID_NAME, "*"+sellerSearchQuery.getUserId()+"*"));
        }

        //根据类别id搜索
        if(sellerSearchQuery.getCategoryId().size()>0){
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_SELLER_CATEGORYID_NAME,sellerSearchQuery.getCategoryId()));
        }

        //根据省份搜索
        if(sellerSearchQuery.getProvinceCode().size()>0){
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_SELLER_PROVINCECODE_NAME,sellerSearchQuery.getProvinceCode()));
        }

        //根据市搜索
        if(sellerSearchQuery.getCityCode().size()>0){
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_SELLER_CITYCODE_NAME,sellerSearchQuery.getCityCode()));
        }

        //根据区/县搜索
        if(sellerSearchQuery.getAreaCode().size()>0){
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_SELLER_AREACODE_NAME,sellerSearchQuery.getAreaCode()));
        }

        //浏览数搜索
        addVisitNum(queryBuilder,sellerSearchQuery.getMinVisitNum(),sellerSearchQuery.getMaxVisitNum());

        //添加距离之前排序条件
        if(sellerSearchQuery.getFristAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:sellerSearchQuery.getFristAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    level++;
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        //距离搜索
        if (sellerSearchQuery.getDistanceUnit() != null && sellerSearchQuery.getCenterGeoPoint() != null) {
            double lat = sellerSearchQuery.getCenterGeoPoint().getLat();
            double lon = sellerSearchQuery.getCenterGeoPoint().getLon();
            double distance = sellerSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_SELLER_LATLON_NAME)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);


            searchBuilder.setPostFilter(distanceQueryBuilder);
//            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_SELLER_LATLON_NAME, lat, lon);
//            sort.unit(sellerSearchQuery.getDistanceUnit());
//            sort.order(SortOrder.ASC);
//            sort.point(lat,lon);
//            searchBuilder.addSort(sort);
            addScriptGeo(searchBuilder,lon,lat,SearchField.QUERY_FIELD_SELLER_LATLON_NAME,sellerSearchQuery.getDistanceUnit());
        }

        //添加距离之后排序条件
        if(sellerSearchQuery.getLastAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:sellerSearchQuery.getLastAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        SearchResponse response = searchBuilder.setQuery(queryBuilder).execute().actionGet();

        logger.info(searchBuilder.toString());
        return processSearchResponse(response);
    }

    private void addVisitNum(BoolQueryBuilder queryBuilder,Integer min,Integer max){
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(SearchField.QUERY_FIELD_SELLER_VISITNUM_NAME);
        Boolean flag = false;
        if(min!=null){
            rangeQueryBuilder.gte(min);
            flag=true;
        }
        if(max!=null){
            rangeQueryBuilder.lte(max);
        }
        if(flag){
            queryBuilder.must(rangeQueryBuilder);
        }
    }

    private List<SellerSearchResponse> processSearchResponse(SearchResponse response){
        List<SellerSearchResponse> sellerSearchResponses = new ArrayList<>();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> resultMap = null;
            try {
                resultMap = searchHit.getSourceAsMap();
                SellerSearchResponse sellerInfo = new SellerSearchResponse();
                if(resultMap.get("location")!=null){
                    if(StringUtils.isNotBlank(resultMap.get("location").toString())){
                        String[] geo = resultMap.get("location").toString().split(",");
                        if(geo.length==2){
                            BigDecimal lon = new BigDecimal(geo[1]);
                            BigDecimal lat = new BigDecimal(geo[0]);
                            if(checkLocationStr(lon) && checkLocationStr(lat)){
                                sellerInfo.setLon(lon);
                                sellerInfo.setLat(lat);
                                if (searchHit.getSortValues().length > 0) {
                                    sellerInfo.setDistance(getDistance(searchHit));
                                }
                            }
                        }
                    }
                }

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_ID_NAME) != null)
                    sellerInfo.setId(resultMap.get(SearchField.QUERY_FIELD_SELLER_ID_NAME).toString());

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_NAME) != null)
                    sellerInfo.setName(resultMap.get(SearchField.QUERY_FIELD_SELLER_NAME).toString());

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_USERID_NAME) != null)
                    sellerInfo.setUserId(resultMap.get(SearchField.QUERY_FIELD_SELLER_USERID_NAME).toString());

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_CATEGORYID_NAME) != null)
                    sellerInfo.setCategoryIds((List<String>)(List) resultMap.get(SearchField.QUERY_FIELD_SELLER_CATEGORYID_NAME));

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_CATEGORYNAME_NAME) != null)
                    sellerInfo.setCategoryNames((List<String>)(List) resultMap.get(SearchField.QUERY_FIELD_SELLER_CATEGORYNAME_NAME));

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_CATEGORYPARENTID_NAME) != null)
                    sellerInfo.setCategoryParentIds((List<String>)(List) resultMap.get(SearchField.QUERY_FIELD_SELLER_CATEGORYPARENTID_NAME));

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_CITYCODE_NAME) != null)
                    sellerInfo.setCityCode(resultMap.get(SearchField.QUERY_FIELD_SELLER_CITYCODE_NAME).toString());

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_LOGOIMAGES_NAME) != null)
                    sellerInfo.setLogoImagesUrl((List<String>)(List) resultMap.get(SearchField.QUERY_FIELD_SELLER_LOGOIMAGES_NAME));

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_CREATETIME_NAME) != null)
                    sellerInfo.setCreateTime(StringToDate(resultMap.get(SearchField.QUERY_FIELD_SELLER_CREATETIME_NAME).toString(),true));

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_AREACODE_NAME) != null)
                    sellerInfo.setAreaCode(resultMap.get(SearchField.QUERY_FIELD_SELLER_AREACODE_NAME).toString());

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_ADDRCOUNTRY_NAME) != null)
                    sellerInfo.setAddrCountry(resultMap.get(SearchField.QUERY_FIELD_SELLER_ADDRCOUNTRY_NAME).toString());

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_ADDRCOUNTRYDETAIL_NAME) != null)
                    sellerInfo.setAddrDetail(resultMap.get(SearchField.QUERY_FIELD_SELLER_ADDRCOUNTRYDETAIL_NAME).toString());

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_SELLERDESC_NAME) != null)
                    sellerInfo.setDesc(resultMap.get(SearchField.QUERY_FIELD_SELLER_SELLERDESC_NAME).toString());

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_VISITNUM_NAME) != null)
                    sellerInfo.setVisitNum(Integer.parseInt(resultMap.get(SearchField.QUERY_FIELD_SELLER_VISITNUM_NAME).toString()));

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_CREDIT_NAME) != null)
                    sellerInfo.setCredit(Integer.parseInt(resultMap.get(SearchField.QUERY_FIELD_SELLER_CREDIT_NAME).toString()));

                if(resultMap.get(SearchField.QUERY_FIELD_SELLER_STATUS_NAME) != null)
                    sellerInfo.setStatus(Integer.parseInt(resultMap.get(SearchField.QUERY_FIELD_SELLER_STATUS_NAME).toString()));

                if(resultMap.get("isHoldCredit")!=null)
                    sellerInfo.setHoldCredit(strToBoolean(resultMap.get("isHoldCredit").toString()));

                if(resultMap.get("firstRate") != null){
                    sellerInfo.setFirstRate(new BigDecimal(resultMap.get("firstRate").toString()));
                }
                if(resultMap.get("nickname") != null){
                    sellerInfo.setNickname(resultMap.get("nickname").toString());
                }
                if(resultMap.get("payStatus") != null){
                    sellerInfo.setPayStatus(Integer.parseInt(resultMap.get("payStatus").toString()));
                }
                if(resultMap.get("volume") != null){
                    sellerInfo.setVolume(Math.round(Double.parseDouble(resultMap.get("volume").toString())));
                }
                sellerSearchResponses.add(sellerInfo);

            } catch (Exception e) {
                logger.error("error at processNewsList, message:" + resultMap, e);
            }
        }
        return sellerSearchResponses;
    }
}
