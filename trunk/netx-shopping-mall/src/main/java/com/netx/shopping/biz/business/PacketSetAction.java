package com.netx.shopping.biz.business;

import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.AddPacketSetRequestDto;
import com.netx.shopping.model.business.SellerPacketSet;
import com.netx.shopping.service.business.PacketSetService;
import com.netx.shopping.service.business.SellerService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <p>
 * 网商-红包设置表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("packetSetAction")
@Transactional(rollbackFor = Exception.class)
public class PacketSetAction {

    @Autowired
    SellerService sellerService;

    @Autowired
    PacketSetService packetSetService;

    @Transactional(rollbackFor = Exception.class)
    
    public String saveOrUpdate(AddPacketSetRequestDto request) throws InvocationTargetException, IllegalAccessException {
        SellerPacketSet packetSet = new SellerPacketSet();
        VoPoConverter.copyProperties(request,packetSet);
        if (request.getChangeRate().equals(1)){
            packetSet.setIsFixedRate(true);
        }else {
            packetSet.setIsFixedRate(false);
        }
        packetSet.setCreateUserId(request.getUserId());
        packetSet.setDeleted(0);
        packetSetService.insertOrUpdate(packetSet);

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
    public SellerPacketSet get(String id) {
        return packetSetService.selectById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean deletePacketSetById(String id) {
        return packetSetService.deleteById(id);
    }


    /**
     *
     * @param sellerIds
     * @return
     */
    public Map<String,String> getPacketIdsBySellerIds(List<String> sellerIds){
        Map<String,String> result=new HashMap<>();
        for (String sellerId:sellerIds) {
            String pacSetId= sellerService.getPacSetId(sellerId);
            if (pacSetId!=null&&!pacSetId.equals("")){
                result.put(sellerId,pacSetId);
            }
        }
        return result;
    }

    public void updatePacketSet(String sellerId,String id){
        SellerPacketSet packetSet = packetSetService.selectById(id);
        if (packetSet != null){
            packetSet.setSellerId(sellerId);
            packetSetService.updateById(packetSet);
        }
    }
}