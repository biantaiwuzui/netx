package com.netx.shopping.biz.merchantcenter;


import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.merchantcenter.ShippingFee;
import com.netx.shopping.service.merchantcenter.ShippingFeeService;
import com.netx.utils.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * <p>
 *  商家物流费用设置 action
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-10
 */
@Service
public class ShippingFeeAction {

	@Autowired
    ShippingFeeService shippingFeeService;

	@Autowired
    MerchantAction merchantAction;

    /**
     * 添加物流费用设置
     * @param merchantId
     * @param fee
     * @return
     */
	public String addOrUpdate(String merchantId, BigDecimal fee){
        Merchant merchant = merchantAction.getMerchantService().selectById(merchantId);
        if(merchant==null){
            throw new RuntimeException("此商家已注销");
        }
        ShippingFee shippingFee = shippingFeeService.queryByMerchantId(merchantId);
        boolean flag;
        if(shippingFee==null){
            shippingFee = new ShippingFee();
            shippingFee.setMerchantId(merchantId);
            shippingFee.setFee(new Money(fee).getCent());
            flag = shippingFeeService.insert(shippingFee);
        }else{
            shippingFee.setFee(new Money(fee).getCent());
            flag = shippingFeeService.updateById(shippingFee);
        }
        return flag?shippingFee.getId():null;
    }

    /**
     * 查询商家物流费用
     * @param merchantId
     * @return
     */
    public BigDecimal getFee(String merchantId){
        ShippingFee shippingFee = shippingFeeService.queryByMerchantId(merchantId);
        if(shippingFee!=null){
            return Money.CentToYuan(shippingFee.getFee()).getAmount();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 查询商家物流费用设置
     * @param merchantId
     * @return
     */
    public ShippingFee getShippingFeeByMerchantId(String merchantId){
        return shippingFeeService.queryByMerchantId(merchantId);
    }
}
