package com.netx.fuse.biz.shoppingmall.order;

import com.netx.common.common.enums.FrozenTypeEnum;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.fuse.client.ucenter.WalletForzenClientAction;
import com.netx.shopping.model.order.ProductOrder;
import com.netx.shopping.service.order.OrderAutoCloseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderAutoCloseFuseAction {

    @Autowired
    OrderAutoCloseService orderAutoCloseService;

    @Autowired
    WalletForzenClientAction walletForzenClientAction;

    public void autoCloseOrder() {
        Long expireTime = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000);
        List<ProductOrder> productOrderList = null;
        productOrderList = orderAutoCloseService.getProductOrderList(new Date(expireTime));
        if (null != productOrderList) {
            for (ProductOrder order : productOrderList) {
                //零钱支付
                if (order.getPayWay() == 2) {
                    FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
                    requestDto.setTypeId(order.getId());
                    requestDto.setUserId(order.getUserId());
                    //Result result = walletForzenClient.repealFrozen(requestDto);
                    //撤销冻结金额成功
                    //if (result.getCode() == 0) {
                    requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
                    if(walletForzenClientAction.repealFrozen(requestDto)){
                        //设置为取消状态
                        order.setStatus(8);
                        orderAutoCloseService.updateById(order);
                    }
                } else {//TODO 网币支付流程不全,需要补全

                }
            }
        }
    }
}
