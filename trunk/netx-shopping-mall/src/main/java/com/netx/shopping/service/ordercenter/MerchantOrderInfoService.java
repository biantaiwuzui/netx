package com.netx.shopping.service.ordercenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.model.StatData;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.mapper.ordercenter.MerchantOrderInfoMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import com.netx.shopping.model.ordercenter.constants.OrderTypeEnum;
import com.netx.shopping.model.ordercenter.constants.ShippingStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class MerchantOrderInfoService extends ServiceImpl<MerchantOrderInfoMapper, MerchantOrderInfo> {

    @Autowired
    MerchantOrderInfoMapper merchantOrderInfoMapper;

    public List<MerchantOrderInfo> getWorthOrder(OrderTypeEnum orderTypeEnum,String typeId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("order_type_business_id = {0} and order_type = {1}",typeId,orderTypeEnum.name());
        return selectList(wrapper);
    }

    public List<String> getMerchantIdsByTime(OrderStatusEnum orderStatusEnum,Date start,Date end){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("deteled=0");
        if(orderStatusEnum!=null){
            wrapper.and("order_status={0}",orderStatusEnum.name());
        }
        if(start!=null){
            wrapper.and("finish_time>={0}",start);
        }
        if(end!=null){
            wrapper.and("finish_time<={0}",end);
        }
        wrapper.setSqlSelect("merchant_id");
        wrapper.groupBy("merchant_id");
        return selectObjs(wrapper);
    }

    public List<MerchantOrderInfo> getShippingList(){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        wrapper.where("shipping_status={0}", ShippingStatus.SS_SHIPPING.name());
        wrapper.isNotNull("shipping_code");
        wrapper.isNotNull("shipping_logistics_no");
        return selectList(wrapper);
    }

    public List<MerchantOrderInfo> getListByDeliveryWay(String userId,OrderStatusEnum orderStatusEnum,Integer deliveryWay){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        if(StringUtils.isNotBlank(userId)){
            wrapper.where("user_id={0}",userId);
        }
        if(orderStatusEnum!=null){
            wrapper.and("order_status={0}", orderStatusEnum.name());
        }
        if(deliveryWay!=null){
            wrapper.and("delivery_way={0}", deliveryWay);
        }
        return selectList(wrapper);
    }

    public List<MerchantOrderInfo> getList(String userId,OrderStatusEnum orderStatusEnum,String... id){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        if(StringUtils.isNotBlank(userId)){
            wrapper.where("user_id={0}",userId);
        }
        if(id!=null && id.length>0){
            wrapper.in("id",id);
        }
        if(orderStatusEnum!=null){
            wrapper.and("order_status={0}", orderStatusEnum.name());
        }
        return selectList(wrapper);
    }

    public List<MerchantOrderInfo> getList(String userId,List<String> merchantId, Page page, String... orderStatusEnum){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        if(StringUtils.isNotBlank(userId)){
            wrapper.where("user_id={0}",userId);
        }
        if(merchantId!=null && merchantId.size()>0){
            wrapper.in("merchant_id",merchantId);
        }
        if(orderStatusEnum!=null){
            int len = orderStatusEnum.length;
            if(len>0){
//                wrapper.in("order_status", orderStatusEnum);
                if(len>1){
//                    wrapper.orderBy("FIELD(order_status,\'"+StringUtils.join(orderStatusEnum,"\',\'")+"\'),create_time desc");
                    wrapper.orderBy("create_time desc");
                }
            }
        }
        if(page==null){
            return selectList(wrapper);
        }
        return selectPage(page,wrapper).getRecords();
    }

    /**
     * 获取商家所有订单金额
     * @param merchantId
     * @param orderStatusEnum
     * @param startTime
     * @param endTime
     * @return
     */
    public long getAllOrderAmountByMerchantId(String merchantId, OrderStatusEnum orderStatusEnum, Date startTime, Date endTime){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("SUM(order_total_fee)");
        wrapper.where("merchant_id = {0} AND order_status = {1}", merchantId, orderStatusEnum.order()).between("create_time", startTime, endTime);
        BigDecimal sum = (BigDecimal)this.selectObj(wrapper);
        return sum==null?0l:sum.longValue();
    }

    public int countFinishOrder(String merchantId, OrderStatusEnum orderStatusEnum, Date startTime, Date endTime){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("merchant_id = {0} AND order_status = {1}", merchantId, orderStatusEnum.order()).between("create_time", startTime, endTime);
        return selectCount(wrapper);
    }

    public boolean DelByTypeId(String typeId,OrderTypeEnum orderTypeEnum){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        wrapper.where("order_type_business_id={0} and order_type={1}",typeId,orderTypeEnum.name());
        return delete(wrapper);
    }

    public List<String> queryOrderNo(String typeId,OrderTypeEnum orderTypeEnum){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        wrapper.where("order_type_business_id={0} and order_type={1}",typeId,orderTypeEnum.name());
        wrapper.setSqlSelect("order_no");
        return (List<String>)(List)selectObjs(wrapper);
    }

    public boolean deleteByOrderNo(List<String> orderNo){
        Wrapper wrapper = new EntityWrapper();
        wrapper.in("order_no",orderNo);
        return delete(wrapper);
    }

    public Boolean updateOrder(String id,MerchantOrderInfo merchantOrderInfo){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        wrapper.where("id={0}",id);
        return update(merchantOrderInfo,wrapper);
    }

    public Boolean updateOrder(String[] ids,Integer noDeliveryWay,MerchantOrderInfo merchantOrderInfo){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        wrapper.in("id",ids);
        if(noDeliveryWay!=null){
            wrapper.and("delivery_way!={0}",noDeliveryWay);
        }
        return update(merchantOrderInfo,wrapper);
    }

    public List<String> getOrderIds(String userId, OrderStatusEnum orderStatusEnum){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").where("user_id={0}",userId);
        if(orderStatusEnum!=null){
            wrapper.and("order_status={0}",orderStatusEnum.name());
        }
        return this.selectObjs(wrapper);
    }

    public MerchantOrderInfo query(String userId,String id,OrderStatusEnum orderStatusEnum){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("user_id={0} and id={1}",userId,id);
        if(orderStatusEnum!=null){
            wrapper.and("order_status={0}",orderStatusEnum.name());
        }
        return this.selectOne(wrapper);
    }

    public int getProductOrderCountByUserIdAndTime(String userId,Date endTime){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0} and order_time<{1}", userId, endTime);
        return this.selectCount(wrapper);
    }

    public int getProductOrderCount(String merchantId, OrderStatusEnum orderStatusEnum, Date startTime, Date endTime){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        wrapper.where("merchant_id = {0} AND order_status = {1}", merchantId, orderStatusEnum.order()).between("create_time", startTime,endTime);
        return this.selectCount(wrapper);
    }

    /**
     * 获取订单数量
     * @param
     * @return
     */
    public int getProductOrderCount(Date startTime,Date endTime){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper<>();
        wrapper.where("order_time>{0} and order_time<{1}", startTime, endTime);
        return this.selectList(wrapper).size();
    }

    public MerchantOrderInfo getproductOrder(OrderStatusEnum orderStatusEnum,Date startTime,Date endTime,String groupBy){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        wrapper.setSqlSelect("sum(total_price) as allTotalPrice,user_id as userId").where("order_status={0}", orderStatusEnum.name()).between("order_time", startTime, endTime).groupBy(groupBy).orderBy("allTotalPrice", false);
        return this.selectOne(wrapper);
    }

    public MerchantOrderInfo getproductOrder(OrderStatusEnum orderStatusEnum,Date startTime,Date endTime){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper();
        wrapper.where("order_status={0}", orderStatusEnum.name()).between("order_time", startTime, endTime).orderBy("order_time");
        return this.selectOne(wrapper);
    }

    /**
     * 获取用户累计订单金额
     * @param userId
     * @return
     */
    public long getAllOrderAmount(String userId, Date startTime){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("sum(order_total_fee+shipping_fee)").where("user_id={0} and create_time<{1}",userId,startTime);
        return BigDecimalToLong(wrapper);
    }



    public List<MerchantOrderInfo> getProductOrderList(Date endTime,OrderStatusEnum orderStatusEnum){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("DISTINCT user_id").where("order_time<{0} and order_status={1}", endTime, orderStatusEnum.name());
        return this.selectList(wrapper);
    }

    public List<StatData> queryShoppingStat(){
//        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper<>();
//        wrapper.setSqlSelect("user_id as userId,sum(order_total_fee+shipping_fee) as total");
//        wrapper.where("deleted=0 and order_status={0}",OrderStatusEnum.OS_FINISH.name());
//        wrapper.groupBy("userId");
       // wrapper.orderBy("total desc");

        //return this.selectMaps(wrapper);
        return merchantOrderInfoMapper.queryShoppingStat();
    }

    public long queryShoppingStat(String userId){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("sum(order_total_fee+shipping_fee)");
        wrapper.where("deleted=0 and order_status={0} and user_id={1}",OrderStatusEnum.OS_FINISH.name(),userId);
        return BigDecimalToLong(wrapper);
    }

    public long queryShoppingStat(String[] merchantId){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("sum(order_total_fee+shipping_fee)");
        wrapper.where("deleted=0 and order_status={0}",OrderStatusEnum.OS_FINISH.name());
        wrapper.in("merchant_id",merchantId);
        return BigDecimalToLong(wrapper);
    }

    private long BigDecimalToLong(Wrapper wrapper){
        BigDecimal total = (BigDecimal) this.selectObj(wrapper);
        return total==null?0l:total.longValue();
    }

    public long queryShoppingStat(List<String> merchantId){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("sum(order_total_fee+shipping_fee)");
        wrapper.where("deleted=0 and order_status={0}",OrderStatusEnum.OS_FINISH.name());
        wrapper.in("merchant_id",merchantId);
        return BigDecimalToLong(wrapper);
    }
    
    /* 修改网能订单 **/
    public List<MerchantOrderInfo> selectWZ(String typeId, OrderTypeEnum orderTypeEnum){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("order_type_business_id={0} and order_type={1}",typeId,orderTypeEnum.name());
        return selectList(wrapper);
    }
    
    public boolean updateWz(List<String> orderNo,MerchantOrderInfo merchantOrderInfo){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("order_no={0}",orderNo);
        return update(merchantOrderInfo,wrapper);
    }
    
    public MerchantOrderInfo getOrderInfoStatus(String id,OrderTypeEnum orderTypeEnum){
        EntityWrapper<MerchantOrderInfo> wrapper = new EntityWrapper<>();
        wrapper.where("id={0} and order_type={1}",id,orderTypeEnum.getName());
        return selectOne(wrapper);
    }
    
}
