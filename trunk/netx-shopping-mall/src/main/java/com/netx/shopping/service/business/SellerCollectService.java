package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerFavoriteMapper;
import com.netx.shopping.model.business.SellerFavorite;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class SellerCollectService extends ServiceImpl<SellerFavoriteMapper, SellerFavorite> {

    /**
     * 是否收藏
     * @param collect
     * @return
     */
    public SellerFavorite isHaveCollect(SellerFavorite collect){
        EntityWrapper<SellerFavorite> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}", collect.getUserId())
                .andNew("seller_id = {0}", collect.getSellerId());
        return this.selectOne(wrapper);
    }

    /**
     * 获取收藏列表
     * @param userId
     * @return
     */
    public List<SellerFavorite> getSellerCollectList(String userId){
        EntityWrapper<SellerFavorite> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}", userId);
        wrapper.orderBy("create_time", false);
        return this.selectList(wrapper);
    }

    /**
     * 根据userId获取收藏商家数
     * @param userId
     * @return
     */
    public int getSellerCollectCount(String userId){
        EntityWrapper<SellerFavorite> wrapper2 = new EntityWrapper<>();
        wrapper2.where("user_id = {0}",userId);
        return this.selectCount(wrapper2);
    }

    /**
     * 根据userId获取最新收藏商家
     * @param userId
     * @return
     */
    public SellerFavorite getSellerCollect(String userId){
        EntityWrapper<SellerFavorite> wrapper = new EntityWrapper();
        wrapper.where("user_id={0}", userId).orderBy("create_time", false);
        return this.selectOne(wrapper);
    }
}
