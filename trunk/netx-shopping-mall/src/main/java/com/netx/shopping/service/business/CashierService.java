package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerCashierMapper;
import com.netx.shopping.model.business.SellerCashier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class CashierService extends ServiceImpl<SellerCashierMapper, SellerCashier> {

    @Autowired
    SellerCashierMapper sellerCashierMapper;
    /**
     * 删除
     * @param id
     * @return
     */
    public boolean delete(String id){
        EntityWrapper<SellerCashier> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0}", id);
        SellerCashier cashier = new SellerCashier();
        cashier.setDeleted(1);
        return this.update(cashier, wrapper);
    }

    /**
     * 获取列表
     * @param userId
     * @return
     */
    public List<SellerCashier> getCashierList(String userId){
        EntityWrapper<SellerCashier> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}", userId).andNew("deleted = {0}", 0);
        return this.selectList(wrapper);
    }

    /**
     * 修改收银人
     * @param userId
     * @return
     */
    public boolean updateCashier(SellerCashier cashier,String userId){
        EntityWrapper<SellerCashier> wrapper3 = new EntityWrapper<>();
        wrapper3.where("user_id={0}", userId);
        return this.update(cashier, wrapper3);
    }
    public boolean updateCashierByMoneyNetworkNum(SellerCashier cashier,String moneyNetworkNum){
        EntityWrapper wrapper8 = new EntityWrapper();
        wrapper8.where("money_network_num={0}", moneyNetworkNum);
        return this.update(cashier, wrapper8);
    }

    public SellerCashier selectCashierBySellerId(String sellerId){
        EntityWrapper<SellerCashier> wrapper = new EntityWrapper();
        wrapper.where("seller_id = {0} and is_current = 1",sellerId);
        return this.selectOne(wrapper);
    }

    public String getCashierIds(String moneyNetworkNum){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("GROUP_CONCAT(id) as id").where("money_network_num={0} and is_current = 1",moneyNetworkNum).groupBy("id");
        return (String) this.selectObj(wrapper);
    }
}
