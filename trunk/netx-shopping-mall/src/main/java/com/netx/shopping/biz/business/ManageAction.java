package com.netx.shopping.biz.business;

import com.netx.common.vo.business.AddBusinessManageRequestDto;
import com.netx.shopping.mapper.business.SellerManageMapper;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.business.SellerManage;
import com.netx.shopping.service.business.ManageService;
import com.netx.shopping.vo.BusinessManageListResponseVo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 网商-业务主管表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("manageAction")
@Transactional(rollbackFor = Exception.class)
public class ManageAction {

    @Autowired
    ManageService manageService;

    @Autowired
    SellerAction sellerAction;

    @Autowired
    SellerManageMapper sellerManageMapper;
    
    public SellerManage addOrUpdate(AddBusinessManageRequestDto request){
        SellerManage manage = new SellerManage();
        BeanUtils.copyProperties(request, manage);
        if(StringUtils.isNotBlank(request.getId())){
            manage.setUpdateUserId(request.getUserId());
        }else{
            manage.setDeleted(0);
            manage.setCreateUserId(request.getUserId());
        }
        manageService.insertOrUpdate(manage);
        return manage;
    }

    
    public boolean delete(String id){
        return manageService.delete(id);
    }

    
    public List<BusinessManageListResponseVo> list(String userId){
        List<BusinessManageListResponseVo> resultList = new ArrayList<>();
        List<SellerManage> list = sellerManageMapper.selectListByUserId(userId);
        if( null != list && list.size() > 0){
            for (SellerManage manage: list) {
                BusinessManageListResponseVo businessManageListResponse = new BusinessManageListResponseVo();
                BeanUtils.copyProperties(manage, businessManageListResponse);
                resultList.add(businessManageListResponse);
            }
        }
        return resultList;
    }

    
    public SellerManage getManage(String manageId){
        return manageService.selectById(manageId);
    }

    public boolean setIsCurrent(String sellerId,String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<SellerManage> sellerManageList = manageService.selectManageBySellerId(sellerId);
        if (sellerManageList != null) {
            for (SellerManage sellerManage : sellerManageList) {
                if (!idList.contains(sellerManage.getId())) {
                    sellerManage.setIsCurrent(0);
                    manageService.updateById(sellerManage);
                }
            }
        }
        return true;
    }

    public boolean setSellerIdAndIsCurrent(String sellerId,String manageIds) {
        String[] manageId = manageIds.split(",");
        for(String id : manageId){
            SellerManage sellerManage = manageService.selectById(id);
            if (sellerManage.getSellerId() == null){
                sellerManage.setSellerId(sellerId);
                sellerManage.setIsCurrent(1);
                return this.setIsCurrent(sellerId,manageId) && manageService.updateById(sellerManage);
            }else {
                if (sellerManage.getSellerId() == sellerId){
                    if (sellerManage.getIsCurrent() !=1){
                        sellerManage.setIsCurrent(1);
                        return this.setIsCurrent(sellerId,manageId) && manageService.updateById(sellerManage);
                    }
                }else {
                    this.setIsCurrent(sellerId,manageId);
                    SellerManage newSellerManage = new SellerManage();
                    newSellerManage.setManageName(sellerManage.getManageName());
                    newSellerManage.setUserId(sellerManage.getUserId());
                    newSellerManage.setManageNetworkNum(sellerManage.getManageNetworkNum());
                    newSellerManage.setManagePhone(sellerManage.getManagePhone());
                    newSellerManage.setCreateUserId(sellerManage.getUserId());
                    newSellerManage.setIsCurrent(1);
                    newSellerManage.setSellerId(sellerId);
                    manageService.insert(newSellerManage);
                    Seller seller = new Seller();
                    seller.setId(sellerId);
                    seller.setManageId(manageIds.replace(id,newSellerManage.getId()));
                    return sellerAction.updateById(seller);
                }
            }
        }
        return true;
    }

    public SellerManage getManageBySellerId(String sellerId){
        return manageService.getManageBySellerId(sellerId);
    }
}
