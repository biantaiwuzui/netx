package com.netx.fuse.biz.shoppingmall.merchantcenter;

import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.merchantcenter.constants.MechantVerifyTypeEnum;
import com.netx.shopping.model.merchantcenter.constants.MerchantManagerEnum;
import com.netx.shopping.service.merchantcenter.MerchantVerifyInfoService;
import com.netx.shopping.vo.ManagerListResponseVo;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.UserService;
import com.netx.ucenter.vo.UserIdAndNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MerchantManagerFuseAction {

    @Autowired
    private MerchantManagerAction merchantManagerAction;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAction userAction;
    @Autowired
    private MerchantVerifyInfoService merchantVerifyInfoService;

    public List<ManagerListResponseVo> getMerchantManagerByMerchantId(String merchantId){
        List<ManagerListResponseVo> responseVos = new ArrayList<>();
        List<MerchantManager> merchantManagerList = merchantManagerAction.getMerchantManagerByMerchantId(merchantId);
        for(MerchantManager merchantManager : merchantManagerList){
            responseVos.add(createMerchantManager(merchantManager));
        }
        return responseVos;
    }

    public ManagerListResponseVo createMerchantManager(MerchantManager merchantManager){
        ManagerListResponseVo response = new ManagerListResponseVo();
        try {
            if(merchantManager.getMerchantUserType().equals(MerchantManagerEnum.LEGAL.getName())){
                String idCard = merchantVerifyInfoService.getIdCardByMerchantId(merchantManager.getMerchantId(), MechantVerifyTypeEnum.IDCARD);
                if(idCard != null){
                    response.setIdCard(idCard);
                }
            }
            User user = userService.getUserByUserNumber(merchantManager.getUserNetworkNum());
            UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg(user);
            if(userInfoAndHeadImg != null){
                VoPoConverter.copyProperties(userInfoAndHeadImg, response);
                VoPoConverter.copyProperties(merchantManager, response);
                return response;
            }
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 获取主管与收银员
     * @return
     */
    public Map<String,UserIdAndNumber> queryMangerByMerchantId(String merchantId){
        List<MerchantManager> merchantManagers = merchantManagerAction.getMerchantManagerService().getUserNetworkNumByMerchantId(merchantId,MerchantManagerEnum.MANAGER.getName(),MerchantManagerEnum.CASHIER.getName());
        Map<String,UserIdAndNumber> map = new HashMap<>();
        if(merchantManagers!=null){
            merchantManagers.forEach(merchantManager -> {
                map.put(merchantManager.getMerchantUserType(),createUserIdAndNumber(merchantManager.getUserNetworkNum()));
            });
        }
        return map;
    }

    private UserIdAndNumber createUserIdAndNumber(String userNumber){
        UserIdAndNumber userIdAndNumber = new UserIdAndNumber();
        userIdAndNumber.setUserNumber(userNumber);
        userIdAndNumber.setUserId(userService.getUserIdByUserNumber(userNumber));
        return userIdAndNumber;
    }
}
