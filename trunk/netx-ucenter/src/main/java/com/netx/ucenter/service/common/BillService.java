package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.vo.common.BillStatisticsRequestDto;
import com.netx.ucenter.mapper.common.CommonBillMapper;
import com.netx.ucenter.model.common.CommonBill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Create by wongloong on 17-9-17
 */
@Service
public class BillService extends ServiceImpl<CommonBillMapper, CommonBill>{
    @Autowired
    CommonBillMapper commonBillMapper;

    public List<CommonBill> getPage(Page page,Integer toAccount,String userId,Long startTime,Long endTime,Integer type) throws Exception{
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.where("trade_type is not null").and("deleted=0").andNew("to_account!={0}",toAccount);
        if (!StringUtils.isEmpty(userId)) {
            entityWrapper.and("user_id={0}", userId);
        }
        System.out.println(startTime);
        if (startTime!=null) {
            entityWrapper.and("create_time>={0}", new Date(startTime));
        }
        System.out.println(endTime);
        if (endTime!=null) {
            entityWrapper.and("create_time<={0}", new Date(endTime));
        }
        if (type!=null) {
            entityWrapper.and("type={0}",type);
        }
        entityWrapper.orderBy("create_time desc");
        return selectPage(page,entityWrapper).getRecords();
    }

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonBill>().where("user_id={0}",userId));
    }

    public List<CommonBill> getWzCommonBillList(Integer tradeType,Integer payChannel,Integer toAccount,Date createTime){
        EntityWrapper<CommonBill> entityWrapper = new EntityWrapper<>(new CommonBill());
        String sql = "trade_type="+tradeType+" and pay_channel="+payChannel+" and to_account="+toAccount;
        entityWrapper.where(sql).andNew("deleted=0");
        if(createTime!=null){
            entityWrapper.and("create_time<{0}",createTime);
        }
        return this.selectList(entityWrapper);
    }

    public BigDecimal getSumAmountByUserId(String userId,Long start,Long end){
        Wrapper wrapper = new EntityWrapper<CommonBill>();
        wrapper.setSqlSelect("SUM(amount)");
        wrapper.where("user_id={0}", userId);
        wrapper.between("create_time", start, end);
        return (BigDecimal) this.selectObj(wrapper);
    }

    public BigDecimal statisticBill(BillStatisticsRequestDto dto) {
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("SUM(amount)").andNew("deleted={0}",0);
        if (!StringUtils.isEmpty(dto.getCurrencyId())){
            wrapper.andNew("currency_id={0}",dto.getCurrencyId());
        }
        if (!StringUtils.isEmpty(dto.getUserId())){
            wrapper.andNew("user_id={0}",dto.getUserId());
        }
        if( dto.getQueryType() != null){
            wrapper.andNew("trade_type={0}",dto.getQueryType());
        }
        if (dto.getPayChannel()!=null){
            wrapper.andNew("pay_channel={0}",dto.getPayChannel());
        }
        if (dto.getStartTime()!=null){
            wrapper.andNew("create_time>={0}",dto.getStartTime());
        }
        if (dto.getEndTime()!=null){
            wrapper.andNew("create_time<={0}",dto.getEndTime());
        }
        if (dto.getType()!=null){
            wrapper.andNew("type={0}",dto.getType());
        }
        BigDecimal result = (BigDecimal) this.selectObj(wrapper);
        if(result == null){
            result = new BigDecimal("0");
        }
        return result;
    }

    /**统计用户一天之内的充值或者是提现金额*/

//    public BigDecimal acountthisDayIncome(String userId,int type) {
//        Wrapper wrapper = new EntityWrapper<CommonBill>();
//
//        wrapper.setSqlSelect("SUM(amount)").where("user_id={0}", userId);
//        wrapper.where("trade_type = {0}", type)
//                .where("pay_channel = {0} OR pay_channel = {1}", 0, 1)
//                .where("create_time>=date(now())")
//                .where("create_time<DATE_ADD(date(now()),INTERVAL 1 DAY)");
//        BigDecimal result = (BigDecimal) this.selectObj(wrapper);
//        if (result == null) {
//            result = new BigDecimal(0);
//        }
//        //System.out.println ( result );
//        return result;
//    }

    /**统计用户一天之内的充值或者是提现金额*/
    public BigDecimal countThisDayOutcome(String userId,int type) {
        BigDecimal result = commonBillMapper.countThisDayOutcome ( userId, type );
        return result;
    }

}
