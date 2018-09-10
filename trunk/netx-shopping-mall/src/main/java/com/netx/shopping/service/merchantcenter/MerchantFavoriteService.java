package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.merchantcenter.MerchantFavoriteMapper;
import com.netx.shopping.model.merchantcenter.MerchantFavorite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantFavoriteService extends ServiceImpl<MerchantFavoriteMapper, MerchantFavorite> {

    /**
     * 查询用户是否收藏商家
     * @param userId
     * @param merchantId
     * @return
     */
    public MerchantFavorite isHaveFavorite(String userId, String merchantId){
        EntityWrapper<MerchantFavorite> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} AND merchant_id = {1} AND deleted = 0", userId, merchantId);
        return this.selectOne(wrapper);
    }

    /**
     * 根据用户id查询商家id
     * @param userId
     * @return
     */
    public List<String> getMerchantIdByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("merchant_id").where("user_id = {0} AND deleted = {1}", userId, 0);
        return this.selectObjs(wrapper);
    }
}
