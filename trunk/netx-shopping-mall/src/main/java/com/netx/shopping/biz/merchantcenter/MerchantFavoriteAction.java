package com.netx.shopping.biz.merchantcenter;

import com.netx.shopping.model.merchantcenter.MerchantFavorite;
import com.netx.shopping.service.merchantcenter.MerchantFavoriteService;
import com.netx.shopping.vo.AddMerchantFavoriteRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MerchantFavoriteAction {


    @Autowired
    private MerchantFavoriteService merchantFavoriteService;

    /**
     * 根据用户id查询商家id
     * @param userId
     * @return
     */
    public List<String> getMerchantIdByUserId(String userId){
        return merchantFavoriteService.getMerchantIdByUserId(userId);
    }

    /**
     * 收藏或者取消收藏商家
     * @param requestDto
     * @return
     */
    @Transactional
    public boolean addMerchantFavorite(AddMerchantFavoriteRequestDto requestDto){
        MerchantFavorite collect = new MerchantFavorite();
        collect.setUserId(requestDto.getUserId());
        collect.setMerchantId(requestDto.getMerchantId());
        MerchantFavorite isCollect = this.isHaveCollect(collect);
        if( isCollect == null){  //没有收藏过就添加收藏
            return merchantFavoriteService.insert(collect);
        }else{       //取消收藏
            return merchantFavoriteService.deleteById(isCollect.getId());
        }
    }

    public MerchantFavorite isHaveCollect(MerchantFavorite collect){
        return merchantFavoriteService.isHaveFavorite(collect.getUserId(), collect.getMerchantId());
    }

}
