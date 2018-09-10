package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.business.*;
import com.netx.shopping.enums.PromotionAwardEnum;
import com.netx.shopping.mapper.business.SellerMapper;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.vo.SellerAgentDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class SellerService extends ServiceImpl<SellerMapper, Seller> {

    @Autowired
    SellerMapper sellerMapper;

    /**
     * 根据userId获取商家id集
     * @param userId
     * @return
     */
    public List<String> getSellersIdList(String userId){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id").where("user_id = {0}",userId);

        return this.selectObjs(wrapper);
    }

    /**
     * 根据userId获取商家列表
     * @param userId
     * @return
     */
    public List<Seller> getSellerListByUserId(String userId){
        EntityWrapper<Seller> wrapper=new EntityWrapper<>();
        wrapper.where("user_id={0}",userId);
        return this.selectList(wrapper);
    }

    /**
     * 根据商家Id获取包装明细id
     * @param id
     * @return
     */
    public String getPacSetId(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("pac_set_id").eq("id", id);
        return (String) this.selectObj(wrapper);
    }

    public Seller getSellerByCreateTime(String userId){
        Wrapper<Seller> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and pay_status = 0 and deleted = 0", userId);
        wrapper.orderBy("create_time");
        return this.selectOne(wrapper);
    }

    public Seller getAreaFristBusiness(String provinceCode, String areaName){
        Wrapper<Seller> wrapper = new EntityWrapper<>();
        wrapper.eq("province_code", provinceCode);
        if (areaName == null || areaName.isEmpty()) {
            wrapper.isNull("city_code");
        } else {
            wrapper.eq("city_code", areaName);
        }
        return this.selectOne(wrapper.orderBy("create_time"));
    }

    public Seller checkCustomerCode(String customerCode){
        return this.selectOne(new EntityWrapper<Seller>().eq("customer_code", customerCode));
    }

    public Integer getJobNum(PromotionAwardEnum awardEnum, String city){
        Wrapper<Seller> wrapper = new EntityWrapper<>();
        wrapper.where("job = {0} and pay_status = 0 and deleted = 0 and city_code = {1}", awardEnum.getJob(), city);
        return this.selectCount(wrapper);
    }

    /**
     * 获取商家列表
     * @param
     * @return
     */
    public List<Seller> getSellerList(){
        Wrapper<Seller> wrapper = new EntityWrapper<>();
        return this.selectList(wrapper);
    }
    public List<Seller> getSellerList(String userId,int status){
        EntityWrapper<Seller> wrapper = new EntityWrapper();
        wrapper.where("user_id={0} and status={1} and deleted = 0 and pay_status !=1",userId, status);
        return this.selectList(wrapper);
    }
    public List<Seller> getSellerList(List<String> sellerIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id", sellerIds);
        return this.selectList(wrapper);
    }
    public List<Seller> getSellerList(String moneyNetworkNum,String userId){
        EntityWrapper<Seller> wrapper6 = new EntityWrapper<>();
        wrapper6.where("money_network_num={0}",moneyNetworkNum).notIn("user_id", userId);
        return this.selectList(wrapper6);
    }
    public List<Seller> getSellerList(List<String> sellerIds,String orderBy){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id", sellerIds).orderBy("(second_num+third_num) desc");
        return this.selectList(wrapper);
    }
    public List<Seller> getSellerListHavesetSqlSelect(){
        EntityWrapper<Seller> sellerWrapper = new EntityWrapper<>();
        sellerWrapper.setSqlSelect("id, name, category_id");
        return selectList(sellerWrapper);
    }
    public List<Seller> selectPage(SelectSellerListByDistanceAndTimeRequestDto requestDto,BigDecimal lon,BigDecimal lat,int length){
        //TODO 距离WRAPPER
        //Wrapper<Seller> wrapper = DistrictUtil.buildEntityWrapper("geohash", DistrictUtil.getHashAdjcent(lat, lon, length));
        Wrapper<Seller> wrapper = new EntityWrapper<>();//暂时逻辑，保持完整
        wrapper.orderBy("create_time");
        wrapper.andNew("category_id={0}", requestDto.getCategoryId());
        Page<Seller> page = new Page<>(requestDto.getCommonListDto().getCurrentPage(), requestDto.getCommonListDto().getSize());
        this.selectPage(page, wrapper);
        return page.getRecords();
    }

    /**
     * 获取商家详情
     * @param
     * @return
     */
    public Seller getSeller(String sellerId){
        EntityWrapper<Seller> wrapper = new EntityWrapper<>();
        wrapper.where(" id = {0} and deleted={1}", sellerId, 0);
        return this.selectOne(wrapper);
    }
    public Seller getSeller(String userId,int status){
        EntityWrapper<Seller> wrapper = new EntityWrapper();
        wrapper.where("user_id={0} and status={1}", userId, status).orderBy("create_time", false);
        return this.selectOne(wrapper);
    }
    public Seller getSellerBySellerIds(String sellerIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").in("id", sellerIds).where("status={0} and deleted={1}", 1, 0).orderBy("create_time", false);;
        return this.selectOne(wrapper);
    }
    public Seller getSeller(String sellerIds,int status,String deleted){
        EntityWrapper<Seller> wrapper = new EntityWrapper();
        wrapper.where("id={0}",sellerIds).andNew("status={1} and deleted={2}", status, 0);
        return this.selectOne(wrapper);
    }
    /**
     * 获取商家详情是否有此商家名
     * @param
     * @return
     */
    public Seller isHaveThisName(String userId, String name){
        EntityWrapper<Seller> wrapper = new EntityWrapper<>();
        wrapper.where("name={0} and status={1}", name, 1).andNew("deleted={0} and user_id={1}", 0, userId);
        return this.selectOne(wrapper);
    }

    /**
     * 修改商家
     * @param
     * @return
     */
    public boolean updateSeller(Seller seller,String userId){
        EntityWrapper<Seller> wrapper1 = new EntityWrapper<>();
        wrapper1.where("user_id={0}", userId);
        return this.update(seller, wrapper1);
    }
    /**
     * 根据主管id修改商家
     * @param
     * @return
     */
    public boolean updateSellerByManageId(Seller seller,String manageId){
        EntityWrapper<Seller> wrapper5 = new EntityWrapper<>();
        wrapper5.where("manage_id={0}",manageId);
        return this.update(seller, wrapper5);
    }

    public Seller isCanRegisterSeller(RegisterSellerRequestDto request){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("user_id={0} and name={1}", request.getUserId(), request.getName())
                .andNew("province_code={0}",request.getProvinceCode())
                .andNew("city_code={0}and area_code={1} ", request.getCityCode(), request.getAreaCode())
                .andNew("addr_country={0} and addr_detail={1}", request.getAddrCountry(), request.getAddrDetail())
                .andNew("addr_unit_name={0} and deleted={1}", request.getAddrUnitName(), 0);
        return this.selectOne(wrapper);
    }

    /**
     * 获取分页列表
     * @param
     * @return
     */
    public Page<Seller> getPageList(BackManageSellerListRequestDto request){
        EntityWrapper<Seller> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(request.getName())) {
            wrapper.like("name", request.getName());
        }
        wrapper.where("status = {0}", request.getStatus()).andNew("deleted = {0}", 0);
        Page<Seller> page = new Page<>();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrent());
        return this.selectPage(page, wrapper);
    }

    /**
     * 获取商家数量
     * @param
     * @return
     */
    public int getSellerCount(String sellerIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id", sellerIds).where("status={0} and deleted={1}", 1, 0);
        return this.selectCount(wrapper);
    }
    public int getSellerCount(String userId,int status){
        EntityWrapper<Seller> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}and status={1}", userId, status);
        return this.selectCount(wrapper);
    }
    public int getSellerCountByUserId(String userId){
        EntityWrapper<Seller> wrapper1 = new EntityWrapper<>();
        wrapper1.where("user_id = {0}", userId);
        return this.selectCount(wrapper1);
    }

    /**
     * 删除
     * @param
     * @return
     */
    public boolean delete(String sellerId){
        EntityWrapper<Seller> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0}", sellerId);
        Seller seller = new Seller();
        seller.setDeleted(1);

        return this.update(seller, wrapper);
    }


    /**
     * 获取商家id集
     * @param
     * @return
     */
    public String getSellerIds(String userId,String[] cashierIds,String[] manageIds){
        return sellerMapper.getAllGoodsOderByUserId(userId,cashierIds,manageIds);
    }

    public List<Seller> getMyManageSeller(GetListByUserIdDto getListByUserIdDto, String[] manageIds){
        /*EntityWrapper<Seller> wrapper = new EntityWrapper<>();
        Page<Seller> page = new Page<>();
        page.setCurrent(getListByUserIdDto.getCurrent());
        page.setSize(getListByUserIdDto.getSize());
        wrapper.in("manage_id", manageIds).or("user_id = {0}",getListByUserIdDto.getUserId()).where("deleted = 0");
        //wrapper.in("manage_id",manageIds).where("user_id = {0} AND deleted = 0", getListByUserIdDto.getUserId());
        wrapper.orderBy("create_time",false);
        return this.selectPage(page, wrapper).getRecords();*/
        Integer size = getListByUserIdDto.getSize();
        Integer current = (getListByUserIdDto.getCurrent()-1)*size;
        return sellerMapper.getRelatedSellersNotMoneyUserByUserId(getListByUserIdDto.getUserId(),manageIds,current, size);
    }

    public List<Seller> getMyFavoriteSeller(List<String> sellerIds, Integer current, Integer size){
        EntityWrapper<Seller> wrapper = new EntityWrapper<>();
        Page<Seller> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        wrapper.where("deleted != 1").in("id",sellerIds).orderBy("create_time",false);
        return this.selectPage(page, wrapper).getRecords();
    }

    public List<SellerAgentDto> getMapList(String userId){
        return sellerMapper.selectSellersAndRandNo(userId);
    }

    public Long selectSellerAndRandNo(String id,String provinceCode,String cityCode){
        return sellerMapper.selectSellerAndRandNo(id,provinceCode,cityCode);
    }

    public boolean updateDayNum(){
        return sellerMapper.updateDayNum();
    }

    public boolean updateSellerRedpacket(){
        return sellerMapper.updateSellerRedpacket();
    }

    public boolean emptySellerRedpacket(){
        return sellerMapper.emptySellerRedpacket();
    }

    public List<String> getSellerIdByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id AS sellerId").where("user_id = {0} AND deleted = 0", userId).isNotNull("id");
        return this.selectObjs(wrapper);
    }

    /**lcx boss*/
    public Page<Map<String,Object>> getSellerList(String name,Integer currentPage,Integer size,Integer status){
        Page page = new Page();
        page.setCurrent(currentPage);
        page.setSize(size);
        Wrapper<Seller> wrapper = new EntityWrapper();
        wrapper.where("status={0}",status);
        if(StringUtils.isNotBlank(name)){
            wrapper.like("name",name);
        }
        return selectMapsPage(page, wrapper);
    }

    public List<Seller> getSellerWhite(){
        Wrapper<Seller> wrapper = new EntityWrapper();
        wrapper.orderBy("create_time",false);
        return selectList(wrapper);
    }

    public boolean defriend(SellerStatusRequestDto request){
        Seller seller = new Seller();
        seller.setId(request.getId());
        seller.setBackReason(request.getBackReason());
        seller.setStatus(request.getStatus());
        return updateById(seller);
    }

    public boolean overBack(SellerStatusRequestDto request){
        Seller seller = new Seller();
        seller.setId(request.getId());
        seller.setOverReason(request.getOverReason());
        seller.setStatus(request.getStatus());
        return updateById(seller);
    }
    /**
     * 根据商家id获取商家名称
     * @param id
     * @return
     */
    public String getNameById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("name").where("id = {0} AND deleted = 0", id);
        return (String) this.selectObj(wrapper);
    }
}
