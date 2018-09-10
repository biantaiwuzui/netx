package com.netx.worth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.WishGroupMapper;
import com.netx.worth.model.WishGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishGroupService extends ServiceImpl<WishGroupMapper, WishGroup> {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**填入心愿组信息*/
    public Boolean WishGroup(WishGroup wishGroup) {
        return insertOrUpdate(wishGroup);
    }
    /**通过心愿id查询组*/
    public WishGroup getGroupByWish(String wishId) {
        EntityWrapper<WishGroup> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("wish_id={0}", wishId);
        return selectOne(entityWrapper);
    }

    public boolean createGroup(String wishId){
        WishGroup wishGroup = new WishGroup();
        wishGroup.setWishId(wishId);
        return insert(wishGroup);
    }

}