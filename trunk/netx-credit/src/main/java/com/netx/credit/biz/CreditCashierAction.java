package com.netx.credit.biz;

import com.netx.common.user.util.VoPoConverter;
import com.netx.credit.model.CreditCashier;
import com.netx.credit.service.CreditCashierService;
import com.netx.credit.vo.AddCashierRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lanyingchu
 * @date 2018/7/11 20:58
 */
@Service
public class CreditCashierAction {

    @Autowired
    private CreditCashierService creditCashierService;

    public CreditCashierService getCreditCashierService() {
        return creditCashierService;
    }

    /**
     * 添加收银人
     */
    @Transactional
    public CreditCashier addCashier(AddCashierRequestDto dto){
        CreditCashier creditCashier = new CreditCashier();
        VoPoConverter.copyProperties(dto, creditCashier);
        creditCashier.setIsCurrent(1);
        creditCashier.setDeleted(0);
        boolean result = creditCashierService.insertOrUpdate(creditCashier);
        if (result) {
            return creditCashier;
        }
        return null;
    }


}
