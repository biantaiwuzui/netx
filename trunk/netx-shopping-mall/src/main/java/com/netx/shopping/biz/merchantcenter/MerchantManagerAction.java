package com.netx.shopping.biz.merchantcenter;


import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.merchantcenter.constants.MerchantManagerEnum;
import com.netx.shopping.service.merchantcenter.MerchantManagerService;
import com.netx.shopping.vo.AddManagerRequestDto;
import com.netx.shopping.vo.QueryManagerResponseDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网商-收银人员表 服务action
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantManagerAction {

    @Autowired
    private MerchantManagerService merchantManagerService;

    public MerchantManagerService getMerchantManagerService() {
        return merchantManagerService;
    }

    /* 根据userNumber获取merchantId **/
    public List<String> getMerchantIdByUserNetworkNum(String userNetworkNum){
        return merchantManagerService.getMerchantIdByUserNetworkNum(userNetworkNum);
    }

    /**
     * 根据merchantId删除相关信息
     * @param merchantId
     * @return
     */
    @Transactional
    public boolean deleteByMerchantId(String merchantId){
        List<MerchantManager> lists = merchantManagerService.getMerchantManagerByMerchantId(merchantId);
        if(lists != null && lists.size() > 0){
            for(MerchantManager list : lists){
                list.setDeleted(1);
            }
            return merchantManagerService.updateBatchById(lists);
        }
        return false;
    }

    /**
     * 添加、编辑法人/主管/收银员/注册者/特权行使人/客服人员
     * @param requestDto
     * @return
     */
    @Transactional
    public MerchantManager addManager(AddManagerRequestDto requestDto){
        MerchantManager merchantManager = new MerchantManager();
        VoPoConverter.copyProperties(requestDto, merchantManager);
        if(requestDto.getMerchantUserType() == 0){
            merchantManager.setMerchantUserType(MerchantManagerEnum.CASHIER.getName());
            merchantManager.setIsCurrent(0);
        }else if(requestDto.getMerchantUserType() == 1){
            merchantManager.setMerchantUserType(MerchantManagerEnum.MANAGER.getName());
            merchantManager.setIsCurrent(0);
        }else if(requestDto.getMerchantUserType() == 2){
            merchantManager.setMerchantUserType(MerchantManagerEnum.LEGAL.getName());
            merchantManager.setIsCurrent(1);
        }else if(requestDto.getMerchantUserType() == 3){
            merchantManager.setMerchantUserType(MerchantManagerEnum.REGISTER.getName());
            merchantManager.setIsCurrent(1);
        }else if(requestDto.getMerchantUserType() == 4){
            List<MerchantManager> manager = merchantManagerService.getMerchantManagerByMerchantId(requestDto.getMerchantId(), MerchantManagerEnum.PRIVILEGE.getName());
            if(manager != null && manager.size() > 0){
                merchantManager.setId(manager.get(0).getId());
            }
            merchantManager.setIsCurrent(1);
            merchantManager.setMerchantUserType(MerchantManagerEnum.PRIVILEGE.getName());
        }else if(requestDto.getMerchantUserType() == 5){
            merchantManager.setMerchantUserType(MerchantManagerEnum.CUSTOMERSERVICE.getName());
            merchantManager.setIsCurrent(1);
        }
        merchantManager.setDeleted(0);
        boolean b = merchantManagerService.insertOrUpdate(merchantManager);
        if(b){
            return merchantManager;
        }
        return null;
    }

    /**
     * 根据id删除主管或者收银员
     * @param managerId
     * @return
     */
    @Transactional
    public boolean delete(String managerId, String merchantId, Integer merchantUserType){
        MerchantManager merchantManager = new MerchantManager();
        if(StringUtils.isNotBlank(merchantId)){
            merchantManager = merchantManagerService.getMerchantManagerByMerchantIdAndMerchantUserType(merchantId, getMerchantUserType(merchantUserType));
        }else {
            merchantManager = merchantManagerService.selectById(managerId);
        }
        if (merchantManager != null) {
            merchantManager.setDeleted(1);
            return merchantManagerService.updateById(merchantManager);
        }
        return false;
    }

    /**
     * 根据id获取商家主管或者收银员
     * @param id
     * @return
     */
    public MerchantManager getMerchantManagerById(String id){
        return merchantManagerService.selectById(id);
    }

    /**
     * 当merchantUserType = 0：查询收银员列表
     * 当merchantUserType = 1：查询主管列表
     * 当merchantUserType = 2：查询法人列表
     * 当merchantUserType = 3：查询注册者列表
     * 当merchantUserType = 4：查询特权行使人列表
     * 当merchantUserType = 5：查询客服人员列表
     * @param userId
     * @param merchantUserType
     * @return
     */
    public List<QueryManagerResponseDto> queryMerchantManagerByUserId(String userId, Integer merchantUserType, String merchantId){
        List<QueryManagerResponseDto> responseDtos = new ArrayList<>();
        List<Map<String,Object>> maps = merchantManagerService.queryMerchantManagerByUserId(userId, getMerchantUserType(merchantUserType), merchantId);
        for(Map<String,Object> map: maps){
            QueryManagerResponseDto responseDto = VoPoConverter.copyProperties(map, QueryManagerResponseDto.class);
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    /**
     * 当merchantUserType = 0：查询收银员列表
     * 当merchantUserType = 1：查询主管列表
     * 当merchantUserType = 2：查询法人列表
     * 当merchantUserType = 3：查询注册者列表
     * 当merchantUserType = 4：查询特权行使人列表
     * 当merchantUserType = 5：查询客服人员列表
     * @param mechantId
     * @param merchantUserType
     * @return
     */
    public List<MerchantManager> getMerchantManagerListByMerchantId(String mechantId, Integer merchantUserType){
        return merchantManagerService.getMerchantManagerByMerchantId(mechantId, getMerchantUserType(merchantUserType));
    }

    /**
     * 获取商家人员列表
     * @param merchantId
     * @return
     */
    public List<MerchantManager> getMerchantManagerByMerchantId(String merchantId){
        return merchantManagerService.getMerchantManagerByMerchantId(merchantId);
    }

    public boolean checkAdmin(String merchantId,String userNumber){
        return merchantManagerService.countByMerchantId(merchantId, userNumber)>0;
    }

    private String getMerchantUserType(Integer merchantUserType){
        if(merchantUserType == 0){
            return MerchantManagerEnum.CASHIER.getName();
        }else if(merchantUserType == 1){
            return MerchantManagerEnum.MANAGER.getName();
        }else if(merchantUserType == 2){
            return MerchantManagerEnum.LEGAL.getName();
        }else if(merchantUserType == 3){
            return MerchantManagerEnum.REGISTER.getName();
        }else if(merchantUserType == 4){
            return MerchantManagerEnum.PRIVILEGE.getName();
        }else if(merchantUserType == 5){
            return MerchantManagerEnum.CUSTOMERSERVICE.getName();
        }
        return null;
    }

    public void updateManager(String id, String merchantId, String merchantUserType){
        merchantManagerService.updateIsCurrentByMerchantId(merchantId, merchantUserType);
        merchantManagerService.updateIsCurrentById(id);
    }
}
