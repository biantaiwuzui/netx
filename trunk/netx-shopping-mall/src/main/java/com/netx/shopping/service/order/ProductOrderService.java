package com.netx.shopping.service.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.business.GetGoodsOrderListRequestDto;
import com.netx.common.vo.business.GetGoodsOrderRequestDto;
import com.netx.common.vo.business.UpdateOrderRequestDto;
import com.netx.shopping.enums.OrderStatusEnum;
import com.netx.shopping.mapper.order.ProductOrderMapper;
import com.netx.shopping.model.order.ProductOrder;
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
public class ProductOrderService extends ServiceImpl<ProductOrderMapper, ProductOrder>{

    @Autowired
    ProductOrderMapper productOrderMapper;

    /**
     * 获取某商品的订单数量
     * @param orderIds
     * @return
     */
    public int getProductOrderCount(List<String> orderIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",orderIds).in("status","6,7");
        return selectCount(wrapper);
    }
    /**
     * 获取订单数量
     * @param
     * @return
     */
    public int getProductOrderCount(Date startTime,Date endTime){
        EntityWrapper<ProductOrder> wrapper1 = new EntityWrapper<>();
        wrapper1.where("order_time>{0} and order_time<{1}", startTime, endTime);
        return this.selectList(wrapper1).size();
    }
    public int getProductOrderCount(String sellerId,int status,Date startTime,Date endTime){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper();
        wrapper.where("seller_id={0} and status={1}", sellerId, status).between("create_time", startTime,endTime);
        return this.selectCount(wrapper);
    }
    public int getProductOrderCountByUserIdAndStatus(String userId,String status){
        EntityWrapper<ProductOrder> wrapper=new EntityWrapper();
        wrapper.where("user_id={0} and deleted={1}",userId,0).in("status",status);
        return this.selectCount(wrapper);
    }
    public int getProductOrderCountBySellerId(String sellerId){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("seller_id = {0}", sellerId);
        return this.selectCount(wrapper);
    }
    public int getProductOrderCountBySellerIdAndStatus(String sellerIds,String status){
        EntityWrapper<ProductOrder> wrapper=new EntityWrapper();
        wrapper.where("deleted={0}",0).in("status",status).in("seller_id",sellerIds);
        return this.selectCount(wrapper);
    }
    public int getProductOrderCountByUserIdAndTime(String userId,Date endTime){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0} and order_time<{1}", userId, endTime);
        return this.selectCount(wrapper);
    }
    /**
     * 获取订单数量
     * @param userId
     * @return
     */
    public int getProductOrderCount(String userId){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}", userId);
        return selectCount(wrapper);
    }
    public int getOkProductOrderCount(String userId){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        Integer[] integer={6,7};
        wrapper.where("user_id = {0}",userId).in("status",integer);//查询已完成
        return selectCount(wrapper);
    }
    public int getCancelProductOrderCount(String userId){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0}and status={1}",userId,8).notIn("status",9);//查询已取消
        return selectCount(wrapper);
    }

    /**
     * 获取某商家最新订单详情
     * @param sellerIds
     * @return
     */
    public ProductOrder getproductOrder(List<String> sellerIds){
        EntityWrapper<ProductOrder> goodsOrderWrapper = new EntityWrapper<>();
        goodsOrderWrapper.in("seller_id",sellerIds).orderBy("create_time",false);
        return this.selectOne(goodsOrderWrapper);
    }
    public ProductOrder getproductOrder(int status,Date startTime,Date endTime,String groupBy){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper();
        wrapper.setSqlSelect("sum(total_price) as allTotalPrice,user_id as userId").where("status={0}", status).between("order_time", startTime, endTime).groupBy("user_id").orderBy("allTotalPrice", false);
        return this.selectOne(wrapper);
    }
    public ProductOrder getproductOrder(int status,Date startTime,Date endTime){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper();
        wrapper.where("status={0}", status).between("order_time", startTime, endTime).orderBy("order_time");
        return this.selectOne(wrapper);
    }

    /**
     * 根据商家id获取已完成订单数量
     * @param sellerId
     * @return
     */
    public int getNoCompletesProductOrderCountBySellerId(String sellerId){
        EntityWrapper<ProductOrder> wrapper=new EntityWrapper<>();
        wrapper.where("seller_id = {0} and status != {1} and deleted = {2}",sellerId, OrderStatusEnum.COMPLETED.getCode(),0);
        return this.selectCount(wrapper);
    }

    /**
     * 根据订单id集获取订单数量
     * @param orderIds
     * @return
     */
    public int getProductOrderCountByOrderIds(List<String> orderIds){
        EntityWrapper<ProductOrder> wrapperone=new EntityWrapper<>();
        wrapperone.where("status != {0} and deleted = {1}", OrderStatusEnum.COMPLETED.getCode(),0).in("id",orderIds);
        return this.selectCount(wrapperone);
    }

    /**
     * 获取订单
     * @param request
     * @return
     */
    public ProductOrder getproductOrder(GetGoodsOrderRequestDto request){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        if(request.getId()!=null){
            wrapper.where("id = {0}", request.getId());
        }
        if(request.getOrderNum()!=null){
            wrapper.where("order_num = {0}", request.getOrderNum());
        }
        return this.selectOne(wrapper);
    }

    /**
     * 分页查询
     * @param request
     * @return
     */
    public Page<ProductOrder> getPage(GetGoodsOrderListRequestDto request){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("seller_id = {0}", request.getSelleId());
        wrapper.orderBy("order_time", false);
        Page<ProductOrder> page = new Page<>();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrent());
        return this.selectPage(page, wrapper);
    }

    /**
     * 获取订单id
     * @param orderId
     * @return
     */
    public String getProductId(String orderId){
        EntityWrapper wrapper1=new EntityWrapper();
        wrapper1.setSqlSelect("product_id").where("order_id={0}",orderId);
        return (String) this.selectObj(wrapper1);
    }

    /**
     * 修改订单
     * @param request
     * @return
     */
    public boolean update(UpdateOrderRequestDto request){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("id={0}", request.getId());
        ProductOrder productOrder = new ProductOrder();
        if(StringUtils.isNotEmpty(request.getAddress())){
            productOrder.setAddress(request.getAddress());
        }
        return this.update(productOrder, wrapper);
    }

    /**
     * 获取订单列表
     * @param sellerId
     * @param status
     * @return
     */
    public List<ProductOrder> getProductOrderListBySellerIdAndStatus(String sellerId,int status){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("seller_id={0}", sellerId);
        wrapper.andNew("status!={0}",status);
        return this.selectList(wrapper);
    }

    public List<ProductOrder> getProductOrderListByUserIdAndStatus(String userId,int status){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and status = {1}", userId,status).orderBy("create_time");
        return this.selectList(wrapper);
    }

    /**
     * 获取总金额
     * @param sellerIds
     * @param startTime
     * @param endTime
     * @return
     */
    public BigDecimal getTotalPrice(String sellerIds,Date startTime,Date endTime){
        EntityWrapper wrapper1=new EntityWrapper();
        wrapper1.setSqlSelect("SUM(total_price)").in("seller_id",sellerIds).between("pay_time",startTime,endTime);
        return (BigDecimal) this.selectObj(wrapper1);
    }

    /**
     * 获取用户累计订单金额
     * @param userId
     * @return
     */
    public BigDecimal getAllOrderAmount(String userId,Date startTime){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("total_price").where("user_id={0} and create_time<{1}",userId,startTime);
        return (BigDecimal) this.selectObj(wrapper);
    }

    /**
     * 获取订单列表
     * @param oderIds
     * @return
     */
    public List<ProductOrder> getProductOrderList(List<String> oderIds){
        EntityWrapper<ProductOrder> wrapper1=new EntityWrapper<>();
        wrapper1.in("id={0}",oderIds);
        return this.selectList(wrapper1);
    }

    public List<ProductOrder> getProductOrderListByIds(String[] ids){
        return productOrderMapper.getProductOrderListByIds(ids);
    }

    public List<ProductOrder> getProductOrderList(String[] sellerId,int status){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("status = {0}",status).in("seller_id",sellerId);
        return this.selectList(wrapper);
    }

    public List<ProductOrder> getProductOrderList(int deliveryWay,int logisticsStatus){
        EntityWrapper<ProductOrder> wrapper=new EntityWrapper<>();
        wrapper.where("delivery_way={0} and logistics_status={1}",deliveryWay,logisticsStatus);
        return this.selectList(wrapper);
    }
    public List<ProductOrder> getProductOrderList(int status){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.where("status = {0} and deleted = {1}",status,0);
        return this.selectList(wrapper);
    }

    public List<ProductOrder> getProductOrderList(Date endTime,int status){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("DISTINCT user_id").where("order_time<{0} and status={1}", endTime, status);
        return this.selectList(wrapper);
    }

    public List<ProductOrder> getProductOrderList(String userId,String[] status,int current,int size){
        return  productOrderMapper.getMyGoodsOrderByUserId(userId,status,current,size);
    }

    public List<ProductOrder> getAllProductOderByUserId(String[] sellerId,String[] status,int current,int size){
        return productOrderMapper.getAllGoodsOderByUserId(sellerId,status,current,size);
    }

    /**
     * 获取商家id集
     * @param status
     * @return
     */
    public List<String> getSellerIds(int status,Date startTime,Date endTime){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("seller_id").andNew("status={0}",status).between("create_time",startTime,endTime);
        return this.selectObjs(wrapper);
    }

    public List<String> getSellerIds(int current,int size){
        return productOrderMapper.getSellerByByDealAmount(current, size);
    }

    /**
     * 获取订单id集
     * @param status
     * @return
     */
    public List<String> getOrderIds(int status){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id").where("status = {0} and deleted = {1}",status,0);
        return this.selectObjs(wrapper);
    }

    public List<String> getOrderIds(String userId,int status){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").where("status = {0} and user_id = {1}",status,userId).andNew("deleted = 0");
        return this.selectObjs(wrapper);
    }

    /**
     * 修改订单
     * @param productOrder,userId
     * @return
     */
    public boolean updateProductOrder(ProductOrder productOrder,String userId){
        EntityWrapper<ProductOrder> wrapper = new EntityWrapper();
        wrapper.where("user_id={0}", userId);
        return this.update(productOrder, wrapper);
    }

    /**
     * 根据商家id获取订单总额
     * @param sellerId
     * @return
     */
    public BigDecimal getSumOrderAmountBySellerId(String sellerId){
        return new BigDecimal(productOrderMapper.getSumOrderAmountBySellerId(sellerId)==null?0:productOrderMapper.getSumOrderAmountBySellerId(sellerId));
    }

    /**
     * 根据userId获取商家id
     * @param userId
     * @return
     */
    public List<String> getSellerIdByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("seller_id").where("user_id = {0} AND status = 0 AND deleted = 0",userId);
        return this.selectObjs(wrapper);
    }

    /**
     * 根据userId获取商家id
     * @param userId
     * @return
     */
    public List<String> getProductIdByUserIdAndSellerId(String userId, String sellerId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").where("user_id = {0} AND seller_id = {1} AND status = 0 AND deleted = 0",userId, sellerId);
        return this.selectObjs(wrapper);
    }

    /**
     * 根据orderId删除订单
     * @param orderId
     * @return
     */
    public boolean delete(String orderId){
        return this.deleteById(orderId);
    }
}
