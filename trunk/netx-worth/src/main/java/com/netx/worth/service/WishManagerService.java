package com.netx.worth.service;

import java.math.BigDecimal;
import java.util.*;

import com.netx.worth.model.Wish;
import com.netx.worth.model.WishReferee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.worth.mapper.WishManagerMapper;
import com.netx.worth.model.WishManager;
import com.netx.worth.model.WishSupport;

@Service
public class WishManagerService extends ServiceImpl<WishManagerMapper, WishManager> {
    private Logger logger = LoggerFactory.getLogger ( this.getClass ().getSimpleName () );
    @Autowired
    private WishSupportService wishSupportService;
    @Autowired
    private WishRefereeService wishRefereeService;
    @Autowired
    private WishService wishService;

    /**
     * 获取心愿监管监管列表
     */
    public List<WishManager> getManagerByWish(String wishId) {
        EntityWrapper<WishManager> entityWrapper = new EntityWrapper<> ();
        entityWrapper.where ( "wish_id={0}", wishId );
        return selectList ( entityWrapper );
    }

    /**
     * 通过userId获得心愿监管列表
     */
    public List<WishManager> getManagerListByUserId(String userId, Page<WishManager> page) {
        EntityWrapper<WishManager> entityWrapper = new EntityWrapper<> ();
        entityWrapper.where ( "user_id={0}", userId );
        Page<WishManager> selectPage = selectPage ( page, entityWrapper );
        return selectPage.getRecords ();
    }

    /**
     * 获取心愿监管通过心愿Id和userId
     */
    public List<WishManager> getManagerByUserIdAndWishId(String wishId) {
        EntityWrapper<WishManager> entityWrapper = new EntityWrapper<> ();
        entityWrapper.where ( "wish_id={0}", wishId );
        return selectList ( entityWrapper );
    }

    /**
     * 通过心愿ID心愿使用表
     */
    public WishManager getWishManagerbyWishId(String wishId) {
        EntityWrapper<WishManager> entityWrapper = new EntityWrapper<> ();
        entityWrapper.where ( "wish_id={0}", wishId );
        return selectOne ( entityWrapper );
    }

    /**
     * 创建心愿监管
     */
    @Transactional
    public boolean create(String wishId) throws RuntimeException {
        List<WishSupport> supports = wishSupportService.getSupportListByWish ( wishId );

        //筛选重复的支持者 并把相同的支持者的金额相加到一起
        for (int i = 0; i < supports.size (); i++) {
            for (int j = i + 1; j < supports.size (); j++) {
                //判断userId是否重复
                if (supports.get ( i ).getUserId ().equals ( supports.get ( j ).getUserId () )) {
                    //重复,先把金额相加再删除该元素
                    supports.get ( i ).setAmount ( supports.get ( i ).getAmount () + supports.get ( j ).getAmount () );
                    supports.remove ( supports.get ( j ) );
                    j = j - 1;
                }
            }
        }
        //根据真实人数记录要创建多少个监管者
        int size = getManagerSize ( supports.size () );

        //支持者金额排序
        for (int i = 0; i < supports.size () - 1; i++) {
            for (int j = i + 1; j < supports.size () - 1; j++) {
                long temp;
                if (supports.get ( i ).getAmount () < (supports.get ( j ).getAmount ())) {
                    temp = supports.get ( j ).getAmount ();
                    supports.get ( j ).setAmount ( supports.get ( i ).getAmount () );
                    supports.get ( i ).setAmount ( temp );
                }
            }
        }

        List<WishReferee> wishReferees = wishRefereeService.selectByWishId ( wishId );
        //插入人数次数
        //for (int i = 0; i < size; i++) {

        int countIsSupports = 0;
        //根据支持者人数去判断是否为推荐者
        for (int j = countIsSupports; j < supports.size (); j++) {
            WishManager wishManager = new WishManager ();
            WishSupport support = supports.get ( j );
            int countIsReferee = 0;
            for (int p = 0; p < wishReferees.size (); p++) {
                WishReferee referee = wishReferees.get ( p );
                if (support.getUserId ().equals ( referee.getUserId () )) {
                    countIsReferee++;
                    break;
                }
            }
            //第j个支持者不是推荐者且监管者人数小于需要插入的人数时 插入
            if (countIsReferee == 0 && countIsSupports < size) {
                wishManager.setUserId ( support.getUserId () );
                wishManager.setWishId ( support.getWishId () );
                wishManager.insertAllColumn ();
                countIsSupports++;
                if (countIsSupports >= size) {
                    break;
                }
            }/**如果这里没有执行,证明遍历之后所有的支持者都是推荐者,那么跳到下面的while循环*/
        }
        /**
         * 在这里我们就要把第countIsSupports个支持者插入
         * countIsSupports代表有多少个不是推荐者的支持者成为了监管者
         * 第countIsSupports个支持者代表"在不是推荐者的支持者成为监管者后,监管者不够的情况下插入推荐者"
         */
        while (countIsSupports < size) {
            WishManager wishManager = new WishManager ();
            wishManager.setUserId ( supports.get ( countIsSupports ).getUserId () );
            wishManager.setWishId ( supports.get ( countIsSupports ).getWishId () );
            wishManager.insertAllColumn ();
            countIsSupports++;
        }

        return true;
    }

    private int getManagerSize(int supportCount) {
        if (supportCount >= 50) {
            return 5;
        }
        if (supportCount >= 11) {
            return 3;
        }
        if (supportCount > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 根距userId列表删除心愿监管列表
     */
    public boolean deleteWishManagerByUserId(String userId) {
        EntityWrapper<WishManager> wishManagerWrapper = new EntityWrapper<WishManager> ();
        wishManagerWrapper.where ( "user_id={0}", userId );
        return delete ( wishManagerWrapper );
    }

    /**
     * 通过wishId获取心愿监管人列表
     */
    public List<WishManager> getManagerByWishId(String wishId) {
        EntityWrapper<WishManager> managerWrapper = new EntityWrapper<> ();
        managerWrapper.where ( "wish_id={0}", wishId );
        return selectList ( managerWrapper );
    }

    /* 判断是否管理者登录*/
    public Integer getManagerCount(String wishId, String userId) {
        EntityWrapper<WishManager> entityWrapper = new EntityWrapper<> ();
        entityWrapper.where ( "user_id={0} and wish_id={1}", userId, wishId );
        return selectCount ( entityWrapper );
    }


}
