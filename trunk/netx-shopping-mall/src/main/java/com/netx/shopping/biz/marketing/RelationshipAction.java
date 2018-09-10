package com.netx.shopping.biz.marketing;


import com.netx.shopping.model.marketing.SellerRelationship;
import com.netx.shopping.service.marketing.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2017-12-29
 */
@Service
public class RelationshipAction{

    @Autowired
    RelationshipService relationshipService;
    
    public Boolean buildRelationship(String sellerId,String pid,String CreateUser){
        long time = System.currentTimeMillis();
        SellerRelationship relationship = new SellerRelationship();
        relationship.setPsellerId(pid);
        relationship.setSellerId(sellerId);
        relationship.setCreateUserId(CreateUser);
        return relationshipService.insert(relationship);
    }

    
    public Integer getSubordinateCount(String sellerId){
        return relationshipService.getSubordinateCount(sellerId);
    }

    
    public String getRelationPSellerId(String sellerId){
        return relationshipService.getRelationPSellerId(sellerId);
    }

    public List<Integer> getSubordinateCountBySellerIds(List<String> sellerIds){
        return getSubordinateCountBySellerIds(sellerIds);
    }
}
