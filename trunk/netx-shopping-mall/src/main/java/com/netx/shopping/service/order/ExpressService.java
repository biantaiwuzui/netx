package com.netx.shopping.service.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.order.SellerExpressMapper;
import com.netx.shopping.model.order.SellerExpress;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ExpressService extends ServiceImpl<SellerExpressMapper, SellerExpress> {

    /**
     * 根据type获取物流公司详情
     * @param type
     * @return
     */
    public SellerExpress getExpress(String type){
        EntityWrapper wrapper1=new EntityWrapper();
        wrapper1.where("type={0}", type);
        return this.selectOne(wrapper1);
    }

    public List<SellerExpress> getExpressList(){
        Wrapper<SellerExpress> wrapper = new EntityWrapper<>();
        wrapper.orderBy("popularity desc,type");
        return this.selectList(wrapper);
    }

    public SellerExpress selectByName(String name){
        Wrapper<SellerExpress> wrapper = new EntityWrapper<>();
        wrapper.eq("name",name);
        return this.selectOne(wrapper);
    }
}
