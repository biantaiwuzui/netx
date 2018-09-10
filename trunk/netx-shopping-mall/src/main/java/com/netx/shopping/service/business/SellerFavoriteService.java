package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerFavoriteMapper;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.business.SellerFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerFavoriteService extends ServiceImpl<SellerFavoriteMapper, SellerFavorite>{

    @Autowired
    SellerFavoriteMapper sellerFavoriteMapper;

    /**
     * 查询收藏商家sellerId
     * @param userId
     * @return
     */
    public List<String> getSellerIdByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("seller_id AS sellerId").where("user_id = {0}", userId);
        return this.selectObjs(wrapper);
    }
}
