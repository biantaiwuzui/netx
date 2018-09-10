package com.netx.shopping.biz.business;

import com.netx.common.vo.business.AddCollectSellerRequestDto;
import com.netx.shopping.model.business.SellerFavorite;
import com.netx.shopping.service.business.SellerCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 网商-商家收藏表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-10-26
 */
@Service("sellerCollectAction")
@Transactional(rollbackFor = Exception.class)
public class SellerCollectAction {

    @Autowired
    SellerCollectService sellerCollectService;

    public boolean collectSeller(AddCollectSellerRequestDto requestDto){
        SellerFavorite collect = new SellerFavorite();
        collect.setUserId(requestDto.getUserId());
        collect.setSellerId(requestDto.getSellerId());
        SellerFavorite isCollect = this.isHaveCollect(collect);
        if( isCollect == null){  //没有收藏过就添加收藏
            return sellerCollectService.insert(collect);
        }else{       //取消收藏
            return sellerCollectService.deleteById(isCollect.getId());
        }
    }

    
    public SellerFavorite isHaveCollect(SellerFavorite collect){
        return sellerCollectService.isHaveCollect(collect);
    }

    
    public List<String> getSellerCollectListByUserId(String userId){
        List<String> sellerIds = new ArrayList<>();
        List<SellerFavorite> list = sellerCollectService.getSellerCollectList(userId);
        if(list.size() > 0){
            for (SellerFavorite sc: list) {
                sellerIds.add(sc.getSellerId());
            }
        }
        return sellerIds;
    }
}
