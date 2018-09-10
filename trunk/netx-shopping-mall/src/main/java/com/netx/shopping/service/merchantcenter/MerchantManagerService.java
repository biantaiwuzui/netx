package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.mapper.merchantcenter.MerchantManagerMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.model.merchantcenter.constants.MerchantManagerEnum;
import com.netx.shopping.vo.QueryManagerResponseDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网商-收银人员表 服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantManagerService extends ServiceImpl<MerchantManagerMapper, MerchantManager>{

    @Autowired
    private MerchantManagerMapper merchantManagerMapper;

    public String getUserId(String id, String merchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_id").where("id = {0} AND merchant_id = {1} AND deleted = 0",id,merchantId);
        return (String)this.selectObj(wrapper);
    }

    public List<String> getUseIdByMerchantId(String merchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_id").where("merchant_id = {0} AND deleted = {1}", merchantId, 0);
        return this.selectObjs(wrapper);
    }

    /* 根据userNumber获取merchantId **/
    public List<String> getMerchantIdByUserNetworkNum(String userNetworkNum){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("merchant_id").where("user_network_num = {0} AND deleted = 0", userNetworkNum).isNotNull("merchant_id");
        return this.selectObjs(wrapper);
    }

    public MerchantManager getMerchantManagerByUserNetworkNum(String userNetworkNums){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("user_network_num = {0} AND deleted = {1}", userNetworkNums, 0);
        return selectOne(wrapper);
    }

    public List<MerchantManager> getMerchantManagersByUserNetworkNum(String userNetworkNums) {
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("user_network_num = {0} AND deleted = {1}",  userNetworkNums, 0);
        return selectList(wrapper);
    }

    public List<MerchantManager> getMerchantManagerByMerchantId(String merchantId){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND is_current = 1 AND deleted = 0", merchantId).orderBy("merchant_user_type", false);
        return selectList(wrapper);
    }

    /**
     * 判断此用户是否商家管理员
     * @param merchantId
     * @param userNumber
     * @return
     */
    public int countByMerchantId(String merchantId,String userNumber){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} and user_network_num={1} AND deleted = 0", merchantId,userNumber);
        return selectCount(wrapper);
    }

    public List<Map<String,Object>> queryMerchantManagerByUserId(String userId, String merchantUserType, String merchantId){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        String sql = "max(id) as id, user_id AS userId, merchant_user_type AS merchantUserType, " +
                "user_name AS userName, user_phone AS userPhone, user_network_num AS userNetworkNum";
        wrapper.setSqlSelect(sql);
        wrapper.where("user_id = {0} AND merchant_user_type = {1} AND deleted = 0", userId, merchantUserType);
        if(StringUtils.isNotBlank(merchantId)) {
            wrapper.and("merchant_id = {0}", merchantId);
        }
        wrapper.groupBy("userId, merchantUserType, userName, userPhone, userNetworkNum");
        return selectMaps(wrapper);
    }

    public List<MerchantManager> getMerchantManagerByUserId(String userId){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} AND deleted = {1}", userId, 0);
        return selectList(wrapper);
    }

    public MerchantManager getMerchantManagerByMerchantIdAndMerchantUserType(String merchantId, String merchantUserType){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND merchant_user_type = {1} AND deleted = {2}", merchantId, merchantUserType, 0);
        return selectOne(wrapper);
    }

    public List<MerchantManager> getMerchantManagerByMerchantId(String merchantId, String merchantUserType){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND merchant_user_type = {1} AND deleted = {2}", merchantId, merchantUserType, 0);
        return selectList(wrapper);
    }

    public MerchantManager getNoCashierByUserNetworkNumAndMerchantId(String userNetworkNum, String merchantId){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("user_network_num = {0} AND merchant_user_type != {1} AND deleted = {2}", userNetworkNum, MerchantManagerEnum.CASHIER.getName(), 0);
        wrapper.and("merchant_id = {0}", merchantId);
        return selectOne(wrapper);
    }

    public String getUserNetworkNumByMerchantId(String merchantId, MerchantManagerEnum merchantManagerEnum){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_network_num");
        wrapper.where("merchant_id = {0} AND merchant_user_type = {1} AND deleted = {2}", merchantId, merchantManagerEnum.getName(), 0);
        return (String)selectObj(wrapper);
    }

    /**
     * 查询收银员与主管
     * @param merchantId
     * @param merchantManagerEnumNames
     * @return
     */
    public List<MerchantManager> getUserNetworkNumByMerchantId(String merchantId,String... merchantManagerEnumNames){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_network_num as userNetworkNum,merchant_user_type as merchantUserType");
        wrapper.and("merchant_id={0} and deleted=0",merchantId).in("merchant_user_type",merchantManagerEnumNames);
        return selectList(wrapper);
    }

    public Boolean updateIsCurrentByMerchantId(String merchantId, String merchantUserType){
        return merchantManagerMapper.updateIsCurrentByMerchantId(merchantId, merchantUserType);
    }

    public Boolean updateIsCurrentById(String id){
        return merchantManagerMapper.updateIsCurrentById(id);
    }

    /*网信**/
    public List<MerchantManager> getMerchantType (String merchantId){
        Wrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id={0}",merchantId);
        return selectList(wrapper);
    }
    public List<String> getMerchantUserTypesByCreditId(String merchantId,List<String> userId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("merchant_user_type");
        wrapper.in("merchant_id",merchantId);
        wrapper.in("user_id={0} ",userId);
        // wrapper.where("deleted = 0");
        return this.selectObjs(wrapper);
    }

    public List<MerchantManager> getMerchantUserTypeName(String merchantId,String userId){
        EntityWrapper<MerchantManager> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id={0} and user_id={1}",merchantId,userId);
        return selectList(wrapper);
    }

    // 网信发布 - 根据用户id 获取用户所在商家的商家id
    public List<String> getUserMerchantIds(String userId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("DISTINCT merchant_id");
        wrapper.where("user_id = {0}", userId);
        return selectList(wrapper);
    }
}
