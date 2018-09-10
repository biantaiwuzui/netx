package com.netx.fuse.biz.credit;

import com.netx.credit.biz.CreditAction;
import com.netx.credit.biz.CreditCashierAction;
import com.netx.credit.biz.CreditSubscriptionAction;
import com.netx.credit.model.Credit;
import com.netx.credit.model.CreditCashier;
import com.netx.credit.model.CreditCategory;
import com.netx.credit.model.constants.CreditStageName;
import com.netx.credit.model.constants.CreditSubscriptionTypeEnum;
import com.netx.credit.service.CreditCategoryService;
import com.netx.credit.service.CreditService;
import com.netx.credit.vo.*;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.biz.productcenter.CategoryAction;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.service.merchantcenter.MerchantManagerService;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.service.productcenter.CategoryService;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author lanyingchu
 * @date 2018/7/9 19:28
 */

@Service
public class CreditFuseAction {

    @Autowired
    private CreditAction creditAction;
    @Autowired
    private CreditCashierAction creditCashierAction;
    @Autowired
    private UserService userService;
    @Autowired
    private MerchantManagerAction merchantManagerAction;
    @Autowired
    private MerchantManagerService merchantManagerService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CreditCategoryService creditCategoryService;
    @Autowired
    private CategoryAction categoryAction;
    @Autowired
    private CreditSubscriptionAction creditSubscriptionAction;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CreditService creditService;

    /**
     * 发行网信/ 编辑网信
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> publishCredit(PublishCreditRequestDto requestDto, String userId) throws Exception {
        // 将金额转化为分
        requestDto.setPresaleUpperLimit(requestDto.getPresaleUpperLimit() * 100);
        requestDto.setEnsureDeal(requestDto.getEnsureDeal() * 100);
        Map<String, String> result = new HashMap<>();
        // 进入编辑网信方法
        if (requestDto.getCreditId() != null) {
            return creditAction.editCredit(requestDto);
        }
        // 检验预售信息
        boolean isCanPublish = creditAction.isCanPublishCredit(requestDto, userId);
        if (!isCanPublish) {
            result.put("cantPublish", "您已申请预售成功，请勿重复申请");
            return result;
        }
        // 获取当前用户网号
        String userNetworkNumber = userService.getUserNumberByUserId(userId);
        // 该用户是否为商家人员
        if (!merchantManagerAction.checkAdmin(requestDto.getMerchantId(), userNetworkNumber)) {
            result.put("notManager", "您不是商家人员");
            return result;
        }
        // 根据网号获取当前用户在该商家的身份
        List<MerchantManager> merchantManagerList = merchantManagerService.getMerchantManagersByUserNetworkNum(userNetworkNumber);
        List<String> managerEnumList = Arrays.asList("注册者","法人代表","业务主管","收银人员");
        // 该用户是否为管理人员
        Boolean temp = false;
        for (MerchantManager list : merchantManagerList) {
            if (managerEnumList.contains(list.getMerchantUserType())) {
                temp = true;
                break;
            }
        }
        if (!temp) {
            result.put("noPermissions", "您没有发行权限");
            return result;
        }
        // 添加收银人
        CreditCashier creditCashier = this.addCashier(requestDto, userId);
        // 插入网信信息
        String creditId = creditAction.publishCredit(requestDto, userId, creditCashier.getId());
        // 修改标签类别
        this.insertCreditCategory(creditId, requestDto.getCategoryId(), requestDto.getTagIds());
        // 邀请好友参加内购
        if (requestDto.getToUserId() != null) {
            CreditSubscriptionDto creditSubscriptionDto = this.setInnerSubscriptionInfo(null, requestDto.getToUserId(), 0, 0);
            this.addSubscriptionInfo(creditSubscriptionDto, userId, creditId, CreditSubscriptionTypeEnum.INNERFRIEND.getName());
        }
        requestDto.getCreditScope().getMerchantIds().add(requestDto.getMerchantId());
        // 添加适用范围的商家人员进入内购(包括自身商家)
        for (String merchantId : requestDto.getCreditScope().getMerchantIds()) {
            // 获取商家人员
            List<MerchantManager> merchantManagers = merchantManagerService.getMerchantManagerByMerchantId(merchantId);
            // 邀请商家人员进入内购
            for (MerchantManager merchantManager : merchantManagers) {
                CreditSubscriptionDto creditSubscriptionDto = this.setInnerSubscriptionInfo(merchantId, merchantManager.getUserId(), 0, 0);
                this.addSubscriptionInfo(creditSubscriptionDto, userId, creditId, merchantManager.getMerchantUserType());
            }
        }
        result.put("creditId", creditId);
        return result;
    }



    /**
     * 添加收银人
     */
    @Transactional(rollbackFor = Exception.class)
    public CreditCashier addCashier(PublishCreditRequestDto requestDto, String userId) {
        AddCashierRequestDto cashierRequestDto = new AddCashierRequestDto();
        User user = userService.getUserByUserNumber(requestDto.getCashierNetworkNum());
        if (user != null) {
            cashierRequestDto.setMerchantId(requestDto.getMerchantId());
            cashierRequestDto.setMerchantUserId(userId);
            cashierRequestDto.setCashierNetworkNum(requestDto.getCashierNetworkNum());
            cashierRequestDto.setCashierName(requestDto.getCashierName());
            cashierRequestDto.setCashierPhone(requestDto.getCashierPhone());
            cashierRequestDto.setCashierIdNumber(requestDto.getCashierIdNumber());
            return creditCashierAction.addCashier(cashierRequestDto);
        }
        throw new RuntimeException("添加收银人操作异常");
    }

    /**
     * 修改类别标签
     */
    private void insertCreditCategory(String creditId, List<String> categoryId, List<String> tagIds) {
        this.addCategory(categoryId, creditId);
        this.addCategory(tagIds, creditId);
    }

    /**
     * 添加网信类别标签方法
     */
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(List<String> array, String creditId) {
        for(String categoryId : array) {
            CreditCategory creditCategory = new CreditCategory();
            Category category = categoryService.selectById(categoryId);
            if (category != null) {
                creditCategory.setCreditId(creditId);
                creditCategory.setCategoryId(categoryId);
                creditCategory.setCreditTime(new Date());
                creditCategory.setUpdateTime(new Date());
                creditCategory.setDeleted(0);
                creditCategoryService.insert(creditCategory);
                categoryAction.updateUsedCount(categoryId, 1);
            }
        }
    }

    /**
     * 添加认购信息（内购，开放认购）
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSubscriptionInfo(CreditSubscriptionDto dto, String userId, String creditId, String subscriptionType) {
        CreditSubscriptionDto creditSubscriptionDto = new CreditSubscriptionDto();
        User user = userService.selectById(userId);
        String message = null;
        creditSubscriptionDto.setCreditId(creditId);
        creditSubscriptionDto.setMerchantId(dto.getMerchantId());
        creditSubscriptionDto.setUserId(dto.getUserId());
        creditSubscriptionDto.setStatus(dto.getStatus());
        creditSubscriptionDto.setSubscriptionNumber(dto.getSubscriptionNumber());
        creditSubscriptionDto.setType(subscriptionType);
        if (CreditSubscriptionTypeEnum.NOMAL.getName().equals(subscriptionType)){
            creditSubscriptionDto.setCreditStageId(CreditStageName.CSN_COMMON.getStageName());
        } else if (CreditSubscriptionTypeEnum.FRIEND.getName().equals(subscriptionType)) {
            creditSubscriptionDto.setCreditStageId(CreditStageName.CSN_FRIEND.getStageName());
        } else {
            creditSubscriptionDto.setCreditStageId(CreditStageName.CSN_TOP.getStageName());
            message = user.getNickname() + "邀请你一起报名参加网信内购";
        }
        if (StringUtils.isNotBlank(creditSubscriptionDto.getUserId())) {
            String[] toUser = creditSubscriptionDto.getUserId().split(",");
            for (String toUserId : toUser) {
                creditSubscriptionDto.setUserId(toUserId);
                creditSubscriptionAction.add(creditSubscriptionDto, message);
            }
        }
    }

    /**
     * 添加内购信息
     */
    private CreditSubscriptionDto setInnerSubscriptionInfo(String merchantId, String userId, double subscriptionNumber, int status) {
        CreditSubscriptionDto creditSubscriptionDto = new CreditSubscriptionDto();
        creditSubscriptionDto.setMerchantId(merchantId);
        creditSubscriptionDto.setUserId(userId);
        creditSubscriptionDto.setSubscriptionNumber(subscriptionNumber);
        creditSubscriptionDto.setStatus(status);
        return creditSubscriptionDto;
    }

    /**
     * 获取商家所在商家及其发行上限(发行网信前)
     */
    public List<BeforPublishVo> getUserMerchantAndLimit(String userId) {
        List<BeforPublishVo> beforPublishVoList = new ArrayList<>();
        BeforPublishVo beforPublishVo = new BeforPublishVo();
        List<String> merchantIds = merchantManagerService.getUserMerchantIds(userId);
        for (String merchantId : merchantIds) {
            beforPublishVo.setMerchantId(merchantId);
            beforPublishVo.setMerchantNmae(merchantService.getMerchantNameById(merchantId));
            // 不理解需求文档，暂定36900
            if (creditService.getUpperLimit(userId, merchantId) == null) {
                beforPublishVo.setPresaleUpperLimit(36900);
            } else {
                beforPublishVo.setPresaleUpperLimit(36900);
            }
            beforPublishVoList.add(beforPublishVo);
        }
        return beforPublishVoList;
    }


}
