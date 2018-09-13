package com.netx.ucenter.biz.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.vo.common.CostSettingResponseDto;
import com.netx.common.vo.common.WzCommonWalletFrozenResponseDto;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.common.vo.common.FrozenQueryRequestDto;
import com.netx.common.vo.currency.CurrencyHoldAddRequestDto;
import com.netx.common.vo.currency.CurrencyOperateRequestDto;
import com.netx.common.vo.currency.GetUserCurrencyAmountVo;
import com.netx.ucenter.model.common.*;
import com.netx.ucenter.model.user.Article;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.BillService;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.ucenter.service.common.DepositBillService;
import com.netx.ucenter.service.common.WalletFrozenService;
import com.netx.ucenter.service.user.ArticleService;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by wongloong on 17-9-17
 */
@Service
public class WalletFrozenAction{
    private Logger logger = LoggerFactory.getLogger(WalletFrozenAction.class);

    @Autowired
    WalletFrozenService walletFrozenService;
    @Autowired
    private BillService billService;
    @Autowired
    private WalletAction walletAction;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CostAction costAction;
    @Autowired
    private UserService userService;
    @Autowired
    private DepositBillService depositBillService;
    @Autowired
    private CommonServiceProvider commonServiceProvider;

    private Integer getAge(Date birthday){
        return ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public List<CommonWalletFrozen> selectPage(Integer current, Integer size, String userId, String typeId) throws Exception{
        Page<CommonWalletFrozen> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        return walletFrozenService.selectPage(page,userId,typeId);
    }

    public List<WzCommonWalletFrozenResponseDto> selectPageAndUserDate(FrozenQueryRequestDto requestDto) throws Exception{
        List<CommonWalletFrozen> result= selectPage(requestDto.getCurrent(),requestDto.getSize(), requestDto.getUserId(),requestDto.getTypeId());
        List<WzCommonWalletFrozenResponseDto> resultList= new ArrayList<>();
        if(result!=null){
            result.forEach(commonWalletFrozen -> {
                resultList.add(createWalletFrozenResponse(commonWalletFrozen));
            });
        }
        return resultList;
    }

    private WzCommonWalletFrozenResponseDto createWalletFrozenResponse(CommonWalletFrozen commonWalletFrozen){
        WzCommonWalletFrozenResponseDto wzCommonWalletFrozenResponseDto = VoPoConverter.copyProperties(commonWalletFrozen,WzCommonWalletFrozenResponseDto.class);
        User user=userService.selectById(commonWalletFrozen.getUserId());
        wzCommonWalletFrozenResponseDto.setLv(user.getLv());
        wzCommonWalletFrozenResponseDto.setAge(getAge(user.getBirthday()));
        wzCommonWalletFrozenResponseDto.setNickname(user.getNickname());
        wzCommonWalletFrozenResponseDto.setSex(user.getSex());
        wzCommonWalletFrozenResponseDto.setCredit(user.getCredit());
        wzCommonWalletFrozenResponseDto.setAmount(Money.CentToYuan(commonWalletFrozen.getAmount()).getAmount());
        return wzCommonWalletFrozenResponseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addFrozenAndBillByChange (CommonBill bill,CommonWalletFrozen frozen){
        CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(frozen.getUserId());
        long yu = wzCommonWallet.getTotalAmount()-frozen.getAmount();
        if ( yu <0) {
            throw new RuntimeException("钱包余额不足,请充值");
        }
        bill.setPayChannel(3);
        wzCommonWallet.setTotalAmount(yu);
        frozen.setBak3("0");
        bill.setTotalAmount(yu);
        return billService.insert(bill) && walletFrozenService.insert(frozen) && commonServiceProvider.getWalletService().updateById(wzCommonWallet);
    }

    @Transactional(rollbackFor = Exception.class)
    public CommonWallet getWzCommonWalletAndValidateAmount(CommonWalletFrozen frozen,GetUserCurrencyAmountVo currencyAmount){
        CommonWallet wzCommonWallet = getWzCommonWalletAndValidateAmount(frozen);
        BigDecimal totalAmount = currencyAmount.getCurrencyAmount().add(getMoney(wzCommonWallet.getTotalAmount()));
        //判断两个余额是否大于支付金额
        if (totalAmount.compareTo(getMoney(frozen.getAmount())) == -1) {
            throw new RuntimeException("账户余额不足");
        }
        return wzCommonWallet;
    }

    private Long getCent(BigDecimal money){
        return new Money(money).getCent();
    }

    private BigDecimal getMoney(Long cent){
        return new BigDecimal(Money.getMoneyString(cent));
    }

    @Transactional(rollbackFor = Exception.class)
    CommonWallet getWzCommonWalletAndValidateAmount(CommonWalletFrozen frozen) {
        CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(frozen.getUserId());
        return wzCommonWallet;
    }

    /**
     * 解除冻结
     * @param requestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean repeal(FrozenOperationRequestDto requestDto){
        List<CommonWalletFrozen> frozenList = walletFrozenService.getFrozenList(requestDto.getTypeId(),requestDto.getUserId(),0,requestDto.getType());
        if (null == frozenList || frozenList.isEmpty()) {
            logger.warn("未找到typeId为"+requestDto.getTypeId()+",userId为"+requestDto.getUserId()+"的冻结记录,操作失败");
            return true;
        }
        boolean result = false;
        for (CommonWalletFrozen frozen : frozenList) {
            CommonWallet wzCommonWallet =walletAction.queryWalletByUserId(requestDto.getUserId());
            frozen.setDeleted(1);
            CommonBill bill = new CommonBill();
            bill.setUserId(frozen.getUserId());
            bill.setDescription(frozen.getDescription() + "冻结金额返还" + Money.CentToYuan(frozen.getAmount()).getAmount()+"元");
            bill.setTradeType(1);
            bill.setAmount(frozen.getAmount());
            //解冻零钱
            if (frozen.getBak3().equals("0")) {
                bill.setPayChannel(3);
                wzCommonWallet.setTotalAmount(wzCommonWallet.getTotalAmount()+frozen.getAmount());
                result = billService.insert(bill);//添加退款交易流水
                walletFrozenService.updateById(frozen); //更新钱包解冻
                commonServiceProvider.getWalletService().updateById(wzCommonWallet);//更新用户钱包
                //解冻网币
            } else if (frozen.getBak3().equals("1")) {
                bill.setPayChannel(2);
                CurrencyOperateRequestDto currencyOperateRequestDto = new CurrencyOperateRequestDto();
                currencyOperateRequestDto.setUserId(frozen.getUserId());
                currencyOperateRequestDto.setCurrencyId(frozen.getBak2());
                currencyOperateRequestDto.setAmount(getMoney(frozen.getAmount()));
                //退回网币
                currencyOperateRequestDto.setType(3);
                //todo
                /*Result currencyOperate = currencyClient.currencyOperate(currencyOperateRequestDto);
                if (currencyOperate.getCode() != 0) {
                    logger.error("解冻退回用户#0网币id#1,数量#2异常", frozen.getUserId(), frozen.getBak2(), frozen.getAmount());
                    throw new Exception("解冻退回用户网币异常");
                }*/
                result = billService.insert(bill) && walletFrozenService.updateById(frozen);
            }
        }
        return result;
    }

    /**
     * 解除冻结
     * @param requestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean matchTicketRepeal(FrozenOperationRequestDto requestDto,String[] hostIds,String ownUserId){
        List<CommonWalletFrozen> frozenList = walletFrozenService.getFrozenList(requestDto.getTypeId(),requestDto.getUserId(),0,requestDto.getType());
        if (null == frozenList || frozenList.isEmpty()) {
            logger.warn("未找到typeId为"+requestDto.getTypeId()+",userId为"+requestDto.getUserId()+"的冻结记录,操作失败");
            return true;
        }
        boolean result = false;
        //主办方收取%9，平台收取%1，剩余90%退还
        for (CommonWalletFrozen frozen : frozenList) {
            frozen.setDeleted(1);
            //解冻零钱
            if (frozen.getBak3().equals("0")) {
                CommonWallet userWallet =walletAction.queryWalletByUserId(requestDto.getUserId());
                Double temp=frozen.getAmount()*0.9;
                Long userAmount=new Long(temp.longValue());
                CommonBill bill = new CommonBill();
                bill.setUserId(frozen.getUserId());
                //返还退票用户
                bill.setDescription(frozen.getDescription() + "冻结金额返还" + Money.CentToYuan(userAmount).getAmount()+"元，给用户");
                bill.setTradeType(1);
                bill.setAmount(userAmount);
                bill.setPayChannel(3);
                userWallet.setTotalAmount(userWallet.getTotalAmount()+userAmount);
                if(!billService.insert(bill)) {
                    //添加退款交易流水
                    throw new RuntimeException("添加交易流水失败。");
                }
                commonServiceProvider.getWalletService().updateById(userWallet);//更新用户钱包
                double hostMultiple=0.09/hostIds.length;
                Double temp2=frozen.getAmount()*hostMultiple;
                Long matchHostAmount=new Long(temp2.longValue());
                //返还主办的
                for (int i = 0; i < hostIds.length; i++) {
                    CommonWallet matchHostWallet =walletAction.queryWalletByUserId(hostIds[i]);
                    CommonBill hostBill = new CommonBill();
                    hostBill.setDescription(frozen.getDescription() + "冻结金额返还" + Money.CentToYuan(matchHostAmount).getAmount()+"元，给主办单位");
                    hostBill.setAmount(matchHostAmount);
                    hostBill.setTradeType(1);
                    hostBill.setUserId(hostIds[i]);
                    hostBill.setPayChannel(3);
                    matchHostWallet.setTotalAmount(userWallet.getTotalAmount()+matchHostAmount);
                    if(!billService.insert(hostBill)) {
                        //添加退款交易流水
                        throw new RuntimeException("添加交易流水失败。");
                    }
                    commonServiceProvider.getWalletService().updateById(matchHostWallet);//更新用户钱包
                }
                /**
                 * 给平台
                 */
                Double temp3=frozen.getAmount()*0.01;
                Long matchOwnAmount=new Long(temp3.longValue());
                CommonWallet matchOwnWallet =walletAction.queryWalletByUserId(ownUserId);
                CommonBill ownBill = new CommonBill();
                ownBill.setDescription(frozen.getDescription() + "冻结金额返还" + Money.CentToYuan(matchOwnAmount).getAmount()+"元，给主平台");
                ownBill.setAmount(matchOwnAmount);
                ownBill.setTradeType(1);
                ownBill.setUserId(ownUserId);
                ownBill.setPayChannel(3);
                matchOwnWallet.setTotalAmount(userWallet.getTotalAmount()+matchOwnAmount);
                result = billService.insert(ownBill);
                commonServiceProvider.getWalletService().updateById(matchOwnWallet);//更新用户钱包
                walletFrozenService.updateById(frozen); //更新钱包解冻
            }
        }
        return result;
    }


    /**
     * 转账
     * @param requestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean pay(FrozenOperationRequestDto requestDto){
        List<CommonWalletFrozen> frozenList = walletFrozenService.getFrozenList(requestDto.getTypeId(),requestDto.getUserId(),0,requestDto.getType());
        if (null == frozenList || frozenList.isEmpty()) {
            logger.warn("未找到typeId为"+requestDto.getTypeId()+",userId为"+requestDto.getUserId()+"的冻结记录,操作失败");
            return true;
        }
        boolean result = false;

        for (CommonWalletFrozen frozen : frozenList) {
            CommonBill bill = createCommonBill(frozen.getToUserId(),frozen.getDescription(),frozen.getAmount());
            frozen.setHasConsume(1);
            //使用零钱支付
            if (frozen.getBak3().equals("0")) {
                CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(frozen.getToUserId());
                //如果是打到公司账户并且公司账户没有创建钱包,此处初始一个公司钱包
                wzCommonWallet.setTotalAmount(wzCommonWallet.getTotalAmount()+frozen.getAmount());
                bill.setPayChannel(3);
                result = billService.insert(bill) && walletFrozenService.updateById(frozen) && commonServiceProvider.getWalletService().updateById(wzCommonWallet);
                //使用网币支付
            } else if (frozen.getBak3().equals("1")) {
                CurrencyOperateRequestDto currencyOperateRequestDto = new CurrencyOperateRequestDto();
                currencyOperateRequestDto.setUserId(frozen.getUserId());
                currencyOperateRequestDto.setCurrencyId(frozen.getBak2());
                currencyOperateRequestDto.setAmount(getMoney(frozen.getAmount()));
                //退回网币
                currencyOperateRequestDto.setType(1);
                //todo
                /*Result currencyOperate = currencyClient.currencyOperate(currencyOperateRequestDto);
                if (currencyOperate.getCode() != 0) {
                    logger.error("消费冻结金额,入账用户#0网币id#1,数量#2异常", frozen.getUserId(), frozen.getBak2(), frozen.getAmount());
                    throw new Exception("消费冻结金额入账网币异常");
                }*/
                result = billService.insert(bill) && walletFrozenService.updateById(frozen);
            }
        }
        return result;
    }

    private CommonBill createCommonBill(String userId,String description,Long amount){
        CommonBill bill = new CommonBill();
        bill.setUserId(userId);
        bill.setDescription(description);
        bill.setTradeType(1);
        bill.setAmount(amount);
        return bill;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean cashDeposit(String typeId, String toUserId){

        CostSettingResponseDto wzCommonCostSetting = costAction.query();
        //logger.info("押金标准："+wzCommonCostSetting.getPicAndVoicePublishDeposit());
        BigDecimal amount=new BigDecimal(0);
        if(wzCommonCostSetting!=null){
            amount=wzCommonCostSetting.getClickFee();
        }
        //添加押金（或续费）到咨讯表
        Article article = articleService.selectById(typeId);
        //若查询的咨讯为空，则支付失败
        if(article == null) {
            throw new RuntimeException("未找到typeId为"+typeId+"的资讯");
        }
        if(getCent(amount)-article.getAmount()<0){
            logger.warn(typeId+"押金余额不足，操作失败");
            throw new RuntimeException("押金余额不足");
        }

        //资讯消费金额类型 0：零钱，1：网币
        Integer payType = article.getAmountType();

        if(payType==null){
            throw new RuntimeException("数据异常：资讯金额类型不能为空");
        }

        if (payType==1) {
            CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(toUserId);
            wzCommonWallet.setTotalAmount(wzCommonWallet.getTotalAmount()+getCent(amount));
            commonServiceProvider.getWalletService().updateById(wzCommonWallet);
            //网币支付
        } else if (payType==2) {
            CurrencyHoldAddRequestDto currencyHoldAddRequestDto=new CurrencyHoldAddRequestDto();
            currencyHoldAddRequestDto.setAmount(amount);
            currencyHoldAddRequestDto.setUserId(toUserId);
            //todo
            /*Result result = currencyHoldClient.addHold(currencyHoldAddRequestDto);
            if (result.getCode() != 0) {
                logger.error("新增用户#0网币,数量#1",toUserId,amount);
                throw new Exception("添加用户持有网币异常："+result.getMessage());
            }*/
        }else{
            throw new RuntimeException("扣取资讯点击费用异常");
        }

        //更新资讯押金金额
        article.setId(typeId);
        article.setAmount(article.getAmount()-getCent(amount));

        //创建押金流水
        CommonDepositBill bill = new CommonDepositBill();
        bill.setUserId(toUserId);
        bill.setDescription("扣取押金"+amount.toString());
        bill.setTradeType(1);
        bill.setAmount(getCent(amount));
        bill.setCreateTime(new Date());
        bill.setUpdateTime(new Date());
        Boolean result = depositBillService.insert(bill) && articleService.updateById(article);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean repealDeposit(FrozenOperationRequestDto requestDto){
        Article article = articleService.getArticleByUserId(requestDto.getTypeId(),requestDto.getUserId());
        //若查询的咨讯为空，则支付失败
        if(article == null) {
            throw new RuntimeException("未找到typeId为"+requestDto.getTypeId()+"的资讯");
        }
        if(article.getAmount()==0){
            logger.warn("押金余额为零，操作失败",requestDto.getTypeId() );
            throw new RuntimeException("押金余额为零，操作失败");
        }
        //资讯消费金额类型 0：零钱，1：网币
        Integer payType = article.getAmountType();
        Long amount = article.getAmount();

        CommonBill bill = new CommonBill();

        boolean flag=false;
        if (payType==1) {
            CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(requestDto.getUserId());
            wzCommonWallet.setTotalAmount(wzCommonWallet.getTotalAmount()+amount);
            flag =commonServiceProvider.getWalletService().updateById(wzCommonWallet);
            bill.setPayChannel(3);
            //网币支付
        } else if (payType==2) {
            CurrencyHoldAddRequestDto currencyHoldAddRequestDto=new CurrencyHoldAddRequestDto();
            currencyHoldAddRequestDto.setAmount(getMoney(amount));
            currencyHoldAddRequestDto.setUserId(requestDto.getUserId());
            //todo
            /*Result result = currencyHoldClient.addHold(currencyHoldAddRequestDto);
            if (result.getCode() != 0) {
                logger.error("新增用户#0网币,数量#1",requestDto.getUserId(),amount);
                throw new Exception("添加用户持有网币异常"+result.getMessage());
            }*/
            flag = true;
            bill.setPayChannel(2);
        }else{
            return true;
        }
        //更新资讯押金金额
        Article al=new Article();
        al.setId(requestDto.getTypeId());
        al.setAmount(0l);
        bill.setUserId(requestDto.getUserId());
        bill.setDescription("撤销押金返还" + new BigDecimal(Money.getMoneyString(amount)));
        bill.setTradeType(1);
        bill.setAmount(amount);
        boolean result = billService.insert(bill) &&articleService.updateById(al) & flag;
        return result;
    }
}
