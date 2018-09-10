package com.netx.worth.service;

import java.util.List;
//
//import com.netx.common.wz.dto.wish.WishBankDto;
import com.netx.common.wz.dto.wish.WishBankDto;
import com.netx.worth.mapper.WishBankMapper;
import com.netx.worth.model.WishBank;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service
public class WishBankService extends ServiceImpl<WishBankMapper, WishBank>{
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**填入银行信息*/
    public Boolean WishBank(WishBank wishBank) {
        return insertOrUpdate(wishBank);
    }

    /**获取用户的银行信息*/
    public List<WishBank> getBankList(String userId, Page<WishBank> page) {
        EntityWrapper<WishBank> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("user_id={0}", userId).orderBy("create_time desc");
        Page<WishBank> selectPage = selectPage(page, entityWrapper);
        return selectPage.getRecords();
    }

    public Page<WishBank> getBankLists(String userId, Integer currentPage, Integer size) {
        EntityWrapper<WishBank> entityWrapper = new EntityWrapper<>();
        Page<WishBank> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(size);
        entityWrapper.where("deleted = 0");
        if(StringUtils.isNotBlank(userId)){
            entityWrapper.and("user_id = {0}", userId);
        }
        entityWrapper.orderBy("create_time", false);
        return selectPage(page, entityWrapper);
    }


    /**得到重复的银行信息*/
    public Integer getSameBankList(WishBankDto wishBankDto){
        EntityWrapper<WishBank> wishWrapper = new EntityWrapper<WishBank>();
        wishWrapper.where("user_id={0}", wishBankDto.getUserId());
        wishWrapper.and("account={0}", wishBankDto.getAccount());
        wishWrapper.and("account_name={0}", wishBankDto.getAccountName());
        wishWrapper.and("deposit_bank={0}", wishBankDto.getDepositBank());
        wishWrapper.and("mobile={0}", wishBankDto.getMobile());

        return selectCount(wishWrapper);
    }
    /**通过心愿ID和用户ID查询银行信息*/
    public WishBank getWishBankUserIdAndWishId(String wishBankId,String userId) {
        EntityWrapper<WishBank> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishBankId).and("user_id={0}", userId);
        return selectOne(entityWrapper);
    }

    /**通过心愿查询银行信息*/
    public WishBank getWishBankWishId(String wishId) {
        EntityWrapper<WishBank> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        return selectOne(entityWrapper);
    }

}
