package com.netx.fuse.biz.shoppingmall.business;

import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.UserInfoRequestDto;
import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import com.netx.common.user.dto.common.CommonUserBaseInfoDto;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.RegisterSellerRequestDto;
import com.netx.common.vo.currency.WzCommonOtherSetResponseDto;
import com.netx.fuse.biz.FuseBaseAction;
import com.netx.fuse.biz.shoppingmall.marketing.RecordingHistoryFuseAction;
import com.netx.fuse.client.shoppingmall.SellerClientAction;
import com.netx.fuse.client.ucenter.OtherSetClientAction;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.shopping.biz.business.*;
import com.netx.shopping.biz.marketing.RelationshipAction;
import com.netx.shopping.biz.order.HashCheckoutAction;
import com.netx.shopping.enums.BusinessJobEnum;
import com.netx.shopping.enums.PromotionAwardEnum;
import com.netx.shopping.enums.SellerStatusEnum;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.service.business.SellerService;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SellerRegisterFuseAction extends FuseBaseAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    HashCheckoutAction hashCheckoutAction;

    @Autowired
    SellerRegisterAction sellerRegisterAction;

    @Autowired
    OtherSetClientAction otherSetClientAction;

    @Autowired
    SellerService sellerService;

    @Autowired
    SellerAction sellerAction;

    @Autowired
    SellerFuseAction sellerFuseAction;

    @Autowired
    UserClientProxy userClientProxy;

    @Autowired
    RelationshipAction relationshipAction;

    @Autowired
    SellerClientAction sellerClientAction;

    @Autowired
    RecordingHistoryFuseAction recordingHistoryFuseAction;

    @Autowired
    CashierAction cashierAction;

    @Autowired
    ManageAction manageAction;

    @Autowired
    SellerCategoryAction sellerCategoryAction;

    @Autowired
    PacketSetAction packetSetAction;

    public Seller register(RegisterSellerRequestDto request) throws Exception{

        //判断是否重复提交
        boolean res = hashCheckoutAction.hashCheckout(request.getHash());
        if (!res) {
            throw new Exception("机器无效提交订单");
        }

        //检验注册信息
        boolean b = sellerRegisterAction.isCanRegisterSeller(request);
        if (!b) {
            throw new Exception("你已注册过此商家");
        }

        //判断是否可以注册商家
//        WzCommonOtherSetResponseDto otherSetResult = otherSetClientAction.queryRemote();
//        if (otherSetResult == null) {
//            throw new Exception("远程其他设置查询失败");
//        }
//        //WzCommonOtherSetResponseDto one = otherSetResult.getObject();
//        StringBuffer buffer = this.booleanIsCanRelease(request.getUserId(), otherSetResult);
//        if (!buffer.toString().equals("")) {
//            throw new Exception(buffer.toString());
//        }

        Seller seller = new Seller();
        Boolean flag = StringUtils.isNotBlank(request.getId());
        if (flag) {
            seller = updateSeller(request);
        } else {
            BeanUtils.copyProperties(request, seller);
            seller.setCreateUserId(request.getUserId());
        }
        if (seller == null) {
            return null;
        }
        seller.setStatus(SellerStatusEnum.NORMAL.getCode());
        seller.setPacSetId(request.getPacSetId());
        seller.setCustomerCode(sellerRegisterAction.buildCustomerCode());
        seller.setDeleted(0);
        sellerService.insertOrUpdate(seller);

        if (!flag) {
            sellerRegisterAction.createBusinessNum(seller.getId(), sellerRegisterAction.getCity(seller.getCityCode(), seller.getProvinceCode()));
        }
        //注册完成后生成付款码
//        sellerRegisterAction.generateSellerQrcode(seller.getId());
        //注册完成后修改收银人员表
        cashierAction.setSellerIdAndIsCurrent(seller.getId(),seller.getSellerCashierId());
        //注册完成后修改主管表
        manageAction.setSellerIdAndIsCurrent(seller.getId(),seller.getManageId());
        //注册完成后操作商家类别关系表
        //sellerCategoryAction.insertOrUpdate(flag,seller.getId(),request.getCategoryId(),request.getTagIds(),request.getUserId());
        //注册完成后修改红包表
        packetSetAction.updatePacketSet(seller.getId(),seller.getPacSetId());


        //判断是否投送信息提醒缴商家管理费用
        if (request.getIsPayAtOne() != null && request.getIsPayAtOne() == 2) {
           this.sendManageFeeMessage(seller);
        }
        return sellerService.selectById(seller);
    }

    public void sendManageFeeMessage(Seller seller){
        //向注册者、法人、收银推送信息
        MessageFormat messageFormat = new MessageFormat("你注册的{0}商家尚未支付注册管理费，请在{1}前完成缴费，逾期未缴，本次注册将自动失效");
        //获取法人代表和收银人员userId
        UserInfoRequestDto userInfoRequestDto = new UserInfoRequestDto();

        userInfoRequestDto.setSelectData(seller.getVerifyNetworkNum());//输入查询数据
        userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);//设计查询条件
        List<SelectFieldEnum> selectFieldEnumList = new ArrayList<>();//设置查询返回结果
        selectFieldEnumList.add(SelectFieldEnum.USER_ID);
        userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
        String verifyUserId = getUserId(userInfoRequestDto);

        String moneyNetworkNum = cashierAction.selectById(seller.getSellerCashierId()).getMoneyNetworkNum();
        userInfoRequestDto.setSelectData(moneyNetworkNum);//输入查询数据
        userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);//设计查询条件
        userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
        String moneyUserId = getUserId(userInfoRequestDto);

        //获取10天后的日期
        long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 10, 2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String tenDateAfter = simpleDateFormat.format(start);
        String alertMsg = messageFormat.format(new String[]{seller.getName(), tenDateAfter});

        //去重后发推送信息
        List<String> list = new ArrayList<>();
        list.add(seller.getUserId());
        list.add(verifyUserId);
        list.add(moneyUserId);
        List<String> list1 = sellerAction.getUntqueuUserId(list);
        for (String string : list1) {
            sellerFuseAction.sendMessage(alertMsg, string, seller.getId());
        }

        //启动10天后定时任务，判断是否缴费，否，商家状态改为下架
//            businessQuartzService.changeSellerStatus(seller.getUserId(), start);
        //TODO 定时任务
    }

    private Seller updateSeller(RegisterSellerRequestDto request) {
        Seller seller = sellerService.selectById(request.getId());
        if (seller != null) {
            //当其他管理人员修改时，保持注册userId一致
            request.setUserId(seller.getUserId());

            VoPoConverter.copyProperties(request, seller);
            if (seller.getPayStatus() == 0 && request.getReferralCode() != null) {
                if (relationshipAction.getRelationPSellerId(request.getId()) == null) {
                    if (buileRelationShip(seller)) {
                        return null;
                    }
                    //关闭定时任务
//                    if (!businessQuartzService.timingDoSeller(seller.getId(), false, System.currentTimeMillis())) {//启动时间这里不重要
//                        logger.info("关闭24小时内填写引荐人推荐码的定时任务");
//                    }
                    //TODO 定时任务
                }
            }
        }
        return seller;
    }

    /**
     * 晋级奖励
     *
     * @param seller
     * @param awardEnum
     */
    private void promotionMoney(Seller seller, PromotionAwardEnum awardEnum) {
        Integer no = sellerRegisterAction.getJobNum(awardEnum, seller.getCityCode()) + 1;
        Integer[] award = awardEnum.getAward();
        if (no < award.length) {
            seller.setAchievementMonth(seller.getAchievementMonth() + award[no - 1]);
            seller.setAchievementTotal(seller.getAchievementTotal() + award[no - 1]);
            //添加流水
            sellerClientAction.addBill("第" + no + "名晋级为" + awardEnum.getName() + "的奖励", BigDecimal.valueOf(award[no - 1]), seller.getUserId());
        }
    }

    private void computSubsidy(Seller seller, Seller pseller, int groupNum, Boolean type, Boolean flag) {
        Integer subsidy = type ? ((groupNum - 1) * 90 + 1600) : ((groupNum - 1) * 50 + 300);
        recordingHistoryFuseAction.addRecording(seller, pseller, type ? 0 : 1, subsidy);
        if (flag) {
            subsidy += type ? (groupNum * 30 * 90) : 300;
            //添加流水
            sellerClientAction.addBill((type ? "" : "间接") + "升组补贴", new BigDecimal(subsidy), pseller.getUserId());
        }
        pseller.setAchievementMonth(pseller.getAchievementMonth() + subsidy);
        pseller.setAchievementTotal(pseller.getAchievementTotal() + subsidy);
    }


    private String getUserId(UserInfoRequestDto dto) {
        UserInfoResponseDto responseDto = userClientProxy.selectUserInfo(dto);
        if (responseDto == null || responseDto.getUserId() == null) {
            return null;
        }
        return responseDto.getUserId();
    }

    public void payStatusSuccess(Seller seller) {
        if (buileRelationShip(seller)) {
            //开启24小时内填写引荐人推荐码的定时任务
//            if (!businessQuartzService.timingDoSeller(seller.getId(), true, DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(System.currentTimeMillis(), 1, 2))) {
//                logger.info("启动开启24小时内填写引荐人推荐码的定时任务");
//            }
            //TODO 定时任务
        }
    }

    private void increase(Seller seller, Seller pseller) {
        pseller.setMonthNum(pseller.getMonthNum() + 1);
        pseller.setMonthSecondNum(pseller.getMonthSecondNum() + 1);
        pseller.setDayNum(pseller.getDayNum() + 1);
        if (pseller.getJob() > 0) {
            for (BusinessJobEnum jobEnum : BusinessJobEnum.values()) {
                if (pseller.getJob() == jobEnum.getJob()) {
                    computMoney(pseller, jobEnum);
                    break;
                }
            }
        } else {
            if (pseller.getMonthNum() == 5) {
                pseller.setJob(pseller.getJob() + 1);
                promotionMoney(pseller, PromotionAwardEnum.BUSINESS_AWARD);
            }
        }

        Seller superSeller = sellerRegisterAction.getSellerByPid(pseller.getId());
        Integer groupNum = sellerRegisterAction.getGroupNum(pseller.getSecondNum());
        Boolean flag = sellerRegisterAction.checkUpGroup(pseller.getSecondNum());
        computSubsidy(seller, pseller, groupNum, true, flag);
        sellerService.updateById(pseller);
        if (superSeller != null) {
            superSeller.setThirdNum(superSeller.getThirdNum() + 1);
            superSeller.setMonthThirdNum(superSeller.getMonthThirdNum() + 1);
            superSeller.setDayNum(superSeller.getDayNum() + 1);
            superSeller.setMonthNum(superSeller.getMonthNum() + 1);
            computSubsidy(pseller, superSeller, groupNum, false, flag);
            sellerService.updateById(superSeller);
            sellerRegisterAction.addBusinessNum(superSeller.getId(), sellerRegisterAction.getCity(superSeller.getCityCode(), superSeller.getProvinceCode()));
        }
    }

    /**
     * 晋级奖励
     */
    private void computMoney(Seller seller, BusinessJobEnum jobEnum) {
        if (seller.getSecondNum() == jobEnum.getNum()) {
            seller.setJob(seller.getJob() + 1);
            promotionMoney(seller, jobEnum.getPromotionAwardEnum());
        }
    }

    public void buildRelationship(Seller seller, Seller pSeller) {
        //关系绑定
        if (relationshipAction.buildRelationship(seller.getId(), pSeller.getId(), seller.getId())) {
            increase(seller, pSeller);
        }
    }

    private Boolean buileRelationShip(Seller seller) {
        if (seller.getReferralCode() != null) {
            Seller pSeller = sellerService.checkCustomerCode(seller.getReferralCode());
            if (pSeller != null) {
                pSeller.setSecondNum(pSeller.getSecondNum() + 1);
                seller.setGroupNo(pSeller.getSecondNum());
                sellerService.insertOrUpdate(seller);
                sellerRegisterAction.addBusinessNum(pSeller.getId(), sellerRegisterAction.getCity(pSeller.getCityCode(), pSeller.getProvinceCode()));
                buildRelationship(seller, pSeller);
                return false;
            }
        }
        return true;
    }

    /**
     * 自用户注册商家起后，24小时后未填写引荐人的客服代码，则执行这个服务
     *
     * @param sellerId
     * @return
     */

    public Boolean timingDoSeller(String sellerId) {
        Seller seller = sellerService.selectById(sellerId);
        if (seller != null) {
            Seller pSeller = sellerRegisterAction.checkArea(seller.getCityCode()) ? sellerRegisterAction.getSellerByCreateTime(seller.getUserId()) : sellerRegisterAction.getAreaFristBusiness(seller.getProvinceCode(), seller.getCityCode());
            if (pSeller != null) {
                seller.setReferralCode(pSeller.getCustomerCode());
                pSeller.setSecondNum(pSeller.getSecondNum() + 1);
                seller.setGroupNo(pSeller.getSecondNum());
                sellerService.updateById(seller);
                sellerRegisterAction.addBusinessNum(pSeller.getId(), sellerRegisterAction.getCity(pSeller.getCityCode(), pSeller.getProvinceCode()));
                buildRelationship(seller, pSeller);
                return true;
            }
        }
        return false;
    }

}
