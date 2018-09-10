package com.netx.fuse.biz.shoppingmall.merchantcenter;

import com.netx.common.common.enums.AuthorEmailEnum;
import com.netx.common.common.enums.JobEnum;
import com.netx.common.common.utils.GenerateQrcodeUtil;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.UserInfoRequestDto;
import com.netx.common.user.dto.wangMing.AddCreditRecordRequestDto;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.fuse.biz.ucenter.WallerFrozenFuseAction;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.shopping.biz.merchantcenter.*;
import com.netx.shopping.biz.ordercenter.HashCheckoutAction;
import com.netx.shopping.biz.productcenter.PropertyAction;
import com.netx.shopping.enums.BusinessJobEnum;
import com.netx.shopping.enums.PromotionAwardEnum;
import com.netx.shopping.model.marketingcenter.MerchantRecordingHistory;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.merchantcenter.MerchantVerifyInfo;
import com.netx.shopping.model.merchantcenter.constants.*;
import com.netx.shopping.service.marketingcenter.MerchantRecordingHistoryService;
import com.netx.shopping.service.merchantcenter.MerchantManagerService;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.service.merchantcenter.MerchantVerifyInfoService;
import com.netx.shopping.vo.*;
import com.netx.ucenter.biz.user.UserCreditAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.UserService;
import com.netx.ucenter.service.user.UserVerifyService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MerchantRegisterFuseAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    HashCheckoutAction hashCheckoutAction;
    @Autowired
    MerchantRegisterAction merchantRegisterAction;
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantPictureAction merchantPictureAction;
    @Autowired
    MerchantManagerService merchantManagerService;
    @Autowired
    UserService userService;
    @Autowired
    MerchantCategoryAction merchantCategoryAction;
    @Autowired
    MerchantFuseAction merchantFuseAction;
    @Autowired
    MerchantAction merchantAction;
    @Autowired
    UserClientProxy userClientProxy;
    @Autowired
    UserVerifyService userVerifyService;
    @Autowired
    MerchantVerifyInfoService merchantVerifyInfoService;
    @Autowired
    JobFuseAction jobFuseAction;
    @Autowired
    PropertyAction propertyAction;
    @Autowired
    MerchantManagerAction merchantManagerAction;
    @Autowired
    ShippingFeeAction shippingFeeAction;
    @Autowired
    MerchantPacketSetAction merchantPacketSetAction;
    @Autowired
    WallerFrozenFuseAction wallerFrozenFuseAction;
    @Autowired
    MerchantRecordingHistoryService merchantRecordingHistoryService;
    @Autowired
    UserCreditAction userCreditAction;

    /**
     * 注册
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public Merchant register(RegisterMerchantRequestDto request){
        boolean res = hashCheckoutAction.hashCheckout(request.getUserId(),request.getHash());
        if (!res) {
            throw new RuntimeException("请勿重复提交！");
        }
        //检验注册信息
        boolean b = merchantRegisterAction.isCanRegisterMerchant(request);
        if (!b) {
            throw new RuntimeException("你已注册过此商家");
        }
        Date date = new Date();
        Merchant merchant = new Merchant();
        VoPoConverter.copyProperties(request, merchant);
        merchant.setStatus(MerchantStatusEnum.NORMAL.getCode());
        merchant.setPayStatus(1);
        merchant.setDeleted(0);
        merchant.setSupportCredit(false);
        merchantService.insert(merchant);
        merchantRegisterAction.createBusinessNum(merchant.getId(), merchantRegisterAction.getCity(merchant.getCityCode(), merchant.getProvinceCode()));
        //添加注册者
        addRegister(merchant.getUserId(), merchant.getId());
        //添加法人代表
        MerchantManager merchantManager = addVerify(request, merchant.getId());
        if(merchantManager != null){
            //修改添加验证表
            verify(merchantManager.getUserId(), request.getVerifyIdCard(), request.getVerifyPhone(), merchant.getId());
        }
        //注册完成后修改主管表
        updateManager(merchant.getId(), request.getManagerId());
        updateManager(merchant.getId(), request.getCashierId());
        updateManager(merchant.getId(), request.getCustomerServiceId());
        //注册完成后添加图片
        merchantPictureAction.addMerchantPicture(request.getLogoImagesUrl(), MerchantPictureEnum.LOGO, merchant.getId());
        merchantPictureAction.addMerchantPicture(request.getMerchantImagesUrl(), MerchantPictureEnum.MERCHANT, merchant.getId());
        merchantPictureAction.addMerchantPicture(request.getCertiImagesUrl(), MerchantPictureEnum.CERTI, merchant.getId());
        //修改关联类目表
        merchantCategoryAction.insertOrUpdate(false, merchant.getId(), request.getCategoryId(), request.getTagIds());
        //更新红包设置
        merchantPacketSetAction.updatePacketSet(request.getPacSetId(), merchant.getId());
        //生成默认费用设置
        shippingFeeAction.addOrUpdate(merchant.getId(), BigDecimal.ZERO);
        //修改红包设置
        merchantPacketSetAction.updateMerchantId(request.getPacSetId(), merchant.getId());
        //生成唯一规格
        AddPropertyRequestDto addPropertyRequestDto = new AddPropertyRequestDto();
        addPropertyRequestDto.setMerchantId(merchant.getId());
        addPropertyRequestDto.setName("规格");
        propertyAction.addProperty(addPropertyRequestDto);
        //判断是否投送信息提醒缴商家管理费用
        if (request.getIsPayAtOne() != null && request.getIsPayAtOne() == 2) {
            this.sendManageFeeMessage(merchant);
        }
        //创建定时任务【注册后一个月，判断每个月是否有发货】
        jobFuseAction.addJob(JobEnum.MERCHANT_SUBTRACT_CREDIT_JOB,merchant.getId(),merchant.getId(),"注册后，一个月未发货处罚",DateTimestampUtil.getCronByMonth(date,1), AuthorEmailEnum.ZI_AN);
        //创建定时任务【注册后三个月，判断每个季度是否有交易】
        jobFuseAction.addJob(JobEnum.MERCHANT_TRANSACTION_PUNISH_JOB,merchant.getId(),merchant.getId(),"注册后，三个月未有交易记录处罚",DateTimestampUtil.getCronByQuarter(date,3), AuthorEmailEnum.ZI_AN);
        return merchant;
    }

    public Merchant edit(RegisterMerchantRequestDto request){
        Merchant merchant = merchantService.selectById(request.getId());
        if(merchant == null){
            return null;
        }
        VoPoConverter.copyProperties(request, merchant);
        merchantService.updateById(merchant);
        //更新红包设置
        merchantPacketSetAction.updatePacketSet(request.getPacSetId(), merchant.getId());
        //更新图片
        if(request.getDeleteImagesUrlId() != null && request.getDeleteImagesUrlId().size() > 0){
            merchantPictureAction.getMerchantPictureService().deleteBatchIds(request.getDeleteImagesUrlId());
        }
        if(request.getLogoImagesUrl() != null) {
            merchantPictureAction.addMerchantPicture(request.getLogoImagesUrl(), MerchantPictureEnum.LOGO, merchant.getId());
        }
        if(request.getMerchantImagesUrl() != null) {
            merchantPictureAction.addMerchantPicture(request.getMerchantImagesUrl(), MerchantPictureEnum.MERCHANT, merchant.getId());
        }
        if(request.getCertiImagesUrl() != null) {
            merchantPictureAction.addMerchantPicture(request.getCertiImagesUrl(), MerchantPictureEnum.CERTI, merchant.getId());
        }
        //修改关联类目表
        merchantCategoryAction.insertOrUpdate(true, merchant.getId(), request.getCategoryId(), request.getTagIds());
        //更新人员
        merchantManagerAction.updateManager(request.getManagerId(), merchant.getId(), MerchantManagerEnum.MANAGER.getName());
        merchantManagerAction.updateManager(request.getCashierId(), merchant.getId(), MerchantManagerEnum.CASHIER.getName());
        merchantManagerAction.updateManager(request.getCustomerServiceId(), merchant.getId(), MerchantManagerEnum.CUSTOMERSERVICE.getName());
        return merchant;
    }


    /**
     * 添加注册者
     * @param userId
     * @param merchantId
     * @return
     */
    public MerchantManager addRegister(String userId, String merchantId){
        AddManagerRequestDto requestDto = new AddManagerRequestDto();
        User user = userService.selectById(userId);
        if(user != null) {
            requestDto.setUserId(userId);
            requestDto.setMerchantId(merchantId);
            requestDto.setUserName(user.getRealName());
            requestDto.setUserNetworkNum(user.getUserNumber());
            requestDto.setUserPhone(user.getMobile());
            requestDto.setMerchantUserType(3);
            return merchantManagerAction.addManager(requestDto);
        }
        return null;
    }

    /**
     * 添加法人
     * @param request
     * @param merchantId
     * @return
     */
    public MerchantManager addVerify(RegisterMerchantRequestDto request, String merchantId){
        AddManagerRequestDto requestDto = new AddManagerRequestDto();
        requestDto.setUserId(request.getUserId());
        requestDto.setUserName(request.getVerifyCorporate());
        requestDto.setMerchantUserType(2);
        requestDto.setUserNetworkNum(request.getVerifyNetworkNum());
        requestDto.setUserPhone(request.getVerifyPhone());
        requestDto.setMerchantId(merchantId);
        return merchantManagerAction.addManager(requestDto);
    }

    /**
     * 修改主管/收银员
     * @param merchantId
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateManager(String merchantId, String id){
        MerchantManager merchantManager = merchantManagerService.selectById(id);
        if(merchantManager != null){
            merchantManager.setIsCurrent(1);
            if(StringUtils.isNotBlank(merchantManager.getMerchantId())) {
                if(!merchantManager.getMerchantId().equals(merchantId)){
                    merchantManager.setId(null);
                }
            }
            merchantManager.setMerchantId(merchantId);
            return merchantManagerService.insertOrUpdate(merchantManager);
        }
        return false;
    }

    /**
     * 修改验证表
     * @param userId
     * @param idCard
     * @param phone
     * @param merchantId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean verify(String userId, String idCard, String phone, String merchantId){
        MerchantVerifyInfo verifyIdCard = new MerchantVerifyInfo();
        verifyIdCard.setMerchantId(merchantId);
        verifyIdCard.setVerifyInfo(idCard);
        verifyIdCard.setVerifyType(MechantVerifyTypeEnum.IDCARD.getName());
        verifyIdCard.setDeleted(0);
        MerchantVerifyInfo verifyPhone = new MerchantVerifyInfo();
        verifyPhone.setMerchantId(merchantId);
        verifyPhone.setVerifyInfo(phone);
        verifyPhone.setVerifyType(MechantVerifyTypeEnum.PHONE.getName());
        verifyPhone.setDeleted(0);
        //获取用户是否用过验证
        Integer stauts = userVerifyService.selectStautsByUserId(userId);
        if(stauts == 1){
            verifyIdCard.setVerifyStatus(MerchantVerifyStatusEnum.ADOPT.getName());
            verifyPhone.setVerifyStatus(MerchantVerifyStatusEnum.ADOPT.getName());
        }else{
            verifyIdCard.setVerifyStatus(MerchantVerifyStatusEnum.NOTADOPT.getName());
            verifyPhone.setVerifyStatus(MerchantVerifyStatusEnum.NOTADOPT.getName());
        }
        return merchantVerifyInfoService.insertOrUpdate(verifyIdCard)&&merchantVerifyInfoService.insertOrUpdate(verifyPhone);
    }

    public void sendManageFeeMessage(Merchant merchant){
        //向注册者、法人、收银推送信息
        MessageFormat messageFormat = new MessageFormat("你注册的{0}商家尚未支付注册管理费，请在{1}前完成缴费，逾期未缴，本次注册将自动失效");

        //获取10天后的日期
        long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 10, 2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String tenDateAfter = simpleDateFormat.format(start);
        String alertMsg = messageFormat.format(new String[]{merchant.getName(), tenDateAfter});

        //去重后发推送信息
        List<String> list = merchantManagerService.getUseIdByMerchantId(merchant.getId());
        list.add(merchant.getUserId());
        List<String> list1 = merchantAction.getUntqueuUserId(list);
        for (String string : list1) {
            merchantFuseAction.sendMessage(alertMsg, string, merchant.getId());
        }

        //启动10天后定时任务，判断是否缴费，否，商家状态改为下架
//            businessQuartzService.changeSellerStatus(seller.getUserId(), start);
        //TODO 定时任务
    }

    /**
     * 生成商家二维码
     * @param merchantId
     * @return
     */
    public boolean generateMerchantQrcode(String merchantId) {
        int width = 300;
        int height = 300;
        String format = "png";
        String suffix = "data:image/png;base64,";
        String type = "\"type\":" + "\"B\"";
        String id = "\"id\":\"";
        String text = "{" + id + merchantId + "\"," + type + "}";
        try {
            String qrcodeBase64Str = GenerateQrcodeUtil.generateQRCodeStream(text, width, height, format);
            Merchant merchant = new Merchant();
            merchant.setId(merchantId);
            merchant.setQrcode(suffix + qrcodeBase64Str);
            merchantService.updateById(merchant);
        } catch (Exception e) {
            e.getMessage();
        }
        return true;
    }

    /**
     * 验证引荐人客服代码
     * @param referralServiceCode
     * @return
     */
    public Boolean isHaveReferralServiceCode(String referralServiceCode){
        Merchant referralMerchant = merchantService.getMerchantByCustomerServiceCode(referralServiceCode);
        if(referralMerchant != null){
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    boolean addCreditRecord(User user,String merchantId,int credit,String description){
        return userCreditAction.addCreditRecord(user,credit,description,merchantId,"Merchant");
    }

    /**
     * 商家管理费处理
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Merchant manageFee(MerchantManageFeeRequestDto request){
        Merchant merchant = merchantService.selectById(request.getMerchantId());
        if (merchant == null) {
            return null;
        }
        //交商家加盟费
        addBill(request.getUserId(),request.getDescription(),request.getAmount(),"999");
        merchant.setPayStatus(0);
        merchant.setFeeTime(new Date());
        merchant.setStatus(MerchantStatusEnum.NORMAL.getCode());
        merchant.setCustomerServiceCode(merchantRegisterAction.buildCustomerServiceCode());
        merchant.setExpireTime(DateTimestampUtil.addYearByDate(new Date(), request.getEffectiveTime()));
        if(merchant.getPosition() == null){
            merchant.setPosition(0);
        }
        merchantService.updateById(merchant);
        //添加特权行使人
        addPrivilege(request.getPrivilegeNetworkNum(), request.getUserId(), request.getMerchantId());
        //发放加盟奖励
        MerchantManager merchantManager = getMerchantOne(merchant.getId(), 2);
        User user = userService.selectById(merchantManager.getUserId());
        String description = "你管理的\""+merchant.getName()+"\"的商家加盟信用奖励。";
        if(user != null){
            addCreditRecord(user,merchant.getId(),100,description);
        }
        User legal = userService.getUserByUserNumber(merchantManager.getUserNetworkNum());
        if(legal != null){
            if(!legal.getId().equals(merchantManager.getUserId())){
                addCreditRecord(legal,merchant.getId(),100,description);
            }
        }

        if(StringUtils.isNotBlank(request.getReferralServiceCode())){
            /*if (relationshipAction.getRelationPSellerId(request.getId()) == null) {
                if (buileRelationShip(seller)) {
                    return null;
                }
                //关闭定时任务
//                    if (!businessQuartzService.timingDoSeller(seller.getId(), false, System.currentTimeMillis())) {//启动时间这里不重要
//                        logger.info("关闭24小时内填写引荐人推荐码的定时任务");
//                    }
                //TODO 定时任务
            }*/
            Merchant referralMerchant = merchantService.getMerchantByCustomerServiceCode(request.getReferralServiceCode());
            if(referralMerchant == null){
                throw new RuntimeException("引荐人的客服代码不存在");
            }
            buildRelationship(merchant,referralMerchant);
        }

        //启动商家缴费到期前10天提醒续缴费定时器

        if (request.getEffectiveTime() == 0)//终身
        {
            logger.info("终身");
        } else {
            long start = DateTimestampUtil.addDateStartOrEndOfDate(new Date(), 1, -10, 0, request.getEffectiveTime()).getTime();
//            businessQuartzService.messagePush(request.getSellerId(), start);
            //TODO 定时任务
        }

        //启动商家管理定时任务，注册成功后30天不发商品扣减信用值，90天无成交记录扣减信用值
        //获取当前时间30天后时间戳
        long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 30, 2);
        //开启30天后判断商家是否有发布商品的定时器
//        businessQuartzService.autoBusinessManagement(seller.getUserId(), start);
        //TODO 定时任务

        //获取90天后同一时间戳
        long start1 = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 90, 2);
        //开启90天后定时器
//        businessQuartzService.autoBusinessManagement1(seller.getUserId(), start1);
        //TODO 定时任务
        //merchantService.updateById(merchant);
        return merchant;

    }

    /**
     * 推荐商家成功，发放奖励
     * @param merchant
     * @return
     * @throws Exception
     */
    public boolean award(Merchant merchant) throws Exception{
        MerchantManager merchantPrivilege = getMerchantOne(merchant.getId(), 4);
        String privilegeUserNumber = merchantPrivilege.getUserNetworkNum();
        if(privilegeUserNumber != null) {
            String privilegeUserId = userService.getUserIdByUserNumber(privilegeUserNumber);
            if(privilegeUserId != null) {
                wallerFrozenFuseAction.add("999", getBillAddRequestDto(privilegeUserId, "加盟奖励发放1960元", new BigDecimal(1960)));
            }
        }
        if(merchant.getParentMerchantId() != null){
            Merchant parent = merchantService.selectById(merchant.getParentMerchantId());
            if(parent != null) {
                MerchantManager merchantParent = getMerchantOne(parent.getId(), 4);
                if(merchantParent != null) {
                    String parentUserNumber = merchantParent.getUserNetworkNum();
                    if(parentUserNumber != null) {
                        String parentUserId = userService.getUserIdByUserNumber(parentUserNumber);
                        if(parentUserId != null){
                            wallerFrozenFuseAction.add("999", getBillAddRequestDto(parentUserId, "次级加盟奖励发放500元", new BigDecimal(500)));
                        }
                    }
                }
            }
        }
        return true;
    }

    public BillAddRequestDto getBillAddRequestDto(String userId, String description, BigDecimal amount){
        BillAddRequestDto requestDto = new BillAddRequestDto();
        requestDto.setAmount(amount);
        requestDto.setToUserId(userId);
        requestDto.setDescription(description);
        requestDto.setType(0);
        requestDto.setPayChannel(3);
        return requestDto;
    }

    /**
     * 获取引荐人商家注册者和法人失败
     * @param merchantId
     * @param status
     * @return
     */
    public MerchantManager getMerchantOne(String merchantId, Integer status){
        List<MerchantManager> merchantManagers = merchantManagerAction.getMerchantManagerListByMerchantId(merchantId, status);
        if(merchantManagers == null || merchantManagers.size() < 1){
            throw new RuntimeException("获取引荐人商家注册者和法人失败！");
        }
        return merchantManagers.get(0);
    }

    /**
     * 添加特权行使人
     * @param privilegeNetworkNum
     * @param userId
     * @param merchantId
     * @return
     */
    public MerchantManager addPrivilege(String privilegeNetworkNum, String userId, String merchantId){
        try {
            User user = userService.getUserByUserNumber(privilegeNetworkNum);
            if(user != null) {
                AddManagerRequestDto requestDto = new AddManagerRequestDto();
                requestDto.setUserId(userId);
                requestDto.setMerchantId(merchantId);
                requestDto.setUserName(user.getRealName());
                requestDto.setUserNetworkNum(user.getUserNumber());
                requestDto.setUserPhone(user.getMobile());
                requestDto.setMerchantUserType(4);
                return merchantManagerAction.addManager(requestDto);
            }
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 上下级关系绑定
     * @param merchant
     * @param parentMerchant
     */
    @Transactional(rollbackFor = Exception.class)
    public void buildRelationship(Merchant merchant, Merchant parentMerchant) {
        //关系绑定
        //计算组内名次
        parentMerchant.setSecondNum(parentMerchant.getSecondNum()+1);
        merchant.setGroupNo(parentMerchant.getSecondNum());
        //上下级关系绑定
        merchant.setReferralServiceCode(parentMerchant.getCustomerServiceCode());
        merchant.setParentMerchantId(parentMerchant.getId());
        //奖励发放
        increase(merchant, parentMerchant);
        merchantService.updateById(merchant);
    }

    /**
     * 奖励发放
     * @param merchant
     * @param parentMerchant
     */
    @Transactional(rollbackFor = Exception.class)
    void increase(Merchant merchant, Merchant parentMerchant) {
        parentMerchant.setMonthNum(parentMerchant.getMonthNum() + 1);
        parentMerchant.setMonthSecondNum(parentMerchant.getMonthSecondNum() + 1);
        parentMerchant.setDayNum(parentMerchant.getDayNum() + 1);
        if (parentMerchant.getPosition() > 0) {
            for (BusinessJobEnum jobEnum : BusinessJobEnum.values()) {
                if (parentMerchant.getPosition() == jobEnum.getJob()) {
                    computMoney(parentMerchant, jobEnum);
                    break;
                }
            }
        } else {
            if (parentMerchant.getMonthNum() == 5) {
                parentMerchant.setPosition(parentMerchant.getPosition() + 1);
                promotionMoney(parentMerchant, PromotionAwardEnum.BUSINESS_AWARD);
            }
        }
        Merchant superMerchant = merchantService.selectById(parentMerchant.getParentMerchantId());
        Integer groupNum = merchantRegisterAction.getGroupNum(parentMerchant.getSecondNum());
        Boolean flag = merchantRegisterAction.checkUpGroup(parentMerchant.getSecondNum());
        computSubsidy(merchant, parentMerchant, groupNum, true, flag);
        merchantService.updateById(parentMerchant);
        if (superMerchant != null) {
            superMerchant.setThirdNum(superMerchant.getThirdNum() + 1);
            superMerchant.setMonthThirdNum(superMerchant.getMonthThirdNum() + 1);
            superMerchant.setDayNum(superMerchant.getDayNum() + 1);
            superMerchant.setMonthNum(superMerchant.getMonthNum() + 1);
            computSubsidy(parentMerchant, superMerchant, groupNum, false, flag);
            merchantService.updateById(superMerchant);
            merchantRegisterAction.createBusinessNum(superMerchant.getId(), merchantRegisterAction.getCity(superMerchant.getCityCode(), superMerchant.getProvinceCode()));
        }
    }

    /**
     * 晋级奖励
     */
    private void computMoney(Merchant merchant, BusinessJobEnum jobEnum) {
        if (merchant.getSecondNum() == jobEnum.getNum()) {
            merchant.setPosition(merchant.getPosition() + 1);
            promotionMoney(merchant, jobEnum.getPromotionAwardEnum());
        }
    }

    /**
     * 晋级奖励
     *
     * @param merchant
     * @param awardEnum
     */
    private void promotionMoney(Merchant merchant, PromotionAwardEnum awardEnum) {
        Integer no = merchantRegisterAction.getPosition(awardEnum, merchant.getCityCode()) + 1;
        Integer[] award = awardEnum.getAward();
        if (no < award.length) {
            merchant.setAchievementMonth(merchant.getAchievementMonth() + award[no - 1]);
            merchant.setAchievementTotal(merchant.getAchievementTotal() + award[no - 1]);
            //添加流水
            addBill("999","第" + no + "名晋级为" + awardEnum.getName() + "的奖励", BigDecimal.valueOf(award[no - 1]), merchant.getUserId());
        }
    }

    /**
     * 添加流水（平台给用户零钱）
     */
    @Transactional(rollbackFor = Exception.class)
    public void addBill(String userId,String description, BigDecimal amount, String toUserId){
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        billAddRequestDto.setToUserId(toUserId);
        billAddRequestDto.setPayChannel(3);
        billAddRequestDto.setType(0);
        billAddRequestDto.setDescription(description);
        billAddRequestDto.setAmount(amount);
        wallerFrozenFuseAction.add(userId,billAddRequestDto);
    }

    private void computSubsidy(Merchant merchant, Merchant parentMerchant, int groupNum, Boolean type, Boolean flag) {
        Integer subsidy = type ? ((groupNum - 1) * 90 + 1600) : ((groupNum - 1) * 50 + 300);
        addRecording(merchant, parentMerchant, type ? 0 : 1, subsidy);
        if (flag) {
            subsidy += type ? (groupNum * 30 * 90) : 300;
            //添加流水
            addBill("999",(type ? "" : "间接") + "升组补贴", new BigDecimal(subsidy), parentMerchant.getUserId());
        }
        parentMerchant.setAchievementMonth(parentMerchant.getAchievementMonth() + subsidy);
        parentMerchant.setAchievementTotal(parentMerchant.getAchievementTotal() + subsidy);
    }

    public Boolean addRecording(Merchant merchant, Merchant parentMerchant, Integer type, Integer money){
        MerchantRecordingHistory recordingHistory = new MerchantRecordingHistory();
        long moneyOne = (long)money;
        recordingHistory.setMoney(moneyOne);
        recordingHistory.setMerchantId(merchant.getId());
        recordingHistory.setToMerchantId(parentMerchant.getId());
        recordingHistory.setType(type);
        merchantRecordingHistoryService.insert(recordingHistory);
        addBill("999","恭喜你获得由"+merchant.getName()+"提供给你的"+(type==0?"直":"间")+"接提成",new BigDecimal(money),parentMerchant.getUserId());
        return true;
    }
}
