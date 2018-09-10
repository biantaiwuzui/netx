package com.netx.searchengine;

import com.netx.searchengine.query.ProductSearchQuery;
import com.netx.searchengine.service.ProductSearchService;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by CloudZou on 2/9/18.
 */
public class SearchTest {
    private Logger logger = LoggerFactory.getLogger(SearchTest.class);

    public static void main(String[] args) throws IOException{

        try {
//            SellerSearchService sellerSearchService = new SellerSearchService("112.74.200.88", 9300);
//            SellerSearchQuery searchQuery = new SellerSearchQuery();
//            searchQuery.setDistanceUnit(DistanceUnit.KILOMETERS);
//            searchQuery.setCenterGeoPoint(new GeoPoint(22.125435, 113.353910));
//            searchQuery.setCategoryId("8d8c43218af54c8cbbd3507d33839dc8");
//
//            sellerSearchService.querySellers(searchQuery);

            searchProduct();

        }catch (Exception ex) {
            System.out.print("test");
        }
        System.in.read();
    }

    private static void searchProduct(){
        ProductSearchService productSearchService = new ProductSearchService();
        productSearchService.setHost("112.74.200.88");
        productSearchService.setPort(9300);
        ProductSearchQuery searchQuery = new ProductSearchQuery();
        searchQuery.setDistanceUnit(DistanceUnit.KILOMETERS);
        searchQuery.setCenterGeoPoint(new GeoPoint(22.125435, 113.353910));
        searchQuery.setOnlineStatus(false);

        productSearchService.queryProducts(searchQuery);
    }
}
