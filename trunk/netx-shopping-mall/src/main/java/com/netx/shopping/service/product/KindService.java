package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.product.SellerKindMapper;
import com.netx.shopping.model.product.SellerKind;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class KindService extends ServiceImpl<SellerKindMapper, SellerKind> {

    /**
     * 获取列表
     * @param userId
     * @return
     */
    public List<SellerKind> list(String userId){
        EntityWrapper<SellerKind> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and deleted = {1}", userId, 0);
        return this.selectList(wrapper);
    }

    public List<SellerKind> getKindlist(){
        EntityWrapper<SellerKind> kindEntityWrapper = new EntityWrapper<>();
        kindEntityWrapper.setSqlSelect("id, name");
        return this.selectList(kindEntityWrapper);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean delete(String id){
        EntityWrapper<SellerKind> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0}", id);
        SellerKind kind= new SellerKind();
        kind.setDeleted(1);
        return this.update(kind, wrapper);
    }
}
