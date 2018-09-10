package com.netx.shopping.biz.marketing;

import com.netx.shopping.biz.business.SellerRegisterAction;
import com.netx.shopping.biz.business.SellerAction;
import com.netx.shopping.service.business.SellerService;
import com.netx.shopping.service.marketing.AddCustomeragentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2017-12-29
 */
@Service
public class AddCustomeragentAction {

    @Autowired
    SellerAction sellerAction;

    @Autowired
    AddCustomeragentService addCustomeragentService;

    @Autowired
    SellerService sellerService;

    @Autowired
    SellerRegisterAction sellerRegisterAction;

    public String getCity(String city,String provinceCode){
        if(city==null || city.isEmpty()){
            return provinceCode;
        }
        return provinceCode+"-"+city;
    }

    public String createAlertMsg(String patter,Object object){
        return new MessageFormat(patter).format(object);
    }
}
