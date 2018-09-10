package com.netx.worth.biz.wish;

import java.util.ArrayList;
import java.util.List;

import com.netx.worth.model.WishApply;
import com.netx.worth.service.WorthServiceprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netx.worth.model.WishAuthorize;
import com.netx.worth.model.WishManager;
import com.netx.worth.service.WishAuthorizeService;

/**
 * <p>
 * 心愿授权表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-26
 */
@Service
public class WishAuthorizeAction{
//    @Autowired
//    private WorthServiceprovider worthServiceprovider;
//
//    public boolean createList(List<WishManager> managers, String wishApplyId, String userId) {
//        List<WishAuthorize> list = new ArrayList<>();
//        managers.forEach(wishManager -> {
//            WishAuthorize wishAuthorize = new WishAuthorize();
//            wishAuthorize.setCreateUserId(userId);
//            wishAuthorize.setUserId(wishManager.getUserId());
//            wishAuthorize.setWishApplyId(wishApplyId);
//            list.add(wishAuthorize);
//        });
//        return worthServiceprovider.getWishAuthorizeService().insertBatch(list);
//    }

}
