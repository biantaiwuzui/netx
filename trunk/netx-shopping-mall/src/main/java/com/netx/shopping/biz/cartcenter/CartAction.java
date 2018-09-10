package com.netx.shopping.biz.cartcenter;


import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.biz.ordercenter.MerchantOrderInfoAction;
import com.netx.shopping.model.cartcenter.Cart;
import com.netx.shopping.model.cartcenter.constants.CartItemStatusEnum;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.ordercenter.constants.OrderTypeEnum;
import com.netx.shopping.service.cartcenter.CartService;
import com.netx.shopping.vo.*;
import com.netx.utils.DistrictUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  购物车action层
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-08
 */
@Service
public class CartAction {

    private Logger logger = LoggerFactory.getLogger(CartAction.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemAction cartItemAction;

    @Autowired
    private MerchantAction merchantAction;

    @Autowired
    private MerchantOrderInfoAction merchantOrderInfoAction;

    @Autowired
    private MerchantManagerAction merchantManagerAction;

    /**
     * 加入购物车
     * @param userId
     * @param addCartRequestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean addCart(String userId,AddCartItemRequestDto addCartRequestDto,String userNumber){
        if(merchantAction.getMerchantService().selectById(addCartRequestDto.getMerchantId())==null){
            throw new RuntimeException("此商家已失效");
        }
        if(merchantManagerAction.checkAdmin(addCartRequestDto.getMerchantId(),userNumber)){
            throw new RuntimeException("商家职员不能购买这个商品");
        }
        Cart cart = cartService.query(userId,addCartRequestDto.getMerchantId());
        boolean flag = true;
        if(cart==null){
            cart=new Cart();
            cart.setMerchantId(addCartRequestDto.getMerchantId());
            cart.setUserId(userId);
            flag = cartService.insert(cart);
        }
        return flag && cartItemAction.addCartItem(cart.getId(),addCartRequestDto.getQuantity(),addCartRequestDto.getSkuId(),addCartRequestDto.getProductId(),addCartRequestDto.getDeliveryWay());
    }

    /**
     * 结算
     * @param userId
     * @param finishCartRequestDtos
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String finish(String userId,String nickname,List<FinishCartRequestDto> finishCartRequestDtos){
        String orderIds = createOrderId(userId,nickname,finishCartRequestDtos.remove(0));
        if(orderIds==null){
            orderIds="";
        }
        String orderId;
        for(FinishCartRequestDto finishCartRequestDto:finishCartRequestDtos){
            orderId=createOrderId(userId,nickname,finishCartRequestDto);
            if(orderId!=null) {
                orderIds += "," + orderId;
            }
        }
        return orderIds;
    }

    @Transactional(rollbackFor = Exception.class)
    String createOrderId(String userId,String nickname,FinishCartRequestDto finishCartRequestDto){
        Cart cart = cartService.query(userId,finishCartRequestDto.getMerchantId());
        if(cart!=null){
            if(finishCartRequestDto.getDtos().size()>0){
                cartItemAction.updateCartItem(cart.getId(),finishCartRequestDto.getDtos(), CartItemStatusEnum.CIS_FINISH);
                String orderId = merchantOrderInfoAction.add(userId,nickname,cart.getMerchantId(),finishCartRequestDto.getDtos(), OrderTypeEnum.NORMAL_ORDER);
                return orderId;
            }
        }
        return null;
    }

    /**
     * 移除
     * @param userId
     * @param cartItemId
     * @return
     */
    public Boolean remove(String userId,String merchantId,String cartItemId){
        Cart cart = cartService.query(userId,merchantId);
        if(cart==null){
            throw new RuntimeException("你此购物详情为空");
        }
        return cartItemAction.removeCartItem(cartItemId,cart.getId());
    }

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    public Boolean removeByUserId(String userId){
        List<String> cartIds = cartService.query(userId);
        if(cartIds!=null &&cartIds.size()>0){
            return cartItemAction.remove(cartIds,CartItemStatusEnum.CIS_WATTING);
        }
        return true;
    }

    /**
     * 获取购物车详情
     * @param userId
     * @return
     */
    public List<CartMerchantResponseDto> list(String userId,Double lat, Double lon){
        List<Cart> list = cartService.queryByUserId(userId);
        List<CartMerchantResponseDto> cartMerchantResponseDtos = new ArrayList<>();
        if(list!=null && list.size()>0){
            list.forEach(cart -> {
                CartMerchantResponseDto cartMerchantResponseDto = createCartMerchantResponseDto(cart.getId(),cart.getMerchantId(),lat,lon);
                if(cartMerchantResponseDto!=null){
                    cartMerchantResponseDtos.add(cartMerchantResponseDto);
                }
            });
        }
        return cartMerchantResponseDtos;
    }

    /**
     * 获取单个购物清单
     * @param cartId
     * @param merchantId
     * @return
     */
    public CartMerchantResponseDto createCartMerchantResponseDto(String cartId,String merchantId,Double lat,Double lon){
        String name = merchantAction.getMerchantService().getMerchantNameById(merchantId);
        if(name!=null){
            List<CartItemListResponseDto> list = cartItemAction.queryByCartId(cartId);
            if(list.size()>0){
                CartMerchantResponseDto cartMerchantResponseDto = new CartMerchantResponseDto();
                cartMerchantResponseDto.setName(name);
                cartMerchantResponseDto.setMerchantId(merchantId);
                cartMerchantResponseDto.setProducts(list);
                Merchant merchant = merchantAction.getMerchantService().selectById(merchantId);
                cartMerchantResponseDto.setDistance(DistrictUtil.calcDistance(merchant.getLat().doubleValue(),merchant.getLon().doubleValue(),lat,lon));
                return cartMerchantResponseDto;
            }
        }
        return null;
    }
}
