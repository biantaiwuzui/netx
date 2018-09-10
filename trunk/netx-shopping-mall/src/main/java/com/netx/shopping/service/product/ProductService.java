package com.netx.shopping.service.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.business.BackManageGoodsListRequestDto;
import com.netx.common.vo.business.GetListBySellerIdDto;
import com.netx.shopping.enums.ProductStatusEnum;
import com.netx.shopping.mapper.product.ProductMapper;
import com.netx.shopping.model.product.Product;

import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by CloudZou on 3/1/2018.
 */
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    @Autowired
    ProductMapper productMapper;

    public List<Product> getProductByName(BackManageGoodsListRequestDto request){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotEmpty(request.getName())){
            wrapper.like( "name", request.getName());
        }
        wrapper.where("status = {0}", request.getStatus()).andNew("deleted = {0}", 0);
        Page<Product> page = new Page<>();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrent());
        Page<Product> resultPage = selectPage(page, wrapper);
        return resultPage.getRecords();
    }

    /**
     * 根据商家id获取商品列表
     * @param sellerId
     * @return
     */
    public List<Product> getProductList(String sellerId){
        EntityWrapper<Product> wrapper = new EntityWrapper();
        wrapper.where("seller_id={0}", sellerId);
        return this.selectList(wrapper);
    }
    /**
     * 获取某个用户发布的商品(包含下架的)
     * @param userId
     * @return
     */
    public int getAllProductCount(String userId){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and deleted={1}", userId,0);//返回商品总数量，包含下架商品
        return selectCount(wrapper);
    }

    /**
     * 获取某个用户发布的商品(只包含上架的)
     * @param userId
     * @return
     */
    public int getAllUpProductCount(String userId){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and status = {1}", userId, ProductStatusEnum.UP.getCode() ).andNew("deleted={0}",0);
        return this.selectCount(wrapper);
    }

    /**
     * 获取某个用户最新发布的一个商品
     * @param userId
     * @return
     */
    public Product getLatestProduct(String userId){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}", userId).orderBy("create_time",false);
        return selectOne(wrapper);
    }

    /**
     * 删除商品
     * @param id
     * @return
     */
    public Boolean delete(String id){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("id = {0}",id);
        Product product = new Product();
        product.setDeleted(1);
        return this.update(product, wrapper);
    }


    /**
     * 根据userid注销商品
     * @param userId
     * @return
     */
    public Boolean deleteByUserId(String userId){
        Product product = new Product();
        product.setStatus(ProductStatusEnum.DOWN.getCode());
        product.setDeleted(1);
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}", userId);
        return this.update(product, wrapper);
    }

    /**
     * 下架商家所以商品
     * @param sellerId
     * @return
     */
    public void downSellerAllGoods(String sellerId){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("seller_id = {0}", sellerId);
        Product product = new Product();
        product.setStatus(ProductStatusEnum.DOWN.getCode());
        product.setDownReason("商家注销");
        this.update(product, wrapper);
    }

    /**
     * 根据商家id集获取商品数量
     * @param sellerIds
     * @return
     */
    public int getProductCountBySellerId(List<String> sellerIds){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.in("seller_id",sellerIds);
        return this.selectCount(wrapper);
    }
    public int getProductCountBySellerId(String sellerId){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("seller_id={0}", sellerId);
        return this.selectCount(wrapper);
    }
    public int getProductCountBySellerIdAndStatus(String sellerId,int status){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.where("seller_id={0} and status={1}", sellerId,status);
       return this.selectCount(wrapper);
    }

    public int getProductCountBySellerIdAndCreateTime(String sellerId, int status, Date date){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.where("seller_id={0} and status={1} create_time>{2}", sellerId,status,date);
        return this.selectCount(wrapper);
    }
    /**
     * 根据状态和名称获取商品列表
     * @param status
     * @param name
     * @return
     */
    public List<Product> getProductsByStatusAndName(Integer status,String name){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("status={0}",status).like("name",name);
        return this.selectList(wrapper);
    }

    /**
     * 根据商家id获取商品id集
     * @param sellerId
     * @return
     */
    public List<String> getProductIdsBySellerId(String sellerId){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("id").where("seller_id={0} and status={1}",sellerId, ProductStatusEnum.UP.getCode());
        return this.selectObjs(wrapper);
    }

    /**
     * 判断某人是否已发行过此商品名称
     * @param name
     * @return
     */
    public int isHaveThisName(String userId, String name){
        EntityWrapper<Product> wrapper=new EntityWrapper<>();
        wrapper.where("name={0} and status={1}",name,1).andNew("deleted={0} and user_id={1}",0,userId);
        return this.selectCount(wrapper);
    }

    /**
     * 根据商品包装id获取商品数量
     * @param packageId
     * @return
     */
    public int getProductCountByPackageId(String packageId){
        EntityWrapper<Product> wrapper=new EntityWrapper<>();
        wrapper.where("package_id={0} and status={1}",packageId, ProductStatusEnum.UP.getCode()).andNew("deleted = 0");
        return this.selectCount(wrapper);
    }

    public BigDecimal getGoodsDealAmount(String goodsId){
        BigDecimal b= new BigDecimal(Money.getMoneyString(productMapper.getGoodsDealAmount(goodsId)==null?0:productMapper.getGoodsDealAmount(goodsId)));
        return b;
    }

    public List<Map<String,String>> selectGoodsList(){
        return productMapper.selectGoodsList();
    }

    /**
     * 根据productId获取商品列表
     * @param productIds
     * @param current
     * @param size
     * @return
     */
    public List<Product> selectProductListByProdectId(List<String> productIds, Integer current, Integer size){
        EntityWrapper<Product> wrapper=new EntityWrapper<>();
        Page<Product> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        wrapper.in("id",productIds).where("deleted = 0").orderBy("create_time",false);
        return this.selectPage(page, wrapper).getRecords();
    }

    /**
     * 根据userId获取用户发布的商品列表
     * @param userId
     * @param current
     * @param size
     * @return
     */
    public List<Product> selectProductListByUserId(String userId, Integer current, Integer size){
        EntityWrapper<Product> wrapper=new EntityWrapper<>();
        Page<Product> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        wrapper.where("user_id = {0} AND deleted = 0",userId).orderBy("create_time",false);
        return this.selectPage(page, wrapper).getRecords();
    }

    public Map<String, Object> getNameAndProductImagesUrlById(String id){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("name AS productName, product_images_url AS productImagesUrl, delivery_way AS deliveryWay, is_delivery AS isDelivery, is_return AS isReturn").where("id = {0} AND deleted = 0", id);
        return this.selectMap(wrapper);
    }

    public List<Product> selectBySellerId(GetListBySellerIdDto getListBySellerIdDto){
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        Page<Product> page = new Page<>();
        page.setCurrent(getListBySellerIdDto.getCurrent());
        page.setSize(getListBySellerIdDto.getSize());
        wrapper.where("seller_id = {0} AND deleted = 0", getListBySellerIdDto.getSellerId()).orderBy("create_time",false);
        return this.selectPage(page, wrapper).getRecords();
    }
}
