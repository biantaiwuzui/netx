package com.netx.worth.service;

import java.util.List;

import com.netx.worth.model.Wish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.enums.WishAuthorizeStatus;
import com.netx.worth.mapper.WishAuthorizeMapper;
import com.netx.worth.model.WishApply;
import com.netx.worth.model.WishAuthorize;
import com.netx.worth.model.WishSupport;

@Service
public class WishAuthorizeService extends ServiceImpl<WishAuthorizeMapper, WishAuthorize>{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@Autowired
    private WishService wishService;
	@Autowired
    private WishApplyService wishApplyService;

	/**通过心愿使用Id列表获得心愿授权列表*/
    public List<WishAuthorize> getListByWishApplyIds(List<String> wishApplyIds) {
        EntityWrapper<WishAuthorize> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("wish_apply_id={0}", wishApplyIds);
        return selectList(entityWrapper);
    }
    
    /**监管人接收心愿*/
    /**查询使用表信息，已处理过的管理员数量加1，通过id更新信息，打开心愿授权表，取声明，状态属性，并将授权表的状态
     * 属性设置为批准，查询授权表的wish_apply_id和user_id
     * */
    @Transactional
    public boolean acceptApply(String applyId, String userId, String description) {
        WishApply wishApply = wishApplyService.selectById(applyId);
        if (wishApply !=null) {
            wishApply.setOpreateManagerCount ( wishApply.getManagerCount () + 1 );
        }
        wishApplyService.updateById(wishApply);
        WishAuthorize wishAuthorize = new WishAuthorize();
        wishAuthorize.setDescription(description);
        wishAuthorize.setWishApplyId(applyId);
        wishAuthorize.setStatus(WishAuthorizeStatus.ACCEPT.status);
        EntityWrapper<WishAuthorize> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_apply_id={0}", applyId).and("user_id={0}", userId);
        return update(wishAuthorize, entityWrapper);
    }
    
    /**获得心愿支持的通过率*/
    public double getAcceptPerecent(String wishApplyId) {
        double accept = 0d;
        List<WishAuthorize> list = getListByWishApplyId(wishApplyId);
        for (WishAuthorize wishAuthorize : list) {
            if (wishAuthorize.getStatus().equals(WishAuthorizeStatus.ACCEPT.status)) {
                accept++;
            }
        }
        return accept / list.size();
    }

    /**获得心愿支持列表-同意*/
    public int countiapplyoneByWish(String Id) {
        EntityWrapper<WishAuthorize> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("status=1 and wish_apply_id={0}", Id);
        return selectCount(entityWrapper);
    }

    /**获得心愿支持列表-待审核*/
    public int countiapplywaitByWish(String Id) {
        EntityWrapper<WishAuthorize> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("status=0 and wish_apply_id={0}", Id);
        return selectCount(entityWrapper);
    }

    /**获得心愿支持列表-拒绝*/
    public int countiapplyzeroByWish(String Id) {
        EntityWrapper<WishAuthorize> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("status=2 and wish_apply_id={0}", Id);
        return selectCount(entityWrapper);
    }
    
    /**获得心愿监管的列表通过心愿支持Id*/
    public List<WishAuthorize> getListByWishApplyId(String wishApplyId) {
        EntityWrapper<WishAuthorize> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_apply_id={0}", wishApplyId);
        return selectList(entityWrapper);
    }
    
    /**拒绝用款*/
    @Transactional
    public boolean refuseApply(String applyId, String userId, String description) {
        WishApply wishApply = wishApplyService.selectById(applyId);
        wishApply.setOpreateManagerCount(wishApply.getManagerCount() + 1);
        wishApplyService.updateById(wishApply);
        Wish wish = wishService.selectById(wishApply.getWishId());
        wish.setCurrentApplyAmount(wish.getCurrentApplyAmount()-wishApply.getAmount());
        WishAuthorize wishAuthorize = new WishAuthorize();
        wishAuthorize.setDescription(description);
        wishAuthorize.setStatus(WishAuthorizeStatus.REFUSE.status);
        EntityWrapper<WishAuthorize> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_apply_id={0}", applyId).and("user_id={0}", userId);
        return update(wishAuthorize, entityWrapper);
    }
    
    /**根距userId列表删除心愿授权列表*/
    public boolean deleteWishAuthorizeByUserId(String userId) { 
    	EntityWrapper<WishAuthorize> wishAuthorizeWrapper = new EntityWrapper<WishAuthorize>();
		wishAuthorizeWrapper.where("user_id={0}", userId);
    	return delete(wishAuthorizeWrapper);
    }
}
