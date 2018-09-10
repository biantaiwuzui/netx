package com.netx.shopping.service.redpacketcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.redpacketcenter.RedpacketRecordMapper;
import com.netx.shopping.model.business.SellerRedpacketRecord;
import com.netx.shopping.model.redpacketcenter.RedpacketRecord;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  购物车详情服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-08
 */
@Service("newRedpacketRecordService")
public class RedpacketRecordService extends ServiceImpl<RedpacketRecordMapper, RedpacketRecord> {
    /**
     * 获取红包记录列表
     * @param redpacketSendId
     * @return
     */
    public List<RedpacketRecord> getRedpacketRecordList(String redpacketSendId){
        EntityWrapper<RedpacketRecord> wrapper=new EntityWrapper<>();
        wrapper.where("redpacket_send_id={0} and amount>0",redpacketSendId).orderBy("amount desc");
        return this.selectList(wrapper);
    }
    public List<RedpacketRecord> getRedpacketRecordList(Date startTime, Date endTime){
        EntityWrapper wrapper1=new EntityWrapper();
        wrapper1.where("create_time>={0} and create_time<={1}",startTime,endTime);
        return this.selectList(wrapper1);
    }

    /**
     * 获取红包数量
     * @param
     * @return
     */
    public int getRedpacketRecordCount(String userId,Date startTime,Date endTime){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.andNew("user_id={0} and amount>0",userId).between("create_time",startTime,endTime);
        return this.selectCount(wrapper);
    }

    /**
     * 获取红包记录
     * @param redpacketSendId
     * @return
     */
    public RedpacketRecord getRedpacketRecord(String userId,String redpacketSendId){
        EntityWrapper<RedpacketRecord> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0} and redpacket_send_id={1}",userId,redpacketSendId);
        return this.selectOne(wrapper);
    }
    public RedpacketRecord getRedpacketRecord(String userId,String redpacketSendId,String amount){
        EntityWrapper<RedpacketRecord> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0} and redpacket_send_id={1} and amount>0",userId,redpacketSendId);
        return this.selectOne(wrapper);
    }
    public RedpacketRecord getRedpacketRecord(String userId){
        EntityWrapper<RedpacketRecord> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}",userId);
        return this.selectOne(wrapper);
    }

    /**
     * 获取红包金额列表
     * @param userId
     * @return
     */
    public List<Long> getAmounts(String userId){
        EntityWrapper wrapper=new EntityWrapper<>();
        wrapper.setSqlSelect("amount").where("user_id={0} and amount>0",userId);
        return this.selectObjs(wrapper);
    }

    /**
     * 获取红包发放信息
     * @param userId
     * @return
     */
    public List<RedpacketRecord> getSellerRedpacketRecordByUserIdAndAmount(String userId){
        EntityWrapper<RedpacketRecord> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} AND amount > 0", userId);
        return this.selectList(wrapper);
    }

    /**
     * 获取红包金额
     * @param userId
     * @return
     */
    public BigDecimal getAmount(String userId){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("SUM(amount)").where("user_id={0} and amount>0",userId);
        return (BigDecimal) this.selectObj(wrapper);
    }
    public BigDecimal getAmount(String userId,Date startTime){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("total_price").where("user_id={0} and create_time<{1}",userId,startTime);
        return (BigDecimal) this.selectObj(wrapper);
    }
}
