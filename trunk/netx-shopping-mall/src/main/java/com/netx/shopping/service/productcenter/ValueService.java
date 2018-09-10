package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.productcenter.Value;
import com.netx.shopping.mapper.productcenter.ValueMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  属性值-服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class ValueService extends ServiceImpl<ValueMapper, Value> {

    /**
     * 根据商家id获取属性值
     * @param merchantId
     * @return
     */
    public List<Value> getValueByMerchantId(String merchantId){
        EntityWrapper<Value> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND deleted = {1}", merchantId, 0);
        return selectList(wrapper);
    }

    /**
     * 根据属性值名获取Value
     * @param name
     * @return
     */
    public Value getValueByName(String name){
        EntityWrapper<Value> wrapper = new EntityWrapper<>();
        wrapper.where("name = {0} AND deleted = {1}", name, 0);
        return selectOne(wrapper);
    }
    /**
     * 根据属性值id获取属性值名
     * @param ids
     * @return
     */
    public List<String> getNameById(List<String> ids){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("name");
        wrapper.in("id", ids);
        wrapper.where("deleted = {0}", 0);
        return selectObjs(wrapper);
    }

    public String getNamesById(List<String> ids){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("GROUP_CONCAT(name)");
        wrapper.in("id", ids);
        wrapper.where("deleted = {0}", 0);
        return (String) selectObj(wrapper);
    }

}
