package com.netx.shopping.biz.cartcenter;


import com.netx.common.common.enums.ProductPictureTypeEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.biz.productcenter.*;
import com.netx.shopping.model.cartcenter.CartItem;
import com.netx.shopping.model.cartcenter.constants.CartItemStatusEnum;
import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.model.productcenter.Sku;
import com.netx.shopping.service.business.CategoryService;
import com.netx.shopping.service.cartcenter.CartItemService;
import com.netx.shopping.vo.CartItemListResponseDto;
import com.netx.shopping.vo.CartRequestDto;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  购物车信息ACTION
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-08
 */
@Service
public class CartItemAction {
    
    @Autowired
    private CategoryProductAction categoryProductAction;

    @Autowired
    private com.netx.shopping.service.productcenter.CategoryService categoryService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductAction productAction;

    @Autowired
    private SkuAction skuAction;

    @Autowired
    private ProductPictureAction productPictureAction;

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    private ProductSkuSpecAction productSkuSpecAction;

    public CartItemService getCartItemService() {
        return cartItemService;
    }

    /**
     * 添加详情
     * @param cartId
     * @param quantity
     * @param skuId
     * @param productId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean addCartItem(String cartId,Integer quantity,String skuId,String productId,Integer deliveryWay){
        Sku sku = skuAction.checkSkuNum(skuId, quantity);
        CartItem cartItem = cartItemService.query(cartId,productId,skuId,deliveryWay,CartItemStatusEnum.CIS_WATTING);
        if(cartItem==null){
            cartItem = createCartItem(cartId, quantity, skuId, productId,sku.getMarketPrice());
            cartItem.setDeliveryWay(deliveryWay);
           return cartItemService.insert(cartItem);
        }else{
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
            return cartItemService.updateById(cartItem);
        }
    }

    /**
     * 购物车中移除详情
     * @param id
     * @param cartId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeCartItem(String id,String cartId){
        int count = cartItemService.queryCount(id,cartId);
        if(count>0){
            return cartItemService.delByCartIdAndId(id,cartId);
        }
        return true;
    }

    public List<CartItemListResponseDto> queryByCartId(String cartId){
        List<CartItemListResponseDto> cartItemListResponseDtos = new ArrayList<>();
        List<CartItem> cartItems = cartItemService.queryByCartId(cartId,CartItemStatusEnum.CIS_WATTING);
        if(cartItems!=null && cartItems.size()>0){
            cartItems.forEach(cartItem -> {
                cartItemListResponseDtos.add(createCartItemListResponseDto(cartItem));
            });
        }
        return cartItemListResponseDtos;
    }

    /**
     * 购物车单个商品信息
     * @param cartItem
     * @return
     */
    private CartItemListResponseDto createCartItemListResponseDto(CartItem cartItem){
        CartItemListResponseDto cartItemListResponseDto = new CartItemListResponseDto();
        cartItemListResponseDto.setPrice(Money.CentToYuan(cartItem.getUnitPrice()).getAmount());
        cartItemListResponseDto.setId(cartItem.getId());
        cartItemListResponseDto.setCartId(cartItem.getCartId());
        cartItemListResponseDto.setProductId(cartItem.getProductId());
        Sku sku = skuAction.getSkuService().selectById(cartItem.getSkuId());
        if(sku!=null){
            cartItemListResponseDto.setPrice(Money.CentToYuan(sku.getPrice()).getAmount());
            cartItemListResponseDto.setMarketPrice(Money.CentToYuan(sku.getMarketPrice()).getAmount());
        }
        cartItemListResponseDto.setSkuId(cartItem.getSkuId());
        cartItemListResponseDto.setValue(productSkuSpecAction.getValueNames(cartItem.getSkuId()));
        cartItemListResponseDto.setQuantity(cartItem.getQuantity());
        Product product = productAction.getProductService().selectById(cartItem.getProductId());
        //获取类目
        List<String> categoryId = categoryProductAction.getCategoryIdByProductId(product.getId());
        if(categoryId != null && categoryId.size() > 0){
            List<Category> categories = categoryService.getParentCategory(categoryId);
            List<Category> tages = categoryService.getKidCategory(categoryId);
            if(categories != null && categories.size() > 0){
                cartItemListResponseDto.setCategories(categories);
            }
            if(tages != null && tages.size() > 0){
                cartItemListResponseDto.setTages(tages);
            }
        }
        cartItemListResponseDto.setCharacteristic(product.getCharacteristic());
        cartItemListResponseDto.setReturn(product.getReturn());
        cartItemListResponseDto.setName(product.getName());
        cartItemListResponseDto.setDeliveryWay(cartItem.getDeliveryWay());
        cartItemListResponseDto.setProductPic(addImgUrlPreUtil.getProductImgPre(productPictureAction.getProductPictureService().getPictureUrlOne(cartItem.getProductId(), ProductPictureTypeEnum.NONE)));
        return cartItemListResponseDto;
    }

    public boolean remove(List<String> cartId,CartItemStatusEnum cartItemStatusEnum){
        int count = cartItemService.countByCartId(cartId,cartItemStatusEnum);
        if(count>0){
            return cartItemService.delByCartId(cartId,cartItemStatusEnum);
        }
        return true;
    }

    /**
     * 修改购物详情
     * @param cartId
     * @param cartRequestDtos
     * @param cartItemStatusEnum
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCartItem(String cartId, List<CartRequestDto> cartRequestDtos,CartItemStatusEnum cartItemStatusEnum){
        for(CartRequestDto cartRequestDto:cartRequestDtos) {
            CartItem cartItem = createCartItem(cartId, cartRequestDto.getQuantity(), cartRequestDto.getSkuId(), cartRequestDto.getProductId(),null);
            cartItem.setId(cartRequestDto.getId());
            cartItem.setStatus(cartItemStatusEnum.name());
            cartItemService.updateCartItem(cartItem);
        }
        return true;
    }

    /**
     * 创建购物车详情
     * @param cartId
     * @param quantity
     * @param skuId
     * @param productId
     * @return
     */
    private CartItem createCartItem(String cartId, Integer quantity, String skuId, String productId, Long unitPrice){
        CartItem cartItem = new CartItem();
        cartItem.setCartId(cartId);
        cartItem.setQuantity(quantity);
        cartItem.setSkuId(skuId);
        cartItem.setProductId(productId);
        cartItem.setUnitPrice(unitPrice);
        cartItem.setStatus(CartItemStatusEnum.CIS_WATTING.name());
        return cartItem;
    }
}
