package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonDepositBillMapper;
import com.netx.ucenter.model.common.CommonDepositBill;
import org.springframework.stereotype.Service;

@Service
public class DepositBillService extends ServiceImpl<CommonDepositBillMapper,CommonDepositBill> {
    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonDepositBill>().where("user_id={0}",userId));
    }
}
