package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.vo.business.SelectSellerListByInPublicCreditDto;
import com.netx.shopping.enums.PromotionAwardEnum;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.mapper.merchantcenter.MerchantMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.vo.RegisterMerchantRequestDto;
import com.netx.shopping.vo.SellerAgentDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantService extends ServiceImpl<MerchantMapper, Merchant>{

    @Autowired
    MerchantMapper merchantMapper;

    public List<Map<String,Object>> queryMerchantIds(){
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.where("deleted=0");
        wrapper.setSqlSelect("user_id as userId,GROUP_CONCAT(id) as ids");
        wrapper.groupBy("userId");
        return selectMaps(wrapper);
    }

    public Merchant isCanRegisterMerchant(RegisterMerchantRequestDto request){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("user_id={0} and name={1}", request.getUserId(), request.getName())
                .andNew("province_code={0}",request.getProvinceCode())
                .andNew("city_code={0} and area_code={1} ", request.getCityCode(), request.getAreaCode())
                .andNew("addr_country={0} and addr_detail={1}", request.getAddrCountry(), request.getAddrDetail())
                .andNew("deleted={0}", 0);
        return this.selectOne(wrapper);
    }

    public Merchant checkCustomerServiceCode(String customerServiceCode){
        return this.selectOne(new EntityWrapper<Merchant>().eq("customer_service_code", customerServiceCode));
    }

    /**
     * 根据id获取商家信息
     * @param ids
     * @param current
     * @param size
     * @return
     */
    public List<Merchant> getMerchantByIds(List<String> ids, Integer current, Integer size){
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        Page<Merchant> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        wrapper.in("id", ids).where("deleted = 0").orderBy("create_time", false);
        return this.selectPage(page, wrapper).getRecords();
    }

    public Merchant queryMerchant(String id,int status,Boolean deleted){
        EntityWrapper<Merchant> wrapper = new EntityWrapper();
        wrapper.where("id={0} and status={1} and deleted={2}",id,status,deleted?0:1);
        return this.selectOne(wrapper);
    }

    /**
     * 根据用户id查询商家id
     * @param userId
     * @return
     */
    public List<String> getIdByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").where("user_id = {0} AND deleted = {1}", userId, 0);
        return this.selectObjs(wrapper);
    }

    /**
     * 根据merchantIds查询最新一个商家
     * @param merchantIds
     * @return
     */
    public Merchant getNewestMerchantById(List<String> merchantIds){
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.in("id", merchantIds).where("deleted = {0}", 0).orderBy("create_time",false);
        return selectOne(wrapper);
    }

    /**
     * 根据userId查询最新一个商家
     * @param userId
     * @return
     */
    public Merchant getNewestMerchantByUserId(String userId){
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} AND deleted = {1}",userId, 0).orderBy("create_time",false);
        return selectOne(wrapper);
    }

    /**
     * 根据userId查询metchantId
     * @param userId
     * @return
     */
    public List<String> getNewestMerchantByUserId(List<String> userId){
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id");
        wrapper.in("user_id",userId);
        return (List<String>)(List)this.selectObjs(wrapper);
    }

    /**
     * 根据用户id查询_现有_注册的商店数
     * @param userId
     * @param status
     * @return
     */
    public Integer getNowCountByUserIdAndStatus(String userId, Integer status){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("user_id = {0} AND status = {1} AND deleted = {2}", userId, status, 0);
        return selectCount(wrapper);
    }

    /**
     * 根据用户id查询_总_注册过的商店数
     * @param userId
     * @return
     */
    public Integer getSumCountByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("user_id = {0}", userId);
        return selectCount(wrapper);
    }

    /**
     * 根据userId获取现有的注册商家
     * @param userId
     * @param status
     * @return
     */
    public List<Merchant> getMerchantByUserId(String userId, Integer status){
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} AND status = {1} AND deleted = {2}", userId, status, 0);
        return selectList(wrapper);
    }

    /**
     * 根据商家名查询商家信息
     * @param name
     * @return
     */
    public Merchant getMerchantByName(String name){
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.where("name = {0}", name);
        return selectOne(wrapper);
    }

    /**
     * 根据引荐人客服代码查询商家信息
     * @param customerServiceCode
     * @return
     */
    public Merchant getMerchantByCustomerServiceCode(String customerServiceCode){
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.where("customer_service_code = {0} AND deleted = 0 AND status = 1", customerServiceCode);
        return selectOne(wrapper);
    }

    /**
     * 根据商家id获取用户id
     * @param id
     * @return
     */
    public String getUserIdById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_id").where("id = {0} AND deleted = {1}", id, 0);
        return (String)selectObj(wrapper);
    }

    /**
     * 获取红包设置
     * @param id
     * @return
     */
    public String getPacSetId(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("pac_set_id").eq("id", id);
        return (String) this.selectObj(wrapper);
    }

    /**
     * 根据商家id获取商家名
     * @param id
     * @return
     */
    public String getMerchantNameById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("name").where("id = {0} AND deleted = {1}", id, 0);
        return (String) this.selectObj(wrapper);
    }

    /**
     * 根据商家id获取商家名
     * @param id
     * @return
     */
    public Merchant getMerchantById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("id = {0} AND deleted = {1}", id, 0);
        return selectOne(wrapper);
    }
    /**
     * --boss系统--
     * 根据商家名称查询（黑/白）名单商家
     * @param name
     * @param currentPage
     * @param size
     * @param status
     * @return
     */
    public Page<Map<String,Object>> getMerchantList(String name, Integer currentPage, Integer size, Integer status){
        Page page = new Page();
        page.setCurrent(currentPage);
        page.setSize(size);
        Wrapper<Merchant> wrapper = new EntityWrapper();
        wrapper.where("status = {0} AND deleted = 0", status);
        if(StringUtils.isNotBlank(name)){
            wrapper.like("name",name);
        }
        return selectMapsPage(page, wrapper);
    }

    /**
     * --boss系统--
     * 获取商家黑名单/白名单
     * @param status
     * @return
     */
    public List<Merchant> getMerchantByStatus(Integer status){
        Wrapper<Merchant> wrapper = new EntityWrapper();
        if(status != null) {
            wrapper.where("status = {0}", status);
        }
        wrapper.where("deleted = 0");
        wrapper.orderBy("create_time",false);
        return selectList(wrapper);
    }

    /**
     * 获取数量
     * @param merchantId
     * @param parentMerchantId
     * @return
     */
    public int getAddCustomeragentCount(String merchantId, String parentMerchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("id = {0} AND parent_merchant_id = {1} AND deleted = 0", merchantId, parentMerchantId);
        return this.selectCount(wrapper);
    }

    public Integer getPosition(PromotionAwardEnum awardEnum, String city){
        Wrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.where("position = {0} and pay_status = 0 and deleted = 0 and city_code = {1}", awardEnum.getJob(), city);
        return this.selectCount(wrapper);
    }

    /**
     * 获取商家列表
     * @return
     */
    public List<Merchant> getMerchantList(){
        Wrapper<Merchant> wrapper = new EntityWrapper();
        return selectList(wrapper);
    }

    /**
     * 根据父商家id获取商家列表
     * @param parentMerchantId
     * @return
     */
    public List<Merchant> getMerchantListByParentMerchantId(String parentMerchantId){
        Wrapper<Merchant> wrapper = new EntityWrapper();
        wrapper.where("parent_merchant_id = {0} AND deleted = 0", parentMerchantId).orderBy("(second_num+third_num) desc");
        return selectList(wrapper);
    }

    public List<SellerAgentDto> getMapList(String userId){
        return merchantMapper.selectMerchantsAndRandNo(userId);
    }

    public Long selectMerchantAndRandNo(String id,String provinceCode,String cityCode){
        return merchantMapper.selectMerchantAndRandNo(id,provinceCode,cityCode);
    }

    public boolean updateDayNum(){
        return merchantMapper.updateDayNum();
    }

    /**boss
     * 根据模糊商家名,获取商家id列表
     * @param merchantName
     * @return
     */
    public List<String> queryMerchantIdByName(String merchantName){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").where("deleted = 0");
        if(StringUtils.isNotBlank(merchantName)){
            wrapper.like("name", merchantName);
        }
        return this.selectObjs(wrapper);
    }


    /**
     * 网信 - 获取商家（必须完成缴费，必选支持网信）
     */
    public List<Merchant> selectMerchantListInPublicCredit(SelectSellerListByInPublicCreditDto requestDto) {
        Wrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.andNew("pay_status={0} and is_support_credit={1}", requestDto.getPayStatus(), requestDto.getHoldCredit());
        return selectList(wrapper);
    }

}
