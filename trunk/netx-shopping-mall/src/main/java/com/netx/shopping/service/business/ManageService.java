package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerManageMapper;
import com.netx.shopping.model.business.SellerManage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ManageService extends ServiceImpl<SellerManageMapper, SellerManage> {

    /**
     * 根据主管昵称获取主管id集
     * @param manageNetworkNum
     * @return
     */
    public String getManageIs(String manageNetworkNum){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("GROUP_CONCAT(id) as id").where("manage_network_num={0} and is_current = 1",manageNetworkNum).groupBy("id");
        return (String) this.selectObj(wrapper);
    }


    /**
     * 获取主管昵称
     * @param manageId
     * @return
     */
   public String getManageNetworkNum(String manageId){
       EntityWrapper wrapper1=new EntityWrapper();
       wrapper1.setSqlSelect("manage_network_num").where("id={0}",manageId);
       return (String) this.selectObj(wrapper1);
   }

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean delete(String id){
        EntityWrapper<SellerManage> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0}", id);
        SellerManage manage = new SellerManage();
        manage.setDeleted(1);
        return this.update(manage, wrapper);
    }

    /**
     * 获取列表
     * @param userId
     * @return
     */
    public List<SellerManage> getManageList(String userId){
        EntityWrapper<SellerManage> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}", userId).andNew("deleted = {0}", 0);
        return this.selectList(wrapper);
    }
    public List<SellerManage> getManageList(String manageNetworkNum,String userId){
        EntityWrapper<SellerManage> wrapper4 = new EntityWrapper<>();
        wrapper4.where("manage_network_num={0}",manageNetworkNum).notIn("user_id", userId);
        return this.selectList(wrapper4);
    }
    public List<String> getSellerIdList(String userId,String manageNetworkNum,int isCurrent){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("seller_id").where("manage_network_num={0} and is_current = {1}",manageNetworkNum,isCurrent).notIn("user_id", userId);
        return this.selectList(wrapper);
    }
    public List<SellerManage> selectManageBySellerId(String sellerId){
        EntityWrapper<SellerManage> wrapper = new EntityWrapper<>();
        wrapper.where("seller_id = {0}",sellerId);
        return this.selectList(wrapper);
    }
    /**
     * 修改主管
     * @param userId
     * @return
     */
    public boolean updateManage(SellerManage manage,String userId){
        EntityWrapper<SellerManage> wrapper2 = new EntityWrapper<>();
        wrapper2.where("user_id={0}", userId);
        return this.update(manage, wrapper2);
    }
    public boolean updateManage(SellerManage manage,String manageNetworkNum,String userId){
        EntityWrapper<SellerManage> wrapper4 = new EntityWrapper<>();
        wrapper4.where("manage_network_num={0}",manageNetworkNum).notIn("user_id", userId);
        return this.update(manage, wrapper4);
    }
    /**
     * 获取主管
     * @param sellerId
     * @return
     */
    public SellerManage getManageBySellerId(String sellerId){
        EntityWrapper<SellerManage> wrapper = new EntityWrapper<>();
        wrapper.where("seller_id = {0} and is_current = 1");
        return this.selectOne(wrapper);
    }

    /**
     * 根据主管id获取管理的商家id
     * @param userId
     * @return
     */
    public List<String> getManageIdByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("seller_id AS sellerId").where("user_id = {0} AND deleted = 0 ", userId).isNotNull("seller_id");
        return this.selectObjs(wrapper);
    }

}


