package com.netx.fuse.client.shoppingmall;

import com.netx.shopping.biz.business.ManageAction;
import com.netx.shopping.model.business.SellerManage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessManageClientAction {

    private Logger logger = LoggerFactory.getLogger(BusinessManageClientAction.class);

    @Autowired
    ManageAction manageAction;

    /**
     * 获取主管
     * @param
     * @return
     *
     */
    public SellerManage getManage(String manageId){
        if (StringUtils.isNotEmpty(manageId)){try{
            SellerManage manage= manageAction.getManage(manageId);
            return manage;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }}

        return null;
    }

}
