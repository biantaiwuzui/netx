package com.netx.fuse.client.ucenter;

import java.util.Date;
import java.util.List;

import com.netx.fuse.biz.ucenter.WallerFrozenFuseAction;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.netx.common.vo.common.FrozenAddRequestDto;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.common.vo.common.FrozenQueryRequestDto;
import com.netx.common.vo.common.WzCommonWalletFrozenResponseDto;
import com.netx.ucenter.biz.common.WalletFrozenAction;
import com.netx.ucenter.model.common.CommonWalletFrozen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletForzenClientAction {

    private Logger logger = LoggerFactory.getLogger(WalletForzenClientAction.class);

    @Autowired
    private WallerFrozenFuseAction wallerFrozenFuseAction;

    @Autowired
    private WalletFrozenAction walletFrozenAction;

    @Transactional(rollbackFor = Exception.class)
    public boolean add(FrozenAddRequestDto requestDto) { //冻结
        try {
            CommonWalletFrozen frozen = new CommonWalletFrozen();
            BeanUtils.copyProperties(requestDto, frozen);
            frozen.setAmount(new Money(requestDto.getAmount()).getCent());
            frozen.setCreateTime(new Date());
            frozen.setBak1(requestDto.getTradeType().toString());
            frozen.setBak2(requestDto.getCurrencyId());
            if (wallerFrozenFuseAction.addFrozenAndBill(frozen)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("添加冻结记录异常:"+e.getMessage(), e);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean repealFrozen(FrozenOperationRequestDto requestDto) {//退款
        try {
            if (walletFrozenAction.repeal(requestDto)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("撤销冻结金额异常:"+e.getMessage(), e);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean pay(FrozenOperationRequestDto requestDto) {
        return walletFrozenAction.pay(requestDto);
    }

    public List<WzCommonWalletFrozenResponseDto> queryList(FrozenQueryRequestDto requestDto) {
        try {
            return walletFrozenAction.selectPageAndUserDate(requestDto);
        } catch (Exception e) {
            logger.error("查询冻结记录异常", e);
        }
        return null;
    }
}
