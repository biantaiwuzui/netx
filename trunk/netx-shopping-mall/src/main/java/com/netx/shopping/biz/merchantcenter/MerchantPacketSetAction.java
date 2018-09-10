package com.netx.shopping.biz.merchantcenter;


import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.AddPacketSetRequestDto;
import com.netx.searchengine.enums.FriendTypeEnum;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.merchantcenter.MerchantPacketSet;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import com.netx.shopping.service.merchantcenter.MerchantPacketSetService;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.service.ordercenter.MerchantOrderInfoService;
import com.netx.utils.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 网商-红包设置 action
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@Service
public class MerchantPacketSetAction {

    @Autowired
    MerchantPacketSetService merchantPacketSetService;

    @Autowired
    MerchantService merchantService;

    @Autowired
    MerchantOrderInfoService merchantOrderInfoService;

    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdate(AddPacketSetRequestDto request){
        MerchantPacketSet packetSet = new MerchantPacketSet();
        VoPoConverter.copyProperties(request,packetSet);
        if (request.getChangeRate() == 1){
            packetSet.setIsFixedRate(true);
        }else {
            packetSet.setIsFixedRate(false);
            packetSet.setFirstRate(request.getFixedRate());
            packetSet.setLimitRate(request.getFixedRate());
        }
        packetSet.setDeleted(0);
        merchantPacketSetService.insertOrUpdate(packetSet);
        return packetSet.getId();
    }

    /**
     * 获取开启每日红包启动金额的商家id
     */
    public List<String> getStartRedpacketAmountSellerIds(){
        List<String> res=new ArrayList<>();
        return res;
    }

    /**
     * 查看 红包设置详情
     * @param id
     * @return
     */
    public MerchantPacketSet get(String id) {
        return merchantPacketSetService.selectById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean deletePacketSetById(String id) {
        return merchantPacketSetService.deleteById(id);
    }


    /**
     *
     * @param merchantIds
     * @return
     */
    public Map<String,String> getPacketIdsByMerchantIds(List<String> merchantIds){
        Map<String,String> result = new HashMap<>();
        for (String merchantId : merchantIds) {
            String pacSetId= merchantService.getPacSetId(merchantId);
            if (pacSetId!=null&&!pacSetId.equals("")){
                result.put(merchantId, pacSetId);
            }
        }
        return result;
    }

    public void updatePacketSet(String merchantId,String id){
        MerchantPacketSet packetSet = merchantPacketSetService.selectById(id);
        if (packetSet != null){
            packetSet.setMerchantId(merchantId);
            merchantPacketSetService.updateById(packetSet);
        }
    }

    /**
     * 获取订单需扣取的红包提成金额
     * @param
     * @return
     * @throws
     */
    public BigDecimal getDirectRedPacketAmount(MerchantOrderInfo merchantOrderInfo){
        //按商家设定的红包提成标准，按当前订单金额计提红包金额至红包池
        Merchant merchant = merchantService.selectById(merchantOrderInfo.getMerchantId());
        if (merchant == null){
            throw new RuntimeException("供应商家不存在");
        }
        MerchantPacketSet packetSet = merchantPacketSetService.selectById(merchant.getPacSetId());
        if (packetSet == null) {
            throw new RuntimeException("红包设置不存在");
        }
        BigDecimal packetAmount;
        Money money = Money.CentToYuan(merchantOrderInfo.getOrderTotalFee());
        //是否变动提成
        if (packetSet.getChangeRate()) {
            //每天根据订单数量，由低到高，逐单加大红包计提比例
            //获取今天开始和结束的时间戳
            Date now = new Date();
            Date start = DateTimestampUtil.getStartOrEndOfDate(now, 1);
            Date end = DateTimestampUtil.getStartOrEndOfDate(now, 2);
            //查找今天订单数
            int orderNum = merchantOrderInfoService.countFinishOrder(merchant.getId(), OrderStatusEnum.OS_FINISH,start,end);
            //获取逐单累加比例
            BigDecimal packet = packetSet.getGradualRate().multiply(new BigDecimal(orderNum-1)).add(packetSet.getFirstRate());
            //判断是否已到封顶比例
            if(packetSet.getLimitRate().compareTo(packet)>-1){
                packetAmount = packet.multiply(money.getAmount());
            }else{
                packetAmount = packetSet.getLimitRate().multiply(money.getAmount());
            }
        } else {
            packetAmount = packetSet.getFixedRate().multiply(money.getAmount());
        }
        return packetAmount.divide(new BigDecimal(100));
    }

    /**
     * 注册商家时插入商家id
     * @param id
     * @param merchantId
     * @return
     */
    public MerchantPacketSet updateMerchantId(String id, String merchantId){
        MerchantPacketSet merchantPacketSet = merchantPacketSetService.selectById(id);
        if(merchantPacketSet != null){
            merchantPacketSet.setMerchantId(merchantId);
            if(merchantPacketSetService.updateById(merchantPacketSet)){
                return merchantPacketSet;
            }
        }
        return null;
    }
}
