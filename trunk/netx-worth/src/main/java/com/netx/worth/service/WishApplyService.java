package com.netx.worth.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.WishApplyMapper;
import com.netx.worth.model.WishApply;

@Service
public class WishApplyService extends ServiceImpl<WishApplyMapper, WishApply>{
	private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
    @Autowired
    private WishManagerService wishManagerService;
    @Autowired
    private WishService wishService;

	/**获得心愿支持列表分页*/
    public List<WishApply> getApplyListByWish(String wishId, Page<WishApply> page) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        Page<WishApply> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }

    /**获得心愿支持列表*/
    public List<WishApply> getApplyLisByWish(String wishId) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        entityWrapper.orderBy ( "create_time",false );
        return selectList(entityWrapper);
    }


    /**获得心愿支持列表*/
    public List<WishApply> getApplyLisByUser(String userId) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId);
        return selectList(entityWrapper);
    }

    /**获得心愿支持列表-拒绝*/
    public int countiapplyzeroByWish(String Id) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("is_pass=0 and id={0}", Id);
        return selectCount(entityWrapper);
    }

    /**获得心愿支持列表-同意*/
    public int countiapplyoneByWish(String Id) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("is_pass=1 and id={0}", Id);
        return selectCount(entityWrapper);
    }

    /**获得心愿支持列表-待审核*/
    public int countiapplywaitByWish(String Id) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("is_pass=2 and id={0}", Id);
        return selectCount(entityWrapper);
    }


    /**通过银行id查询支持信息*/
    public WishApply getWishistoryWishapplyId(String BankId) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("bank_id={0}", BankId);
        return selectOne(entityWrapper);
    }


    /**通过心愿ID心愿使用表*/
    public WishApply getWishApplybWishId(String wishId) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        return selectOne(entityWrapper);
    }

    /**通过心愿ID心愿使用表*/
    public WishApply getWishApplybyWishId(String wishId,String userId) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0} and user_id={1} and is_pass = 2", wishId,userId);
        return selectOne(entityWrapper);
    }
    
 /*   *//**检查是否有未通过的心愿*//*
    public boolean allPass(String wishId) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        //找未通过的心愿（已处理的超过了30%，那么就算剩下的全同意，也达不到3分之2，因为3分之2同意才代表批准用款。）
        entityWrapper.where("wish_id={0}", wishId).and("(is_pass={0} and manager_count > 0 and opreate_manager_count/manager_count > 0.3 )", 0);

        return selectList(entityWrapper).size() <= 0;

    }*/

    /**检查是否有待通过的心愿*/
    public boolean isPass(String wishId) {
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        //找待通过的审核心愿（不存在待通过的审核心愿才代表批准用款。）
        entityWrapper.where("wish_id={0}", wishId).and("(is_pass={0})", 2);

        return selectList(entityWrapper).size() > 0;

    }

    /**创建心愿使用*/
//    @Transactional
//    public WishApply create(WishApplyDto wishApplyDto) {
//        WishApply wishApply = new WishApply();
//        VoPoConverter.copyProperties(wishApplyDto, wishApply);
//        wishApply.setAmount(wishApplyDto.getAmount().longValue()*100);
//        Wish wish = wishService.selectById(wishApplyDto.getWishId());
//        wishApply.setBalance(wish.getCurrentAmount() - wish.getCurrentApplyAmount()*100);
//        List<WishManager> managers = wishManagerService.getManagerByWish(wishApplyDto.getWishId());
//        wishApply.setManagerCount(managers.size());
//        return insert(wishApply)? wishApply : null;
//    }
    @Transactional
    public Boolean create(WishApply wishApply) {
        return insertOrUpdate(wishApply);
    }
    
    /**根距userId列表删除心愿使用列表*/
    public boolean deleteWishApplyByUserId(String userId) { 
    	EntityWrapper<WishApply> wishApplyWrapper = new EntityWrapper<WishApply>();
    	wishApplyWrapper.where("user_id={0}", userId);
    	return delete(wishApplyWrapper);
    }
    
    /**查出最近一次申请用款记录时间*/
    public Long getLastCreateTime(String wishId) { 
		//查出最近一次申请用款记录
		EntityWrapper<WishApply> wishApplyWrapper = new EntityWrapper<WishApply>();
		wishApplyWrapper.setSqlSelect("MAX(create_time)");
		wishApplyWrapper.where("wish_id={0}", wishId);
		return (Long) selectObj(wishApplyWrapper);
    }

    /* 查询申请用款信息*/
    public WishApply getWishApplyApplicant(String wishApplyId,String userId){
        EntityWrapper<WishApply> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("id={0} and user_id={1}",wishApplyId,userId);
        return selectOne(entityWrapper);
    }
}
