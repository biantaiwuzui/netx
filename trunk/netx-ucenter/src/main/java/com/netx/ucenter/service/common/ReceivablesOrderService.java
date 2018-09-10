package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonReceivablesOrderMapper;
import com.netx.ucenter.model.common.CommonReceivablesOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网币收款订单表 服务实现类
 * </p>
 * @author allen
 * @since 2017-12-08
 */
@Service
public class ReceivablesOrderService extends ServiceImpl<CommonReceivablesOrderMapper, CommonReceivablesOrder>{
    private Logger logger = LoggerFactory.getLogger(ReceivablesOrderService.class);

    public void delByUserId(String userId) throws Exception{
        delete(new EntityWrapper<CommonReceivablesOrder>().where("user_id={0} or to_user_id={0}",userId));
    }
}
