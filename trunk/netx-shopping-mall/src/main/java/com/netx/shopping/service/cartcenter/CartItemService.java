package com.netx.shopping.service.cartcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.cartcenter.CartItemMapper;
import com.netx.shopping.model.cartcenter.CartItem;
import com.netx.shopping.model.cartcenter.constants.CartItemStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  购物车详情服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-08
 */
@Service
public class CartItemService extends ServiceImpl<CartItemMapper, CartItem> {

    public int queryCount(String id, String cartId){
        return selectCount(createWrapper(id, cartId));
    }

    public CartItem query(String id,CartItemStatusEnum cartItemStatusEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("id = {0}",id);
        if(cartItemStatusEnum!=null){
            wrapper.and("status={0}",cartItemStatusEnum.name());
        }
        return selectOne(wrapper);
    }

    public CartItem query(String cartId, String productId, String skuId,Integer deliveryWay, CartItemStatusEnum cartItemStatusEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("cart_id={0} and product_id={1} and sku_id={2} and delivery_way={3}",cartId,productId,skuId,deliveryWay);
        if(cartItemStatusEnum!=null){
            wrapper.and("status={0}",cartItemStatusEnum.name());
        }
        return selectOne(wrapper);
    }

    private Wrapper createWrapper(String id, String cartId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("cart_id={0}",cartId);
        if(StringUtils.isNotBlank(id)){
            wrapper.and("id={0}",id);
        }
        return wrapper;
    }

    public boolean delByCartIdAndId(String id, String cartId){
        return delete(createWrapper(id, cartId));
    }

    public boolean delByCartId(List<String> cartId,CartItemStatusEnum cartItemStatusEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.in("cart_id",cartId);
        if(cartItemStatusEnum!=null){
            wrapper.and("status={0}",cartItemStatusEnum.name());
        }
        return delete(wrapper);
    }

    public int countByCartId(List<String> cartId,CartItemStatusEnum cartItemStatusEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.in("cart_id",cartId);
        if(cartItemStatusEnum!=null){
            wrapper.and("status={0}",cartItemStatusEnum.name());
        }
        return selectCount(wrapper);
    }

    public boolean updateCartItem(CartItem cartItem){
        return update(cartItem,createWrapper(cartItem.getId(), cartItem.getCartId()));
    }

    public List<CartItem> queryByCartId(String cartId,CartItemStatusEnum cartItemStatusEnum){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("cart_id={0}",cartId);
        if(cartItemStatusEnum!=null){
            wrapper.and("status={0}",cartItemStatusEnum.name());
        }
        return selectList(wrapper);
    }
}
