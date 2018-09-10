package com.netx.shopping.biz.productcenter;


import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.model.productcenter.ProductDelivery;
import com.netx.shopping.service.productcenter.ProductDeliveryService;
import com.netx.shopping.vo.AddDeliveryWayRequestDto;
import com.netx.utils.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author CHEN-QIAN
 * @since 2018-06-08
 */
@Service
public class ProductDeliveryAction {

    @Autowired
    private ProductDeliveryService productDeliveryService;

    @Autowired
    private ProductAction productAction;
    

    /**
     * CHEN-QIAN
     * 添加/编辑配送方式
     * @param deliveryWayList
     * @param productId
     * @return
     */
    public Boolean addDelivery(List<AddDeliveryWayRequestDto> deliveryWayList, String productId){
        productDeliveryService.deleteByProductId(productId);
        for(AddDeliveryWayRequestDto requestDto : deliveryWayList){
            ProductDelivery productDelivery = new ProductDelivery();
            productDelivery.setDeliveryWay(requestDto.getDeliveryWay());
            productDelivery.setProductId(productId);
            productDelivery.setFee(new Money(requestDto.getShoppingFee()).getCent());
            productDeliveryService.insert(productDelivery);
        }
        return true;
    }

    /**
     * CHEN-QIAN
     * 查询配送方式
     * @param productId
     * @return
     */
    public List<AddDeliveryWayRequestDto> queryDeliveryByProductId(String productId){
        List<ProductDelivery> productDeliveries = productDeliveryService.getProductDeliveryByProductId(productId);
        List<AddDeliveryWayRequestDto> requestDtos = new ArrayList<>();
        if(productDeliveries.size()<=0){
            Product product = productAction.getProductService().selectById(productId);
            if(product!=null){
                AddDeliveryWayRequestDto requestDto = new AddDeliveryWayRequestDto();
                requestDto.setDeliveryWay(product.getDeliveryWay());
                requestDto.setShoppingFee(new BigDecimal(Money.getMoneyString(product.getShippingFee())));
                requestDtos.add(requestDto);
            }
        }
        for(ProductDelivery productDelivery : productDeliveries){
            AddDeliveryWayRequestDto requestDto = new AddDeliveryWayRequestDto();
            VoPoConverter.copyProperties(productDelivery, requestDto);
            requestDto.setShoppingFee(new BigDecimal(Money.getMoneyString(productDelivery.getFee())));
            requestDtos.add(requestDto);
        }
        return requestDtos;
    }

    /**
     * 根据配送方式和商品id查询物流费用
     * @param productId
     * @param deliveryWay
     * @return
     */
    public long queryFeeByProductIdAndDeliveryWay(String productId, Integer deliveryWay){
        Long fee = productDeliveryService.getFeeByProductIdAndDeliveryWay(productId, deliveryWay);
        return fee==null?0:fee;
    }
	
}
