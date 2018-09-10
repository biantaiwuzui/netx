package com.netx.fuse.client.shoppingmall;

import com.netx.shopping.biz.business.SellerAction;
import com.netx.shopping.biz.business.SellerRegisterAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketingClientAction {

    private Logger logger = LoggerFactory.getLogger(MarketingClientAction.class);

    @Autowired
    SellerAction sellerAction;

    @Autowired
    SellerRegisterAction sellerRegisterAction;

    /**
     * 定时清空商家每月拓展的商家数
     * @param
     * @return
     *
     */
    public void updateMonthNum(){
        try {
            sellerRegisterAction.operateMonthThing();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }


    /**
     * 定时清空商家每日拓展的商家数
     * @param
     * @return
     *
     */
    public boolean updateDayNum(){
        try {
            return sellerAction.updateDayNum();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }

}
