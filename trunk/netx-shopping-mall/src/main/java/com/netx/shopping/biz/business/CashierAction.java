package com.netx.shopping.biz.business;

import com.netx.common.vo.business.AddBusinessCashierRequestDto;
import com.netx.shopping.mapper.business.SellerCashierMapper;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.business.SellerCashier;
import com.netx.shopping.service.business.CashierService;
import com.netx.shopping.vo.BusinessCashierListResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 网商-收银人员 服务实现类
 * </p>
 *
 * @author liwei
 * @since 2018-01-23
 */
@Service
public class CashierAction {

    @Autowired
    CashierService cashierService;

    @Autowired
    SellerAction sellerAction;

    @Autowired
    SellerCashierMapper sellerCashierMapper;

    @Transactional(rollbackFor = Exception.class)
    public SellerCashier addOrUpdate(AddBusinessCashierRequestDto request) {
        SellerCashier cashier = new SellerCashier();
        BeanUtils.copyProperties(request, cashier);
        if(StringUtils.isNotBlank(request.getId())){
            cashier.setUpdateUserId(request.getUserId());
        }else{
            cashier.setDeleted(0);
            cashier.setCreateUserId(request.getUserId());
        }
        cashierService.insertOrUpdate(cashier);
        return cashier;
    }

    
    public boolean delete(String id){
        return cashierService.delete(id);
    }

    
    public List<BusinessCashierListResponseVo> list(String userId){
        List<BusinessCashierListResponseVo> resultList = new ArrayList<>();
        List<SellerCashier> list = sellerCashierMapper.selectListByUserId(userId);
        if( null != list && list.size() > 0){
            for (SellerCashier cashier: list) {
                BusinessCashierListResponseVo businessCashierListResponseVo = new BusinessCashierListResponseVo();
                BeanUtils.copyProperties(cashier, businessCashierListResponseVo);
                resultList.add(businessCashierListResponseVo);
            }
        }
        return resultList;
    }

    
    public SellerCashier getCashier(String cashierId){
        return cashierService.selectById(cashierId);
    }

    public boolean setIsCurrent(String sellerId,String cashierId) {
        SellerCashier sellerCashier = cashierService.selectCashierBySellerId(sellerId);
        if (sellerCashier != null && sellerCashier.getId() != cashierId){
            sellerCashier.setIsCurrent(0);
            cashierService.updateById(sellerCashier);
        }
        return true;
    }

    public boolean setSellerIdAndIsCurrent(String sellerId,String cashierId) {
        SellerCashier sellerCashier = cashierService.selectById(cashierId);
        if (sellerCashier.getSellerId() == null){
            sellerCashier.setSellerId(sellerId);
            sellerCashier.setIsCurrent(1);
            return this.setIsCurrent(sellerId,cashierId) && cashierService.updateById(sellerCashier);
        }else {
            if (sellerCashier.getSellerId() == sellerId){
                if (sellerCashier.getIsCurrent() == 1){
                    return true;
                }else {
                    sellerCashier.setIsCurrent(1);
                    return  this.setIsCurrent(sellerId,cashierId) && cashierService.updateById(sellerCashier);
                }
            }else {
                this.setIsCurrent(sellerId,cashierId);
                SellerCashier newSellerCashier = new SellerCashier();
                newSellerCashier.setMoneyName(sellerCashier.getMoneyName());
                newSellerCashier.setUserId(sellerCashier.getUserId());
                newSellerCashier.setCreateUserId(sellerCashier.getUserId());
                newSellerCashier.setMoneyNetworkNum(sellerCashier.getMoneyNetworkNum());
                newSellerCashier.setMoneyPhone(sellerCashier.getMoneyPhone());
                newSellerCashier.setIsCurrent(1);
                newSellerCashier.setSellerId(sellerId);
                cashierService.insert(newSellerCashier);
                Seller seller = new Seller();
                seller.setId(sellerId);
                seller.setSellerCashierId(newSellerCashier.getId());
                return sellerAction.updateById(seller);
            }
        }
    }

    public SellerCashier selectById(String id){
        return cashierService.selectById(id);
    }

    public SellerCashier selectSellerCashierBySellerId(String sellerId){
        return cashierService.selectCashierBySellerId(sellerId);
    }
}
