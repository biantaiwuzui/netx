package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.vo.business.GetListByUserIdDto;
import com.netx.shopping.enums.ProductStatusEnum;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.mapper.productcenter.ProductMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.vo.QueryBusinessProductRequestDto;
import com.netx.shopping.vo.QueryProductListRequestDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网商-商品表 服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service("newProductService")
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    @Autowired
    ProductMapper productMapper;

    /**
     * 根据商家id获取商品
     * @param merchantId
     * @return
     */
    public List<Product> getProductByMerchantId(String merchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("merchant_id = {0} AND deleted = 0", merchantId);
        return selectList(wrapper);
    }

    /**
     * 根据商家ids获取商品
     * @param merchantIds
     * @return
     */
    public List<Product> getProductByMerchantIds(List<String> merchantIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("merchant_id", merchantIds).where("deleted = 0");
        return selectList(wrapper);
    }

    /**
     * 根据商家id获取商品
     * @param id
     * @return
     */
    public Product getProductById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("id={0}", id).and("deleted = 0");
        return selectOne(wrapper);
    }
    /*
    * 根据商家ids分页获取商品 
    */
    public List<Product> getProductByMerchantIds(List<String> merchantIds,Page page){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("merchant_id", merchantIds).where("deleted = 0");
        return selectPage(page,wrapper).getRecords();
    }
    
    
    /**
     * 根据商家id分页获取商品
     * @param requestDto
     * @return
     */
    public List<Product> getProductByMerchantId(QueryProductListRequestDto requestDto){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("merchant_id = {0} AND deleted = 0", requestDto.getMerchantId());
        Page page = new Page();
        page.setCurrent(requestDto.getCurrent());
        page.setSize(requestDto.getSize());
        return selectPage(page, wrapper).getRecords();
    }

    /**
     * 是否已发布过这个商品名
     * @param name
     * @param merchantId
     * @return
     */
    public Product isHaveThisName(String name, String merchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("name = {0} AND merchant_id = {1} AND deleted = 0", name, merchantId);
        return selectOne(wrapper);
    }

    /**
     * 根据商家id获取商品id集
     * @param merchantId
     * @return
     */
    public List<String> getProductIdsByMerchantIds(String merchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id");
        wrapper.where("merchant_id = {0} AND deleted = 0", merchantId);
        wrapper.orderBy("create_time", false);
        return selectObjs(wrapper);
    }


    /**
     * 根据商品id集获取商家id集
     * @param productIds
     * @return
     */
    public List<String> getMerchantIdsByProductIds(List<String> productIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("merchant_id");
        wrapper.in("id",productIds);
        wrapper.groupBy("merchant_id");
        return selectObjs(wrapper);
    }

    /**
     * 获取某个用户经营的商品(包含下架的)
     * @param merchantIds
     * @return
     */
    public int getAllProductCount(List<String> merchantIds){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.in("merchant_id", merchantIds).where("deleted={0}",0);//返回商品总数量，包含下架商品
        return selectCount(wrapper);
    }

    public int getProductCountByMerchantId(String merchantId){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id={0} and deleted={1}",merchantId,0);//返回商品总数量，包含下架商品
        return selectCount(wrapper);
    }

    /**
     * 获取商家上架商品数量
     * @param merchantIds
     * @return
     */
    public int getAllUpProductCount(List<String> merchantIds){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.in("merchant_id", merchantIds).where("deleted={0}",0);//返回商品总数量，包含下架商品
        return selectCount(wrapper);
    }

    /**
     * 获取某个用户发布的商品
     * @param userId
     * @return
     */
    public int getPublishProductCount(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("publisher_user_id = {0} AND deleted = 0",userId);
        return this.selectCount(wrapper);
    }

    public int getProductCountByMerchantIdAndCreateTime(String merchantId, int status, Date date){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} and status = {1} create_time > {2}", merchantId, status, date);
        return this.selectCount(wrapper);
    }

    public String getProductNameById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("name").where("id = {0} AND deleted = {1}", id, 0);
        return (String) selectObj(wrapper);
    }

    public List<Map<String,String>> selectGoodsList(){
        return productMapper.selectGoodsList();
    }

    /**
     * 模糊查询商品列表
     * @param merchantIds
     * @param requestDto
     * @return
     */
    public Page<Product> selectProductList(List<String> merchantIds, QueryBusinessProductRequestDto requestDto){
        EntityWrapper<Product> wrapper=new EntityWrapper<>();
        Page<Product> page = new Page<>();
        page.setCurrent(requestDto.getCurrentPage());
        page.setSize(requestDto.getSize());
        if(merchantIds != null && merchantIds.size() > 0){
            wrapper.in("merchant_id", merchantIds);
        }
        wrapper.where("deleted = 0");
        if(requestDto.getOnlineStatus() != null){
            wrapper.and("online_status = {0}", requestDto.getOnlineStatus());
        }
        if(StringUtils.isNotBlank(requestDto.getProductName())){
            wrapper.like("name", requestDto.getProductName());
        }
        wrapper.orderBy("create_time",false);
        return this.selectPage(page, wrapper);
    }

    /**
     * boss
     * 后台强制改变商品状态(更新状态为3.强制下架,2下架,1上架)
     * @param productId
     */
    public boolean forceChangeProductOnlineStatusByProductId(String productId, Integer onlineStatus){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0} AND deleted = 0", productId);
        Product product = new Product();
        product.setOnlineStatus(onlineStatus);
        return this.update(product, wrapper);
    }

    public Integer getDeliveryWay(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("delivery_way").where("id = {0} AND deleted = 0", id);
        return (Integer)selectObj(wrapper);
    }

}