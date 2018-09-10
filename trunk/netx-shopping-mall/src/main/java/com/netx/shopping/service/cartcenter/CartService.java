package com.netx.shopping.service.cartcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.cartcenter.CartMapper;
import com.netx.shopping.model.cartcenter.Cart;
import com.netx.shopping.model.cartcenter.constants.CartItemStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  购物车服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-08
 */
@Service
public class CartService extends ServiceImpl<CartMapper, Cart> {

    public List<Cart> queryByUserId(String userId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_id={0}",userId);
        return selectList(wrapper);
    }

    public List<String> query(String userId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_id={0}",userId);
        wrapper.setSqlSelect("id");
        return selectObjs(wrapper);
    }

    public Cart query(String userId,String merchantId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_id={0} and merchant_id={1}",userId,merchantId);
        return selectOne(wrapper);
    }

    public Boolean delectByUserId(String userId,String id){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_id={0}",userId);
        if(StringUtils.isNotBlank(id)){
            wrapper.and("id={0}",id);
        }
        return delete(wrapper);
    }
}
