package com.netx.shopping.service.marketing;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.marketing.SellerRelationshipMapper;
import com.netx.shopping.model.marketing.SellerRelationship;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class RelationshipService  extends ServiceImpl<SellerRelationshipMapper, SellerRelationship> {

    public List<String> getIds(String psellerId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("seller_id").where("pseller_id={0}",psellerId);
        return this.selectObjs(wrapper);
    }

    public Integer getSubordinateCount(String sellerId){
        return this.selectCount(createWrapper(sellerId));
    }


    public String getRelationPSellerId(String sellerId){
        Wrapper<SellerRelationship> wrapper = createWrapper(sellerId);
        wrapper.setSqlSelect("pseller_id");
        return (String) this.selectObj(wrapper);
    }

    private Wrapper<SellerRelationship> createWrapper(String sellerId){
        EntityWrapper<SellerRelationship> wrapper = new EntityWrapper<>();
        wrapper.eq("seller_id",sellerId);
        return wrapper;
    }

    public List<Integer> getSubordinateCountBySellerIds(List<String> sellerIds){
        Wrapper<SellerRelationship> wrapper = new EntityWrapper<>();
        wrapper.in("seller_id",sellerIds);
        wrapper.groupBy("pseller_id");
        wrapper.setSqlSelect("count(*)");
        return (List<Integer>)(List)this.selectObjs(wrapper);
    }
}
