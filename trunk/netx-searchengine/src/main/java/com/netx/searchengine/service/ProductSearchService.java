package com.netx.searchengine.service;

import com.netx.searchengine.BaseSearchService;
import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.SearchField;
import com.netx.searchengine.model.ProductSearchResponse;
import com.netx.searchengine.query.ProductSearchQuery;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
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
 * Created by CloudZou on 2/10/18.
 */
@Service
public class ProductSearchService extends BaseSearchService{
    private Logger logger = LoggerFactory.getLogger(ProductSearchService.class);

    public List<ProductSearchResponse> queryProducts(ProductSearchQuery productSearchQuery) {
        if(getClient()==null){
            return new ArrayList<>();
        }
        level =0;
        SearchRequestBuilder searchBuilder = client.prepareSearch(SearchField.INDEX_PRODUCT_NAME).setTypes(SearchField.TYPE_PRODUCT_NAME);
        searchBuilder.setFrom(productSearchQuery.getFrom())
                .setSize(productSearchQuery.getPageSize())
                .setExplain(false);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(productSearchQuery.getName())) {
            queryBuilder.must(QueryBuilders.wildcardQuery("name", "*"+productSearchQuery.getName()+"*"));
        }
        //根据类别id搜索
        if(productSearchQuery.getCategoryId()!= null && productSearchQuery.getCategoryId().size()>0){
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_SELLER_CATEGORYID_NAME,productSearchQuery.getCategoryId()));
        }

        //是否搜索在线
        if(productSearchQuery.getLogin()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isLogin",productSearchQuery.getLogin()?1:0));
        }

        //是否搜索支持网信
        if(productSearchQuery.getHoldCredit()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isHoldCredit",productSearchQuery.getHoldCredit()?1:0));
        }

        //根据省份搜索
        if(productSearchQuery.getProvinceCode()!= null && productSearchQuery.getProvinceCode().size()>0){
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_SELLER_PROVINCECODE_NAME,productSearchQuery.getProvinceCode()));
        }

        //根据市搜索
        if(productSearchQuery.getCityCode()!= null && productSearchQuery.getCityCode().size()>0){
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_SELLER_CITYCODE_NAME,productSearchQuery.getCityCode()));
        }

        //根据区/县搜索
        if(productSearchQuery.getAreaCode()!= null && productSearchQuery.getAreaCode().size()>0){
            queryBuilder.must(QueryBuilders.termsQuery(SearchField.QUERY_FIELD_SELLER_AREACODE_NAME,productSearchQuery.getAreaCode()));
        }
        if (StringUtils.isNotEmpty(productSearchQuery.getMerchantId())) {
            queryBuilder.must(QueryBuilders.matchQuery("merchantId", productSearchQuery.getMerchantId()));
        }
        if(productSearchQuery.getDelivery()!=null){
            queryBuilder.must(QueryBuilders.matchQuery("isDelivery",productSearchQuery.getDelivery()?0:1));
        }
        if(productSearchQuery.getOnlineStatus()!=null) {
            queryBuilder.must(QueryBuilders.matchQuery("onlineStatus",productSearchQuery.getOnlineStatus()?1:2));
        }

        if(productSearchQuery.getFirstRate() != null){
            queryBuilder.must(QueryBuilders.matchQuery("firstRate", productSearchQuery.getFirstRate()));
        }

        addRangeNum(queryBuilder,"marketPrice",productSearchQuery.getMinPrice(),productSearchQuery.getMaxPrice());

        //添加距离之前排序条件
        if(productSearchQuery.getFristAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:productSearchQuery.getFristAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    level++;
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }

        if (productSearchQuery.getDistanceUnit() != null && productSearchQuery.getCenterGeoPoint() != null) {
            double lat = productSearchQuery.getCenterGeoPoint().getLat();
            double lon = productSearchQuery.getCenterGeoPoint().getLon();
            double distance = productSearchQuery.getMaxDistance();

            GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery(SearchField.QUERY_FIELD_SELLER_LATLON_NAME)
                    .point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);

            searchBuilder.setPostFilter(distanceQueryBuilder);
            /*GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort(SearchField.QUERY_FIELD_SELLER_LATLON_NAME, lat, lon);
            sort.unit(userFriendSearchQuery.getDistanceUnit());
            sort.order(SortOrder.ASC);
            sort.point(lat,lon);
            searchBuilder.addSort(sort);*/
            addScriptGeo(searchBuilder,lon,lat,SearchField.QUERY_FIELD_SELLER_LATLON_NAME,productSearchQuery.getDistanceUnit());
        }

        //添加距离之后排序条件
        if(productSearchQuery.getLastAscQueries().size()>0){
            for(LastAscQuery lastAscQuery:productSearchQuery.getLastAscQueries()){
                if(lastAscQuery.getAsc()!=null){
                    searchBuilder.addSort(lastAscQuery.getKey(),lastAscQuery.getAsc()?SortOrder.ASC:SortOrder.DESC);
                }
            }
        }
        //super.printfSearch(queryBuilder);

        SearchResponse response = searchBuilder.setQuery(queryBuilder).execute().actionGet();
        logger.info(searchBuilder.toString());
        return processSearchResponse(response);
    }

    private void addRangeNum(BoolQueryBuilder queryBuilder,String key,Long min,Long max){
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key);
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

    private List<ProductSearchResponse> processSearchResponse(SearchResponse response){
        List<ProductSearchResponse> productSearchResponses = new ArrayList<>();
        for (SearchHit searchHit : response.getHits()) {
            Map<String, Object> resultMap = null;
            try {
                resultMap = searchHit.getSourceAsMap();
                ProductSearchResponse productInfo = new ProductSearchResponse();
                if(resultMap.get("location")!=null){
                    if(StringUtils.isNotBlank(resultMap.get("location").toString())){
                        String[] geo = resultMap.get("location").toString().split(",");
                        if(geo.length==2){
                            BigDecimal lon = new BigDecimal(geo[1]);
                            BigDecimal lat = new BigDecimal(geo[0]);
                            if(checkLocationStr(lon) && checkLocationStr(lat)){
                                productInfo.setLon(lon);
                                productInfo.setLat(lat);
                                if (searchHit.getSortValues().length > 0) {
                                    productInfo.setDistance(getDistance(searchHit));
                                }
                            }
                        }
                    }
                }
                if(resultMap.get("id") != null)
                    productInfo.setId(resultMap.get("id").toString());

                if(resultMap.get("name") != null)
                    productInfo.setName(resultMap.get("name").toString());

                if(resultMap.get("userId") != null)
                    productInfo.setUserId(resultMap.get("userId").toString());

                if(resultMap.get("merchantId") != null)
                    productInfo.setMerchantId(resultMap.get("merchantId").toString());

                if(resultMap.get("productImagesUrl") != null)
                    productInfo.setProductImagesUrl((List<String>)(List)resultMap.get("productImagesUrl"));

                if(resultMap.get("price") != null)
                    productInfo.setPrice(Long.parseLong(resultMap.get("price").toString()));

                if (resultMap.get("isDelivery") != null) {
                    productInfo.setDelivery(strToBoolean(resultMap.get("isDelivery").toString()));
                }
                if (resultMap.get("onlineStatus") != null) {
                   String stas= resultMap.get("onlineStatus").toString();
                    if (stas.equals("4")||stas.equals("1")){
                        productInfo.setOnlineStatus(true);
                    }
                   else if (stas.equals("3")||stas.equals("2")){
                        productInfo.setOnlineStatus(false);
                    }
                }

                if(resultMap.get("price") != null)
                    productInfo.setPrice(Long.parseLong(resultMap.get("price").toString()));

                if(resultMap.get("marketPrice") != null)
                    productInfo.setMarketPrice(Long.parseLong(resultMap.get("marketPrice").toString()));

                if(resultMap.get("visitNum") != null)
                    productInfo.setVisitNum(Long.parseLong(resultMap.get("visitNum").toString()));

                if(resultMap.get("merchantName") != null)
                    productInfo.setMerchantName(resultMap.get("merchantName").toString());

                if(resultMap.get("categoryIds") != null)
                    productInfo.setCategoryIds((List<String>)(List)(resultMap.get("categoryIds")));

                if(resultMap.get("categoryNames") != null)
                    productInfo.setCategoryNames((List<String>)(List)(resultMap.get("categoryNames")));

                if(resultMap.get("categoryParentIds") != null)
                    productInfo.setCategoryParentIds((List<String>)(List)(resultMap.get("categoryParentIds")));

                if(resultMap.get("provinceCode") != null)
                    productInfo.setProvinceCode(resultMap.get("provinceCode").toString());

                if(resultMap.get("cityCode") != null)
                    productInfo.setCityCode(resultMap.get("cityCode").toString());

                if(resultMap.get("areaCode") != null)
                    productInfo.setAreaCode(resultMap.get("areaCode").toString());

                if(resultMap.get("addrCountry") != null)
                    productInfo.setAddrCountry(resultMap.get("addrCountry").toString());

                if(resultMap.get("credit")!=null)
                    productInfo.setCredit(Integer.parseInt(resultMap.get("credit").toString()));

                if(resultMap.get("volume")!=null)
                    productInfo.setVolume(Math.round(Double.parseDouble(resultMap.get("volume").toString())));

                if(resultMap.get("isHoldCredit")!=null)
                    productInfo.setHoldCredit(strToBoolean(resultMap.get("isHoldCredit").toString()));

                if(resultMap.get("isReturn")!=null)
                    productInfo.setReturn(strToBoolean(resultMap.get("isReturn").toString()));

                if(resultMap.get("firstRate") != null){
                    productInfo.setFirstRate(new BigDecimal(resultMap.get("firstRate").toString()));
                }
                if(resultMap.get("payStatus") != null){
                    productInfo.setPayStatus(Integer.parseInt(resultMap.get("payStatus").toString()));
                }
                if(resultMap.get("characteristic") != null){
                    productInfo.setCharacteristic(resultMap.get("characteristic").toString());
                }

                productSearchResponses.add(productInfo);

            } catch (Exception e) {
                logger.error("error at processNewsList, message:" + resultMap, e);
            }
        }
        return productSearchResponses;
    }
}
